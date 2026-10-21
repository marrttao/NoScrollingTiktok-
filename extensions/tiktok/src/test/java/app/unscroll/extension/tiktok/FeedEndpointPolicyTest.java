package app.unscroll.extension.tiktok;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

import app.unscroll.extension.tiktok.feed.FeedEndpointPolicy;

class FeedEndpointPolicyTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "https://api16-normal-c-useast1a.tiktokv.com/aweme/v1/feed/?type=0&max_cursor=0",
            "https://api16-normal-c-useast1a.tiktokv.com/aweme/v2/feed/?count=6",
            "https://api.tiktokv.com/aweme/v2/feed",
            "https://api.tiktokv.com/aweme/v2/follow/feed/?cursor=0",
            "https://api.tiktokv.com/aweme/v2/follow/feed/inner",
            "https://api.tiktokv.com/tiktok/v1/friend/friend_feed?count=10",
            "https://api.tiktokv.com/tiktok/feed/explore/v1",
            "https://api.tiktokv.com/tiktok/feed/nearby/revamp",
            "https://webcast.tiktokv.com/webcast/feed/?channel_id=86",
            "https://api.tiktokv.com/aweme/v1/search/item/?keyword=x",
            "https://api.tiktokv.com/aweme/v1/general/search/single/?keyword=x",
            "https://api.tiktokv.com/aweme/v1/challenge/aweme/?ch_id=1",
            "https://api.tiktokv.com/aweme/v1/music/aweme/?music_id=1",
            "https://api.tiktokv.com/tiktok/v1/mix/videos/?mix_id=1",
            "https://API.TIKTOKV.COM/AWEME/V1/FEED/",
            "https://api.tiktokv.com//aweme//v1//feed/",
            "/aweme/v1/feed/",
            "aweme/v2/feed/",
    })
    void feedEndpointsAreBlocked(String url) {
        assertEquals(FeedEndpointPolicy.BLOCKED_URL, FeedEndpointPolicy.filterUrl(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://api.tiktokv.com/aweme/v1/aweme/detail/?aweme_id=1",
            "https://api.tiktokv.com/aweme/v1/multi/aweme/detail/",
            "https://api.tiktokv.com/aweme/v1/aweme/post/?user_id=1",
            "https://api.tiktokv.com/aweme/v1/discover/search/?keyword=friend",
            "https://api.tiktokv.com/aweme/v1/search/user/sug/",
            "https://api.tiktokv.com/aweme/v2/feedback/reasons",
            "https://api.tiktokv.com/aweme/v1/aweme/feedback/",
            "https://api.tiktokv.com/aweme/v1/music/search/?keyword=x",
            "https://api.tiktokv.com/aweme/v1/music/detail/",
            "https://api.tiktokv.com/aweme/v1/user/following/list/",
            "https://im-api.tiktokv.com/v1/message/send",
            "https://api.tiktokv.com/aweme/v1/create/aweme/",
            "https://example.com/aweme/v1/feedx",
            "https://api.tiktokv.com",
            "",
    })
    void otherEndpointsPass(String url) {
        assertEquals(url, FeedEndpointPolicy.filterUrl(url));
    }

    @Test
    void nullPasses() {
        assertNull(FeedEndpointPolicy.filterUrl(null));
    }
}
