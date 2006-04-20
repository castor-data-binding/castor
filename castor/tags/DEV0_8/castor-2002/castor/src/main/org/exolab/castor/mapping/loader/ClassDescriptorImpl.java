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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.mapping.loader;


import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.AccessMode;


/**
 * A basic class descriptor implementation. Engines will extend this
 * class to provide additional functionality.
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class ClassDescriptorImpl
    implements ClassDescriptor
{


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


    /**
     * The field of the identity for this class.
     */
    private final FieldDescriptor    _identity;


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
                                FieldDescriptor identity, ClassDescriptor extend,
                                AccessMode accessMode )
        throws MappingException
    {
        if ( ! Types.isConstructable( javaClass ) )
            throw new MappingException( "mapping.classNotConstructable", javaClass.getName() );
        _javaClass = javaClass;
        if ( fields == null )
            throw new IllegalArgumentException( "Argument 'fields' is null" );
        _fields = (FieldDescriptor[]) fields.clone();

        if ( extend != null ) {
            if ( ! extend.getJavaClass().isAssignableFrom( javaClass ) )
                throw new MappingException( "mapping.classDoesNotExtend",
                                            _javaClass.getName(), extend.getJavaClass().getName() );
            _extends = extend;
            _identity = ( identity == null ? _extends.getIdentity() : identity );
        } else {
            _extends = null;
            _identity = identity;
        }
        _accessMode = accessMode;
    }
    
    
    /**
     * Constructor used by derived classes.
     */
    protected ClassDescriptorImpl( Class javaClass )
    {
        _javaClass = javaClass;
        _extends = null;
        _fields = null;
        _identity = null;
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


    public FieldDescriptor getIdentity()
    {
        return _identity;
    }


    public AccessMode getAccessMode()
    {
        return _accessMode;
    }


    public String toString()
    {
        return _javaClass.getName();
    }


}


