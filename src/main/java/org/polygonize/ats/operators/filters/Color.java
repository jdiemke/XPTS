package org.polygonize.ats.operators.filters;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsEnumeration;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "color", category = "filters", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class Color extends AtsProcessOperator {

    AtsEnumeration mode = new AtsEnumeration(
            new String[] { "multiply", "add", "substract", "gray", "invert", "scale" });

    AtsColor color = new AtsColor(255, 255, 255, 255);

    public void edit(PropertyContainer container) {
        container.attachParam("mode", mode);
        container.attachParam("color", color);
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

                switch (mode.selected) {
                case 0: // multiply
                    r = (int) (r * color.red.value / 255.f);
                    g = (int) (g * color.green.value / 255.f);
                    b = (int) (b * color.blue.value / 255.f);
                    break;
                case 1: // add
                    r = (int) (r + color.red.value);
                    g = (int) (g + color.green.value);
                    b = (int) (b + color.blue.value);
                    break;
                case 2: // substract
                    r = (int) (r - color.red.value);
                    g = (int) (g - color.green.value);
                    b = (int) (b - color.blue.value);
                    break;
                case 3: // gray
                    r = (int) (r * 0.3f + g * 0.59f + b * 0.11);
                    g = r;
                    b = r;
                    break;
                case 4: // invert
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;
                    break;
                case 5: // scale
                    r = (int) (r * color.red.value / 255.f * 16);
                    g = (int) (g * color.green.value / 255.f * 16);
                    b = (int) (b * color.blue.value / 255.f * 16);
                    break;
                }

                texture_.setPixel(x, y, AtsUtil.clamp(r), AtsUtil.clamp(g), AtsUtil.clamp(b), 255);
            }
        }

    }

}
