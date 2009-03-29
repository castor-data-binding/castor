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
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;
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
public class ImportDescriptor extends HandlerBase implements Serializable {
    /** SerialVersionUID. */
    private static final long serialVersionUID = 197365948293655041L;

    public static class Policy {
        /** Under the <code>DeleteEmpty</code> policy, entries without attributes in the
         *  DSML are deleted from the Ldap. */
        public static final int DELETE_EMPTY = 0x01;
        /** Under the <code>ReplaceAttr</code> policy, attributes in the Ldap that are not
         *  specified in the DSML are deleted. */
        public static final int REPLACE_ATTRIBUTE = 0x02;
        /** Under the <code>RefreshOnly</code> policy, DSML entries that do not already
         *  exist in the Ldap are not created. */
        public static final int REFRESH_ONLY = 0x04;
        /** Under the <code>NewAttrOnly</code> policy, new attributes are created according
         *  to the DSML, but attributes that already have a value in the ldap are not updated. */
        public static final int NEW_ATTRIBUTE_ONLY = 0x08;
        /** Under the <code>UpdateOnly</code> policy, DSML attributes that do not already
         *  exist in the Ldap are not created. */
        public static final int UPDATE_ONLY = 0x10;
        /** Under the <code>DefaultPolicy</code> policy. */
        public static final int DEFAULT_POLICY = 0x00;
    }

    static class Names {
        static class Element {
            public static final String POLICIES = "import-policies";
            public static final String POLICY = "import-policy";
        }

        static class Attribute {
            public static final String DN = "dn";
            public static final String DELETE_EMPTY = "delete-empty";
            public static final String REPLACE_ATTRIBUTE = "replace-attr";
            public static final String REFRESH_ONLY = "refresh-only";
            public static final String UPDATE_ONLY = "update-only";
            public static final String NEW_ATTRIBUTE_ONLY = "new-attr-only";
        }
    }
    
    private Hashtable _policies = new Hashtable();

    private boolean _insideRoot;

    public ImportDescriptor() {
    }

    public Enumeration listDNs() {
        return _policies.keys();
    }

    public void addPolicy(final String name, final int policy) {
        _policies.put(name, new Integer(policy));
    }

    public int getDirectPolicy(final String name) {
        Integer policy = (Integer) _policies.get(name);
        if (policy != null) { return policy.intValue(); }
        return Policy.DEFAULT_POLICY;
    }

    public int getPolicy(String name) {
        DN       dn;
        Integer  policy;
        int      i;
        
        policy = (Integer) _policies.get(name);
        if (policy != null) { return policy.intValue(); }
        dn = new DN( name );
        for (i = 1; i < dn.size(); ++i) {
            name = dn.suffix(i);
            policy = (Integer) _policies.get(name);
            if (policy != null) { return policy.intValue(); }
        }
        return Policy.DEFAULT_POLICY;
    }

    public void produce(final DocumentHandler docHandler) throws SAXException {
        AttributeListImpl attrList;
        int policy;
        Enumeration enumeration;
        String name;

        attrList = new AttributeListImpl();
        docHandler.startElement(XML.Namespace.ROOT, attrList);
        attrList = new AttributeListImpl();
        docHandler.startElement(Names.Element.POLICIES, attrList);

        enumeration = listDNs();
        while (enumeration.hasMoreElements()) {
            name = (String) enumeration.nextElement();
            policy = getDirectPolicy(name);
            attrList = new AttributeListImpl();
            attrList.addAttribute(Names.Attribute.DN, "ID", name);
            if ((policy & Policy.DELETE_EMPTY) != 0) {
                attrList.addAttribute(Names.Attribute.DELETE_EMPTY, null, "true");
            }
            if ((policy & Policy.REPLACE_ATTRIBUTE) != 0) {
                attrList.addAttribute(Names.Attribute.REPLACE_ATTRIBUTE, null, "true");
            }
            if ((policy & Policy.REFRESH_ONLY) != 0) {
                attrList.addAttribute(Names.Attribute.REFRESH_ONLY, null, "true");
            }
            if ((policy & Policy.UPDATE_ONLY) != 0) {
                attrList.addAttribute(Names.Attribute.UPDATE_ONLY, null, "true");
            }
            if ((policy & Policy.NEW_ATTRIBUTE_ONLY) != 0) {
                attrList.addAttribute(Names.Attribute.NEW_ATTRIBUTE_ONLY, null, "true");
            }
            docHandler.startElement(Names.Element.POLICY, attrList);
            docHandler.endElement(Names.Element.POLICY);
        }

        docHandler.endElement(Names.Element.POLICIES);
        docHandler.endElement(XML.Namespace.ROOT);
    }


    public void startElement(final String tagName, final AttributeList attr) throws SAXException {
        String dn;
        int policy;

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

            if (tagName.equals(Names.Element.POLICIES)) {
                // Nothing to do at level of top element
            } else if (tagName.equals(Names.Element.POLICY)) {
                dn = attr.getValue(Names.Attribute.DN);
                if (dn == null) {
                    throw new SAXException(Messages.format("dsml.missingAttribute",
                            Names.Element.POLICY, Names.Attribute.DN));
                }
                policy = 0;
                if ("true".equals(attr.getValue(Names.Attribute.DELETE_EMPTY))) {
                    policy = policy | Policy.DELETE_EMPTY;
                }
                if ("true".equals(attr.getValue(Names.Attribute.REFRESH_ONLY))) {
                    policy = policy | Policy.REFRESH_ONLY;
                }
                if ("true".equals(attr.getValue(Names.Attribute.REPLACE_ATTRIBUTE))) {
                    policy = policy | Policy.REPLACE_ATTRIBUTE;
                }
                if ("true".equals(attr.getValue(Names.Attribute.NEW_ATTRIBUTE_ONLY))) {
                    policy = policy | Policy.NEW_ATTRIBUTE_ONLY;
                }
                if ("true".equals(attr.getValue(Names.Attribute.UPDATE_ONLY))) {
                    policy = policy | Policy.UPDATE_ONLY;
                }
                addPolicy(dn, policy);
            } else {
                throw new SAXException(Messages.format(
                        "dsml.expectingOpeningTag", Names.Element.POLICIES, tagName));
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
            if (tagName.equals(Names.Element.POLICIES)) {
                // Nothing to do here
            } else if (tagName.equals(Names.Element.POLICY)) {
                // Nothing to do here
            } else {
                throw new SAXException(Messages.format("dsml.expectingClosingTag",
                        Names.Element.POLICIES, tagName));
            }
        }
    }

    static class DN {
        private String[] _names;

        DN(final String name) {
            StringTokenizer token;
            int i;

            token = new StringTokenizer(name, ", ");
            _names = new String[token.countTokens()];
            for (i = 0; token.hasMoreTokens(); ++i) {
                _names[ i ] = token.nextToken();
            }
        }

        int size() {
            return _names.length;
        }

        String suffix(int index) {
            StringBuffer name;

            name = new StringBuffer(_names[index]);
            for (++index; index < _names.length; ++index) {
                name.append(',').append(_names[index]);
            }
            return name.toString();
        }
    }
}
