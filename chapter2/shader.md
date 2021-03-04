# 着色器

[上一节](hello_triangle.md)

在这里不过多说明与图形学等相关的概念。请读者自行查阅相关资料。  
本节的源码可在[shader-src](shader-src)中找到。

在开始前，我们先列出想要实现的功能。
- 一个鲜艳的三角形
- 可以移动
- 可以缩放
- 可以旋转

现在可以开工了。

## 一个鲜艳的三角形

在开始前，我们先把代码整理下。

然后创建一个接口。该接口包含游戏逻辑。
```java
public interface IGameLogic {
    void init();

    void input();

    void update(float delta);

    void render(int mouseX, int mouseY);
}
```

---
[下一节](texture.md)
