package ru.m210projects.bafeditor.ui.models;

import javax.swing.tree.DefaultMutableTreeNode;

public class ObjectTreeNode extends DefaultMutableTreeNode {
    private final Object value;
    public ObjectTreeNode(String name, Object value) {
        super(name);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

}
