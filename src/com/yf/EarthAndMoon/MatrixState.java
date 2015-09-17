package com.yf.EarthAndMoon;

import android.opengl.Matrix;

/**
 * Created by Administrator on 9/5 0005.
 */
public class MatrixState {

    private static final int STACK_SIZE = 10;

    private static int mCurrentPosition = -1; // 栈中栈顶元素的位置索引
    private static float[][] mStackArray = new float[STACK_SIZE][16];
    private static float[] mCurrentMatrixArray = new float[16];
    private static float[] mCameraMatrixArray = new float[16];
    private static float[] mProjectMatrixArray = new float[16];
    private static float[] mLightPosition = new float[3];

    public static void initMatrixArray() {
        // Matrix.setIdentityM();
        Matrix.setRotateM(mCurrentMatrixArray, 0, 0, 1, 0, 0);
    }

    public static void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy,
        float upz) {
        Matrix.setLookAtM(mCameraMatrixArray, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    public static void setProjectFrustum(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(mProjectMatrixArray, 0, left, right, bottom, top, near, far);
    }

    public static void saveCurrentMatrixArray() {
        if (-1 <= mCurrentPosition && mCurrentPosition + 1 < STACK_SIZE) {
            mCurrentPosition++;
            for (int i = 0; i < 16; i++) {
                mStackArray[mCurrentPosition][i] = mCurrentMatrixArray[i];
            }
            return;
        }
        throw new ArrayIndexOutOfBoundsException("数组已经越界，无法继续保存");
    }

    public static void restoreCurrentMatrixArray() {
        if (-1 < mCurrentPosition && mCurrentPosition < STACK_SIZE) {
            for (int i = 0; i < 16; i++) {
                mCurrentMatrixArray[i] = mStackArray[mCurrentPosition][i];
            }
            mCurrentPosition--;
            return;
        }
        throw new ArrayIndexOutOfBoundsException("已经为栈底或者索引越界，恢复失败");
    }

    public static void translate(float x, float y, float z) {
        Matrix.translateM(mCurrentMatrixArray, 0, x, y, z);
    }

    public static void rotate(float degree, float x, float y, float z) {
        Matrix.rotateM(mCurrentMatrixArray, 0, degree, x, y, z);
    }

    public static void scale(float x, float y, float z) {
        Matrix.scaleM(mCurrentMatrixArray, 0, x, y, z);
    }

    private static float[] tempFinalMatrixArray = new float[16];

    public static float[] getFinalMatrixArray() {
        Matrix.multiplyMM(tempFinalMatrixArray, 0, mCameraMatrixArray, 0, mCurrentMatrixArray, 0);
        Matrix.multiplyMM(tempFinalMatrixArray, 0, mProjectMatrixArray, 0, tempFinalMatrixArray, 0);
        return tempFinalMatrixArray;
    }

    public static float[] getCurrentMatrixArray() {
        return mCurrentMatrixArray;
    }

    public static float[] getCameraPositionArray() {
        return mCameraMatrixArray;
    }

    public static float[] getLightPositionArray() {
        return mLightPosition;
    }

    public static void setLightPosition(float x, float y, float z) {
        mLightPosition[0] = x;
        mLightPosition[1] = y;
        mLightPosition[2] = z;
    }
}
