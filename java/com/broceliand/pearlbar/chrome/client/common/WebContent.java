package com.broceliand.pearlbar.chrome.client.common;



public class WebContent {

    public static boolean bookmarklet;
    public static boolean onIpad;
    public static boolean cookieEnabled;

    public enum PearltreesState {
        Normal,
        Tunnel,
        GetStarted,
        Settings,
        BookmarkletTunnel
    }

    public static boolean isInPearltrees(String url) {
        return url != null && url.startsWith(getPearltreesUrl());
    }

    public static PearltreesState getPearltreesState(String url) {
        if (!isInPearltrees(url)) {
            return null;
        }

        if (url.indexOf("collectorbookmark/ipad") >= 0) {
            return PearltreesState.BookmarkletTunnel;
        }
        int anchor = getAnchorStart(url);
        if (anchor < 0) {
            return null;
        }
        if (url.indexOf("DP-n=settings", anchor) >= 0) {
            return PearltreesState.Settings;
        }
        if (url.indexOf("DP-n=tunnel", anchor) >= 0) {
            return PearltreesState.Tunnel;
        }
        if (url.indexOf("DP-n=gettingStarted", anchor) >= 0) {
            return PearltreesState.GetStarted;
        }
        return null;
    }

    public static String getUnanchoredUrl(String url) {
        int anchor = getAnchorStart(url);
        return anchor < 0 ? url : url.substring(0, anchor);
    }

    public static String getCurrentTree(String ptUrl) {
        int anchor = getAnchorStart(ptUrl);

        String param = parseAnchorParam(ptUrl, anchor, "N-s=1_");
        if (param != null) {
            return param;
        }
        return parseAnchorParam(ptUrl, anchor, "N-f=1_");
    }

    private static String parseAnchorParam(String ptUrl, int anchor, String start) {
        if (anchor < 0) {
            return null;
        }
        int i = ptUrl.indexOf(start, anchor);
        if (i < 0) {
            return null;
        }
        i += start.length();
        int pos = i;
        for (; i < ptUrl.length(); i++) {
            char c = ptUrl.charAt(i);
            if (c < '0' || c > '9') {
                break;
            }
        }
        return pos == i ? null : ptUrl.substring(pos, i);
    }

    private static int getAnchorStart(String url) {
        return url.indexOf('#');
    }

    public static boolean isWebUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }

    public static native void openInNewTab(String href) /*-{
        $wnd.openInNewTab(href);
    }-*/;

    public static native String getPearltreesUrl() /*-{
        return $wnd.PEARLTREES_URL;
    }-*/;

    public static native String getCDNLocations() /*-{
    return $wnd.CDNAVATAR_URL;
    }-*/;

    public static native String getCollectorUrl() /*-{
        return $wnd.COLLECTOR_URL;
    }-*/;

    private static native void navigateTo(int tabId, String toUrl) /*-{
        chrome.tabs.update(tabId, toUrl ? {url: toUrl, selected: true} : {selected: true});
    }-*/;
}
