#version 300 es
precision mediump float;

const vec3 edgeColor = vec3(0.918, 0.956, 1.0);

uniform mat4 viewMatrix;
uniform mat4 projMatrix;
uniform mat4 modelMatrix;

uniform int showElementMesh;
uniform int showLight;
uniform int coloredInSelectedColor;
uniform vec3 modelColor;

in vec3 fragmentColor;
in vec3 barycentricCoords;
in vec3 triangleNormal;

out vec4 color;

vec3 calculateLighting(vec3 color) {

    vec3 lightDirection = normalize(vec3(0.0, 0.5, -1));

    vec3 normal = normalize((modelMatrix * vec4(triangleNormal, 0.0)).xyz);
    vec3 viewLightDirection = normalize((viewMatrix * vec4(lightDirection, 0.0)).xyz);

    float diff = abs(dot(normal, viewLightDirection));
    vec3 finalColor = diff * color;

    return finalColor;
}

void main(void) {
  vec3 pixelColor;

  // define face color (default one or provided in vertex info)
  if (coloredInSelectedColor == 0) {
    pixelColor = fragmentColor;
  } else {
    pixelColor = modelColor;
  }

  // show edges if enabled
  if (showElementMesh == 1) {
    vec3 d = fwidth(barycentricCoords);
    vec3 f = smoothstep(vec3(0.0), d, barycentricCoords);
    float factor = min(min(f.x, f.y), f.z);

    pixelColor = mix(edgeColor, pixelColor, factor);
  }

  // show light if enabled
  if (showLight == 1) {
    pixelColor = calculateLighting(pixelColor);
  }

  color = vec4(pixelColor, 1.0);
}
