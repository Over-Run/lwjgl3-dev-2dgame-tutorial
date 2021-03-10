package io.github.overrun.ldgt.block;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author squid233
 * @since 2021/03/10
 */
public final class BlockModel {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(BlockModel.class, new Serializer())
            .disableHtmlEscaping()
            .create();
    private static final Map<String, BlockModel> MODELS = new HashMap<>();

    public static BlockModel of(String name) {
        return MODELS.computeIfAbsent(name, s -> {
            try (InputStream is = ClassLoader.getSystemResourceAsStream(s);
                 Reader r = new InputStreamReader(
                         Objects.requireNonNull(is),
                         StandardCharsets.UTF_8
                 )) {
                return GSON.fromJson(r, BlockModel.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public static final class Vertex {
        private int x, y;
        private float u, v, r = 1, g = 1, b = 1;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public float getU() {
            return u;
        }

        public float getV() {
            return v;
        }

        public float getR() {
            return r;
        }

        public float getG() {
            return g;
        }

        public float getB() {
            return b;
        }
    }

    private Vertex[] vertices;
    private int[] indices;
    private String texture;

    public static final class Serializer extends TypeAdapter<BlockModel> {
        @Override
        public void write(JsonWriter out, BlockModel value) throws IOException {
            out.beginObject().name("vertices").beginArray();
            for (Vertex vertex : value.vertices) {
                out.beginObject()
                        .name("x").value(vertex.x)
                        .name("y").value(vertex.y)
                        .name("u").value(vertex.u)
                        .name("v").value(vertex.v)
                        .name("r").value(vertex.r)
                        .name("g").value(vertex.g)
                        .name("b").value(vertex.b)
                        .endObject();
            }
            out.endArray().name("indices").beginArray();
            for (int index : value.indices) {
                out.value(index);
            }
            out.endArray().name("texture").value(value.texture).endObject();
        }

        @Override
        public BlockModel read(JsonReader in) throws IOException {
            BlockModel model = new BlockModel();
            List<Vertex> vertices = new ArrayList<>();
            List<Integer> indices = new ArrayList<>();
            String texture = null;
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "vertices":
                        in.beginArray();
                        while (in.hasNext()) {
                            Vertex vertex = new Vertex();
                            in.beginObject();
                            while (in.hasNext()) {
                                switch (in.nextName()) {
                                    case "x":
                                        vertex.x = in.nextInt();
                                        break;
                                    case "y":
                                        vertex.y = in.nextInt();
                                        break;
                                    case "u":
                                        vertex.u = (float) in.nextDouble();
                                        break;
                                    case "v":
                                        vertex.v = (float) in.nextDouble();
                                        break;
                                    case "r":
                                        vertex.r = (float) in.nextDouble();
                                        break;
                                    case "g":
                                        vertex.g = (float) in.nextDouble();
                                        break;
                                    case "b":
                                        vertex.b = (float) in.nextDouble();
                                        break;
                                    default:
                                }
                            }
                            in.endObject();
                            vertices.add(vertex);
                        }
                        in.endArray();
                        break;
                    case "indices":
                        in.beginArray();
                        while (in.hasNext()) {
                            indices.add(in.nextInt());
                        }
                        in.endArray();
                        break;
                    case "texture":
                        texture = in.nextString();
                    default:
                }
            }
            in.endObject();
            model.vertices = vertices.toArray(new Vertex[0]);
            Integer[] integers = indices.toArray(new Integer[0]);
            model.indices = new int[integers.length];
            for (int i = 0; i < integers.length; i++) {
                model.indices[i] = integers[i];
            }
            model.texture = texture;
            return model;
        }
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public float[] getVerticesV() {
        float[] v = new float[vertices.length * 2];
        for (int i = 0; i < vertices.length; i++) {
            v[i * 2] = vertices[i].x;
            v[i * 2 + 1] = vertices[i].y;
        }
        return v;
    }

    public float[] getUv() {
        float[] v = new float[vertices.length * 2];
        for (int i = 0; i < vertices.length; i++) {
            v[i * 2] = vertices[i].u;
            v[i * 2 + 1] = vertices[i].v;
        }
        return v;
    }

    public float[] getColors() {
        float[] v = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; i++) {
            v[i * 3] = vertices[i].r;
            v[i * 3 + 1] = vertices[i].g;
            v[i * 3 + 2] = vertices[i].b;
        }
        return v;
    }

    public int[] getIndices() {
        return indices;
    }

    public String getTexture() {
        return texture;
    }
}
