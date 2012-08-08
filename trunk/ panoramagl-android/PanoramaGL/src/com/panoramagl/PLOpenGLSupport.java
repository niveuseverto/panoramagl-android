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

import com.panoramagl.enumeration.PLOpenGLVersion;

public class PLOpenGLSupport
{
	/**static variables*/
	
	private static PLOpenGLVersion sGLVersion = null;
	
	/**property methods*/
	
	public static PLOpenGLVersion getOpenGLVersion(GL10 gl)
	{
		if(sGLVersion == null)
		{
			String version = gl.glGetString(GL10.GL_VERSION);
			if(version.indexOf("1.0") != -1)
				sGLVersion = PLOpenGLVersion.PLOpenGLVersion1_0;
			else if(version.indexOf("1.1") != -1)
				sGLVersion = PLOpenGLVersion.PLOpenGLVersion1_1;
			else
				sGLVersion = PLOpenGLVersion.PLOpenGLVersion2_0;
		}
		return sGLVersion;
	}
	
	/**check methods*/
	
	public static boolean checkIfSupportsFrameBufferObject(GL10 gl)
	{
        return checkIfSupportsExtension(gl, "GL_OES_framebuffer_object");
    }

	public static boolean checkIfSupportsExtension(GL10 gl, String extension)
    {
        return (" " + gl.glGetString(GL10.GL_EXTENSIONS) + " ").indexOf(" " + extension + " ") >= 0;
    }
}