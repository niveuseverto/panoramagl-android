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

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.panoramagl.ios.structs.CGRect;
import com.panoramagl.ios.structs.CGSize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class PLImage 
{	
	/**member variables*/
	
	private Bitmap bitmap;
	private int width, height;
	private boolean isRecycled, isLoaded;
	
	/**init methods*/
	
	public PLImage(Bitmap bitmap)
	{
		this(bitmap, true);
	}
	
	public PLImage(Bitmap bitmap, boolean copy)
	{
		super();
		this.createWithBitmap(bitmap, copy);
	}
	
	public PLImage(CGSize size)
	{
		this(size.width, size.height);
	}
	
	public PLImage(int width, int height)
	{
		super();
		this.createWithSize(width, height);	
	}
	
	public PLImage(String path)
	{
		super();
		this.createWithPath(path);
	}
	
	public PLImage(byte[] buffer)
	{
		super();
		this.createWithBuffer(buffer);
	}
	
	public static PLImage imageWithBitmap(Bitmap bitmap)
	{
		return new PLImage(bitmap);
	}
	
	public static PLImage imageWithBitmap(Bitmap bitmap, boolean copy)
	{
		return new PLImage(bitmap, copy);
	}
	
	public static PLImage imageWithSize(CGSize size)
	{
		return new PLImage(size);
	}	

	public static PLImage imageWithDimensions(int width ,int height)
	{
		return new PLImage(width, height);
	}
	
	public static PLImage imageWithPath(String path)
	{
		return new PLImage(path);
	}
	
	public static PLImage imageWithBuffer(byte[] buffer)
	{
		return new PLImage(buffer);
	}
	
	/**create methods*/
	
	protected void createWithPath(String path)
	{
		bitmap = BitmapFactory.decodeFile(path);
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		isRecycled = false;
		isLoaded = true;
	}
	
	protected void createWithBitmap(Bitmap bitmap, boolean copy)
	{
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		this.bitmap = (copy ? Bitmap.createBitmap(bitmap) : bitmap);
		isRecycled = false;
		isLoaded = true;
	}
	
	protected void createWithSize(int width, int height)
	{
		this.deleteImage();
		this.createWithBitmap(Bitmap.createBitmap(null, 0, 0, width, height), false);
	}
	
	protected void createWithBuffer(byte[] buffer)
	{
		bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		isRecycled = false;
		isLoaded = true;
	}
	
	/**property methods*/
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public CGSize getSize()
	{
		return CGSize.CGSizeMake(width, height);
	}
	
	public CGRect getRect()
	{
		return CGRect.CGRectMake(0, 0, width, height);
	}
	
	public int getCount()
	{
		return (width * height * 4);
	}
	
	public Bitmap getBitmap() 
	{
		return bitmap;
	}
	
	public ByteBuffer getBits() 
	{
		if(this.isValid())
			return null;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer);
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer.toByteArray());
		byteBuffer.order(ByteOrder.BIG_ENDIAN);
		return byteBuffer;
	}
	
	public boolean isValid()
	{
		return (bitmap != null && !bitmap.isRecycled());
	}
	
	public boolean isRecycled()
	{
		return isRecycled;
	}
	
	public boolean isLoaded()
	{
		return isLoaded;
	}
	
	/**operation methods*/

	public boolean equals(PLImage image)
	{	
		if(image.getBitmap() == bitmap)
			return true;
		if(image.getBitmap() == null || bitmap == null || image.getHeight() != height || image.getWidth() != width)
			return false;
		ByteBuffer bits = image.getBits();
		ByteBuffer _bits = this.getBits();
		for(int i = 0; i < this.getCount(); i++)
		{
			if(bits.get() != _bits.get())
				return false;
		}
		return true;
	}
	
	public PLImage assign(PLImage image)
	{
		this.deleteImage();
		this.createWithBitmap(image.getBitmap(), true);
		return this;
	}
	
	/**crop methods*/
	
	public PLImage crop(CGRect rect)
	{
		return this.crop(rect.x, rect.y, rect.width, rect.height);
	}
	
	public PLImage crop(int x, int y, int width, int height)
	{
		Bitmap croppedBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
		Canvas canvas = new Canvas(croppedBitmap);
		canvas.drawBitmap(bitmap, new Rect(x, y, x + width, y + height), new Rect(0, 0, width, height), null);
		this.deleteImage();
		this.createWithBitmap(croppedBitmap, false);
		return this;
	}
	
	public static PLImage crop(PLImage image, int x, int y, int width, int height)
	{
		Bitmap source = image.getBitmap();
		Bitmap dest = Bitmap.createBitmap(width, height, source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, new Rect(x, y, x + width, y + height), new Rect(0, 0, width, height), null);
		return new PLImage(dest, false);
	}
	
	/**scale methods*/
	
	public PLImage scale(CGSize size)
	{
		return this.scale(size.width, size.height);
	}
	
	public PLImage scale(int width, int height)
	{
		if((width < 0 || height < 0) || (width == 0 && height == 0) || (width == this.width && height == this.height))
			return this;
		Bitmap image = Bitmap.createScaledBitmap(bitmap, width, height, true);
	    this.deleteImage();
	    this.createWithBitmap(image, false);
		return this;
	}
	
	/**rotate methods*/
	
	public PLImage rotate(int angle)
	{
		if((angle % 90) != 0)
			return this;
		Matrix matrix = new Matrix();
		matrix.preRotate(angle);
    	Bitmap image = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    	this.deleteImage();
    	this.createWithBitmap(image, false);
    	return this;
	}
	
	public PLImage rotate(float degrees, float px, float py)
	{
		Matrix matrix = new Matrix();
		matrix.preRotate(degrees, px, py);
    	Bitmap image = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    	this.deleteImage();
    	this.createWithBitmap(image, false);
    	return this;
	}
	
	/**mirror methods*/
	
	public PLImage mirrorHorizontally()
	{
		return this.mirror(true, false);
	}
	
	public PLImage mirrorVertically()
	{
		return this.mirror(false,true);
	}
	
	public PLImage mirror(boolean horizontally, boolean vertically)
	{
		//-1,1 Horizontal, 1, -1 Vertical, Both = -1,-1
		Matrix matrix = new Matrix();
		matrix.preScale(horizontally ? -1.0f : 1.0f, vertically ? -1.0f : 1.0f);
		Bitmap image = Bitmap.createBitmap(bitmap , 0, 0, width, height, matrix, false);
		this.deleteImage();
		this.createWithBitmap(image, false);
		return this;
	}
	
	/**sub-image methods*/
	
	public Bitmap getSubImage(CGRect rect)
	{
		return this.getSubImage(rect.x, rect.y, rect.width, rect.height);
	}
	
	public Bitmap getSubImage(int x, int y, int width, int height)
	{
		int pixels[] = new int[width * height];
		bitmap.getPixels(pixels, 0, width, x, y, width, height);
		return Bitmap.createBitmap(pixels, 0, width, width, height, bitmap.getConfig());
	}
	
	public static PLImage joinImagesHorizontally(PLImage leftImage, PLImage rightImage)
	{
	    if(leftImage != null && leftImage.isValid() && rightImage != null && rightImage.isValid())
	    {
	    	Bitmap bitmap = Bitmap.createBitmap(leftImage.getWidth() + rightImage.getWidth(), (leftImage.getHeight() > rightImage.getHeight() ? leftImage.getHeight() : rightImage.getHeight()), Bitmap.Config.ARGB_8888);
	    	Canvas canvas = new Canvas(bitmap);
	    	canvas.drawBitmap(leftImage.getBitmap(), 0.0f, 0.0f, null);
	    	canvas.drawBitmap(rightImage.getBitmap(), leftImage.getWidth(), 0.0f, null);
	    	canvas.save();
	    	return new PLImage(bitmap, false);
	    }
	    return null;
	}
	
	/**recycle methods*/
	
	public void recycle()
	{
		if(!isRecycled)
			this.deleteImage();
	}
	
	/**clone methods*/
	
	public Bitmap cloneBitmap()
	{
		return Bitmap.createBitmap(bitmap);
	}
	
	@Override
	public PLImage clone()
	{
		return new PLImage(bitmap, true);
	}
	
	/**dealloc methods*/

	protected void deleteImage()
	{
		if(bitmap != null)
		{
			if(!bitmap.isRecycled())
				bitmap.recycle();
			bitmap = null;
			isRecycled = true;
			isLoaded = false;
		}
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		this.deleteImage();
		super.finalize();
	}
}