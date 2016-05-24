import org.polygonize.ats.core.operator.*;

public class MyClearOperator extends AtsDataSourceOperator {

	public void process() {
		texture_.clear(0,255,128,128);
	}
}
