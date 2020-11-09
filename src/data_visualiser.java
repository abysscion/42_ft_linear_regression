import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.awt.TextRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Background implements GLEventListener {
    @Override
    public void init(GLAutoDrawable arg0) { }
    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f( .64f,.53f,.92f );
        gl.glVertex3f(-1f, -1f, 0);
        gl.glVertex3f(1f, -1f, 0);
        gl.glVertex3f(1f, 1f, 0);
        gl.glVertex3f(-1f, 1f, 0);
        gl.glEnd();
    }
    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) { }
    @Override
    public void dispose(GLAutoDrawable arg0) { }
}

class Line implements GLEventListener {
    private final boolean isAxis;
    private final float width;
    private final float x0;
    private final float y0;
    private final float x1;
    private final float y1;

    public Line(float x0, float y0, float x1, float y1, float width) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.width = width;
        this.isAxis = true;
    }

    public Line(float x0, float y0, float x1, float y1, float width, boolean isAxis) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.width = width;
        this.isAxis = isAxis;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glLineWidth(width);
        gl.glBegin(GL2.GL_LINES);
        if (isAxis)
            gl.glColor3f( 1f,1f,0f );
        else
            gl.glColor3f( 0.5f,1f,1.0f );
        gl.glVertex3f(x0, y0, 0);
        gl.glVertex3f(x1, y1, 0);
        gl.glEnd();
    }
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    @Override
    public void dispose(GLAutoDrawable arg0) {}
    @Override
    public void init(GLAutoDrawable arg0) {}
}

class Circle implements GLEventListener {
    private final boolean isSpecial;
    private final float detailLevel;
    private final float cx;
    private final float cy;
    private final float r;

    public Circle(float cx, float cy, float r, int detailLevel) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.detailLevel = detailLevel;
        this.isSpecial = false;
    }

    public Circle(float cx, float cy, float r, int detailLevel, boolean isSpecial) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.detailLevel = detailLevel;
        this.isSpecial = isSpecial;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        var twicePI = Math.PI * 2;
        var detailFactor = twicePI/detailLevel;

        if (!isSpecial)
            gl.glColor3f(1f, 1f, 0f);
        else
            gl.glColor3f(0f, 0.5f, 1f);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(cx, cy);
        for (var i = 0; i <= detailLevel; i++)
            gl.glVertex2f((float) (cx + (r * Math.cos(i * detailFactor))), (float) (cy + (r * Math.sin(i * detailFactor))));
        gl.glEnd();
    }
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    @Override
    public void dispose(GLAutoDrawable arg0) {}
    @Override
    public void init(GLAutoDrawable arg0) {}
}

class Text implements GLEventListener {
    private final String text;
    private final float angle;
    private final float x0;
    private final float y0;
    private final int fontSize;

    public Text(float x0, float y0, int fontSize, String text) {
        this.text = text;
        this.x0 = x0;
        this.y0 = y0;
        this.fontSize = fontSize;
        this.angle = 0.0f;
    }

    public Text(float x0, float y0, int fontSize, float angle, String text) {
        this.text = text;
        this.x0 = x0;
        this.y0 = y0;
        this.fontSize = fontSize;
        this.angle = angle;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        var textRenderer = new TextRenderer(new Font("Arial", Font.BOLD, fontSize));

        textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glTranslatef(x0, y0, 0f);
        gl.glRotatef(angle, 0f, 0f, 1f);
        textRenderer.draw(text, 0, 0);
        textRenderer.endRendering();
        gl.glPopMatrix();
        gl.glFlush();
    }
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    @Override
    public void dispose(GLAutoDrawable arg0) {}
    @Override
    public void init(GLAutoDrawable arg0) {}
}

public class data_visualiser {
    private static GLCanvas canvas;

    public static void showOriginalData(ArrayList<double[]> data, double minX, double minY, double maxX, double maxY) {
        init();

        drawAxisesAndMarks();
        drawLinearRegression();
        drawOriginalDataText(minX, minY, maxX, maxY);
        drawDataCircles(data, false);
        setAndShowWindow("Original data");
    }

    public static void showStandardizedData(ArrayList<double[]> data) {
        init();

        var canW = canvas.getSize().width * 0.9f;
        var canH = canvas.getSize().height * 0.9f;

        for (var i = 0; i <= 10; i++) {
            canvas.addGLEventListener(new Text(canW * 0.05f + (i * canW / 10.2f), canH * 0.025f, 15, ("" + i * 0.1f).substring(0, 3)));
            canvas.addGLEventListener(new Text(canW * 0.0125f, canH * 0.05f + (i * canH / 10.5f), 15, ("" + i * 0.1f).substring(0, 3)));
        }

        drawAxisesAndMarks();
        drawLinearRegression();
        drawDataCircles(data, false);
        setAndShowWindow("Standardized data");
    }

