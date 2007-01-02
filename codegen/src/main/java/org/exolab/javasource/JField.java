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
 */
package org.exolab.javasource;

/**
 * A class which holds information about a field. Modelled closely after the
 * Java Reflection API. This class is part of package which is used to create
 * source code in memory.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-02-26 17:30:28 -0700 (Sat, 26 Feb 2005) $
 */
public final class JField extends JAnnotatedElementHelper implements JMember {
    //--------------------------------------------------------------------------

    /** The set of modifiers for this JField. */
    private JModifiers _modifiers = null;
    
    /** Type of this field. */
    private final JType _type;
    
    /** Name of the field. */
    private String _name = null;
    
    /** JavaDoc for this field. */
    private JDocComment _comment = null;
    
    /** Initialization string for this field. */
    private String _initString = null;
    
    /** Indicates whether this field is of type date/time. */
    private boolean _isDateTime = false;
    
    /** The Class in this JField has been declared. */
    private JClass _declaringClass = null;
    

    //--------------------------------------------------------------------------
    /**
     * Creates a new JField.
     * 
     * @param type JType of this new field.
     * @param name Name of this new field.
     */
    public JField(final JType type, final String name) {
        setName(name);
        
        _type = type;
        _modifiers = new JModifiers();
        _modifiers.makePrivate();
        _comment = new JDocComment();
        _comment.appendComment("Field " + name);
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the JavaDoc comment describing this member.
     *
     * @return The JavaDoc comment describing this member, or null if no comment
     *         has been set.
     */
    public JDocComment getComment() {
        return _comment;
    }

    /**
     * Returns the class in which this JField has been declared.
     *
     * @return The class in which this JField has been declared.
     */
    public JClass getDeclaringClass() {
        return _declaringClass;
    }

    /**
     * Returns the initialization String for this JField.
     *
     * @return The initialization String for this JField, or null if no
     *         initialization String was specified.
     */
    public String getInitString() {
        return _initString;
    }

    /**
     * Returns the modifiers for this JField.
     *
     * @return The modifiers for this JField.
     */
    public JModifiers getModifiers() {
        return _modifiers;
    }

    /**
     * Returns the name of this JField.
     *
     * @return The name of this JField.
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the JType representing the type of this JField.
     *
     * @return The JType representing the type of this JField.
     */
    public JType getType() {
        return _type;
    }

    /**
     * Sets the JavaDoc comment describing this JField.
     *
     * @param comment The JavaDoc comment for this JField.
     */
    public void setComment(final JDocComment comment) {
        _comment = comment;
    }

    /**
     * Sets the JavaDoc comment describing this JField.
     *
     * @param comment The JavaDoc comment for this JField.
     */
    public void setComment(final String comment) {
        if (_comment == null) {
            _comment = new JDocComment();
        }
        _comment.setComment(comment);
    }

    /**
     * Sets the initialization string for this JField. This allows some
     * flexibility in declaring default values.
     *
     * @param init The initialization string for this member.
     */
    public void setInitString(final String init) {
        _initString = init;
    }

    /**
     * Sets the name of this JField.
     *
     * @param name The name of this JField.
     */
    public void setName(final String name) {
        if (!JNaming.isValidJavaIdentifier(name)) {
            String err = "'" + name + "' is ";
            if (JNaming.isKeyword(name)) {
                err += "a reserved word and may not be used as "
                    + " a field name.";
            } else {
                err += "not a valid Java identifier.";
            }
            throw new IllegalArgumentException(err);
        }
        _name = name;
    }

    /**
     * Sets the access modifiers on this JField.
     *
     * @param modifiers The access modifiers to be used for this JField.
     */
    public void setModifiers(final JModifiers modifiers) {
        _modifiers = modifiers;
    }

    /**
     * Sets the class that declares this JField.
     *
     * @param declaringClass The class in which this Jfield is declared.
     */
    protected void setDeclaringClass(final JClass declaringClass) {
        _declaringClass = declaringClass;
    }

    /**
     * Indicates whether this JField instance represents a field of type date/time.
     * 
     * @return True if this field is of type date/time.
     */
    public boolean isDateTime() {
        return _isDateTime;
    }

    /**
     * To indicate whether this JField instance represents a field of type date/time.
     * 
     * @param isDateTime True if this field is of type date/time.
     */
    public void setDateTime(final boolean isDateTime) {
        _isDateTime = isDateTime;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(_modifiers.toString());
        sb.append(' ');
        sb.append(_type);
        sb.append(' ');
        sb.append(_name);
        return sb.toString();
    }

    //--------------------------------------------------------------------------
}
