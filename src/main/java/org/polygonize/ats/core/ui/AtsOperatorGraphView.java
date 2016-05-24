package org.polygonize.ats.core.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.CubicCurve2D;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;

import org.polygonize.ats.ApplicationController;
import org.polygonize.ats.core.comment.AtsComment;
import org.polygonize.ats.core.operator.AtsDataSourceOperator;
import org.polygonize.ats.core.operator.AtsOperator;
import org.polygonize.ats.core.operator.AtsOperatorMetadata;
import org.polygonize.ats.core.plugin.AtsPlugInSystem;
import org.polygonize.ats.operators.misc.Store;

public class AtsOperatorGraphView extends JPanel
        implements MouseListener, MouseMotionListener, KeyListener, ActionListener, FocusListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // the operator graph displayed by this component
    public AtsOperatorGraph atsOperatorGraph_;
    public AtsCommentHolder commentHolder_;
    public AtsPropertyView atsTexturePreview;

    // aktueller zustand des statecharts
    private AtsOperatorGraphViewState currentState_;
    // automata variables
    public Point dragOffset = new Point();
    int commentDragOffsetX_;
    int commentDragOffsetY_;
    // public AtsOperator selectedOperator_;
    public FocusRect focusRect_ = new FocusRect();
    public GroupSelection selection = new GroupSelection();
    public RubberBand rubberBand = new RubberBand();
    AtsComment comment_;

    private static AtsOperatorGraphView instance_;

    public void clearComments() {
        commentHolder_.clear();
    }

    private AtsOperatorGraphView() {
        setDoubleBuffered(true);
        atsOperatorGraph_ = AtsOperatorGraph.getInstance();
        atsTexturePreview = AtsPropertyView.getInstance();
        commentHolder_ = AtsCommentHolder.getInstance();
        setPreferredSize(new Dimension(16 * 100, 16 * 100));
        // setSize(new Dimension(16*100,16*100));
        // setSize(new Dimension(300,300));
        // setBackground(new Color(79, 74, 59));
        // setBackground(new Color((int)(79*0.7),(int)( 74*0.7),(int)(
        // 59*0.7)));

        // register listeners for user input
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setEnabled(true);
        addKeyListener(this);

        // set the initial state
        setState(new InitialState(this));
        // commentHolder_.add(new AtsComment());

        addFocusListener(this);
    }

    public void setFocus() {
        requestFocusInWindow();
    }

    public static AtsOperatorGraphView getInstance() {
        if (instance_ == null) {
            instance_ = new AtsOperatorGraphView();
        }
        return instance_;
    }

    Color brightGridColor = new Color(64, 62, 56);
    Color darkGridColor = new Color(52, 51, 46);
    BasicStroke gridStroke = new BasicStroke(1.2f);

    void drawGrid(Graphics2D g2) {
        int width = getWidth();
        int height = getHeight();

        g2.setStroke(gridStroke);
        g2.setColor(brightGridColor);

        for (int i = 0; i < height; i += 16) {
            g2.drawLine(0, i, width, i);
        }

        for (int i = 0; i < width; i += 16) {
            g2.drawLine(i, 0, i, height);
        }

        g2.setColor(darkGridColor);

        for (int i = 0; i < height; i += 16 * 4) {
            g2.drawLine(0, i, width, i);
        }

        for (int i = 0; i < width; i += 16 * 4) {
            g2.drawLine(i, 0, i, height);
        }
    }

    CubicCurve2D connectionCurve = new CubicCurve2D.Float();
    BasicStroke outerStroke = new BasicStroke(4.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    BasicStroke innerStroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    Color innerColor = Color.getHSBColor(83 / 369.f, 0.4f, 0.69f);// Color.getHSBColor(80/360.f,
                                                                  // 0.8f, 0.99f
                                                                  // );
    Color outerColor = Color.BLACK;

    private void drawSplines(Graphics2D graphics2D) {
        Vector<AtsOperator> ops = atsOperatorGraph_.getOperators();

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < ops.size(); i++) {

            AtsOperator op = ops.get(i);

            if (op instanceof Store) {
                float x = op.getX() + op.getWidth() / 2.0f;
                float y = op.getY() + 16;

                for (int j = 0; j < op.getOutput().size(); j++) {
                    AtsOperator op2 = op.getOutput().get(j);

                    float x2 = op2.getX() + op2.getWidth() / 2.0f;
                    float y2 = op2.getY();

                    connectionCurve.setCurve(x, y, x, y + 16 * 5, x2, y2 - 16 * 5, x2, y2);

                    graphics2D.setColor(outerColor);
                    graphics2D.setStroke(outerStroke);
                    graphics2D.draw(connectionCurve);

                    graphics2D.setColor(innerColor);
                    graphics2D.setStroke(innerStroke);
                    graphics2D.draw(connectionCurve);

                }
            }

        }
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;

        drawGrid(graphics2D);

        commentHolder_.draw(graphics2D);

        atsOperatorGraph_.draw(graphics2D);

        // drawSplines(graphics2D);

        focusRect_.draw(graphics2D);
        rubberBand.draw(graphics2D);
        selection.draw(graphics2D);
    }

    public void setState(AtsOperatorGraphViewState atsOperatorGraphViewState) {
        currentState_ = atsOperatorGraphViewState;
        // debug info: shows the state every state change
        System.out.println("state change to: " + currentState_);
    }

    public FocusRect getFocusRect() {
        return focusRect_;
    }

    // Hier fangen die die m�glichen Events an! die
    // in dem Statechart auftreten k�nnen
    @Override
    public void mouseClicked(MouseEvent e) {
        currentState_.mouseClicked(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    Point point;
    boolean dragView = false;

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON2) {
            point = e.getPoint();
            dragView = true;
            setCursor(new Cursor(Cursor.MOVE_CURSOR));

        }

        // TODO: this was just put in here for linux support!!!
        // probably not a good location. verify later!

        // quick fix so a right clikc doesnt change to workspace clicked state
        if (!checkPopupMenu(e))
            currentState_.mousePressed(e);
        setFocus();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (dragView) {
            dragView = !dragView;
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            return;
        }
        checkPopupMenu(e);
        currentState_.mouseReleased(e);

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if (dragView) {

            Point p = ((JViewport) getParent()).getViewPosition();
            int newX = p.x - (e.getX() - point.x);
            int newY = p.y - (e.getY() - point.y);

            int maxX = this.getWidth() - ((JViewport) getParent()).getWidth();
            int maxY = this.getHeight() - ((JViewport) getParent()).getHeight();
            if (newX < 0)
                newX = 0;
            if (newX > maxX)
                newX = maxX;
            if (newY < 0)
                newY = 0;
            if (newY > maxY)
                newY = maxY;
            ((JViewport) getParent()).setViewPosition(new Point(newX, newY));

            // scrollRectToVisible(new Rectangle(x,y,100,100));
        }
        currentState_.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentState_.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentState_.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // hier fangen die conditions an
    //
    public boolean mouseInDragArea(MouseEvent e) {
        return atsOperatorGraph_.isPointInDragArea(e.getPoint());
    }

    public boolean mouseInCommentDragArea(MouseEvent e) {
        return commentHolder_.isPointInDragArea(e.getPoint());
    }

    public boolean mouseInResizeArea(MouseEvent e) {
        return atsOperatorGraph_.isPointInResizeArea(e.getPoint());
    }

    public boolean mouseInCommentResizeArea(MouseEvent e) {
        return commentHolder_.isPointInResizeArea(e.getPoint());
    }

    public void deleteSelection() {
        Vector<AtsOperator> selection = atsOperatorGraph_.getSelected();

        for (AtsOperator op : selection) {
            op.setChidrenDirty(true);
            if (op.isActive())
                atsTexturePreview.removePreviewOp();
        }

        // if(operator != null && operator.isActive()) {
        // atsTexturePreview.removePreviewOp();
        // operator.setChidrenDirty(true);
        // }

        commentHolder_.deleteSelection();
        atsOperatorGraph_.deleteSelection();

        atsTexturePreview.removePropertyPanel();
        atsOperatorGraph_.connectOperators();
        AtsOperator operator = atsOperatorGraph_.getActive();
        if (operator != null) {

            operator.evaluate();
        } else {
            AtsTexturePreview.getInstance().setMissingInputs(false);
        }

    }

    public void render() {
        // revalidate();
        repaint();
    }

    public AtsOperator getOperatorAtMousePosition(Point point) {
        return atsOperatorGraph_.getOperatorAtMousePosition(point);
    }

    public AtsComment getCommentAtMousePosition(Point point) {
        return commentHolder_.getCommentAtMousePosition(point);
    }

    public void selectOperator(Point point) {
        atsOperatorGraph_.selectOperatorAtPosition(point);
        AtsOperator operator = atsOperatorGraph_.getSelected().get(0);
        atsTexturePreview.setPropertyPanel(operator);
        /////////////////////////////////////// atsTexturePreview.
    }

    public void toggleOperatorSelection(Point point) {
        AtsOperator op = atsOperatorGraph_.getOperatorAtMousePosition(point);
        if (op != null)
            op.setSelected(!op.isSelected());
    }

    public void selectComment(Point point) {
        commentHolder_.selectCommentAtPosition(point);
        AtsComment comment = commentHolder_.getSelected();
        atsTexturePreview.setPropertyPanel(comment);

    }

    public void selectNext() {
        atsOperatorGraph_.selectNext();
        AtsOperator operator = atsOperatorGraph_.getSelected().get(0);
        atsTexturePreview.setPropertyPanel(operator);
    }

    public void activateSelectedOperator() {
        atsOperatorGraph_.activateOperator();
        AtsOperator operator = atsOperatorGraph_.getActive();
        if (operator != null) {
            // atsOperatorGraph_.connectOperators();
            // AtsOperatorGraph.getInstance().setDirty();
            operator.evaluate();
            atsTexturePreview.setOperator(operator);
        }
    }

    public void evaluateGraph() {

        atsOperatorGraph_.connectOperators();

        AtsOperator operator = atsOperatorGraph_.getActive();
        if (operator != null) {
            // AtsOperatorGraph.getInstance().setDirty();
            AtsOperator op = atsOperatorGraph_.getSelected().get(0);

            // falls es sich um einen generator handelt muss er selbst nicht
            // dirty gesetzt werden

            if (op instanceof AtsDataSourceOperator)
                op.setChidrenDirty(true);
            else
                op.setDirty(true);

            if (operator.allInputsAccepted()) {
                operator.evaluate();
                atsTexturePreview.updatePreview(operator);
                AtsTexturePreview.getInstance().setMissingInputs(false);
            } else {
                AtsTexturePreview.getInstance().setMissingInputs(true);

            }

        }
    }

    public void storeDragOffset(AtsOperator operator, Point point) {
        dragOffset.x = (point.x) / 16 * 16;
        dragOffset.y = (point.y) / 16 * 16;
    }

    public void storeCommentDragOffset(AtsComment comment, Point point) {
        commentDragOffsetX_ = (point.x - comment.getX()) / 16;
        commentDragOffsetY_ = (point.y - comment.getY()) / 16;
    }

    public void setFocusRectWidht(Point point) {
        int newSize = (point.x / 16 - (focusRect_.x_ / 16 - 1));
        if (newSize < 4)
            newSize = 4;
        newSize *= 16;
        focusRect_.setWidth(newSize);
        render();
    }

    public void setFocusRectWidhtHeight(Point point) {
        int newSize = (point.x / 16 - (focusRect_.x_ / 16 - 1));
        if (newSize < 3)
            newSize = 3;
        newSize *= 16;

        int newSize2 = (point.y / 16 - (focusRect_.y_ / 16 - 1));
        if (newSize2 < 3)
            newSize2 = 3;
        newSize2 *= 16;

        focusRect_.setWidth(newSize);
        focusRect_.setHeight(newSize2);
        render();
    }

    // this method is only for dragging and drag copy of ops not for resize
    public boolean setOperatorPosition(boolean copyOperator) {

        if (copyOperator) {
            if (!atsOperatorGraph_.intersectsCopy(selection)) {
                Vector<AtsOperator> selection = this.selection.getSelection();

                for (AtsOperator op : selection) {
                    AtsOperator newOp = op.copyOperator();
                    newOp.setPosition(op.getX() + this.selection.offset.x, op.getY() + this.selection.offset.y);
                    atsOperatorGraph_.add(newOp);
                }
                return true;
            }
        } else {
            if (!atsOperatorGraph_.intersectsDrag(selection)) {
                selection.computeNewPos();
                return true;
            }
        }

        return false;
    }
    //
    // // this method is only for dragging and drag copy of ops not for resize
    // public boolean setOperatorPosition(boolean copyOperator) {
    //
    //
    //
    ////
    // if(!atsOperatorGraph_.intersectsDrag(selection)) {
    // if(copyOperator) {
    //
    // Vector<AtsOperator> selection = this.selection.getSelection();
    //
    // for(AtsOperator op : selection) {
    // AtsOperator newOp = op.copyOperator();
    // newOp.setPosition(op.getX()+this.selection.offset.x,op.getY()+this.selection.offset.y);
    // atsOperatorGraph_.add(newOp);
    // }
    //
    ////
    // } else {
    // selection.computeNewPos();
    // }
    // return true;
    // }
    //
    // return false;
    // }

    public void setCommentPosition() {

        AtsComment comment = commentHolder_.getSelected();

        comment.setBounds(focusRect_);

    }

    public Vector<AtsOperator> getSelectedOperator() {
        return atsOperatorGraph_.getSelected();
    }

    public AtsComment getSelectedComment() {
        return commentHolder_.getSelected();
    }

    public void dragFocusRectPosition(Point point, FocusRect focusRect) {
        int newx = (point.x / 16) * 16 - dragOffset.x;
        int newy = (point.y / 16 * 16) - dragOffset.y;

        // if(newx < 0)newx = 0;
        // if(newy < 0)newy = 0;

        focusRect.x_ = newx;
        focusRect.y_ = newy;
        selection.setOffset(new Point(newx, newy));
    }

    public void dragCommentFocusRectPosition(Point point, FocusRect focusRect) {
        int newx = (point.x / 16 - commentDragOffsetX_) * 16;
        int newy = (point.y / 16 - commentDragOffsetY_) * 16;

        if (newx < 0)
            newx = 0;
        if (newy < 0)
            newy = 0;

        focusRect.x_ = newx;
        focusRect.y_ = newy;
    }

    Point p;

    private boolean checkPopupMenu(MouseEvent event) {
        if (event.isPopupTrigger()) {
            JPopupMenu popup = new JPopupMenu();

            Map<String, SortedSet<Class<? extends AtsOperator>>> categoryMap = AtsPlugInSystem.getInstance()
                    .getCategorizedPlugins();
            Set<String> keyset = categoryMap.keySet();

            for (String category : keyset) {
                JMenu menu = new JMenu(category);
                for (Class<? extends AtsOperator> operator : categoryMap.get(category)) {

                    AtsOperatorMetadata info = operator.getAnnotation(AtsOperatorMetadata.class);
                    String name = operator.getCanonicalName();
                    if (info != null) {
                        name = info.operatorDesignation();
                    }
                    // else {
                    // name = "WOKWOK";
                    // }
                    JMenuItem item = new JMenuItem(name);
                    item.setActionCommand(operator.getCanonicalName());
                    item.addActionListener(this);
                    menu.add(item);
                }
                popup.add(menu);
            }
            JMenuItem item = new JMenuItem("annotation");
            item.setActionCommand("annotation");
            item.addActionListener(this);
            popup.add(item);
            JMenuItem pluginItem;

            p = event.getPoint();

            popup.show(event.getComponent(), event.getX(), event.getY());
            return true;
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int newx = (p.x / 16 * 16);
        int newy = (p.y / 16 * 16);
        if (e.getActionCommand().equals("annotation")) {
            AtsComment comment = new AtsComment();
            comment.setPosition(newx, newy);
            commentHolder_.add(comment);

            AtsComment selected = commentHolder_.getSelected();
            if (selected != null)
                selected.setSelected(false);
            atsOperatorGraph_.deselectAll();
            comment.setSelected(true);
            atsTexturePreview.setPropertyPanel(comment);

            render();
            ApplicationController.getInstance().setDataChanged(true);
            ApplicationController.getInstance().updateCaption();
            return;
        }

        Map<String, Class<? extends AtsOperator>> plugins = AtsPlugInSystem.getInstance().getPlugins();

        Class<? extends AtsOperator> cls = plugins.get(e.getActionCommand());
        System.out.println("333" + cls.getCanonicalName());
        System.out.flush();
        AtsOperator op = null;
        try {
            op = cls.newInstance();
        } catch (InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (op != null) {
            op.setPosition(newx, newy);
            if (!atsOperatorGraph_.intersects(op)) {
                atsOperatorGraph_.add(op);
                AtsComment selected = commentHolder_.getSelected();
                if (selected != null)
                    selected.setSelected(false);
                atsOperatorGraph_.select(op);
                atsTexturePreview.setPropertyPanel(op);
                atsOperatorGraph_.connectOperators();
                op.setDirty(true);
                render();
                ApplicationController.getInstance().setDataChanged(true);
                ApplicationController.getInstance().updateCaption();
                evaluateGraph();
            }
        }

    }

    @Override
    public void focusGained(FocusEvent arg0) {
        System.out.println("graphfocus gained" + arg0);

    }

    @Override
    public void focusLost(FocusEvent arg0) {
        System.out.println("graphfocus lost" + arg0);

    }

}

class AtsOperatorGraphViewState {

    AtsOperatorGraphView context_;
    public static boolean[] Keys = new boolean[256];

    public AtsOperatorGraphViewState(AtsOperatorGraphView atsOperatorGraphView) {
        context_ = atsOperatorGraphView;
        System.out.println("transition to: " + this.getClass());
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < 256)
            Keys[keyCode] = false;
        System.out.println("key pressed:" + e);
    }

    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();
        if (keyCode < 256)
            Keys[keyCode] = true;
        System.out.println("key pressed:" + e);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    // public void keyPressed(KeyEvent e) { }
    // public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {
    }

}

