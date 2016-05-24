package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "contrast", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsContrastBrightnessOperator extends AtsProcessOperator {

    AtsInteger contrast_ = new AtsInteger(255);
    AtsInteger brightness_ = new AtsInteger(255);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("contrast", contrast_);
        container.attachParam("brightness", brightness_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("contrast", contrast_);
        propertyContainer.attachParam("bightness", brightness_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    // void ContrastBrightness(Texel *pTexture, int Contrast, int Brightness)
    // {
    // float fContrast = Contrast/(float)255*2;
    // Brightness = Brightness - 127;
    // for (int x=0;x<256;x++)
    // for (int y=0;y<256;y++)
    // {
    // pTexture[x+(y<<8)].R = FixRange(((pTexture[x+(y<<8)].R - 127) *
    // fContrast) + 127 + Brightness);
    // pTexture[x+(y<<8)].G = FixRange(((pTexture[x+(y<<8)].G - 127) *
    // fContrast) + 127 + Brightness);
    // pTexture[x+(y<<8)].B = FixRange(((pTexture[x+(y<<8)].B - 127) *
    // fContrast) + 127 + Brightness);
    // }
    // }
    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();
        float contrast = contrast_.value / 255f * 2;
        float brightness = brightness_.value;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                int red = AtsUtil
                        .clamp((int) (((AtsUtil.extractR(source.getPixel(x, y)) - 127) * contrast) + brightness));
                int green = AtsUtil
                        .clamp((int) (((AtsUtil.extractG(source.getPixel(x, y)) - 127) * contrast) + brightness));
                int blue = AtsUtil
                        .clamp((int) (((AtsUtil.extractB(source.getPixel(x, y)) - 127) * contrast) + brightness));

                texture_.setPixel(x, y, red, green, blue, 255);
            }
    }

}
