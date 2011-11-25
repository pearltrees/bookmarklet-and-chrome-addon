package com.broceliand.pearlbar.chrome.client.popup;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.broceliand.pearlbar.chrome.client.popup.PopupControl.*;
import com.google.gwt.dom.client.Style.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

public class PopupElements {

    static final String RIGHT_TRIANGLE;
    static final String DOWN_TRIANGLE;
    static final String AVATAR_CACHE;
    static final String PUZZLE;
    static final String DROPZONE;
    static final String NEW_PEARLTREE;
    static final String DESCRIPTION;
    static final String GOBACK;
    static final String OK;
    static final String TEXTBOX;
    static final String LOADINGMESSAGE;
    static final String BUTTON_GO_TO_APP;
    static final String COOKIES;
    static final String PEARLED;
    static final String PRELOADED_PEARLED;

    static final String PRIMARY_STYLE_TEXT = "pt-text";
    static final String PRIMARY_STYLE_AVATARBOX = "pt-avatarbox";
    static final String PRIMARY_STYLE_TEXTBOX = "pt-textbox";
    static final String PRIMARY_STYLE_INPUT = "pt-input";
    static final String PRIMARY_STYLE_INDENTBLOCK = "pt-indentblock";
    static final String PRIMARY_STYLE_FOCUS = "pt-focus";
    static final String PRIMARY_STYLE_TREELIST = "pt-treelist";
    static final String PRIMARY_STYLE_TREENODEPANEL = "pt-treenodepanel";
    static final String PRIMARY_STYLE_CHILDRENPANEL = "pt-childrenpanel";
    static final String PRIMARY_STYLE_FLOATWRAPPER = "pt-floatwrapper";
    static final String PRIMARY_STYLE_DESCRIPTION = "pt-description";
    static final String PRIMARY_STYLE_NEWPEARLTREE = "pt-newpearltree";
    static final String PRIMARY_STYLE_NONSCROLLABLE = "pt-nonscrollable";
    static final String PRIMARY_STYLE_SCROLLABLE = "pt-scrollable";
    static final String PRIMARY_STYLE_NEWTREE_BUTTONS_IPAD = "pt-newtreebuttons";
    static final String PRIMARY_STYLE_INPUTBOX_IPAD = "pt-newtree-inputbox";
    static final String PRIMARY_STYLE_LOGIN_IPAD = "pt-login-ipad";
    static final String PRIMARY_STYLE_INVISIBLE="pt-invisible";
    static final String PRIMARY_STYLE_CENTERED="pt-centered";
    static final String PRIMARY_STYLE_PEARLED_IPAD="pt-pearled-ipad";

    static final String STYLE_GREY = "grey";
    static final String STYLE_REALLY_UNDER_MESSAGE = "reallyunder";
    static final String STYLE_UNDER_MESSAGE = "under";
    static final String STYLE_DESCRIPTION_UP = "up";
    static final String STYLE_DESCRIPTION_DOWN = "down";
    static final String STYLE_LIST_REVEALED = "revealed";
    static final String STYLE_LIST_COLLAPSED = "collapsed";
    static final String STYLE_PADDING = "padding";
    static final String STYLE_SPECIAL_PADDING = "specialpadding";
    static final String STYLE_NEW_PEARLTREE = "newpearltree";
    static final String STYLE_NOPADDINGLEFT = "nopaddingleft";
    static final String STYLE_NOPADDINGRIGHT = "nopaddingright";
    static final String STYLE_ITEM = "item";
    static final String STYLE_ESCAPE = "escape";
    static final String STYLE_BACKTOLIST = "backtolist";
    static final String STYLE_SELECTABLE = "selectable";
    static final String STYLE_SELECTBOX_IPAD = "selectboxipad";
    static final String STYLE_SELECTED = "selected";
    static final String STYLE_SELECTED_TIMER = "selectedtimer";
    static final String STYLE_INACTIVE = "inactive";
    static final String STYLE_FULL = "full";
    static final String STYLE_LINK = "link";
    static final String STYLE_THINLINK = "thinlink";
    static final String STYLE_BOLD = "bold";
    static final String STYLE_BLUEBOLD = "bluebold";
    static final String STYLE_BLUEHOVER = "bluehover";
    static final String STYLE_LINKBLACK = "linkblack";
    static final String STYLE_LEFT = "left";
    static final String STYLE_RIGHT = "right";
    static final String STYLE_ERROR = "error";
    static final String STYLE_LOGIN = "login";
    static final String STYLE_NEWFIELD = "newfield";
    static final String STYLE_INPUT_IPAD = "ipad";
    static final String STYLE_TREETITLE = "treetitle";
    static final String STYLE_TREEAVATAR = "treeavatar";
    static final String STYLE_DEPTH = "depth";
    static final String STYLE_ARROW = "arrow";
    static final String STYLE_NOARROW = "noarrow";
    static final String STYLE_ARROW_HIGHLIGHT = "arrow-highlight";
    static final String STYLE_COLLAPSED = "collapsed";
    static final String STYLE_HUGE = "huge";
    static final String STYLE_SMALL = "small";
    static final String STYLE_MARGIN = "margin";
    static final String STYLE_FOCUS_IPAD = "ipad";
    static final String STYLE_INPUTBOX_DEFAULT = "default";

