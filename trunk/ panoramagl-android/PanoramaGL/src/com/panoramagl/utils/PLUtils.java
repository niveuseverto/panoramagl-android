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

package com.panoramagl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.panoramagl.structs.PLPosition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class PLUtils
{
	/**swap methods*/
	
	public static float[] swapFloatValues(float firstValue, float secondValue)
	{
		float swapValue = firstValue;
		firstValue = secondValue;
		secondValue = swapValue;
		return new float[]{firstValue, secondValue};
	}
	
	public static int[] swapIntValues(int firstValue,int secondValue)
	{
		firstValue = firstValue ^ secondValue;
		secondValue = secondValue ^ firstValue;
		firstValue = firstValue ^ secondValue;
		return new int[]{firstValue, secondValue};
	}
	
	/**conversion methods*/
	
	public static PLPosition convertSphericalCoordsToCartesianCoords(float ratio, float pitch, float yaw, float picthOffset, float yawOffset)
	{
		double pr = (pitch + picthOffset) * Math.PI / 180.0;
		double yr = (yaw + yawOffset) * Math.PI / 180.0;
		float x = (float)(ratio * Math.sin(pr) * Math.cos(yr));
		float y = (float)(ratio * Math.sin(pr) * Math.sin(yr));
		float z = (float)(ratio * Math.cos(pr));
		return PLPosition.PLPositionMake(x, y, z);
	}
	
	public static PLPosition convertSphericalCoordsToCartesianCoords(float ratio, float pitch, float yaw)
	{
		return convertSphericalCoordsToCartesianCoords(ratio, pitch, yaw, 90.0f, 180.0f);
	}
	
	/**buffer methods*/
	
	public static IntBuffer makeIntBuffer(int[] array)
	{
		final int integerSize = Integer.SIZE / 8;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * integerSize);
		byteBuffer.order(ByteOrder.nativeOrder());
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		intBuffer.put(array);
		intBuffer.position(0);
		return intBuffer;
	}
	
	public static ByteBuffer makeByteBuffer(byte[] array)
	{
		final int SIZE = Byte.SIZE / 8;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * SIZE);
		byteBuffer.order(ByteOrder.nativeOrder());
		byteBuffer.put(array);
		byteBuffer.position(0);
		return byteBuffer;
	}
	
	public static FloatBuffer makeFloatBuffer(int length)
	{
		final int floatSize = Float.SIZE / 8;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(length * floatSize);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.position(0);
		return floatBuffer;
	}
	
	public static FloatBuffer makeFloatBuffer(float[] array)
	{
		final int floatSize = Float.SIZE / 8;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * floatSize);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(array);
		floatBuffer.position(0);
		return floatBuffer;
	}
	
	public static FloatBuffer makeFloatBuffer(float[][] array2d, int rows, int cols)
	{
		float[] result = new float[rows * cols];
		int k = 0;
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++)
				result[k++] = array2d[i][j];
		return makeFloatBuffer(result);
	}
	
	/**resource methods*/
	
	public static Bitmap getBitmap(Context context, String url)
	{
		try
		{
			url = url.trim();
			InputStream is = null;
			if(url.indexOf("res://") == 0)
			{
				int sepPos = url.lastIndexOf("/");
				int resId = context.getResources().getIdentifier(url.substring(sepPos + 1), url.substring(6, sepPos), context.getPackageName());
				is = context.getResources().openRawResource(resId);
			}
			else if(url.indexOf("file://") == 0)
			{
				File file = new File(url.substring(7));
				if(file.canRead())
					is = new FileInputStream(file);
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither = true;
			Bitmap bitmap = BitmapFactory.decodeStream(is, new Rect(), options);
			is.close();
			return bitmap;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static Bitmap getBitmap(Context context, int resId)
	{
		try
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither = true;
			InputStream is = context.getResources().openRawResource(resId);
            Bitmap bitmap = BitmapFactory.decodeStream(is, new Rect(), options);
            is.close();
			return bitmap;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}