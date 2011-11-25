package com.broceliand.pearlbar.chrome.client.background;

import java.util.*;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.google.gwt.core.client.*;

public class BrowserTab {

    private static final Map<Integer, BrowserTab> tabs = new LinkedHashMap<Integer, BrowserTab>();

    private int id;
    private Date creation;
    private String url;

    static void init() {
        exports();
    }

    private static BrowserTab getById(Integer id) {
        BrowserTab t = tabs.get(id);
        if (t == null) {
            t = new BrowserTab();
            t.id = id;
            t.creation = new Date();
            tabs.put(id, t);
        }
        return t;
    }

    static BrowserTab getPearltreesTab() {
        for (BrowserTab t : tabs.values()) {
            if (WebContent.isInPearltrees(t.getUrl())) {
                return t;
            }
        }
        return null;
    }

    int getId() {
        return id;
    }

    String getUrl() {
        return url;
    }

    /**
     * Used in JS.
     */
    static void onRemoved(int tabId) {
        tabs.remove(tabId);
    }

    /**
     * Used in JS.
     */
    static void setCurrentTab(boolean isSelection, int tabId, String url, JavaScriptObject tab) {
        BrowserTab t = getById(tabId);
        t.url = url;
        /*if (isSelection) {
            Alerts.alert("select " + tabId);
        }*/
        //selectedTabId = tabId;
        AppState.isWebUrl = WebContent.isWebUrl(url);
        AppState.isInPearltrees = AppState.isWebUrl && WebContent.isInPearltrees(url);
        MainButton.setIcon(false, false);
        if (isSelection && AppState.isInPearltrees && AppState.getSelectedTree() != null && AppState.lastPearled != null && t.creation.before(AppState.lastPearled)) {
            AppState.reveal(AppState.getSelectedTree(), AppState.getSelectedAsso(), tabId, url);
        }
        else if (AppState.isInPearltrees) {
            String tree = WebContent.getCurrentTree(url);
            if (tree != null) {
                AppState.setSelectedTree(tree, "");
            }
        }
    }

    private static native void exports() /*-{
        function onUpdateTab(isSelection) {
            chrome.tabs.getSelected(null, function(tab) {
                $entry(@com.broceliand.pearlbar.chrome.client.background.BrowserTab::setCurrentTab(ZILjava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;))(isSelection, tab.id, tab.url, tab);
            });
        }

        chrome.windows.onFocusChanged.addListener(function(windowId) {
            onUpdateTab(true);
        });

        chrome.tabs.onSelectionChanged.addListener(function(tabId, selectInfo) {
            onUpdateTab(true);
        });

        chrome.tabs.onUpdated.addListener(function(tabId, changeInfo, tab) {
            onUpdateTab(false);
        });

        chrome.tabs.onCreated.addListener(function(tab) {
            onUpdateTab(false);
        });

        chrome.tabs.onRemoved.addListener(function(tabId) {
            $entry(@com.broceliand.pearlbar.chrome.client.background.BrowserTab::onRemoved(I))(tabId);
        });

        onUpdateTab(false);
    }-*/;
}
