package com.broceliand.pearlbar.chrome.client.bookmark;

import com.broceliand.pearlbar.chrome.client.popup.*;
import com.google.gwt.core.client.*;

public class BookmarkMain implements EntryPoint {

    @Override
    public void onModuleLoad() {
        BookmarkState.init();
        PopupControl.onModuleLoad();
    }
}
