package com.yf.EarthAndMoon;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by hwz on 2015-09-17.
 */
public class OpenGLSurfaceView extends GLSurfaceView {

    private OpenGLRender mOpenGLRender;

    public OpenGLSurfaceView(Context context) {
        this(context, null);
    }

    public OpenGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(2);
        requestFocus();
        setFocusableInTouchMode(true);

        mOpenGLRender = new OpenGLRender();
        setRenderer(mOpenGLRender);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    class OpenGLRender implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            GLES20.glClearColor(0,0,0,0);



        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int i, int i1) {

        }

        @Override
        public void onDrawFrame(GL10 gl10) {

        }
    }

}
