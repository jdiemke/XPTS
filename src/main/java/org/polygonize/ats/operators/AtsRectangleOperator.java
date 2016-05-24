package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsPosition2D;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "rect", category = "generator", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsRectangleOperator extends AtsProcessOperator {

    AtsColor color = new AtsColor(255, 255, 255, 255);
    AtsPosition2D pos = new AtsPosition2D(0, 0);
    AtsPosition2D dim = new AtsPosition2D(255, 127);

    public void edit(PropertyContainer container) {
        container.attachParam("color", color);
        container.attachParam("position", pos);
        container.attachParam("dimension", dim);

    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    // TODO: xphase, yphase
    public void process() {
        int x1 = pos.x.value;
        int x2 = dim.x.value;
        int y1 = pos.y.value;
        int y2 = dim.y.value;
        // AtsUtil.srand(cosPeriods_.value_);
        //

        int r, g, b;

        texture_.copy(inputs_.get(0).getTexture());

        for (int y = y1; y < y1 + y2 + 1; y++)
            for (int x = x1; x < x1 + x2 + 1; x++) {

                r = (int) (AtsUtil.extractR(inputs_.get(0).getTexture().getPixel(AtsUtil.repeat(x), AtsUtil.repeat(y)))
                        * (1 - color.alpha.value / 255.f) + color.alpha.value / 255.f * color.red.value);
                g = (int) (AtsUtil.extractG(inputs_.get(0).getTexture().getPixel(AtsUtil.repeat(x), AtsUtil.repeat(y)))
                        * (1 - color.alpha.value / 255.f) + color.alpha.value / 255.f * color.green.value);
                b = (int) (AtsUtil.extractB(inputs_.get(0).getTexture().getPixel(AtsUtil.repeat(x), AtsUtil.repeat(y)))
                        * (1 - color.alpha.value / 255.f) + color.alpha.value / 255.f * color.blue.value);

                texture_.setPixel(AtsUtil.repeat(x), AtsUtil.repeat(y), r, g, b, 255);
            }
    }

}
