package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.util.AtsBoolean;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsEnumeration;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "value noise", category = "generator", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

/**
 * http://www.noisemachine.com/talk1/index.html
 * 
 * @author trigger
 *
 */
public class AtsSubPlasmaOperator extends AtsDataSourceOperator {

    private AtsInteger seed = new AtsInteger(2);
    private AtsInteger frequency = new AtsInteger(1, 1, 8);
    private AtsInteger octaves = new AtsInteger(1, 1, 8);
    private AtsBoolean absolute = new AtsBoolean(false);
    private AtsEnumeration marble = new AtsEnumeration(
            new String[] { "disabled", "sin(noise(p))", "abs(sin(noise(p)))" });
    private AtsInteger periods = new AtsInteger(1, 1, 10);
    private AtsInteger distortion = new AtsInteger(1);
    AtsColor color = new AtsColor(255, 255, 255, 255);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("color", color);
        propertyContainer.attachParam("frequency", frequency);
        propertyContainer.attachParam("fbm octaves", octaves);
        propertyContainer.attachParam("random seed", seed);
        propertyContainer.attachParam("absolute noise", absolute);
        propertyContainer.attachParam("marble mode", marble);
        propertyContainer.attachParam("periods", periods);
        propertyContainer.attachParam("displacement", distortion);

    }

    void computeSubPlasma(AtsProceduralTexture texture, int m, int octave) {
        // int m=sinPeriods_.value_;
        // int c=cosPeriods_.value_;

        AtsUtil.srand(m);

        int max = 256;
        int c = Math.max(7 - octave, 0);
        int dim = (int) Math.pow(2, c);
        if (dim > 128)
            dim = 128;

        for (int loop1 = 0; loop1 < 256; loop1 += dim)
            for (int loop2 = 0; loop2 < 256; loop2 += dim) {
                int random1 = AtsUtil.rand() % max;
                int random2 = AtsUtil.rand() % max;
                int random3 = AtsUtil.rand() % max;

                texture.setPixel(loop2, loop1, random1, random1, random1, 255);
            }

        for (int loopx = 0; loopx < 256; loopx += dim)
            for (int loopy = 0; loopy < 257 - dim; loopy += dim)
                for (int loop1 = loopy, loop2 = 0; loop1 < loopy + dim; loop1++, loop2++) {

                    int red = AtsUtil.clamp((int) AtsUtil.cubicInterpolate(
                            AtsUtil.extractR(texture.getPixel(loopx, AtsUtil.repeat(loopy - dim))),
                            AtsUtil.extractR(texture.getPixel(loopx, AtsUtil.repeat(loopy))),
                            AtsUtil.extractR(texture.getPixel(loopx, AtsUtil.repeat(loopy + dim))),
                            AtsUtil.extractR(texture.getPixel(loopx, AtsUtil.repeat(loopy + dim + dim))),
                            loop2 / (float) dim));

                    int green = AtsUtil.clamp((int) AtsUtil.cubicInterpolate(
                            AtsUtil.extractG(texture.getPixel(loopx, AtsUtil.repeat(loopy - dim))),
                            AtsUtil.extractG(texture.getPixel(loopx, AtsUtil.repeat(loopy))),
                            AtsUtil.extractG(texture.getPixel(loopx, AtsUtil.repeat(loopy + dim))),
                            AtsUtil.extractG(texture.getPixel(loopx, AtsUtil.repeat(loopy + dim + dim))),
                            loop2 / (float) dim));

                    int blue = AtsUtil.clamp((int) AtsUtil.cubicInterpolate(
                            AtsUtil.extractB(texture.getPixel(loopx, AtsUtil.repeat(loopy - dim))),
                            AtsUtil.extractB(texture.getPixel(loopx, AtsUtil.repeat(loopy))),
                            AtsUtil.extractB(texture.getPixel(loopx, AtsUtil.repeat(loopy + dim))),
                            AtsUtil.extractB(texture.getPixel(loopx, AtsUtil.repeat(loopy + dim + dim))),
                            loop2 / (float) dim));

                    texture.setPixel(loopx, AtsUtil.repeat(loop1), red, green, blue, 255);
                }

        for (int loopy = 0; loopy < 256; loopy++)
            for (int loopx = 0; loopx < 257 - dim; loopx += dim)
                for (int loop1 = loopx, loop2 = 0; loop1 < loopx + dim; loop1++, loop2++) {

                    int red = AtsUtil.clamp((int) AtsUtil.cubicInterpolate(
                            AtsUtil.extractR(texture.getPixel(AtsUtil.repeat(loopx - dim), loopy)),
                            AtsUtil.extractR(texture.getPixel(AtsUtil.repeat(loopx), loopy)),
                            AtsUtil.extractR(texture.getPixel(AtsUtil.repeat(loopx + dim), loopy)),
                            AtsUtil.extractR(texture.getPixel(AtsUtil.repeat(loopx + dim + dim), loopy)),
                            loop2 / (float) dim));

                    int green = AtsUtil.clamp((int) AtsUtil.cubicInterpolate(
                            AtsUtil.extractG(texture.getPixel(AtsUtil.repeat(loopx - dim), loopy)),
                            AtsUtil.extractG(texture.getPixel(AtsUtil.repeat(loopx), loopy)),
                            AtsUtil.extractG(texture.getPixel(AtsUtil.repeat(loopx + dim), loopy)),
                            AtsUtil.extractG(texture.getPixel(AtsUtil.repeat(loopx + dim + dim), loopy)),
                            loop2 / (float) dim));

                    int blue = AtsUtil.clamp((int) AtsUtil.cubicInterpolate(
                            AtsUtil.extractB(texture.getPixel(AtsUtil.repeat(loopx - dim), loopy)),
                            AtsUtil.extractB(texture.getPixel(AtsUtil.repeat(loopx), loopy)),
                            AtsUtil.extractB(texture.getPixel(AtsUtil.repeat(loopx + dim), loopy)),
                            AtsUtil.extractB(texture.getPixel(AtsUtil.repeat(loopx + dim + dim), loopy)),
                            loop2 / (float) dim));

                    texture.setPixel(loop1, loopy, red, green, blue, 255);
                }

    }

    public void process() {

        // AtsProceduralTexture tex = new AtsProceduralTexture();
        // texture_.clear();
        // int rand= sinPeriods_.value_;
        //
        // int n = 7;
        // int k=0;
        // for(int i = 1; i < n+1; i++) {
        // computeSubPlasma(tex,rand,n+1-i);
        // k++;
        // float pow = (float)Math.pow(1/2.0, i);
        // for(int j=0; j <256*256;j++) {
        // int texel = tex.getPixel(j);
        // int r = Math.abs(AtsUtil.extractR(texel)-128)+128;
        // r = (int)(r *pow);
        // int texel2 = AtsUtil.extractR(texture_.getPixel(j));
        // texel2 += r;
        // texture_.setPixel(j, (int)texel2, (int)texel2, (int)texel2, 255);
        // }
        //
        // }

        AtsProceduralTexture tex = new AtsProceduralTexture();
        texture_.clear();
        int rand = seed.value;

        // computeSubPlasma(texture_,rand,frequency.value_-1);

        int o = Math.min(octaves.value, 8 - (frequency.value - 1));

        float amplitude = 1.0f;
        float totalamplitude = 0.0f;
        float persistance = 0.5f;

        for (int i = 0; i < o; i++) {
            computeSubPlasma(tex, rand, frequency.value - 1 + i);
            amplitude *= persistance;
            totalamplitude += amplitude;

            float pow = amplitude;
            for (int j = 0; j < 256 * 256; j++) {
                int texel = tex.getPixel(j);

                int r, g, b;

                if (!absolute.value_) {
                    r = AtsUtil.extractR(texel);

                    g = AtsUtil.extractG(texel);
                    b = AtsUtil.extractB(texel);
                } else {
                    r = Math.abs(AtsUtil.extractR(texel) - 128) * 2;
                    g = Math.abs(AtsUtil.extractG(texel) - 128) * 2;
                    b = Math.abs(AtsUtil.extractB(texel) - 128) * 2;
                }

                r = (int) (r * pow);
                g = (int) (g * pow);
                b = (int) (b * pow);
                int texel2 = AtsUtil.extractR(texture_.getPixel(j));
                texel2 += r;
                int texel3 = AtsUtil.extractG(texture_.getPixel(j));
                texel3 += g;
                int texel4 = AtsUtil.extractB(texture_.getPixel(j));
                texel4 += b;
                texture_.setPixel(j, AtsUtil.clamp(texel2), AtsUtil.clamp(texel3), AtsUtil.clamp(texel4), 255);
            }

        }

        for (int i = 0; i < 256 * 256; i++) {
            int g = AtsUtil.extractG(texture_.getPixel(i));

            g = AtsUtil.clamp((int) (g / totalamplitude));
            texture_.setPixel(i, (int) (g * color.red.value / 255.f), (int) (g * color.green.value / 255.f),
                    (int) (g * color.blue.value / 255.f), 255);
        }

        int periods = this.periods.value;
        float dist = distortion.value / 255.f * 0.1f;

        if (marble.selected == 1) {
            for (int x = 0; x < 256; x++)
                for (int y = 0; y < 256; y++) {
                    int texel = texture_.getPixel(x, y);
                    int r = AtsUtil.extractR(texel);
                    int color = (int) ((1 + (Math.sin(x / 256.0f * 2 * Math.PI * periods + periods * dist * r))) / 2.0
                            * 255);
                    texture_.setPixel(x, y, color, color, color, 255);
                }
        }
        if (marble.selected == 2) {
            for (int x = 0; x < 256; x++)
                for (int y = 0; y < 256; y++) {
                    int texel = texture_.getPixel(x, y);
                    int color = AtsUtil.extractR(texel);

                    float inten = (float) Math.abs(Math.sin(x / 256.f * Math.PI * periods + periods * dist * color));

                    int color2 = AtsUtil.clamp((int) (inten * 255));
                    texture_.setPixel(x, y, color2, color2, color2, 255);
                }
        }

        // texture_.copy(tex);

    }

}
