package org.polygonize.ats.operators.generators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "flat", category = "generators", author = "Johannes Diemke", version = "1.0", description = "Generates a constant color.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class Flat extends AtsDataSourceOperator {

    private AtsColor color = new AtsColor(128, 255, 128, 255);

    public void edit(PropertyContainer container) {
        container.attachParam("color", color);
    }

    public void process() {
        texture_.clear(color.red.value, color.green.value, color.blue.value, color.alpha.value);
    }

}