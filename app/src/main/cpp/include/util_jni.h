/*-----------------------------------------------------------------------------
- Author :  Yunus KIZILTEPE
- Date   :  21.11.2020
- Version:

- File: util_jni.h

- Description: Defines, types and function prototypes related with the 
            jni util operations are declared in this file

- Warnings \

-----------------------------------------------------------------------------*/
#ifndef _UTIL_JNI_H_
#define _UTIL_JNI_H_

/*------------------------------ TAB INCLUDE --------------------------------*/

/*------------------------------ TAB DEFINE ---------------------------------*/

/*------------------------------- TAB TYPES ---------------------------------*/

/*-------------------------- TAB GLOBAL FUNCTIONS ---------------------------*/
// Function to convert jbyteArray to C byte array
int utilJniJByteArrayToChar(JNIEnv *env, jbyteArray input, unsigned char *output, int maxLen);

#endif //end of #ifndef _UTIL_JNI_H_
