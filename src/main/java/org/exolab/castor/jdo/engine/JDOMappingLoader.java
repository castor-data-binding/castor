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

import org.castor.jdo.engine.SQLTypeInfos;
import org.castor.jdo.engine.SQLTypeConverters;
import org.castor.jdo.engine.SQLTypeConverters.Convertor;
import org.castor.util.Messages;
import org.exolab.castor.mapping.*;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerFriend;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.loader.AbstractMappingLoader;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.*;
import org.exolab.castor.mapping.xml.types.SqlDirtyType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * A JDO implementation of mapping helper. Creates JDO class descriptors
 * from the mapping file.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-13 07:37:49 -0600 (Thu, 13 Apr 2006) $
 */
public final class JDOMappingLoader extends AbstractMappingLoader {
    //-----------------------------------------------------------------------------------

    /** Separators between type name and parameter, e.g. "char[01]". */
    private static final char LEFT_PARAM_SEPARATOR = '[';

    /** Separators after parameter, e.g. "char[01]". */
    private static final char RIGHT_PARAM_SEPARATOR = ']';

    /**
     * Extracts parameter for type convertor from the SQL type definition of the
     * form "SQL_TYPE_NAME[PARAMETER]". If the type is not parameterized, returns
     * null.
     *
     * @param sqlTypeDef SQL type definition (e.g. char[01]).
     * @return Parameter (e.g. "01") or null if not parameterized.
     */
    public static String definition2param(final String sqlTypeDef) {
        int left = sqlTypeDef.indexOf(LEFT_PARAM_SEPARATOR);
        int right = sqlTypeDef.indexOf(RIGHT_PARAM_SEPARATOR);
        if (right < 0) { right = sqlTypeDef.length(); }
        if (left < 0) { return null; }
        return sqlTypeDef.substring(left + 1, right);
    }

    /**
     * Extracts SQL type name from the the SQL type definition of the form
     * "SQL_TYPE_NAME[PARAMETER]".
     *
     * @param sqlTypeDef SQL type definition (e.g. char[01]).
     * @return SQL type name (e.g. "char").
     */
    public static String definition2type(final String sqlTypeDef) {
        int sep = sqlTypeDef.indexOf(LEFT_PARAM_SEPARATOR);
        if (sep < 0) { return sqlTypeDef; }
        return sqlTypeDef.substring(0, sep);
    }

    //-----------------------------------------------------------------------------------



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
    
    private boolean _loaded = false;


    /**
     * Used by the constructor for creating key generators.
     * Each database must have a proprietary KeyGeneratorRegistry instance
     * Otherwise it is impossible to implement correctly stateful
     * key generator algorithms like HIGH-LOW.
     * See {@link #loadMapping}.
     */
    private KeyGeneratorRegistry _keyGenReg = new KeyGeneratorRegistry();


    public JDOMappingLoader(ClassLoader loader) {
        super(loader);
    }
    
    public void clear() {
        super.clear();
        _loaded = false;
    }

    public BindingType getBindingType() { return BindingType.JDO; }

