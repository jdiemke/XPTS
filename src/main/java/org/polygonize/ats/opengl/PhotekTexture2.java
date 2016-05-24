package org.polygonize.ats.opengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class PhotekTexture2 {

    private int textureId_ = 1;
    private int width_;
    private int height_;

    public PhotekTexture2(GL2 gl, Image image, PhotekTextureFilter photekTextureFilter,
            PhotekTextureWrapMode photekWrapMode) {
        GLU glu = new GLU();

        if (!checkDimension(image.getHeight()) || !checkDimension(image.getWidth())) {
            System.out.println("wrong size");

            return;
        } else
            System.out.println("correct size");

        textureId_ = genTexture(gl);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureId_);

        switch (photekTextureFilter) {
        case PHOTEK_NEAREST:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
            break;
        case PHOTEK_MIPMAP:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
            break;
        case PHOTEK_LINEAR:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            break;
        }

        switch (photekWrapMode) {
        case PHOTEK_REPEAT:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            break;
        case PHOTEK_CLAMP_TO_EDGE:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
            break;
        }

        if (photekTextureFilter == PhotekTextureFilter.PHOTEK_MIPMAP) {
            glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, image.getWidth(), image.getHeight(), GL.GL_RGBA,
                    GL.GL_UNSIGNED_BYTE, image.getByteBuffer());
        } else {
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL.GL_RGBA,
                    GL.GL_UNSIGNED_BYTE, image.getByteBuffer());
        }
    }

    public PhotekTexture2(GL gl, int textureWidth, int textureHeight, PhotekTextureFilter photekTextureFilter,
            PhotekTextureWrapMode photekWrapMode, PhotekTextureFormat photekTextureFormat) {

        GLU glu = new GLU();

        textureId_ = genTexture(gl);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureId_);

        switch (photekTextureFilter) {
        case PHOTEK_NEAREST:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
            break;
        case PHOTEK_MIPMAP:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
            break;
        case PHOTEK_LINEAR:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            break;
        }

        switch (photekWrapMode) {
        case PHOTEK_REPEAT:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            break;
        case PHOTEK_CLAMP_TO_EDGE:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
            break;
        }

        int textureFormat = GL.GL_RGBA;
        switch (photekTextureFormat) {
        case PHOTEK_DEPTH_COMPONENT:
            textureFormat = GL2.GL_DEPTH_COMPONENT;
            break;
        case PHOTEK_RGBA:
            textureFormat = GL.GL_RGBA;
            break;
        }

        if (photekTextureFilter == PhotekTextureFilter.PHOTEK_MIPMAP) {
            glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, textureFormat, textureWidth, textureHeight, GL.GL_RGBA,
                    GL.GL_UNSIGNED_BYTE, null);
        } else {
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, textureFormat, textureWidth, textureHeight, 0, GL.GL_RGBA,
                    GL.GL_UNSIGNED_BYTE, null);
        }
    }

    private boolean checkDimension(int dim) {
        int[] powerOfTwo = { 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024 };
        for (int i = 0; i < powerOfTwo.length; i++) {
            if (dim == powerOfTwo[i])
                return true;
        }
        return false;
    }

    private int genTexture(GL gl) {
        final int[] tmp = new int[1];
        gl.glGenTextures(1, tmp, 0);
        return tmp[0];
    }

    public void bind(GL gl) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureId_);
    }

    public int getWidth() {
        return width_;
    }

    public int getHeight() {
        return height_;
    }

    public static void enableTexturing(GL gl) {
        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    public static void disableTexturing(GL gl) {
        gl.glDisable(GL.GL_TEXTURE_2D);
    }
}
