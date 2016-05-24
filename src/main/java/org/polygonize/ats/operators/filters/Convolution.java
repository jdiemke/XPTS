package org.polygonize.ats.operators.filters;

import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.core.operator.AtsProcessOperator;
import org.polygonize.ats.util.AtsColor;
import org.polygonize.ats.util.AtsEnumeration;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.AtsUtil;
import org.polygonize.ats.util.HSV;
import org.polygonize.ats.util.PropertyContainer;
import org.polygonize.ats.util.RGB;

@AtsOperatorMetadata(operatorDesignation = "sharpen", category = "filters", author = "Johannes Diemke", version = "1.0", description = "This operator generates a XOR texture.", date = "2008-08-08", sourceOfSupply = "http://informatik.uni-oldenburg.de/~trigger")

/*
 * http://www.cambridgeincolour.com/tutorials/sharpness.htm
 * http://www.cambridgeincolour.com/tutorials/image-sharpening.htm
 * http://www.thedphoto.com/post_processing/achieving-image-sharpness/
 * http://www.thephotoargus.com/tips/sharpening-your-image-high-pass-filter-vs-
 * unsharp-mask/ http://www.bythom.com/sharpening.htm
 * http://www.creativepro.com/article/out-gamut-almost-everything-you-wanted-
 * know-about-sharpening-photoshop-were-afraid-ask
 * http://www.creativepro.com/article/out-gamut-almost-everything-you-wanted-
 * know-about-sharpening-photoshop-were-afraid-ask
 * http://photo.net/learn/digital-photography-workflow/advanced-photoshop-
 * tutorials/sharpening-in-lab-color/
 * http://blog.epicedits.com/2008/02/08/photoshop-technique-lab-sharpening/
 * http://www.123rf.com/blog/blog.php?idblog=b1000077
 * http://www.flickr.com/groups/photowalkthrough/discuss/72157613523100331/
 * http://www.psdbox.com/tutorials/sharpening-in-lab-color-mode-photoshop-
 * tutorial/ http://www.luminous-landscape.com/tutorials/smart_sharp.shtml
 * http://www.gimp.org/tutorials/Smart_Sharpening/
 * http://en.wikipedia.org/wiki/Edge_enhancement
 * http://www.stanford.edu/class/cs448f/lectures/2.1/Sharpening.pdf
 * http://homepages.inf.ed.ac.uk/rbf/HIPR2/gsmooth.htm
 * http://de.wikipedia.org/wiki/Gau%C3%9F-Filter
 * http://gimp.open-source-solution.org/manual/plug-in-unsharp-mask.html
 * http://www.tankedup-imaging.com/unsharp.html
 * http://www.luminous-landscape.com/tutorials/understanding-series/
 * understanding-usm.shtml http://www.scantips.com/simple6.html
 * http://www.cambridgeincolour.com/tutorials/unsharp-mask.htm
 * http://en.wikipedia.org/wiki/Unsharp_masking
 * http://en.wikipedia.org/wiki/Gaussian_blur
 * 
 * unsharp masking for AO: http://mattebb.com/weblog/unsharp-mask-ao/
 * http://mattebb.com/weblog/unsharp-mask-ao/
 * http://www.cs.rpi.edu/~cutler/classes/advancedgraphics/S11/final_projects/
 * chamberlin_sullivan.pdf
 * http://graphics.uni-konstanz.de/publikationen/2006/unsharp_masking/webseite/
 */
public class Convolution extends AtsProcessOperator {

    AtsEnumeration mode = new AtsEnumeration(
            new String[] { "multiply", "add", "substract", "gray", "invert", "scale" });

    AtsColor color = new AtsColor(255, 255, 255, 255);
    AtsInteger radius = new AtsInteger(1, 1, 255);
    AtsInteger scale = new AtsInteger(127, 0, 255);
    AtsInteger thresh = new AtsInteger(127, 0, 255);

    public void edit(PropertyContainer container) {
        container.attachParam("radius", radius);
        container.attachParam("amount", scale);
        container.attachParam("threshold", thresh);

    }

