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

package org.exolab.castor.dsml;

import java.io.Serializable;
import java.util.Vector;
import java.util.Enumeration;
import org.xml.sax.DocumentHandler;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;
import org.xml.sax.HandlerBase;
import org.xml.sax.helpers.AttributeListImpl;
import org.castor.core.util.Messages;

/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class SearchDescriptor extends HandlerBase implements Serializable {
    /** SerialVersionUID. */
    private static final long serialVersionUID = -6614367393322175115L;

    public static class Scope {
        public static final int ONE_LEVEL = 0;
        public static final int BASE = 1;
        public static final int SUB_TREE = 3;
    }

    static class Names {
        static class Element {
            public static final String SEARCH = "search";
            public static final String RETURN_ATTRIBUTE = "return-attr";
        }

        static class Attribute {
            public static final String ATTRIBUTE_NAME = "name";
            public static final String BASE_DN = "base";
            public static final String SCOPE = "scope";
            public static final String FILTER = "filter";
            public static final String SCOPE_ONE_LEVEL= "onelevel";
            public static final String SCOPE_BASE = "base";
            public static final String SCOPE_SUB_TREE = "subtree";
        }
    }

    private int _scope = Scope.BASE;

    private String _baseDN;

    private String _filter;

    private Vector _returnAttrs;

    private StringBuffer _attrName;

    private boolean _insideRoot;

    public SearchDescriptor() {
    }

    public int getScope() {
        return _scope;
    }

    public void setScope(final int scope) {
        _scope = scope;
    }

    public String getBaseDN() {
        return _baseDN;
    }

    public void setBaseDN(final String baseDN) {
        _baseDN = baseDN;
    }

    public String getFilter() {
        return _filter;
    }

    public void setFilter(final String filter) {
        _filter = filter;
    }

    public String[] getReturnAttrs() {
        if (_returnAttrs == null) { return null; }
        String[] array = new String[_returnAttrs.size()];
        _returnAttrs.copyInto(array);
        return array;
    }

    public Enumeration listReturnAttrs() {
        if (_returnAttrs == null) { return new Vector().elements(); }
        return _returnAttrs.elements();
    }

    public void addReturnAttr(final String attrName) {
        if (_returnAttrs == null) {
            _returnAttrs = new Vector();
        }
        if (!_returnAttrs.contains(attrName)) {
            _returnAttrs.addElement(attrName);
        }
    }


    public void produce(final DocumentHandler docHandler) throws SAXException {
        AttributeListImpl attrList;
        Enumeration       enumeration;

        attrList = new AttributeListImpl();
        docHandler.startElement(XML.Namespace.ROOT, attrList);

        attrList = new AttributeListImpl();
        if (_baseDN != null) {
            attrList.addAttribute(Names.Attribute.BASE_DN, "CDATA", _baseDN);
        }
        if (_filter != null) {
            attrList.addAttribute(Names.Attribute.FILTER, "CDATA", _filter);
        }
        switch (_scope) {
        case Scope.ONE_LEVEL:
            attrList.addAttribute(Names.Attribute.SCOPE, null,
                    Names.Attribute.SCOPE_ONE_LEVEL);
            break;
        case Scope.BASE:
            attrList.addAttribute(Names.Attribute.SCOPE, null,
                    Names.Attribute.SCOPE_BASE);
            break;
        case Scope.SUB_TREE:
            attrList.addAttribute(Names.Attribute.SCOPE, null,
                    Names.Attribute.SCOPE_SUB_TREE);
            break;
        }
        docHandler.startElement(Names.Element.SEARCH, attrList);

        if (_returnAttrs != null) {
            enumeration = _returnAttrs.elements();
            while (enumeration.hasMoreElements()) {
                attrList = new AttributeListImpl();
                attrList.addAttribute(Names.Attribute.ATTRIBUTE_NAME, "NMTOKEN",
                        (String) enumeration.nextElement());
                docHandler.startElement(Names.Element.RETURN_ATTRIBUTE, attrList);
                docHandler.endElement(Names.Element.RETURN_ATTRIBUTE);
            }
        }

        docHandler.endElement(Names.Element.SEARCH);
        docHandler.endElement(XML.Namespace.ROOT);
    }

    public void startElement(final String tagName, final AttributeList attr) throws SAXException {
        String value;

        if (tagName.equals(XML.Namespace.ROOT)) {
            // Flag when entering (and leaving) the root element.
            if (_insideRoot) {
                throw new SAXException(Messages.format(
                        "dsml.elementNested", XML.Namespace.ROOT));
            }
            _insideRoot = true;
        } else {
            if (!_insideRoot) {
                throw new SAXException(Messages.format(
                        "dsml.expectingOpeningTag", XML.Namespace.ROOT, tagName));
            }

            if (tagName.equals(Names.Element.SEARCH)) {
                _baseDN = attr.getValue(Names.Attribute.BASE_DN);
                if (_baseDN == null) {
                    throw new SAXException(Messages.format("dsml.missingAttribute",
                            Names.Element.SEARCH, Names.Attribute.BASE_DN));
                }
                _filter = attr.getValue(Names.Attribute.FILTER);
                value = attr.getValue(Names.Attribute.SCOPE);
                if (value != null) {
                    if (value.equals(Names.Attribute.SCOPE_ONE_LEVEL)) {
                        _scope = Scope.ONE_LEVEL;
                    } else if (value.equals(Names.Attribute.SCOPE_BASE)) {
                        _scope = Scope.BASE;
                    } else if (value.equals(Names.Attribute.SCOPE_SUB_TREE)) {
                        _scope = Scope.SUB_TREE;
                    } else {
                        throw new SAXException(Messages.format(
                                "dsml.invalidValue", Names.Attribute.SCOPE, value));
                    }
                }
            } else if (tagName.equals(Names.Element.RETURN_ATTRIBUTE)) {
                if (_baseDN == null) {
                    throw new SAXException(Messages.format(
                            "dsml.expectingOpeningTag", Names.Element.SEARCH, tagName));
                }
                // Create a string buffer, characters() will fill it up,
                // endElement() will add it to the list.
                _attrName = new StringBuffer();
            } else {
                throw new SAXException(Messages.format(
                        "dsml.expectingOpeningTag", Names.Element.SEARCH, tagName));
            }
        }
    }

    public void endElement(final String tagName) throws SAXException {
        if (tagName.equals(XML.Namespace.ROOT)) {
            if (_insideRoot) {
                _insideRoot = false;
            } else {
                throw new SAXException(Messages.format("dsml.closingOutsideRoot", tagName));
            }
        } else {
            if (!_insideRoot) {
                throw new SAXException(Messages.format("dsml.closingOutsideRoot", tagName));
            }
            if (tagName.equals(Names.Element.SEARCH)) {
                // Nothing to do hare
            } else if (tagName.equals(Names.Element.RETURN_ATTRIBUTE)) {
                if (_attrName.length() > 0) {
                    addReturnAttr(_attrName.toString());
                    _attrName = null;
                }
            } else {
                throw new SAXException(Messages.format(
                        "dsml.expectingClosingTag", Names.Element.SEARCH, tagName));
            }
        }
    }

    public void characters(final char[] ch, final int offset, final int length) {
        if (_attrName != null) {
            _attrName.append(ch, offset, length);
        }
    }
}


