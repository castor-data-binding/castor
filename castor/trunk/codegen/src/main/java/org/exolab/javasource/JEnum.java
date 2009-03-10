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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Describes the definition of a enum type class.
 *
 * @author <a href="mailto:andrew DOT fawcett AT coda DOT com">Andrew Fawcett</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class JEnum extends JClass {

    /** The list of elements of this JEnumConstant. */
    private Map<String, JEnumConstant> _enumConstants = new LinkedHashMap<String, JEnumConstant>();

    /**
     * Construct JEnum with given name.
     * 
     * @param name The name for this JEnum.
     */
    public JEnum(final String name) {
        super(name);
        
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
     * Adds the given {@link JMember} to this {@link JEnum}.
     *
     * @param jMember The {@link JMember} to add.
     */
    public void addMember(final JMember jMember) {
        if (jMember instanceof JEnumConstant) {
            addEnumConstant((JEnumConstant) jMember);
        } else { 
            super.addMember(jMember);
        }
    }

    /**
     * Adds the given {@link JEnumConstant} to this {@link JEnum}.
     *
     * @param jEnumConstant The constant to add.
     */
    public void addEnumConstant(final JEnumConstant jEnumConstant) {
        if (jEnumConstant == null) {
            throw new IllegalArgumentException("Enum fields cannot be null");
        }

        String name = jEnumConstant.getName();
        if (_enumConstants.get(name) != null) {
            String err = "duplicate name found: " + name;
            throw new IllegalArgumentException(err);
        }
        _enumConstants.put(name, jEnumConstant);
    }

    /**
     * Returns the member with the given name, or null if no member was found
     * with the given name.
     *
     * @param name The name of the member to return.
     * @return The member with the given name, or null if no member was found
     *         with the given name.
     */
    public JEnumConstant getEnumConstant(final String name) {
        return _enumConstants.get(name);
    }

    /**
     * Returns an array of all the JEnumConstant of this JEnum.
     *
     * @return An array of all the JEnumConstant of this JEnum.
     */
    public JEnumConstant[] getEnumConstants() {
        return _enumConstants.values().toArray(new JEnumConstant[_enumConstants.size()]);
    }
    
    /**
     * Returns the number of enum constants.
     * 
     * @return The number of enum constants.
     */
    public int getEnumConstantCount() {
        return this._enumConstants.size();
    }

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

            Enumeration<String> enumeration = getInterfaces();
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
        if (!_enumConstants.isEmpty()) {
            jsw.writeln();
            jsw.writeln("  //------------------/");
            jsw.writeln(" //- Enum Constants -/");
            jsw.writeln("//------------------/");
            jsw.writeln();
        }
        
        int i = 0;
        for (JEnumConstant jConstant : _enumConstants.values()) {
            i++;
            jConstant.print(jsw);
            if (i < _enumConstants.size()) {
                jsw.write(",");
            } else {
                jsw.write(";");
            }
            jsw.writeln();
        }
    }

}
