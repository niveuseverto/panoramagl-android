# PanoramaGL Library #

---


**PanoramaGL library** was the first **open source** library in the world to see panoramic views on Android.

The supported features in version _**0.2 beta**_ are:

  * SDK 2.0 to 4.2.2.
  * Architectures ARM, x86 and MIPS.
  * OpenGL ES 1.0 and 1.1.
  * Support for spherical, cubic and cylindrical panoramic images.
  * Scrolling and continuous scrolling.
  * Inertia to stop continuous scrolling.
  * Zoom in and zoom out (moving two fingers on the screen).
  * Reset (placing three fingers on the screen or shaking the device).
  * Scrolling left to right and from top to bottom using the accelerometer.
  * Sensorial rotation (Only compatible for devices with Gyroscope or Accelerometer and Magnetometer).
  * Full control of camera including field of view, zoom, rotation, rotation range, animations, etc.
  * Hotspots with commands.
  * Simple JSON protocol.
  * Creation of virtual tours using the JSON protocol or with programming.
  * Transitions between panoramas.
  * Support for events.

## **Version 0.2 beta (September/2013):** ##

---


### Bugs fixes: ###

  * Bitmap recycle problem on Android 3.x or higher.
  * Aspect ratio changes with different screen sizes.
  * OpenGL 1.0 wrong axis rotation.
  * Camera rotation acceleration (too fast by default).
  * Camera rotation sensitivity is too fast when the zoom increases.
  * Camera field of view range (wrong range).
  * Camera field of view sensitivity (too fast by default).
  * Camera properties vLookAt and hLookAt in JSON protocol do not work properly.
  * Reset with 3 fingers on screen (works when the Touch Move event is fired).
  * Problem with scrolling and inertia together.
  * setImage method in PLSpherical2Panorama only loads images of 2048x1024 pixels.
  * Hotspot click detection (vertical offset problem).
  * Accelerometer problem with camera reverse rotation.
  * Sensorial rotation and camera initial rotation.
  * Android emulator detection.

### Improvements: ###

  * Support for SDK 2.0 to 4.2.2.
  * Supports UI hardware acceleration for Android 3.x or higher.
  * New options and HTTP/S support for JSON protocol. Note: some property names had changed for camera.
  * JSON protocol can be used to create virtual tours.
  * Supports transitions between panoramas.
  * New options for camera: setZoomLevels, setZoomLevel, setZoomFactor, lookAtAndFov, lookAtAndFovFactor and lookAtAndZoomFactor methods.
  * Camera animations: setFov, setFovFactor, setZoomLevel, setZoomFactor, lookAt, lookAtAndFov, lookAtAndFovFactor and lookAtAndZoomFactor methods.
  * Commands for Hotspot using the onClick property: load, lookAt, lookAtAndZoom, zoom and fov commands.
  * Hotspot width and height are float values from 0.0 to 1.0, that represent the panorama's diameter at percentage.
  * ProgressBar in PLView: isProgressBarVisible, showProgressBar, hideProgressBar and load methods.
  * onContentViewCreated event in PLView: Allows to return the root content view that Activity will use.
  * Sensorial rotation now supports gyroscope and works together with touch events.
  * Optimizations for glues library.

## Notes: ##

---


  * PanoramaGL only supports images with sizes at power of two **e.g.** 2048x1024, 1024x1024, 1024x512, 512x512, 512x256, 256x256, 256x128.


## Resources: ##

---


**Docs:** [User Guide](http://code.google.com/p/panoramagl-android/wiki/UserGuide)

**Code:** [Source Code ](http://panoramagl-android.googlecode.com/files/PanoramaGL_0.2-beta.zip) - [Example](http://panoramagl-android.googlecode.com/files/HelloPanoramaGL.zip)

**Installer:** [Example Installer](http://panoramagl-android.googlecode.com/files/HelloPanoramaGL.apk)

**Video:** [HelloPanorama Example](http://www.youtube.com/watch?v=1eSnM9eL3l0)

**Author:** _**[Javier Baez](mailto:javbaezga@gmail.com)**_ _**<[javbaezga@gmail.com](mailto:javbaezga@gmail.com)>**_
<br />
## Supporting this project: ##

---


If you want to support this project, please donate to my Paypal account

[![](https://www.paypal.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=TN942N9FFXYEL&lc=EC&item_name=PanoramaGL&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)

Please, do not forget to put the credits in your application :).