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

package com.example.hellopanoramagl;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import com.panoramagl.PLCubicPanorama;
import com.panoramagl.PLCylindricalPanorama;
import com.panoramagl.PLILoader;
import com.panoramagl.PLIPanorama;
import com.panoramagl.PLIView;
import com.panoramagl.PLImage;
import com.panoramagl.PLJSONLoader;
import com.panoramagl.PLSpherical2Panorama;
import com.panoramagl.PLSphericalPanorama;
import com.panoramagl.PLView;
import com.panoramagl.PLViewEventListener;
import com.panoramagl.enumeration.PLCubeFaceOrientation;
import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.structs.PLPosition;
import com.panoramagl.utils.PLUtils;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;

public class MainActivity extends PLView
{
	/**constants*/
	
	private static final int kHotspotIdMin = 1;
	private static final int kHotspotIdMax = 1000;
	
	/**member variables*/
	
	private Random random = new Random();
	
	/**init methods*/
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.loadPanorama(0);
        this.setListener(new PLViewEventListener()
        {
        	@Override
    		public void onDidClickHotspot(PLIView pView, PLHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
        	{
        		Toast.makeText(pView.getActivity(), String.format("You select the hotspot with ID %d", hotspot.getIdentifier()), Toast.LENGTH_SHORT).show();
        	}
		});
    }
    
    /**
     * This event is fired when OpenGL renderer was created
     * @param gl Current OpenGL context
     */
    @Override
	protected void onGLContextCreated(GL10 gl)
	{
    	super.onGLContextCreated(gl);
    	
    	//Add layout
    	View mainView = this.getLayoutInflater().inflate(R.layout.activity_main, null);
        this.addContentView(mainView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        //Spinner control
        Spinner panoramaTypeSpinner = (Spinner)mainView.findViewById(R.id.spinner_panorama_type);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.panorama_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        panoramaTypeSpinner.setAdapter(adapter);
        panoramaTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
			{
				loadPanoramaFromJSON(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView)
			{
			}
		});
        
        //Zoom controls
        ZoomControls zoomControls = (ZoomControls)mainView.findViewById(R.id.zoomControls);
        zoomControls.setOnZoomInClickListener(new OnClickListener()
        {	
			@Override
			public void onClick(View view)
			{
				getCamera().zoomIn(true);
			}
		});
        zoomControls.setOnZoomOutClickListener(new OnClickListener()
        {	
			@Override
			public void onClick(View view)
			{
				getCamera().zoomOut(true);
			}
		});
	}
    
    /**load methods*/
    
    /**
     * Load panorama image manually
     * @param index Spinner position where 0 = spherical, 1 = spherical2, 2 = cubic, 3 = cylindrical
     */
    private void loadPanorama(int index)
    {
    	GL10 gl = this.getCurrentGL();
    	PLIPanorama panorama = null;
    	//Lock panoramic view
    	this.setBlocked(true);
    	//Spherical panorama example (supports up 1024x512 texture)
        if(index == 0)
        {
            panorama = new PLSphericalPanorama();
            ((PLSphericalPanorama)panorama).setImage(gl, PLImage.imageWithBitmap(PLUtils.getBitmap(this, R.raw.pano_sphere), false));
        }
        //Spherical2 panorama example (only support 2048x1024 texture)
        else if(index == 1)
        {
        	panorama = new PLSpherical2Panorama();
            ((PLSpherical2Panorama)panorama).setImage(gl, PLImage.imageWithBitmap(PLUtils.getBitmap(this, R.raw.pano_sphere2), false));
        }
        //Cubic panorama example (supports up 1024x1024 texture per face)
        else if(index == 2)
        {
        	PLCubicPanorama cubicPanorama = new PLCubicPanorama();
        	cubicPanorama.setImage(gl, PLImage.imageWithBitmap(PLUtils.getBitmap(this, R.raw.pano_f), false), PLCubeFaceOrientation.PLCubeFaceOrientationFront);
        	cubicPanorama.setImage(gl, PLImage.imageWithBitmap(PLUtils.getBitmap(this, R.raw.pano_b), false), PLCubeFaceOrientation.PLCubeFaceOrientationBack);
        	cubicPanorama.setImage(gl, PLImage.imageWithBitmap(PLUtils.getBitmap(this, R.raw.pano_l), false), PLCubeFaceOrientation.PLCubeFaceOrientationLeft);
        	cubicPanorama.setImage(gl, PLImage.imageWithBitmap(PLUtils.getBitmap(this, R.raw.pano_r), false), PLCubeFaceOrientation.PLCubeFaceOrientationRight);
        	cubicPanorama.setImage(gl, PLImage.imageWithBitmap(PLUtils.getBitmap(this, R.raw.pano_u), false), PLCubeFaceOrientation.PLCubeFaceOrientationUp);
        	cubicPanorama.setImage(gl, PLImage.imageWithBitmap(PLUtils.getBitmap(this, R.raw.pano_d), false), PLCubeFaceOrientation.PLCubeFaceOrientationDown);
            panorama = cubicPanorama;
        }
        //Cylindrical panorama example (supports up 1024x1024 texture)
        else if(index == 3)
        {
        	PLCylindricalPanorama cylindricalPanorama = new PLCylindricalPanorama();
        	cylindricalPanorama.setHeightCalculated(false);
        	cylindricalPanorama.getCamera().setPitchRange(0.0f, 0.0f);
        	cylindricalPanorama.setImage(gl, PLImage.imageWithBitmap(PLUtils.getBitmap(this, R.raw.pano_sphere), false));
            panorama = cylindricalPanorama;
        }
        //Add a hotspot
        panorama.addHotspot(new PLHotspot((kHotspotIdMin + Math.abs(random.nextInt()) % ((kHotspotIdMax + 1) - kHotspotIdMin)), PLImage.imageWithBitmap(PLUtils.getBitmap(this, R.raw.hotspot), false), 0.0f, 0.0f, 0.08f, 0.08f));
        //Load panorama
        this.reset();
        this.setPanorama(panorama);
        //Unlock panoramic view
        this.setBlocked(false);
    }
    
    /**
     * Load panorama image using JSON protocol
     * @param index Spinner position where 0 = spherical, 1 = spherical2, 2 = cubic, 3 = cylindrical
     */
    private void loadPanoramaFromJSON(int index)
    {
    	PLILoader loader = null;
    	if(index == 0)
    		loader = new PLJSONLoader(this, "res://raw/json_spherical");
    	else if(index == 1)
    		loader = new PLJSONLoader(this, "res://raw/json_spherical2");
    	else if(index == 2)
    		loader = new PLJSONLoader(this, "res://raw/json_cubic");
    	else if(index == 3)
    		loader = new PLJSONLoader(this, "res://raw/json_cylindrical");
    	this.load(loader);
    }
}
