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
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;


import java.util.StringTokenizer;

/**
 * A class to represent the values of the XML Schema block property
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
**/
public final class FinalList {
    
    /**
     * The #all value
    **/
    public static final String ALL          = "#all";
    
    /**
     * The extension value
    **/
    public static final String EXTENSION    = "extension";
    
    /**
     * The restriction value
    **/
    public static final String RESTRICTION  = "restriction";
    
    
    private boolean _all = false;
    
    /**
     * Flag for blocking extensions
    **/
    private boolean _extension = false;
        
    /**
     * Flag for blocking restrictions
    **/
    private boolean _restriction = false;
    
    /**
     * Creates a new default FinalList. Nothing is flagged
     * as being final.
    **/
    public FinalList() {
        super();
    } //-- FinalList
    
    /**
     * Creates a new FinalList using the given list of values.
     *
     * @param listOfValues the list of final values
     * @exception IllegalArgumentException if the list of values contains
     * something other than "#all", "extension", "restriction".
    **/
    public FinalList(String listOfValues) {
        
        super();
        
        if (listOfValues != null) {
            parseValues(listOfValues);
        }
        
    } //-- FinalList
    
    /**
     * Returns true if the FinalList contains "#all".
     *
     * @return true if the FinalList contains "#all".
    **/
    public boolean hasAll() {
        return _all;
    } //-- hasAll
    
    /**
     * Returns true if extension is contained within this FinalList.
     *
     * @return true if extension is contained within this FinalList.
    **/
    public boolean hasExtension() {
        return _extension;
    } //-- hasExtension
    
    /**
     * Returns true if restriction is contained within this FinalList.
     *
     * @return true if restriction is contained within this FinalList.
    **/
    public boolean hasRestriction() {
        return _restriction;
    } //-- hasRestriction
    
    /**
     * Returns the String representation of this FinalList.
     *
     * @return the String representation of this FinalList.
    **/
    public String toString() {
        if (_all) return ALL;

        StringBuffer value = new StringBuffer();
        if (_extension) {
            value.append(EXTENSION);
        }
        if (_restriction) {
            if (value.length() > 0) {
                value.append(' ');
            }
            value.append(RESTRICTION);
        }
        return value.toString();
    } //-- toString
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    /**
     * Parses the given values and sets the appropriate flags for this 
     * FinalList.
     *
     * @param values the list of Final values.
    **/
    private void parseValues(String values) {
        
        if (ALL.equals(values)) {
            _all = true;
            return;
        }
        
        StringTokenizer tokenizer = new StringTokenizer(values);
        
        while (tokenizer.hasMoreTokens()) {
            String value = tokenizer.nextToken();
            
            if (EXTENSION.equals(value)) {
                _extension = true;
            }
            else if (RESTRICTION.equals(value)) {
                _restriction = true;
            }
            else {
                String err = "invalid final list: " + values;
                throw new IllegalArgumentException(err);
            }
        }
            
    } //-- parseValues
    
} //-- class: FinalList
