/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.3</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * The definition of the Root Type in the object model. Contains
 * two
 *  boolean attributes: random and dump.
 *  If random is set to true, a test using randomized objects will
 *  be executed. If dump is set to true, the object will be dumped
 *  to specific files.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class RootType extends org.exolab.castor.tests.framework.testDescriptor.StringType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * Field _random
     */
    private boolean _random = false;

    /**
     * keeps track of state for field: _random
     */
    private boolean _has_random;

    /**
     * Field _dump
     */
    private boolean _dump = false;

    /**
     * keeps track of state for field: _dump
     */
    private boolean _has_dump;


      //----------------/
     //- Constructors -/
    //----------------/

    public RootType() 
     {
        super();
        setContent("");
    } //-- org.exolab.castor.tests.framework.testDescriptor.RootType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteDump()
    {
        this._has_dump= false;
    } //-- void deleteDump() 

    /**
     */
    public void deleteRandom()
    {
        this._has_random= false;
    } //-- void deleteRandom() 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'Content'.
     */
    public java.lang.String getContent()
    {
        return this._content;
    } //-- java.lang.String getContent() 

    /**
     * Returns the value of field 'dump'.
     * 
     * @return the value of field 'Dump'.
     */
    public boolean getDump()
    {
        return this._dump;
    } //-- boolean getDump() 

    /**
     * Returns the value of field 'random'.
     * 
     * @return the value of field 'Random'.
     */
    public boolean getRandom()
    {
        return this._random;
    } //-- boolean getRandom() 

    /**
     * Method hasDump
     * 
     * 
     * 
     * @return true if at least one Dump has been added
     */
    public boolean hasDump()
    {
        return this._has_dump;
    } //-- boolean hasDump() 

    /**
     * Method hasRandom
     * 
     * 
     * 
     * @return true if at least one Random has been added
     */
    public boolean hasRandom()
    {
        return this._has_random;
    } //-- boolean hasRandom() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid()
    {
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
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content)
    {
        this._content = content;
    } //-- void setContent(java.lang.String) 

    /**
     * Sets the value of field 'dump'.
     * 
     * @param dump the value of field 'dump'.
     */
    public void setDump(boolean dump)
    {
        this._dump = dump;
        this._has_dump = true;
    } //-- void setDump(boolean) 

    /**
     * Sets the value of field 'random'.
     * 
     * @param random the value of field 'random'.
     */
    public void setRandom(boolean random)
    {
        this._random = random;
        this._has_random = true;
    } //-- void setRandom(boolean) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return the unmarshaled
     * org.exolab.castor.tests.framework.testDescriptor.StringType
     */
    public static org.exolab.castor.tests.framework.testDescriptor.StringType unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.StringType) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.RootType.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.StringType unmarshal(java.io.Reader) 

    /**
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
