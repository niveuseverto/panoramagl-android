# Introduction #

This guide is a simple guideline to use _**PanoramaGL 0.2 beta**_ library, for more details please check _**[HelloPanoramaGL](http://panoramagl-android.googlecode.com/files/HelloPanoramaGL.zip)**_ example and read _**[README.txt ](https://code.google.com/p/panoramagl-android/source/browse/trunk/panoramagl-android/PanoramaGL/README.txt)**_ file.

# Details #

## 1. How to import PanoramaGL library? ##

---


## 1.1. From source code ##

a. Download _**[PanoramaGL\_0.2-beta.zip](http://panoramagl-android.googlecode.com/files/PanoramaGL_0.2-beta.zip)**_ or download the source code from _**[repository](http://code.google.com/p/panoramagl-android/source/checkout)**_<br />
b. If you download the zip file then decompress the file<br />
c. Import PanoramaGL project with Eclipse:
> - Go to **File->Import** menu<br />
> - Select **"Existing Projects into Workspace"** and click on **"Next"** button<br />
> - Click on **"Browse"** button and select PanoramaGL project folder<br />
> - Click on **"Finish"** button
d. Right click on your project and select **"Properties"** option<br />
e. Select on left panel **"Android"** option<br />
f. In right panel go to **"Library"** section and click on **"Add..."** button<br />
g. Select **"PanoramaGL"** library and click on **"OK"** button<br />
h. Accept the changes selecting the **"OK"** button on right bottom corner in properties window<br />

## 1.2. From the compiled files ##

a. Download _**[libglues\_0.2-beta.zip](http://panoramagl-android.googlecode.com/files/libglues_0.2-beta.zip)**_<br />
b. Decompress the zip file and copy **"libs"** folder in your project<br />
c. Download _**[PanoramaGL\_0.2-beta.jar](http://panoramagl-android.googlecode.com/files/PanoramaGL_0.2-beta.jar)**_<br />
d. Copy jar file in **"libs"** folder in your project<br />
e. Import jar file in your project:
> - Right click on your project and select **"Properties"** option<br />
> - Select on left panel **"Java Build Path"** option<br />
> - Select **"Libraries"** tabulator<br />
> - Click on **"Add JARs..."** button<br />
> - Select **"libs/PanoramaGL\_0.2-beta.jar"** file from your project<br />
> - Click on **"OK"** button<br />
> - Accept the changes selecting the **"OK"** button on right bottom corner in properties window

## 2. How to use PanoramaGL in your application? ##

---


a. Import the library as described on previous literal<br />
b. Import a spherical image _**(e.g spherical\_pano.jpg)**_ in **"res/raw"** folder<br />
c. In the Activity class that you need to make a panoramic viewer, do the next changes:

> - Inherit from PLView class
```
public class YourActivity extends PLView
```
> - Within the onCreate method, load the panoramic image
```
@Override
public void onCreate(Bundle savedInstanceState)
{
     super.onCreate(savedInstanceState);
     PLSpherical2Panorama panorama = new PLSpherical2Panorama();
     panorama.setImage(new PLImage(PLUtils.getBitmap(this, R.raw.spherical_pano), false));
     this.setPanorama(panorama);
}
```

**_Note:_** You can load panoramic images from other methods or events if you need it.

## 3. Simple JSON Protocol ##

---

You can use JSON protocol to load panoramas and to create virtual tours.

### 3.1. Source code ###

#### 3.1.1. From the resources ####

```
this.load(new PLJSONLoader("res://raw/json_spherical"));
```
**_Note:_** For this code, you need to have a file named **"json\_spherical.data"** in **"res/raw"** folder in your application.

#### 3.1.2. From the file system ####

```
this.load(new PLJSONLoader("file:///sdcard/files/json_spherical.data"));
```
**_Note:_** For this code, you need to have a file named **"json\_spherical.data"** in **"/sdcard/files"** folder on the Android device or emulator.

#### 3.1.3. From a Web server ####

```
this.load(new PLJSONLoader("http://yourdomain/files/json_spherical.data"));
```
**_Note:_** For this code, you need to have a file named **"json\_spherical.data"** in **"http://yourdomain/files"** Web server path.

### 3.2. JSON Protocol ###

```
{
    "urlBase": "file:///sdcard/files",		//URL base where the files are
						//The options are: http://, https://, res:// for application resources and file:// for the file system
    "type": "spherical",			//Panorama type: [spherical, spherical2, cubic, cylindrical]
    "keep": "none",				//Keeps the current settings [none, reset, scrolling, inertia, accelerometer, sensorialRotation, all] <Optional>
						//By default the value is none and you can use the options like a mask e.g. all|~scrolling that meaning keep all except the scrolling options
    "imageColorFormat": "RGBA8888",		//Color format to be used for all images [RGBA8888, RGB565, RGBA4444] <Optional>
    "height": 3.0,				//Sets the panorama's height only for cylindrical panorama <Optional>
    "divisions":				//Divisions section only for spherical, spherical2 and cylindrical panoramas <Optional>
    {
    	"preview": 50,				//Number of divisions for the preview panorama <Optional>
    	"panorama": 50				//Number of divisions for the panorama <Optional>
    },
    "reset":					//Reset section <Optional>
    {
    	"enabled": true,			//Enable reset feature [true, false] <Optional>
    	"numberOfTouches": 3,			//Number of touches to reset <Optional>
    	"shake":				//Shake reset section <Optional>
    	{
    		"enabled": true,		//Enable shake reset [true, false] <Optional>
    		"threshold": 1300		//Shake threshold <Optional>
    	}
    },
    "scrolling":				//Scrolling section <Optional>
    {
    	"enabled": true,			//Enable scrolling feature [true, false] <Optional>
    	"minDistanceToEnableScrolling": 30	//Minimum distance to enable scrolling in pixels <Optional>
    },
    "inertia":					//Inertia section <Optional>
    {
        "enabled": false,			//Enable inertia feature [true, false] <Optional>
        "interval": 3				//Inertia's interval in seconds <Optional>
    },
    "accelerometer":				//Accelerometer section <Optional>
    {
        "enabled": false,			//Enable the accelerometer feature [true, false] <Optional>
        "interval": 0.033,			//Update interval of accelerometer (this value must be calculated as 1/frequency) <Optional>
        "sensitivity": 10.0,			//Sensitivity of the accelerometer <Optional>
        "leftRightEnabled": true,		//Enable the movement (left/right) <Optional>
        "upDownEnabled": false			//Enable the movement (up/down) <Optional>
    },
    "sensorialRotation": false,			//Automatic rotation using sensors [true, false] <Optional>
    "images":					//Panoramic images section
						//Image properties can be a name e.g. preview.jpg, preview or an URL e.g. http://mydomain/files/preview.jpg, file:///sdcard/files/preview.jpg, res://raw/preview
						//if an image property only have a name, the real path will be the urlBase + image name
    {
    	"preload": true,			//Preload the images [true, false] <Optional>. Note: For HTTP is better to use preview option and preload option equal to false
        "preview": "preview.jpg",		//Preview image name or URL <Optional>
        "image": "pano.jpg",			//Panoramic image name or URL only for spherical, spherical2 and cylindrical panoramas
        "front": "front.jpg",			//Front image name or URL only for cubic panorama
        "back": "back.jpg",			//Back image name or URL only for cubic panorama
        "left": "left.jpg",			//Left image name or URL only for cubic panorama
        "right": "right.jpg",			//Right image name or URL only for cubic panorama
        "up": "up.jpg",				//Up image name or URL only for cubic panorama
        "down": "down.jpg"			//Down image name or URL only for cubic panorama
    },
    "camera":					//Camera section <Optional>
    {
    	"keep":	"none",				//Keeps the current camera settings [none, atvMin, atvMax, atvRange, athMin, athMax, athRange, reverseRotation, rotationSensitivity, vLookAt, hLookAt, rotation, zoomLevels, fovMin, fovMax, fovRange, fovSensitivity, fov, allRotation, allZoom, all] <Optional>
						//By default the value is none and you can use the options like a mask e.g. all|~atvRange that meaning keep all except the atvRange options
    	"atvMin": -90.0,			//Minimum vertical rotation in degrees [-90.0, 90.0] (down) <Optional>
        "atvMax": 90.0,				//Maximum vertical rotation in degrees [-90.0, 90.0] (up) <Optional>
        "athMin": -180.0,			//Minimum horizontal rotation in degrees [-180.0, 180.0] (left) <Optional>
        "athMax": 180.0,			//Maximum horizontal rotation in degrees [-180.0, 180.0] (right) <Optional>
        "reverseRotation": true,		//Reverse rotation [true, false] <Optional>
        "rotationSensitivity": 30.0,		//Rotation sensitivity in pixels [1.0, 180.0] <Optional>
        "vLookAt": 0.0,				//Initial vertical rotation in degrees [-90.0, 90.0] <Optional>
        "hLookAt": 0.0,				//Initial horizontal rotation in degrees [-180.0, 180.0] <Optional>
        "zoomLevels": 2,			//Zoom levels for zoom in and zoom out [1, ...] <Optional>
        "fovMin": 30.0,				//Minimum field of view in degrees [0.01, 179.0] <Optional>
        "fovMax": 90.0,				//Maximum field of view in degrees [0.01, 179.0] <Optional>
        "fovSensitivity": 30.0,			//Field of view sensitivity in pixels [1.0, 100.0] <Optional>
        "fov": 90.0,				//Initial field of view in degrees [0.01, 179.0] <Optional> Note: see notes at the end of literal
        "fovFactor": 1.0,			//Field of view factor [0.0, 1.0] <Optional> Note: see notes at the end of literal
        "zoomFactor": 0.0,			//Zoom factor [0.0, 1.0] <Optional> Note: see notes at the end of literal
        "zoomLevel": 0				//Zoom level [0, zoomLevels] <Optional> Note: see notes at the end of literal
    },
    "hotspots":					//Hotspots section (this section is an array of hotspots) <Optional>
    [
        {
            "id": 1,				//Identifier (Integer number) <Optional>
            "atv": 0.0,				//Vertical position in degrees [-90.0, 90.0] (down to up) <Optional>
            "ath": 0.0,				//Horizontal position in degrees [-180.0, 180.0] (left to right) <Optional>
            "width": 0.05,			//Width (panorama's diameter at percentage) [0.0, 1.0] e.g. 0.05 is the 5% <Optional>
            "height": 0.05,			//Height (panorama's diameter at percentage) [0.0, 1.0] e.g. 0.05 is the 5% <Optional>
            "image": "hotspot.png",		//Image name or URL e.g. hotspot.png or res://raw/hotspot
            "alpha": 0.8,			//Transparency when is not selected the hotspot [0.0, 1.0] <Optional> Note: see notes at the end of literal
            "overAlpha": 1.0,			//Transparency when is selected the hotspot [0.0, 1.0] <Optional> Note: see notes at the end of literal
            "onClick": "lookAt(0.0, 90.0)"	//onClick event <Optional> Note: see the literal 6.3
        }
    ]
}
```

**_Notes:_**
  * keep options retain the settings loaded before the JSON protocol in progress, allowing you to control the settings for creating virtual tours.
  * fov, fovFactor, zoomFactor and zoomLevel should not be used together because they have the same function, where the order of priority is fov, fovFactor, zoomFactor and finally zoomLevel.
  * alpha options are float values from 0.0 to 1.0, where 1.0 means without transparency and 0.0 completely transparent.
  * All options are case sensitive.

### 3.3. Hotspot commands ###
The commands are case sensitive and they can be used together using the semicolon character.

**Example:**

```
"onClick": "lookAt(0.0, 90.0, true); load('res://raw/json_spherical2', true, BLEND(3.0, 1.0))"
```

#### 3.3.1. Load ####
  * **Description:** Load a new panorama
  * **Syntax:**
```
load(url, <showProgressBar>, <transition>, <vLookAt>, <hLookAt>)
```
  * **Parameters:**
    1. **url:** a string with the JSON file URL to load
    1. **showProgressBar:** show progress bar [true, false] _(Optional)_
    1. **transition:** transition function or null _(Optional)_
      * **BLEND:** Blend transition
      * **Syntax:**
```
BLEND(interval, <zoomFactor>)
```
      * **Parameters:**
        1. **interval:** duration of the transition in seconds [1.0, ...]
        1. **zoomFactor:** zoom factor [0.0, 1.0] _(Optional)_
    1. **vLookAt:** camera vertical rotation for the new panorama in degrees [-90.0, 90.0] _(Optional)_
    1. **hLookAt:** camera horizontal rotation for the new panorama in degrees [-180.0, 180.0] _(Optional)_
  * **Examples:**
```
load('res://raw/json_cubic', true, BLEND(2.0, 1.0))
load('res://raw/json_cubic', false, null, 0.0, 90.0)
```

**_Note:_** The commands after load will not be executed.

#### 3.3.2. lookAt ####
  * **Description:** Set the camera rotation
  * **Syntax:**
```
lookAt(vLookAt, hLookAt, <animated>)
```
  * **Parameters:**
    1. **vLookAt:** vertical rotation in degrees [-90.0, 90.0]
    1. **hLookAt:** horizontal rotation in degrees [-180.0, 180.0]
    1. **animated:** using animation [true, false] _(Optional)_
  * **Examples:**
```
lookAt(0.0, 90.0, true)
lookAt(0.0, 90.0)
```

#### 3.3.3. lookAtAndZoom ####
  * **Description:** Set the rotation and zoom of the camera
  * **Syntax:**
```
lookAtAndZoom(vLookAt, hLookAt, zoomFactor, <animated>)
```
  * **Parameters:**
    1. **vLookAt:** vertical rotation in degrees [-90.0, 90.0]
    1. **hLookAt:** horizontal rotation in degrees [-180.0, 180.0]
    1. **zoomFactor:** zoom factor [0.0, 1.0]
    1. **animated:** using animation [true, false] _(Optional)_
  * **Examples:**
```
lookAtAndZoom(0.0, 90.0, 1.0, true)
lookAtAndZoom(0.0, 90.0, 1.0)
```

#### 3.3.4. zoom ####
  * **Description:** Set the camera zoom
  * **Syntax:**
```
zoom(zoomFactor, <animated>)
```
  * **Parameters:**
    1. **zoomFactor:** zoom factor [0.0, 1.0]
    1. **animated:** using animation [true, false] _(Optional)_
  * **Examples:**
```
zoom(1.0, true)
zoom(1.0)
```

#### 3.3.5. fov ####
  * **Description:** Set the field of view of the camera
  * **Syntax:**
```
fov(fov, <animated>)
```
  * **Parameters:**
    1. **fov:** field of view [0.01, 179.0]
    1. **animated:** using animation [true, false] _(Optional)_
  * **Examples:**
```
fov(90.0, true)
fov(90.0)
```

### 3.4. See ###

  * _PLJSONLoader_ class and _PLView_ load method.
  * The files _json\_spherical.data_, _json\_spherical2.data_, _json\_cylindrical.data_ and _json\_cubic.data_ are in **"res/raw"** folder of _**[HelloPanoramaGL](http://panoramagl-android.googlecode.com/files/HelloPanoramaGL.zip)**_ example.

## 4. More information ##

---

  * For more details check the _**[HelloPanoramaGL](http://panoramagl-android.googlecode.com/files/HelloPanoramaGL.zip)**_ example, it is compatible with Android 2.0 or higher.
  * To compile the HelloPanoramaGL example, you must import the library as described at the literal 1.1.

## 5. Supporting this project ##

---

If you want to support this project, please donate to my Paypal account:

[![](https://www.paypal.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=TN942N9FFXYEL&lc=EC&item_name=PanoramaGL&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)