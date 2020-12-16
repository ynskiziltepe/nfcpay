/**-------------------------------------------------------------------------------------------------
- Author :  Yunus KIZILTEPE
- Date   :  30.11.2020 - 20:12
- Version:

- File   : util_afl.c
- Description: File includes utilities of EMV application file locator
- Warnings \
--------------------------------------------------------------------------------------------------*/
/**------------------------------------ TAB INCLUDE ----------------------------------------------*/
#include <jni.h>
#include <string.h>
#include <malloc.h>

#include "util_types.h"
#include "util_emv_consts.h"
#include "util_jni.h"
#include "util_tlv.h"
/**------------------------------------ TAB DEFINE -----------------------------------------------*/
/**------------------------------------ TAB TYPES ------------------------------------------------*/
/**------------------------------------ TAB FUNCTION ABSTRACTION ---------------------------------*/
/**------------------------------------ TAB PUBLIC VARIABLES -------------------------------------*/
/**------------------------------------ TAB PRIVATE VARIABLES ------------------------------------*/
/**------------------------------------ TAB GLOBAL FUNCTIONS -------------------------------------*/

/**-------------------------------------------------------------------------------------------------
                            Java_com_yunus_nfcpay_EMVNative_getAFLDataRecords
- Brief  : Function to parse EMV application file locator
- Detail : Function to parse EMV application file locator and return AFL list
- Parameters \
 -- @env      : java environment
 -- @clazz    : instance of java function
 -- @jAFLData : EMV data to be parse for AFL array
- Returns  \
  --@jobject (ArrayList<AFL>) : AFL list
--------------------------------------------------------------------------------------------------*/
JNIEXPORT jobject JNICALL
Java_com_yunus_nfcpay_EMVNative_getAFLDataRecords(JNIEnv *env, jclass clazz, jbyteArray jAFLData) {
    byte *aflData;
    uint aflLen = (*env)->GetArrayLength(env, jAFLData);
    jclass clsList = (*env)->FindClass(env, "java/util/ArrayList");
    jmethodID mArray = (*env)->GetMethodID(env, clsList, "<init>", "()V");
    jobject aflList = (*env)->NewObject(env, clsList, mArray);
    mArray = (*env)->GetMethodID(env, clsList, "add", "(ILjava/lang/Object;)V");

    jclass clsAFL = (*env)->FindClass(env, "com/yunus/nfcpay/model/AFL");
    jmethodID mAFLInit = (*env)->GetMethodID(env, clsAFL, "<init>", "(II[B)V");


    aflData = malloc(aflLen);
    utilJniJByteArrayToChar(env, jAFLData, aflData, aflLen);

    if (aflLen < 4) {
        return NULL;
    } else {
        int sFi;
        int index = 0;
        int arrayIndex = 0;
        byte readRecord[512] = {0};
        byte *currPtr = readRecord;
        jobject afl;

        for (int i = 0; i < aflLen / 4; i++) {
            int firstRecordNum = aflData[4 * i + 1];
            int lastRecordNum = aflData[4 * i + 2];

            while (firstRecordNum <= lastRecordNum) {
                sFi = aflData[4 * i] >> 3u;

                currPtr[index++] = (byte)(READ_RECORD_TAG / 0x100);
                currPtr[index++] = (byte)(READ_RECORD_TAG % 0x100);
                currPtr[index++] = firstRecordNum;
                currPtr[index++] = ((uint)sFi << 0x03u) | 0x04u;
                currPtr[index++] = 0x00;

                jbyteArray jRecord = (*env)->NewByteArray(env, index);
                (*env)->SetByteArrayRegion(env, jRecord, 0, index, (const jbyte *)readRecord);
                afl = (*env)->NewObject(env, clsAFL, mAFLInit, sFi, firstRecordNum, jRecord);
                (*env)->CallVoidMethod(env, aflList, mArray, arrayIndex, afl);
                arrayIndex++;
                firstRecordNum++;

                (*env)->DeleteLocalRef(env, jRecord);
                (*env)->DeleteLocalRef(env, afl);
                index = 0;
            }
        }
    }

    free(aflData);

    return aflList;
}
