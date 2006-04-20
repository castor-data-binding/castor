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


import java.lang.reflect.Modifier;
import java.util.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.Serializable;
import java.math.BigDecimal;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.util.Messages;
import org.exolab.castor.util.MimeBase64Encoder;
import org.exolab.castor.util.MimeBase64Decoder;


/**
 * Type information. Can be used to map between short type names (such
 * as 'int') and actual Java types (java.lang.Integer), to determine
 * whether a type is simple (i.e. maps to a single XML attribute, SQL
 * column, etc), as well as to create a new instance of a type.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class Types
{


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
        if ( loader != null )
            return loader.loadClass( typeName );
        else
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
     * Returns a type convertor. A type convertor can be used to convert
     * an object from Java type <tt>fromType</tt> to Java type <tt>toType</tt>.
     *
     * @param fromType The Java type to convert from
     * @param toType The Java type to convert to
     * @throws MappingException No suitable convertor was found
     */
    public static TypeConvertor getConvertor( Class fromType, Class toType )
        throws MappingException
    {
        // first seek for exact match
        // TODO: the closest possible match
        for ( int i = 0 ; i < _typeConvertors.length ; ++i ) {
            if ( _typeConvertors[ i ].fromType.equals( fromType ) &&
                 toType.equals( _typeConvertors[ i ].toType ) )
                return _typeConvertors[ i ].convertor;
        }

        // else seek for any match
        for ( int i = 0 ; i < _typeConvertors.length ; ++i ) {
            if ( _typeConvertors[ i ].fromType.isAssignableFrom( fromType ) &&
                 toType.isAssignableFrom( _typeConvertors[ i ].toType ) )
                return _typeConvertors[ i ].convertor;
        }
        throw new MappingException( "mapping.noConvertor", fromType.getName(), toType.getName() );
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
     * Constructs a new object from the given class. Does not generate any
     * checked exceptions, since object creation has been proven to work
     * when creating descriptor from mapping.
     *
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
     * Returns true if the objects of this class are constructable.
     * The class must be publicly available and have a default public
     * constructor.
     *
     * @param type The Java type
     * @return True if constructable
     */
    public static boolean isConstructable( Class type )
    {
        try {
            if ( ( type.getModifiers() & Modifier.PUBLIC ) == 0 )
                return false;
            if ( ( type.getModifiers() & ( Modifier.ABSTRACT | Modifier.INTERFACE ) ) != 0 )
                return false;
            if ( ( type.getConstructor( new Class[0] ).getModifiers() & Modifier.PUBLIC ) != 0 )
                return true;
        } catch ( NoSuchMethodException except ) {
        } catch ( SecurityException except ) {
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
     * Transforms short date format pattern into full format pattern
     * for SimpleDateFormat (e.g., "YMD" to "yyyyMMdd").
     *
     * @param pattern The short pattern
     * @return The full pattern
     */
    public static String getFullDatePattern( String pattern )
    {
        StringBuffer sb;
        int len;

        if ( pattern == null || pattern.length() == 0 )
            return "yyyyMMdd";

        sb = new StringBuffer();
        len = pattern.length();

        for ( int i = 0; i < len; i++ ) {
            switch ( pattern.charAt( i ) ) {
            case 'y': case 'Y': sb.append( "yyyy" ); break;
            case 'M':           sb.append( "MM" ); break;
            case 'd': case 'D': sb.append( "dd" ); break;
            case 'h': case 'H': sb.append( "HH" ); break;
            case 'm':           sb.append( "mm" ); break;
            case 's':           sb.append( "ss" ); break;
            case 'S':           sb.append( "SSS" ); break;
            }
        }

        return sb.toString();
    }


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
        new TypeInfo( "long",        java.lang.Long.TYPE,
                      java.lang.Long.class,       true,     new Long( 0 ) ),
        new TypeInfo( "boolean",     java.lang.Boolean.TYPE,
                      java.lang.Boolean.class,    true,     Boolean.FALSE ),
        new TypeInfo( "double",      java.lang.Double.TYPE,
                      java.lang.Double.class,     true,     new Double( 0 ) ),
        new TypeInfo( "float",       java.lang.Float.TYPE,
                      java.lang.Float.class,      true,     new Float( 0 ) ),
        new TypeInfo( "big-decimal", null,
                      java.math.BigDecimal.class, true,     new BigDecimal( 0 ) ),
        new TypeInfo( "byte",        java.lang.Byte.TYPE,
                      java.lang.Byte.class,       true,     new Byte( (byte) 0 ) ),
        new TypeInfo( "date",        null,
                      java.util.Date.class,       true,     null ),
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
          new TypeInfo( Stream,     "stream",      java.io.InputStream.class,  null ),
          new TypeInfo( Reader,     "reader",      java.io.Reader.class,       null ),
          new TypeInfo( XML,        "xml",         org.w3c.dom.Document.class, org.w3c.dom.Element.class ),
          new TypeInfo( Serialized, "ser",         java.io.Serializable.class, null )
        */
    };


    /**
     * Information used to locate a type convertor.
     */
    static class TypeConvertorInfo
    {

        /**
         *  The type being converted to.
         */
        final Class toType;

        /**
         * The type being converted from.
         */
        final Class fromType;

        /**
         * The convertor.
         */
        final TypeConvertor convertor;

        TypeConvertorInfo( Class fromType, Class toType, TypeConvertor convertor )
        {
            this.fromType  = fromType;
            this.toType    = toType;
            this.convertor = convertor;
        }

    }


    /**
     * Date format used by the date convertor.
     */
    private static DateFormat _dateFormat = new SimpleDateFormat();


    /**
     * Date format used by the date convertor when nonempy parameter
     * is specified.
     */
    private static SimpleDateFormat _paramDateFormat = new SimpleDateFormat();

    /**
     * Date format used by the double->date convertor.
     */
    private static DecimalFormat _decimalFormat = new DecimalFormat("#################0");




    /**
     * List of all the default convertors between Java types.
     */
    static TypeConvertorInfo[] _typeConvertors = new TypeConvertorInfo[] {
        // Convertors to boolean
        new TypeConvertorInfo( java.lang.Short.class, java.lang.Boolean.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Boolean( ( (Short) obj ).shortValue() != 0 );
            }
            public String toString() { return "Short->Boolean"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Boolean.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Boolean( ( (Integer) obj ).intValue() != 0 );
            }
            public String toString() { return "Integer->Boolean"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Boolean.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                switch ( ( (String) obj ).length() ) {
                    case 0: return Boolean.FALSE;
                    case 1: char ch = ( (String) obj ).charAt( 0 );
                        if (param == null || param.length() != 2 )
                            return ( ch == 'T' || ch == 't'  ) ? Boolean.TRUE : Boolean.FALSE;
                        else
                            return ( ch == param.charAt( 1 ) ) ? Boolean.TRUE : Boolean.FALSE;
                    case 4: return ( (String) obj ).equalsIgnoreCase( "true" ) ? Boolean.TRUE : Boolean.FALSE;
                    case 5: return ( (String) obj ).equalsIgnoreCase( "false" ) ? Boolean.TRUE : Boolean.FALSE;
                }
                return Boolean.FALSE;
            }
            public String toString() { return "String->Boolean"; }
        } ),
        new TypeConvertorInfo( java.math.BigDecimal.class, java.lang.Boolean.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Boolean( ( (java.math.BigDecimal) obj).intValue() != 0 );
            }
            public String toString() { return "BigDecimal->Boolean"; }
        } ),
        // Convertors to integer
        new TypeConvertorInfo( java.lang.Byte.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (Byte) obj ).intValue() );
            }
            public String toString() { return "Byte->Integer"; }
        } ),
        new TypeConvertorInfo( java.lang.Short.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (Short) obj ).intValue() );
            }
            public String toString() { return "Short->Integer"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (Long) obj ).intValue() );
            }
            public String toString() { return "Long->Integer"; }
        } ),
        new TypeConvertorInfo( java.lang.Float.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (Float) obj ).intValue() );
            }
            public String toString() { return "Float->Integer"; }
        } ),
        new TypeConvertorInfo( java.lang.Double.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (Double) obj ).intValue() );
            }
            public String toString() { return "Double->Integer"; }
        } ),
        new TypeConvertorInfo( java.math.BigDecimal.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (BigDecimal) obj ).intValue() );
            }
            public String toString() { return "BigDecimal->Integer"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return Integer.valueOf( (String) obj );
            }
            public String toString() { return "String->Integer"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                _paramDateFormat.applyPattern( Types.getFullDatePattern( param ) );
                return new Integer( _paramDateFormat.format( (Date) obj ) );
            }
            public String toString() { return "Date->Integer"; }
        } ),
        // Convertors to long
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Long( ( (Integer) obj ).longValue() );
            }
            public String toString() { return "Integer->Long"; }
        } ),
        new TypeConvertorInfo( java.lang.Short.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Long( ( (Short) obj ).longValue() );
            }
            public String toString() { return "Short->Long"; }
        } ),
        new TypeConvertorInfo( java.lang.Float.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Long( ( (Float) obj ).longValue() );
            }
            public String toString() { return "Float->Long"; }
        } ),
        new TypeConvertorInfo( java.lang.Double.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Long( ( (Double) obj ).longValue() );
            }
            public String toString() { return "Double->Long"; }
        } ),
        new TypeConvertorInfo( java.math.BigDecimal.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Long( ( (BigDecimal) obj ).longValue() );
            }
            public String toString() { return "BigDecimal->Long"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return Long.valueOf( (String) obj );
            }
            public String toString() { return "String->Long"; }
        } ),
        // Convertors to short
        new TypeConvertorInfo( java.lang.Byte.class, java.lang.Short.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Short( ( (Byte) obj ).shortValue() );
            }
            public String toString() { return "Byte->Short"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Short.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Short( ( (Integer) obj ).shortValue() );
            }
            public String toString() { return "Integer->Short"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.lang.Short.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Short( ( (Long) obj ).shortValue() );
            }
            public String toString() { return "Long->Short"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Short.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return Short.valueOf( (String) obj );
            }
            public String toString() { return "String->Short"; }
        } ),
        // Convertors to byte
        new TypeConvertorInfo( java.lang.Short.class, java.lang.Byte.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Byte( ( (Short) obj ).byteValue() );
            }
            public String toString() { return "Short->Byte"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Byte.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Byte( ( (Integer) obj ).byteValue() );
            }
            public String toString() { return "Integer->Byte"; }
        } ),
        // Convertors to double
        new TypeConvertorInfo( java.lang.Float.class, java.lang.Double.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Double( ( (Float) obj ).floatValue() );
            }
            public String toString() { return "Float->Double"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Double.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Double( (double) ( (Integer) obj ).intValue() );
            }
            public String toString() { return "Integer->Double"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.lang.Double.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Double( (double) ( (Long) obj ).longValue() );
            }
            public String toString() { return "Long->Double"; }
        } ),
        new TypeConvertorInfo( java.math.BigDecimal.class, java.lang.Double.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Double( ( (BigDecimal) obj ).doubleValue() );
            }
            public String toString() { return "BigDecimal->Double"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.lang.Double.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                _paramDateFormat.applyPattern( Types.getFullDatePattern( param ) );
                return new Double( _paramDateFormat.format( (Date) obj ) );
            }
            public String toString() { return "Date->Double"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Double.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return Double.valueOf( (String) obj );
            }
            public String toString() { return "Double->String"; }
        } ),
        // Convertors to float
        new TypeConvertorInfo( java.lang.Double.class, java.lang.Float.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Float( ( (Double) obj ).floatValue() );
            }
            public String toString() { return "Double->Float"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Float.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Float( (float) ( (Integer) obj ).intValue() );
            }
            public String toString() { return "Integer->Float"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.lang.Float.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Float( (float) ( (Long) obj ).longValue() );
            }
            public String toString() { return "Long->Float"; }
        } ),
        new TypeConvertorInfo( java.math.BigDecimal.class, java.lang.Float.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Float( ( (BigDecimal) obj ).floatValue() );
            }
            public String toString() { return "BigDecimal->Float"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Float.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return Float.valueOf( (String) obj );
            }
            public String toString() { return "String->Float"; }
        } ),
        // Convertors to big decimal
        new TypeConvertorInfo( java.lang.Double.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                // Don't remove "toString" below! Otherwise the result is incorrect.
                return new BigDecimal( ( (Double) obj ).toString() );
            }
            public String toString() { return "Double->BigDecimal"; }
        } ),
        new TypeConvertorInfo( java.lang.Float.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                // Don't remove "toString" below! Otherwise the result is incorrect.
                return new BigDecimal( ( (Float) obj ).toString() );
            }
            public String toString() { return "Float->BigDecimal"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return BigDecimal.valueOf( ( (Integer) obj ).intValue() );
            }
            public String toString() { return "Integer->BigDecimal"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return BigDecimal.valueOf( ( (Long) obj ).longValue() );
            }
            public String toString() { return "Long->BigDecimal"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new BigDecimal( (String) obj );
            }
            public String toString() { return "String->BigDecimal"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                _paramDateFormat.applyPattern( Types.getFullDatePattern( param ) );
                return new BigDecimal( _paramDateFormat.format( (Date) obj ) + ".0" );
            }
            public String toString() { return "Date->BigDecimal"; }
        } ),
        new TypeConvertorInfo( java.lang.Boolean.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return BigDecimal.valueOf( ( (Boolean) obj).booleanValue() ? 1 : 0 );
            }
            public String toString() { return "Boolean->BigDecimal"; }
        } ),
        // Convertors to string
        new TypeConvertorInfo( java.lang.Short.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Short->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Integer->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Long->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Float.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Float->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Double.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Double->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Object.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Object->String"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                if ( param == null || param.length() == 0 )
                    return obj.toString();
                else {
                    _paramDateFormat.applyPattern( param );
                    return _paramDateFormat.format( (Date) obj );
                }
            }
            public String toString() { return "Date->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Character.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new String( obj.toString() );
            }
            public String toString() { return "Character->String"; }
        } ),
        new TypeConvertorInfo( char[].class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new String( (char[]) obj );
            }
            public String toString() { return "chars->String"; }
        } ),
        new TypeConvertorInfo( byte[].class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                MimeBase64Encoder encoder;

                encoder = new MimeBase64Encoder();
                encoder.translate( (byte[]) obj );
                return new String( encoder.getCharArray() );
            }
            public String toString() { return "bytes->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Boolean.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                if ( param == null || param.length() != 2 )
                    return ( (Boolean) obj ).booleanValue() ? "T" : "F";
                else
                    return ( (Boolean) obj ).booleanValue() ? param.substring( 1, 2 ) : param.substring( 0, 1 );
            }
            public String toString() { return "Boolean->String"; }
        } ),
        // Convertors to character/byte array
        new TypeConvertorInfo( java.lang.String.class, java.lang.Character.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return ( new Character( ( (String ) obj ).charAt( 0 ) ) );
            }
            public String toString() { return "String->Character"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, char[].class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return ( (String ) obj ).toCharArray();
            }
            public String toString() { return "String->chars"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, byte[].class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                MimeBase64Decoder decoder;

                decoder = new MimeBase64Decoder();
                decoder.translate( (String ) obj );
                return decoder.getByteArray();
            }
            public String toString() { return "String->bytes"; }
        } ),
        // Convertors to date
        new TypeConvertorInfo( java.lang.String.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                try {
                    if ( param == null || param.length() == 0 )
                        return _dateFormat.parse( (String) obj );
                    else {
                        _paramDateFormat.applyPattern( param );
                        return _paramDateFormat.parse( (String) obj );
                    }
                } catch ( ParseException except ) {
                    throw new IllegalArgumentException( except.toString() );
                }
            }
            public String toString() { return "String->Date"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                try {
                    _paramDateFormat.applyPattern( Types.getFullDatePattern( param ) );
                    return _paramDateFormat.parse( obj.toString() );
                } catch ( ParseException except ) {
                    throw new IllegalArgumentException( except.toString() );
                }
            }
            public String toString() { return "Integer->Date"; }
        } ),
        new TypeConvertorInfo( java.math.BigDecimal.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                try {
                    _paramDateFormat.applyPattern( Types.getFullDatePattern( param ) );
                    return _paramDateFormat.parse( obj.toString() );
                } catch ( ParseException except ) {
                    throw new IllegalArgumentException( except.toString() );
                }
            }
            public String toString() { return "BigDecimal->Date"; }
        } ),
        new TypeConvertorInfo( java.lang.Double.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                try {
                    _paramDateFormat.applyPattern( Types.getFullDatePattern( param ) );
                    return _paramDateFormat.parse( _decimalFormat.format(obj).trim() );
                } catch ( ParseException except ) {
                    throw new IllegalArgumentException( except.toString() );
                }
            }
            public String toString() { return "Double->Date"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.sql.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new java.sql.Date( ( (java.util.Date) obj ).getTime() );
            }
            public String toString() { return "util.Date->sql.Date"; }
        } ),
        new TypeConvertorInfo( java.sql.Date.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj;
            }
            public String toString() { return "sql.Date->util.Date"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.sql.Time.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new java.sql.Time( ( (java.util.Date) obj ).getTime() );
            }
            public String toString() { return "util.Date->sql.Time"; }
        } ),
        new TypeConvertorInfo( java.sql.Time.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj;
            }
            public String toString() { return "sql.Time->util.Date"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.sql.Timestamp.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new java.sql.Timestamp( ( (java.util.Date) obj ).getTime() );
            }
            public String toString() { return "util.Date->sql.Timestamp"; }
        } ),

        new TypeConvertorInfo( java.sql.Timestamp.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj;
            }
            public String toString() { return "sql.Timestamp->util.Date"; }
        } ),

        new TypeConvertorInfo( java.util.Date.class, org.exolab.castor.types.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new org.exolab.castor.types.Date((java.util.Date) obj);
            }
            public String toString() { return "util.Date->castor.types.Date"; }
        } ),

        new TypeConvertorInfo( org.exolab.castor.types.Date.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                Object result = null;
                try {
                    result = ((org.exolab.castor.types.Date)obj).toDate();
                } catch (java.text.ParseException e) {
                    //we can never reach that point
                }
                return result;
            }
            public String toString() { return "castor.types.Date->util.Date"; }
        } ),

        new TypeConvertorInfo( java.sql.Date.class, org.exolab.castor.types.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new org.exolab.castor.types.Date((java.util.Date) obj);
            }
            public String toString() { return "sql.Date->castor.types.Date"; }
        } ),

        new TypeConvertorInfo( org.exolab.castor.types.Date.class, java.sql.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                Object result = null;
                try {
                    result = new java.sql.Date( ((org.exolab.castor.types.Date)obj).toDate().getTime() );
                } catch (java.text.ParseException e) {
                    //we can never reach that point
                }
                return result;
            }
            public String toString() { return "castor.types.Date->sql.Date"; }
        } )

    };


}


