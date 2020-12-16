/**-------------------------------------------------------------------------------------------------
- Author :  Yunus KIZILTEPE
- Date   :  21.11.2020 - 20:41
- Version:

- File   : util_tlv.c
- Description: File includes utilities of tlv
- Warnings \
--------------------------------------------------------------------------------------------------*/
/**------------------------------------ TAB INCLUDE ----------------------------------------------*/
#include <jni.h>
#include <string.h>

#include <stdlib.h>

#include "util_types.h"
#include "util_jni.h"
#include "util_tlv.h"
/**------------------------------------ TAB DEFINE -----------------------------------------------*/
/**------------------------------------ TAB TYPES ------------------------------------------------*/
/**------------------------------------ TAB FUNCTION ABSTRACTION ---------------------------------*/
/**------------------------------------ TAB PUBLIC VARIABLES -------------------------------------*/
/**------------------------------------ TAB PRIVATE VARIABLES ------------------------------------*/
static JNIEnv *gJniEnv = NULL;
/**------------------------------------ TAB PRIVATE FUNCTONS -------------------------------------*/
/**-------------------------------------------------------------------------------------------------
                                     getBit
- Brief  : Function to get bit value from byte value
- Detail : Function will get bit value rom byte value
           There is 8 bit in a byte so max bit value should be 8
- Parameters \
 -- @value    : bit value will find in this value
 -- @bit      : related bit
- Returns  \
  -- 0 or 1   : 1 0r 0 will return
--------------------------------------------------------------------------------------------------*/
static int getBit(byte value, int bit)
{
    byte bitValue[8] = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};

    if((bit >= 1) && (bit <= 8))
    {
        if(value & bitValue[bit - 1])
            return 1;
        else
            return 0;
    }
    else
    {
        return 0;
    }
}
/**-------------------------------------------------------------------------------------------------
                                     tlvFree
- Brief  : Function to free memory in array
- Detail : Function will free memory in tlv array
- Parameters \
 -- @tlvArray    : array of to be free
- Returns  \
--------------------------------------------------------------------------------------------------*/
static void tlvFree(jobject tlvArray)
{
    int subCount;
    jclass clsTlv;
    jmethodID methodId;
    jobject subData = NULL;

    if(tlvArray == NULL) {
        return;
    }

    clsTlv = (*gJniEnv)->FindClass(gJniEnv, "com/yunus/nfcpay/model/TLVData");
    methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getSubCount", "()I");
    subCount = (*gJniEnv)->CallIntMethod(gJniEnv, tlvArray, methodId);

    for(int i =0; i < subCount; i++) {

        methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getSubData", "(I)Lcom/yunus/nfcpay/model/TLVData;");
        subData = (*gJniEnv)->CallObjectMethod(gJniEnv, tlvArray, methodId, i);
        tlvFree(subData);
        (*gJniEnv)->DeleteLocalRef(gJniEnv, subData);
    }
}
/**-------------------------------------------------------------------------------------------------
                                     tlvParseOne
- Brief  : Function to parse first tlv item
- Detail : Function will parse first Tag Len Value and return it
- Parameters \
 -- @buf  : tlv array to be parse
 -- @len : size of buf
- Returns  \
 -- tlvData: first parsed tlv item
--------------------------------------------------------------------------------------------------*/
static jobject tlvParseOne(const byte *buf, int len)
{
    uint i;
    uint tlvTag;
    uint tlvLen;
    uint tlvLenSize;
    int tlvExtraFlag = 0;
    uint tag1;
    uint tag2 = 0;
    int index = 0;
    int tagSize = 1;
    byte *value;

    tag1 = buf[index++];
    if((tag1 & 0x1fu) == 0x1f)
    {
        tagSize++;
        tag2 = buf[index++];
    }
    if(tagSize == 1)
        tlvTag = tag1;
    else
        tlvTag = (tag1 << 8u) + tag2;

    tlvLenSize = 1;
    tlvLen = buf[index++];
    if(getBit(tlvLen, 8))
    {
        tlvLenSize = tlvLen & 0x7fu;
        tlvLen = 0;
        for(i = 0; i < tlvLenSize; i++)
        {
            tlvLen += buf[index++] << (i * 8);
        }
        tlvLenSize++;
    }

    value = (byte *)malloc(tlvLen);
    memcpy(value,buf + index, tlvLen);

    index += tlvLen;

    if(index < len) {
        tlvExtraFlag = 1;
    }
    else if(index == len) {
        tlvExtraFlag = 0;
    }

    jbyteArray jValue = (*gJniEnv)->NewByteArray(gJniEnv, tlvLen);
    (*gJniEnv)->SetByteArrayRegion(gJniEnv, jValue, 0, tlvLen, (const jbyte *)value);
    jclass clsTlv = (*gJniEnv)->FindClass(gJniEnv, "com/yunus/nfcpay/model/TLVData");
    jmethodID tlvInit = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "<init>", "(II[BIIII)V");
    jobject tlvData = (*gJniEnv)->NewObject(gJniEnv, clsTlv, tlvInit, (int)tlvTag, (int)tlvLen,
                                            jValue, tagSize, (int)tlvLenSize,
                                            getBit(tag1, 6), tlvExtraFlag);

    (*gJniEnv)->DeleteLocalRef(gJniEnv, jValue);

    if(NULL != value)
    {
        free(value);
    }

    return tlvData;
}
/**-------------------------------------------------------------------------------------------------
                                     tlvParseSubNodes
- Brief  : Function to parse sub item
- Detail : Function will parse sub item and it is will add to parent
- Parameters \
 -- @parent  : parent tlv data. Sub item will to be find in this data
- Returns  \
 -- extraFlag: sub item's extra flag
--------------------------------------------------------------------------------------------------*/
static int tlvParseSubItem(jobject *parent)
{
    int subLen;
    int parentLen;
    jclass clsTlv;
    jmethodID methodId;

    clsTlv = (*gJniEnv)->FindClass(gJniEnv, "com/yunus/nfcpay/model/TLVData");
    methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getSubFlag", "()I");

    if(!((*gJniEnv)->CallIntMethod(gJniEnv, *parent, methodId))) // if sub flag not exist
        return 0;

    methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getSubLen", "()I");
    subLen = (*gJniEnv)->CallIntMethod(gJniEnv, *parent, methodId);

    methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getTlvLen", "()I");

    parentLen = ((*gJniEnv)->CallIntMethod(gJniEnv, *parent, methodId));
    if(subLen < parentLen)
    {
        jobject subData;
        int lenVal;
        int extraFlag;
        byte *value;

        methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getValue", "()[B");
        jbyteArray jValue = (*gJniEnv)->CallObjectMethod(gJniEnv, *parent, methodId);
        lenVal = (*gJniEnv)->GetArrayLength(gJniEnv, jValue);

        value = malloc(lenVal);

        utilJniJByteArrayToChar(gJniEnv, jValue, value, lenVal);

        subData = tlvParseOne(value + subLen, parentLen - subLen);

        methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "addSubData", "(Lcom/yunus/nfcpay/model/TLVData;)V");
        (*gJniEnv)->CallVoidMethod(gJniEnv, *parent, methodId, subData);

        methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getExtraFlag", "()I");
        extraFlag = (*gJniEnv)->CallIntMethod(gJniEnv, subData, methodId);

        if(NULL != value)
            free(value);

        return extraFlag;
    }
    else
    {
        return 0;
    }
}
/**-------------------------------------------------------------------------------------------------
                                     utlTlvParseSubItems
- Brief  : Function to parse all sub items
- Detail : Function will parse all sub items and  will add to parent
- Parameters \
 -- @parent  : parent tlv data. Sub items will to be find in this data
- Returns  \
--------------------------------------------------------------------------------------------------*/
static void utlTlvParseSubItems(jobject *parent)
{
    int i;
    int subFlag;
    jclass clsTlv;
    jmethodID methodId;

    clsTlv = (*gJniEnv)->FindClass(gJniEnv, "com/yunus/nfcpay/model/TLVData");
    methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getSubFlag", "()I");
    subFlag = (*gJniEnv)->CallIntMethod(gJniEnv, *parent, methodId);

    if(subFlag != 0)
    {
        int subCount;
        jobject subData;

        while(tlvParseSubItem(parent) != 0);

        methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getSubCount", "()I");
        subCount = (*gJniEnv)->CallIntMethod(gJniEnv, *parent, methodId);

        for(i = 0; i < subCount; i++)
        {
            methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getSubData", "(I)Lcom/yunus/nfcpay/model/TLVData;");
            subData = (*gJniEnv)->CallObjectMethod(gJniEnv, *parent, methodId, i);
            if(NULL != subData)
            {
                methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getSubFlag", "()I");
                subFlag = (*gJniEnv)->CallIntMethod(gJniEnv, subData, methodId);
                if(subFlag != 0)
                {
                    utlTlvParseSubItems(&subData);
                }
            }
        }
    }
}

