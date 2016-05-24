package org.polygonize.ats.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.polygonize.ats.core.operator.AtsOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.ui.AtsOperatorGraphView;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class AtsPropertyContainer implements PropertyChangeListener, KeyListener {

    PropertyChangeListener listener_;
    AtsText text;
    JTextArea tf;
    DefaultFormBuilder builder;

    public AtsPropertyContainer() {

        FormLayout lm = new FormLayout("right:pref:grow, 2dlu, fill:150dlu");
        builder = new DefaultFormBuilder(lm);// , new FormDebugPanel());
        builder.setDefaultDialogBorder();
    }

    public void setup(final AtsOperator op) {
        JTextField fiedl = new JTextField("stuff");
        fiedl.setEditable(false);

        AtsOperatorMetadata info = op.getClass().getAnnotation(AtsOperatorMetadata.class);
        String name = op.getClass().getCanonicalName();

        if (info != null) {
            name = info.operatorDesignation();
        }

        fiedl.setText(name);
        fiedl.putClientProperty(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS, Boolean.TRUE);
        // builder.appendSeparator("Metadata");
        builder.appendSeparator("Basic Properties");
        builder.append("type", fiedl);

        final JTextField nameField = new JTextField(op.getName());

        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                op.setName(nameField.getText());
                AtsOperatorGraphView.getInstance().render();
            }
        });

        builder.append("custom name", nameField);
        builder.appendSeparator("Operator Parameters");
    }

    /*
     * The Properties Panel is registered so in case of a parameter change it
     * can start a new graph evaluation
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listener_ = listener;
    }

    public void attachParam(String caption, AtsInteger integer) {
        AtsSlider slider = new AtsSlider(0, 255, integer);
        slider.addPropertyChangeListener(this);

        builder.append(caption, slider);
    }

    public JPanel getOperatorPropertyPanel() {
        return null;
    }

    public JPanel getPanel() {

        JPanel result = builder.getPanel();
        JScrollPane pane = new JScrollPane(result, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(pane);

        panel.setPreferredSize(new Dimension(result.getPreferredSize().width, 0));

        return panel;
    }

    @Override
    public void propertyChanged() {
        listener_.propertyChanged();

    }

    public void attachParam(String string, AtsText text_) {
        tf = new JTextArea();
        tf.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tf.setMargin(new Insets(5, 5, 5, 5));
        tf.setLineWrap(true);
        tf.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(tf, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(new Dimension(250, 110));

        tf.addKeyListener(this);
        text = text_;
        tf.setText(text.value_);

        // builder.appendSeparator("Basic Properties");
        // builder.append("type", new JTextField("comment"));
        // builder.append("custom name", new JTextField(""));
        builder.appendSeparator("Comment Parameters");
        builder.append("caption", new JTextField("IMPORTANT"));
        builder.append("color", new JTextField("WARNING"));
        builder.append("", new JLabel());
        CellConstraints cc = new CellConstraints();
        builder.add(new JLabel("comment"), cc.xy(1, 7, "right, top, center"));
        builder.add(sp, cc.xy(3, 7, "fill, top"));

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        text.value_ = tf.getText();
        AtsOperatorGraphView.getInstance().render();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void attachParam(String string, final AtsEnumeration enumeration) {

        JComboBox box = new JComboBox(enumeration.value_);
        box.setSelectedIndex(enumeration.selected);
        box.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                enumeration.selected = cb.getSelectedIndex();
                propertyChanged();

            }

        });
        builder.append(string, box);
    }

    public void attachParam(String string, AtsColor color) {
        AtsColorSlider slider = new AtsColorSlider(color);
        slider.addPropertyChangeListener(this);
        builder.append(string, slider);
    }

}
