package app.unscroll.extension.tiktok.navigation;

import java.util.Locale;

/**
 * Bottom-bar tabs that may be shown (decisions P1 and P2).
 *
 * <p>Allow-list: only Create (+), Inbox and Profile. Every tab switch — including the
 * one TikTok performs at start-up to open Home — is redirected to Inbox when the
 * target is not allowed. Tabs unknown to this version of Unscroll are redirected too,
 * so a new feed tab added by a TikTok update cannot be opened.
 */
public final class TabPolicy {
    /** TikTok's internal tag of the Inbox tab. */
    public static final String INBOX_TAG = "NOTIFICATION";

    private TabPolicy() {
    }

    /** True when the TikTok bottom-tab tag is one Unscroll allows. */
    public static boolean isAllowedBottomTab(String tag) {
        if (tag == null) {
            return false;
        }
        switch (tag.trim().toUpperCase(Locale.US)) {
            case "NOTIFICATION": // Inbox
            case "USER":         // Profile
            case "PUBLISH":      // Create (+)
                return true;
            default:
                return false;
        }
    }

    /** Called from TikTok's tab switcher with the target tag; returns the tag to open. */
    public static String redirectTab(String tag) {
        if (tag == null || isAllowedBottomTab(tag)) {
            return tag;
        }
        return INBOX_TAG;
    }
}
