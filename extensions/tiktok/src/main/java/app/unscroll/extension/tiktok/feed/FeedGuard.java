package app.unscroll.extension.tiktok.feed;

import java.lang.reflect.Field;
import java.util.ArrayList;

import app.unscroll.extension.tiktok.UnscrollLog;

/**
 * Empties feed responses before TikTok's UI sees them (decision P1).
 *
 * <p>Fields are written by reflection so that a renamed or re-typed field in a future
 * TikTok build fails loudly in the log instead of crashing the app; such a build is not
 * supported anyway because patch fingerprints are pinned to one version.
 */
public final class FeedGuard {
    private FeedGuard() {
    }

    /** Main feed (For You and other FeedApiService feeds): no items, no further pages. */
    public static void emptyFeedItemList(Object feedItemList) {
        if (feedItemList == null) {
            return;
        }
        setField(feedItemList, "items", new ArrayList<>());
        setIntIfPresent(feedItemList, "hasMore", 0);
    }

    /** Friends tab feed response. */
    public static void emptyFriendsFeedResponse(Object response) {
        if (response == null) {
            return;
        }
        setField(response, "friendFeedData", new ArrayList<>());
        setIntIfPresent(response, "hasMore", 0);
    }

    static boolean setField(Object target, String name, Object value) {
        Field field = findField(target.getClass(), name);
        if (field == null) {
            UnscrollLog.info("feed guard: field not found: " + name);
            return false;
        }
        try {
            field.setAccessible(true);
            field.set(target, value);
            return true;
        } catch (Throwable throwable) {
            UnscrollLog.error("feed guard set " + name, throwable);
            return false;
        }
    }

    static void setIntIfPresent(Object target, String name, int value) {
        Field field = findField(target.getClass(), name);
        if (field == null) {
            return;
        }
        try {
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (type == int.class) {
                field.setInt(target, value);
            } else if (type == boolean.class) {
                field.setBoolean(target, value != 0);
            } else if (type == long.class) {
                field.setLong(target, value);
            }
        } catch (Throwable throwable) {
            UnscrollLog.error("feed guard set " + name, throwable);
        }
    }

    private static Field findField(Class<?> type, String name) {
        for (Class<?> current = type; current != null && current != Object.class; current = current.getSuperclass()) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                // keep looking in the superclass
            }
        }
        return null;
    }
}
