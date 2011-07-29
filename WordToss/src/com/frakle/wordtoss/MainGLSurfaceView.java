package com.frakle.wordtoss;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class MainGLSurfaceView extends GLSurfaceView {
    public MainGLSurfaceView(Context context) {
        super(context);
        mRenderer = new MainGLSurfaceRenderer();
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
        	case MotionEvent.ACTION_MOVE:
        		float dx = x - mPreviousX;
        		float dy = y - mPreviousY;
        		mRenderer.mAngleX += dx * TOUCH_SCALE_FACTOR;
        		mRenderer.mAngleY += dy * TOUCH_SCALE_FACTOR;
        		requestRender();
        	}
        	mPreviousX = x;
        	mPreviousY = y;
        	return true;
        }

        private MainGLSurfaceRenderer mRenderer;
        private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
        private float mPreviousX;
        private float mPreviousY;
	
}


	
