/*
 *  Copyright 2017 Idealnaya rabota LLC
 *  Licensed under Multy.io license.
 *  See LICENSE for details
 */

package io.multy.util;

import android.os.Build;

public class Constants {

    public static final String DEVICE_NAME = "Andrdoid " + Build.MANUFACTURER
            + " " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")"
            + " " + Build.VERSION.RELEASE
            + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

//    static final String BASE_URL = "http://192.168.0.121:7778/";  // local
//    static final String BASE_URL = "http://88.198.47.112:7778/";  // remote
//    static final String BASE_URL = "https://api.multy.io/";  // Special for Jack Bolt!
    public static final String BASE_URL = "https://stage.multy.io/";  // Special for Jack Bolt!

    public static final String DONTAION_ADDRESS = "mzqiDnETWkunRDZxjUQ34JzN1LDevh5DpU";

    public static final int ANDROID_OS_ID = 1;

    public static final int POSITION_ASSETS = 0;
    public static final int POSITION_FEED = 1;
    public static final int POSITION_CONTACTS = 3;
    public static final int POSITION_SETTINGS = 4;

    public static final String SPAN_DIVIDER = ",";
    public static final String SPACE = " ";
    public static final String NEW_LINE = "\n";
    public static final String QUESTION_MARK = "?";
    public static final String EQUAL = "=";

    public static final String PREF_APP_INITIALIZED = "is_first_start";
    public static final String PREF_BACKUP_SEED = "backup_seed";
    public static final String PREF_AUTH = "pref_auth";
    public static final String PREF_EXCHANGE_PRICE = "pref_exchange_price";
    public static final String PREF_WALLET_TOP_INDEX_BTC = "pref_wallet_top_index_btc";
    public static final String PREF_WALLET_TOP_INDEX_ETH = "pref_wallet_top_index_eth";
    public static final String PREF_IS_FINGERPRINT_ENABLED = "PREF_IS_FINGERPRINT_ENABLED";
    public static final String PIN_COUNTER = "PIN_COUNTER";
    public static final String ANDROID_PACKAGE = "android.webkit.";
    public static final String PREF_PIN = "PREF_PIN";
    public static final String PREF_IV = "PREF_IV";
    public static final String PREF_KEY = "PREF_KEY";
    public static final String PREF_LOCK = "PREF_LOCK";
    public static final String PREF_UNLOCKED = "PREF_UNLOCKED";
    public static final String PREF_LOCK_DATE = "PREF_LOCK_DATE";
    public static final String PREF_LOCK_MULTIPLIER = "PREF_LOCK_MULTIPLIER";
    public static final String PREF_VERSION = "PREF_VERSION";
    public static final String PREF_SELF_CLICKED = "PREF_SELF_CLICKED";
    public static final String PREF_DONATE_ADDRESS_BTC = "PREF_DONATE_ADDRESS_BTC";
    public static final String PREF_DONATE_ADDRESS_ETH = "PREF_DONATE_ADDRESS_ETH";
    public static final String FLAG_VIEW_SEED_PHRASE = "view_seed_phrase";
    public static final String PREF_IS_PUSH_ENABLED = "PREF_IS_PUSH_ENABLED";

    public static final int REQUEST_CODE_SET_CHAIN = 560;
    public static final String CHAIN_NAME = "chain_name";
    public static final String CHAIN_NET = "chain_net";

    public static final int CAMERA_REQUEST_CODE = 328;
    public static final String EXTRA_QR_CONTENTS = "EXTRA_QR_CONTENTS";
    public static final String EXTRA_WALLET_ID = "EXTRA_WALLET_ID";
    public static final String EXTRA_SENDER_ADDRESS = "EXTRA_SENDER_ADDRESS";
    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    public static final String EXTRA_AMOUNT = "EXTRA_AMOUNT";
    public static final String EXTRA_RESTORE = "EXTRA_RESTORE";
    public static final String EXTRA_DONATION_CODE = "EXTRA_DONATION_CODE";

    public static final String DEEP_LINK_QR_CODE = "QR_CODE";

    public final static String BTC = "BTC";
    public final static String ETH = "ETH";
    public final static String USD = "USD";
    public final static String EUR = "EUR";

    public static final String ERROR_LOAD_EXCHANGE_PRICE = "Can't load exchange price. Will be used the last one";
    public static final String ERROR_ADDING_ADDRESS = "An error occurred while adding new address";

    public static final int ZERO = 0;
    public static final int ONE = 1;

    public static final String BULLETS_FIVE = " • • • ∙ ";

    public static final int TRANSACTIONS_EMPTY_SIZE = 0;
    public static final int ADDRESS_PART = 9;

    public static final int TX_MEMPOOL_INCOMING = 1;
    public static final int TX_MEMPOOL_OUTCOMING = 3;
    public static final int TX_IN_BLOCK_INCOMING = 2;
    public static final int TX_IN_BLOCK_OUTCOMING = 4;
    public static final int TX_CONFIRMED_INCOMING = 5;
    public static final int TX_CONFIRMED_OUTCOMING = 6;

    public static final int REQUEST_CODE_RESTORE = 22;
    public static final int REQUEST_CODE_CREATE = 22;

    public static final String BLOCKCHAIN_INFO_PATH = "https://testnet.blockchain.info/tx/";
    public static final String PUSH_TOPIC = "btcTransactionUpdate-";
    public static final String MULTY_IO_URL = "http://multy.io";
    public static final String CHAIN_ID = "CHAIN_ID";

    public static final int DONATE_WITH_TRANSACTION = 10000;
    public static final int DONATE_ADDING_ACTIVITY = 10200;
    public static final int DONATE_ADDING_CONTACTS = 10201;
    public static final int DONATE_ADDING_PORTFOLIO = 10202;
    public static final int DONATE_ADDING_CHARTS = 10203;
    public static final int DONATE_ADDING_IMPORT_WALLET = 10300;
    public static final int DONATE_ADDING_EXCHANGE = 10301;
    public static final int DONATE_ADDING_WIRELESS_SCAN = 10302;
}
