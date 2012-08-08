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

public interface PLIScene extends PLIRenderableElement, PLIReleaseView
{
	/**property methods*/
	
	List<PLCamera> getCameras();
	
	PLCamera getCurrentCamera();
	PLCamera getCamera();

	int getCameraIndex();
	void setCameraIndex(int index);
	
	List<PLSceneElement> getElements();
	
	PLIView getView();

	/**reset methods*/

	void resetAlpha();

	/**camera methods*/
	
	void addCamera(PLCamera camera);
	void addCamera(PLCamera camera, int index);
	void removeCameraAtIndex(int index);
	void removeCamera(PLCamera camera);
	void removeAllCameras();
	
	/**element methods*/
	
	void addElement(PLSceneElement element);
	void removeElement(PLSceneElement element);
	void removeElementAtIndex(int index);
	void removeAllElements(GL10 gl);
}