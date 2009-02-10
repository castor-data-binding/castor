/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.xmlctf.bestpractise.genpackage.descriptors;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification;

/**
 * Class AssignmentNotificationDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class AssignmentNotificationDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _elementDefinition.
     */
    private boolean _elementDefinition;

    /**
     * Field _nsPrefix.
     */
    private java.lang.String _nsPrefix;

    /**
     * Field _nsURI.
     */
    private java.lang.String _nsURI;

    /**
     * Field _xmlName.
     */
    private java.lang.String _xmlName;

    /**
     * Field _identity.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor _identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public AssignmentNotificationDescriptor() {
        super();
        _nsURI = "http://www.fpml.org/2003/FpML-4-0";
        _xmlName = "AssignmentNotification";
        _elementDefinition = false;
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _unwindCashflow
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow.class, "_unwindCashflow", "unwindCashflow", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AssignmentNotification target = (AssignmentNotification) object;
                return target.getUnwindCashflow();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AssignmentNotification target = (AssignmentNotification) object;
                    target.setUnwindCashflow( (org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow();
            }
        };
        desc.setSchemaType("xml.srcgen.template.generated.UnwindCashflow");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _unwindCashflow
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _partyReference
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.PartyReference.class, "_partyReference", "partyReference", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AssignmentNotification target = (AssignmentNotification) object;
                return target.getPartyReference();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AssignmentNotification target = (AssignmentNotification) object;
                    target.setPartyReference( (org.castor.xmlctf.bestpractise.genpackage.PartyReference) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.castor.xmlctf.bestpractise.genpackage.PartyReference();
            }
        };
        desc.setSchemaType("xml.srcgen.template.generated.PartyReference");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _partyReference
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _assignmentRole
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.types.AssignmentRole.class, "_assignmentRole", "assignmentRole", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AssignmentNotification target = (AssignmentNotification) object;
                return target.getAssignmentRole();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AssignmentNotification target = (AssignmentNotification) object;
                    target.setAssignmentRole( (org.castor.xmlctf.bestpractise.genpackage.types.AssignmentRole) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        handler = new org.exolab.castor.xml.handlers.EnumFieldHandler(org.castor.xmlctf.bestpractise.genpackage.types.AssignmentRole.class, handler);
        desc.setImmutable(true);
        desc.setSchemaType("xml.srcgen.template.generated.types.AssignmentRole");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _assignmentRole
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _priorNotification
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_priorNotification", "priorNotification", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AssignmentNotification target = (AssignmentNotification) object;
                if (!target.hasPriorNotification()) { return null; }
                return (target.getPriorNotification() ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE);
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AssignmentNotification target = (AssignmentNotification) object;
                    // ignore null values for non optional primitives
                    if (value == null) { return; }
                    
                    target.setPriorNotification( ((java.lang.Boolean) value).booleanValue());
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("boolean");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _priorNotification
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
            org.exolab.castor.xml.validators.BooleanValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.BooleanValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _contactName
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_contactName", "contactName", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AssignmentNotification target = (AssignmentNotification) object;
                return target.getContactName();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AssignmentNotification target = (AssignmentNotification) object;
                    target.setContactName( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _contactName
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode.
     * 
     * @return the access mode specified for this class.
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode(
    ) {
        return null;
    }

    /**
     * Method getIdentity.
     * 
     * @return the identity field, null if this class has no
     * identity.
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity(
    ) {
        return _identity;
    }

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    public java.lang.Class getJavaClass(
    ) {
        return org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification.class;
    }

    /**
     * Method getNameSpacePrefix.
     * 
     * @return the namespace prefix to use when marshaling as XML.
     */
    public java.lang.String getNameSpacePrefix(
    ) {
        return _nsPrefix;
    }

    /**
     * Method getNameSpaceURI.
     * 
     * @return the namespace URI used when marshaling and
     * unmarshaling as XML.
     */
    public java.lang.String getNameSpaceURI(
    ) {
        return _nsURI;
    }

    /**
     * Method getValidator.
     * 
     * @return a specific validator for the class described by this
     * ClassDescriptor.
     */
    public org.exolab.castor.xml.TypeValidator getValidator(
    ) {
        return this;
    }

    /**
     * Method getXMLName.
     * 
     * @return the XML Name for the Class being described.
     */
    public java.lang.String getXMLName(
    ) {
        return _xmlName;
    }

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that
     * of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition(
    ) {
        return _elementDefinition;
    }

}
