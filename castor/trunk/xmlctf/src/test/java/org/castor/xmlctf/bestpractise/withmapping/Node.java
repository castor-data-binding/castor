package org.castor.xmlctf.bestpractise.withmapping;

import java.util.Iterator;
import java.util.List;

public class Node {
    protected int nodeNum;

    protected List siblings;

    public Node() {
    };

    public Node(int nodeNum, List siblings) {
        this.nodeNum = nodeNum;
        this.siblings = siblings;
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(int nodeNum) {
        this.nodeNum = nodeNum;
    }

    public List getSiblings() {
        return siblings;
    }

    public void setSiblings(List siblings) {
        this.siblings = siblings;
    }

    public boolean equals(Object obj) {
        Node node = (Node) obj;
        if (node.getNodeNum() != getNodeNum()) {
            return false;
        }
        
        Iterator siblingIter = getSiblings().iterator();
        Iterator iter = node.getSiblings().iterator();
        while (iter.hasNext()) {
            Node siblingNode = (Node) iter.next();
            Node sibling = (Node) siblingIter.next();
            
            if (siblingNode.getNodeNum() != sibling.getNodeNum()) {
                return false;
            }
        }
        
        return true;
    }
}