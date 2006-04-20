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


import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;


/**
 * JDO field descriptor. Wraps {@link FieldDescriptorImpl} and adds
 * SQL-related information, type conversion, and set/get for JDBC.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class JDOFieldDescriptor
    extends FieldDescriptorImpl
{


    /**
     * True if the field requires dirty checking.
     */
    private boolean        _dirtyCheck;


    /**
     * The SQL name of the field.
     */
    private String[]       _sqlName;


    private String         _manyTable;

    private String[]       _manyKey;

    private int[]          _sqlType;

    private boolean        _readonly;

    /**
     * Construct a new field descriptor for the specified field. This is
     * a JDO field descriptor wrapping a field descriptor and adding JDO
     * related properties and methods.
     *
     * @param fieldDesc The field descriptor
     * @throws MappingException Invalid mapping information
     */
    public JDOFieldDescriptor( FieldDescriptorImpl fieldDesc, String[] sqlName, int[] sqlType,
                               boolean dirtyCheck, String manyTable, String[] manyKey, boolean readonly)
        throws MappingException
    {
        super( fieldDesc );
        if ( fieldDesc.getHandler() == null )
            throw new IllegalArgumentException( "Argument 'fieldDesc' has no handler" );
        //if ( sqlName == null )
        //    throw new IllegalArgumentException( "Argument 'sqlName' is null" );
        _sqlName = (sqlName.length == 0 ? null : sqlName);
        _dirtyCheck = dirtyCheck;
        _manyTable = manyTable;
        _manyKey = (manyKey.length>0? manyKey : null);
        _sqlType = sqlType;
        _readonly = readonly;
    }


    public String getManyTable()
    {
        return _manyTable;
    }


    public String[] getManyKey()
    {
        return _manyKey;
    }


    /**
     * Returns the SQL name of this field.
     *
     * @return The SQL name of this field
     */
    public String[] getSQLName()
    {
        return _sqlName;
    }


    /**
     * Returns true if dirty checking is required for this field.
     *
     * @return True if dirty checking required
     */
    public boolean isDirtyCheck()
    {
        return _dirtyCheck;
    }


    /**
     * Returns the SQL type of this field.
     *
     * @return The SQL type of this field
     */
    public int[] getSQLType()
    {
        return _sqlType;
    }


    public String toString()
    {
        return super.toString() + (_sqlName == null ? "" : " AS " + _sqlName[0]);
    }


    public boolean isReadonly()
    {
        return _readonly;
    }
}










