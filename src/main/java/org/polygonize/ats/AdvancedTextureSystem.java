/**
 * file: AdvancedTextureSystem.java
 * created: 29. April 2008
 * author: johannes "trigger" diemke
 * purpose: blah
 * revision: 1.0
 * Copyright 2008 (C) Johannes Diemke. All Rights Reserved.
 */

/**
 * WindowDemo.java
 * 
 * run with: -Dsun.awt.noerasebackground=true
 * 			 -Dsun.java2d.noddraw=true
 *  -Dsun.java2d.opengl=true

 * 
 * @author Johannes Diemke
 */

package org.polygonize.ats;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import org.polygonize.ats.core.RecentFiles.RecentFiles;
import org.polygonize.ats.core.plugin.AtsPlugInSystem;
import org.polygonize.ats.core.ui.AtsMainWindow;
import org.polygonize.ats.core.ui.ImageFactory;
import org.polygonize.ats.operators.AtsAddOperator;
import org.polygonize.ats.operators.AtsAdjustIntensityOperator;
import org.polygonize.ats.operators.AtsAlphaBlendOperator;
import org.polygonize.ats.operators.AtsBitwiseOperator;
import org.polygonize.ats.operators.AtsBlurOperator;
import org.polygonize.ats.operators.AtsChromeOperator;
import org.polygonize.ats.operators.AtsColorizeOperator;
import org.polygonize.ats.operators.AtsContrastBrightnessOperator;
import org.polygonize.ats.operators.AtsDirectionalBlurOperator;
import org.polygonize.ats.operators.AtsDistortOperator;
import org.polygonize.ats.operators.AtsEnvironmentMapOperator;
import org.polygonize.ats.operators.AtsGammaCorrectionOperator;
import org.polygonize.ats.operators.AtsInvertOperator;
import org.polygonize.ats.operators.AtsMappedAlphaBlendOperator;
import org.polygonize.ats.operators.AtsMarbleDistortOperator;
import org.polygonize.ats.operators.AtsNormalMapOperator;
import org.polygonize.ats.operators.AtsPhongOperator;
import org.polygonize.ats.operators.AtsPlasmaCloudOperator;
import org.polygonize.ats.operators.AtsPlasmaOperator;
import org.polygonize.ats.operators.AtsPolarDistortionOperator;
import org.polygonize.ats.operators.AtsRectangleOperator;
import org.polygonize.ats.operators.AtsRefractionOperator;
import org.polygonize.ats.operators.AtsRotoZoomOperator;
import org.polygonize.ats.operators.AtsShearOperator;
import org.polygonize.ats.operators.AtsSineColorOperator;
import org.polygonize.ats.operators.AtsSmoothOperator;
import org.polygonize.ats.operators.AtsSphereMappingOperator;
import org.polygonize.ats.operators.AtsSubPlasmaOperator;
import org.polygonize.ats.operators.AtsTextureOperator;
import org.polygonize.ats.operators.AtsTileOperator;
import org.polygonize.ats.operators.AtsTurbolenceOperator;
import org.polygonize.ats.operators.AtsWhiteNoiseOperator;
import org.polygonize.ats.operators.filters.Bchsl;
import org.polygonize.ats.operators.filters.Color;
import org.polygonize.ats.operators.filters.Convolution;
import org.polygonize.ats.operators.filters.Grayscale;
import org.polygonize.ats.operators.filters.MarblePerlin;
import org.polygonize.ats.operators.filters.Merge;
import org.polygonize.ats.operators.filters.Multiply;
import org.polygonize.ats.operators.filters.Pixelize;
import org.polygonize.ats.operators.filters.Ripple;
import org.polygonize.ats.operators.filters.RotateMul;
import org.polygonize.ats.operators.filters.Sphere;
import org.polygonize.ats.operators.generators.Brick;
import org.polygonize.ats.operators.generators.Cell;
import org.polygonize.ats.operators.generators.Checker;
import org.polygonize.ats.operators.generators.Flat;
import org.polygonize.ats.operators.generators.GlowRect;
import org.polygonize.ats.operators.generators.Gradient;
import org.polygonize.ats.operators.generators.Hexagon;
import org.polygonize.ats.operators.generators.PerlinNoise;
import org.polygonize.ats.operators.generators.Pixels;
import org.polygonize.ats.operators.generators.SinePlasma;
import org.polygonize.ats.operators.generators.Spots;
import org.polygonize.ats.operators.generators.Text;
import org.polygonize.ats.operators.generators.Twirl;
import org.polygonize.ats.operators.generators.Weave;
import org.polygonize.ats.operators.misc.Load;
import org.polygonize.ats.operators.misc.Nop;
import org.polygonize.ats.operators.misc.Store;
import org.polygonize.ats.util.XPTSCompiler;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel;

