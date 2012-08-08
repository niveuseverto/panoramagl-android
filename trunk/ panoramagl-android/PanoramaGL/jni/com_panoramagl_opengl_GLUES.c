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

#include "com_panoramagl_opengl_GLUES.h"

#include <android/log.h>

#include "glues_quad.c"
#include "glues_error.c"
#include "glues_mipmap.c"
#include "glues_project.c"

#define  LOG_TAG    "libglues"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

/********************** 
 * quadric methods    *
 **********************/

GLUquadric * glues_copy_quadric(JNIEnv * env, jobject qobj)
{
	GLUquadric * result = gluNewQuadric();
	
	jclass qclass = (*env)->GetObjectClass(env, qobj);
	
	//normals
	jfieldID fieldID = (*env)->GetFieldID(env, qclass, "normals", "I");
	
	if(fieldID == NULL)
		return NULL;
	
	result->normals = (GLint)(*env)->GetIntField(env, qobj, fieldID);
	
	//textureCoords
	fieldID = (*env)->GetFieldID(env, qclass, "textureCoords", "I");
	
	if(fieldID == NULL)
		return NULL;
	
	result->textureCoords = (GLboolean)(*env)->GetIntField(env, qobj, fieldID);
	
	//orientation
	fieldID = (*env)->GetFieldID(env, qclass, "orientation", "I");
	
	if(fieldID == NULL)
		return NULL;
	
	result->orientation = (GLint)(*env)->GetIntField(env, qobj, fieldID);
	
	//drawStyle
	fieldID = (*env)->GetFieldID(env, qclass, "drawStyle", "I");
	
	if(fieldID == NULL)
		return NULL;
	
	result->drawStyle = (GLint)(*env)->GetIntField(env, qobj, fieldID);
	
	return result;
}


typedef void (APIENTRY* _GLUESfuncptr)(GLint);

GLUquadric * glues_new_quadric(GLint normals, GLint textureCoords, GLint orientation, GLint drawStyle, jboolean hasCallback, _GLUESfuncptr fn)
{
	GLUquadric * result = (GLUquadric*)malloc(sizeof(GLUquadric));
	if (result == NULL)
		return NULL;
	
	result->normals = normals;
	result->textureCoords = textureCoords;
	result->orientation = orientation;
	result->drawStyle = drawStyle;
	result->errorCallback = (hasCallback ? fn : NULL);
	
	return result;
}

#define GLUES_ERROR_CODE -1
GLint glues_errorCode = GLUES_ERROR_CODE;

void APIENTRY glues_error_callback(GLint errorCode)
{
	glues_errorCode = errorCode;
}

void glues_call_error_callback(JNIEnv * env, jclass sclass, jobject qobj)
{
	if(glues_errorCode != GLUES_ERROR_CODE)
	{
		jmethodID methodID = (*env)->GetStaticMethodID(env, sclass, "gluQuadricError", "(Lcom/panoramagl/opengl/GLUquadric;I)V");
		if(methodID == NULL)
			return;
		(*env)->CallStaticVoidMethod(env, sclass, methodID, qobj, glues_errorCode);
	}
}

JNIEXPORT void JNICALL Java_com_panoramagl_opengl_GLUES_gluCylinderAndroid
(JNIEnv * env, jclass sclass, jobject gl, jobject qobj, jfloat baseRadius, jfloat topRadius, jfloat height, jint slices, jint stacks, jint qnormals, jint qtextureCoords, jint qorientation, jint qdrawStyle, jboolean qhasCallback)
{
	GLUquadric * quad = glues_new_quadric(qnormals, qtextureCoords, qorientation, qdrawStyle, qhasCallback, glues_error_callback);
	gluCylinder(quad, (GLfloat)baseRadius, (GLfloat)topRadius, (GLfloat)height, (GLint)slices, (GLint)stacks);
	gluDeleteQuadric(quad);
	glues_call_error_callback(env, sclass, qobj);
}

