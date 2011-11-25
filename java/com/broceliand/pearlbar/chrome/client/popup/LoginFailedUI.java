package com.broceliand.pearlbar.chrome.client.popup;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.broceliand.pearlbar.chrome.client.popup.PopupControl.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

public class LoginFailedUI {

    static void init(Panel parent) {
        FlowPanel panel = new FlowPanel();
        parent.add(panel);


        InlineLabel l2 = PopupElements.createInlineLabel();

        l2.setText("Please try again to ");
        l2.addStyleDependentName(PopupElements.STYLE_NOPADDINGRIGHT);

        InlineLabel log = PopupElements.createInlineLabel();

        log.setText("log in");
        log.addStyleDependentName(PopupElements.STYLE_LINK);
        log.addStyleDependentName(PopupElements.STYLE_NOPADDINGLEFT);
        log.addStyleDependentName(PopupElements.STYLE_NOPADDINGRIGHT);
        log.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                PopupControl.changeUI(PopupUIs.Login);
            }
        });

        InlineLabel l3 = PopupElements.createInlineLabel();

        l3.setText(" or ");
        l3.addStyleDependentName(PopupElements.STYLE_NOPADDINGLEFT);
        l3.addStyleDependentName(PopupElements.STYLE_NOPADDINGRIGHT);

        InlineLabel create = PopupElements.createInlineLabel();

        create.setText("create an account.");
        create.addStyleDependentName(PopupElements.STYLE_LINK);
        create.addStyleDependentName(PopupElements.STYLE_NOPADDINGLEFT);
        create.addStyleDependentName(PopupElements.STYLE_NOPADDINGRIGHT);
        create.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                goCreate();
            }
        });

        if (!WebContent.onIpad) {
        Label l1 = PopupElements.createLabel();
        panel.add(l1);
        l1.setText("Sorry, invalid username or password.");
        l1.addStyleDependentName(PopupElements.STYLE_BOLD);
        PopupElements.setMarginBottom(l1, 15);
        l2.addStyleDependentName(PopupElements.STYLE_BOLD);
        panel.add(l2);
        panel.add(log);
        l3.addStyleDependentName(PopupElements.STYLE_BOLD);
        panel.add(l3);
        panel.add(create);
        } else {
            FlowPanel line = new FlowPanel();
            panel.add(line);
            PopupElements.createDescription(panel, line, false, "Sorry, invalid username or password", false);
            FlowPanel wrapp = new FlowPanel();
            panel.add(wrapp);
            wrapp.setStylePrimaryName(PopupElements.PRIMARY_STYLE_LOGIN_IPAD);
            wrapp.addStyleDependentName(PopupElements.STYLE_GREY);
            wrapp.add(l2);
            wrapp.add(log);
            wrapp.add(l3);
            wrapp.add(create);
        }


        PopupControl.onViewCreated();
    }

    static void goCreate() {
        if (!WebContent.onIpad) {
            WebContent.openInNewTab(WebContent.getCollectorUrl() + "createAccount");
            Alerts.closeWindow();
            } else {
                TreeListUI.goBackToPT(null);
                Timer t = new Timer() {

                    @Override
                    public void run() {
                        Alerts.closeWindow();
                    }
                };
                t.schedule(1000);
                PopupControl.onViewCreated();
            }
    }
}
