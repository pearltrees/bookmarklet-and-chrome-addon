package com.broceliand.pearlbar.chrome.client.common;

import com.google.gwt.core.client.*;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.*;

public class Alerts {

    public static void alert(String arg) {
        Window.alert(arg);
    }
    public static native void log(String arg) /*-{
        console.log(arg);
    }-*/;

    public static native void closeWindow() /*-{
        $wnd.close();
    }-*/;

    public static native int getTabId(JavaScriptObject tab) /*-{
        return tab.id;
    }-*/;

    public static String getUserMessage(Throwable e) {
        //return "Request error: " + e.getMessage();
        return getUserMessage(0);
    }

    public static String getUserMessage(int statusCode) {
        if (statusCode == Response.SC_SERVICE_UNAVAILABLE) {
            return "Pearltrees is currently unavailable. Please retry later.";
        }
        if (statusCode == Response.SC_SEE_OTHER) {
            return "Please enable cookies to use the bookmarklet.";
        }
        return "Pearltrees is currently unreachable. Please check your internet connection or retry later.";
    }

    public static String getUserMessageForInvalid() {
        return "The Pearltrees server sent an invalid response. If the problem persists, please install the latest version of the addon, or send a mail to contact@pearltrees.com";
    }

    public static String getUserMessageForMalformed() {
        return "The Pearltrees server sent a malformed response. If the problem persists, please install the latest version of the addon, or send a mail to contact@pearltrees.com";
    }

    public static native void setButtonIcon(boolean forceGo, boolean startAnim) /*-{
        $wnd.setButtonIcon(forceGo, startAnim);
    }-*/;

    public static String escapeHtml(String maybeHtml) {
        final Element div = DOM.createDiv();
        DOM.setInnerText(div, maybeHtml);
        return DOM.getInnerHTML(div);
     }
}
