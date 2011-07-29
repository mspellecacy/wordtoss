package com.frakle.wordtoss;

import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Cloud {
	
	public FloatBuffer letterPlots;
	public FloatBuffer quadPlots;
	/**
	 * The Cloud constructor. e
	 * 
	 * Initiate the buffers.
	 */
	

	public Cloud(int plotCount) {
		// Generate the cloud for display
		//ByteBuffer.all
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(((plotCount+1)*3)*4);
		byteBuf.order(ByteOrder.nativeOrder());
		letterPlots = byteBuf.asFloatBuffer();
		try{
			for (int i=0; i<= plotCount;i++){
				letterPlots.put(generatePlot());
			}
		}catch(Exception e){
			Log.v("blah",e.toString());
			Log.v("blah","Count: "+letterPlots.capacity());
		};
		letterPlots.position(0);
		Log.v("blah","Capacity: "+letterPlots.capacity());
		quadPlots = generateQuadsFromPoints(letterPlots);
		letterPlots.position(0);
		quadPlots.position(0);
	}
	public Cloud(){
			this(500);
	}
	/**
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 */
	public void draw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CW);
		//Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, quadPlots);
	
		//Enable vertex buffer
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		//Set The Color To Blue
		gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);	
			
		//Draw the vertices as points
		//gl.glDrawArrays(GL10.GL_POINTS, 0, quadPlots.capacity() / 4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, quadPlots.capacity() / 4);
		
		//gl.glDrawArrays(GL10.GL_VERTEX_ARRAY, first, count)
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
    
    public float[] generatePlot(){

    		float x = (float) (Math.random() - 0.5);
    		float y = (float) (Math.random() - 0.5); 
    		float z = (float) (Math.random() - 0.5);
    		float k = (float) Math.sqrt(x*x + y*y + z*z);
    		while (k < 0.2 || k > 0.3)
    		{
    			x = (float) (Math.random() - 0.5);
    			y = (float) (Math.random() - 0.5);
    			z = (float) (Math.random() - 0.5);
    			k = (float) Math.sqrt(x*x + y*y + z*z);
    		}
    		float[] toReturn = {x/k,y/k,z/k};
    		return toReturn;
    }
    
    public FloatBuffer generateQuadsFromPoints(FloatBuffer points){
    	FloatBuffer quads;
    	int count = 0;
    	
		ByteBuffer byteBuf = ByteBuffer.allocateDirect((((points.capacity()/3)*4)*4)*4);
		byteBuf.order(ByteOrder.nativeOrder());
		quads = byteBuf.asFloatBuffer();
		
    	while(points.hasRemaining()){
    		Log.v("Blah","Position" + count);
    		float thisX = points.get();
    		float thisY = points.get();
    		float thisZ = points.get();
    		//Log.v("Blah","Location: "+thisX+","+thisY+","+thisZ);
    		float[] thisQuad = {
    							thisX,thisY-0.05f,thisZ, 		// Bottom Left
    							thisX+0.05f,thisY-0.05f,thisZ, 	// Bottom Right
    							thisX,thisY,thisZ, 				// Top Left
    							thisX+0.05f,thisY,thisZ			// Top Right
    		};
    		quads.put(thisQuad);
    		count++;
    	}
    	return quads;
    }
}