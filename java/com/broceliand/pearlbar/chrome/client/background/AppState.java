package com.broceliand.pearlbar.chrome.client.background;

import java.util.*;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.broceliand.pearlbar.chrome.client.common.AppClient.PearlClient;
import com.google.gwt.core.client.*;

public class AppState {

    private static final String HAS_RUN_FIRST_TIME_KEY = "hasRunFirstTime";
    private static final String HAS_AUTH_FIRST_TIME_KEY = "hasAuthFirstTime";

    private static UserTrees userTrees;
    static Date lastPearled = null;
    static boolean hasRevealed = false;
    private static String selectedTree = null;
    private static String selectedAsso = null;
    static boolean isWebUrl;
    static boolean isInPearltrees;

    static void init() {
        exports();
        BrowserTab.init();
        MainButton.init();
        userTrees = UserTrees.init();
        UserTrees.refreshTrees(false);
        checkFirstRun();
    }

    static void setUserTrees(JavaScriptObject jsut) {
        UserTrees ut = new UserTrees(jsut);
        boolean hasChanged = userTrees.hasChanged(ut);
        userTrees = ut;
        // Notify
        if (hasChanged || getInLocalStorage(HAS_AUTH_FIRST_TIME_KEY) == null) {
            //Alerts.alert("setUserTrees: " + userTrees);
            if (userTrees.getStatus() == UserTrees.LOADED) {
                onPopupRefresh(true);
            }
            onUserTreesChanged();
        }
    }

    private static native void onUserTreesChanged() /*-{
        var viewUrl = chrome.extension.getURL('popup.html');
        var views = chrome.extension.getViews();
        for (var i = 0; i < views.length; i++) {
            var view = views[i];
            if (view.location.href == viewUrl) {
                view.onUserTreesChanged();
            }
        }
    }-*/;

    /**
     * Used in JS.
     */
    static JavaScriptObject getUserTreesData() {
        return userTrees.getData();
    }

    /**
     * Used in JS.
     */
    static String getSelectedTree() {
        return selectedTree;
    }

    /**
     * Used in JS.
     */
    static String getSelectedAsso() {
        return selectedAsso;
    }

    /**
     * Used in JS.
     */
    static void setSelectedTree(String id, String assoId) {
        selectedTree = id;
        selectedAsso = assoId;
    }

    /**
     * Used in JS.
     */
    static void openInNewTab(String href) {
        //Alerts.alert("open");
        hasRevealed |= WebContent.isInPearltrees(href);
        createTab(href);
    }

    /**
     * Used in JS.
     */
    static void reveal(String treeId, String assoId, int tabId, String url) {
        hasRevealed = true;
        lastPearled = null;
        String hash = AppClient.buildRevealHash(userTrees, treeId, assoId);

        if (WebContent.isInPearltrees(url)) {
            // Current tab is pt, use it
            if (hash != null) {
                navigateTo(tabId, WebContent.getUnanchoredUrl(url) + '#' + hash);
            }
            return;
        }

        BrowserTab t = BrowserTab.getPearltreesTab();
        if (t == null) {
            // No pt tab, create new
            createTab(WebContent.getPearltreesUrl() + (hash == null ? "" : '#' + hash));
            return;
        }

        // Select existing pt tab
        //TODO Focus its window (no api yet)
        navigateTo(t.getId(), hash == null ? null : WebContent.getUnanchoredUrl(t.getUrl()) + '#' + hash);
    }

    /**
     * Used in JS.
     */
    static void goToAccountSync(int tabId, String url) {

        if (WebContent.isInPearltrees(url)) {
            // Current tab is pt, use it
            navigateTo(tabId, WebContent.getUnanchoredUrl(url) + "#/DP-n=socialSync");
            return;
        }

        BrowserTab t = BrowserTab.getPearltreesTab();
        if (t == null) {
            // No pt tab, create new
            createTab(WebContent.getPearltreesUrl() + "#/DP-n=socialSync");
            return;
        }

        // Select existing pt tab
        //TODO Focus its window (no api yet)
        navigateTo(t.getId(), WebContent.getUnanchoredUrl(t.getUrl()) + "#/DP-n=socialSync");
    }

