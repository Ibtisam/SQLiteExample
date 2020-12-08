package com.example.datamanagementsqllite;

import android.provider.BaseColumns;

public final class ProfileContract {
    // to prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ProfileContract(){}

    /* Inner class that defines the table profile */
    public static class Profile implements BaseColumns {
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_MARITAL = "marital";
    }
}
