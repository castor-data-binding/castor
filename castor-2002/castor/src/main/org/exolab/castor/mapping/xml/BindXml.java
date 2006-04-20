/*
 * This class was automatically generated with
 * <a href="http://castor.exolab.org">Castor 0.8.12</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.mapping.xml.types.NodeType;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 *
 * @version $Revision$ $Date$
**/
public class BindXml implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.String _type;

    private java.lang.String _matches;

    private java.lang.String _qNamePrefix = null;

    private org.exolab.castor.mapping.xml.types.NodeType _node;

    private boolean _reference = false;

    private boolean _has_reference = false;

      //----------------/
     //- Constructors -/
    //----------------/

    public BindXml() {
        super();
    } //-- org.exolab.castor.mapping.xml.BindXml()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteReference()
    {
        this._has_reference = false;
        this._reference = false;
    } //-- void deleteReference()

    /**
    **/
    public java.lang.String getMatches()
    {
        return this._matches;
    } //-- java.lang.String getMatches()

    /**
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName()

    /**
    **/
    public org.exolab.castor.mapping.xml.types.NodeType getNode()
    {
        return this._node;
    } //-- org.exolab.castor.mapping.xml.types.NodeType getNode()

    /**
    **/
    public java.lang.String getQNamePrefix()
    {
        return this._qNamePrefix;
    } //-- java.lang.String getType()

    /**
    **/
    public boolean getReference()
    {
        return this._reference;
    } //-- boolean getReference()

    /**
    **/
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType()

    /**
    **/
    public boolean hasReference()
    {
        return this._has_reference;
    } //-- boolean hasReference()

    /**
    **/
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
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {

        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer)

    /**
     *
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {

        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler)

    /**
     *
     * @param _matches
    **/
    public void setMatches(java.lang.String _matches)
    {
        this._matches = _matches;
    } //-- void setMatches(java.lang.String)

    /**
     *
     * @param _name
    **/
    public void setName(java.lang.String _name)
    {
        this._name = _name;
    } //-- void setName(java.lang.String)

    /**
     *
     * @param _node
    **/
    public void setNode(org.exolab.castor.mapping.xml.types.NodeType _node)
    {
        this._node = _node;
    } //-- void setNode(org.exolab.castor.mapping.xml.types.NodeType)

    /**
     *
     * @param _qNamePrefix
    **/
    public void setQNamePrefix(java.lang.String _qNamePrefix)
    {
        this._qNamePrefix = _qNamePrefix;
    } //-- void setType(java.lang.String)

    /**
     *
    **/
    public void setReference(boolean reference)
    {
        this._has_reference = true;
        this._reference = reference;
    } //-- void setReference(boolean)

    /**
     *
     * @param _type
    **/
    public void setType(java.lang.String _type)
    {
        this._type = _type;
    } //-- void setType(java.lang.String)

    /**
     *
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.BindXml unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.BindXml) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.BindXml.class, reader);
    } //-- org.exolab.castor.mapping.xml.BindXml unmarshal(java.io.Reader)

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        Validator validator = new Validator();
        validator.validate(this);
    } //-- void validate()

}
