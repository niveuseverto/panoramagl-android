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

package com.panoramagl.transitions;

import com.panoramagl.PLIScene;
import com.panoramagl.PLIView;
import com.panoramagl.PLObjectBase;

import com.panoramagl.enumeration.PLTransitionType;
import com.panoramagl.PLConstants;
import com.panoramagl.ios.NSTimer;
import com.panoramagl.transitions.PLTransitionListener;

public abstract class PLTransition extends PLObjectBase implements PLITransition
{
	/**member variables*/
	
	private NSTimer timer;
	private float interval;
	private PLTransitionType type;
	private int progressPercentage;
	
	private PLIView view;
	private PLIScene scene;
	
	private boolean isRunning;

	private PLTransitionListener listener;
	
	/**init methods*/

	public PLTransition()
	{
		super();
	}

	public PLTransition(float interval, PLTransitionType type)
	{
		super();
		this.setInterval(interval);
		this.type = type;
	}

	@Override
	protected void initializeValues()
	{
		progressPercentage = 0;
		isRunning = false;
		listener = null;
	}
	
	/**property methods*/
	
	@Override
	public PLIView getView()
	{
		return view;
	}
	
	protected PLIScene getScene()
	{
		return scene;
	}
	
	@Override
	public float getInterval()
	{
		return interval;
	}

	@Override
	public void setInterval(float interval)
	{
		this.interval = (interval > 0.0f ? interval : PLConstants.kDefaultTransitionInterval);
	}
	
	@Override
	public int getProgressPercentage()
	{
		return progressPercentage;
	}
	
	protected void setProgressPercentage(int value)
	{
		progressPercentage = value;
	}
	
	@Override
	public PLTransitionType getType()
	{
		return type;
	}
	
	@Override
	public boolean isRunning()
	{
		return isRunning;
	}
	
	protected void setRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
	}
	
	protected NSTimer getTimer()
	{
		return timer;
	}
	
	protected void setTimer(NSTimer newTimer)
	{
		if(timer != null)
		{
			timer.invalidate();
			timer = null;
		}
		timer = newTimer;
	}
	
	@Override
	public PLTransitionListener getListener()
	{
		return listener;
	}
	
	@Override
	public void setListener(PLTransitionListener listener)
	{
		if(!isRunning && listener != null)
			this.listener = listener;
	}

	/**internal control methods*/

	protected void beginExecute()
	{
	}

	protected void endExecute()
	{
	}

	protected void process()
	{
		if(view != null && isRunning)
		{
			boolean isEnd = this.processInternally();
			view.drawView();
			
			if(listener != null)
				listener.didProcessTransition(this, type, progressPercentage);
			
			if(isEnd)
				this.stop();
		}
	}

	protected boolean processInternally()
	{
		return true;
	}
	
	/**control methods*/

	@Override
	public boolean start(PLIView plView, PLIScene plScene)
	{	
		if(isRunning || plView == null || plScene == null)
			return false;
		
		isRunning = true;
		
		view = plView;
		scene = plScene;
		progressPercentage = 0;
		this.setTimer(null);
		
		view.stopAnimation();
		
		this.beginExecute();
		
		timer = NSTimer.scheduledTimerWithTimeInterval
		(
			interval,
			new NSTimer.Runnable()
			{	
				@Override
				public void run(NSTimer target, Object[] userInfo)
				{
					process();
				}
			},
			null,
			false
		);
		
		if(listener != null)
			listener.didBeginTransition(this, type);
		
		this.endExecute();
		
		return true;
	}

	@Override
	public boolean stop()
	{
		if(isRunning)
		{
			synchronized(this)
			{
				isRunning = false;
				this.setTimer(null);
				view = null;
				scene = null;
				if(listener != null)
					listener.didEndTransition(this, type);
				return true;
			}
		}
		return false;
	}
	
	/**PLIReleaseView methods*/
	
	@Override
	public void releaseView()
	{
		view = null;
		scene = null;
		listener = null;
	}

	/**dealloc methods*/

	@Override
	protected void finalize() throws Throwable
	{
		this.setTimer(null);
		this.releaseView();
		super.finalize();
	}
}