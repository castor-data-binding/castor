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
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.javasource;

/**
 * Represents the set of modifiers for a Method or Member variable
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-02-26 17:30:28 -0700 (Sat, 26 Feb 2005) $
 */
public final class JModifiers {

    /* static members */

    private static final String ABSTRACT   = "abstract";
    private static final String FINAL      = "final";
    private static final String PRIVATE    = "private";
    private static final String PROTECTED  = "protected";
    private static final String PACKAGE    = "";
    private static final String PUBLIC     = "public";
    private static final String STATIC     = "static";
    private static final String TRANSIENT  = "transient";

    private static final short VISIBILITY_PRIVATE   = 1;
    private static final short VISIBILITY_PROTECTED = 2;
    private static final short VISIBILITY_PUBLIC    = 3;
    private static final short VISIBILITY_PACKAGE   = 4;

    /* local members */

    /**
     * The visibility modifier for the object associated with this JModifiers
     */
    private short _visibility = VISIBILITY_PUBLIC;

    /**
     * A flag indicating whether or not the object associated with this
     * JModifiers is static
     */
    private boolean _isStatic = false;
    
    /**
     * A flag indicating whether or not the object associated with this
     * JModifiers is final
     */
    private boolean _isFinal  = false;
    
    /**
     * A flag indicating whether or not the object associated with this
     * JModifiers is abstract
     */
    private boolean _isAbstract = false;
    
    /**
     * A flag indicating whether or not the object associated with this
     * JModifiers is transient
     */
    private boolean _isTransient = false;
    
    /**
     * Creates a new JModifiers class. By default the only modifier present is
     * public.
     */
    public JModifiers() {
        super();
    } //-- JModifiers
    
    /**
     * Creates a new JModifiers instance
     * 
     * @param visibility the visibility qualifier
     * @param isStatic a boolean, if true, indicating that this JModifiers
     *            includes "static" as a qualifier.
     * @param isFinal a boolean, if true, indicating that this JModifiers
     *            includes "final" as a qualifier.
     */
    private JModifiers(final short visibility, final boolean isStatic,
            final boolean isFinal) {
        this._visibility = visibility;
        this._isStatic = isStatic;
        this._isFinal = isFinal;
    } //-- JModifiers
    
    /**
     * Creates a copy of this JModifiers instance
     * 
     * @return a copy of this JModifiers
     */
    public JModifiers copy() {
        JModifiers mods = new JModifiers(_visibility, _isStatic, _isFinal);
        mods.setAbstract(_isAbstract);
        mods.setTransient(_isTransient);
        return mods;
    } //-- copy
    
    /**
     * Changes the visibility qualifier to "private"
     */
    public void makePrivate() {
        this._visibility = VISIBILITY_PRIVATE;
    } //-- makePrivate
    
    /**
     * Changes the visibility qualifier to "protected"
     */
    public void makeProtected() {
        this._visibility = VISIBILITY_PROTECTED;
    } //-- makeProtected

    /**
     * Changes the visibility qualifier to "public"
     */
    public void makePublic() {
        this._visibility = VISIBILITY_PUBLIC;
    } //-- makePublic
    
    /**
     * Changes the visibility qualifier to package (= without qualifier).
     */
    public void makePackage() {
        this._visibility = VISIBILITY_PACKAGE;
    } //-- makePackage
    
    /**
     * Returns true if this JModifiers includes the qualifier "final". This
     * is only applicable to methods and classes.
     * 
     * @return true if this JModifiers includes the qualifier "final". This
     *         is only applicable to methods and classes.
     */
    public boolean isFinal() {
        return _isFinal;
    } //-- isAbstract
    
    /**
     * Returns true if this JModifiers includes the qualifier "abstract". This
     * is only applicable to methods and classes.
     * 
     * @return true if this JModifiers includes the qualifier "abstract". This
     *         is only applicable to methods and classes.
     */
    public boolean isAbstract() {
        return _isAbstract;
    } //-- isAbstract
    
