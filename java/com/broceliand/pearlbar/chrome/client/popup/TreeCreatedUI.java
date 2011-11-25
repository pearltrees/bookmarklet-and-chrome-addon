package com.broceliand.pearlbar.chrome.client.popup;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

public class TreeCreatedUI {

    static void init(Panel parent) {
        FlowPanel panel = new FlowPanel();
        FocusPanel focus = new FocusPanel(panel);
        parent.add(focus);
        focus.setStylePrimaryName(PopupElements.PRIMARY_STYLE_FOCUS);
        focus.setFocus(true);

        HTML header = PopupElements.createHtml();
        panel.add(header);
        header.setHTML(PopupElements.DOWN_TRIANGLE + "Done! The content you pearled is now in your new pearltree.");
        header.addStyleDependentName(PopupElements.STYLE_BLUEBOLD);
        PopupElements.setMarginBottom(header, 20);

        FlowPanel buttons = new FlowPanel();
        panel.add(buttons);

        Label ok = PopupElements.createLabel();
        buttons.add(ok);
        ok.setText("ok");
        ok.addStyleDependentName(PopupElements.STYLE_LINK);
        ok.addStyleDependentName(PopupElements.STYLE_RIGHT);

        focus.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Alerts.closeWindow();
            }
        });

        focus.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                Alerts.closeWindow();
            }
        });

        focus.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                Alerts.closeWindow();
            }
        });

        PopupControl.onViewCreated();
    }
}
