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

package com.panoramagl.hotspots;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.panoramagl.computation.PLVector3;
import com.panoramagl.enumeration.PLHotspotTouchStatus;
import com.panoramagl.enumeration.PLSceneElementType;
import com.panoramagl.PLConstants;
import com.panoramagl.PLIObject;
import com.panoramagl.PLImage;
import com.panoramagl.PLSceneElement;
import com.panoramagl.PLTexture;
import com.panoramagl.structs.PLPosition;
import com.panoramagl.structs.PLRect;
import com.panoramagl.utils.PLUtils;

public class PLHotspot extends PLSceneElement
{
	/**member variables*/
	
	private float width, height;
	private float atv, ath;
	private float[] cube;
	private FloatBuffer cubeBuffer, textureCoordsBuffer;
	private float overAlpha, defaultOverAlpha;
	private boolean hasChangedCoordProperty;
	private boolean isTouchBlock;
	private PLHotspotTouchStatus touchStatus;
	
	/**init methods*/

	public PLHotspot(long identifierValue, float atv, float ath)
	{
		super(identifierValue);
		this.setAtv(atv);
		this.setAth(ath);
	}

	public PLHotspot(long identifierValue, PLTexture texture, float atv, float ath)
	{
		super(identifierValue, texture);
		this.setAtv(atv);
		this.setAth(ath);
	}
	
