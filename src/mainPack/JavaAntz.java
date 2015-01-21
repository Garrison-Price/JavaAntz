package mainPack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Model;
import java.util.ArrayList;
import org.lwjgl.util.vector.Matrix4f;
import camera.Camera;
import models.OBJLoader;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 *
 * @author Dasty
 */
public class JavaAntz 
{
    private boolean fullScreen = false;
    private boolean running = false;
    
    private int fps;
    private long lastFPS;
    private long lastFrame;
    
    private Camera camera;
    private ArrayList<Model> models = new ArrayList<Model>();
    
    private Matrix4f transMatrix;
    
    public JavaAntz()
    {
        getDelta();
        lastFPS = getTime();
        initGL();
        camera = new Camera(1000, new Vector3f(0f,0f,0f), new Vector3f(0f,0f,0f));
        

        try {
            models.add(OBJLoader.loadModel(new File("C:\\Users\\Dasty\\Documents\\NetBeansProjects\\JavaAntz\\res\\Torus.obj")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JavaAntz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JavaAntz.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        transMatrix = new Matrix4f();
        transMatrix.translate(camera.startPosition);
        transMatrix.rotate(camera.rotation.x, new Vector3f(1f,0f,0f));
        transMatrix.rotate(camera.rotation.y, new Vector3f(0f,1f,0f));
        transMatrix.rotate(camera.rotation.z, new Vector3f(0f,0f,1f));
        start();
    }
    
    public final void initGL()
    {
        try 
        {
            if (fullScreen) 
            {
                Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
            } 
            else 
            {
                Display.setResizable(true);
                Display.setDisplayMode(new DisplayMode(800, 600));
            }
            Display.setTitle("MB Test");
            Display.create();

            glViewport(0, 0, Display.getWidth(), Display.getHeight());
            glLoadIdentity();

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glShadeModel(GL_SMOOTH);              // Enable Smooth Shading
            glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
            glClearDepth(1.0f);                      // Depth Buffer Setup
            glEnable(GL_DEPTH_TEST);              // Enables Depth Testing
            glDepthFunc(GL_LEQUAL);               // The Type Of Depth Testing To Do
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_ARRAY_BUFFER_BINDING);
            glEnable(GL_CULL_FACE);
            
            glCullFace(GL_BACK);
            
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            
            glMaterialf(GL_FRONT, GL_SHININESS, 128.0f);
            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
            
            
        } 
        catch (LWJGLException ex) 
        {
            System.err.println("Display initialization failed.");
            System.exit(1);
        }
    }
    
    public final void start()
    {
        if(!running)
            running = true;

        for(Model m : models)
        {
            m.setUp();
        }
        while(running)
        {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            
            transMatrix = new Matrix4f();
            
            camera.moveToPosition(transMatrix);
            
            //draw here
            for(int x = 0; x < 30; x++)
            {
                for(int y = 0; y < 5000; y++)
                {
                    models.get(0).draw();
                    glTranslatef(2.1f, 0f, 0f);
                }
                glTranslatef(-2.1f*5000, 0f, 2.1f);
            }
            
            
            camera.Update();
            
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                if (!Mouse.isGrabbed() || Display.isFullscreen()) {
                    running = false;
                } else {
                    Mouse.setGrabbed(false);
                }
            }

            updateFPS();
            Display.update();
            if (Display.isCloseRequested()) {
                running = false;
            }
        }
    }
    
    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private int getDelta() {
        long currentTime = getTime();
        int delta = (int) (currentTime - lastFrame);
        lastFrame = getTime();
        return delta;
    }

    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            System.out.println("FPS: " + fps);
            
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
    
    public void torus(int numc, int numt)
    {
       int i, j, k;
       double s, t, x, y, z, twopi;

       twopi = 2 * (double)Math.PI;
       for (i = 0; i < numc; i++) {
          glBegin(GL_QUAD_STRIP);
          for (j = 0; j <= numt; j++) {
             for (k = 1; k >= 0; k--) {
                s = (i + k) % numc + 0.5;
                t = j % numt;

                x = (1+.1*Math.cos(s*twopi/numc))*Math.cos(t*twopi/numt);
                y = (1+.1*Math.cos(s*twopi/numc))*Math.sin(t*twopi/numt);
                z = .1 * Math.sin(s * twopi / numc);
                glVertex3f((float) x,(float) y,(float) z);
             }
          }
          glEnd();
       }
    }
    
    public static void main(String args[])
    {
        //System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/natives/");
        new JavaAntz();
    }
}
