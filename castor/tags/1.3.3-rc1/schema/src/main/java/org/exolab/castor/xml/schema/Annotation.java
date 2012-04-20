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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.ValidationException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A class representing the XML Schema Annotation.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $ 
**/
public class Annotation extends Structure {
    /** SerialVersionUID */
    private static final long serialVersionUID = 2838816224303555598L;

    /**
     * List of {@literal <appinfo/>} objects.
    **/
    private Vector<AppInfo> _appInfos = new Vector<AppInfo>();
    
    /**
     * List of <documentation/> objects.
    **/
    private Vector<Documentation> _documentations = new Vector<Documentation>();
    
    /**
     * Adds the given {@link AppInfo} to this annotation.
     * @param appInfo the AppInfo to add
    **/
    public void addAppInfo(final AppInfo appInfo) {
        if (appInfo != null) {
            _appInfos.addElement(appInfo);
        }
    }
    
    /**
     * Adds the given Documentation to this Annotation.
     * @param documentation the documentation to add to this Annotation
    **/
    public void addDocumentation(final Documentation documentation) {
        if (documentation != null) {
            _documentations.addElement(documentation);
        }
    }

    /**
     * Returns an enumeration of all {@link AppInfo} elements for this Annotation.
     * @return an enumeration of all {@link AppInfo} elements for this Annotation
    **/
    public Enumeration<AppInfo> getAppInfo() {
        return _appInfos.elements();
    }
    
    /**
     * Returns an enumeration of all documentation elements for this Annotation.
     * @return an enumeration of all documentation elements for this Annotation
    **/
    public Enumeration<Documentation> getDocumentation() {
        return _documentations.elements();
    }
    
    /**
     * Removes the given AppInfo from this Annotation.
     * @param appInfo the AppInfo to remove
    **/
    public void removeAppInfo(final AppInfo appInfo) {
        if (appInfo != null) {
            _appInfos.removeElement(appInfo);
        }
    }

    /**
     * Removes the given {@link Documentation} from this Annotation.
     * @param documentation the Documentation to remove
    **/
    public void removeDocumentation(final Documentation documentation) {
        if (documentation != null) {
            _documentations.removeElement(documentation);
        }
    }
    
    /**
     * Returns the type of this Schema Structure.
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.ANNOTATION;
    }
    
    /**
     * Checks the validity of this Schema definition.
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public void validate() throws ValidationException {
        // -- do nothing
    }
    
}
