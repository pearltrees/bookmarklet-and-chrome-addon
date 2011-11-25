package com.broceliand.pearlbar.chrome.client.common;

import java.util.*;

import com.google.gwt.core.client.*;
import com.google.gwt.http.client.*;

public class UserTrees {

    public static final int NOT_LOADED = 0;
    public static final int LOADED = 1;
    public static final int LOGOUT = 2;
    public static final int ERROR = 3;

    public static final int DROPZONE_INDEX = 0;
    public static final int ROOTTREE_INDEX = 1;

    private static final int MAX_PEARLS = 100;

    private static final String USER_TREE_STORED = "userTreeStored";

    private final JavaScriptObject data;

    public UserTrees(JavaScriptObject d) {
        data = d;
    }

    public JavaScriptObject getData() {
        return data;
    }

    public int getStatus() {
        return getStatus(data);
    }

    public String getMessage() {
        return getMessage(data);
    }

    private static native int getStatus(JavaScriptObject data) /*-{
        return data.status;
    }-*/;

    private static native String getMessage(JavaScriptObject data) /*-{
        return data.message;
    }-*/;

    public String getUserId() {
        return getUserId(data);
    }

    private static native String getUserId(JavaScriptObject data) /*-{
        return data.currentUser.userID;
    }-*/;

    public int getTreeCount() {
        return getTreeCount(data);
    }

    private static native int getTreeCount(JavaScriptObject data) /*-{
        return data.treeList.length;
    }-*/;

    private static native JavaScriptObject getTree(JavaScriptObject data, int i) /*-{
        return data.treeList[i];
    }-*/;

    public String getTreeId(int i) {
        return getTreeId(getTree(data, i));
    }

    private static native String getTreeId(JavaScriptObject tree) /*-{
        return tree.treeID;
    }-*/;

    public String getTreeTitle(int i) {
        return getTreeTitle(getTree(data, i));
    }

    private static native String getTreeTitle(JavaScriptObject tree) /*-{
        return tree.title;
    }-*/;

    public String getTreeAssoId(int i) {
        return getTreeAssoId(getTree(data, i));
    }

    private static native String getTreeAssoId(JavaScriptObject tree) /*-{
        return tree.assoId;
    }-*/;

    public String getTreeAvatarHash(int i) {
        return getTreeAvatarHash(getTree(data, i));
    }

    private static native String getTreeAvatarHash(JavaScriptObject tree) /*-{
        return tree.avatarHash;
    }-*/;

    public boolean isTeam(int i) {
        JavaScriptObject tree = getTree(data, i);
        return getTreeAssoId(tree).equals(getTreeId(tree));
    }

    public int getTreeDepth(int i) {
        return Integer.parseInt(getTreeDepth(getTree(data, i)));
    }

    private static native String getTreeDepth(JavaScriptObject tree) /*-{
        return tree.depth;
    }-*/;

    public int getTreeCollapsed(int i) {
        String collapsedTree = getTreeCollapsed(getTree(data, i));
        if (collapsedTree != null && !collapsedTree.equals("")) {
            return Integer.parseInt(collapsedTree);
        }
        return 0;
    }

    private static native String getTreeCollapsed(JavaScriptObject tree) /*-{
        return tree.collapsed;
    }-*/;

    public void setTreeCollapsed(int i, int collapsed) {
        setTreeCollapsed(getTree(data, i),collapsed);
    }

    private static native void setTreeCollapsed(JavaScriptObject tree, int collapsed) /*-{
        tree.collapsed = collapsed;
    }-*/;

    public boolean isTreeFull(int i) {
        return Integer.parseInt(getTreePearlCount(getTree(data, i))) >= MAX_PEARLS;
    }

    private static native String getTreePearlCount(JavaScriptObject tree) /*-{
        return tree.pearlCount;
    }-*/;

    public static native void backgroundRefreshTrees(boolean force) /*-{
        $wnd.backgroundRefreshTrees(force);
    }-*/;

    public static native JavaScriptObject getUserTreesData() /*-{
        return $wnd.getUserTreesData();
    }-*/;

    // Background only

    UserTrees(JavaScriptObject d, int status, String msg) {
        data = d == null ? makeData() : d;
        setStatus(data, status, msg);
    }

    private static native JavaScriptObject makeData() /*-{
        return {};
    }-*/;

    private static native void setStatus(JavaScriptObject data, int status, String msg) /*-{
        data.status = status;
        data.message = msg;
    }-*/;

    private static native void exports() /*-{
        $wnd.refreshTrees = $entry(@com.broceliand.pearlbar.chrome.client.common.UserTrees::refreshTrees(Z));
    }-*/;

    static boolean requestTreesRunning = false;
    static Date lastRequestTreesReceived = new Date(0);

    public static UserTrees init() {
        UserTrees ut;
        String textStored = getInLocalStorage(USER_TREE_STORED);
        exports();
        if (textStored != null && !"".equals(textStored)) {
            //Alerts.alert(textStored);
            ut = new UserTrees(parseJson(textStored), LOADED, "Loaded");
            return ut;
        }
        ut = new UserTrees(null, NOT_LOADED, "Not loaded");
        return ut;
    }

