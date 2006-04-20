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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.javasource;

import java.io.Writer;

/**
 * The writer used by the javasource classes
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class JSourceWriter extends Writer {

    /**
     * The default character to use for indentation
    **/
    public static final char DEFAULT_CHAR = ' ';

    /**
     * The default indentation size
    **/
    public static final short DEFAULT_SIZE = 4;

    /**
     * The line separator to use for the writeln methods
    **/
    private String lineSeparator = System.getProperty("line.separator");

    /**
     * Flag for indicating whether we need to add
     * the whitespace to beginning of next write
     * call
    **/
    private boolean addIndentation = true;

    /**
     * A flag indicating whether this JSourceWriter should perform
     * autoflush at the end of a new line
    **/
    private boolean autoflush = false;

    /**
     * The tab (indentation) size
    **/
    private short tabSize = DEFAULT_SIZE;

    /**
     * The tab representation
    **/
    private char[] tab;

    /**
     * The character to use for indentation
    **/
    private char tabChar = DEFAULT_CHAR;

    /**
     * The current tab level
    **/
    private short tabLevel = 0;

    /**
     * The writer to send all output to
    **/
    private Writer out = null;

    
    /**
     * Creates a new JSourceWriter
     * @param out the Writer to write the actual output to
    **/
    public JSourceWriter(Writer out) {
        this(out, DEFAULT_SIZE, DEFAULT_CHAR, false);
    } //-- JSourceWriter

    /**
     * Creates a new JSourceWriter
     * @param out the Writer to write the actual output to
     * @param autoflush a boolean indicating whether or not to
     * perform automatic flush at the end of a line
    **/
    public JSourceWriter(Writer out, boolean autoflush) {
        this(out, DEFAULT_SIZE, DEFAULT_CHAR, autoflush);
    } //-- JSourceWriter

    /**
     * Creates a new JSourceWriter
     * @param out the Writer to write the actual output to
     * @param tabSize the size of each indentation
     * @param autoflush a boolean indicating whether or not to
     * perform automatic flush at the end of a line
    **/
    public JSourceWriter
        (Writer out, short tabSize, boolean autoflush)
    {
        this(out, tabSize, DEFAULT_CHAR, autoflush);
    } //-- JSourceWriter

    /**
     * Creates a new JSourceWriter
     * @param out the Writer to write the actual output to
     * @param tabSize the size of each indentation
     * @param tabChar the character to use for indentation
     * @param autoflush a boolean indicating whether or not to
     * perform automatic flush at the end of a line
    **/
    public JSourceWriter
        (Writer out, short tabSize, char tabChar, boolean autoflush)
    {
        this.out = out;
        this.autoflush = autoflush;
        this.tabChar = tabChar;
        this.tabSize = tabSize;
        createTab();
    } //-- JSourceWriter

    /**
     * Returns the line separator being used by this JSourceWriter
     * @return the line separator being used by this JSourceWriter
    **/
    public String getLineSeparator() {
        return lineSeparator;
    } //-- getLineSeparator

    
    /**
     * Increases the indentation level by 1
    **/
    public void indent() {
        ++tabLevel;
    } //-- increaseIndent

    /**
     * Checks to see if the cursor is positioned on a new line
     * @return true if the cursor is at the start of a new line, otherwise false
    **/
    public boolean isNewline() {
        //-- if we need to add indentation, we are on a new line
        return addIndentation;
    } //--  isNewline

    /**
     * Sets the line separator to use at the end of each line
     * @param lineSeparator the String to use as a line
     * separator. 
     * <BR />
     * Typically a line separator will be one of the following:
     * <BR />
     * "\r\n" for MS Windows<BR />
     * "\n"   for UNIX<BR />
     * "\r"   for Macintosh
    **/
    public void setLineSeparator(String lineSeparator) {

        this.lineSeparator = lineSeparator;

    } //-- setLineSeparator

    /**
     * Decreases the indentation level by 1
    **/
    public void unindent() {
        if (tabLevel > 0) --tabLevel;
    } //-- decreaseIndent


    //----------------------------/
    //- Additional write methods -/
    //----------------------------/

    public void write(float f) {
        write(String.valueOf(f));
    } //-- write(float)

    public void write(long l) {
        write(String.valueOf(l));
    } //-- write(long)

    public void write(double d) {
        write(String.valueOf(d));
    } //-- write(double)

    public void write(Object obj) {
        write(obj.toString());
    } //-- write(Object)

    public void write(boolean b) {
        write(String.valueOf(b));
    } //-- write(boolean)

    //- writeln() methods
    
    public void writeln() {
        synchronized (lock) {
            linefeed();
            addIndentation = true;
        }
    } //-- writeln

    public void writeln(float f) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(String.valueOf(f));
            }
            catch (java.io.IOException ioe) {};
            linefeed();
            addIndentation = true;
        }
    } //-- writeln(float)

    public void writeln(long l) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(String.valueOf(l));
            }
            catch (java.io.IOException ioe) {};
            linefeed();
            addIndentation = true;
        }
    } //-- writeln(long)

    public void writeln(int i) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(String.valueOf(i));
            }
            catch (java.io.IOException ioe) {};
            linefeed();
            addIndentation = true;
        }
    } //-- writeln(int)

    public void writeln(double d) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(String.valueOf(d));
            }
            catch (java.io.IOException ioe) {};
            linefeed();
            addIndentation = true;
        }
    } //-- writeln(double)

    
    public void writeln(Object obj) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(obj.toString());
            }
            catch (java.io.IOException ioe) {};
            linefeed();
            addIndentation = true;
        }
    } //-- writeln(Object)

    public void writeln(String string) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(string);
            }
            catch (java.io.IOException ioe) {};
            linefeed();
            addIndentation = true;
        }
    } //-- writeln(String)

    public void writeln(char[] chars) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(chars);
            }
            catch (java.io.IOException ioe) {};
            linefeed();
            addIndentation = true;
        }
    } //-- writeln(char[])

    public void writeln(boolean b) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(String.valueOf(b));
            }
            catch (java.io.IOException ioe) {};
            linefeed();
            addIndentation = true;
        }
    } //-- writeln(boolean)

    public void writeln(char c) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(c);
            }
            catch (java.io.IOException ioe) {};
            linefeed();
            addIndentation = true;
        }
    } //-- writeln(char)

    
    //-----------------------/
    //- Methods from Writer -/
    //-----------------------/

    public void close() {
        try {
            out.close();
        }
        catch(java.io.IOException ioe) {};
    } //-- close

    

    public void flush() {
        try {
            out.flush();
        }
        catch(java.io.IOException ioe) {};
    } //-- flush



    public void write(String s, int off, int len) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(s, off, len);
            }
            catch(java.io.IOException ioe) {};
            if (autoflush) flush();
        }
    } //-- write

    

    public void write(String s) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(s);
            }
            catch(java.io.IOException ioe) {};
            if (autoflush) flush();
        }
    } //-- write
    
    public void write(char[] buf) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(buf);
            }
            catch(java.io.IOException ioe) {};
            
            if (autoflush) flush();
        }
    } //-- write
    

    public void write(int c) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(c);
            }
            catch(java.io.IOException ioe) {};
            if (autoflush) flush();
        }
    } //-- write

    
    public void write(char[] buf, int off, int len) {
        synchronized (lock) {
            ensureIndent();
            try {
                out.write(buf, off, len);
            }
            catch(java.io.IOException ioe) {};
            if (autoflush) flush();
        }
    } //-- write

    //---------------------/
    //- Protected Methods -/
    //---------------------/
    
    protected short getIndentLevel() {
        return tabLevel;
    }

    /**
     * Returns the current indent size (getIndentLevel()*tabSize);
     * @return the current indent size
    **/
    protected short getIndentSize() {
        return (short)(tabLevel*tabSize);
    } //-- getIndentSize

    protected char getIndentChar() {
        return tabChar;
    }
    
    protected void writeIndent() {
        
        try {
            for (int i = 0; i < tabLevel; i++) out.write(tab);
        }
        catch(java.io.IOException ioe) {};
        
    } //-- writeIndent

    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    private void ensureIndent() {
        if (addIndentation) {
            writeIndent();
            addIndentation = false;
        }
    } //-- ensureIndent

    

    /**
     * writes the line separator character to the writer
    **/
    private void linefeed() {
        try {
            out.write(lineSeparator);
        }
        catch(java.io.IOException ioe) {};
    } //-- linefeed

    /**
     * Creates the tab from the tabSize and the tabChar
    **/
    private void createTab() {
        tab = new char[tabSize];
        for (int i = 0; i < tabSize; i++) {
            tab[i] = tabChar;
        }
    } //-- createTab

} //-- JSourceWriter

    

