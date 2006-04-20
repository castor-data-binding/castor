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
public class ProductMarshalInfo implements org.exolab.castor.xml.MarshalInfo {


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

    public ProductMarshalInfo() {
        SimpleMarshalDescriptor desc = null;
        Class[] emptyClassArgs = new Class[0];
        Class[] classArgs = new Class[1];
        gvr = new GroupValidationRule();
        BasicValidationRule bvr = null;
        rules = new ValidationRule[4];
        //-- initialize attributes
        
        attributes = new MarshalDescriptor[3];
        //-- vPrice
        desc = new SimpleMarshalDescriptor(java.lang.Double.TYPE, "vPrice", "price");
        desc.setDescriptorType(DescriptorType.attribute);
        try {
            desc.setReadMethod(Product.class.getMethod("getPrice", emptyClassArgs));
            classArgs[0] = double.class;
            desc.setWriteMethod(Product.class.getMethod("setPrice", classArgs));
        }
        catch(java.lang.NoSuchMethodException nsme) {};
        
        attributes[0] = desc;
        
        bvr = new BasicValidationRule("price");
        bvr.setAsAttributeRule();
        bvr.setMaxOccurs(1);
        rules[0] = bvr;
        //-- vId
        desc = new SimpleMarshalDescriptor(java.lang.String.class, "vId", "id");
        desc.setDescriptorType(DescriptorType.attribute);
        try {
            desc.setReadMethod(Product.class.getMethod("getId", emptyClassArgs));
            classArgs[0] = java.lang.String.class;
            desc.setWriteMethod(Product.class.getMethod("setId", classArgs));
        }
        catch(java.lang.NoSuchMethodException nsme) {};
        
        attributes[1] = desc;
        
        bvr = new BasicValidationRule("id");
        bvr.setAsAttributeRule();
        bvr.setMaxOccurs(1);
        rules[1] = bvr;
        //-- vName
        desc = new SimpleMarshalDescriptor(java.lang.String.class, "vName", "name");
        desc.setDescriptorType(DescriptorType.attribute);
        try {
            desc.setReadMethod(Product.class.getMethod("getName", emptyClassArgs));
            classArgs[0] = java.lang.String.class;
            desc.setWriteMethod(Product.class.getMethod("setName", classArgs));
        }
        catch(java.lang.NoSuchMethodException nsme) {};
        
        attributes[2] = desc;
        
        bvr = new BasicValidationRule("name");
        bvr.setAsAttributeRule();
        bvr.setMaxOccurs(1);
        bvr.setTypeValidator(new StringValidator());
        rules[2] = bvr;
        rules[3] = gvr;
        //-- initialize elements
        
        elements = new MarshalDescriptor[2];
        //-- vProductGroup
        desc = new SimpleMarshalDescriptor(ProductGroup.class, "vProductGroup", "product-group");
        desc.setDescriptorType(DescriptorType.element);
        try {
            desc.setReadMethod(Product.class.getMethod("getProductGroup", emptyClassArgs));
            classArgs[0] = ProductGroup.class;
            desc.setWriteMethod(Product.class.getMethod("setProductGroup", classArgs));
        }
        catch(java.lang.NoSuchMethodException nsme) {};
        
        desc.setRequired(true);
        elements[0] = desc;
        
        bvr = new BasicValidationRule("product-group");
        bvr.setMinOccurs(1);
        bvr.setMaxOccurs(1);
        gvr.addValidationRule(bvr);
        //-- vProductInventory
        desc = new SimpleMarshalDescriptor(ProductInventory.class, "vProductInventory", "product-inventory");
        desc.setDescriptorType(DescriptorType.element);
        try {
            desc.setReadMethod(Product.class.getMethod("getProductInventory", emptyClassArgs));
            classArgs[0] = ProductInventory.class;
            desc.setWriteMethod(Product.class.getMethod("setProductInventory", classArgs));
        }
        catch(java.lang.NoSuchMethodException nsme) {};
        
        desc.setRequired(true);
        elements[1] = desc;
        
        bvr = new BasicValidationRule("product-inventory");
        bvr.setMinOccurs(1);
        bvr.setMaxOccurs(1);
        gvr.addValidationRule(bvr);
    } //-- ProductMarshalInfo()


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
        return Product.class;
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
