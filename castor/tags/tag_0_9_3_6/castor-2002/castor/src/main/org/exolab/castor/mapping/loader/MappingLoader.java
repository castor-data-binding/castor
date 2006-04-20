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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.Container;
import org.exolab.castor.mapping.xml.Include;
import org.exolab.castor.util.Messages;


/**
 * Assists in the construction of descriptors. Can be used as a mapping
 * resolver to the engine. Engines will implement their own mapping
 * scheme typically by extending this class.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public abstract class MappingLoader
    implements MappingResolver
{


    /**
     * The suffix for the name of a compiled class.
     */
    private static final String CompiledSuffix = "Descriptor";

    /**
     * The prefix for the "add" method
    **/
    private static final String ADD_METHOD_PREFIX = "add";


    /**
     * All class descriptors added so far, keyed by Java class.
     */
    private Hashtable  _clsDescs = new Hashtable();


    /**
     * All Java classes in the original order.
     */
    private Vector  _javaClasses = new Vector();


    /**
     * Log writer to report progress. May be null.
     */
    private PrintWriter _logWriter;


    /**
     * The class loader to use.
     */
    private ClassLoader _loader;


    public static final ClassDescriptor NoDescriptor = new ClassDescriptorImpl( Class.class );


    /**
     * Constructs a new mapping helper. This constructor is used by
     * a derived class.
     *
     * @param loader The class loader to use, null for the default
     */
    protected MappingLoader( ClassLoader loader, PrintWriter logWriter )
    {
        if ( loader == null )
            loader = getClass().getClassLoader();
        _loader = loader;
        _logWriter = logWriter;
    }


    public ClassDescriptor getDescriptor( Class type )
    {
        return (ClassDescriptor) _clsDescs.get( type );
    }


    public Enumeration listDescriptors()
    {
        return _clsDescs.elements();
    }


    public Enumeration listJavaClasses()
    {
        //return _clsDescs.keys();
        // The original order of classes is important for CasheEngine
        return _javaClasses.elements();
    }


    public ClassLoader getClassLoader()
    {
        return _loader;
    }


    /**
     * Returns the log writer. If not null, errors and other messages
     * should be directed to the log writer.
     */
    protected PrintWriter getLogWriter()
    {
        return _logWriter;
    }


    /**
     * Returns the Java class for the named type. The type name can
     * be one of the accepted short names (e.g. <tt>integer</tt>) or
     * the full Java class name (e.g. <tt>java.lang.Integer</tt>).
     * If the short name is used, the primitive type might be returned.
     */
    protected Class resolveType( String typeName )
        throws ClassNotFoundException
    {
        return Types.typeFromName( _loader, typeName );
    }


    /**
     * Loads the mapping from the specified mapping object. Calls {@link
     * #createDescriptor} to create each descriptor and {@link
     * #addDescriptor} to store it. Also loads all the included mapping
     * files.
     *
     * @param mapping The mapping information
     * @param param Arbitrary parameter that can be used by subclasses
     * @throws MappingException The mapping file is invalid
     */
    public void loadMapping( MappingRoot mapping, Object param )
        throws MappingException
    {
        Enumeration   enum;

        // Load the mapping for all the classes. This is always returned
        // in the same order as it appeared in the mapping file.
        enum = mapping.enumerateClassMapping();
        while ( enum.hasMoreElements() ) {
            ClassMapping    clsMap;
            ClassDescriptor clsDesc;

            clsMap = (ClassMapping) enum.nextElement();
            clsDesc = createDescriptor( clsMap );
            if ( clsDesc != NoDescriptor )
                addDescriptor( clsDesc );
            // If the return value is NoDescriptor then the derived
            // class was not successful in constructing a descriptor.
            if ( clsDesc == NoDescriptor && _logWriter != null ) {
                _logWriter.println( Messages.format( "mapping.ignoringMapping", clsMap.getName() ) );
            }
        }

        enum = _clsDescs.elements();
        while ( enum.hasMoreElements() ) {
            ClassDescriptor clsDesc;

            clsDesc = (ClassDescriptor) enum.nextElement();
            if ( clsDesc != NoDescriptor  )
                resolveRelations( clsDesc );
            }
        }


    /**
     * Adds a class descriptor. Will throw a mapping exception if a
     * descriptor for this class already exists.
     *
     * @param clsDesc The descriptor to add
     * @throws MappingException A descriptor for this class already
     *  exists
     */
    protected void addDescriptor( ClassDescriptor clsDesc )
        throws MappingException
    {
        if ( _clsDescs.containsKey( clsDesc.getJavaClass() ) )
            throw new MappingException( "mapping.duplicateDescriptors", clsDesc.getJavaClass().getName() );
        _clsDescs.put( clsDesc.getJavaClass(), clsDesc );
        _javaClasses.addElement( clsDesc.getJavaClass() );
    }


    protected void resolveRelations( ClassDescriptor clsDesc )
        throws MappingException
    {
        FieldDescriptor[] fields;

        fields = clsDesc.getFields();
        for ( int i = 0 ; i < fields.length ; ++i ) {
            ClassDescriptor   relDesc;

            relDesc = getDescriptor( fields[ i ].getFieldType() );
            if ( relDesc == NoDescriptor ) {
                // XXX Error message should come here
            } else if ( relDesc != null && fields[ i ] instanceof FieldDescriptorImpl ) {
                ( (FieldDescriptorImpl) fields[ i ] ).setClassDescriptor( relDesc );
            }
        }
    }


    /**
     * Creates a new descriptor. The class mapping information is used
     * to create a new stock {@link ClassDescriptor}. Implementations may
     * extend this class to create a more suitable descriptor.
     *
     * @param clsMap The class mapping information
     * @throws MappingException An exception indicating why mapping for
     *  the class cannot be created
     */
    protected ClassDescriptor createDescriptor( ClassMapping clsMap )
            throws MappingException {

        FieldDescriptor[] fields;
        FieldDescriptor[] identities;
        Enumeration       enum;
        Class             javaClass;
        ClassDescriptor   extend;
        ClassDescriptor   depend;
        Container         contMaps[];
        Vector            relations;
        ClassDescriptor   clsDesc;

        // See if we have a compiled descriptor.
        clsDesc = loadClassDescriptor( clsMap.getName() );
        if ( clsDesc != null )
            return clsDesc;

        // Obtain the Java class.
        try {
            javaClass = resolveType( clsMap.getName() );
        } catch ( ClassNotFoundException except ) {
            throw new MappingException( "mapping.classNotFound", clsMap.getName() );
        }

        // If this class extends another class, need to obtain the extended
        // class and make sure this class indeed extends it.
        if ( clsMap.getExtends() != null ) {
            try {
                extend = getDescriptor( resolveType( ( (ClassMapping) clsMap.getExtends() ).getName() ) );
                if ( extend == null )
                    throw new MappingException( "mapping.extendsMissing",
                                                clsMap.getExtends(), javaClass.getName() );
                if ( extend == NoDescriptor )
                    throw new MappingException( "mapping.extendsNoMapping",
                                                clsMap.getExtends(), javaClass.getName() );
            } catch ( ClassNotFoundException except ) {
                throw new MappingException( except );
            }
        } else
            extend = null;


        // If this class depends another class, need to obtain the depended class
        if ( clsMap.getDepends() != null ) {
            try {
                depend = getDescriptor( resolveType( ( (ClassMapping)  clsMap.getDepends() ).getName() ) );
                if ( depend == null )
                    throw new MappingException( "Depends not found" +
                                                clsMap.getDepends() + " " + javaClass.getName() );
                if ( depend == NoDescriptor )
                    throw new MappingException( "Depends not found" +
                                                clsMap.getDepends() + " " + javaClass.getName() );
            } catch ( ClassNotFoundException except ) {
                throw new MappingException( except );
            }
        } else
            depend = null;


        // Get field descriptors first. Note: order must be preserved for fields,
        // but not for relations or container fields. Add all the container fields
        // in there.
        FieldMapping[] fm = clsMap.getFieldMapping();
        fields = createFieldDescs( javaClass, fm );

        // Make sure there are no two fields with the same name.
        // Crude but effective way of doing this.
        for ( int i = 0 ; i < fields.length ; ++i ) {
            for ( int j = i + 1 ; j < fields.length ; ++j ) {
                if ( fields[ i ].getFieldName().equals( fields[ j ].getFieldName() ) )
                    throw new MappingException( "The field " + fields[ i ].getFieldName() +
                                                " appears twice in the descriptor for " +
                                                javaClass.getName() );
            }
        }

        // Obtain the identity field from one of the above fields.
        // The identity field is removed from the list of fields.
        identities = null;
        boolean idfield = false;
        String[] ids;
        ClassMapping origin = clsMap;
        Vector fieldList = new Vector();

        while (origin.getExtends() != null) {
            origin = (ClassMapping) origin.getExtends();
        }
        ids = origin.getIdentity();
        if (( ids != null ) && ( ids.length > 0)) {

            // Check that an XML mapping file do not declare more identity
            // attributes for a given class than there are field elements
            // defined for that class.
            // (Patch submitted by Gabriel Richard Pack <gpack@electroneconomy.com>)
            if ( ids.length > fields.length && origin == clsMap ) {
                String badIdentities = "";
                String delimiter     = " or ";
                for ( int index = 0; index < ids.length; index++ ) {
                    badIdentities += ids[index];
                    if ( index != ids.length - 1 )
                        badIdentities += delimiter;
                }
                throw new MappingException( "mapping.identityMissing",
                badIdentities, javaClass.getName() );
            }

            identities = new FieldDescriptor[ids.length];
            // separates fields into identity fields and regular fields
            for ( int i=0; i < fields.length ; i++ ) {
                //System.out.println("MappingLoader.createClassDesc.for:id: " + i );
                idfield = false;
                for ( int k=0; k<ids.length; k++ ) {
                    //System.out.println(fields[i].getFieldName() + " " + ids[k] );
                    if ( fields[i].getFieldName().equals( ids[k] ) ) {
                        identities[k] = fields[i];
                        idfield = true;
                        break;
                    }
                }
                if ( idfield ) {
                    //System.out.println("Field["+i+"] is an id field");
                    if ( fields[i] instanceof FieldDescriptorImpl )
                        ( (FieldDescriptorImpl) fields[i] ).setRequired( true );
                    if ( fields[i].getHandler() instanceof FieldHandlerImpl )
                        ( (FieldHandlerImpl) fields[i].getHandler() ).setRequired( true );
                } else {
                    // copy non identity field from list of fields.
                    fieldList.addElement(fields[i]);
                }
            }

            if (extend != null) {
                // we allow identity fields to be re-defined in the extends
                // class mapping to override some properties of the field,
                // for example, <sql name="..."/>.
                if ( extend instanceof ClassDescriptorImpl ) {
                    ClassDescriptorImpl extendImpl = (ClassDescriptorImpl) extend;
                    for (int i = 0; i < identities.length; i++) {
                        if (identities[i] == null) {
                            identities[i] = extendImpl.getIdentities()[i];
                        }
                    }
                } else {
                    // we leave things in the old way for the XML side
                    if ( identities[0] == null )
                        if ( extend.getIdentity() != null ) {
                            identities = new FieldDescriptor[] {extend.getIdentity()};
                        } else {
                            identities = new FieldDescriptor[0];
                        }
                }
            }

            // convert fieldList into array
            fields = new FieldDescriptor[fieldList.size()];
            fieldList.copyInto(fields);

            // the following check only needed by JDO side, move it to JDOMappingLoader
            /*
            if ( identities == null || identities.length == 0 ) {
                throw new MappingException( "mapping.identityMissing", clsMap.getIdentity(),
                                            javaClass.getName() );
            }*/

            // do a more general test instead
            if ( ids != null && ids.length > 0
                    && (identities == null || identities.length <= 0 ) ) {
                StringBuffer sb = new StringBuffer();
                for ( int i=0; i < ids.length; i++ ) {
                    if ( i != 0 ) sb.append("/");
                    sb.append( ids[i] );
                }
                throw new MappingException("mapping.identityMissing", sb,
                        javaClass.getName() );
            }
        }



        // Create the class descriptor.
        String hack = (clsMap.getAccess() == null)?"shared":clsMap.getAccess().toString();
        clsDesc = new ClassDescriptorImpl( javaClass, fields, identities, extend, depend,
                                           AccessMode.getAccessMode(hack) );

        if ( clsDesc instanceof ClassDescriptorImpl )
            ((ClassDescriptorImpl)clsDesc).setMapping( clsMap );

        return clsDesc;
    }

    // expect a string which seperated by normalized delimitator
    private String[] breakApart( String strings, char delimit ) {
        if ( strings == null )
            return new String[0];
        Vector v = new Vector();
        int start = 0;
        int count = 0;
        while ( count < strings.length() ) {
            if ( strings.charAt( count ) == delimit ) {
                if ( start < (count - 1) ) {
                    //System.out.println( "("+strings.substring( start, count )+")" );
                    v.addElement( strings.substring( start, count ) );
                    count++;
                    start = count;
                    continue;
                }
            }
            count++;
        }
        if ( start < (count - 1) ) {
            //System.out.println( "("+strings.substring( start, count )+")" );
            v.addElement( strings.substring( start, count ) );
        }

        String[] result = new String[v.size()];
        v.copyInto( result );
        return result;
    }

    /**
     * Create field descriptors. The class mapping information is used
     * to create descriptors for all the fields in the class, except
     * for container fields. Implementations may extend this method to
     * create more suitable descriptors, or create descriptors only for
     * a subset of the fields.
     *
     * @param javaClass The class to which the fields belong
     * @param fieldMaps The field mappings
     * @throws MappingException An exception indicating why mapping for
     *  the class cannot be created
     */
    protected FieldDescriptor[] createFieldDescs( Class javaClass, FieldMapping[] fieldMaps )
            throws MappingException {
        FieldDescriptor[] fields;

        if ( fieldMaps == null || fieldMaps.length == 0 )
            return new FieldDescriptor[ 0 ];
        fields = new FieldDescriptor[ fieldMaps.length ];
        for ( int i = 0 ; i < fieldMaps.length ; ++i )
            fields[ i ] = createFieldDesc( javaClass, fieldMaps[ i ] );
        return fields;
    }


    /**
     * Create container field descriptor. The contained mapping is used
     * to create a single field descriptor which includes several field
     * descriptors for all contained fields.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldMap The field mapping
     * @throws MappingException An exception indicating why mapping for
     *  the class cannot be created
     */
    /*
    protected ContainerFieldDesc createContainerFieldDesc( Class javaClass, Container fieldMap )
        throws MappingException
    {
        // Container type must be constructable.
        if ( ! Types.isConstructable( fieldType ) )
            throw new MappingException( "mapping.classNotConstructable", fieldType.getName() );
        // Create descriptors for all the fields.
        fields = createFieldDescs( fieldType, fieldMap.getFieldMapping() );
        return new ContainerFieldDesc( fieldMap.getName(), fieldType, handler, fields );
    }
    */


    /**
     * Creates a single field descriptor. The field mapping is used to
     * create a new stock {@link FieldDescriptor}. Implementations may
     * extend this class to create a more suitable descriptor.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldMap The field mapping information
     * @return The field descriptor
     * @throws MappingException The field or its accessor methods are not
     *  found, not accessible, not of the specified type, etc
     */
    protected FieldDescriptor createFieldDesc( Class javaClass, FieldMapping fieldMap )
        throws MappingException
    {
        TypeInfo          typeInfo;
        Class             fieldType = null;
        Class             colType = null;
        CollectionHandler colHandler = null;
        boolean           getSetCollection = true;
        FieldHandlerImpl  handler;
        String            fieldName;
        Method            getMethod = null;
        Method            setMethod = null;
        Method[]          getSequence = null;
        Method[]          setSequence = null;


        // If the field type is supplied, grab it and use it to locate the
        // field/accessor.
        if ( fieldMap.getType() != null ) {
            try {
                fieldType = resolveType( fieldMap.getType() );
            } catch ( ClassNotFoundException except ) {
                throw new MappingException( "mapping.classNotFound", fieldMap.getType() );
            }
        }
        // If the field is declared as a collection, grab the collection type as
        // well and use it to locate the field/accessor.
        if ( fieldMap.getCollection() != null ) {
            colType = CollectionHandlers.getCollectionType( fieldMap.getCollection().toString() );
            colHandler = CollectionHandlers.getHandler( colType );
            getSetCollection = CollectionHandlers.isGetSetCollection( colType );
            if ( colType == Object[].class ) {
                Object obj = Array.newInstance(fieldType, 0);
                colType = obj.getClass();
            }
        }

        fieldName = fieldMap.getName();

        // If get/set methods not specified, use field names to determine them.
        if ( fieldMap.getDirect() ) {
            // No accessor, map field directly.
            Field field;

            field = findField( javaClass, fieldName, ( colType == null ? fieldType : colType ) );
            if ( field == null )
                throw new MappingException( "mapping.fieldNotAccessible", fieldName, javaClass.getName() );
            if ( fieldType == null )
                fieldType = field.getType();
            typeInfo = getTypeInfo( fieldType, colHandler, fieldMap );
            handler = new FieldHandlerImpl( field, typeInfo );
        } else {

            if ( fieldMap.getGetMethod() == null && fieldMap.getSetMethod() == null ) {
                int    point;
                Vector getSeq = new Vector();
                Vector setSeq = new Vector();
                String methodName;
                Method method;

                if ( fieldName == null )
                    throw new MappingException( "mapping.missingFieldName", javaClass.getName() );

                try {
                    while ( true ) {
                        Class last;

                        point = fieldName.indexOf( '.' );
                        if ( point < 0 )
                            break;
                        last = javaClass;
                        // getter
                        methodName = "get" + capitalize( fieldName.substring( 0, point ) );
                        method = javaClass.getMethod( methodName, null );
                        fieldName = fieldName.substring( point + 1 );
                        // Make sure method is not abstract/static
                        // (note: Class.getMethod() returns only public methods).
                        if ( ( method.getModifiers() & Modifier.ABSTRACT ) != 0 ||
                             ( method.getModifiers() & Modifier.STATIC ) != 0 )
                            throw new MappingException( "mapping.accessorNotAccessible",
                                                        methodName, javaClass.getName() );
                        getSeq.addElement( method );
                        javaClass = method.getReturnType();
                        // setter;   Note: javaClass already changed, use "last"
                        methodName = "set" + methodName.substring(3);
                        try {
                            method = last.getMethod( methodName, new Class[] { javaClass } );
                            if ( ( method.getModifiers() & Modifier.ABSTRACT ) != 0 ||
                                 ( method.getModifiers() & Modifier.STATIC ) != 0 )
                                method = null;
                        } catch ( Exception except ) {
                            method = null;
                        }
                        setSeq.addElement( method );
                    }
                    if ( getSeq.size() > 0 ) {
                        getSequence = new Method[ getSeq.size() ];
                        getSeq.copyInto( getSequence );
                        setSequence = new Method[ setSeq.size() ];
                        setSeq.copyInto( setSequence );
                    }
                    getMethod = findAccessor( javaClass, "get" + capitalize( fieldName ),
                                            ( colType == null ? fieldType : colType ), true );
                } catch ( MappingException except ) {
                    throw except;
                } catch ( Exception except ) {
                }
                if ( getMethod == null )
                    throw new MappingException( "mapping.accessorNotFound",
                                                "get" + capitalize( fieldName ),
                                                ( colType == null ? fieldType : colType ),
                                                javaClass.getName() );
                if ( fieldType == null && colType == null )
                    fieldType = getMethod.getReturnType();


                // We try to locate a set method anyway and we complain only if we really need one.
                    setMethod = findAccessor( javaClass, "set" + capitalize( fieldName ),
                                              ( colType == null ? fieldType : colType ), false );

                // If we have a collection that need both set and get and that
                // we don't have a set method, we fail
                if ( setMethod == null && colType != null && getSetCollection )
                        throw new MappingException( "mapping.accessorNotFound",
                                                    "set" + capitalize( fieldName ),
                                                    ( colType == null ? fieldType : colType ),
                                                    javaClass.getName() );


            } else {
                // First look up the get accessors
                if ( fieldMap.getGetMethod() != null ) {
                    getMethod = findAccessor( javaClass, fieldMap.getGetMethod(),
                                              ( colType == null ? fieldType : colType ), true );
                    if ( getMethod == null )
                        throw new MappingException( "mapping.accessorNotFound",
                                                    fieldMap.getGetMethod(), ( colType == null ? fieldType : colType ),
                                                    javaClass.getName() );
                    if ( fieldType == null && colType == null )
                        fieldType = getMethod.getReturnType();
                }

                // Second look up the set/add accessor
                if ( fieldMap.getSetMethod() != null ) {

                    String methodName = fieldMap.getSetMethod();
                    Class type = fieldType;
                    if (colType != null) {
                        if (!methodName.startsWith(ADD_METHOD_PREFIX))
                            type = colType;
                    }

                    setMethod = findAccessor( javaClass, fieldMap.getSetMethod(),
                                              type , false );
                    if ( setMethod == null )
                        throw new MappingException( "mapping.accessorNotFound",
                                                    fieldMap.getSetMethod(), type,
                                                    javaClass.getName() );
                    if ( fieldType == null )
                        fieldType = setMethod.getParameterTypes()[ 0 ];
                }
            }

            typeInfo = getTypeInfo( fieldType, colHandler, fieldMap );
              
            fieldName = fieldMap.getName(); // Not the same for nested fields
            if ( fieldName == null )
                fieldName = ( getMethod == null ? setMethod.getName() : getMethod.getName() );
            handler = new FieldHandlerImpl( fieldName, getSequence, setSequence, getMethod, setMethod, typeInfo );

            if ((setMethod != null) && (setMethod.getName().startsWith(ADD_METHOD_PREFIX)))
                handler.setAddMethod(setMethod);
        }

        // If there is a create method, add it to the field handler
        if ( fieldMap.getCreateMethod() != null ) {
            try {
                Method method;

                method = javaClass.getMethod( fieldMap.getCreateMethod(), null );
                handler.setCreateMethod( method );
            } catch ( Exception except ) {
                // No such/access to method
                throw new MappingException( "mapping.createMethodNotFound",
                                            fieldMap.getCreateMethod(), javaClass.getName() );
            }
        } else if ( fieldName != null && ! Types.isSimpleType( fieldType ) ) {
            try {
                Method method;

                method = javaClass.getMethod( "create" + capitalize( fieldName ), null );
                handler.setCreateMethod( method );
            } catch ( Exception except ) { }
        }

        // If there is an has/delete method, add them to field handler
        if ( fieldName != null ) {
            Method hasMethod = null;
            Method deleteMethod = null;

            try {
                hasMethod = javaClass.getMethod( "has" + capitalize( fieldName ), null );
                if ( ( hasMethod.getModifiers() & Modifier.ABSTRACT ) != 0 ||
                     ( hasMethod.getModifiers() & Modifier.STATIC ) != 0 )
                    hasMethod = null;
                try {
                    if ( ( hasMethod.getModifiers() & Modifier.ABSTRACT ) != 0 ||
                         ( hasMethod.getModifiers() & Modifier.STATIC ) != 0 )
                        deleteMethod = null;
                    deleteMethod = javaClass.getMethod( "delete" + capitalize( fieldName ), null );
                } catch ( Exception except ) { }
                handler.setHasDeleteMethod( hasMethod, deleteMethod );
            } catch ( Exception except ) { }
        }

        FieldDescriptorImpl fieldDesc =
            new FieldDescriptorImpl( fieldName, typeInfo, handler, fieldMap.getTransient() );

        fieldDesc.setRequired(fieldMap.getRequired());

        return fieldDesc;
    }


    protected TypeInfo getTypeInfo( Class fieldType, CollectionHandler colHandler, FieldMapping fieldMap )
        throws MappingException
    {
        return new TypeInfo( Types.typeFromPrimitive( fieldType ), null, null,
                             fieldMap.getRequired(), null, colHandler );
    }


    /**
     * Returns the named field. Uses reflection to return the named
     * field and check the field type, if specified.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldName The name of the field
     * @param fieldType The type of the field if known, or null
     * @return The field, null if not found
     * @throws MappingException The field is not accessible or is not of the
     *  specified type
     */
    private Field findField( Class javaClass, String fieldName, Class fieldType )
        throws MappingException
    {
        Field field;

        try {
            // Look up the field based on its name, make sure it's only modifier
            // is public. If a type was specified, match the field type.
            field = javaClass.getField( fieldName );
            if ( field.getModifiers() != Modifier.PUBLIC &&
                 field.getModifiers() != ( Modifier.PUBLIC | Modifier.VOLATILE ) )
                throw new MappingException( "mapping.fieldNotAccessible", fieldName, javaClass.getName() );
            if ( fieldType == null )
                fieldType = Types.typeFromPrimitive( field.getType() );
            else if ( fieldType != java.io.Serializable.class
                    && Types.typeFromPrimitive( fieldType ) != Types.typeFromPrimitive( field.getType() ) )
                throw new MappingException( "mapping.fieldTypeMismatch", field, fieldType.getName() );
            return field;
        } catch ( NoSuchFieldException except ) {
        } catch ( SecurityException except ) {
        }
        return null;
    }


    /**
     * Returns the named accessor. Uses reflection to return the named
     * accessor and check the return value or parameter type, if
     * specified.
     *
     * @param javaClass The class to which the field belongs
     * @param methodName The name of the accessor method
     * @param fieldType The type of the field if known, or null
     * @param isGetMethod True if get method, false if set method
     * @return The method, null if not found
     * @throws MappingException The method is not accessible or is not of the
     *  specified type
     */
    private static Method findAccessor( Class javaClass, String methodName,
                                        Class fieldType, boolean getMethod )
        throws MappingException
    {
        Method   method;
        Method[] methods;
        Class[] parameterTypes;
        Class fieldTypeFromPrimitive;
        int      i;

        try {
            if ( getMethod ) {
                // Get method: look for the named method or prepend get to the
                // method name. Look up the field and potentially check the
                // return type.
                method = javaClass.getMethod( methodName, new Class[ 0 ] );

                if ( fieldType == null ) {
                    fieldType = Types.typeFromPrimitive( method.getReturnType() );
                }
                else {
                    fieldType = Types.typeFromPrimitive(fieldType);
                    Class returnType = Types.typeFromPrimitive( method.getReturnType());
                    
                    //-- First check against whether the declared type is
                    //-- an interface or abstract class. We also check
                    //-- type as Serializable for CMP 1.1 compatibility.
                    if (fieldType.isInterface() ||
                        ((fieldType.getModifiers() & Modifier.ABSTRACT) != 0) ||
                        (fieldType == java.io.Serializable.class))
                    {
                        if ( ! fieldType.isAssignableFrom( returnType ) )
                        throw new MappingException("mapping.accessorReturnTypeMismatch",
                                                    method, fieldType.getName() );
                    }
                    else {
                        if ( ! returnType.isAssignableFrom( fieldType ) )
                        throw new MappingException("mapping.accessorReturnTypeMismatch",
                                                    method, fieldType.getName() );
                    }
                }                    
            } 
            else {
                method = null;
                fieldTypeFromPrimitive = null;
                // Set method: look for the named method or prepend set to the
                // method name. If the field type is know, look up a suitable
                // method. If the fielf type is unknown, lookup the first
                // method with that name and one parameter.
                if ( fieldType != null ) {
                    fieldTypeFromPrimitive = Types.typeFromPrimitive( fieldType );
                    try {
                        method = javaClass.getMethod( methodName, new Class[] { fieldType } );
                    } catch ( Exception except ) {
                        try {
                            method = javaClass.getMethod( methodName, new Class[] { fieldTypeFromPrimitive } );
                        } catch ( Exception except2 ) {
                        }
                    }
                }
                if ( null == method ) {
                    methods = javaClass.getMethods();
                    method = null;
                    for ( i = 0 ; i < methods.length ; ++i ) {
                        if ( methods[ i ].getName().equals( methodName ) ) {
                            parameterTypes = methods[ i ].getParameterTypes();
                            if (( parameterTypes.length == 1 ) &&
                                (( fieldType == null ) ||
                                 Types.typeFromPrimitive( parameterTypes[0] ).isAssignableFrom( fieldTypeFromPrimitive ) )) {
                                method = methods[ i ];
                                break;
                            }
                        }
                    }
                    if ( method == null )
                        return null;
                }
            }
            // Make sure method is not abstract/static.
            // (note: Class.getMethod() returns only public methods).
            if ( ( method.getModifiers() & Modifier.ABSTRACT ) != 0 ||
                 ( method.getModifiers() & Modifier.STATIC ) != 0 )
                throw new MappingException( "mapping.accessorNotAccessible",
                                            methodName, javaClass.getName() );
            return method;
        } catch ( MappingException except ) {
            throw except;
        } catch ( Exception except ) {
        }
        return null;
    }


    /**
     * Loads a class descriptor from a compiled class.
     *
     * @param clsName The class for which the descriptor is loaded
     * @return An instance of the class descriptor or null if not found
     */
    protected ClassDescriptor loadClassDescriptor( String clsName )
    {
        /** Temporarily disabled
        clsName = clsName + CompiledSuffix;
        try {
            Object obj;

            obj = resolveType( clsName ).newInstance();
            if ( obj instanceof ClassDescriptor )
                return (ClassDescriptor) obj;
            return null;
        } catch ( Exception except ) {
            return null;
        }
        */
        return null;
    }


    private String capitalize( String name )
    {
        char first;

        first = name.charAt( 0 );
        if ( Character.isUpperCase( first  ) )
            return name;
        return Character.toUpperCase( first ) + name.substring( 1 );
    }


}
