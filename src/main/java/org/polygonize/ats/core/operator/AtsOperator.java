/* TODO:
 * 
 * 		Red mask:              0x00ff0000
 * 		Green mask:            0x0000ff00
 * 		Blue mask:             0x000000ff
 * 		Alpha mask:            0xff000000
 * - operator funktionali�t implementieren:
 * 				* rename
 * 				* clip
 * 				* group select
 * 				* move selection
 *				* operator state
 *				* view operator properties & operator content
 *				* textblock comment
 *				* operator texture screenshot
 *				* drawpane screenshot
 * BEI DEN JAVA 2D API TUTORIALS LESEN WIE MAN TEXT BLOECKE RENDERT
 * 
 */

package org.polygonize.ats.core.operator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.polygonize.ats.core.ui.FocusRect;
import org.polygonize.ats.core.ui.ImageFactory;
import org.polygonize.ats.core.ui.ImageFactory.ImageType;
import org.polygonize.ats.core.ui.RubberBand;
import org.polygonize.ats.core.ui.TextRenderer;
import org.polygonize.ats.util.AtsInteger;
import org.polygonize.ats.util.PropertyContainer;

public abstract class AtsOperator implements Cloneable, Comparable<AtsOperator> {

    public static String opName = "AtsOperator";
    private boolean activeView_ = false;
    public boolean selected_ = false;

    // only used for xml
    private int id_ = 0;

    protected int positionX_ = 0;
    protected int positionY_ = 0;
    protected int width_ = 64 + 32;
    protected int grabBarWidth_ = 10;
    protected int height_ = 16;
    String name_ = "";

    protected BufferedImage operator;
    protected BufferedImage operatorActive;
    protected BufferedImage operatorSelected;
    protected BufferedImage operatorSelectedActive;
    BufferedImage pixelFont;
    BufferedImage pixelFontShadow;
    String caption;
    // protected int[] data_;
    protected AtsProceduralTexture texture_;
    protected Vector<AtsOperator> inputs_ = new Vector<AtsOperator>();
    protected Vector<AtsOperator> outputs_ = new Vector<AtsOperator>();
    TextRenderer textRenderer = TextRenderer.getInstance();

    AtsInteger red_ = new AtsInteger(255);
    AtsInteger green_ = new AtsInteger(255);
    AtsInteger blue_ = new AtsInteger(255);

    AtsInteger x_ = new AtsInteger(4);
    AtsInteger y_ = new AtsInteger(4);

    @SuppressWarnings("unchecked")
    // @Override
    public Object clone() throws CloneNotSupportedException {
        AtsOperator op = (AtsOperator) super.clone();

        op.inputs_ = (Vector<AtsOperator>) this.inputs_.clone();
        op.outputs_ = (Vector<AtsOperator>) this.outputs_.clone();
        op.texture_ = (AtsProceduralTexture) this.texture_.clone();
        return op;
    }

