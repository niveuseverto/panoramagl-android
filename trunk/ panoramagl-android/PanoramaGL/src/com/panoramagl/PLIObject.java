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

import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.structs.PLPosition;
import com.panoramagl.structs.PLRange;
import com.panoramagl.structs.PLRotation;

public interface PLIObject
{
	/**property methods*/
	
	boolean isXAxisEnabled();
	void setXAxisEnabled(boolean isXAxisEnabled);

	boolean isYAxisEnabled();
	void setYAxisEnabled(boolean isYAxisEnabled);

	boolean isZAxisEnabled();
	void setZAxisEnabled(boolean isZAxisEnabled);
	
	PLRange getXRange();
	void setXRange(PLRange xRange);
	void setXRange(float min, float max);

	PLRange getYRange();
	void setYRange(PLRange yRange);
	void setYRange(float min, float max);

	PLRange getZRange();
	void setZRange(PLRange zRange);
	void setZRange(float min, float max);

	boolean isPitchEnabled();
	void setPitchEnabled(boolean isPitchEnabled);

	boolean isYawEnabled();
	void setYawEnabled(boolean isYawEnabled);

	boolean isRollEnabled();
	void setRollEnabled(boolean isRollEnabled);

	boolean isReverseRotation();
	void setReverseRotation(boolean isReverseRotation);
	
	boolean isYZAxisInverseRotation();
	void setYZAxisInverseRotation(boolean isYZAxisInverseRotation);
	
	PLRange getPitchRange();
	void setPitchRange(PLRange pitchRange);
	void setPitchRange(float min, float max);

	PLRange getYawRange();
	void setYawRange(PLRange yawRange);
	void setYawRange(float min, float max);

	PLRange getRollRange();
	void setRollRange(PLRange rollRange);
	void setRollRange(float min, float max);

	float getRotateSensitivity();
	void setRotateSensitivity(float rotateSensitivity);
	
	float getAlpha();
	void setAlpha(float alpha);

	float getDefaultAlpha();
	void setDefaultAlpha(float defaultAlpha);
	
	PLPosition getPosition();
	void setPosition(PLPosition value);
	void setPosition(float x, float y, float z);
	
	float getX();
	void setX(float value);
	
	float getY();
	void setY(float value);
	
	float getZ();
	void setZ(float value);

	PLRotation getRotation();
	void setRotation(PLRotation value);
	void setRotationWith(float pitch, float yaw);
	void setRotationWith(float pitch, float yaw, float roll);
	
	float getPitch();
	void setPitch(float value);
	
	float getYaw();
	void setYaw(float value);
	
	float getRoll();
	void setRoll(float value);
	
	/**reset methods*/

	void reset();

	/**translate methods*/
	
	void translateWith(float xValue, float yValue);
	void translateWith(float xValue, float yValue, float zValue);

	/**rotate methods*/
	
	void rotateWith(float pitchValue, float yawValue);
	void rotateWith(float pitchValue, float yawValue, float rollValue);
	void rotateWith(CGPoint startPoint, CGPoint endPoint);
	void rotateWith(CGPoint startPoint, CGPoint endPoint, float sensitivity);

	/**clone methods*/
	
	void clonePropertiesOf(PLIObject value);
}