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


package org.exolab.castor.jdo;


import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.exolab.castor.jdo.desc.ObjectDesc;
import org.exolab.castor.jdo.desc.FieldDesc;
import org.exolab.castor.jdo.desc.AccessorDesc;
import org.exolab.castor.jdo.desc.ObjectFieldDesc;
import org.exolab.castor.jdo.desc.ContainedFieldDesc;
import org.exolab.castor.jdo.desc.PrimaryKeyDesc;
import org.exolab.castor.jdo.desc.Relation;
import org.exolab.castor.jdo.desc.RelationDesc;
import org.exolab.castor.jdo.mapping.ObjectMapping;
import org.exolab.castor.jdo.mapping.FieldMapping;
import org.exolab.castor.jdo.mapping.ObjectFieldMapping;
import org.exolab.castor.jdo.mapping.PrimaryKeyMapping;
import org.exolab.castor.jdo.mapping.AccessorMapping;
import org.exolab.castor.jdo.mapping.RelationMapping;


/**
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class MappingHelper
{


    public static ObjectDesc createObjectDesc( ObjectMapping objMap, MappingTable mapTable )
	throws MappingException
    {
	Class               objClass;
	String              tableName;
	FieldMapping        fieldMap;
	FieldDesc           fieldDesc;
	ObjectFieldMapping  objField;
	PrimaryKeyDesc      primKeyDesc;
	FieldDesc           primKeyField;
	ClassLoader         loader;
	Enumeration         enum;
	Hashtable           fields;
	RelationDesc[]      related;
	ObjectDesc          extend;

	loader = null;
	// Obtain the class loaded from the current thread, this class,
	// or the system class loader. Success is not guaranteed.
	if ( loader == null )
	    loader = Thread.currentThread().getContextClassLoader();
	if ( loader == null )
	    loader = ObjectDesc.class.getClassLoader();
	if ( loader == null )
	    loader = ClassLoader.getSystemClassLoader();
	if ( loader == null )
	    throw new MappingException( "Could not obtain a suitable class loader" );

	// Find the named class and make sure it has a default
	// public constructor. If not, complain about it.
	try {
	    objClass = loader.loadClass( objMap.getType() );
	} catch ( ClassNotFoundException except ) {
	    throw new MappingException( "Could not locate the class " + objMap.getType() );
	}

	tableName = objMap.getTable();
	if ( tableName == null ) {
	    tableName = Util.javaToSqlName( objClass.getName() );
	}

	if ( objMap.getExtends() != null ) {
	    extend = mapTable.getDescriptor( objMap.getExtends() );
	    if ( extend == null )
		throw new MappingException( "Could not find definition of super mapping " +
					    objMap.getExtends() + " -- forward references not supported" );
	    if ( ! extend.getObjectClass().isAssignableFrom( objClass ) )
		throw new MappingException( "This class must inherit from existing class" );
	} else {
	    extend = null;
	}

	fields = new Hashtable();
	enum = objMap.listFields();
	while ( enum.hasMoreElements() ) {
	    fieldMap = (FieldMapping) enum.nextElement();
	    fieldDesc = createFieldDesc( fieldMap.getName(), fieldMap.getType(), fieldMap.getSqlName(),
					 fieldMap.getAccessor(), loader, objClass );
	    if ( fields.put( fieldDesc.getFieldName(), fieldDesc ) != null )
		throw new MappingException( "Duplicate fields with the same name: " +
					    fieldDesc.getFieldName() );
	}

	enum = objMap.listObjectFields();
	while ( enum.hasMoreElements() ) {
	    FieldDesc[] contained;

	    objField = (ObjectFieldMapping) enum.nextElement();
	    fieldDesc = createFieldDesc( objField.getName(), objField.getType(), "--",
					 objField.getAccessor(), loader, objClass );
	    contained = createContainedDescs( objField, fieldDesc, loader, objClass );
	    fieldDesc = new ObjectFieldDesc( fieldDesc, contained );
	    if ( fields.put( fieldDesc.getFieldName(), fieldDesc ) != null )
		throw new MappingException( "Duplicate fields with the same name: " +
					    fieldDesc.getFieldName() );
	}

	if ( extend != null ) {
	    primKeyDesc = extend.getPrimaryKey();
	    primKeyField = extend.getPrimaryKeyField();
	} else {
	    if ( objMap.getPrimaryKey() == null )
		throw new MappingException( "Cannot create mapping for an object without a primary key" );
	    if ( objMap.getPrimaryKey().getFieldRef() == null ) {
		primKeyDesc = createPrimaryKeyDesc( objMap.getPrimaryKey(), null, loader );
		primKeyField = null;
	    } else {
		primKeyField = (FieldDesc) fields.remove( objMap.getPrimaryKey().getFieldRef().getName() );
		if ( primKeyField == null )
		    throw new MappingException( "Primary key references an undefined field " +
						objMap.getPrimaryKey().getFieldRef().getName() );
		primKeyDesc = createPrimaryKeyDesc( objMap.getPrimaryKey(), primKeyField, loader );
	    }
	}

	enum = objMap.listRelations();
	related = new RelationDesc[ objMap.getRelationCount() ];
	int index;
	for ( index = 0 ; enum.hasMoreElements() ; ++index ) {
	    FieldDesc       relField;
	    RelationMapping relMap;
	    ObjectDesc      relDesc;
	    FieldDesc[]     contained;
	    String          objType;

	    relMap = (RelationMapping) enum.nextElement();
	    if ( relMap.getObjectRef() == null ) {
		relDesc = createObjectDesc( relMap.getObject(), mapTable );
		objType = relMap.getObject().getType();
	    } else {
		objType = relMap.getObjectRef().getType();
		relDesc = mapTable.getDescriptor( objType );
		if ( relDesc == null )
		    throw new MappingException( "Could not find mapping for object " +
						objType );
	    }
	    relField = createFieldDesc( relMap.getName(), objType, "--",
					relMap.getAccessor(), loader, objClass );
	    relField = new ObjectFieldDesc( relField, relDesc.getFieldDescs() );

	    if ( Relation.OneToOne.equals( relMap.getType() ) ) {
		related[ index ] = new RelationDesc( Relation.OneToOne, relDesc, relField,
						     relDesc.getPrimaryKey().getFieldDescs()[ 0 ],
						     relMap.getForeignKey().getSqlColumns()[ 0 ].getSqlName() );
	    } else {
		related[ index ] = new RelationDesc( Relation.ManyToOne, relDesc, relField, null,
						     relMap.getForeignKey().getSqlColumns()[ 0 ].getSqlName() );
	    }
	}

	enum = fields.keys();
	while ( enum.hasMoreElements() ) {
	    String      name;
	    FieldDesc[] descs;

	    name = (String) enum.nextElement();
	    fieldDesc = (FieldDesc) fields.get( name );
	    if ( fieldDesc instanceof ObjectFieldDesc ) {
		fields.remove( name );
		descs = ( (ObjectFieldDesc) fieldDesc ).getFieldDescs();
		for ( int i = 0 ; i < descs.length ; ++i ) {
		    if ( fields.put( fieldDesc.getFieldName() + "." + descs[ i ].getFieldName(), descs[ i ] ) != null )
			throw new MappingException( "Duplicate fields with the same name: " +
						    descs[ i ].getFieldName() );
		}
		// Must create a new enumerator since fields have just
		// been added and must be iterated over. The new
		// enumerator is not created because of remove and so
		// no need to use an iterator.
		enum = fields.keys();
	    }
	}

	return new ObjectDesc( objClass, tableName,
			       (FieldDesc[]) fields.values().toArray( new FieldDesc[ fields.size() ] ),
			       primKeyDesc, primKeyField, extend, related );
    }


    public static FieldDesc createFieldDesc( String fieldName, String fieldType, String sqlName,
					     AccessorMapping accessor, ClassLoader loader,
					     Class objClass )
	throws MappingException
    {
	Class     fieldClass;
	FieldDesc fieldDesc;

	if ( fieldType != null ) {
	    try {
		fieldClass = loader.loadClass( fieldType );
	    } catch ( ClassNotFoundException except ) {
		throw new MappingException( "Could not locate the field type " + fieldType );
	    }
	} else {
	    fieldClass = null;
	}
	
	if ( sqlName == null ) {
	    sqlName = Util.javaToSqlName( fieldName );
	}
	if ( accessor == null ) {
	    fieldDesc = new FieldDesc( findField( objClass, fieldName, fieldClass ), sqlName );
	} else {
	    fieldDesc = new AccessorDesc( findAccessor( objClass, fieldName, accessor.getReader(),
							true, fieldClass ),
					  findAccessor( objClass, fieldName, accessor.getWriter(),
							false, fieldClass ),
					  fieldName, sqlName, objClass );
	}
	return fieldDesc;
    }


    public static FieldDesc[] createContainedDescs( ObjectFieldMapping objField, FieldDesc parentDesc,
						    ClassLoader loader, Class objClass )
	throws MappingException
    {
	FieldMapping[] fieldMaps;
	Enumeration    enum;
	FieldDesc      parent;
	FieldMapping   fieldMap;
	FieldDesc[]    fieldDescs;
	int            i;

	fieldMaps = objField.getFields();
	fieldDescs = new FieldDesc[ fieldMaps.length ];
	for ( i = 0 ; i < fieldMaps.length ; ++i ) {
	    fieldDescs[ i ] = createFieldDesc( fieldMaps[ i ].getName(), fieldMaps[ i ].getType(),
					 fieldMaps[ i ].getSqlName(), fieldMaps[ i ].getAccessor(),
					 loader, objClass );
	    fieldDescs[ i ] = new ContainedFieldDesc( fieldDescs[ i ], parentDesc );
	}
	return fieldDescs;
    }


    public static FieldDesc[] createContainedDescs( ObjectMapping objMap, FieldDesc parentDesc,
						    ClassLoader loader, Class objClass )
	throws MappingException
    {
	FieldMapping[] fieldMaps;
	Enumeration    enum;
	FieldDesc      parent;
	FieldMapping   fieldMap;
	FieldDesc[]    fieldDescs;
	int            i;

	fieldMaps = objMap.getFields();
	fieldDescs = new FieldDesc[ fieldMaps.length ];
	for ( i = 0 ; i < fieldMaps.length ; ++i ) {
	    fieldDescs[ i ] = createFieldDesc( fieldMaps[ i ].getName(), fieldMaps[ i ].getType(),
					 fieldMaps[ i ].getSqlName(), fieldMaps[ i ].getAccessor(),
					 loader, objClass );
	    fieldDescs[ i ] = new ContainedFieldDesc( fieldDescs[ i ], parentDesc );
	}
	return fieldDescs;
    }


    public static Field findField( Class objClass, String fieldName, Class fieldType )
	throws MappingException
    {
	Field field;

	try {
	    // Look up the field based on its name, make sure it's only modifier
	    // is public. If a type was specified, match the field type.
	    field = objClass.getField( fieldName );
	    if ( field.getModifiers() != Modifier.PUBLIC &&
		 field.getModifiers() != ( Modifier.PUBLIC | Modifier.VOLATILE ) )
		throw new MappingException( "Field " + fieldName +
					    " is not public, or is static/transient/final" );
	    if ( fieldType == null ) {
		fieldType = Util.mapFromPrimitive( field.getType() );
	    } else {
		if ( fieldType != Util.mapFromPrimitive( field.getType() ) )
		    throw new MappingException( "Field " + fieldName +
						" is not of specified type " + fieldType );
	    }

	    // Make sure the field type can be represented in a single column
	    if ( ! Util.isSingleColumn( fieldType ) && ! Util.isSerializable( fieldType ) )
		throw new MappingException( "Field " + objClass.getClass().getName() + "." + fieldName +
					    " must be a single column type or a serializable object, otherwise use object-field" );

	    return field;
	} catch ( NoSuchFieldException except ) {
	} catch ( SecurityException except ) {
	}
	// No such/access to field
	throw new MappingException( "Field " + fieldName +
				    " does not exist or is not accessible in " +
				    objClass.getName() );
    }


    public static Method findAccessor( Class objClass, String fieldName, String methodName,
					boolean reader, Class fieldType )
	throws MappingException
    {
	Method   method;
	Method[] methods;
	int      i;

	try {
	    if ( reader ) {
		// Reader: look for the named method or prepend get to the
		// method name. Look up the field and potentially check the
		// return type.
		if ( methodName == null ) {
		    methodName = "get" + Character.toUpperCase( fieldName.charAt( 0 ) ) +
			fieldName.substring( 1 );
		}
		method = objClass.getMethod( methodName, new Class[ 0 ] );

		if ( fieldType == null ) {
		    fieldType = Util.mapFromPrimitive( method.getReturnType() );
		} else {
		    if ( fieldType != Util.mapFromPrimitive( method.getReturnType() ) )
			throw new MappingException( "Field accessor " + methodName +
						    " is not of specified type " + fieldType );
		}
	    } else {
		// Writer: look for the named method or prepend set to the
		// method name. If the field type is know, look up a suitable
		// method. If the fielf type is unknown, lookup the first
		// method with that name and one parameter.
		if ( methodName == null ) {
		    methodName = "set" + Character.toUpperCase( fieldName.charAt( 0 ) ) +
			fieldName.substring( 1 );
		}
		if ( fieldType != null ) {
		    method = objClass.getMethod( methodName, new Class[] { fieldType } );
		} else {
		    methods = objClass.getMethods();
		    method = null;
		    for ( i = 0 ; i < methods.length ; ++i ) {
			if ( methods[ i ].getName().equals( methodName ) &&
			     methods[ i ].getParameterTypes().length == 1 ) {
			    method = methods[ i ];
			    fieldType = Util.mapFromPrimitive( methods[ i ].getParameterTypes()[ 0 ] );
			    break;
			}
		    }
		    if ( method == null )
			throw new NoSuchMethodException();
		}
	    }
	    // Make sure method is public and not abstract/static.
	    if ( method.getModifiers() != Modifier.PUBLIC &&
		 method.getModifiers() != ( Modifier.PUBLIC | Modifier.SYNCHRONIZED ) )
		throw new MappingException( "Field accessor " + methodName +
					    " is not public, or is static/abstract" );
	    // Make sure the field type can be represented in a single column
	    if ( ! Util.isSingleColumn( fieldType ) && ! Util.isSerializable( fieldType ) )
		throw new MappingException( "Field must a single column type or a serializable object, otherwise use object-field" );
	    return method;
	} catch ( NoSuchMethodException except ) {
	} catch ( SecurityException except ) {
	}
	// No such/access to method
	throw new MappingException( "Field accessor " + methodName +
				    " does not exist or is not accessible in " +
				    objClass.getName() );
    }


    public static PrimaryKeyDesc createPrimaryKeyDesc( PrimaryKeyMapping primKeyMap,
						       FieldDesc primKeyField,
						       ClassLoader loader )
	throws MappingException
    {
	Class          objClass;
	PrimaryKeyDesc primKeyDesc;

	try {
	    objClass = loader.loadClass( primKeyMap.getType() );
	} catch ( ClassNotFoundException except ) {
	    throw new MappingException( "Could not locate the primary key type " + primKeyMap.getType() );
	}

	if ( primKeyMap.getFieldRef() != null ) {
	    // Primary key is single/multi column, internal to object
	    if ( primKeyField == null )
		throw new MappingException( "Internal error: Must specify the primary key field for an internal primary key" );
	    if ( Util.mapFromPrimitive( primKeyField.getObjectClass() ) != objClass )
		throw new MappingException( "Primary key type and primary key field type are not the same" );
	    if ( primKeyField instanceof ObjectFieldDesc ) {
		primKeyDesc = new PrimaryKeyDesc( objClass, ( (ObjectFieldDesc) primKeyField ).getFieldDescs() );
	    } else {
		primKeyDesc = new PrimaryKeyDesc( objClass, new FieldDesc[] { primKeyField } );
	    }
	    
	} else if ( primKeyMap.getSqlColumn() != null ) {
	    // Primary key is single column, external to object

	    if ( ! Util.isSingleColumn( objClass ) || objClass.isArray() )
		throw new MappingException( "This type of primary key must be a primitive type" );
	    primKeyDesc = new PrimaryKeyDesc( objClass, primKeyMap.getSqlColumn().getSqlName() );

	} else {
	    // Primary key is multi column, external to object
	    FieldMapping[] fieldMaps;
	    FieldDesc[]    fieldDescs;
	    int            i;

	    if ( Util.isSingleColumn( objClass ) || objClass.isArray() )
		throw new MappingException( "This type of primary key cannot be a primitive or array" );
	    if ( ! Util.isConstructable( objClass ) )
		throw new MappingException( "Primary key class is not constructable" );
	    fieldMaps = primKeyMap.getFields();
	    if ( fieldMaps.length == 0 )
		throw new MappingException( "Primary key must have at least one field" );
	    fieldDescs = new FieldDesc[ fieldMaps.length ];
	    for ( i = 0 ; i < fieldMaps.length ; ++i ) {
		fieldDescs[ i ] = createFieldDesc( fieldMaps[ i ].getName(), fieldMaps[ i ].getType(),
						   fieldMaps[ i ].getSqlName(),
						   fieldMaps[ i ].getAccessor(), loader, objClass );
	    }
	    primKeyDesc = new PrimaryKeyDesc( objClass, fieldDescs );

	}
	return primKeyDesc;
    }


}



