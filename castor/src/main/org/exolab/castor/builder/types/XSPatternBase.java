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
 * Copyright 2000-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder.types;

import java.util.Vector;
import org.exolab.javasource.*;

/**
 * A base class for types which support the pattern facet
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public abstract class XSPatternBase extends XSType {


    /**
     * The value of the pattern facet
    **/
    private Vector _pattern = null;


    /**
     * Creates a new XSPatternBase
     * @param type that this XSType represents
    **/
    protected XSPatternBase(short type) {
        super(type);
    } //-- XSPatternBase

    /**
     * Creates a new XSPatternBase with the given regular
     * expression and type.
     *
     * @param type that this XSType represents
     * @param patterns a vector containing regular expressions
    **/
    public XSPatternBase(short type, Vector pattern) {
        super(type);
        _pattern = pattern;
    } //-- XSPatternBase

    /**
     * Creates a new XSPatternBase with the given regular
     * expression and type.
     *
     * @param type that this XSType represents
     * @param pattern the regular expression
    **/
    public XSPatternBase(short type, String pattern) {
        super(type);
        _pattern = new Vector();
        _pattern.add(pattern);
    } //-- XSPatternBase

    /**
     * Returns the pattern facet for this XSType
     *
     * @return the pattern facet for this XSType
    **/
    public String[] getPatterns() {
        if (_pattern == null)
            return null;
        else {
            int size = _pattern.size();
            String[] result = new String[size];
            int i = 0;
            while (i < size) {
                result[i] = (String)_pattern.get(i);         
                i++;     
            }
            return result;
        }
    } //-- setPattern

    /**
     * Sets the pattern facet for this XSType
     * @param pattern the regular expression for this XSType
    **/
    public void setPattern(Vector pattern) {
        _pattern = pattern;
    } //-- setPattern
    
    /**
     * Adds a pattern to the already existing patterns.
     * 
     * @param pattern a string representing a pattern to be added
     * to the existing list of patterns.
     */
    public void addPattern(String pattern) {
        if (_pattern == null)
            _pattern = new Vector();
        _pattern.add(pattern);
    }

} //-- XSPatternBase
