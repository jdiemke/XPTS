package org.polygonize.ats.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.polygonize.ats.core.operator.AtsProceduralTexture;
import org.polygonize.ats.opengl.Image;
import org.polygonize.ats.opengl.PhotekSphere;
import org.polygonize.ats.opengl.PhotekTexture;
import org.polygonize.ats.opengl.PhotekTexture2;
import org.polygonize.ats.opengl.PhotekTextureFilter;
import org.polygonize.ats.opengl.PhotekTextureWrapMode;
import org.polygonize.ats.util.AtsUtil;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

public class AtsTexturePreview extends JPanel
        implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener, ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private GLJPanel canvas_;
    private FPSAnimator animator_;
    PhotekSphere sphere_ = new PhotekSphere();

    /**
     * FIXME this view seems to be broken
     */

    int width_ = 0;
    int height_ = 0;

    int valuex = 0, oldvaluex = 0;
    int valuey = 0, oldvaluey = 0;
    int distance = 1;
    float scale = 1.0f;
    BufferedImage image;
    PhotekTexture texure_, texture2_;
    PhotekTexture2 background_;
    PhotekTexture2 missingInputs;
    double displacementX_ = 0;
    double displacementY_ = 0;
    boolean repeast_ = false;
    boolean missingInputsB = false;
    boolean textureMode = false;

    boolean update = false;
    int[] pixels = new int[256 * 256];
    int[] pixels2 = new int[256 * 256];

    @Override
    protected void finalize() throws Throwable {
        shutDown();
        super.finalize();
    }

    public void shutDown() {
        new Thread(new Runnable() {
            public void run() {
                animator_.stop();
                System.exit(0);
            }
        }).start();
    }

    public void updateTexture() {
        update = true;
        canvas_.repaint();
    }

    private static AtsTexturePreview instance_;

    private AtsTexturePreview() {
        // setDoubleBuffered(true);
        // setBorder(BorderFactory.createLoweredBevelBorder());

        // setBorder(BorderFactory.createLoweredBevelBorder());
        // setBorder(BorderFactory.createTitledBorder("title"));
        GLProfile glp = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(glp);

        // caps.setDoubleBuffered(true);
        caps.setSampleBuffers(true);
        caps.setNumSamples(4);
        // glCaps.setStencilBits(8);
        // setBackground(Color.BLACK);
        // setPreferredSize(new Dimension(400,256));

        // setLayout(new GridLayout(1, 1, 3, 3));

        // setMinimumSize(new Dimension(0, 0));
        // setMaximumSize(new Dimension(0, 0));

        canvas_ = new GLJPanel(caps);

        canvas_.setPreferredSize(new Dimension(400, 256));

        setLayout(new BorderLayout());
        // setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        add(BorderLayout.CENTER, canvas_);

        // add(canvas_);

        canvas_.addMouseListener(this);
        canvas_.addMouseMotionListener(this);
        canvas_.addMouseWheelListener(this);

        canvas_.addGLEventListener(this);

        animator_ = new FPSAnimator(canvas_, 25);
        animator_.start();

    }

    public static AtsTexturePreview getInstance() {
        if (instance_ == null) {
            instance_ = new AtsTexturePreview();
        }
        return instance_;
    }

    int[] Hr = new int[256 / 2];
    int[] Hg = new int[256 / 2];
    int[] Hb = new int[256 / 2];
    AtsProceduralTexture procTex = new AtsProceduralTexture();

    public void createHistogram(int[] texture) {
        procTex.texels_ = texture;
        Arrays.fill(Hr, 0);
        Arrays.fill(Hg, 0);
        Arrays.fill(Hb, 0);

        for (int i = 0; i < 256 * 256; i++) {
            int r = AtsUtil.extractR(procTex.getPixel(i));
            int g = AtsUtil.extractG(procTex.getPixel(i));
            int b = AtsUtil.extractB(procTex.getPixel(i));
            Hr[r / 2]++;
            Hg[g / 2]++;
            Hb[b / 2]++;
        }
    }

    public void drawHistogram(GL2 gl) {

        gl.glDisable(GL.GL_MULTISAMPLE);

        gl.glPushMatrix();
        gl.glTranslatef(8, 8, 0);

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor4f(0, 0, 0, 0.9f);
        gl.glVertex2f(0, 0);
        gl.glColor4f(1, 1, 1, 0.9f);
        gl.glVertex2f(256, 0);
        gl.glVertex2f(256, 8);
        gl.glColor4f(0, 0, 0, 0.9f);
        gl.glVertex2f(0, 8);
        gl.glEnd();

        gl.glColor4d(0.2, 0.2, 0.2, 0.8);
        gl.glPushMatrix();
        gl.glTranslatef(0, 10, 0);

        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(256, 0);
        gl.glVertex2f(256, 100 + 1);
        gl.glVertex2f(0, 100 + 1);
        gl.glEnd();

        int maxHr = 0;
        int maxHg = 0;
        int maxHb = 0;
        for (int i = 0; i < 256 / 2; i++) {
            maxHr = Math.max(maxHr, Hr[i]);
            maxHg = Math.max(maxHg, Hg[i]);
            maxHb = Math.max(maxHb, Hb[i]);
        }
        float maxHrf = (100.f) / (maxHr);
        float maxHgf = (100.f) / (maxHg);
        float maxHbf = (100.f) / (maxHb);

        gl.glEnable(GL.GL_MULTISAMPLE);

        gl.glColor4f(0.5f, 0.5f, 1, 0.9f);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (int i = 0; i < 256 / 2; i++) {
            gl.glVertex3f(i * 2 + 1, Hb[i] * maxHbf, 0);
        }
        gl.glEnd();
        gl.glColor4f(0.5f, 1, 0.5f, 0.9f);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (int i = 0; i < 256 / 2; i++) {
            gl.glVertex3f(i * 2 + 1, Hg[i] * maxHgf, 0);
        }
        gl.glEnd();
        gl.glColor4f(1, 0.5f, 0.5f, 0.9f);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (int i = 0; i < 256 / 2; i++) {
            gl.glVertex3f(i * 2 + 1, Hr[i] * maxHrf, 0);
        }
        gl.glEnd();

        //
        gl.glDisable(GL.GL_MULTISAMPLE);

        gl.glPolygonMode(GL.GL_FRONT, GL2.GL_LINE);
        gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(256, 0);
        gl.glVertex2f(256, 100 + 1);
        gl.glVertex2f(0, 100 + 1);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(256, 0);
        gl.glVertex2f(256, 8);
        gl.glVertex2f(0, 8);
        gl.glEnd();

        gl.glPopMatrix();
        gl.glPolygonMode(GL.GL_FRONT, GL2.GL_FILL);
        gl.glLineWidth(1.2f);
        //

        gl.glEnable(GL.GL_MULTISAMPLE);
    }

    boolean mode3d = false;

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();

        if (update) {
            update = false;
            texure_.update(gl);
            texture2_.update(gl);
            createHistogram(texure_.texture_);
        }

        if (mode3d) {
            texure_.bind(gl);
            ////////// preview.reshape(drawable, 0, 0, width_, height_);
            ////////// preview.display(drawable);
        } else {
            switchTo2DMode(gl);

            gl.glLoadIdentity();
            gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);

            // setup
            gl.glPushMatrix();
            gl.glLoadIdentity();
            gl.glTranslatef(0.35f, 0.35f, 0);
            gl.glDisable(GL2.GL_LIGHTING);
            //
            PhotekTexture.enableTexturing(gl);
            gl.glDisable(GL.GL_BLEND);
            gl.glDisable(GL.GL_DEPTH_TEST);
            //
            gl.glColor4f(68 / 255f * 2, 66 / 255f * 2, 58 / 255f * 2, 255 / 255f);

            float widthRatioB = width_ / 16.f;
            float heightRatioB = height_ / 16.f;

            background_.bind(gl);
            // texure_.bind(gl);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(0.0f, 0.0f, 0.0f);

            gl.glTexCoord2f(widthRatioB, 0.0f);
            gl.glVertex3f(width_, 0.0f, 0.0f);

            gl.glTexCoord2f(widthRatioB, heightRatioB);
            gl.glVertex3f(width_, height_, 0.0f);

            gl.glTexCoord2f(0.0f, heightRatioB);
            gl.glVertex3f(0.0f, height_, 0.0f);
            gl.glEnd();
            //
            PhotekTexture.disableTexturing(gl);
            gl.glEnable(GL.GL_BLEND);

            gl.glDisable(GL2.GL_DEPTH_TEST);

            // drawHistogram(gl);

            PhotekTexture.enableTexturing(gl);
            gl.glPopMatrix();
            // preview.reshape(drawable, 0, 0, width_, height_);

            float widthRatio = width_ / 256.f;
            float heightRatio = height_ / 256.f;

            double xDisplacement = 0.5f - (widthRatio / 2);// -(displacementX_/(double)(width_+1));
            double yDisplacement = 0.5f - (heightRatio / 2);// +(displacementY_/(double)(height_+1));

            gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

            texure_.bind(gl);

            if (missingInputsB)
                missingInputs.bind(gl);

            if (repeast_) {

                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            } else {
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_BORDER);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_BORDER);
            }

            gl.glMatrixMode(GL.GL_TEXTURE);
            gl.glLoadIdentity();
            gl.glTranslated(+0.5, 0.5, 0);
            gl.glScalef(1.0f * scale, -1.0f * scale, 1.0f * scale);
            gl.glTranslated(-displacementX_ / (float) 256 - 0.5, (displacementY_ / (float) 256) - 0.5, 0);
            gl.glMatrixMode(GL2.GL_MODELVIEW);

            gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            gl.glDisable(GL.GL_MULTISAMPLE);
            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2d(0.0f + xDisplacement, 0.0f + yDisplacement);
            gl.glVertex3f(0.0f, 0.0f, 0.0f);
            gl.glTexCoord2d(widthRatio + xDisplacement, 0.0f + yDisplacement);
            gl.glVertex3f(width_, 0.0f, 0.0f);
            gl.glTexCoord2d(widthRatio + xDisplacement, heightRatio + yDisplacement);
            gl.glVertex3f(width_, height_, 0.0f);
            gl.glTexCoord2d(0.0f + xDisplacement, heightRatio + yDisplacement);
            gl.glVertex3f(0.0f, height_, 0.0f);
            gl.glEnd();
            gl.glEnable(GL.GL_MULTISAMPLE);
            // PhotekTexture.disableTexturing(gl);
            gl.glTranslatef(.23f, .23f, 0.0f);
            gl.glMatrixMode(GL.GL_TEXTURE);
            gl.glLoadIdentity();

            gl.glDisable(GL2.GL_BLEND);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

            gl.glMatrixMode(GL2.GL_MODELVIEW);
            PhotekTexture.disableTexturing(gl);
            gl.glEnable(GL.GL_BLEND);
            gl.glLoadIdentity();
            // gl.glTranslatef(0.35f, 0.35f,0);
            drawHistogram(gl);

            gl.glEnable(GL2.GL_DEPTH_TEST);
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Einige OpenGL-Werte ausgeben.
        System.out.println("OpenGL Java-Implementation: " + gl.getClass().getName());
        System.out.println(drawable.getChosenGLCapabilities());
        System.out.println("GL_EXTENSIONS: " + gl.glGetString(GL.GL_EXTENSIONS));
        System.out.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.out.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.out.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

        gl.glEnable(GL.GL_CULL_FACE);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        gl.glClearColor(79 / 255f, 74 / 255f, 59 / 255f, 1.0f);

        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.setSwapInterval(1);

        background_ = new PhotekTexture2(gl,
                new Image(this.getClass().getResourceAsStream("/images/visualization/background.png")),
                PhotekTextureFilter.PHOTEK_NEAREST, PhotekTextureWrapMode.PHOTEK_REPEAT);

        missingInputs = new PhotekTexture2(gl,
                new Image(this.getClass().getResourceAsStream("/images/visualization/missingInputs.png")),
                PhotekTextureFilter.PHOTEK_NEAREST, PhotekTextureWrapMode.PHOTEK_CLAMP_TO_EDGE);

        gl.glActiveTexture(GL2.GL_TEXTURE0);
        texure_ = new PhotekTexture(gl, pixels, PhotekTextureFilter.PHOTEK_MIPMAP, PhotekTextureWrapMode.PHOTEK_REPEAT);
        texure_.update(gl);
        gl.glActiveTexture(GL2.GL_TEXTURE3);

        texture2_ = new PhotekTexture(gl, pixels2, PhotekTextureFilter.PHOTEK_MIPMAP,
                PhotekTextureWrapMode.PHOTEK_REPEAT);

        texture2_.update(gl);

        gl.glActiveTexture(GL2.GL_TEXTURE0);

    }

    void switchTo2DMode(GL2 gl) {
        int width = width_;
        int height = height_;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(0.0f, width_, 0, height_, -1.0f, 1.0f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    void switchTo3DMode(GL2 gl) {
        GLU glu = new GLU();

        int width = getWidth();
        int height = getHeight();

        float aspect = (float) width / (float) height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(45.0f, aspect, 1.0f, 100.0f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        width_ = width;

        height_ = height;
        ////////// preview.width_ = width;
        ////////// preview.height_ = height;
        //////////// preview.reshape(drawable, x, y, width, height);
        return;
        // GL2 gl = drawable.getGL().getGL2();
        // GLU glu = new GLU();
        // float aspect;
        //
        //
        //
        //
        // if (height == 0) {
        // height = 1;
        // }
        // aspect = (float) width / (float) height;
        //
        //
        // width_=width;
        //
        // height_=height;
        //
        //
        // //height_ = height;
        //
        // // Setze die Einstellungen fuer die PROJECTION_MATRIX.
        // gl.glMatrixMode(GL2.GL_PROJECTION);
        // gl.glLoadIdentity();
        // glu.gluPerspective(60.0f, aspect, 1.0f, 100.0f);
        //
        //// gl.glMatrixMode(GL.GL_PROJECTION);
        //// gl.glLoadIdentity();
        ////
        //// gl.glOrtho(0.0f, width, 0 , height,-1.0f,1.0f);
        ////
        //// gl.glMatrixMode(GL.GL_MODELVIEW);
        //
        //
        // // Nach diesem Befehl ist die aktuelle Matrix die MODELVIEW_MATRIX.
        // gl.glMatrixMode(GL2.GL_MODELVIEW);
        // // Diese wird hiermit auf die Einheitsmatrix gesetzt.
        // gl.glLoadIdentity();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    Point p, point;
    boolean dragView = false;

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            point = e.getPoint();
            dragView = true;
        }
        // TODO Auto-generated method stub
        p = e.getPoint();
        oldvaluey = valuey;
        oldvaluex = valuex;

        // linux support for context menus
        // on linux it reacts to right mouse pressed events and
        // not to right mouse releases events like in windows
        checkPopupMenu(e);

    }

    Point p2 = null;

    private void checkPopupMenu(MouseEvent event) {
        if (event.isPopupTrigger()) {
            JPopupMenu popup = new JPopupMenu();

            JMenuItem item1 = new JMenuItem("reset");
            JMenuItem item2 = new JMenuItem("switch mode");
            JMenuItem item3 = new JMenuItem("switch preview mode");
            item1.setActionCommand("reset");
            item1.addActionListener(this);
            item2.setActionCommand("switch");
            item2.addActionListener(this);
            item3.setActionCommand("previewmode");
            item3.addActionListener(this);
            popup.add(item1);
            popup.add(item2);
            popup.add(item3);

            JMenuItem pluginItem;

            p2 = event.getPoint();

            popup.show(event.getComponent(), event.getX(), event.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        if (dragView) {
            dragView = !dragView;
        }

        checkPopupMenu(e);

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragView) {
            int x = e.getPoint().x - point.x;
            int y = e.getPoint().y - point.y;
            point = e.getPoint();
            displacementX_ += x;
            displacementY_ += y;

            /////////// preview.setCamera((int) displacementX_, (int)
            /////////// displacementY_, distance);
            // scrollRectToVisible(new Rectangle(x,y,100,100));
        }

        // TODO Auto-generated method stub
        valuex = oldvaluex + (e.getPoint().x - p.x);
        valuey = oldvaluey + (e.getPoint().y - p.y);
        // if(value < 0) value = 0;
        // if(value > 255) value = 255;

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        distance -= e.getWheelRotation();
        scale -= 0.1 * e.getWheelRotation();
        ///////// preview.setCamera((int) displacementX_, (int) displacementY_,
        ///////// distance);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("reset")) {
            displacementX_ = 0;
            displacementY_ = 0;
        }
        if (e.getActionCommand().equals("switch")) {
            repeast_ = !repeast_;
        }
        if (e.getActionCommand().equals("previewmode")) {
            mode3d = !mode3d;
        }

    }

    public void setMissingInputs(boolean b) {
        missingInputsB = b;

    }

    public void setTextureMode(boolean b) {
        textureMode = b;

    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }
}