    static {
        RIGHT_TRIANGLE = "<img src=\"" + getUrl("image/close.png") + "\"/> ";
        DOWN_TRIANGLE = "<img src=\"" + getUrl("image/open.png") + "\"/> ";
        AVATAR_CACHE = "<img class=\"pt-avatarcache\" src=\"" + getUrl("image/avatar-cache.png") + "\"/> ";
        PUZZLE = "<img class=\"pt-puzzle\" src=\"" + getUrl("image/puzzle.png") + "\"/> ";
        DROPZONE = "<img class=\"pt-avatarcache\" src=\"" + getUrl("image/dropzone.png") + "\"/> ";
        NEW_PEARLTREE = "<img class=\"pt-avatarcache\" src=\"" + getUrl("image/newPearltree.png") + "\"/> ";
        DESCRIPTION = "<img class=\"pt-avatarcache\" src=\"" + getUrl("image/description.png") + "\"/> ";
        GOBACK = "<img class=\"pt-avatarcache\" src=\"" + getUrl("image/goTo.png") + "\"/> ";
        OK = "<img src=\"" + getUrl("image/okButton.png") + "\"> ";
        TEXTBOX = "<img style=\"width:100% height:100%\"  src=\"" + getUrl("image/textBox.png") + "\"> ";
        LOADINGMESSAGE = "Initializing<br /><img src=\"" + getUrl("image/loader.gif") + "\"/> ";
        BUTTON_GO_TO_APP = "<img src=\"" + getUrl("image/buttonGoToApp.png") + "\"/> ";
        COOKIES = "<img src=\"" + getUrl("image/decoSafariCookies.png") + "\"/> ";
        PEARLED = "<img src=\"" + getUrl("image/decoPearled.png") + "\"/> ";
        PRELOADED_PEARLED = "<img src=\"" + getUrl("image/decoPearled.png") + "\" width=\"1\" height=\"1\" border=\"0\"/> ";
    }

    public static String credentials = "";

    static HTML createHtml() {
        HTML item = new HTML();
        item.setStylePrimaryName(PRIMARY_STYLE_TEXT);
        return item;
    }

    static Label createLabel() {
        Label item = new Label();
        item.setStylePrimaryName(PRIMARY_STYLE_TEXT);
        return item;
    }

    static InlineLabel createInlineLabel() {
        InlineLabel item = new InlineLabel();
        item.setStylePrimaryName(PRIMARY_STYLE_TEXT);
        return item;
    }

    static InlineHTML createInlineHTML() {
        InlineHTML item = new InlineHTML();
        item.setStylePrimaryName(PRIMARY_STYLE_TEXT);
        return item;
    }

    static TextBox createTextBox() {
        TextBox item = new TextBox();
        item.setStylePrimaryName(PRIMARY_STYLE_TEXTBOX);
        if (WebContent.onIpad) {
            item.getElement().setAttribute("autocorrect","off");
            item.getElement().setAttribute("autocapitalize","off");
        }
        return item;
    }

    static PasswordTextBox createPasswordTextBox() {
        PasswordTextBox item = new PasswordTextBox();
        item.setStylePrimaryName(PRIMARY_STYLE_TEXTBOX);
        return item;
    }

