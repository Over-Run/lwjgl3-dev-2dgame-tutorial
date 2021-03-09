#version 110

attribute vec2 vert;
attribute vec3 in_color;
attribute vec2 in_texCoord;
varying vec3 out_color;
varying vec2 out_texCoord;
uniform mat4 orthoMatrix;

void main()
{
    gl_Position = orthoMatrix * vec4(vert, 0, 1);
    out_color = in_color;
    out_texCoord = in_texCoord;
}