    public AtsOperator copyOperator() {

        AtsOperator operator = null;

        try {
            operator = this.getClass().newInstance();
            operator.width_ = this.width_;
            operator.name_ = new String(this.name_);
            PropertyContainer source = new PropertyContainer();
            this.edit(source);
            PropertyContainer dest = new PropertyContainer();
            operator.edit(dest);

            Vector<String> names = source.getParamNames();
            for (String name : names) {
                dest.getParam(name).copy(source.getParam(name));
            }

        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return operator;
    }

    AtsOperator(AtsOperator operator) {

    }

    public int getId() {
        return id_;
    }

    public void setId(int i) {
        id_ = i;
    }

    public Vector<AtsOperator> getInputs() {
        return inputs_;
    }

    public static String getOperatorName() {
        return opName;
    }

    public void setInputs(Vector<AtsOperator> inputs) {
        inputs_ = inputs;
    }

    public String getName() {
        return name_;
    }

    public AtsProceduralTexture getTexture() {
        return texture_;
    }

    public boolean isSelected() {
        return selected_;
    }

    public boolean isActive() {
        return activeView_;
    }

    public void edit(PropertyContainer propertyContainer) {
    }

    public void clear() {
        texture_.clear();
    }

    /*
     * if(!isDirty()) return;
     * 
     * process operator...
     * 
     * setDirty(false);
     * 
     * kann zu prblememn fuehren: weil drity ist am ende gesetzt wird kann es
     * vorkommen, dass zwei operatorne gleichzeitig rechnen -> ach ne doch
     * nicht!!! die kinder eines operators werden ja stren von links nach rechts
     * berechnet!!!
     */

    boolean active = false;

    boolean dirty_ = true;

    public void setDirty(boolean dirty) {

        // if(isDirty()) return;

        dirty_ = dirty;
        // first set "this" dirty and all parents lateron
        for (AtsOperator ops : outputs_)
            ops.setDirty(true);
    }

    public void setChidrenDirty(boolean dirty) {

        for (AtsOperator ops : outputs_)
            ops.setDirty(true);
    }

    public boolean isDirty() {
        return dirty_;
    }

    public boolean allInputsAccepted() {

        for (int i = 0; i < inputs_.size(); i++) {
            if (!inputs_.get(i).allInputsAccepted())
                return false;
        }

        if (!isInputAccepted())
            return false;

        return true;
    }

    public void evaluate() {

        if (!isDirty())
            return;

        for (int i = 0; i < inputs_.size(); i++) {
            inputs_.get(i).evaluate();
            // Thread.yield();
        }

        if (isInputAccepted())
            process();
        else
            clear();

        setDirty(false);

    }

    public boolean isInputAccepted() {
        return inputs_.size() > 0;
    }

    public void process() {
        int SinPeriod = x_.getValue();
        int CosPeriod = y_.getValue();
        for (int y = 0; y < 256; y++)
            for (int x = 0; x < 256; x++) {
                float scale = (float) ((Math.sin(2 * Math.PI / 256 * x * SinPeriod)
                        + Math.sin(2 * Math.PI / 256 * y * CosPeriod) + 2) / 4);
                int red = (int) (red_.getValue() * scale);
                int green = (int) (green_.getValue() * scale);
                int blue = (int) (blue_.getValue() * scale);
                int color = (100 << 24) | (red << 16) | (green << 8) | (blue);
                texture_.setPixel(x, y, red, green, blue, 255);
            }
    }

    public AtsOperator() {
        this("test", 0, 0);
    }

    public AtsOperator(String name, int xpos, int ypos) {
        positionX_ = xpos;
        positionY_ = ypos;

        operator = ImageFactory.getInstance().getImage(ImageType.PROCESS_OPERATOR);
        operatorSelected = ImageFactory.getInstance().getImage(ImageType.PROCESS_OPERATOR_SELECTED);
        operatorActive = ImageFactory.getInstance().getImage(ImageType.PROCESS_OPERATOR_ACTIVE);
        operatorSelectedActive = ImageFactory.getInstance().getImage(ImageType.PROCESS_OPERATOR_SELECTED_AND_ACTIVE);

        caption = name;
        texture_ = new AtsProceduralTexture();

        AtsOperatorMetadata info = this.getClass().getAnnotation(AtsOperatorMetadata.class);

        caption = this.getClass().getCanonicalName();
        if (info != null) {
            caption = info.operatorDesignation();

        }
    }

    Color inputNotAcceptedColor = new Color(255, 135, 31);
    BasicStroke inputNotAcceptedStroke = new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    void drawInputNotAccepted(Graphics2D graphics2D) {
        graphics2D.drawLine(positionX_ + 0 + 1, positionY_ + 0 - 1, positionX_ + width_ - 1 - 1, positionY_ + 0 - 1);
    }

    public void draw(Graphics2D graphics2D) {
        String caption;

        if (name_.length() == 0)
            caption = this.caption;
        else
            caption = name_;

        if (!isInputAccepted()) {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(inputNotAcceptedColor);
            graphics2D.setStroke(inputNotAcceptedStroke);
            drawInputNotAccepted(graphics2D);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        Shape shape = graphics2D.getClip();

        graphics2D.clipRect(positionX_, positionY_, width_, height_);

        drawOp(graphics2D);
        // drawStringOp(graphics2D,caption, positionX_, positionY_+16/2-8/2,);
        textRenderer.drawStringOp(graphics2D, caption, positionX_, positionY_ + 16 / 2 - 8 / 2, width_);

        graphics2D.setClip(shape);

        // only fpor debug: colors inputs of dirty ops
        // graphics2D.setColor( Color.getHSBColor(123/369.f, 0.8f, 0.99f ));
        // if(isDirty())
        // graphics2D.drawLine(positionX_+ 0, positionY_+
        // 0,positionX_+width_-1,positionY_ + 0);
    }

    void drawOp(Graphics2D g2) {
        drawTile(g2, 0, positionX_, positionY_);
        for (int i = 0; i < width_ / 16 - 2; i++)
            drawTile(g2, 1, positionX_ + i * 16 + 16, positionY_);
        drawTile(g2, 3, positionX_ + width_ - 16, positionY_);
    }

    void drawTile(Graphics2D g2, int index, int x, int y) {
        if (selected_ && !activeView_)
            g2.drawImage(operatorSelected, 0 + x, 0 + y, 16 + x, 16 + y, 0 + index * 16, 0, 16 + index * 16, 16, null);
        else if (activeView_ && !selected_)
            g2.drawImage(operatorActive, 0 + x, 0 + y, 16 + x, 16 + y, 0 + index * 16, 0, 16 + index * 16, 16, null);
        else if (activeView_ && selected_)
            g2.drawImage(operatorSelectedActive, 0 + x, 0 + y, 16 + x, 16 + y, 0 + index * 16, 0, 16 + index * 16, 16,
                    null);
        else
            g2.drawImage(operator, 0 + x, 0 + y, 16 + x, 16 + y, 0 + index * 16, 0, 16 + index * 16, 16, null);
    }

    public void setActiveView(boolean active) {
        activeView_ = active;
    }

    public void setSelected(boolean selected) {
        selected_ = selected;
    }

    public boolean contains(Point point) {
        return positionX_ <= point.x && point.x <= positionX_ + width_ - 1 && positionY_ <= point.y
                && point.y <= positionY_ + height_ - 1;
    }

    public boolean isPointInDragArea(Point point) {
        return positionX_ <= point.x && point.x <= positionX_ + width_ - grabBarWidth_ - 1 && positionY_ <= point.y
                && point.y <= positionY_ + height_ - 1;
    }

    public boolean isPointInResizeArea(Point point) {
        return positionX_ + width_ - grabBarWidth_ <= point.x && point.x <= positionX_ + width_ - 1
                && positionY_ <= point.y && point.y <= positionY_ + height_ - 1;
    }

    public boolean resizeArea(Point point) {
        return positionX_ + width_ - grabBarWidth_ <= point.x && point.x <= positionX_ + width_ - 1
                && positionY_ <= point.y && point.y <= positionY_ + height_ - 1;
    }

    public void setBounds(FocusRect focusRect) {
        positionX_ = focusRect.x_;
        positionY_ = focusRect.y_;
        width_ = focusRect.width_;
        height_ = focusRect.height_;
    }

    public boolean intersects(AtsOperator operator) {
        // if(this.bottom < op.top) return false;
        // if(this.top > op.bottom) return false;
        // if(this.right < op.left) return false;
        // if(this.left > op.right) return false;

        if (this.positionY_ + height_ - 1 < operator.positionY_)
            return false;
        if (this.positionY_ > operator.positionY_ + operator.height_ - 1)
            return false;
        if (this.positionX_ + width_ - 1 < operator.positionX_)
            return false;
        if (this.positionX_ > operator.positionX_ + operator.width_ - 1)
            return false;

        return true;
    }

    public boolean intersects(FocusRect operator) {
        if (this.positionY_ + height_ - 1 < operator.y_)
            return false;
        if (this.positionY_ > operator.y_ + operator.height_ - 1)
            return false;
        if (this.positionX_ + width_ - 1 < operator.x_)
            return false;
        if (this.positionX_ > operator.x_ + operator.width_ - 1)
            return false;

        return true;
    }

    public boolean intersects(RubberBand rubber) {
        rubber.computeRect();
        if (this.positionY_ + height_ - 1 < rubber.y)
            return false;
        if (this.positionY_ > rubber.y + rubber.height - 1)
            return false;
        if (this.positionX_ + width_ - 1 < rubber.x)
            return false;
        if (this.positionX_ > rubber.x + rubber.width - 1)
            return false;

        return true;
    }

    public boolean isOverOp(AtsOperator operator) {
        if (this.positionX_ + width_ - 1 < operator.positionX_)
            return false;
        if (this.positionX_ > operator.positionX_ + operator.width_ - 1)
            return false;
        if (this.positionY_ - height_ != operator.positionY_)
            return false;
        return true;

    }

    public void setPosition(int x, int y) {
        positionX_ = x;
        positionY_ = y;
    }

    public void setWidth(int width) {
        width_ = width;
    }

    public int getWidth() {
        return width_;
    }

    public int getHeight() {
        return height_;
    }

    public int getX() {
        return positionX_;
    }

    public int getY() {
        return positionY_;
    }

    public int compareTo(AtsOperator o) {
        AtsOperator operator = o;
        if ((this.positionY_ == operator.positionY_ && this.positionX_ < operator.positionX_)
                || this.positionY_ < operator.positionY_)
            return -1;
        else if ((this.positionY_ == operator.positionY_ && this.positionX_ > operator.positionX_)
                || this.positionY_ > operator.positionY_)
            return 1;
        else
            return 0;
    }

    public void setName(String text) {
        name_ = text;

    }

    public void clearOutput() {
        outputs_ = new Vector<AtsOperator>();

    }

    public Vector<AtsOperator> getOutput() {
        return outputs_;
    }

    public void addOutput(AtsOperator atsOperator) {
        outputs_.add(atsOperator);

    }

}