/**------------------------------------ TAB GLOBAL FUNCTIONS -------------------------------------*/
/**-------------------------------------------------------------------------------------------------
                                     utilTlvParse
- Brief  : Function to parse all tlv data
- Detail : Function will parse all tlv data in raw buffer
           After parse Tlv construct will return
- Parameters \
 -- @buf    : raw tlv data to be parse
 -- @len    : raw data len
- Returns  \
 -- @tlvData : parsed tlv data
--------------------------------------------------------------------------------------------------*/
jobject utilTlvParse(byte *buf, int len)
{
    jobject tlvData = tlvParseOne(buf, len);
    utlTlvParseSubItems(&tlvData);

    return tlvData;
}
/**-------------------------------------------------------------------------------------------------
                                     utilTlvFind
- Brief  : Function to find related tag in tlv data
- Detail : Function will find related tag in tlv data
           null will return if related tag not found
- Parameters \
 -- @tlvData   : constructed tlv data. tag value will search in this data
 -- @tag       : related tag to be search
- Returns  \
 -- @foundData : Found tlv data. null will return if not find
--------------------------------------------------------------------------------------------------*/
jobject utilTlvFind(jobject tlvData, int tag)
{
    int i;
    int tlvTag;
    int subCount;
    jclass clsTlv;
    jobject subData;
    jmethodID methodId;
    jobject foundData = NULL;

    clsTlv = (*gJniEnv)->FindClass(gJniEnv, "com/yunus/nfcpay/model/TLVData");
    methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getTag", "()I");
    tlvTag = (*gJniEnv)->CallIntMethod(gJniEnv, tlvData, methodId);

    if(tlvTag == tag)
    {
        return tlvData;
    }

    methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getSubCount", "()I");
    subCount = (*gJniEnv)->CallIntMethod(gJniEnv, tlvData, methodId);

    for(i = 0; i < subCount; i++)
    {
        methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getSubData", "(I)Lcom/yunus/nfcpay/model/TLVData;");
        subData = (*gJniEnv)->CallObjectMethod(gJniEnv, tlvData, methodId, i);

        foundData = utilTlvFind(subData, tag);
        if(foundData != NULL)
            return foundData;
    }

    return foundData;
}
/**-------------------------------------------------------------------------------------------------
                       Java_com_yunus_nfcpay_EMVNative_getTlvValue
- Brief  : Function to find related tag in tlv data
- Detail : Function will find related tag in tlv data
           null will return if related tag not found
- Parameters \
 -- @env       : java environment
 -- @clazz     : instance of java function
 -- @dataBytes : raw tlv data array
 -- @emvTag    : related tag to be search
- Returns  \
 -- @jValue : Found tlv value. This value is under emvTag value
--------------------------------------------------------------------------------------------------*/
JNIEXPORT jbyteArray JNICALL
Java_com_yunus_nfcpay_EMVNative_getTlvValue(JNIEnv *env, jclass clazz,
                                            jbyteArray dataBytes, jint emvTag) {
    int dataLen;
    byte emvData[MAX_EMV_LEN] = {0};
    jbyteArray jValue = NULL;

    gJniEnv = env;

    dataLen = utilJniJByteArrayToChar(env, dataBytes, emvData, MAX_EMV_LEN);

    jobject allNodes = utilTlvParse(emvData, dataLen);
    jobject foundData = utilTlvFind(allNodes, emvTag);

    if(NULL != foundData)
    {
        jclass clsTlv;
        jmethodID methodId;

        clsTlv = (*gJniEnv)->FindClass(gJniEnv, "com/yunus/nfcpay/model/TLVData");
        methodId = (*gJniEnv)->GetMethodID(gJniEnv, clsTlv, "getValue", "()[B");
        jValue = (*gJniEnv)->CallObjectMethod(gJniEnv, foundData, methodId);

        tlvFree(foundData);
    }

    return jValue;
}

