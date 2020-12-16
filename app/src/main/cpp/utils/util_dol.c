/**-------------------------------------------------------------------------------------------------
- Author :  Yunus KIZILTEPE
- Date   :  27.11.2020 - 19:24
- Version:

- File   : util_dol.c
- Description: File includes utilities of EMV data object list
- Warnings \
--------------------------------------------------------------------------------------------------*/
/**------------------------------------- TAB INCLUDE ---------------------------------------------*/
#include <jni.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <malloc.h>

#include "util_types.h"
#include "util_jni.h"
#include "util_tlv.h"

#include "util_emv_consts.h"
/**------------------------------------- TAB DEFINE ----------------------------------------------*/
/**------------------------------------- TAB TYPES -----------------------------------------------*/
/**------------------------------------- TAB FUNCTION ABSTRACTION --------------------------------*/
/**------------------------------------- TAB PUBLIC VARIABLES ------------------------------------*/
/**------------------------------------- TAB PRIVATE VARIABLES -----------------------------------*/
/**------------------------------------- TAB PRIVATE FUNCTONS ------------------------------------*/
/**-------------------------------------------------------------------------------------------------
                            int2Bcd
- Brief  : Function to convert int number to bcd
- Detail : Function to convert int number to bcd array.
           Bcd array will be padding right.
           Void area will be 00
- Parameters \
 -- @ptr    : input pointer. This is start pointer
 -- @num    : number to be converted to bcd
 -- @len    : length of bcd array
- Returns  \
  --@ptr    : pointer of after converted number to bcd
--------------------------------------------------------------------------------------------------*/
static byte *int2Bcd(byte *ptr, int num, int len)
{
    int lastTwoNum;
    int bcdLen = 0;

    ptr += len; // for padding right
    while (num > 0) {

        if(bcdLen == len)
            break;

        lastTwoNum = num % 100;
        (--ptr)[0]  = ((lastTwoNum / 10) * 0x10 + (lastTwoNum % 10));
        num /= 100;
        bcdLen++;
    }

    ptr += bcdLen;

    return ptr;
}
/**------------------------------------- TAB GLOBAL FUNCTIONS ------------------------------------*/
/**-------------------------------------------------------------------------------------------------
                            Java_com_yunus_nfcpay_EMVNative_checkDOL

- Brief  : Function to check dol data
- Detail : Function will check all dol length
- Parameters \
 -- @env    : java environment
 -- @clazz  : instance of java function
 -- @jdol   : data object list
 -- @tag    : DOL tag, example: CDOL, PDOL
- Returns  \
 --@retVal  : if true dol is ok, if false dol is wrong
--------------------------------------------------------------------------------------------------*/
JNIEXPORT jboolean JNICALL
Java_com_yunus_nfcpay_EMVNative_checkDOL(JNIEnv *env, jclass clazz, jbyteArray jdol, jint tag) {
    boolean retVal = FALSE;
    byte *dol;
    uint tagLen;
    byte lenData;
    uint tlvTag;
    uint goNext;
    uint dolLen = (*env)->GetArrayLength(env, jdol);

    if ((PDOL_TAG != tag) && (CDOL1_TAG != tag) && (CDOL2_TAG != tag))
    {
        return FALSE; // DOL tag is invalid
    }

    dol = malloc(dolLen);
    utilJniJByteArrayToChar(env, jdol, dol, dolLen);

    for (int i = 0; i < dolLen; i++) {
        goNext = i;
        tlvTag = dol[goNext++];
        tagLen = 1;
        if ((tlvTag & 0x1Fu) == 0x1F) {
            tlvTag = (tlvTag << 8u) + dol[goNext++];
            tagLen = 2;
        }

        lenData = dol[goNext];
        i += tagLen;

        switch (tlvTag) {
            case TTQ_TAG:
                retVal |= (boolean)(4 == lenData ? TRUE : FALSE);
                break;
            case AMOUNT_AUTHORISED_TAG:
                retVal |= (boolean)(6 == lenData ? TRUE : FALSE);
                break;
            case AMOUNT_OTHER_TAG:
                retVal |= (boolean)(6 == lenData ? TRUE : FALSE);
                break;
            case TERMINAL_COUNTRY_CODE_TAG:
                retVal |= (boolean)(2 == lenData ? TRUE : FALSE);
                break;
            case TRANSACTION_CURRENCY_CODE_TAG:
                retVal |= (boolean)(2 == lenData ? TRUE : FALSE);
                break;
            case TVR_TAG:
                retVal |= (boolean)(5 == lenData ? TRUE : FALSE);
                break;
            case TRANSACTION_DATE_TAG:
                retVal |= (boolean)(3 == lenData ? TRUE : FALSE);
                break;
            case TRANSACTION_TIME_TAG:
                retVal |= (boolean)(3 == lenData ? TRUE : FALSE);
                break;
            case TRANSACTION_TYPE_TAG:
                retVal |= (boolean)(1 == lenData ? TRUE : FALSE);
                break;
            case UNPREDIC_NUM_TAG:
                if((1 == lenData) || (4 == lenData))
                                    retVal |= (boolean)TRUE;
                break;

        }
    }



    free(dol);
    return retVal;
}
/**-------------------------------------------------------------------------------------------------
                            Java_com_yunus_nfcpay_EMVNative_prepareDOL

- Brief  : Function to parepare dol
- Detail : Function will prapre dol data from readed dol

- Parameters \
 -- @env    : java environment
 -- @clazz  : instance of java function
 -- @jpdol  : processing data object list
 -- @tag    : DOL tag, example: CDOL, PDOL
- Returns  \
 --@preparedDol  : prepared dol data
--------------------------------------------------------------------------------------------------*/
JNIEXPORT jbyteArray JNICALL
Java_com_yunus_nfcpay_EMVNative_prepareDOL(JNIEnv *env, jclass clazz,
                                           jbyteArray jpdol, jint dolTag) {
    byte pdolOut[2048] = {0};
    byte *currPtr = pdolOut;
    uint tagLen;
    byte lenData;
    uint tlvTag;
    uint allDataLen = 0;
    uint goNext;
    byte *pdol = NULL;
    uint pdolLen = 0;

    if(NULL != jpdol)
    {
        pdolLen = (*env)->GetArrayLength(env, jpdol);
        pdol = malloc(pdolLen);
        utilJniJByteArrayToChar(env, jpdol, pdol, pdolLen);
    }

    for (int i = 0; i < pdolLen; i++) {
        goNext = i;
        tlvTag = pdol[goNext++];
        tagLen = 1;
        if ((tlvTag & 0x1Fu) == 0x1F) {
            tlvTag = (tlvTag << 8u) + pdol[goNext++];
            tagLen = 2;
        }

        lenData = pdol[goNext];
        allDataLen += lenData;
        i += tagLen;

        switch (tlvTag) {
            case TTQ_TAG: // Terminal Transaction Qualifiers -- 0x9F66 -- 4 bytes
                currPtr++[0] = 0xF0;
                currPtr++[0] = 0x20;
                currPtr += 2;
                break;
            case AMOUNT_AUTHORISED_TAG: // Amount authorised  -- 0x9F02 -- 6 bytes
            case AMOUNT_OTHER_TAG: // Amount other -- 0x9F03 -- 6 bytes
            case TVR_TAG: //Transaction Verification Results -- 0x95 -- 5 bytes:
                //TODO: add amount pay and TVR
                currPtr += lenData;
                break;
            case TERMINAL_COUNTRY_CODE_TAG: // Terminal Country Code -- 0x9F1A -- 2 bytes
                currPtr++[0] = 0x01;
                currPtr++[0] = 0x00;
                break;
            case TRANSACTION_CURRENCY_CODE_TAG: // Transaction Currency Code -- 0x5F2A -- 2 bytes
                //0x978 --- EURO
                currPtr++[0] = 0x09;
                currPtr++[0] = 0x78;
                break;
            case TRANSACTION_DATE_TAG: // Transaction Date -- 0x9A -- 3 byte
            {
                struct tm tmInfo;
                time_t currentTime = time(NULL);
                tmInfo = *localtime(&currentTime);      // convert from

                currPtr =  int2Bcd(currPtr, tmInfo.tm_year % 100, 1);
                currPtr =  int2Bcd(currPtr, tmInfo.tm_mon + 1, 1);
                currPtr =  int2Bcd(currPtr, tmInfo.tm_mday, 1);
            }
                break;
            case TRANSACTION_TIME_TAG: // Transaction Time -- 0x9F21 -- 3 bytes
            {
                struct tm tmInfo;
                time_t currentTime = time(NULL);
                tmInfo = *localtime(&currentTime);      // convert from

                //TODO: check local time
                currPtr =  int2Bcd(currPtr, tmInfo.tm_hour, 1);
                currPtr =  int2Bcd(currPtr, tmInfo.tm_min, 1);
                currPtr =  int2Bcd(currPtr, tmInfo.tm_sec, 1);
            }
                break;
            case TRANSACTION_TYPE_TAG: // Transaction Type -- 9C -- 1 byte
                currPtr++[0] = 0x00;
                break;
            case UNPREDIC_NUM_TAG: //Unpredictable Number -- 9F37 -- 1 or 4 bytes -- for DDA
            {
                jbyteArray randNumber;
                byte unPredicNum[MAXLEN_UNPREDIC + 1] = {0};
                jmethodID mSecurRand = (*env)->GetStaticMethodID(env, clazz, "getSecureRandom","(I)[B");
                randNumber = (*env)->CallStaticObjectMethod(env, clazz, mSecurRand, lenData);
                /*srand(i);
                randNumber = rand();*/

                utilJniJByteArrayToChar(env, randNumber, unPredicNum, lenData);
                memcpy(currPtr, unPredicNum, lenData);
                currPtr += lenData; // 1 or 4 bytes
            }
                break;
            default:
                currPtr += lenData;
                break;
        }
    }
    switch (dolTag) {
        case PDOL_TAG:
            memmove(pdolOut + 2, pdolOut, currPtr - pdolOut);
            pdolOut[0] = 0x83;
            pdolOut[1] = allDataLen;
            currPtr += 2;
            break;
        case CDOL1_TAG:
            break;
        case CDOL2_TAG:
            break;
        default:
            break;

    }

    jbyteArray preparedDol = (*env)->NewByteArray(env, currPtr - pdolOut);
    (*env)->SetByteArrayRegion(env, preparedDol, 0, currPtr - pdolOut, (jbyte *)pdolOut);

    if(NULL != pdol)
        free(pdol);

    return preparedDol;
}
