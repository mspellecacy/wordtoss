package com.frakle.wordtoss;

import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;

public class Cloud {
	/**
	 * The Cloud constructor. e
	 * 
	 * Initiate the buffers.
	 */
	public boolean __DEBUG__ = true;
	public FloatBuffer letterPlots;
	public FloatBuffer quadPlots;
	public ByteBuffer indices;
	public float qSize = 0.08f;

	public Cloud(int plotCount) {
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
		letterPlots.position(0);
		
		//Calculate those quad's corner points.
		quadPlots = generateQuadsFromPoints(letterPlots);
		
		//Generate an triangle index for each quad. 
		indices = ByteBuffer.allocateDirect(((quadPlots.capacity()+1)*6)+6);
		indices = this.generateQuadIndex(quadPlots.capacity()+1);
		
		letterPlots.flip();
		quadPlots.flip();
		indices.flip();
		
		// out put some stats
		if(__DEBUG__){
			Log.v("blah","letCap: "+letterPlots.capacity());
			Log.v("blah","quadCap: "+quadPlots.capacity());
			Log.v("blah","indCap: "+indices.capacity());
		}

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
	
			//Point to our vertex buffer
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, quadPlots);
			
			//Enable vertex buffer
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				
			//Set The Color To Blue
			gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);	
				
			//Draw the triangles. 
			try{	
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.capacity(),
						GL10.GL_UNSIGNED_BYTE, indices);
			}catch(Exception e){ 
				Log.e("WordToss",e.toString()); 
			};
			
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
    
    public FloatBuffer generateQuadsFromPoints(FloatBuffer points){
    	// Generate a bunch of quads (squares) based on the single
    	// points given to us. The point will be the Top Left corner.
    	FloatBuffer toReturn;
    	// 3 cords per point, we just want the actual number of 'points'
    	// multiply it out to compensate for converting to float.
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
    							thisX,thisY-qSize,thisZ, 		// Bottom Left
    							thisX+qSize,thisY-qSize,thisZ, 	// Bottom Right
    							thisX,thisY,thisZ, 				// Top Left
    							thisX+qSize,thisY,thisZ			// Top Right
    		};
    		
    		toReturn.put(thisQuad);
    	}
    	return toReturn;
    }
    
    public void update(GL10 gl){
    	quadPlots.clear();
		quadPlots = generateQuadsFromPoints(letterPlots);
		quadPlots.flip();
    };
    
    public ByteBuffer generateQuadIndex(int indexCount){
    	// We need to create triangle vertexes (quad = 2 triangles, 2 tri's = 6 points ) 
    	// for each quad we're generating.
    	ByteBuffer toReturn = ByteBuffer.allocateDirect((indexCount*6)+6);
		toReturn.order(ByteOrder.nativeOrder());
    	int curPoint = 0;

    	for(int i=0;i<=indexCount;i++){
    		byte thisIndex[] = {
    				(byte) curPoint, (byte) (curPoint+1), (byte) (curPoint+2),
		    		(byte) (curPoint+1), (byte) (curPoint+2), (byte) (curPoint+3)
		    		};
    		curPoint=curPoint+4;
    		toReturn.put(thisIndex);
    	}
    	if(__DEBUG__){
    		Log.v("Blah","Exiting indices gen.");
    	}
    	//toReturn.flip();
		return toReturn;
    };
}