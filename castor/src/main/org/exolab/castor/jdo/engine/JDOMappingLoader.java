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


import org.exolab.castor.mapping.*;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.*;
import org.exolab.castor.mapping.xml.types.DirtyType;
import org.exolab.castor.util.Messages;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * A JDO implementation of mapping helper. Creates JDO class descriptors
 * from the mapping file.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class JDOMappingLoader
    extends MappingLoader
{


    /**
     * Used by the constructor for creating key generators.
     * See {@link #loadMapping}.
     */
    private Hashtable _keyGenDefs = new Hashtable();


    /**
     * Used by the constructor for creating key generators.
     * See {@link #loadMapping}.
     */
    private Hashtable _keyGenDescs = new Hashtable();


    /**
     * The JDO PersistenceFactory (aka BaseFactory) is used for adjusting
     * SQL type for the given database.
     */
    private BaseFactory _factory;


    /**
     * Used by the constructor for creating key generators.
     * Each database must have a proprietary KeyGeneratorRegistry instance
     * Otherwise it is impossible to implement correctly stateful
     * key generator algorithms like HIGH-LOW.
     * See {@link #loadMapping}.
     */
    private KeyGeneratorRegistry _keyGenReg = new KeyGeneratorRegistry();


    public JDOMappingLoader( ClassLoader loader, PrintWriter logWriter )
    {
        super( loader, logWriter );
    }


    protected ClassDescriptor createDescriptor( ClassMapping clsMap )
        throws MappingException
    {
        ClassDescriptor   clsDesc;
        String            keyGenName;
        KeyGeneratorDescriptor keyGenDesc;

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

        // create a key generator descriptor
        keyGenName = clsMap.getKeyGenerator();
        keyGenDesc = null;
        if ( keyGenName != null ) {
            String keyGenFactoryName;
            KeyGeneratorDef keyGenDef;
            Enumeration enum;
            Properties params;

            // first search among declared key generators
            // and resolve alias
            keyGenDef = (KeyGeneratorDef) _keyGenDefs.get( keyGenName );
            params = new Properties();
            keyGenFactoryName = keyGenName;
            if ( keyGenDef != null ) {
                keyGenFactoryName = keyGenDef.getName();
                enum = keyGenDef.enumerateParam();
                while ( enum.hasMoreElements() ) {
                    Param par = (Param) enum.nextElement();
                    params.put( par.getName(), par.getValue() );
                }
            }
            keyGenDesc = (KeyGeneratorDescriptor) _keyGenDescs.get(keyGenName);
            if ( keyGenDesc == null ) {
                keyGenDesc = new KeyGeneratorDescriptor( keyGenName,
                        keyGenFactoryName, params, _keyGenReg );
                _keyGenDescs.put(keyGenName, keyGenDesc);
            }
        }

        JDOClassDescriptor jd;
        CacheTypeMapping cacheMapping = clsMap.getCacheTypeMapping();
        if ( cacheMapping != null )
            jd = new JDOClassDescriptor( clsDesc, clsMap.getMapTo().getTable(),
                    keyGenDesc, cacheMapping.getType().toString(), cacheMapping.getCapacity() );
        else
            jd = new JDOClassDescriptor( clsDesc, clsMap.getMapTo().getTable(),
                    keyGenDesc, null, 0 );

        jd.setMapping( clsMap );
        return jd;
    }

    /**
     * Parse the sql type attribute to build an
     * array of types, needed to support whitespace inside
     * parameterized types (see Bug 1045)
     */
    protected String[] getSqlTypes( FieldMapping fieldMap ) {
      if (fieldMap.getSql() == null) { return new String[0]; }
      String sqlType = fieldMap.getSql().getType();
      if (sqlType == null) { return new String[0]; }

      ArrayList types = new ArrayList();
      int current = 0;
      int begin = 0;
      int state = 0;
      while (current < sqlType.length()) {
        switch (state) {
          case 0:
            if (sqlType.charAt(current) == ' ') {
              types.add(sqlType.substring(begin, current));
              begin = current + 1;
            }
            else if (sqlType.charAt(current) == '[') {
              state = 1;
            }
            break;
          case 1:
            if (sqlType.charAt(current) == ']') {
              state = 0;
            }

        }
        current++;
      }
      types.add(sqlType.substring(begin, current));
      String[] result = new String[types.size()];
      return (String[])types.toArray(result);
    }


    protected TypeInfo getTypeInfo( Class fieldType, CollectionHandler colHandler, FieldMapping fieldMap )
        throws MappingException
    {
        TypeConvertor convertorTo = null;
        TypeConvertor convertorFrom = null;
        String        convertorParam = null;
        String        typeName = null;
        Class         sqlType = null;

        fieldType = Types.typeFromPrimitive( fieldType );
        String[] sqlTypes = getSqlTypes(fieldMap);

        if ( fieldMap.getSql() != null && sqlTypes.length > 0 ) {
            //--TO Check
            typeName = sqlTypes[0];
            sqlType = SQLTypes.typeFromName( typeName );
        } else {
            sqlType = fieldType;
        }
        if ( _factory != null ) {
            sqlType = _factory.adjustSqlType( sqlType );
        }
        if ( fieldType != sqlType ) {
            convertorTo = SQLTypes.getConvertor( sqlType, fieldType );
            convertorFrom = SQLTypes.getConvertor( fieldType, sqlType );
            if ( typeName != null ) {
                convertorParam = SQLTypes.paramFromName( typeName );
            }
        }
        return new TypeInfo( fieldType, convertorTo, convertorFrom, convertorParam,
                             fieldMap.getRequired(), null, colHandler );
    }


    protected FieldDescriptor createFieldDesc( Class javaClass, FieldMapping fieldMap )
            throws MappingException {

        FieldDescriptor  fieldDesc;
        String[]           sqlName;
        Class            sqlType;
        int[]            sType;

        // If not an SQL field, return a stock field descriptor.
        if ( fieldMap.getSql() == null )
            return super.createFieldDesc( javaClass, fieldMap );

        // Create a JDO field descriptor
        fieldDesc = super.createFieldDesc( javaClass, fieldMap );

        sqlName = fieldMap.getSql().getName();

        String[] sqlTypes = getSqlTypes(fieldMap);

        int len = sqlTypes.length;
        if ( len > 0 ) {
            sType = new int[len];
            for ( int i=0; i < len; i++ ) {
                sqlType = SQLTypes.typeFromName( sqlTypes[i] );
                if ( _factory != null )
                    sqlType = _factory.adjustSqlType( sqlType );
                sType[i] = SQLTypes.getSQLType( sqlType );
            }
        } else {
            sqlType = fieldDesc.getFieldType();
            if ( _factory != null )
                sqlType = _factory.adjustSqlType( sqlType );
            sType = new int[] {SQLTypes.getSQLType(sqlType)};
        }

        return new JDOFieldDescriptor( (FieldDescriptorImpl) fieldDesc, sqlName, sType,
            ! DirtyType.IGNORE.equals( fieldMap.getSql().getDirty() ),
            fieldMap.getSql().getManyTable(),
            fieldMap.getSql().getManyKey(),
            fieldMap.getSql().getReadonly() );
    }

    public void loadMapping( MappingRoot mapping, Object param )
        throws MappingException
    {
        Enumeration enum;
        _factory = (BaseFactory) param;
        // Load the key generator definitions and check for duplicate names
        enum = mapping.enumerateKeyGeneratorDef();
        while ( enum.hasMoreElements() ) {
            KeyGeneratorDef keyGenDef;
            String name;

            keyGenDef = (KeyGeneratorDef) enum.nextElement();
            name = keyGenDef.getAlias();
            if (name == null) {
                name = keyGenDef.getName();
            }
            if ( _keyGenDefs.get( name ) != null ) {
                throw new MappingException( Messages.format( "mapping.dupKeyGen", name ) );
            }
            _keyGenDefs.put( name, keyGenDef );
        }

        super.loadMapping( mapping, null );

        _keyGenDefs = null;
        _keyGenDescs = null;
        _keyGenReg = null;
    }

}



