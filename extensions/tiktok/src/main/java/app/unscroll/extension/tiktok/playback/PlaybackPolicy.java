package app.unscroll.extension.tiktok.playback;

/**
 * Who may be watched from a profile grid (decisions P5–P7 in docs/DECISIONS.md).
 *
 * <ul>
 *   <li>Own profile: allowed.</li>
 *   <li>Friend profile (mutual follow): allowed, including its reposts.</li>
 *   <li>Anyone else: not allowed. The profile and its grid stay visible.</li>
 * </ul>
 *
 * Videos shared in Direct Messages, opened from a notification or from a link to a
 * single video are not subject to this rule (P4).
 */
public final class PlaybackPolicy {
    /** TikTok's follow status value for a mutual follow ("Friends"). */
    public static final int FOLLOW_STATUS_MUTUAL = 2;

    /** Sentinel for "the opener did not say" — treated as not a friend (fail-closed). */
    public static final int FOLLOW_STATUS_UNKNOWN = Integer.MIN_VALUE;

    private PlaybackPolicy() {
    }

    public static boolean canPlayFromProfile(boolean isOwnProfile, int followStatus) {
        return isOwnProfile || followStatus == FOLLOW_STATUS_MUTUAL;
    }

    /**
     * Decision for a video screen opened by TikTok.
     *
     * @param videoFrom    TikTok's {@code video_from} extra (e.g. {@code from_profile_other})
     * @param isMyProfile  TikTok's {@code is_my_profile} extra
     * @param followStatus the profile owner's follow status from the {@code follow_status}
     *                     extra, or {@link #FOLLOW_STATUS_UNKNOWN}
     */
    public static boolean canOpenVideoScreen(String videoFrom, boolean isMyProfile, int followStatus) {
        if (videoFrom == null || !videoFrom.startsWith("from_profile")) {
            return true; // not opened from a profile grid: DMs, notifications, links...
        }
        if (videoFrom.equals("from_profile_self")) {
            return true;
        }
        return canPlayFromProfile(isMyProfile, followStatus);
    }
}
