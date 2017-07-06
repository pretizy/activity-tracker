package com.pretizy.activitytracker.model;

import android.provider.BaseColumns;

/**
 * Created by gerald on 10/5/16.
 */
public final class EventReaderContract {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private EventReaderContract() {}

        /* Inner class that defines the table contents */
        public static class EventEntry implements BaseColumns {
            public static final String TABLE_NAME = "event";
            public static final String COLUMN_NAME_TITLE = "title";
            public static final String COLUMN_NAME_CONTENT = "content";
            public static final String COLUMN_NAME_DATE = "date";
            public static final String COLUMN_NAME_TIME = "time";
            public static final String COLUMN_NAME_AM_PM = "am_pm";
        }


}
