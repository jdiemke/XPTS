package org.polygonize.ats.operators.filters;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "pixelize", category = "filters", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class Pixelize extends AtsProcessOperator {

    AtsInteger steps = new AtsInteger(0, 0, 8);

    public void edit(AtsPropertyContainer container) {
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("steps", steps);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        int value = (int) Math.pow(2, steps.value);

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {

                int u = x / value * value + value / 2;
                int v = y / value * value + value / 2;

                int color = source.getPixel(u, v);
                texture_.setPixel(x, y, color);

            }
    }

}