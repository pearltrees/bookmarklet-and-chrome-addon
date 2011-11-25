package com.broceliand.pearlbar.chrome.client.common;

import com.google.gwt.http.client.*;

public class AppClient {

    public static interface PearlClient {

        boolean resetRevealed();

        void onError(String msg);

        void onPearled(String newTreeId);
    }

    public static void pearlContent(String url, String title, String treeId, String parentTreeId, String newTreeName, final PearlClient client) {
        //Alerts.alert(" tree=" + treeId + " title=" + title + " url=" + url);
        String service = WebContent.getCollectorUrl() + "add";
        // Fix a bug in safari where spaces are not encoded in %20
        url = url.replace(" ", "%20");
        String requestData = "url=" + URL.encodeComponent(url) + "&title=" + URL.encodeComponent(title);
        if (treeId != null) {
            requestData += "&treeID=" + treeId;
        }
        else {
            requestData += "&isStart=true";
            requestData += "&newTreeName=" + URL.encodeComponent(newTreeName);
            requestData += "&parentTreeID=" + parentTreeId;
        }
        if (client.resetRevealed()) {
            requestData += "&dup=1";
        }
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, service);
        builder.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");

        try {
            builder.sendRequest(requestData, new RequestCallback() {

                @Override
                public void onError(Request request, Throwable exception) {
                    client.onError(Alerts.getUserMessage(exception));
                }

                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) {
                        if ("0".equals(response.getText())) {
                            client.onPearled(null);
                        }
                        else if ("2".equals(response.getText())) {
                            client.onError("The tree you are pearling into no longer exists.");
                        }
                        else if ("3".equals(response.getText())) {
                            client.onError("The tree you are pearling into has been deleted.");
                        }
                        else if ("4".equals(response.getText())) {
                            client.onError("The tree you are pearling into has been given to someone else.");
                        }
                        else if (response.getText().startsWith("0,")) {
                            client.onPearled(response.getText().substring(2));
                        }
                        else {
                            client.onError(Alerts.getUserMessageForInvalid());
                        }
                    }
                    else {
                        client.onError(Alerts.getUserMessage(response.getStatusCode()));
                    }
                }
            });
        }
        catch (Exception e) {
            // Couldn't connect to server
            client.onError(Alerts.getUserMessage(e));
        }
    }

    public static String buildRevealHash(UserTrees userTrees, String treeId, String assoId) {
        /*Alerts.alert("buildRevealHash " + treeId + " " + assoId);
        if (treeId == null || assoId == null) {
            return null;
        }*/
        if (userTrees.getTreeId(UserTrees.DROPZONE_INDEX).equals(treeId)) {
            return null;
        }

        //Alerts.alert("Reveal tree=" + treeId + " tab=" + Alerts.getTabId(tab) + " url=" + url);
        String hash = "/N-reveal=1";
        hash += "&N-u=1_" + userTrees.getUserId();
        hash += "&N-s=1_" + treeId;
        hash += "&N-f=1_" + treeId;
        if (!assoId.isEmpty()) {
            hash += "&N-fa=" + assoId;
        }
        return hash;
    }
}
