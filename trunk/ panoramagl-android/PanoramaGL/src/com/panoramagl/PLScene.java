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

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public abstract class PLScene extends PLRenderableElementBase implements PLIScene
{	
	/**member variables*/
	
	private List<PLCamera> cameras;
	private PLCamera currentCamera;
	private int cameraIndex;

	private List<PLSceneElement> elements;
	
	private PLIView view;
	
	/**init methods*/
	
	public PLScene()
	{
		super();
		this.addCamera(PLCamera.camera());
	}

	public PLScene(PLCamera camera)
	{
		super();
		this.addCamera(camera);
	}
	
	public PLScene(PLSceneElement element)
	{
		this(element, PLCamera.camera());
	}
	
	public PLScene(PLSceneElement element, PLCamera camera)
	{
		super();
		this.addElement(element);
		this.addCamera(camera);
	}
	
	public PLScene(PLIView aView)
	{
		this();
		view = aView;
	}

	public PLScene(PLIView aView, PLCamera camera)
	{
		this(camera);
		view = aView;
	}

	public PLScene(PLIView aView, PLSceneElement element)
	{
		this(element);
		view = aView;
	}

	public PLScene(PLIView aView, PLSceneElement element, PLCamera camera)
	{
		this(element, camera);
		view = aView;
	}
	
	@Override
	protected void initializeValues()
	{
		super.initializeValues();
		elements = new ArrayList<PLSceneElement>();
		cameras = new ArrayList<PLCamera>();
	}
	
	/**property methods*/
	
	@Override
	public List<PLCamera> getCameras()
	{
		return cameras;
	}

	@Override
	public PLCamera getCurrentCamera()
	{
		return currentCamera;
	}
	
	@Override
	public PLCamera getCamera()
	{
		return currentCamera;
	}

	@Override
	public int getCameraIndex()
	{
		return cameraIndex;
	}
	
	@Override
	public void setCameraIndex(int index)
	{
		if(index < cameras.size())
		{
			cameraIndex = index;
			currentCamera = cameras.get(index);
		}
	}

	@Override
	public List<PLSceneElement> getElements()
	{
		synchronized(this)
		{
			return elements;
		}
	}
	
	@Override
	public PLIView getView()
	{
		return view;
	}
	
	@Override
	public void setAlpha(float value)
	{
		super.setAlpha(value);
		for(int i = 0; i < elements.size(); i++)
		{
			PLSceneElement element = elements.get(i);
			elements.get(i).setAlpha(Math.min(value, element.getDefaultAlpha()));
		}
	}
	
	/**reset methods*/
	
	@Override
	public void resetAlpha()
	{
		this.setAlpha(this.getDefaultAlpha());
		for(int i = 0; i < elements.size(); i++)
		{
			PLSceneElement element = elements.get(i);
			elements.get(i).setAlpha(element.getDefaultAlpha());
		}
	}
	
	/**camera methods*/
	
	@Override
	public void addCamera(PLCamera camera)
	{
		if(camera != null)
		{
			if(cameras.size() == 0)
			{
				cameraIndex = 0;
				currentCamera = camera;
			}
			cameras.add(camera);
		}
	}
	
	@Override
	public void addCamera(PLCamera camera, int index)
	{
		if(camera != null)
			cameras.add(index, camera);
	}

	@Override
	public void removeCameraAtIndex(int index)
	{
		cameras.remove(index);
		this.evaluateWhenCameraIsRemoved();
	}
	
	@Override
	public void removeCamera(PLCamera camera)
	{
		if(camera != null)
		{
			boolean flag = false;
			int i, camerasLength = cameras.size();
			for(i = 0; i < camerasLength; i++)
			{
				if(cameras.get(i) == camera)
				{
					flag = true;
					break;
				}
				
			}
			if(flag)
			{
				elements.remove(i);
				this.evaluateWhenCameraIsRemoved();
			}
		}
	}
	
	@Override
	public void removeAllCameras()
	{
		cameras.clear();
		this.evaluateWhenCameraIsRemoved();
	}
	
	protected void evaluateWhenCameraIsRemoved()
	{
		int camerasCount = cameras.size();
		if(camerasCount == 0)
		{
			currentCamera = null;
			cameraIndex = -1;
		}
		else if(cameraIndex > camerasCount - 1) 
		{
			currentCamera = cameras.get(0);
			cameraIndex = 0;
		}
		else
			currentCamera = cameras.get(cameraIndex);
	}
	
	/**element methods*/
	
	@Override
	public void addElement(PLSceneElement element)
	{
		elements.add(element);
	}
	
	@Override
	public void removeElement(PLSceneElement element)
	{
		elements.remove(element);
	}
	
	@Override
	public void removeElementAtIndex(int index)
	{
		elements.remove(index);
	}
	
	@Override
	public void removeAllElements(GL10 gl)
	{
		elements.clear();
	}
	
	/**render methods*/

	@Override
	protected void endRender(GL10 gl)
	{
		int elementsLength = elements.size();
		for(int i = 0; i < elementsLength; i++)
			elements.get(i).render(gl);
		super.endRender(gl);
	}
	
	/**PLIReleaseView methods*/
	
	@Override
	public void releaseView()
	{
		view = null;
	}
	
	/**dealloc methods*/

	@Override
	protected void finalize() throws Throwable
	{
		this.releaseView();
		elements = null;
		cameras = null;
		currentCamera = null;
		super.finalize();
	}
}