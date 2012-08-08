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

import com.panoramagl.structs.PLPosition;
import com.panoramagl.structs.PLRotation;

public abstract class PLRenderableElementBase extends PLObject implements PLIRenderableElement
{
	/**member variables*/
	
	private boolean isVisible, isValid;

	/**init methods*/

	protected void initializeValues()
	{
		super.initializeValues();
		isVisible = true;
		isValid = false;
	}
	
	/**property methods*/
	
	@Override
	public boolean isVisible()
	{
		return isVisible;
	}
	
	@Override
	public void setVisible(boolean value)
	{
		isVisible = value;
	}
	
	@Override
	public boolean isValid()
	{
		return isValid;
	}
	
	protected void setValid(boolean value)
	{
		isValid = value;
	}

	/**translate methods*/
	
	protected void translate(GL10 gl)
	{
		PLPosition position = this.getPosition();
		float yValue = this.isYZAxisInverseRotation() ? position.z : position.y, zValue = this.isYZAxisInverseRotation() ? position.y : position.z;
		gl.glTranslatef(this.isXAxisEnabled() ? position.x : 0.0f, this.isYAxisEnabled() ? yValue : 0.0f, this.isZAxisEnabled() ? zValue : 0.0f);
	}
	
	/**rotate methods*/
	
	protected void rotate(GL10 gl)
	{
		this.internalRotate(gl, this.getRotation());
	}
	
	protected void internalRotate(GL10 gl, PLRotation rotationValue)
	{
		float yDirection = this.isYZAxisInverseRotation() ? 0.0f : 1.0f, zDirection = this.isYZAxisInverseRotation() ? 1.0f : 0.0f;
		if(this.isPitchEnabled())
			gl.glRotatef(rotationValue.pitch * (this.isReverseRotation() ? 1.0f : -1.0f), 1.0f, 0.0f, 0.0f);
		if(this.isYawEnabled())
			gl.glRotatef(rotationValue.yaw * (this.isReverseRotation() ? 1.0f : -1.0f), 0.0f, yDirection, zDirection);
		if(this.isRollEnabled())
			gl.glRotatef(rotationValue.roll * (this.isReverseRotation() ? 1.0f : -1.0f), 0.0f, yDirection, zDirection);
	}
	
	/**alpha methods*/

	protected void beginAlpha(GL10 gl)
	{
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(1.0f, 1.0f, 1.0f, this.getAlpha());
	}
	
	protected void endAlpha(GL10 gl)
	{
		gl.glDisable(GL10.GL_BLEND);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	/**render methods*/
	
	protected void beginRender(GL10 gl)
	{
		gl.glPushMatrix();
		this.rotate(gl);
		this.translate(gl);
		this.beginAlpha(gl);
	}
	
	protected void endRender(GL10 gl)
	{
		this.endAlpha(gl);
		gl.glPopMatrix();
	}
	
	@Override
	public boolean render(GL10 gl)
	{
		try
		{
			if(isVisible && isValid)
			{
				this.beginRender(gl);
				this.internalRender(gl);
				this.endRender(gl);
				return true;
			}
		}
		catch(Exception e)
		{
		}
		return false;
	}
	
	protected abstract void internalRender(GL10 gl);
}