class InitialState extends AtsOperatorGraphViewState {

    public InitialState(AtsOperatorGraphView atsOperatorGraphView) {
        super(atsOperatorGraphView);
    }

    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);

        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            context_.deleteSelection();
            context_.render();
            context_.setState(new InitialState(context_));

        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            context_.activateSelectedOperator();
            context_.render();
            context_.setState(new InitialState(context_));

        } else if (e.getKeyCode() == KeyEvent.VK_F) {
            context_.selectNext();
            context_.render();
            context_.setState(new InitialState(context_));

        }
    }

    public void mousePressed(MouseEvent e) {

        if (context_.mouseInDragArea(e) && e.getClickCount() > 1) {
            Point mousePosition = e.getPoint();
            AtsOperator operator = context_.getOperatorAtMousePosition(mousePosition);

            if (operator != null) {
                AtsOperator old = context_.atsOperatorGraph_.getActive();
                if (old != null)
                    old.setActiveView(false);
                operator.setActiveView(true);

                if (operator.allInputsAccepted()) {
                    operator.evaluate();
                    context_.atsTexturePreview.setOperator(operator);
                    AtsTexturePreview.getInstance().setMissingInputs(false);
                } else {
                    context_.atsTexturePreview.setOperator(operator);
                    AtsTexturePreview.getInstance().setMissingInputs(true);
                }

                context_.render();
            }
            context_.setState(new InitialState(context_));

        } else if (context_.mouseInDragArea(e)) {
            Point mousePosition = e.getPoint();
            AtsOperator operator = context_.getOperatorAtMousePosition(mousePosition);
            // the following method gains focus and looses it right after (focus
            // of the graph view)
            AtsComment selected = context_.commentHolder_.getSelected();
            if (selected != null)
                selected.setSelected(false);

            // new
            if (Keys[KeyEvent.VK_SHIFT]) {
                context_.toggleOperatorSelection(mousePosition);
            } else {
                if (!operator.isSelected())
                    context_.selectOperator(mousePosition);
            }

            // TODO: START TO FIX CODE HERE FOR GROUP DRAG
            // manage focus rects and collision for all selected ops!!!!!!!!!

            context_.selection.set(context_.atsOperatorGraph_.getSelected());

            // context_.atsOperatorGraph_.getSelected();

            // context_.focusRect_.set(operator);

            context_.storeDragOffset(operator, mousePosition);
            context_.render();
            context_.setState(new DragAreaPressed(context_));

        } else if (context_.mouseInResizeArea(e)) {
            Point mousePosition = e.getPoint();
            AtsOperator operator = context_.getOperatorAtMousePosition(mousePosition);
            AtsComment selected = context_.commentHolder_.getSelected();
            if (selected != null)
                selected.setSelected(false);
            context_.selectOperator(mousePosition);
            context_.focusRect_.set(operator);
            context_.focusRect_.setVisible(true);
            // context_.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
            context_.render();
            context_.setState(new SingleOpResize(context_));

        } else if (context_.mouseInCommentResizeArea(e)) {
            Point mousePosition = e.getPoint();
            AtsComment comment = context_.getCommentAtMousePosition(mousePosition);
            AtsComment selected = context_.commentHolder_.getSelected();
            if (selected != null)
                selected.setSelected(false);
            comment.setSelected(true);
            // AtsOperator operator =
            // context_.atsOperatorGraph_.getSelected().get(0);
            // if(operator != null) operator.selected_ = false;
            context_.atsOperatorGraph_.deselectAll();
            context_.focusRect_.set(comment);
            context_.focusRect_.setVisible(true);
            context_.render();
            context_.setState(new SingleCommentResize(context_));

        } else if (context_.mouseInCommentDragArea(e)) {
            Point mousePosition = e.getPoint();
            AtsComment comment = context_.getCommentAtMousePosition(mousePosition);
            AtsComment selected = context_.commentHolder_.getSelected();
            if (selected != null)
                selected.setSelected(false);
            comment.setSelected(true);
            // AtsOperator operator =
            // context_.atsOperatorGraph_.getSelected().get(0);
            // if(operator != null) operator.selected_ = false;
            context_.atsOperatorGraph_.deselectAll();
            context_.selectComment(mousePosition);
            context_.focusRect_.set(comment);
            context_.focusRect_.setVisible(true);
            context_.storeCommentDragOffset(comment, mousePosition);
            context_.render();
            context_.setState(new CommentDragAreaPressed(context_));
            System.out.println("comment pressed");
        } else { // mouse in workspace
            Point mousePosition = e.getPoint();
            context_.rubberBand.setStart(e.getX(), e.getY());

            AtsComment selected = context_.commentHolder_.getSelected();
            if (selected != null)
                selected.setSelected(false);

            context_.atsTexturePreview.removePropertyPanel();

            // new
            if (!Keys[KeyEvent.VK_SHIFT]) {
                context_.atsOperatorGraph_.deselectAll();
            }

            context_.focusRect_.setVisible(false);
            context_.render();

            context_.setState(new WorkspaceAreaPressed(context_));

        }
    }

}

