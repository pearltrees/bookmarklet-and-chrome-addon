package com.broceliand.pearlbar.chrome.client.popup;

import java.util.*;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.broceliand.pearlbar.chrome.client.common.WebContent.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

public class PopupControl {

    private static UserTrees userTrees;
    public static boolean panelScrollableExist;
    public static int height;
    public static int width;
    private static PopupUIs currentUI = PopupUIs.Loading;

    public static enum PopupUIs {
        Error,
        Loading,
        Login,
        LoginFailed,
        LoginSuccessfull,
        InTunnel,
        InSettings,
        InBookmarkletTunnel,

        TreeList,
        NewTree,
        ChooseParent,
        Pearled,
        PearledDirectly,
        TreeCreated,
        BackToList
    }

    public static void newScroll(String id) {
        setNewScroll(id);
    }

    public static void refreshScroll() {
        refresh();
    }

    public static void onModuleLoad() {
        //Alerts.alert("pop");
        Window.addCloseHandler(new CloseHandler<Window>() {

            @Override
            public void onClose(CloseEvent<Window> event) {
                Alerts.setButtonIcon(false, false);
            }
        });
        exports();
        userTrees = new UserTrees(UserTrees.getUserTreesData());
        UserTrees.backgroundRefreshTrees(true);
        if (!WebContent.onIpad) {
            retrieveSize();
        } else {
            retrieveSizeForIpad();
        }
        //Decide quickly if the URL is pearlable for the bookmarklet
        if (WebContent.bookmarklet) {
            getFirstDetails();
            PearltreesState s = WebContent.getPearltreesState(TreeListUI.currentUrl);
            if (s == PearltreesState.Settings) {
                PopupControl.changeUI(PopupUIs.InSettings);
                return;
            }
            else if (s == PearltreesState.Tunnel || s == PearltreesState.GetStarted) {
                PopupControl.changeUI(PopupUIs.InTunnel);
                return;
            }
            else if (s == PearltreesState.BookmarkletTunnel) {
                PopupControl.changeUI(PopupUIs.InBookmarkletTunnel);
                return;
            }
        }
        makeUI(false);
    }

    public static void changeUI(PopupUIs ui) {
        changeUI(ui, false);
    }

    public static void changeUI(PopupUIs ui, boolean dueToTreesChange) {
        cleanUI();
        currentUI = ui;
        makeUI(dueToTreesChange);
    }

    private static void cleanUI() {
        RootPanel.get().clear();
    }

    public static void onResize() {
        boolean noGoBackBar = (TreeListUI.chooseParent || TreeListUI.goDirectly);
        resize(noGoBackBar, PopupControl.panelScrollableExist);
    }

    private static void makeUI(boolean dueToTreesChange) {
        if (currentUI == PopupUIs.InTunnel || currentUI == PopupUIs.InSettings || currentUI == PopupUIs.InBookmarkletTunnel) {
            InactiveUI.init(RootPanel.get(), currentUI);
            return;
        }
        if (currentUI == PopupUIs.Pearled && TreeListUI.PearledDirect) {
            PearledUI.init(RootPanel.get(), false, TreeListUI.isPearlable);
            return;
        }

        switch (userTrees.getStatus()) {
        case UserTrees.NOT_LOADED:
            currentUI = PopupUIs.Loading;
            if (WebContent.onIpad) {
                LoadingUI.init(RootPanel.get(), userTrees);
            }
            break;
        case UserTrees.LOADED:
            onPopupRefresh(true);
            switch (currentUI) {
            case NewTree:
                NewTreeUI.init(RootPanel.get());
                break;
            case TreeCreated:
                if (WebContent.bookmarklet) {
                    PearledUI.init(RootPanel.get(), true, TreeListUI.isPearlable);
                }
                else {
                    TreeCreatedUI.init(RootPanel.get());
                }
                break;
            case Pearled:
                PearledUI.init(RootPanel.get(), false, TreeListUI.isPearlable);
                break;
            case ChooseParent:
                TreeListUI.chooseParent = true;
                TreeListUI.init(RootPanel.get(), userTrees);
                break;
            default:
                TreeListUI.chooseParent = false;
                currentUI = PopupUIs.TreeList;
                TreeListUI.init(RootPanel.get(), userTrees);
            }
            break;
        case UserTrees.LOGOUT:
            if (dueToTreesChange) {
                onPopupRefresh(false);
            }
            if (currentUI == PopupUIs.LoginFailed) {
                LoginFailedUI.init(RootPanel.get());
            } else if (currentUI == PopupUIs.LoginSuccessfull && WebContent.onIpad) {
                LoadingUI.init(RootPanel.get(), userTrees);
            } else {
                if (!WebContent.cookieEnabled && WebContent.onIpad) {
                    currentUI = PopupUIs.Error;
                    ErrorUI.init(RootPanel.get(), userTrees);
                } else {
                    currentUI = PopupUIs.Login;
                    LoginUI.init(RootPanel.get(), userTrees);
                }
            }
            break;
        default:
            currentUI = PopupUIs.Error;
            ErrorUI.init(RootPanel.get(), userTrees);
        }
    }

