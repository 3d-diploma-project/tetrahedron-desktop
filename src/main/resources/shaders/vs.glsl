#version 150

uniform mat4 viewMatrix;
uniform mat4 projMatrix;

in vec3 color;

in vec3 position;

out Vertex {
  vec3 fragmentColor;
} vertex;

void main(void) {
  gl_Position = projMatrix * viewMatrix * vec4(position, 1.0);
  vertex.fragmentColor = color;
}
