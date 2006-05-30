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
 * Copyright 2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.net.*;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.xml.sax.*;

/**
 * The purpose of this class is to read redefined elements in an XML schema.
 * The following xml schema structure can be redefined:
 * <ul>
 *     <li>Complextypes</li>
 *     <li>Simpletypes</li>
 *     <li>AttributeGroup</li>
 *     <li>Group</li>
 * </ul>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 **/
public class RedefineUnmarshaller extends ComponentReader
{

	/**
	 * The current ComponentReader used to read nested structures
	 **/
	private ComponentReader _unmarshaller;
	
	/**
	 * The current branch depth
	 **/
	private int _depth = 0;
	
	/**
	 * The parent XML schema
	 */
	private Schema _schema;
	
	/**
	 * The imported XML Schema
	 */
	private Schema _importedSchema;
	
	private RedefineSchema _redefineSchema;
	
	/**
	 * The XML Schema imported
	 */
	
    public RedefineUnmarshaller
        (Schema schema, AttributeSet atts, Resolver resolver, URIResolver uriResolver, Locator locator, SchemaUnmarshallerState state)
		throws XMLException
    {
        super();
        if (schema == null) {
        	String err = SchemaNames.REDEFINE + " must be used with an existing parent XML Schema.";
        	throw new SchemaException(err);
        }
        setResolver(resolver);
        setURIResolver(uriResolver);

        URILocation uri = null;
		//-- Get schemaLocation
		String schemaLocation = atts.getValue(SchemaNames.SCHEMALOCATION_ATTR);
		_schema = schema;
		
		if (schemaLocation == null) {
		    //-- <redefine/> or <redefine> <annotation>(*) </redefine>
		    _redefineSchema = new RedefineSchema(schema);
		    _schema.addRedefineSchema(_redefineSchema);
			return; 
		}

        
        if (schemaLocation.indexOf("\\") != -1) {
            String err = "'" + schemaLocation + 
                "' is not a valid URI as defined by IETF RFC 2396.";
            err += "The URI mustn't contain '\\'.";
            error(err);
	    }

        try {
            String documentBase = locator.getSystemId();
            if (documentBase != null) {
                if (!documentBase.endsWith("/"))
                    documentBase = documentBase.substring(0, documentBase.lastIndexOf('/') +1 );
            }
	        uri = getURIResolver().resolve(schemaLocation, documentBase);
            if (uri != null) {
                schemaLocation = uri.getAbsoluteURI();
            }
        } 
        catch (URIException urix) {
            throw new XMLException(urix);
        }

        //-- Schema object to hold import schema
		boolean addSchema = false;
		_redefineSchema = schema.getRedefineSchema(schemaLocation);
		Schema importedSchema = null;
		
		boolean alreadyLoaded = false;
		
        //-- The schema is not yet loaded
		if (_redefineSchema == null) {
            if (uri instanceof SchemaLocation) {
                importedSchema = ((SchemaLocation)uri).getSchema();
                //-- set the main schema in order to handle 
                //-- redefinition at runtime
                
                // importedSchema.addMainSchema(schema);
                
                _redefineSchema = new RedefineSchema(schema, importedSchema);
			    schema.addRedefineSchema(_redefineSchema);
			    alreadyLoaded = true;
            }
            else {
			    importedSchema = new Schema();
			    addSchema = true;
			}
		}
		else {
			//-- check schema location, if different, allow merge
		    String tmpLocation = _redefineSchema.getOriginalSchema().getSchemaLocation();
		    alreadyLoaded = schemaLocation.equals(tmpLocation);
		}

        state.markAsProcessed(schemaLocation, importedSchema);

        if (alreadyLoaded) return;
        
        //-- Parser Schema
		Parser parser = null;
		try {
		    parser = state.getConfiguration().getParser();
		}
		catch(RuntimeException rte) {}
		if (parser == null) {
		    throw new SchemaException("Error failed to create parser for import");
		}
		//-- Create Schema object and setup unmarshaller
		SchemaUnmarshaller schemaUnmarshaller = new SchemaUnmarshaller(state);
		schemaUnmarshaller.setURIResolver(getURIResolver());
		schemaUnmarshaller.setSchema(importedSchema);
		Sax2ComponentReader handler = new Sax2ComponentReader(schemaUnmarshaller);
		parser.setDocumentHandler(handler);
		parser.setErrorHandler(handler);

		try {
		    InputSource source = new InputSource(uri.getReader());
            source.setSystemId(uri.getAbsoluteURI());
            parser.parse(source);
		}
		catch(java.io.IOException ioe) {
		    throw new SchemaException("Error reading import file '"+schemaLocation+"': "+ ioe);
		}
		catch(org.xml.sax.SAXException sx) {
		    throw new SchemaException(sx);
		}
		
		//-- namespace checking
		String namespace = importedSchema.getTargetNamespace();
		if ( namespace != null ) {
			//-- Make sure targetNamespace is not the same as the
			//-- importing schema, see section 4.2.2 in the
			//-- XML Schema Recommendation
			if (!namespace.equals(schema.getTargetNamespace()) ) {
			    String err = "The 'namespace' attribute in the <redefine> element must be the same of the targetNamespace of the global schema.\n"
			    	         +namespace+" is different from:"+schema.getTargetNamespace();
			    error (err);
			}
		} else {
			importedSchema.setTargetNamespace(schema.getTargetNamespace());
		}
		
        //-- set the main schema in order to handle 
		//-- redefinition at runtime
		
		// importedSchema.addMainSchema(schema);
		
		_importedSchema = importedSchema;
		_redefineSchema = new RedefineSchema(schema, _importedSchema);
		//-- Add schema to list of redefine schemas (if not already present)
		if (addSchema)
		{
            importedSchema.setSchemaLocation(schemaLocation);
            _schema.addRedefineSchema(_redefineSchema);
		}
	}