    /**
     * Used in JS.
     */
    public static void onUserTreesChanged() {
        userTrees = new UserTrees(UserTrees.getUserTreesData());

        if (userTrees.getStatus() == UserTrees.LOADED) {
            switch (currentUI) {
            case NewTree:
            case TreeCreated:
            case Pearled:
            case InTunnel:
            case InSettings:
                return;
            case TreeList:
            case ChooseParent:
                if (TreeListUI.isClosing) {
                    return;
                }
                break;
            }
        }

        changeUI(currentUI, true);
    }

    static void onViewCreated() {
        Iterator<Widget> it = RootPanel.get().iterator();
        if (it.hasNext()) {
            it.next(); // Keep first child
            while (it.hasNext()) {
                it.next();
                it.remove(); // Kill its siblings
            }
        }

        Alerts.setButtonIcon(currentUI == PopupUIs.TreeList && TreeListUI.isGoMode(), false);
    }

    /**
     * Used in JS.
     */

    public static void onFirstDetails(String url, String title) {
        TreeListUI.currentUrl = url;
        TreeListUI.currentTitle = title;
    }

    public static native void getFirstDetails() /*-{
        $wnd.getFirstDetails($entry(@com.broceliand.pearlbar.chrome.client.popup.PopupControl::onFirstDetails(Ljava/lang/String;Ljava/lang/String;)));
    }-*/;

    static void onSize(int h,int w) {
        if (!WebContent.onIpad) {
            RootPanel.get().setHeight(h + "px");
            if (WebContent.bookmarklet) {
                RootPanel.get().setWidth("100%");
            }
            else {
                RootPanel.get().setWidth(w + "px");
            }
            height = h;
            width = w;
        }
    }

    public static native void setNewScroll(String id) /*-{
    $wnd.setTimeout(function () {
        myScroll = new iScroll(id);
    }, 0);
    }-*/;

    private static native void refresh() /*-{
    $wnd.setTimeout(function () {
        myScroll.refresh();
    }, 0);
    }-*/;

    private static native void onPopupRefresh(boolean login) /*-{
        $wnd.onPopupRefresh(login);
    }-*/;

    private static native void retrieveSize() /*-{
        $wnd.retrieveSize($entry(@com.broceliand.pearlbar.chrome.client.popup.PopupControl::onSize(II)));
    }-*/;

    public static native void resize(boolean noGoBackBar, boolean panelScrollableExist)  /*-{
    var windowHeight = window.innerHeight;
    var windowWidth = window.innerWidth;
//    alert("resize " + windowWidth + " x " + windowHeight);
    height = window.innerHeight;
    width = window.innerWidth;
    if (document.getElementById("panelScrollable") != null && document.getElementById("panelNonScrollable") != null) {
        if (noGoBackBar) {
            document.getElementById("panelScrollable").style.height = (windowHeight - 54) +"px";
            if (myScroll) {
            myScroll.refresh();
            }
        } else {
            document.getElementById("panelScrollable").style.height = (windowHeight - 126) +"px";
            if (myScroll) {
            myScroll.refresh();
            }
        }
    document.getElementById("panelNonScrollable").style.height = windowHeight +"px";
    document.getElementById("panelNonScrollable").style.width = windowWidth +"px";
    document.getElementById("panelScrollable").style.width = windowWidth +"px";

//    alert("panelNonScrollable " + document.getElementById("panelNonScrollable").style.width + " x " + document.getElementById("panelNonScrollable").style.height);
    }
    }-*/;

    private static native void retrieveSizeForIpad() /*-{
    $wnd.retrieveSizeForIpad($entry(@com.broceliand.pearlbar.chrome.client.popup.PopupControl::onSize(II)));
    }-*/;

    private static native void exports() /*-{
        $wnd.onUserTreesChanged = $entry(@com.broceliand.pearlbar.chrome.client.popup.PopupControl::onUserTreesChanged());
    }-*/;
}
