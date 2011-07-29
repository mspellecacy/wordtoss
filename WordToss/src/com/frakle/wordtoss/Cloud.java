package com.frakle.wordtoss;


import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Cloud {
	
	public FloatBuffer letterPlots;
	/**
	 * The Cloud constructor. e
	 * 
	 * Initiate the buffers.
	 */
	public Cloud() {
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(3000);
		byteBuf.order(ByteOrder.nativeOrder());
		letterPlots = byteBuf.asFloatBuffer();
		try{
		for (int i=0; i<= 500;i++){
			letterPlots.put(generatePlot());
		}
		}catch(Exception e){
			Log.v("blah",e.toString());
			Log.v("blah","Count: "+letterPlots.capacity());
		};
		letterPlots.position(0);
		
	}

	/**
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 */
	public void draw(GL10 gl) {		

		//Set the face rotation
		gl.glFrontFace(GL10.GL_CW);
		
		//Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, letterPlots);

		//Enable vertex buffer
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		//Set The Color To Blue
		gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);	
		
		//Draw the vertices as points
		gl.glDrawArrays(GL10.GL_POINTS, 0, letterPlots.capacity() / 3);
		
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
}