public class AdvancedTextureSystem {

    private static void createAndShowGUI() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        // AtsMainWindow.setDefaultLookAndFeelDecorated(true);
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JPopupMenu.setDefaultLightWeightPopupEnabled(true);
        // SubstanceLookAndFeel.setToUseConstantThemesOnDialogs(true);

        try {
            UIManager.setLookAndFeel(new SubstanceTwilightLookAndFeel());
        } catch (Exception e) {
            System.out.println("Substance Raven Graphite failed to initialize");
        }

        RecentFiles.getInstance();
        XPTSCompiler.getInstance();

        AtsPlugInSystem loader = AtsPlugInSystem.getInstance();

        // loader.addPluginPath("./bin");
        loader.addPluginPath("./plugins/bin");

        addBuildInOps(loader);

        // loader.addPluginPath("./bin");

        // AtsXMLEncoder.getInstance().save("trigger.xml", );
        UIManager.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS, Boolean.TRUE);

        new AtsMainWindow();
    }

    private static void addBuildInOps(AtsPlugInSystem loader) {
        // misc
        loader.addPlugin(Store.class);
        loader.addPlugin(Load.class);
        loader.addPlugin(Nop.class);

        // generators
        loader.addPlugin(Brick.class);
        loader.addPlugin(Cell.class);
        loader.addPlugin(Checker.class);
        loader.addPlugin(Flat.class);
        loader.addPlugin(GlowRect.class);
        loader.addPlugin(Gradient.class);
        loader.addPlugin(Hexagon.class);
        loader.addPlugin(Pixels.class);
        loader.addPlugin(SinePlasma.class);
        loader.addPlugin(Spots.class);
        loader.addPlugin(Text.class);
        loader.addPlugin(Twirl.class);
        loader.addPlugin(Weave.class);

        // filters
        loader.addPlugin(Bchsl.class);
        loader.addPlugin(Color.class);
        loader.addPlugin(Convolution.class);
        loader.addPlugin(Grayscale.class);
        loader.addPlugin(MarblePerlin.class);
        loader.addPlugin(Merge.class);
        loader.addPlugin(Multiply.class);
        loader.addPlugin(Pixelize.class);
        loader.addPlugin(Ripple.class);
        loader.addPlugin(RotateMul.class);
        loader.addPlugin(Sphere.class);

        // oldops
        loader.addPlugin(AtsAddOperator.class);
        loader.addPlugin(AtsAdjustIntensityOperator.class);
        loader.addPlugin(AtsAlphaBlendOperator.class);
        loader.addPlugin(AtsBitwiseOperator.class);
        loader.addPlugin(AtsBlurOperator.class);
        loader.addPlugin(AtsChromeOperator.class);
        loader.addPlugin(AtsColorizeOperator.class);
        loader.addPlugin(AtsContrastBrightnessOperator.class);
        loader.addPlugin(AtsDirectionalBlurOperator.class);
        loader.addPlugin(AtsDistortOperator.class);
        loader.addPlugin(AtsEnvironmentMapOperator.class);
        loader.addPlugin(AtsGammaCorrectionOperator.class);
        loader.addPlugin(AtsInvertOperator.class);
        loader.addPlugin(AtsMappedAlphaBlendOperator.class);
        loader.addPlugin(AtsMarbleDistortOperator.class);
        loader.addPlugin(AtsNormalMapOperator.class);
        loader.addPlugin(AtsPhongOperator.class);
        loader.addPlugin(AtsPlasmaCloudOperator.class);
        loader.addPlugin(AtsPlasmaOperator.class);
        loader.addPlugin(AtsPolarDistortionOperator.class);
        loader.addPlugin(AtsRectangleOperator.class);
        loader.addPlugin(AtsRefractionOperator.class);
        loader.addPlugin(AtsRotoZoomOperator.class);
        loader.addPlugin(AtsShearOperator.class);
        loader.addPlugin(AtsSineColorOperator.class);
        loader.addPlugin(AtsSineColorOperator.class);
        loader.addPlugin(AtsSmoothOperator.class);
        loader.addPlugin(AtsSphereMappingOperator.class);
        loader.addPlugin(AtsSubPlasmaOperator.class);
        loader.addPlugin(AtsTextureOperator.class);
        loader.addPlugin(AtsTileOperator.class);
        loader.addPlugin(AtsTurbolenceOperator.class);
        loader.addPlugin(AtsWhiteNoiseOperator.class);
        loader.addPlugin(PerlinNoise.class);
    }

    // http://shemnon.com/speling/2011/04/insubstantial-62-release.html
    public static void main(String[] args) {

        ApplicationController.getInstance();
        ImageFactory.getInstance();
        ApplicationProperties.getInstance().load();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
