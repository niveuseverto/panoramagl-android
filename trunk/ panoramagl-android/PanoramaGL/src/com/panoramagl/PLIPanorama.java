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

import com.panoramagl.hotspots.PLHotspot;

public interface PLIPanorama extends PLIScene
{	
	/**property methods*/
	
	int getPreviewSides();
	
	int getSides();
	
	void setPreviewImage(GL10 gl, PLImage image);
	
	void setImage(GL10 gl, PLImage image);

	/**texture remove methods*/

	void removePreviewTextureAtIndex(GL10 gl, int index);
	void removeAllPreviewTextures(GL10 gl);

	void removeAllTextures(GL10 gl);

	/**clear methods*/

	void clearPanorama(GL10 gl);

	/**hotspot methods*/

	void addHotspot(PLHotspot hotspot);
	void removeHotspot(GL10 gl, PLHotspot hotspot);
	void removeHotspotAtIndex(GL10 gl, int index);
	void removeAllHotspots(GL10 gl);
}