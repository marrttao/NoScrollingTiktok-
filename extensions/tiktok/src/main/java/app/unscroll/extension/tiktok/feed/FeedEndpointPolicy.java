package app.unscroll.extension.tiktok.feed;

import java.util.Locale;

/**
 * Network layer of decision P1/P8/P9/P11: API paths that return feeds of videos.
 *
 * <p>Every TikTok API request built through Retrofit passes through
 * {@link #filterUrl(String)}. Requests to a feed endpoint are redirected to an
 * unreachable local address, so they fail immediately and no feed content is
 * downloaded, whatever UI path asked for it.
 *
 * <p>Paths are TikTok's public API routes; they are much more stable across app
 * versions than obfuscated method names. The list is checked against TikTok 47.4.3.
 *
 * <p>Deliberately <b>not</b> blocked: single video detail ({@code /aweme/v1/aweme/detail/},
 * {@code /aweme/v1/multi/aweme/detail/}), profile video lists ({@code /aweme/v1/aweme/post/},
 * subject to the profile playback rule in the UI), account search
 * ({@code /aweme/v1/discover/search/}), messaging, music search used by the editor, and
 * everything related to publishing.
 */
public final class FeedEndpointPolicy {
    /** Where blocked requests are sent: the discard port on localhost, refused immediately. */
    public static final String BLOCKED_URL = "https://127.0.0.1:9/unscroll-blocked-feed";

    /**
     * Path prefixes. A prefix ending in '/' matches itself and anything below it; a prefix
     * without a trailing '/' matches only the exact path or the path followed by '/'.
     */
    static final String[] BLOCKED_PATHS = {
            // For You / main feed (also used to "load more" after a single video)
            "/aweme/v1/feed",
            "/aweme/v2/feed",
            // Following feed
            "/aweme/v2/follow/feed",
            "/aweme/v1/following/interest/feed",
            // Friends feed
            "/tiktok/v1/friend/friend_feed",
            "/tiktok/v1/friend/post_list",
            "/tiktok/v2/mutual_feed/item_list",
            // Explore, Nearby, STEM, landscape, offline and other video feeds
            "/tiktok/feed/",
            "/tiktok/v1/upvote/feed",
            "/tiktok/v1/rss/feed",
            "/tiktok/v1/skylight/feed",
            "/tiktok/v2/repost/feed",
            "/tiktok/v1/thought/feed",
            "/tiktok/v2/thought/feed",
            "/tiktok/v2/share/feed",
            "/tiktok/v1/paid_content/mini_drama/",
            "/tiktok/md/",
            "/api/v1/recommend/video_in_stream/",
            // Shop / mall feeds
            "/api/v1/mall/",
            "/api/v1/shop/recommend/feed",
            // LIVE feeds
            "/webcast/feed/",
            "/webcast/game_feed/",
            // Video results of search (account search stays: /aweme/v1/discover/search/)
            "/aweme/v1/search/item/",
            "/aweme/v1/search/loadmore/",
            "/aweme/v2/search/loadmore/",
            "/aweme/v1/search/stream/",
            "/aweme/v1/search/photo/",
            "/aweme/v1/search/horizontal/",
            "/aweme/v1/general/search/",
            "/aweme/v2/search/general/",
            "/aweme/v1/live/search/",
            "/aweme/v1/challenge/search/",
            "/aweme/v1/tako/api/video/search/",
            // Hashtag, sound and playlist video lists
            "/aweme/v1/challenge/aweme/",
            "/aweme/v1/music/aweme/",
            "/tiktok/v1/music/aweme/list/",
            "/tiktok/v1/mix/videos/",
    };

    private FeedEndpointPolicy() {
    }

    /** Returns {@link #BLOCKED_URL} for feed endpoints, otherwise {@code url} unchanged. */
    public static String filterUrl(String url) {
        try {
            if (url != null && isBlockedPath(pathOf(url))) {
                return BLOCKED_URL;
            }
        } catch (Throwable ignored) {
            // Never break a request because of the guard itself.
        }
        return url;
    }

    static boolean isBlockedPath(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        String p = path.toLowerCase(Locale.US);
        while (p.contains("//")) {
            p = p.replace("//", "/");
        }
        for (String blocked : BLOCKED_PATHS) {
            if (blocked.endsWith("/")) {
                if (p.startsWith(blocked) || p.equals(blocked.substring(0, blocked.length() - 1))) {
                    return true;
                }
            } else if (p.equals(blocked) || p.startsWith(blocked + "/")) {
                return true;
            }
        }
        return false;
    }

    /** Path component of an absolute or relative URL, without query or fragment. */
    static String pathOf(String url) {
        String rest = url;
        int scheme = rest.indexOf("://");
        if (scheme >= 0) {
            rest = rest.substring(scheme + 3);
            int slash = rest.indexOf('/');
            rest = slash < 0 ? "/" : rest.substring(slash);
        } else if (!rest.startsWith("/")) {
            rest = "/" + rest;
        }
        int query = rest.indexOf('?');
        if (query >= 0) {
            rest = rest.substring(0, query);
        }
        int fragment = rest.indexOf('#');
        if (fragment >= 0) {
            rest = rest.substring(0, fragment);
        }
        return rest;
    }
}
