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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.ValidationException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A class representing the XML Schema Annotation
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
public class Annotation extends Structure {
    
    
    /**
     * List of <appinfo> objects
    **/
    private Vector appInfoList = null;
    
    /**
     * List of <info> objects
    **/
    private Vector infoList = null;
    
    
    /**
     * Creates a new Annotation
    **/
    public Annotation() {
        appInfoList = new Vector();
        infoList = new Vector();
    } //-- Annotation
    
    /**
     * Adds the given AppInfo to this Annotation
     * @param appInfo the AppInfo to add
    **/
    public void addAppInfo(AppInfo appInfo) {
        if (appInfo != null) appInfoList.addElement(appInfo);
    } //-- addAppInfo
    
    /**
     * Adds the given Info to this Annotation
     * @param info the info to add to this Annotation
    **/
    public void addInfo(Info info) {
        if (info != null) infoList.addElement(info);
    } //-- addInfo

    /**
     * Returns an enumeration of all AppInfo elements for this Annotation
     * @return an enumeration of all AppInfo elements for this Annotation
    **/
    public Enumeration getAppInfo() {
        return appInfoList.elements();
    } //-- getAppInfo
    
    /**
     * Returns an enumeration of all info elements for this Annotation
     * @return an enumeration of all info elements for this Annotation
    **/
    public Enumeration getInfo() {
        return infoList.elements();
    } //-- getInfo
    
    /**
     * Removes the given AppInfo from this Annotation
     * @param appInfo the AppInfo to remove
    **/
    public void removeAppInfo(AppInfo appInfo) {
        if (appInfo != null) appInfoList.removeElement(appInfo);
    } //-- removeAppInfo

    /**
     * Removes the given Info from this Annotation
     * @param info the Info to remove
    **/
    public void removeInfo(Info info) {
        if (info != null) infoList.removeElement(info);
    } //-- removeInfo
    
    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/
    
    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.ANNOTATION;
    } //-- getStructureType
    
    /**
     * Checks the validity of this Schema defintion.
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public void validate()
        throws ValidationException
    {
        //-- do nothing
    } //-- validate
    
} //-- Annotation