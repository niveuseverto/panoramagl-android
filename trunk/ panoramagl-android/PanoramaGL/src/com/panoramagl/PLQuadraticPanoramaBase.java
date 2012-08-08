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

import com.panoramagl.opengl.GLUES;
import com.panoramagl.opengl.GLUquadric;

public abstract class PLQuadraticPanoramaBase extends PLPanoramaBase implements PLIQuadraticPanorama
{
	/**member variables*/
	
	private GLUquadric quadratic;
	private int previewDivs, divs;
	
	/**init methods*/

	@Override
	protected void initializeValues()
	{
	    super.initializeValues();
		quadratic = GLUES.gluNewQuadric();
		GLUES.gluQuadricNormals(quadratic, GLUES.GLU_SMOOTH);
		GLUES.gluQuadricTexture(quadratic, true);
	}

	/**property methods*/
	
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
	
	protected GLUquadric getQuadratic()
	{
		return quadratic;
	}
	
	protected void setQuadratic(GLUquadric quadratic)
	{
		this.quadratic = quadratic;
	}
	
	@Override
	public int getPreviewDivs()
	{
		return previewDivs;
	}
	
	@Override
	public void setPreviewDivs(int previewDivs)
	{
		if(previewDivs > 3)
			this.previewDivs = previewDivs;
	}
	
	@Override
	public int getDivs()
	{
		return divs;
	}
	
	@Override
	public void setDivs(int divs)
	{
		if(divs > 3)
			this.divs = divs;
	}
	
	/**dealloc methods*/

	@Override
	protected void finalize() throws Throwable
	{
		if(quadratic != null)
		{
			GLUES.gluDeleteQuadric(quadratic);
			quadratic = null;
		}
		super.finalize();
	}
}