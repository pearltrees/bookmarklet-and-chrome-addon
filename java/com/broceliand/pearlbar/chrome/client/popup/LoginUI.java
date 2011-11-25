package com.broceliand.pearlbar.chrome.client.popup;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.broceliand.pearlbar.chrome.client.popup.PopupControl.PopupUIs;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.*;

public class LoginUI {

    private static boolean frozen;
    private static String fieldUser = "";
    private static String fieldPass = "";

    static void init(Panel parent, UserTrees userTrees) {

        frozen = false;
        FlowPanel panel = new FlowPanel();
        parent.add(panel);
        PopupElements.createCredentials(panel, userTrees);

        FlowPanel line = new FlowPanel();
        panel.add(line);

        Label userLbl = PopupElements.createLabel();
        userLbl.setText("username");
        userLbl.addStyleDependentName(PopupElements.STYLE_PADDING);
        userLbl.addStyleDependentName(PopupElements.STYLE_LOGIN);

        FlowPanel userPanel = new FlowPanel();
        userPanel.setStylePrimaryName(PopupElements.PRIMARY_STYLE_INPUT);
        PopupElements.setMarginBottom(userPanel, 15);

        Label passLbl = PopupElements.createLabel();
        passLbl.setText("password");
        passLbl.addStyleDependentName(PopupElements.STYLE_PADDING);
        passLbl.addStyleDependentName(PopupElements.STYLE_LOGIN);

        FlowPanel passPanel = new FlowPanel();
        passPanel.setStylePrimaryName(PopupElements.PRIMARY_STYLE_INPUT);
        PopupElements.setMarginBottom(passPanel, 15);

        FlowPanel buttons = new FlowPanel();

        final TextBox userBox = PopupElements.createTextBox();
        userPanel.add(userBox);
        userBox.setMaxLength(64);
        userBox.setText(fieldUser);
        userBox.setFocus(true);


        final PasswordTextBox passBox = PopupElements.createPasswordTextBox();
        passPanel.add(passBox);
        passBox.setMaxLength(500);
        passBox.setText(fieldPass);
        passBox.setTabIndex(1);


        if (!WebContent.onIpad){
            line.setStylePrimaryName(PopupElements.PRIMARY_STYLE_TEXT);
            line.addStyleDependentName(PopupElements.STYLE_PADDING);

            InlineLabel l1 = PopupElements.createInlineLabel();
            line.add(l1);
            l1.setText("New to Pearltrees? ");
            l1.addStyleDependentName(PopupElements.STYLE_BOLD);

            InlineLabel create = PopupElements.createInlineLabel();
            line.add(create);
            create.setText("Click here");
            create.addStyleDependentName(PopupElements.STYLE_LINK);
            create.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    LoginFailedUI.goCreate();
                }
            });

            InlineLabel l2 = PopupElements.createInlineLabel();
            line.add(l2);
            l2.setText(" to create an account.");
            l2.addStyleDependentName(PopupElements.STYLE_BOLD);
            PopupElements.setMarginBottom(line, 40);


            Label login = PopupElements.createLabel();
            panel.add(login);
            login.setText("Already have an account? Please log in.");
            login.addStyleDependentName(PopupElements.STYLE_BOLD);
            login.addStyleDependentName(PopupElements.STYLE_PADDING);
            PopupElements.setMarginBottom(login, 10);
            panel.add(userLbl);
            panel.add(userPanel);
            panel.add(passLbl);
            panel.add(passPanel);
            panel.add(buttons);
            Label ok = PopupElements.createLabel();
            buttons.add(ok);
            ok.setText("ok");
            ok.addStyleDependentName(PopupElements.STYLE_LINKBLACK);
            ok.addStyleDependentName(PopupElements.STYLE_RIGHT);
            ok.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    onValidate(userBox.getText(), passBox.getText());
                }
            });

        } else {
            PopupElements.createDescription(panel, line, false, "Already have an account? Please log in", false);
            userBox.addStyleDependentName(PopupElements.STYLE_LOGIN);
            passBox.addStyleDependentName(PopupElements.STYLE_LOGIN);
            FlowPanel wrapp = new FlowPanel();
            panel.add(wrapp);
            wrapp.setStylePrimaryName(PopupElements.PRIMARY_STYLE_LOGIN_IPAD);
            wrapp.add(userLbl);
            wrapp.add(userPanel);
            wrapp.add(passLbl);
            wrapp.add(passPanel);
            HTML ok = PopupElements.createHtml();
            ok.setStylePrimaryName(PopupElements.PRIMARY_STYLE_NEWTREE_BUTTONS_IPAD);
            ok.addStyleDependentName(PopupElements.STYLE_LOGIN);
            ok.setHTML(PopupElements.OK);
            wrapp.add(ok);
            ok.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    onValidate(userBox.getText(), passBox.getText());
                }
            });
        }



        // We use keyDown here instead of keyPress
        // because no keyPressEvent is sent for special keys like "tab" in some browsers
        userBox.addKeyDownHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == 13 || event.getNativeKeyCode() == 9) {
                    event.preventDefault();
                    passBox.setFocus(true);
                }
            }
        });

        passBox.addKeyDownHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == 13) {
                    onValidate(userBox.getText(), passBox.getText());
                }else if(event.getNativeKeyCode() == 9 && event.isShiftKeyDown()){
                    event.preventDefault();
                    userBox.setFocus(true);
                }
            }
        });

        PopupControl.onViewCreated();
    }

    static void onValidate(String user, String pass) {
        if (frozen) {
            return;
        }
        if (user.isEmpty() || pass.isEmpty()) {
            return;
        }
        fieldUser = user;
        fieldPass = pass;
        frozen = true;
        sendLogin(fieldUser, fieldPass);
    }

    static void loginError(String msg) {
        failedLogin(); //TODO Display in error panel
    }

    static void successfulLogin() {
        if (WebContent.onIpad) {
            PopupControl.changeUI(PopupUIs.LoginSuccessfull);
            PopupControl.onModuleLoad();
        } else {
        UserTrees.backgroundRefreshTrees(true);
        }
    }

    static void failedLogin() {
        PopupControl.changeUI(PopupUIs.LoginFailed);
    }

    static void sendLogin(String fieldUser, String fieldPass) {
        String service = WebContent.getCollectorUrl() + "loguser";
        String requestData = "username=" + URL.encodeComponent(fieldUser) + "&password=" + URL.encodeComponent(fieldPass);
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, service);
        builder.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
        try {
            builder.sendRequest(requestData, new RequestCallback() {

                @Override
                public void onError(Request request, Throwable exception) {
                    loginError(Alerts.getUserMessage(exception));
                }

                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) {
                        if (response.getText().equals("0")) {
                            successfulLogin();
                        }
                        else if (response.getText().equals("1")) {
                            failedLogin();
                        }
                        else {
                            loginError(Alerts.getUserMessageForMalformed());
                        }
                    }
                    else {
                        // Handle the error. Can get the status text from response.getStatusText()
                        loginError(Alerts.getUserMessage(response.getStatusCode()));
                    }
                }
            });
        }
        catch (Exception e) {
            // Couldn't connect to server
            loginError(Alerts.getUserMessage(e));
        }
    }
}
