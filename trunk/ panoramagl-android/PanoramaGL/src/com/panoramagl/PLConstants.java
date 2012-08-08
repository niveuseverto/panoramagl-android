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

public class PLConstants
{
	/**utility consts*/
	
	public static final float kPI = 			3.14159265358979323846f;
	public static final float kPI8 =			kPI / 8.0f;
	public static final float kPI16 =			kPI / 16.0f;
	public static final float kFloatMinValue = 	-1000000.0f;
	public static final float kFloatMaxValue =  Float.MAX_VALUE;
	public static final float kUndefinedValue = -5974.10456f;
	public static final float kToDegrees = 		180.0f / kPI;
	public static final float kToRadians =		kPI / 180.0f;
	
	/**object consts*/
	
	public static final float kObjectDefaultAlpha = 1.0f;
	
	/**buffer consts*/
	
	public static final int kUseDepthBuffer = 0;
	
	/**texture consts*/
	
	public static final int kTextureMaxWidth  = 	1024;
	public static final int kTextureMaxHeight = 	1024;
	public static final int kDefaultTextureSide = 	0;
	
	/**cube consts*/
	
	public static final int kCubeFrontFaceIndex =	0;
	public static final int kCubeBackFaceIndex	=	1;
	public static final int kCubeLeftFaceIndex	=	2;
	public static final int kCubeRightFaceIndex =	3;
	public static final int kCubeTopFaceIndex =		4;
	public static final int kCubeUpFaceIndex =		4;
	public static final int kCubeBottomFaceIndex =	5;
	public static final int kCubeDownFaceIndex =	5;
	
	/**sphere consts*/
	
	public static final int kDefaultSpherePreviewDivs =	50;
	public static final int kDefaultSphereDivs = 		50;
	
	/**sphere2 consts*/
	
	public static final int kDefaultSphere2PreviewDivs =	30;
	public static final int kDefaultSphere2Divs = 			20;

	/**cylinder consts*/
	
	public static final int kDefaultCylinderPreviewDivs =			60;
	public static final int kDefaultCylinderDivs =					60;
	public static final float  kDefaultCylinderHeight =				3.0f;
	public static final boolean kDefaultCylinderHeightCalculated = 	false;

	/**rotation consts*/
	
	public static final float kDefaultRotateSensitivity	=				30.0f;
	public static final float kDefaultAnimationTimerInterval =			1.0f/45.0f;
	public static final float kDefaultAnimationTimerIntervalByFrame =	1.0f/30.0f;
	public static final int kDefaultAnimationFrameInterval = 			1;

	public static final float kDefaultRotateMinRange = -180.0f;
	public static final float kDefaultRotateMaxRange =  180.0f;
	
	public static final float kDefaultYawMinRange = -180.f;
	public static final float kDefaultYawMaxRange =  180.f;

	public static final float kDefaultPitchMinRange = -90.0f;
	public static final float kDefaultPitchMaxRange =  90.0f;

	/**fov (field of view) consts*/
	
	public static final float kDefaultFov = -0.2f;
	public static final float kDefaultFovSensitivity = -1.0f;

	public static final float kFovMinValue = -1.0f;
	public static final float kFovMaxValue =  1.0f;
	
	public static final float kDefaultFovMinValue =	-0.2f;
	public static final float kDefaultFovMaxValue =	kFovMaxValue;

	public static final float kDefaultFovFactorMinValue = 0.8f;
	public static final float kDefaultFovFactorMaxValue = 1.20f;
	
	public static final float kFovFactorOffsetValue = 			1.0f;
	public static final float kFovFactorNegativeOffsetValue = 	kFovFactorOffsetValue - kDefaultFovFactorMinValue;
	public static final float kFovFactorPositiveOffsetValue = 	kDefaultFovFactorMaxValue - kFovFactorOffsetValue;
	
	public static final float kDefaultFovFactorCorrectedMinValue = 0.87f;
	public static final float kDefaultFovFactorCorrectedMaxValue = 1.1192f;

	public static final float kFovFactorCorrectedNegativeOffsetValue = 	kFovFactorOffsetValue - kDefaultFovFactorCorrectedMinValue;
	public static final float kFovFactorCorrectedPositiveOffsetValue = 	kDefaultFovFactorCorrectedMaxValue - kFovFactorOffsetValue;
	
	public static final int kDefaultMinDistanceToEnableFov = 5;
	
	public static final float kDefaultFovControlIncreaseDistance = 1600.0f;
	
	public static final int kDefaultFovMinCounter = 3;
	
	public static final float kZoomActionDistanceProportional = 7.0f;
	public static final long kZoomActionSleepInterval = 25;

	/**reset consts*/

	public static final int kDefaultNumberOfTouchesForReset = 3;
	
	/**inertia consts*/
	
	public static final int kDefaultInertiaInterval = 3;

	/**accelerometer consts*/
	
	public static final float kDefaultAccelerometerSensitivity =	10.0f;
	public static final float kDefaultAccelerometerInterval	= 		1.0f/30.0f;
	public static final float kAccelerometerSensitivityMinValue	=	1.0f;
	public static final float kAccelerometerSensitivityMaxValue	=	10.0f;
	public static final float kAccelerometerMultiplyFactor =		5.0f;
	
	/**gyroscope consts*/
	
	public static final float kGyroscopeTimeConversion = 	1.0f / 1000000000.0f;
	public static final float kGyroscopeMinTimeStep = 		1.0f / 40.0f;
	
	/**sensorial rotation consts*/
	
	public static final int kSensorialRotationThreshold =			300;
	public static final int kSensorialRotationPitchErrorMargin = 	7;
	public static final int kSensorialRotationYawErrorMargin = 		3;

	/**scrolling consts*/
	
	public static final int kDefaultMinDistanceToEnableScrolling = 50;
	
	/**drawing consts*/
	
	public static final int kDefaultMinDistanceToEnableDrawing = 10;
	
	/**perspective consts*/
	
	public static final float kPerspectiveValue =	290.0f;
	public static final float kPerspectiveZNear = 	0.01f;
	public static final float kPerspectiveZFar =	100.0f;
	
	/**scene-elements consts*/
	
	public static final float kRatio = 1.0f;
	
	/**shake consts*/
	
	public static final int kShakeThreshold = 	1000;
	public static final int kShakeDiffTime =	100;
	
	/**render consts*/
	
	public static final int kDrawLineWidth =	1;
	
	/**control consts*/
	
	public static final int kZoomControlMinWidth =				64;
	public static final int kZoomControlMinHeight =				40;
	public static final float kZoomControlWidthPercentage =		0.21f;
	public static final float kZoomControlHeightPercentage = 	0.08f;

	/**button consts*/
	
	public static final float kDefaultButtonSize =		0.2f;
	public static final float kDefaultButtonAlpha =		0.8f;
	public static final float kDefaultButtonOverAlpha =	1.0f;

	/**transition consts*/
	
	public static final float kDefaultTransitionInterval = 1.0f;
	public static final float kDefaultStepFade = 0.05f;
	public static final float kDefaultStepBlend = 0.05f;
	public static final float kDefaultStepZoomBlend = 0.06f;
	public static final float kDefaultMaxZoomBlend = 0.6f;
	
	/**hotspot consts*/
	
	public static final float kDefaultHotspotSize =			0.05f;
	public static final float kDefaultHotspotAlpha =		0.8f;
	public static final float kDefaultHotspotOverAlpha =	1.0f;
}