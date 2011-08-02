package com.frakle.wordtoss;

import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;

public class Cloud {
	public boolean __DEBUG__ = true;
	public FloatBuffer letterPlots;
	public FloatBuffer quadPlots;
	public ByteBuffer indices;
	/**
	 * The Cloud constructor. e
	 * 
	 * Initiate the buffers.
	 */
	

	public Cloud(int plotCount) {
		// Generate the cloud for display
		//Letter Plots
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(((plotCount+1)*3)*4);
		byteBuf.order(ByteOrder.nativeOrder());
		letterPlots = byteBuf.asFloatBuffer();
		
		//Generate a set of locations for us to place quads
		for (int i=0; i<= plotCount;i++){
			letterPlots.put(generatePlot());
		}
		letterPlots.position(0);
		
		//Calculate those quad's corner points.
		quadPlots = generateQuadsFromPoints(letterPlots);
		
		//Generate an triangle index for each quad. 
		indices = ByteBuffer.allocateDirect(((quadPlots.capacity()+1)*6)+6);
		indices = this.generateQuadIndex(quadPlots.capacity()+1);
		
		letterPlots.position(0);
		quadPlots.position(0);
		indices.position(0);
		
		// out put some stats
		if(__DEBUG__){
			Log.v("blah","letCap: "+letterPlots.capacity());
			Log.v("blah","quadCap: "+quadPlots.capacity());
			Log.v("blah","indCap: "+indices.capacity());
		}

	}
	
	public Cloud(){
		//Calling without a plotCount returns 500 plots.
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

		gl.glFrontFace(GL10.GL_CW);

		//Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, quadPlots);
		
		//Enable vertex buffer
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			
		//Set The Color To Blue
		gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);	
			
		//Draw the vertices as points
		//gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, curPlot.capacity() / 4);
		try{	
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.capacity(),
					GL10.GL_UNSIGNED_BYTE, indices);
		}catch(Exception e){ 
			Log.e("WordToss",e.toString()); 
		};
		
		//reset indices position so we can do this all over again.
		//indices.position(0);
		
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
    	// Generate a bunch of quads (squares) based on the single
    	// points given to us. The point will be the Top Left corner.
    	FloatBuffer toReturn;
    	
		ByteBuffer byteBuf = ByteBuffer.allocateDirect((((points.capacity()/3)*4)*4)*4);
		byteBuf.order(ByteOrder.nativeOrder());
		toReturn = byteBuf.asFloatBuffer();
		
		// Loop over every point we're given and generate 3 more around it. 
    	while(points.hasRemaining()){
    		// Just to make things a bit more readable.
    		float thisX = points.get();
    		float thisY = points.get();
    		float thisZ = points.get();
    		
    		// add .05f to each point's needed coordinate 
    		float[] thisQuad = {
    							thisX,thisY-0.05f,thisZ, 		// Bottom Left
    							thisX+0.05f,thisY-0.05f,thisZ, 	// Bottom Right
    							thisX,thisY,thisZ, 				// Top Left
    							thisX+0.05f,thisY,thisZ			// Top Right
    		};
    		
    		toReturn.put(thisQuad);
    	}
    	return toReturn;
    }
    public ByteBuffer generateQuadIndex(int indexCount){
    	// We need to create triangle points (6) for each quad we're generating.
    	ByteBuffer toReturn = ByteBuffer.allocateDirect((indexCount*6)+6);
		toReturn.order(ByteOrder.nativeOrder());
    	int curPoint = 0;

    	for(int i=0;i<=indexCount;i++){
    		byte thisIndex[] = {
    				(byte) curPoint, (byte) (curPoint+1), (byte) (curPoint+2),
		    		(byte) (curPoint+1), (byte) (curPoint+2), (byte) (curPoint+3)
		    		};
    		//Arrays.deepToString(Byte temp[] = thisIndex[]);
    		curPoint=curPoint+4;
    		toReturn.put(thisIndex);
    		Log.v("blah","Out"+thisIndex[0]);
    	}
    	if(__DEBUG__){
    		Log.v("Blah","Exiting indices gen.");
    	}
		return toReturn;
    };
}