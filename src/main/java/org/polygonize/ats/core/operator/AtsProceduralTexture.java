package org.polygonize.ats.core.operator;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Arrays;

import org.polygonize.ats.util.AtsUtil;

public class AtsProceduralTexture implements Cloneable {

    public int[] texels_ = new int[256 * 256];
    int width = 256;
    int height = 256;

    public Object clone() throws CloneNotSupportedException {
        AtsProceduralTexture tex = (AtsProceduralTexture) super.clone();
        tex.texels_ = texels_.clone();
        return tex;
    }

    public AtsProceduralTexture() {
    }

    public void clear() {
        Arrays.fill(texels_, 0);
    }

    public void clear(int color) {
        Arrays.fill(texels_, color);
    }

    public BufferedImage getBufferedImage() {
        // BufferedImage image = new BufferedImage(width, height,
        // BufferedImage.TYPE_4BYTE_ABGR);
        // image.setRGB(0, 0, width, height, texels_, 0,width);

        WritableRaster raster = Raster.createPackedRaster(DataBuffer.TYPE_INT, 256, 256, 4, 8, null);

        raster.setDataElements(0, 0, 256, 256, texels_);

        BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        image.setData(raster);

        return image;
    }

    public void setBufferedImage(BufferedImage image) {
        // image.getRGB(0, 0, width, height, texels_, 0,width);
        // DataBufferByte buf = (DataBufferByte)
        // image.getData().getDataBuffer();
        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < 256 * 256; i++)
            texels_[i] = (((data[i] >> 24) & 0xff)) | (((data[i] >> 16) & 0xff) << 24) | (((data[i] >> 8) & 0xff) << 16)
                    | (((data[i]) & 0xff) << 8);

    }

    public void clear(int red, int green, int blue, int alpha) {
        int color = ((red & 0x000000ff) << 24) | ((green & 0x000000ff) << 16) | ((blue & 0x000000ff) << 8)
                | ((alpha & 0x000000ff));

        Arrays.fill(texels_, color);
    }

    public void setPixel(int x, int y, int red, int green, int blue, int alpha) {
        texels_[x + (y << 8)] = ((red & 0x000000ff) << 24) | ((green & 0x000000ff) << 16) | ((blue & 0x000000ff) << 8)
                | ((alpha & 0x000000ff));
    }

    public void setPixel(int index, int red, int green, int blue, int alpha) {
        texels_[index] = ((red & 0x000000ff) << 24) | ((green & 0x000000ff) << 16) | ((blue & 0x000000ff) << 8)
                | ((alpha & 0x000000ff));
    }

    public void setPixel(int x, int y, int color) {
        texels_[x + (y << 8)] = color;
    }

    public int getPixel(int x, int y) {
        return texels_[x + (y << 8)];
    }

    public int getPixel(int index) {
        return texels_[index];
    }

    public int getPixelIntensity(int x, int y) {
        int pixel = texels_[x + (y << 8)];

        return (int) (AtsUtil.extractR(pixel) * 0.3 + AtsUtil.extractG(pixel) * 0.59 + AtsUtil.extractB(pixel) * 0.11);
    }

    public int getPixelIntensity(int offset) {
        int pixel = texels_[offset];

        return (int) (AtsUtil.extractR(pixel) * 0.3 + AtsUtil.extractG(pixel) * 0.59 + AtsUtil.extractB(pixel) * 0.11);
    }

    public void copy(AtsProceduralTexture texture) {
        System.arraycopy(texture.texels_, 0, texels_, 0, texels_.length);
    }

    public int getBilinearFilteredPixel(float u, float v) {
        int x = (int) Math.floor(u);
        int y = (int) Math.floor(v);

        float u_ratio = u - x;
        float v_ratio = v - y;
        float u_opposite = 1 - u_ratio;
        float v_opposite = 1 - v_ratio;

        int texel1 = getPixel(AtsUtil.repeat(x), AtsUtil.repeat(y));
        int texel2 = getPixel(AtsUtil.repeat(x + 1), AtsUtil.repeat(y));
        int texel3 = getPixel(AtsUtil.repeat(x), AtsUtil.repeat(y + 1));
        int texel4 = getPixel(AtsUtil.repeat(x + 1), AtsUtil.repeat(y + 1));

        int red = (int) ((AtsUtil.extractR(texel1) * u_opposite + AtsUtil.extractR(texel2) * u_ratio) * v_opposite
                + (AtsUtil.extractR(texel3) * u_opposite + AtsUtil.extractR(texel4) * u_ratio) * v_ratio);

        int green = (int) ((AtsUtil.extractG(texel1) * u_opposite + AtsUtil.extractG(texel2) * u_ratio) * v_opposite
                + (AtsUtil.extractG(texel3) * u_opposite + AtsUtil.extractG(texel4) * u_ratio) * v_ratio);

        int blue = (int) ((AtsUtil.extractB(texel1) * u_opposite + AtsUtil.extractB(texel2) * u_ratio) * v_opposite
                + (AtsUtil.extractB(texel3) * u_opposite + AtsUtil.extractB(texel4) * u_ratio) * v_ratio);

        int alpha = (int) ((AtsUtil.extractA(texel1) * u_opposite + AtsUtil.extractA(texel2) * u_ratio) * v_opposite
                + (AtsUtil.extractA(texel3) * u_opposite + AtsUtil.extractA(texel4) * u_ratio) * v_ratio);

        return ((red & 0x000000ff) << 24) | ((green & 0x000000ff) << 16) | ((blue & 0x000000ff) << 8)
                | ((alpha & 0x000000ff));
    }

    public void convolution(int[] filter, float bias, float divide, AtsProceduralTexture texture) {

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                int gridCounter = 0;
                float finalr = 0, finalg = 0, finalb = 0;

                for (int y2 = -1; y2 <= 1; y2++)
                    for (int x2 = -1; x2 <= 1; x2++) {
                        int offset = (((x + x2) & 0xff) + (((y + y2) & 0xff) << 8));

                        finalr += AtsUtil.extractR(texture.texels_[offset]) * (float) filter[gridCounter] / divide;
                        finalg += AtsUtil.extractG(texture.texels_[offset]) * (float) filter[gridCounter] / divide;
                        finalb += AtsUtil.extractB(texture.texels_[offset]) * (float) filter[gridCounter] / divide;

                        gridCounter++;
                    }

                finalr += bias;
                finalg += bias;
                finalb += bias;

                setPixel(x, y, AtsUtil.clamp((int) finalr), AtsUtil.clamp((int) finalg), AtsUtil.clamp((int) finalb),
                        255);
            }
    }

}
