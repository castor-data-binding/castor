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
 * This class is used as a factory to create all the FieldInfo objects used by the
 * source generator. You may override the FieldInfo classes and this factory for
 * specific adaptions.
 *
 * @author <a href="mailto:frank.thelen@poet.de">Frank Thelen</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
**/
public class FieldInfoFactory {

    // FIXME: These strings should probably be public so they can be used in
    // place of hardcoded strings, eg, in org.exolab.castor.builder.types.XSListJ2
    // Alternately and probably better, the usage of these strings could be refactored.
    private static final String VECTOR = "vector";
    private static final String ARRAY_LIST = "arraylist";
    private static final String ODMG = "odmg";

    /**
     * The default collection name
     */
    private String _default = null;

    /**
     * A flag indicating that "extra" accessor methods
     * should be created for returning and setting a
     * reference to the underlying collection
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

    public FieldInfoFactory(String collectionName) {
        super();
        if (!(collectionName.equals(VECTOR) || collectionName.equals(ARRAY_LIST) || collectionName.equals(ODMG)))
            throw new IllegalArgumentException(collectionName+" is currently not a supported Java collection.");
        _default = collectionName;
    }

    public IdentityInfo createIdentity (String name) {
        IdentityInfo idInfo = new IdentityInfo(name);
        if (_bound) idInfo.setBound(_bound);
        return idInfo;
    } //-- createIdentity


    public CollectionInfo createCollection
        (XSType contentType, String name, String elementName)
    {
        return createCollection(contentType, name, elementName, _default);
    }

    public CollectionInfo createCollection
        (XSType contentType, String name, String elementName, String collectionName)
    {

        String temp = collectionName;
        if (temp == null || temp.length() == 0)
            temp = _default;

        CollectionInfo cInfo = null;
        if (temp.equals(VECTOR)) {
             cInfo = new CollectionInfo(contentType,name,elementName);
        } else if (temp.equals(ARRAY_LIST)) {
             cInfo = new CollectionInfoJ2(contentType,name,elementName, "arraylist");
        } else if (temp.equals(ODMG)) {
             cInfo = new CollectionInfoODMG30(contentType,name,elementName);
        }
//         else if (temp.equals("collection")) {
//            cInfo = new CollectionInfoJ2(contentType,name,elementName, "collection");
//       } else if(temp.equalsIgnoreCase("set")) {
//            cInfo = new CollectionInfoJ2(contentType,name,elementName, "set");
//        }
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

    public FieldInfo createFieldInfo (XSType type, String name) {
        FieldInfo fieldInfo = new FieldInfo(type, name);
        if (_bound) fieldInfo.setBound(true);
        return fieldInfo;
    } //-- createFieldInfo


    /**
     * Sets whether or not the fields should be bound
     * properties
     *
     * @param bound a boolean that when true indicates
     * the FieldInfo should have the bound property
     * enabled.
     */
    public void setBoundProperties(boolean bound) {
        _bound = bound;
    } //-- setBoundProperties

    /**
     * Sets whether or not to create extra collection methods
     * for accessing the actual collection
     *
     * @param extraMethods a boolean that when true indicates that
     * extra collection accessor methods should be created. False
     * by default.
     * @see org.exolab.castor.builder.FieldInfoFactory#setReferenceMethodSuffix
     */
    public void setCreateExtraMethods(boolean extraMethods) {
        _extraMethods = extraMethods;
    } //-- setCreateExtraMethods

    /**
     * Sets the method suffix (ending) to use when creating
     * the extra collection methods.
     *
     * @param suffix the method suffix to use when creating
     * the extra collection methods. If null or emtpty the default
     * value, as specified in CollectionInfo will be used.
     * @see org.exolab.castor.builder.FieldInfoFactory#setCreateExtraMethods
     */
    public void setReferenceMethodSuffix(String suffix) {
        _referenceSuffix = suffix;
    } //-- setReferenceMethodSuffix

} //-- FieldInfoFactory
