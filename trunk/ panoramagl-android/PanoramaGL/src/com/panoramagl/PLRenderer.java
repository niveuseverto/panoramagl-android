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

import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

import com.panoramagl.computation.PLIntersection;
import com.panoramagl.computation.PLVector3;
import com.panoramagl.enumeration.PLHotspotTouchStatus;
import com.panoramagl.enumeration.PLOpenGLVersion;

import com.panoramagl.opengl.GLUES;
import com.panoramagl.opengl.matrix.MatrixGrabber;
import com.panoramagl.opengl.matrix.MatrixTrackingGL;

import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.ios.EAGLContext;
import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.ios.structs.CGSize;
import com.panoramagl.structs.PLPosition;
import com.panoramagl.structs.PLRGBA;
import com.panoramagl.utils.PLUtils;

public class PLRenderer extends PLObjectBase implements PLIRenderer
{
	/**member variables*/
	
	private boolean isValid;
	
	private int[] backingWidth, backingHeight;

	private int[] defaultFramebuffer, colorRenderbuffer;

	private PLIView view;
	private PLIScene scene;

	private float aspect;
	
	private MatrixGrabber matrixGrabber;
	
	private FloatBuffer drawLineBuffer;
	
	private boolean isWaitingForClick;
	
	private PLCollisionData collisionData;
	
	private float[] modelMatrix, projectionMatrix;
	private int[] viewport;
	private float[] position;
	
	private boolean contextSupportsFrameBufferObject;
	private boolean isGLContextCreated;
	private CGSize size;
	
	private boolean isHigherThanOpenGL1;
	private MatrixTrackingGL legacyGL;
	
	private PLRendererListener listener;

	/**init methods*/

	public PLRenderer(PLIView aView, PLIScene aScene)
	{
		super();
		isValid = false;
		this.setView(aView);
		this.setScene(aScene);
	}

	public static PLRenderer rendererWithView(PLIView view, PLIScene scene)
	{
		return new PLRenderer(view, scene);
	}

	@Override
	protected void initializeValues()
	{
		backingWidth = new int[1];
		backingHeight = new int[1];

		defaultFramebuffer = new int[1];
		colorRenderbuffer = new int[1];
		
		matrixGrabber = new MatrixGrabber();
		
		collisionData = PLCollisionData.collisionData();
		
		modelMatrix = new float[16];
		projectionMatrix = new float[16];
		viewport = new int[4];
		position = new float[3];
		
		isWaitingForClick = false;
		
		size = CGSize.CGSizeMake(0.0f, 0.0f);
		
		isGLContextCreated = contextSupportsFrameBufferObject = false;
	}
	
	/**property methods*/
	
	@Override
	public boolean getWaitingForClick()
	{
		return isWaitingForClick;
	}
	
	@Override
	public void setWaitingForClick(boolean value)
	{
		this.isWaitingForClick = value;
	}

	@Override
	public int getBackingWidth()
	{
		return backingWidth[0];
	}

	@Override
	public int getBackingHeight()
	{
		return backingHeight[0];
	}
	
	@Override
	public PLIView getView()
	{
		return view;
	}

	@Override
	public void setView(PLIView view)
	{
		this.view = view;
	}

	@Override
	public PLIScene getScene()
	{
		return scene;
	}

	@Override
	public void setScene(PLIScene scene)
	{
		this.scene = scene;
	}
	
	@Override
	public boolean isValid()
	{
		return isValid;
	}
	
	protected boolean getContextSupportsFrameBufferObject()
	{
		return contextSupportsFrameBufferObject;
	}
	
	protected boolean isHigherThanOpenGL1()
	{
		return isHigherThanOpenGL1;
	}
	
	protected MatrixTrackingGL getLegacyGL()
	{
		return legacyGL;
	}
	
	@Override
	public PLRendererListener getListener()
	{
		return listener;
	}

	@Override
	public void setListener(PLRendererListener listener)
	{
		this.listener = listener;
	}
	
