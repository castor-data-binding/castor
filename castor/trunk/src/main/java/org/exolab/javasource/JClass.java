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
 *
 * $Id$
 *
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * Contributors:
 * --------------
 * Keith Visco (keith AT kvisco DOT com) 
 *    - Original Author
 * 
 * Martin Skopp (skopp AT riege DOT de)  
 *    - Moved some core code into JStructure and revised to 
 *      extend JStructure
 *
 */

package org.exolab.javasource;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A representation of the Java Source code for a Java Class. This is a useful
 * utility when creating in memory source code. This package was modelled after
 * the Java Reflection API as much as possible to reduce the learning curve.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:skopp AT riege DOT de">Martin Skopp</a> 
 * @version $Revision$ $Date: 2005-05-08 12:32:06 -0600 (Sun, 08 May 2005) $
 */
public class JClass extends JStructure {

    /**
     * The list of constructors for this JClass
     */
    protected Vector _constructors  = null;

    /**
     * The list of member variables (fields) of this JClass
     */
    protected JNamedMap _fields    = null;

    /**
     * A collection of inner classes for this JClass
     */
    protected Vector _innerClasses = null;
    
    /**
     * The list of methods of this JClass
     */
    protected Vector _methods       = null;

    /**
     * The superclass for this JClass
     */
    protected JTypeName _superClass = null;
    
    /**
     * The source code for static initialization
     */
    protected JSourceCode _staticInitializer = new JSourceCode();
    
    /**
     * Creates a new JClass with the given name
     * 
     * @param name the name of the JClass to create
     * @throws IllegalArgumentException when the given name is not a valid Class
     *             name
     */
    public JClass(String name)
        throws IllegalArgumentException
    {
        super(name);
        _constructors     = new Vector();
        _fields           = new JNamedMap();
        _methods          = new Vector();
        _innerClasses     = null;
        //-- initialize default Java doc
        getJDocComment().appendComment("Class " + getLocalName() + ".");
        
    } //-- JClass

    /**
     * Adds the given Constructor to this classes list of constructors. The
     * constructor must have been created with this JClass' createConstructor.
     * 
     * @param constructor the constructor to add
     * @throws IllegalArgumentException
     */
    public void addConstructor(JConstructor constructor)
        throws IllegalArgumentException
    {
        if (constructor == null)
            throw new IllegalArgumentException("Constructors cannot be null");
            
        if (constructor.getDeclaringClass() == this) {

            /** check signatures (add later) **/
            if (!_constructors.contains(constructor)) {
                _constructors.addElement(constructor);
            }
        }
        else {
            String err = "The given JConstructor was not created ";
            err += "by this JClass";
            throw new IllegalArgumentException(err);
        }       
    }

    /**
     * Adds the given JField to this JClass
     * 
     * @param jField the JField to add
     * @throws IllegalArgumentException when the given JField has a name of an
     *             existing JField
     */
    public void addField(JField jField)
        throws IllegalArgumentException
    {
        if (jField == null) {
            throw new IllegalArgumentException("Class members cannot be null");
        }
        
        String name = jField.getName();
        
        if (_fields.get(name) != null) {
            String err = "duplicate name found: " + name;
            throw new IllegalArgumentException(err);
        }
        _fields.put(name, jField);
    } //-- addField

    /**
     * @see org.exolab.javasource.JStructure#addImport(java.lang.String)
     */
    public void addImport(String name) {
        if ((name == null) || (name.length() == 0))
            return;
        
        JTypeName jtName = new JTypeName(name);
        
        String cls = jtName.getLocalName();
        String pkg = jtName.getPackageName();
        
        if (_superClass != null) {
            if (_superClass.getLocalName().equals(cls)) {
                if (_superClass.getPackageName() == null) {
                    if (pkg != null) {
                        // if we make it here we are extending
                        // a class from the default package which
                        // conflicts with this import, we cannot
                        // import this class
                        return;
                    }
                }
            }
        }
        
        super.addImport(name);
    } //-- addImport
    