    protected ClassDescriptor createDescriptor(final ClassMapping clsMap)
    throws MappingException {
        ClassDescriptor clsDesc;
        String keyGenName;
        KeyGeneratorDescriptor keyGenDesc;

        // If no SQL information for class, ignore it. JDO only
        // supports JDO class descriptors.
        if ((clsMap.getMapTo() == null) || (clsMap.getMapTo().getTable() == null)) {
            return AbstractMappingLoader.NoDescriptor;
        }

        // See if we have a compiled descriptor.
        clsDesc = loadClassDescriptor(clsMap.getName());
        if ((clsDesc != null) && (clsDesc instanceof JDOClassDescriptor)) {
            return clsDesc;
        }

        // Use super class to create class descriptor. Field descriptors will be
        // generated only for supported fields, see createFieldDesc later on.
        // This class may only extend a JDO class, otherwise no mapping will be
        // found for the parent.
        clsDesc = super.createDescriptor(clsMap);

        // JDO descriptor must include an identity field, the identity field
        // is either a field, or a container field containing only JDO fields.
        // If the identity field is not a JDO field, it will be cleaned later
        // on (we need the descriptor for relations mapping).
        if (clsDesc.getIdentity() == null) {
            throw new MappingException("mapping.noIdentity", clsDesc.getJavaClass().getName());
        }

        // create a key generator descriptor
        keyGenName = clsMap.getKeyGenerator();
        keyGenDesc = null;
        if (keyGenName != null) {
            String keyGenFactoryName;
            KeyGeneratorDef keyGenDef;
            Enumeration enumeration;
            Properties params;

            // first search among declared key generators
            // and resolve alias
            keyGenDef = (KeyGeneratorDef) _keyGenDefs.get(keyGenName);
            params = new Properties();
            keyGenFactoryName = keyGenName;
            if (keyGenDef != null) {
                keyGenFactoryName = keyGenDef.getName();
                enumeration = keyGenDef.enumerateParam();
                while (enumeration.hasMoreElements()) {
                    Param par = (Param) enumeration.nextElement();
                    params.put(par.getName(), par.getValue());
                }
            }
            keyGenDesc = (KeyGeneratorDescriptor) _keyGenDescs.get(keyGenName);
            if (keyGenDesc == null) {
                keyGenDesc = new KeyGeneratorDescriptor(keyGenName,
                        keyGenFactoryName, params, _keyGenReg);
                _keyGenDescs.put(keyGenName, keyGenDesc);
            }
        }

        return new JDOClassDescriptor(clsDesc, keyGenDesc);
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
            sqlType = SQLTypeInfos.sqlTypeName2javaType( definition2type(typeName) );
        } else {
            sqlType = fieldType;
        }
        if ( _factory != null ) {
            sqlType = _factory.adjustSqlType( sqlType );
        }
        if ( fieldType != sqlType ) {
            try {
                convertorTo = SQLTypeConverters.getConvertor( sqlType, fieldType );
            } catch (MappingException ex) {
                boolean isTypeSafeEnum = false;
                //-- check for type-safe enum style classes
                if ((fieldType != null) && (!isPrimitive(fieldType))) {
                    //-- make sure no default constructor
                    Constructor cons = null;
                    try {
                        cons = fieldType.getConstructor(EMPTY_ARGS);
                        if (!Modifier.isPublic(cons.getModifiers())) {
                            cons = null;
                        }
                    }
                    catch(NoSuchMethodException nsmx) {
                        //-- Do nothing
                    }
                    try {
                        if (cons == null) {
                            //-- make sure a valueOf factory method
                            //-- exists and no user specified handler exists
                            Method method = fieldType.getMethod(VALUE_OF, STRING_ARG);
                            Class returnType = method.getReturnType();
                            if ((returnType != null) && fieldType.isAssignableFrom(returnType)) {
                                int mods = method.getModifiers();
                                if (Modifier.isStatic(mods)) {
                                    // create individual SQLTypeConverter
                                    convertorTo = new Convertor( sqlType, fieldType ) {
                                        private Method method = null;
                                        public Object convert( Object obj, String param ) {
                                            try {
                                                if (method == null)  method = toType().getMethod(VALUE_OF, STRING_ARG);
                                                return method.invoke(toType(), new Object[] { (String)obj });
                                            } catch (Exception ex) {
                                                return null;
                                            }
                                        }
                                    } ;

                                    Types.addEnumType(fieldType);
                                    
                                    isTypeSafeEnum = true;
                                }
                            }
                        }
                    }
                    catch(NoSuchMethodException nsmx) {
                        //-- Do nothing
                    }
                }
                if (!isTypeSafeEnum)
                    throw new MappingException( "mapping.noConvertor", sqlType.getName(), fieldType.getName() );
            }
            convertorFrom = SQLTypeConverters.getConvertor( fieldType, sqlType );
            if ( typeName != null ) {
                convertorParam = definition2param(typeName);
            }
        }
        return new TypeInfo( fieldType, convertorTo, convertorFrom, convertorParam,
                             fieldMap.getRequired(), null, colHandler );
    }


    protected FieldDescriptor createFieldDesc( Class javaClass, FieldMapping fieldMap )
            throws MappingException {

        // FieldDescriptor  fieldDesc;
        String[]           sqlName;
        Class            sqlType;
        int[]            sType;

        // If not an SQL field, return a stock field descriptor.
        if ( fieldMap.getSql() == null )
            return super.createFieldDesc( javaClass, fieldMap );
        
        String fieldName = fieldMap.getName();
        
        // If the field type is supplied, grab it and use it to locate the
        // field/accessor.
        Class fieldType = null;
        if ( fieldMap.getType() != null ) {
            try {
                fieldType = resolveType( fieldMap.getType() );
            } catch ( ClassNotFoundException except ) {
                throw new MappingException( "mapping.classNotFound", fieldMap.getType() );
            }
        }
        
        // If the field is declared as a collection, grab the collection type as
        // well and use it to locate the field/accessor.
        CollectionHandler colHandler = null;
        if ( fieldMap.getCollection() != null ) {
            Class colType = CollectionHandlers.getCollectionType( fieldMap.getCollection().toString() );
            colHandler = CollectionHandlers.getHandler( colType );
            
            if (colType.getName().equals("java.util.Iterator")
                    && fieldMap.getLazy() == true) 
            {
                String err = "Lazy loading not supported for collection type 'iterator'";
                throw new MappingException(err);
            }

            if (colType.getName().equals("java.util.Enumeration")
                    && fieldMap.getLazy() == true) 
            {
                String err = "Lazy loading not supported for collection type 'enumerate'";
                throw new MappingException(err);
            }
        }
        
        TypeInfo typeInfo = getTypeInfo( fieldType, colHandler, fieldMap );
            
        ExtendedFieldHandler exfHandler = null;
        FieldHandler handler = null;
        
        //-- check for user supplied FieldHandler
        if (fieldMap.getHandler() != null) {
            
            Class handlerClass = null;
            try {
                handlerClass = resolveType( fieldMap.getHandler() );
            }
            catch (ClassNotFoundException except) {
                throw new MappingException( "mapping.classNotFound", fieldMap.getHandler() );
            }
            
            if (!FieldHandler.class.isAssignableFrom(handlerClass)) {
                String err = "The class '" + fieldMap.getHandler() + 
                    "' must implement " + FieldHandler.class.getName();
                throw new MappingException(err);
            }
            
            //-- get default constructor to invoke. We can't use the
            //-- newInstance method unfortunately becaue FieldHandler
            //-- overloads this method 
            Constructor constructor = null;
            try {
                constructor = handlerClass.getConstructor(new Class[0]);
                handler = (FieldHandler) 
                    constructor.newInstance(new Object[0]);
            }
            catch(java.lang.Exception except) {
                String err = "The class '" + handlerClass.getName() + 
                    "' must have a default public constructor.";
                throw new MappingException(err);
            }
            
            
            //-- ExtendedFieldHandler?
            if (handler instanceof ExtendedFieldHandler) {
                exfHandler = (ExtendedFieldHandler) handler;
            }
            
            //-- Fix for Castor JDO from Steve Vaughan, Castor JDO
            //-- requires FieldHandlerImpl or a ClassCastException
            //-- will be thrown... [KV 20030131 - also make sure this new handler 
            //-- doesn't use it's own CollectionHandler otherwise
            //-- it'll cause unwanted calls to the getValue method during
            //-- unmarshalling]
            colHandler = typeInfo.getCollectionHandler();
            typeInfo.setCollectionHandler(null);
            handler = new FieldHandlerImpl(handler, typeInfo);
            typeInfo.setCollectionHandler(colHandler);
            //-- End Castor JDO fix
            
        } 
        
        boolean generalized = (exfHandler instanceof GeneralizedFieldHandler);
        
        //-- if generalized we need to change the fieldType to whatever
        //-- is specified in the GeneralizedFieldHandler so that the
        //-- correct getter/setter methods can be found
        FieldHandler custom = handler;
        if (generalized) {
            fieldType = ((GeneralizedFieldHandler)exfHandler).getFieldType();
        }
        
        if (generalized || (handler == null)) {
            //-- create TypeInfoRef to get new TypeInfo from call
            //-- to createFieldHandler
            TypeInfoReference typeInfoRef = new TypeInfoReference();
            typeInfoRef.typeInfo = typeInfo;
            handler = createFieldHandler(javaClass, fieldType, fieldMap, typeInfoRef);
            if (custom != null) {
                ((GeneralizedFieldHandler)exfHandler).setFieldHandler(handler);
                handler = custom;
            }
            else  typeInfo = typeInfoRef.typeInfo;
        }
                
        FieldDescriptorImpl fieldDesc 
            = new FieldDescriptorImpl(fieldName, typeInfo, handler,
                fieldMap.getTransient(), fieldMap.getComparator());

        fieldDesc.setRequired(fieldMap.getRequired());

        //-- If we're using an ExtendedFieldHandler we need to set the 
        //-- FieldDescriptor
        if (exfHandler != null)
            ((FieldHandlerFriend)exfHandler).setFieldDescriptor(fieldDesc);

        // if SQL mapping declares transient 
        if ( fieldMap.getSql().getTransient()) {
        	fieldDesc.setTransient (true);
        }
        
        sqlName = fieldMap.getSql().getName();

        String[] sqlTypes = getSqlTypes(fieldMap);

        int len = sqlTypes.length;
        if ( len > 0 ) {
            sType = new int[len];
            for ( int i=0; i < len; i++ ) {
                sqlType = SQLTypeInfos.sqlTypeName2javaType( definition2type(sqlTypes[i]) );
                if ( _factory != null )
                    sqlType = _factory.adjustSqlType( sqlType );
                sType[i] = SQLTypeInfos.javaType2sqlTypeNum( sqlType );
            }
        } else {
            sqlType = fieldDesc.getFieldType();
            if ( _factory != null )
                sqlType = _factory.adjustSqlType( sqlType );
            sType = new int[] {SQLTypeInfos.javaType2sqlTypeNum(sqlType)};
        }

        return new JDOFieldDescriptor( fieldDesc, sqlName, sType,
            !SqlDirtyType.IGNORE.equals( fieldMap.getSql().getDirty() ),
            fieldMap.getSql().getManyTable(),
            fieldMap.getSql().getManyKey(),
            fieldMap.getSql().getReadOnly() );
    }

    public void loadMapping( MappingRoot mapping, Object param )
        throws MappingException
    {
        if (!_loaded) {
            _loaded = true;
            
            Enumeration enumeration;
            _factory = (BaseFactory) param;
            // Load the key generator definitions and check for duplicate names
            enumeration = mapping.enumerateKeyGeneratorDef();
            while ( enumeration.hasMoreElements() ) {
                KeyGeneratorDef keyGenDef;
                String name;

                keyGenDef = (KeyGeneratorDef) enumeration.nextElement();
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

}
