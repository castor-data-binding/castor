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
    //--------------------------------------------------------------------------

    /** The auto style, allows this JComment to automatically choose a style for
     *  this comment. */
    public static final short AUTO_STYLE   = 0;

    /** The block comment style. \/* *\/ */
    public static final short BLOCK_STYLE  = 1;

    /** The line comment style. \/\/ */
    public static final short LINE_STYLE   = 2;

    /** The header style, similiar to block, but with an '*' at the start of each line. */
    public static final short HEADER_STYLE = 3;

    /** Similiar to HEADER_STYLE.  But starts with: \/** */
    public static final short JAVADOC_STYLE = 4;

    private static final String START_BLOCK     = "/*";
    private static final String END_BLOCK       = " */";

    private static final String START_JAVADOC   = "/**";
    private static final String END_JAVADOC     = " */";

    private static final String ASTERIX_PREFIX          = " * ";
    private static final String LINE_COMMENT_PREFIX     = "// ";
    private static final String SPACE_PREFIX            = " ";

    //--------------------------------------------------------------------------

    /** The style of this comment. */
    private short _style = AUTO_STYLE;

    /** The main comment for this JDocComment. */
    private StringBuffer _comment = null;

    /** The maximum number of characters per line. */
    protected static final int MAX_LENGTH = 65;

    /** The maximum number of characters to indent comments. */
    protected static final int MAX_INDENT = 17;

    //--------------------------------------------------------------------------

    /**
     * Creates a new Java Comment.
     */
    public JComment() {
        super();
        
        _comment = new StringBuffer();
    }

    /**
     * Creates a new Java comment with the given style.
     * 
     * @param style The desired style.
     */
    public JComment(final short style) {
        this();
        
        _style = style;
    }

    //--------------------------------------------------------------------------

    /**
     * Appends the comment String to this JDocComment.
     *
     * @param comment The comment to append.
     */
    public void appendComment(final String comment) {
        _comment.append(comment);
    }

    /**
     * Sets the comment String of this JDocComment.
     *
     * @param comment The comment String of this JDocComment.
     */
    public void setComment(final String comment) {
        _comment.setLength(0);
        _comment.append(comment);
    }

    /**
     * Sets the style for this JComment.
     *
     * @param style The style to use for this JComment.
     */
    public void setStyle(final short style) {
        _style = style;
    }

    //--------------------------------------------------------------------------

    /**
     * Prints this JComment using the given JSourceWriter.
     *
     * @param jsw The JSourceWriter to print to.
     */
    public void print(final JSourceWriter jsw) {
        if (jsw == null) { return; }

        JCommentFormatter formatter = null;

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
        formatter = new JCommentFormatter(_comment.toString(), maxLength, prefix);
        while (formatter.hasMoreLines()) {
            jsw.writeln(formatter.nextLine());
        }
        if (end != null) { jsw.writeln(end); }
        jsw.flush();
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "";
    }

    //--------------------------------------------------------------------------
}
