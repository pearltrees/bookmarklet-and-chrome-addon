package com.broceliand.pearlbar.chrome.client.popup;

import java.util.*;

import com.broceliand.pearlbar.chrome.client.common.*;
import com.broceliand.pearlbar.chrome.client.common.WebContent.PearltreesState;
import com.broceliand.pearlbar.chrome.client.popup.PopupControl.PopupUIs;
import com.google.gwt.core.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

class TreeListUI {

    public static int currentTabId;
    public static String currentUrl;
    public static String currentTitle;
    public static boolean PearledDirect = false;
    private static Map<String, Integer> assoIds2Index = new HashMap<String, Integer>();

    public static FlowPanel panelInside;
    private static FlowPanel front;
    private static Panel parent;
    private static FlowPanel list;
    private static boolean listdisplayed;
    private static UserTrees userTrees;

    public static boolean isPearlable;
    private static boolean isInited = false;
    public static boolean treeListInited = false;
    public static boolean goDirectly;
    public static boolean chooseParent;
    public static boolean defaultItemClicked = false;
    static boolean isClosing;
    private static ArrayList<TreeNode> trees;
    private static int selectedIndex;
    private static String selectedId;
    private static Widget selectedWidget;
    private static TreeNode rootNode;
    private static TreeNode selectedTreeNode;

    static void init(Panel p, UserTrees ut) {
        parent = p;
        userTrees = ut;
        getTab();
    }

    private static void init() {
        PearltreesState s = WebContent.getPearltreesState(currentUrl);
        if (s == PearltreesState.Settings) {
            PopupControl.changeUI(PopupUIs.InSettings);
            return;
        }
        else if (s == PearltreesState.Tunnel || s == PearltreesState.GetStarted) {
            PopupControl.changeUI(PopupUIs.InTunnel);
            return;
        }
        else if (s == PearltreesState.BookmarkletTunnel) {
            PopupControl.changeUI(PopupUIs.InBookmarkletTunnel);
            return;
        }

        if (!isInited) {
            chooseParent = false;
            if (!chooseParent) {
                isPearlable = WebContent.isWebUrl(currentUrl) && !WebContent.isInPearltrees(currentUrl);
                goDirectly = !isPearlable;
                if (goDirectly) {

                }
            }
            isInited = true;
        }
        initUI();
    }

