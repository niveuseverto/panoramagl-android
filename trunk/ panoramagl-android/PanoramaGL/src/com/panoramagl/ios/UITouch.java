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

import com.panoramagl.PLIReleaseView;
import com.panoramagl.ios.structs.CGPoint;

import android.app.Activity;

public class UITouch implements PLIReleaseView
{
	/**member variables*/
	
	private int tapCount;
	private Activity view;
	private CGPoint position;
	
	/**property methods*/
	
	public int getTapCount()
	{
		return tapCount;
	}
	
	public void setTapCount(int value)
	{
		if(value > 0)
			tapCount = value;
	}
	
	public Activity getView()
	{
		return view;
	}
	
	/**init methods*/
	
	public UITouch(Activity view)
	{
		this(view, CGPoint.CGPointMake(0.0f, 0.0f), 1);
	}
	
	public UITouch(Activity view, CGPoint position)
	{
		this(view, position, 1);
	}
	
	public UITouch(Activity view, CGPoint position, int tapCount)
	{
		super();
		this.view = view;
		this.position = position;
		this.tapCount = tapCount;
	}
	
	/**location methods*/
	
	public CGPoint locationInView(Activity view)
	{
		return position;
	}
	
	/**PLIReleaseView methods*/
	
	@Override
	public void releaseView()
	{
		view = null;
	}
}