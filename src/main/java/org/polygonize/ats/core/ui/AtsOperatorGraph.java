/*	AtsOperatorGraph.java
 * 
 * 	
 */

package org.polygonize.ats.core.ui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collections;
import java.util.Vector;

import org.polygonize.ats.core.operator.AtsOperator;
import org.polygonize.ats.operators.misc.Load;
import org.polygonize.ats.operators.misc.Store;

public class AtsOperatorGraph {

    private static AtsOperatorGraph instance_;
    StatusBar bar;

    private Vector<AtsOperator> operators_;

    private AtsOperatorGraph() {
        operators_ = new Vector<AtsOperator>();
    }

    public static AtsOperatorGraph getInstance() {
        if (instance_ == null) {
            instance_ = new AtsOperatorGraph();
        }
        return instance_;
    }

    public void draw(Graphics2D graphics2D) {
        for (int i = 0; i < operators_.size(); i++)
            operators_.get(i).draw(graphics2D);
    }

    public void setDirty() {
        for (int i = 0; i < operators_.size(); i++)
            operators_.get(i).setDirty(true);
    }

    public void deleteSelection() {
        Vector<AtsOperator> selectedOperators = new Vector<AtsOperator>();

        for (int i = 0; i < operators_.size(); i++) {
            if (operators_.get(i).isSelected())
                selectedOperators.add(operators_.get(i));
        }

        operators_.removeAll(selectedOperators);
        bar.inform(operators_.size());
    }

    public void computeIds() {
        for (int i = 0; i < operators_.size(); i++) {
            operators_.get(i).setId(i);
        }
    }

    public void connectOperators() {
        clearOutputs();

        for (int i = 0; i < operators_.size(); i++) {
            Vector<AtsOperator> inputs = findInputs(operators_.get(i));
            operators_.get(i).setInputs(inputs);

            for (AtsOperator op : inputs) {
                op.addOutput(operators_.get(i));
            }
        }
        // sort for zapping through ops
        Collections.sort(operators_);

        // connect load store oprs
        for (int i = 0; i < operators_.size(); i++) {
            AtsOperator myop = operators_.get(i);
            if (myop instanceof Load) {
                String name = myop.getName();
                System.out.println("Load: " + name);

                Vector<AtsOperator> inputs = new Vector<AtsOperator>();
                for (AtsOperator op : operators_) {
                    if (op instanceof Store && op.getName().equals(name)) {
                        inputs.add(op);
                        System.out.println("input: " + op.getName());
                        // break; // only take first store op with same name!
                    }
                }
                myop.setInputs(inputs);

                for (AtsOperator op : inputs) {
                    op.addOutput(myop);
                }
            }
        }
    }

    public void clearOutputs() {
        for (int i = 0; i < operators_.size(); i++) {
            operators_.get(i).clearOutput();
        }
    }

    public Vector<AtsOperator> findInputs(AtsOperator operator) {
        Vector<AtsOperator> inputs = new Vector<AtsOperator>();

        for (int i = 0; i < operators_.size(); i++) {
            if (operator.isOverOp(operators_.get(i))) {
                inputs.add(operators_.get(i));

            }
        }

        Collections.sort(inputs);

        return inputs;
    }

    public AtsOperator getOperatorAtMousePosition(Point point) {
        for (int i = 0; i < operators_.size(); i++) {
            if (operators_.get(i).contains(point))
                return operators_.get(i);
        }
        return null;
    }

    public boolean intersects(AtsOperator operator) {
        for (int i = 0; i < operators_.size(); i++) {
            if (operators_.get(i).intersects(operator) && !(operator == operators_.get(i)))
                return true;
        }
        return false;
    }

    public boolean intersects(AtsOperator operator, FocusRect focusRect) {
        for (int i = 0; i < operators_.size(); i++) {
            if (operators_.get(i).intersects(focusRect) && !(operator == operators_.get(i)))
                return true;
        }
        return false;
    }

    public boolean intersectsDrag(GroupSelection selection) {

        for (int i = 0; i < operators_.size(); i++) {
            if (!operators_.get(i).isSelected()) {
                if (selection.intersects(operators_.get(i)))
                    return true;
            }

        }
        return false;
    }

    public boolean intersectsCopy(GroupSelection selection) {

        for (int i = 0; i < operators_.size(); i++) {

            if (selection.intersects(operators_.get(i)))
                return true;

        }
        return false;
    }

    public boolean selectRubberBand(RubberBand rubber, boolean toggle) {
        for (int i = 0; i < operators_.size(); i++) {
            if (operators_.get(i).intersects(rubber))
                if (toggle)
                    operators_.get(i).setSelected(!operators_.get(i).isSelected());
                else
                    operators_.get(i).setSelected(true);
        }
        return false;
    }

    public boolean isPointInDragArea(Point point) {
        for (int i = 0; i < operators_.size(); i++) {
            if (operators_.get(i).isPointInDragArea(point))
                return true;
        }
        return false;
    }

    public boolean isPointInResizeArea(Point point) {
        for (int i = 0; i < operators_.size(); i++) {
            if (operators_.get(i).isPointInResizeArea(point))
                return true;
        }
        return false;
    }

    public void selectOperatorAtPosition(Point point) {
        for (int i = 0; i < operators_.size(); i++) {
            if (operators_.get(i).contains(point))
                operators_.get(i).setSelected(true);
            else
                operators_.get(i).setSelected(false);
        }
    }

    public void activateOperator() {
        AtsOperator operator = getSelected().get(0);

        for (int i = 0; i < operators_.size(); i++) {
            if (operators_.get(i) == operator)
                operators_.get(i).setActiveView(true);
            else
                operators_.get(i).setActiveView(false);
        }
    }

    public AtsOperator getActive() {
        for (int i = 0; i < operators_.size(); i++) {
            if (operators_.get(i).isActive())
                return operators_.get(i);
        }

        return null;
    }

    public Vector<AtsOperator> getSelected() {
        Vector<AtsOperator> ops = new Vector<AtsOperator>();
        for (int i = 0; i < operators_.size(); i++) {
            if (operators_.get(i).isSelected())
                ops.add(operators_.get(i));
        }

        return ops;
    }

    public void select(AtsOperator operator) {
        // AtsOperator op = null;
        //
        // if(getSelected().size() >0)
        // op = getSelected().get(0);
        //
        // if(op != null)
        // op.setSelected(false);
        deselectAll();

        if (operator != null)
            operator.setSelected(true);
    }

    public void selectNext() {
        AtsOperator op = getSelected().get(0);

        if (op != null) {
            op.setSelected(false);
            int index = operators_.indexOf(op);
            int newIndex = (index + 1) % operators_.size();
            operators_.get(newIndex).setSelected(true);
        }
    }

    public Vector<AtsOperator> getOperators() {
        return operators_;
    }

    public void clear() {
        operators_.clear();
    }

    public void add(AtsOperator operator) {
        operators_.add(operator);
        bar.inform(operators_.size());
    }

    public void addListener(StatusBar statusBar) {
        bar = statusBar;
        bar.inform(0);

    }

    public void deselectAll() {
        for (int i = 0; i < operators_.size(); i++)
            operators_.get(i).setSelected(false);
    }

}
