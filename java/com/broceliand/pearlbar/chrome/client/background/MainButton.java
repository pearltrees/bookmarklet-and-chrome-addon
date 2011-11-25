package com.broceliand.pearlbar.chrome.client.background;

import com.google.gwt.user.client.*;

public class MainButton {

    private static boolean go = false;
    private static int animPhase = 0;
    private static String currentIcon = null;

    static void init() {
        exports();
    }

    static void nextPhase() {
        animPhase++;
        switch (animPhase) {
        case 1:
        case 7:
            setIcon("pearl-2");
            break;
        case 2:
        case 6:
            setIcon("pearl-3");
            break;
        case 3:
        case 5:
            setIcon("pearl-4");
            break;
        case 4:
            setIcon("pearl-5");
            break;
        default:
            animPhase = 0;
        }

        if (animPhase == 0) {
            setIcon(go, false);
        }
        else {
            Timer t = new Timer() {

                @Override
                public void run() {
                    nextPhase();
                }
            };
            t.schedule(200);
        }
    }

    /**
     * Used in JS.
     */
    static void setIcon(boolean forceGo, boolean startAnim) {
        go = forceGo;
        if (animPhase == 0) {
            if (startAnim) {
                nextPhase();
            }
            else {
                setIcon(forceGo || !AppState.isPearlable() ? "go" : "in");
            }
        }
    }

    private static void setIcon(String name) {
        if (!name.equals(currentIcon)) {
            setBrowserActionIcon("image/button/" + name + ".png");
            currentIcon = name;
        }
    }

    private static native void setBrowserActionIcon(String iconPath) /*-{
        chrome.browserAction.setIcon({path: chrome.extension.getURL(iconPath)});
    }-*/;

    private static native void exports() /*-{
        $wnd.setButtonIcon = $entry(@com.broceliand.pearlbar.chrome.client.background.MainButton::setIcon(ZZ));
    }-*/;
}
