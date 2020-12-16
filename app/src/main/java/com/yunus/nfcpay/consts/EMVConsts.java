package com.yunus.nfcpay.consts;

public class EMVConsts {

    public static final String PSE = "1PAY.SYS.DDF01";
    public static final String PPSE = "2PAY.SYS.DDF01";

    // EMV Command(s) (First Bytes - Cla, Ins)
    public static final int SELECT = 0x00A4;
    public static final int COMPUTE_CRYPTOGRAPHIC_CHECKSUM = 0x802A; //COMPUTE CRYPTOGRAPHIC CHECKSUM
    public static final int EXCHANGE_RELAY_RESISTANCE_DATA = 0x80EA; // EXCHANGE RELAY RESISTANCE DATA
    public static final int GENERATE_AC = 0x80AE; // GENERATE AC
    public static final int GET_DATA = 0x80CA; // GET DATA
    public static final int GET_PROCESSING_OPTIONS = 0x80A8; // GET PROCESSING OPTIONS
    public static final int PUT_DATA = 0x80DA; // PUT DATA
    public static final int READ_RECORD = 0x00B2; // READ RECORD
    public static final int RECOVER_AC = 0x80D0; // RECOVER AC
    //--------------------------------------------------------------------------------------------

    // TLV Tag(s) (Constant(s))
    public static final int AID = 0x4F; // AID (Application Identifier)
    public static final int PDOL = 0x9F38;// PDOL (Processing Options Data Object List)
    public static final int CDOL1 = 0x8C; // CDOL1 (Card Risk Management Data Object List 1)
    public static final int CDOL2 = 0x8D; // CDOL2 (Card Risk Management Data Object List 2)
    public static final int GPO_RMT1 = 0x80; // GPO Response message template 1
    public static final int GPO_RMT2 = 0x77; // GPO Response message template 2
    public static final int AFL = 0x94; // AFL (Application File Locator)
    public static final int LAST_ONLINE_ATC_REGISTER = 0x9F13; // Last Online ATC (Application Transaction Counter) Register
    public static final int PIN_TRY_COUNTER = 0x9F17; // PIN (Personal Identification Number) Try Counter
    public static final int ATC = 0x9F36; // ATC (Application Transaction Counter)
    public static final int P_UN_ATC_TRACK1 = 0x9F63;
    public static final int N_ATC_TRACK1 = 0x9F64;
    public static final int P_UN_ATC_TRACK2 = 0x9F66;
    public static final int N_ATC_TRACK2 = 0x9F67;
    public static final int PAYPASS_LOG_ENTRY = 0x9F4D;
    public static final int PAYWAVE_LOG_ENTRY = 0xDF60;
    public static final int PAYPASS_LOG_FORMAT = 0x9F4F;
    public static final int PAYWAVE_LOG_FORMAT = 0x9F80;
    public static final int APPLICATION_LABEL = 0x50; // Application Label
    public static final int APPLICATION_PAN = 0x5A; // Application PAN (Primary Account Number)
    public static final int CARDHOLDER_NAME = 0x5F20; // Cardholder Name
    public static final int APPLICATION_EXPIRATION_DATE = 0x5F24; // Application Expiration Date
    public static final int TTQ = 0x9F66; // Terminal Transaction Qualifiers (TTQ)
    public static final int AMOUNT_AUTHORISED = 0x9F02; // Amount, Authorised (Numeric)
    public static final int AMOUNT_OTHER = 0x9F03; // Amount, Other (Numeric)
    public static final int APPLICATION_IDENTIFIER = 0x9F06; // Application Identifier
    public static final int APPLICATION_VERSION_NUMBER = 0x9F09; // Application Version Number
    public static final int TERMINAL_COUNTRY_CODE = 0x9F1A; // Terminal Country Code
    public static final int TERMINAL_FLOOR_LIMIT = 0x9F1B; // Terminal Floor Limit
    public static final int TRANSACTION_CURRENCY_CODE = 0x5F2A; // Transaction Currency Code
    public static final int TVR = 0x95; // Transaction Verification Results (TVR)
    public static final int TRANSACTION_DATE = 0x9A; // Transaction Date
    public static final int TRANSACTION_STATUS_INFORMATION = 0x9B; // Transaction Status Information
    public static final int TRANSACTION_TYPE = 0x9C; // Transaction Type
    public static final int TRANSACTION_TIME = 0x9F21; // Transaction Time
    public static final int CRYPTOGRAM_INFORMATION_DATA = 0x9F27; // Cryptogram Information Data
    public static final int UNPREDICTABLE = 0x9F37; // Unpredictable Number
    public static final int MERCHANT_NAME = 0x9F4E; // Merchant Name

    public enum CardBrand {
                VISA,
                VISA_ELECTRON,
                MASTER,
                MAESTRO,
                AMEX
    }

    public enum CardTech
    {
        PAYWAVE,
        PAYPASS
    }
}
