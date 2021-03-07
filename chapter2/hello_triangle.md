# 你好，三角形

[上一节](hello_window.md)

---
现在我们想绘制一个五×N彩的三角形。  
在本节中，我们将首次也是最后一次使用固定管线来绘制。如果想了解更多固定管线的使用示例，请参考[Minecraft2D](https://github.com/Over-Run/Minecraft2D).

下一节，我们将会介绍着色器——一个自定义的管线

首先，我们定位到这个位置：
```java
public class HelloWorld {
    // ..
    private void loop() {
        // ..
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glfwSwapBuffers(window); // swap the color buffers
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
        // ..
    }
    // ..
}
```
然后，把`glClearColor(1.0f, 0.0f, 0.0f, 0.0f);`改为`glClearColor(0.0f, 0.0f, 0.0f, 0.0f);`，这样就不会亮瞎眼了。  
接着在`glClear`后添加下面几条语句：
```java
public class HelloWorld {
    // ..
    private void loop() {
        // ..
        glBegin(GL_TRIANGLES); // 开始绘制三角形
        // 注意：需要逆时针绘制
        glColor4f(1.0f, 0.0f, 0.0f, 0.0f); // 红
        glVertex3f(0.0f, 1.0f, 0.0f); // 中上
        glColor4f(0.0f, 1.0f, 0.0f, 0.0f); // 绿
        glVertex3f(-1.0f, -1.0f, 0.0f); // 左下
        glColor4f(0.0f, 0.0f, 1.0f, 0.0f); // 蓝
        glVertex3f(1.0f, -1.0f, 0.0f); // 右下
        glEnd(); // 绘制结束
        // ..
    }
    // ..
}
```
启动后，会得到一个鲜艳的三角形。

`glColor4f()`用于设置颜色, 以OpenGL的RGBA模式

`glVertex3f()`设置顶点位置, 以x, y, z的格式

---
失败了？还不快抛[issue](https://github.com/Over-Run/lwjgl3-dev-2dgame-tutorial/issues/new)!

[下一节](shader.md)
