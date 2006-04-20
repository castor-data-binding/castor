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


package org.exolab.javasource;

/**
 * A descriptor for a JavaDoc comment
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class JDocDescriptor {
    
    /**
     * The default version string
    **/    
    //-- separated using "+" so that CVS doesn't expand
    public static final String DEFAULT_VERSION =
        "$"+"Revision"+"$ $"+"Date"+"$";
    
    
    //-- These are listed in order of how they should
    //-- appear in a JavaDoc list, so the numbers
    //-- are important...see #compareTo
    /**
     * The param descriptor (param)
    **/
    public static final short PARAM      = 0;
    
    /**
     * The exception descriptor (exception)
    **/
    public static final short EXCEPTION  = 1;
    
    /**
     * The return descriptor (return)
    **/
    public static final short RETURN     = 2;
    
    /**
     * The author descriptor
    **/
    public static final short AUTHOR     = 3;
    
        
    /**
     * the version descriptor (version)
    **/
    public static final short VERSION    = 4;
    
    
    /**
     * The reference descriptor (see)
    **/
    public static final short REFERENCE  = 5;
    

    
    private String description = null;
    private String name = null;
    private short  type = -1;
    
    /**
     * Creates a new JDocDescriptor
    **/
    private JDocDescriptor(short type) {
        this.type = type;
    } //-- JDocDescriptor
    
    /**
     * Creates a new JDocDescriptor
     * @param name the name string for this descriptor
     * @param desc the description string for this descriptor
    **/
    private JDocDescriptor(short type, String name, String desc) {
        this.type        = type;
        this.name        = name;
        this.description = desc;
    } //-- JDocDescriptor

    /**
     * Compares the type of this JDocDescriptor with the given descriptor.
     * Enables sorting of descriptors.
     * @return 0 if the two descriptor types are equal, 1 if the type of
     * this descriptor is greater than the given descriptor, or -1 if the
     * type of this descriptor is less than the given descriptor
    **/
    protected short compareTo(JDocDescriptor jdd) {
        short jddType = jdd.getType();
        
        if (jddType == this.type) return 0;
        
        // The ordering is as follows
        // #param
        // #exception
        // #author
        // #version
        // #see (reference)
        //
        return (short) ((jddType < this.type) ? 1 : -1);
            
    } //-- compareTo
    
    /**
     * Creates a new author descriptor
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createAuthorDesc() {
        return new JDocDescriptor(AUTHOR);
    } //-- createAuthorDesc
    
    /**
     * Creates a new author descriptor
     * @param name the author name string
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createAuthorDesc(String name) {
        return new JDocDescriptor(AUTHOR, name, null);
    } //-- createAuthorDesc

    /**
     * Creates a new exception descriptor
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createExceptionDesc() {
        return new JDocDescriptor(EXCEPTION);
    } //-- createExceptionDesc
    
    /**
     * Creates a new exception descriptor
     * @param name the exception name
     * @param desc the description of when the exception is called
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createExceptionDesc
        (String name, String desc) 
    {
        return new JDocDescriptor(EXCEPTION, name, desc);
    } //-- createExceptionDesc
    

    /**
     * Creates a new param descriptor
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createParamDesc() {
        return new JDocDescriptor(PARAM);
    } //-- createParamDesc
    
    /**
     * Creates a new param descriptor
     * @param name the param name
     * @param desc the param description string
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createParamDesc(String name, String desc) 
    {
        return new JDocDescriptor(PARAM, name, desc);
    } //-- createParamDesc
    
    
    /**
     * Creates a new reference descriptor
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createReferenceDesc() {
        return new JDocDescriptor(REFERENCE);
    } //-- createReferenceDesc
    
    /**
     * Creates a new reference descriptor
     * @param name the reference name string
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createReferenceDesc(String name) {
        return new JDocDescriptor(REFERENCE, name , null);
    } //-- createReferenceDesc
    
    /**
     * Creates a new return descriptor
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createReturnDesc() {
        return new JDocDescriptor(RETURN);
    } //-- createReferenceDesc
    
    /**
     * Creates a new return descriptor
     * @param desc the return description
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createReturnDesc(String desc) {
        return new JDocDescriptor(RETURN, null , desc);
    } //-- createReturnDesc
    
    /**
     * Creates a new version descriptor
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createVersionDesc() {
        return new JDocDescriptor(VERSION);
    } //-- createVersionDesc
    
    /**
     * Creates a new version descriptor
     * @param version the version string
     * @return the new JDocDescriptor    
    **/
    public static JDocDescriptor createVersionDesc(String version) {
        return new JDocDescriptor(VERSION, null, version);
    } //-- createVersionDesc
    
    /**
     * Returns the description String for this descriptor
     * @return the description string for this descriptor
    **/
    public String getDescription() {
        return description;
    } //-- getDescription
    
    /**
     * Returns the name of the object being described. This
     * is valid for the following fields:<br>
     *  <ul>
     *     <li>author</li>
     *     <li>exception</li>
     *     <li>param</li>
     *     <li>see</li>
     *  </ul>
     * @return the name of the object being described. This
    **/
    public String getName() {
        return name;
    } //-- getName
    
    /**
     * Returns the type of this JDocDescriptor
     * @return the type of this JDocDescriptor
    **/
    public short getType() {
        return this.type;
    } //-- getType
    
    /**
     * Sets the description String for this descriptor
     * @param desc the description of the object being described
    **/
    public void setDescription(String desc) {
        this.description = desc;
    } //-- setDescription
    
    
    /**
     * Sets the name value of the JavaDoc field. This is
     * only valid for the following fields:<br>
     *  <ul>
     *     <li>author</li>
     *     <li>exception</li>
     *     <li>param</li>
     *     <li>see</li>
     *  </ul>
     * @param name the name value of the JavaDoc field
    **/
    public void setName(String name) {
        this.name = name;
    } //-- setName
    
    /**
     * Returns the String representation of this JDocDescriptor
     * @return the String representation of this JDocDescriptor
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer();
        boolean allowName = true;
        switch(type) {
            case AUTHOR:
                sb.append("@author");
                break;
            case EXCEPTION:
                sb.append("@exception");
                break;
            case PARAM:
                sb.append("@param");
                break;
            case REFERENCE:
                sb.append("@see");
                break;
            case RETURN:
                sb.append("@return");
                break;
            case VERSION:
                allowName = false;
                sb.append("@version");
                break;
            default:
                break;
        }
        
        if ((name != null) && allowName) {
            sb.append(' ');
            sb.append(name);
        }
        
        if (description != null) {
            sb.append(' ');
            sb.append(description);
        }
        
        return sb.toString();
    } //-- toString
    
} //-- JDocDescriptor
