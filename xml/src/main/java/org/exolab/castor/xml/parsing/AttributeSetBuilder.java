/*
 * Copyright 2005 Philipp Erlacher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.xml.parsing;

import org.apache.commons.lang.StringUtils;
import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.util.AttributeSetImpl;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A helper class that takes SAX v1 AttributeList or SAX v2 attributes and
 * converts those into Castor's internal {@link AttributeSet} representation.
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 * @since 1.3.2
 */
public class AttributeSetBuilder {

    /**
     * The built-in XML prefix used for xml:space, xml:lang and, as the XML 1.0
     * Namespaces document specifies, are reserved for use by XML and XML
     * related specs.
     **/
    private static final String XML_PREFIX = "xml";

    /**
     * Attribute name for default namespace declaration
     **/
    private static final String XMLNS = "xmlns";

    /**
     * Attribute prefix for prefixed namespace declaration.
     **/
    private final static String XMLNS_PREFIX = "xmlns:";

    /**
     * Length of the XMLNS prefix.
     */
    private final static int XMLNS_PREFIX_LENGTH = XMLNS_PREFIX.length();

    /**
     * Tool class to deal with XML name spaces.
     */
    private NamespaceHandling _namespaceHandling = null;

    /**
     * Creates an instance of this class.
     * 
     * @param namespaceHandling
     *            Instance of a tool class to handle XML name spaces.
     */
    public AttributeSetBuilder(NamespaceHandling namespaceHandling) {
        super();
        _namespaceHandling = namespaceHandling;
    }

    /**
     * Prepares a reusable {@link AttributeSet} object instance.
     * 
     * @param atts
     *            Attributes to determine the length of the reusable attribute
     *            object, can be null
     */
    private AttributeSetImpl prepareAttributeSetImpl(Attributes atts) {
            if (atts != null) {
                return new AttributeSetImpl(atts.getLength());
            }
            return new AttributeSetImpl();
    }

    /**
     * Processes the attributes and XML name space declarations found in the
     * given {@link Attributes} instance. XML namespace declarations are added
     * to the set of name spaces in scope.
     * 
     * @return AttributeSet,
     * @throws SAXException
     *             If a name space associated with the prefix could not be
     *             resolved.
     */
    public AttributeSet getAttributeSet(Attributes atts) throws SAXException {
        AttributeSetImpl attributeSet = prepareAttributeSetImpl(atts);
        return processAttributes(atts, attributeSet);
    }

    /**
     * Processes the attributes and XML name space declarations found in the
     * given SAX Attributes. The global {@link AttributeSet} is cleared and
     * updated with the attributes. XML name space declarations are added to the
     * set of name spaces in scope.
     * 
     * @param atts
     *            the Attributes to process (can be null).
     **/
    private AttributeSet processAttributes(Attributes atts, AttributeSetImpl attributeSet) {
        // -- process attributes

        if (atts == null || atts.getLength() == 0) {
            return attributeSet;
        }

        boolean hasQNameAtts = false;
        // -- look for any potential namespace declarations
        // -- in case namespace processing was disable
        // -- on the parser
        for (int i = 0; i < atts.getLength(); i++) {
            String attName = atts.getQName(i);
            if (StringUtils.isNotEmpty(attName)) {
                if (attName.equals(XMLNS)) {
                    _namespaceHandling.addDefaultNamespace(atts.getValue(i));
                } else if (attName.startsWith(XMLNS_PREFIX)) {
                    String prefix = attName.substring(XMLNS_PREFIX.length());
                    _namespaceHandling.addNamespace(prefix, atts.getValue(i));
                } else {
                    // -- check for prefix
                    if (attName.indexOf(':') < 0) {
                    	attributeSet.setAttribute(attName, atts.getValue(i), atts
                                .getURI(i));
                    } else
                        hasQNameAtts = true;
                }
            } else {
                // -- if attName is null or empty, just process as a normal
                // -- attribute
                attName = atts.getLocalName(i);
                if (XMLNS.equals(attName)) {
                    _namespaceHandling.addDefaultNamespace(atts.getValue(i));
                } else {
                	attributeSet.setAttribute(attName, atts.getValue(i), atts
                            .getURI(i));
                }
            }
        }

        // return if there are no qualified name attributes
        if (!hasQNameAtts) {
            return attributeSet;
        }
        // -- if we found any qName-only atts, process those
        for (int i = 0; i < atts.getLength(); i++) {
            String attName = atts.getQName(i);
            if (StringUtils.isNotEmpty(attName)) {
                // -- process any non-namespace qName atts
                if ((!attName.equals(XMLNS))
                        && (!attName.startsWith(XMLNS_PREFIX))) {
                    int idx = attName.indexOf(':');
                    if (idx >= 0) {
                        String prefix = attName.substring(0, idx);
                        attName = attName.substring(idx + 1);
                        String nsURI = atts.getURI(i);
                        if (StringUtils.isEmpty(nsURI)) {
                            nsURI = _namespaceHandling.getNamespaceURI(prefix);
                        }
                        attributeSet.setAttribute(attName, atts.getValue(i), nsURI);
                    }
                }
            }
            // -- else skip already processed in previous loop
        }
        return attributeSet;
    }

