package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "tubolence", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsTurbolenceOperator extends AtsProcessOperator {

    AtsInteger x_ = new AtsInteger(0);
    AtsInteger y_ = new AtsInteger(0);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("xScale", x_);
        propertyContainer.attachParam("yScale", y_);
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    // TODO: FIX
    public void process() {

        int xscale = x_.value;
        int yscale = y_.value;
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                float xdisplace = (float) (((Math.cos(2 * Math.PI * x / 256.f * xscale))
                        * (Math.sin(x * 2 * Math.PI / 256.f * yscale))) * 16.f);
                float ydisplace = (float) (((Math.cos(2 * Math.PI * y / 256.f * xscale))
                        * (Math.sin(y * 2 * Math.PI / 256.f * yscale))) * 16.f);

                float u = (x + xdisplace);
                float v = (y + ydisplace);

                int filteredTexel = source.getBilinearFilteredPixel(u, v);

                texture_.setPixel(x, y, filteredTexel);
            }
    }

}