JNIEXPORT void JNICALL Java_com_panoramagl_opengl_GLUES_gluDiskAndroid
(JNIEnv * env, jclass sclass, jobject gl, jobject qobj, jfloat innerRadius, jfloat outerRadius, jint slices, jint loops, jint qnormals, jint qtextureCoords, jint qorientation, jint qdrawStyle, jboolean qhasCallback)
{
	GLUquadric * quad = glues_new_quadric(qnormals, qtextureCoords, qorientation, qdrawStyle, qhasCallback, glues_error_callback);
	gluDisk(quad, innerRadius, outerRadius, slices, loops);
	gluDeleteQuadric(quad);
	glues_call_error_callback(env, sclass, qobj);
}

JNIEXPORT void JNICALL Java_com_panoramagl_opengl_GLUES_gluPartialDiskAndroid
(JNIEnv * env, jclass sclass, jobject gl, jobject qobj, jfloat innerRadius, jfloat outerRadius, jint slices, jint loops, jfloat startAngle, jfloat sweepAngle, jint qnormals, jint qtextureCoords, jint qorientation, jint qdrawStyle, jboolean qhasCallback)
{
	GLUquadric * quad = glues_new_quadric(qnormals, qtextureCoords, qorientation, qdrawStyle, qhasCallback, glues_error_callback);
	gluPartialDisk(quad, innerRadius, outerRadius, slices, loops, startAngle, sweepAngle);
	gluDeleteQuadric(quad);
	glues_call_error_callback(env, sclass, qobj);
}

JNIEXPORT void JNICALL Java_com_panoramagl_opengl_GLUES_gluSphereAndroid
(JNIEnv * env, jclass sclass, jobject gl, jobject qobj, jfloat radius, jint slices, jint stacks, jint qnormals, jint qtextureCoords, jint qorientation, jint qdrawStyle, jboolean qhasCallback)
{
	GLUquadric * quad = glues_new_quadric(qnormals, qtextureCoords, qorientation, qdrawStyle, qhasCallback, glues_error_callback);
	gluSphere(quad, radius, slices, stacks);
	gluDeleteQuadric(quad);
	glues_call_error_callback(env, sclass, qobj);
}

JNIEXPORT void JNICALL Java_com_panoramagl_opengl_GLUES_gluHemisphereAndroid
  (JNIEnv * env, jclass sclass, jobject gl, jobject qobj, jboolean positive, jfloat radius, jint slices, jint stacks, jint qnormals, jint qtextureCoords, jint qorientation, jint qdrawStyle, jboolean qhasCallback)
{
	GLUquadric * quad = glues_new_quadric(qnormals, qtextureCoords, qorientation, qdrawStyle, qhasCallback, glues_error_callback);
	gluHemisphere(quad, positive, radius, slices, stacks);
	gluDeleteQuadric(quad);
	glues_call_error_callback(env, sclass, qobj);
}

JNIEXPORT void JNICALL Java_com_panoramagl_opengl_GLUES_glu3DArcAndroid
(JNIEnv * env, jclass sclass, jobject gl, jobject qobj, jfloat angleWidth, jfloat offsetAngle, jboolean positive, jfloat radius, jint slices, jint stacks, jint qnormals, jint qtextureCoords, jint qorientation, jint qdrawStyle, jboolean qhasCallback)
{
	GLUquadric * quad = glues_new_quadric(qnormals, qtextureCoords, qorientation, qdrawStyle, qhasCallback, glues_error_callback);
	glu3DArc(quad, angleWidth, offsetAngle, positive, radius, slices, stacks);
	gluDeleteQuadric(quad);
	glues_call_error_callback(env, sclass, qobj);
}

/********************** 
 * project methods    *
 **********************/

JNIEXPORT void JNICALL Java_com_panoramagl_opengl_GLUES_gluPerspective
(JNIEnv * env, jclass sclass, jobject gl, jfloat fovy, jfloat aspect, jfloat zNear, jfloat zFar)
{
	gluPerspective(fovy, aspect, zNear, zFar);
}
