/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
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
     * Field _random.
     */
    private boolean _random = false;

    /**
     * keeps track of state for field: _random
     */
    private boolean _has_random;

    /**
     * Field _dump.
     */
    private boolean _dump = false;

    /**
     * keeps track of state for field: _dump
     */
    private boolean _has_dump;


      //----------------/
     //- Constructors -/
    //----------------/

    public RootType() {
        super();
        setContent("");
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteDump(
    ) {
        this._has_dump= false;
    }

    /**
     */
    public void deleteRandom(
    ) {
        this._has_random= false;
    }

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'Content'.
     */
    public java.lang.String getContent(
    ) {
        return this._content;
    }

    /**
     * Returns the value of field 'dump'.
     * 
     * @return the value of field 'Dump'.
     */
    public boolean getDump(
    ) {
        return this._dump;
    }

    /**
     * Returns the value of field 'random'.
     * 
     * @return the value of field 'Random'.
     */
    public boolean getRandom(
    ) {
        return this._random;
    }

    /**
     * Method hasDump.
     * 
     * @return true if at least one Dump has been added
     */
    public boolean hasDump(
    ) {
        return this._has_dump;
    }

    /**
     * Method hasRandom.
     * 
     * @return true if at least one Random has been added
     */
    public boolean hasRandom(
    ) {
        return this._has_random;
    }

    /**
     * Returns the value of field 'dump'.
     * 
     * @return the value of field 'Dump'.
     */
    public boolean isDump(
    ) {
        return this._dump;
    }

    /**
     * Returns the value of field 'random'.
     * 
     * @return the value of field 'Random'.
     */
    public boolean isRandom(
    ) {
        return this._random;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(
            final java.lang.String content) {
        this._content = content;
    }

    /**
     * Sets the value of field 'dump'.
     * 
     * @param dump the value of field 'dump'.
     */
    public void setDump(
            final boolean dump) {
        this._dump = dump;
        this._has_dump = true;
    }

    /**
     * Sets the value of field 'random'.
     * 
     * @param random the value of field 'random'.
     */
    public void setRandom(
            final boolean random) {
        this._random = random;
        this._has_random = true;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.tests.framework.testDescriptor.StringType
     */
    public static org.exolab.castor.tests.framework.testDescriptor.StringType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.tests.framework.testDescriptor.StringType) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.RootType.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
