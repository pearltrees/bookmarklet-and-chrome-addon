package com.broceliand.pearlbar.chrome.client.common;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.*;
import com.google.gwt.user.client.ui.*;


public class TreeListItem  extends FlowPanel implements InsertPanel {
    
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}
