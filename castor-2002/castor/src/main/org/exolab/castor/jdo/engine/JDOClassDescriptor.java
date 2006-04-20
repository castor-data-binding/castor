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


package org.exolab.castor.jdo.engine;


import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;


/**
 * JDO class descriptors. Extends {@link ClassDescriptor} to include the
 * table name and other SQL-related information. All fields are of
 * type {@link JDOFieldDescriptor}, identity field is not included in the
 * returned field list, and contained fields are flattened out for
 * efficiency (thus all fields are directly accessible).
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class JDOClassDescriptor
    extends ClassDescriptorImpl
{


    /**
     * The name of the SQL table.
     */
    private String  _tableName;

    /**
     * The ClassDescriptor of the class which this class depends on.
     */
    private ClassDescriptor _depends;

    /**
     * The key generator specified for this class.
     */
    private final KeyGeneratorDescriptor _keyGenDesc;

    /**
     * The preferred mechanism for caching instance of this class
     */
    private final String _cacheType;

    /**
     * The preferred mechanism for caching instance of this class
     */
    private final int _cacheParam;


    public JDOClassDescriptor( ClassDescriptor clsDesc, String tableName, 
            KeyGeneratorDescriptor keyGenDesc, String cacheType, int cacheParam ) 
            throws MappingException {

        super( clsDesc.getJavaClass(), clsDesc.getFields(), 
               (clsDesc instanceof ClassDescriptorImpl? ((ClassDescriptorImpl)clsDesc).getIdentities():null),
               clsDesc.getExtends(), 
               (clsDesc instanceof ClassDescriptorImpl? ((ClassDescriptorImpl)clsDesc).getDepends(): null), 
               clsDesc.getAccessMode() );
        if ( tableName == null )
            throw new IllegalArgumentException( "Argument 'tableName' is null" );
        if ( getIdentity() == null )
            throw new MappingException( "mapping.noIdentity", getJavaClass().getName() );
        if ( ! ( getIdentity() instanceof JDOFieldDescriptor ) )
            throw new IllegalArgumentException( "Identity field must be of type JDOFieldDescriptor" );
        if ( getExtends() != null && ! ( getExtends() instanceof JDOClassDescriptor ) )
            throw new IllegalArgumentException( "Extended class does not have a JDO descriptor" );

        _tableName = tableName;
        _keyGenDesc = keyGenDesc;
        _cacheType = cacheType;
        _cacheParam = cacheParam;
        if ( clsDesc instanceof ClassDescriptorImpl )
            _depends = ((ClassDescriptorImpl)clsDesc).getDepends();
    }


    /**
     * Returns the table name to which this object maps.
     *
     * @return Table name
     */
    public String getTableName()
    {
        return _tableName;
    }

    public ClassDescriptor getDepends() {
        return _depends;
    }

    /**
     * Returns the preferred mechanism for caching instance of this class
     *
     * @return a String represent the cache type
     */
    public String getCacheType() {
        return _cacheType;
    }

    /**
     * Returns the preferred mechanism for caching instance of this class
     *
     * @return an int represent the param
     */
    public int getCacheParam() {
        return _cacheParam;
    }

    /**
     * Returns a JDOFieldDescriptor for the field with the name passed.  Null
     * if named field does not exist.
     *
     * @param name The name of the field to return
     * @return The field if it exists, otherwise null.
     */
    public JDOFieldDescriptor getField(String name) 
    {
        JDOFieldDescriptor field = null;
        for ( int i = 0 ; i < _fields.length ; ++i ) {
            if ( _fields[ i ] instanceof JDOFieldDescriptor &&
                 _fields[ i ].getFieldName().equals( name ) ) {
                field = (JDOFieldDescriptor) _fields[ i ];
                break;
            }
        }
        
        if ( field == null ) {
            if ( this.getIdentity() instanceof JDOFieldDescriptor &&
                 this.getIdentity().getFieldName().equals( name ) ) {
                field = (JDOFieldDescriptor) this.getIdentity();
            }
        }

        return field;
                
    }


    /**
     * Returns the key generator specified for this class.
     *
     * @return The key generator descriptor
     */
    public KeyGeneratorDescriptor getKeyGeneratorDescriptor()
    {
        return _keyGenDesc;
    }


    public String toString()
    {
        return super.toString() + " AS " + _tableName;
    }


}


