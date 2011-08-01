package com.frakle.wordtoss;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * This class is an object representation of 
 * a Square containing the vertex information
 * and drawing functionality, which is called 
 * by the renderer.
 * 
 * @author Savas Ziplies (nea/INsanityDesign)
 */
public class Square {
		
	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	
	/** The initial vertex definition */
	private float vertices[] = { 
			0.54988253f,-0.2932576f,-0.7990338f,
			0.59988254f,-0.2932576f,-0.7990338f,
			0.54988253f,-0.24325758f,-0.7990338f,
			0.59988254f,-0.24325758f,-0.7990338f
												};
	
	/**
	 * The Square constructor.
	 * 
	 * Initiate the buffers.
	 */
	public Square() {
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}

	/**
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL context
	 */
	public void draw(GL10 gl) {
		//Set the face rotation
		gl.glFrontFace(GL10.GL_CW);
		
		//Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		//Enable vertex buffer
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		//Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}