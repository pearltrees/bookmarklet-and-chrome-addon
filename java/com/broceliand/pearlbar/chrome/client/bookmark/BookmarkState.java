package com.broceliand.pearlbar.chrome.client.bookmark;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.broceliand.pearlbar.chrome.client.common.AppClient.PearlClient;
import com.broceliand.pearlbar.chrome.client.popup.*;
import com.broceliand.pearlbar.chrome.client.popup.PopupControl.PopupUIs;
import com.google.gwt.core.client.*;
import com.google.gwt.user.client.Window.Navigator;

public class BookmarkState {

    private static UserTrees userTrees;
    private static final String HAS_RUN_FIRST_TIME_KEY = "hasRunFirstTime";
    private static final String HAS_AUTH_FIRST_TIME_KEY = "hasAuthFirstTime";

    static void init() {
        WebContent.bookmarklet = true;
        WebContent.cookieEnabled = Navigator.isCookieEnabled();
        WebContent.onIpad = Navigator.getUserAgent().indexOf("iPad") != -1;
        //WebContent.onIpad = true;

        exports();
        userTrees = UserTrees.init();
        UserTrees.refreshTrees(false);
        checkFirstRun();
    }

    static void setUserTrees(JavaScriptObject jsut) {
        UserTrees ut = new UserTrees(jsut);
        boolean hasChanged = userTrees.hasChanged(ut);
        userTrees = ut;
        if (hasChanged || UserTrees.getInLocalStorage(HAS_AUTH_FIRST_TIME_KEY) == null) {
            //Alerts.alert("setUserTrees when changed: " + userTrees);
            //Alerts.alert("hasChanged " + hasChanged);
            //Alerts.alert("hasChanged " + UserTrees.getInLocalStorage(HAS_AUTH_FIRST_TIME_KEY));
            if (userTrees.getStatus() == UserTrees.LOADED) {
                onPopupRefresh(true);
            }
            PopupControl.onUserTreesChanged();
        }
    }

    static void resizeScrollablePanel() {
        PopupControl.onResize();
    }

    static JavaScriptObject getUserTreesData() {
        return userTrees.getData();
    }

    private static native String getUserTreesString() /*-{
        return $wnd.userTreesString;
    }-*/;

    /**
     * Used in JS.
     */
    static void reveal(String treeId, String assoId, int tabId, String url) {
        String hash = AppClient.buildRevealHash(userTrees, treeId, assoId);

        if (WebContent.isInPearltrees(url)) {
            // Current tab is pt, use it
            if (hash != null) {
                navigateTo(WebContent.getUnanchoredUrl(url) + '#' + hash);
            }
            return;
        }

        navigateTo(WebContent.getPearltreesUrl() + (hash == null ? "" : '#' + hash));
    }

    /**
     * Used in JS.
     */
    static void goToAccountSync(int tabId, String url) {

        if (WebContent.isInPearltrees(url)) {
            // Current tab is pt, use it
            navigateTo(WebContent.getUnanchoredUrl(url) + "#/DP-n=socialSync");
            return;
        }

        navigateTo(WebContent.getPearltreesUrl() + "#/DP-n=socialSync");
    }

    private static native void navigateTo(String toUrl) /*-{
        $wnd.openInNewTab(toUrl);
    }-*/;


    private static void openCreateAccount() {
        navigateTo(WebContent.getPearltreesUrl());
    }

    /**
     * Used in JS.
     */
    static void pearlContent(int tab, String url, String title, final String treeId, String parentTreeId, String newTreeName) {
        AppClient.pearlContent(url, title, treeId, parentTreeId, newTreeName, new PearlClient() {

            @Override
            public boolean resetRevealed() {
                return false;
            }

            @Override
            public void onError(String msg) {
                Alerts.alert(msg);
                if (WebContent.onIpad) {
                    Alerts.closeWindow();
                }
            }

            @Override
            public void onPearled(String newTreeId) {
                if (newTreeId != null) {
                    PopupControl.changeUI(PopupUIs.TreeCreated);
                }
                else {
                    PopupControl.changeUI(PopupUIs.Pearled);
                }
            }
        });
    }

    /**
     * Used in JS.
     */
    public static void onPopupRefresh(boolean login) {
        if (UserTrees.getInLocalStorage(HAS_AUTH_FIRST_TIME_KEY) == null) {
            if (login) {
                UserTrees.setInLocalStorage(HAS_AUTH_FIRST_TIME_KEY, "1");
                //Alerts.alert("Login!");
                return;
            }
        }
    }

    public static void checkFirstRun() {
        if (UserTrees.getInLocalStorage(HAS_RUN_FIRST_TIME_KEY) == null) {
            UserTrees.setInLocalStorage(HAS_RUN_FIRST_TIME_KEY, "1");
            //Alerts.alert("run first time");
            //openCreateAccount();
        }
    }

    static void onCredentials(String c) {
        if (c.equals(PopupElements.credentials)) {
            return;
        }
        PopupElements.credentials = c;
        PopupControl.onUserTreesChanged();
    }

    private static native void exports() /*-{
        $wnd.getUserTreesData = $entry(@com.broceliand.pearlbar.chrome.client.bookmark.BookmarkState::getUserTreesData());
        $wnd.setUserTrees = $entry(@com.broceliand.pearlbar.chrome.client.bookmark.BookmarkState::setUserTrees(Lcom/google/gwt/core/client/JavaScriptObject;));
        $wnd.pearlContent = $entry(@com.broceliand.pearlbar.chrome.client.bookmark.BookmarkState::pearlContent(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
        $wnd.reveal = $entry(@com.broceliand.pearlbar.chrome.client.bookmark.BookmarkState::reveal(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;));
        $wnd.goToAccountSync = $entry(@com.broceliand.pearlbar.chrome.client.bookmark.BookmarkState::goToAccountSync(ILjava/lang/String;));
        $wnd.onCredentials = $entry(@com.broceliand.pearlbar.chrome.client.bookmark.BookmarkState::onCredentials(Ljava/lang/String;));
        $wnd.resize = $entry(@com.broceliand.pearlbar.chrome.client.bookmark.BookmarkState::resizeScrollablePanel());
    }-*/;
}
