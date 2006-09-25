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
 * A descriptor for a JavaDoc comment.
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public final class JDocDescriptor {
    
    /**
     * The default version string, broken into parts so CVS does not expand it.
     */
    public static final String DEFAULT_VERSION = "$" + "Revision" + "$ $" + "Date" + "$";
        
    //-- These are listed in order of how they should appear in a JavaDoc
    //-- list, so the numbers are important...see #compareTo
    /**
     * The param descriptor (param)
     */
    public static final short PARAM      = 0;
    
    /**
     * The exception descriptor (exception)
     */
    public static final short EXCEPTION  = 1;
    
    /**
     * The return descriptor (return)
     */
    public static final short RETURN     = 2;
    
    /**
     * The author descriptor
     */
    public static final short AUTHOR     = 3;
    
    /**
     * the version descriptor (version)
     */
    public static final short VERSION    = 4;
    
    /**
     * The reference descriptor (see)
     */
    public static final short REFERENCE  = 5;
    
    /**
     * A description string for the object being described
     */
    private String _description = null;
    
    /**
     * The name of this JDocDescriptor
     */
    private String _name = null;
    
    /**
     * The type of JDocDescriptor, one of {@link #PARAM}, {@link #EXCEPTION},
     * {@link #RETURN}, {@link #AUTHOR}, {@link #VERSION}, {@link #REFERENCE}
     */
    private short  _type = -1;
    
    /**
     * Creates a new JDocDescriptor
     * 
     * @param type the type of JDocDescriptor (e.g., {@link #REFERENCE}
     */
    private JDocDescriptor(final short type) {
        this._type = type;
    } //-- JDocDescriptor
    
    /**
     * Creates a new JDocDescriptor
     * 
     * @param type the type of JDocDescriptor (e.g., {@link #REFERENCE}
     * @param name the name string for this descriptor
     * @param desc the description string for this descriptor
     */
    private JDocDescriptor(final short type, final String name, final String desc) {
        this._type        = type;
        this._name        = name;
        this._description = desc;
    } //-- JDocDescriptor

    /**
     * Compares the type of this JDocDescriptor with the given descriptor.
     * Enables sorting of descriptors.
     *
     * @param jdd a JDocDescriptor to be compared to this one
     * @return 0 if the two descriptor types are equal, 1 if the type of this
     *         descriptor is greater than the given descriptor, or -1 if the
     *         type of this descriptor is less than the given descriptor
     */
    protected short compareTo(final JDocDescriptor jdd) {
        short jddType = jdd.getType();

        if (jddType == this._type) { return 0; }

        // The ordering is as follows
        // #param
        // #exception
        // #author
        // #version
        // #see (reference)
        //
        return (short) ((jddType < this._type) ? 1 : -1);
    } //-- compareTo

    /**
     * Creates a new, empty &#064;author JavaDoc descriptor
     *
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createAuthorDesc() {
        return new JDocDescriptor(AUTHOR);
    } //-- createAuthorDesc

    /**
     * Creates a new &#064;author JavaDoc descriptor with the provided author name
     * string
     *
     * @param name the author name string
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createAuthorDesc(final String name) {
        return new JDocDescriptor(AUTHOR, name, null);
    } //-- createAuthorDesc

    /**
     * Creates a new, empty &#064;exception JavaDoc descriptor
     *
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createExceptionDesc() {
        return new JDocDescriptor(EXCEPTION);
    } //-- createExceptionDesc

    /**
     * Creates a new &#064;exception JavaDoc descriptor with a given exception
     * name and a description of when the exception is thrown
     *
     * @param name the exception name
     * @param desc the description of when the exception is thrown
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createExceptionDesc(
            final String name, final String desc) {
        return new JDocDescriptor(EXCEPTION, name, desc);
    } //-- createExceptionDesc


    /**
     * Creates a new, empty &#064;param JavaDoc descriptor
     *
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createParamDesc() {
        return new JDocDescriptor(PARAM);
    } //-- createParamDesc

    /**
     * Creates a new &#064;param JavaDoc descriptor with the given parameter
     * name and description
     *
     * @param name the param name
     * @param desc the param description string
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createParamDesc(final String name, final String desc) {
        return new JDocDescriptor(PARAM, name, desc);
    } //-- createParamDesc

    /**
     * Creates a new, empty &#064;reference JavaDoc descriptor
     *
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createReferenceDesc() {
        return new JDocDescriptor(REFERENCE);
    } //-- createReferenceDesc

    /**
     * Creates a new &#064;reference JavaDoc descriptor with the provided
     * reference string
     *
     * @param name the reference name string
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createReferenceDesc(final String name) {
        return new JDocDescriptor(REFERENCE, name , null);
    } //-- createReferenceDesc

    /**
     * Creates a new, empty &#064;return JavaDoc descriptor
     *
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createReturnDesc() {
        return new JDocDescriptor(RETURN);
    } //-- createReferenceDesc

    /**
     * Creates a new &#064;return JavaDoc descriptor with the provided
     * description of what is returned
     *
     * @param desc the return description
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createReturnDesc(final String desc) {
        return new JDocDescriptor(RETURN, null , desc);
    } //-- createReturnDesc

    /**
     * Creates a new, empty &#064;version JavaDoc descriptor
     *
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createVersionDesc() {
        return new JDocDescriptor(VERSION);
    } //-- createVersionDesc

    /**
     * Creates a new &#064;version JavaDoc descriptor with the provided version
     * string
     *
     * @param version the version string
     * @return the new JDocDescriptor
     */
    public static JDocDescriptor createVersionDesc(final String version) {
        return new JDocDescriptor(VERSION, null, version);
    } //-- createVersionDesc

    /**
     * Returns the description String
     *
     * @return the description string
     */
    public String getDescription() {
        return _description;
    } //-- getDescription

    /**
     * Returns the name of the object being described. This is valid for the
     * following fields:
     * <ul>
     *   <li>author</li>
     *   <li>exception</li>
     *   <li>param</li>
     *   <li>see</li>
     * </ul>
     *
     * @return the name of the object being described. This
     */
    public String getName() {
        return _name;
    } //-- getName

    /**
     * Returns the type of this JDocDescriptor
     *
     * @return the type of this JDocDescriptor
     */
    public short getType() {
        return this._type;
    } //-- getType

    /**
     * Sets the description String for this descriptor
     *
     * @param desc the description of the object being described
     */
    public void setDescription(final String desc) {
        this._description = desc;
    } //-- setDescription


    /**
     * Sets the name value of the JavaDoc field. This is only valid for the
     * following fields:
     * <ul>
     *   <li>author</li>
     *   <li>exception</li>
     *   <li>param</li>
     *   <li>see</li>
     * </ul>
     *
     * @param name the name value of the JavaDoc field
     */
    public void setName(final String name) {
        this._name = name;
    } //-- setName

    /**
     * Returns the String representation of this JDocDescriptor
     *
     * @return the String representation of this JDocDescriptor
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        boolean allowName = true;
        switch(_type) {
            case AUTHOR:
                sb.append("@author");
                break;
            case EXCEPTION:
                sb.append("@throws");
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

        if ((_name != null) && allowName) {
            sb.append(' ');
            sb.append(_name);
        }

        if (_description != null) {
            sb.append(' ');
            sb.append(_description);
        }

        return sb.toString();
    } //-- toString

} //-- JDocDescriptor
