#version 110

attribute vec2 vert;
attribute vec3 in_color;
varying vec3 out_color;
uniform mat4 orthoMatrix;

void main()
{
    gl_Position = orthoMatrix * vec4(vert, 0, 1);
    out_color = in_color;
}