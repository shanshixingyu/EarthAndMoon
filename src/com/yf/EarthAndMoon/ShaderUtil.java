package com.yf.EarthAndMoon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 9/6 0006.
 */
public class ShaderUtil {
    private static final String TAG = ShaderUtil.class.getName();

    public static String readSourceFromAssetsFile(String fileName, Resources resources) {
        String result = null;
        if (!TextUtils.isEmpty(fileName) && resources != null) {
            try {
                InputStream inputStream = resources.getAssets().open(fileName);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int len;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                result = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
                result = result.replaceAll("\\r\\n", "\n");
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }
        }
        return result;
    }

    private static int loadShader(int shaderType, String shaderSource) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, shaderSource);
            GLES20.glCompileShader(shader);
            int[] result = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, result, 0);
            if (result[0] == 0) {
                Log.w(TAG, "加载Shader失败 shaderTyp=" + shaderType);
                Log.w(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    private static void checkGLError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            // Log.w(TAG, "操作：" + op + ",错误：" + error);
            throw new IllegalStateException("操作：" + op + "错误,错误为：" + error);
        }
    }

    public static int createProgram(String vertexShaderSource, String fragmentShaderSource) {
        if (TextUtils.isEmpty(vertexShaderSource) || TextUtils.isEmpty(fragmentShaderSource)) {
            throw new IllegalArgumentException("传入的顶点着色器源码为空或者传入的片元着色器源码为空");
        }

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
        Log.i(TAG, "vertexShader=" + vertexShader);
        if (vertexShader == 0) {
            return 0;
        }

        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);
        Log.i(TAG, "fragmentShader=" + fragmentShader);
        if (fragmentShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            checkGLError("AttachVertexShader");
            GLES20.glAttachShader(program, fragmentShader);
            checkGLError("AttachFragmentShader");

            GLES20.glLinkProgram(program);
            int[] result = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, result, 0);
            if (result[0] == 0) {
                Log.w(TAG, "加载程序失败");
                Log.w(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

}
