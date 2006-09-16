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
 * THIS SOFTWARE IS PROVIDED BY THE CASTOR PROJECT AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * The CASTOR PROJECT OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2005 (C) Keith Visco. All Rights Reserved.
 *
 * Created on Feb 26, 2005
 *
 * $Id$
 */

package org.exolab.javasource;

/**
 * Represents a class name
 * 
 * @author <a href="mailto:keith (at) kvisco (dot) com">Keith Visco</a>
 * @version $REVISION$ $DATE$
 */
public class JTypeName {

    private String _package   = null;
    private String _qName     = null;
    private String _localName = null;
        
    /**
     * Creates a default JTypeName
     */
    public JTypeName() {
        super();
    }
    
    /**
     * Creates a new JTypeName with the given name
     * 
     * @param name the fully qualified class name
     */
    public JTypeName(String name) {
        super();
        init(name);
    } //-- JTypeName
    
    /**
     * Returns the local name of this JTypeName
     * 
     * @return the local name of this JTypeName
     */
    public String getLocalName() {
        return _localName;
    } //-- getLocalName
    
    /**
     * Returns the package name of this JTypeName
     * 
     * @return the package name of this JTypeName
     */
    public String getPackageName() {
        return _package;
    } //-- getPackageName
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof JTypeName) {
            
            JTypeName jname = (JTypeName)obj;
            String qn1 = jname.getQualifiedName();
            String qn2 = getQualifiedName();
            if (qn1 == qn2) return true;
            if (qn1 == null) return (qn2 == null);
            return qn1.equals(qn2);
        }
        return false;
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        String qName = getQualifiedName();
        if (qName != null) {
            return qName.hashCode();
        }
        return 0;
    }
    
    /**
     * Returns the qualified name of this JTypeName
     * 
     * @return the qualified name of this JTypeName
     */
    public String getQualifiedName() {
        if (_qName == null) {
            if (_localName != null) {
                if (_package != null)
                    _qName = _package + "." + _localName;
                else
                    _qName = _localName;
            }
            else {
                _qName = _package;
            }
        }
        return _qName;
    } //-- getQualifiedName
    
    /**
     * Parses the given name value and initializes the variables
     * 
     * @param name the name to initialize with
     */
    private void init(String name) {
        
        if (name == null) {
            _qName     = null;
            _localName = null;
            _package   = null;
        }
        else {
            _qName = name;
            _localName = name;
            
            int idx = -1;
            if ((idx = name.lastIndexOf('.')) > 0) {
                _package = name.substring(0, idx);
                _localName = name.substring(idx+1);
            }
        }
    } //-- init

    /**
     * Sets the local name for this JTypeName. Setting the local name will
     * modify the existing local name and will reset the existing qualified
     * name.
     * 
     * @param localName the local name to set
     */
    public void setLocalName(String localName) {
        _localName = localName;
        _qName = null;
    } //-- setLocalName
    
    /**
     * Sets the package name of this JTypeName. Setting the package name will
     * modify the existing package name and will reset the existing qualified
     * name.
     * 
     * @param packageName the package name to set
     */
    public void setPackageName(String packageName) {
        _package = packageName;
        _qName = null;
    } //-- setPackageName
    
    /**
     * Sets the qualified name of this JTypeName. Setting the qualified name
     * will overwrite any previous values set via calls to {@link #setLocalName(String)}
     * and {@link #setPackageName(String)}.
     * 
     * @param qName the qualified name
     */
    public void setQualifiedName(String qName) {
        init(qName);
    } //-- setQualifiedName 
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getQualifiedName();
    }

}
