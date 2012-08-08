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

import java.util.List;

import com.panoramagl.enumeration.PLHotspotTouchStatus;
import com.panoramagl.enumeration.PLSceneElementType;
import com.panoramagl.enumeration.PLTransitionType;
import com.panoramagl.PLConstants;
import com.panoramagl.PLIScene;
import com.panoramagl.PLSceneElement;
import com.panoramagl.hotspots.PLHotspot;

public abstract class PLTransitionFadeBase extends PLTransition
{
	/**member variables*/
	
	private float fadeStep;
	
	/**init methods*/

	protected PLTransitionFadeBase(float interval, PLTransitionType type)
	{
		super(interval, type);
	}
	
	@Override
	protected void initializeValues()
	{
		super.initializeValues();
		fadeStep = PLConstants.kDefaultStepFade;
	}
	
	/**reset methods*/
	
	public void resetSceneAlpha()
	{
		if(this.getView() != null)
			this.getView().resetSceneAlpha();
	}
	
	/**property methods*/

	public float getFadeStep()
	{
		return fadeStep;
	}
	
	public void setFadeStep(float value)
	{
		if(value > 0.0f)
			fadeStep = value;
	}
	
	/**internal methods*/

	@Override
	protected void beginExecute()
	{
		PLIScene scene = this.getScene();
		float alpha = 0.0f;
		
		switch(this.getType())
		{
			case PLTransitionTypeFadeIn:
				alpha = 0.0f;
				break;
			case PLTransitionTypeFadeOut:
				alpha = 0.0f;
				break;
		}
		
		List<PLSceneElement> elements = scene.getElements();
		int elementsLength = elements.size();
		for(int i = 0; i < elementsLength; i++)
		{
			PLSceneElement element = elements.get(i);
			if(element.getType() == PLSceneElementType.PLSceneElementTypeHotspot)
			{
				PLHotspot hotspot = (PLHotspot)element;
				if(hotspot.getTouchStatus() != PLHotspotTouchStatus.PLHotspotTouchStatusOut)
				{
					hotspot.touchOut(this);
					hotspot.touchBlock();
				}
			}
		}
		scene.setAlpha(Math.min(alpha, scene.getDefaultAlpha()));
		this.getView().drawView();
	}

	@Override
	protected boolean processInternally()
	{
		boolean isEnd = false;
		PLIScene scene = this.getScene();
		switch(this.getType())
		{
			case PLTransitionTypeFadeIn:
				scene.setAlpha(Math.min(scene.getAlpha() + fadeStep, scene.getDefaultAlpha()));
				this.setProgressPercentage((int)Math.min(scene.getAlpha() * 100.0f, 100.0f));
				isEnd = (scene.getAlpha() >= scene.getDefaultAlpha());
				if(isEnd)
					scene.resetAlpha();
				break;
			case PLTransitionTypeFadeOut:
				scene.setAlpha(scene.getAlpha() - fadeStep);
				this.setProgressPercentage((int)Math.max((1.0f - scene.getAlpha()) * 100.0f, 0.0f));
				isEnd = (scene.getAlpha() <= -fadeStep);
				break;
		}
		if(isEnd)
		{
			List<PLSceneElement> elements = scene.getElements();
			int elementsLength = elements.size();
			for(int i = 0; i < elementsLength; i++)
			{
				PLSceneElement element = elements.get(i);
				if(element.getType() == PLSceneElementType.PLSceneElementTypeHotspot)
					((PLHotspot)element).touchUnblock();
			}
		}
		return isEnd;
	}
}