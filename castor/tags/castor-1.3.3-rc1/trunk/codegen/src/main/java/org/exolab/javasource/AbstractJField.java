/*
 * Copyright 2009 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.javasource;

/**
 * A (abstract) base class which holds information about fields. Modeled closely after the
 * Java Reflection API. This class is part of package which is used to create
 * source code in memory.
 *
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @since 1.3
 */
public class AbstractJField extends JAnnotatedElementHelper implements JMember {

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
    
    /**
     * Creates a new JField.
     * 
     * @param type JType of this new field.
     * @param name Name of this new field.
     */
    public AbstractJField(final JType type, final String name) {
        setName(name);
        
        _type = type;
        
        setModifiers(new JModifiers());
    }

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

    /**
     * {@inheritDoc}
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_modifiers.toString());
        sb.append(' ');
        sb.append(_type);
        sb.append(' ');
        sb.append(_name);
        return sb.toString();
    }

}
