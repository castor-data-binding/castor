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

import java.util.Vector;

/**
 * Describes the definition of a enum constant.
 *
 * @author <a href="mailto:andrew DOT fawcett AT coda DOT com">Andrew Fawcett</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class JEnumConstant extends JAnnotatedElementHelper implements JMember {
    //--------------------------------------------------------------------------

    /**  Name of this JEnumConstant. */
    private String _name;
    
    /** Array of arguments provided to this JEnumConstant at initialization. May be null. */
    private String[] _arguments;
    
    /** JavaDoc comment for this JEnumConstant. */
    private JDocComment _comment;
    
    /** A list of methods attached to this JEnumConstant. */
    private Vector _methods = null;

    //--------------------------------------------------------------------------

    /**
     * Constructs a JEnumConstant with a given name and no initialization
     * arguements.
     *
     * @param name Name of the constant.
     */
    public JEnumConstant(final String name) {
        this(name, null);
    }

    /**
     * Constructs a JEnumConstant with a given name and initialization arguments.
     *
     * @param name Name of the constant.
     * @param arguments The initialization arguments provided.
     */
    public JEnumConstant(final String name, final String[] arguments) {
        setName(name);
        
        _methods = new Vector();
        _comment = new JDocComment();
        _comment.appendComment("Constant " + name);
        _arguments = arguments;
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the modifiers for this JEnumConstant.
     *
     * @return The modifiers for this JEnumConstant.
     */
    public JModifiers getModifiers() {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Sets the arguments specified by this constant.
     *
     * @param args Initialization arguments for this constant.
     */
    public void setArguments(final String[] args) {
        _arguments = args;
    }

    /**
     * Returns the arguments used by this constant.
     *
     * @return The arguments used by this constant.
     */
    public String[] getArguments() {
        return _arguments;
    }

    /**
     * Adds the given JMethod to this JEnumConstant.
     *
     * @param jMethod The JMethod to add.
     */
    public void addMethod(final JMethod jMethod) {
         addMethod(jMethod, true);
    }

    /**
     * Adds the given JMethod to this JEnumConstant.
     *
     * @param jMethod The JMethod to add.
     * @param importReturnType True if we add the importReturnType to the class
     *        import lists. It could be useful to set it to false when all
     *        types are fully qualified.
     */
    public void addMethod(final JMethod jMethod, final boolean importReturnType) {
        if (jMethod == null) {
            throw new IllegalArgumentException("Class methods cannot be null");
        }

        //-- check method name and signatures *add later*

        //-- keep method list sorted for esthetics when printing
        //-- START SORT :-)
        boolean added = false;
        JModifiers modifiers = jMethod.getModifiers();

        if (modifiers.isAbstract()) {
            getModifiers().setAbstract(true);
        }

        for (int i = 0; i < _methods.size(); i++) {
            JMethod tmp = (JMethod) _methods.elementAt(i);
            //-- first compare modifiers
            if (tmp.getModifiers().isPrivate()) {
                if (!modifiers.isPrivate()) {
                    _methods.insertElementAt(jMethod, i);
                    added = true;
                    break;
                }
            }
            //-- compare names
            if (jMethod.getName().compareTo(tmp.getName()) < 0) {
                    _methods.insertElementAt(jMethod, i);
                    added = true;
                    break;
            }
        }
        //-- END SORT
        if (!added) { _methods.addElement(jMethod); }
    }

    /**
     * Adds the given array of JMethods to this JEnumConstant.
     *
     * @param jMethods The array of JMethod to add.
     */
    public void addMethods(final JMethod[] jMethods) {
        for (int i = 0; i < jMethods.length; i++) {
            addMethod(jMethods[i]);
        }
    }

    /**
     * Returns an array of all the JMethods of this JEnumConstant.
     *
     * @return An array of all the JMethods of this JEnumConstant.
     */
    public JMethod[] getMethods() {
        int size = _methods.size();
        JMethod[] marray = new JMethod[size];

        for (int i = 0; i < _methods.size(); i++) {
            marray[i] = (JMethod) _methods.elementAt(i);
        }
        return marray;
    }

    /**
     * Returns the first occurance of the method with the given name, starting
     * from the specified index.
     *
     * @param name The name of the method to look for.
     * @param startIndex The starting index to begin the search.
     * @return The method if found, otherwise null.
     */
    public JMethod getMethod(final String name, final int startIndex) {
        for (int i = startIndex; i < _methods.size(); i++) {
            JMethod jMethod = (JMethod) _methods.elementAt(i);
            if (jMethod.getName().equals(name)) { return jMethod; }
        }
        return null;
    }

    /**
     * Returns the JMethod located at the specified index.
     *
     * @param index The index of the JMethod to return.
     * @return The JMethod.
     */
    public JMethod getMethod(final int index) {
        return (JMethod) _methods.elementAt(index);
    }

    /**
     * Sets the name of this JEnumConstant.
     *
     * @param name The name of this JEnumConstant.
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
     * Returns the name of this JEnumConstant.
     *
     * @return The name of this JEnumConstant.
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the JavaDoc comment describing this JEnumConstant.
     *
     * @param comment The JavaDoc comment for this JEnumConstant.
     */
    public void setComment(final JDocComment comment) {
        _comment = comment;
    }

    /**
     * Sets the JavaDoc comment describing this JEnumConstant.
     *
     * @param comment The JavaDoc comment for this JEnumConstant.
     */
    public void setComment(final String comment) {
        if (_comment == null) {
            _comment = new JDocComment();
        }
        _comment.setComment(comment);
    }

    /**
     * Returns the JavaDoc comment describing this JEnumConstant.
     *
     * @return The JavaDoc comment describing this JEnumConstant, or null if
     *         none has been set.
     */
    public JDocComment getComment() {
        return _comment;
    }

    //--------------------------------------------------------------------------

    /**
     * prints this enum constant.
     *
     * @param jsw The JSourceWriter to print to. Must not be null.
     */
    public void print(final JSourceWriter jsw) {
        //-- print comments
        if (_comment != null) { _comment.print(jsw); }
        //-- print annotation
        if (printAnnotations(jsw)) { jsw.writeln(); }
        //-- print name
        jsw.write(_name);
        //-- print arguments
        if (_arguments != null && _arguments.length > 0) {
            jsw.write("(");
            for (int a = 0; a < _arguments.length; a++) {
                jsw.write(_arguments[a]);
                if (a < _arguments.length - 1) { jsw.write(", "); }
            }
            jsw.write(")");
        }
        //-- print methods
        if (_methods.size() > 0) {
            jsw.write(" {");
            jsw.writeln();
            jsw.indent();
            for (int i = 0; i < _methods.size(); i++) {
                JMethod jMethod = (JMethod) _methods.elementAt(i);
                jMethod.print(jsw);
                if (i < _methods.size() - 1) { jsw.writeln(); }
            }
            jsw.unindent();
            jsw.write("}");
        }
    }

    //--------------------------------------------------------------------------
}
