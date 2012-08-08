/*
 * PanoramaGL library
 * Version 0.1
 * Copyright (c) 2010 Javier Baez <javbaezga@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.panoramagl;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.widget.RelativeLayout;

import com.panoramagl.enumeration.PLTouchStatus;
import com.panoramagl.ios.enumeration.UIDeviceOrientation;
import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.ios.structs.CGSize;
import com.panoramagl.transitions.PLITransition;

public interface PLIView
{
	/**reset methods*/
	
	void reset();
	void resetWithoutAlpha();
	void resetSceneAlpha();
	
	/**property methods*/
	
	PLIPanorama getPanorama();
	void setPanorama(PLIPanorama panorama);
	
	float getAnimationInterval();
	void setAnimationInterval(float interval);
	
	int getAnimationFrameInterval();
	void setAnimationFrameInterval(int interval);
	
	boolean isAnimating();
	
	float getAccelerometerInterval();
	void setAccelerometerInterval(float interval);

	float getAccelerometerSensitivity();
	void setAccelerometerSensitivity(float sensitivity);
	
	boolean isBlocked();
	void setBlocked(boolean isBlocked);
	
	boolean isValidForFov();
	
	boolean isValidForInertia();
	
	float getShakeThreshold();
	void setShakeThreshold(float shakeThreshold);
	
	boolean isValidForTransition();
	
	PLTouchStatus getTouchStatus();
	
	boolean isPointerVisible();
	void setPointerVisible(boolean isPointerVisible);

	boolean isAccelerometerEnabled();
	void setAccelerometerEnabled(boolean isAccelerometerEnabled);

	boolean isAccelerometerLeftRightEnabled();
	void setAccelerometerLeftRightEnabled(boolean isAccelerometerLeftRightEnabled);

	boolean isAccelerometerUpDownEnabled();
	void setAccelerometerUpDownEnabled(boolean isAccelerometerUpDownEnabled);

	CGPoint getStartPoint();
	void setStartPoint(CGPoint startPoint);

	CGPoint getEndPoint();
	void setEndPoint(CGPoint endPoint);

	boolean isScrollingEnabled();
	void setScrollingEnabled(boolean isScrollingEnabled);

	int getMinDistanceToEnableScrolling();
	void setMinDistanceToEnableScrolling(int minDistanceToEnableScrolling);
	
	int getMinDistanceToEnableDrawing();
	void setMinDistanceToEnableDrawing(int minDistanceToEnableDrawing);

	boolean isInertiaEnabled();
	void setInertiaEnabled(boolean isInertiaEnabled);

	float getInertiaInterval();
	void setInertiaInterval(float inertiaInterval);

	boolean isResetEnabled();
	void setResetEnabled(boolean isResetEnabled);
	
	boolean isShakeResetEnabled();
	void setShakeResetEnabled(boolean isShakeResetEnabled);
	
	boolean isRendererCreated();

	PLCamera getCamera();
	void setCamera(PLCamera camera);
	
	float getSceneAlpha();
	void setSceneAlpha(float alpha);
	
	int getNumberOfTouchesForReset();
	void setNumberOfTouchesForReset(int value);
	
	boolean isRunningSensorialRotation();
	
	UIDeviceOrientation getCurrentDeviceOrientation();
	
	Activity getActivity();
	
	PLViewEventListener getListener();
	void setListener(PLViewEventListener listener);
	
	GL10 getCurrentGL();
	CGSize getSize();
	
	RelativeLayout getControlsLayout();
	
	/**draw methods*/

	void drawView();
	void drawViewNTimes(int times);
	
	/**animation methods*/
	
	void startAnimation();
	void stopAnimation();
	
	/**sensorial rotation methods*/

	void startSensorialRotation();
	void stopSensorialRotation();
	
	/**transition methods*/
	
	boolean executeTransition(PLITransition transition);
	
	/**progressbar methods*/

	boolean isProgressBarVisible();
	boolean showProgressBar();
	boolean hideProgressBar();
	boolean resetProgressBar();
	
	/**clear methods*/

	void clear();

	/**load methods*/

	void load(PLILoader loader);
}