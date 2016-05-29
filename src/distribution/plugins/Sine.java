package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.AtsUtil;

// new metadata
@AtsOperatorMetadata(
		operatorDesignation = "sine test",
		category			= "generator",
		author			= "Johannes Diemke",
		version			= "1.0",
		description 		= "This operator generates a XOR texture.",
		date				= "2008-08-08",
		sourceOfSupply		= "http://informatik.uni-oldenburg.de/~trigger"
)

public class Sine extends AtsDataSourceOperator {

	AtsInteger distort_ = new AtsInteger(0);



	public void process() {
		AtsUtil.srand(distort_.getValue());

		for(int y=0; y < 256;y++)
			for(int x=0; x< 256;x++){
				int color =(int)(127+127* Math.sin(2.0*Math.PI/255.0f*x*10));
				texture_.setPixel(x,y,color, color, color,255);
			}
	}
}