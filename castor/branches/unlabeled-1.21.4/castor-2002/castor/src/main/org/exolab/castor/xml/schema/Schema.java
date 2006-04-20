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

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * An XML Schema Definition. This class also contains the Factory methods for
 * creating Top-Level structures.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Schema extends Annotated {

    public static final String DEFAULT_SCHEMA_NS
        = "http://www.w3.org/1999/XMLSchema";


    private static final String NULL_ARGUMENT
        = "A null argument was passed to " +
           Schema.class.getName() + "#";

    private String name     = null;
    private String schemaNS = null;
    private String targetNS = null;


    /**
     * A list of defined architypes
    **/
    private Hashtable complexTypes = null;


    /**
     * A list of defined SimpleTypes
    **/
    private Hashtable simpleTypes = null;

    /**
     * A list of defined elements
    **/
    private Hashtable elements = null;

	/**
	 * A list of imported schemas
	 */
	private Hashtable importedSchemas = null;

	/**
	 * A list of  XML Schema files included in this schema
	 */
	private Vector includedSchemas = null;

	/**
	 * A list of namespaces declared in this schema
	 */
	private Hashtable namespaces = null;

    private static SimpleTypesFactory simpleTypesFactory= new SimpleTypesFactory();

    /**
     * Creates a new SchemaDef
    **/
    public Schema() {
        this(DEFAULT_SCHEMA_NS);
    } //-- ScehamDef


    /**
     * Creates a new SchemaDef
    **/
    public Schema(String schemaNS) {
        super();
        complexTypes = new Hashtable();
        simpleTypes  = new Hashtable();
        elements   = new Hashtable();
		importedSchemas = new Hashtable();
		includedSchemas = new Vector();
		namespaces = new Hashtable();
        this.schemaNS = schemaNS;
        init();
    } //-- ScehamDef

    private void init() {

    } //-- init

	/**
	 * Adds the given Schema definition to this Schema definition as an imported schenma
	 * @param schema the Schema to add to this Schema as an imported schema
     * @exception SchemaException if the Schema already exists
	 */
	public synchronized void addSchema(Schema schema)
        throws SchemaException
	{
		String targetNamespace = schema.getTargetNamespace();
		if (importedSchemas.get(targetNamespace)!=null)
		{
            String err = "a Schema has already been imported with the given namespace: ";
            throw new SchemaException(err + targetNamespace);
		}
		importedSchemas.put(targetNamespace, schema);
	}

	/**
	 * Returns True if the namespace is known to this schema
	 * @param namespace the namespace URL
	 * @return True if the namespace was declared in the schema
	 */
	public boolean isKnownNamespace(String namespaceURL)
	{
		Enumeration urls = namespaces.elements();
		while(urls.hasMoreElements())
			if (urls.nextElement().equals(namespaceURL))
				return true;
		return false;
	}

    /**
     * Adds the given Complextype definition to this Schema defintion
     * @param complextype the Complextype to add to this Schema
     * @exception SchemaException if the Complextype does not have
     * a name or if another Complextype already exists with the same name
    **/
    public synchronized void addComplexType(ComplexType complexType)
        throws SchemaException
    {

        String name = complexType.getName();

        if (name == null) {
            String err = "a global ComplexType must contain a name.";
            throw new SchemaException(err);
        }
        if (complexType.getSchema() != this) {
            String err = "invalid attempt to add an ComplexType which ";
            err += "belongs to a different Schema; type name: " + name;
            throw new SchemaException(err);
        }
        if (complexTypes.get(name) != null) {
            String err = "a ComplexType already exists with the given name: ";
            throw new SchemaException(err + name);
        }
        complexTypes.put(name, complexType);

    } //-- addComplextype

    /**
     * Adds the given SimpletType definition to this Schema defintion
     * @param simpletype the SimpleType to add to this Schema
     * @exception SchemaException if the SimpleType does not have
     * a name or if another SimpleType already exists with the same name
    **/
    public synchronized void addSimpleType(SimpleType simpleType)
        throws SchemaException
    {

        String name = simpleType.getName();

        if ((name == null) || (name.length() == 0)) {
            String err = "No name found for top-level SimpleType. " +
                " A top-level SimpleType must have a name.";
            throw new SchemaException(err);
        }

        if (simpleType.getSchema() != this) {
            String err = "invalid attempt to add a SimpleType which ";
            err += "belongs to a different Schema; type name: " + name;
            throw new SchemaException(err);
        }
        if (simpleTypes.get(name) != null) {
            String err = "a SimpleType already exists with the given name: ";
            throw new SchemaException(err + name);
        }

        simpleType.setParent(this);
        simpleTypes.put(name, simpleType);

    } //-- addSimpleType

    /**
     * Adds the given Element declaration to this Schema defintion
     * @param elementDecl the ElementDecl to add to this SchemaDef
     * @exception SchemaException when an ElementDecl already
     * exists with the same name as the given ElementDecl
    **/
    public void addElementDecl(ElementDecl elementDecl)
        throws SchemaException
    {

        String name = elementDecl.getName();

        if (name == null) {
            String err = "an element declaration must contain a name.";
            throw new SchemaException(err);
        }
        if (elements.get(name) != null) {
            String err = "an element declaration already exists with the given name: ";
            throw new SchemaException(err + name);
        }

        elements.put(name, elementDecl);


    } //-- addElementDecl


    /**
     * Creates a new ComplexType using this Schema as the owning Schema
     * document. A call to #addComplexType must still be made in order
     * to add the complexType to this Schema.
     * @return the new ComplexType
    **/
    public ComplexType createComplexType() {
        return new ComplexType(this);
    } //-- createComplexType

    /**
     * Creates a new ComplexType using this Schema as the owning Schema
     * document. A call to #addComplexType must still be made in order
     * to add the complexType to this Schema.
     * @param name the name of the ComplexType
     * @return the new ComplexType
    **/
    public ComplexType createComplexType(String name) {
        return new ComplexType(this, name);
    } //-- createComplexType

    /**
     * Creates a new SimpleType using this Schema as the owning Schema
     * document. A call to #addSimpleType must till be made in order
     * to add the SimpleType to this Schema.
     * @param name the name of the SimpleType
     * @param baseName the name of the SimpleType's base type
     * @param derivation the name of the derivation method (""/"list"/"restriction")
     * @return the new SimpleType.
    **/
    public SimpleType createUserSimpleType(String name, String baseName, String derivation)
    {
        return simpleTypesFactory.createUserSimpleType(this, name, baseName, derivation, true);
    }


    /**
     * Returns the ComplexType of associated with the given name
     * @return the ComplexType of associated with the given name, or
     *  null if no ComplexType with the given name was found.
    **/
    public ComplexType getComplexType(String name) {

		//-- Null?
        if (name == null)  {
            String err = NULL_ARGUMENT + "getComplexType: ";
            err += "'name' cannot be null.";
            throw new IllegalArgumentException(err);
        }

        //-- Namespace prefix?
        String canonicalName = name;
		String nsprefix = "";
		String ns = targetNS;
        int colon = name.indexOf(':');
        if (colon != -1)
		{
            canonicalName = name.substring(colon + 1);
			nsprefix = name.substring(0,colon);
			ns = (String) namespaces.get(nsprefix);
			if (ns == null)  {
			    String err = "getComplexType: ";
			    err += "Namespace prefix not recognised '"+name+"'";
			    throw new IllegalArgumentException(err);
			}
		}

		//-- Get GetComplexType object
		if ((ns==null) || (ns.equals(targetNS)) )
			return (ComplexType)complexTypes.get(canonicalName);
		else {
			Schema schema = getImportedSchema(ns);
			if (schema!=null)
				return schema.getComplexType(canonicalName);
		}

		return null;

    } //-- getComplexType

    /**
     * Returns an Enumeration of all top-level ComplexType declarations
     * @return an Enumeration of all top-level ComplexType declarations
    **/
    public Enumeration getComplexTypes() {
        return complexTypes.elements();
    } //-- getComplextypes


    /**
     * Gets a built in type's name given its code.
     */
    public String getBuiltInTypeName(int builtInTypeCode) {
        return simpleTypesFactory.getBuiltInTypeName(builtInTypeCode);
    }

    /**
     * Returns the SimpleType associated with the given name,
     * or null if no such SimpleType exists.
     * @return the SimpleType associated with the given name,
     * or null if no such SimpleType exists.
    **/
    public SimpleType getSimpleType(String name) {

		//-- Null?
        if (name == null)  {
            String err = NULL_ARGUMENT + "getSimpleType: ";
            err += "'name' cannot be null.";
            throw new IllegalArgumentException(err);
        }

        //-- Namespace prefix?
        String canonicalName = name;
		String nsPrefix = "";
		String ns = null;
        int colon = name.indexOf(':');
        if (colon >= 0) {
            canonicalName = name.substring(colon + 1);
			nsPrefix = name.substring(0,colon);
			ns = (String) namespaces.get(nsPrefix);
			if (ns == null)  {
			    String err = "getSimpleType: ";
			    err += "Namespace prefix not recognised '"+name+"'";
			    throw new IllegalArgumentException(err);
			}
		}

		//-- Get SimpleType object
		SimpleType result = null;
		if (ns == null) {
		    //-- first try built-in types
			result= simpleTypesFactory.getBuiltInType(name);
			//-- otherwise check user-defined types
			if (result == null)
			    result = (SimpleType)simpleTypes.get(name);
		}
		else if (ns.equals(schemaNS))
			result= simpleTypesFactory.getBuiltInType(canonicalName);
		else if (ns.equals(targetNS))
			result = (SimpleType)simpleTypes.get(canonicalName);
		else {
			Schema schema = getImportedSchema(ns);
			if (schema!=null)
				result = schema.getSimpleType(canonicalName);
		}

		//-- Result could be a deferredSimpleType => getType will resolve it
		if (result!=null)
            result= (SimpleType)result.getType();

        return result;
    } //-- getSimpleType

    /**
     * Returns an Enumeration of all SimpleType declarations
     * @return an Enumeration of all SimpleType declarations
    **/
    public Enumeration getSimpleTypes() {
        return simpleTypes.elements();
    } //-- getSimpleTypes

    /**
     * Returns the ElementDecl of associated with the given name
     * @return the ElementDecl of associated with the given name, or
     *  null if no ElementDecl with the given name was found.
    **/
    public ElementDecl getElementDecl(String name) {
        return (ElementDecl)elements.get(name);
    } //-- getElementDecl

    /**
     * Returns an Enumeration of all top-level element declarations
     * @return an Enumeration of all top-level element declarations
    **/
    public Enumeration getElementDecls() {
        return elements.elements();
    } //-- getElementDecls

	/**
	 * Returns an imported schema by it's namespace
	 * @return The imported schema
	 */
	public Schema getImportedSchema(String ns)
	{
		return (Schema) importedSchemas.get(ns);
	} //-- getImportedSchema

	/**
	 * Indicates that the given XML Schema file has been processed via an <xsd:include>
	 */
	public void addInclude(String include)
	{
		includedSchemas.addElement(include);
	} //-- addInclude

	/**
	 * Returns True if the given XML Schema has already been included via <xsd:include>
	 * @return True if the file specified has already been processed
	 */
	public boolean includeProcessed(String includeFile)
	{
		return includedSchemas.contains(includeFile);
	} //-- includeProcessed

    /**
     * Returns the namespace of the XML Schema
     * <BR />
     * Note: This is not the same as targetNamespace. This is
     * the namespace of "XML Schema" itself and not the namespace of the
     * schema that is represented by this object model
     * (see #getTargetNamespace).
     * @return the namespace of the XML Schema
     *
    **/
    public String getSchemaNamespace() {
        return this.schemaNS;
    } //-- getSchemaNamespace

    /**
     * Returns the target namespace for this Schema, or null if no
     * namespace has been defined.
     * @return the target namespace for this Schema, or null if no
     * namespace has been defined
    **/
    public String getTargetNamespace() {
        return this.targetNS;
    } //-- getTargetNamespace

    /**
     * Returns the namespaces declared for this Schema
     * @return the namespaces declared for this Schema
    **/
    public Hashtable getNamespaces() {
        return this.namespaces;
    } //-- getNamespaces

    /**
     * Removes the given top level ComplexType from this Schema
     * @param complexType the ComplexType to remove
     * @return true if the complexType has been removed, or
     * false if the complexType wasn't top level or
     * didn't exist in this Schema
    **/
    public boolean removeComplexType(ComplexType complexType) {
        if (complexType.isTopLevel()) {
            if (complexTypes.contains(complexType)) {
                complexTypes.remove(complexType.getName());
                return true;
            }
        }
        return false;
    } //-- removeComplexType

    /**
     * Removes the given top level SimpleType from this Schema
     * @param SimpleType the SimpleType to remove
     * @return true if the SimpleType has been removed, or
     * false if the SimpleType wasn't top level or
     * didn't exist in this Schema
    **/
    public boolean removeSimpleType(SimpleType simpleType) {
        if (simpleTypes.contains(simpleType)) {
            simpleTypes.remove(simpleType.getName());
            return true;
        }
        return false;
    } //-- removeSimpleType

    /**
     * Sets the name of this Schema definition
    **/
    public void setName(String name) {
        this.name = name;
    } //-- setName


    /**
     * Returns the first simple or complex type which name equals TypeName
     */
    public XMLType getType(String typeName)
    {
        XMLType result= getSimpleType(typeName);
        if (result == null)
            result = getComplexType(typeName);
        return result;
    }


    /**
     * Sets the target namespace for this Schema
     * @param targetNamespace the target namespace for this Schema
     * @see <B>&sect; 2.7 XML Schema Part 1: Structures</B>
    **/
    public void setTargetNamespace(String targetNamespace) {
        this.targetNS = targetNamespace;
    } //-- setTargetNamespace


	/**
	 * Adds to the namespaces declared in this Schema
	 * @param namespaces the list of namespaces
	 */
	public void addNamespace(String prefix, String ns) {
		namespaces.put(prefix, ns);
	} //-- setNamespaces


    /** Gets the type factory, package private */
    static SimpleTypesFactory getTypeFactory() { return simpleTypesFactory; }

    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.SCHEMA;
    } //-- getStructureType

    /**
     * Checks the validity of this Schema defintion.
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public void validate()
        throws ValidationException
    {
        //-- do nothing
    } //-- validate

} //-- SchemaDef


