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

package org.exolab.javasource;


import java.util.Vector;

/**
 * The set of modifiers for a Method or Member variable
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class JModifiers {
    
    /* static members */
    
    private static final String sFinal      = "final";
    private static final String sPrivate    = "private";
    private static final String sProtected  = "protected";
    private static final String sPublic     = "public";
    private static final String sStatic     = "static";
    
    
    private static final short vPrivate   = 1;
    private static final short vProtected = 2;
    private static final short vPublic    = 3;
    
    /* local members */
    
    /**
     * The visibility
    **/
    private short visibility = vPublic;
    
    /**
     * A flag indicating the whether the object associated
     * with this JModifiers is static
    **/
    boolean isStatic = false;
    
    /**
     * A flag indicating the whether the object associated
     * with this JModifiers is final
    **/
    boolean isFinal  = false;
    
    /**
     * Creates a new JModifiers class, by default the
     * modifiers presented are public.
    **/
    public JModifiers() {
        super();
    } //-- JModifiers
    
    /**
     * Creates a new JModifiers 
     * @param visibility the visibile qualifier
     * @param isStatic a boolean indicating the static qualifier.
     * A value of true indicates that this static qualifier is present.
     * @param isFinal a boolean indicating the final qualifier. A value
     * of true indicates that the final qualifier is present.
    **/
    private JModifiers(short visibility, boolean isStatic, boolean isFinal) {
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
    } //-- JModifiers
    
    /**
     * Creates a copy of this JModifiers
     * @return the copy of this JModifiers
    **/
    public JModifiers copy() {
        return new JModifiers(visibility, isStatic, isFinal);
    } //-- copy
    
    /**
     * Changes the visibility qualifier to "private"
    **/
    public void makePrivate() {
        this.visibility = vPrivate;
    } //-- makePrivate
    
    /**
     * Changes the visibility qualifier to "protected"
    **/
    public void makeProtected() {
        this.visibility = vProtected;
    } //-- makeProtected

    /**
     * Changes the visibility qualifier to "public"
    **/
    public void makePublic() {
        this.visibility = vPublic;
    } //-- makePublic
    
    /**
     * Returns true if the modifier represented is private.
     * @return true if the modifier represented is private.
    **/
    public boolean isPrivate() {
        return (visibility == vPrivate);
    } //-- isPrivate
    
    /**
     * Returns true if the modifier represented is protected.
     * @return true if the modifier represented is protected.
    **/
    public boolean isProtected() {
        return (visibility == vProtected);
    } //-- isProtected
    
    /**
     * Returns true if the modifier represented is public.
     * @return true if the modifier represented is public.
    **/
    public boolean isPublic() {
        return (visibility == vPublic);
    } //-- isPublic
    
    /**
     * Sets whether or not the "final" qualifier is present
     * @param isFinal is a boolean which when true will indicate
     * the final qualifiter is present
    **/
    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    } //-- setFinal
    
    /**
     * Sets whether or not the "static" qualifier is present
     * @param isStatic is a boolean which when true will indicate
     * the "static" qualifiter is present
    **/
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    } //-- setStatic
    
    /**
     * Returns the String represetation of this JModifiers
     * @return the String represetation of this JModifiers
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        //-- visibility
        switch(visibility) {
            case vPrivate:
                sb.append(sPrivate);
                break;
            case vProtected:
                sb.append(sProtected);
                break;
            default:
                sb.append(sPublic);
                break;
        }
        
        //-- final
        if (isFinal) {
            sb.append(' ');
            sb.append(sFinal);
        }
        
        //-- static
        if (isStatic) {
            sb.append(' ');
            sb.append(sStatic);
        }
        
        return sb.toString();
    } //-- toString
    
} //-- JMember
