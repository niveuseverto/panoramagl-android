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

import javax.microedition.khronos.opengles.GL10;

import com.panoramagl.computation.PLMath;
import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.ios.EAGLContext;

public abstract class PLPanoramaBase extends PLScene implements PLIPanorama
{
	/**static variables*/
	
	protected static final int sPreviewFacesOrder[] = { 1, 3, 0, 2, 4, 5 };
	
	/**member variables*/
	
	private PLTexture previewTextures[], textures[];
	
	/**init methods*/

	protected PLPanoramaBase()
	{
		super();
	}
	
	@Override
	protected void initializeValues()
	{
		super.initializeValues();
		int sides = this.getSides();
		previewTextures = new PLTexture[sides];
		textures = new PLTexture[sides];
		for(int i = 0; i < sides; i++)
		{
			textures[i] = null;
			previewTextures[i] = null;
		}
		this.setValid(true);
	}
	
	/**property methods*/
	
	protected PLTexture[] getPreviewTextures()
	{
		return previewTextures;
	}
	
	protected PLTexture[] getTextures()
	{
		return textures;
	}
	
	@Override
	public int getPreviewSides()
	{
		return 1;
	}
	
	@Override
	public int getSides()
	{
		return 1;
	}
	
	/**preview methods*/
	
	@Override
	public void setPreviewImage(GL10 gl, PLImage image)
	{
		if(image != null && image.isValid())
		{
			synchronized(this)
			{
				this.removeAllPreviewTextures(gl);
				int width = image.getWidth();
				int height = image.getHeight();
				if(PLMath.isPowerOfTwo(width) && (height % width == 0 || width % height == 0))
				{
					int sides = this.getPreviewSides(), counter = 0;
					boolean isSideByDefault = (sides == 1);
					for(int i = 0; i < sides; i++)
					{
						try
						{
							PLImage subImage = PLImage.imageWithBitmap(image.getSubImage(0, (isSideByDefault ? i : sPreviewFacesOrder[i]) * width, width, (isSideByDefault ? height : width)));
							previewTextures[counter++] = PLTexture.textureWithImage(subImage);
						}
						catch(Exception e)
						{
							this.removeAllPreviewTextures(gl);
							PLLog.error("PLPanoramaBase::setPreviewTexture", "setPreviewTexture fails: %s", e.getMessage());
							e.printStackTrace();
							break;
						}
					}
				}
			}
		}
	}
	
	/**texture remove methods*/

	@Override
	public void removePreviewTextureAtIndex(GL10 gl, int index)
	{
		if(index < this.getPreviewSides())
	    {
			synchronized(this)
	        {
	            PLTexture texture = previewTextures[index];
	            if(texture != null)
	            {
	                texture.recycle(gl);
					previewTextures[index] = null;
	            }
	        }
	    }
	}
	
	@Override
	public void removeAllPreviewTextures(GL10 gl)
	{
		synchronized(this)
		{
			int sides = this.getPreviewSides();
			for(int i = 0; i < sides; i++)
			{
				PLTexture texture = previewTextures[i];
				if(texture != null)
				{
					texture.recycle(gl);
					previewTextures[i] = null;
				}
			}
		}
	}

	@Override
	public void removeAllTextures(GL10 gl)
	{
		if(gl != null)
		{
			synchronized(this)
			{
				int sides = this.getSides();
				for(int i = 0; i < sides; i++)
				{
					PLTexture texture = textures[i];
					if(texture != null)
					{
						texture.recycle(gl);
						textures[i] = null;
					}
				}
			}
		}
	}
	
	/**clear methods*/

	@Override
	public void clearPanorama(GL10 gl)
	{
	    this.removeAllPreviewTextures(gl);
	    this.removeAllTextures(gl);
	    this.removeAllHotspots(gl);
	}
	
	/**hotspot methods*/
	
	@Override
	public void addHotspot(PLHotspot hotspot)
	{
		this.addElement(hotspot);
	}

	@Override
	public void removeHotspot(GL10 gl, PLHotspot hotspot)
	{
		if(hotspot != null)
		{
			this.removeElement(hotspot);
			hotspot.removeAllTextures(gl);
		}
	}

	@Override
	public void removeHotspotAtIndex(GL10 gl, int index)
	{
		((PLHotspot)this.getElements().get(index)).removeAllTextures(gl);
		this.removeElementAtIndex(index);
	}

	@Override
	public void removeAllHotspots(GL10 gl)
	{
		List<PLSceneElement> elements = this.getElements();
		for(int i = elements.size() - 1; i >= 0; i--)
		{
			PLSceneElement element = elements.get(i);
			if(element instanceof PLHotspot)
			{
				((PLHotspot)element).removeAllTextures(gl);
				elements.remove(i);
			}
		}
	}

	/**dealloc methods*/
	
	@Override
	protected void finalize() throws Throwable
	{
		try
		{
			this.clearPanorama(EAGLContext.contextGL());
		}
		catch(Exception e)
		{
		}
		previewTextures = textures = null;
		super.finalize();
	}
}
