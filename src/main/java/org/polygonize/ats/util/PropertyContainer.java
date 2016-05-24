package org.polygonize.ats.util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.polygonize.ats.core.operator.AtsOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.ui.AtsOperatorGraph;
import org.polygonize.ats.core.ui.AtsOperatorGraphView;
import org.polygonize.ats.operators.misc.Load;
import org.polygonize.ats.operators.misc.Store;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class PropertyContainer {

    HashMap<String, Parameter> params_;
    Vector<String> sortedKeys_;

    PropertyChangeListener listener_;

    /*
     * The following methods are for saving data to xml
     */

    public PropertyContainer() {
        params_ = new HashMap<String, Parameter>();
        sortedKeys_ = new Vector<String>();
    }

    public void attachParam(String name, Parameter param) {
        params_.put(name, param);
        sortedKeys_.add(name);
    }

    public Vector<String> getParamNames() {
        return sortedKeys_;
    }

    public Parameter getParam(String name) {
        return params_.get(name);
    }

    /*
     * the following methods are to construct the property panel
     */

    /*
     * The Properties Panel is registered so in case of a parameter change it
     * can start a new graph evaluation
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listener_ = listener;
    }

    public JPanel getOperatorPropertyPanel(AtsOperator op) {
        FormLayout lm = new FormLayout("right:pref:grow, 2dlu, fill:150dlu");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm);
        builder.setDefaultDialogBorder();

        setup(op, builder);

        // loop throough all parameters and attach them
        for (int i = 0; i < sortedKeys_.size(); i++) {
            String paramName = sortedKeys_.get(i);
            Parameter param = getParam(paramName);

            if (param instanceof AtsInteger)
                addInteger(builder, paramName, (AtsInteger) param);
            if (param instanceof AtsEnumeration)
                addEnumeration(builder, paramName, (AtsEnumeration) param);
            if (param instanceof AtsColor)
                addColor(builder, paramName, (AtsColor) param);
            if (param instanceof AtsText)
                addText(builder, paramName, (AtsText) param);
            if (param instanceof AtsPosition2D)
                addPosition(builder, paramName, (AtsPosition2D) param);
            if (param instanceof AtsBoolean)
                addBoolean(builder, paramName, (AtsBoolean) param);
        }

        JPanel result = builder.getPanel();
        JScrollPane pane = new JScrollPane(result, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(pane);
        return panel;
    }

    public void addText(DefaultFormBuilder builder, String caption, final AtsText text_) {
        final JTextField field = new JTextField(text_.value_);

        field.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                text_.value_ = field.getText();
                listener_.propertyChanged();
            }

        });

        builder.append(caption, field);
    }

    public void addInteger(DefaultFormBuilder builder, String caption, AtsInteger integer) {
        AtsSlider slider = new AtsSlider(0, 255, integer);
        slider.addPropertyChangeListener(listener_);
        builder.append(caption, slider);
    }

    public void addPosition(DefaultFormBuilder builder, String caption, AtsPosition2D position) {
        AtsPositionSlider slider = new AtsPositionSlider(position);
        slider.addPropertyChangeListener(listener_);
        builder.append(caption, slider);
    }

    public void addColor(DefaultFormBuilder builder, String string, AtsColor color) {
        AtsColorSlider slider = new AtsColorSlider(color);
        slider.addPropertyChangeListener(listener_);
        builder.append(string, slider);
    }

    public void addBoolean(DefaultFormBuilder builder, String string, final AtsBoolean selected) {

        JCheckBox box = new JCheckBox();
        box.setSelected(selected.value_);
        // box.setHorizontalTextPosition(SwingConstants.LEFT);

        box.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                selected.value_ = cb.isSelected();
                listener_.propertyChanged();

            }

        });
        builder.append(string, box);
    }

    public void addEnumeration(DefaultFormBuilder builder, String string, final AtsEnumeration enumeration) {

        JComboBox box = new JComboBox(enumeration.value_);
        box.setSelectedIndex(enumeration.selected);
        box.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                enumeration.selected = cb.getSelectedIndex();
                listener_.propertyChanged();

            }

        });
        builder.append(string, box);
    }

    public void setup(final AtsOperator op, DefaultFormBuilder builder) {
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

                if (op instanceof Load || op instanceof Store) {

                    // if(op instanceof Load)
                    // op.setDirty(true);
                    // else // store
                    // op.setChidrenDirty(true);

                    AtsOperatorGraph.getInstance().connectOperators();

                    listener_.propertyChanged();
                }

                AtsOperatorGraphView.getInstance().render();
            }
        });

        builder.append("custom name", nameField);
        builder.appendSeparator("Operator Parameters");
    }

}
