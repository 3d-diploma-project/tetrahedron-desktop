#version 150

uniform mat4 viewMatrix;
uniform mat4 projMatrix;
uniform mat4 modelMatrix;

in vec3 color;
in vec3 normal;

in vec3 position;

out Vertex {
  vec3 fragmentColor;
  vec3 normal;
} vertex;

void main(void) {
  gl_Position = projMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
  vertex.fragmentColor = color;
  vertex.normal = normal;
}
