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
package ucf.chickenzombiebonanza.common;

public class LocalOrientation {
	private double azimuth, pitch, roll;
	
	public LocalOrientation(double azimuth, double pitch, double roll) {
		this.azimuth = azimuth;
		this.pitch = pitch;
		this.roll = roll;
	}
	
	public double getAzimuth() {
		return azimuth;
	}
	
	public double getPitch() {
		return pitch;
	}
	
	public double getRoll() {
		return roll;
	}
	
	public float[] toMatrix() {
	    float[] matrix = new float[9];
	    
	    float sinX = (float)Math.sin(pitch);
	    float cosX = (float)Math.cos(pitch);
	    float sinY = (float)Math.sin(azimuth);
	    float cosY = (float)Math.cos(azimuth);
	    float sinZ = (float)Math.sin(roll);
	    float cosZ = (float)Math.cos(roll);
	    
	    matrix[0] = cosY*cosZ;
	    matrix[1] = cosZ*sinX*sinY-cosX*sinZ;
	    matrix[2] = cosX*cosZ*sinY+sinX*sinZ;
	    matrix[3] = cosY*sinZ;
	    matrix[4] = cosX*cosZ+sinX*sinY*sinZ;
	    matrix[5] = -cosZ*sinX+cosX*sinY*sinZ;
	    matrix[6] = -sinY;
	    matrix[7] = cosY*sinX;
	    matrix[8] = cosX*cosY;
	    
	    return matrix;
	}
}
