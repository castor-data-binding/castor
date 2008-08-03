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
 * Copyright 1999,2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * Contribution(s):
 *
 * - Frank Thelen, frank.thelen@poet.de
 *     - Moved creation of access methods into an appropriate
 *       set of separate methods, for extensibility
 *
 * $Id$
 */
package org.exolab.castor.builder.info;

import org.exolab.castor.builder.SourceGeneratorConstants;
import org.exolab.castor.builder.factory.FieldMemberAndAccessorFactory;
import org.exolab.castor.builder.types.XSCollectionFactory;
import org.exolab.castor.builder.types.XSListType;
import org.exolab.castor.builder.types.XSType;

/**
 * A helper used for generating source that deals with Collections.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-02-23 01:08:24 -0700 (Thu, 23 Feb 2006) $
 */
public class CollectionInfo extends FieldInfo {

    /** Default suffix for the setter/getter by reference method names. */
    public static final String DEFAULT_REFERENCE_SUFFIX  = "AsReference";

    /**
     * The property used to overwrite the reference suffix for extra collection
     * methods.
     */
    public static final String REFERENCE_SUFFIX_PROPERTY =
                       "org.exolab.castor.builder.collections.reference.suffix";

    /**
     * A flag indicating that "extra" accessor methods should be created for
     * returning and setting a reference to the underlying collection.
     */
    private boolean            _extraMethods;
    /** The reference suffix to use. */
    private String             _referenceSuffix          = DEFAULT_REFERENCE_SUFFIX;

    /** Element type name converted to a method suffix. */
    private final String       _methodSuffix;
    /** Element type name converted to a parameter prefix. */
    private final String       _parameterPrefix;
    /** FieldInfo describing the _content (i.e. the elements) of this collection. */
    private final FieldInfo    _content;
    /** The name to be used when referring to the elements of this collection. */
    private final String       _elementName;

    /**
     * Creates a new CollectionInfo.
     *
     * @param contentType
     *            the _content type of the collection, ie. the type of objects
     *            that the collection will contain
     * @param name
     *            the name of the Collection
     * @param elementName
     *            the element name for each element in collection
     * @param useJava50
     *            true if source code is supposed to be generated for Java 5
     * @param memberAndAccessorFactory 
     *            the FieldMemberAndAccessorFactory to be used
     * @param contentMemberAndAccessorFactory 
     *            the FieldMemberAndAccessorFactory for the content          
     */
    public CollectionInfo(final XSType contentType, final String name,
            final String elementName, final boolean useJava50, 
            final FieldMemberAndAccessorFactory memberAndAccessorFactory,
            final FieldMemberAndAccessorFactory contentMemberAndAccessorFactory) {
        super(XSCollectionFactory.createCollection(SourceGeneratorConstants.FIELD_INFO_VECTOR, 
                contentType, useJava50), name, memberAndAccessorFactory);

        if (elementName.charAt(0) == '_') {
            this._elementName = elementName.substring(1);
        } else {
            this._elementName = elementName;
        }

        this._methodSuffix = memberAndAccessorFactory.getJavaNaming().toJavaClassName(
                this.getElementName());
        this._parameterPrefix = memberAndAccessorFactory.getJavaNaming().toJavaMemberName(
                this.getElementName());
        this._content = new FieldInfo(contentType, "v" + this.getMethodSuffix(), 
                contentMemberAndAccessorFactory);
    } // -- CollectionInfo
    

    /**
     * Return the contents of the collection.
     * @return the contents of the collection.
     */
    public final FieldInfo getContent() {
        return this._content;
    }

    /**
     * Returns the variable name for the content of the collection.
     * @return the variable name for the content of the collection.
     */
    public final String getContentName() {
        return this.getContent().getName();
    }

    /**
     * Returns the type of content in this collection.
     * @return the type of content in this collection.
     */
    public final XSType getContentType() {
        return this.getContent().getSchemaType();
    }

    /**
     * Returns the name to be used when referring to the elements of this
     * collection.
     *
     * @return the name to be used when referring to the elements of this
     *          collection.
     */
    public final String getElementName() {
        return this._elementName;
    }

    /**
     * Returns the schema type represented by this collection.
     * @return the schema type represented by this collection.
     */
    public final XSListType getXSList() {
        return (XSListType) this.getSchemaType();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.builder.info.XMLInfo#isMultivalued()
     */
    public final boolean isMultivalued() {
        return true;
    }

    /**
     * Sets whether or not to create extra collection methods for accessing the
     * actual collection.
     *
     * @param extraMethods
     *            a boolean that when true indicates that extra collection
     *            accessor methods should be created. False by default.
     * @see #setReferenceMethodSuffix
     */
    public final void setCreateExtraMethods(final boolean extraMethods) {
        this._extraMethods = extraMethods;
    } // -- setCreateExtraMethods

    /**
     * Sets the method suffix (ending) to use when creating the extra collection
     * methods.
     *
     * @param suffix
     *            the method suffix to use when creating the extra collection
     *            methods. If null or emtpty the default value, as specified by
     *            DEFAULT_REFERENCE_SUFFIX will used.
     * @see #setCreateExtraMethods
     */
    public final void setReferenceMethodSuffix(final String suffix) {
        if (suffix == null || suffix.length() == 0) {
            this._referenceSuffix = DEFAULT_REFERENCE_SUFFIX;
        } else {
            this._referenceSuffix = suffix;
        }
    } // -- setReferenceMethodSuffix

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.builder.info.FieldInfo#getMethodSuffix()
     */
    public final String getMethodSuffix() {
        return this._methodSuffix;
    }

    /**
     * Returns the suffix (ending) that should be used when creating the extra
     * collection methods.
     *
     * @return the suffix for the reference methods
     */
    public final String getReferenceMethodSuffix() {
        return this._referenceSuffix;
    } // -- getReferenceMethodSuffix
    
    /**
     * Indicates whether extra collection methods should be created.
     * @return True if extra collection methods will be created.
     */
    public boolean isExtraMethods() {
        return _extraMethods;
    }
    
    /**
     * Returns the element type name converted to a parameter prefix.
     * @return the element type name converted to a parameter prefix.
     */
    public String getParameterPrefix() {
        return _parameterPrefix;
    }


    /**
     * Returns the reference suffix to use for 'reference style' methods.
     * @return the reference suffix to use 
     */
    public String getReferenceSuffix() {
        return _referenceSuffix;
    }


} // -- CollectionInfo
