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
 * Copyright 1999-2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import java.io.FileReader;
import java.io.PrintWriter;
import java.io.InputStream;

import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;

import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.Messages;
import org.exolab.castor.mapping.Mapping;

import org.exolab.castor.xml.Unmarshaller;

import org.exolab.castor.xml.schema.simpletypes.*;
import org.exolab.castor.xml.schema.simpletypes.factory.*;

import org.xml.sax.InputSource;

/**
 * SimpleTypesFactory provides code constants for every built
 * in type defined in www.w3.org/TR/xmlschma-2-20000407
 * USER_TYPE is used for user derived types.
 *
 * This factory can also create instances of classes derived from SimpleType
 * that represent the simple types defined by xmlschema and those derived from them.
 *
 * @author <a href="mailto:berry@intalio.com">Arnaud Berry</a>
 * @version $Revision$ $Date$
**/
public class SimpleTypesFactory
{
    /**
     * Tells if a type code corresponds to an xml schema built in type
     */
    public static boolean isBuiltInType(int codeType)
    {
        return USER_TYPE < codeType;
    }

    /**
     * Tells if a type code corresponds to an xml schema (built in) primitive type
     */
    public static boolean isPrimitiveType(int codeType)
    {
        return (STRING_TYPE <= codeType)&&(codeType <= QNAME_TYPE);
    }

    /**
     * Gets an instance of a class derived from SimpleType representing the
     * built in type which name is given as a parameter.
     */
    public SimpleType getBuiltInType(String typeName)
    {
        Type type= getType(typeName);
        if (type == null) return null;
        return type.getSimpleType();
    }


    //Type Codes:

    /**
     * This code is for errors or uninitialized types.
    **/
    public static final int INVALID_TYPE                = -1;

    /**
     * Simple type defined by the user
    **/
    public static final int USER_TYPE                     =  0;

    //Primitive types
    public static final int STRING_TYPE                   =  1;
    public static final int BOOLEAN_TYPE                  =  2;
    public static final int FLOAT_TYPE                    =  3;
    public static final int DOUBLE_TYPE                   =  4;
    public static final int DECIMAL_TYPE                  =  5;
    public static final int TIME_DURATION_TYPE            =  6;
    public static final int RECURRING_DURATION_TYPE       =  7;
    public static final int BINARY_TYPE                   =  8;
    public static final int URIREFERENCE_TYPE             =  9;
    public static final int ID_TYPE                       = 10;
    public static final int IDREF_TYPE                    = 11;
    public static final int ENTITY_TYPE                   = 12;
    public static final int QNAME_TYPE                    = 13;
    //Derived datatypes
    public static final int NOTATION_TYPE                 = 14;
    public static final int CDATA_TYPE                    = 15;
    public static final int LANGUAGE_TYPE                 = 16;
    public static final int IDREFS_TYPE                   = 17;
    public static final int ENTITIES_TYPE                 = 18;
    public static final int NMTOKEN_TYPE                  = 19;
    public static final int NMTOKENS_TYPE                 = 20;
    public static final int NAME_TYPE                     = 21;
    public static final int NCNAME_TYPE                   = 22;
    public static final int INTEGER_TYPE                  = 23;
    public static final int NON_POSITIVE_INTEGER_TYPE     = 24;
    public static final int NEGATIVE_INTEGER_TYPE         = 25;
    public static final int LONG_TYPE                     = 26;
    public static final int INT_TYPE                      = 27;
    public static final int SHORT_TYPE                    = 28;
    public static final int BYTE_TYPE                     = 29;
    public static final int NON_NEGATIVE_INTEGER_TYPE     = 30;
    public static final int UNSIGNED_LONG_TYPE            = 31;
    public static final int UNSIGNED_INT_TYPE             = 32;
    public static final int UNSIGNED_SHORT_TYPE           = 33;
    public static final int UNSIGNED_BYTE_TYPE            = 34;
    public static final int POSITIVE_INTEGER_TYPE         = 35;
    public static final int TIME_INSTANT_TYPE             = 36;
    public static final int TIME_TYPE                     = 37;
    public static final int TIME_PERIOD_TYPE              = 38;
    public static final int DATE_TYPE                     = 39;
    public static final int MONTH_TYPE                    = 40;
    public static final int YEAR_TYPE                     = 41;
    public static final int CENTURY_TYPE                  = 42;
    public static final int RECURRING_DATE_TYPE           = 43;
    public static final int RECURRING_DAY_TYPE            = 44;

