package org.cmps.tetrahedron.model;

import lombok.*;
import org.cmps.tetrahedron.utils.DataReader;

import java.io.File;
import java.util.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Model {

    private Map<Integer, float[]> vertices;
    private List<float[][]> faces;
}
