package com.broceliand.pearlbar.chrome.client.common;

import java.util.*;


public class TreeNode {
    private TreeListItem treeListItem;
    private TreeNode parent;
    private ArrayList<TreeNode> children;
    
    public TreeNode(TreeNode parent) {
        super();
        this.parent = parent;
    }
    
    public TreeListItem getTreeListItem() {
        return treeListItem;
    }

    
    public void setTreeListItem(TreeListItem treeListItem) {
        this.treeListItem = treeListItem;
    }

    public TreeNode getParent() {
        return parent;
    }

    
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }
    
    public void addChild(TreeNode child) {
        if(children == null){
            children = new ArrayList<TreeNode>();
        }
        children.add(child);
    }
}
