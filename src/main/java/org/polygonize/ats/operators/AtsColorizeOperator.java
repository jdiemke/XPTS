package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "range", category = "filters", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsColorizeOperator extends AtsProcessOperator {

    AtsColor color1 = new AtsColor(0, 0, 0, 255);
    AtsColor color2 = new AtsColor(255, 255, 255, 255);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("color 1", color1);
        propertyContainer.attachParam("color 2", color2);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {

        AtsProceduralTexture source = inputs_.get(0).getTexture();
        int r1 = color1.red.value;
        int g1 = color1.green.value;
        int b1 = color1.blue.value;
        int a1 = color1.alpha.value;

        int r2 = color2.red.value;
        int g2 = color2.green.value;
        int b2 = color2.blue.value;
        int a2 = color2.alpha.value;

        float scale = 0.3f;
        float scaler, scaleg, scaleb, scalea;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                scaler = AtsUtil.extractR(source.getPixel(x, y)) / 255.f;
                scaleg = AtsUtil.extractG(source.getPixel(x, y)) / 255.f;
                scaleb = AtsUtil.extractB(source.getPixel(x, y)) / 255.f;
                scalea = AtsUtil.extractA(source.getPixel(x, y)) / 255.f;

                int red = (int) (r1 + scaler * (r2 - r1));
                int green = (int) (g1 + scaleg * (g2 - g1));
                int blue = (int) (b1 + scaleb * (b2 - b1));
                int alpha = (int) (a1 + scaleb * (a2 - a1));

                texture_.setPixel(x, y, red, green, blue, alpha);
            }
    }

}
