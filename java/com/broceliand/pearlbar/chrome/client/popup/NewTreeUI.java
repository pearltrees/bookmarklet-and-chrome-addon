package com.broceliand.pearlbar.chrome.client.popup;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.broceliand.pearlbar.chrome.client.popup.PopupControl.PopupUIs;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

public class NewTreeUI {

    static String newTreeTitle;

    static void init(Panel parent) {
        PopupControl.panelScrollableExist = false;
        FlowPanel panel = new FlowPanel();
        parent.add(panel);
        if (WebContent.onIpad) {
            panel.setSize("100%", "100%");
        }
        FlowPanel description = new FlowPanel();
        panel.add(description);
        HTML header = PopupElements.createHtml();
        panel.add(header);
        //initInactiveUI(panel);

        if (!WebContent.onIpad) {
            header.setHTML(PopupElements.DOWN_TRIANGLE + "Name your new pearltree");
            header.addStyleDependentName(PopupElements.STYLE_LINK);
            PopupElements.setMarginBottom(header, 5);
        } else {
            PopupElements.createDescription(panel, description, false, "Create a new pearltree", false);

            Label backToList = PopupElements.createLabel();
            description.add(backToList);
            backToList.setText("Go back");
            backToList.addStyleDependentName(PopupElements.STYLE_TREETITLE);
            backToList.addStyleDependentName(PopupElements.STYLE_BACKTOLIST);
            backToList.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    TreeListUI.onBackToList();
                }
            });

        }

        FlowPanel newTreePanel = new FlowPanel();
        newTreePanel.setStylePrimaryName(PopupElements.PRIMARY_STYLE_INPUT);
        final TextBox name = PopupElements.createTextBox();
        name.setMaxLength(64);
        name.addStyleDependentName(PopupElements.STYLE_NEWFIELD);
        name.addStyleDependentName(PopupElements.STYLE_INPUTBOX_DEFAULT);

        name.addFocusHandler(new FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                if ("Name your new pearltree".equals(name.getText())) {
                    name.setText("");
                    name.removeStyleDependentName(PopupElements.STYLE_INPUTBOX_DEFAULT);
                }
            }
        });

        if (WebContent.onIpad) {
            FlowPanel newTreePanelBox = new FlowPanel();
            newTreePanelBox.setStylePrimaryName(PopupElements.PRIMARY_STYLE_INPUTBOX_IPAD);
            panel.add(newTreePanelBox);
            newTreePanel.addStyleDependentName(PopupElements.STYLE_INPUT_IPAD);
            newTreePanelBox.add(newTreePanel);
            name.setText("Name your new pearltree");
            newTreePanel.add(name);

            HTML ok = PopupElements.createHtml();
            ok.setStylePrimaryName(PopupElements.PRIMARY_STYLE_NEWTREE_BUTTONS_IPAD);
            ok.setHTML(PopupElements.OK);
            newTreePanelBox.add(ok);
            ok.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    onValidate(name);
                }
            });
        } else {
            panel.add(newTreePanel);
            newTreePanel.add(name);
        }

        name.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == 13) {
                    onValidate(name);
                }
            }
        });

        header.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onValidate(name);
            }
        });

        PopupElements.setMarginBottom(header, 5);

        if (!WebContent.onIpad) {
            FlowPanel buttons = new FlowPanel();
            panel.add(buttons);
            Label cancel = PopupElements.createLabel();
            newTreePanel.add(cancel);
            cancel.setText("cancel");
            cancel.addStyleDependentName(PopupElements.STYLE_THINLINK);
            cancel.addStyleDependentName(PopupElements.STYLE_LEFT);

            cancel.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    Alerts.closeWindow();
                }
            });
            Label ok = PopupElements.createLabel();
            buttons.add(ok);
            ok.setText("ok");
            ok.addStyleDependentName(PopupElements.STYLE_LINK);
            ok.addStyleDependentName(PopupElements.STYLE_RIGHT);
            ok.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    onValidate(name);
                }
            });
        }


        if (!WebContent.onIpad) {
            name.setFocus(true);
        }

        PopupControl.onViewCreated();
    }

    /*static void initInactiveUI(Panel panel) {
        PopupElements.createGoAccount(panel, false);
        PopupElements.createSwitchMode(panel, false, false);
        PopupElements.createDirectAction(panel, false, false);
        PopupElements.createNewPearltree(panel, false);
    }*/

    static void onValidate(TextBox name) {
        newTreeTitle = name.getText().trim();
        if (newTreeTitle.isEmpty()) {
            if (WebContent.onIpad) {
                    name.setText("Name your new pearltree");
                    name.addStyleDependentName(PopupElements.STYLE_INPUTBOX_DEFAULT);
            }
            return;
        }
        PopupControl.changeUI(PopupUIs.ChooseParent);
    }

}
