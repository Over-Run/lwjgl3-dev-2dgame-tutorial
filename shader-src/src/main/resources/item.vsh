#version 110

attribute vec2 vert;
attribute vec3 in_color;
varying vec3 out_color;
// 你可以看到我们添加了一个矩阵。
uniform mat4 orthoMatrix;

void main() {
    // 该矩阵将对vert进行乘法操作。
    gl_Position = orthoMatrix * vec4(vert, 0.0, 1.0);
    out_color = in_color;
}