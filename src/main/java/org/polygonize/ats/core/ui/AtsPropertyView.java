package org.polygonize.ats.core.ui;

import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import org.polygonize.ats.core.comment.AtsComment;
import org.polygonize.ats.core.operator.AtsOperator;
import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.operators.AtsTextureOperator;
import org.polygonize.ats.util.AtsPropertyContainer;
import org.polygonize.ats.util.PropertyChangeListener;
import org.polygonize.ats.util.PropertyContainer;

public class AtsPropertyView extends JPanel implements PropertyChangeListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    AtsTexturePreview texture_;

    JPanel panel = new JPanel();
    JScrollPane scrollPane;
    AtsOperator operator_, propertyPanelOp;

    private static AtsPropertyView instance_;

    private AtsPropertyView() {
        super(true);
        texture_ = AtsTexturePreview.getInstance();
        // setBackground(new Color(255, 74, 59));
        // setPreferredSize(new Dimension(256+0,256+0));
        setLayout(new BorderLayout());
        // setMaximumSize(new Dimension(300,200));
        // setMinimumSize(new Dimension(300,200));
        // setBackground(new Color(255, 74, 59));

        // setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        add(BorderLayout.CENTER, panel);
        removePropertyPanel();

    }

    public static AtsPropertyView getInstance() {
        if (instance_ == null) {
            instance_ = new AtsPropertyView();
        }
        return instance_;
    }

    // setzt aktiven operator fuer die preview
    public void setOperator(AtsOperator operator) {

        System.arraycopy(operator.getTexture().texels_, 0, texture_.pixels, 0, texture_.pixels.length);

        AtsProceduralTexture tex = new AtsProceduralTexture();
        tex.clear(127, 127, 255, 255);

        System.arraycopy(tex.texels_, 0, texture_.pixels2, 0, texture_.pixels2.length);

        if (operator instanceof AtsTextureOperator) {
            AtsProceduralTexture normal = ((AtsTextureOperator) operator).normalMap;
            System.arraycopy(normal.texels_, 0, texture_.pixels2, 0, texture_.pixels2.length);
        }

        operator_ = operator;
        /*
         * add property panel here!!!!!
         *
         */
        texture_.updateTexture();
        AtsTexturePreview.getInstance().setMissingInputs(false);
    }

    public void removePreviewOp() {
        // texture is still drawn but alpha is set to zero
        Arrays.fill(texture_.pixels, 0x00000000);
        operator_ = null;
        texture_.updateTexture();

    }

    public void removePropertyPanel() {
        remove(panel);
        // remove(scrollPane);
        panel = new JPanel(new BorderLayout());
        JScrollPane pane = new JScrollPane(null, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(pane);

        add(panel, BorderLayout.CENTER);
        propertyPanelOp = null;
        validate(); // nur validate??
    }

    // updated aktiven operator fuer die preview
    public void updatePreview(AtsOperator operator) {
        System.arraycopy(operator.getTexture().texels_, 0, texture_.pixels, 0, texture_.pixels.length);

        if (operator instanceof AtsTextureOperator) {
            AtsProceduralTexture normal = ((AtsTextureOperator) operator).normalMap;
            System.arraycopy(normal.texels_, 0, texture_.pixels2, 0, texture_.pixels2.length);
        }

        texture_.updateTexture();

        if (operator instanceof AtsTextureOperator)
            AtsTexturePreview.getInstance().setTextureMode(true);
    }

    // setzt selektierten operator fuer das panel
    public void setPropertyPanel(AtsOperator operator) {
        /*
         * propertyPanelOp = operator; AtsPropertyContainer container = new
         * AtsPropertyContainer(); container.setup(operator);
         * container.addPropertyChangeListener(this); operator.edit(container);
         * remove(panel); panel = container.getPanel(); add(panel,
         * BorderLayout.CENTER); validate(); // nur validate??
         */

        propertyPanelOp = operator;
        PropertyContainer propertyContainer = new PropertyContainer();
        propertyContainer.addPropertyChangeListener(this);
        operator.edit(propertyContainer);
        remove(panel);
        panel = propertyContainer.getOperatorPropertyPanel(operator);
        add(panel, BorderLayout.CENTER);
        validate();

    }

    public void setPropertyPanel(AtsComment comment) {
        AtsPropertyContainer container = new AtsPropertyContainer();
        container.addPropertyChangeListener(this);
        comment.edit(container);
        remove(panel);
        panel = container.getPanel();
        add(panel, BorderLayout.CENTER);
        validate(); // nur validate??
    }

    boolean dirty = false;
    boolean done = true;
    GraphEvaluation evaluationThread = null;// new GraphEvaluation();

    // still bugy with the recalc after set to dirty!!!
    public void propertyChanged() {
        // System.out.println(evaluationThread.getState());
        if (operator_ != null) {
            System.out.println("op not equal zero");
            if (done) {
                done = false;
                StatusBar.getInstance().setBusy(true);

                if (operator_.allInputsAccepted()) {
                    System.out.println("all inputs accepted");
                    AtsTexturePreview.getInstance().setMissingInputs(false);
                    evaluationThread = new GraphEvaluation();
                    evaluationThread.execute();
                } else {
                    System.out.println("Still missing!");
                    AtsTexturePreview.getInstance().setMissingInputs(true);
                    done = true;
                }

                // System.out.println(System.currentTimeMillis() - startTime);
            } else {

                if (operator_.allInputsAccepted()) {
                    AtsTexturePreview.getInstance().setMissingInputs(false);
                    dirty = true;
                } else {
                    AtsTexturePreview.getInstance().setMissingInputs(true);
                    done = true;
                }

            }
        }
    }

    class GraphEvaluation extends SwingWorker<Void, Void> {

        long elapsedTime;

        protected Void doInBackground() {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            long startTime = System.currentTimeMillis();

            // AtsOperatorGraph.getInstance().setDirty();
            propertyPanelOp.setDirty(true);

            operator_.evaluate();
            elapsedTime = System.currentTimeMillis() - startTime;
            System.arraycopy(operator_.getTexture().texels_, 0, texture_.pixels, 0, texture_.pixels.length);

            if (operator_ instanceof AtsTextureOperator) {
                AtsProceduralTexture normal = ((AtsTextureOperator) operator_).normalMap;
                System.arraycopy(normal.texels_, 0, texture_.pixels2, 0, texture_.pixels2.length);
            }

            // texture_.paintImmediately(0, 0, texture_.getSize().width,
            // texture_.getSize().height);

            // done=true;

            return null;
        }

        synchronized protected void done() {

            // System.out.println("painted.");
            StatusBar.getInstance().setElapsedTime(elapsedTime);
            texture_.updateTexture();

            if (dirty) {
                evaluationThread = new GraphEvaluation();
                evaluationThread.execute();
                dirty = false;
            } else {
                done = true;
                StatusBar.getInstance().setBusy(false);
            }

        }

    }

}
