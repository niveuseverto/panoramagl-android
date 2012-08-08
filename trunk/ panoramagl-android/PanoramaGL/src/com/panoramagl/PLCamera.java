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

import com.panoramagl.computation.PLMath;
import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.structs.PLRange;
import com.panoramagl.structs.PLRotation;

public class PLCamera extends PLRenderableElementBase
{	
	/**member variables*/
	
	private boolean isFovEnabled;
	private float fov, initialFov, fovFactor, fovSensitivity;
	private PLRange fovRange;
	private int minDistanceToEnableFov;
	private PLRotation absoluteRotation, initialLookAt;
	private PLCameraListener listener;

	/**init methods*/
	
	public PLCamera()
	{
		super();
	}
	
	public static PLCamera camera()
	{
		return new PLCamera();
	}
	
	@Override
	protected void initializeValues()
	{
		fovRange = PLRange.PLRangeMake(PLConstants.kDefaultFovMinValue, PLConstants.kDefaultFovMaxValue);
		initialFov = fov = PLMath.normalizeFov(PLConstants.kDefaultFov, fovRange);
		isFovEnabled = true;
		fovSensitivity = PLConstants.kDefaultFovSensitivity;
		minDistanceToEnableFov = PLConstants.kDefaultMinDistanceToEnableFov;
		absoluteRotation = PLRotation.PLRotationMake(0.0f, 0.0f, 0.0f);
		initialLookAt = PLRotation.PLRotationMake(0.0f, 0.0f, 0.0f);
		super.initializeValues();
		this.setReverseRotation(true);
		this.setValid(true);
	}
	
	/**reset methods*/
	
	@Override
	public void reset()
	{
		fov = initialFov;
		this.setFov(fov);
		super.reset();
		this.lookAt(initialLookAt.pitch, initialLookAt.yaw);
		if(listener != null)
			listener.didReset(this);
	}
	
	/**property methods*/
	
	public float getInitialFov()
	{
		return initialFov;
	}
	
	public void setInitialFov(float value)
	{
		initialFov = PLMath.normalizeFov(value, fovRange);
	}
	
	public boolean isFovEnabled()
	{
		return isFovEnabled;
	}
	
	public void setFovEnabled(boolean isFovEnabled)
	{
		this.isFovEnabled = isFovEnabled;
	}
	
	public float getFov()
	{
		return fov;
	}
	
	public void setFov(float value)
	{
		if(isFovEnabled)
		{
			fov = PLMath.normalizeFov(value, fovRange);
			if(fov < 0.0f)
				fovFactor = PLConstants.kFovFactorOffsetValue + PLConstants.kFovFactorNegativeOffsetValue * (fov / PLConstants.kDefaultFovFactorMinValue);
			else if(fov >= 0.0f)
				fovFactor = PLConstants.kFovFactorOffsetValue + PLConstants.kFovFactorPositiveOffsetValue * (fov / PLConstants.kDefaultFovFactorMaxValue);
			if(listener != null)
				listener.didFovDistance(this, fov);
		}
	}
		
	public float getFovSensitivity()
	{
		return fovSensitivity;
	}
	
	public void setFovSensitivity(float value)
	{
		if(value > 0.0f)
			this.fovSensitivity = value;
	}
	
	public PLRange getFovRange()
	{
		return fovRange;
	}
	
	public void setFovRange(PLRange value)
	{
		this.setFovRange(value.min, value.max);
	}
	
	public void setFovRange(float min, float max)
	{
		if(max >= min)
		{
			fovRange.setValues((min < PLConstants.kFovMinValue ? PLConstants.kFovMinValue : min), (max > PLConstants.kFovMaxValue ? PLConstants.kFovMaxValue : max));
			this.setInitialFov(initialFov);
			this.setFov(fov);
		}
	}
	
	public float getFovFactor()
	{
		return fovFactor;
	}
	
	public int getMinDistanceToEnableFov()
	{
		return minDistanceToEnableFov;
	}
	
	public void setMinDistanceToEnableFov(int value)
	{
		if(value > 0)
			this.minDistanceToEnableFov = value;
	}
	
	public PLRotation getInitialLookAt()
	{
		return initialLookAt;
	}
	
	public void setInitialLookAt(float pitch, float yaw)
	{
		initialLookAt.pitch = pitch;
		initialLookAt.yaw = yaw;
	}
	