    /**
     * Signals the start of an element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element. This may be null.
     * Note: A null namespace is not the same as the default namespace unless
     * the default namespace is also null.
     * @param atts the AttributeSet containing the attributes associated
     * with the element.
     * @param nsDecls the namespace declarations being declared for this
     * element. This may be null.
     **/
    public void startElement(String name, String namespace, AttributeSet atts,
    		Namespaces nsDecls)
	throws XMLException
	{

    	
    	
    	//-- DEBUG
    	//System.out.println("#startElement: " + name + " {" + namespace + "}");
    	//-- /DEBUG

    	//-- Do delagation if necessary
    	if (_unmarshaller != null) {
    		try {
    			_unmarshaller.startElement(name, namespace, atts, nsDecls);
    			_depth ++;
    			return;
    		} catch(RuntimeException rtx) {
    			error(rtx);
    		}
    	}
    	
    	//-- <annotation>
    	if (name.equals(SchemaNames.ANNOTATION)) {
    		_unmarshaller = new AnnotationUnmarshaller(atts);
    	}
    	//-- <attributeGroup>
    	else if (name.equals(SchemaNames.ATTRIBUTE_GROUP)) {
    		_unmarshaller = new AttributeGroupUnmarshaller(_schema, atts);
    	}
    	//-- <complexType>
    	else if (name.equals(SchemaNames.COMPLEX_TYPE)) {
    		_unmarshaller
			= new ComplexTypeUnmarshaller(_schema, atts, getResolver());
    	}
    	//-- <simpleType>
    	else if (name.equals(SchemaNames.SIMPLE_TYPE)) {
    		_unmarshaller = new SimpleTypeUnmarshaller(_schema, atts);
    	}
    	//-- <group>
    	else if (name.equals(SchemaNames.GROUP)) {
    		_unmarshaller = new ModelGroupUnmarshaller(_schema, atts, getResolver());
    	}
    	else {
    		//--Exception here
    		String err = "<" + name +"> elements cannot be used in a redefine.";
            error(err);
    	}

    	_unmarshaller.setDocumentLocator(getDocumentLocator());

    } //-- startElement

