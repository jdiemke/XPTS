package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsEnumeration;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "blur", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")
/**
 * @see http://www.blackpawn.com/texts/blur/default.html
 * 
 */
public class AtsBlurOperator extends AtsProcessOperator {

    AtsInteger radius = new AtsInteger(1);
    AtsInteger amplify = new AtsInteger(32);
    AtsInteger passes = new AtsInteger(1);
    AtsEnumeration direction = new AtsEnumeration(new String[] { "both", "x", "y" });

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("radius", radius);
        propertyContainer.attachParam("amplify", amplify);
        propertyContainer.attachParam("passes", passes);
        propertyContainer.attachParam("direction", direction);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();
        int radius = this.radius.value;
        int passes = this.passes.value;
        float amplify = this.amplify.value / 255.f * 8;

        fastBlur(source, texture_, radius, passes, amplify);
    }

    public void transpose(AtsProceduralTexture texture) {
        for (int y = 0; y < 256; y++)
            for (int x = y + 1; x < 256; x++) {
                int pixel = texture.getPixel(x, y);
                texture.setPixel(x, y, texture.getPixel(y, x));
                texture.setPixel(y, x, pixel);
            }
    }

    public void blurHorizontalLightspeed(AtsProceduralTexture source, AtsProceduralTexture dest, int radius,
            float amplify) {

        for (int y = 0; y < 256; y++) {
            int pixel = 0;
            float totalR = 0;
            float totalG = 0;
            float totalB = 0;

            for (int kx = -radius; kx <= radius; kx++) {
                pixel = source.getPixel(AtsUtil.repeat(kx), AtsUtil.repeat(y));
                totalR += AtsUtil.extractR(pixel);
                totalG += AtsUtil.extractG(pixel);
                totalB += AtsUtil.extractB(pixel);

            }
            dest.setPixel(0, y, AtsUtil.clamp((int) (totalR / ((radius * 2.f + 1)) * amplify)),
                    AtsUtil.clamp((int) (totalG / ((radius * 2.f + 1)) * amplify)),
                    AtsUtil.clamp((int) (totalB / ((radius * 2.f + 1)) * amplify)), 255);

            for (int x = 1; x < 256; x++) {
                pixel = source.getPixel(AtsUtil.repeat(x - radius - 1), AtsUtil.repeat(y));
                totalR -= AtsUtil.extractR(pixel);
                totalG -= AtsUtil.extractG(pixel);
                totalB -= AtsUtil.extractB(pixel);

                pixel = source.getPixel(AtsUtil.repeat(x + radius), AtsUtil.repeat(y));
                totalR += AtsUtil.extractR(pixel);
                totalG += AtsUtil.extractG(pixel);
                totalB += AtsUtil.extractB(pixel);

                dest.setPixel(x, y, AtsUtil.clamp((int) (totalR / ((radius * 2.f + 1)) * amplify)),
                        AtsUtil.clamp((int) (totalG / ((radius * 2.f + 1)) * amplify)),
                        AtsUtil.clamp((int) (totalB / ((radius * 2.f + 1)) * amplify)), 255);
            }
        }
    }

    public void fastBlur(AtsProceduralTexture source, AtsProceduralTexture dest, int radius, int passes,
            float amplify) {

        AtsProceduralTexture sourceCopy = new AtsProceduralTexture();
        sourceCopy.copy(source);

        if (direction.selected != 2) {
            for (int i = 0; i < passes; i++) {
                blurHorizontalLightspeed(sourceCopy, dest, radius, amplify);
                sourceCopy.copy(dest);
            }
        }

        if (direction.selected != 1) {
            transpose(sourceCopy);
            for (int i = 0; i < passes; i++) {
                blurHorizontalLightspeed(sourceCopy, dest, radius, amplify);
                sourceCopy.copy(dest);
            }
            transpose(dest);
        }
    }

}