	public float getFovFactorCorrected()
	{
		float value = 1.0f;
		if(fov < 0.0f)
			value = PLConstants.kFovFactorOffsetValue + PLConstants.kFovFactorCorrectedNegativeOffsetValue * (fov / PLConstants.kDefaultFovFactorCorrectedMinValue);
		else if(fov >= 0.0f)
			value = PLConstants.kFovFactorOffsetValue + PLConstants.kFovFactorCorrectedPositiveOffsetValue * (fov / PLConstants.kDefaultFovFactorCorrectedMaxValue);
		return value;
	}
	
	public PLRotation getAbsoluteRotation()
	{
		PLRotation rotation = this.getRotation();
		return absoluteRotation.setValues(-rotation.pitch, -rotation.yaw, rotation.roll);
	}
	
	public PLCameraListener getListener()
	{
		return listener;
	}

	public void setListener(PLCameraListener listener)
	{
		this.listener = listener;
	}
	
	/**fov methods*/
	
	public void addFovWithDistance(float distance)
	{
		this.setFov(this.getFov() + ((distance < 0.0f ? distance / 2.5f : distance) / fovSensitivity));
	}

	public void addFovWithStartPoint(CGPoint startPoint, CGPoint endPoint, int sign)
	{
		this.addFovWithDistance(PLMath.distanceBetweenPoints(startPoint, endPoint) * (sign < 0 ? -1 : 1));
	}
	
	/**rotate methods*/
	
	@Override
	public void rotateWith(float pitchValue ,float yawValue)
	{
		super.rotateWith(pitchValue, yawValue);
		if(listener != null)
			listener.didRotate(this, this.getPitch(), this.getYaw(), this.getRoll());
	}
	
	@Override
	public void rotateWith(float pitchValue, float yawValue, float rollValue)
	{
		super.rotateWith(pitchValue, yawValue, rollValue);
		if(listener != null)
			listener.didRotate(this, this.getPitch(), this.getYaw(), this.getRoll());
	}
	
	@Override
	public void rotateWith(CGPoint startPoint ,CGPoint endPoint)
	{
		super.rotateWith(startPoint, endPoint);
		if(listener != null)
			listener.didRotate(this, this.getPitch(), this.getYaw(), this.getRoll());
	}
	
	@Override
	public void rotateWith(CGPoint startPoint, CGPoint endPoint, float sensitivity)
	{
		super.rotateWith(startPoint, endPoint, sensitivity);
		if(listener != null)
			listener.didRotate(this, this.getPitch(), this.getYaw(), this.getRoll());
	}
	
	/**render methods*/

	@Override
	protected void beginRender(GL10 gl)
	{
		this.rotate(gl);
		this.translate(gl);
	}

	@Override
	protected void endRender(GL10 gl)
	{
	}

	@Override
	protected void internalRender(GL10 gl)
	{
	}
	
	/**lookat methods*/

	public void lookAt(float pitch, float yaw)
	{
		float originalPitch = pitch, originalYaw = yaw;
		pitch = -pitch;
		yaw = -yaw;
		if(listener != null)
			listener.didLookAt(this, originalPitch, originalYaw, pitch, yaw);
		this.rotateWith(pitch, yaw);
	}
	
	/**zoom methods*/
	
	protected boolean zoom(int direction, boolean animated)
	{
		boolean isZoomOut = (direction < 0);
		if((isZoomOut ? fov > fovRange.min : fov < fovRange.max))
		{
			float distance = (fovRange.max - fovRange.min) / 3.0f;
			if(animated)
			{
				float step = (isZoomOut ? -distance : distance) / 10.0f;
				for(int i = 0; i < 10; i++)
				{
					try
					{
						Thread.sleep(25);
					}
					catch(InterruptedException e)
					{
					}
					this.setFov(this.getFov() + step);
				}
			}
			else
				this.setFov(this.getFov() + distance);
			return true;
		}
		return false;
	}
	
	public boolean zoomIn(boolean animated)
	{
		return this.zoom(1, animated);
	}
	
	public boolean zoomOut(boolean animated)
	{
		return this.zoom(-1, animated);
	}
	
	/**clone methods*/
	
	public void cloneCameraProperties(PLCamera value)
	{
		super.clonePropertiesOf(value);
		fovRange.setValues(value.getFovRange());
		isFovEnabled = value.isFovEnabled();
		fovSensitivity = value.getFovSensitivity();
		minDistanceToEnableFov = value.getMinDistanceToEnableFov();
		initialLookAt.setValues(value.getInitialLookAt());
		initialFov = value.getInitialFov();
		fov = value.getFov();
		fovFactor = value.getFovFactor();
	}
	
	/**dealloc methods*/
	
	protected void finalize() throws Throwable
	{
		absoluteRotation = null;
		listener = null;
		initialLookAt = null;
		fovRange = null;
		super.finalize();
	}
}


