package com.broceliand.pearlbar.chrome.client.popup;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

public class LoadingUI {

    static void init(Panel parent, UserTrees userTrees) {
        FlowPanel panel = new FlowPanel();
        parent.add(panel);

        if (!WebContent.onIpad) {
            Label header = PopupElements.createLabel();
            header.addStyleDependentName(PopupElements.STYLE_HUGE);
            panel.add(header);
            header.setText("Loading");
        }
        else {
            PopupControl.panelScrollableExist = false;
            panel.setStylePrimaryName(PopupElements.PRIMARY_STYLE_NONSCROLLABLE);
            DOM.setElementAttribute(panel.getElement(), "id", "panelNonScrollable");
            FlowPanel description = new FlowPanel();
            PopupElements.createDescription(panel, description, true, "Pearl this page in", true);
            FlowPanel panelInside = new FlowPanel();
            panel.add(panelInside);
            FlowPanel scroller = new FlowPanel();
            panelInside.add(scroller);
            DOM.setElementAttribute(panelInside.getElement(), "id", "panelScrollable");
            DOM.setElementAttribute(scroller.getElement(), "id", "scroller");
            PopupControl.onResize();
            HTML message = PopupElements.createHtml();
            message.setHTML(PopupElements.LOADINGMESSAGE);
            message.setStylePrimaryName(PopupElements.PRIMARY_STYLE_CENTERED);
            panel.add(message);
            PopupElements.createGoBackToApp(panel, false);
            //PopupControl.setNewScroll("panelScrollable");
        }

        PopupControl.onViewCreated();
    }


}
