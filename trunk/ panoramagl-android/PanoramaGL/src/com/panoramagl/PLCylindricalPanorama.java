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

public class PLCylindricalPanorama extends PLQuadraticPanoramaBase
{
	/**member variables*/
	
    private boolean isHeightCalculated;
    private float height;
	
	/**init methods*/
	
	public static PLCylindricalPanorama panorama()
	{
		return new PLCylindricalPanorama();
	}

	@Override
	protected void initializeValues()
	{
	    super.initializeValues();	    
	    this.setPreviewDivs(PLConstants.kDefaultCylinderPreviewDivs);
	    this.setDivs(PLConstants.kDefaultCylinderDivs);
	    height = PLConstants.kDefaultCylinderHeight;
	    isHeightCalculated = PLConstants.kDefaultCylinderHeightCalculated;
	    this.setPitchRange(0.0f, 0.0f);
	    this.setXAxisEnabled(false);
	}

	/**property methods*/

	public boolean isHeightCalculated()
	{
		return isHeightCalculated;
	}
	
	public void setHeightCalculated(boolean isHeightCalculated)
	{
		this.isHeightCalculated = isHeightCalculated;
	}
	
	public float getHeight()
	{
		return height;
	}
	
	public void setHeight(float height)
	{
	    if(!isHeightCalculated && height > 0.0f)
	        this.height = Math.abs(height);
	}
	
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
	            if(isHeightCalculated)
	                height = -1.0f;
			}
		}
	}

	/**render methods*/

	@Override
	protected void internalRender(GL10 gl)
	{
	    PLTexture previewTexture = this.getPreviewTextures()[0];
	    PLTexture texture = this.getTextures()[0];
	    
	    boolean previewTextureIsValid = (previewTexture != null && previewTexture.getTextureId(gl) != 0);
	    boolean textureIsValid = (texture != null && texture.getTextureId(gl) != 0);
	    
	    if(isHeightCalculated && textureIsValid && height == -1.0f)
	    {
	        int textureWidth = texture.getWidth();
	        int textureHeight = texture.getHeight();
	        height = (textureWidth > textureHeight ? (float)textureWidth/(float)textureHeight : (float)textureHeight/(float)textureWidth);
	    }
	    
	    float h2 = height/2.0f;
	    
	    gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
	    gl.glTranslatef(0.0f, 0.0f, -h2);
	    
		gl.glEnable(GL10.GL_TEXTURE_2D);
	    
		int divs = this.getDivs();
		
	    if(textureIsValid)
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
	    
		GLUES.gluCylinder(gl, this.getQuadratic(), PLConstants.kRatio, PLConstants.kRatio, height, divs, divs);
	    
		gl.glDisable(GL10.GL_TEXTURE_2D);
	    
		gl.glTranslatef(0.0f, 0.0f, h2);
		gl.glRotatef(-180.0f, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
	}
}