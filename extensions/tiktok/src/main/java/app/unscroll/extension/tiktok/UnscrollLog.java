package app.unscroll.extension.tiktok;

import android.util.Log;

/**
 * Minimal logger. Messages are fixed strings chosen by Unscroll code; never pass
 * user content (names, ids, URLs, message text) into this class.
 */
public final class UnscrollLog {
    private static final String TAG = "Unscroll";

    private UnscrollLog() {
    }

    public static void info(String message) {
        Log.i(TAG, message);
    }

    /** Logs the failure type only; exception messages may contain user data. */
    public static void error(String where, Throwable throwable) {
        Log.e(TAG, where + " failed: " + (throwable == null ? "null" : throwable.getClass().getName()));
    }
}
