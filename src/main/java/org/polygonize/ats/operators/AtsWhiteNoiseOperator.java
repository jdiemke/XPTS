package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;

@AtsOperatorMetadata(operatorDesignation = "white noise", category = "generator", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsWhiteNoiseOperator extends AtsDataSourceOperator {

    AtsInteger distort_ = new AtsInteger(0);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("Distort:", distort_);
    }

    public void process() {
        AtsUtil.srand(distort_.getValue());

        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                int color = AtsUtil.rand() % 256;
                texture_.setPixel(x, y, color, color, color, 255);
            }

    }
}
