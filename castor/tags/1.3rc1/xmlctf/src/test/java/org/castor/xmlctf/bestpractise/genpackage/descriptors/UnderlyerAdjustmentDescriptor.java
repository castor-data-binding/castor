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

import org.castor.xmlctf.bestpractise.genpackage.UnderlyerAdjustment;

/**
 * Class UnderlyerAdjustmentDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class UnderlyerAdjustmentDescriptor extends org.castor.xmlctf.bestpractise.genpackage.descriptors.CBMTransactionEventDescriptor {


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

    public UnderlyerAdjustmentDescriptor() {
        super();
        setExtendsWithoutFlatten(new org.castor.xmlctf.bestpractise.genpackage.descriptors.CBMTransactionEventDescriptor());
        _nsURI = "http://www.fpml.org/2003/FpML-4-0";
        _xmlName = "underlyerAdjustment";
        _elementDefinition = true;
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _partyThree
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.PartyThree.class, "_partyThree", "partyThree", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnderlyerAdjustment target = (UnderlyerAdjustment) object;
                return target.getPartyThree();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnderlyerAdjustment target = (UnderlyerAdjustment) object;
                    target.setPartyThree( (org.castor.xmlctf.bestpractise.genpackage.PartyThree) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.castor.xmlctf.bestpractise.genpackage.PartyThree();
            }
        };
        desc.setSchemaType("xml.srcgen.template.generated.PartyThree");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _partyThree
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _unwindCashflowList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow.class, "_unwindCashflowList", "unwindCashflow", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnderlyerAdjustment target = (UnderlyerAdjustment) object;
                return target.getUnwindCashflow();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnderlyerAdjustment target = (UnderlyerAdjustment) object;
                    target.addUnwindCashflow( (org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    UnderlyerAdjustment target = (UnderlyerAdjustment) object;
                    target.removeAllUnwindCashflow();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow();
            }
        };
        desc.setSchemaType("list");
        desc.setComponentType("xml.srcgen.template.generated.UnwindCashflow");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        addSequenceElement(desc);
        
        //-- validation code for: _unwindCashflowList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
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
        if (_identity == null) {
            return super.getIdentity();
        }
        return _identity;
    }

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    public java.lang.Class getJavaClass(
    ) {
        return org.castor.xmlctf.bestpractise.genpackage.UnderlyerAdjustment.class;
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
