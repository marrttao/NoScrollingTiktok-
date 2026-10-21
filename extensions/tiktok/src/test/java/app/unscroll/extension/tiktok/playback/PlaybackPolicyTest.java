package app.unscroll.extension.tiktok.playback;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PlaybackPolicyTest {
    private static final int UNKNOWN = PlaybackPolicy.FOLLOW_STATUS_UNKNOWN;

    @Test
    void ownProfileAlwaysPlays() {
        assertTrue(PlaybackPolicy.canPlayFromProfile(true, 0));
        assertTrue(PlaybackPolicy.canOpenVideoScreen("from_profile_self", false, UNKNOWN));
        assertTrue(PlaybackPolicy.canOpenVideoScreen("from_profile_other", true, UNKNOWN));
    }

    @Test
    void friendProfilePlays() {
        assertTrue(PlaybackPolicy.canOpenVideoScreen("from_profile_other", false, 2));
        assertTrue(PlaybackPolicy.canOpenVideoScreen("from_profile_page_repost_tab", false, 2));
    }

    @Test
    void nonFriendProfileDoesNotPlay() {
        assertFalse(PlaybackPolicy.canOpenVideoScreen("from_profile_other", false, 0)); // not following
        assertFalse(PlaybackPolicy.canOpenVideoScreen("from_profile_other", false, 1)); // one-way follow
        assertFalse(PlaybackPolicy.canOpenVideoScreen("from_profile_other", false, 4)); // request pending
        assertFalse(PlaybackPolicy.canOpenVideoScreen("from_profile_page_repost_tab", false, 1));
    }

    @Test
    void unknownFollowStatusFromProfileIsBlocked() {
        assertFalse(PlaybackPolicy.canOpenVideoScreen("from_profile_other", false, UNKNOWN));
    }

    @Test
    void videosNotFromAProfileGridPlay() {
        assertTrue(PlaybackPolicy.canOpenVideoScreen(null, false, UNKNOWN));
        assertTrue(PlaybackPolicy.canOpenVideoScreen("from_chat", false, UNKNOWN));
        assertTrue(PlaybackPolicy.canOpenVideoScreen("push", false, UNKNOWN));
    }
}
