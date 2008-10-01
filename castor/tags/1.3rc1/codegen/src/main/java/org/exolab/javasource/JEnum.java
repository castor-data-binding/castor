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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 */
package org.exolab.javasource;

import java.util.Enumeration;

/**
 * Describes the definition of a enum type class.
 *
 * @author <a href="mailto:andrew DOT fawcett AT coda DOT com">Andrew Fawcett</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class JEnum extends JClass {
    //--------------------------------------------------------------------------

    /** The list of elements of this JEnumConstant. */
    private JNamedMap _constants;

    //--------------------------------------------------------------------------

    /**
     * Construct JEnum with given name.
     * 
     * @param name The name for this JEnum.
     */
    public JEnum(final String name) {
        super(name);
        
        _constants = new JNamedMap();
        
        //-- initialize default Java doc
        getJDocComment().setComment("Enumeration " + getLocalName() + ".");
    }
    
    /**
     * Override to only allow private constructors. 
     * @param constructor The constructor that should be added.
     */
    public void addConstructor(final JConstructor constructor) {
        if (constructor.getModifiers().isPrivate()) {   
            super.addConstructor(constructor);
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Adds the given JMember to this JEnum.
     *
     * @param jMember The JMember to add.
     */
    public void addMember(final JMember jMember) {
        if (jMember instanceof JEnumConstant) {
            addConstant((JEnumConstant) jMember);
        } else if (jMember instanceof JField) {
            addField((JField) jMember);
        } else if (jMember instanceof JMethod) {
            addMethod((JMethod) jMember);
        } else {
            String error = null;
            if (jMember == null) {
                error = "the argument 'jMember' must not be null.";
            } else {
                error = "Cannot add JMember '" + jMember.getClass().getName()
                      + "' to JClass, unrecognized type.";
            }
            throw new IllegalArgumentException(error);
        }
    }

    /**
     * Adds the given JEnumConstant to this JEnum.
     *
     * @param jConstant The constant to add.
     */
    public void addConstant(final JEnumConstant jConstant) {
        if (jConstant == null) {
            throw new IllegalArgumentException("Enum fields cannot be null");
        }

        String name = jConstant.getName();
        if (_constants.get(name) != null) {
            String err = "duplicate name found: " + name;
            throw new IllegalArgumentException(err);
        }
        _constants.put(name, jConstant);
    }

    /**
     * Returns the member with the given name, or null if no member was found
     * with the given name.
     *
     * @param name The name of the member to return.
     * @return The member with the given name, or null if no member was found
     *         with the given name.
     */
    public JEnumConstant getConstant(final String name) {
        return (JEnumConstant) _constants.get(name);
    }

    /**
     * Returns an array of all the JEnumConstant of this JEnum.
     *
     * @return An array of all the JEnumConstant of this JEnum.
     */
    public JEnumConstant[] getConstants() {
        int size = _constants.size();
        JEnumConstant[] farray = new JEnumConstant[size];
        for (int i = 0; i < size; i++) {
            farray[i] = (JEnumConstant) _constants.get(i);
        }
        return farray;
    }
    
    /**
     * Returns the amount of enum constants.
     * 
     * @return The amount of enum constants.
     */
    public int getConstantCount() {
        return this._constants.size();
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void print(final JSourceWriter jsw, final boolean classOnly) {
        if (jsw == null) {
            
            throw new IllegalArgumentException("argument 'jsw' should not be null.");
        }

        //-- print class headers (comment header, package, imports) if desired
        if (!classOnly) {
            printClassHeaders(jsw);
        }

        getJDocComment().print(jsw);

        printEnumDefinitionLine(jsw); // includes the opening '{'
        
        jsw.writeln();
        jsw.indent();

        printEnumConstants(jsw);
        printMemberVariables(jsw);
        printStaticInitializers(jsw);
        printConstructors(jsw);
        printMethods(jsw);
        printInnerClasses(jsw);

        jsw.unindent();
        jsw.writeln('}');
        jsw.flush();
    }

    /**
     * Writes to the JSourceWriter the line that defines this enum. This
     * line includes the enum name, implements entries, and
     * any modifiers such as "private".
     * 
     * @param jsw The JSourceWriter to be used.
     */
    private void printEnumDefinitionLine(final JSourceWriter jsw) {
        StringBuilder buffer = new StringBuilder();

        //-- print annotations
        getAnnotatedElementHelper().printAnnotations(jsw);

        JModifiers modifiers = getModifiers();
        if (modifiers.isPrivate()) {
            buffer.append("private ");
        } else if (modifiers.isPublic()) {
            buffer.append("public ");
        }

        buffer.append("enum ");
        buffer.append(getLocalName());
        buffer.append(' ');
        if (getInterfaceCount() > 0) {
            boolean endl = false;
            if ((getInterfaceCount() > 1)) {
                jsw.writeln(buffer.toString());
                buffer.setLength(0);
                endl = true;
            }
            buffer.append("implements ");

            Enumeration enumeration = getInterfaces();
            while (enumeration.hasMoreElements()) {
                buffer.append(enumeration.nextElement());
                if (enumeration.hasMoreElements()) { buffer.append(", "); }
            }
            if (endl) {
                jsw.writeln(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append(' ');
            }
        }

        buffer.append('{');
        jsw.writeln(buffer.toString());
        buffer.setLength(0);
    }

    /**
     * Writes to the JSourceWriter all constants belonging to this enum.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    private void printEnumConstants(final JSourceWriter jsw) {
        if (_constants.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //------------------/");
            jsw.writeln(" //- Enum Constants -/");
            jsw.writeln("//------------------/");
            jsw.writeln();
        }
        for (int i = 0; i < _constants.size(); i++) {
            JEnumConstant jConstant = (JEnumConstant) _constants.get(i);
            jConstant.print(jsw);
            if (i < _constants.size() - 1) {
                jsw.write(",");
            } else {
                jsw.write(";");
            }
            jsw.writeln();
        }
    }

    //--------------------------------------------------------------------------
}
