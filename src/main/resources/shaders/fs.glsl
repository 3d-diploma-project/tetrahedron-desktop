#version 150

const vec3 edgeColor = vec3(0.918, 0.956, 1.0);

uniform int coloredInSelectedColor;
uniform vec3 modelColor;

in VertexData {
  noperspective vec3 distance;
  vec3 fragmentColor;
} vVertexIn;

out vec4 color;

void main(void) {
  // determine frag distance to closest edge
  float fNearest = min(min(vVertexIn.distance[0], vVertexIn.distance[1]), vVertexIn.distance[2]);
  float fEdgeIntensity = clamp(exp2(-0.8 * fNearest * fNearest), 0.0, 1.0);

  // blend between edge color and face color
  if (coloredInSelectedColor == 0) {
    color = vec4(mix(vVertexIn.fragmentColor, edgeColor, fEdgeIntensity), 1.0);
  } else {
    color = vec4(mix(modelColor, edgeColor, fEdgeIntensity), 1.0);
  }
}