    /**
     * The resource location for the built-in types
     * property files
    **/
    static final String RESOURCE_LOCATION =
        "/org/exolab/castor/util/resources/";

    /**
     * The resource for the mapping properties
    **/
    static final String TYPE_MAPPINGS = RESOURCE_LOCATION +
        "SimpleTypesMapping.properties";

    /**
     * The resource for the Simple types
    **/
    static final String TYPE_DEFINITIONS = RESOURCE_LOCATION +
        "SimpleTypes.properties";
    /**
     * Holds simpletypesfactory.Type instances that record information about
     * xml schema built in types.
     */
    private static Hashtable _typesByName;

    /** Cross index for _typesByName to quickly get type information from its code*/
    private static Hashtable _typesByCode;

    /**
     * Log writer to report progress/errors. May be null.
     */
    private static PrintWriter _logWriter= new PrintWriter(System.out);


    /**
     * Gets a built in type's name given its code.
     */
    public String getBuiltInTypeName(int builtInTypeCode) {
        Type type= getType(builtInTypeCode);
        if (type == null) return null;
        else
            return type.getName();
    }


    /**
     * Creates an instance of a class derived from SimpleType, representing the
     * user type defined by the given name, baseName and derivation method.
     *
     * Package private (used by Schema and DeferredSimpleType).
     *
     * The given schema is used as the owning Schema document, yet a call to
     * schema.addSimpleType must till be made to add the SimpleType to the Schema.
     *
     * If the base type is not found in the schema, a DeferredSimpleType
     * will be returned if createDeferredSimpleType is true, null otherwise.
     *
     * @param schema the owning schema
     * @param name the name of the SimpleType
     * @param baseName the name of the SimpleType's base type
     * @param derivation the name of the derivation method (null/""/"list"/"restriction")
     * @param createDeferredSimpleType should the type be deferred if it can't be created.
     * @return the new SimpleType, or null if its parent could not be found.
    **/
    SimpleType createUserSimpleType( Schema schema,
                                     String name,
                                     String baseName,
                                     String derivation,
                                     boolean createDeferredSimpleType)
    {
        if ( (baseName == null) || (baseName.length() == 0) ) {
            //We need a base type name...
            sendToLog(Messages.format( "schema.noBaseType", name ));
            return null;
        }

        //Find the base type
        SimpleType baseType= schema.getSimpleType(baseName);
        if (baseType == null) {
            //couldn't find the base type, must be forward declared
            if (createDeferredSimpleType) { // => create a DeferredSimpleType
	            DeferredSimpleType result= new DeferredSimpleType();
	            result.setSchema(schema);
	            result.setName(name);
	            result.setBaseTypeName(baseName);
	            result.setDerivationMethod(derivation);
	            result.setTypeCode(USER_TYPE);
	            return result;
             }
            else
                return null;
        }
        
        return createUserSimpleType(schema, name, baseType, derivation);
    } 
        