class CommentDragAreaPressed extends AtsOperatorGraphViewState {

    public CommentDragAreaPressed(AtsOperatorGraphView atsOperatorGraphView) {
        super(atsOperatorGraphView);
    }

    public void mouseReleased(MouseEvent e) {

        if (true) {
            context_.focusRect_.setVisible(false);
            context_.render();
            context_.setState(new InitialState(context_));
        }
    }

    public void mouseDragged(MouseEvent e) {

        if (true) {
            context_.dragCommentFocusRectPosition(e.getPoint(), context_.focusRect_);
            context_.render();
            context_.setState(new CommentDragAreaDragged(context_));
        }
    }

}

class CommentDragAreaDragged extends AtsOperatorGraphViewState {

    public CommentDragAreaDragged(AtsOperatorGraphView atsOperatorGraphView) {
        super(atsOperatorGraphView);
    }

    public void mouseReleased(MouseEvent e) {
        if (true) {
            context_.focusRect_.setVisible(false);
            context_.setCommentPosition();
            // context_.evaluateGraph();
            context_.render();
            context_.setState(new InitialState(context_));
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (true) {
            context_.dragCommentFocusRectPosition(e.getPoint(), context_.focusRect_);
            context_.render();
            context_.setState(new CommentDragAreaDragged(context_));
        }
    }

}

class WorkspaceAreaPressed extends AtsOperatorGraphViewState {

