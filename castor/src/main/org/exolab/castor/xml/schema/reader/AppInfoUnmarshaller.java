/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.AppInfo;
import org.exolab.castor.xml.schema.SchemaNames;

/**
 * A class for Unmarshalling XML Schema <appinfo> elements
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class AppInfoUnmarshaller extends ComponentReader {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The current ComponentReader
    **/
    private ComponentReader unmarshaller;

    /**
     * The current branch depth
    **/
    private int depth = 0;

    /**
     * The Attribute reference for the Attribute we are constructing
    **/
    private AppInfo _appInfo = null;

    private StringBuffer sb = null;


      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new AppInfoUnmarshaller
     * @param atts the AttributeList
    **/
    public AppInfoUnmarshaller(AttributeSet atts)
        throws XMLException
    {
        super();

        _appInfo = new AppInfo();

        //-- handle attributes
        String attValue = null;

        _appInfo.setSource(atts.getValue(SchemaNames.SOURCE_ATTR));

    } //-- AppInfoUnmarshaller

      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
    **/
    public AppInfo getAppInfo() {
        if (sb != null) finish();
        return _appInfo;
    } //-- getArchetype

    /**
     * Returns the name of the element that this ComponentReader
     * handles
     * @return the name of the element that this ComponentReader
     * handles
    **/
    public String elementName() {
        return SchemaNames.APPINFO;
    } //-- elementName

    /**
     * Called to signal an end of unmarshalling. This method should
     * be overridden to perform any necessary clean up by an unmarshaller
    **/
    public void finish() {
        if (sb != null) {
            _appInfo.setContent(sb.toString());
            sb = null;
        }
    } //-- finish

    /**
     * Returns the Object created by this ComponentReader
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return getAppInfo();
    } //-- getObject

    /**
     * Signals the start of an element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element. This may be null.
     * Note: A null namespace is not the same as the default namespace unless
     * the default namespace is also null.
     * @param atts the AttributeSet containing the attributes associated
     * with the element.
     * @param nsDecls the namespace declarations being declared for this 
     * element. This may be null.
    **/
    public void startElement(String name, String namespace, AttributeSet atts,
        Namespaces nsDecls)
        throws XMLException
    {
        //-- ignore all daughter elements for now
        ++depth;

    } //-- startElement

    /**
     * Signals to end of the element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element.
    **/
    public void endElement(String name, String namespace)
        throws XMLException
    {
        --depth;
    } //-- endElement

    public void characters(char[] ch, int start, int length)
        throws XMLException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.characters(ch, start, length);
        }

        if (sb == null) sb = new StringBuffer();
        sb.append(ch, start, length);

    } //-- characters

} //-- AppInfoUnmarshaller
