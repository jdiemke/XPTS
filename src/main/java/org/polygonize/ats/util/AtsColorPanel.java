package org.polygonize.ats.util;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AtsColorPanel extends JPanel {

    AtsColor color_;

    public AtsColorPanel(String caption, AtsColor color) {
        setLayout(new GridLayout(1, 2));
        add(new JLabel(caption));
        color_ = color;

        this.setBorder(BorderFactory.createEmptyBorder(2, 2, 0, 3));

    }
}
