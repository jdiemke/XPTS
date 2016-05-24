package org.polygonize.ats.operators.filters;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.HSV;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.RGB;

@AtsOperatorMetadata(operatorDesignation = "hscb", category = "filters", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

/**
 * http://lodev.org/cgtutor/color.html
 * http://visl.technion.ac.il/labs/anat/hsvspace.pdf
 * http://www.pixtur.org/project/show/8 http://www.framefield.com/contents/3
 * 
 * James D. Foley, Andries van Dam, Steven K. Feiner, John F. Hughes: Computer
 * Graphics: Principles and Practice in C. Addison-Wesley, München 1990.
 * 
 * @author trigger
 *
 */
public class Bchsl extends AtsProcessOperator {

    AtsInteger hue = new AtsInteger(0);
    AtsInteger saturation = new AtsInteger(127);
    AtsInteger brightness = new AtsInteger(127);
    AtsInteger contrast = new AtsInteger(127);

    public void edit(PropertyContainer container) {
        container.attachParam("hue", hue);
        container.attachParam("saturation", saturation);
        container.attachParam("contrast", contrast);
        container.attachParam("brightness", brightness);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {

        AtsProceduralTexture input = inputs_.get(0).getTexture();

        int r, g, b;

        for (int y = 0; y < 256; y++) {
            for (int x = 0; x < 256; x++) {

                r = AtsUtil.extractR(input.getPixel(x, y));
                g = AtsUtil.extractG(input.getPixel(x, y));
                b = AtsUtil.extractB(input.getPixel(x, y));

                RGB rgb = new RGB(r / 255f, g / 255f, b / 255f);
                HSV hsv = AtsUtil.RGB2HSV(rgb);

                // change based on hsv color space
                hsv.hue = (hsv.hue + hue.value * 360.0f / 255.0f) % 360.0f;
                hsv.saturation = Math.min(hsv.saturation * saturation.value / 255.0f * 2, 1.0f);
                hsv.value = Math.min(hsv.value * brightness.value / 255.0f * 2, 1.0f);

                rgb = AtsUtil.HSV2RGB(hsv);

                rgb.red = (rgb.red - 0.5f) * contrast.value * 2 / 255.f + 0.5f;
                rgb.green = (rgb.green - 0.5f) * contrast.value * 2 / 255.f + 0.5f;
                rgb.blue = (rgb.blue - 0.5f) * contrast.value * 2 / 255.f + 0.5f;

                texture_.setPixel(x, y, AtsUtil.clamp((int) (rgb.red * 255)), AtsUtil.clamp((int) (rgb.green * 255)),
                        AtsUtil.clamp((int) (rgb.blue * 255)), 255);
            }
        }

    }

}
