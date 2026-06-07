package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.model.TetraModelApi;
import org.gmsh.Gmsh;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.HashMap;
import java.util.Map;

import static java.lang.foreign.ValueLayout.*;

public class StlToTetraMesh {

    public static TetraModelApi generateMesh(String inputStl) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment ierr = arena.allocate(JAVA_INT);

            // 1. Initialize
            System.out.println("Step 1: gmshInitialize");
            MemorySegment arg0 = arena.allocateFrom("gmsh");
            MemorySegment arg1 = arena.allocateFrom("-no_signal_handler");
            MemorySegment argv = arena.allocate(ADDRESS, 3);
            argv.setAtIndex(ADDRESS, 0, arg0);
            argv.setAtIndex(ADDRESS, 1, arg1);
            argv.setAtIndex(ADDRESS, 2, MemorySegment.NULL);
            Gmsh.gmshInitialize(2, argv, 0, 0, ierr);
            if (ierr.get(JAVA_INT, 0) != 0) {
                System.err.println("gmshInitialize failed");
                throw new RuntimeException("gmshInitialize failed");
            }

            System.out.println("Step 2: gmshModelAdd");
            Gmsh.gmshModelAdd(arena.allocateFrom("STL_Converter"), ierr);

            // 1. Load the STL file
            System.out.println("Step 3: gmshMerge");
            Gmsh.gmshMerge(arena.allocateFrom(inputStl), ierr);
            if (ierr.get(JAVA_INT, 0) != 0) {
                System.err.println("Error loading STL");
                Gmsh.gmshFinalize(ierr);
                throw new RuntimeException("Error loading STL");
            }

            // 2. Classify Surfaces & Create Geometry
            System.out.println("Step 4: classifySurfaces");
            double angle = 40.0 * Math.PI / 180.0;
            Gmsh.gmshModelMeshClassifySurfaces(angle, 1, 1, Math.PI, 1, ierr);
            System.out.println("Step 5: createGeometry");
            Gmsh.gmshModelMeshCreateGeometry(MemorySegment.NULL, 0, ierr);

            // 3. Create Volume from Surfaces
            System.out.println("Step 6: getEntities");
            MemorySegment dimTagsPtr = arena.allocate(ADDRESS);
            MemorySegment dimTags_n = arena.allocate(JAVA_LONG);
            Gmsh.gmshModelGetEntities(dimTagsPtr, dimTags_n, 2, ierr);

            long dimTagsLen = dimTags_n.get(JAVA_LONG, 0);
            long numEntities = dimTagsLen / 2; // dimTags_n is the length of the integer array
            System.out.println("Entities found: " + numEntities + ", dimTags_n: " + dimTagsLen);

            MemorySegment dimTags = dimTagsPtr.get(ADDRESS, 0).reinterpret(dimTagsLen * 4);

            MemorySegment surfaceTags = arena.allocate(JAVA_INT, numEntities);
            for (long i = 0; i < numEntities; i++) {
                int dim = dimTags.getAtIndex(JAVA_INT, i * 2);
                int tag = dimTags.getAtIndex(JAVA_INT, i * 2 + 1);
                surfaceTags.setAtIndex(JAVA_INT, i, tag);
            }

            System.out.println("Step 7: addSurfaceLoop");
            int loopTag = Gmsh.gmshModelGeoAddSurfaceLoop(surfaceTags, numEntities, -1, ierr);

            System.out.println("Step 8: addVolume");
            MemorySegment loopTags = arena.allocateFrom(JAVA_INT, loopTag);
            Gmsh.gmshModelGeoAddVolume(loopTags, 1, -1, ierr);

            System.out.println("Step 9: synchronize");
            Gmsh.gmshModelGeoSynchronize(ierr);

            System.out.println("Step 10: free dimTags");
            Gmsh.gmshFree(dimTagsPtr.get(ADDRESS, 0));

            // 4. Mesh Settings
            System.out.println("Step 11: options");
            // Use HXT (Algorithm 10) for high-quality tetrahedral meshing
            Gmsh.gmshOptionSetNumber(arena.allocateFrom("Mesh.Algorithm3D"), 10.0, ierr);
            Gmsh.gmshOptionSetNumber(arena.allocateFrom("Mesh.MeshSizeMin"), 1.0, ierr);
            Gmsh.gmshOptionSetNumber(arena.allocateFrom("Mesh.MeshSizeMax"), 5.0, ierr);

            // 5. Generate 3D Mesh
            System.out.println("Step 12: meshGenerate");
            Gmsh.gmshModelMeshGenerate(3, ierr);

            System.out.println("Step 13: meshOptimize");
            Gmsh.gmshModelMeshOptimize(arena.allocateFrom("Netgen"), 0, 1, MemorySegment.NULL, 0, ierr);

