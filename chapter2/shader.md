# 着色器

[上一节](hello_triangle.md)
 
本节的源码可在[shader-src](shader-src)中找到。

在开始前，我们先列出想要实现的功能。
- 一个鲜艳的三角形
- ~~ 可以移动（类似于`glTranslate*`）~~
- ~~ 可以缩放（类似于`glScale*`）~~
- ~~ 可以旋转（类似于`glRotate*`）~~

现在可以开工了。

## 一个鲜艳的三角形

在开始前，我们先把代码整理下（乱糟糟的代码总是不好)。不给出太多的实现，如果需要详细信息请查看源码。

首先创建一个接口。该接口包含游戏逻辑。
```java
public interface IGameLogic {
    void init();

    void input(Window window);

    void update(float delta);

    void render(int mouseX, int mouseY);
}
```  
再创建一个Window类：  
```java
public final class Window {
    private final String title;

    private int width, height;

    private long handle;

    private boolean resized;

    public Window(String title, int width, int height);
    public void init();

    public boolean isKeyPressed(int key);

    public boolean shouldClose();

    public void update();

    public String getTitle();

    public int getWidth();

    public int getHeight();

    public boolean isResized();

    public void setResized(boolean resized);
}
```  
然后把GLFW初始化代码放到`Window`类下。  
之后创建一个`GameRenderer`负责渲染。  
```java
public final class GameRenderer {
    public void init();
    public void render();
}
```  
最后我们创建`DummyGame`来实现`IGameLogic`，并由`GameEngine`托管。
```java
public final class DummyGame implements IGameLogic {
    private final GameRenderer renderer = new GameRenderer();

    @Override
    public void init();

    @Override
    public void input(Window window);

    @Override
    public void update(float delta);

    @Override
    public void render(Window window);
}
```  
```java
public final class GameEngine implements Runnable {
    private final Window window;
    private final IGameLogic logic;
    private final Timer timer = new Timer();

    public GameEngine(String title, int width, int height, IGameLogic logic);

    public void init();

    public void input();

    public void update(float delta);

    public void render();

    private void loop();

    @Override
    public void run();
}
```  
打开后，你就可以得到一个无聊的黑色窗口了。

### 编写着色器

为了不使用固定管线而在窗口中显示图像，我们需要至少2个着色器：顶点着色器(vertex shader(vsh))和片元着色器(fragment shader(fsh))。  
当然，你还会遇到几何着色器(geomety shader gsh)。  
vsh示例很简单：  
(item.vsh)  
```glsl
#version 110

in vec2 vert;

void main()
{
    gl_Position = vec4(vert, 0, 1);
}
```
fsh也类似：  
(item.fsh)  
```glsl
#version 110

void main()
{
    gl_FragColor = vec4(1, 0, 0, 1);
}
```

`#version 110`代表了着色器的版本  
`in vec2 vert;`代表输入一个二维向量vert， 即顶点位置  
`void main()`是着色器程序的入口  
`gl_Position`是一个特殊变量，OpenGL会从这里读取顶点位置  
`gl_FragColor`是一个特殊变量，OpenGL会从这里读取片段颜色  
以上着色器将在`vert.x, vert.y`处显示一个红色的图形。   
显然，我们需要读取着色器并让OpenGL识别它：~~废话~~  
为了方便，我们写一个着色器类  
```java
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

    public int getProgram();

    public void use() {
        glUseProgram(id);
    }

    public int getUniformLocation(String name);
}
```  
按照惯例，开始解释
```java
        char[] vertex = new char[32767];
        char[] fragment = new char[32767];

        try {
            new BufferedReader(new InputStreamReader(new FileInputStream(new File(vertexFile)))).read(vertex);
            new BufferedReader(new InputStreamReader(new FileInputStream(new File(fragmentFile)))).read(fragment);
        } catch (FileInputStream | IOException e) {
            e.printStackTrace();
        }
```  
这部分相信大家都能看懂：用Reader读取文件中所有的字节  
```java
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexId, new String(vertex));
        glCompileShader(vertexId);
```  
先创建一个顶点着色器`glCreateShader(GL_VERTEX_SHADER)`  
`glCreateShader`代表创建着色器  
`GL_VERTEX_SHADER`代表顶点着色器  
`glShaderSource`绑定着色器源码  
`glCompileShader`编译着色器  

`glGetShaderiv`用于读取着色器状态，这里读取的是编译状态  
其中第三个参数必须是个数组  
当`status[0]`为0使，编译不成功  
`glGetShaderInfoLog`获取其状态  
片段着色器以此类推  
着色器程序以此类推  
最后`glDeleteShader`释放内存  
当finalize被调用时，`glDeleteProgram`释放内存  
`glUseProgram`使用着色器

至此，着色器类完

---

<div style="font-size:5em">TODO</div>

---
[下一节](texture.md)
