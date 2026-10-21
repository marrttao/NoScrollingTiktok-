package app.unscroll.extension.tiktok.playback;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.util.Locale;

import app.unscroll.extension.tiktok.UnscrollLog;

/** Closes a video screen opened from the profile grid of someone who is not a friend. */
public final class ProfilePlaybackGuard {
    private ProfilePlaybackGuard() {
    }

    /** Called at the end of TikTok's video screen {@code onCreate}. */
    public static void onVideoScreenCreated(Activity activity) {
        try {
            Intent intent = activity.getIntent();
            if (intent == null) {
                return;
            }
            String videoFrom = intent.getStringExtra("video_from");
            boolean isMyProfile = intent.getBooleanExtra("is_my_profile", false);
            int followStatus = intent.hasExtra("follow_status")
                    ? intent.getIntExtra("follow_status", PlaybackPolicy.FOLLOW_STATUS_UNKNOWN)
                    : PlaybackPolicy.FOLLOW_STATUS_UNKNOWN;
            if (PlaybackPolicy.canOpenVideoScreen(videoFrom, isMyProfile, followStatus)) {
                return;
            }
            Toast.makeText(activity, message(), Toast.LENGTH_SHORT).show();
            activity.finish();
        } catch (Throwable throwable) {
            UnscrollLog.error("profile playback guard", throwable);
        }
    }

    private static String message() {
        return "ru".equals(Locale.getDefault().getLanguage())
                ? "Видео доступны только в профилях друзей"
                : "Videos can only be watched on friends' profiles";
    }
}
