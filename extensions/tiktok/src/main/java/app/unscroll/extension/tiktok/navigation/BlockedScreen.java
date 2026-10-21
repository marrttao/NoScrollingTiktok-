package app.unscroll.extension.tiktok.navigation;

import android.app.Activity;
import android.widget.Toast;

import java.util.Locale;

import app.unscroll.extension.tiktok.UnscrollLog;

/**
 * Screens that are feeds by nature (decisions P9 and P11): other people's LIVE rooms,
 * hashtag pages and sound pages. They close as soon as they are created.
 */
public final class BlockedScreen {
    private BlockedScreen() {
    }

    /** Called at the end of the blocked screen's {@code onCreate}. */
    public static void close(Activity activity) {
        try {
            Toast.makeText(activity, message(), Toast.LENGTH_SHORT).show();
            activity.finish();
        } catch (Throwable throwable) {
            UnscrollLog.error("blocked screen", throwable);
        }
    }

    private static String message() {
        return "ru".equals(Locale.getDefault().getLanguage())
                ? "Unscroll: эта лента недоступна"
                : "Unscroll: this feed is not available";
    }
}
