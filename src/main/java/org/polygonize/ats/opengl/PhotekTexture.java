package org.polygonize.ats.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.polygonize.ats.core.operator.AtsProceduralTexture;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class PhotekTexture {

    private int textureId_ = 1;
    private int width_;
    private int height_;

    public int[] texture_;

    public void update(GL gl) {

        // IntBuffer hallo = IntBuffer.wrap(texture_);

        ByteBuffer byteBuffer = ByteBuffer.allocate(texture_.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(texture_);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textureId_);
        gl.glTexSubImage2D(GL.GL_TEXTURE_2D, 0, 0, 0, 256, 256, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, byteBuffer);

    }

    public PhotekTexture(GL2 gl, int[] texture, PhotekTextureFilter photekTextureFilter,
            PhotekTextureWrapMode photekWrapMode) {
        GLU glu = new GLU();
        texture_ = texture;

        AtsProceduralTexture tex = new AtsProceduralTexture();

        tex.clear(0x0000ffff);

        IntBuffer hallo = IntBuffer.wrap(tex.texels_);

        textureId_ = genTexture(gl);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureId_);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, GL2.GL_TRUE);

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

        gl.glTexParameterfv(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_BORDER_COLOR, new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, 0);

        switch (photekWrapMode) {
        case PHOTEK_REPEAT:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            // gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S,
            // GL.GL__REPEAT);
            // gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T,
            // GL.GL__REPEAT);
            break;
        case PHOTEK_CLAMP_TO_EDGE:
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
            // gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S,
            // GL.GL_REPEAT);
            // gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T,
            // GL.GL_REPEAT);
            break;
        }

        // if(photekTextureFilter == PhotekTextureFilter.PHOTEK_MIPMAP) {
        // glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, 256, 256,
        // GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, hallo);
        // } else {
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, 256, 256, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, hallo);
        // }
    }

    public PhotekTexture(GL2 gl, int textureWidth, int textureHeight, PhotekTextureFilter photekTextureFilter,
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
            // glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, textureFormat,
            // textureWidth, textureHeight,
            // GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, GL.GL_TRUE);
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, textureFormat, textureWidth, textureHeight, 0, GL.GL_RGBA,
                    GL.GL_UNSIGNED_BYTE, null);

        } else {
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, textureFormat, textureWidth, textureHeight, 0, GL.GL_RGBA,
                    GL.GL_UNSIGNED_BYTE, null);
        }
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
