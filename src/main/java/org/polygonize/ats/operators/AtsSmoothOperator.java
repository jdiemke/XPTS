package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.PropertyContainer;

@AtsOperatorMetadata(operatorDesignation = "smooth", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsSmoothOperator extends AtsProcessOperator {

    public void edit(AtsPropertyContainer container) {
    }

    public void edit(PropertyContainer propertyContainer) {
    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    // TODO: read chapter about convolution and point operations
    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        int[] filter = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };

        texture_.convolution(filter, 0, 9, source);
        // int[] filter = { 0, -1, 0,
        // -1, 5, -1,
        // 0, -1, 0};
        //
        // texture_.convolution(filter, 0, 1, source);
    }

}
