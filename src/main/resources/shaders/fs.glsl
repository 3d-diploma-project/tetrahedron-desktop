#version 150

const vec3 edgeColor = vec3(0.918, 0.956, 1.0);

uniform mat4 viewMatrix;
uniform mat4 projMatrix;
uniform mat4 modelMatrix;

uniform int coloredInSelectedColor;
uniform vec3 modelColor;

uniform int showElementMesh;
uniform int showLight;

in VertexData {
  noperspective vec3 distance;
  vec3 fragmentColor;
  vec3 normal;
} vVertexIn;

out vec4 color;

vec3 calculateLighting(vec3 color) {

    vec3 lightDirection = normalize(vec3(0.0, 0.5, -1));

    vec3 normal = normalize((modelMatrix * vec4(vVertexIn.normal, 0.0)).xyz);
    vec3 viewLightDirection = normalize((viewMatrix * vec4(lightDirection, 0.0)).xyz);

    float diff = abs(dot(normal, viewLightDirection));
    vec3 finalColor = diff * color;

    return finalColor;
}

void main(void) {

  vec3 pixelColor;

  // define face color (default one or provided in vertex info)
  if (coloredInSelectedColor == 0) {
    pixelColor = vVertexIn.fragmentColor;
  } else {
    pixelColor = modelColor;
  }

  // show edges if enabled
  if (showElementMesh == 1) {
    // determine frag distance to closest edge
    float fNearest = min(min(vVertexIn.distance[0], vVertexIn.distance[1]), vVertexIn.distance[2]);
    float fEdgeIntensity = clamp(exp2(-0.8 * fNearest * fNearest), 0.0, 1.0);
    // blend between edge color and face color
    pixelColor = mix(pixelColor, edgeColor, fEdgeIntensity);
  }

  // show light if enabled
  if (showLight == 1) {
    pixelColor = calculateLighting(pixelColor);
  }

  color = vec4(pixelColor, 1.0);
}
