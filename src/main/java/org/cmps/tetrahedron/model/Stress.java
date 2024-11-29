package org.cmps.tetrahedron.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Stress {

    private List<Float> stress;
    private float minStress = Float.MAX_VALUE;
    private float maxStress = Float.MIN_VALUE;

    private List<float[]> colors;
}
