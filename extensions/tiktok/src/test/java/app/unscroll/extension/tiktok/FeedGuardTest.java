package app.unscroll.extension.tiktok;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import app.unscroll.extension.tiktok.feed.FeedGuard;

class FeedGuardTest {
    static class BaseResponse {
        private int hasMore = 1;
    }

    /** Shape of TikTok's FeedItemList as far as Unscroll relies on it. */
    static final class FakeFeedItemList extends BaseResponse {
        public List<Object> items = new ArrayList<>(Arrays.asList("a", "b"));
    }

    static final class FakeFriendsFeedResponse {
        private List<Object> friendFeedData = new ArrayList<>(Arrays.asList("a"));
        boolean hasMore = true;
    }

    @Test
    void feedItemListIsEmptiedAndStopsPaging() throws Exception {
        FakeFeedItemList list = new FakeFeedItemList();
        FeedGuard.emptyFeedItemList(list);
        assertTrue(list.items.isEmpty());
        java.lang.reflect.Field hasMore = BaseResponse.class.getDeclaredField("hasMore");
        hasMore.setAccessible(true);
        assertEquals(0, hasMore.getInt(list));
    }

    @Test
    void friendsResponseIsEmptied() {
        FakeFriendsFeedResponse response = new FakeFriendsFeedResponse();
        FeedGuard.emptyFriendsFeedResponse(response);
        assertTrue(response.friendFeedData.isEmpty());
        assertFalse(response.hasMore);
    }

    @Test
    void unknownShapesAndNullAreIgnored() {
        FeedGuard.emptyFeedItemList(new Object());
        FeedGuard.emptyFeedItemList(null);
        FeedGuard.emptyFriendsFeedResponse(null);
    }
}
