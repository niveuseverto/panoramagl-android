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

import com.panoramagl.computation.PLMath;
import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.structs.PLPosition;
import com.panoramagl.structs.PLRange;
import com.panoramagl.structs.PLRotation;

public class PLObject extends PLObjectBase implements PLIObject
{	
	/**member variables*/
	
	private boolean isXAxisEnabled, isYAxisEnabled, isZAxisEnabled;
	private PLPosition position;
	private PLRange xRange, yRange, zRange;
	
	private boolean isPitchEnabled, isYawEnabled, isRollEnabled, isReverseRotation, isYZAxisInverseRotation;
	private PLRotation rotation;
	private PLRange pitchRange, yawRange, rollRange;
	private float rotateSensitivity;
	
	private PLRange tempRange;
	
	private float alpha, defaultAlpha;
	
	/**init methods*/
	
	public PLObject()
	{
		super();
	}
	
	protected void initializeValues()
	{
		xRange = PLRange.PLRangeMake(PLConstants.kFloatMinValue, PLConstants.kFloatMaxValue); 
		yRange = PLRange.PLRangeMake(PLConstants.kFloatMinValue, PLConstants.kFloatMaxValue); 
		zRange = PLRange.PLRangeMake(PLConstants.kFloatMinValue, PLConstants.kFloatMaxValue);
		
		pitchRange = PLRange.PLRangeMake(PLConstants.kDefaultPitchMinRange, PLConstants.kDefaultPitchMaxRange); 
		yawRange = PLRange.PLRangeMake(PLConstants.kDefaultYawMinRange, PLConstants.kDefaultYawMaxRange); 
		rollRange = PLRange.PLRangeMake(PLConstants.kDefaultRotateMinRange, PLConstants.kDefaultRotateMaxRange);
		
		isXAxisEnabled = isYAxisEnabled = isZAxisEnabled = true;
		isPitchEnabled = isYawEnabled = isRollEnabled = true;
		
		rotateSensitivity = PLConstants.kDefaultRotateSensitivity;	
		isReverseRotation = false;
		
		isYZAxisInverseRotation = true;
		
		position = PLPosition.PLPositionMake(0.0f, 0.0f, 0.0f);
		rotation = PLRotation.PLRotationMake(0.0f, 0.0f, 0.0f);
		
		tempRange = PLRange.PLRangeMake(0.0f, 0.0f);
		
		defaultAlpha = PLConstants.kObjectDefaultAlpha;
		
		this.reset();
	}
	
	/**reset methods*/
	
	@Override
	public void reset()
	{
		rotation.setValues(0.0f, 0.0f, 0.0f);
		alpha = defaultAlpha;
	}

	/**property methods*/
	
	@Override
	public void setPosition(PLPosition value)
	{
		this.setX(value.x);
		this.setY(value.y);
		this.setZ(value.z);
	}
	
