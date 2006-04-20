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


package org.exolab.castor.mapping.loader;


import org.exolab.castor.mapping.ValidityException;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.util.Messages;


/**
 * A basic class descriptor implementation. Engines will extend this
 * class to provide additional functionality.
 *
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class ClassDescriptorImpl
    implements ClassDescriptor
{


    private ClassMapping         _map;
    /**
     * The Java class for this descriptor.
     */
    private final Class                _javaClass;

    /**
     * The fields described for this class.
     */
    protected final FieldDescriptor[]  _fields;


    /**
     * The descriptor of the class which this class extends,
     * or null if this is a top-level class.
     */
    private final ClassDescriptor     _extends;


    private final ClassDescriptor     _depends;


    /**
     * The field of the identity for this class.
     */
    protected final FieldDescriptor[]    _identities;


    /**
     * The access mode specified for this class.
     */
    private final AccessMode         _accessMode;


    /**
     * Constructs a new descriptor for the specified class. When describing
     * inheritence, the descriptor of the parent class should be used and only
     * the fields added in this object must be supplied here.
     *
     * @param javaClass The Java type of this class
     * @param fields The fields described for this class
     * @param identity The field of the identity (key) of this class,
     *   may be null
     * @param extend The descriptor of the class which this class extends,
     * @param accessMode The access mode for this class (null is shared)
     * or null if this is a top-level class
     * @throws MappingException The extended descriptor does not match
     *   a parent class of this type
     */
    public ClassDescriptorImpl( Class javaClass, FieldDescriptor[] fields,
                                FieldDescriptor[] identities, ClassDescriptor extend, ClassDescriptor depend,
                                AccessMode accessMode )
        throws MappingException
    {
        if ( ! Types.isConstructable( javaClass, true ) ) {
            throw new MappingException( "mapping.classNotConstructable", javaClass.getName() );
        }
            
        _javaClass = javaClass;
        if ( fields == null )
            throw new IllegalArgumentException( "Argument 'fields' is null" );
        _fields = (FieldDescriptor[]) fields.clone();
        _accessMode = accessMode;
        _depends = depend;

        //if ( extend != null && depend != null )
        //    throw new MappingException( "Class must either extends or depends on other classes, but not both!" );

        if ( extend != null ) {
            if ( ! extend.getJavaClass().isAssignableFrom( javaClass ) )
                throw new MappingException( "mapping.classDoesNotExtend",
                                            _javaClass.getName(), extend.getJavaClass().getName() );
            _extends = extend;
            if ( _extends instanceof ClassDescriptorImpl )
                _identities = ( identities == null ? ((ClassDescriptorImpl)_extends).getIdentities() : identities );
            else
                // a quick hack to fix a ClassCastException :-(
                _identities = ( identities == null ?
                (_extends.getIdentity() == null? null : new FieldDescriptor[] { _extends.getIdentity() } )
                : identities );
        } else {
            _extends = null;
            _identities = identities;
        }

        // fritz: propagate containing class to fields
        // oleg: don't alter the identity's info if the identity was taken
        // from the ancestor class.
        // So complicated condition is needed since for JDO fields first a pure
        // ClassDescriptorImpl is created, and then JDOClassDescriptorImpl for
        // the same class
        if ( (_identities != null) && (_identities.length >0) && _identities[0] != null 
                && ( _identities[0].getContainingClassDescriptor() == null 
                     || _identities[0].getContainingClassDescriptor().getJavaClass() == _javaClass ) ) {
            for ( int i = 0; i < _identities.length; i++ )
                _identities[i].setContainingClassDescriptor( this );
        }
        for ( int i = 0; i < _fields.length; i++ )
            _fields[i].setContainingClassDescriptor( this );
    }

    public ClassMapping getMapping() {
        return _map;
    }

    public void setMapping( ClassMapping map ) {
        _map = map;
    }

    /**
     * Constructor used by derived classes.
     */
    protected ClassDescriptorImpl( Class javaClass )
    {
        _javaClass = javaClass;
        _extends = null;
        _fields = null;
        _identities = null;
        _depends = null;
        _accessMode = null;

    }


    public Class getJavaClass()
    {
        return _javaClass;
    }


    public FieldDescriptor[] getFields()
    {
        return _fields;
    }


    public ClassDescriptor getExtends()
    {
        return _extends;
    }

    public ClassDescriptor getDepends() {
        return _depends;
    }

    public FieldDescriptor getIdentity() {
        return _identities == null? null : _identities[0];
    }

    public FieldDescriptor[] getIdentities() {
        return _identities;
    }


    public AccessMode getAccessMode()
    {
        return _accessMode;
    }


    /**
     * Checks the object validity. Returns successfully if the object
     * can be stored, is valid, etc, throws an exception otherwise.
     *
     * @param object The object
     * @throws ValidityException The object is invalid, a required is
     *  null, or any other validity violation
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler
     *  is not compatiable with the Java object
     */
    public void checkValidity( Object object )
        throws ValidityException, IllegalStateException
    {
        // Object cannot be saved if one of the required fields is null
        for ( int i = 0 ; i < _fields.length ; ++i ) {
            if ( _fields[ i ].isRequired() && _fields[ i ].getHandler().getValue( object ) == null )
                throw new ValidityException( "mapping.requiredField",
                                             object.getClass().getName(), _fields[ i ].getFieldName() );
        }
    }


    public String toString()
    {
        return _javaClass.getName();
    }


}


