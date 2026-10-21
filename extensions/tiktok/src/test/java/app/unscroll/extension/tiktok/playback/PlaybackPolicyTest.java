package app.unscroll.extension.tiktok.playback;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PlaybackPolicyTest {
    @Test
    void ownProfileAlwaysPlays() {
        assertTrue(PlaybackPolicy.canPlayFromProfile(true, 0));
    }

    @Test
    void mutualFollowPlays() {
        assertTrue(PlaybackPolicy.canPlayFromProfile(false, PlaybackPolicy.FOLLOW_STATUS_MUTUAL));
    }

    @Test
    void oneWayFollowOrStrangerDoesNotPlay() {
        assertFalse(PlaybackPolicy.canPlayFromProfile(false, 0)); // not following
        assertFalse(PlaybackPolicy.canPlayFromProfile(false, 1)); // I follow them only
        assertFalse(PlaybackPolicy.canPlayFromProfile(false, 4)); // follow request pending
    }
}
