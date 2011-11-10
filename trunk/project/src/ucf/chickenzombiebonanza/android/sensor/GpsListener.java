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
package ucf.chickenzombiebonanza.android.sensor;

import geotransform.coords.Gcc_Coord_3d;
import geotransform.coords.Gdc_Coord_3d;
import geotransform.transforms.Gdc_To_Gcc_Converter;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import ucf.chickenzombiebonanza.common.GeocentricCoordinate;
import ucf.chickenzombiebonanza.common.sensor.PositionPublisher;

public class GpsListener extends PositionPublisher implements LocationListener {

	@Override
	public void onLocationChanged(Location location) {
		Gdc_Coord_3d gdc = new Gdc_Coord_3d(location.getLatitude(),location.getLongitude(),location.getAltitude());
		Gcc_Coord_3d gcc = new Gcc_Coord_3d();
		Gdc_To_Gcc_Converter.Convert(gdc, gcc);
		GeocentricCoordinate geo = new GeocentricCoordinate(gcc.x,gcc.y,gcc.z);
		this.updatePosition(geo);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// Do nothing
	}

	@Override
	public void onProviderEnabled(String provider) {
		// Do nothing		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Do nothing
	}

}
