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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.mapping.loader;


import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Vector;

import org.castor.core.util.Messages;
import org.exolab.castor.types.Duration;


/**
 * Type information. Can be used to map between short type names (such
 * as 'int') and actual Java types (java.lang.Integer), to determine
 * whether a type is simple (i.e. maps to a single XML attribute, SQL
 * column, etc), as well as to create a new instance of a type.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class Types {
    /**
     * Returns the class name based on the supplied type name. The type name
     * can be a short name (e.g. int, byte) or any other Java class (e.g.
     * myapp.Product). If a short type name is used, the primitive type might
     * be returned. If a Java class name is used, the class will be loaded and
     * returned through the supplied class loader.
     *
     * @param loader The class loader to use, may be null
     * @param typeName The type name
     * @return The type class
     * @throws ClassNotFoundException The specified class could not be found
     */
    public static Class typeFromName( ClassLoader loader, String typeName )
        throws ClassNotFoundException
    {
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
            if ( typeName.equals( _typeInfos[ i ].shortName ) )
                return ( _typeInfos[ i ].primitive != null ? _typeInfos[ i ].primitive :
                         _typeInfos[ i ].javaType );
        }
        if ( loader != null ) {
            Class aClass = Class.forName(typeName, false, loader);
            // _log.debug ("Loaded class " + aClass);
            return aClass;
        }
        return Class.forName( typeName );
    }


    /**
     * Returns the default value for this Java type (e.g. 0 for integer, empty
     * string) or null if no default value is known. The default value only
     * applies to primitive types (that is, <tt>Integer.TYPE</tt> but not
     * <tt>java.lang.Integer</tt>).
     *
     * @param type The Java type
     * @return The default value or null
     */
    public static Object getDefault( Class type )
    {
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
            if ( _typeInfos[ i ].primitive == type ||
                 _typeInfos[ i ].javaType == type )
                return _typeInfos[ i ].defValue;
        }
        return null;
    }


    /**
     * Maps from a primitive Java type to a Java class. Returns the same class
     * if <tt>type</tt> is not a primitive. The following conversion applies:
     * <pre>
     * From            To
     * --------------  ---------------
     * Boolean.TYPE    Boolean.class
     * Byte.TYPE       Byte.class
     * Character.TYPE  Character.class
     * Short.TYPE      Short.class
     * Integer.TYPE    Integer.class
     * Long.TYPE       Long.class
     * Float.TYPE      Float.class
     * Double.TYPE     Double.class
     * </pre>
     *
     * @param type The Java type (primitive or not)
     * @return A comparable non-primitive Java type
     */
    public static Class typeFromPrimitive( Class type )
    {
        /// Fix for arrays
        if ((type != null) && (type.isArray())
                           && !type.getComponentType().isPrimitive()) {
            return typeFromPrimitive( type.getComponentType() );
        }
        /// end fix
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
            if ( _typeInfos[ i ].primitive == type )
                return _typeInfos[ i ].javaType;
        }
        return type;
    }


    /**
     * Returns true if the Java type is represented as a simple type.
     * A simple can be described with a single XML attribute value,
     * a single SQL column, a single LDAP attribute value, etc.
     * The following types are considered simple:
     * <ul>
     * <li>All primitive types
     * <li>String
     * <li>Date
     * <li>java.sql.Date
     * <li>java.sql.Time
     * <li>Timestamp
     * <li>byte/char arrays
     * <li>BigDecimal
     * </ul>
     *
     * @param type The Java type
     * @return True if a simple type
     */
    public static boolean isSimpleType( Class type )
    {
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
            if ( _typeInfos[ i ].javaType == type || _typeInfos[ i ].primitive == type )
                return true;
        }
        return false;
    }


    /**
     * Returns true if the Java type is represented as a primitive type. Wrapper
     * like java.lang.Integer are considered as primitive.
     *
     * @param type The Java type
     * @return True if a primitive type
     */
    public static boolean isPrimitiveType( Class type )
    {
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
            if (_typeInfos[ i ].primitive == type || 
                (_typeInfos[ i ].javaType == type && _typeInfos[ i ].primitive != null))
                return true;
        }
        return false;
    }


    private static final Vector _enums = new Vector();
    
    public static void addEnumType(Class type) {
        _enums.add(type);
    }
    
    public static boolean isEnumType(Class type) {
        return _enums.contains(type);
    }

    
    private static final Vector _convertible = new Vector();
    
    public static void addConvertibleType(Class type) {
        _convertible.add(type);
    }
    
    public static boolean isConvertibleType(Class type) {
        return _convertible.contains(type);
    }

    
    /**
     * Constructs a new object from the given class. Does not generate any
     * checked exceptions, since object creation has been proven to work
     * when creating descriptor from mapping.
     *
     * @param type The class type of the object instance to be constructed.
     * @return An instance of the class type specified.
     * @throws IllegalStateException The Java object cannot be constructed
     */
    public static Object newInstance( Class type )
        throws IllegalStateException
    {
        try {
            return type.newInstance();
        } catch ( IllegalAccessException except ) {
            // This should never happen unless  bytecode changed all of a sudden
            throw new IllegalStateException( Messages.format( "mapping.schemaNotConstructable",
                                                              type.getName(), except.getMessage() ) );
        } catch ( InstantiationException except ) {
            // This should never happen unless  bytecode changed all of a sudden
            throw new IllegalStateException( Messages.format( "mapping.schemaNotConstructable",
                                                              type.getName(), except.getMessage() ) );
        }
    }

    /**
     * Constructs a new object from the given class. Does not generate any
     * checked exceptions, since object creation has been proven to work
     * when creating descriptor from mapping.
     *
     * @param type The class type of the object instance to be constructed.
     * @param args Arguments to be supplied to constructor call. 
     * @return An instance of the class type specified.
     * @throws IllegalStateException The Java object cannot be constructed
     */
    public static Object newInstance( Class type, Object args[] )
        throws IllegalStateException
    {
        
        if ((args == null) || (args.length == 0))
            return newInstance( type );
            
        try {
            Constructor cons = findConstructor(type, args);
            return cons.newInstance(args);
        }
        catch (NoSuchMethodException except) {
            throw new IllegalStateException( Messages.format( "mapping.constructorNotFound",
                                                              type.getName(), except.getMessage() ) );
        }
        catch ( java.lang.reflect.InvocationTargetException except) {
            // This should never happen unless  bytecode changed all of a sudden
            throw new IllegalStateException( Messages.format( "mapping.schemaNotConstructable",
                                                              type.getName(), except.getMessage() ) );
        }
        catch ( IllegalAccessException except ) {
            // This should never happen unless  bytecode changed all of a sudden
            throw new IllegalStateException( Messages.format( "mapping.schemaNotConstructable",
                                                              type.getName(), except.getMessage() ) );
        } catch ( InstantiationException except ) {
            // This should never happen unless  bytecode changed all of a sudden
            throw new IllegalStateException( Messages.format( "mapping.schemaNotConstructable",
                                                              type.getName(), except.getMessage() ) );
        }
    }


    /**
     * Returns true if the objects of this class are constructable.
     * The class must be publicly available and have a default public
     * constructor.
     *
     * @param type The Java type
     * @return True if constructable
     */
    public static boolean isConstructable( Class type )
    {
        return isConstructable( type, false );
    }

    /**
     * Returns true if the objects of this class are constructable.
     * The class must be publicly available and have a default public
     * constructor.
     *
     * @param allowAbstractOrInterface True to indicate that abstract classes of interfaces are allowed.
     * @param type The Java type
     * @return True if constructable
     */
    public static boolean isConstructable( Class type, boolean allowAbstractOrInterface )
    {
        try {
            if ( ( type.getModifiers() & Modifier.PUBLIC ) == 0 )
                return false;
            if ((! allowAbstractOrInterface) && 
                (( type.getModifiers() & ( Modifier.ABSTRACT | Modifier.INTERFACE ) ) != 0 ))
                return false;
            if ( ( type.getConstructor( new Class[0] ).getModifiers() & Modifier.PUBLIC ) != 0 )
                return true;
        } catch ( NoSuchMethodException except ) {
        	// TODO [WG]: no code available
        } catch ( SecurityException except ) {
        	// TODO [WG]: no code available
        }
        return false;
    }


    /**
     * Returns true if the Java type implements the {@link Serializable}
     * interface.
     *
     * @param type The Java type
     * @return True if declared as serializable
     */
    public static boolean isSerializable( Class type )
    {
        return ( Serializable.class.isAssignableFrom( type ) );
    }


    /**
     * Returns true if the Java type is immutable. Immutable objects are
     * not copied.
     *
     * @param type The Java type
     * @return True if immutable type
     */
    public static boolean isImmutable( Class type )
    {
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
            if ( _typeInfos[ i ].javaType == type || _typeInfos[ i ].primitive == type )
                return _typeInfos[ i ].immutable;
        }
        return false;
    }


    /**
     * Returns true if the Java type implements the {@link Cloneable}
     * interface.
     *
     * @param type The Java type
     * @return True if declared as cloneable
     */
    public static boolean isCloneable( Class type )
    {
        return ( Cloneable.class.isAssignableFrom( type ) );
    }



    /**
     * Returns the matching constructor for the given arguments
     *
     * @param type The class type of the object instance to be constructed.
     * @param args Arguments to be supplied to constructor call. 
     * @return the matching constructor for the given arguments
     * @throws NoSuchMethodException If no constructor with the given number/types of arguments exists.
     */
    private static Constructor findConstructor(Class type, Object[] args) 
        throws NoSuchMethodException
    {
        
        Constructor[] constructors = type.getConstructors();
        Constructor cons = null;
        int rank = 0;
        
        for (int c = 0; c < constructors.length; c++) {
            Class[] paramTypes = constructors[c].getParameterTypes();
            if (paramTypes.length != args.length) continue;
            
            int tmpRank = 0;
            boolean matches = true;
            for (int p = 0; p < paramTypes.length; p++) {
                if (args[p] == null) {
                    if (paramTypes[p].isPrimitive()) {
                        matches = false;
                        break;
                    }
                    // null matches any non primitive object
                    continue;
                }
                
                //-- check direct param match
                if (paramTypes[p] == args[p].getClass()) {
                    ++tmpRank;
                    continue;
                }
                
                //-- check for inheritence
                if (paramTypes[p].isAssignableFrom(args[p].getClass())) {
                    //-- good keep going, but don't increment rank
                    continue;
                }
                //-- check for primitive match
                if (paramTypes[p].isPrimitive()) {
                    Class pType = typeFromPrimitive(paramTypes[p]);
                    if (pType.isAssignableFrom(args[p].getClass())) {
                        //-- good keep going, but don't increment rank
                        continue;
                    }
                }
                matches = false;
                break;
            }
            
            if (matches) {                
                if (tmpRank == paramTypes.length)
                    return constructors[c];                 
                if ((cons == null) || (tmpRank > rank)) {
                    cons = constructors[c];
                    rank = tmpRank;
                }
            }
        }
        
        if (cons == null)
            throw new NoSuchMethodException();
            
        return cons;
    } //-- findConstructor

    /**
     * Information about a specific Java type.
     */
    static class TypeInfo
    {

        /**
         * The short type name (e.g. <tt>integer</tt>).
         */
        final String  shortName;
        
        /**
         * The primitive Java type, if exists (e.g. <tt>Integer.TYPE</tt>).
         */
        final Class   primitive;

        /**
         * The Java type (e.g. <tt>java.lang.Integer</tt>).
         */        
        final Class   javaType;
        
        /**
         * True if the type is immutable.
         */
        final boolean immutable;
        
        /**
         * The default value for the type, if known.
         */
        final Object  defValue;
        
        TypeInfo( String shortName, Class primitive, Class javaType,
                  boolean immutable, Object defValue )
        {
            this.shortName  = shortName;
            this.primitive  = primitive;
            this.javaType   = javaType;
            this.immutable  = immutable;
            this.defValue   = defValue;
        }

    }


    /**
     * List of all the simple types supported by Castor.
     */
    static TypeInfo[] _typeInfos = new TypeInfo[] {
        //            shortName      primitive
        //            javaType                    immutable defValue
        new TypeInfo( "other",       null,
                      java.lang.Object.class,     false,    null ),
        new TypeInfo( "string",      null,
                      java.lang.String.class,     true,     null ),
        new TypeInfo( "integer",     java.lang.Integer.TYPE,
                      java.lang.Integer.class,    true,     new Integer( 0 ) ),
        new TypeInfo( "int",         java.lang.Integer.TYPE,
                      java.lang.Integer.TYPE,    true,     new Integer( 0 ) ),
        new TypeInfo( "long",        java.lang.Long.TYPE,
                      java.lang.Long.class,       true,     new Long( 0L ) ),
        new TypeInfo( "big-integer", null,
                      java.math.BigInteger.class, true,     BigInteger.valueOf( 0 ) ),
        new TypeInfo( "boolean",     java.lang.Boolean.TYPE,
                      java.lang.Boolean.class,    true,     Boolean.FALSE ),
        new TypeInfo( "double",      java.lang.Double.TYPE,
                      java.lang.Double.class,     true,     new Double( 0.0 ) ),
        new TypeInfo( "float",       java.lang.Float.TYPE,
                      java.lang.Float.class,      true,     new Float( 0.0f ) ),
        new TypeInfo( "big-decimal", null,
                      java.math.BigDecimal.class, true,     new BigDecimal( 0.0 ) ),
        new TypeInfo( "byte",        java.lang.Byte.TYPE,
                      java.lang.Byte.class,       true,     new Byte( (byte) 0 ) ),
        new TypeInfo( "date",        null,
                      java.util.Date.class,       true,     null ),
        new TypeInfo( "timestamp",        null,
                      java.sql.Timestamp.class,       true,     null ),
        new TypeInfo( "sqldate",        null,
                      java.sql.Date.class,       true,     null ),
        new TypeInfo( "sqltime",        null,
                      java.sql.Time.class,       true,     null ),
        new TypeInfo( "short",       java.lang.Short.TYPE,
                      java.lang.Short.class,      true,     new Short( (short) 0 ) ),
        new TypeInfo( "char",        java.lang.Character.TYPE,
                      java.lang.Character.class,  true,     new Character( (char) 0 ) ),
        new TypeInfo( "bytes",       null,
                      byte[].class,               false,    null ),
        new TypeInfo( "chars",       null,
                      char[].class,               false,    null ),
        new TypeInfo( "strings",     null,
                      String[].class,             false,    null ),
        new TypeInfo( "locale",      null,
                      java.util.Locale.class,     true,     null ),
        new TypeInfo( "stream",      null,
                      java.io.InputStream.class,  true,     null ),
        new TypeInfo( "clob",      null,
                      getClobClass(),        true,     null ),
        new TypeInfo( "serializable", null,
                      java.io.Serializable.class, false,   null ), 

        /* Mapping for the java array of primitive type so they use the same
         * naming encoding as array of object.
         */
        new TypeInfo( "[Lbyte;",      null,
                      byte[].class,    false,     null ),
        new TypeInfo( "[Lchar;",      null,
                      char[].class,    false,     null ),
        new TypeInfo( "[Ldouble;",    null,
                      double[].class,  false,     null ),
        new TypeInfo( "[Lfloat;",     null,
                      float[].class,   false,     null ),
        new TypeInfo( "[Lint;",       null,
                      int[].class,     false,     null ),
        new TypeInfo( "[Llong;",      null,
                      long[].class,    false,     null ),
        new TypeInfo( "[Lshort;",     null,
                      int[].class,     false,     null ),
        new TypeInfo( "[Lboolean;",   null,
                      int[].class,     false,     null ),

        /*
          new TypeInfo( XML,        "xml",         org.w3c.dom.Document.class, org.w3c.dom.Element.class ),
          new TypeInfo( Serialized, "ser",         java.io.Serializable.class, null )
        */
        new TypeInfo( "duration",   null,
                      org.exolab.castor.types.Duration.class,     false,     new Duration (0) ),
        new TypeInfo( "xml-date",   null,
                      org.exolab.castor.types.Date.class,     false,     new org.exolab.castor.types.Date (0) ),
        new TypeInfo( "xml-time",   null,
                      org.exolab.castor.types.Time.class,     false,     new org.exolab.castor.types.Time (0) )
                              
    };

    /**
     * A hack for JDK 1.1 Compatibility
     * @return A Class instance for CLOB types.
     */
    private static final Class getClobClass() {        
        Class type = null;
        try {
            type = Class.forName("java.sql.Clob");
        }
        catch(ClassNotFoundException cnfe) {
            /// If this is thrown we are probably in JDK 1.1, so
            /// that's ok, otherwise there is some nasty ClassLoader problem :-)
        }
        return type;
    } //-- getClobClass

}


