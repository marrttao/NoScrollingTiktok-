package app.unscroll.extension.tiktok.navigation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import app.unscroll.extension.tiktok.navigation.LinkPolicy.Decision;

class LinkPolicyTest {
    @ParameterizedTest
    @CsvSource({
            // single videos and photo posts
            "https://www.tiktok.com/@someone/video/7300000000000000000, VIDEO",
            "https://www.tiktok.com/@someone/video/7300000000000000000?is_from_webapp=1&sender_device=pc, VIDEO",
            "https://www.tiktok.com/@someone/photo/7300000000000000000, VIDEO",
            "https://vm.tiktok.com/ZMabcdef/, VIDEO",
            "https://vt.tiktok.com/ZSabcdef/, VIDEO",
            "https://www.tiktok.com/t/ZTabcdef/, VIDEO",
            "https://m.tiktok.com/v/7300000000000000000.html, VIDEO",
            "snssdk1233://aweme/detail/7300000000000000000, VIDEO",
            // profiles
            "https://www.tiktok.com/@someone, PROFILE",
            "https://www.tiktok.com/@someone?lang=en, PROFILE",
            "snssdk1233://user/profile/6800000000000000000, PROFILE",
            // feeds and feed-like pages
            "https://www.tiktok.com/, BLOCK",
            "https://www.tiktok.com, BLOCK",
            "https://www.tiktok.com/foryou, BLOCK",
            "https://www.tiktok.com/following, BLOCK",
            "https://www.tiktok.com/friends, BLOCK",
            "https://www.tiktok.com/live, BLOCK",
            "https://www.tiktok.com/@someone/live, BLOCK",
            "https://www.tiktok.com/tag/cats, BLOCK",
            "https://www.tiktok.com/music/original-sound-123, BLOCK",
            "https://www.tiktok.com/search?q=cats, BLOCK",
            "https://www.tiktok.com/discover/cats, BLOCK",
            "snssdk1233://feed?refer=web, BLOCK",
            "snssdk1233://challenge/detail/123, BLOCK",
            "snssdk1233://music/detail/123, BLOCK",
            "snssdk1233://live?room_id=1, BLOCK",
            "snssdk1233://webcast_room?room_id=1, BLOCK",
            "snssdk1233://search?keyword=cats, BLOCK",
            // everything else stays reachable (login, settings, chats, other sites)
            "snssdk1233://chat?uid=1, ALLOW",
            "snssdk1233://notification, ALLOW",
            "snssdk1233://setting, ALLOW",
            "https://www.tiktok.com/legal/privacy-policy, ALLOW",
            "https://example.com/tag/cats, ALLOW",
            "not a link, ALLOW",
    })
    void classifies(String link, Decision expected) {
        assertEquals(expected, LinkPolicy.classify(link));
    }

    @org.junit.jupiter.api.Test
    void nullIsAllowed() {
        assertEquals(Decision.ALLOW, LinkPolicy.classify(null));
    }
}