	public CGSize getSize()
	{
		return size;
	}
	
	/**buffer methods*/
	
	protected void createFrameBuffer(GL11ExtensionPack gl11ep)
	{
		if(contextSupportsFrameBufferObject)
		{
	        gl11ep.glGenFramebuffersOES(1, defaultFramebuffer, 0);
	        if(defaultFramebuffer[0] <= 0)
	        	PLLog.error("PLRenderer::createFrameBuffer", "Invalid framebuffer id returned!");
	        gl11ep.glGenRenderbuffersOES(1, colorRenderbuffer, 0);
	        if(colorRenderbuffer[0] <= 0)
	        	PLLog.error("PLRenderer::createFrameBuffer", "Invalid renderbuffer id returned!");
	        gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES, defaultFramebuffer[0]);
	        gl11ep.glBindRenderbufferOES(GL11ExtensionPack.GL_RENDERBUFFER_OES, colorRenderbuffer[0]);
		}
	}

	protected void destroyFramebuffer(GL11ExtensionPack gl11ep)
	{
		if(contextSupportsFrameBufferObject)
		{
			if(defaultFramebuffer[0] != 0)
		    {
				gl11ep.glDeleteFramebuffersOES(1, defaultFramebuffer, 0);
		        defaultFramebuffer[0] = 0;
		    }
		    if(colorRenderbuffer[0] != 0)
		    {
		    	gl11ep.glDeleteRenderbuffersOES(1, colorRenderbuffer, 0);
		        colorRenderbuffer[0] = 0;
		    }
		}
	}
	
	@Override
	public boolean resizeFromLayer()
	{
		return this.resizeFromLayer(null);
	}
	
	@Override
	public boolean resizeFromLayer(GL11ExtensionPack gl11ep)
	{	
		if(contextSupportsFrameBufferObject && gl11ep != null)
		{
			synchronized(this)
			{
				if(backingWidth[0] != size.width || backingHeight[0] != size.height)
				{
					isValid = false;
					
					this.destroyFramebuffer(gl11ep);
					this.createFrameBuffer(gl11ep);
					
		            gl11ep.glRenderbufferStorageOES(GL11ExtensionPack.GL_RENDERBUFFER_OES,
		                    GL11ExtensionPack.GL_RGBA8, size.width, size.height);
		            gl11ep.glFramebufferRenderbufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES,
		                    GL11ExtensionPack.GL_COLOR_ATTACHMENT0_OES,
		                    GL11ExtensionPack.GL_RENDERBUFFER_OES, colorRenderbuffer[0]);

		            gl11ep.glGetRenderbufferParameterivOES(GL11ExtensionPack.GL_RENDERBUFFER_OES, GL11ExtensionPack.GL_RENDERBUFFER_WIDTH_OES, backingWidth, 0);
			        gl11ep.glGetRenderbufferParameterivOES(GL11ExtensionPack.GL_RENDERBUFFER_OES, GL11ExtensionPack.GL_RENDERBUFFER_HEIGHT_OES, backingHeight, 0);
			        
					aspect = (float)backingWidth[0]/(float)backingHeight[0];
					
					if(scene != null && scene.getCurrentCamera().getFovSensitivity() == PLConstants.kDefaultFovSensitivity)
						scene.getCurrentCamera().setFovSensitivity((aspect > 1.0f ? backingWidth[0] : backingHeight[0]) * 10.0f);
					
					if(gl11ep.glCheckFramebufferStatusOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES) != GL11ExtensionPack.GL_FRAMEBUFFER_COMPLETE_OES)
					{
						PLLog.error("PLRenderer::resizeFromLayer", "Failed to make complete framebuffer object %x", gl11ep.glCheckFramebufferStatusOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES));
						return false;
					}
					
					isValid = true;
					return true;
				}
			}
		}
		else
		{
			synchronized(this)
			{
				aspect = (float)size.width/(float)size.height;
				if(scene != null && scene.getCurrentCamera().getFovSensitivity() == PLConstants.kDefaultFovSensitivity)
					scene.getCurrentCamera().setFovSensitivity((aspect > 1.0f ? size.width : size.height) * 10.0f);
			}
		}
		return false;
	}

	/**render methods*/
	
	@Override
	public void render(GL10 gl)
	{
		try
		{
			if(gl != null && isValid)
			{
				if(contextSupportsFrameBufferObject)
				{
					GL11ExtensionPack gl11ep = (GL11ExtensionPack)EAGLContext.contextGL();
					gl11ep.glBindFramebufferOES(GL11ExtensionPack.GL_FRAMEBUFFER_OES, defaultFramebuffer[0]);
				}
				
				gl.glViewport(0, 0, size.width, size.height);
				
				gl.glMatrixMode(GL10.GL_PROJECTION);
				gl.glLoadIdentity();
				
				PLCamera camera = scene.getCurrentCamera();
				boolean isCorrectedRatio = (size.width > size.height);
				float zoomFactor = (camera.isFovEnabled() ? (isCorrectedRatio ? camera.getFovFactorCorrected() : camera.getFovFactor()): 1.0f);
				float perspective = PLConstants.kPerspectiveValue + (isCorrectedRatio ? 25.0f : 0.0f);
								
				GLUES.gluPerspective(gl, Math.min(perspective * zoomFactor, 359.0f), aspect, PLConstants.kPerspectiveZNear, PLConstants.kPerspectiveZFar);
				
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				
				gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
				gl.glClearDepthf(1.0f);
				
				gl.glClear(GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_COLOR_BUFFER_BIT);
				
				gl.glEnable(GL10.GL_DEPTH_TEST);
				gl.glDepthFunc(GL10.GL_LEQUAL);
				gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
				
				gl.glTranslatef(0.0f, 0.0f, 0.0f);
				gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
				
				this.getMatrixes(gl);
				
				camera.render(gl);
				scene.render(gl);
				
				this.getMatrixes(gl);
				
				if(!view.isValidForFov() && !view.isValidForTransition())
				{
					this.checkCollisionsWithScreenPoint(gl, view.getEndPoint(), !isWaitingForClick);
					isWaitingForClick = false;
				}
				
				gl.glFlush();
				
				if(contextSupportsFrameBufferObject)
				{
					GL11ExtensionPack gl11ep = (GL11ExtensionPack)EAGLContext.contextGL();
					gl11ep.glBindRenderbufferOES(GL11ExtensionPack.GL_RENDERBUFFER_OES, colorRenderbuffer[0]);
				}
			}
		}
		catch(Exception e)
		{
			PLLog.debug("PLRenderer::render", e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void renderNTimes(GL10 gl, int times)
	{
		for(int i = 0; i < times; i++)
			this.render(gl);
	}
	
	/**collition methods*/

	protected int checkCollisionsWithScreenPoint(GL10 gl, CGPoint screenPoint, boolean isMoving)
	{
		if(view.isValidForTransition())
			return 0;
		
		int result = 0;
		this.createRayWithScreenPoint(gl, screenPoint, collisionData.ray);
		result = this.checkHotspotsCollisionWithRay(gl, collisionData.ray, isMoving, screenPoint);
		return result;
	}
	
	protected void getMatrixes(GL10 gl)
	{
		if(isHigherThanOpenGL1)
		{
			GL11 gl11 = (GL11)EAGLContext.contextGL();
			gl11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelMatrix, 0);
			gl11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projectionMatrix, 0);
		}
		else
		{
			matrixGrabber.getCurrentModelView(gl);
			matrixGrabber.getCurrentProjection(gl);
			modelMatrix = matrixGrabber.mModelView;
			projectionMatrix = matrixGrabber.mProjection;
		}
	}
	
	protected void createRayWithScreenPoint(GL10 gl, CGPoint point, PLVector3[] ray)
	{
		viewport[0] = 0;
		viewport[1] = 0;
		viewport[2] = size.width;
		viewport[3] = size.height;
		
		position[0] = 0.0f;
		position[1] = 0.0f;
		position[2] = 0.0f;

		float y = (float)size.height - point.y;
		
		GLUES.gluUnProject(point.x, y, 0.0f, modelMatrix, 0, projectionMatrix, 0, viewport, 0, position, 0);
		ray[0].setValues(position);
		GLUES.gluUnProject(point.x, y, 1.0f, modelMatrix, 0, projectionMatrix, 0, viewport, 0, position, 0);
		ray[1].setValues(position);
	}
	
	protected void convertPointTo3DPoint(GL10 gl, CGPoint point, float z, PLPosition result)
	{
		viewport[0] = 0;
		viewport[1] = 0;
		viewport[2] = size.width;
		viewport[3] = size.height;
		float y = (float)size.height - point.y;
		GLUES.gluUnProject(point.x, y, z, modelMatrix, 0, projectionMatrix, 0, viewport, 0, position, 0);
		result.setValues(position[0], position[1], position[2]);	
	}
	
	protected int checkHotspotsCollisionWithRay(GL10 gl, PLVector3[] ray, boolean isMoving, CGPoint screenPoint)
	{
		int hits = 0;
		if(scene == null)
			return hits;
		List<PLSceneElement> elements = scene.getElements();
		int elementsLength = elements.size();
		for(int i = 0; i < elementsLength; i++)
		{
			PLSceneElement element = elements.get(i);
			if(element instanceof PLHotspot)
			{
				PLHotspot hotspot = (PLHotspot)element;
				float vertexs[] = hotspot.getVertexs();
				if(vertexs == null)
					continue;
	
				collisionData.points[0].setValues(vertexs[0] ,-vertexs[2]  ,vertexs[1]);
				collisionData.points[1].setValues(vertexs[3] ,-vertexs[5]  ,vertexs[4]);
				collisionData.points[2].setValues(vertexs[6] ,-vertexs[8]  ,vertexs[7]);
				collisionData.points[3].setValues(vertexs[9] ,-vertexs[11] ,vertexs[10]);
	
				if(PLIntersection.checkLineBox(ray, collisionData.points[0], collisionData.points[1], collisionData.points[2], collisionData.points[3], collisionData.hitPoint))
				{
					if(view.isPointerVisible())
						this.highlightVertex(gl, collisionData.hitPoint[0], PLRGBA.PLRGBAMake(1.0f, 0.0f, 0.0f, 1.0f));
					if(isMoving)
					{
						if(hotspot.getTouchStatus() == PLHotspotTouchStatus.PLHotspotTouchStatusOut)
						{
							hotspot.touchOver(this);
							this.performHotspotOverEvent(view, hotspot, screenPoint, collisionData.hitPoint[0].getPosition(new PLPosition()));
						}
						else
							hotspot.touchMove(this);
					}
					else
					{
						view.setBlocked(true);
						view.getStartPoint().setValues(view.getEndPoint().x, view.getEndPoint().y);
						hotspot.touchDown(this);
						this.performHotspotClickEvent(view, hotspot, screenPoint, collisionData.hitPoint[0].getPosition(new PLPosition()));
						break;
					}
					hits++;
				}
				else
				{
					if(hotspot.getTouchStatus() != PLHotspotTouchStatus.PLHotspotTouchStatusOut)
					{
						hotspot.touchOut(this);
						this.performHotspotOutEvent(view, hotspot, screenPoint, collisionData.hitPoint[0].getPosition(new PLPosition()));
					}
				}
			}
		}
		return hits;
	}
	
	/**hotspot event methods*/
	
	protected void performHotspotClickEvent(PLIView view, PLHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
	{
		view.getActivity().runOnUiThread(new Runnable(view, hotspot, screenPoint, scene3DPoint)
		{
			@Override
			public void run()
			{
				PLIView view = this.getView();
				if(view.getListener() != null)
					view.getListener().onDidClickHotspot(view, this.getHotspot(), this.getScreenPoint(), this.getScene3DPoint());
				view.setBlocked(false);
			}
		});
	}
	
	protected void performHotspotOverEvent(PLIView view, PLHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
	{
		view.getActivity().runOnUiThread(new Runnable(view, hotspot, screenPoint, scene3DPoint)
		{
			@Override
			public void run()
			{
				PLIView view = this.getView();
				if(view.getListener() != null)
					view.getListener().onDidOverHotspot(view, this.getHotspot(), this.getScreenPoint(), this.getScene3DPoint());
			}
		});
	}
	
	protected void performHotspotOutEvent(PLIView view, PLHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
	{
		view.getActivity().runOnUiThread(new Runnable(view, hotspot, screenPoint, scene3DPoint)
		{
			@Override
			public void run()
			{
				PLIView view = this.getView();
				if(view.getListener() != null)
					view.getListener().onDidOutHotspot(view, this.getHotspot(), this.getScreenPoint(), this.getScene3DPoint());
			}
		});
	}

	/**draw methods*/

	protected void setColor(GL10 gl, float red, float green, float blue, float alpha)
	{
		gl.glColor4f(red, green, blue, alpha);
	}

	protected void drawLineWith(GL10 gl, PLVector3 startPoint, PLVector3 endPoint, float width)
	{
		float points[] = { startPoint.x, startPoint.y, startPoint.z, endPoint.x, endPoint.y, endPoint.z };
		
		if(drawLineBuffer == null)
			drawLineBuffer = PLUtils.makeFloatBuffer(points);
		else
		{
			drawLineBuffer.put(points);
			drawLineBuffer.position(0);
		}
		
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_LINE_SMOOTH);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glLineWidth(width);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, drawLineBuffer);
		gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 2);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_LINE_SMOOTH);
	}

	protected void drawLineWith(GL10 gl, PLVector3 startPoint, PLVector3 endPoint)
	{
		this.drawLineWith(gl, startPoint, endPoint, PLConstants.kDrawLineWidth);
	}

	protected void highlightVertex(GL10 gl, PLVector3 vertex, PLRGBA rgba)
	{
		gl.glPushMatrix();
		
		gl.glColor4f(rgba.red, rgba.green, rgba.blue, rgba.alpha);
		
		final float DD = 0.008f;
		this.drawLineWith(gl, (PLVector3)vertex.sub(PLVector3.vector3(-DD,  DD,  DD)), (PLVector3)vertex.add(PLVector3.vector3(-DD,  DD,  DD)), PLConstants.kDrawLineWidth);
		this.drawLineWith(gl, (PLVector3)vertex.sub(PLVector3.vector3( DD, -DD,  DD)), (PLVector3)vertex.add(PLVector3.vector3( DD, -DD,  DD)), PLConstants.kDrawLineWidth);
		this.drawLineWith(gl, (PLVector3)vertex.sub(PLVector3.vector3( DD,  DD, -DD)), (PLVector3)vertex.add(PLVector3.vector3( DD,  DD, -DD)), PLConstants.kDrawLineWidth);
		
		gl.glPopMatrix();
		
		gl.glColor4f(1.0f, 1.0f, 1.0f , 1.0f);
	}
	
	/**control methods*/
	
	@Override
	public void start()
	{
		if(!isValid)
	    {
	        synchronized(this)
	        {
	            isValid = true;
	        }
	    }
	}
	
	@Override
	public boolean isRunning()
	{
		return isValid;
	}
	
	@Override
	public void stop()
	{
		if(isValid)
	    {
	        synchronized(this)
	        {
	            isValid = false;
	        }
	    }
	}
	
	/**PLIReleaseView methods*/
	
	@Override
	public void releaseView()
	{
		synchronized(this)
		{
			view = null;
			scene = null;
			listener = null;
		}
	}
	
	/**dealloc methods*/

	@Override
	protected void finalize() throws Throwable
	{
		try
		{
			this.stop();
			defaultFramebuffer = colorRenderbuffer = null;
		    backingWidth = backingHeight = null;
		    matrixGrabber = null;
		    drawLineBuffer = null;
		    collisionData = null;
		    size = null;
		    listener = null;
		    if(contextSupportsFrameBufferObject)
		    	this.destroyFramebuffer((GL11ExtensionPack)EAGLContext.contextGL());
		    view = null;
		    scene = null;
		    legacyGL = null;
		}
		catch(Exception e)
		{
		}
		super.finalize();
	}
	
	
	
	/**GLSurfaceView.Renderer android methods*/
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		isGLContextCreated = false;
		try
		{
			EAGLContext.setContextGL(gl);
			isHigherThanOpenGL1 = (PLOpenGLSupport.getOpenGLVersion(gl).ordinal() > PLOpenGLVersion.PLOpenGLVersion1_0.ordinal());
			if(!isHigherThanOpenGL1)
			{
				legacyGL = new MatrixTrackingGL(gl);
				EAGLContext.setLegacyGL(legacyGL);
			}
			//contextSupportsFrameBufferObject = PLOpenGLSupport.checkIfContextSupportsFrameBufferObject(gl);
			this.initializeValues();
			this.start();
			if(listener != null)
				listener.rendererCreated(this);
		}
		catch(Exception e)
		{
			PLLog.error("PLRenderer::onSurfaceCreated", "Error: %s", e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		size.setValues(width, height);
		this.resizeFromLayer(contextSupportsFrameBufferObject ? (GL11ExtensionPack)gl : null);
		if(!isGLContextCreated)
		{
			if(listener != null)
				listener.rendererFirstChanged(gl, this, width, height);
			isGLContextCreated = true;
		}
		if(listener != null)
			listener.rendererChanged(this, width, height);
	}
	
	
	@Override
	public void onDrawFrame(GL10 gl)
	{
		if(isGLContextCreated && view != null)
		{
			try
			{
				this.render(isHigherThanOpenGL1 ? gl : legacyGL);
			} 
			catch(Exception e)
			{
			}
		}
	}
	
	/**internal classes declaration*/
	
	private static class PLCollisionData
	{
		/**member variables*/
		
		public PLVector3 ray[];
		public PLVector3 hitPoint[];
		public PLVector3 points[];
		
		/**init methods*/
		
		public PLCollisionData()
		{
			super();
			ray = new PLVector3[]{ new PLVector3(), new PLVector3() };
			hitPoint = new PLVector3[]{ new PLVector3() };
			points = new PLVector3[]{ new PLVector3(), new PLVector3(), new PLVector3(), new PLVector3() };
		}
		
		public static PLCollisionData collisionData()
		{
			return new PLCollisionData();
		}
		
		/**dealloc methods*/
		
		@Override
		protected void finalize() throws Throwable
		{
			ray = null;
			hitPoint = null;
			points = null;
			super.finalize();
		}
	}
	
	private abstract class Runnable implements java.lang.Runnable
	{
		/**member variables*/
		
		private PLIView view;
		private PLHotspot hotspot;
		private CGPoint screenPoint;
		private PLPosition scene3DPoint;
		
		/**init methods*/
		
		public Runnable(PLIView view, PLHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
		{
			super();
			this.view = view;
			this.hotspot = hotspot;
			this.screenPoint = screenPoint;
			this.scene3DPoint = scene3DPoint;
		}
		
		/**property methods*/
		
		protected PLIView getView()
		{
			return view;
		}
		
		protected PLHotspot getHotspot()
		{
			return hotspot;
		}
		
		protected CGPoint getScreenPoint()
		{
			return screenPoint;
		}
		
		protected PLPosition getScene3DPoint()
		{
			return scene3DPoint;
		}
	}
}