package com.frakle.wordtoss;

import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import java.util.Arrays;

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
		//Calling without a desired plotCount returns 500 plots.
		this(500);
	}
	/**	 
	 * * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 */
	public void draw(GL10 gl) {
		FloatBuffer curPlot;
		//float[] thisQuad;
		//
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(48);
		byteBuf.order(ByteOrder.nativeOrder());
		curPlot = byteBuf.asFloatBuffer();
		while(quadPlots.hasRemaining()){
			gl.glFrontFace(GL10.GL_CW);
			float[] thisQuad = { quadPlots.get(), quadPlots.get(), quadPlots.get(),
								 quadPlots.get(), quadPlots.get(), quadPlots.get(),
								 quadPlots.get(), quadPlots.get(), quadPlots.get(),
								 quadPlots.get(), quadPlots.get(), quadPlots.get()
			};
			curPlot.put(thisQuad);
			//Point to our vertex buffer
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, curPlot);
		
			//Enable vertex buffer
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			
			//Set The Color To Blue
			gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);	
			
			//Draw the vertices as points
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, curPlot.capacity() / 3);
			
			//gl.glDrawElements(GL10.GL_TRIANGLE_STRIP,quadPlots.capacity(),
			//				GL10.GL_FLOAT, quadPlots.capacity() / 3);
			//Disable the client state before leaving
			curPlot.clear();
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
		
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
    	// Generate a bunch of quads (squares) based on the single
    	// points given to us. The point will be the Top Left corner.
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
    		//;
    		float[] thisQuad = {
    							thisX,thisY-0.05f,thisZ, 		// Bottom Left
    							thisX+0.05f,thisY-0.05f,thisZ, 	// Bottom Right
    							thisX,thisY,thisZ, 				// Top Left
    							thisX+0.05f,thisY,thisZ			// Top Right
    		};
    		Log.v("Blah","Location: "+thisQuad[0]+","+thisQuad[1]+","+thisQuad[2]);
    		Log.v("Blah","Location: "+thisQuad[3]+","+thisQuad[4]+","+thisQuad[5]);
    		Log.v("Blah","Location: "+thisQuad[6]+","+thisQuad[7]+","+thisQuad[8]);
    		Log.v("Blah","Location: "+thisQuad[9]+","+thisQuad[10]+","+thisQuad[11]);
    		quads.put(thisQuad);
    		count++;
    	}
    	return quads;
    }
}