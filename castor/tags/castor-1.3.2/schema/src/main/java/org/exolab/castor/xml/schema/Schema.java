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

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.ValidationException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;


/**
 * A class representing an XML Schema Definition. This class also 
 * contains some Factory methods for creating Top-Level structures.
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-26 13:58:52 -0600 (Wed, 26 Apr 2006) $
 */
public class Schema extends Annotated {
    /** SerialVersionUID */
    private static final long serialVersionUID = -8130246250710502508L;

    //-----------------------------/
    //- Class Fields / Constants  -/
    //-----------------------------/
    
    /*
       // Old schema namespaces, left here for
       // reference
       
      April 2000 Working Draft Namespace
        = "http://www.w3.org/1999/XMLSchema";
        
      October 2000 Candidate Release Namespace
        = "http://www.w3.org/2000/10/XMLSchema";
        
     */

    /**
     * The Namespace supported by the W3C XML Schema
     * Recommendation.
     */
    public static final String DEFAULT_SCHEMA_NS
        = "http://www.w3.org/2001/XMLSchema";

    /**
     * The Namespace supported by the W3C XML Schema
     * Recommendation for the built-in types:
     * xsi:type, xsi:nil, and xsi:schemaLocation.
     */
    public static final String XSI_NAMESPACE
        = "http://www.w3.org/2001/XMLSchema-instance";
        
    /** 
     * Null argument error message
     */
    private static final String NULL_ARGUMENT
        = "A null argument was passed to " +
           Schema.class.getName() + "#";

    /**
     * The SimpleTypesFactory used by this Schema
     */
    private static SimpleTypesFactory simpleTypesFactory
        = new SimpleTypesFactory();

    //--------------------/
    //- Member Variables -/
    //--------------------/

    /**
     * The attributeFormDefault property
    **/
    private Form _attributeFormDefault = null;

    /**
     * The global AttribteGroups for this Schema
    **/
    private Map<String, AttributeGroup> _attributeGroups = new Hashtable<String, AttributeGroup>();

    /**
     * The global attributes for this Schema
    **/
    private Map<String, AttributeDecl> _attributes = new Hashtable<String, AttributeDecl>();

    /**
     * The value of the block attribute.
    **/
    private BlockList _block = null;

    
    /**
     * A list of defined architypes
    **/
    private Map<String, ComplexType> _complexTypes = new Hashtable<String, ComplexType>();

    /**
     * The elementFormDefault attribute for this Schema
    **/
    private Form _elementFormDefault = null;

    /**
     * A list of defined elements
    **/
    private Map<String, ElementDecl> _elements = new Hashtable<String, ElementDecl>();

    /**
     * The value of the final attribute.
    **/
    private FinalList _final = null;

    /**
     * A list of defined top-levels groups
     */
    private Map<String, ModelGroup> _groups = new Hashtable<String, ModelGroup>();
    
    /**
     * A list of defined <redefine>
     */
    private Map<String, RedefineSchema> _redefineSchemas = new Hashtable<String, RedefineSchema>();

    /**
     * The ID for this Schema
    **/
    private String _id = null;

    /**
     * A list of imported schemas
    **/
    private Map<String, Schema> _importedSchemas = new Hashtable<String, Schema>();
    
    /**
     * A list of included schemas meant to be used only 
     * when the cache mechanism is enabled.
     **/
    private Map<String, Schema> _cachedincludedSchemas = new Hashtable<String, Schema>();
    

    /**
     * A list of XML Schema files included in this schema
    **/
    private Vector<String> _includedSchemas = new Vector<String>();

    /**
     * A list of namespaces declared in this schema
     */
    private Namespaces _namespaces = new Namespaces();

    /**
     * The schemaLocation hint provided in the 'import' tag.
     * By default the schemaLocation is the locator of the SaxUnmarshaller
    **/
    private String _schemaLocation = null;

    /**
     * The namespace of this XML Schema (ie the namespace
     * of the W3C Schema supported by this Schema).
    **/
    private String _schemaNamespace = null;


    /**
     * A list of defined SimpleTypes
    **/
    private Hashtable<String, SimpleType> _simpleTypes = new Hashtable<String, SimpleType>();

    /**
     * The targetNamespace for this Schema
    **/
    private String _targetNamespace = null;

    /**
     * The version information as specified by the version
     * attribute
    **/
    private String _version  = null;
    
    /**
     * A reference to the master schema used when this
     * instance of Schema is used in another schema
     * (redefine, include or import)
     */
    private Schema _masterSchema = null;

    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates a new Schema definition
     */
    public Schema() {
        this(null, DEFAULT_SCHEMA_NS);
    }


    /**
     * Creates a new Schema definition
     *
     * @param schemaNS the namespace of the XML Schema itself. Note
     * this is not the same as the targetNamespace.
     */
    public Schema(String schemaNS) {
        this(null, schemaNS);
    }

    /**
     * Creates a new Schema definition
     *
     * @param prefix the desired namespace prefix for the schemaNS.
     * @param schemaNamespace the namespace of the XML Schema itself. Note
     * this is not the same as the targetNamespace.
     */
    public Schema(String prefix, String schemaNamespace) {
        super();

        if (schemaNamespace == null) {
            _schemaNamespace = DEFAULT_SCHEMA_NS;
        } else {
            _schemaNamespace = schemaNamespace;
        }
        
        //-- declare default namespace bindings
        if (prefix == null) {
            prefix = "";
        }
        
        addNamespace(prefix, _schemaNamespace);

    }
    
    /**
     * Adds the given attribute definition to this Schema definition
     *
     * @param attribute the AttributeDecl to add
     * @sexception SchemaException if an AttributeDecl
     * already exisits with the same name
    **/
    public void addAttribute(AttributeDecl attribute)
        throws SchemaException
    {
        if (attribute == null) return;

        String name = attribute.getName();

        if (attribute.getSchema() != this) {
            String err = "invalid attempt to add an AttributeDecl which ";
            err += "belongs to a different Schema; " + name;
            throw new SchemaException(err);
        }

        if ( (name == null) && (attribute.isReference()) ) {
                String err = "Error attempting to add a top-level AttributeDecl that " +
                "is a reference. Top-level attributes can only be attribute declarations: " +
                attribute.getName(false);
            throw new SchemaException(err);
        }
        //-- we check if the attribute is not already present in the schema 
        //-- the attribute can be already added
        //-- the attribute can be part of a schema redefinition
        //-- the attribute can be part of a cached included schema
        Object obj = getAttribute(name);
        
        if (obj == attribute) return;

        if (obj != null) {
            String err = "Error attempting to add an AttributeDecl to this " +
                "Schema definition, an AttributeDecl already exists with " +
                "the given name: ";
            throw new SchemaException(err + name);
        }

        _attributes.put(name, attribute);
        //--set the parent
        attribute.setParent(this);
    } //-- addAttribute

    /**
     * Adds the given attribute group definition to this Schema
     * definition.
     *
     * @param attrGroup the AttributeGroupDecl to add
     * @exception SchemaException if an AttributeGroupDecl
     * already exisits with the same name
    **/
    public void addAttributeGroup(AttributeGroupDecl attrGroup)
        throws SchemaException
    {
        if (attrGroup == null) return;

        String name = attrGroup.getName();

        //-- handle namespace prefix, if necessary
        int idx = name.indexOf(':');
        if (idx >= 0)
        {
            String nsPrefix = name.substring(0,idx);
            name = name.substring(idx + 1);
            String ns = _namespaces.getNamespaceURI(nsPrefix);
            if (ns == null)  {
                String err = "addAttributeGroup: ";
                err += "Namespace prefix not recognized '"+nsPrefix+"'";
                throw new IllegalArgumentException(err);
            }
            if (!ns.equals(_targetNamespace)) {
                String err = "AttributeGroup has different namespace " +
                 "than this Schema definition.";
                throw new IllegalArgumentException(err);
            }
        }

        if (attrGroup.getSchema() != this) {
            String err = "invalid attempt to add an AttributeGroup which ";
            err += "belongs to a different Schema; " + name;
            throw new SchemaException(err);
        }

        Object obj = getAttributeGroup(name);
      
        if (obj == attrGroup) return;
        boolean redefine = attrGroup.isRedefined();
        
        if (obj != null && !redefine) {
            String err = "Error attempting to add an AttributeGroup to this " +
                "Schema definition, an AttributeGroup already exists with " +
                "the given name: ";
            throw new SchemaException(err + name);
        }

        _attributeGroups.put(name, attrGroup);

    } //-- addAttributeGroup


