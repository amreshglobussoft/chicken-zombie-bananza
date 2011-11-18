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
package ucf.chickenzombiebonanza;

import ucf.chickenzombiebonanza.android.opengl.ShootingGameGLES20Renderer;
import ucf.chickenzombiebonanza.game.GameManager;
import ucf.chickenzombiebonanza.game.GameStateEnum;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

/**
 * 
 */
public class ShootingGameActivity extends AbstractGameActivity {
	
	private GLSurfaceView glView;
	
	private ShootingGameGLES20Renderer renderer;
	
	private PowerManager.WakeLock wl;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        renderer = new ShootingGameGLES20Renderer(this);
        glView = new ShootingGameSurfaceView(this);
        setContentView(glView);
        
		GameManager.getInstance().getPlayerOrientationPublisher().registerForOrientationUpdates(renderer);
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		glView.onPause();
		GameManager.getInstance().getPlayerOrientationPublisher().pauseSensor();
		wl.release();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		glView.onResume();
		GameManager.getInstance().getPlayerOrientationPublisher().resumeSensor();
		wl.acquire();
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
	    GameManager.getInstance().getPlayerOrientationPublisher().unregisterForOrientationUpdates(renderer);
	    
	}
	
	@Override
	public void onBackPressed() {
		//TODO: This is only temporary, needs to query the user first to ensure that they want to exit.
		GameManager.getInstance().updateGameState(GameStateEnum.GAME_NAVIGATION);
	}
	
	private class ShootingGameSurfaceView extends GLSurfaceView {
		public ShootingGameSurfaceView(Context context) {
			super(context);
			
			setEGLContextClientVersion(2);
			setRenderer(ShootingGameActivity.this.renderer);
		}
	}
}
