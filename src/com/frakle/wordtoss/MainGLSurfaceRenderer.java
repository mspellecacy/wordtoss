package com.frakle.wordtoss;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

class MainGLSurfaceRenderer implements GLSurfaceView.Renderer {
	private Cloud cloud;
	private float rcloud;
    public float mAngleX;
    public float mAngleY;
    public int cloudSize = 75;
	
	public MainGLSurfaceRenderer(){
		cloud = new Cloud(cloudSize);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		
		gl.glPushMatrix();
			//Clear Screen And Depth Buffer
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
			gl.glLoadIdentity();					//Reset The Current Modelview Matrix
			
			gl.glTranslatef(-0.75f, 0.0f, -3.0f);  // Move the viewport so we can see things.
	        gl.glRotatef(mAngleX, 0f, 1f, 0f);
	        gl.glRotatef(mAngleY, 1f, 0f, 0f);	
			//gl.glRotatef(rcloud, 1.0f, 0.0f, 0.0f);
			cloud.draw(gl);						//Draw the cloud
			rcloud -= 0.35f;
		gl.glPopMatrix();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}
		//gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 70.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {		
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
	}

}