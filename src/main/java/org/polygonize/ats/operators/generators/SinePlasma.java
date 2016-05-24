package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "sine plasma", category = "generators", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class SinePlasma extends AtsDataSourceOperator {

    private AtsInteger sinPeriods_ = new AtsInteger(2);
    private AtsInteger cosPeriods_ = new AtsInteger(2);

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("sine periods", sinPeriods_);
        propertyContainer.attachParam("cosine periods", cosPeriods_);
    }

    // TODO: xphase, yphase
    public void process() {
        int m = sinPeriods_.value;
        int c = cosPeriods_.value;

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                int value = (int) ((Math.sin(2 * Math.PI / 256 * x * m) + Math.cos(2 * Math.PI / 256 * y * c) + 2) / 4
                        * 255);

                texture_.setPixel(x, y, value, value, value, 255);
            }
    }

}