    /**
     * Adds the given JMember to this JClass
     * 
     * @param jMember the JMember to add
     * @throws IllegalArgumentException when the given JMember has the same name
     *             of an existing JField or JMethod respectively, or if the
     *             JMember is of an unrecognized class.
     */
    public void addMember(JMember jMember)
        throws IllegalArgumentException
    {
        if (jMember instanceof JField)
            addField((JField)jMember);
        else if (jMember instanceof JMethod) 
            addMethod((JMethod)jMember);
        else {
            String error = null;
            if (jMember == null) {
                error = "the argument 'jMember' must not be null.";
            }
            else {
                error = "Cannot add JMember '" + jMember.getClass().getName() + 
                    "' to JClass, unrecognized type.";
            }
            throw new IllegalArgumentException(error);
        }

    } //-- addMember

    /**
     * Adds the given JMethod to this JClass
     * 
     * @param jMethod the JMethod to add
     * @throws IllegalArgumentException when the given JMethod has the same name
     *             of an existing JMethod.
     */
    public void addMethod(JMethod jMethod) {
         addMethod(jMethod, true);
    }
    
    /**
     * Adds the given JMethod to this JClass
     * 
     * @param jMethod the JMethod to add
     * @param importReturnType true if we add the importReturnType to the class
     *            import lists. It could be useful to set it to false when all
     *            types are fully qualified.
     * @throws IllegalArgumentException when the given JMethod has the same name
     *             of an existing JMethod.
     */
    public void addMethod(JMethod jMethod, boolean importReturnType)
        throws IllegalArgumentException
    {
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
        if (!added) _methods.addElement(jMethod);

    } //-- addMethod

    /**
     * Adds the given array of JMethods to this JClass
     * 
     * @param jMethods the JMethod[] to add
     * @throws IllegalArgumentException when any of the given JMethods has the
     *             same name of an existing JMethod.
     */
    public void addMethods(JMethod[] jMethods)
        throws IllegalArgumentException
    {
        for (int i = 0; i < jMethods.length; i++)
            addMethod(jMethods[i]);
    } //-- addMethods

    /**
     * Creates a new JConstructor and adds it to this JClass.
     * 
     * @return the newly created constructor
     */
    public JConstructor createConstructor() {
        return createConstructor(null);
    } //-- createConstructor
    
    /**
     * Creates a new JConstructor and adds it to this JClass.
     * 
     * @param params a list of parameters for this constructor
     * @return the newly created constructor
     */
    public JConstructor createConstructor(JParameter[] params) {
        JConstructor cons = new JConstructor(this);
        if (params != null) {
            for (int i = 0; i < params.length; i++)
                cons.addParameter(params[i]);
        }
        addConstructor(cons);
        return cons;
    } //-- createConstructor
    
    /**
     * Creates and returns an inner-class for this JClass
     * 
     * @param localname the name of the class (no package name)
     * @return the new JClass
     */
    public JClass createInnerClass(String localname) {
        if (localname == null) {
            String err = "argument 'localname' must not be null.";
            throw new IllegalArgumentException(err);
        }
        if (localname.indexOf('.') >= 0) {
            String err = "The name of an inner-class must not contain a package name.";
            throw new IllegalArgumentException(err);
        }
        String classname = getPackageName();
        if (classname != null) {
            classname = classname + "." + localname;
        }
        else {
            classname = localname;
        }
        
        JClass innerClass = new JInnerClass(classname);
        if (_innerClasses == null) {
            _innerClasses = new Vector();
        }
        _innerClasses.addElement(innerClass);
        return innerClass;
        
    } //-- createInnerClass

    /**
     * Returns the constructor at the specified index.
     * 
     * @param index the index of the constructor to return
     * @return the JConstructor at the specified index.
     */
    public JConstructor getConstructor(int index) {
        return (JConstructor)_constructors.elementAt(index);
    } //-- getConstructor

    /**
     * Returns the an array of the JConstructors contained within this JClass
     * 
     * @return an array of JConstructor
     */
    public JConstructor[] getConstructors() {
        int size = _constructors.size();
        JConstructor[] jcArray = new JConstructor[size];

        for (int i = 0; i < _constructors.size(); i++) {
            jcArray[i] = (JConstructor)_constructors.elementAt(i);
        }
        return jcArray;
    } //-- getConstructors

    /**
     * Returns the member with the given name, or null if no member is found
     * with the given name
     * 
     * @param name the name of the member to return
     * @return the member with the given name, or null if no member was found
     *         with the given name
     */
    public JField getField(String name) {
        return (JField)_fields.get(name);
    } //-- getField

