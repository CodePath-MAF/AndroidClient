
package org.missionassetfund.apps.android.utils;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class ParseUtils {
    protected static final String DEBUG_TAG = "debug";
    protected static final String ERROR_TAG = "error";

    protected static final String PIN_SUCCESS_MESSAGE = "Object pinned successfully";
    protected static final String PIN_ERROR_MESSAGE = "Error pinning object";

    protected static final String SAVE_SUCCESS_MESSAGE = "Object pinned successfully";
    protected static final String SAVE_ERROR_MESSAGE = "Error pinning object";

    protected static final String DEL_SUCCESS_MESSAGE = "Object unpinned successfully";
    protected static final String DEL_ERROR_MESSAGE = "Error unpinning object";

    public static final SaveCallback PIN_CALLBACK = new SaveCallback() {

        @Override
        public void done(ParseException e) {
            if (e == null) {
                Log.d(DEBUG_TAG, PIN_SUCCESS_MESSAGE);
            } else {
                Log.d(ERROR_TAG, PIN_ERROR_MESSAGE);
            }
        }
    };

    public static final DeleteCallback DELETE_CALLBACK = new DeleteCallback() {

        @Override
        public void done(ParseException e) {
            if (e == null) {
                Log.d(DEBUG_TAG, DEL_SUCCESS_MESSAGE);
            } else {
                Log.d(ERROR_TAG, DEL_ERROR_MESSAGE);
            }

        }
    };

    public static final SaveCallback SAVE_CALLBACK = new SaveCallback() {

        @Override
        public void done(ParseException e) {
            if (e == null) {
                Log.d(DEBUG_TAG, SAVE_SUCCESS_MESSAGE);
            } else {
                Log.d(ERROR_TAG, SAVE_ERROR_MESSAGE);
            }
        }
    };

}
