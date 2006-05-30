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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.javasource;


/**
 * A class that represents a Java comment.
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-02-26 17:30:28 -0700 (Sat, 26 Feb 2005) $
 */
public class JComment {

        
    /**
     * The auto style, allows this JComment to automatically
     * choose a style for this comment
    **/
    public static final short AUTO_STYLE   = 0;
    
    /**
     * The block comment style: \/* *\/
    **/
    public static final short BLOCK_STYLE  = 1;
    
    /**
     * The line comment style: \/\/
    **/
    public static final short LINE_STYLE   = 2;
    
    /**
     * The header style, similiar to block, but with an '*'
     * at the start of each line.
    **/
    public static final short HEADER_STYLE = 3;
    
    /**
     * Similiar to HEADER_STYLE, but starts with: \/**
    **/
    public static final short JAVADOC_STYLE = 4;
    
    
    private static final String START_BLOCK     = "/*";
    private static final String END_BLOCK       = " */";
    
    private static final String START_JAVADOC   = "/**";
    private static final String END_JAVADOC     = " */";
    
    private static final String ASTERIX_PREFIX          = " * ";
    private static final String LINE_COMMENT_PREFIX     = "// ";
    private static final String SPACE_PREFIX            = " ";
    
    
    /**
     * The style of this comment
    **/
    private short style = AUTO_STYLE;
    
    /**
     * The main comment for this JDocComment
    **/
    private StringBuffer _comment = null;
    
    /**
     * The maximum number of characters per line
    **/
    protected static final int MAX_LENGTH = 65;
    
    
    /**
     * Creates a new Java Comment
    **/
    public JComment() {
        super();
        _comment = new StringBuffer();
    } //-- JComment

    /**
     * Creates a new Java comment with the given style
    **/
    public JComment(short style) {
        this();
        this.style = style;
    } //-- JComment
    
    /**
     * Appends the comment String to this JDocComment
     * @param comment the comment to append
    **/
    public void appendComment(String comment) {
        _comment.append(comment);
    } //-- appendComment
    
    
    /**
     * prints this JComment using the given JSourceWriter
     * @param jsw the JSourceWriter to print to
    **/
    public void print(JSourceWriter jsw) {
        
        
        if (jsw == null) return; //-- nothing to do
        
        LineFormatter formatter = null;
        
        //-- calculate comment length
        short currentIndent = jsw.getIndentSize();
        int maxLength = MAX_LENGTH - currentIndent;
        
        //-- a simple to check to make sure we have some room
        //-- to print the comment
        if (maxLength <= 17) maxLength = MAX_LENGTH/2;
        
        short resolvedStyle = style;
        
        if (style == AUTO_STYLE) {
            //-- estimation of number of lines
            int nbrLines = _comment.length()/maxLength;
            
            if (nbrLines > 2) 
                resolvedStyle = BLOCK_STYLE;
            else 
                resolvedStyle = LINE_STYLE;
        }
        
        
        
        //-- start comment
        String prefix = null;
        String start  = null;
        String end    = null;
        
        switch(resolvedStyle) {
            case BLOCK_STYLE:
                start  = START_BLOCK;
                end    = END_BLOCK;
                prefix = SPACE_PREFIX;
                break;
            case HEADER_STYLE:
                start = START_BLOCK;
                end   = END_BLOCK;
                prefix = ASTERIX_PREFIX;
                break;
            case JAVADOC_STYLE:
                start = START_JAVADOC;
                end   = END_JAVADOC;
                prefix = ASTERIX_PREFIX;
                break;            
            default: //-- LINE
                prefix = LINE_COMMENT_PREFIX;
                break;
        }
        if (start != null) jsw.writeln(start);
        //-- print main comment
        formatter = new LineFormatter(_comment.toString(), maxLength, prefix);
        while(formatter.hasMoreLines()) {
            jsw.writeln(formatter.nextLine());
        }
        if (end != null) jsw.writeln(end);
        jsw.flush();
    } //-- print
    
    /**
     * Sets the comment String of this JDocComment
     * @param comment the comment String of this JDocComment
    **/
    public void setComment(String comment) {
        _comment.setLength(0);
        _comment.append(comment);
    } //-- setComment
    
    /**
     * Sets the style for this JComment
     * @param style the style to use for this JComment
    **/
    public void setStyle(short style) {
        this.style = style;
    } //-- setStyle
    
    /**
     * Returns the String representation of this Java Doc Comment
     * @return the String representation of this Java Doc Comment
    **/
    public String toString() {
        return "";
    } //-- toString
    
} //-- JComment

/**
 * Formats a given String for use within a Java comment
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
**/
class LineFormatter {
        
    String comment = null;
        
    int maxLength = 65;
    int offset = 0;
    int length = 0;
        
    String prefix = null;
    
    private StringBuffer sb = null;
    
    /**
     * Creates a LineFormatter for the given comment
     * @param comment the String to format
    **/
    LineFormatter(String comment) {
        this.comment = comment;
        if (comment != null) this.length = comment.length();
        sb = new StringBuffer();
    } //-- LineFormatter
        
     
    /**
     * Creates a new LineFormatter for the given comment
     * @param comment the String to format
     * @param maxLength the maximum number of characters per line
    **/
    LineFormatter(String comment, int maxLength) {
        this(comment, maxLength, null);
    } //-- LineFormatter
    
    
    /**
     * Creates a new LineFormatter for the given comment
     * @param comment the String to format
     * @param maxLength the maximum number of characters per line
     * @param prefix a prefix to append to the beginning of each line
    **/
    LineFormatter(String comment, int maxLength, String prefix) {
        this(comment);
        this.maxLength = maxLength;
        this.prefix = prefix;
    } //-- LineFormatter
        
    boolean hasMoreLines() {
        if (comment == null) return false;
        return (offset < length);
    } //-- isFinished
        
    String nextLine() {
        if (comment == null) return null;
        if (offset >= length) return null;
            
        sb.setLength(0);
        if (prefix != null) sb.append(prefix);
            
        int max = offset+maxLength;
        if (max > this.length) max = this.length;
        
        
        int index = offset;
        int breakable = offset;
        for ( ; index < max; index++) {
            char ch = comment.charAt(index);
            if (isNewLine(ch)) {
                sb.append(comment.substring(offset, index));
                offset = index+1;
                return sb.toString();
            }
            if (isWhitespace(ch)) breakable = index;
        }
        
        if (index < length-1) {
            //-- if we could not find a breakable character, we must look
            //-- ahead
            if (offset == breakable) {
                while(index < length) {
                    if (isBreakable(comment.charAt(index))) break;
                    ++index;
                }
            } 
            else index = breakable;
        }
        sb.append(comment.substring(offset, index));
        offset = index+1;
        return sb.toString();
    } //-- getNextLine
        
    /** 
     * Sets the prefix that should be appended to the beginning of
     * each line
     * @param prefix the prefix for this LineFormatter
    **/
    void setPrefix(String prefix) {
        this.prefix = prefix;
    } //-- setPrefix
        
    private boolean isBreakable(char ch) {
        return (isWhitespace(ch) || isNewLine(ch));
    }
    
    private boolean isWhitespace(char ch) {
        return ((ch == ' ') || (ch == '\t'));
    } //-- isWhitespace
    
    private boolean isNewLine(char ch) {
        return ((ch == '\n') || (ch == '\r'));
    } //-- isNewLineChar
    
} //-- LineFormatter

