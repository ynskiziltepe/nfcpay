/*-----------------------------------------------------------------------------
- Author :  Yunus KIZILTEPE
- Date   :  21.11.2020 - 20:31
- Version:

- File: util_emv_consts.h

- Description: Defines, types and function prototypes related with the 
            emv consts are declared in this file

- Warnings \

-----------------------------------------------------------------------------*/
#ifndef _UTIL_EMV_CONSTS_H_
#define _UTIL_EMV_CONSTS_H_

/*------------------------------ TAB INCLUDE --------------------------------*/

/*------------------------------ TAB DEFINE ---------------------------------*/
#define SELECT 0x00A4
#define COMPUTE_CRYPTOGRAPHIC_CHECKSUM_TAG 0x802A
#define EXCHANGE_RELAY_RESISTANCE_DATA_TAG 0x80EA
#define GENERATE_AC_TAG 0x80AE
#define GET_DATA_TAG 0x80CA
#define GET_PROCESSING_OPTIONS_TAG 0x80A8
#define PUT_DATA_TAG 0x80DA
#define READ_RECORD_TAG 0x00B2
#define RECOVER_AC_TAG 0x80D0
#define AID_TAG 0x4F
#define PDOL_TAG 0x9F38
#define CDOL1_TAG 0x8C
#define CDOL2_TAG 0x8D
#define GPO_RMT1_TAG 0x80
#define GPO_RMT2_TAG 0x77
#define AFL_TAG 0x94
#define LAST_ONLINE_ATC_REGISTER_TAG 0x9F13
#define PIN_TRY_COUNTER_TAG 0x9F17
#define ATC_TAG 0x9F36
#define P_UN_ATC_TRACK1_TAG 0x9F63
#define N_ATC_TRACK1_TAG 0x9F64
#define P_UN_ATC_TRACK2_TAG 0x9F66
#define N_ATC_TRACK2_TAG 0x9F67
#define PAYPASS_LOG_ENTRY_TAG 0x9F4D
#define PAYWAVE_LOG_ENTRY_TAG 0xDF60
#define PAYPASS_LOG_FORMAT_TAG 0x9F4F
#define PAYWAVE_LOG_FORMAT_TAG 0x9F80
#define APPLICATION_LABEL_TAG 0x50
#define APPLICATION_PAN_TAG 0x5A
#define CARDHOLDER_NAME_TAG 0x5F20
#define APPLICATION_EXPIRATION_DATE_TAG 0x5F24
#define TTQ_TAG  0x9F66
#define AMOUNT_AUTHORISED_TAG 0x9F02
#define AMOUNT_OTHER_TAG 0x9F03
#define APPLICATION_IDENTIFIER_TAG 0x9F06
#define APPLICATION_VERSION_NUMBER_TAG 0x9F,0x09
#define TERMINAL_COUNTRY_CODE_TAG 0x9F1A
#define TERMINAL_FLOOR_LIMIT_TAG 0x9F1B
#define TRANSACTION_CURRENCY_CODE_TAG 0x5F2A
#define TVR_TAG 0x95
#define TSI_TAG 0x9B
#define TRANSACTION_DATE_TAG 0x9A
#define TRANSACTION_STATUS_INFORMATION_TAG 0x9B
#define TRANSACTION_TYPE_TAG 0x9C
#define TRANSACTION_TIME_TAG 0x9F21
#define CRYPTOGRAM_INFORMATION_DATA_TAG 0x9F27
#define UNPREDIC_NUM_TAG 0x9F37
#define MERCHANT_NAME_TAG 0x9F4E

#define MAXLEN_UNPREDIC             4
/*------------------------------- TAB TYPES ---------------------------------*/

/*-------------------------- TAB GLOBAL FUNCTIONS ---------------------------*/

#endif //end of #ifndef _UTIL_EMV_CONSTS_H_
