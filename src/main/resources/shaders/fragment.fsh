#version 400 core

in vec2 fragTextureCoord;

out vec4 fragColour;

uniform sampler2D textureSampler;

void main() {
    fragColour = texture(textureSampler, fragTextureCoord);
}