    /**
     * Used in JS.
     */
    public static void refreshTrees(boolean force) {
        if (requestTreesRunning) {
            return;
        }
        if (!force && new Date().getTime() - lastRequestTreesReceived.getTime() < 3000) {
            return;
        }
        requestTrees();
    }

    private static void requestTrees() {
        requestTreesRunning = true;
        String service = WebContent.getCollectorUrl() + "gettreesandcurrentuser";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, service);

        try {
            builder.sendRequest(null, new RequestCallback() {

                @Override
                public void onError(Request request, Throwable exception) {
                    requestTreesRunning = false;
                    UserTrees ut = new UserTrees(null, ERROR, Alerts.getUserMessage(exception));
                    lastRequestTreesReceived = new Date();
                    setUserTrees(ut.getData());
                }

                @Override
                public void onResponseReceived(Request request, Response response) {
                    requestTreesRunning = false;
                    setUserTrees(onTreesReceived(response.getStatusCode(), response.getText()).getData());
                }
            });
        }
        catch (Exception e) {
            // Couldn't connect to server
            requestTreesRunning = false;
            UserTrees ut = new UserTrees(null, ERROR, Alerts.getUserMessage(e));
            setUserTrees(ut.getData());
        }
    }

    public static UserTrees onTreesReceived(int statusCode, String text) {
        UserTrees ut;
        if (statusCode == Response.SC_OK) {
            if (text.startsWith("{\"treeList\":") && text.endsWith("}}")) {
                ut = new UserTrees(parseJson(text), LOADED, "Loaded");
                setInLocalStorage(USER_TREE_STORED, text);
                if (ut.getTreeCount() < 2) {
                    ut = new UserTrees(null, ERROR, Alerts.getUserMessageForMalformed());
                }
            }
            else {
                ut = new UserTrees(null, ERROR, Alerts.getUserMessageForInvalid());
            }
        }
        else if (statusCode == Response.SC_SEE_OTHER) {
            ut = new UserTrees(null, LOGOUT, Alerts.getUserMessage(statusCode));
        }
        else {
            // Handle the error. Can get the status text from response.getStatusText()
            ut = new UserTrees(null, ERROR, Alerts.getUserMessage(statusCode));
        }
        lastRequestTreesReceived = new Date();
        return ut;
    }

    public void updateTreeCollapsed(ArrayList<Integer> treesToClose, ArrayList<Integer> treesToOpen) {
        String service = WebContent.getCollectorUrl() + "updatetreecollapsed";
        String requestData = "{'data':[";
        for(int index : treesToClose){
            setTreeCollapsed(index, 1);
            requestData += "{'treeID':"+getTreeId(index)+",'collapsed':1},";
        }
        for(int index : treesToOpen){
            setTreeCollapsed(index, 0);
            requestData += "{'treeID':"+getTreeId(index)+",'collapsed':0},";
        }
        requestData = requestData.substring(0, requestData.length()-1);
        requestData += "]}";

        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, service);

        try {
            builder.sendRequest(requestData, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    setInLocalStorage(USER_TREE_STORED, stringify(getData()));
                }

                @Override
                public void onError(Request request, Throwable exception) {

                }
            });
        }
        catch (Exception e) {
            // Couldn't connect to server
        }
    }

    static native String stringify(JavaScriptObject jsut) /*-{
    return $wnd.JSON.stringify(jsut,",");
    }-*/;

    static native void setUserTrees(JavaScriptObject jsut) /*-{
        $wnd.setUserTrees(jsut);
    }-*/;

    static native JavaScriptObject parseJson(String json) /*-{
        return $wnd.JSON.parse(json);
    }-*/;

    public static native String getInLocalStorage(String key) /*-{
        if (localStorage) {
        return localStorage[key];
        }
        return null;
    }-*/;

    public static native void setInLocalStorage(String key, String value) /*-{
        if (localStorage) {
        localStorage[key] = value;
        }
    }-*/;

    public boolean hasChanged(UserTrees n) {
        if (getStatus() != n.getStatus()) {
            return true;
        }
        if (!getMessage().equals(n.getMessage())) {
            return true;
        }
        if (getStatus() != LOADED) {
            return false;
        }

        int i = getTreeCount();
        if (i != n.getTreeCount()) {
            return true;
        }
        for (; i-- > 0;) {
            if (!getTreeId(i).equals(n.getTreeId(i))) {
                return true;
            }
            if (!getTreeAssoId(i).equals(n.getTreeAssoId(i))) {
                return true;
            }
            if (!getTreeTitle(i).equals(n.getTreeTitle(i))) {
                return true;
            }
            if (getTreeDepth(i) != n.getTreeDepth(i)) {
                return true;
            }
            if (isTreeFull(i) != n.isTreeFull(i)) {
                return true;
            }
            //if (getTreeCollapsed(i) != n.getTreeCollapsed(i)) {
            //    return true;
            //}
        }
        return false;
    }
}