    /**
     * Signals to end of the element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element.
     **/
    public void endElement(String name, String namespace)
	throws XMLException
	{
    	
    	//-- DEBUG
    	//System.out.println("#endElement: " + name + " {" + namespace + "}");
    	//-- /DEBUG

    	//-- Do delagation if necessary
    	if ((_unmarshaller != null) && (_depth > 0)) {
    		_unmarshaller.endElement(name, namespace);
    		--_depth;
    		return;
    	}


    	//-- use internal JVM String
    	name = name.intern();


    	//-- check for name mismatches
    	if ((_unmarshaller != null)) {
    		if (!name.equals(_unmarshaller.elementName())) {
    			String err = "error: missing end element for ";
    			err += _unmarshaller.elementName();
    			error(err);
    		}
    	}
    	else {
    		String err = "error: missing start element for " + name;
    		throw new SchemaException(err);
    	}

    	//-- call unmarshaller.finish() to perform any necessary cleanup
    	_unmarshaller.finish();

    	//-- <annotation>
    	if (name.equals(SchemaNames.ANNOTATION)) {
    		_redefineSchema.addAnnotation((Annotation)_unmarshaller.getObject());
    	}
    	//-- <attributeGroup>
        else if (name.equals(SchemaNames.ATTRIBUTE_GROUP)) {
        	if (_redefineSchema.getSchemaLocation() == "") {
        		String err = "In a <redefine>, only annotations can be defined when no -schemaLocation- is specified.";
        		error(err);
        	}
        	
        	AttributeGroupDecl group = null;
        	group = (AttributeGroupDecl)(((AttributeGroupUnmarshaller)_unmarshaller).getAttributeGroup());
        	
        	if (!(group instanceof AttributeGroupDecl)) {
        		String err = "A redefinition of an AttributeGroup must be an attributeGroup declaration and not a reference.";
        		error(err);
        	}
        	
        	String structureName = group.getName();
        	if (structureName == null) {
        		String err = "When redefining an AttributeGroup, the group must have a name.\n";
        		error(err);
        	}
        	
        	//1-- the attributeGroup must exist in the imported schema
        	AttributeGroup original = _importedSchema.getAttributeGroup(structureName);
        	if (original == null) {
        		String err = "When redefining an AttributeGroup, the AttributeGroup must be present in the imported XML schema.\n"
        			+"AttributeGroup: "+structureName+" is not defined in XML Schema:" + _importedSchema.getSchemaLocation();
        		error(err);
        	}
        	
        	//-- todo: add code to check the Derivation Valid (Restriction, Complex) constraint.
        	group.setRedefined();
    		_redefineSchema.addAttributeGroup(group);
    		
    	}
    	//-- <complexType>
    	else if (name.equals(SchemaNames.COMPLEX_TYPE)) {
    		if (_redefineSchema.getSchemaLocation() == "") {
    			String err = "In a <redefine>, only annotations can be defined when no -schemaLocation- is specified.";
    			error(err);
    		}
    		ComplexType complexType = null;
    		complexType = ((ComplexTypeUnmarshaller)_unmarshaller).getComplexType();
    		//-- Checks that the complexType exists in the imported schema
    		String structureName = complexType.getName();
    		if (structureName == null) {
    			String err = "When redefining a complexType, the complexType must have a name.\n";
    			error(err);
    		}
    		
    		//1-- the complexType must exist in the imported schema
    		ComplexType original = _importedSchema.getComplexType(structureName);
    		if (original == null) {
    		    String err = "When redefining a complexType, the complexType must be present in the imported XML schema.\n"
    		    	         +"ComplexType: "+structureName+" is not defined in XML Schema:" + _importedSchema.getSchemaLocation();
    		    error(err);
    		}
    		
    		//2-- the base type must be itself
    		XMLType baseType = complexType.getBaseType();
    		//--just check the names since a top level complexType can only be defined once.
    		if (baseType == null || !baseType.getName().equals(structureName)) {
    			String err = "When redefining a complexType, the complexType must use itself as the base type definition.\n"
    				+"ComplexType: "+structureName+" uses:" + baseType+ " as its base type.";
    			error(err);
    		}
    		
    		complexType.setRedefined();
    		_redefineSchema.addComplexType(complexType);
            getResolver().addResolvable(complexType.getReferenceId(), complexType);
    	}
    	//-- <simpleType>
    	else if (name.equals(SchemaNames.SIMPLE_TYPE)) {
    		if (_redefineSchema.getSchemaLocation() == "") {
    			String err = "In a <redefine>, only annotations can be defined when no -schemaLocation- is specified.";
    			error(err);
    		}
    		
    		SimpleType simpleType = null;
    		simpleType = ((SimpleTypeUnmarshaller)_unmarshaller).getSimpleType();
            //-- Checks that the simpleType exists in the imported schema
    		String structureName = simpleType.getName();
    		if (structureName == null) {
    			String err = "When redefining a simpleType, the simpleType must have a name.\n";
    			error(err);
    		}
    		
    		//1-- the simpleType must exist in the imported schema
    		SimpleType original = _importedSchema.getSimpleType(structureName,_schema.getTargetNamespace() );
    		if (original == null) {
    			String err = "When redefining a simpleType, the simpleType must be present in the imported XML schema.\n"
    				+"SimpleType: "+structureName+" is not defined in XML Schema:" + _importedSchema.getSchemaLocation();
    			error(err);
    		}
    		
    		//2-- the base type must be itself
    		XMLType baseType = simpleType.getBaseType();
    		//--just check the names since a top level complexType can only be defined once.
    		if (!baseType.getName().equals(structureName)) {
    			String err = "When redefining a simpleType, the simpleType must use itself as the base type definition.\n"
    				+"SimpleType: "+structureName+" uses:" + baseType.getName() + " as its base type.";
    			error(err);
    		}	
    		
    		simpleType.setRedefined();
    		_redefineSchema.addSimpleType(simpleType);
    		getResolver().addResolvable(simpleType.getReferenceId(), simpleType);
    	}
    	//--<group>
    	else if (name.equals(SchemaNames.GROUP)) {
    		if (_redefineSchema.getSchemaLocation() == "") {
    			String err = "In a <redefine>, only annotations can be defined when no -schemaLocation- is specified.";
    			error(err);
    		}
    		
    		ModelGroup group = null;
    		group = (((ModelGroupUnmarshaller)_unmarshaller).getGroup());
    		
    		String structureName = group.getName();
    		if (structureName == null) {
    			String err = "When redefining a group, the group must have a name.\n";
    			error(err);
    		}
    		
    		//1-- the group must exist in the imported schema
    		Group original = _importedSchema.getModelGroup(structureName);
    		if (original == null) {
    			String err = "When redefining a group, the group must be present in the imported XML schema.\n"
    				+"Group: "+structureName+" is not defined in XML Schema:" + _importedSchema.getSchemaLocation();
    			error(err);
    		}
    		
    		//-- code needs to be added to check the Particle Valid (Restriction) constraint
    		//--TBD
    		
    		group.setRedefined();
    		_redefineSchema.addGroup(group);
    	} else {
    		String err = "In a <redefine>, only complexTypes|simpleTypes|groups or attributeGroups can be redefined.";
			error(err);	
    	}

    	_unmarshaller = null;
    } //-- endElement

    public void characters(char[] ch, int start, int length)
	throws XMLException
	{
    	//-- Do delagation if necessary
    	if (_unmarshaller != null) {
    		_unmarshaller.characters(ch, start, length);
    	}
    } //-- characters
    
    
    /**
     * Sets the name of the element that this UnknownUnmarshaller handles
     * @param name the name of the element that this unmarshaller handles
    **/
    public String elementName() {
        return SchemaNames.REDEFINE;
    } //-- elementName

    /**
     * Returns the Object created by this ComponentReader
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return _redefineSchema;
    } //-- getObject

}
