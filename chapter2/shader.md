# 着色器

[上一节](hello_triangle.md)
 
本节的源码可在[shader-src](shader-src)中找到。

在开始前，我们先列出想要实现的功能。
- 一个鲜艳的三角形
- 可以移动（类似于`glTranslate*`）
- 可以缩放（类似于`glScale*`）
- 可以旋转（类似于`glRotate*`）

现在可以开工了。

## 一个鲜艳的三角形

在开始前，我们先把代码整理下（乱糟糟的代码总是不好。如果需要详细信息请查看源码。

首先创建一个接口。该接口包含游戏逻辑。
```java
public interface IGameLogic {
    void init();

    void input(Window window);

    void update(float delta);

    void render(int mouseX, int mouseY);
}
```
然后把GLFW初始化代码放到`Window`类下。  
之后我们需要一个`GameRenderer`负责渲染。  
最后我们创建`DummyGame`来实现`IGameLogic`，并由`GameEngine`托管。

打开后，你会得到一个无聊的黑色窗口。

### 编写着色器

为了能在窗口中显示图像，我们需要至少2个着色器：顶点着色器(vertex shader(vsh))和片元着色器(fragment shader(fsh))。  
vsh示例很简单：
```glsl
#version 110

in vec2 vert;

void main()
{
    gl_Position = vec4(vert, 0, 1);
}
```
fsh也类似：
```glsl
#version 110

void main()
{
    gl_FragColor = vec4(1, 0, 0, 1);
}
```

`#version 110`代表了着色器对版本
`in vec2 vert;`代表输入一个二维向量vert， 即顶点位置
`void main()`是着色器程序的入口
`gl_Position`是一个特殊变量，OpenGL会从这里读取顶点位置
`gl_FragColor`是一个特殊变量，OpenGL会从这里读取片段颜色

以上着色器将在`vert.x, vert.y`处显示一个红色的图形。

让我们读取这些着色器吧。

---

<div style="font-size:5em">TODO</div>

---
[下一节](texture.md)
