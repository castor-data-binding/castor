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
 * Copyright 2000-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.descriptors;


import org.exolab.castor.xml.XMLClassDescriptor;

import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

/**
 * The default set of built-in ClassDescriptors
 * 
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-11-04 15:55:27 -0700 (Thu, 04 Nov 2004) $
 */
public final class CoreDescriptors {

    private static final String LIST_CLASS_NAME = "java.util.List";
    
    private static final String LIST_DESCRIPTOR_NAME =
        "org.exolab.castor.xml.descriptors.ListClassDescriptor";
        
    /**
     * The java.util.Enumeration ClassDescriptor, really only
     * useful for marshalling as Enumerations are read-only.
     */
    private static final XMLClassDescriptor ENUMERATION_DESCRIPTOR =
        new EnumerationDescriptor();
     
    /**
     * The String ClassDescriptor
     */
    private static final XMLClassDescriptor STRING_DESCRIPTOR =
        new StringClassDescriptor();

    /**
     * The Date ClassDescriptor
     */
    private static final XMLClassDescriptor DATE_DESCRIPTOR =
        new DateClassDescriptor();
        
    
    /**
     * The java.util.Locale ClassDescriptor
     */
    private static final XMLClassDescriptor LOCALE_DESCRIPTOR =
        new LocaleDescriptor();
    
    /**
     * The java.sql.Date ClassDescriptor
     */
    private static final XMLClassDescriptor SQL_DATE_DESCRIPTOR =
        new SQLDateClassDescriptor();
        
    /**
     * The java.sql.Time ClassDescriptor
     */
    private static final XMLClassDescriptor SQL_TIME_DESCRIPTOR =
        new SQLTimeClassDescriptor();
        
    /**
     * The java.sql.Timestamp ClassDescriptor
     */
    private static final XMLClassDescriptor SQL_TIMESTAMP_DESCRIPTOR =
        new SQLTimestampClassDescriptor();
        
        
    /**
     * The Vector ClassDescriptor
    **/
    private static final XMLClassDescriptor VECTOR_DESCRIPTOR =
        new VectorClassDescriptor();
    
    /**
     * The List ClassDescriptor (only loaded for JDK 1.2+)
    **/
    private static XMLClassDescriptor LIST_DESCRIPTOR;
    
    private static Class LIST_CLASS;
    
    //-- static initializer
    static {
        loadDescriptors();
    }
    
    /**
     * Default constructor. Intentionally private.
    **/
    private CoreDescriptors() {
        super();
    } //-- CoreDescriptors
    
    /**
     * Returns the XMLClassDescriptor for the given Class.
     * This method will return null if there is no built-in 
     * XMLClassDescriptor.
     *
     * @param clazz the Class to return the XMLClassDescriptor for.
     * @return the XMLClassDescriptor for the given class, or null.
    **/
    public static XMLClassDescriptor getDescriptor(Class clazz) {
        if (clazz == null) return null;
        
        if (clazz == String.class) 
            return STRING_DESCRIPTOR;
            
        if (clazz == Date.class)
            return DATE_DESCRIPTOR;
            
        if (Enumeration.class.isAssignableFrom(clazz))
            return ENUMERATION_DESCRIPTOR;
            
        if ((clazz == Vector.class) || Vector.class.isAssignableFrom(clazz))
            return VECTOR_DESCRIPTOR;
        
        //-- JDK 1.2
        if (LIST_DESCRIPTOR != null) {
            if ((LIST_CLASS == clazz) || LIST_CLASS.isAssignableFrom(clazz))
                return LIST_DESCRIPTOR;
        }
        
        if (clazz == Locale.class) {
            return LOCALE_DESCRIPTOR;
        }
        
        //-- java.sql Date/Time classes
        if (clazz == java.sql.Date.class)
            return SQL_DATE_DESCRIPTOR;
            
        if (clazz == java.sql.Time.class)
            return SQL_TIME_DESCRIPTOR;
            
        if (clazz == java.sql.Timestamp.class)
            return SQL_TIMESTAMP_DESCRIPTOR;
            
        
        return null;
        
    } //-- getDescriptor
    
    private static void loadDescriptors() {
        //-- JDK 1.2 Collections
        
        ClassLoader loader = null;
        try {
            loader = java.util.Vector.class.getClassLoader();
            if (loader == null) {
                //-- probably JDK 1.1 if loader is null, but
                //-- we can double check anyway
                LIST_CLASS = Class.forName(LIST_CLASS_NAME);
            }
            else {
                LIST_CLASS = loader.loadClass(LIST_CLASS_NAME);
            }
        }
        catch(ClassNotFoundException cnfe) {
            //-- do nothing...handled below
        }
        
        //-- If not null, then JDK 1.2 or greater
        if (LIST_CLASS != null) {
            loader = CoreDescriptors.class.getClassLoader();
            Class descriptorClass = null;
            try {
                if (loader == null) {
                    descriptorClass = Class.forName(LIST_DESCRIPTOR_NAME);
                }
                else descriptorClass = loader.loadClass(LIST_DESCRIPTOR_NAME);
            }
            catch(ClassNotFoundException cnfe) {
                //-- do nothing...handled below
            }
            
            if (descriptorClass != null) {
                try {
                    LIST_DESCRIPTOR = 
                        (XMLClassDescriptor) descriptorClass.newInstance();
                }
                catch(InstantiationException ie) {
                    //-- just ignore
                }
                catch(IllegalAccessException iae) {
                    //-- just ignore
                }
                
                
            }
            else LIST_DESCRIPTOR = null;
            
        }
        else LIST_DESCRIPTOR = null;
        
    } //--
    
    
} //-- CoreDescriptors
