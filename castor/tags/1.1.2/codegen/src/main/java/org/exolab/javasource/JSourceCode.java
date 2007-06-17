/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 */
package org.exolab.javasource;

import java.text.MessageFormat;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * A class for holding in-memory Java source code.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public final class JSourceCode {
    //--------------------------------------------------------------------------

    /** Default indentation size. */
    public static final int DEFAULT_INDENT_SIZE = 4;

    //--------------------------------------------------------------------------

    /** A list of JCodeStatements. */
    private Vector _source = null;
    
    /** The indent size. */
    private short _indentSize = DEFAULT_INDENT_SIZE;
    
    /** The current indent size. */
    private short _currentIndent = _indentSize;

    //--------------------------------------------------------------------------

    /**
     * Creates an empty JSourceCode.
     */
    public JSourceCode() {
        super();
        
        _source = new Vector();
    }

    /**
     * Creates a JSourceCode and adds the given String to its contents.
     *
     * @param sourceCode the source to add
     */
    public JSourceCode(final String sourceCode) {
        this();
        
        _source.addElement(new JCodeStatement(sourceCode, _currentIndent));
    }

    //--------------------------------------------------------------------------

    /**
     * Adds statement generated by replacing parameters of given pattern with given argument to
     * this JSourceCode. The statement will be added on a new line.
     *
     * @param pattern Pattern for the statement.
     * @param argument Argument to replace first parameter in pattern.
     */
    public void add(final String pattern, final Object argument) {
        add(pattern, new Object[] {argument});
    }

    /**
     * Adds statement generated by replacing parameters of given pattern with given arguments to
     * this JSourceCode. The statement will be added on a new line.
     *
     * @param pattern Pattern for the statement.
     * @param argument1 Argument to replace first parameter in pattern.
     * @param argument2 Argument to replace second parameter in pattern.
     */
    public void add(final String pattern, final Object argument1, final Object argument2) {
        add(pattern, new Object[] {argument1, argument2});
    }

    /**
     * Adds statements generated by replacing parameters of given pattern with given arguments to
     * this JSourceCode. Multiple statements can be separated by '\n' characters. Each statement
     * will be added on a new line. If that statement starts with whitespace characters it will
     * be indented.
     *
     * @param pattern Pattern for the statement.
     * @param arguments Arguments to replace parameters in pattern.
     */
    public void add(final String pattern, final Object[] arguments) {
        StringTokenizer tokenizer = new StringTokenizer(pattern, "\n");
        while (tokenizer.hasMoreTokens()) { addWithIndent(tokenizer.nextToken(), arguments); }
    }
    
    /**
     * Adds statement generated by replacing parameters of given pattern with given arguments to
     * this JSourceCode. The statement will be added on a new line. If the statement starts with
     * whitespace characters the statement will be indented.
     *
     * @param pattern Pattern for the statement.
     * @param arguments Arguments to replace parameters in pattern.
     */
    private void addWithIndent(final String pattern, final Object[] arguments) {
        if (pattern.startsWith(" ")) {
            indent();
            addWithIndent(pattern.substring(1), arguments);
            unindent();
        } else if ((pattern.indexOf('{') + 2) == pattern.indexOf('}')) {
            add(MessageFormat.format(pattern, arguments));
        } else {
            add(pattern);
        }
    }

    /**
     * Adds the given statement to this JSourceCode. The statement will be added
     * on a new line.
     *
     * @param statement The statement to add.
     */
    public void add(final String statement) {
        _source.addElement(new JCodeStatement(statement, _currentIndent));
    }

    /**
     * Adds the given statement to this JSourceCode. The statement will be added
     * on a new line and added with increased indent. This is a convenience
     * method for the sequence:
     * <code>
     * indent();
     * add(statement);
     * unindent();
     * </code>
     * @param statement The statement to add.
     */
    public void addIndented(final String statement) {
        indent();
        _source.addElement(new JCodeStatement(statement, _currentIndent));
        unindent();
    }

    /**
     * Appends the given String to the last line in this JSourceCode.
     *
     * @param segment The String to append.
     */
    public void append(final String segment) {
        if (_source.isEmpty()) {
            add(segment);
        } else {
            JCodeStatement jcs = (JCodeStatement) _source.lastElement();
            jcs.append(segment);
        }
    }

    /**
     * Clears all the code statements from this JSourceCode.
     */
    public void clear() {
        _source.removeAllElements();
    }

    /**
     * Copies the contents of this JSourceCode into the given JSourceCode.
     *
     * @param jsc The JSourceCode to copy this JSourceCode into.
     */
    public void copyInto(final JSourceCode jsc) {
        for (int i = 0; i < _source.size(); i++) {
             jsc.addCodeStatement((JCodeStatement) _source.elementAt(i));
        }
    }

    /**
     * Increases the current indent level by 1.
     */
    public void indent() {
        _currentIndent += _indentSize;
    }

    /**
     * Returns true if this JSourceCode is empty (ie. no source).
     *
     * @return True if this JSourceCode is empty.
     */
    public boolean isEmpty() {
        return _source.isEmpty();
    }

    /**
     * Prints this JSourceCode to the given JSourceWriter.
     *
     * @param jsw The JSourceWriter to print to.
     */
    public void print(final JSourceWriter jsw) {
        for (int i = 0; i < _source.size(); i++) {
            jsw.writeln(_source.elementAt(i).toString());
        }
    }

    /**
     * Decreases the indent level by 1.
     */
    public void unindent() {
        _currentIndent -= _indentSize;
    }

    /**
     * Returns the number of source code lines (aka JCodeStatements) set so far.
     * 
     * @return The number of source code lines set so far.
     */
    public int size() {
        return _source.size();
    }

    /**
     * Adds the given JCodeStatement to this JSourceCode.
     *
     * @param jcs The JCodeStatement to add.
     */
    private void addCodeStatement(final JCodeStatement jcs) {
        short indent = (short) (jcs.getIndent() + _currentIndent - DEFAULT_INDENT_SIZE);
        _source.addElement(new JCodeStatement(jcs.getStatement(), indent));
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        String lineSeparator = System.getProperty("line.separator");
        for (int i = 0; i < _source.size(); i++) {
            sb.append(_source.elementAt(i).toString());
            sb.append(lineSeparator);
        }
        return sb.toString();
    }
    
    //--------------------------------------------------------------------------
}