	@Override
	public void setPosition(float x, float y, float z)
	{
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	
	@Override
	public float getX()
	{
		return position.x;	
	}
	
	@Override
	public void setX(float value)
	{
		if(isXAxisEnabled)
			position.x = PLMath.valueInRange(value, xRange); 
	}
	
	@Override
	public float getY()
	{
		return position.y;
	}
	
	@Override
	public void setY(float value)
	{
		if(isYAxisEnabled)
			position.y = PLMath.valueInRange(value, yRange);
	}
	
	@Override
	public float getZ()
	{
		return position.z;
	}
	
	@Override
	public void setZ(float value)
	{
		if(isZAxisEnabled)
			position.z = PLMath.valueInRange(value, zRange);
	}
	
	@Override
	public void setRotation(PLRotation value)
	{
		this.setPitch(value.pitch);
		this.setYaw(value.yaw);
		this.setRoll(value.roll);
	}
	
	@Override
	public void setRotationWith(float pitch, float yaw)
	{
		this.setPitch(pitch);
		this.setYaw(yaw);
	}
	
	@Override
	public void setRotationWith(float pitch, float yaw, float roll)
	{
		this.setPitch(pitch);
		this.setYaw(yaw);
		this.setRoll(roll);
	}
	
	@Override
	public float getPitch()
	{
		return rotation.pitch;
	}
	
	@Override
	public void setPitch(float value)
	{
		if(isPitchEnabled)
			rotation.pitch = PLMath.normalizeAngle(value, tempRange.setValues(-pitchRange.max, -pitchRange.min));
	}
	
	@Override
	public float getYaw()
	{
		return rotation.yaw;
	}
	
	@Override
	public void setYaw(float value)
	{
		if(isYawEnabled)
			rotation.yaw = PLMath.normalizeAngle(value, tempRange.setValues(-yawRange.max, -yawRange.min));
	}
	
	@Override
	public float getRoll()
	{
		return rotation.roll;
	}
	
	@Override
	public void setRoll(float value)
	{
		if(isRollEnabled)
			rotation.roll = PLMath.normalizeAngle(value, rollRange);
	}
	
	@Override
	public boolean isXAxisEnabled()
	{
		return isXAxisEnabled;
	}

	@Override
	public void setXAxisEnabled(boolean isXAxisEnabled)
	{
		this.isXAxisEnabled = isXAxisEnabled;
	}

	@Override
	public boolean isYAxisEnabled()
	{
		return isYAxisEnabled;
	}

	@Override
	public void setYAxisEnabled(boolean isYAxisEnabled)
	{
		this.isYAxisEnabled = isYAxisEnabled;
	}

	@Override
	public boolean isZAxisEnabled()
	{
		return isZAxisEnabled;
	}

	@Override
	public void setZAxisEnabled(boolean isZAxisEnabled)
	{
		this.isZAxisEnabled = isZAxisEnabled;
	}
	
	@Override
	public PLPosition getPosition()
	{
		return position;
	}
	
	@Override
	public PLRange getXRange()
	{
		return xRange;
	}

	@Override
	public void setXRange(PLRange xRange)
	{
		this.xRange.setValues(xRange);
	}
	
	@Override
	public void setXRange(float min, float max)
	{
		this.xRange.setValues(min, max);
	}

	@Override
	public PLRange getYRange()
	{
		return yRange;
	}

	@Override
	public void setYRange(PLRange yRange)
	{
		this.yRange.setValues(yRange);
	}

	@Override
	public void setYRange(float min, float max)
	{
		this.yRange.setValues(min, max);
	}
	
	@Override
	public PLRange getZRange()
	{
		return zRange;
	}

	@Override
	public void setZRange(PLRange zRange)
	{
		this.zRange.setValues(zRange);
	}
	
	@Override
	public void setZRange(float min, float max)
	{
		this.zRange.setValues(min, max);
	}

	@Override
	public boolean isPitchEnabled()
	{
		return isPitchEnabled;
	}

	@Override
	public void setPitchEnabled(boolean isPitchEnabled)
	{
		this.isPitchEnabled = isPitchEnabled;
	}

	@Override
	public boolean isYawEnabled()
	{
		return isYawEnabled;
	}

	@Override
	public void setYawEnabled(boolean isYawEnabled)
	{
		this.isYawEnabled = isYawEnabled;
	}

	@Override
	public boolean isRollEnabled()
	{
		return isRollEnabled;
	}

	@Override
	public void setRollEnabled(boolean isRollEnabled)
	{
		this.isRollEnabled = isRollEnabled;
	}

	@Override
	public boolean isReverseRotation()
	{
		return isReverseRotation;
	}

	@Override
	public void setReverseRotation(boolean isReverseRotation)
	{
		this.isReverseRotation = isReverseRotation;
	}
	
	@Override
	public boolean isYZAxisInverseRotation()
	{
		return isYZAxisInverseRotation;
	}

	@Override
	public void setYZAxisInverseRotation(boolean isYZAxisInverseRotation)
	{
		this.isYZAxisInverseRotation = isYZAxisInverseRotation;
	}

	@Override
	public PLRotation getRotation()
	{
		return rotation;
	}
	
	@Override
	public PLRange getPitchRange()
	{
		return pitchRange;
	}

	@Override
	public void setPitchRange(PLRange pitchRange)
	{
		this.pitchRange.setValues(pitchRange);
	}
	
	@Override
	public void setPitchRange(float min, float max)
	{
		this.pitchRange.setValues(min, max);
	}

	@Override
	public PLRange getYawRange()
	{
		return yawRange;
	}

	@Override
	public void setYawRange(PLRange yawRange)
	{
		this.yawRange.setValues(yawRange);
	}
	
	@Override
	public void setYawRange(float min, float max)
	{
		this.yawRange.setValues(min, max);
	}

	@Override
	public PLRange getRollRange()
	{
		return rollRange;
	}

	@Override
	public void setRollRange(PLRange rollRange)
	{
		this.rollRange.setValues(rollRange);
	}
	
	@Override
	public void setRollRange(float min, float max)
	{
		this.rollRange.setValues(min, max);
	}

	@Override
	public float getRotateSensitivity()
	{
		return rotateSensitivity;
	}

	@Override
	public void setRotateSensitivity(float rotateSensitivity)
	{
		this.rotateSensitivity = rotateSensitivity;
	}
	
	@Override
	public float getAlpha()
	{
		return alpha;
	}

	@Override
	public void setAlpha(float alpha)
	{
		this.alpha = alpha;
	}

	@Override
	public float getDefaultAlpha()
	{
		return defaultAlpha;
	}

	@Override
	public void setDefaultAlpha(float defaultAlpha)
	{
		this.defaultAlpha = defaultAlpha;
		this.alpha = defaultAlpha;
	}
	
	/**translate methods*/
	
	@Override
	public void translateWith(float xValue, float yValue)
	{
		position.x = xValue;
		position.y = yValue;
	}
	
	@Override
	public void translateWith(float xValue, float yValue, float zValue)
	{
		position.setValues(xValue, yValue, zValue);
	}
	
	/**rotate methods*/
	
	@Override
	public void rotateWith(float pitchValue, float yawValue)
	{
		this.setPitch(pitchValue);
		this.setYaw(yawValue);
	}
	
	@Override
	public void rotateWith(float pitchValue, float yawValue, float rollValue)
	{
		this.setPitch(pitchValue);
		this.setYaw(yawValue);
		this.setRoll(rollValue);
	}
	
	@Override
	public void rotateWith(CGPoint startPoint, CGPoint endPoint)
	{
		this.rotateWith(startPoint, endPoint, rotateSensitivity);
	}
	
	@Override
	public void rotateWith(CGPoint startPoint, CGPoint endPoint, float sensitivity)
	{
		this.setPitch(this.getPitch() + ((endPoint.y - startPoint.y) / sensitivity));
		this.setYaw(this.getYaw() + ((startPoint.x - endPoint.x) / sensitivity));
	}

	/**clone methods*/
	
	@Override
	public void clonePropertiesOf(PLIObject value)
	{
		this.setXAxisEnabled(value.isXAxisEnabled());
		this.setYAxisEnabled(value.isYAxisEnabled());
		this.setZAxisEnabled(value.isZAxisEnabled());
		
		this.setPitchEnabled(value.isPitchEnabled());
		this.setYawEnabled(value.isYawEnabled());
		this.setRollEnabled(value.isRollEnabled());
		
		this.setReverseRotation(value.isReverseRotation());
		
		this.setYZAxisInverseRotation(value.isYZAxisInverseRotation());
		
		this.setRotateSensitivity(value.getRotateSensitivity());
		
		this.setXRange(value.getXRange());
		this.setYRange(value.getYRange());
		this.setZRange(value.getZRange());
		
		this.setPitchRange(value.getPitchRange());
		this.setYawRange(value.getYawRange());
		this.setRollRange(value.getRollRange());
		
		this.setX(value.getX());
		this.setY(value.getY());
		this.setZ(value.getZ());
		
		this.setPitch(value.getPitch());
		this.setYaw(value.getYaw());
		this.setRoll(value.getRoll());
		
		this.setDefaultAlpha(value.getDefaultAlpha());
		this.setAlpha(value.getAlpha());
	}
	
	/**dealloc methods*/
	
	protected void finalize() throws Throwable
	{
		position = null;
		xRange = yRange = zRange = tempRange = null;
		rotation = null;
		pitchRange = yawRange = rollRange = null;
		super.finalize();
	}
}