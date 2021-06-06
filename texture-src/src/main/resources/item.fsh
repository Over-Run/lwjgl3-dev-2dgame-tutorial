#version 110

varying vec3 out_color;
varying vec2 out_texCoord;
uniform sampler2D textureSampler;

void main() {
    gl_FragColor = texture2D(textureSampler, out_texCoord) * vec4(out_color, 1.0);
}