    /**
     * Returns true if the visibility modifier for this JModifier is "private"
     * 
     * @return true if the visibility modifier for this JModifier is "private"
     */
    public boolean isPrivate() {
        return (_visibility == VISIBILITY_PRIVATE);
    } //-- isPrivate
    
    /**
     * Returns true if the visibility modifier for this JModifier is "protected"
     * 
     * @return true if the visibility modifier for this JModifier is "protected"
     */
    public boolean isProtected() {
        return (_visibility == VISIBILITY_PROTECTED);
    } //-- isProtected
    
    /**
     * Returns true if the visibility modifier for this JModifier is "public"
     * 
     * @return true if the visibility modifier for this JModifier is "public"
     */
    public boolean isPublic() {
        return (_visibility == VISIBILITY_PUBLIC);
    } //-- isPublic

    /**
     * Returns true if the visibility modifier for this JModifier is package
     * (i.e., without qualifier)
     * 
     * @return true if the visibility modifier for this JModifier is package
     *         (i.e., without qualifier)
     */
    public boolean isPackage() {
        return (_visibility == VISIBILITY_PACKAGE);
    } //-- isPackage

    /**
     * Returns true if this JModifier includes the qualifier "static"
     * 
     * @return true if this JModifier includes the qualifier "static"
     */
    public boolean isStatic() {
        return this._isStatic;
    } //-- isPublic

    /**
     * Returns true if this JModifier includes the qualifier "transient"
     * 
     * @return true if this JModifier includes the qualifier "transient"
     */
    public boolean isTransient() {
        return this._isTransient;
    } //-- isTransient
    
    /**
     * Sets whether or not this JModifiers includes the qualifier "abstract".
     * This applies only to methods or classes.
     * 
     * @param isAbstract if true, indicates that this JModifier should include
     *            the qualifier "abstract"
     */
    public void setAbstract(final boolean isAbstract) {
        this._isAbstract = isAbstract;
    } //-- setAbstract
    
    /**
     * Sets whether or not this JModifiers includes the qualifier "final"
     * 
     * @param isFinal if true, indicates that this JModifier should include the
     *            qualifier "final"
     */
    public void setFinal(final boolean isFinal) {
        this._isFinal = isFinal;
    } //-- setFinal
    
    /**
     * Sets whether or not this JModifiers includes the qualifier "static"
     * 
     * @param isStatic if true, indicates that this JModifier should include the
     *            qualifier "static"
     */
    public void setStatic(final boolean isStatic) {
        this._isStatic = isStatic;
    } //-- setStatic
    
    /**
     * Sets whether or not this JModifiers includes the qualifier "transient"
     * 
     * @param isTransient is a boolean which when true indicates that this
     *            JModifier should include the qualifier "transient"
     */
    public void setTransient(final boolean isTransient) {
        this._isTransient = isTransient;
    } //-- setTransient
    
    /**
     * Returns the String represetation of this JModifiers, in the order
     * recommended by the Java Language Specification
     * 
     * @return the String represetation of this JModifiers
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        //-- visibility
        switch(_visibility) {
            case VISIBILITY_PRIVATE:
                sb.append(PRIVATE);
                break;
            case VISIBILITY_PROTECTED:
                sb.append(PROTECTED);
                break;
            case VISIBILITY_PACKAGE:
                sb.append(PACKAGE);
                break;
            default:
                sb.append(PUBLIC);
                break;
        }
        
        //-- static
        if (_isStatic) {
            if (sb.length() > 0)  {
                sb.append(' ');
            }
            sb.append(STATIC);
        }
        
        //-- final
        if (_isFinal) {
            if (sb.length() > 0)  {
                sb.append(' ');
            }
            sb.append(FINAL);
        }

        //-- abstract
        if (_isAbstract) {
            if (sb.length() > 0)  {
                sb.append(' ');
            }
            sb.append(ABSTRACT);
        }

        //-- transient
        if (_isTransient) {
            if (sb.length() > 0)  {
                sb.append(' ');
            }
            sb.append(TRANSIENT);
        }

        
        return sb.toString();
    } //-- toString
    
} //-- JModifiers