    public WorkspaceAreaPressed(AtsOperatorGraphView atsOperatorGraphView) {
        super(atsOperatorGraphView);
    }

    public void mouseReleased(MouseEvent e) {

        if (true) {

            context_.render();
            context_.setState(new InitialState(context_));

        }
    }

    public void mouseDragged(MouseEvent e) {

        if (true) {

            // context_.dragFocusRectPosition(e.getPoint(),
            // context_.focusRect_);
            context_.rubberBand.setVisible(true);
            context_.rubberBand.setEnd(e.getX(), e.getY());
            context_.render();
            context_.setState(new RubberBandDragged(context_));
        }
    }

}

class RubberBandDragged extends AtsOperatorGraphViewState {

    public RubberBandDragged(AtsOperatorGraphView atsOperatorGraphView) {
        super(atsOperatorGraphView);
    }

    public void mouseReleased(MouseEvent e) {

        // if(true) {
        // context_.focusRect_.setVisible(false);
        // // only evaluate if position changed!!!
        // if(context_.setOperatorPosition(Keys[KeyEvent.VK_CONTROL])) {
        // AtsOperator operator = context_.atsOperatorGraph_.getSelected();
        //
        // //falls quelle nur nachfolger dirty setzen
        // if(operator instanceof AtsDataSourceOperator)
        // operator.setChidrenDirty(true);
        // else
        // operator.setDirty(true);
        //
        // context_.evaluateGraph();
        // }
        context_.rubberBand.setVisible(false);

        // context_.atsOperatorGraph_.selectOperatorAtPosition(point);

        if (Keys[KeyEvent.VK_SHIFT]) {
            context_.atsOperatorGraph_.selectRubberBand(context_.rubberBand, true);
        } else {
            context_.atsOperatorGraph_.selectRubberBand(context_.rubberBand, false);
        }

        context_.render();
        context_.setState(new InitialState(context_));
        // }
    }

