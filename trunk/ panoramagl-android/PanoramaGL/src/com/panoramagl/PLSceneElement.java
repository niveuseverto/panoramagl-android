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

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.panoramagl.enumeration.PLSceneElementType;

public abstract class PLSceneElement extends PLSceneElementBase
{	
	/**member variables*/
	
	protected List<PLTexture> textures;
	
	/**init methods*/
	
	public PLSceneElement()
	{
		super();
	}
	
	public PLSceneElement(long identifierValue)
	{
		super();
		identifier = identifierValue;
	}

	public PLSceneElement(long identifierValue, PLTexture texture)
	{
		this(identifierValue);
		this.addTexture(texture);
	}
	
	public PLSceneElement(PLTexture texture)
	{
		super();
		this.addTexture(texture);
	}
	
	@Override
	protected void initializeValues()
	{
		super.initializeValues();
		textures = new ArrayList<PLTexture>();
	}
	
	/**property methods*/

	@Override
	public PLSceneElementType getType()
	{
		return PLSceneElementType.PLSceneElementTypeObject;
	}
	
	/**texture methods*/

	protected List<PLTexture> getTextures()
	{
		return textures;
	}
	
	public void addTexture(PLTexture texture)
	{
		if(texture != null)// && texture.isValid)
		{
			textures.add(texture);
			this.evaluateIfElementIsValid();
		}
	}

	public void removeTexture(PLTexture texture)
	{
		if(texture != null)// && texture.isValid)
		{
			textures.remove(texture);
			this.evaluateIfElementIsValid();
		}
	}

	public void removeTextureAtIndex(int index)
	{
		textures.remove(index);
		this.evaluateIfElementIsValid();
	}

	public void removeAllTextures()
	{
		textures.clear();
		this.evaluateIfElementIsValid();
	}
	
	public void removeAllTextures(GL10 gl)
	{
		for(int i = textures.size() - 1; i >= 0; i--)
			textures.get(i).recycle(gl);
		textures.clear();
		this.evaluateIfElementIsValid();
	}
	
	/**utility methods*/

	public void evaluateIfElementIsValid()
	{
		this.setValid(textures.size() > 0);
	}
	
	/**dealloc methods*/
	
	@Override
	protected void finalize() throws Throwable
	{
		textures = null;
		super.finalize();
	}
}