	public PLHotspot(long identifierValue, PLTexture texture, float atv, float ath, float width, float height)
	{
		super(identifierValue, texture);
		this.setAtv(atv);
		this.setAth(ath);
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public PLHotspot(long identifierValue, PLImage image, float atv, float ath)
	{
		this(identifierValue, PLTexture.textureWithImage(image), atv, ath);
	}
	
	public PLHotspot(long identifierValue, PLImage image, float atv, float ath, float width, float height)
	{
		this(identifierValue, PLTexture.textureWithImage(image), atv, ath, width, height);
	}

	public static PLHotspot hotspotWithId(long identifierValue, float atv, float ath)
	{
		return new PLHotspot(identifierValue, atv, ath);
	}

	public static PLHotspot hotspotWithId(long identifierValue, PLTexture texture, float atv, float ath)
	{
		return new PLHotspot(identifierValue, texture, atv, ath);
	}
	
	public static PLHotspot hotspotWithId(long identifierValue, PLTexture texture, float atv, float ath, float width, float height)
	{
		return new PLHotspot(identifierValue, texture, atv, ath, width, height);
	}
	
	public static PLHotspot hotspotWithId(long identifierValue, PLImage image, float atv, float ath)
	{
		return new PLHotspot(identifierValue, image, atv, ath);
	}
	
	public static PLHotspot hotspotWithId(long identifierValue, PLImage image, float atv, float ath, float width, float height)
	{
		return new PLHotspot(identifierValue, image, atv, ath, width, height);
	}

	@Override
	protected void initializeValues()
	{
		super.initializeValues();
		cube = new float[12];
		width = height = PLConstants.kDefaultButtonSize;
		atv = ath = 0.0f;
		this.setYZAxisInverseRotation(true);
		this.setZ(PLConstants.kRatio - 0.05f);
		this.setAlpha(PLConstants.kDefaultButtonAlpha);
		this.setDefaultAlpha(PLConstants.kDefaultButtonAlpha);
		overAlpha = defaultOverAlpha = PLConstants.kDefaultButtonOverAlpha;
		hasChangedCoordProperty = true;
		touchStatus = PLHotspotTouchStatus.PLHotspotTouchStatusOut;
	}
	
	/**reset methods*/

	@Override
	public void reset()
	{
		super.reset();
		isTouchBlock = false;
		overAlpha = defaultOverAlpha;
		touchStatus = PLHotspotTouchStatus.PLHotspotTouchStatusOut;
	}
	
	/**property methods*/
	
	public float getAtv()
	{
		return atv;
	}
	
	public void setAtv(float value)
	{
		if(this.atv != value)
		{
			this.atv = value;
			this.hasChangedCoordProperty = true;
		}
	}

	public float getAth()
	{
		return ath;
	}
	
	public void setAth(float value)
	{
		if(this.ath != value)
		{
			this.ath = value;
			this.hasChangedCoordProperty = true;
		}
	}
	
	public PLHotspotTouchStatus getTouchStatus()
	{
		return touchStatus;
	}
	
	protected void setTouchStatus(PLHotspotTouchStatus value)
	{
		touchStatus = value;
	}
	
	public float getWidth()
	{
		return width;
	}
	
	public void setWidth(float value)
	{
		if(width != value)
		{
			width = value;
			hasChangedCoordProperty = true;
		}
	}

	public float getHeight()
	{
		return height;
	}
	
	public void setHeight(float value)
	{
		if(height != value)
		{
			height = value;
			hasChangedCoordProperty = true;
		}
	}

	public float getOverAlpha()
	{
		return overAlpha;
	}

	public void setOverAlpha(float overAlpha)
	{
		this.overAlpha = overAlpha;
	}

	public float getDefaultOverAlpha()
	{
		return defaultOverAlpha;
	}

	public void setDefaultOverAlpha(float defaultOverAlpha)
	{
		this.defaultOverAlpha = defaultOverAlpha;
		this.setAlpha(defaultOverAlpha);
	}
	
	public PLRect getRect()
	{
		if(cubeBuffer != null)
			return PLRect.PLRectMake(cube[0], cube[1], cube[2], cube[9], cube[10], cube[11]);
		return PLRect.PLRectMake(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	}

	public float[] getVertexs()
	{
		return (cubeBuffer != null ? cube : null);
	}
	
	@Override
	public PLSceneElementType getType()
	{
		return PLSceneElementType.PLSceneElementTypeHotspot;
	}
	
	@Override
	public void setX(float value)
	{
	}
	
	@Override
	public void setY(float value)
	{
	}
	
	@Override
	public void setZ(float value)
	{
		if(this.getZ() != value)
		{
			super.setZ(value);
			hasChangedCoordProperty = true;
		}
	}
	
	public void setSize(float width, float height)
	{
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public void setLayout(float x, float y, float width, float height)
	{
		super.setX(x);
		super.setY(y);
		this.width = width;
		this.height = height;
		hasChangedCoordProperty = true;
	}

	/**utility methods*/

	protected void array(float[] result, int size, float ... args)
	{
	    for(int i = 0; i < size; i++)
	        result[i] = args[i];
	}

	/**calculate methods*/
	
	protected PLPosition convertPitchAndYawToPosition(float pitch, float yaw)
	{
		float r = this.getZ();
		double pr = (90.0f - pitch) * PLConstants.kPI / 180.0;
		double yr = -yaw * PLConstants.kPI / 180.0;
		
		float x = (float)(r * Math.sin(pr) * Math.cos(yr));
		float y = (float)(r * Math.sin(pr) * Math.sin(yr));
		float z = (float)(r * Math.cos(pr));
		
		return PLPosition.PLPositionMake(y, z, x);
	}
	
	protected List<PLPosition> calculatePoints(GL10 gl)
	{
		PLPosition pos = this.convertPitchAndYawToPosition(atv, ath);
		PLPosition pos1 = this.convertPitchAndYawToPosition(atv + 0.0001f, ath);
		
		List<PLPosition> result = new ArrayList<PLPosition>();
		//1
		PLVector3 p1 = new PLVector3(pos.x, pos.y, pos.z), p2 = new PLVector3(0.0f, 0.0f, 0.0f);
		PLVector3 n = new PLVector3(), p = new PLVector3(pos1.x,pos1.y,pos1.z), r = new PLVector3(0.0f, 0.0f, 0.0f), s, p2p1 = null, p0p1 = null;
		//2
		p0p1 = p.sub(p1);
		p2p1 = p2.sub(p1);
		r = p2p1.crossProduct(p0p1);
		//3
		s = p2p1.crossProduct(r);
		//4
		r.normalize();
		s.normalize();
		
		//5.1
		double w = width * PLConstants.kRatio, h = height * PLConstants.kRatio;
		double ratio = Math.sqrt((w * w) + (h * h));
		//5.2
		double angle = Math.asin(h/ratio);
		//5.3
		for(double theta : new double[]{ Math.PI - angle, angle, Math.PI + angle, 2 * Math.PI - angle})
		{
			double x = p1.x + (ratio * Math.cos(theta) * r.x) + (ratio * Math.sin(theta) * s.x);
			double y = p1.y + (ratio * Math.cos(theta) * r.y) + (ratio * Math.sin(theta) * s.y);
			double z = p1.z + (ratio * Math.cos(theta) * r.z) + (ratio * Math.sin(theta) * s.z);
			n.x = (float)x;
			n.y = (float)y;
			n.z =(float)z;
			n.normalize();
			result.add(PLPosition.PLPositionMake(n.x, n.y, n.z));
		}
		return result;
	}
	
	protected void calculateCoords(GL10 gl)
	{
		if(!hasChangedCoordProperty)
			return;
		
		hasChangedCoordProperty = false;
		
		float textureCoords[] = new float[8];
		
		List<PLPosition> positions = this.calculatePoints(gl);
		PLPosition pos1 = positions.get(0);
		PLPosition pos2 = positions.get(1);
		PLPosition pos3 = positions.get(2);
		PLPosition pos4 = positions.get(3);
		
		this.array(cube, 12,
				pos1.x, pos1.y, pos1.z,
				pos2.x, pos2.y, pos2.z,
				pos3.x, pos3.y, pos3.z,
				pos4.x, pos4.y, pos4.z
			);
		this.array(textureCoords, 8,
				1.0f, 1.0f,	
				0.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 0.0f
			);
		
		cubeBuffer = PLUtils.makeFloatBuffer(cube);
		textureCoordsBuffer = PLUtils.makeFloatBuffer(textureCoords);
	}
	
	/**translate methods*/
	
	@Override
	protected void translate(GL10 gl)
	{
	}
	
	/**render methods*/
	
	@Override
	protected void internalRender(GL10 gl)
	{
		this.calculateCoords(gl);
		
		if(cubeBuffer == null || textureCoordsBuffer == null)
			return;
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCoordsBuffer);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_FRONT);
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		gl.glColor4f(1.0f, 1.0f, 1.0f, touchStatus.ordinal() == PLHotspotTouchStatus.PLHotspotTouchStatusOut.ordinal() ? this.getAlpha() : overAlpha);
		
		if(textures.size() > 0)
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures.get(0).getTextureId(gl));
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	/**touch methods*/
	
	public void touchBlock()
	{
		isTouchBlock = true;
	}

	public void touchUnblock()
	{
		isTouchBlock = false;
	}
	
	public void touchOver(Object sender)
	{
		if(!isTouchBlock && touchStatus != PLHotspotTouchStatus.PLHotspotTouchStatusOver)
			touchStatus = PLHotspotTouchStatus.PLHotspotTouchStatusOver;
	}
	
	public void touchMove(Object sender)
	{
		if(!isTouchBlock)
			touchStatus = PLHotspotTouchStatus.PLHotspotTouchStatusMove;
	}
	
	public void touchOut(Object sender)
	{
		if(!isTouchBlock && touchStatus != PLHotspotTouchStatus.PLHotspotTouchStatusOut)
			touchStatus = PLHotspotTouchStatus.PLHotspotTouchStatusOut;
	}
	
	public void touchDown(Object sender)
	{
		if(!isTouchBlock && touchStatus != PLHotspotTouchStatus.PLHotspotTouchStatusDown)
			touchStatus = PLHotspotTouchStatus.PLHotspotTouchStatusDown;
	}
	
	/**clone methods*/

	@Override
	public void clonePropertiesOf(PLIObject object)
	{
		super.clonePropertiesOf(object);
		if(object instanceof PLHotspot)
		{
			PLHotspot hotspot = (PLHotspot)object;
			defaultOverAlpha = hotspot.getDefaultOverAlpha();
			overAlpha = hotspot.getOverAlpha();
		}
	}
	
	/**dealloc methods*/

	@Override
	protected void finalize() throws Throwable
	{
		cubeBuffer = textureCoordsBuffer = null;
		super.finalize();
	}
}