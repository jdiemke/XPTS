package org.polygonize.ats.core.ui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import org.polygonize.ats.core.comment.AtsComment;

public class AtsCommentHolder {

    Vector<AtsComment> comments_;
    private static AtsCommentHolder instance_;

    public Vector<AtsComment> getComments() {
        return comments_;
    }

    public static AtsCommentHolder getInstance() {
        if (instance_ == null) {
            instance_ = new AtsCommentHolder();
        }
        return instance_;
    }

    private AtsCommentHolder() {
        comments_ = new Vector<AtsComment>();
    }

    public void add(AtsComment comment) {
        comments_.add(comment);
    }

    public void draw(Graphics2D g2) {
        for (int i = 0; i < comments_.size(); i++)
            comments_.get(i).draw(g2);
    }

    public boolean isPointInDragArea(Point point) {
        for (int i = 0; i < comments_.size(); i++) {
            if (comments_.get(i).isPointInDragArea(point))
                return true;
        }
        return false;
    }

    public AtsComment getCommentAtMousePosition(Point point) {
        for (int i = comments_.size() - 1; i >= 0; i--) {
            // for (int i=0; i < comments_.size(); i++) {
            if (comments_.get(i).isPointInDragArea(point))
                return comments_.get(i);
        }
        return null;
    }

    public void deleteSelection() {
        Vector<AtsComment> selectedComments = new Vector<AtsComment>();

        for (int i = 0; i < comments_.size(); i++) {
            if (comments_.get(i).isSelected())
                selectedComments.add(comments_.get(i));
        }

        comments_.removeAll(selectedComments);
    }

    // TODO: this selectds more comments if they are all at the
    // mouse pos FIXME!!!
    public void selectCommentAtPosition(Point point) {
        for (int i = comments_.size() - 1; i >= 0; i--) {
            // for (int i=0; i < comments_.size(); i++) {
            if (comments_.get(i).isPointInDragArea(point)) {
                comments_.get(i).setSelected(true);
                return;
            }

        }
    }

    // FIXME: if selectCOmmentAtPosition is fixed this doesnt need to run
    // from back to front!!
    public AtsComment getSelected() {
        for (int i = comments_.size() - 1; i >= 0; i--) {
            // for (int i=0; i < comments_.size(); i++) {
            if (comments_.get(i).isSelected())
                return comments_.get(i);
        }

        return null;
    }

    public void clear() {
        comments_.clear();
    }

    public boolean isPointInResizeArea(Point point) {
        for (int i = 0; i < comments_.size(); i++) {
            if (comments_.get(i).isPointInResizeArea(point))
                return true;
        }
        return false;
    }

}
