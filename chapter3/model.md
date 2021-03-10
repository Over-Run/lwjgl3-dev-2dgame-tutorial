# JSON模型

[上一章](../chapter2/hud.md)

在本节中我们将使用GSON来读取模型。  
首先在`build.gradle`中添加如下语句：
```groovy
implementation 'com.google.code.gson:gson:2.8.6'
```
导入项目即可。

## 定义格式

在想办法解析前，先把 JSON 的格式定义了。
```json
{
  "vertices": [
    {
      "x": 0, "y": 0,
      "u": 0, "v": 0
    },
    {
      "x": 0, "y": 32,
      "u": 0, "v": 1
    },
    {
      "x": 32, "y": 32
      "u": 1, "v": 1
    },
    {
      "x": 32, "y": 0,
      "u": 1, "v": 0
    }
  ],
  "indices": [0, 1, 3, 3, 1, 2]
  "texture": "grass_block"
}
```
这和我们的硬编码数组完全一样：
```java
    private static final float[] VERTICES = {
            // 左上
            0, 0,
            // 左下
            0, 32,
            // 右下
            32, 32,
            // 右上
            32, 0
    };
    private static final float[] TEX_COORDS = {
            0, 0,
            0, 1,
            1, 1,
            1, 0
    };
    private static final int[] INDICES = {
            0, 1, 3, 3, 2, 1
    };
```
现在我们需要一个`BlockModel`。  
```java
    public static final class Serializer extends TypeAdapter<BlockModel> {
```
该类以流式解析JSON。

最后我们只需要处理顶点、纹理和颜色信息即可。

> 问题：纹理名都是草方块
> 
> 我们将在`单例化`中解决。

现在，我们的游戏的可扩展性越来越高了。

---
[下一节](worldgen.md)
