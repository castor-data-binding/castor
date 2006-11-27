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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder;

import org.exolab.castor.builder.types.XSType;

/**
 * This class is used as a factory to create all the FieldInfo objects used by
 * the source generator. You may override the FieldInfo classes and this factory
 * for specific adaptions.
 *
 * @author <a href="mailto:frank.thelen@poet.de">Frank Thelen</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public class FieldInfoFactory {

    // FIXME: These strings should probably be public so they can be used in
    // place of hardcoded strings, eg, in org.exolab.castor.builder.types.XSListJ2
    // Alternately and probably better, the usage of these strings could be refactored.
    public static final String VECTOR     = "vector";
    public static final String ARRAY_LIST = "arraylist";
    public static final String ODMG       = "odmg";
    public static final String COLLECTION = "collection";
    public static final String SET = "set";
    public static final String SORTED_SET = "sortedset";

    /**
     * The default collection name
     */
    private String _default = null;

    /**
     * A flag indicating that "extra" accessor methods should be created for
     * returning and setting a reference to the underlying collection
     */
    private boolean _extraMethods = false;

    /**
     * The reference suffix to use.
     */
    private String _referenceSuffix = null;

    /**
     * Bound properties
     */
    private boolean _bound = false;

    /**
     * Creates a new FieldInfoFactory. The default collection used will be
     * Java 1 type.
    **/
    public FieldInfoFactory () {
        this("vector");
    } //-- FieldInfoFactory

    public FieldInfoFactory(final String collectionName) {
        super();
        if (!(collectionName.equals(VECTOR) 
                || collectionName.equals(ARRAY_LIST) 
                || collectionName.equals(ODMG))) {
            throw new IllegalArgumentException(collectionName+" is currently not a supported Java collection.");
        }
        _default = collectionName;
    }

    public IdentityInfo createIdentity (final String name) {
        IdentityInfo idInfo = new IdentityInfo(name);
        if (_bound) {
            idInfo.setBound(_bound);
        }
        return idInfo;
    } //-- createIdentity


    public CollectionInfo createCollection(final XSType contentType, final String name,
                                           final String elementName, final boolean usejava50) {
        return createCollection(contentType, name, elementName, _default, usejava50);
    }

    public CollectionInfo createCollection(final XSType contentType, final String name,
                                           final String elementName, final String collectionName,
                                           final boolean useJava50) {
        String temp = collectionName;
        if (temp == null || temp.length() == 0) {
            temp = _default;
        }

        CollectionInfo cInfo = null;
        if (temp.equals(VECTOR)) {
             cInfo = new CollectionInfo(contentType, name, elementName, useJava50);
        } else if (temp.equals(ARRAY_LIST)) {
             cInfo = new CollectionInfoJ2(contentType, name, elementName, "arraylist", useJava50);
        } else if (temp.equals(ODMG)) {
             cInfo = new CollectionInfoODMG30(contentType, name, elementName, useJava50);
        } else if (temp.equals(COLLECTION)) {
            cInfo = new CollectionInfoJ2Collection(contentType, name, elementName, useJava50);
        } else if (temp.equalsIgnoreCase(SET)) {
            cInfo = new CollectionInfoJ2Set(contentType, name, elementName, useJava50);
        } else if (temp.equalsIgnoreCase(SORTED_SET)) {
            cInfo = new CollectionInfoJ2SortedSet(contentType, name, elementName, useJava50);
        }

        //--other to come here
        //--not sure it is pluggable enough, it is not really beautiful to specify
        //--the collection to use here
        cInfo.setCreateExtraMethods(_extraMethods);
        if (_referenceSuffix != null) {
            cInfo.setReferenceMethodSuffix(_referenceSuffix);
        }
        if (_bound) {
            cInfo.setBound(true);
        }
        return cInfo;
    }

    public FieldInfo createFieldInfo(final XSType type, final String name) {
        FieldInfo fieldInfo = new FieldInfo(type, name);
        if (_bound) {
            fieldInfo.setBound(true);
        }
        return fieldInfo;
    } //-- createFieldInfo

    /**
     * Sets whether or not the fields should be bound properties.
     *
     * @param bound
     *            a boolean that when true indicates the FieldInfo should have
     *            the bound property enabled.
     */
    public void setBoundProperties(final boolean bound) {
        _bound = bound;
    } //-- setBoundProperties

    /**
     * Sets whether or not to create extra collection methods for accessing the
     * actual collection.
     *
     * @param extraMethods
     *            a boolean that when true indicates that extra collection
     *            accessor methods should be created. False by default.
     * @see org.exolab.castor.builder.FieldInfoFactory#setReferenceMethodSuffix
     */
    public void setCreateExtraMethods(final boolean extraMethods) {
        _extraMethods = extraMethods;
    } //-- setCreateExtraMethods

    /**
     * Sets the method suffix (ending) to use when creating the extra collection
     * methods.
     *
     * @param suffix
     *            the method suffix to use when creating the extra collection
     *            methods. If null or emtpty the default value, as specified in
     *            CollectionInfo will be used.
     * @see org.exolab.castor.builder.FieldInfoFactory#setCreateExtraMethods
     */
    public void setReferenceMethodSuffix(final String suffix) {
        _referenceSuffix = suffix;
    } //-- setReferenceMethodSuffix

} //-- FieldInfoFactory