    public void mouseDragged(MouseEvent e) {

        if (true) {
            // context_.dragFocusRectPosition(e.getPoint(),
            // context_.focusRect_);
            context_.rubberBand.setEnd(e.getX(), e.getY());
            context_.render();
            context_.setState(new RubberBandDragged(context_));
        }
    }

}

class DragAreaPressed extends AtsOperatorGraphViewState {

    public DragAreaPressed(AtsOperatorGraphView atsOperatorGraphView) {
        super(atsOperatorGraphView);
    }

    public void mouseReleased(MouseEvent e) {

        if (true) {
            // context_.focusRect_.setVisible(false);

            context_.render();
            context_.setState(new InitialState(context_));

        }
    }

    public void mouseDragged(MouseEvent e) {

        if (true) {
            // context_.focusRect_.setVisible(true);
            context_.selection.setVisible(true);
            context_.dragFocusRectPosition(e.getPoint(), context_.focusRect_);
            context_.render();
            context_.setState(new DragAreaDragged(context_));
        }
    }

}

class DragAreaDragged extends AtsOperatorGraphViewState {

    public DragAreaDragged(AtsOperatorGraphView atsOperatorGraphView) {
        super(atsOperatorGraphView);
    }

    public void mouseReleased(MouseEvent e) {

        if (true) {
            context_.selection.setVisible(false);
            // only evaluate if position changed!!!
            if (context_.setOperatorPosition(Keys[KeyEvent.VK_CONTROL])) {
                AtsOperator operator = context_.atsOperatorGraph_.getSelected().get(0);

                // falls quelle nur nachfolger dirty setzen
                if (operator instanceof AtsDataSourceOperator)
                    operator.setChidrenDirty(true);
                else
                    operator.setDirty(true);

                context_.evaluateGraph();
            }
            context_.render();
            context_.setState(new InitialState(context_));
        }
    }