    private static native void navigateTo(int tabId, String toUrl) /*-{
        chrome.tabs.update(tabId, toUrl ? {url: toUrl, selected: true} : {selected: true});
    }-*/;

    private static native void createTab(String toUrl) /*-{
        chrome.tabs.create({url: toUrl, selected: true});
    }-*/;

    /**
     * Used in JS.
     */
    static void pearlContent(int tab, String url, String title, String treeId, String parentTreeId, String newTreeName) {
        lastPearled = new Date();
        AppClient.pearlContent(url, title, treeId, parentTreeId, newTreeName, new PearlClient() {

            @Override
            public boolean resetRevealed() {
                if (hasRevealed) {
                    hasRevealed = false;
                    return true;
                }
                return false;
            }

            @Override
            public void onError(String msg) {
                Alerts.alert(msg);
            }

            @Override
            public void onPearled(String newTreeId) {
                if (newTreeId != null) {
                    setSelectedTree(newTreeId, "");
                    UserTrees.refreshTrees(true);
                }
            }
        });
    }

    static boolean isPearlable() {
        return isWebUrl && !isInPearltrees;
    }

    private static native String getInLocalStorage(String key) /*-{
        return localStorage[key];
    }-*/;

    private static native void setInLocalStorage(String key, String value) /*-{
        localStorage[key] = value;
    }-*/;

    /**
     * Used in JS.
     */
    static void onPopupRefresh(boolean login) {
        if (getInLocalStorage(HAS_AUTH_FIRST_TIME_KEY) == null) {
            if (login) {
                setInLocalStorage(HAS_AUTH_FIRST_TIME_KEY, "1");
                //Alerts.alert("Login!");
                return;
            }
            //openCreateAccount();
        }
    }

    private static void checkFirstRun() {
        if (getInLocalStorage(HAS_RUN_FIRST_TIME_KEY) == null) {
            setInLocalStorage(HAS_RUN_FIRST_TIME_KEY, "1");
            //Alerts.alert("run first time");
            openCreateAccount();
        }
    }

    private static void openCreateAccount() {
        AppState.createTab(WebContent.getPearltreesUrl() + "s/collectorChrome/createAccount");
    }

    private static native void exports() /*-{
        $wnd.getBackgroundUserTreesData = $entry(@com.broceliand.pearlbar.chrome.client.background.AppState::getUserTreesData());
        $wnd.setUserTrees = $entry(@com.broceliand.pearlbar.chrome.client.background.AppState::setUserTrees(Lcom/google/gwt/core/client/JavaScriptObject;));
        $wnd.pearlContent = $entry(@com.broceliand.pearlbar.chrome.client.background.AppState::pearlContent(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
        $wnd.backgroundReveal = $entry(@com.broceliand.pearlbar.chrome.client.background.AppState::reveal(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;));
        $wnd.goToAccountSync = $entry(@com.broceliand.pearlbar.chrome.client.background.AppState::goToAccountSync(ILjava/lang/String;));
        $wnd.backgroundOpenInNewTab = $entry(@com.broceliand.pearlbar.chrome.client.background.AppState::openInNewTab(Ljava/lang/String;));
        $wnd.getBackgroundSelectedTree = $entry(@com.broceliand.pearlbar.chrome.client.background.AppState::getSelectedTree());
        $wnd.setBackgroundSelectedTree = $entry(@com.broceliand.pearlbar.chrome.client.background.AppState::setSelectedTree(Ljava/lang/String;Ljava/lang/String;));
        $wnd.onPopupRefresh = $entry(@com.broceliand.pearlbar.chrome.client.background.AppState::onPopupRefresh(Z));

        chrome.extension.onRequest.addListener(
            function(request, sender, sendResponse) {
                if (request.login) {
                    $wnd.refreshTrees(false);
                }
            });
    }-*/;
}
