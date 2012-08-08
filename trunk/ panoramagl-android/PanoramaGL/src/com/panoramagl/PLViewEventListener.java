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

import java.util.List;

import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.ios.UITouch;
import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.ios.structs.UIAcceleration;
import com.panoramagl.structs.PLPosition;
import com.panoramagl.transitions.PLITransition;

import android.hardware.SensorEvent;
import android.view.MotionEvent;

public abstract class PLViewEventListener
{
	/**touch methods*/
	
	public void onTouchesBegan(PLIView pView, List<UITouch> touches, MotionEvent event)
	{	
	}
	
	public void onTouchesMoved(PLIView pView, List<UITouch> touches, MotionEvent event)
	{
	}
	
	public void onTouchesEnded(PLIView pView, List<UITouch> touches, MotionEvent event)
	{
	}
	
	public boolean onShouldBeginTouching(PLIView pView, List<UITouch> touches, MotionEvent event)
	{
		return true;
	}
	
	public void onDidBeginTouching(PLIView pView, List<UITouch> touches, MotionEvent event)
	{
	}
	
	public boolean onShouldTouch(PLIView pView, List<UITouch> touches, MotionEvent event)
	{
		return true;
	}
	
	public void onDidTouch(PLIView pView, List<UITouch> touches, MotionEvent event)
	{
	}
	
	public boolean onShouldEndTouching(PLIView pView, List<UITouch> touches, MotionEvent event)
	{
		return true;
	}
	
	public void onDidEndTouching(PLIView pView, List<UITouch> touches, MotionEvent event)
	{
	}
	
	/**accelerate methods*/
	
	public boolean onShouldAccelerate(PLIView pView, UIAcceleration acceleration, SensorEvent event)
	{
		return true;
	}
	
	public void onDidAccelerate(PLIView pView, UIAcceleration acceleration, SensorEvent event)
	{
	}
	
	/**inertia methods*/

	public boolean onShouldBeginInertia(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
		return true;
	}
	
	public void onDidBeginInertia(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
	}
	
	public boolean onShouldRunInertia(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
		return true;
	}
	
	public void onDidRunInertia(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
	}
	
	public void onDidEndInertia(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
	}
	
	/**moving methods*/

	public void onDidBeginMoving(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{	
	}
	
	public boolean onShouldRunMoving(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
		return true;
	}
	
	public void onDidRunMoving(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
	}
	
	public void onDidEndMoving(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
	}
	
	public boolean onShouldBeginMoving(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
		return true;
	}
	
	/**scrolling methods*/
	
	public boolean onShouldBeingScrolling(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
		return true;
	}

	public void onDidBeginScrolling(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
	}
	
	public boolean onShouldScroll(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
		return true;
	}

	public void onDidScroll(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
	}
	
	public void onDidEndScrolling(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
	}
	
	/**zooming methods*/
	
	public boolean onShouldBeginZooming(PLIView pView)
	{
		return true;
	}
	
	public void onDidBeginZooming(PLIView pView, CGPoint startPoint, CGPoint endPoint)
	{
	}
	
	public boolean onShouldRunZooming(PLIView pView, float distance, boolean isZoomIn, boolean isZoomOut)
	{
		return true;
	}
	
	public void onDidRunZooming(PLIView pView, float distance, boolean isZoomIn, boolean isZoomOut)
	{
	}

	public void onDidEndZooming(PLIView pView, float distance, boolean isZoomIn, boolean isZoomOut)
	{
	}

	/**reset methods*/
	
	public boolean onShouldReset(PLIView pView)
	{
		return true;
	}
	
	public void onDidReset(PLIView pView)
	{
	}

	/**transition methods*/
	
	public void onDidBeginTransition(PLIView pView, PLITransition transition)
	{
	}
	
	public void onDidProcessTransition(PLIView pView, PLITransition transition, int progressPercentage)
	{	
	}
	
	public void onDidEndTransition(PLIView pView, PLITransition transition)
	{
	}
	
	/**hotspots methods*/
	
	public void onDidOverHotspot(PLIView pView, PLHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
	{	
	}
	
	public void onDidOutHotspot(PLIView pView, PLHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
	{
	}
	
	public void onDidClickHotspot(PLIView pView, PLHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
	{
	}
	
	/**camera methods*/
	
	public void onDidCameraReset(PLCamera camera)
	{
	}
	
	public void onDidCameraLookAt(PLCamera camera, float pitch, float yaw, float realPitch, float realYaw)
	{
	}
	
	public void onDidCameraRotate(PLCamera camera, float pitch, float yaw, float roll)
	{
	}
	
	public void onDidCameraFovDistance(PLCamera camera, float fov)
	{
	}
}