            // 6. EXPORT NODES
            System.out.println("Step 14: getNodes");
            MemorySegment nodeTagsPtr = arena.allocate(ADDRESS);
            MemorySegment nodeTags_n = arena.allocate(JAVA_LONG);
            MemorySegment coordsPtr = arena.allocate(ADDRESS);
            MemorySegment coords_n = arena.allocate(JAVA_LONG);
            MemorySegment parametricCoordPtr = arena.allocate(ADDRESS);
            MemorySegment parametricCoord_n = arena.allocate(JAVA_LONG);

            Gmsh.gmshModelMeshGetNodes(nodeTagsPtr, nodeTags_n, coordsPtr, coords_n, parametricCoordPtr,
                                       parametricCoord_n, -1, -1, 0, 0, ierr);

            long numNodes = nodeTags_n.get(JAVA_LONG, 0);
            MemorySegment nodeTagsArray = nodeTagsPtr.get(ADDRESS, 0).reinterpret(numNodes * 8);
            MemorySegment coordsArray = coordsPtr.get(ADDRESS, 0).reinterpret(numNodes * 3 * 8); // 3 doubles per node

            System.out.println("Step 15: write nodes");
            TetraModelApi.TetraModelApiBuilder modelApi = TetraModelApi.builder();
            Map<Integer, float[]> coordinates = new HashMap<>();
            modelApi.coordinates(coordinates);

            for (long i = 0; i < numNodes; i++) {
                int tag = Math.toIntExact(nodeTagsArray.getAtIndex(JAVA_LONG, i));
                float x = (float) coordsArray.getAtIndex(JAVA_DOUBLE, i * 3);
                float y = (float) coordsArray.getAtIndex(JAVA_DOUBLE, i * 3 + 1);
                float z = (float) coordsArray.getAtIndex(JAVA_DOUBLE, i * 3 + 2);
                coordinates.put(tag, new float[]{x, y, z});
            }

            System.out.println("Step 16: free nodes");
            Gmsh.gmshFree(nodeTagsPtr.get(ADDRESS, 0));
            Gmsh.gmshFree(coordsPtr.get(ADDRESS, 0));
            Gmsh.gmshFree(parametricCoordPtr.get(ADDRESS, 0));

            // 7. EXPORT TETRAHEDRONS
            System.out.println("Step 17: getElements");
            MemorySegment elementTypePtr = arena.allocate(ADDRESS);
            MemorySegment elementType_n = arena.allocate(JAVA_LONG);
            MemorySegment elementTagsPtr = arena.allocate(ADDRESS);
            MemorySegment elementTags_n = arena.allocate(ADDRESS);
            MemorySegment elementTags_nn = arena.allocate(JAVA_LONG);
            MemorySegment elementNodeTagsPtr = arena.allocate(ADDRESS);
            MemorySegment elementNodeTags_n = arena.allocate(ADDRESS);
            MemorySegment elementNodeTags_nn = arena.allocate(JAVA_LONG);

            Gmsh.gmshModelMeshGetElements(elementTypePtr, elementType_n,
                                          elementTagsPtr, elementTags_n, elementTags_nn,
                                          elementNodeTagsPtr, elementNodeTags_n, elementNodeTags_nn,
                                          3, -1, ierr);

            long numTypes = elementType_n.get(JAVA_LONG, 0);
            MemorySegment elemTypesArray = elementTypePtr.get(ADDRESS, 0).reinterpret(numTypes * 4);
            MemorySegment elemTagsPtrArray = elementTagsPtr.get(ADDRESS, 0).reinterpret(numTypes * 8);
            MemorySegment elemTags_nArray = elementTags_n.get(ADDRESS, 0).reinterpret(numTypes * 8);
            MemorySegment nodeTagsPerElemPtrArray = elementNodeTagsPtr.get(ADDRESS, 0).reinterpret(numTypes * 8);

            long totalElems = 0;

            System.out.println("Step 18: write elements");
            for (long i = 0; i < numTypes; i++) {
                int eType = elemTypesArray.getAtIndex(JAVA_INT, i);
                if (eType == 4) { // Tetrahedron
                    int nElems = Math.toIntExact(elemTags_nArray.getAtIndex(JAVA_LONG, i));
                    totalElems += nElems;

                    int[][] indices = new int[nElems][4];
                    System.out.println("Collecting indices");
                    modelApi.indices(indices);

                    MemorySegment tags = elemTagsPtrArray.getAtIndex(ADDRESS, i).reinterpret(nElems * 8);
                    MemorySegment conns = nodeTagsPerElemPtrArray.getAtIndex(ADDRESS, i)
                                                                 .reinterpret(nElems * 4 * 8); // 4 nodes per tetra

                    for (int j = 0; j < nElems; j++) {
                        long eTag = tags.getAtIndex(JAVA_LONG, j);
                        long n1 = conns.getAtIndex(JAVA_LONG, j * 4L);
                        long n2 = conns.getAtIndex(JAVA_LONG, j * 4L + 1);
                        long n3 = conns.getAtIndex(JAVA_LONG, j * 4L + 2);
                        long n4 = conns.getAtIndex(JAVA_LONG, j * 4L + 3);
                        indices[j][0] = Math.toIntExact(n1);
                        indices[j][1] = Math.toIntExact(n2);
                        indices[j][2] = Math.toIntExact(n3);
                        indices[j][3] = Math.toIntExact(n4);
                    }
                }
            }

