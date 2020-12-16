/*-----------------------------------------------------------------------------
- Author :  Yunus KIZILTEPE
- Date   :  22.11.2020 - 21:02
- Version:

- File: util_tlv.h

- Description: File to parse tlv data

- Warnings \

-----------------------------------------------------------------------------*/
#ifndef _UTIL_TLV_H_
#define _UTIL_TLV_H_

/*------------------------------ TAB INCLUDE --------------------------------*/
/*------------------------------ TAB DEFINE ---------------------------------*/
/*------------------------------- TAB TYPES ---------------------------------*/
/*-------------------------- TAB GLOBAL FUNCTIONS ---------------------------*/

///Function to parse all tlv data
jobject utilTlvParse(byte *buf, int len);

///Function to find related tag in tlv data
jobject utilTlvFind(jobject data, int tag);

#endif //end of #ifndef _UTIL_TLV_H_
