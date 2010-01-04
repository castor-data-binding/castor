package org.castor.xmlctf.bestpractise.withmapping;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class NodeList {
    
    private List nodes = new ArrayList();;

    public List getNodes() {
        return nodes;
    }

    public void setNodes(List nodes) {
        this.nodes = nodes;
    }
    
    public void addNode(Node node) {
        nodes.add(node);
    }

    public boolean equals(Object obj) {
        if (getNodes().containsAll(((NodeList) obj).getNodes()) &&
                ((NodeList) obj).getNodes().containsAll(nodes)) {
            return true;
        }
        return false;
    }
    
    

}