    public static void showPredictedOverOriginalData(ArrayList<double[]> data, ArrayList<double[]> predictionsData, double minX, double minY, double maxX, double maxY) {
        init();

        var canW = canvas.getSize().width * 0.9f;
        var canH = canvas.getSize().height * 0.9f;
        var stepX = (maxX - minX) / 10;
        var stepY = (maxY - minY) / 10;

        drawAxisesAndMarks();
        drawLinearRegression();
        drawOriginalDataText(minX, minY, maxX, maxY);
        drawDataCircles(data, false);
        drawDataCircles(predictionsData, true);
        setAndShowWindow("Predicted over standardized data");
    }

    private static void init() {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        Background bkg = new Background();
        canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(bkg);
        canvas.setSize(800, 800);
    }

    private static void setAndShowWindow(String name) {
        final JFrame frame = new JFrame(name);
        frame.getContentPane().add(canvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void drawOriginalDataText(double minX, double minY, double maxX, double maxY) {
        var canW = canvas.getSize().width * 0.9f;
        var canH = canvas.getSize().height * 0.9f;
        var stepX = (maxX - minX) / 10;
        var stepY = (maxY - minY) / 10;

        canvas.addGLEventListener(new Text(canW * 0.05f, canH * 0.004f, 12, 45, "" + (int)minX));
        canvas.addGLEventListener(new Text(canW * 0.0125f, canH * 0.04f, 12, 45, "" + (int)minY));
        canvas.addGLEventListener(new Text(canW * 1.02f, canH * 0.004f, 12, 45, "" + (int)maxX));
        canvas.addGLEventListener(new Text(canW * 0.0125f, canH * 0.99f, 12, 45, "" + (int)maxY));
        for (var i = 1; i < 10; i++) {
            var xText = "" + ((i * stepX) + minX);
            var yText = "" + ((i * stepY) + minY);

            xText = xText.split("\\.")[0];
            yText = yText.split("\\.")[0];

            if (xText.length() > 6)
                xText = xText.substring(0, 6);
            if (yText.length() > 6)
                yText = yText.substring(0, 6);
            canvas.addGLEventListener(new Text(canW * 0.05f + (i * canW / 10.2f), canH * 0.004f, 12, 45, xText));
            canvas.addGLEventListener(new Text(canW * 0.0125f, canH * 0.05f + (i * canH / 10.7f), 12, 45, yText));
        }
    }

    private static void drawLinearRegression() {
        canvas.addGLEventListener(new Line(
                -0.9f + 0 * 2f * 0.9f, -0.9f + (float) train.estimatePrice(0) * 2f * 0.9f,
                -0.9f + 1 * 2f * 0.9f, -0.9f + (float) train.estimatePrice(1) * 2f * 0.9f,
                2f,
                false));
    }

    private static void drawAxisesAndMarks() {
        var canW = canvas.getSize().width * 0.9f;
        var canH = canvas.getSize().height * 0.9f;

        canvas.addGLEventListener(new Text(canW * 0.1f, canH * 0.065f, 15, "mileage"));
        canvas.addGLEventListener(new Text(canW * 0.08f, canH * 0.1f, 15, 90f, "price"));
        canvas.addGLEventListener(new Line(-0.9f, -0.9f, -0.9f, 1f, 2f));
        canvas.addGLEventListener(new Line(-0.9f, -0.9f, 1f, -0.9f, 2f));
        for (var i = 0; i <= 10; i++) {
            canvas.addGLEventListener(new Line( -0.9f + 0.2f * 0.9f * i, -0.9f - 0.01f,
                    -0.9f + 0.2f * 0.9f * i, -0.9f + 0.01f,
                    1.5f));
            canvas.addGLEventListener(new Line( -0.9f - 0.01f, -0.9f + 0.2f * 0.9f * i,
                    -0.9f + 0.01f, -0.9f + 0.2f * 0.9f * i,
                    1.5f));
        }
    }

    private static void drawDataCircles(ArrayList<double[]> data, boolean isSpecial) {
        for (var dataEntry : data) {
            float scaledValX = -0.9f + ((float) dataEntry[0] * 2f) * 0.9f;
            float scaledValY = -0.9f + ((float) dataEntry[1] * 2f) * 0.9f;
            canvas.addGLEventListener(new Circle(scaledValX, scaledValY, 0.01f, 16, isSpecial));
        }
    }
}
