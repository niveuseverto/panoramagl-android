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

import com.panoramagl.opengl.GLUES;

public class PLSphericalPanorama extends PLQuadraticPanoramaBase
{
	/**init methods*/

	public static PLSphericalPanorama panorama()
	{
	    return new PLSphericalPanorama();
	}

	@Override
	protected void initializeValues()
	{
	    super.initializeValues();
	    this.setPreviewDivs(PLConstants.kDefaultSpherePreviewDivs);
		this.setDivs(PLConstants.kDefaultSphereDivs);
	}

	/**property methods*/
	
	@Override
	public void setImage(GL10 gl, PLImage image)
	{
	    if(image != null)
	        this.setTexture(gl, PLTexture.textureWithImage(image));
	}

	public void setTexture(GL10 gl, PLTexture texture)
	{
	    if(texture != null)
	    {
	        synchronized(this)
	        {
				PLTexture textures[] = this.getTextures();
				PLTexture currentTexture = textures[0];
				if(currentTexture != null)
					currentTexture.recycle(gl);
				textures[0] = texture;
			}
		}
	}

	/**render methods*/

	@Override
	public void internalRender(GL10 gl)
	{
	    gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);

		gl.glEnable(GL10.GL_TEXTURE_2D);
	    
	    PLTexture previewTexture = this.getPreviewTextures()[0];
	    PLTexture texture = this.getTextures()[0];
	    
	    boolean previewTextureIsValid = (previewTexture != null && previewTexture.getTextureId(gl) != 0);
	    int divs = this.getDivs();
	    
	    if(texture != null && texture.getTextureId(gl) != 0)
	    {
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getTextureId(gl));
	        if(previewTextureIsValid)
	            this.removePreviewTextureAtIndex(gl, 0);
	    }
	    else if(previewTextureIsValid)
	    {
	    	divs = this.getPreviewDivs();
	    	gl.glBindTexture(GL10.GL_TEXTURE_2D, previewTexture.getTextureId(gl));
	    }
	    
		GLUES.gluSphere(gl, this.getQuadratic(), PLConstants.kRatio, divs, divs);
	    
		gl.glDisable(GL10.GL_TEXTURE_2D);
	    
	    gl.glRotatef(-180.0f, 0.0f, 1.0f, 0.0f);
	    gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
	}
}