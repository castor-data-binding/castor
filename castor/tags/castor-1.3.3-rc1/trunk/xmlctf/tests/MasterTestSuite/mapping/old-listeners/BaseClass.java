/*
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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 */

/**
 * Simple implementation of some fields with a corresponding
 * mapping file to marshal/unmarshal.
 */
public class BaseClass implements org.castor.xmlctf.CastorTestable {

    private String      _attribute = "";
    private String      _element;
    protected BaseClass _child;
    private int         _numMods   = 0;

    //--
    public void setElement (String element) {
        _element = element;
    }

    public String getElement () {
        return _element;
    }

    //--
    public void setAttribute (String attr) {
        _attribute = attr;
    }

    public String getAttribute () {
        return _attribute;
    }

    public BaseClass getChild () {
        return _child;
    }

    public void setChild (BaseClass child) {
        _child = child;
    }

    public void setNumMods (int numMods) {
        _numMods = numMods;
    }

    public int getNumMods () {
        return _numMods;
    }

    /**
     * Testing depends on this, as the toString representation of an object is
     * used for logging the objects in the Listeners
     */
    public String toString () {
        return _attribute;
    }


    // --- CastorTestable ------------------------

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if ( ! (object instanceof BaseClass)) {
            return false;
        }

        BaseClass a = (BaseClass)object;

        if (_child != null) {
             if (a.getChild() == null)
                 return false;
             else if (!_child.equals(a.getChild()))
                 return false;
        }
        if (_element != null) {
            if (a.getElement() == null)
                return false;
            else if ( !_element.equals(a.getElement()) )
                 return false;
        }
        if (_attribute != null) {
            if (a.getAttribute() == null)
                return false;
            else if ( !_attribute.equals(a.getAttribute()) )
                return false;
        }
        return true;
    }

    public String dumpFields() {
        StringBuffer dump = new StringBuffer();

        dump.append("<" + getClass().getName());
        if ( _attribute != null ) dump.append(" attribute=\"" + _attribute + "\"");
        dump.append(">");

        if ( _element != null ) dump.append("<Element>" + _element + "</Element>");
        if ( _child != null ) {
            dump.append("<Child>");
            dump.append(_child.dumpFields());
            dump.append("</Child>");
        }

        dump.append("</" + getClass().getName() + ">");
        return dump.toString();
    }

    public void randomizeFields() throws InstantiationException, IllegalAccessException {
        // Not Implemented
    }

}
