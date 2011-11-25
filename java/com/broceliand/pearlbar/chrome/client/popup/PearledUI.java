package com.broceliand.pearlbar.chrome.client.popup;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

public class PearledUI {

    private static HandlerRegistration clickListener;
    private static HandlerRegistration keyPressListener;
    private static HandlerRegistration blurListener;

    static void init(Panel parent, boolean hasCreatedNewTree, boolean isPearlable) {
        PopupControl.panelScrollableExist = false;
        FlowPanel panel = new FlowPanel();
        FocusPanel focus = new FocusPanel(panel);
        parent.add(focus);
        focus.setStylePrimaryName(PopupElements.PRIMARY_STYLE_FOCUS);
        if (WebContent.onIpad){
            focus.addStyleDependentName(PopupElements.STYLE_FOCUS_IPAD);
        }
        focus.setFocus(true);

        String text;
        if (hasCreatedNewTree) {
            if (WebContent.onIpad){
                text = "Done! Your new pearl is in your new pearltree";
            } else {
                text = "Done!";
            }
        } else if (!isPearlable) {
            text = "This content isn't pearlable.";
        } else {
            text = "Pearled!";
        }

        if (!WebContent.onIpad){
        Label label = PopupElements.createLabel();
        label.setText(text);
        label.addStyleDependentName(PopupElements.STYLE_HUGE);
        panel.add(label);
        } else {
            HTML message = PopupElements.createHtml();
            message.setHTML(text);
            message.setStylePrimaryName(PopupElements.PRIMARY_STYLE_PEARLED_IPAD);
            panel.add(message);
            HTML logo = PopupElements.createHtml();
            logo.setHTML(PopupElements.PEARLED);
            logo.setStylePrimaryName(PopupElements.PRIMARY_STYLE_PEARLED_IPAD);
            if (hasCreatedNewTree) {
            logo.addStyleDependentName(PopupElements.STYLE_REALLY_UNDER_MESSAGE);
            } else {
            logo.addStyleDependentName(PopupElements.STYLE_UNDER_MESSAGE);
            }
            panel.add(logo);
        }


        clickListener = focus.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                closeWindow();
            }
        });

        keyPressListener = focus.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                closeWindow();
            }
        });

        blurListener = focus.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                closeWindow();
            }
        });

        Timer t = new Timer() {

            @Override
            public void run() {
                closeWindow();
            }
        };
        t.schedule(1000);

        PopupControl.onViewCreated();
    }

    public static final void closeWindow() {
        if (clickListener != null) {
            clickListener.removeHandler();
        }
        if (keyPressListener != null) {
            keyPressListener.removeHandler();
        }
        if (blurListener != null) {
            blurListener.removeHandler();
        }
        Alerts.closeWindow();
    }
}
