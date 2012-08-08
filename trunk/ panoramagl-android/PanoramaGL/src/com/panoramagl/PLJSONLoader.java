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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import org.json.JSONArray;
import org.json.JSONObject;

import com.panoramagl.enumeration.PLCubeFaceOrientation;
import com.panoramagl.enumeration.PLPanoramaType;
import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.utils.PLUtils;

import android.content.Context;
import android.graphics.Bitmap;

public class PLJSONLoader extends PLObjectBase implements PLILoader
{
	/**member variables*/
	
	private Map<String, PLTexture> hotspotTextures;
	private JSONObject json;
	private Context context;
	private boolean hasPreviewImage;
	
	/**init methods*/

	public PLJSONLoader(String string)
	{
		super();
		this.loadJSON(string);
	}

	public PLJSONLoader(Context context, String url)
	{
		super();
		try
		{
			url = url.trim();
			InputStream is = null;
			if(url.indexOf("res://") == 0)
			{
				int sepPos = url.lastIndexOf("/");
				int resId = context.getResources().getIdentifier(url.substring(sepPos + 1), url.substring(6, sepPos), context.getPackageName());
				is = context.getResources().openRawResource(resId);
			}
			else
				is = context.openFileInput(url);
			byte[] bytes = new byte[is.available()];
			is.read(bytes);
			is.close();
			this.loadJSON(new String(bytes, "utf-8"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static PLJSONLoader loaderWithString(String string)
	{
		return new PLJSONLoader(string);
	}

	public static PLJSONLoader loaderWithUrl(Context context, String url)
	{
		return new PLJSONLoader(context, url);
	}

	@Override
	protected void initializeValues()
	{
		hotspotTextures = new HashMap<String, PLTexture>();
	}
	
	/**property methods*/
	
	protected Map<String, PLTexture> getHotspotTextures()
	{
		return hotspotTextures;
	}
	
	protected JSONObject getJSON()
	{
		return json;
	}

	/**utility methods*/

	protected void loadJSON(String jsonString)
	{
	    if(jsonString != null)
	    {
	    	try
	    	{
	    		json = new JSONObject(jsonString);
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		throw new RuntimeException("JSON parse failed", e);
	    	}
	    }
	    else
	    	throw new RuntimeException("JSON string is empty");     
	}

	protected Bitmap getBitmap(String filename, String urlbase)
	{
		filename = filename.trim();
		if(filename.indexOf("res://") == -1 && filename.indexOf("file://") == -1)
			filename = urlbase + "/" + filename;
		return PLUtils.getBitmap(context, filename);
	}
	
	protected PLTexture createTexture(String filename, String urlbase)
	{
		if(filename != null)
			return PLTexture.textureWithImage(PLImage.imageWithBitmap(this.getBitmap(filename, urlbase), false));
	    return null;
	}

	protected void loadCubicPanoramaTexture(GL10 gl, PLIPanorama panorama, PLCubeFaceOrientation face, JSONObject images, String property, String urlbase) throws Exception
	{
		if(images.has(property))
		{
			PLTexture texture = this.createTexture(images.getString(property), urlbase);
			if(texture != null)
				((PLCubicPanorama)panorama).setTexture(gl, texture, face);
			else if(!hasPreviewImage)
				throw new RuntimeException(String.format("images.%s property wrong value", property));
		}
		else if(!hasPreviewImage)
			throw new RuntimeException(String.format("images.%@ property not exists", property));
	}

	protected PLTexture createHotspotTexture(String filename, String urlbase)
	{
	    if(filename != null)
	    {
	    	filename = filename.trim();
			String url = (filename.indexOf("res://") == -1 && filename.indexOf("file://") == -1 ? urlbase + "/" + filename : filename);
	    	if(hotspotTextures.containsKey(url))
	    		return hotspotTextures.get(url);
	    	else
	    	{
	    		PLTexture texture = PLTexture.textureWithImage(PLImage.imageWithBitmap(this.getBitmap(url, urlbase), false));
	    		hotspotTextures.put(url, texture);
	            return texture;
	    	}
	    }
	    return null;
	}

	/**load methods*/

	public void load(PLIView view)
	{
	    if(view != null && json != null)
	    {
	    	context = view.getActivity();
	    	try
	    	{
	    		view.setBlocked(true);
		    	GL10 gl = view.getCurrentGL();
		        String urlbase = json.getString("urlBase").trim();
		        if(urlbase == null)
		        	throw new RuntimeException("urlBase property not exists");
		        else if(urlbase.indexOf("res://") != 0 && urlbase.indexOf("file://") != 0)
		        	throw new RuntimeException("urlBase property is wrong");
		        String type = json.getString("type").trim();
		        PLIPanorama panorama = null;
		        PLPanoramaType panoramaType = PLPanoramaType.PLPanoramaTypeUnknow;
		        if(type != null)
		        {
		        	if(type.equals("spherical"))
		            {
		                panoramaType = PLPanoramaType.PLPanoramaTypeSpherical;
		                panorama = new PLSphericalPanorama();
		            }
		            else if(type.equals("spherical2"))
		            {
		                panoramaType = PLPanoramaType.PLPanoramaTypeSpherical2;
		                panorama = new PLSpherical2Panorama();
		            }
		            else if(type.equals("cubic"))
		            {
		                panoramaType = PLPanoramaType.PLPanoramaTypeCubic;
		                panorama = new PLCubicPanorama();
		            }
		            else if(type.equals("cylindrical"))
		            {
		            	panoramaType = PLPanoramaType.PLPanoramaTypeCylindrical;
		            	panorama = new PLCylindricalPanorama();
		            }
		            if(panorama == null)
		            	throw new RuntimeException("Panorama type is wrong");
		        }
		        else
		        	throw new RuntimeException("type property not exists");
		        hasPreviewImage = false;
		        JSONObject images = json.getJSONObject("images");
		        if(images != null)
		        {
		        	if(images.has("preview"))
		        	{
		                panorama.setPreviewImage(gl, PLImage.imageWithBitmap(this.getBitmap(images.getString("preview"), urlbase), false));
		                hasPreviewImage = true;
		        	}
		            if(panoramaType == PLPanoramaType.PLPanoramaTypeSpherical)
		            {
		                if(images.has("image"))
		                {
		                    PLTexture imageTexture = this.createTexture(images.getString("image"), urlbase);
		                    if(imageTexture != null)
		                        ((PLSphericalPanorama)panorama).setTexture(gl, imageTexture);
		                    else if(!hasPreviewImage)
		                    	throw new RuntimeException("images.image property wrong value");
		                }
		                else if(!hasPreviewImage)
		                	throw new RuntimeException("images.image property not exists");
		            }
		            else if(panoramaType == PLPanoramaType.PLPanoramaTypeSpherical2)
		            {
		                if(images.has("image"))
		                {
		                	PLImage image = PLImage.imageWithBitmap(this.getBitmap(images.getString("image"), urlbase), false);
		                    if(image != null)
		                        ((PLSpherical2Panorama)panorama).setImage(gl, image);
		                    else if(!hasPreviewImage)
		                    	throw new RuntimeException("images.image property wrong value");
		                }
		                else if(!hasPreviewImage)
		                	throw new RuntimeException("images.image property not exists");
		            }
		            else if(panoramaType == PLPanoramaType.PLPanoramaTypeCubic)
		            {
		            	this.loadCubicPanoramaTexture(gl, panorama, PLCubeFaceOrientation.PLCubeFaceOrientationFront, images, "front", urlbase);
		            	this.loadCubicPanoramaTexture(gl, panorama, PLCubeFaceOrientation.PLCubeFaceOrientationBack, images, "back", urlbase);
		            	this.loadCubicPanoramaTexture(gl, panorama, PLCubeFaceOrientation.PLCubeFaceOrientationLeft, images, "left", urlbase);
		            	this.loadCubicPanoramaTexture(gl, panorama, PLCubeFaceOrientation.PLCubeFaceOrientationRight, images, "right", urlbase);
		            	this.loadCubicPanoramaTexture(gl, panorama, PLCubeFaceOrientation.PLCubeFaceOrientationUp, images, "up", urlbase);
		            	this.loadCubicPanoramaTexture(gl, panorama, PLCubeFaceOrientation.PLCubeFaceOrientationDown, images, "down", urlbase);
		            }
		            else if(panoramaType == PLPanoramaType.PLPanoramaTypeCylindrical)
		            {
		            	if(images.has("image"))
		                {
		                	PLTexture imageTexture = this.createTexture(images.getString("image"), urlbase);
		                    if(imageTexture != null)
		                        ((PLCylindricalPanorama)panorama).setTexture(gl, imageTexture);
		                    else if(!hasPreviewImage)
		                    	throw new RuntimeException("images.image property wrong value");
		                }
		                else if(!hasPreviewImage)
		                	throw new RuntimeException("images.image property not exists");
		            }
		            
		        }
		        else
		        	throw new RuntimeException("images property not exists");
		        
		        JSONObject camera = json.getJSONObject("camera");
		        if(camera != null)
		        {
		            if(camera.has("athmin") && camera.has("athmax") && camera.has("atvmin") && camera.has("atvmax") && camera.has("hlookat") && camera.has("vlookat"))
		            {
		                PLCamera currentCamera = panorama.getCurrentCamera();
		                int athmin = camera.getInt("athmin");
		                int athmax = camera.getInt("athmax");
		                int atvmin = camera.getInt("atvmin");
		                int atvmax = camera.getInt("atvmax");
		                int hlookat = camera.getInt("hlookat");
		                int vlookat = camera.getInt("vlookat");
		                currentCamera.setPitchRange(atvmin, atvmax);
		                currentCamera.setYawRange(athmin, athmax);
		                currentCamera.setInitialLookAt(vlookat, hlookat);
		            }
		            else
		            	throw new RuntimeException("camera properties are wrong");
		        }
		        if(hotspotTextures.size() > 0)
	    			hotspotTextures.clear();
		        JSONArray hotspots = json.getJSONArray("hotspots");
		        if(hotspots != null)
		        {
		            int hotspotsCount = hotspots.length();
		            for(int i = 0; i < hotspotsCount; i++)
		            {
		                JSONObject hotspot = hotspots.getJSONObject(i);
		                if(hotspot != null)
		                {
		                    if(hotspot.has("id") && hotspot.has("image") && hotspot.has("atv") && hotspot.has("ath") && hotspot.has("width") && hotspot.has("height"))
		                    {
		                        PLTexture hotspotTexture = this.createHotspotTexture(hotspot.getString("image"), urlbase);
		                        int identifier = hotspot.getInt("id");
		                        int atv = hotspot.getInt("atv");
		                        int ath = hotspot.getInt("ath");
		                        float width = (float)hotspot.getDouble("width");
		                        float height = (float)hotspot.getDouble("height");
		                        PLHotspot currentHotspot = PLHotspot.hotspotWithId(identifier, hotspotTexture, atv, ath, width, height);
		            			panorama.addHotspot(currentHotspot);
		                    }
		                }
		            }
		            hotspotTextures.clear();
		        }
		        view.reset();
		        view.setPanorama(panorama);
		        if(json.has("sensorialRotation") && json.getBoolean("sensorialRotation"))
		        	view.startSensorialRotation();
		        if(json.has("scrolling"))
		        {
		        	JSONObject scrolling = json.getJSONObject("scrolling");
		        	if(scrolling.has("enabled"))
		        		view.setScrollingEnabled(scrolling.getBoolean("enabled"));
		        }
		        if(json.has("inertia"))
		        {
		        	JSONObject inertia = json.getJSONObject("inertia");
		        	if(inertia.has("enabled"))
		        		view.setInertiaEnabled(inertia.getBoolean("enabled"));
		        	if(inertia.has("interval"))
		        		view.setInertiaInterval((float)inertia.getDouble("interval"));
		        }
		        if(json.has("accelerometer"))
		        {
		        	JSONObject accelerometer = json.getJSONObject("accelerometer");
		        	if(accelerometer.has("enabled"))
		        		view.setAccelerometerEnabled(accelerometer.getBoolean("enabled"));
		        	if(accelerometer.has("interval"))
		        		view.setAccelerometerInterval((float)accelerometer.getDouble("interval"));
		        	if(accelerometer.has("sensitivity"))
		        		view.setAccelerometerSensitivity((float)accelerometer.getDouble("sensitivity"));
		        	if(accelerometer.has("leftRightEnabled"))
		        		view.setAccelerometerLeftRightEnabled(accelerometer.getBoolean("leftRightEnabled"));
		        	if(accelerometer.has("upDownEnabled"))
		        		view.setAccelerometerUpDownEnabled(accelerometer.getBoolean("upDownEnabled"));
		        }
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		throw new RuntimeException(e.getMessage(), e);
	    	}
	    	finally
	    	{
	    		view.setBlocked(false);
	    		context = null;
	    	}
	    }
	}

	/**dealloc methods*/

	@Override
	protected void finalize() throws Throwable
	{
		context = null;
		json = null;
		hotspotTextures.clear();
		hotspotTextures = null;
		super.finalize();
	}
}