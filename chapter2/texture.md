# 纹理

[上一节](shader-2.md)

在绘制出纹理前，我们先把三角形变成~~四角形~~四边形。  
我们将把这个四边形看做是两个三角形而不是`GL_QUADS`。

我们将创建`Mesh`来处理。这样便无需让渲染器来处理。

我们还需要修改着色器，以适应新的更改。
```glsl
#version 110

attribute vec2 vert;
attribute vec3 in_color;
// 我们新添了2个变量。
attribute vec2 in_texCoord;
varying vec3 out_color;
varying vec2 out_texCoord;
uniform mat4 orthoMatrix;

void main()
{
    gl_Position = orthoMatrix * vec4(vert, 0, 1);
    out_color = in_color;
    out_texCoord = in_texCoord;
}
```
```glsl
#version 110

varying vec3 out_color;
varying vec2 out_texCoord;
// 纹理采样器
uniform sampler2D textureSampler;

void main()
{
    // 我们将生成2D纹理并与颜色混合。
    // 这就是为什么在固定管线中调用glColor*(1, 1, 1)的原因。
    // 如果不调用这个函数，那么颜色全部都会乘0，结果为黑色。
    gl_FragColor = texture2D(textureSampler, out_texCoord) * vec4(out_color, 1);
}
```

我们还创建了一个`Texture`类，用于快速创建纹理。
```java
private static int loadTexture(String name) {
    int w, h;
    int[] pixels;
    try (InputStream is = ClassLoader.getSystemResourceAsStream(name)) {
        BufferedImage img = ImageIO.read(Objects.requireNonNull(is));
        w = img.getWidth();
        h = img.getHeight();
        // ..
    }
}
```
你可以看到，我们使用了AWT来读取图片。这是因为stb是本地库，它无法获取与Classpath有关的东西，使我们只能用绝对路径。  
我们使用`pixels = img.getRGB(0, 0, w, h, null, 0, w)`来设置像素。得到的数组是`BGRA`格式的。  
我们使用`int id = glGenTextures()`获取纹理ID，并使用`glBindTexture(GL_TEXTURE_2D, id)`将其绑定。  
我们还使用下面这2行代码来设置纹理放大时处理的方式：
```java
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
```
在像素游戏中，使用最近邻居`GL_NEAREST`是最好的方式。  
最后，我们使用`glTexImage2D`来“上传”图片。
```java
glTexImage2D(GL_TEXTURE_2D,
        0,
        GL_RGBA,
        w,
        h,
        0,
        GL_BGRA,
        GL_UNSIGNED_BYTE,
        pixels);
```
- `target`: 要上传到的目标。
- `level`: Mipmap的级别。
- `internalFormat`: 希望GL在内部储存的格式。
- `width`, `height`: 宽高。
- `border`: 此值必须为0。
- `format`: 原像素的格式。
- `type`: 数组类型。
- `pixels`: 像素数组。
> 为什么会有个`type`?
> 
> GL为了在C中实现“重载”，使`pixels`的类型为`const void *`。GL通过`type`来判断`pixels`的类型。

现在我们可以修改渲染器了。  
把之前的vbo代码全部删除，换上全新的`Mesh`。
```java
mesh = Mesh.builder()
        .program(program)
        .vertices(vertices)
        .texCoords(texCoords)
        .indices(indices)
        .texture(new Texture("grass_block.png"))
        .build(-1);
```
我们使用了一个草方块作为纹理。

然后我们设置`textureSampler`为0：`program.setUniform("textureSampler", 0)`  
接着把`glDrawArrays`替换为`mesh.render()`。  
最后，我们在`close`里释放内存：`mesh.close()`。

---
启动后，我们会看到一个能动的草方块。  
![texture](texture.png)

---
[下一节](camera.md)
