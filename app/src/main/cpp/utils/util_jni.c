/**-------------------------------------------------------------------------------------------------
- Author :  Yunus KIZILTEPE
- Date   :  20.11.2020 - 23:29
- Version:

- File   : jni_utils.c

- Description: File includes jni utilities functions

- Warnings \
--------------------------------------------------------------------------------------------------*/

/**------------------------------------ TAB INCLUDE ----------------------------------------------*/
#include <jni.h>
#include <string.h>

#include "util_jni.h"
/**------------------------------------ TAB DEFINE -----------------------------------------------*/
/**------------------------------------ TAB TYPES ------------------------------------------------*/
/**------------------------------------ TAB FUNCTION ABSTRACTION ---------------------------------*/
/**------------------------------------ TAB PRIVATE FUNCTONS -------------------------------------*/
/**------------------------------------ TAB PUBLIC FUNCTONS --------------------------------------*/
/**-------------------------------------------------------------------------------------------------
                                     utilJniJByteArrayToChar
- Brief  : Function to convert java byte array to C byte array
- Detail : Function to convert java byte array to C byte array and return array len
- Parameters \
 -- @env     : java environment
 -- @input  : java byte array
 -- @output : C byte array
 -- @maxLen : convertion len
- Returns  \
  --@len    : data len
--------------------------------------------------------------------------------------------------*/
int utilJniJByteArrayToChar(JNIEnv *env, jbyteArray input, unsigned char *output, int maxLen)
{
    jbyte *nativeBytes;
    int len = 0;

    len = (*env)->GetArrayLength(env, input);
    nativeBytes = (*env)->GetByteArrayElements(env, input, 0);
    memcpy(output, nativeBytes, (size_t) (len > maxLen ? maxLen : len));
    (*env)->ReleaseByteArrayElements(env, input, nativeBytes, JNI_ABORT);

    return len;
}
