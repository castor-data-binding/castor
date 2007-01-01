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
 */
package org.exolab.javasource;

/**
 * A class that represents a Java comment.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-02-26 17:30:28 -0700 (Sat, 26 Feb 2005) $
 */
public final class JComment {

    /**
     * The auto style, allows this JComment to automatically choose a style for
     * this comment.
     */
    public static final short AUTO_STYLE   = 0;

    /**
     * The block comment style. \/* *\/
     */
    public static final short BLOCK_STYLE  = 1;

    /**
     * The line comment style. \/\/
     */
    public static final short LINE_STYLE   = 2;

    /**
     * The header style, similiar to block, but with an '*' at the start of each
     * line.
     */
    public static final short HEADER_STYLE = 3;

    /**
     * Similiar to HEADER_STYLE.  But starts with: \/**
     */
    public static final short JAVADOC_STYLE = 4;

    private static final String START_BLOCK     = "/*";
    private static final String END_BLOCK       = " */";

    private static final String START_JAVADOC   = "/**";
    private static final String END_JAVADOC     = " */";

    private static final String ASTERIX_PREFIX          = " * ";
    private static final String LINE_COMMENT_PREFIX     = "// ";
    private static final String SPACE_PREFIX            = " ";

    /**
     * The style of this comment.
     */
    private short _style = AUTO_STYLE;

    /**
     * The main comment for this JDocComment.
     */
    private StringBuffer _comment = null;

    /** The maximum number of characters per line. */
    protected static final int MAX_LENGTH = 65;

    /** The maximum number of characters to indent comments. */
    protected static final int MAX_INDENT = 17;

    /**
     * Creates a new Java Comment.
     */
    public JComment() {
        super();
        _comment = new StringBuffer();
    } //-- JComment

    /**
     * Creates a new Java comment with the given style.
     * @param style the desired style
     */
    public JComment(final short style) {
        this();
        this._style = style;
    } //-- JComment

    /**
     * Appends the comment String to this JDocComment.
     *
     * @param comment the comment to append
     */
    public void appendComment(final String comment) {
        _comment.append(comment);
    } //-- appendComment

    /**
     * Prints this JComment using the given JSourceWriter.
     *
     * @param jsw the JSourceWriter to print to
     */
    public void print(final JSourceWriter jsw) {
        if (jsw == null) { return; }

        LineFormatter formatter = null;

        //-- calculate comment length
        short currentIndent = jsw.getIndentSize();
        int maxLength = MAX_LENGTH - currentIndent;

        //-- a simple check to make sure we have some room to print the comment
        if (maxLength <= MAX_INDENT) { maxLength = MAX_LENGTH / 2; }

        short resolvedStyle = _style;

        if (_style == AUTO_STYLE) {
            //-- estimation of number of lines
            int nbrLines = _comment.length() / maxLength;

            if (nbrLines > 2) {
                resolvedStyle = BLOCK_STYLE;
            } else {
                resolvedStyle = LINE_STYLE;
            }
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

        if (start != null) { jsw.writeln(start); }
        //-- print main comment
        formatter = new LineFormatter(_comment.toString(), maxLength, prefix);
        while (formatter.hasMoreLines()) {
            jsw.writeln(formatter.nextLine());
        }
        if (end != null) { jsw.writeln(end); }
        jsw.flush();
    } //-- print

    /**
     * Sets the comment String of this JDocComment.
     *
     * @param comment the comment String of this JDocComment
     */
    public void setComment(final String comment) {
        _comment.setLength(0);
        _comment.append(comment);
    } //-- setComment

    /**
     * Sets the style for this JComment.
     *
     * @param style the style to use for this JComment
     */
    public void setStyle(final short style) {
        this._style = style;
    } //-- setStyle

    /**
     * Returns the String representation of this Java Doc Comment.
     *
     * @return the String representation of this Java Doc Comment.
     */
    public String toString() {
        return "";
    } //-- toString

} //-- JComment

/**
 * Formats a given String for use within a Java comment.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 */
class LineFormatter {

    private String _comment = null;

    private int _maxLength = JComment.MAX_LENGTH;
    private int _offset = 0;
    private int _length = 0;

    private String _prefix = null;

    private StringBuffer _sb = null;

    /**
     * Creates a LineFormatter for the given comment.
     *
     * @param comment the String to format
     */
    LineFormatter(final String comment) {
        this._comment = comment;
        if (comment != null) { this._length = comment.length(); }
        _sb = new StringBuffer();
    } //-- LineFormatter

    /**
     * Creates a new LineFormatter for the given comment.
     *
     * @param comment the String to format
     * @param maxLength the maximum number of characters per line
     */
    LineFormatter(final String comment, final int maxLength) {
        this(comment, maxLength, null);
    } //-- LineFormatter

    /**
     * Creates a new LineFormatter for the given comment.
     *
     * @param comment the String to format
     * @param maxLength the maximum number of characters per line
     * @param prefix a prefix to append to the beginning of each line
     */
    LineFormatter(final String comment, final int maxLength, final String prefix) {
        this(comment);
        this._maxLength = maxLength;
        this._prefix = prefix;
    } //-- LineFormatter

    boolean hasMoreLines() {
        if (_comment == null) { return false; }
        return (_offset < _length);
    } //-- isFinished

    String nextLine() {
        if (_comment == null) { return null; }
        if (_offset >= _length) { return null; }

        _sb.setLength(0);
        if (_prefix != null) { _sb.append(_prefix); }

        int max = _offset + _maxLength;
        if (max > this._length) { max = this._length; }

        int index = _offset;
        int breakable = _offset;
        for ( ; index < max; index++) {
            char ch = _comment.charAt(index);
            if (isNewLine(ch)) {
                _sb.append(_comment.substring(_offset, index));
                _offset = index + 1;
                return _sb.toString();
            }
            if (isWhitespace(ch)) { breakable = index; }
        }

        if (index < _length - 1) {
            //-- if we could not find a breakable character, we must look
            //-- ahead
            if (_offset == breakable) {
                while (index < _length) {
                    if (isBreakable(_comment.charAt(index))) { break; }
                    ++index;
                }
            } else {
                index = breakable;
            }
        }
        _sb.append(_comment.substring(_offset, index));
        _offset = index + 1;
        return _sb.toString();
    } //-- getNextLine

    /**
     * Sets the prefix that should be appended to the beginning of each line.
     *
     * @param prefix the prefix for this LineFormatter
     */
    void setPrefix(final String prefix) {
        this._prefix = prefix;
    } //-- setPrefix

    /**
     * Returns true if we can break a line at this character.
     *
     * @param ch character to examine
     * @return true if we can break a line at this character.
     */
    private boolean isBreakable(final char ch) {
        return (isWhitespace(ch) || isNewLine(ch));
    }

    /**
     * Returns true if this character is whitespace.
     *
     * @param ch character to examine
     * @return true if this character is whitespace.
     */
    private boolean isWhitespace(final char ch) {
        return ((ch == ' ') || (ch == '\t'));
    } //-- isWhitespace

    /**
     * Returns true if this character is a new line character.
     *
     * @param ch character to examine
     * @return true if this character is a new line character.
     */
    private boolean isNewLine(final char ch) {
        return ((ch == '\n') || (ch == '\r'));
    } //-- isNewLineChar

} //-- LineFormatter
