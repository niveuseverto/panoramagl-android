PanoramaGL Library
==================

Version: 0.1

Copyright (c) 2010 Javier Baez <javbaezga@gmail.com>

1. Project description
======================
PanoramaGL library was the first open source library in the world to see panoramic views on Android. The supported features in version 0.1 are:

- Tested with SDK 2.x to 4.x
- Support for architectures ARM, x86 and MIPS
- Supports OpenGL ES 1.x
- Supports spherical, cubic and cylindrical panoramic images
- Allows scrolling and continuous scrolling
- Supports scrolling left to right and from top to bottom using the accelerometer
- Allows to use the inertia to stop scrolling
- Supports zoom in and zoom out (moving two fingers on the screen)
- Supports reset (placing three fingers on the screen or shaking the device)
- Allows you to control the range of camera rotation in the x and y axis
- Support for view events
- Support for hotspots
- Support for sensorial rotation (Only compatible for devices with Accelerometer and Magnetometer)
- Support for simple JSON protocol

2. Licensing
============
* PanoramaGL is open source licensed under the Apache License version 2.0.
* Please, do not forget to put the credits in your application :).

2.1. PanoramaGL uses
====================
* glues: It is an OpenGL ES 1.0 CM port of part of GLU library by Mike Gorchak <mike@malva.ua> and licensed under SGI FREE SOFTWARE LICENSE B version 2.0.
* Matrix, MatrixGrabber, MatrixStack and MatrixTrackingGL classes: Copyright (C) 2007 The Android Open Source Project, licensed under Apache License version 2.0.

3. Requirements
===============
* OpenGL >= 1.0
* Android >= 2.0
* Some functionalities need the Accelerometer and Magnetometer
 
4. How to import PanoramaGL library?
====================================

4.1. From source code
=====================
a. Download PanoramaGL_0.1.zip or download the source code from repository
b. If you download the zip file then decompress the file
c. Import PanoramaGL project with Eclipse:
    - Go to File->Import menu
    - Select "Existing Projects into Workspace" and click on "Next" button
    - Click on "Browse" button and select PanoramaGL project folder
    - Click on "Finish" button
d. Right click on your project and select "Properties" option
e. Select on left panel "Android" option
f. In right panel go to "Library" section and click on "Add..." button
g. Select "PanoramaGL" library and click on "OK" button
h. Accept the changes selecting the "OK" button on right bottom corner in properties window

4.2. From the compiled files
============================
a. Download libglues.zip
b. Decompress the zip file and copy "libs" folder in your project
c. Download PanoramaGL_0.1.jar
d. Copy jar file in "libs" folder in your project
e. Import jar file in your project:
    - Right click on your project and select "Properties" option
    - Select on left panel "Java Build Path" option
    - Select "Libraries" tabulator
    - Click on "Add JARs..." button
    - Select "libs/PanoramaGL_0.1.jar" file from your project
    - Click on "OK" button
    - Accept the changes selecting the "OK" button on right bottom corner in properties window

5. How to use PanoramaGL in your application?
=============================================

a. Import the library as described on previous literal
b. Import a spherical image (e.g pano_sphere.jpg) in "res/raw" folder
c. In the Activity class that you need to do a panoramic viewer, do the next changes:
	
    - Inherit from PLView class
        public class YourActivity extends PLView
    - Inside of onCreate method load the panoramic image
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            PLSphericalPanorama panorama = new PLSphericalPanorama();
            panorama.setImage(this.getCurrentGL(), PLImage.imageWithBitmap(PLUtils.getBitmap(this, R.raw.pano_sphere)));
            this.setPanorama(panorama);
        }

Note: You can load panoramic images from other methods or events if you need it.

6. Simple JSON Protocol
=======================
Also, you can use JSON protocol to load panoramas. 

Note: JSON protocol is limited for local files in this version.

6.1. Source code
================

this.load(new PLJSONLoader(this, "res://raw/json_spherical"));

Note: For this code, I have a file named "json_spherical.data" in "res/raw" folder in my application.

