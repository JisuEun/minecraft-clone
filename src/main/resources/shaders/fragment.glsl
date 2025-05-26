#version 330 core

in vec2 vTexCoord;
out vec4 fragColor;

uniform sampler2D texture1;

void main() {
    fragColor = texture(texture1, vTexCoord);
}