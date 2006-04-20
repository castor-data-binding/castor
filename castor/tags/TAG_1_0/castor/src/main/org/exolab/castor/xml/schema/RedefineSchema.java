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
 * Copyright 2004 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.schema;

import java.util.Enumeration;
import java.util.Hashtable;

import org.exolab.castor.xml.ValidationException;

/**
 * <p>This class is a wrapper used to save meta information concerning redefined
 * structures from an XML schema.</p>
 *  
 * <p>This wrapper is identified by:
 * <ul>
 *     <li>a reference to the schema redefined</li>
 *     <li>the schema location of the redefined schema</li>
 *     <li>A vector containing the names of the structures redefined</li>
 * </ul>
 * 
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 **/
public class RedefineSchema extends Annotated {
    /** SerialVersionUID */
    private static final long serialVersionUID = -7095458840388436859L;

    /**
	 * The original schema that is imported in the 
	 * parent schema
	 */
	private Schema _originalSchema;
	
	/**
	 * The parent schema in which this redefined XML Schema
	 * is used.
	 */
	private Schema _parentSchema;
	
	/**
	 * The schema location of the redefined schema
	 */
	private String _schemaLocation = null;
	
	/**
	 * A vector with the names of the redefined complexTypes
	 */
	private Hashtable _complexTypes;
	
	/**
	 * A vector with the names of the redefined simpleTypes
	 */
	private Hashtable _simpleTypes;
	
	/**
	 * A vector with the names of the redefined groups
	 */
	private Hashtable _groups;
	
	/**
	 * A vector with the names of the redefined attributeGroups
	 */
	private Hashtable _attributeGroups;
	
	/**
	 * Default constructor to create a RedefineSchema
	 * that contains only Annotations
	 * 
	 */
	public RedefineSchema(Schema parentSchema) {
		super();
		_parentSchema = parentSchema;
		_schemaLocation = "";
	}
	
	/**
	 * Constructs a new RedefineSchema structure
	 * @param uri the URI identifying the Schema
	 */
	public RedefineSchema(Schema parentSchema, Schema originalSchema) {
		super();
		_schemaLocation = originalSchema.getSchemaLocation();
		_parentSchema = parentSchema;
		_originalSchema= originalSchema;
		_complexTypes = new Hashtable();
		_simpleTypes = new Hashtable();
		_attributeGroups = new Hashtable();
		_groups = new Hashtable();
	}
	
	/**
	 * Adds the given complexType in the list of redefined complexTypes.
	 * @param complexType the ComplexType to add.
	 */
	public void addComplexType(ComplexType complexType) throws SchemaException {
	    if (_schemaLocation.length() == 0)
	    	throw new IllegalStateException("A RedefineSchema with no schemaLocation must contain only annotations");
	    String name = complexType.getName();

	    if (name == null) {
	    	String err = "a global ComplexType must contain a name.";
	    	throw new SchemaException(err);
	    }
	    
	    if (complexType.getSchema() != _parentSchema) {
	    	String err = "invalid attempt to add an ComplexType which ";
	    	err += "belongs to a different Schema; type name: " + name;
	    	throw new SchemaException(err);
	    }
	    
	    if (getComplexType(name) != null ) {
	    	String err = "In the RedefineSchema:"+_schemaLocation+"a ComplexType already exists with the given name: ";
	    	throw new SchemaException(err + name);
	    }
	    
	    //--forces the redefine character
	    complexType.setRedefined();
	    
	    _complexTypes.put(name, complexType);
	    complexType.setParent(_parentSchema);
	}
	
	/**
	 * Enumerates the complexType names.
	 * @return an enumeration of the names of the redefined ComplexTypes
	 */
	public Enumeration enumerateComplexTypes() {
		if (_schemaLocation.length() == 0)
			return new EmptyEnumeration();
		return _complexTypes.elements();
	}
	
	/**
	 * Returns the ComplexType corresponding to the given name.
	 * 
	 * @param name the name of the ComplexType to look for.
	 * @return the ComplexType corresponding to the gven name.
	 */
	public ComplexType getComplexType(String name) {
		if (_schemaLocation.length() == 0)
			return null;
		
		return (ComplexType)_complexTypes.get(name);
	}
	
	/**
	 * Returns true if this redefinition contains a redefinition for a complexType with 
	 * the given name.
	 * 
	 * @param name the canonical name of the complexType.
	 */
	public boolean hasComplexTypeRedefinition(String name) {
		if (_schemaLocation.length() == 0)
			return false;
		
		return (_complexTypes.containsKey(name));
	}
	
	/**
	 * Removes the complexType with the given name from the redefine structure.
	 * 
	 * @param complexType The complexType to be removed from this Redefined Structure.
	 */
	public boolean removeComplexType(ComplexType complexType) {
		if (_schemaLocation.length() == 0)
			return false;
		
		boolean result = false;
		if (_complexTypes.contains(complexType)) {
			_complexTypes.remove(complexType);
			result = true;
		}
		return result;
	}

