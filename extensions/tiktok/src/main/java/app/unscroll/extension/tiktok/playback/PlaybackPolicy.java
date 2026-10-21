package app.unscroll.extension.tiktok.playback;

/**
 * Who may be watched from a profile grid (decision P5–P7 in docs/DECISIONS.md).
 *
 * <ul>
 *   <li>Own profile: allowed.</li>
 *   <li>Friend profile (mutual follow): allowed.</li>
 *   <li>Anyone else: not allowed. The profile and grid remain visible.</li>
 * </ul>
 *
 * Videos shared in Direct Messages, opened from a notification or from a link to a
 * single video are not subject to this rule (P4).
 */
public final class PlaybackPolicy {
    /** TikTok's follow status value for a mutual follow ("Friends"). */
    public static final int FOLLOW_STATUS_MUTUAL = 2;

    private PlaybackPolicy() {
    }

    public static boolean canPlayFromProfile(boolean isOwnProfile, int followStatus) {
        return isOwnProfile || followStatus == FOLLOW_STATUS_MUTUAL;
    }
}