    /**
     * Adds the given Complextype definition to this Schema defintion
     * @param complexType the Complextype to add to this Schema
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
        //-- we check if the complexType is not already present in the schema 
        //-- the complexType can be already added
        //-- the complexType can be part of a schema redefinition
        //-- the complexType can be part of a cached included schema
        if (getComplexType(name) != null  && !complexType.isRedefined()) {
            String err = "a ComplexType already exists with the given name: ";
            throw new SchemaException(err + name);
        }
        _complexTypes.put(name, complexType);
        complexType.setParent(this);

    } //-- addComplextype

    /**
     * Adds the given Element declaration to this Schema defintion
     * @param elementDecl the ElementDecl to add to this SchemaDef
     * @exception SchemaException when an ElementDecl already
     * exists with the same name as the given ElementDecl
    **/
    public void addElementDecl(ElementDecl elementDecl)
        throws SchemaException
    {

        String name = elementDecl.getName(true);

        if ( (name == null) && (elementDecl.isReference()) ) {
              String err = "Error attempting to add a top-level Element that " +
              "is a reference. Top-level elements can only be element declarations: " +
                elementDecl.getName(false);
            throw new SchemaException(err);
        }

        if (name == null) {
            String err = "an element declaration must contain a name.";
            throw new SchemaException(err);
        }
        if (getElementDecl(name) != null) {
            String err = "an element declaration already exists with the given name: ";
            throw new SchemaException(err + name);
        }

        _elements.put(name, elementDecl);
        elementDecl.setParent(this);

    } //-- addElementDecl

     /**
     * Adds the given Group declaration to this Schema definition
     * @param group the Group to add to this SchemaDef
     * @exception SchemaException when an Group already
     * exists with the same name as the given Group
    **/
    public void addModelGroup(ModelGroup group)
        throws SchemaException
    {

        String name = group.getName();

        if (name == null) {
            String err = "a group declaration must contain a name.";
            throw new SchemaException(err);
        }
        
        if (getModelGroup(name) != null && !group.isRedefined()) {
            String err = "a group declaration already exists with the given name: ";
            throw new SchemaException(err + name);
        }

        _groups.put(name, group);
        group.setParent(this);
    } //-- addModelGroup
    
    /**
     * Adds the given redefinition of structures to this Schema definition.
     * This structure is mainly used to allow the writing of an XML schema that
     * contains redefinitions. The validation process is permissive since the method
     * won't check that the XML Schema is already imported nor will it check that the 
     * redefined structures exist.
     * 
     * @param schema the Group to add to this SchemaDef
     * @exception SchemaException when an redefintion already
     * exists with the same name as the given ElementDecl
     **/
    public void addRedefineSchema(RedefineSchema schema) throws SchemaException
	{

    	String uri = schema.getSchemaLocation();

    	if ( (uri == null) && (schema.hasRedefinition()) ) {
    		String err = "A <redefine> structure with no 'schemaLocation' attribute must contain only <annotation> elements";
    		throw new SchemaException(err);
    	}
    	if (_redefineSchemas.get(uri) != null) {
    		String err = "The redefinition for schema:"+ uri +" can only be used once.";
    		throw new SchemaException(err);
    	}
        
    	_redefineSchemas.put(uri, schema);
    } //-- addModelGroup
    

    /**
     * Adds the given Schema definition to this Schema definition as an imported schenma
     * @param schema the Schema to add to this Schema as an imported schema
     * @exception SchemaException if the Schema already exists
     */
    public synchronized void addImportedSchema(Schema schema)
        throws SchemaException
    {
        String targetNamespace = schema.getTargetNamespace();
        if (targetNamespace == null) targetNamespace = "";
        if (_importedSchemas.get(targetNamespace)!=null)
        {
            String err = "a Schema has already been imported with the given namespace: ";
            throw new SchemaException(err + targetNamespace);
        }
        _importedSchemas.put(targetNamespace, schema);
    } //-- addImportedSchema
    
    /**
     * Caches the given Schema definition as an included XML Schema of this
     * Schema definition.
     * 
     * @param schema the Schema to add to this Schema as a cached included schema.
     * @exception SchemaException if the Schema already exists
     */
    public synchronized void cacheIncludedSchema(Schema schema)
	throws SchemaException
	{
    	String schemaLocation = schema.getSchemaLocation();
    	if (schemaLocation == null) schemaLocation= "";
    	if (_cachedincludedSchemas.get(schemaLocation)!=null)
    	{
    		String err = "a Schema has already been included with the given schemaLocation: ";
    		throw new SchemaException(err + schemaLocation);
    	}
    	_cachedincludedSchemas.put(schemaLocation, schema);
    } //-- addImportedSchema
    

    /**
     * Adds to the namespaces declared in this Schema
     */
    public void addNamespace(String prefix, String ns) {
        _namespaces.addNamespace(prefix, ns);
    } //-- setNamespaces

    /**
     * Adds the given SimpletType definition to this Schema defintion
     * @param simpleType the SimpleType to add to this Schema
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
        if (getSimpleType(name, _targetNamespace) != null && !simpleType.isRedefined()) {
            String err = "a SimpleType already exists with the given name: ";
            throw new SchemaException(err + name);
        }
        simpleType.setParent(this);
        _simpleTypes.put(name, simpleType);

    } //-- addSimpleType



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
    public SimpleType createSimpleType(String name, String baseName, String derivation)
    {
        return simpleTypesFactory.createUserSimpleType(this, name, baseName, derivation, true);
    } //-- createSimpleType

    /**
     * Creates a new SimpleType using this Schema as the owning Schema
     * document. A call to #addSimpleType must till be made in order
     * to add the SimpleType to this Schema if the type is to be global.
     * @param name the name of the SimpleType
     * @param baseType the base type of the SimpleType to create
     * @return the new SimpleType.
    **/
    public SimpleType createSimpleType(String name, SimpleType baseType)
    {
        return simpleTypesFactory.createUserSimpleType(this, name, baseType, "restriction");
    } //-- createSimpleType

    /**
     * Returns the attributeFormDefault property of this Schema.
     *
     * @return the attributeFormDefault property of this Schema, or null
     * if no default Form was set. If no default Form has been set, the
     * user should assume Form.Unqualified.
    **/
    public Form getAttributeFormDefault() {
        return _attributeFormDefault;
    } //-- getAttributeFormDefault

    /**
     * Returns an Enumeration of all top-level Attribute declarations
     * @return an Enumeration of all top-level Attribute declarations
    **/
    public Collection<AttributeDecl> getAttributes() {
    	Vector<AttributeDecl> result = new Vector<AttributeDecl>(_attributes.size()*2);
    	
    	result.addAll(_attributes.values());
    
    	Collection<Schema> cachedincluded = _cachedincludedSchemas.values();
    	for (Schema includedSchema : cachedincluded) {
            Collection<AttributeDecl> tempAtt = includedSchema.getAttributes();
            for (AttributeDecl attributeDecl : tempAtt) {
                result.add(attributeDecl);
            }
        }
    	
    	Collection<RedefineSchema> redefinition = _redefineSchemas.values();
    	for (RedefineSchema redefineSchema : redefinition) {
            Schema tempSchema = redefineSchema.getOriginalSchema();
            //-- a redefinition doesn't always contain a schema
            if (tempSchema != null) {
                //-- Sets the master schema
                //-- The master schema will help resolving the links at runtime between
                //-- an structure and its type.
                tempSchema.setMasterSchema(this);
                Collection<AttributeDecl> tempAtt = tempSchema.getAttributes();
                for (AttributeDecl attributeDecl : tempAtt) {
                    result.add(attributeDecl);
                }
            }
        }
    	
    	return result;
    }