    /**
     * Returns an array of all the JFields of this JClass
     * 
     * @return an array of all the JFields of this JClass
     */
    public JField[] getFields() {
        int size = _fields.size();
        JField[] farray = new JField[size];
        for (int i = 0; i < size; i++) {
            farray[i] = (JField)_fields.get(i);
        }
        return farray;
    } //-- getFields

    /**
     * Returns an array of JClass (the inner classes) contained within this
     * JClass.
     * 
     * @return an array of JClass contained within this JClass
     */
    public JClass[] getInnerClasses() {
        if (_innerClasses != null) {
            int size = _innerClasses.size();
            JClass[] carray = new JClass[size];
            _innerClasses.copyInto(carray);
            return carray;
        }
        return new JClass[0];
    } //-- getInnerClasses;
    
    /**
     * Returns an array of all the JMethods of this JClass
     * 
     * @return an array of all the JMethods of this JClass
     */
    public JMethod[] getMethods() {
        int size = _methods.size();
        JMethod[] marray = new JMethod[size];

        for (int i = 0; i < _methods.size(); i++) {
            marray[i] = (JMethod)_methods.elementAt(i);
        }
        return marray;
    } //-- getMethods

    /**
     * Returns the first occurance of the method with the given name, starting
     * from the specified index.
     * 
     * @param name the name of the method to look for
     * @param startIndex the starting index to begin the search
     * @return the method if found, otherwise null.
     */
    public JMethod getMethod(String name, int startIndex) {
        for (int i = startIndex; i < _methods.size(); i++) {
            JMethod jMethod = (JMethod)_methods.elementAt(i);
            if (jMethod.getName().equals(name)) return jMethod;
        }
        return null;
    } //-- getMethod

    /**
     * Returns the JMethod located at the specified index
     * 
     * @param index the index of the JMethod to return.
     * @return the JMethod
     */
    public JMethod getMethod(int index) {
        return (JMethod)_methods.elementAt(index);
    } //-- getMethod


    /**
     * Returns the JSourceCode for the static initializer of this JClass
     * 
     * @return the JSourceCode for the static initializer of this JClass
     */
    public JSourceCode getStaticInitializationCode() {
        return _staticInitializer;
    } //-- getStaticInitializationCode
    
    /**
     * Teturns the super Class that this class extends
     * 
     * @return superClass the super Class that this Class extends
     */
    public String getSuperClass() {
        if (_superClass == null) return null;
        return _superClass.getQualifiedName();
    } //-- getSuperClass

    /**
     * Prints the source code for this JClass to the given JSourceWriter
     * 
     * @param jsw the JSourceWriter to print to. Must not be null.
     */
    public void print(JSourceWriter jsw) {
        print(jsw, false);
    } //-- print

