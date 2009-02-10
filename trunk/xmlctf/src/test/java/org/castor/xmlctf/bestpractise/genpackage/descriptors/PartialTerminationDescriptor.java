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

import org.castor.xmlctf.bestpractise.genpackage.PartialTermination;

/**
 * Class PartialTerminationDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class PartialTerminationDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public PartialTerminationDescriptor() {
        super();
        _nsURI = "http://www.fpml.org/2003/FpML-4-0";
        _xmlName = "partialTermination";
        _elementDefinition = true;
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _partyOne
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.PartyOne.class, "_partyOne", "partyOne", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                PartialTermination target = (PartialTermination) object;
                return target.getPartyOne();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    PartialTermination target = (PartialTermination) object;
                    target.setPartyOne( (org.castor.xmlctf.bestpractise.genpackage.PartyOne) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.castor.xmlctf.bestpractise.genpackage.PartyOne();
            }
        };
        desc.setSchemaType("xml.srcgen.template.generated.PartyOne");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _partyOne
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _partyTwo
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.PartyTwo.class, "_partyTwo", "partyTwo", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                PartialTermination target = (PartialTermination) object;
                return target.getPartyTwo();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    PartialTermination target = (PartialTermination) object;
                    target.setPartyTwo( (org.castor.xmlctf.bestpractise.genpackage.PartyTwo) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.castor.xmlctf.bestpractise.genpackage.PartyTwo();
            }
        };
        desc.setSchemaType("xml.srcgen.template.generated.PartyTwo");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _partyTwo
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _partyThreeHref
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.PartyThreeHref.class, "_partyThreeHref", "partyThreeHref", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                PartialTermination target = (PartialTermination) object;
                return target.getPartyThreeHref();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    PartialTermination target = (PartialTermination) object;
                    target.setPartyThreeHref( (org.castor.xmlctf.bestpractise.genpackage.PartyThreeHref) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.castor.xmlctf.bestpractise.genpackage.PartyThreeHref();
            }
        };
        desc.setSchemaType("xml.srcgen.template.generated.PartyThreeHref");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _partyThreeHref
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _partialAmount
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.math.BigDecimal.class, "_partialAmount", "partialAmount", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                PartialTermination target = (PartialTermination) object;
                return target.getPartialAmount();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    PartialTermination target = (PartialTermination) object;
                    target.setPartialAmount( (java.math.BigDecimal) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new java.math.BigDecimal(0);
            }
        };
        desc.setImmutable(true);
        desc.setSchemaType("decimal");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _partialAmount
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
            org.exolab.castor.xml.validators.DecimalValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.DecimalValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _assignmentNotificationList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification.class, "_assignmentNotificationList", "assignmentNotification", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                PartialTermination target = (PartialTermination) object;
                return target.getAssignmentNotification();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    PartialTermination target = (PartialTermination) object;
                    target.addAssignmentNotification( (org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    PartialTermination target = (PartialTermination) object;
                    target.removeAllAssignmentNotification();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification();
            }
        };
        desc.setSchemaType("list");
        desc.setComponentType("xml.srcgen.template.generated.AssignmentNotification");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _assignmentNotificationList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _partialTerminationChoice
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.PartialTerminationChoice.class, "_partialTerminationChoice", "-error-if-this-is-used-", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                PartialTermination target = (PartialTermination) object;
                return target.getPartialTerminationChoice();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    PartialTermination target = (PartialTermination) object;
                    target.setPartialTerminationChoice( (org.castor.xmlctf.bestpractise.genpackage.PartialTerminationChoice) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.castor.xmlctf.bestpractise.genpackage.PartialTerminationChoice();
            }
        };
        desc.setSchemaType("xml.srcgen.template.generated.PartialTerminationChoice");
        desc.setHandler(handler);
        desc.setContainer(true);
        desc.setClassDescriptor(new org.castor.xmlctf.bestpractise.genpackage.descriptors.PartialTerminationChoiceDescriptor());
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _partialTerminationChoice
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
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
        return org.castor.xmlctf.bestpractise.genpackage.PartialTermination.class;
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
