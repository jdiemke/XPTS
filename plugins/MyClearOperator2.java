package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.*;
import org.polygonize.ats.util.*;

@AtsOperatorMetadata(
		operatorDesignation = "clear",
		category			= "generator",
		author			= "Johannes Diemke",
		version			= "1.0",
		description 		= "This operator clears to const color.",
		date				= "2012-11-05",
		sourceOfSupply		= "http://informatik.uni-oldenburg.de/~trigger"
)

public class MyClearOperator2 extends AtsDataSourceOperator {

	private AtsColor color = new AtsColor(255,255,255,255);

	public void edit(AtsPropertyContainer container) {
		container.add("color", color);
	}

	public void process() {
		int red = color.getRed().getValue();
		int green = color.getGreen().getValue();
		int blue = color.getBlue().getValue();
		int alpha = color.getAlpha().getValue();
		
		texture_.clear(red, green, blue, alpha);
	}
}