    /**
     * Returns the top-level Attribute associated with the given name.
     *
     * @return the Attribute associated with the given name,
     * or null if no Attribute association is found.
    **/
    public AttributeDecl getAttribute(String name) {

        //-- Null?
        if (name == null)  {
            String err = NULL_ARGUMENT + "getAttribute: ";
            err += "'name' cannot be null.";
            throw new IllegalArgumentException(err);
        }

        //-- Namespace prefix?
        String canonicalName = name;
        String nsprefix = "";
        String ns = _targetNamespace;
        int colon = name.indexOf(':');
        if (colon != -1)
        {
            canonicalName = name.substring(colon + 1);
            nsprefix = name.substring(0,colon);
            ns = _namespaces.getNamespaceURI(nsprefix);
            if (ns == null)  {
                String err = "getAttribute: ";
                err += "Namespace prefix not recognized '"+name+"'";
                throw new IllegalArgumentException(err);
            }
        }

        if ((ns==null) || (ns.equals(_targetNamespace)) ) {
        	AttributeDecl tempAtt = _attributes.get(canonicalName);
        	if (tempAtt == null) {
    			Collection<Schema> cacheIncluded = _cachedincludedSchemas.values();
                boolean found = false;
    			for (Schema includedSchema : cacheIncluded) {
                    tempAtt = includedSchema.getAttribute(canonicalName);
                    if (tempAtt != null) {
                        found = true;
                        break;
                    }
                }
//    			boolean found = false;
//    			while (cacheIncluded.hasMoreElements() && !found) {
//    				Schema temp = cacheIncluded.nextElement();
//    				tempAtt = temp.getAttribute(canonicalName);
//    				if (tempAtt != null)
//    					found = true;
//    			}
    			
                //--look in the redefinition
    			if (!found) {
    				Iterator<RedefineSchema> redefinition = _redefineSchemas.values().iterator();
    				while (redefinition.hasNext() && !found) {
    					Schema tempSchema = redefinition.next().getOriginalSchema();
    					if (tempSchema != null) {
    						//-- Sets the master schema
    						//-- The master schema will help resolving the links at runtime between
    						//-- an structure and its type.
    						tempSchema.setMasterSchema(this);
    						tempAtt = tempSchema.getAttribute(canonicalName);
    					    if (tempAtt != null)
    						    found = true;
    					}
    				}
    			}
        	}
        	return tempAtt;
        }
        Schema schema = getImportedSchema(ns);
        if (schema!=null) {
            AttributeDecl att = schema.getAttribute(canonicalName);
            return att;
        }

        return null;

    } //-- getAttribute


    /**
     * Returns an Enumeration of all top-level AttributeGroup declarations
     * @return an Enumeration of all top-level AttributeGroup declarations
    **/
    public Collection<AttributeGroup> getAttributeGroups() {
    	Vector<AttributeGroup> result = new Vector<AttributeGroup>(_attributeGroups.size()*2);
    	
    	result.addAll(_attributeGroups.values());
    	
    	Collection<Schema> cachedincluded = _cachedincludedSchemas.values();
    	for (Schema includedSchema : cachedincluded) {
            Collection<AttributeGroup> tempAtt = includedSchema.getAttributeGroups();
            for (AttributeGroup attributeGroup : tempAtt) {
                result.add(attributeGroup);  
            }
        }

    	Collection<RedefineSchema> redefinition = _redefineSchemas.values();
    	for (RedefineSchema redefineSchema : redefinition) {
    	    //1--  Add the AttributeGroups from the RedefineSchema
    	    for (AttributeGroup attributeGroup : redefineSchema.enumerateAttributeGroups()) {
                result.add(attributeGroup);
            }
    	    
    	    //2-- Add the AttributeGroups from the Original Schema of the
    	    //--  RedefineSchema Structure by making sure that the AttributeGroups
    	    //--  are not redefined.
    	    Schema tempSchema = redefineSchema.getOriginalSchema();
    	    //-- a redefinition doesn't always contain a schema
    	    if (tempSchema != null) {
    	        //-- Sets the master schema
    	        //-- The master schema will help resolving the links at runtime between
    	        //-- a structure and its type.
    	        tempSchema.setMasterSchema(this);
    	        for (AttributeGroup attributeGroup : tempSchema.getAttributeGroups()) {
    	            boolean alreadyRedefined = true;
    	            if (attributeGroup instanceof AttributeGroupDecl) {
    	                alreadyRedefined = redefineSchema.hasAttributeGroupRedefinition(((AttributeGroupDecl)attributeGroup).getName());
    	            }
    	            if (!alreadyRedefined) {
                        result.add(attributeGroup);
                    }	
                }
    	    }
        }
    	
    	return result;
    } //-- getAttributeGroups

    /**
     * Returns the AttributeGroup associated with the given name.
     *
     * @return the AttributeGroup associated with the given name,
     * or null if no AttributeGroup association is found.
    **/
    public AttributeGroup getAttributeGroup(String name) {

        //-- Null?
        if (name == null)  {
            String err = NULL_ARGUMENT + "getAttributeGroup: ";
            err += "'name' cannot be null.";
            throw new IllegalArgumentException(err);
        }
        
        AttributeGroup result = null;
        //-- Namespace prefix?
        String canonicalName = name;
        String nsprefix = "";
        String ns = _targetNamespace;
        int colon = name.indexOf(':');
        if (colon != -1)
        {
            canonicalName = name.substring(colon + 1);
            nsprefix = name.substring(0,colon);
            ns = _namespaces.getNamespaceURI(nsprefix);
            if (ns == null)  {
                String err = "getAttributeGroup: ";
                err += "Namespace prefix not recognized '"+name+"'";
                throw new IllegalArgumentException(err);
            }
        }

        if ((ns==null) || (ns.equals(_targetNamespace)) ) {
        	result = _attributeGroups.get(canonicalName);
            
        	if (result == null) {
            	Iterator<Schema> cacheIncluded = _cachedincludedSchemas.values().iterator();
            	boolean found = false;
            	while (cacheIncluded.hasNext() && !found) {
            	    Schema temp = cacheIncluded.next();
            		result = temp.getAttributeGroup(canonicalName);
            		if (result != null) {
                        found = true;
                    }
            	}
            	
            	//--we look in the redefinitions
            	if (!found) {
	            	//-- Search through the redefinition:
                	Iterator<RedefineSchema> redefinitions = getRedefineSchema().iterator();
	            	while (redefinitions.hasNext() && !found) {
	            		RedefineSchema redefine = redefinitions.next();
	            		
	            		//-- the AttributeGroup can be redefined
	            		if (redefine.hasAttributeGroupRedefinition(canonicalName)) {
	            			result = redefine.getAttributeGroup(canonicalName);
	            			if (result != null)
	            				found = true;
	            			break;
	            		}
	            		
	            		//-- or if can be part of the Original Schema of the Redefined structures.
	            		Schema schema = redefine.getOriginalSchema();
	            		if (schema != null) {
	            			//-- Sets the master schema
	            			//-- The master schema will help resolving the links at runtime between
	            			//-- an structure and its type.
	            			schema.setMasterSchema(this);
	            			result = schema.getAttributeGroup(name);
	            		}
	            	}    
            	}//--end of redefinition
            }//--result == null
        }
        else {
            Schema schema = getImportedSchema(ns);
            if (schema!=null)
                result = schema.getAttributeGroup(canonicalName);
        }

        return result;

    } //-- getAttributeGroup

    /**
     * Returns the default BlockList for this Schema.
     *
     * @return the default BlockList for this Schema.
    **/
    public BlockList getBlockDefault() {
        return _block;
    } //-- getBlockDefault