or

this.load(new PLJSONLoader(this, "file:///sdcard/files/json_spherical.data"));

Note: For this code, I have a file named "json_spherical.data" in "/sdcard/files" folder in the Android device.

6.2. JSON Protocol
==================

{
    "urlBase": "file:///sdcard/files",  //URL base where the files are
                                        //The options are res:// for application resources and file:// for file system (this feature will be improved to support the http protocol)
    "type": "spherical",                //Panorama type: [spherical, spherical2, cubic, cylindrical]
    "sensorialRotation": false,         //Automatic rotation using sensors [true, false] <Optional>
    "scrolling":                        //Scrolling section <Optional>
    {
        "enabled": false                //Enable scrolling feature [true, false] <Optional>
    },
    "inertia":                          //Inertia section <Optional>
    {
        "enabled": false,               //Enable inertia feature [true, false] <Optional>
        "interval": 3                   //Inertia's interval in seconds <Optional>
    },
    "accelerometer":                    //Accelerometer section <Optional>
    {
        "enabled": false,               //Enable the accelerometer feature [true, false] <Optional>
        "interval": 0.033,              //Update interval of accelerometer (this value must be calculated as 1/frequency) <Optional>
        "sensitivity": 10.0,            //Sensitivity of the accelerometer <Optional>
        "leftRightEnabled": true,       //Enable the direction of movement with the accelerometer (left/right) <Optional>
        "upDownEnabled": false          //Enable the direction of movement with the accelerometer (up/down) <Optional>
    },
    "images":                           //Panoramic images section
                                        //A property can be a name e.g. preview.jpg, preview or URL e.g. file:///sdcard/files/preview.jpg, res://raw/preview
                                        //if a property only have a name, the real path will be the urlBase + image name
    {
        "preview": "preview.jpg",       //Preview image name or URL (this option will be used with http protocol) <Optional>
        "image": "pano.jpg"             //Panoramic image name or URL for spherical, spherical2 and cylindrical panoramas
        "front": "front.jpg",           //Front image name or URL for cubic panorama (only use with cubic panorama)
        "back": "back.jpg",             //Back image name or URL for cubic panorama (only use with cubic panorama)
        "left": "left.jpg",             //Left image name or URL for cubic panorama (only use with cubic panorama)
        "right": "right.jpg",           //Right image name or URL for cubic panorama (only use with cubic panorama)
        "up": "up.jpg",                 //Up image name or URL for cubic panorama (only use with cubic panorama)
        "down": "down.jpg"              //Down image name or URL for cubic panorama (only use with cubic panorama)
    },
    "camera":                           //Camera settings section <Optional>
    {
        "vlookat": 0,                   //Initial vertical position [-90, 90]
        "hlookat": 0,                   //Initial horizontal position [-180, 180]
        "atvmin": -90,                  //Min vertical position [-90, 90]
        "atvmax": 90,                   //Max vertical position [-90, 90]
        "athmin": -180,                 //Min horizontal position [-180, 180]
        "athmax": 180                   //Max horizontal position [-180, 180]
    },
    "hotspots": [                       //Hotspots section (this section is an array of hotspots) <Optional>
                {
                 "id": 1,               //Hotspot identifier (long)
                 "atv": 0,              //Vertical position [-90, 90]
                 "ath": 0,              //Horizontal position [-180, 180]
                 "width": 0.08,         //Width
                 "height": 0.08,        //Height
                 "image": "hotspot.png" //Image name or URL
                }
                ]
}

6.3. See
========
* PLJSONLoader class and PLView load method.
* The files json_spherical.data, json_spherical2.data, json_cylindrical.data and json_cubic.data in "res/raw" folder of "HelloPanoramaGL" example.

7. More information
===================
For more details, please check "HelloPanoramaGL" example, this example is compatible with Android 2.x or higher.

8. Supporting this project
==========================
If you want to support this project, please donate to my Paypal account

https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=TN942N9FFXYEL&lc=EC&item_name=PanoramaGL&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted