package org.polygonize.ats.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;

import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.MattePainter;
import org.polygonize.ats.ApplicationController;
import org.polygonize.ats.ApplicationProperties;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants.SubstanceWidgetType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.fonts.FontPolicy;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.api.painter.overlay.TopShadowOverlayPainter;

public class AtsMainWindow extends JFrame {

    AtsOperatorGraphView graph;

    public AtsMainWindow() {
        ApplicationController.getInstance().mainWindow = this;
        // ApplicationController.getInstance().setActiveFile(new
        // File("default.xml"));
        ApplicationController.getInstance().updateCaption();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                ApplicationController.getInstance().close();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        // FramesIconImage.class.getResource("discovery.gif");
        // datei aus dem klassenphad, kann auch in Java-Archiv eingebettet sein
        // setIconImage(new
        // ImageIcon("resources/utilities-terminal.png").getImage());
        setIconImage(new ImageIcon("resources/applications-graphics.png").getImage());
        SubstanceLookAndFeel.setWidgetVisible(this.getRootPane(), true, SubstanceWidgetType.TITLE_PANE_HEAP_STATUS);
        SubstanceLookAndFeel.getCurrentSkin().addOverlayPainter(TopShadowOverlayPainter.getInstance(),
                DecorationAreaType.TOOLBAR);
        // SubstanceLookAndFeel.getCurrentSkin()
        SubstanceLookAndFeel.setFontPolicy(new FontPolicy() {

            @Override
            public FontSet getFontSet(String lafName, UIDefaults table) {
                // TODO Auto-generated method stub
                return new FontSet() {

                    FontUIResource res = new FontUIResource("Tahoma", Font.PLAIN, 12);

                    @Override
                    public FontUIResource getWindowTitleFont() {
                        // TODO Auto-generated method stub
                        return res;
                    }

                    @Override
                    public FontUIResource getTitleFont() {
                        // TODO Auto-generated method stub
                        return res;
                    }

                    @Override
                    public FontUIResource getSmallFont() {
                        // TODO Auto-generated method stub
                        return res;
                    }

                    @Override
                    public FontUIResource getMessageFont() {
                        // TODO Auto-generated method stub
                        return res;
                    }

                    @Override
                    public FontUIResource getMenuFont() {
                        // TODO Auto-generated method stub
                        return res;
                    }

                    @Override
                    public FontUIResource getControlFont() {
                        // TODO Auto-generated method stub
                        return res;
                    }
                };
            }
        });
        JPanel contentPane = new JPanel(new BorderLayout(0, 0));
        AtsTexturePreview texture = AtsTexturePreview.getInstance();
        AtsPropertyView preview = AtsPropertyView.getInstance();
        graph = AtsOperatorGraphView.getInstance();
        JScrollPane scrollPane = new JScrollPane(graph, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JXTitledPanel titledPanel = new JXTitledPanel();
        titledPanel.setTitlePainter(new MattePainter(new GradientPaint(0f, 0f, Color.BLACK, 0f, 16f, Color.BLACK)));

        titledPanel.setTitle("Operator Stack");

        JXTitledPanel titledPanel2 = new JXTitledPanel();

        titledPanel2.setTitle("Object Properties");

        SubstanceColorScheme scheme = SubstanceLookAndFeel.getCurrentSkin().getColorScheme(DecorationAreaType.NONE,
                ColorSchemeAssociationKind.BORDER, ComponentState.DEFAULT);
        Color dark = scheme.getMidColor();
        Color bright = scheme.getExtraLightColor();
        Color extraDark = scheme.getDarkColor();

        Border compound = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(dark),
                BorderFactory.createLineBorder(bright));
        texture.setBorder(BorderFactory.createLineBorder(extraDark));

        compound = BorderFactory.createEmptyBorder();
        titledPanel2.setBorder(compound);
        titledPanel2.add(preview);

        JXTitledPanel titledPanel3 = new JXTitledPanel();
        titledPanel3.setTitle("Operator Visualization");
        titledPanel3.setBorder(compound);
        titledPanel3.add(texture);

        titledPanel.setBorder(compound);
        // titledPanel.setLayout(new BorderLayout());
        titledPanel.add(scrollPane, BorderLayout.CENTER);
        JToolBar bar1 = new JToolBar(JToolBar.VERTICAL);
        bar1.add(new JButton(new ImageIcon("resources/view-fullscreen.png")));
        // bar1.addSeparator();
        bar1.setFloatable(false);
        bar1.add(new JButton(new ImageIcon("src/main/resources/images/icons/document-new.png")));
        bar1.add(new JButton(new ImageIcon("resources/edit-clear.png")));
        bar1.add(new JButton(new ImageIcon("resources/edit-copy.png")));
        bar1.add(new JButton(new ImageIcon("resources/edit-cut.png")));
        bar1.add(new JButton(new ImageIcon("resources/edit-delete.png")));
        bar1.add(new JButton(new ImageIcon("resources/edit-paste.png")));

        // bar1.putClientProperty(SubstanceLookAndFeel.FLAT_PROPERTY,
        // false);

        titledPanel.add(bar1, BorderLayout.LINE_START);
        // scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, titledPanel3, titledPanel2);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(1);
        // splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setDividerLocation(780);
        splitPane.setContinuousLayout(true);

        JXTitledPanel titledPanelEditor = new JXTitledPanel();
        titledPanelEditor.setTitle("Plugin IDE");
        titledPanelEditor.add(new PluginEditor());
        titledPanelEditor.setBorder(compound);

        JSplitPane splitPaneGraphEditor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, titledPanel, titledPanelEditor);
        splitPaneGraphEditor.setOneTouchExpandable(true);
        splitPaneGraphEditor.setContinuousLayout(true);
        splitPaneGraphEditor.setResizeWeight(1);
        // splitPaneGraphEditor.setDividerLocation(780);

        JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPane, splitPaneGraphEditor);
        splitPane2.setOneTouchExpandable(true);
        splitPane2.setDividerLocation(256 + 32);
        splitPane2.setContinuousLayout(true);

        // scrollPane.getViewport().setViewPosition(new Point(20,20));
        // use this to drag the viewport when pressing the middle mouse in teh
        // graph view

        contentPane.add(new AtsToolBar(), BorderLayout.PAGE_START);
        // contentPane.setPreferredSize(new Dimension(650, 430));
        contentPane.add(splitPane2, BorderLayout.CENTER);
        StatusBar bar = StatusBar.getInstance();
        contentPane.add(bar, BorderLayout.PAGE_END);
        // contentPane.add(scrollPane, BorderLayout.CENTER);
        // contentPane.add(new AtsDrawPanel(preview), BorderLayout.CENTER);
        // contentPane.add(preview, BorderLayout.LINE_START);
        setContentPane(contentPane);

        ApplicationProperties.getInstance().getRecentFiles();

        AtsMenuBar.getInstance().setParent(this);

        AtsMenuBar menu = AtsMenuBar.getInstance();

        setJMenuBar(menu);
        menu.update();

        // JColorChooser.showDialog(null,"", Color.black);
        // dimension in respect to sectio aurea
        // see http://de.wikipedia.org/wiki/Goldener_Schnitt
        int a = 700;
        int b = (int) (a / 1.618033988f);
        int height = a;
        int width = a + b;

        setMinimumSize(new Dimension(320, 240));
        setPreferredSize(new Dimension(width, height));
        setLocation(ApplicationProperties.getInstance().getWindowLocation());

        // setPreferredSize(new Dimension(820, 560));
        // setMinimumSize(new Dimension(150, 100));
        pack();
        // setLocationRelativeTo(null);
        // focus has to be called after pack but before frame gets visible
        graph.setFocus();
        setVisible(true);
        JDialog.setDefaultLookAndFeelDecorated(false);
        new SplashScreen(this);
        JDialog.setDefaultLookAndFeelDecorated(true);
        // JOptionPane.showMessageDialog(this, "Wusten Sie schon, dass der Save
        // Operator es ihnen erm�glicht... \nWusten Sie schon, dass der Save
        // Operator es ihnen erm�glicht... \nWusten Sie schon, dass der Save
        // Operator es ihnen erm�glicht...",
        // "Tip of The Day", JOptionPane.INFORMATION_MESSAGE, new
        // ImageIcon("resources/dialog-information.png"));
        // JOptionPane.showInputDialog(this, "Search Operator", "Operator
        // Search", JOptionPane.PLAIN_MESSAGE ,new
        // ImageIcon("resources/system-search.png"),null,null );
        // JOptionPane.showConfirmDialog(this, "Overwriting will lead to Data
        // Loss!", "Overwrite File?", JOptionPane.YES_NO_OPTION,
        // JOptionPane.WARNING_MESSAGE, new
        // ImageIcon("resources/dialog-warning.png"));
        //
        // SwingUtilities.invokeLater(new Runnable() {
        // public void run() {
        // Color color = JColorChooser.showDialog(AtsMainWindow.this,
        // "Color chooser", new Color(23, 45, 200));
        // if (color != null) {
        // //Check.out("Chosen " + color.toString());
        // }
        // }
        // });

    }

}