    /**
     * Prints the source code for this JClass to the given JSourceWriter
     * 
     * @param classOnly if true, the file header, package declaration, and
     *            imports are not printed
     * @param jsw the JSourceWriter to print to. Must not be null.
     */
    public void print(JSourceWriter jsw, boolean classOnly) {
        if (jsw == null) {
            throw new IllegalArgumentException("argument 'jsw' should not be null.");
        }
        

        StringBuffer buffer = new StringBuffer();


        if (!classOnly) {
            printHeader(jsw);
            printPackageDeclaration(jsw);
            
            //-- get imports from inner-classes
            Vector removeImports = null;
            if ((_innerClasses != null) && (_innerClasses.size() > 0)) {
                removeImports = new Vector();
                for (int i = 0; i < _innerClasses.size(); i++) {
                    JClass iClass = (JClass)_innerClasses.elementAt(i);
                    Enumeration enumeration = iClass.getImports();
                    while (enumeration.hasMoreElements()) {
                        String classname = (String)enumeration.nextElement();
						
                        int paramTypeIndex = classname.indexOf("<Object>");
                        if ( paramTypeIndex != -1 ) {
                        	classname = classname.substring(0,paramTypeIndex-1);
                        }
                        if (!hasImport(classname)) {
                            addImport(classname);
                            removeImports.addElement(classname);
                        }
                    }
                }
            }
            printImportDeclarations(jsw);
            
            //-- remove imports from inner-classes, if necessary
            if (removeImports != null) {
                for (int i = 0; i < removeImports.size(); i++) {
                    removeImport((String)removeImports.elementAt(i));
                }
            }
            
        }

        //------------/
        //- Java Doc -/
        //------------/

        getJDocComment().print(jsw);

        //-- print class information
        //-- we need to add some JavaDoc API adding comments

        buffer.setLength(0);

		//-- print annotations
		getAnnotatedElementHelper().printAnnotations(jsw);
				
        JModifiers modifiers = getModifiers();
        if (modifiers.isPrivate()) {
            buffer.append("private ");
        }
        else if (modifiers.isPublic()) {
            buffer.append("public ");
        }
        
        if (modifiers.isFinal()) {
            buffer.append ("final ");
        }

        if (modifiers.isAbstract()) {
            buffer.append("abstract ");
        }

        buffer.append("class ");
        buffer.append(getLocalName());
        buffer.append(' ');
        if (_superClass != null) {
            buffer.append("extends ");
            buffer.append(_superClass);
            buffer.append(' ');
        }
        if (getInterfaceCount() > 0) {
            boolean endl = false;
            if ((getInterfaceCount() > 1) || (_superClass != null)) {
                jsw.writeln(buffer.toString());
                buffer.setLength(0);
                endl = true;
            }
            buffer.append("implements ");
            
            Enumeration enumeration = getInterfaces();
            while (enumeration.hasMoreElements()) {
                buffer.append(enumeration.nextElement());
                if (enumeration.hasMoreElements()) buffer.append(", ");
            }
            if (endl) {
                jsw.writeln(buffer.toString());
                buffer.setLength(0);
            }
            else buffer.append(' ');
        }

        buffer.append('{');
        jsw.writeln(buffer.toString());
        buffer.setLength(0);
        jsw.writeln();

        jsw.indent();

        //-- declare members

        if (_fields.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //--------------------------/");
            jsw.writeln(" //- Class/Member Variables -/");
            jsw.writeln("//--------------------------/");
            jsw.writeln();
        }

        for (int i = 0; i < _fields.size(); i++) {

            JField jField = (JField)_fields.get(i);

            //-- print Java comment
            JDocComment comment = jField.getComment();
            if (comment != null) comment.print(jsw);

			//-- print Annotations
			jField.printAnnotations(jsw);
			
            // -- print member
            jsw.write(jField.getModifiers().toString());
            jsw.write(' ');

            JType type = jField.getType();
            String typeName = type.toString();
            //-- for esthetics use short name in some cases
            if (typeName.equals(toString())) {
                typeName = type.getLocalName();
            }
            jsw.write(typeName);
            jsw.write(' ');
            jsw.write(jField.getName());

            String init = jField.getInitString();
            if (init != null) {
                jsw.write(" = ");
                jsw.write(init);
            }

            jsw.writeln(';');
            jsw.writeln();
        }

        //----------------------/
        //- Static Initializer -/
        //----------------------/
        
        if (!_staticInitializer.isEmpty()) {
            jsw.writeln();
            jsw.writeln("static {");
            jsw.writeln(_staticInitializer.toString());
            jsw.writeln("};");
            jsw.writeln();
        }
        
        //-- print constructors
        if (_constructors.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //----------------/");
            jsw.writeln(" //- Constructors -/");
            jsw.writeln("//----------------/");
            jsw.writeln();
        }
        for (int i = 0; i < _constructors.size(); i++) {
            JConstructor jConstructor = (JConstructor)_constructors.elementAt(i);
            jConstructor.print(jsw);
            jsw.writeln();
        }

        //-- print methods
        if (_methods.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //-----------/");
            jsw.writeln(" //- Methods -/");
            jsw.writeln("//-----------/");
            jsw.writeln();
        }

        for (int i = 0; i < _methods.size(); i++) {
            JMethod jMethod = (JMethod)_methods.elementAt(i);
            jMethod.print(jsw);
            jsw.writeln();
        }

        //-- print inner-classes
        if ((_innerClasses != null) && (_innerClasses.size() > 0)) {
            jsw.writeln();
            jsw.writeln("  //-----------------/");
            jsw.writeln(" //- Inner Classes -/");
            jsw.writeln("//-----------------/");
            jsw.writeln();
        
            for (int i = 0; i < _innerClasses.size(); i++) {
                JClass jClass = (JClass)_innerClasses.elementAt(i);
                jClass.print(jsw, true);
                jsw.writeln();
            }
            
        }
        
        jsw.unindent();
        jsw.writeln('}');
        jsw.flush();
    } //-- printSource

