package com.broceliand.pearlbar.chrome.client.popup;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.broceliand.pearlbar.chrome.client.popup.PopupControl.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

public class InactiveUI {

    private static HandlerRegistration clickListener;
    private static HandlerRegistration keyPressListener;

    static void init(Panel parent, PopupUIs ui) {
        FlowPanel panel = new FlowPanel();
        FocusPanel focus = new FocusPanel(panel);
        parent.add(focus);
        focus.setStylePrimaryName(PopupElements.PRIMARY_STYLE_FOCUS);
        if (WebContent.onIpad){
            focus.addStyleDependentName(PopupElements.STYLE_FOCUS_IPAD);
        }
        focus.setFocus(true);

        HTML label1 = PopupElements.createHtml();
        HTML label1bis = PopupElements.createHtml();
        panel.add(label1);
        label1.addStyleDependentName(PopupElements.STYLE_BLUEHOVER);
        if (WebContent.onIpad) {
            label1.setHTML("You have successfully installed" +
                    "<br />" +
                    " the pearler on your iPad. " +
                    "<br />" +
                    "<br />" +
                    "When you find a web page you like," +
                    "<br />" +
                    "click on the pearler and put it directly" +
                    "<br />" +
                    "where you want in your account.");
            label1.addStyleDependentName(PopupElements.STYLE_FOCUS_IPAD);
            label1.addStyleDependentName(PopupElements.STYLE_HUGE);
        } else {
            label1.setHTML("Your " + (WebContent.bookmarklet ? "bookmarklet" : "extension") + " is successfully installed.");
            label1.addStyleDependentName(PopupElements.STYLE_HUGE);
        }

        if (WebContent.onIpad){
            panel.add(label1bis);
            label1bis.setHTML(PopupElements.BUTTON_GO_TO_APP);
            label1bis.addStyleDependentName(PopupElements.STYLE_HUGE);
        }

        Label label2 = PopupElements.createLabel();
        panel.add(label2);
        if (WebContent.onIpad) {
            label2.setText("or tap here to continue in Safari");
            label2.addStyleDependentName(PopupElements.STYLE_SMALL);
        } else {
            label2.setText("Click here to close this page.");
            label2.addStyleDependentName(PopupElements.STYLE_HUGE);
            label2.addStyleDependentName(PopupElements.STYLE_BLUEHOVER);
        }

        clickListener = label2.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                    closeWindow();
            }
        });

        if (!WebContent.onIpad) {
            keyPressListener = focus.addKeyPressHandler(new KeyPressHandler() {
                @Override
                public void onKeyPress(KeyPressEvent event) {
                    closeWindow();
                }
            });
        } else {
            keyPressListener = label1bis.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (WebContent.onIpad){
                        TreeListUI.goBackToPT(null);
                        Timer t = new Timer() {

                            @Override
                            public void run() {
                                Alerts.closeWindow();
                            }
                        };
                        t.schedule(1000);

                        PopupControl.onViewCreated();
                    }else{
                        closeWindow();
                    }
                }
            });
        }

    PopupControl.onViewCreated();
}

public static final void closeWindow() {
    if (clickListener != null) {
        clickListener.removeHandler();
    }
    if (keyPressListener != null) {
        keyPressListener.removeHandler();
    }
    Alerts.closeWindow();
}
}
