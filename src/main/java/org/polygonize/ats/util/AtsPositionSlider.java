package org.polygonize.ats.util;

import java.awt.GridLayout;

import javax.swing.JPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class AtsPositionSlider extends JPanel implements PropertyChangeListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    AtsPosition2D position;
    PropertyChangeListener listener_;

    public AtsPositionSlider(AtsPosition2D position) {
        this.position = position;

        FormLayout lm = new FormLayout("pref:grow, 1px, pref:grow", // columns
                "p");
        // new FormLayout("pref:grow, 2dlu, pref:grow, 2dlu, pref:grow, 2dlu,
        // pref:grow", "");
        PanelBuilder builder = new PanelBuilder(lm);
        // builder.setDefaultDialogBorder();

        setLayout(new GridLayout(1, 0));
        AtsSlider x = new AtsSlider(0, 255, position.getX());
        AtsSlider y = new AtsSlider(0, 255, position.getY());

        x.addPropertyChangeListener(this);
        y.addPropertyChangeListener(this);

        CellConstraints cc = new CellConstraints();

        builder.add(x, cc.xy(1, 1));
        builder.add(y, cc.xy(3, 1));

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