package app.unscroll.extension.tiktok.navigation;

import java.util.Locale;

/**
 * Classifies links that open TikTok (web links and the app's own URL scheme).
 *
 * <p>Links to one specific video or photo post, to a profile, to chats, settings,
 * login/verification and creator tools are allowed. Links whose destination is a
 * feed (For You, Following, Friends, LIVE, hashtag, sound, search, discover) are
 * blocked.
 *
 * <p>This is a deny-list on purpose: an unknown link may be an account-security or
 * login flow, and blocking it could lock the owner out of the account. Feeds that
 * are reached anyway stay empty through the data-layer patches.
 */
public final class LinkPolicy {
    public enum Decision {
        /** Allowed, not a video or profile (inbox, settings, login, creator tools...). */
        ALLOW,
        /** One specific video or photo post. */
        VIDEO,
        /** A user profile. Playback from it follows the profile playback rule. */
        PROFILE,
        /** A feed or feed-like destination. Must not open. */
        BLOCK
    }

    /** First path segments (web) that lead to feeds. */
    private static final String[] BLOCKED_WEB_SEGMENTS = {
            "foryou", "following", "friends", "live", "tag", "music", "search",
            "discover", "explore", "channel", "trending", "topics", "shop", "find",
    };

    /** Hosts of the app scheme (snssdk1233:// and similar) that lead to feeds. */
    private static final String[] BLOCKED_SCHEME_HOSTS = {
            "feed", "home", "main", "following", "friends", "live", "webcast_room", "challenge",
            "music", "search", "discover", "explore", "hashtag", "trending", "nearby", "shop",
            "mall", "ecommerce", "playlist", "mix", "sound",
    };

    private LinkPolicy() {
    }

    public static Decision classify(String link) {
        if (link == null) {
            return Decision.ALLOW;
        }
        String value = link.trim();
        int schemeEnd = value.indexOf("://");
        if (schemeEnd <= 0) {
            return Decision.ALLOW;
        }
        String scheme = value.substring(0, schemeEnd).toLowerCase(Locale.US);
        String rest = value.substring(schemeEnd + 3);
        rest = cut(rest, '#');
        rest = cut(rest, '?');

        int slash = rest.indexOf('/');
        String host = (slash < 0 ? rest : rest.substring(0, slash)).toLowerCase(Locale.US);
        String path = slash < 0 ? "" : rest.substring(slash + 1);
        String[] segments = splitPath(path);

        if (scheme.equals("http") || scheme.equals("https")) {
            return classifyWeb(host, segments);
        }
        if (scheme.startsWith("snssdk") || scheme.equals("musically") || scheme.equals("tiktok")) {
            return classifyAppScheme(host, segments);
        }
        return Decision.ALLOW;
    }

    private static Decision classifyWeb(String host, String[] segments) {
        int colon = host.indexOf(':');
        if (colon >= 0) {
            host = host.substring(0, colon);
        }
        if (!(host.equals("tiktok.com") || host.endsWith(".tiktok.com"))) {
            return Decision.ALLOW;
        }
        // Short links (vm./vt.tiktok.com/XXXX, tiktok.com/t/XXXX) are resolved by TikTok's
        // servers to a full link; they are shared links to a single item.
        if (host.startsWith("vm.") || host.startsWith("vt.")) {
            return Decision.VIDEO;
        }
        if (segments.length == 0) {
            return Decision.BLOCK; // tiktok.com/ is the For You feed
        }
        String first = segments[0].toLowerCase(Locale.US);
        if (first.equals("t") && segments.length >= 2) {
            return Decision.VIDEO;
        }
        if (first.startsWith("@")) {
            if (segments.length >= 3) {
                String kind = segments[1].toLowerCase(Locale.US);
                if (kind.equals("video") || kind.equals("photo")) {
                    return Decision.VIDEO;
                }
                if (kind.equals("live")) {
                    return Decision.BLOCK;
                }
            }
            if (segments.length >= 2 && segments[1].equalsIgnoreCase("live")) {
                return Decision.BLOCK;
            }
            return Decision.PROFILE;
        }
        if (first.equals("v") && segments.length >= 2) {
            return Decision.VIDEO; // tiktok.com/v/<id>.html
        }
        if (first.equals("embed") || first.equals("player")) {
            return Decision.VIDEO;
        }
        for (String blocked : BLOCKED_WEB_SEGMENTS) {
            if (first.equals(blocked)) {
                return Decision.BLOCK;
            }
        }
        return Decision.ALLOW;
    }

    private static Decision classifyAppScheme(String host, String[] segments) {
        String first = segments.length > 0 ? segments[0].toLowerCase(Locale.US) : "";
        if (host.equals("aweme") && first.equals("detail")) {
            return Decision.VIDEO;
        }
        if (host.equals("user") && first.equals("profile")) {
            return Decision.PROFILE;
        }
        for (String blocked : BLOCKED_SCHEME_HOSTS) {
            if (host.equals(blocked)) {
                return Decision.BLOCK;
            }
        }
        if (host.equals("aweme") && (first.equals("feed") || first.equals("list"))) {
            return Decision.BLOCK;
        }
        return Decision.ALLOW;
    }

    private static String cut(String value, char marker) {
        int index = value.indexOf(marker);
        return index < 0 ? value : value.substring(0, index);
    }

    private static String[] splitPath(String path) {
        String trimmed = path;
        while (trimmed.startsWith("/")) {
            trimmed = trimmed.substring(1);
        }
        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed.isEmpty() ? new String[0] : trimmed.split("/+");
    }
}
