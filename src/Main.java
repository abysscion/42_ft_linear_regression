import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.JFrame;

class Background implements GLEventListener {

    @Override
    public void init(GLAutoDrawable arg0) { }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f( 1.0f,1.0f,1.0f );
        gl.glVertex3f(-0.5f, 0.5f, 0);
        gl.glVertex3f(0.5f, 0.5f, 0);
        gl.glVertex3f(0.5f, -0.5f, 0);
        gl.glVertex3f(-0.5f, -0.5f, 0);
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) { }
    @Override
    public void dispose(GLAutoDrawable arg0) { }
}

class Line implements GLEventListener {

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glBegin(GL2.GL_LINES);
        gl.glColor3f( 0f,1.0f,0f );
        gl.glVertex3f(0.50f, -0.50f, 0);
        gl.glVertex3f(-0.50f, 0.50f, 0);
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        //method body
    }

    @Override
    public void init(GLAutoDrawable arg0) {
        // method body
    }
}

public class Main {
    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        final GLCanvas glcanvas = new GLCanvas(capabilities);
        Background bkg = new Background();
        Line line = new Line();
        glcanvas.addGLEventListener(bkg);
        glcanvas.addGLEventListener(line);
        glcanvas.setSize(800, 800);

        final JFrame frame = new JFrame("ft_linear_regression");
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
    }
}
