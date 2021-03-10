package io.github.overrun.ldgt.client;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static org.lwjgl.opengl.GL12.*;

/**
 * @author squid233
 * @since 2021/03/09
 */
public final class Texture implements Closeable {
    private final int id;

    public Texture(String name) {
        id = loadTexture(name);
    }

    private static int loadTexture(String name) {
        int w, h;
        int[] pixels;
        try (InputStream is = ClassLoader.getSystemResourceAsStream(name)) {
            BufferedImage img = ImageIO.read(Objects.requireNonNull(is));
            w = img.getWidth();
            h = img.getHeight();
            pixels = img.getRGB(0, 0, w, h, null, 0, w);
            int id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            if (GL.getCapabilities().glGenerateMipmap == 0) {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            }
            glTexImage2D(GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    w,
                    h,
                    0,
                    GL_BGRA,
                    GL_UNSIGNED_BYTE,
                    pixels);
            if (GL.getCapabilities().glGenerateMipmap != 0) {
                GL30.glGenerateMipmap(GL_TEXTURE_2D);
            }
            return id;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public void close() {
        glDeleteTextures(id);
    }
}
