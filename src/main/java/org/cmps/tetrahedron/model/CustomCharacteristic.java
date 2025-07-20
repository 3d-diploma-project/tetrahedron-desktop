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
public class CustomCharacteristic {
    private List<Float> values;
    @Builder.Default
    private float minValue = Float.MAX_VALUE;
    @Builder.Default
    private float maxValue = Float.MIN_VALUE;
}
