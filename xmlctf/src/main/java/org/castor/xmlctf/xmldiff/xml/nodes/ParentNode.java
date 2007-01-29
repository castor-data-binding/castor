/*
 * Copyright 2007 Edward Kuns
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: ParentNode.java 0000 2007-01-11 00:00:00Z ekuns $
 */
package org.castor.xmlctf.xmldiff.xml.nodes;

import java.util.Iterator;
import java.util.LinkedList;


/**
 * The base object for both Element and RootNode.  The children of a ParentNode
 * can be any type of XMLNode.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-11 00:00:00 -0600 (Thu, 11 Jan 2007) $
 * @since Castor 1.1
 */
public abstract class ParentNode extends XMLNode {

    /** The list of all children.  Children may be any type of XMLNode. */
    private final LinkedList _children = new LinkedList();

    /**
     * Creates a new ParentNode.
     *
     * @param namespace the namespace URI for this node. (May be null.)
     * @param localName the local-name of this node. (Cannot be null.)
     * @param nodeType the node type being created
     */
    ParentNode(String namespace, String localName, int nodeType) {
        super(namespace, localName, nodeType);
    }

    /**
     * Returns an Iterator over the list of child nodes.
     * @return an Iterator over the list of child nodes.
     */
    public Iterator getChildIterator() {
        return _children.iterator();
    }

    /**
     * Returns true if this node has any child nodes.
     * @return True if this node has any child nodes.
     */
    public boolean hasChildNodes() {
        return !_children.isEmpty();
    }

    /**
     * Returns the string value of this parent node. The string value is the
     * concatenation of the string value of all child nodes.
     *
     * @return The string value of the node
     */
    public String getStringValue() {
        if (_children.isEmpty()) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        for (Iterator i = _children.iterator(); i.hasNext(); ) {
            XMLNode child = (XMLNode) i.next();
            sb.append(child.getStringValue());
        }
        return sb.toString();
    }

    /**
     * Adds the given child node to this ParentNode.
     *
     * @param node the child node to add
     */
    public void addChild(XMLNode node) {
        if (node == null) {
            return;
        }

        // Normalize text nodes if necessary
        XMLNode last = (_children.isEmpty()) ? null : (XMLNode) _children.getLast();
        if (last != null && last.getNodeType() == XMLNode.TEXT && node.getNodeType() == XMLNode.TEXT) {
            Text text = (Text) last;
            text.setValue(text.getStringValue() + node.getStringValue());
        } else {
            node.setParent(this);
            _children.add(node);
        }
    }

}
