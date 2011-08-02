package com.frakle.wordtoss;


import com.frakle.wordtoss.MainGLSurfaceView;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class WordTossActivity extends Activity {
	//public float[][] letterPlots;
	/** The OpenGL View */
	private GLSurfaceView glSurface;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//Create an Instance with this Activity
		glSurface = new MainGLSurfaceView(this);
		//Set our own Renderer
		//glSurface.setRenderer(new MainGLSurfaceView());
		//Set the GLSurface as View to this Activity
		setContentView(glSurface);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurface.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurface.onResume();
    }
}
    

