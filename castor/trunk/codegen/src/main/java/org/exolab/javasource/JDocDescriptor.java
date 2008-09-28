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
 */
package org.exolab.javasource;

/**
 * A descriptor for a JavaDoc comment.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public final class JDocDescriptor {
    //--------------------------------------------------------------------------

    /** The default version string, broken into parts so CVS does not expand it. */
    public static final String DEFAULT_VERSION = "$" + "Revision" + "$ $" + "Date" + "$";

    // These are listed in order of how they should appear in a JavaDoc
    // list, so the numbers are important...see #compareTo
    
    /** The param descriptor (param). */
    public static final short PARAM = 0;

    /** The exception descriptor (exception). */
    public static final short EXCEPTION = 1;

    /** The return descriptor (return). */
    public static final short RETURN = 2;

    /** The author descriptor. */
    public static final short AUTHOR = 3;

    /** The version descriptor (version). */
    public static final short VERSION = 4;

    /** The reference descriptor (see). */
    public static final short REFERENCE = 5;

    //--------------------------------------------------------------------------

    /** A description string for the object being described. */
    private String _description = null;

    /** The name of this JDocDescriptor. */
    private String _name = null;

    /** The type of JDocDescriptor, one of {@link #PARAM}, {@link #EXCEPTION},
     *  {@link #RETURN}, {@link #AUTHOR}, {@link #VERSION}, {@link #REFERENCE}. */
    private short _type = -1;

    //--------------------------------------------------------------------------

    /**
     * Creates a new JDocDescriptor.
     *
     * @param type The type of JDocDescriptor (e.g., {@link #REFERENCE}.
     */
    private JDocDescriptor(final short type) {
        _type = type;
    }

    /**
     * Creates a new JDocDescriptor.
     *
     * @param type The type of JDocDescriptor (e.g., {@link #REFERENCE}.
     * @param name The name string for this descriptor.
     * @param desc The description string for this descriptor.
     */
    private JDocDescriptor(final short type, final String name, final String desc) {
        _type = type;
        _name = name;
        _description = desc;
    }

    //--------------------------------------------------------------------------

    /**
     * Compares the type of this JDocDescriptor with the given descriptor.
     * Enables sorting of descriptors.
     *
     * @param jdd A JDocDescriptor to be compared to this one.
     * @return 0 if the two descriptor types are equal, 1 if the type of this
     *         descriptor is greater than the given descriptor, or -1 if the
     *         type of this descriptor is less than the given descriptor.
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
    }

    /**
     * Creates a new, empty &#064;author JavaDoc descriptor.
     *
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createAuthorDesc() {
        return new JDocDescriptor(AUTHOR);
    }

    /**
     * Creates a new &#064;author JavaDoc descriptor with the provided author name
     * string.
     *
     * @param name The author name string.
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createAuthorDesc(final String name) {
        return new JDocDescriptor(AUTHOR, name, null);
    }

    /**
     * Creates a new, empty &#064;exception JavaDoc descriptor.
     *
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createExceptionDesc() {
        return new JDocDescriptor(EXCEPTION);
    }

    /**
     * Creates a new &#064;exception JavaDoc descriptor with a given exception
     * name and a description of when the exception is thrown.
     *
     * @param name The exception name.
     * @param desc The description of when the exception is thrown.
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createExceptionDesc(final String name, final String desc) {
        return new JDocDescriptor(EXCEPTION, name, desc);
    }

    /**
     * Creates a new, empty &#064;param JavaDoc descriptor.
     *
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createParamDesc() {
        return new JDocDescriptor(PARAM);
    }

    /**
     * Creates a new &#064;param JavaDoc descriptor with the given parameter
     * name and description.
     *
     * @param name The param name.
     * @param desc The param description string.
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createParamDesc(final String name, final String desc) {
        return new JDocDescriptor(PARAM, name, desc);
    }

    /**
     * Creates a new, empty &#064;reference JavaDoc descriptor.
     *
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createReferenceDesc() {
        return new JDocDescriptor(REFERENCE);
    }

    /**
     * Creates a new &#064;reference JavaDoc descriptor with the provided
     * reference string.
     *
     * @param name The reference name string.
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createReferenceDesc(final String name) {
        return new JDocDescriptor(REFERENCE, name , null);
    }

    /**
     * Creates a new, empty &#064;return JavaDoc descriptor.
     *
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createReturnDesc() {
        return new JDocDescriptor(RETURN);
    }

    /**
     * Creates a new &#064;return JavaDoc descriptor with the provided
     * description of what is returned.
     *
     * @param desc The return description.
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createReturnDesc(final String desc) {
        return new JDocDescriptor(RETURN, null , desc);
    }

    /**
     * Creates a new, empty &#064;version JavaDoc descriptor.
     *
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createVersionDesc() {
        return new JDocDescriptor(VERSION);
    }

    /**
     * Creates a new &#064;version JavaDoc descriptor with the provided version
     * string.
     *
     * @param version The version string.
     * @return The new JDocDescriptor.
     */
    public static JDocDescriptor createVersionDesc(final String version) {
        return new JDocDescriptor(VERSION, null, version);
    }

    /**
     * Returns the description String.
     *
     * @return The description string.
     */
    public String getDescription() {
        return _description;
    }

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
     * @return The name of the object being described.
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the type of this JDocDescriptor.
     *
     * @return The type of this JDocDescriptor.
     */
    public short getType() {
        return _type;
    }

    /**
     * Sets the description String for this descriptor.
     *
     * @param desc The description of the object being described.
     */
    public void setDescription(final String desc) {
        _description = desc;
    }


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
     * @param name The name value of the JavaDoc field.
     */
    public void setName(final String name) {
        _name = name;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
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
    }

    //--------------------------------------------------------------------------
}
