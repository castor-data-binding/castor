/*
 * Copyright 2006 Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.exolab.javasource;

/**
 * Represents a line of code, used by JSourceCode class.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 6668 $ $Date: 2005-05-08 12:32:06 -0600 (Sun, 08 May 2005) $
 * @since 1.1
 */
public final class JCodeStatement {
    //--------------------------------------------------------------------------

    /** Contents of this code statement. */
    private final StringBuffer _value;
    
    /** Indentation depth of this statement. */
    private short _indentSize = JSourceCode.DEFAULT_INDENT_SIZE;

    //--------------------------------------------------------------------------

    JCodeStatement() {
        super();
        
        _value = new StringBuffer();
    }

    JCodeStatement(final String statement) {
        this();
        
        _value.append(statement);
    }

    JCodeStatement(final String statement, final short indentSize) {
        this(statement);
        
        _indentSize = indentSize;
    }

    //--------------------------------------------------------------------------

    void append(final String segment) {
        _value.append(segment);
    }

    short getIndent() {
        return _indentSize;
    }

    String getStatement() {
        return _value.toString();
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(_indentSize + _value.length());
        for (int i = 0; i < _indentSize; i++) { sb.append(' '); }
        sb.append(_value.toString());
        return sb.toString();
    }

    //--------------------------------------------------------------------------
}
