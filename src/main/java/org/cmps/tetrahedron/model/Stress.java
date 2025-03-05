package org.cmps.tetrahedron.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Stress {

    private Map<Integer, float[]> stress;
    private List<Float> misesStress;
    @Builder.Default
    private float minStress = Float.MAX_VALUE;
    @Builder.Default
    private float maxStress = Float.MIN_VALUE;

    private float qx;
    private float txy;
    private float tzx;
    private float qy;
    private float tyz;
    private float qz;

    private List<float[]> colors;
}
