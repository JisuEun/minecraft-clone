#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;

out vec2 vTexCoord;

uniform mat4 mvp;

void main() {
    gl_Position = mvp * vec4(position, 1.0);
    vTexCoord = texCoord;
}