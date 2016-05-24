package org.polygonize.ats.opengl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

public class Image {

    private ByteBuffer buffer;
    int[] temp;
    private int width_;
    private int height_;

    public int getHeight() {
        return height_;
    }

    public int getWidth() {
        return width_;
    }

    public ByteBuffer getByteBuffer() {
        buffer = ByteBuffer.allocateDirect(temp.length * 4);
        buffer.order(ByteOrder.nativeOrder());

        buffer.put(toByteArray(temp));

        buffer.rewind();
        return buffer;
    }

    public void flip() {
        int[] temp2 = new int[temp.length];
        for (int x = 0; x < width_; x++)
            for (int y = 0; y < height_; y++) {
                // temp2[x+(y*width_)] = temp[x+(y)*width_] ;
                temp2[x + (y * width_)] = temp[x + ((height_ - 1 - y) * width_)];
            }
        temp = temp2;
    }

    public static byte[] toByteArray(int[] array) {
        byte[] data = new byte[array.length * 4];
        int count = 0;

        for (int x = 0; x < array.length; x++) {
            data[count++] = (byte) ((array[x] & 0x00ff0000) >> 16);
            data[count++] = (byte) ((array[x] & 0x0000ff00) >> 8);
            data[count++] = (byte) ((array[x] & 0x000000ff) >> 0);
            if (((array[x] & 0x00ff0000) >> 16) == 255 && ((array[x] & 0x000000ff) >> 0) == 255
                    && ((array[x] & 0x0000ff00) >> 0) == 0)
                data[count++] = (byte) 0;
            else
                // data[count++] = (byte)255;
                data[count++] = (byte) ((array[x] & 0xff000000) >> 24);
        }
        return data;
    }

    public Image(InputStream inputStream) {
        BufferedImage bufferedImage;

        try {

            bufferedImage = ImageIO.read(inputStream);

            width_ = bufferedImage.getWidth();
            height_ = bufferedImage.getHeight();
            int[] packedPixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];

            bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), packedPixels, 0,
                    bufferedImage.getWidth());
            temp = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];

            for (int x = 0; x < bufferedImage.getWidth(); x++)
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    temp[x + (y * bufferedImage.getWidth())] = packedPixels[x
                            + ((bufferedImage.getHeight() - 1 - y) * bufferedImage.getWidth())];
                }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

    }

    public Image(String filename) throws FileNotFoundException {
        this(new FileInputStream(new File(filename)));

    }

}
