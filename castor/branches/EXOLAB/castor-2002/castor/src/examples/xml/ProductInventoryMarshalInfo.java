/*
 * Add code header here
 * $Id$ 
 */

package xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.lang.reflect.Method;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.GroupValidationRule;
import org.exolab.castor.xml.MarshalDescriptor;
import org.exolab.castor.xml.SimpleMarshalDescriptor;
import org.exolab.castor.xml.ValidationRule;

/**
 * @author 
 * @version $Revision$ $Date$
**/
public class ProductInventoryMarshalInfo implements org.exolab.castor.xml.MarshalInfo {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * 
    **/
    private org.exolab.castor.xml.MarshalDescriptor[] elements;

    /**
     * 
    **/
    private org.exolab.castor.xml.MarshalDescriptor[] attributes;

    /**
     * 
    **/
    private org.exolab.castor.xml.SimpleMarshalDescriptor contentDesc;

    /**
     * 
    **/
    private org.exolab.castor.xml.GroupValidationRule gvr;

    /**
     * 
    **/
    private org.exolab.castor.xml.ValidationRule[] rules;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProductInventoryMarshalInfo() {
        SimpleMarshalDescriptor desc = null;
        Class[] emptyClassArgs = new Class[0];
        Class[] classArgs = new Class[1];
        gvr = new GroupValidationRule();
        BasicValidationRule bvr = null;
        rules = new ValidationRule[2];
        //-- initialize attributes
        
        attributes = new MarshalDescriptor[1];
        //-- vQuantity
        desc = new SimpleMarshalDescriptor(java.lang.Integer.TYPE, "vQuantity", "quantity");
        desc.setDescriptorType(DescriptorType.attribute);
        try {
            desc.setReadMethod(ProductInventory.class.getMethod("getQuantity", emptyClassArgs));
            classArgs[0] = int.class;
            desc.setWriteMethod(ProductInventory.class.getMethod("setQuantity", classArgs));
        }
        catch(java.lang.NoSuchMethodException nsme) {};
        
        attributes[0] = desc;
        
        bvr = new BasicValidationRule("quantity");
        bvr.setAsAttributeRule();
        bvr.setMaxOccurs(1);
        rules[0] = bvr;
        rules[1] = gvr;
        //-- initialize elements
        
        elements = new MarshalDescriptor[0];
    } //-- ProductInventoryMarshalInfo()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
    **/
    public org.exolab.castor.xml.ValidationRule[] getValidationRules() {
        return rules;
    } //-- org.exolab.castor.xml.ValidationRule[] getValidationRules() 

    /**
     * 
    **/
    public org.exolab.castor.xml.MarshalDescriptor[] getAttributeDescriptors() {
        return attributes;
    } //-- org.exolab.castor.xml.MarshalDescriptor[] getAttributeDescriptors() 

    /**
     * 
    **/
    public java.lang.Class getClassType() {
        return ProductInventory.class;
    } //-- java.lang.Class getClassType() 

    /**
     * 
    **/
    public org.exolab.castor.xml.MarshalDescriptor[] getElementDescriptors() {
        return elements;
    } //-- org.exolab.castor.xml.MarshalDescriptor[] getElementDescriptors() 

    /**
     * 
    **/
    public org.exolab.castor.xml.MarshalDescriptor getContentDescriptor() {
        return contentDesc;
    } //-- org.exolab.castor.xml.MarshalDescriptor getContentDescriptor() 

    /**
     * 
    **/
    public java.lang.String getNameSpacePrefix() {
        return null;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     * 
    **/
    public java.lang.String getNameSpaceURI() {
        return null;
    } //-- java.lang.String getNameSpaceURI() 

}