    /**
     * Gets a built in type's name given its code.
     */
    public String getBuiltInTypeName(int builtInTypeCode) {
        return simpleTypesFactory.getBuiltInTypeName(builtInTypeCode);
    } //-- getBuiltInTypeName


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
        ComplexType result = null;
        //-- Namespace prefix?
        String canonicalName = name;
        String nsprefix = "";
        String ns = _targetNamespace;
        int colon = name.indexOf(':');
        if (colon != -1)
        {
            canonicalName = name.substring(colon + 1);
            nsprefix = name.substring(0,colon);
            ns = _namespaces.getNamespaceURI(nsprefix);
            if (ns == null)  {
                String err = "getComplexType: ";
                err += "Namespace prefix not recognized '"+name+"'";
                throw new IllegalArgumentException(err);
            }
        }
        
        //-- Get GetComplexType object
        if ((ns==null) || (ns.equals(_targetNamespace)) ) {
            result = _complexTypes.get(canonicalName);
            if (result == null) {
            	boolean found = false;
            	//-- check for any included schemas that are cached
            	Iterator<Schema> cacheIncluded = _cachedincludedSchemas.values().iterator();
            	while (cacheIncluded.hasNext() && !found) {
            		Schema temp = cacheIncluded.next();
            		result = temp.getComplexType(canonicalName);
            		if (result != null)
            			found = true;
            	}
            	if (!found) {
	            	//--we might face a redefinition
	            	Iterator<RedefineSchema> redefinitions = getRedefineSchema().iterator();
	            	while (redefinitions.hasNext() && result == null) {
	            		RedefineSchema redefine = redefinitions.next();
	            		//-- the ComplexType can be redefined
	            		if (redefine.hasComplexTypeRedefinition(canonicalName)) {
	            			result = redefine.getComplexType(canonicalName);
	            			
	            			if (result != null)
	            				found = true;
	            			break;
	            		}
	            		
	            		Schema schema = redefine.getOriginalSchema();
		            	if (schema != null) {
                            //-- Sets the master schema
		            		//-- The master schema will help resolving the links at runtime between
		            		//-- an structure and its type.
		            		schema.setMasterSchema(this);
		            		result = schema.getComplexType(canonicalName);
		            	}
	            	}
            	}
            	
            }
        }
        else {
            Schema schema = getImportedSchema(ns);
            if (schema!=null) {
            	result = schema.getComplexType(canonicalName);
            }
        }

