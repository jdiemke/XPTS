package org.polygonize.ats.util;

import java.awt.GridLayout;

import javax.swing.JPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class AtsColorSlider extends JPanel implements PropertyChangeListener {

    AtsColor color;
    PropertyChangeListener listener_;

    public AtsColorSlider(AtsColor color) {
        this.color = color;

        FormLayout lm = new FormLayout("pref:grow, 1px, pref:grow, 1px, pref:grow, 1px, pref:grow", // columns
                "p");
        // new FormLayout("pref:grow, 2dlu, pref:grow, 2dlu, pref:grow, 2dlu,
        // pref:grow", "");
        PanelBuilder builder = new PanelBuilder(lm);
        // builder.setDefaultDialogBorder();

        setLayout(new GridLayout(1, 0));
        AtsSlider red = new AtsSlider(0, 255, color.getRed());
        AtsSlider green = new AtsSlider(0, 255, color.getGreen());
        AtsSlider blue = new AtsSlider(0, 255, color.getBlue());
        AtsSlider alpha = new AtsSlider(0, 255, color.getAlpha());

        red.addPropertyChangeListener(this);
        green.addPropertyChangeListener(this);
        blue.addPropertyChangeListener(this);
        alpha.addPropertyChangeListener(this);

        CellConstraints cc = new CellConstraints();

        builder.add(red, cc.xy(1, 1));
        builder.add(green, cc.xy(3, 1));
        builder.add(blue, cc.xy(5, 1));
        builder.add(alpha, cc.xy(7, 1));

        add(builder.getPanel());
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listener_ = listener;
    }

    @Override
    public void propertyChanged() {
        if (listener_ != null)
            listener_.propertyChanged();
    }

}