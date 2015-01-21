/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 *
 * @author Dasty
 */
public class Model 
{
    private static int vboVertexHandle;
    private static int vboNormalHandle;
    
    public List<Vector3f> vertices = new ArrayList<Vector3f>();
    public List<Vector3f> normals = new ArrayList<Vector3f>();
    public List<Face> faces = new ArrayList<Face>();
    
    public void setUp()
    {
        vboVertexHandle = glGenBuffers();
        vboNormalHandle = glGenBuffers();
        
        FloatBuffer vs = reserveData(faces.size() * 36);
        FloatBuffer ns = reserveData(faces.size() * 36);
        
        for(Face face : faces)
        {
            vs.put(asFloats(vertices.get((int) face.vertex.x - 1)));
            vs.put(asFloats(vertices.get((int) face.vertex.y - 1)));
            vs.put(asFloats(vertices.get((int) face.vertex.z - 1)));
            
            ns.put(asFloats(normals.get((int) face.normal.x - 1)));
            ns.put(asFloats(normals.get((int) face.normal.y - 1)));
            ns.put(asFloats(normals.get((int) face.normal.z - 1)));
        }
        vs.flip();
        ns.flip();
        
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vs, GL_STATIC_DRAW);
        
        glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
        glBufferData(GL_ARRAY_BUFFER, ns, GL_STATIC_DRAW);
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        System.out.println("Setup Complete");
    }
    
    public void draw()
    {
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
        glNormalPointer(GL_FLOAT, 0, 0L);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glColor3f(0.4f,.27f,.17f);
        glMaterialf(GL_FRONT, GL_SHININESS, 10f);
        
        glDrawArrays(GL_TRIANGLES, 0, faces.size() * 3);
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    public void destroy()
    {
        glDeleteBuffers(vboVertexHandle);
        glDeleteBuffers(vboNormalHandle);
    }
    
    private static float[] asFloats(Vector3f v)
    {
        return new float[]{v.x,v.y,v.z};
    }
    
    private static FloatBuffer reserveData(int size)
    {
        FloatBuffer data = BufferUtils.createFloatBuffer(size);
        return data;
    }
}
