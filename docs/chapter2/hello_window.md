# 你好，窗口！

我们已经配置好了环境，现在来创建一个窗口。  
只需根据[LWJGL官方给出的示例即可](https://www.lwjgl.org/guide).  
在启动后，您将会看到一个红色窗口。

在本节中，我们使用了[GLFW](https://www.glfw.org)作为GUI库。

---

我相信你无论如何都会对示例感到费解:

What's this?

而且这个示例也太过冗长了

让我们改改

```java
public class HelloWorld {

    // The window handle
    private long window;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default

        // Create the window
        window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
	}

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new HelloWorld().run();
    }

}
```

---

让我们从头开始讲解吧

`private long window;`

我TM为什么需要它？

**窗口句柄。**  
这是答案。

你必须得知道，GLFW是一个C库  
C语言。。。  
哦我的上帝！他竟然不支持类！！！  
~~这可真是见了鬼了~~

聪明的人们想到：
我用一个指向这个窗口信息地址的变量来指代它  
不就得了？！
这就是：**指针**

但是,Java并不支持指针  
幸好指针也算是个数字
人们就用**long**来替代它

---

然后是init()函数

`GLFWErrorCallback.createPrint(System.err).set();`

> Setup an error callback. The default implementation
>
> will print the error message in System.err.

翻译：

> 设置一个错误回调。默认实现会在System.err流中打印一个错误消息

有点像**异常**

```java
if ( !glfwInit() )
    throw new IllegalStateException("Unable to initialize GLFW");
```

`glfwInit()` 会初始化glfw,返回是否成功初始化

`glfwDefaultWindowHints();` 使用默认的配置  
同时，也要提及`glfwWindowHint();` 它用于设置配置

`glfwSetKeyCallback();`

> Setup a key callback. It will be called every time a key is pressed, repeated or released.

翻译：

> 设置一个按键回调, 每次按下、重复或释放某个键时都会调用它。

而`glfwSetWindowShouldClose(window, true);`则会让窗口关闭

`glfwMakeContextCurrent(window);`设置OpenGL上下文

`glfwSwapInterval(1);`开启垂直同步

---

然后是`loop()`函数

`GL.createCapabilities();`创建OpenGL上下文

Stop...

> `glfwMakeContextCurrent(window);`设置OpenGL上下文

WTF?

别急。。。

> This line is critical for LWJGL's interoperation with GLFW's
>
> OpenGL context, or any context that is managed externally.
>
> LWJGL detects the context that is current in the current thread,
>
> creates the GLCapabilities instance and makes the OpenGL
>
> bindings available for use.

翻译：

> 这一行对于LWJGL与GLFW的OpenGL上下文或任何外部管理的上下文的操作是至关重要的.
>
> LWJGL检测当前线程中的当前上下文，创建GLCapabilities实例并使OpenGL绑定可用

言简易赅

**LWJGL特性**

`glClearColor(1.0f, 0.0f, 0.0f, 0.0f);`

设置背景色， 以RGBA模式
OpenGL的RGBA与普通的转换关系如下

```java
public OpenGLRGBA toOpenGL(RGBA color) {
    OpenGLRGBA rgba;
    rgba.r = (float) color.r / 256.0f;
    rgba.g = (float) color.g / 256.0f;
    rgba.b = (float) color.b / 256.0f;
    rgba.a = (float) color.a / 256.0f;
}
```

`glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);`清除颜色缓冲和深度缓冲

`glfwSwapBuffers(window);`交换缓冲

## OpenGL的双缓冲

为了保障体验，OpenGL用两个缓冲轮流工作

一个在台前唱戏

一个在幕后化妆

当`glfwSwapBuffers()`被调用时

两个缓冲就会交换位置

你肯定不希望看到一个演员边化妆边唱戏吧

`glfwPollEvents();`

让GLFW按情况触发事件

```java
    // 清空回调
    glfwFreeCallbacks(window);
    // 关闭（删除）窗口
    glfwDestroyWindow(window);

    // 释放内存
    glfwTerminate();
    // 释放错误回调
    glfwSetErrorCallback(null).free();
```
Nice~
