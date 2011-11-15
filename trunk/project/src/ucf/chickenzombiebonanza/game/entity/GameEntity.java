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
package ucf.chickenzombiebonanza.game.entity;

import java.util.ArrayList;
import java.util.List;

import ucf.chickenzombiebonanza.common.GeocentricCoordinate;
import ucf.chickenzombiebonanza.common.LocalOrientation;
import ucf.chickenzombiebonanza.common.sensor.OrientationListener;
import ucf.chickenzombiebonanza.common.sensor.PositionListener;
import ucf.chickenzombiebonanza.common.sensor.PositionPublisher;

public abstract class GameEntity implements PositionListener, OrientationListener {
	
	private static int nextAvailableId = 0;
	
	private int id;
	
	private GeocentricCoordinate position;
	
	private LocalOrientation orientation;
	
	private final PositionPublisher positionPublisher = new PositionPublisher();
	
	private final List<GameEntityTagEnum> tags = new ArrayList<GameEntityTagEnum>(1);
	
	private final List<GameEntityStateListener> stateListeners = new ArrayList<GameEntityStateListener>();
	
	public GameEntity(GeocentricCoordinate position, LocalOrientation orientation, GameEntityTagEnum tag) {
		this.id = nextAvailableId;
		this.position = position;
		this.orientation = orientation;
		this.tags.add(tag);
		
		nextAvailableId += 1;
	}
	
	public int getId() {
		return id;
	}
	
	public GeocentricCoordinate getPosition() {
		return position;
	}
	
	public LocalOrientation getOrientation() {
		return orientation;
	}
	
	public final PositionPublisher getPositionPublisher() {
		return positionPublisher;
	}
	
	public List<GameEntityTagEnum> getTags() {
		return tags;
	}
	
	public final void receivePositionUpdate(GeocentricCoordinate coordinate) {
		this.position = coordinate;
		positionPublisher.updatePosition(this.position);
	}
	
	public final void receiveOrientationUpdate(LocalOrientation orientation) {
		this.orientation = orientation;
	}
	
	public void destroyEntity() {
		
	}
	
	abstract public void interactWith(GameEntity entity);
	
	public final void registerGameEntityStateListener(GameEntityStateListener listener) {
		if(!stateListeners.contains(listener)) {
			stateListeners.add(listener);
		}
	}
	
	public final void unregisterGameEntityStateListener(GameEntityStateListener listener) {
		stateListeners.remove(listener);
	}

}
