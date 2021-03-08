package io.github.overrun.ldgt.client.gl;

import java.io.Closeable;

import static org.lwjgl.opengl.GL20.*;

/**
 * @author squid233
 * @since 2021/03/08
 */
public final class GlProgram implements Closeable {
    private final int programId = glCreateProgram();
    private int vshId, fshId;

    public GlProgram() {
        if (programId == 0) {
            throw new NullPointerException("Failed to create GL program");
        }
    }

    public void createVsh(String src) {
        vshId = createShader(src, GL_VERTEX_SHADER);
    }

    public void createFsh(String src) {
        fshId = createShader(src, GL_FRAGMENT_SHADER);
    }

    private int createShader(String src, int type) {
        int id = glCreateShader(type);
        if (id == 0) {
            throw new NullPointerException("Failed to create shader (Shader type: "
                    + type + ")");
        }
        glShaderSource(id, src);
        glCompileShader(id);
        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error compiling shader src: " + glGetShaderInfoLog(id));
        }
        glAttachShader(programId, id);
        return id;
    }

    public void link() {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error linking GL program: " + glGetProgramInfoLog(programId));
        }
        if (vshId != 0) {
            glDetachShader(programId, vshId);
        }
        if (fshId != 0) {
            glDetachShader(programId, fshId);
        }
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.out.println("[WARN]" + glGetProgramInfoLog(programId));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void enableVertexAttribArray(String name) {
        glEnableVertexAttribArray(glGetAttribLocation(programId, name));
    }

    public void vertexAttribPointer(String name,
                                    int size,
                                    int type,
                                    boolean normalized,
                                    int stride) {
        glVertexAttribPointer(
                glGetAttribLocation(programId, name),
                size,
                type,
                normalized,
                stride,
                0);
    }

    public void disableVertexAttribArrays(String... names) {
        for (String nm : names) {
            glDisableVertexAttribArray(glGetAttribLocation(programId, nm));
        }
    }

    @Override
    public void close() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
        if (vshId != 0) {
            glDeleteShader(vshId);
        }
        if (fshId != 0) {
            glDeleteShader(fshId);
        }
    }
}
