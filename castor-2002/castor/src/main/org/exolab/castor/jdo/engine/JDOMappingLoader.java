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


package org.exolab.castor.jdo.engine;


import java.io.PrintWriter;
import java.util.Vector;
import java.util.Enumeration;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;


/**
 * A JDO implementation of mapping helper. Creates JDO class descriptors
 * from the mapping file.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class JDOMappingLoader
    extends MappingLoader
{


    private static final String IgnoreDirty = "ignore";


    public JDOMappingLoader( ClassLoader loader, PrintWriter logWriter )
    {
        super( loader, logWriter );
    }


    protected ClassDescriptor createDescriptor( ClassMapping clsMap )
        throws MappingException
    {
        ClassDescriptor   clsDesc;
        FieldDescriptor[] fields;
        Vector            jdoFields;
        
        // If no SQL information for class, ignore it. JDO only
        // supports JDO class descriptors.
        if ( clsMap.getMapTo() == null || clsMap.getMapTo().getTable() == null )
            return NoDescriptor;

        // See if we have a compiled descriptor.
        clsDesc = loadClassDescriptor( clsMap.getName() );
        if ( clsDesc != null && clsDesc instanceof JDOClassDescriptor )
            return clsDesc;

        // Use super class to create class descriptor. Field descriptors will be
        // generated only for supported fields, see createFieldDesc later on.
        // This class may only extend a JDO class, otherwise no mapping will be
        // found for the parent.
        clsDesc = super.createDescriptor( clsMap );

        // JDO descriptor must include an identity field, the identity field
        // is either a field, or a container field containing only JDO fields.
        // If the identity field is not a JDO field, it will be cleaned later
        // on (we need the descriptor for relations mapping).
        if ( clsDesc.getIdentity() == null )
            throw new MappingException( "mapping.noIdentity", clsDesc.getJavaClass().getName() );
        
        return new JDOClassDescriptor( clsDesc, clsMap.getMapTo().getTable() );
    }


    protected TypeInfo getTypeInfo( Class fieldType, Class colType, FieldMapping fieldMap )
        throws MappingException
    {
        TypeConvertor convertorTo = null;
        TypeConvertor convertorFrom = null;

        if ( fieldMap.getSql() != null && fieldMap.getSql().getType() != null ) {
            Class sqlType;

            fieldType = Types.typeFromPrimitive( fieldType );
            sqlType = SQLTypes.typeFromName( fieldMap.getSql().getType() );
            if ( fieldType != sqlType ) {
                convertorTo = Types.getConvertor( sqlType, fieldType );
                convertorFrom = Types.getConvertor( fieldType, sqlType );
            }
        }
        return new TypeInfo( fieldType, convertorTo, convertorFrom,
                             fieldMap.getRequired(), null, colType );
    }


    protected FieldDescriptor createFieldDesc( Class javaClass, FieldMapping fieldMap )
        throws MappingException
    {
        FieldDescriptor fieldDesc;
        String          sqlName;
        
        // If not an SQL field, return a stock field descriptor.
        if ( fieldMap.getSql() == null )
            return super.createFieldDesc( javaClass, fieldMap );
        
        // Create a JDO field descriptor
        fieldDesc = super.createFieldDesc( javaClass, fieldMap );
        if ( fieldMap.getSql().getName() == null )
            sqlName = SQLTypes.javaToSqlName( fieldDesc.getFieldName() );
        else
            sqlName = fieldMap.getSql().getName();
        return new JDOFieldDescriptor( (FieldDescriptorImpl) fieldDesc, sqlName,
            ! IgnoreDirty.equals( fieldMap.getSql().getDirty() ),
            fieldMap.getSql().getManyTable(), fieldMap.getSql().getManyKey() );
    }


}



