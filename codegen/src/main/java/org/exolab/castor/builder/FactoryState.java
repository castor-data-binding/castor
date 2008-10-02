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

import java.util.Vector;

import org.exolab.castor.builder.binding.XMLBindingComponent;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JEnum;

/**
 * A class used to save State information for the SourceFactory.
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 */
public class FactoryState implements ClassInfoResolver {

    /** The JClass for which we are currently generating code. */
    private final JClass _jClass;

    /** A ClassInfo for <code>_jClass</code>. */
    private final ClassInfo _classInfo;

    /** A FieldInfo used to handle <code>xsd:choice</code>. */
    private FieldInfo _fieldInfoForChoice = null;

    /** Package for the class currently being generated. */
    private final String _packageName;

    /** Our ClassInfoResolver to keep track of ClassInfos for easy lookup. */
    private ClassInfoResolver _resolver = null;

    /** Keeps track of which classes have been processed. */
    private Vector<Annotated> _processed = null;

    /** SourceGenerator state. */
    private SGStateInfo _sgState = null;

    /** If true, we are currently generating code for a group. */
    private boolean _createGroupItem = false;

    /**
     * Keeps track of whether or not the BoundProperties methods have been
     * created.
     */
    private boolean _bound = false;

    /** Keeps track of the different FactoryState. */
    private FactoryState _parent = null;

    /**
     * {@link JClassRegistry} instance used for automatic class name conflict
     * resolution.
     */
    private JClassRegistry _xmlInfoRegistry = null;

    /**
     * Constructs a new FactoryState.
     * 
     * @param className
     *            Class name of the class currently being generated.
     * @param sgState
     *            Source Generator State object
     * @param packageName
     *            package name for generated code.
     * @param component
     *            TODO
     */
    public FactoryState(final String className, final SGStateInfo sgState,
            final String packageName, final XMLBindingComponent component) {
        this(className, sgState, packageName, component, false);
    }
    
    /**
     * Constructs a factory state with the option of choosing between JClass and JEnum.
     * 
     * @param className
     *            Class name of the class currently being generated.
     * @param sgState
     *            Source Generator State object
     * @param packageName
     *            package name for generated code.
     * @param component
     *            TODO           
     * @param enumeration
     *            use a JEnum instead if a JClass
     */
    public FactoryState(final String className, final SGStateInfo sgState,
            final String packageName, final XMLBindingComponent component, final boolean enumeration) {
        if (sgState == null) {
            throw new IllegalArgumentException("SGStateInfo cannot be null.");
        }

        _sgState = sgState;
        _processed = new Vector<Annotated>();

        // keep the elements and complexType already processed
        // if (resolver instanceof FactoryState) {
        // _processed = ((FactoryState)resolver)._processed;
        // }

        if (enumeration) {
            _jClass = new JEnum(className);
        } else {
            _jClass = new JClass(className);
        }

        // if configured, try automatic class name conflict resolution
        if (_sgState.getSourceGenerator().isAutomaticConflictResolution()) {
            _xmlInfoRegistry = sgState.getSourceGenerator()
                    .getXMLInfoRegistry();
            _xmlInfoRegistry.bind(_jClass, component, "class");
        }

        _classInfo = new ClassInfo(_jClass);

        _resolver = sgState;

        _packageName = packageName;

        // -- boundProperties
        _bound = sgState.getSourceGenerator().boundPropertiesEnabled();

    } // -- FactoryState

    /**
     * Get JClass for which we are currently generating code.
     * 
     * @return JClass for which we are currently generating code.
     */
    public final JClass getJClass() {
        return _jClass;
    }

    /**
     * Get ClassInfo for <code>_jClass</code>.
     * 
     * @return ClassInfo for <code>_jClass</code>.
     */
    public final ClassInfo getClassInfo() {
        return _classInfo;
    }

    /**
     * Get FieldInfo used to handle <code>xsd:choice</code>.
     * 
     * @return FieldInfo used to handle <code>xsd:choice</code>.
     */
    public final FieldInfo getFieldInfoForChoice() {
        return _fieldInfoForChoice;
    }

    /**
     * Set FieldInfo used to handle <code>xsd:choice</code>.
     * 
     * @param fieldInfoForChoice
     *            FieldInfo used to handle <code>xsd:choice</code>.
     */
    public final void setFieldInfoForChoice(final FieldInfo fieldInfoForChoice) {
        _fieldInfoForChoice = fieldInfoForChoice;
    }

    /**
     * Get package for the class currently being generated.
     * 
     * @return Package for the class currently being generated.
     */
    public final String getPackageName() {
        return _packageName;
    }

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
    } // -- bindReference

    /**
     * Returns the SGStateInfo.
     * 
     * @return the SGStateInfo.
     */
    public SGStateInfo getSGStateInfo() {
        return _sgState;
    } // -- getSGStateInfo

    /**
     * Marks the given Annotated XML Schema structure as having been processed.
     * 
     * @param annotated
     *            the Annotated XML Schema structure to mark as having been
     *            processed.
     */
    public void markAsProcessed(final Annotated annotated) {
        _processed.addElement(annotated);
    } // -- markAsProcessed

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
    public boolean processed(final Annotated annotated) {
        boolean result = _processed.contains(annotated);
        if (!result && _parent != null) {
            return _parent.processed(annotated);
        }
        return result;
    } // -- processed

    /**
     * Returns true if any bound properties have been found.
     * 
     * @return true if any bound properties have been found.
     */
    public boolean hasBoundProperties() {
        return _bound;
    } // -- hasBoundProperties

    /**
     * Allows setting the bound properties flag.
     * 
     * @param bound
     *            the new value of the bound properties flag
     * @see #hasBoundProperties
     */
    public void setBoundProperties(final boolean bound) {
        _bound = bound;
    } // -- setBoundProperties

    /**
     * Returns the ClassInfo which has been bound to the given key.
     * 
     * @param key
     *            the object to which the ClassInfo has been bound
     * @return the ClassInfo which has been bound to the given key
     */
    public ClassInfo resolve(final Object key) {
        return _resolver.resolve(key);
    } // -- resolve

    /**
     * Returns true if we are currently in the state of creating a group item
     * class.
     * 
     * @return true if we are currently in the state of creating a group item
     *         class.
     */
    public boolean isCreateGroupItem() {
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
    public void setCreateGroupItem(final boolean createGroupItem) {
        _createGroupItem = createGroupItem;
    }

    /**
     * Returns the parent of this FactoryState. The parent of a factory state is
     * the previous item of the list that contained all the created factory
     * states.
     * 
     * @return the parent of this FactoryState.
     */
    FactoryState getParent() {
        return _parent;
    }

    /**
     * Sets the parent of this FactoryState.
     * 
     * @param parent
     *            the parent FactoryState
     * @see #getParent
     */
    public void setParent(final FactoryState parent) {
        _parent = parent;
    }

} // -- FactoryState