    /**
     * Creates an instance of a class derived from SimpleType, representing the
     * user type defined by the given name, baseName and derivation method.
     *
     * Package private (used by Schema and DeferredSimpleType).
     *
     * The given schema is used as the owning Schema document, yet a call to
     * schema#addSimpleType must still be made to add the SimpleType to the 
     * Schema if the SimpleType is not anonymous.
     *
     * If the base type is not found in the schema, a DeferredSimpleType
     * will be returned if createDeferredSimpleType is true, null otherwise.
     *
     * @param schema the owning schema
     * @param name the name of the SimpleType
     * @param baseType the base type
     * @param derivation the name of the derivation method (null/""/"list"/"restriction")
     * @return the new SimpleType, or null if its parent could not be found.
    **/
    SimpleType createUserSimpleType( Schema schema,
                                     String name,
                                     SimpleType baseType,
                                     String derivation)
    {
        String internalName = name;
        if (name == null) internalName = "anonymous-simple-type";
        
        if ( (baseType == null) ) {
            //We need a base type
            sendToLog(Messages.format( "schema.noBaseType", internalName ));
            return null;
        }

        SimpleType result= null;

        if ( (derivation != null) && (derivation.equals(SchemaNames.LIST)) ) {
            //derive as list
            if ( !(baseType instanceof AtomicType) ) {
                //only lists of atomic values are allowed by the specification
                sendToLog( Messages.format("schema.deriveByListError", 
                                           internalName, 
                                           baseType.getName()) );
                return null;
            }
            result= new ListType();
            ((ListType)result).setType( (AtomicType)baseType );
        }
        else {
            //derive as restriction (only derivation allowed apart from list for simple types)
            //Find the built in ancestor type
            SimpleType builtInBase= baseType.getBuiltInBaseType();
            if (builtInBase == null) {
               sendToLog( Messages.format("schema.noBuiltInParent", 
                                          internalName) );
               return null;
            }

            //creates the instance of a class derived from SimpleType representing the new type.
	        result= createInstance(builtInBase.getName());
            if (result == null) {
                throw new RuntimeException( Messages.message("schema.cantLoadBuiltInTypes") );
            }
        }

        result.setSchema(schema);
	    result.setName(name);
        result.setBaseType(baseType);
        result.setDerivationMethod(derivation);
        result.setTypeCode(USER_TYPE);
        return result;
    } //-- createUserSimpleType

    /**
     * Returns the log writer.
     */
    private PrintWriter getLogWriter()
    {
        return _logWriter;
    }

    /**
     * Sends a message to the log through the logWriter (if its not null)
     */
    private void sendToLog(String message)
    {
        PrintWriter logger= getLogWriter();
        if (logger != null) {
            logger.println(message);
            logger.flush();
        }
    }

    /**
     * Gets the informations about the built in type which name is provided
     * as input parameter.
     * Loads the types definitions if they were not yet loaded
     */
    private Type getType(String typeName)
    {
        if (_typesByName == null) {
            loadTypesDefinitions();
        }
        return (Type)_typesByName.get(typeName);
    }

    /**
     * Gets the informations about the built in type which code is provided
     * as input parameter.
     * Loads the types definitions if they were not yet loaded
     */
    private Type getType(int typeCode)
    {
        if (_typesByCode == null) {
            loadTypesDefinitions();
        }
        return (Type)_typesByCode.get(new Integer(typeCode));
    }

