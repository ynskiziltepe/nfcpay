/*-----------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
- Date   :  21.11.2020 - 20:08
- Version:

- File: util_types.h

- Description: Generic types are defined in this file

- Warnings \

-----------------------------------------------------------------------------*/
#ifndef _UTIL_TYPES_H_
#define _UTIL_TYPES_H_

/*------------------------------ TAB INCLUDE --------------------------------*/
/*------------------------------ TAB DEFINE ---------------------------------*/
#if !_LOGIC_DEFINED_
//Boolean values
#define TRUE                           1 // if comparison matches
#define FALSE                          0 // if comparison does not match
#endif

#if !_BOOLEAN_DEFINED_
typedef unsigned int boolean;
#endif

//Return values
#define RET_OK                         0  //means no error
#define RET_FAIL                      -1  //means error not specified

#ifndef NULL
#define NULL                          0   // constant for NULL pointers
#endif

//get len in ascii array
#define GETLEN(stringVal)             strlen((char*)stringVal)
/*------------------------------- TAB TYPES ---------------------------------*/
#if !BYTE_DEFINED
typedef unsigned char         byte;      //type for unsigned  8-bit values
#endif
typedef unsigned short        ushort;    //type for unsigned 16-bit values
typedef unsigned int          uint;      //type for unsigned 32-bit values
typedef unsigned long long    uint64;    //type for unsigned 64-bit values
typedef long long             int64;     //type for signed   64-bit values

#define SW1_OK 0x90
#define SW2_OK 0x00
#define SW_LEN 2
#define MAX_EMV_LEN 4096
#define MAX_EMV_TAG_LEN 2

#define PSE "1PAY.SYS.DDF01"
#define PPSE "2PAY.SYS.DDF01"
/*-------------------------- TAB GLOBAL FUNCTIONS ---------------------------*/


#endif //end of #ifndef _UTIL_TYPES_H_
