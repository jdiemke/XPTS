import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsUtil;

public class MyInverter2 extends AtsProcessOperator {
	
	public boolean isInputAccepted() {
		return inputs_.size() == 1;
	}
	
	public void process() {
		for(int y=0; y < 256; y++)
			for(int x=0; x < 256; x++) {
				
				int pixel = inputs_.get(0).getTexture().getPixel(x, y);
				
				int r = 255 - AtsUtil.extractR(pixel);
				int g = 255 - AtsUtil.extractG(pixel);
				int b = 255 - AtsUtil.extractB(pixel);
				
				texture_.setPixel(x, y, r, g, b, 255);
			}
	}
	
}