package app.unscroll.extension.tiktok.navigation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.Test;

class TabPolicyTest {
    /** Mirrors the public accessor TikTok's tab models expose. */
    public static final class FakeTab {
        private final String tag;

        FakeTab(String tag) {
            this.tag = tag;
        }

        public String tag() {
            return tag;
        }
    }

    private static List<String> tags(List<?> tabs) {
        List<String> result = new ArrayList<>();
        for (Object tab : tabs) {
            result.add(((FakeTab) tab).tag());
        }
        return result;
    }

    @Test
    void bottomBarKeepsOnlyCreateInboxProfile() {
        List<FakeTab> tabs = new CopyOnWriteArrayList<>(Arrays.asList(
                new FakeTab("HOME"), new FakeTab("FRIENDS"), new FakeTab("PUBLISH"),
                new FakeTab("NOTIFICATION"), new FakeTab("USER"), new FakeTab("SHOP")));

        List<?> filtered = TabPolicy.filterBottomTabs(tabs);

        assertEquals(Arrays.asList("PUBLISH", "NOTIFICATION", "USER"), tags(filtered));
        assertEquals(CopyOnWriteArrayList.class, filtered.getClass());
    }

    @Test
    void unknownBottomTabsAreDropped() {
        List<FakeTab> tabs = Arrays.asList(new FakeTab("inbox"), new FakeTab("NEW_VIDEO_TAB"), new FakeTab(null));
        assertEquals(Arrays.asList("inbox"), tags(TabPolicy.filterBottomTabs(tabs)));
    }

    @Test
    void unreadableBottomTabsKeepOriginalList() {
        List<Object> tabs = Arrays.asList(new Object(), new Object());
        assertSame(tabs, TabPolicy.filterBottomTabs(tabs));
    }

    @Test
    void topBarIsReducedToOneTab() {
        List<FakeTab> tabs = new ArrayList<>(Arrays.asList(
                new FakeTab("homepage_hot"), new FakeTab("homepage_follow"), new FakeTab("homepage_friends")));
        assertEquals(Arrays.asList("homepage_hot"), tags(TabPolicy.filterTopTabs(tabs)));
    }

    @Test
    void nullListsPassThrough() {
        assertNull(TabPolicy.filterBottomTabs(null));
        assertNull(TabPolicy.filterTopTabs(null));
    }

    @Test
    void allowedKeysAreCaseInsensitive() {
        assertEquals(TabPolicy.INBOX, TabPolicy.allowedBottomKey(" Message "));
        assertEquals(TabPolicy.PROFILE, TabPolicy.allowedBottomKey("me"));
        assertEquals(TabPolicy.CREATE, TabPolicy.allowedBottomKey("plus"));
        assertNull(TabPolicy.allowedBottomKey("homepage_home"));
    }
}
