package app.unscroll.extension.tiktok.navigation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.unscroll.extension.tiktok.UnscrollLog;

/**
 * Decides which navigation tabs survive.
 *
 * <p>Bottom bar: allow-list. Only Create (+), Inbox and Profile are kept; Home,
 * Friends, Shop and every tab unknown to this version of Unscroll are removed, so a
 * new feed tab added by a TikTok update does not appear silently.
 *
 * <p>Top bar (the tabs above the Home feed): every tab is a video feed, so none is
 * allowed. TikTok needs at least one top tab to build the Home page, so if the list
 * would become empty the first tab is kept; its content is emptied by the feed
 * data-layer patches and the Home page itself is unreachable from the bottom bar.
 */
public final class TabPolicy {
    public static final String CREATE = "CREATE";
    public static final String INBOX = "INBOX";
    public static final String PROFILE = "PROFILE";

    private TabPolicy() {
    }

    /** Maps a raw TikTok bottom-tab tag to an allowed key, or null when the tab is not allowed. */
    public static String allowedBottomKey(String rawTag) {
        if (rawTag == null) {
            return null;
        }
        switch (rawTag.trim().toLowerCase(Locale.US)) {
            case "publish":
            case "create":
            case "plus":
                return CREATE;
            case "notification":
            case "inbox":
            case "message":
                return INBOX;
            case "user":
            case "profile":
            case "me":
                return PROFILE;
            default:
                return null;
        }
    }

    /** Called from patched TikTok code with the bottom tab models. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<?> filterBottomTabs(List<?> tabs) {
        if (tabs == null) {
            return null;
        }
        List kept = new ArrayList();
        for (Object tab : tabs) {
            if (allowedBottomKey(tagOf(tab)) != null) {
                kept.add(tab);
            }
        }
        if (kept.isEmpty()) {
            // Tags could not be read: TikTok changed its tab model. Keeping the original list
            // avoids an unusable app; feeds stay empty through the data-layer patches.
            UnscrollLog.info("bottom tabs: no known tab tag, keeping original list");
            return tabs;
        }
        return copyLike(tabs, kept);
    }

    /** Called from patched TikTok code with the top (Home feed) tab models. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<?> filterTopTabs(List<?> tabs) {
        if (tabs == null || tabs.size() <= 1) {
            return tabs;
        }
        List kept = new ArrayList();
        kept.add(tabs.get(0));
        return copyLike(tabs, kept);
    }

    /** Reads the tab tag through TikTok's public accessor. */
    static String tagOf(Object tab) {
        if (tab == null) {
            return null;
        }
        String tag = invokeString(tab, "tag");
        return tag != null ? tag : invokeString(tab, "getTag");
    }

    private static String invokeString(Object target, String name) {
        try {
            Method method = target.getClass().getMethod(name);
            Object value = method.invoke(target);
            return value instanceof String ? (String) value : null;
        } catch (NoSuchMethodException ignored) {
            return null;
        } catch (Throwable throwable) {
            UnscrollLog.error("tab tag", throwable);
            return null;
        }
    }

    /** Returns a list of the same mutability class as TikTok's original where possible. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List copyLike(List<?> original, List kept) {
        try {
            List copy = (List) original.getClass().getDeclaredConstructor().newInstance();
            copy.addAll(kept);
            return copy;
        } catch (Throwable ignored) {
            return kept;
        }
    }
}
