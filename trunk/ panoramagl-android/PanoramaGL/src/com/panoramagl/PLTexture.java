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
import com.panoramagl.enumeration.PLTextureColorFormat;
import com.panoramagl.ios.EAGLContext;

import android.opengl.GLU;
import android.opengl.GLUtils;

public class PLTexture extends PLObjectBase
{
	/**member variables*/
	
	private int[] textureId;
	private PLImage image;
	private int width, height;
	private boolean isValid, isRecycled;
	private PLTextureColorFormat format;
	private PLTextureListener listener;

	/**init methods*/
	
	public PLTexture(PLImage image)
	{
		super();
		this.image = image;
	}
	
	public static PLTexture textureWithImage(PLImage image)
	{
		return new PLTexture(image);
	}
	
	@Override
	protected void initializeValues()
	{
	    isValid = false;
		isRecycled = true;
		textureId = new int[]{ 0 };
		format = PLTextureColorFormat.PLTextureColorFormatRGBA8888;
	}
	
	/**property methods*/
		
	public int getTextureId(GL10 gl)
	{
		if(!isValid)
		{
			try
			{
				this.loadTextureWithObject(gl);
			}
			catch(Exception e)
			{
				PLLog.error("PLTexture::getTextureId", "getTextureId: %s", e.getMessage());
				e.printStackTrace();
			}
		}
		return textureId[0];
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public boolean isValid()
	{
		return isValid;
	}
	
	public boolean isRecycled()
	{
		return isRecycled;
	}
	
	public PLTextureColorFormat getFormat()
	{
		return format;
	}
				 
	public void setFormat(PLTextureColorFormat value)
	{
		format = value;
	}
	
	public PLTextureListener getListener()
	{
		return listener;
	}
	
	public void setListener(PLTextureListener value)
	{
		listener = value;
	}
	
	/**utility methods*/
	
	protected int convertToValidValueForDimension(int dimension)
	{
		if(dimension <= 4)
			return 4;
		else if(dimension <= 8)
			return 8;
		else if(dimension <= 16)
			return 16;
		else if(dimension <= 32)
			return 32;
		else if(dimension <= 64)
			return 64;
		else if(dimension <= 128)
			return 128;
		else if(dimension <= 256)
			return 256;
		else if(dimension <= 512)
			return 512;
		else
			return 1024;
	}
	
	/**load methods*/
	
	protected boolean loadTextureWithObject(GL10 gl) throws Exception
	{
		try
		{
			if(image == null || !image.isValid())
				return false;
			
			this.deleteTexture(gl);
			
			width = image.getWidth();
			height = image.getHeight();
			
			if(width > PLConstants.kTextureMaxWidth || height > PLConstants.kTextureMaxHeight)
			{
				PLLog.error("PLTexture::loadTextureWithObject", "Invalid texture size. Texture max size is %d x %d, currently is (%d x %d)", PLConstants.kTextureMaxWidth, PLConstants.kTextureMaxHeight, width, height);
				this.releaseImage();
				return false;
			}
			
			boolean isResizableImage = false;
			if(!PLMath.isPowerOfTwo(width) || width > PLConstants.kTextureMaxWidth)
			{
				isResizableImage = true;
				width = this.convertToValidValueForDimension(width);
			}
			if(!PLMath.isPowerOfTwo(height) || height > PLConstants.kTextureMaxHeight)
			{
				isResizableImage = true;
				height = this.convertToValidValueForDimension(height);
			}
			
			if(isResizableImage)
				image.scale(width, height);
			
			gl.glGenTextures(1, textureId, 0);
			
			int errGL = gl.glGetError();
			if(errGL != GL10.GL_NO_ERROR)
			{
				PLLog.error("PLTexture::loadTextureWithObject", "glGetError #1 = (%d) %s ...", errGL, GLU.gluErrorString(errGL));
				this.releaseImage();
				return false;
			}
			
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId[0]);
			errGL = gl.glGetError();
			if(errGL != GL10.GL_NO_ERROR)
			{
				PLLog.error("PLTexture::loadTextureWithObject", "glGetError #2 = (%d) %s ...", errGL, GLU.gluErrorString(errGL));
				this.releaseImage();
				return false;
			}
			
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR); //GLES10.GL_NEAREST
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE); //GL10.GL_REPEAT
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
			
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE); //GL10.GL_REPLACE
			
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, image.getBitmap(), 0);
			
			errGL = gl.glGetError();
			if(errGL != GL10.GL_NO_ERROR)
			{
				PLLog.error("PLTexture::loadTextureWithObject", "glGetError #3 = (%d) %s ...", errGL, GLU.gluErrorString(errGL));
				this.releaseImage();
				return false;
			}
			
			this.releaseImage();
			
			isValid = true;
			isRecycled = false;
			
			if(listener != null)
				listener.didLoad(this);
			
			return true;
		}
		catch(Exception e)
		{
			PLLog.error("PLTexture::loadTextureWithObject", "Error: %s", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	/**delete methods*/

	protected void deleteTexture(GL10 gl)
	{
		if(gl != null && textureId != null && textureId[0] != 0)
		{
			gl.glDeleteTextures(1, textureId, 0);
			textureId[0] = 0;
		}
		isValid = false;
	}
	
	/**recycle methods*/
	
	public void recycle(GL10 gl)
	{
		if(!isRecycled)
		{
			this.releaseImage();
			this.deleteTexture(gl);
			isRecycled = true;
		}
	}
	
	/**dealloc methods*/
	
	protected void releaseImage()
	{
		if(image != null)
		{
			image.recycle();
			image = null;
		}
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		try
		{
			this.recycle(EAGLContext.contextGL());
		}
		catch(Exception e)
		{
		}
		listener = null;
		textureId = null;
		super.finalize();
	}
}