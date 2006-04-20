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
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class JMember {

    /**
     * The set of modifiers for this JMember
    **/
    private JModifiers modifiers = null;

    private JType type = null;

    private String name = null;

    private JDocComment comment = null;

    private String initString = null;
    
    /**
     * The Class in this JMember has been declared
    **/
    private JClass declaringClass = null;

    public JMember(JType type, String name) {
        
        this.type = type;
        this.name = name;
        this.modifiers = new JModifiers();
        this.modifiers.makePrivate();
        
    } //-- JMember

    

    /**
     * Returns the comment describing this member. 
     * @return the comment describing this member, or
     * null if no comment has been set.
    **/
    public JDocComment getComment() {
        return this.comment;
    } //-- getComment

    

    /**
     * Returns the class in which this JMember has been declared
     * @return the class in which this JMember has been declared
    **/
    public JClass getDeclaringClass() {
        return this.declaringClass;
    } //-- getDeclaringClass

    /**
     * Returns the initialization String for this JMember
     * @return the initialization String for this JMember,
     * or null if no initialization String was specified.
    **/
    public String getInitString() {
        return initString;
    } //-- getInitString
    
     
    /**
     * Returns the modifiers for this JMember
     * @return the modifiers for this JMember     
    **/
    public JModifiers getModifiers() {
        return this.modifiers;
    } //-- getModifiers

    /**
     * Returns the name of this JMember
     * @return the name of this JMember
    **/
    public String getName() {
        return this.name;
    } //-- getName

    /**
     * Returns the JType represting the type of this JMember
     * @return the JClass represting the type of this JMember
    **/
    public JType getType() {
        return this.type;
    } //-- getType

    /**
     * Sets the comment describing this member. 
     * @param comment the JDocComment for this member
    **/
    public void setComment(JDocComment comment) {
        this.comment = comment;
    } //-- setComment

    /**
     * Sets the comment describing this member. 
     * @param comment the JDocComment for this member
    **/
    public void setComment(String comment) {
        if (this.comment == null) {
            this.comment = new JDocComment();
        }
        this.comment.setComment(comment);
    } //-- setComment

    /**
     * Sets the initialization string for this JMember;
     * Allows some flexibility in declaring default values.
     * @param init the initialization string for this member.
    **/
    public void setInitString(String init) {
        this.initString = init;
    } //-- setInitString
    
    /**
     * Sets the name of this JMember
     * @param name the name of this JMember
     * @exception IllegalArgumentException when the
     * name is not a valid Java member name, or if a member
     * with the given name already exists in the declaring class
    **/
    public void setName(String name) throws 
        IllegalArgumentException
    {
        this.name = name;
    } //-- setName

    public void setModifiers(JModifiers modifiers) {
        this.modifiers = modifiers;
    } //-- setModifiers

    protected void setDeclaringClass(JClass declaringClass) {
        this.declaringClass = declaringClass;
    } //-- setDeclaringClass
   
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(modifiers.toString());
        sb.append(' ');
        sb.append(type);
        sb.append(' ');
        sb.append(name);
        return sb.toString();
    } //-- toString

} //-- JMember