	/**
	 * Adds a simpleType in the list of redefined simpleTypes.
	 * 
	 * @param SimpleType the SimpleType to add.
	 */
	public void addSimpleType(SimpleType simpleType) throws SchemaException {
		
		String name = simpleType.getName();
		if (name == null)
			throw new IllegalArgumentException("A redefined simpleType must have a name");
		if (_schemaLocation.length() == 0)
			throw new IllegalStateException("A RedefineSchema with no schemaLocation must contain only annotations");

		if (simpleType.getSchema() != _parentSchema) {
			String err = "invalid attempt to add a SimpleType which ";
			err += "belongs to a different Schema; type name: " + name;
			throw new SchemaException(err);
		}
		
		if (getSimpleType(name) != null) {
			String err = "In the RedefineSchema:"+_schemaLocation+"a SimpleType already exists with the given name: ";
			throw new SchemaException(err + name);
		}
        //--forces the redefine character
		simpleType.setRedefined();
		
		simpleType.setParent(this);
		_simpleTypes.put(name, simpleType);
	}
	
	/**
	 * Enumerates the simpleType names.
	 * @return an enumeration of the names of the redefined SimpleTypes.
	 */
	public Enumeration enumerateSimpleTypes() {
		if (_schemaLocation.length() == 0)
			return new EmptyEnumeration();
		
		return _simpleTypes.elements();
	}
	
	/**
	 * Returns the SimpleType corresponding to the given name.
	 * 
	 * @param name the name of the SimpleType to look for.
	 * @return the SimpleType corresponding to the gven name.
	 */
	public SimpleType getSimpleType(String name) {
		if (_schemaLocation.length() == 0)
			return null;
		
		return (SimpleType)_simpleTypes.get(name);
	}
	
	/**
	 * Returns true if this redefinition contains a redefinition for a simpleType with 
	 * the given name.
	 * 
	 * @param name the canonical name of the simpleType.
	 */
	public boolean hasSimpleTypeRedefinition(String name) {
		if (_schemaLocation.length() == 0)
			return false;
		
		return (_simpleTypes.containsKey(name));
	}
	
	/**
	 * Removes the given simpleType from the redefine structure.
	 * 
	 * @param simpleType the simpleType to be removed from this Redefined Structure.
	 */
	public boolean removeSimpleType(SimpleType simpleType) {
		if (_schemaLocation.length() == 0)
			return false;
		
		boolean result = false;
		if (_simpleTypes.contains(simpleType)) {
			_simpleTypes.remove(simpleType);
			result = true;
		}
		return result;
	}

	/**
	 * Adds a group name in the list of redefined groups.
	 * @param name the Group name
	 */
	public void addGroup(ModelGroup group) throws SchemaException {
		if (_schemaLocation.length() == 0)
			throw new IllegalStateException("A RedefineSchema with no schemaLocation must contain only annotations");
		
		String name = group.getName();

		if (name == null) {
			String err = "a group declaration must contain a name.";
			throw new SchemaException(err);
		}
		
		if (getModelGroup(name) != null) {
			String err = "In the RedefineSchema:"+ _schemaLocation+" a group declaration already exists with the given name: ";
			throw new SchemaException(err + name);
		}
        //--forces the redefine character
		group.setRedefined();
		group.setParent(_parentSchema);
		
		_groups.put(name, group);
	}
	
	/**
	 * Returns the Model Group of the given name that is contained in this RedefineSchema.
	 * 
	 * @param name the name of the ModelGroup to retrieve.
	 * @return the ModelGroup of the given name contained in this RedefineSchema.
	 */
	public ModelGroup getModelGroup(String name) {
		if (_schemaLocation.length() == 0)
			return null;
		
		return (ModelGroup)_groups.get(name);
	}
	
	/**
	 * Enumerates the group names.
	 * @return an enumeration of the names of the redefined groups.
	 */
	public Enumeration enumerateGroups() {
		if (_schemaLocation.length() == 0)
			return new EmptyEnumeration();
		
		return _groups.elements();
	}
	
	/**
	 * Returns true if this redefinition contains a redefinition for a group with 
	 * the given name.
	 * 
	 * @param name the canonical name of the complexType.
	 */
	public boolean hasGroupRedefinition(String name) {
		if (_schemaLocation.length() == 0)
			return false;
		
		return (_groups.containsKey(name));
	}
	
	/**
	 * Removes the given ModelGroup from the redefine structure.
	 * 
	 * @param group the ModelGroup to be removed from this Redefined Structure.
	 */
	public boolean removeGroup(ModelGroup group) {
		if (_schemaLocation.length() == 0)
			return false;
		
		boolean result = false;
		if (_groups.contains(group)) {
		    _groups.remove(group);
		    result = true;
		}
		return result;
	}
	
