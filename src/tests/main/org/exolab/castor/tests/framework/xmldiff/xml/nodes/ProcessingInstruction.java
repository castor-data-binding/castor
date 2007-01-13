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
 * $Id: ProcessingInstruction.java 0000 2007-01-11 00:00:00Z ekuns $
 */
package org.exolab.castor.tests.framework.xmldiff.xml.nodes;

/**
 * A class representing an XML Processing Instruction.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-11 00:00:00 -0600 (Thu, 11 Jan 2007) $
 * @since Castor 1.1
 */
public class ProcessingInstruction extends XMLNode {

    /** Serial Version UID. */
    private static final long serialVersionUID = 4502831396237763129L;
    /** Value of the processing instructions. */
    private final String      _value;

    /**
     * Creates a new ProcessingInstruction.
     *
     * @param target the target for this Processing Instruction. (May be null.)
     * @param value the value of this Processing Instruction. (May be null.)
     */
    public ProcessingInstruction(final String target, final String value) {
        super(null, target, XMLNode.PROCESSING_INSTRUCTION);
        _value = value;
    }

    /**
     * Returns the string value of the node. The string value of a processing
     * instruction is the instruction.
     *
     * @return The string value of the node.
     */
    public String getStringValue() {
        return _value;
    }

}
