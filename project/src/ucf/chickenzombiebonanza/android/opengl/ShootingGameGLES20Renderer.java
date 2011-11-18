/**
 * Copyright (c) 2011, Chicken Zombie Bonanza Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Chicken Zombie Bonanza Project nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CHICKEN ZOMBIE BONANZA PROJECT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package ucf.chickenzombiebonanza.android.opengl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ucf.chickenzombiebonanza.common.LocalOrientation;
import ucf.chickenzombiebonanza.common.sensor.OrientationListener;
import ucf.chickenzombiebonanza.R;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

/**
 * 
 */
public class ShootingGameGLES20Renderer implements GLSurfaceView.Renderer, OrientationListener {
	
    private final String vertexShaderCode = 
        "uniform mat4 uMVPMatrix;   \n" +            
        "attribute vec4 vPosition;  \n" +
        "attribute vec4 vColor;  \n" +
        "varying vec4 fragColor;  \n" +
        "void main(){               \n" +
        " fragColor = vColor; \n" +
        " gl_Position = uMVPMatrix * vPosition; \n" +
        "}  \n";

    private final String fragmentShaderCode = 
        "precision mediump float;  \n" + 
        "varying vec4 fragColor;  \n" +
        "void main(){              \n" + 
        " gl_FragColor = fragColor; \n" + 
        "}                         \n";

    private int mProgram;
    private int maPositionHandle;
    private int colorHandle;
    
    private int muMVPMatrixHandle;
    private float[] mMVPMatrix = new float[16];
    private float[] mVMatrix = new float[16];
    private float[] mProjMatrix = new float[16];
    
    private int numPoints = 0;

    private FloatBuffer floorVertexBuffer, floorColorBuffer;
    
    private final Context shootingGameContext;
    
    public ShootingGameGLES20Renderer(Context shootingGameContext) {
        this.shootingGameContext = shootingGameContext;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1.0f);

        int vertexShader = loadShaderFromFile(GLES20.GL_VERTEX_SHADER, R.raw.simplevertshader);
        int fragmentShader = loadShaderFromFile(GLES20.GL_FRAGMENT_SHADER, R.raw.simplefragshader);

        mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram); // creates OpenGL program executables

        // get handle to the vertex shader's vPosition member
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        colorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
        
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        
        
        initFloor();
    }
    
    private void renderFloor() {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        
        GLES20.glVertexAttribPointer(maPositionHandle, 4, GLES20.GL_FLOAT, false, 16, floorVertexBuffer);
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 16, floorColorBuffer);
        GLES20.glEnableVertexAttribArray(colorHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, numPoints);
        
        GLES20.glDisable(GLES20.GL_BLEND);
    }
	
    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        renderFloor();
    }
    
    private static void gluPerspective(float[] matrix, float fovy, float aspect, float zNear, float zFar) {
        float top = zNear * (float) Math.tan(fovy * (Math.PI / 360.0));
        float bottom = -top;
        float left = bottom * aspect;
        float right = top * aspect;
        Matrix.frustumM(matrix, 0, left, right, bottom, top, zNear, zFar); 
    }

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		
		float ratio = (float) width / height;
        
		gluPerspective(mProjMatrix, 45.0f, ratio, 0.9f, 20.0f);
	}
	
    private int loadShaderFromFile(int type, int resourceId) {
        
        InputStream is = shootingGameContext.getResources().openRawResource(resourceId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        
        StringBuilder shaderCode = new StringBuilder();
        String readLine;
        
        try {
            while ((readLine = br.readLine()) != null) {
                shaderCode.append(readLine);
                shaderCode.append('\n');
            }
        } catch (IOException e) {
        }
        
        Log.d("Shader",shaderCode.toString());

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode.toString());
        GLES20.glCompileShader(shader);

        return shader;
    }

    private void initFloor() {
        
        float floorVertexArray[] = new float[]{
              0.0f,   0.0f, -1.0f, 1.0f,
              0.0f,  10.0f, -1.0f, 1.0f,
             10.0f,   0.0f, -1.0f, 1.0f,
              0.0f, -10.0f, -1.0f, 1.0f,
            -10.0f,   0.0f, -1.0f, 1.0f,
              0.0f,  10.0f, -1.0f, 1.0f,
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(floorVertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        floorVertexBuffer = vbb.asFloatBuffer();
        floorVertexBuffer.put(floorVertexArray);
        floorVertexBuffer.position(0);

        float floorColorArray[] = new float[]{
            0.2f, 0.6f, 0.0f, 1.0f,
            0.2f, 0.6f, 0.0f, 0.0f,
            0.2f, 0.6f, 0.0f, 0.0f,
            0.2f, 0.6f, 0.0f, 0.0f,
            0.2f, 0.6f, 0.0f, 0.0f,
            0.2f, 0.6f, 0.0f, 0.0f,
        };

        ByteBuffer cbb = ByteBuffer.allocateDirect(floorColorArray.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        floorColorBuffer = cbb.asFloatBuffer();
        floorColorBuffer.put(floorColorArray);
        floorColorBuffer.position(0);
        
        numPoints = 6;
    }

    @Override
    public void receiveOrientationUpdate(LocalOrientation orientation) {
        Matrix.setIdentityM(mVMatrix, 0);
        mVMatrix[0] = (float) orientation.getRight().u();
        mVMatrix[4] = (float) orientation.getRight().v();
        mVMatrix[8] = (float) orientation.getRight().w();
        
        mVMatrix[1] = (float) orientation.getUp().u();
        mVMatrix[5] = (float) orientation.getUp().v();
        mVMatrix[9] = (float) orientation.getUp().w();
        
        mVMatrix[2] = (float) orientation.getLookAt().u();
        mVMatrix[6] = (float) orientation.getLookAt().v();
        mVMatrix[10] = (float) orientation.getLookAt().w();
    }

}