	/**
	 * Adds a AttributeGroup name in the list of redefined attributeGroups.
	 * @param name the AttributeGroup name
	 */
	public void addAttributeGroup(AttributeGroupDecl attrGroup) throws SchemaException {
		if (_schemaLocation.length() == 0)
			throw new IllegalStateException("A RedefineSchema with no schemaLocation must contain only annotations");
		
		if (attrGroup == null) return;

		String name = attrGroup.getName();

		//-- handle namespace prefix, if necessary
		int idx = name.indexOf(':');
		if (idx >= 0)
		{
			String nsPrefix = name.substring(0,idx);
			name = name.substring(idx + 1);
			String ns = (String) _parentSchema.getNamespace(nsPrefix);
			if (ns == null)  {
				String err = "addAttributeGroup: ";
				err += "Namespace prefix not recognized '"+nsPrefix+"'";
				throw new IllegalArgumentException(err);
			}
			if (!ns.equals(_parentSchema.getTargetNamespace())) {
				String err = "AttributeGroup has different namespace " +
				"than this Schema definition.";
				throw new IllegalArgumentException(err);
			}
		}

		if (attrGroup.getSchema() != _parentSchema) {
			String err = "invalid attempt to add an AttributeGroup which ";
			err += "belongs to a different Schema; " + name;
			throw new SchemaException(err);
		}
		
		attrGroup.setRedefined();
		
		_attributeGroups.put(name, attrGroup);
	}
	
	/**
	 * Returns the AttributeGroup corresponding to the given 
	 * canonical name (unqualified name).
	 * 
	 * @return he AttributeGroup corresponding to the given 
	 * canonical name (unqualified name).
	 */
	public AttributeGroupDecl getAttributeGroup(String name) {
		if (_schemaLocation.length() == 0)
			return null;
		
		return (AttributeGroupDecl)_attributeGroups.get(name);
	}
	
	/**
	 * Enumerates the attributeGroup names.
	 * @return an enumeration of the names of the redefined AttributeGroups.
	 */
	public Enumeration enumerateAttributeGroups() {
		if (_schemaLocation.length() == 0)
			return new EmptyEnumeration();
		
		return _attributeGroups.elements();
	}
		
	/**
	 * Returns true if this redefinition contains a redefinition for an AttributeGroup with 
	 * the given name.
	 * 
	 * @param name the canonical name of the complexType.
	 */
	public boolean hasAttributeGroupRedefinition(String name) {
		if (_schemaLocation.length() == 0)
			return false;
		
		return (_attributeGroups.containsKey(name));
	}
	
	/**
	 * Removes the attributeGroup with the given name from the redefine structure.
	 * 
	 * @param name the name of the attributeGroup to be removed from this Redefined Structure.
	 */
	public boolean removeAttributeGroup(AttributeGroupDecl attrGroup) {
		if (_schemaLocation.length() == 0)
			return false;
		
		boolean result = false;
		if (_attributeGroups.contains(attrGroup)) {
		    _attributeGroups.remove(attrGroup);
		    result = true;
		}
		return result;
	}
	
	/**
	 * Returns true if at least one structure other than
	 * an annotation is present.
	 * 
	 * @return  true if at least one structure other than
	 * an annotation is present.
	 */
	public boolean hasRedefinition() {
		if (_schemaLocation.length() == 0)
			return false;
		
		return (!_complexTypes.isEmpty()) || (!_simpleTypes.isEmpty()) || (!_groups.isEmpty()) || (!_attributeGroups.isEmpty());
	}
	
	/**
	 * Returns the URI of the imported schema.
	 * 
	 * @return the URI of the imported schema.
	 */
	public String getSchemaLocation() {
		return _schemaLocation;
	}
	
	/**
	 * Returns the schema imported used for the redefinitions.
	 * @return the original schema imported.
	 */
	public Schema getOriginalSchema() {
		return _originalSchema;
	}
	
	/**
	 * Returns the parent schema in which this RedefineSchema is used.
	 * 
	 * @return the parent schema in which this Redefined Schema 
	 * is used.
	 */
	public Schema getParentSchema(){
		return _parentSchema;
	}
	
	/**
	 * Returns the type of this Redefine Structure
	 * @return the type of this Redefin Structure
	 **/
	public short getStructureType() {
		return Structure.REDEFINE;
	} //-- getStructureType
	
	
	public void validate() throws ValidationException {
		//-- no validation performed on the structure since
		//-- it is simply a place holder for names and not for real structures.
	}
	
	class EmptyEnumeration implements Enumeration {
		EmptyEnumeration() {
			super();
		}
		
		public Object nextElement() {
			return null;
		}
		
		public boolean hasMoreElements() {
			return false;
		}
	}
}
