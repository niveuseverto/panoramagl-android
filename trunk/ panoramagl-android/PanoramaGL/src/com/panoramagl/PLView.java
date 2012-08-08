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

import com.panoramagl.ios.structs.CGSize;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class PLView extends PLViewBase
{
	/**member variables*/
	
	private RelativeLayout controlsLayout;
	private ProgressBar progressBar;
	
	/**property methods*/
	
	@Override
	public RelativeLayout getControlsLayout()
	{
		if(controlsLayout == null)
		{
			controlsLayout = new RelativeLayout(this);
			this.addContentView(controlsLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		return controlsLayout;
	}
	
	protected ProgressBar getProgressBar()
	{
		return progressBar;
	}
	
	@Override
	public boolean isProgressBarVisible()
	{
		return (progressBar != null && progressBar.getRootView() == this.getControlsLayout());
	}
	
	/**progressbar methods*/

	@Override
	public boolean showProgressBar()
	{
		if(progressBar == null)
		{
			CGSize size = this.getSize();
			int dimension = (int)((size.width > size.height ? size.height : size.width) * 0.15f);
			LayoutParams layoutParams = new LayoutParams(dimension, dimension);
			layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			progressBar = new ProgressBar(this);
			progressBar.setIndeterminate(true);
			this.getControlsLayout().addView(progressBar, layoutParams);
			return true;
		}
		return false;
	}

	@Override
	public boolean hideProgressBar()
	{
		if(progressBar != null)
		{
			progressBar.setIndeterminate(false);
			this.getControlsLayout().removeView(progressBar);
			progressBar = null;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean resetProgressBar()
	{
		if(progressBar != null)
		{
			this.hideProgressBar();
			this.showProgressBar();
			return true;
		}
		return false;
	}
	
	/**clear methods*/

	@Override
	public void clear()
	{
	    PLIPanorama panorama = this.getPanorama();
	    if(panorama != null)
	    {
	        synchronized(this)
	        {
	        	panorama.clearPanorama(this.getCurrentGL());
	        }
	    }
	}

	/**load methods*/

	@Override
	public void load(PLILoader loader)
	{
	    if(loader != null)
	    {
	        synchronized(this)
	        {
	            loader.load(this);
	        }
	    }
	    else
	    	throw new RuntimeException("PLView::load -> loader arg is null");
	}

	/**dealloc methods*/

	@Override
	protected void onDestroy() 
	{
		this.hideProgressBar();
		super.onDestroy();
	}
}