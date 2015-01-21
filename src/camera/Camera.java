/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package camera;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.Sys;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;
/**
 *
 * @author Dasty
 */
public class Camera 
{
    public int cameraMode;
    
    public final int maxLookUp = 85;
    public final int maxLookDown = 275;

    public Vector3f startPosition = new Vector3f(0, 0, 0);
    public Vector3f position = new Vector3f(0, 0, 0);
    public Vector3f rotation = new Vector3f(0, 0, 0);
    public float zFar = 2000f;
    public float zNear = 0.03f;
    public int cameraSpeed = 10;
    public int mouseSpeed = 2;
    private long lastFrame;
    
    public final int FREEFLY = 1000;
    public final int FOLLOW = 1001;
    
    
    public Camera(int mode, Vector3f p, Vector3f r)
    {
        cameraMode = mode;
        startPosition = new Vector3f(p.x,p.y,p.z);
        position = p;
        rotation = r;
        
        gluPerspective(45.0f, (float)Display.getWidth()/(float)Display.getHeight(), zNear, zFar);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
    
    public void moveToPosition(Matrix4f transMatrix)
    {
        glRotatef(rotation.x, 1, 0, 0);
        glRotatef(rotation.y, 0, 1, 0);
        glRotatef(rotation.z, 0, 0, 1);
        glTranslatef(position.x, position.y, position.z);
        
        
        transMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1f,0f,0f));
        transMatrix.rotate((float)Math.toRadians(rotation.y), new Vector3f(0f,1f,0f));
        transMatrix.translate(position);
    }
    
    public void Update()
    {
        if(cameraMode == FREEFLY)
            updateMovementFreeFly();

        if (Display.wasResized()) {
            glViewport(0, 0, Display.getWidth(), Display.getHeight());
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            gluPerspective(45.0f, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
        }
    }
    
    private void updateMovementFreeFly()
    {
        int delta = getDelta();
        
        if (Mouse.isGrabbed()) {
            float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
            float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f * -1;
            if (rotation.y + mouseDX >= 360) {
                rotation.y = rotation.y + mouseDX - 360;
            } else if (rotation.y + mouseDX < 0) {
                rotation.y = 360 - rotation.y + mouseDX;
            } else {
                rotation.y += mouseDX;
            }
            if (rotation.x + mouseDY >= 360) {
                rotation.x = rotation.x + mouseDY - 360;
            } else if (rotation.x + mouseDY < 0) {
                rotation.x = 360 - rotation.x + mouseDY;
            } else {
                rotation.x += mouseDY;
            }
        }

        boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        boolean moveFaster = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
        boolean moveSlower = Keyboard.isKeyDown(Keyboard.KEY_TAB);

        if (moveFaster && !moveSlower) {
            cameraSpeed *= 4f;
        }
        if (moveSlower && !moveFaster) {
            cameraSpeed /= 10f;
        }

        if (keyUp && keyRight && !keyLeft && !keyDown) {
            float angle = rotation.y + 45;
            Vector3f newPosition = new Vector3f(position);
            float schuine = (cameraSpeed * 0.0002f) * delta;
            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
            newPosition.z += aanliggende;
            newPosition.x -= overstaande;
            position.z = newPosition.z;
            position.x = newPosition.x;
        }
        if (keyUp && keyLeft && !keyRight && !keyDown) {
            float angle = rotation.y - 45;
            Vector3f newPosition = new Vector3f(position);
            float schuine = (cameraSpeed * 0.0002f) * delta;
            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
            newPosition.z += aanliggende;
            newPosition.x -= overstaande;
            position.z = newPosition.z;
            position.x = newPosition.x;
        }
        if (keyUp && !keyLeft && !keyRight && !keyDown) {
            float angle = rotation.y;
            Vector3f newPosition = new Vector3f(position);
            float schuine = (cameraSpeed * 0.0002f) * delta;
            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
            newPosition.z += aanliggende;
            newPosition.x -= overstaande;
            position.z = newPosition.z;
            position.x = newPosition.x;
        }
        if (keyDown && keyLeft && !keyRight && !keyUp) {
            float angle = rotation.y - 135;
            Vector3f newPosition = new Vector3f(position);
            float schuine = (cameraSpeed * 0.0002f) * delta;
            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
            newPosition.z += aanliggende;
            newPosition.x -= overstaande;
            position.z = newPosition.z;
            position.x = newPosition.x;
        }
        if (keyDown && keyRight && !keyLeft && !keyUp) {
            float angle = rotation.y + 135;
            Vector3f newPosition = new Vector3f(position);
            float schuine = (cameraSpeed * 0.0002f) * delta;
            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
            newPosition.z += aanliggende;
            newPosition.x -= overstaande;
            position.z = newPosition.z;
            position.x = newPosition.x;
        }
        if (keyDown && !keyUp && !keyLeft && !keyRight) {
            float angle = rotation.y;
            Vector3f newPosition = new Vector3f(position);
            float schuine = -(cameraSpeed * 0.0002f) * delta;
            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
            newPosition.z += aanliggende;
            newPosition.x -= overstaande;
            position.z = newPosition.z;
            position.x = newPosition.x;
        }
        if (keyLeft && !keyRight && !keyUp && !keyDown) {
            float angle = rotation.y - 90;
            Vector3f newPosition = new Vector3f(position);
            float schuine = (cameraSpeed * 0.0002f) * delta;
            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
            newPosition.z += aanliggende;
            newPosition.x -= overstaande;
            position.z = newPosition.z;
            position.x = newPosition.x;
        }
        if (keyRight && !keyLeft && !keyUp && !keyDown) {
            float angle = rotation.y + 90;
            Vector3f newPosition = new Vector3f(position);
            float schuine = (cameraSpeed * 0.0002f) * delta;
            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
            newPosition.z += aanliggende;
            newPosition.x -= overstaande;
            position.z = newPosition.z;
            position.x = newPosition.x;
        }
        if (flyUp && !flyDown) {
            double newPositionY = (cameraSpeed * 0.0002) * delta;
            position.y -= newPositionY;
        }
        if (flyDown && !flyUp) {
            double newPositionY = (cameraSpeed * 0.0002) * delta;
            position.y += newPositionY;
        }
        if (moveFaster && !moveSlower) {
            cameraSpeed /= 4f;
        }
        if (moveSlower && !moveFaster) {
            cameraSpeed *= 10f;
        }
        while (Mouse.next()) {
            if (Mouse.isButtonDown(0)) {
                Mouse.setGrabbed(true);
            }
            if (Mouse.isButtonDown(1)) {
                Mouse.setGrabbed(false);
            }

        }
        while (Keyboard.next()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                position = new Vector3f(0, 0, 0);
                rotation = new Vector3f(0, 0, 0);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
                mouseSpeed += 1;
                System.out.println("Mouse speed changed to " + mouseSpeed + ".");
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
                if (mouseSpeed - 1 > 0) {
                    mouseSpeed -= 1;
                    System.out.println("Mouse speed changed to " + mouseSpeed + ".");
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
                System.out.println("Walking speed changed to " + cameraSpeed + ".");
                cameraSpeed += 1;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                System.out.println("Walking speed changed to " + cameraSpeed + ".");
                cameraSpeed -= 1;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_F11)) {
                try {
                    Display.setFullscreen(!Display.isFullscreen());
                    if (!Display.isFullscreen()) {
                        Display.setResizable(true);
                        Display.setDisplayMode(new DisplayMode(800, 600));
                        glViewport(0, 0, Display.getWidth(), Display.getHeight());
                        glMatrixMode(GL_PROJECTION);
                        glLoadIdentity();
                        gluPerspective(45.0f, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
                        glMatrixMode(GL_MODELVIEW);
                        glLoadIdentity();
                    } else {
                        glViewport(0, 0, Display.getWidth(), Display.getHeight());
                        glMatrixMode(GL_PROJECTION);
                        glLoadIdentity();
                        gluPerspective(45.0f, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
                        glMatrixMode(GL_MODELVIEW);
                        glLoadIdentity();
                    }
                } catch (Exception ex) {

                }
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
}
