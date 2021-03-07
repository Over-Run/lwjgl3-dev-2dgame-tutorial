# 你好，三角形

[上一节](hello_window.md)

---
现在我们想绘制一个五×N彩的三角形。  
在本节中，我们将首次也是最后一次使用固定管线来绘制。如果想了解更多固定管线的使用示例，请参考[Minecraft2D](https://github.com/Over-Run/Minecraft2D).

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
接着在`glClear`后渲染循环()添加下面几条语句：
```java
public class HelloWorld {
    // ..
    private void loop() {
        // ..
        glBegin(GL_TRIANGLES); // 开始绘制三角形
        // 注意：需要逆时针绘制
        glColor3f(1, 0, 0); // 红
        glVertex2f(0, 1); // 中上
        glColor3f(0, 1, 0); // 绿
        glVertex2f(-1, -1); // 左下
        glColor3f(0, 0, 1); // 蓝
        glVertex2f(1, -1); // 右下
        glEnd(); // 绘制结束
        // ..
    }
    // ..
}
```
启动后，会得到一个鲜艳的三角形。

---
失败了？还不快抛[issue](https://github.com/Over-Run/lwjgl3-dev-2dgame-tutorial/issues/new)!

[下一节](shader.md)
