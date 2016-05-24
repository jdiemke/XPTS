package org.polygonize.ats.operators;

import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.PropertyContainer;

/**
 * plasma fractal using random midpoint displacement
 * 
 * @see http://www.ic.sunysb.edu/Stu/jseyster/plasma/
 * @see http://www.ic.sunysb.edu/Stu/jseyster/plasma/source.html
 * @see http://www2.vo.lu/homepages/phahn/fractals/plasma.htm
 * @see http://charm.cs.uiuc.edu/users/olawlor/projects/2000/plasma/index.html
 * @see http://www.archadegames.com/articles.php?aname=texgenpt2mod.php
 * @see http://en.wikipedia.org/wiki/Diamond-square_algorithm
 * @see http://www.lighthouse3d.com/opengl/terrain/index.php?mpd2
 * @see http://www.gameprogrammer.com/fractal.html#diamond
 * @see http://library.thinkquest.org/26242/full/types/ch10.html
 * @see http://library.thinkquest.org/26242/full/ap/ap18.html
 * @see http://plus.maths.org/issue6/turner2/index.html
 * @see http://amath.colorado.edu/outreach/demos/hshi/2003Sum/Fractals.html
 * @see http://query.nytimes.com/gst/fullpage.html?res=
 *      9D00E2DE153BF931A15752C0A963948260&sec=technology&spon=&pagewanted=2
 * @see http://en.wikipedia.org/wiki/Fractal_landscape
 * @see http://www.fractal-landscapes.co.uk/maths.html
 * @see http://davis.wpi.edu/~matt/courses/fractals/clouds.html
 * @see http://www.mi.sanu.ac.yu/vismath/javier1/
 * 
 * @author Johannes Diemke
 *
 */

@AtsOperatorMetadata(operatorDesignation = "plasma clouds", category = "generator", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

public class AtsPlasmaCloudOperator extends AtsDataSourceOperator {

    private AtsInteger magnitude2 = new AtsInteger(205);
    private AtsInteger seed = new AtsInteger(255);
    private AtsInteger roughness = new AtsInteger(255);

    public void edit(PropertyContainer container) {
        container.attachParam("displacement", magnitude2);
        container.attachParam("roughness", roughness);
        container.attachParam("random seed", seed);

    }

    public void process() {

        AtsUtil.srand(seed.value);

        int p1 = 128;// AtsUtil.rand() % 256;
        int p2 = AtsUtil.rand() % 256;
        int p3 = AtsUtil.rand() % 256;
        int p4 = AtsUtil.rand() % 256;
        int magnitude = magnitude2.value;

        texture_.setPixel(0, 0, p1, p1, p1, 255);

        fractalPlasma(0, 0, 256, 256, 255);
    }

    void fractalPlasma(int x1, int y1, int x2, int y2, float magnitude) {

        int cx = (x1 + x2) / 2;
        int cy = (y1 + y2) / 2;
        if (x2 - x1 == 1)
            return;

        // if(cx==x1 && cy==y1) return;

        // if(y1==y2 && x1==x2) return;

        int p1 = AtsUtil.extractR(texture_.getPixel(x1 & 0xff, y1 & 0xff));
        int p2 = AtsUtil.extractR(texture_.getPixel(x2 & 0xff, y1 & 0xff));
        int p3 = AtsUtil.extractR(texture_.getPixel(x1 & 0xff, y2 & 0xff));
        int p4 = AtsUtil.extractR(texture_.getPixel(x2 & 0xff, y2 & 0xff));
        int mymagnitude = (int) (magnitude);

        int displacement = (int) ((-(mymagnitude / 2.f) * (magnitude2.value / 255.f))
                + (magnitude2.value / 255.f) * magnitude * ((AtsUtil.rand() % (1001)) / 1000.f));
        magnitude = (mymagnitude * (roughness.value / 255.f));

        int center = (int) ((p1 + p2 + p3 + p4) / 4.f + displacement);
        center = AtsUtil.clamp(center);
        texture_.setPixel(cx & 0xff, cy & 0xff, center, center, center, 255);

        int top = (int) ((p1 + p2) / 2f);
        top = AtsUtil.clamp(top);
        texture_.setPixel(cx & 0xff, y1 & 0xff, top, top, top, 255);

        int left = (int) ((p1 + p3) / 2f);
        left = AtsUtil.clamp(left);
        texture_.setPixel(x1 & 0xff, cy & 0xff, left, left, left, 255);

        int right = (int) ((p2 + p4) / 2f);
        texture_.setPixel(x2 & 0xff, cy & 0xff, right, right, right, 255);

        int bottom = (int) ((p3 + p4) / 2f);
        bottom = AtsUtil.clamp(bottom);
        texture_.setPixel(cx & 0xff, y2 & 0xff, bottom, bottom, bottom, 255);

        fractalPlasma(x1, y1, cx, cy, magnitude);
        fractalPlasma(cx, y1, x2, cy, magnitude);
        fractalPlasma(x1, cy, cx, y2, magnitude);
        fractalPlasma(cx, cy, x2, y2, magnitude);
    }

}
