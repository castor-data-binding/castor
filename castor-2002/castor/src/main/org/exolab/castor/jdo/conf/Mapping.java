/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8 (20000324)</a>,
 * using an XML Schema.
 * $Id
 */

package org.exolab.castor.jdo.conf;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class Mapping implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _href;


      //----------------/
     //- Constructors -/
    //----------------/

    public Mapping() {
        super();
    } //-- org.exolab.castor.jdo.conf.Mapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public java.lang.String getHref() {
        return this._href;
    } //-- java.lang.String getHref() 

    /**
     * 
     * @param _href
    **/
    public void setHref(java.lang.String _href) {
        this._href = _href;
    } //-- void setHref(java.lang.String) 

}