    static TreeListItem createTreeListItem() {
        TreeListItem panel = new TreeListItem();
        panel.setStylePrimaryName(PRIMARY_STYLE_TEXT);
        return panel;
    }

    static Label createIndentBlock() {
        Label item = new Label();
        item.setStylePrimaryName(PRIMARY_STYLE_INDENTBLOCK);
        return item;
    }

    static void createGoAccount(Panel panel, boolean active) {
        if (WebContent.onIpad) {
            return;
        }
        HTML item = createHtml();
        panel.add(item);
        item.setHTML(RIGHT_TRIANGLE + "Go to your account");
        if (active) {
            item.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    TreeListUI.onGoAccount();
                }
            });
        }
        item.addStyleDependentName(active ? STYLE_LINK : STYLE_INACTIVE);
        setMarginBottom(item, 10);
    }

    static void createConnectAccount(Panel panel) {
        if (WebContent.onIpad) {
            return;
        }
        HTML item = createHtml();
        panel.add(item);
        item.setHTML(RIGHT_TRIANGLE + "Manage your FB / Twitter connections");
        item.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                TreeListUI.onGoToSyncAccount();
            }
        });
        item.addStyleDependentName(STYLE_LINK);
        setMarginBottom(item, 10);
    }

    static void createSwitchMode(Panel panel, boolean active, boolean currentIsGoDirectly) {
        if (WebContent.onIpad) {
            return;
        }
        HTML item = createHtml();
        panel.add(item);
        item.setHTML(currentIsGoDirectly ? RIGHT_TRIANGLE + "Pearl directly" : RIGHT_TRIANGLE + "Go directly to one of your pearltrees");
        if (active) {
            item.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    TreeListUI.onSwichModeClicked();
                }
            });
        }
        item.addStyleDependentName(active ? STYLE_LINK : STYLE_INACTIVE);
        setMarginBottom(item, 10);
    }

    static void createAvatar(Panel panel) {
        InlineHTML item = new InlineHTML();
        panel.add(item);
        item.setHTML(AVATAR_CACHE);
    }

    static void createDescription(Panel panel, final FlowPanel description, boolean active, String label, final boolean begin) {
        description.setStylePrimaryName(PRIMARY_STYLE_DESCRIPTION);
        description.addStyleDependentName(STYLE_DESCRIPTION_UP);
        panel.add(description);
        HTML descriptionBox = createHtml();
        descriptionBox.setHTML(DESCRIPTION);
        descriptionBox.setStylePrimaryName(PopupElements.PRIMARY_STYLE_AVATARBOX);
        descriptionBox.addStyleDependentName(PopupElements.STYLE_SPECIAL_PADDING);
        description.add(descriptionBox);
        Label item = createLabel();
        description.add(item);
        item.setText(label);
        item.addStyleDependentName(STYLE_TREETITLE);
        item.addStyleDependentName(STYLE_ITEM);
        if (active) {
            descriptionBox.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (begin) {
                        description.addStyleDependentName(PopupElements.STYLE_SELECTED_TIMER);
                        TreeListUI.onClickedDirectly();
                    } else {
                        TreeListUI.onDefaultItemClicked();
                    }
                }
            });
            item.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (begin) {
                        description.addStyleDependentName(PopupElements.STYLE_SELECTED_TIMER);
                        TreeListUI.onClickedDirectly();
                    } else {
                        TreeListUI.onDefaultItemClicked();
                    }
                }
            });
        }
    }

    static void createGoBackToApp(Panel panel, boolean active) {
        FlowPanel goBack = new FlowPanel();
        goBack.setStylePrimaryName(PRIMARY_STYLE_DESCRIPTION);
        goBack.addStyleDependentName(STYLE_DESCRIPTION_DOWN);
        panel.add(goBack);
        HTML goBackBox = createHtml();
        goBackBox.setHTML(GOBACK);
        goBackBox.setStylePrimaryName(PopupElements.PRIMARY_STYLE_AVATARBOX);
        goBackBox.addStyleDependentName(PopupElements.STYLE_SPECIAL_PADDING);
        goBack.add(goBackBox);
        Label item = createLabel();
        goBack.add(item);
        item.setText("Go to one of your pearltrees");
        item.addStyleDependentName(STYLE_TREETITLE);
        item.addStyleDependentName(STYLE_ITEM);
        Label escape = createLabel();
        goBack.add(escape);
        escape.setText("Exit");
        escape.addStyleDependentName(STYLE_TREETITLE);
        escape.addStyleDependentName(STYLE_ESCAPE);

        HTML preload = createHtml();
        preload.setHTML(PRELOADED_PEARLED);
        goBack.add(preload);

        if (active) {
            goBackBox.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    TreeListUI.onSwichModeClicked();
                }
            });
            item.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    TreeListUI.onSwichModeClicked();
                }
            });
            escape.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    TreeListUI.onEscapeItemClicked();
                }
            });
        }
    }

    static void createDirectAction(Panel panel, boolean active, boolean currentIsGoDirectly) {
        HTML item = createHtml();
        panel.add(item);
        item.setHTML(currentIsGoDirectly ? DOWN_TRIANGLE + "Go directly to one of your pearltrees" : DOWN_TRIANGLE + "Pearl this content");
        item.addStyleDependentName(active ? STYLE_LINK : STYLE_INACTIVE);
        if (active) {
            item.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    TreeListUI.onDefaultItemClicked();
                }
            });
        }
        setMarginBottom(item, 5);
    }

    static void createNewPearltree(Panel panel, boolean active) {
        final Label item = createLabel();
        final FlowPanel description = new FlowPanel();
        item.setText("a new pearltree");
        if (WebContent.onIpad) {
            description.setStylePrimaryName(PRIMARY_STYLE_NEWPEARLTREE);
            panel.add(description);
            HTML newPearltreeBox = PopupElements.createHtml();
            newPearltreeBox.setStylePrimaryName(PopupElements.PRIMARY_STYLE_AVATARBOX);
            newPearltreeBox.addStyleDependentName(PopupElements.STYLE_SPECIAL_PADDING);
            newPearltreeBox.setHTML(NEW_PEARLTREE);
            description.add(newPearltreeBox);
            description.add(item);
        }
        else {
            panel.add(item);
        }

        if (!WebContent.onIpad) {
            item.addStyleDependentName(STYLE_SELECTABLE);
            item.addStyleDependentName(STYLE_BOLD);
            item.addStyleDependentName(STYLE_PADDING);
        }
        item.addStyleDependentName(STYLE_SELECTABLE);
        item.addStyleDependentName(STYLE_ITEM);

        if (active) {
            item.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (WebContent.onIpad) {
                        description.addStyleDependentName(STYLE_SELECTED_TIMER);
                        Timer t = new Timer() {

                            @Override
                            public void run() {
                                DOM.removeElementAttribute(TreeListUI.panelInside.getElement(), "id");
                                PopupControl.changeUI(PopupUIs.NewTree);
                            }
                        };
                        t.schedule(120);
                    } else {
                        PopupControl.changeUI(PopupUIs.NewTree);
                    }
                }
            });
        }
        else {
            item.addStyleDependentName(STYLE_INACTIVE);
        }
    }

    static void setMarginBottom(Widget w, double px) {
        w.getElement().getStyle().setMarginBottom(px, Unit.PX);
    }

    static void setMarginTop(Widget w, double px) {
        w.getElement().getStyle().setMarginTop(px, Unit.PX);
    }

    private static native String getUrl(String file) /*-{
		return $wnd.getElementUrl(file);
    }-*/;

    static void createCredentials(Panel panel, UserTrees userTrees) {
        if (credentials.isEmpty()) {
            return;
        }
        int n = 0;
        Label l = null;
        String[] c = credentials.split(":");
        for (int i = 1; i < c.length; i += 2) {
            final String user = c[i - 1].trim();
            if (userTrees.getStatus() == UserTrees.LOADED && user.equals(userTrees.getTreeTitle(UserTrees.ROOTTREE_INDEX))) {
                continue;
            }
            final String pass = c[i].trim();
            l = PopupElements.createLabel();
            panel.add(l);
            l.setText("Log in as " + user);
            l.addStyleDependentName(STYLE_LINK);
            l.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    LoginUI.sendLogin(user, pass);
                }
            });
            n++;
        }
        if (n != 0) {
            setMarginBottom(l, 10);
        }
    }
}
