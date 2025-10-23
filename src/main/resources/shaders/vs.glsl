#version 300 es
precision highp float;

uniform mat4 viewMatrix;
uniform mat4 projMatrix;
uniform mat4 modelMatrix;

in vec3 color;
in vec3 normal;
in vec3 position;

out vec3 fragmentColor;
out vec3 barycentricCoords;
out vec3 triangleNormal;
out float depth;

void main(void) {
  gl_Position = projMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
  fragmentColor = color;
  triangleNormal = normal;

  if (gl_VertexID % 3 == 0) {
    barycentricCoords = vec3(1.0, 0.0, 0.0); // Vertex 0
  } else if (gl_VertexID % 3 == 1) {
    barycentricCoords = vec3(0.0, 1.0, 0.0); // Vertex 1
  } else {
    barycentricCoords = vec3(0.0, 0.0, 1.0); // Vertex 2
  }

  float ndcZ = gl_Position.z / gl_Position.w;
  depth = ndcZ * 0.5 + 0.5;
}