    private static void initUI() {
        isClosing = false;
        trees = new ArrayList<TreeNode>();
        FlowPanel panel = new FlowPanel();
        FocusPanel focus = new FocusPanel(panel);
        parent.add(focus);
        focus.setStylePrimaryName(PopupElements.PRIMARY_STYLE_FOCUS);
        focus.setFocus(true);

        selectedIndex = obtainSelectedTree(!chooseParent && isPearlable && !goDirectly);
        selectedId = userTrees.getTreeId(selectedIndex);
        selectedWidget = null;

        rootNode = buildTreeHierarchy();

        if (chooseParent) {
            initChooseParentUI(panel);
        }
        else if (!isPearlable) {
            initUnpearlableUI(panel);
        }
        else if (goDirectly) {
            initGoDireclyUI(panel);
        }
        else {
            initPearlDirectlyUI(panel);
        }

        focus.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == 13) {
                    onDefaultItemClicked();
                }
            }
        });

        PopupControl.onViewCreated();
    }

    private static void initTreeList(Panel panel) {
        addTreeItem(panel, UserTrees.ROOTTREE_INDEX, selectedIndex, true);

        int size = userTrees.getTreeCount();
        if (size > UserTrees.ROOTTREE_INDEX + 1) {
            listdisplayed = true;
            list = new FlowPanel();
            panel.add(list);
            list.setStylePrimaryName(PopupElements.PRIMARY_STYLE_TREELIST);
            if (!WebContent.onIpad) {
                list.setHeight((PopupControl.height - list.getAbsoluteTop()) + "px");
            }
            addChildrenToPanel(rootNode.getChildren(), list);

            DOM.setElementAttribute(selectedWidget.getElement(), "id", "selected");
            if (selectedIndex > UserTrees.ROOTTREE_INDEX) {
                if (!WebContent.onIpad) {
                    selectedWidget.getElement().scrollIntoView();
                }
            }
        }
    }

    private static TreeNode buildTreeHierarchy() {
        int size = userTrees.getTreeCount();
        TreeNode dropZoneNode = new TreeNode(null);
        trees.add(dropZoneNode);
        TreeNode rootNode = new TreeNode(null);
        trees.add(rootNode);
        assoIds2Index.put(userTrees.getTreeId(UserTrees.ROOTTREE_INDEX), new Integer(UserTrees.ROOTTREE_INDEX));
        if (size > UserTrees.ROOTTREE_INDEX + 1) {
            int currentDepth = 0;
            TreeNode currentParentNode = rootNode;
            for (int i = UserTrees.ROOTTREE_INDEX + 1; i < size; i++) {
                int treeDepth = userTrees.getTreeDepth(i);
                int difference = currentDepth - treeDepth;
                if (difference >= 0) {
                    for(int j = 0 ; j <= difference ; j++){
                        currentParentNode = currentParentNode.getParent();
                    }
                }
                if (userTrees.getTreeId(i).equals(userTrees.getTreeAssoId(i))) {
                    assoIds2Index.put(userTrees.getTreeId(i), new Integer(i));
                }
                TreeNode treeNode = new TreeNode(currentParentNode);
                trees.add(treeNode);
                if (i == selectedIndex) {
                    selectedTreeNode = treeNode;
                }
                currentParentNode.addChild(treeNode);
                currentParentNode = treeNode;
                currentDepth = treeDepth;
            }
            //Add all the first

        }
        return rootNode;
    }
    private static boolean isInSelectedParentPath(int index) {
        if (selectedTreeNode != null) {
            TreeNode currentNode = selectedTreeNode;
            while (currentNode != null) {
                if (trees.indexOf(currentNode) == index) {
                    return  true;
                }
                currentNode = currentNode.getParent();
            }
        }
        return false;
    }

    private static void addChildrenToPanel(ArrayList<TreeNode> children, Panel panel){
        if (children != null && !children.isEmpty()) {
            for (TreeNode child : children) {
                FlowPanel wrapper = new FlowPanel();
                int index = trees.indexOf(child);
                wrapper.setStylePrimaryName(PopupElements.PRIMARY_STYLE_TREENODEPANEL);
                panel.add(wrapper);
                ArrayList<TreeNode> children2 = child.getChildren();
                Boolean hasChildren = ( children2 != null && !children2.isEmpty() );
                addTreeItem(wrapper, index, selectedIndex, hasChildren);

                int collapsed = userTrees.getTreeCollapsed(index);
                if (collapsed == 1 && !isInSelectedParentPath(index)
                        || collapsed == 0 && userTrees.getTreeCollapsed(trees.indexOf(child.getParent())) == 1 && child.getParent() == selectedTreeNode) {
                    wrapper.addStyleDependentName(PopupElements.STYLE_COLLAPSED);
                }
                else if (hasChildren) {
                    FlowPanel childrenPanel = new FlowPanel();
                    wrapper.add(childrenPanel);
                    childrenPanel.setStylePrimaryName(PopupElements.PRIMARY_STYLE_CHILDRENPANEL);
                    addChildrenToPanel(children2, childrenPanel);
                }
            }
        }
    }

    private static void removeChildrenFromPanel(FlowPanel panel) {
        panel.remove(1);
    }

    private static void initChooseParentUI(Panel panel) {
        if (!WebContent.onIpad) {
            HTML choose = PopupElements.createHtml();
            panel.add(choose);
            choose.setHTML(PopupElements.DOWN_TRIANGLE + "Choose where to put your new pearltree");
            choose.addStyleDependentName(PopupElements.STYLE_LINK);
            PopupElements.setMarginBottom(choose, 5);
            choose.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    onDefaultItemClicked();
                }
            });
            initTreeList(panel);
        } else {
            PopupControl.panelScrollableExist = true;
            panel.setStylePrimaryName(PopupElements.PRIMARY_STYLE_NONSCROLLABLE);
            DOM.setElementAttribute(panel.getElement(), "id", "panelNonScrollable");
            FlowPanel description = new FlowPanel();
            PopupElements.createDescription(panel, description, false, "Choose where to put your new pearltree", false);
            panelInside = new FlowPanel();
            panel.add(panelInside);
            FlowPanel scroller = new FlowPanel();
            panelInside.add(scroller);
            DOM.setElementAttribute(panelInside.getElement(), "id", "panelScrollable");
            DOM.setElementAttribute(scroller.getElement(), "id", "scroller");
            PopupControl.onResize();
            initTreeList(scroller);
            PopupControl.setNewScroll("panelScrollable");
        }
    }

    private static void initUnpearlableUI(Panel panel) {
        if (!WebContent.onIpad) {
            PopupElements.createCredentials(panel, userTrees);
            PopupElements.createConnectAccount(panel);
            PopupElements.createDirectAction(panel, true, goDirectly);

            initTreeList(panel);
        } else {
            PopupControl.panelScrollableExist = false;
            panel.setStylePrimaryName(PopupElements.PRIMARY_STYLE_NONSCROLLABLE);
            DOM.setElementAttribute(panel.getElement(), "id", "panelNonScrollable");
            FlowPanel description = new FlowPanel();
            PopupElements.createDescription(panel, description, false, "Sorry, you can't pearl this site.", false);
            panelInside = new FlowPanel();
            panel.add(panelInside);
            FlowPanel scroller = new FlowPanel();
            panelInside.add(scroller);
            DOM.setElementAttribute(panelInside.getElement(), "id", "panelScrollable");
            DOM.setElementAttribute(scroller.getElement(), "id", "scroller");
            PopupControl.onResize();
            PopupElements.createGoBackToApp(panel, true);
        }
    }

    private static void initGoDireclyUI(Panel panel) {
        if (!WebContent.onIpad) {
            PopupElements.createCredentials(panel, userTrees);
            PopupElements.createGoAccount(panel, true);
            PopupElements.createSwitchMode(panel, true, goDirectly);
            PopupElements.createConnectAccount(panel);
            PopupElements.createDirectAction(panel, true, goDirectly);
            initTreeList(panel);
        } else {
            PopupControl.panelScrollableExist = true;
            panel.setStylePrimaryName(PopupElements.PRIMARY_STYLE_NONSCROLLABLE);
            DOM.setElementAttribute(panel.getElement(), "id", "panelNonScrollable");
            FlowPanel description = new FlowPanel();
            PopupElements.createDescription(panel, description, true, "Where do you want to go?", false);
            Label backToList = PopupElements.createLabel();
            description.add(backToList);
            backToList.setText("Go back");
            backToList.addStyleDependentName(PopupElements.STYLE_TREETITLE);
            backToList.addStyleDependentName(PopupElements.STYLE_BACKTOLIST);
            backToList.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    onSwichModeClicked();
                }
            });

            panelInside = new FlowPanel();
            panel.add(panelInside);
            FlowPanel scroller = new FlowPanel();
            panelInside.add(scroller);
            DOM.setElementAttribute(panelInside.getElement(), "id", "panelScrollable");
            DOM.setElementAttribute(scroller.getElement(), "id", "scroller");
            PopupControl.onResize();
            initTreeList(scroller);
            PopupControl.setNewScroll("panelScrollable");
        }
    }

    private static void initPearlDirectlyUI(Panel panel) {
        PopupElements.createCredentials(panel, userTrees);
        PopupElements.createGoAccount(panel, true);
        PopupElements.createSwitchMode(panel, true, goDirectly);
        PopupElements.createConnectAccount(panel);
        if (!WebContent.onIpad) {
            PopupElements.createDirectAction(panel, true, goDirectly);
            PopupElements.createNewPearltree(panel, true);
            addTreeItem(panel, UserTrees.DROPZONE_INDEX, selectedIndex, false);
            Label existing = PopupElements.createLabel();
            existing.setText("in one of your existing pearltrees:");
            existing.addStyleDependentName(PopupElements.STYLE_BOLD);
            existing.addStyleDependentName(PopupElements.STYLE_PADDING);
            existing.addStyleDependentName(PopupElements.STYLE_ITEM);
            panel.add(existing);
            initTreeList(panel);
        }
        else {
            PopupControl.panelScrollableExist = true;
            panel.setStylePrimaryName(PopupElements.PRIMARY_STYLE_NONSCROLLABLE);
            DOM.setElementAttribute(panel.getElement(), "id", "panelNonScrollable");
            FlowPanel description = new FlowPanel();
            PopupElements.createDescription(panel, description, true, "Pearl this page in", false);
            panelInside = new FlowPanel();
            panel.add(panelInside);
            FlowPanel scroller = new FlowPanel();
            panelInside.add(scroller);
            panelInside.setStylePrimaryName(PopupElements.PRIMARY_STYLE_INVISIBLE);
            DOM.setElementAttribute(panelInside.getElement(), "id", "panelScrollable");
            DOM.setElementAttribute(scroller.getElement(), "id", "scroller");
            PopupControl.onResize();
            addTreeItem(scroller, UserTrees.DROPZONE_INDEX, selectedIndex, false);
            PopupElements.createNewPearltree(scroller, true);
            initTreeList(scroller);
            PopupElements.createGoBackToApp(panel, true);
            PopupControl.setNewScroll("panelScrollable");
            scrollToSelected();
        }


    }

    private static void addTreeItem(Panel panel, final int index, int selectedIndex, boolean hasChildren) {
        final TreeListItem treeListItem = PopupElements.createTreeListItem();
        panel.add(treeListItem);


        String avatarcache = PopupElements.AVATAR_CACHE;

        String title;
        if (index == UserTrees.DROPZONE_INDEX) {
            title = "your drop zone";
            if (!WebContent.onIpad){
                treeListItem.addStyleDependentName(PopupElements.STYLE_BOLD);
            }
            treeListItem.addStyleDependentName(PopupElements.STYLE_PADDING);
        }
        else {
            if (index == UserTrees.ROOTTREE_INDEX) {
                treeListItem.addStyleDependentName(PopupElements.STYLE_PADDING);
                if (!WebContent.onIpad){
                    treeListItem.addStyleDependentName(PopupElements.STYLE_MARGIN);
                }
            }
            title = userTrees.getTreeTitle(index);
        }

        if (index > UserTrees.ROOTTREE_INDEX && userTrees.isTeam(index)) {
            title = "team " + title;
            avatarcache = PopupElements.PUZZLE + avatarcache;
        }

        boolean isFull = (chooseParent || !goDirectly) && index > UserTrees.ROOTTREE_INDEX && userTrees.isTreeFull(index);
        if (isFull) {
            title += " (full)";
        }

        final int depth = userTrees.getTreeDepth(index);
        panel.addStyleDependentName(PopupElements.STYLE_DEPTH+depth);
        FlowPanel wrapper = new FlowPanel();
        wrapper.setStylePrimaryName(PopupElements.PRIMARY_STYLE_FLOATWRAPPER);
        treeListItem.add(wrapper);
        int u = (WebContent.onIpad ? 0 : 1);
        for (int i = u; i <= Math.min(depth, 12); i++) {
            boolean isLast = (i == Math.min(depth, 12));
            Label indentBlock = PopupElements.createIndentBlock();
            indentBlock.addStyleDependentName(PopupElements.STYLE_DEPTH+i);
            if (!isFull) {
                if (isLast && hasChildren) {
                    indentBlock.addStyleDependentName(PopupElements.STYLE_ARROW);
                    indentBlock.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            onArrowClicked(index);
                            event.stopPropagation();
                        }
                    });
                    indentBlock.addMouseOverHandler(new MouseOverHandler() {

                        @Override
                        public void onMouseOver(MouseOverEvent event) {
                            if (!WebContent.onIpad){
                                treeListItem.getParent().addStyleDependentName(PopupElements.STYLE_ARROW_HIGHLIGHT);
                            }
                        }
                    });
                    indentBlock.addMouseOutHandler(new MouseOutHandler() {

                        @Override
                        public void onMouseOut(MouseOutEvent event) {
                            if (!WebContent.onIpad){
                                treeListItem.getParent().removeStyleDependentName(PopupElements.STYLE_ARROW_HIGHLIGHT);
                            }
                        }
                    });
                }
                else{
                    if(isLast){
                        indentBlock.addStyleDependentName(PopupElements.STYLE_NOARROW);
                    }
                }
            }

            wrapper.add(indentBlock);
        }
        HTML avatarBox = null;
        if (WebContent.onIpad){
            String avatar = "<img class=\"pt-avatar\" src=\"" + getTreeAvatarURL(index) + "\"/> ";
            avatarBox = PopupElements.createHtml();
            avatarBox.setStylePrimaryName(PopupElements.PRIMARY_STYLE_AVATARBOX);
            if (index == UserTrees.DROPZONE_INDEX) {
                avatarBox.addStyleDependentName(PopupElements.STYLE_SPECIAL_PADDING);
                avatar= PopupElements.DROPZONE;
                avatarBox.setHTML(avatar);
                //treeListItem.addStyleDependentName(PopupElements.STYLE_MARGIN);
            } else {
                avatarBox.setHTML(avatar + avatarcache);
            }
            treeListItem.add(avatarBox);
        }

        InlineHTML titleBox = PopupElements.createInlineHTML();
        titleBox.addStyleDependentName(PopupElements.STYLE_TREETITLE);
        titleBox.setHTML(Alerts.escapeHtml(title));
        treeListItem.add(titleBox);

        if (!isFull) {
            TreeNode treeNode = trees.get(index);
            treeNode.setTreeListItem(treeListItem);
            if (WebContent.onIpad){
                titleBox.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        onItemClicked(index);
                    }
                });
                if (avatarBox != null){
                    avatarBox.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            onItemClicked(index);
                        }
                    });
                }
                if (index == selectedIndex){
                    treeListItem.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            onDefaultItemClicked();
                        }
                    });
                }
            } else {
                if (treeListItem != null){
                    treeListItem.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            onItemClicked(index);
                        }
                    });
                }
            }
        }
        boolean selected = index == selectedIndex;
        treeListItem.addStyleDependentName(isFull ? PopupElements.STYLE_FULL : PopupElements.STYLE_SELECTABLE);
        treeListItem.addStyleDependentName(PopupElements.STYLE_ITEM);

        if (selected) {
            //if (!WebContent.onIpad){
            treeListItem.addStyleDependentName(PopupElements.STYLE_SELECTED);
            //}
            selectedWidget = treeListItem;
        }
    }

    public static void onBackToList() {
        PopupControl.changeUI(PopupUIs.BackToList);
    }

    static void onGoAccount() {
        goDirectly = true;
        onDefaultItemClicked();
    }

    static void onGoToSyncAccount() {
        goToAccountSync(currentTabId, currentUrl);
        Alerts.closeWindow();
    }

    static void onClickedDirectly() {
        if (isClosing) {
            return;
        }
        isClosing = true;
        PearledDirect = true;
        pearlContent(currentTabId, currentUrl, currentTitle, getSelectedTree(), null, null);
    }

    static void onDefaultItemClicked() {
        onItemClicked(selectedIndex);
        //scrollToSelected();
    }

    static void onGoBackItemClicked() {
        String assoId = userTrees.getTreeAssoId(selectedIndex);
        String hash = AppClient.buildRevealHash(userTrees, selectedId, assoId);
        goBackToPT(hash);
        Timer t = new Timer() {

            @Override
            public void run() {
                Alerts.closeWindow();
            }
        };
        t.schedule(100);

        PopupControl.onViewCreated();
    }

    static void onEscapeItemClicked() {
        Alerts.closeWindow();
    }

    static void onItemClicked(final int index) {
        if (isClosing) {
            return;
        }
        isClosing = true;
        for (TreeNode tree : trees) {
            TreeListItem treeListItem = tree.getTreeListItem();
            if(treeListItem != null){
                treeListItem.removeStyleDependentName(PopupElements.STYLE_SELECTED);
                treeListItem.removeStyleDependentName(PopupElements.STYLE_SELECTED_TIMER);
                treeListItem.removeStyleDependentName(PopupElements.STYLE_SELECTABLE);
            }
        }

        Timer t = new Timer() {

            @Override
            public void run() {
                onSelectionTimer1(index);
            }
        };
        t.schedule(120);
    }

    static void onArrowClicked(final int index) {
        TreeNode treeNode = trees.get(index);
        TreeListItem treeListItem = treeNode.getTreeListItem();
        FlowPanel wrapper = (FlowPanel)treeListItem.getParent();
        ArrayList<Integer> treesToClose = new ArrayList<Integer>();
        ArrayList<Integer> treesToOpen = new ArrayList<Integer>();
        ArrayList<TreeNode> children = treeNode.getChildren();


        if (index == UserTrees.ROOTTREE_INDEX) {
            if (listdisplayed){
                list.removeStyleDependentName(PopupElements.STYLE_LIST_REVEALED);
                list.addStyleDependentName(PopupElements.STYLE_LIST_COLLAPSED);
                wrapper.addStyleDependentName(PopupElements.STYLE_COLLAPSED);
                listdisplayed = false;

                //Change the selected treenode
                selectedTreeNode = treeNode;
                selectedIndex = index;
                selectedId = userTrees.getTreeId(index);
                selectedWidget.removeStyleDependentName(PopupElements.STYLE_SELECTED);
                selectedWidget = treeListItem;
                selectedWidget.addStyleDependentName(PopupElements.STYLE_SELECTED);
                for (TreeNode child : children) {
                    TreeListItem childTreeListItem = child.getTreeListItem();
                    FlowPanel childWrapper = (FlowPanel)childTreeListItem.getParent();
                    boolean isOpen = !childWrapper.getStyleName().contains(PopupElements.STYLE_COLLAPSED);
                    ArrayList<TreeNode> children2 = child.getChildren();
                    boolean hasChildren = ( children2 != null && !children2.isEmpty() );
                    if (hasChildren && isOpen) {
                        onArrowClicked(trees.indexOf(child));
                    }
                }
                if (WebContent.onIpad){
                    treeListItem.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            onDefaultItemClicked();
                        }
                    });
                }
            } else {
                list.addStyleDependentName(PopupElements.STYLE_LIST_REVEALED);
                list.removeStyleDependentName(PopupElements.STYLE_LIST_COLLAPSED);
                wrapper.removeStyleDependentName(PopupElements.STYLE_COLLAPSED);
                FlowPanel childrenPanel = new FlowPanel();
                wrapper.add(childrenPanel);
                childrenPanel.setStylePrimaryName(PopupElements.PRIMARY_STYLE_CHILDRENPANEL);
                listdisplayed = true;
            }
            PopupControl.refreshScroll();
        } else {
            if (isInSelectedParentPath(index)) {
                selectedTreeNode = treeNode;
                selectedIndex = index;
                selectedId = userTrees.getTreeId(index);
                selectedWidget.removeStyleDependentName(PopupElements.STYLE_SELECTED);
                selectedWidget = treeListItem;
                selectedWidget.addStyleDependentName(PopupElements.STYLE_SELECTED);
                if (WebContent.onIpad){
                    treeListItem.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            onDefaultItemClicked();
                        }
                    });
                }
            }
            if (!wrapper.getStyleName().contains(PopupElements.STYLE_COLLAPSED)) {
                wrapper.addStyleDependentName(PopupElements.STYLE_COLLAPSED);
                treesToClose.add(index);
                removeChildrenFromPanel(wrapper);
                for (TreeNode child : children) {
                    ArrayList<TreeNode> children2 = child.getChildren();
                    Boolean hasChildren = ( children2 != null && !children2.isEmpty() );
                    if (hasChildren) {
                        treesToClose.add(trees.indexOf(child));
                    }
                }
                userTrees.updateTreeCollapsed(treesToClose, treesToOpen);
            }
            else {
                wrapper.removeStyleDependentName(PopupElements.STYLE_COLLAPSED);
                treesToOpen.add(index);
                FlowPanel childrenPanel = new FlowPanel();
                wrapper.add(childrenPanel);
                childrenPanel.setStylePrimaryName(PopupElements.PRIMARY_STYLE_CHILDRENPANEL);
                for (TreeNode child : children) {
                    ArrayList<TreeNode> children2 = child.getChildren();
                    Boolean hasChildren = ( children2 != null && !children2.isEmpty() );
                    if (hasChildren) {
                        treesToClose.add(trees.indexOf(child));
                    }
                }
                userTrees.updateTreeCollapsed(treesToClose, treesToOpen);
                addChildrenToPanel(children, childrenPanel);
            }
            PopupControl.refreshScroll();
        }
    }

    public static String buildAvatarUrl(String avatarHash) {
        String twoFirst = avatarHash.substring(0, 2);
        String twoFollowing = avatarHash.substring(2, 4);
        return WebContent.getCDNLocations().substring(0, WebContent.getCDNLocations().length()-1) + twoFirst + "/" + twoFollowing + "/" + avatarHash + ".pearlBigger.png";
    }

    public static String getTreeAvatarURL(int i) {
        String avatarHash =  userTrees.getTreeAvatarHash(i);
        if (avatarHash != null) {
            return buildAvatarUrl(avatarHash);
        }
        Integer assoIndex = assoIds2Index.get(userTrees.getTreeAssoId(i));
        if (assoIndex != null) {
            String avatarHashParent =  userTrees.getTreeAvatarHash(assoIndex);
            if (avatarHashParent != null) {
                return buildAvatarUrl(avatarHashParent);
            }
        }
        return WebContent.getCDNLocations().substring(0, WebContent.getCDNLocations().length()-1) + "default/default.pearlBigger.png";
    }

    static void onSelectionTimer1(final int index) {
        TreeNode treeNode = trees.get(index);
        TreeListItem treeListItem = treeNode.getTreeListItem();
        if (!WebContent.onIpad){
            treeListItem.addStyleDependentName(PopupElements.STYLE_SELECTED);
        } else {
            treeListItem.addStyleDependentName(PopupElements.STYLE_SELECTED_TIMER);
        }
        Timer t = new Timer() {

            @Override
            public void run() {
                onSelectionTimer2(index);
            }
        };
        t.schedule(120);
    }

    static void onSelectionTimer2(final int index) {
        onTreeSelected(index);
    }

    private static void onTreeSelected(final int index) {
        String id = userTrees.getTreeId(index);
        String assoId = userTrees.getTreeAssoId(index);
        if (chooseParent) {
            pearlContent(currentTabId, currentUrl, currentTitle, null, id, NewTreeUI.newTreeTitle);
            if (!WebContent.bookmarklet) {
                Alerts.setButtonIcon(false, true);
                PopupControl.changeUI(PopupUIs.TreeCreated);
            }
        }
        else if (goDirectly) {
            if (!WebContent.onIpad) {
                setSelectedTree(id, assoId);
                reveal(id, assoId, currentTabId, currentUrl);
                Alerts.closeWindow();
            } else {
                isClosing = false;
                setSelectedTree(id, assoId);
                TreeNode treeNode = trees.get(index);
                TreeListItem treeListItem = treeNode.getTreeListItem();
                treeListItem.addStyleDependentName(PopupElements.STYLE_SELECTED);
                treeListItem.removeStyleDependentName(PopupElements.STYLE_SELECTED_TIMER);
                String hash = AppClient.buildRevealHash(userTrees, id, assoId);
                goBackToPT(hash);
                //Alerts.closeWindow();
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
        else {
            setSelectedTree(id, assoId);
            pearlContent(currentTabId, currentUrl, currentTitle, id, null, null);
            if (!WebContent.bookmarklet) {
                Alerts.setButtonIcon(false, true);
                Alerts.closeWindow();
            }
        }
    }

    static void onSwichModeClicked() {
        goDirectly = !goDirectly;
        PopupControl.changeUI(PopupUIs.TreeList);
    }

    /**
     * Used in JS.
     */
    static void onTab(JavaScriptObject tab, int tabId, String url, String title) {
        currentTabId = tabId;
        currentUrl = url;
        currentTitle = title;
        init();
    }
    private static native void scrollToSelected() /*-{
    $wnd.setTimeout(function () {
         scrollToSelectedElement();;
    }, 200);
     }-*/;

    public static native void goBackToPT(String hash) /*-{
       goToPT(hash);
    }-*/;

    public static native void getTab() /*-{
        $wnd.getTab($entry(@com.broceliand.pearlbar.chrome.client.popup.TreeListUI::onTab(Lcom/google/gwt/core/client/JavaScriptObject;ILjava/lang/String;Ljava/lang/String;)));
    }-*/;

    private static native void pearlContent(int tab, String url, String title, String treeId, String parentTreeId, String newTreeName) /*-{
        $wnd.pearlContent(tab, url, title, treeId, parentTreeId, newTreeName);
    }-*/;

    private static native void reveal(String treeId, String assoId, int tabId, String url) /*-{
        $wnd.reveal(treeId, assoId, tabId, url);
    }-*/;

    private static native void goToAccountSync(int tabId, String currentUrl) /*-{
        $wnd.goToAccountSync(tabId, currentUrl);
    }-*/;

    private static int obtainSelectedTree(boolean includeDropzone) {
        String sel = getSelectedTree();
        if (sel != null) {
            for (int i = userTrees.getTreeCount(); i-- > 0;) {
                if (!userTrees.getTreeId(i).equals(sel)) {
                    continue;
                }
                if ((chooseParent || !goDirectly) && i > UserTrees.ROOTTREE_INDEX && userTrees.isTreeFull(i)) {
                    break;
                }
                if (includeDropzone || i != UserTrees.DROPZONE_INDEX) {
                    return i;
                }
                break;
            }
        }
        return UserTrees.ROOTTREE_INDEX;
    }

    private static native String getSelectedTree() /*-{
        return $wnd.getSelectedTree();
    }-*/;

    private static native void setSelectedTree(String id, String assoId) /*-{
        $wnd.setSelectedTree(id, assoId);
    }-*/;

    public static boolean isGoMode() {
        return goDirectly && !chooseParent;
    }
}
