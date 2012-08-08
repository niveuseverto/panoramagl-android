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

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.panoramagl.enumeration.PLCubeFaceOrientation;
import com.panoramagl.utils.PLUtils;

public class PLCubicPanorama extends PLPanoramaBase 
{	
	/**constants*/
	
	protected final static float R = PLConstants.kRatio;
	
	/**static variables*/
	
	protected final static float[] sCube = 
	{
		// Front Face
		-R, -R,  R,
		 R, -R,  R,
		-R,  R,  R,
		 R,  R,  R,
		// Back Face
		-R, -R, -R,
		-R,  R, -R,
		 R, -R, -R,
		 R,  R, -R,
		// Left Face
		 R, -R, -R,
		 R,  R, -R,
		 R, -R,  R,
		 R,  R,  R,
		// Right Face
		-R, -R,  R,
		-R,  R,  R,
		-R, -R, -R,
		-R,  R, -R,
		// Top Face
		-R,  R,  R,
		 R,  R,  R,
		-R,  R, -R,
		 R,  R, -R,
		// Bottom Face
		-R, -R,  R,
		-R, -R, -R,
		 R, -R,  R,
		 R, -R, -R,
	};
	
	protected final static float[] sTextureCoords = 
	{
		// Front Face
		1.0f, 1.0f,
		0.0f, 1.0f,	
		1.0f, 0.0f,
		0.0f, 0.0f,
		// Back Face
		0.0f, 1.0f,
		0.0f, 0.0f,
		1.0f, 1.0f,
		1.0f, 0.0f,
		// Left Face
		0.0f, 1.0f,
		0.0f, 0.0f,
		1.0f, 1.0f,
		1.0f, 0.0f,
		// Right Face
		0.0f, 1.0f,
		0.0f, 0.0f,
		1.0f, 1.0f,
		1.0f, 0.0f,
		// Top Face
		1.0f, 1.0f,
		0.0f, 1.0f,
		1.0f, 0.0f,
		0.0f, 0.0f,
		// Bottom Face
		1.0f, 0.0f,
		1.0f, 1.0f,
		0.0f, 0.0f,
		0.0f, 1.0f,
	};
	
	/**member variables*/
	
	private FloatBuffer cubeBuffer, textureCoordsBuffer;
	
	/**init methods*/
	
	public PLCubicPanorama()
	{
		super();
		cubeBuffer = PLUtils.makeFloatBuffer(sCube);
		textureCoordsBuffer = PLUtils.makeFloatBuffer(sTextureCoords);
	}
	
	public static PLCubicPanorama panorama()
	{
		return new PLCubicPanorama();
	}
	
	/**property methods*/
	
	@Override
	public int getPreviewSides()
	{
		return 6;
	}
	
	@Override
	public int getSides()
	{
		return 6;
	}
	
	public static final float[] getCoordinates()
	{
		return sCube;
	}
	
	protected FloatBuffer getCubeBuffer()
	{
		return cubeBuffer;
	}
	
	protected FloatBuffer getTextureCoordsBuffer()
	{
		return textureCoordsBuffer;
	}
	
	@Override
	public void setImage(GL10 gl, PLImage image)
	{
		if(image != null)
		{
			int width = image.getWidth(), height = image.getHeight();
			if(width <= PLConstants.kTextureMaxWidth && height % width == 0 && height / width == 6)
			{
				this.setTexture(gl, PLTexture.textureWithImage(PLImage.crop(image, 0, 0, width, width)), PLCubeFaceOrientation.PLCubeFaceOrientationLeft);
				this.setTexture(gl, PLTexture.textureWithImage(PLImage.crop(image, 0, width, width, width)), PLCubeFaceOrientation.PLCubeFaceOrientationFront);
				this.setTexture(gl, PLTexture.textureWithImage(PLImage.crop(image, 0, width * 2, width, width)), PLCubeFaceOrientation.PLCubeFaceOrientationRight);
				this.setTexture(gl, PLTexture.textureWithImage(PLImage.crop(image, 0, width * 3, width, width)), PLCubeFaceOrientation.PLCubeFaceOrientationBack);
				this.setTexture(gl, PLTexture.textureWithImage(PLImage.crop(image, 0, width * 4, width, width)), PLCubeFaceOrientation.PLCubeFaceOrientationUp);
				this.setTexture(gl, PLTexture.textureWithImage(PLImage.crop(image, 0, width * 5, width, width)), PLCubeFaceOrientation.PLCubeFaceOrientationDown);
			}
		}
	}
	
	public void setImage(GL10 gl, PLImage image, PLCubeFaceOrientation face)
	{
	    if(image != null)
	        this.setTexture(gl, PLTexture.textureWithImage(image), face);
	}
	
	public void setTexture(GL10 gl, PLTexture texture, PLCubeFaceOrientation face)
	{
		synchronized(this)
		{
			if(texture != null)
			{
				PLTexture[] textures = this.getTextures();
				PLTexture currentTexture = textures[face.ordinal()];
				if(currentTexture != null)
					currentTexture.recycle(gl);
				textures[face.ordinal()] = texture;
			}
		}
	}
	
	/**render methods*/
	
	protected boolean bindTextureBySide(GL10 gl, int side)
	{
		boolean result = false;
		try
		{
			PLTexture textures[] = this.getTextures();
			PLTexture texture = textures[side];
			if(texture != null && texture.getTextureId(gl) != 0)
			{
				gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getTextureId(gl));
				result = true;
	            PLTexture previewTextures[] = this.getPreviewTextures();
				texture = previewTextures[side];
	            if(texture != null)
	                this.removePreviewTextureAtIndex(gl, side);
			}
			else
			{
				PLTexture previewTextures[] = this.getPreviewTextures();
				texture = previewTextures[side];
				if(texture != null && texture.getTextureId(gl) != 0)
				{
					gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getTextureId(gl));
					result = true;
				}
			}
		}
		catch(Exception e)
		{
		}
		return result;
	}
	
	@Override
	protected void internalRender(GL10 gl)
	{
		gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_FRONT);
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCoordsBuffer);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		// Front Face
		if(this.bindTextureBySide(gl, PLConstants.kCubeFrontFaceIndex))
		{
			gl.glNormal3f(0.0f, 0.0f, 1.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		}
		
		// Back Face
		if(this.bindTextureBySide(gl, PLConstants.kCubeBackFaceIndex))
		{
			gl.glNormal3f(0.0f, 0.0f, -1.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
		}
		
		// Left Face
		if(this.bindTextureBySide(gl, PLConstants.kCubeLeftFaceIndex))
		{
			gl.glNormal3f(1.0f, 0.0f, 0.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
		}
		
		// Right Face
		if(this.bindTextureBySide(gl, PLConstants.kCubeRightFaceIndex))
		{
			gl.glNormal3f(-1.0f, 0.0f, 0.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
		}
		
		// Up Face
		if(this.bindTextureBySide(gl, PLConstants.kCubeUpFaceIndex))
		{
			gl.glNormal3f(0.0f, 1.0f, 0.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
		}
		
		// Down Face
		if(this.bindTextureBySide(gl, PLConstants.kCubeDownFaceIndex))
		{
			gl.glNormal3f(0.0f, -1.0f, 0.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
		}
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);	
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	/**dealloc methods*/
	
	@Override
	protected void finalize() throws Throwable
	{
		cubeBuffer = textureCoordsBuffer = null;
		super.finalize();
	}
}