    /**
     * Processes the attributes and XML name space declarations found in the SAX
     * v1 {@link AttributeList}. XML name space declarations are added to the
     * set of XML name spaces in scope.
     * 
     * 
     * @return AttributeSet An internal representation of XML attributes.
     * @throws SAXException
     *             If the XML name space associated with the prefix could not be
     *             resolved.
     */
    public AttributeSet getAttributeSet(AttributeList atts) throws SAXException {
    	AttributeSetImpl attributeSet = prepareAttributeSetImpl(atts);
        return processAttributeList(atts, attributeSet);
    }

    /**
     * Processes the attributes and XML name space declarations found in the
     * given SAX v1 AttributeList. The global AttributeSet is cleared and
     * updated with the attribute data. XML name space declarations are added to
     * the set of XML name spaces in scope.
     * 
     * @deprecated
     * @param atts
     *            the {@link AttributeList} to process (can be null)
     **/
    private AttributeSet processAttributeList(AttributeList atts, AttributeSetImpl attributeSet) throws SAXException {
        if (atts == null || atts.getLength() == 0)
            return attributeSet;

        // -- process all namespaces first
        int attCount = 0;
        boolean[] validAtts = new boolean[atts.getLength()];
        for (int i = 0; i < validAtts.length; i++) {
            String attName = atts.getName(i);
            if (attName.equals(XMLNS)) {
                _namespaceHandling.addDefaultNamespace(atts.getValue(i));
            } else if (attName.startsWith(XMLNS_PREFIX)) {
                String prefix = attName.substring(XMLNS_PREFIX_LENGTH);
                _namespaceHandling.addNamespace(prefix, atts.getValue(i));
            } else {
                validAtts[i] = true;
                ++attCount;
            }
        }
        // -- process validAtts...if any exist
        for (int i = 0; i < validAtts.length; i++) {
            if (!validAtts[i])
                continue;
            String namespace = null;
            String attName = atts.getName(i);
            int idx = attName.indexOf(':');
            if (idx > 0) {
                String prefix = attName.substring(0, idx);
                if (!prefix.equals(XML_PREFIX)) {
                    attName = attName.substring(idx + 1);
                    namespace = _namespaceHandling.getNamespaceURI(prefix);
                    if (namespace == null) {
                        String error = "The namespace associated with "
                                + "the prefix '" + prefix
                                + "' could not be resolved.";
                        throw new SAXException(error);

                    }
                }
            }
            attributeSet.setAttribute(attName, atts.getValue(i), namespace);
        }
        return attributeSet;
    }

    /**
     * Prepares a reusable AttributeSet object.
     * 
     * @deprecated
     * @param atts
     *            {@link Attributes} to determine the length of the reusable
     *            {@link AttributeSet} object (can be null).
     */
    private AttributeSetImpl prepareAttributeSetImpl(AttributeList atts) {
    	if (atts == null) {
    		return new AttributeSetImpl();
    	}
    	return new AttributeSetImpl(atts.getLength());
    }
}
