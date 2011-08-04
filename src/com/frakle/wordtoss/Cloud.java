package com.frakle.wordtoss;

import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;

import com.frakle.wordtoss.Letter;

public class Cloud {
	/**
	 * The Cloud constructor. e
	 * 
	 * Initiate the buffers.
	 */
	public boolean __DEBUG__ = true;
	public int plotCount;
	public FloatBuffer letterPlots;
	public float qSize = 0.08f;
	public Letter[] letters;

	public Cloud(int plotCount) {
		this.plotCount = plotCount;
		Letter[] letters = new Letter[plotCount];
		// Generate the cloud for display
		// number of plots+1, multiply by 3 (3 entries per point)
		// multiply by 4 to compensate for float.
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(((plotCount+1)*3)*4);
		byteBuf.order(ByteOrder.nativeOrder());
		letterPlots = byteBuf.asFloatBuffer();
		
		//Generate a set of locations for us to place quads
		for (int i=0; i<= plotCount;i++){
			letterPlots.put(generatePlot());
		}
		letterPlots.flip();

		try{
		for(int i=0;i<=plotCount;i++){
			letters[i] = new Letter(
					new float[] { letterPlots.get(),letterPlots.get(),letterPlots.get()},
					qSize);
		}
		}catch(Exception e){Log.v("Blah",e.toString());}
		Log.v("Blah","Gen'd Letters");
		this.letters = letters;
	}
	
	public Cloud(){
		//Calling without a plotCount returns 50 plots.
		this(25);
	}
	/**	 
	 * * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 */
	//
	public void draw(GL10 gl) {
		
		gl.glPushMatrix();

			gl.glFrontFace(GL10.GL_CW);
			
			//Enable vertex buffer
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			
			for(int i=0;i<letters.length;i++){
				letters[i].draw(gl);
			}
			
			//Disable the client state before leaving
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPopMatrix();
		
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