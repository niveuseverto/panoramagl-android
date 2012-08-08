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

package com.panoramagl.ios;

import javax.microedition.khronos.opengles.GL10;

import com.panoramagl.opengl.matrix.MatrixTrackingGL;

public class EAGLContext
{
	/**static variables*/
	
	private static GL10 sContextGL = null;
	private static MatrixTrackingGL sLegacyGL = null;
	
	/**property methods*/
	
	public static void setContextGL(GL10 gl)
	{
		sContextGL = gl;
	}
	
	public static void setLegacyGL(MatrixTrackingGL gl)
	{
		sLegacyGL = gl;
	}
	
	public static GL10 contextGL()
	{
		return sContextGL;
	}
	
	public static GL10 legacyGL()
	{
		return sLegacyGL;
	}
	
	/**dealloc methods*/
	
	public static void destroyContext()
	{
		sContextGL = null;
		sLegacyGL = null;
	}
}