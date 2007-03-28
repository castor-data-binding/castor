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
 * A class to format comments. 
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 6668 $ $Date: 2005-05-08 12:32:06 -0600 (Sun, 08 May 2005) $
 * @since 1.1
 */
public final class JCommentFormatter {
    //--------------------------------------------------------------------------

    private String _comment = null;
    private int _maxLength = JComment.MAX_LENGTH;
    private int _offset = 0;
    private int _length = 0;
    private String _prefix = null;
    private StringBuffer _sb = null;

    //--------------------------------------------------------------------------

    /**
     * Creates a new LineFormatter for the given comment.
     *
     * @param comment The String to format.
     * @param maxLength The maximum number of characters per line.
     * @param prefix A prefix to append to the beginning of each line.
     */
    public JCommentFormatter(final String comment, final int maxLength, final String prefix) {
        _comment = comment;
        if (comment != null) { _length = comment.length(); }
        _sb = new StringBuffer();
        _maxLength = maxLength;
        _prefix = prefix;
    }

    //--------------------------------------------------------------------------

    public boolean hasMoreLines() {
        if (_comment == null) { return false; }
        return (_offset < _length);
    }

    public String nextLine() {
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
    }

    /**
     * Returns true if we can break a line at this character.
     *
     * @param ch Character to examine.
     * @return True if we can break a line at this character.
     */
    private boolean isBreakable(final char ch) {
        return (isWhitespace(ch) || isNewLine(ch));
    }

    /**
     * Returns true if this character is whitespace.
     *
     * @param ch Character to examine.
     * @return True if this character is whitespace.
     */
    private boolean isWhitespace(final char ch) {
        return ((ch == ' ') || (ch == '\t'));
    }

    /**
     * Returns true if this character is a new line character.
     *
     * @param ch Character to examine.
     * @return True if this character is a new line character.
     */
    private boolean isNewLine(final char ch) {
        return ((ch == '\n') || (ch == '\r'));
    }

    //--------------------------------------------------------------------------
}