    /**
     * Loads the built in type definitions from their xml file and its mapping file
     * into the static fields typesByName and typeByCode. Loading is done only once.
     */
    private synchronized void loadTypesDefinitions()
    {
        if ( (_typesByName == null) && (_typesByCode == null) ) {

            InputStream is = null;

	        try
             {  //Load the mapping file
		        Mapping mapping= new Mapping(getClass().getClassLoader());
		        mapping.setLogWriter(getLogWriter());

		        is = this.getClass().getResourceAsStream(TYPE_MAPPINGS);
				mapping.loadMapping( new InputSource(is) );

                //unmarshall the list of built in simple types
		        Unmarshaller unmarshaller= new Unmarshaller(TypeList.class);
		        unmarshaller.setMapping(mapping);
		        //-- turn off validation
		        unmarshaller.setValidation(false);

                is = this.getClass().getResourceAsStream(TYPE_DEFINITIONS);
		        TypeList typeList= (TypeList)unmarshaller.unmarshal( new org.xml.sax.InputSource(is) );

                //print what we just read (only in debug mode and if we have a logWriter)
		        if (Configuration.debug() && getLogWriter()!= null)
                   typeList.Print(getLogWriter());

                //Store the types by name in the typesByName and typesByCode hashtables
                //and create for each its associated SimpleType instance.
                Vector types= typeList.getTypes();
                _typesByName= new Hashtable();
                _typesByCode= new Hashtable();
		        for( int index= 0; index < types.size(); index++)
		        {
		            Type type= (Type)(types.elementAt(index));
                    _typesByName.put(type.getName(), type);
                    type.setSimpleType(createSimpleType(type));
                    _typesByCode.put(new Integer( type.getSimpleType().getTypeCode() ), type);
		        }
	        }
	        catch (Exception except)
	        {
                //Of course, this should not happen if the config files are there.
                System.out.println(Messages.message("schema.cantLoadBuiltInTypes")
                    + "\n  initial exception: " + except);
                throw new RuntimeException( Messages.message("schema.cantLoadBuiltInTypes") );
	        }
        }
    } //-- loadTypeDefinitions


    /**
     * Creates a SimpleType, valid only for built in types.
     */
    private SimpleType createSimpleType(Type type)
    {
        //Creates the instance of a class derived from SimpleType representing the type.
        SimpleType result= createInstance(type.getName());


        if (result == null)
            throw new RuntimeException( Messages.message("schema.cantLoadBuiltInTypes") );

	    result.setName(type.getName());


        //Load the result's typeCode
        int intCode;
        try {
            intCode= getClass().getDeclaredField(type.getCode()).getInt(null);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException( Messages.message("schema.cantLoadBuiltInTypes") );
        }

        result.setTypeCode(intCode);


        //Find and set the result's SimpleType basetype (if any).
        if (type.getBase() != null) {
            result.setBaseType( getType(type.getBase()).getSimpleType() );
        }

        //Adds the facets to the result
        Vector facets= type.getFacet();
        for (int index= 0; index < facets.size(); index++) {
            TypeProperty prop= (TypeProperty)facets.elementAt(index);
            if (!prop.getPseudo()) {
                //adds a "real" facet (defined in the xml specs)
                result.addFacet( new Facet(prop.getName(), prop.getValue()) );
            }
            else {
                //sets the information linked with the pseudo facet
                if (new RealType().getClass().isInstance(result))
                {
                    RealType realResult= (RealType)result;
                    if      (prop.getName().equals("minM")) realResult.setMinMantissa( Long.parseLong(prop.getValue()) );
                    else if (prop.getName().equals("maxM")) realResult.setMaxMantissa( Long.parseLong(prop.getValue()) );
                    else if (prop.getName().equals("minE")) realResult.setMinExponent( Long.parseLong(prop.getValue()) );
                    else if (prop.getName().equals("maxE")) realResult.setMaxExponent( Long.parseLong(prop.getValue()) );
                }
            }
        }

        return result;
    }

    /**
     * Creates the correct instance for the given type name.
     * Valid only for built in type names.
     */
    private SimpleType createInstance(String builtInTypeName)
    {
        Type type= getType(builtInTypeName);

        //If the type is derived by list, return a new ListType.
        String derivation= type.getDerivedBy();
        if ( (derivation != null) && (derivation.equals(SchemaNames.LIST)) ) {
            ListType result= new ListType();
        }

        //Finds the primitive ancestor (defines the class that represents it)
        Class implClass= null;
        while (type != null) {
            if (type.getImplClass() != null) {
                implClass= type.getImplClass();
                break;
            }
            else
                type= getType(type.getBase());
        }

        if (implClass == null) return null;

        SimpleType result;
        try
        {
            result= (SimpleType)implClass.newInstance();
        }
        catch (Exception except)
        {
            except.printStackTrace();
            result= null;
        }
        return result;
    }

}