    public void mouseDragged(MouseEvent e) {

        if (true) {
            context_.dragFocusRectPosition(e.getPoint(), context_.focusRect_);
            context_.render();
            context_.setState(new DragAreaDragged(context_));
        }
    }

}

class SingleCommentResize extends AtsOperatorGraphViewState {

    public SingleCommentResize(AtsOperatorGraphView atsOperatorGraphView) {
        super(atsOperatorGraphView);
    }

    public void mouseDragged(MouseEvent e) {
        if (true) {
            context_.setFocusRectWidhtHeight(e.getPoint());
            context_.render();
            context_.setState(new SingleCommentResizeDragged(context_));
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (true) {
            context_.focusRect_.setVisible(false);
            context_.render();
            context_.setState(new InitialState(context_));
        }
    }

}

class SingleOpResize extends AtsOperatorGraphViewState {

    public SingleOpResize(AtsOperatorGraphView atsOperatorGraphView) {
        super(atsOperatorGraphView);
    }

    public void mouseDragged(MouseEvent e) {
        if (true) {
            context_.setFocusRectWidht(e.getPoint());
            context_.render();
            context_.setState(new SingleOpResizeDragged(context_));
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (true) {
            context_.focusRect_.setVisible(false);

            context_.render();
            context_.setState(new InitialState(context_));
        }
    }

}

class SingleCommentResizeDragged extends AtsOperatorGraphViewState {

