package org.polygonize.ats.core.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXStatusBar;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

public class StatusBar extends JXStatusBar {

    private static final long serialVersionUID = 1337L;
    private JLabel label;
    private JLabel time;
    private static StatusBar instance_;
    JXBusyLabel label2;

    public void inform(int opsCount) {
        label.setText("Operator Count: " + opsCount);
    }

    public static StatusBar getInstance() {
        if (instance_ == null) {
            instance_ = new StatusBar();
        }
        return instance_;
    }

    private StatusBar() {

        label = new JLabel("1.0.0 RC Cytherea [built on 2009-11-01]");

        JXStatusBar.Constraint cStatusLabel = new JXStatusBar.Constraint();
        cStatusLabel.setFixedWidth(label.getWidth());
        setPreferredSize(new Dimension(10, 31));
        add(label, cStatusLabel);

        label = new JLabel("statusBar.text");
        JXStatusBar.Constraint c2 = new JXStatusBar.Constraint();// new
                                                                 // JXStatusBar.Constraint(
        c2.setFixedWidth(115); // JXStatusBar.Constraint.ResizeBehavior.FILL);
        add(label, c2);

        AtsOperatorGraph.getInstance().addListener(this);

        // tabp.getModel().addChangeListener(new ChangeListener() {
        // public void stateChanged(ChangeEvent e) {
        // int selectedIndex = tabp.getSelectedIndex();
        // if (selectedIndex < 0)
        // label.setText("No selected tab");
        // else
        // label.setText("Tab "
        // + tabp.getTitleAt(selectedIndex)
        // + " selected");
        // }
        // });

        JPanel alphaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        final JLabel alphaLabel = new JLabel("100%");
        final JSlider alphaSlider = new JSlider(0, 100, 100);
        alphaSlider.setFocusable(false);
        alphaSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int currValue = alphaSlider.getValue();
                alphaLabel.setText(currValue + "%");
            }

        });

        alphaSlider.setToolTipText("Changes the global opacity. Is not Substance-specific");
        alphaSlider.setPreferredSize(new Dimension(120, alphaSlider.getPreferredSize().height));

        JXStatusBar.Constraint alphaPanelConstraints = new JXStatusBar.Constraint();
        alphaPanelConstraints.setFixedWidth(160);

        label2 = new JXBusyLabel(new Dimension(20, 20)); // createComplexBusyLabel();
        label2.getBusyPainter().setBaseColor(Color.GRAY);
        label2.getBusyPainter().setHighlightColor(new Color(0xF4A817));
        label2.setEnabled(true);
        label2.setBusy(false);
        // JLabel label = new JLabel("ss");
        // label.setForeground(Color.RED);
        label2.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, new Double(0.0));
        label2.repaint();

        label2.setDelay(100);

        time = new JLabel();

        JXStatusBar.Constraint c3 = new JXStatusBar.Constraint();// new
                                                                 // JXStatusBar.Constraint(
        c3.setFixedWidth(200);

        // add(new JLabel(new ImageIcon("resources/image-loading.png")), c3);
        add(time, c3);
        setElapsedTime(0);
        // JProgressBar progbar = new JProgressBar();
        // progbar.setValue(55);
        // add(progbar);

    }

    public void setText(String name) {
        label.setText(name);
    }

    public void setBusy(boolean busy) {
        label2.setBusy(busy);
    }

    public void setElapsedTime(long elapsedTime) {
        time.setText("Evaluation Time: " + elapsedTime + " ms");

    }

}
