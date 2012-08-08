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

import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

//This class use Handler and Thread forms
//If you want use as Handler you must set runAsHandler = true by default is false
public class NSTimer extends Object
{
	/**static variables*/
	
	public static boolean runAsHandler = false;
	
	/**sub-interfaces declaration*/
	
	public interface Runnable
	{
		public void run(NSTimer target, Object[] userInfo);
	}
	
	/**member variables*/
	
	protected boolean isRunning;
	
	protected long interval;
	protected Runnable target;
	protected Object[] userInfo;
	protected boolean repeats;
	
	protected Thread thread;
	protected Handler handler;
	protected long lastTime, time;
	
	protected static final int NEXT_MESSAGE = 0;
	
	/**init methods*/
	
	public NSTimer(Date date, float interval, Runnable target, Object[] userInfo, boolean repeats)
	{
		super();
		
		this.isRunning = true;
		this.interval = (long)(interval * 1000.0f);
		this.target = target;
		this.userInfo = userInfo;
		this.repeats = repeats;
		this.lastTime = date.getTime();
		
		if(runAsHandler)
		{
			this.handler = new Handler(Looper.getMainLooper())
			{
				@SuppressLint("HandlerLeak")
				@Override
				public void handleMessage(Message message)
				{
					if(NSTimer.this.isRunning && message.what == NSTimer.NEXT_MESSAGE)
					{
						try
						{		
							NSTimer.this.target.run(NSTimer.this, NSTimer.this.userInfo);
						} 
						catch(Exception e)
						{
						}
						if(NSTimer.this.repeats)
						{
							removeMessages(NSTimer.NEXT_MESSAGE);
							sendMessageDelayed(obtainMessage(NSTimer.NEXT_MESSAGE), NSTimer.this.interval);
						}
						else
							NSTimer.this.invalidate();
					}
				}
			};
			this.handler.sendMessageDelayed(this.handler.obtainMessage(NSTimer.NEXT_MESSAGE), this.interval);
		}
		else
		{
			this.thread = new Thread(new java.lang.Runnable()
			{
				@Override
				public void run()
				{
					while(NSTimer.this.isRunning)
					{
						NSTimer.this.time = SystemClock.uptimeMillis();
						if(NSTimer.this.time - NSTimer.this.lastTime >= NSTimer.this.interval)
						{
							try
							{	
								NSTimer.this.target.run(NSTimer.this, NSTimer.this.userInfo);
							} 
							catch(Exception e)
							{
								Log.d("NSTimer", "NSTimer run: " + e.getMessage());
							}
							if(!NSTimer.this.repeats)
								NSTimer.this.invalidate();
						}
						NSTimer.this.lastTime = NSTimer.this.time;
						try
						{
							Thread.sleep(NSTimer.this.interval);
						}
						catch(Exception ex)
						{	
						}
					}
				}
			});
			this.thread.start();
		}
	}
	
	public static NSTimer scheduledTimerWithTimeInterval(float interval, Runnable target, Object[] userInfo, boolean repeats)
	{
		return new NSTimer(new Date(SystemClock.uptimeMillis()), interval, target, userInfo, repeats);
	}
	
	public void invalidate()
	{
		isRunning = false;
		if(handler != null)
			handler.removeMessages(NSTimer.NEXT_MESSAGE);
		handler = null;
		thread = null;
		target = null;
		userInfo = null;
	}
	
	public boolean isValid()
	{
		return isRunning;
	}
	
	/**dealloc methods*/
	
	@Override
	protected void finalize() throws Throwable
	{
		this.invalidate();
		super.finalize();
	}
}

