/**-------------------------------------------------------------------------------------------------
- Author :  Yunus KIZILTEPE
- Date   :  20.11.2020 - 23:10
- Version:

- File   : util_emv.c
- Description: File includes functions that EMV
- Warnings \
--------------------------------------------------------------------------------------------------*/

/**------------------------------------- TAB INCLUDE ---------------------------------------------*/
#include <jni.h>
#include <string.h>

#include "util_types.h"
#include "util_jni.h"
/**------------------------------------- TAB DEFINE ----------------------------------------------*/
/**------------------------------------- TAB TYPES -----------------------------------------------*/
/**------------------------------------- TAB FUNCTION ABSTRACTION --------------------------------*/
/**------------------------------------- TAB PUBLIC VARIABLES ------------------------------------*/
/**------------------------------------- TAB PRIVATE VARIABLES -----------------------------------*/
/**------------------------------------- TAB PRIVATE FUNCTONS ------------------------------------*/
static int getSW(JNIEnv *env, jbyteArray emvData, byte *SW)
{
    int retVal = RET_OK;
    int dataLen;
    byte emvRawData[MAX_EMV_LEN] = {0};

    dataLen = utilJniJByteArrayToChar(env, emvData, emvRawData, MAX_EMV_LEN);

    if(SW_LEN > dataLen)
    {
        retVal = RET_FAIL;
    } else
    {
        memcpy(SW, &emvRawData[dataLen - 2], SW_LEN);
        //if SW is 0x9000 and len is 2: incorrect message. Data should be exist in this case
        if((SW_LEN == dataLen) && (SW1_OK == SW[0]) && (SW2_OK == SW[1]))
        {
            retVal = RET_FAIL;
        }
    }

    return retVal;
}
/**------------------------------------- TAB GLOBAL FUNCTIONS ------------------------------------*/
/**-------------------------------------------------------------------------------------------------
                            Java_com_yunus_nfcpay_EMVNative_checkEMVResponse

- Brief  : Function to check card EMV data response
- Detail : Function will check EMV data response
           Last 2 bytes is response data
- Parameters \
 -- @env     : java environment
 -- @clazz   : instance of java function
 -- @emvData : card EMV data
- Returns  \
 --@retVal  : if true response is ok, if false response fail
--------------------------------------------------------------------------------------------------*/
JNIEXPORT jboolean JNICALL
Java_com_yunus_nfcpay_EMVNative_checkEMVResponse(JNIEnv *env, jclass clazz, jbyteArray emvData) {
    jboolean retVal = RET_OK;
    byte SW[SW_LEN + 1] = {0};

    retVal = getSW(env, emvData, (byte *)&SW);

    if((SW1_OK == SW[0]) && (SW2_OK == SW[1]) && (RET_OK == retVal)) {

        return JNI_TRUE;
    }

    return JNI_FALSE;
}

/**-------------------------------------------------------------------------------------------------
                            Java_com_yunus_nfcpay_EMVNative_getSW

- Brief  : Function to get card EMV data response
- Detail : Function will parse EMV data response and return the response
           Last 2 bytes is response data
- Parameters \
 -- @env    : java environment
 -- @clazz  : instance of java function
 -- @data   : card EMV data
- Returns  \
 --@jSW     : EMV response data
--------------------------------------------------------------------------------------------------*/
JNIEXPORT jbyteArray JNICALL
Java_com_yunus_nfcpay_EMVNative_getSW(JNIEnv *env, jclass clazz, jbyteArray data) {
    byte SW[SW_LEN + 1] = {0};
    jbyteArray  jSW = (*env)->NewByteArray(env, SW_LEN);

     getSW(env, data, SW);
    (*env)->SetByteArrayRegion(env, jSW, 0, SW_LEN, (jbyte *)SW);

    return jSW;
}