    public boolean isInputAccepted() {
        return inputs_.size() == 1;
    }

    public void process() {

        AtsProceduralTexture input = inputs_.get(0).getTexture();

        // STEP ONE:
        // COMPUTE SEPERABLE GAUSSIAN FILTER KERNEL
        // OF RADIUS SIGMA
        // @see http://de.wikipedia.org/wiki/Gau%C3%9F-Filter

        int filterKernelRadius = radius.value;

        if (filterKernelRadius < 1)
            filterKernelRadius = 1;

        int filterKernelSize = filterKernelRadius * 2 + 1;
        double[] kernel = new double[filterKernelSize];

        // precomputing gauss function
        double sigma = filterKernelRadius / 3.0d;
        double twoSigmaSquare = 2d * sigma * sigma;
        double sigmaRoot = Math.sqrt(twoSigmaSquare * Math.PI);

        double sum = 0.0d;

        for (int i = 0; i < filterKernelSize; i++) {
            double x = i - filterKernelRadius;
            kernel[i] = Math.exp(-x * x / twoSigmaSquare) / sigmaRoot;
            sum += kernel[i];
        }

        // normalize gauss function
        for (int i = 0; i < filterKernelSize; i++) {
            kernel[i] /= sum;
        }

        // STEP TWO:
        // BLUR Brightness component of the original image

        // copy brightness into double buffer

        double[] luminance = new double[256 * 256];

        for (int i = 0; i < 256 * 256; i++) {
            int pixel = input.getPixel(i);

            int red = AtsUtil.extractR(pixel);
            int green = AtsUtil.extractG(pixel);
            int blue = AtsUtil.extractB(pixel);

            RGB rgb = new RGB(red / 255.f, green / 255.f, blue / 255.f);
            HSV hsv = AtsUtil.RGB2HSV(rgb);

            luminance[i] = hsv.value;
        }

        double[] luminanceH = new double[256 * 256];

        // horizonal blur pass
        for (int x = 0; x < 256; x++) {
            for (int y = 0; y < 256; y++) {
                for (int i = 0; i < filterKernelSize; i++) {
                    int offset = i - filterKernelRadius;
                    luminanceH[x + (y << 8)] += luminance[((x + offset) & 0xff) + (y << 8)] * kernel[i];
                }
            }
        }

        double[] luminanceV = new double[256 * 256];
        // vertical blur pass
        for (int x = 0; x < 256; x++) {
            for (int y = 0; y < 256; y++) {
                for (int i = 0; i < filterKernelSize; i++) {
                    int offset = i - filterKernelRadius;
                    luminanceV[x + (y << 8)] += luminanceH[x + (((y + offset) & 0xff) << 8)] * kernel[i];
                }
            }
        }

        // compute difference
        double[] diff = new double[256 * 256];
        for (int i = 0; i < 256 * 256; i++) {
            diff[i] = luminance[i] - luminanceV[i];
        }

        // hier erstmal nur bild mit variablem radius blurren!
        for (int i = 0; i < 256 * 256; i++) {
            double lum = diff[i] * scale.value / 255d * 2 + luminance[i];

            int pixel = input.getPixel(i);

            int red = AtsUtil.extractR(pixel);
            int green = AtsUtil.extractG(pixel);
            int blue = AtsUtil.extractB(pixel);

            RGB rgb = new RGB(red / 255.f, green / 255.f, blue / 255.f);
            HSV hsv = AtsUtil.RGB2HSV(rgb);

            hsv.value = (float) Math.max(Math.min(lum, 1.0), 0.0);

            rgb = AtsUtil.HSV2RGB(hsv);

            texture_.setPixel(i, (int) (255 * rgb.red), (int) (255 * rgb.green), (int) (255 * rgb.blue), 255);
        }

        // int sharpen[] = new int[] { 1, 2, 1,
        // 2, -12, 2,
        // 1, 2, 1 };
        //
        //
        // texture_.convolution(sharpen, 0, 1, input);

    }

}
