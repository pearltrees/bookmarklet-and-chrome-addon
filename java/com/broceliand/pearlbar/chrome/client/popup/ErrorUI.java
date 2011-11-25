package com.broceliand.pearlbar.chrome.client.popup;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

public class ErrorUI {

    static void init(Panel parent, UserTrees userTrees) {
        if (!WebContent.onIpad) {
            FlowPanel panel = new FlowPanel();
            FocusPanel focus = new FocusPanel(panel);
            parent.add(focus);
            focus.setStylePrimaryName(PopupElements.PRIMARY_STYLE_FOCUS);
            focus.setFocus(true);

            Label title = PopupElements.createLabel();
            panel.add(title);
            title.setText("Error");
            title.addStyleDependentName(PopupElements.STYLE_BLUEBOLD);
            PopupElements.setMarginBottom(title, 20);

            Label message = PopupElements.createLabel();
            panel.add(message);
            message.setText(userTrees.getMessage());
            message.addStyleDependentName(PopupElements.STYLE_ERROR);
            PopupElements.setMarginBottom(message, 20);

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

        } else {
            PopupControl.panelScrollableExist = false;
            parent.setStylePrimaryName(PopupElements.PRIMARY_STYLE_NONSCROLLABLE);
            DOM.setElementAttribute(parent.getElement(), "id", "panelNonScrollable");
            FlowPanel description = new FlowPanel();
            PopupElements.createDescription(parent, description, false, userTrees.getMessage(), false);
            FlowPanel panelError = new FlowPanel();
            parent.add(panelError);
            if (("Please enable cookies to use the bookmarklet.").equals(userTrees.getMessage())) {
                HTML errorBox = PopupElements.createHtml();
                errorBox.setStylePrimaryName(PopupElements.PRIMARY_STYLE_CENTERED);
                errorBox.setHTML(PopupElements.COOKIES);
            }
            FlowPanel scroller = new FlowPanel();
            panelError.add(scroller);
            DOM.setElementAttribute(panelError.getElement(), "id", "panelScrollable");
            DOM.setElementAttribute(scroller.getElement(), "id", "scroller");
            PopupControl.onResize();
            PopupElements.createGoBackToApp(parent, true);
        }


        PopupControl.onViewCreated();
    }
}
