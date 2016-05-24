package org.polygonize.ats.operators.misc;

import org.polygonize.ats.core.operator.AtsDataSinkOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;

@AtsOperatorMetadata(operatorDesignation = "store", category = "misc", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")
public class Store extends AtsDataSinkOperator {

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {
        AtsProceduralTexture source = inputs_.get(0).getTexture();

        texture_.copy(source);
    }

}
