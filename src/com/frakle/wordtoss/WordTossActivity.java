package com.frakle.wordtoss;


import com.frakle.wordtoss.MainGLSurfaceView;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class WordTossActivity extends Activity {
	//public float[][] letterPlots;
	/** The OpenGL View */
	private GLSurfaceView glSurface;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//Create an Instance with this Activity
		glSurface = new MainGLSurfaceView(this);

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
    

