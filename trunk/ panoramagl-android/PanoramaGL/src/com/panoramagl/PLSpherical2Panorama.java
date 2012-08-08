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

import com.panoramagl.enumeration.PLSpherical2FaceOrientation;
import com.panoramagl.opengl.GLUES;
import com.panoramagl.opengl.GLUquadric;

public class PLSpherical2Panorama extends PLQuadraticPanoramaBase
{
	/**init methods*/

	public static PLSpherical2Panorama panorama()
	{
	    return new PLSpherical2Panorama();
	}

	@Override
	protected void initializeValues()
	{
	    super.initializeValues();
	    this.setPreviewDivs(PLConstants.kDefaultSphere2PreviewDivs);
		this.setDivs(PLConstants.kDefaultSphere2Divs);
	}

	/**property methods*/

	@Override
	public int getSides()
	{
		return 4;
	}
	
	@Override
	public void setImage(GL10 gl, PLImage image)
	{
	    if(image != null && image.getWidth() == 2048 && image.getHeight() == 1024)
	    {
	    	PLImage frontImage = PLImage.crop(image, 960, 0, 128, 1024);
	    	PLImage backImage = PLImage.joinImagesHorizontally(PLImage.crop(image, 1984, 0, 64, 1024), PLImage.crop(image, 0, 0, 64, 1024));
	        PLImage leftImage = PLImage.crop(image, 0, 0, 1024, 1024);
	        PLImage rightImage = PLImage.crop(image, 1024, 0, 1024, 1024);
	        this.setTexture(gl, PLTexture.textureWithImage(frontImage), PLSpherical2FaceOrientation.PLSpherical2FaceOrientationFront);
	        this.setTexture(gl, PLTexture.textureWithImage(backImage), PLSpherical2FaceOrientation.PLSpherical2FaceOrientationBack);
	        this.setTexture(gl, PLTexture.textureWithImage(leftImage), PLSpherical2FaceOrientation.PLSpherical2FaceOrientationLeft);
	        this.setTexture(gl, PLTexture.textureWithImage(rightImage), PLSpherical2FaceOrientation.PLSpherical2FaceOrientationRight);
	    }
	}

	protected void setTexture(GL10 gl, PLTexture texture, PLSpherical2FaceOrientation face)
	{
        synchronized(this)
        {
			PLTexture textures[] = this.getTextures();
			PLTexture currentTexture = textures[face.ordinal()];
			if(currentTexture != null)
				currentTexture.recycle(gl);
			textures[face.ordinal()] = texture;
		}
	}

	/**render methods*/

	@Override
	protected void internalRender(GL10 gl)
	{
	    gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
	    
		gl.glEnable(GL10.GL_TEXTURE_2D);
	    
		PLTexture previewTexture = this.getPreviewTextures()[0];
	    PLTexture textures[] = this.getTextures();
	    PLTexture frontTexture = textures[PLSpherical2FaceOrientation.PLSpherical2FaceOrientationFront.ordinal()];
	    PLTexture backTexture = textures[PLSpherical2FaceOrientation.PLSpherical2FaceOrientationBack.ordinal()];
	    PLTexture leftTexture = textures[PLSpherical2FaceOrientation.PLSpherical2FaceOrientationLeft.ordinal()];
	    PLTexture rightTexture = textures[PLSpherical2FaceOrientation.PLSpherical2FaceOrientationRight.ordinal()];
	    
	    boolean previewTextureIsValid = (previewTexture != null && previewTexture.getTextureId(gl) != 0);
	    boolean frontTextureIsValud = (frontTexture != null && frontTexture.getTextureId(gl) != 0);
	    boolean backTextureIsValid = (backTexture != null && backTexture.getTextureId(gl) != 0);
	    boolean leftTextureIsValid = (leftTexture != null && leftTexture.getTextureId(gl) != 0);
	    boolean rightTextureIsValid = (rightTexture != null && rightTexture.getTextureId(gl) != 0);
	    
	    GLUquadric quadratic = this.getQuadratic();
	    int divs = this.getDivs();
	    
	    if(previewTextureIsValid)
	    {
	        if(frontTextureIsValud && backTextureIsValid && leftTextureIsValid && rightTextureIsValid)
	            this.removePreviewTextureAtIndex(gl, 0);
	        else
	        {
	        	int previewDivs = this.getPreviewDivs();
	            gl.glBindTexture(GL10.GL_TEXTURE_2D, previewTexture.getTextureId(gl));
	            GLUES.gluSphere(gl, quadratic, PLConstants.kRatio + 0.05f, previewDivs, previewDivs);
	        }
	    }
	    if(frontTextureIsValud)
	    {
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, frontTexture.getTextureId(gl));
	        GLUES.glu3DArc(gl, quadratic, PLConstants.kPI8, -PLConstants.kPI16, false, PLConstants.kRatio, divs, divs);
	    }
	    if(backTextureIsValid)
	    {
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, backTexture.getTextureId(gl));
	        GLUES.glu3DArc(gl, quadratic, PLConstants.kPI8, -PLConstants.kPI16, true, PLConstants.kRatio, divs, divs);
	    }
	    if(leftTextureIsValid)
	    {
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, leftTexture.getTextureId(gl));
	        GLUES.gluHemisphere(gl, quadratic, false, PLConstants.kRatio, divs, divs);
	    }
	    if(rightTextureIsValid)
	    {
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, rightTexture.getTextureId(gl));
	        GLUES.gluHemisphere(gl, quadratic, true, PLConstants.kRatio, divs, divs);
	    }
		
		gl.glDisable(GL10.GL_TEXTURE_2D);
	    
	    gl.glRotatef(-180.0f, 0.0f, 1.0f, 0.0f);
	    gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
	}
}