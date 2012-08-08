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

package com.panoramagl.example;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import android.widget.Toast;

import com.panoramagl.PLIView;
import com.panoramagl.PLJSONLoader;
import com.panoramagl.PLView;
import com.panoramagl.PLViewEventListener;
import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.structs.PLPosition;

public class HelloPanoramaGLActivity extends PLView
{
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        this.load(new PLJSONLoader(this, "res://raw/json"));
        this.setListener
        (
        	new PLViewEventListener()
        	{
        		@Override
        		public void onDidClickHotspot(PLIView pView, PLHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
        		{
        			Toast.makeText(pView.getActivity(), String.format("You select the hotspot with ID %d", hotspot.getIdentifier()), Toast.LENGTH_SHORT).show();
        		}
        	}
        );
    }

	@Override
	protected void onGLContextCreated(GL10 gl)
	{
		super.onGLContextCreated(gl);
	}
}