    /**
     * Removes the given constructor from this JClass
     * 
     * @param constructor the JConstructor to remove
     * @return true if the constructor was removed, otherwise false
     */
    public boolean removeConstructor(JConstructor constructor) {
        return _constructors.removeElement(constructor);
    } //-- removeConstructor

	/**
     * Removes the given method from this JClass
     * 
     * @param method the JMethod to remove
     * @return true if the method was removed, otherwise false
     */
	public boolean removeMethod(JMethod method) 
	{
		return _methods.removeElement(method);
	} //-- removeMethod
    
    /**
     * Removes the field with the given name from this JClass
     * 
     * @param name the name of the field to remove
     * @return the JField if it was found and removed
     */
    public JField removeField(String name) 
	{
        if (name == null) return null;
        
        JField field = (JField) _fields.remove(name);
        
        //-- clean up imports
        //-- NOT YET IMPLEMENTED
        return field;
    } //-- removeField

    /**
     * Removes the given JField from this JClass
     * 
     * @param jField the JField to remove
     * @return true if the field was found and removed
     */
    public boolean removeField(JField jField) {
        if (jField == null) return false;
        
        Object field = _fields.get(jField.getName());
        if (field == jField) {
            _fields.remove(jField.getName());
            return true;
        }        
        //-- clean up imports
        //-- NOT YET IMPLEMENTED
        return false;

    } //-- removeField

    /**
     * Removes the given inner-class (JClass) from this JClass
     * 
     * @param jClass the JClass (inner-class) to remove
     * @return true if the JClass was removed, otherwise false
     */
    public boolean removeInnerClass(JClass jClass) {
        if (_innerClasses != null) {
            return _innerClasses.removeElement(jClass);
        }
        return false;
    } //-- removeInnerClass

    /**
     * Sets the super Class that this class extends
     * 
     * @param superClass the super Class that this Class extends
     */
    public void setSuperClass(String superClass) {
        if (superClass == null)
            _superClass = null;
        else
            _superClass = new JTypeName(superClass);
    } //-- setSuperClass

    /**
     * Test drive method...to be removed or commented out
     */
    public static void main(String[] args) {
        JClass testClass = new JClass("org.acme.Test");

        testClass.addImport("java.util.Vector");
        testClass.addMember(new JField(JType.Int, "x"));
        JClass jcString = new JClass("String");

        JField field = null;
        field = new JField(JType.Int, "_z");
        field.getModifiers().setStatic(true);
        testClass.addField(field);
        
        testClass.getStaticInitializationCode().add("_z = 75;");
        
        field = new JField(jcString, "myString");
        field.getModifiers().makePrivate();
        testClass.addMember(field);

        //-- create constructor
        JConstructor cons = testClass.createConstructor();
        cons.getSourceCode().add("this.x = 6;");

        JMethod jMethod = new JMethod(JType.Int, "getX");
        jMethod.setSourceCode("return this.x;");
        testClass.addMethod(jMethod);

        //-- create inner-class
        JClass innerClass = testClass.createInnerClass("Foo");
        innerClass.addImport("java.util.Hashtable");
        innerClass.addMember(new JField(JType.Int, "_type"));

        field = new JField(jcString, "_name");
        field.getModifiers().makePrivate();
        innerClass.addMember(field);

        //-- create constructor
        cons = innerClass.createConstructor();
        cons.getSourceCode().add("_name = \"foo\";");

        jMethod = new JMethod(jcString, "getName");
        jMethod.setSourceCode("return _name;");
        innerClass.addMethod(jMethod);
        
        testClass.print();
    } //-- main
    /* */

    final class JInnerClass extends JClass {
        
        JInnerClass(String name) {
            super(name);
        }
        
        /**
         * Allows changing the package name of this JStructure
         * 
         * @param packageName the package name to use
         */
        public void setPackageName(String packageName)  {
            throw new IllegalStateException("Cannot change the package of an inner-class");
        } //-- setPackageName
    } //-- JInnerClass
    
} //-- JClass
