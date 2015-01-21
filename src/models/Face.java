/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Dasty
 */
public class Face 
{
    public Vector3f vertex = new Vector3f(); //three indices
    public Vector3f normal = new Vector3f(); //three indices
    
    public Face(Vector3f v, Vector3f n)
    {
        vertex = v;
        normal = n;
    }
}
