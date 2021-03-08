package io.github.overrun.ldgt.client;

/**
 * @author flysong
 * @since 2021/03/08
 */
public final class Shader {
	private long id;

	public Shader(strring vertexFile, string fragmentFile) {
        char[] vertex = new char[32767];
        char[] fragment = new char[32767];

        try {
            new BufferedReader(new InputStreamReader(new FileInputStream(new File(vertexFile)))).read(vertex);
            new BufferedReader(new InputStreamReader(new FileInputStream(new File(fragmentFile)))).read(fragment);
        } catch (FileInputStream | IOException e) {
            e.printStackTrace();
		    }

        int vertexId, fragmentId;
        int[] status = new int[1];

        vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexId, new String(vertex));
        glCompileShader(vertexId);

        glGetShaderiv(vertexId, GL_COMPILE_STATUS, status);
        if(status[0] == 0) {
            String log = glGetShaderInfoLog(vertexId, 32767);
            throw new IllegalStateException("[Shader Error] Compile vertex shader fail: " + log);
        }

        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentId, new String(fragment));
        glCompileShader(fragmentId);

        glGetShaderiv(fragmentId, GL_COMPILE_STATUS, status);
        if(status[0] == 0) {
            String log = glGetShaderInfoLog(fragmentId, 32767);
            throw new IllegalStateException("[Shader Error] Compile fragment shader fail: " + log);
        }

        shader = glCreateProgram();
        glAttachShader(id, vertexId);
        glAttachShader(id, fragmentId);
        glLinkProgram(id);

        glGetProgramiv(fragmentId, GL_LINK_STATUS, status);
        if(status[0] == 0) {
            String log = glGetProgramInfoLog(fragmentId, 32767);
            throw new IllegalStateException("[Shader Error] Link program fail: " + log);
        }

        glDeleteShader(vertexId);
        glDeleteShader(fragmentId);
    }

    @Override
    protected void finalize() throws Throwable {
        glDeleteProgram(id);
    }

    public int getProgram() {
        return id;
    }

    public void use() {
        glUseProgram(id);
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation(id, name);
    }
}