            System.out.println("Step 19: free elements");
            // Free allocated arrays for elements
            for (long i = 0; i < numTypes; i++) {
                Gmsh.gmshFree(elemTagsPtrArray.getAtIndex(ADDRESS, i));
                Gmsh.gmshFree(nodeTagsPerElemPtrArray.getAtIndex(ADDRESS, i));
            }
            Gmsh.gmshFree(elementTypePtr.get(ADDRESS, 0));
            Gmsh.gmshFree(elementTagsPtr.get(ADDRESS, 0));
            Gmsh.gmshFree(elementTags_n.get(ADDRESS, 0));
            Gmsh.gmshFree(elementNodeTagsPtr.get(ADDRESS, 0));
            Gmsh.gmshFree(elementNodeTags_n.get(ADDRESS, 0));
            System.out.printf("Done! Nodes: %d, Elements: %d\n", numNodes, totalElems);

            System.out.println("Step 20: finalize");
            Gmsh.gmshFinalize(ierr);
            System.out.println("Finished successfully!");

            return modelApi.build();
        }
    }

    public static TetraModelApi extractStlData(String inputStl) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment ierr = arena.allocate(JAVA_INT);

            // 1. Initialize
            System.out.println("Step 1: gmshInitialize");
            MemorySegment arg0 = arena.allocateFrom("gmsh");
            MemorySegment arg1 = arena.allocateFrom("-no_signal_handler");
            MemorySegment argv = arena.allocate(ADDRESS, 3);
            argv.setAtIndex(ADDRESS, 0, arg0);
            argv.setAtIndex(ADDRESS, 1, arg1);
            argv.setAtIndex(ADDRESS, 2, MemorySegment.NULL);
            Gmsh.gmshInitialize(2, argv, 0, 0, ierr);
            if (ierr.get(JAVA_INT, 0) != 0) {
                System.err.println("gmshInitialize failed");
                throw new RuntimeException("gmshInitialize failed");
            }

            // 2. Load the STL file
            System.out.println("Step 2: gmshMerge");
            Gmsh.gmshMerge(arena.allocateFrom(inputStl), ierr);
            if (ierr.get(JAVA_INT, 0) != 0) {
                System.err.println("Error loading STL");
                Gmsh.gmshFinalize(ierr);
                throw new RuntimeException("Error loading STL");
            }

            // --- SKIPPED: Geometry creation, volume classification, and mesh generation ---
            // An STL already consists of 2D surface triangles. We can read them directly.

            // 3. EXPORT NODES (Vertices)
            System.out.println("Step 3: getNodes");
            MemorySegment nodeTagsPtr = arena.allocate(ADDRESS);
            MemorySegment nodeTags_n = arena.allocate(JAVA_LONG);
            MemorySegment coordsPtr = arena.allocate(ADDRESS);
            MemorySegment coords_n = arena.allocate(JAVA_LONG);
            MemorySegment parametricCoordPtr = arena.allocate(ADDRESS);
            MemorySegment parametricCoord_n = arena.allocate(JAVA_LONG);

            // Fetch nodes for all entities (dim = -1)
            Gmsh.gmshModelMeshGetNodes(nodeTagsPtr, nodeTags_n, coordsPtr, coords_n, parametricCoordPtr,
                                       parametricCoord_n, -1, -1, 0, 0, ierr);

            long numNodes = nodeTags_n.get(JAVA_LONG, 0);
            MemorySegment nodeTagsArray = nodeTagsPtr.get(ADDRESS, 0).reinterpret(numNodes * 8);
            MemorySegment coordsArray = coordsPtr.get(ADDRESS, 0).reinterpret(numNodes * 3 * 8); // 3 doubles per node

            System.out.println("Step 4: write nodes");
            TetraModelApi.TetraModelApiBuilder modelApi = TetraModelApi.builder();
            Map<Integer, float[]> coordinates = new HashMap<>();
            modelApi.coordinates(coordinates);

            for (long i = 0; i < numNodes; i++) {
                int tag = Math.toIntExact(nodeTagsArray.getAtIndex(JAVA_LONG, i));
                float x = (float) coordsArray.getAtIndex(JAVA_DOUBLE, i * 3);
                float y = (float) coordsArray.getAtIndex(JAVA_DOUBLE, i * 3 + 1);
                float z = (float) coordsArray.getAtIndex(JAVA_DOUBLE, i * 3 + 2);
                coordinates.put(tag, new float[]{x, y, z});
            }

            System.out.println("Step 5: free nodes");
            Gmsh.gmshFree(nodeTagsPtr.get(ADDRESS, 0));
            Gmsh.gmshFree(coordsPtr.get(ADDRESS, 0));
            Gmsh.gmshFree(parametricCoordPtr.get(ADDRESS, 0));

            // 4. EXPORT TRIANGLES (Indices)
            System.out.println("Step 6: getElements");
            MemorySegment elementTypePtr = arena.allocate(ADDRESS);
            MemorySegment elementType_n = arena.allocate(JAVA_LONG);
            MemorySegment elementTagsPtr = arena.allocate(ADDRESS);
            MemorySegment elementTags_n = arena.allocate(ADDRESS);
            MemorySegment elementTags_nn = arena.allocate(JAVA_LONG);
            MemorySegment elementNodeTagsPtr = arena.allocate(ADDRESS);
            MemorySegment elementNodeTags_n = arena.allocate(ADDRESS);
            MemorySegment elementNodeTags_nn = arena.allocate(JAVA_LONG);

            // Retrieve 2D elements (triangles) from the loaded STL (dim = 2)
            Gmsh.gmshModelMeshGetElements(elementTypePtr, elementType_n,
                                          elementTagsPtr, elementTags_n, elementTags_nn,
                                          elementNodeTagsPtr, elementNodeTags_n, elementNodeTags_nn,
                                          2, -1, ierr);

            long numTypes = elementType_n.get(JAVA_LONG, 0);
            MemorySegment elemTypesArray = elementTypePtr.get(ADDRESS, 0).reinterpret(numTypes * 4);
            MemorySegment elemTagsPtrArray = elementTagsPtr.get(ADDRESS, 0).reinterpret(numTypes * 8);
            MemorySegment elemTags_nArray = elementTags_n.get(ADDRESS, 0).reinterpret(numTypes * 8);
            MemorySegment nodeTagsPerElemPtrArray = elementNodeTagsPtr.get(ADDRESS, 0).reinterpret(numTypes * 8);

            long totalElems = 0;

            System.out.println("Step 7: write elements");
            for (long i = 0; i < numTypes; i++) {
                int eType = elemTypesArray.getAtIndex(JAVA_INT, i);

                // eType 2 corresponds to a 3-node Triangle in Gmsh
                if (eType == 2) {
                    int nElems = Math.toIntExact(elemTags_nArray.getAtIndex(JAVA_LONG, i));
                    totalElems += nElems;

                    // STL consists of triangles, so we need arrays of size 3, not 4
                    int[][] indices = new int[nElems][3];
                    System.out.println("Collecting indices");
                    modelApi.indices(indices);

                    MemorySegment tags = elemTagsPtrArray.getAtIndex(ADDRESS, i).reinterpret(nElems * 8);
                    MemorySegment conns = nodeTagsPerElemPtrArray.getAtIndex(ADDRESS, i)
                                                                 .reinterpret(nElems * 3 * 8); // 3 nodes per triangle

                    for (int j = 0; j < nElems; j++) {
                        long n1 = conns.getAtIndex(JAVA_LONG, j * 3L);
                        long n2 = conns.getAtIndex(JAVA_LONG, j * 3L + 1);
                        long n3 = conns.getAtIndex(JAVA_LONG, j * 3L + 2);

                        indices[j][0] = Math.toIntExact(n1);
                        indices[j][1] = Math.toIntExact(n2);
                        indices[j][2] = Math.toIntExact(n3);
                    }
                }
            }

            System.out.println("Step 8: free elements");
            // Free allocated arrays for elements
            for (long i = 0; i < numTypes; i++) {
                Gmsh.gmshFree(elemTagsPtrArray.getAtIndex(ADDRESS, i));
                Gmsh.gmshFree(nodeTagsPerElemPtrArray.getAtIndex(ADDRESS, i));
            }
            Gmsh.gmshFree(elementTypePtr.get(ADDRESS, 0));
            Gmsh.gmshFree(elementTagsPtr.get(ADDRESS, 0));
            Gmsh.gmshFree(elementTags_n.get(ADDRESS, 0));
            Gmsh.gmshFree(elementNodeTagsPtr.get(ADDRESS, 0));
            Gmsh.gmshFree(elementNodeTags_n.get(ADDRESS, 0));
            System.out.printf("Done! Nodes: %d, Elements (Triangles): %d\n", numNodes, totalElems);

            System.out.println("Step 9: finalize");
            Gmsh.gmshFinalize(ierr);
            System.out.println("Finished successfully!");

            return modelApi.build();
        }
    }
}