        return result;

    } //-- getComplexType

    /**
     * Returns an Enumeration of all top-level ComplexType declarations
     * @return an Enumeration of all top-level ComplexType declarations
    **/
    public Collection<ComplexType> getComplexTypes() {
    	Vector<ComplexType> result = new Vector<ComplexType>(_complexTypes.size()*2);
    	
    	result.addAll(_complexTypes.values());
    	
    	Collection<Schema> cachedincluded = _cachedincludedSchemas.values();
    	for (Schema includedSchema : cachedincluded) {
            Collection<ComplexType> tempEnum = includedSchema.getComplexTypes();
            for (ComplexType complexType : tempEnum) {
                result.add(complexType); 
            }
        }
    	
    	Collection<RedefineSchema> redefinition = _redefineSchemas.values();
    	for (RedefineSchema redefineSchema : redefinition) {
    		//1--  Add the ComplexType from the RedefineSchema
    		for (ComplexType complexType : redefineSchema.enumerateComplexTypes()) {
                result.add(complexType);
            }
    		
    		Schema tempSchema = redefineSchema.getOriginalSchema();
            //-- a redefinition doesn't always contain a schema
    		if (tempSchema != null) {
                //-- Sets the master schema
    			//-- The master schema will help resolving the links at runtime between
    			//-- an structure and its type.
    			tempSchema.setMasterSchema(this);
    			Iterator<ComplexType> tempEnum = tempSchema.getComplexTypes().iterator();
	    		while (tempEnum.hasNext()) {
	    			ComplexType tempType = tempEnum.next();
	    			if (!redefineSchema.hasComplexTypeRedefinition(tempType.getName()))
	    				result.add(tempType);	
	    		}
    		}
    	}
    	
    	return result;
    	
    } //-- getComplextypes

    /**
     * Returns the ElementDecl of associated with the given name
     * @return the ElementDecl of associated with the given name, or
     *  null if no ElementDecl with the given name was found.
    **/
    public ElementDecl getElementDecl(String name) {

        String ns = null;
        if (name == null) {
            String err = NULL_ARGUMENT + "getElementDecl: ";
            err += " 'name' can not be null";
            throw new IllegalArgumentException(err);
        }
        ElementDecl result = null;
        int idx = name.indexOf(':');
        if (idx >= 0)
        {
            String nsPrefix = name.substring(0,idx);
            name = name.substring(idx + 1);
            ns = _namespaces.getNamespaceURI(nsPrefix);
            if (ns == null)  {
                String err = "getElementDecl: ";
                err += "Namespace prefix not recognized '"+nsPrefix+"'";
                throw new IllegalArgumentException(err);
            }
        }

        if ((ns==null) || (ns.equals(_targetNamespace)) ) {
           result = _elements.get(name);
		   if (result == null) {
            	//-- check for any included schemas that are cached
            	Iterator<Schema> cacheIncluded = _cachedincludedSchemas.values().iterator();
            	boolean found = false;
            	while (cacheIncluded.hasNext() && !found) {
            		Schema temp = cacheIncluded.next();
            		result = temp.getElementDecl(name);
            		if (result != null)
            			found = true;
            	}
            	//--look in the redefinition
            	if (!found) {
            		Iterator<RedefineSchema> redefinition = _redefineSchemas.values().iterator();
            		while (redefinition.hasNext() && !found) {
            			Schema schema = redefinition.next().getOriginalSchema();
            			if (schema != null) {
                            //-- Sets the master schema
            				//-- The master schema will help resolving the links at runtime between
            				//-- an structure and its type.
            				schema.setMasterSchema(this);
            				result = schema.getElementDecl(name);
            				if (result != null)
            					found = true;	
            			}
            		}
             	}
		   }
        }
        else {
            Schema schema = getImportedSchema(ns);
            if (schema!=null) {
                result = schema.getElementDecl(name);
            }
        }

        return result;
    } //--getElementDecl

    /**
     * Returns an Enumeration of all top-level element declarations
     * @return an Enumeration of all top-level element declarations
    **/
    public Collection<ElementDecl> getElementDecls() {
    	Vector<ElementDecl> result = new Vector<ElementDecl>(_elements.size()*2);
    	
    	result.addAll(_elements.values());
    	
    	Collection<Schema> cachedincluded = _cachedincludedSchemas.values();
    	for (Schema includedSchema : cachedincluded) {
            Collection<ElementDecl> tempEnum = includedSchema.getElementDecls();
            for (ElementDecl elementDecl : tempEnum) {
                result.add(elementDecl);
            }
        }

    	Collection<RedefineSchema> redefinition = _redefineSchemas.values();
    	for (RedefineSchema redefineSchema : redefinition) {
    		Schema tempSchema = redefineSchema.getOriginalSchema();
            //-- a redefinition doesn't always contain a schema
    		if (tempSchema != null) {
                //-- Sets the master schema
    			//-- The master schema will help resolving the links at runtime between
    			//-- an structure and its type.
    			tempSchema.setMasterSchema(this);
    			Collection<ElementDecl> redefinedElementDeclarations = tempSchema.getElementDecls();
    			for (ElementDecl elementDecl : redefinedElementDeclarations) {
                    result.add(elementDecl);
                }
    		}
    	}
    	
    	return result;
    } //-- getElementDecls

    /**
     * Returns the elementFormDefault property of this Schema.
     *
     * @return the elementFormDefault property of this Schema, or null
     * if no default Form was set. If no default Form has been set, the
     * user should assume Form.Unqualified.
    **/
    public Form getElementFormDefault() {
        return _elementFormDefault;
    } //-- getElementFormDefault

    /**
     * Returns the default FinalList for this Schema.
     *
     * @return final the default FinalList for this Schema.
    **/
    public FinalList getFinalDefault() {
        return _final;
    } //-- getFinalDefault

    /**
     * Returns the SimpleType associated with the given name,
     * or null if no such SimpleType exists.
     *
     * @param name the name of the SimpleType. The name may
     * be a QName (contain a namespace prefix).
     * @return the SimpleType associated with the given name,
     * or null if no such SimpleType exists.
    **/
    public SimpleType getSimpleType(String name) {

        //-- name must not be null
        if (name == null)  {
            String err = NULL_ARGUMENT + "getSimpleType: ";
            err += "'name' cannot be null.";
            throw new IllegalArgumentException(err);
        }

        //-- Handle namespace resolution?
        String nsPrefix = "";
        String ns = null;
        int colon = name.indexOf(':');
        if (colon >= 0) {
            nsPrefix = name.substring(0,colon);
            name = name.substring(colon + 1);
            ns = _namespaces.getNamespaceURI(nsPrefix);
            if (ns == null)  {
                String err = "getSimpleType: ";
                err += "Namespace prefix not recognised '"+nsPrefix+"'";
                err += "for simpleType:"+name;
                throw new IllegalArgumentException(err);
            }
        }
        else {
        	ns = _namespaces.getNamespaceURI(nsPrefix);
        }
        
        //--if at this point, there is no namespace
        //--then we assume it is the targetNamespace
        if (ns == null)
            ns = _targetNamespace;
       
        return getSimpleType(name, ns);

    } //-- getSimpleType


    /**
     * Returns the SimpleType associated with the given name
     * and namespace, or null if no such SimpleType exists.
     *
     * @param name the name of the simpleType. It is an error
     * if this name contains a prefix, it must be an NCName.
     * @param namespace the namespace URI of the simpleType.
     * @return the SimpleType, or null if no such SimpleType exists.
    **/
    public SimpleType getSimpleType(String name, String namespace) {

        //-- name must not be null
        if (name == null)  {
            String err = NULL_ARGUMENT + "getSimpleType: ";
            err += "'name' cannot be null.";
            throw new IllegalArgumentException(err);
        }

        //--Is the declaration in the default namespace (if any)
        boolean isDefaultNS = false;
        if (namespace == null) {
            namespace = _namespaces.getNamespaceURI("");
            isDefaultNS = true;
        }
       
        //-- Get SimpleType object
        SimpleType result = null;
        if ((namespace == null) || (isDefaultNS)) {
            
            //-- first check user-defined types
            result = _simpleTypes.get(name);
            if (result != null) {
                //-- resolve deferred type if necessary
                if (result.getType() != result) {
                    //-- can result.getType ever return null?
                    //-- We can check, just in case.
                    if (result.getType() != null) {
                        result = (SimpleType)result.getType();
                        result.setParent(this);
                        _simpleTypes.put(name, result);
                    }
                }
            }
            //-- otherwise try built-in types
            else {
                result= simpleTypesFactory.getBuiltInType(name);
                //if we have a built-in type not declared in the good namespace -> Exception
                if ( (result != null) && (!_schemaNamespace.equals(namespace))) {
                    String err = "getSimpleType: the simple type '"+name+
                                "' has not been declared in XML Schema namespace.";
                    throw new IllegalArgumentException(err);
                }
            }
        }
        else if (namespace.equals(_schemaNamespace)) {
            result= simpleTypesFactory.getBuiltInType(name);
            if (result == null)  {
        		String err = "getSimpleType: the simple type '"+name+
                                "' is not a built-in type as defined in XML Schema specification.";
                    throw new IllegalArgumentException(err);
            }
        }
        else if (namespace.equals(_targetNamespace)) {
            result = _simpleTypes.get(name);
            if (result != null) {
                //-- resolve deferred type if necessary
                if (result.getType() != result) {
                    //-- can result.getType ever return null?
                    //-- We can check, just in case.
                    if (result.getType() != null) {
                        result = (SimpleType)result.getType();
                        result.setParent(this);
                        _simpleTypes.put(name, result);
                    }
                }
            }
            if (result == null) {
            	//-- check the cached included schema
        		Iterator<Schema> cacheIncluded = _cachedincludedSchemas.values().iterator();
        		boolean found = false;
        		while (cacheIncluded.hasNext() && !found) {
        			Schema temp = cacheIncluded.next();
        			result = temp.getSimpleType(name, namespace);
        			if (result != null)
        				found = true;
        		}
        	    if (!found) {
	            	//--we might face a redefinition
	            	Iterator<RedefineSchema> redefinitions = getRedefineSchema().iterator();
	            	while (redefinitions.hasNext() && result == null) {
                        RedefineSchema redefine = redefinitions.next();
	            		//-- the SimpleType can be redefined
	            		if (redefine.hasSimpleTypeRedefinition(name)) {
	            			result = redefine.getSimpleType(name);
	            			if (result != null)
	            				found = true;
	            			break;
	            		}
	            		
	            		Schema schema = redefine.getOriginalSchema();
	            		if (schema != null) {
	            			//-- Sets the master schema
	            			//-- The master schema will help resolving the links at runtime between
	            			//-- an structure and its type.
	            			schema.setMasterSchema(this);
	            			result = schema.getSimpleType(name, namespace);
	            		}
	            	}    
        	    }
            }
        }
        else {
            Schema schema = getImportedSchema(namespace);
            if (schema != null) {
                result = schema.getSimpleType(name, namespace);
            }
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
    public Collection<SimpleType> getSimpleTypes() {

        //-- clean up "deferred types" if necessary
        Enumeration<SimpleType> enumeration = _simpleTypes.elements();
        while(enumeration.hasMoreElements()) {
            SimpleType type = enumeration.nextElement();
            if (type != type.getType()) {
                //-- resolve deferred type if necessary
                if (type.getType() != null) {
                    String name = type.getName();
                    type = (SimpleType)type.getType();
                    type.setParent(this);
                    _simpleTypes.put(name, type);
                }
            }
        }
        Vector<SimpleType> result = new Vector<SimpleType>(_simpleTypes.size()*2);
        
        result.addAll(_simpleTypes.values());
        
        Collection<Schema> cachedincluded = _cachedincludedSchemas.values();
        for (Schema includedSchema : cachedincluded) {
            Collection<SimpleType> tempEnum = includedSchema.getSimpleTypes();
            for (SimpleType simpleType : tempEnum) {
                result.add(simpleType);
            }
        }
        
        Collection<RedefineSchema> redefinition = _redefineSchemas.values();
        for (RedefineSchema redefineSchema : redefinition) {
        	//1--  Add the SimpleType from the RedefineSchema
        	for (SimpleType simpleType : redefineSchema.enumerateSimpleTypes()) {
                result.add(simpleType);
            }
        	
        	Schema tempSchema = redefineSchema.getOriginalSchema();
            //-- a redefinition doesn't always contain a schema
        	if (tempSchema != null) {
        		//-- Sets the master schema
        		//-- The master schema will help resolving the links at runtime between
        		//-- an structure and its type.
        		tempSchema.setMasterSchema(this);
        		Collection<SimpleType> tempEnum = tempSchema.getSimpleTypes();
        		for (SimpleType simpleType : tempEnum) {
        		    if (!redefineSchema.hasSimpleTypeRedefinition(simpleType.getName())) {
                        result.add(simpleType);
                    }	
                }
        	}
        }
        
        return result;
    } //-- getSimpleTypes

    /**
     * Returns the schemaLocation hint provided of this schema
     * @return the schemaLocation hint provided of this schema
     */
     public String getSchemaLocation() {
         return _schemaLocation;
     }


     /**
     * Returns the ModeGroup of associated with the given name
     * @return the ModelGroup of associated with the given name, or
     *  null if no ModelGroup with the given name was found.
    **/
    public ModelGroup getModelGroup(String name) {

        String ns = null;
        if (name == null) {
            String err = NULL_ARGUMENT + "getModelGroup: ";
            err += " 'name' can not be null";
            throw new IllegalArgumentException(err);
        }
        ModelGroup result = null;
        int idx = name.indexOf(':');
        if (idx >= 0)
        {
            String nsPrefix = name.substring(0,idx);
            name = name.substring(idx + 1);
            ns = _namespaces.getNamespaceURI(nsPrefix);
            if (ns == null)  {
                String err = "getModelGroup: ";
                err += "Namespace prefix not recognized '"+nsPrefix+"'";
                throw new IllegalArgumentException(err);
            }
        }

        if ((ns==null) || (ns.equals(_targetNamespace)) ) {
            result = _groups.get(name);
            if (result == null) {
        		//-- check for any included schemas that are cached
        		Iterator<Schema> cacheIncluded = _cachedincludedSchemas.values().iterator();
        		boolean found = false;
        		while (cacheIncluded.hasNext() && !found) {
        			Schema temp = cacheIncluded.next();
        			result = temp.getModelGroup(name);
        			if (result != null)
        				found = true;
        		}
        	    
        		if (!found) {
	            	//--we might face a redefinition
	            	Iterator<RedefineSchema> redefinitions = getRedefineSchema().iterator();
	            	while (redefinitions.hasNext() && result == null) {
                        RedefineSchema redefine = redefinitions.next();
	            		//-- the ModelGroup can be redefined
	            		if (redefine.hasGroupRedefinition(name)) {
	            			result = redefine.getModelGroup(name);
	            			if (result != null)
	            				found = true;
	            			break;
	            		}
	            		
	            		Schema schema = redefine.getOriginalSchema();
	            		if (schema != null) {
	            			//-- Sets the master schema
	            			//-- The master schema will help resolving the links at runtime between
	            			//-- an structure and its type.
	            			schema.setMasterSchema(this);
	            		    result = schema.getModelGroup(name);
	            		}
	            	}    
        		}
            }
        }
        else {
            Schema schema = getImportedSchema(ns);
            if (schema!=null) {
                result =  schema.getModelGroup(name);
            }
        }

        return result;
    } //--getModelGroup

    /**
     * Returns an Enumeration of all top-level ModelGroup declarations
     * @return an Enumeration of all top-level ModelGroup declarations
    **/
    public Collection<ModelGroup> getModelGroups() {
    	Vector<ModelGroup> result = new Vector<ModelGroup>(_groups.size()*2);
    	
    	result.addAll(_groups.values());
   	
    	Collection<Schema> cachedincluded = _cachedincludedSchemas.values();
    	for (Schema includedSchema : cachedincluded) {
            Collection<ModelGroup> tempEnum = includedSchema.getModelGroups();
            for (ModelGroup modelGroup : tempEnum) {
                result.add(modelGroup);
            }
        }

    	Collection<RedefineSchema> redefinition = _redefineSchemas.values();
    	for (RedefineSchema redefineSchema : redefinition) {
    		//1--  Add the AttributeGroups from the RedefineSchema
    		for (ModelGroup modelGroup : redefineSchema.enumerateGroups()) {
                result.add(modelGroup);
            }
    		
    		Schema tempSchema = redefineSchema.getOriginalSchema();
           //-- a redefinition doesn't always contain a schema
    		if (tempSchema != null) {	
    			//-- Sets the master schema
    			//-- The master schema will help resolving the links at runtime between
    			//-- an structure and its type.
    			tempSchema.setMasterSchema(this);
    			
    			for (ModelGroup modelGroup : tempSchema.getModelGroups()) {
    			    if (!redefineSchema.hasGroupRedefinition(modelGroup.getName())) 			
    			        result.add(modelGroup);	
                }
    		}
    	}
    	
    	return result;
    } //-- getmodelGroup

    /**
     * Returns the Id for this Schema, as specified by the
     * Id attribute, or null if no Id exists.
     *
     * @return the Id for this Scheam, or null if no Id exists
    **/
    public String getId() {
        return _id;
    } //-- getId

    /**
     * Returns the imported schemas of this schema
     * @return the hashtable of the imported schemas
     */
     public Collection<Schema> getImportedSchema() {
         return _importedSchemas.values();
     }

    /**
     * Returns the imported schema with the given namespace
     * 
     * @param ns the namespace of the imported schema to return
     * @return the imported schema
     */
    public Schema getImportedSchema(String ns) {
        return getImportedSchema(ns, null);
    } //-- getImportedSchema
    
    /**
     * Returns an enumeration of redefined schemas.
     * @return an enumeration of redefined schemas.
     */
    public Collection<RedefineSchema> getRedefineSchema() {
    	return _redefineSchemas.values();
    }
    
    /**
     * Returns the redefined schema corresponding schemaLocation.
     * @param schemaLocation the string corresponding to the schemaLocation.
     * @return the redefined schema corresponding schemaLocation.
     */
    public RedefineSchema getRedefineSchema(String schemaLocation) {
    	RedefineSchema result = _redefineSchemas.get(schemaLocation);
    	return result;
    }
    
    /**
     * Returns the cached included schema with the given SchemaLocation
     * 
     * @param schemaLocation the schemaLocation value used as a key to store the 
     * cached included XML schema
     * @return the cached included XML schema
     */
    public Schema getCachedIncludedSchema(String schemaLocation) {
    		return _cachedincludedSchemas.get(schemaLocation);
    } //-- getCachedIncludedSchema
    
    /**
     * Returns an enumeration of all the included schemas that are cached 
     * in this XML Schema Definition.
     * 
     * @return an enumeration of all the included schemas that are cached 
     * in this XML Schema Definition.
     * 
     */
    public Collection<Schema> getCachedIncludedSchemas() {
    	return _cachedincludedSchemas.values();
    }    
    /**
     * Returns the imported schema with the given namespace
     * 
     * @param ns the namespace of the imported schema to return
     * @param localOnly a boolean that indicates only local imports
     * should be searched.
     * @return the imported schema
     */
    public Schema getImportedSchema(String ns, boolean localOnly) {
        if (localOnly) {
            return _importedSchemas.get(ns);
        }
        return getImportedSchema(ns, null);
    } //-- getImportedSchema
    
    /**
     * Returns the imported schema with the given namespace
     * 
     * @param ns the namespace of the imported schema to return
     * @return the imported schema
     */
    private Schema getImportedSchema(String ns, Schema caller) {
        
        //-- Check for recursive calls
        if (caller == this) {
            return null;
        }
        //-- Associate caller if necessary
        if (caller == null) {
            caller = this;
        }
        
        Schema result = _importedSchemas.get(ns);
        //--maybe we are the schema imported is at
        //--a depth > 1
        if (result == null) {
            Iterator<Schema> schemas = _importedSchemas.values().iterator();
            while (schemas.hasNext()) {
                Schema temp = schemas.next();
                result = temp.getImportedSchema(ns, caller);
                if (result != null) {
                    break;
                }
           }
        }
        return result;
    }

    /**
     * Returns the namespace associated with the given prefix.
     *
     * @return the namespace associated with the given prefix,
     * or null if no associated namespace exists.
     */
    public final String getNamespace(String prefix) {
        if (prefix == null) prefix = "";
        return _namespaces.getNamespaceURI(prefix);
    } //-- getNamespace

    /**
     * Returns the namespaces declared for this Schema
     *
     * @return the namespaces declared for this Schema
     */
    public Namespaces getNamespaces() {
        return _namespaces;
    } //-- getNamespaces



    /**
     * Indicates that the given XML Schema file has been processed via an <xs:include>
     */
    public void addInclude(String include)
    {
        _includedSchemas.addElement(include);
    } //-- addInclude

    /**
     * Returns True if the given XML Schema has already been included via <xs:include>
     * @return True if the file specified has already been processed
     */
    public boolean includeProcessed(String includeFile)
    {
        return _includedSchemas.contains(includeFile);
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
        return _schemaNamespace;
    } //-- getSchemaNamespace

    /**
     * Returns the target namespace for this Schema, or null if no
     * namespace has been defined.
     * @return the target namespace for this Schema, or null if no
     * namespace has been defined
    **/
    public String getTargetNamespace() {
        return this._targetNamespace;
    } //-- getTargetNamespace


    /**
     * Returns the version information of the XML Schema definition
     * represented by this Schema instance.
     *
     * @return the version information of the XML Schema
     * definition, or null if no version information exists.
    **/
    public String getVersion() {
        return _version;
    } //-- getVersion

    /**
     * Returns True if the namespace is known to this schema
     * @param namespaceURL the namespace URL
     * @return True if the namespace was declared in the schema
     */
    public boolean isKnownNamespace(String namespaceURL)
    {
        return (_namespaces.getNamespacePrefix(namespaceURL) != null);
    }

    /**
     * Removes the given top level ComplexType from this Schema
     * @param complexType the ComplexType to remove
     * @return true if the complexType has been removed, or
     * false if the complexType wasn't top level or
     * didn't exist in this Schema
    **/
    public boolean removeComplexType(ComplexType complexType) {
        boolean result = false;
    	if (complexType.isTopLevel()) {
            if (_complexTypes.containsValue(complexType)) {
                _complexTypes.remove(complexType.getName());
                complexType.setParent(null);
                result = true;
            }
            if (!result) {
	            //--check the cached included schemas
	            Iterator<Schema> cacheIncluded = _cachedincludedSchemas.values().iterator();
	            while (cacheIncluded.hasNext() && !result) {
	                Schema temp = cacheIncluded.next();
	                result = temp.removeComplexType(complexType);
	            }
	            //--Still false?
	            if (!result) {
		            //--check the redefinition
		            Iterator<RedefineSchema> redefinitions = getRedefineSchema().iterator();
		            while (redefinitions.hasNext() && !result) {
		            	RedefineSchema redefine = redefinitions.next();
		            	result = redefine.removeComplexType(complexType);
		            }
	            }
            }
    	}
        return result;
    } //-- removeComplexType

    /**
     * Removes the given top level Element from this Schema
     * @param element the ElementDecl to remove
     * @return true if the ElementDecl has been removed, or
     * false if the ElementDecl wasn't top level or
     * didn't exist in this Schema
    **/
    public boolean removeElement(ElementDecl element) {
        boolean result = false;
    	if (_elements.containsValue(element)) {
            _elements.remove(element.getName());
            result = true;
        }
    	if (!result) {
    		//--check the cached included schemas
    		Iterator<Schema> cacheIncluded = _cachedincludedSchemas.values().iterator();
    		while (cacheIncluded.hasNext() && !result) {
    			Schema temp = cacheIncluded.next();
    			result = temp.removeElement(element);
    		}
    	}
        return result;
    } //-- removeElement


    /**
     * Removes the given top level Attribute from this Schema
     * @param attribute the AttributeDecl to remove
     * @return true if the AttributeDecl has been removed, or
     * false if the AttributeDecl wasn't top level or
     * didn't exist in this Schema
     */
    public boolean removeAttribute(AttributeDecl attribute) {
        boolean result = false;
    	if (_attributes.containsValue(attribute)) {
            _attributes.remove(attribute.getName());
            result = true;
        }
    	if (result == false) {
    		//--check the cached included schemas
    		Iterator<Schema> cacheIncluded = _cachedincludedSchemas.values().iterator();
    		while (cacheIncluded.hasNext() && !result) {
    			Schema temp = cacheIncluded.next();
    			result = temp.removeAttribute(attribute);
    		}
    	}
        return false;
    } //-- removeAttribute

    /**
     * Removes the given top level ModelGroup definition from this Schema
     * @param group the ModelGroup definition to remove
     * @return true if the ModelGroup definition has been removed, or
     * false if the ModelGroup definition wasn't top level or
     * didn't exist in this Schema.
     */
    public boolean removeGroup(ModelGroup group) {
        boolean result = false;
    	if (_groups.containsValue(group)) {
            _groups.remove(group.getName());
            result = true;
        }
    	if (!result) {
    		//--check the cached included schemas
    		Iterator<Schema> cacheIncluded = _cachedincludedSchemas.values().iterator();
    		while (cacheIncluded.hasNext() && !result) {
    			Schema temp = cacheIncluded.next();
    			result = temp.removeGroup(group);
    		}
    		
    		if (!result) {
	    		//--check the redefinition
	    		Iterator<RedefineSchema> redefinitions = getRedefineSchema().iterator();
	    		while (redefinitions.hasNext() && !result) {
	    			RedefineSchema redefine = redefinitions.next();
	    			result = redefine.removeGroup(group);
	    		}
    		}
    	}
    	
       
        return result;
    } //-- removeGroup
    
    /**
     * Removes the given AttributeGroup definition from this Schema
     * @param group the AttributeGroup definition to remove
     * @return true if the AttributeGroup definition has been removed.
     */
    public boolean removeAttributeGroup(AttributeGroupDecl group) {
    	boolean result = false;
    	if (_attributeGroups.containsValue(group)) {
    		_attributeGroups.remove(group.getName());
    		result = true;
    	}
    	if (result == false) {
    		//--check the cached included schemas
    		Iterator<Schema> cacheIncluded = _cachedincludedSchemas.values().iterator();
    		while (cacheIncluded.hasNext() && !result) {
    			Schema temp = cacheIncluded.next();
    			result = temp.removeAttributeGroup(group);
    		}
    		
    		if (!result) {
	    		//--check the redefinition
	    		Iterator<RedefineSchema> redefinitions = getRedefineSchema().iterator();
	    		while (redefinitions.hasNext() && !result) {
	    			RedefineSchema redefine = redefinitions.next();
	    			result = redefine.removeAttributeGroup(group);
	    		}
    		}
    	}
        
    	return result;
    } //-- removeGroup
    
    /**
     * Removes the given cached included schema from this Schema definition's 
     * list of cached included schema.
     *
     * @param schema the Schema to remove from this Schema's redefinition list
     *
     * @return true if the Schema was removed, otherwise false
     */
    public synchronized boolean removeCachedIncludedSchema(Schema schema)
	{
    	if (schema == null) return false;
    	String schemaLocation = schema.getSchemaLocation();
    	Schema tmp = _cachedincludedSchemas.get(schemaLocation);
    	if (schema.equals(tmp)) {
    		_cachedincludedSchemas.remove(schemaLocation);
    		return true;
    	}
    	return false;
    } //-- removeImportedSchema

    /**
     * Removes the given Schema definition from this Schema definition's 
     * list of imported schenma
     *
     * @param schema the Schema to remove from this Schema's import list
     *
     * @return true if the Schema was removed, otherwise false
     */
    public synchronized boolean removeImportedSchema(Schema schema)
    {
        if (schema == null) return false;
        String targetNamespace = schema.getTargetNamespace();
        if (targetNamespace == null) targetNamespace = "";
        Schema tmp = _importedSchemas.get(targetNamespace);
        if (schema.equals(tmp)) {
            _importedSchemas.remove(targetNamespace);
            return true;
        }
        return false;
    } //-- removeImportedSchema
    
    /**
     * Removes the namespace from the set of namespace declarations for 
     * this Schema definition.
     *
     * @param prefix the namespace prefix of the namespace to remove.
     */
    public boolean removeNamespace(String prefix) {
        if (prefix == null) prefix = "";
        return _namespaces.removeNamespace(prefix);
    } //-- removeNamespace
    
    /**
     * Removes the given redefined structure from this Schema definition's 
     * list of redefinitions.
     *
     * @param schema the Schema to remove from this Schema's redefinition list
     *
     * @return true if the Schema was removed, otherwise false
     */
    public synchronized boolean removeRedefineSchema(RedefineSchema schema)
	{
    	if (schema == null) return false;
    	String schemaLocation = schema.getSchemaLocation();
    	RedefineSchema tmp = _redefineSchemas.get(schemaLocation);
    	if (schema.equals(tmp)) {
    		_redefineSchemas.remove(schemaLocation);
    		return true;
    	}
    	return false;
    } //-- removeRedefineSchema
    /**
     * Removes the given top level SimpleType from this Schema
     * @param simpleType the SimpleType to remove
     * @return true if the SimpleType has been removed, or
     * false if the SimpleType wasn't top level or
     * didn't exist in this Schema
    **/
    public boolean removeSimpleType(SimpleType simpleType) {
        boolean result = false;
    	if (_simpleTypes.containsValue(simpleType)) {
            _simpleTypes.remove(simpleType.getName());
            result = true;
        }
    	if (result == false) {
    		//--check the cached included schemas
    		Iterator<Schema> cacheIncluded = _cachedincludedSchemas.values().iterator();
    		while (cacheIncluded.hasNext() && !result) {
    			Schema temp = cacheIncluded.next();
    			result = temp.removeSimpleType(simpleType);
    		}
    		
    		if (!result) {
	            //--check the redefinition
	    		Iterator<RedefineSchema> redefinitions = getRedefineSchema().iterator();
	    		while (redefinitions.hasNext() && !result) {
	    			RedefineSchema redefine = redefinitions.next();
	    			result = redefine.removeSimpleType(simpleType);
	    		}
    		}
    	}    	
        return result;
    } //-- removeSimpleType

    /**
     * Sets the attributeFormDefault property of this Schema.
     *
     * @param attributeFormDefault the Form value of the attributeFormDefault
     * property for this Schema.
    **/
    public void setAttributeFormDefault(Form attributeFormDefault) {
        _attributeFormDefault = attributeFormDefault;
    } //-- setAttributeFormDefault

    /**
     * Sets the default BlockList for this Schema.
     *
     * @param block the default BlockList to set for this Schema.
    **/
    public void setBlockDefault(BlockList block) {
        _block = block;
    } //-- setBlockDefault

    /**
     * Sets the default Block values for this Schema.
     *
     * @param block the default Block values to set for this Schema.
    **/
    public void setBlockDefault(String block) {
        _block = new BlockList(block);
    } //-- setBlockDefault

    /**
     * Sets the elementFormDefault property of this Schema.
     *
     * @param elementFormDefault the Form value of the elementFormDefault
     * property for this Schema.
    **/
    public void setElementFormDefault(Form elementFormDefault) {
        _elementFormDefault = elementFormDefault;
    } //-- setElementFormDefault

    /**
     * Sets the default FinalList for this Schema.
     *
     * @param finalList the default FinalList to set for this Schema.
    **/
    public void setFinalDefault(FinalList finalList) {
        _final = finalList;
    } //-- setFinalDefault

    /**
     * Sets the default final values for this Schema.
     *
     * @param finalValues the default final values to set for this Schema.
    **/
    public void setFinalDefault(String finalValues) {
        _final = new FinalList(finalValues);
    } //-- setFinalDefault

    /**
     * Set the schemaLocation for this schema. This is useful
     * when this schema has been imported by another schema
     * @param schemaLocation the location hint for this Schema
     */
     public void setSchemaLocation(String schemaLocation) {
         _schemaLocation = schemaLocation;
     }


    /**
     * Returns the first simple or complex type which name equals TypeName
     */
    public XMLType getType(String typeName)
    {
        //-- Null?
        if (typeName == null)  {
            String err = NULL_ARGUMENT + "Schema#getType: ";
            err += "'name' cannot be null.";
            throw new IllegalArgumentException(err);
        }

        XMLType result = null;
        
        String localName = typeName;
        String prefix = "";
        String ns = null;
        
        int colon = typeName.indexOf(':');
        
        if (colon >= 0) {
            localName = typeName.substring(colon + 1);
            prefix = typeName.substring(0,colon);
            ns = _namespaces.getNamespaceURI(prefix);
            if (ns == null)  {
                String err = "Schema#getType: ";
                err += "Namespace prefix not recognised '"+typeName+"'";
                throw new IllegalArgumentException(err);
            }
        }
        
        //-- use default namespace if necessary
        if (ns == null) {
            ns = _namespaces.getNamespaceURI(prefix);
        }
       
        //--if at this point, there is no namespace
        //--then we assume it is the targetNamespace
        if (ns == null) {
            //--SHOULD WE THROW AN EXCEPTION?
        	ns = _targetNamespace;
        }
        
        //1--support for anyType
        if (localName.equals(SchemaNames.ANYTYPE)) {
            //--if 'anyType' in the default schema namespace-->type is anyType
            if (ns.equals(DEFAULT_SCHEMA_NS)) {
                result = new AnyType(this);
            }
        }//--anyType

        IllegalArgumentException exception = null;
        //2--look for a simpleType
        if (result == null) {
            try {
                result= getSimpleType(localName, ns);
            } catch (IllegalArgumentException iox) {
                exception = iox;
            }
        }

        //3--look for a complexType
        if (result == null) {
            try {
                result = getComplexType(typeName);
            } catch (IllegalArgumentException iox) {
                exception = iox;
            }
        }

        if ((result == null) && (exception != null))
            throw exception;

        return result;
    }

    /**
     * Sets the Id for this Schema
     *
     * @param id the Id for this Schema
    **/
    public void setId(String id) {
        this._id = id;
    } //-- setId

    /**
     * Sets the target namespace for this Schema
     * @param targetNamespace the target namespace for this Schema
     * @see "&sect; 2.7 XML Schema Part 1: Structures"
    **/
    public void setTargetNamespace(String targetNamespace) {
        if (targetNamespace != null) {
            targetNamespace = targetNamespace.trim();
            if (targetNamespace.length() == 0)
                throw new IllegalStateException("an empty string is not a valid namespace.");
        }
        _targetNamespace = targetNamespace;
    } //-- setTargetNamespace

    /**
     * Sets the version information for the XML Schema defintion
     * represented by this Schema instance.
     *
     * @param version the version for this XML Schema defination.
    **/
    public void setVersion(String version) {
        _version = version;
    } //-- setVersion


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
     * Checks the validity of this {@link Schema} definition.
     *
     * @throws ValidationException when this {@link Schema} definition
     * is invalid.
    **/
    public void validate() throws ValidationException {

        //-- Note: This method needs to be completed.

        //-- top-level complexTypes
        for (ComplexType complexType : _complexTypes.values()) {
            complexType.validate();
        }

        //-- top-level simpleTypes
        for (SimpleType simpleType : _simpleTypes.values()) {
            simpleType.validate();
        }

        //-- top-level elements
        for (ElementDecl element : _elements.values()) {
           element.validate();
        }

        //-- top-level attributes
        for (AttributeDecl attribute : _attributes.values()) {
            attribute.validate();
        }

        //-- top-level groups
        for (ModelGroup group : _groups.values()) {
            group.validate();
        }
        //-- top-level attribute groups

    } //-- validate


    //-- private methods:

    /**
     * Returns the namespace prefix associated with the
     * given namespace. If more than one prefix has been
     * associated, the first one found will be returned.
     *
     * @return the namespace prefix associaed with the
     * given namespace.
    **/
    public String getNamespacePrefix(String namespace) {
        return _namespaces.getNamespacePrefix(namespace);
    }
    
    /**
     * Returns the master schema in which this instance of schema
     * is used at runtime. This method is meant to be used at runtime.
     * 
     * @return  the master schema in which this instance of schema
     * is used at runtime. This method is meant to be used at runtime.
     */
     protected Schema getMasterSchema() {
     	return _masterSchema;
     }
     
     /**
      * Sets the master schema in which this instance of schema
      * is used at runtime. This method is meant to be used at runtime.
      * A master schema is a schema in which this instance of Schema is
      * included, redefined or imported.
      * 
      * @param masterSchema the master schema in which this instance of schema
      * is used at runtime. This method is meant to be used at runtime.
      */
     protected void setMasterSchema(Schema masterSchema) {
     	_masterSchema = masterSchema;
     }
     

} //-- Schema


