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
 * $Id: Root.java 0000 2007-01-11 00:00:00Z ekuns $
 */
package org.exolab.castor.tests.framework.xmldiff.xml.nodes;

/**
 * A representation of a Root node.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-11 00:00:00 -0600 (Thu, 11 Jan 2007) $
 * @since Castor 1.1
 */
public class Root extends ParentNode {

    /** Serial Version UID. */
    private static final long   serialVersionUID = -4945943618209646133L;
    /** Local name for a root object is special. */
    private static final String ROOT_NAME = "#root";

    /**
     * Creates a new Root Node.  Basically, a root node can contain only
     * Elements and nothing else.
     */
    public Root() {
        super(null, ROOT_NAME, XMLNode.ROOT);
    }

    /**
     * Returns the root node, i.e., <code>this</code>.
     * @return The root node.
     */
    public XMLNode getRootNode() {
        return this;
    }

}
