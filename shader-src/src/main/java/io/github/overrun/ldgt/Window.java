package io.github.overrun.ldgt;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * @author squid233
 * @since 2021/03/04
 */
public final class Window implements AutoCloseable {
    private final String title;
    private int width, height;
    private long handle;
    private boolean resized;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.create((error, description) -> {
            System.err.println("########## GL ERROR ##########");
            System.err.println(error + ": " + MemoryUtil.memUTF8(description));
        }).set();
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        // the window will stay hidden after creation
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
        // Create the window
        handle = glfwCreateWindow(width, height, title, 0, 0);
        if (handle == 0) {
            throw new IllegalStateException("Failed to create the window");
        }
        glfwSetFramebufferSizeCallback(handle, (window, w, h) -> {
            width = w;
            height = h;
            setResized(true);
        });
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                // We will detect this in the rendering loop
                glfwSetWindowShouldClose(handle, true);
            }
        });
        // Get the resolution of the primary monitor
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode != null) {
            // Center the window
            glfwSetWindowPos(handle,
                    (vidMode.width() - width) / 2,
                    (vidMode.height() - height) / 2);
        }
        // Make the OpenGL context current
        glfwMakeContextCurrent(handle);
        // Enable v-sync
        glfwSwapInterval(1);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities(true);
        // Make the window visible
        glfwShowWindow(handle);
        // Set the clear color
        glClearColor(.4f, .6f, .9f, 1);
    }

    public boolean isKeyPressed(int key) {
        return glfwGetKey(handle, key) == GLFW_PRESS;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void update() {
        // swap the color buffers
        glfwSwapBuffers(handle);
        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    @Override
    public void close() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
    }
}