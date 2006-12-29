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
 * Copyright 1999-2003 (C) Intalio Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.javasource.JClass;
import java.util.Vector;

/**
 * A class used to save State information for the SourceFactory
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 */
class FactoryState implements ClassInfoResolver {

    //--------------------/
    //- Member Variables -/
    //--------------------/

    JClass       jClass              = null;
    ClassInfo    classInfo           = null;
    FieldInfo    fieldInfoForChoice  = null;
    String       packageName         = null;

    private ClassInfoResolver _resolver  = null;
    private Vector            _processed = null;
    private SGStateInfo       _sgState   = null;
    private boolean           _createGroupItem = false;

    /**
     * Keeps track of whether or not the BoundProperties methods have been
     * created
     */
    private boolean           _bound = false;

    /**
     * Keeps track of the different FactoryState
     */
    private FactoryState _parent = null;

    //----------------/
    //- Constructors -/
    //----------------/

    protected FactoryState(final String className, final SGStateInfo sgState,
                           final String packageName) {
        if (sgState == null) {
            throw new IllegalArgumentException("SGStateInfo cannot be null.");
        }

        _sgState     = sgState;
        _processed   = new Vector();

        //keep the elements and complexType already processed
        //if (resolver instanceof FactoryState)
           //_processed = ((FactoryState)resolver)._processed;

        jClass       = new JClass(className);
        classInfo    = new ClassInfo(jClass);

        _resolver = sgState;

        this.packageName = packageName;

        //-- boundProperties
        _bound = sgState.getSourceGenerator().boundPropertiesEnabled();
    } //-- FactoryState

    //-----------/
    //- Methods -/
    //-----------/

    /**
     * Adds the given Reference to this ClassInfo resolver.
     *
     * @param key
     *            the key to bind a reference to
     * @param classInfoRef
     *            the ClassInfo which is being referenced
     */
    public void bindReference(final Object key, final ClassInfo classInfoRef) {
        _resolver.bindReference(key, classInfoRef);
    } //-- bindReference

    /**
     * Returns the SGStateInfo.
     *
     * @return the SGStateInfo.
     */
    SGStateInfo getSGStateInfo() {
        return _sgState;
    } //-- getSGStateInfo

    /**
     * Marks the given Annotated XML Schema structure as having been processed.
     *
     * @param annotated
     *            the Annotated XML Schema structure to mark as having been
     *            processed.
     */
    void markAsProcessed(final Annotated annotated) {
        _processed.addElement(annotated);
    } //-- markAsProcessed

    /**
     * Returns true if the given Annotated XML Schema structure has been marked
     * as processed.
     *
     * @param annotated
     *            the Annotated XML Schema structure to check for being marked
     *            as processed
     * @return true if the given Annotated XML Schema structure has been marked
     *         as processed
     */
    boolean processed(final Annotated annotated) {
        boolean result = _processed.contains(annotated);
        if (!result) {
            if (_parent != null) {
                return _parent.processed(annotated);
            }
        }
        return result;
    } //-- processed

    /**
     * Returns true if any bound properties have been found.
     *
     * @return true if any bound properties have been found.
     */
    boolean hasBoundProperties() {
        return _bound;
    } //-- hasBoundProperties

    /**
     * Allows setting the bound properties flag.
     *
     * @param bound
     *            the new value of the bound properties flag
     * @see #hasBoundProperties
     */
    void setBoundProperties(final boolean bound) {
        _bound = bound;
    } //-- setBoundProperties

    /**
     * Returns the ClassInfo which has been bound to the given key
     *
     * @param key
     *            the object to which the ClassInfo has been bound
     * @return the ClassInfo which has been bound to the given key
     */
    public ClassInfo resolve(final Object key) {
        return _resolver.resolve(key);
    } //-- resolve

    /**
     * Returns true if we are currently in the state of creating a group item
     * class.
     *
     * @return true if we are currently in the state of creating a group item
     *         class.
     */
    boolean isCreateGroupItem() {
        return _createGroupItem;
    }

    /**
     * Sets to true if we are currently generating a class to represent items in
     * a group.
     *
     * @param createGroupItem
     *            true if we are currently generating a class to represent items
     *            in a group.
     */
     void setCreateGroupItem(final boolean createGroupItem) {
         _createGroupItem = createGroupItem;
     }

     /**
      * Returns the parent of this FactoryState. The parent of a factory
      * state is the previous item of the list that contained all the created
      * factory states.
      *
      * @return the parent of this FactoryState.
      */
     FactoryState getParent() {
         return _parent;
     }

     /**
      * Sets the parent of this FactoryState
      *
      * @param parent
      *            the parent FactoryState
      * @see #getParent
      */
     void setParent(final FactoryState parent) {
         _parent = parent;
     }

} //-- FactoryState
