package com.frakle.wordtoss;

import java.nio.ByteBuffer;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

public class Letter {
	public boolean __DEBUG__ = true;
	private ByteBuffer byteBuf;
	private float[] point;
	private float qSize;
	private FloatBuffer thisQuad;
	private ByteBuffer indices;
	
	public Letter(float[] point, float qSize){
		this.point = point;
		this.qSize = qSize;
		byteBuf = ByteBuffer.allocateDirect(64);
		byteBuf.order(ByteOrder.nativeOrder());
		thisQuad = byteBuf.asFloatBuffer();
		
		
		//Initialize stuff to build the letter/quad
		indices = ByteBuffer.allocateDirect(6);
		indices.order(ByteOrder.nativeOrder());
		
		//since it'll always be a quad we'll assign the index statically
		indices.put(new byte[] {0,1,2,1,2,3});
		
		//calcuate our quad corners and put them in the buffer...
		
		thisQuad.put(new float[] {
				point[0],point[1]-qSize,point[2], 		// Bottom Left
				point[0]+qSize,point[1]-qSize,point[2], // Bottom Right
				point[0],point[1],point[2], 			// Top Left
				point[0]+qSize,point[1],point[2]		// Top Right
		});
		
		//flip'em to use'em
		thisQuad.flip();
		indices.flip();
	}
	public void draw(GL10 gl){

		//Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, thisQuad);
			
		//Set The Color To Blue
		gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);	
		
		gl.glDrawElements(GL10.GL_TRIANGLES, 6,
				GL10.GL_UNSIGNED_BYTE, indices);
		
	}
	
	// My theory was that could recalc every frame to make the quad's bottom
	// always face down... this doesn't really seem the case, so I'm not sure
	// what's going on...
	private FloatBuffer getQuad(float[] point){
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(64);
		byteBuf.order(ByteOrder.nativeOrder());
		FloatBuffer thisQuad = byteBuf.asFloatBuffer();
		thisQuad.put(new float[] {
				point[0],point[1]-qSize,point[2], 		// Bottom Left
				point[0]+qSize,point[1]-qSize,point[2], // Bottom Right
				point[0],point[1],point[2], 			// Top Left
				point[0]+qSize,point[1],point[2]		// Top Right
		});
		thisQuad.flip();
		return thisQuad;
	}
}
