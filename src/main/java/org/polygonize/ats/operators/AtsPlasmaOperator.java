package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "plasma", category = "generator", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsPlasmaOperator extends AtsDataSourceOperator {

    private AtsInteger a_ = new AtsInteger(205);
    private AtsInteger b_ = new AtsInteger(255);
    private AtsInteger c_ = new AtsInteger(225);

    private AtsInteger d_ = new AtsInteger(2);
    private AtsInteger e_ = new AtsInteger(2);
    private AtsInteger f_ = new AtsInteger(2);

    public void edit(AtsPropertyContainer container) {
        container.attachParam("a", a_);
        container.attachParam("b", b_);
        container.attachParam("c", c_);
        container.attachParam("d", d_);
        container.attachParam("e", e_);
        container.attachParam("f", f_);
    }

    public void edit(PropertyContainer propertyContainer) {
        propertyContainer.attachParam("a", a_);
        propertyContainer.attachParam("b", b_);
        propertyContainer.attachParam("c", c_);
        propertyContainer.attachParam("d", d_);
        propertyContainer.attachParam("e", e_);
        propertyContainer.attachParam("f", f_);
    }

    public void process() {

        int a = a_.value;
        int b = b_.value;
        int c = c_.value;
        int d = d_.value;
        int e = e_.value;
        int f = f_.value;
        // AtsUtil.srand(cosPeriods_.value_);
        //
        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                int value = (int) ((Math
                        .sin(2 * Math.PI / 256 * x * a + 2 * Math.PI / 256 * y * e
                                + 2 * Math.sin(2 * Math.PI / 256 * y * b))
                        + Math.sin(2 * Math.PI / 256 * x * c + 2 * Math.PI / 256 * y * f
                                + 2 * Math.sin(2 * Math.PI / 256 * x * d))
                        + 2) / 4 * 255);

                texture_.setPixel(x, y, value, value, value, 255);
            }
    }

}