    public SingleCommentResizeDragged(AtsOperatorGraphView atsOperatorGraphView) {
        super(atsOperatorGraphView);
    }

    public void mouseDragged(MouseEvent e) {
        if (true) {
            context_.setFocusRectWidhtHeight(e.getPoint());
            context_.render();
            context_.setState(new SingleCommentResizeDragged(context_));
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (true) {
            AtsComment comment = context_.getSelectedComment();
            context_.setCommentPosition();
            context_.focusRect_.setVisible(false);
            // context_.evaluateGraph();
            context_.render();
            context_.setState(new InitialState(context_));
        }
    }

}

class SingleOpResizeDragged extends AtsOperatorGraphViewState {

    public SingleOpResizeDragged(AtsOperatorGraphView atsOperatorGraphView) {
        super(atsOperatorGraphView);
    }

    public void mouseDragged(MouseEvent e) {
        if (true) {
            context_.setFocusRectWidht(e.getPoint());
            context_.render();
            context_.setState(new SingleOpResizeDragged(context_));
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (true) {
            AtsOperator operator = context_.getSelectedOperator().get(0);

            if (!context_.atsOperatorGraph_.intersects(operator, context_.focusRect_))
                operator.setBounds(context_.focusRect_);
            // context_.setOperatorPosition(false);

            context_.focusRect_.setVisible(false);
            context_.evaluateGraph();
            context_.render();
            context_.setState(new InitialState(context_));
        }
    }

}
