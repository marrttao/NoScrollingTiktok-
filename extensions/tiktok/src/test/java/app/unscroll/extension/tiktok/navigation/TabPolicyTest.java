package app.unscroll.extension.tiktok.navigation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TabPolicyTest {
    @ParameterizedTest
    @ValueSource(strings = {"NOTIFICATION", "USER", "PUBLISH", "notification", " USER "})
    void allowedTabsAreKept(String tag) {
        assertTrue(TabPolicy.isAllowedBottomTab(tag));
        assertEquals(tag, TabPolicy.redirectTab(tag));
    }

    // Tags of TikTok 47.4.3 bottom tabs that lead to feeds, plus an unknown future tab.
    @ParameterizedTest
    @ValueSource(strings = {"HOME", "FRIENDS_TAB", "SHOP_MALL", "DISCOVER", "homepage_explore", "NEW_FEED_TAB", ""})
    void feedTabsGoToInbox(String tag) {
        assertFalse(TabPolicy.isAllowedBottomTab(tag));
        assertEquals(TabPolicy.INBOX_TAG, TabPolicy.redirectTab(tag));
    }

    @Test
    void nullPassesThrough() {
        assertNull(TabPolicy.redirectTab(null));
        assertFalse(TabPolicy.isAllowedBottomTab(null));
    }
}
