/*
 * Add code header here
 * $Id$ 
 */

package xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;

/**
 * @author 
 * @version $Revision$ $Date$
**/
public class Product implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * 
    **/
    private double vPrice;

    /**
     * 
    **/
    private java.lang.String vId;

    /**
     * 
    **/
    private java.lang.String vName;

    /**
     * 
    **/
    private ProductGroup vProductGroup;

    /**
     * 
    **/
    private ProductInventory vProductInventory;


      //----------------/
     //- Constructors -/
    //----------------/

    public Product() {
    } //-- Product()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
    **/
    public void validate() 
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator.validate(this, null);
    } //-- void validate() 

    /**
     * 
    **/
    public boolean isValid() {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
    **/
    public double getPrice() {
        return this.vPrice;
    } //-- double getPrice() 

    /**
     * 
     * @param vPrice 
    **/
    public void setPrice(double vPrice) {
        this.vPrice = vPrice;
    } //-- void setPrice(double) 

    /**
     * 
    **/
    public java.lang.String getId() {
        return this.vId;
    } //-- java.lang.String getId() 

    /**
     * 
     * @param vId 
    **/
    public void setId(java.lang.String vId) {
        this.vId = vId;
    } //-- void setId(java.lang.String) 

    /**
     * 
    **/
    public java.lang.String getReferenceId() {
        return this.vId;
    } //-- java.lang.String getReferenceId() 

    /**
     * 
    **/
    public java.lang.String getName() {
        return this.vName;
    } //-- java.lang.String getName() 

    /**
     * 
     * @param vName 
    **/
    public void setName(java.lang.String vName) {
        this.vName = vName;
    } //-- void setName(java.lang.String) 

    /**
     * 
    **/
    public ProductGroup getProductGroup() {
        return this.vProductGroup;
    } //-- ProductGroup getProductGroup() 

    /**
     * 
     * @param vProductGroup 
    **/
    public void setProductGroup(ProductGroup vProductGroup) {
        this.vProductGroup = vProductGroup;
    } //-- void setProductGroup(ProductGroup) 

    /**
     * 
    **/
    public ProductInventory getProductInventory() {
        return this.vProductInventory;
    } //-- ProductInventory getProductInventory() 

    /**
     * 
     * @param vProductInventory 
    **/
    public void setProductInventory(ProductInventory vProductInventory) {
        this.vProductInventory = vProductInventory;
    } //-- void setProductInventory(ProductInventory) 

    /**
     * 
     * @param out 
    **/
    public void marshal(java.io.Writer out) 
        throws java.io.IOException, org.xml.sax.SAXException
    {
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler 
    **/
    public void marshal(org.xml.sax.DocumentHandler handler) 
        throws java.io.IOException, org.xml.sax.SAXException
    {
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
     * 
     * @param reader 
    **/
    public static Product unmarshal(java.io.Reader reader) 
        throws java.io.IOException
    {
        return (Product) Unmarshaller.unmarshal(Product.class, reader);
    } //-- Product unmarshal(java.io.Reader) 

}
