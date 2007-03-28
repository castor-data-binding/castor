/*
 * Copyright 2006 Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.exolab.javasource;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A abstract base class for representations of the Java Source code for a Java Class.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 6668 $ $Date: 2005-05-08 12:32:06 -0600 (Sun, 08 May 2005) $
 * @since 1.1
 */
public abstract class AbstractJClass extends JStructure {
    //--------------------------------------------------------------------------

    /** The source code for static initialization. */
    private JSourceCode _staticInitializer;

    /** The list of member variables (fields) of this JClass. */
    private JNamedMap _fields;

    /** The list of constructors for this JClass. */
    private Vector _constructors;

    /** The list of methods of this JClass. */
    private Vector _methods;

    /** A collection of inner classes for this JClass. */
    private Vector _innerClasses;

    //--------------------------------------------------------------------------

    /**
     * Creates a new AbstractJClass with the given name.
     *
     * @param name The name of the AbstractJClass to create.
     */
    protected AbstractJClass(final String name) {
        super(name);
        
        _staticInitializer = new JSourceCode();
        _fields = new JNamedMap();
        _constructors = new Vector();
        _methods = new Vector();
        _innerClasses = null;
        
        //-- initialize default Java doc
        getJDocComment().appendComment("Class " + getLocalName() + ".");
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the JSourceCode for the static initializer of this JClass.
     *
     * @return The JSourceCode for the static initializer of this JClass.
     */
    public final JSourceCode getStaticInitializationCode() {
        return _staticInitializer;
    }

    /**
     * {@inheritDoc}
     */
    public final JField getField(final String name) {
        return (JField) _fields.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public final JField[] getFields() {
        int size = _fields.size();
        JField[] farray = new JField[size];
        for (int i = 0; i < size; i++) {
            farray[i] = (JField) _fields.get(i);
        }
        return farray;
    }

    /**
     * {@inheritDoc}
     */
    public final void addField(final JField jField) {
        if (jField == null) {
            throw new IllegalArgumentException("Class members cannot be null");
        }

        String name = jField.getName();

        if (_fields.get(name) != null) {
            String nameToCompare = (name.startsWith("_")) ? name.substring(1) : name;
            nameToCompare = nameToCompare.substring(0, 1).toUpperCase()
                            + nameToCompare.substring(1);
            if (JNaming.isReservedByCastor(nameToCompare)) {
                String warn = "'" + nameToCompare + "' might conflict with a field name used"
                        + " by Castor.  If you get a complaint\nabout a duplicate name, you will"
                        + " need to use a mapping file or change the name\nof the conflicting"
                        + " schema element.";
                System.out.println(warn);
            }

            String err = "Duplicate name found as a class member: " + name;
            throw new IllegalArgumentException(err);
        }
        _fields.put(name, jField);
    }

    /**
     * Removes the field with the given name from this JClass.
     *
     * @param name The name of the field to remove.
     * @return The JField if it was found and removed.
     */
    public final JField removeField(final String name) {
        if (name == null) { return null; }

        JField field = (JField) _fields.remove(name);

        //-- clean up imports
        //-- NOT YET IMPLEMENTED
        return field;
    }

    /**
     * Removes the given JField from this JClass.
     *
     * @param jField The JField to remove.
     * @return true if the field was found and removed.
     */
    public final boolean removeField(final JField jField) {
        if (jField == null) { return false; }

        Object field = _fields.get(jField.getName());
        if (field == jField) {
            _fields.remove(jField.getName());
            return true;
        }
        //-- clean up imports
        //-- NOT YET IMPLEMENTED
        return false;
    }

    /**
     * Creates a new JConstructor and adds it to this JClass.
     *
     * @return The newly created constructor.
     */
    public final JConstructor createConstructor() {
        return createConstructor(null);
    }

    /**
     * Creates a new JConstructor and adds it to this JClass.
     *
     * @param params A list of parameters for this constructor.
     * @return The newly created constructor.
     */
    public final JConstructor createConstructor(final JParameter[] params) {
        JConstructor cons = new JConstructor(this);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                cons.addParameter(params[i]);
            }
        }
        addConstructor(cons);
        return cons;
    }

    /**
     * Returns the constructor at the specified index.
     *
     * @param index The index of the constructor to return.
     * @return The JConstructor at the specified index.
     */
    public final JConstructor getConstructor(final int index) {
        return (JConstructor) _constructors.elementAt(index);
    }

    /**
     * Returns the an array of the JConstructors contained within this JClass.
     *
     * @return An array of JConstructor.
     */
    public final JConstructor[] getConstructors() {
        int size = _constructors.size();
        JConstructor[] jcArray = new JConstructor[size];

        for (int i = 0; i < _constructors.size(); i++) {
            jcArray[i] = (JConstructor) _constructors.elementAt(i);
        }
        return jcArray;
    }

    /**
     * Adds the given Constructor to this classes list of constructors. The
     * constructor must have been created with this JClass' createConstructor.
     *
     * @param constructor The constructor to add.
     */
    public final void addConstructor(final JConstructor constructor) {
        if (constructor == null) {
            throw new IllegalArgumentException("Constructors cannot be null");
        }

        if (constructor.getDeclaringClass() == this) {

            /** check signatures (add later) **/
            if (!_constructors.contains(constructor)) {
                _constructors.addElement(constructor);
            }
        } else {
            String err = "The given JConstructor was not created by this JClass";
            throw new IllegalArgumentException(err);
        }
    }

    /**
     * Removes the given constructor from this JClass.
     *
     * @param constructor The JConstructor to remove.
     * @return true if the constructor was removed, otherwise false.
     */
    public final boolean removeConstructor(final JConstructor constructor) {
        return _constructors.removeElement(constructor);
    }

    /**
     * Returns an array of all the JMethods of this JClass.
     *
     * @return An array of all the JMethods of this JClass.
     */
    public final JMethod[] getMethods() {
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
    public final JMethod getMethod(final String name, final int startIndex) {
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
    public final JMethod getMethod(final int index) {
        return (JMethod) _methods.elementAt(index);
    }

    /**
     * Adds the given JMethod to this JClass.
     *
     * @param jMethod The JMethod to add.
     * @param importReturnType true if we add the importReturnType to the class
     *        import lists. It could be useful to set it to false when all
     *        types are fully qualified.
     */
    public final void addMethod(final JMethod jMethod, final boolean importReturnType) {
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
     * Adds the given JMethod to this JClass.
     *
     * @param jMethod The JMethod to add.
     */
    public final void addMethod(final JMethod jMethod) {
         addMethod(jMethod, true);
    }

    /**
     * Adds the given array of JMethods to this JClass.
     *
     * @param jMethods The JMethod[] to add.
     */
    public final void addMethods(final JMethod[] jMethods) {
        for (int i = 0; i < jMethods.length; i++) { addMethod(jMethods[i]); }
    }

    /**
     * Removes the given method from this JClass.
     *
     * @param method The JMethod to remove.
     * @return true if the method was removed, otherwise false.
     */
    public final boolean removeMethod(final JMethod method) {
        return _methods.removeElement(method);
    }

    /**
     * Creates and returns an inner-class for this JClass.
     *
     * @param localname The name of the class (no package name).
     * @return the new JClass.
     */
    public final JClass createInnerClass(final String localname) {
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
        } else {
            classname = localname;
        }

        JClass innerClass = new JInnerClass(classname);
        if (_innerClasses == null) {
            _innerClasses = new Vector();
        }
        _innerClasses.addElement(innerClass);
        return innerClass;

    }

    /**
     * Returns an array of JClass (the inner classes) contained within this
     * JClass.
     *
     * @return An array of JClass contained within this JClass.
     */
    public final JClass[] getInnerClasses() {
        if (_innerClasses != null) {
            int size = _innerClasses.size();
            JClass[] carray = new JClass[size];
            _innerClasses.copyInto(carray);
            return carray;
        }
        return new JClass[0];
    }

    /**
     * Removes the given inner-class (JClass) from this JClass.
     *
     * @param jClass The JClass (inner-class) to remove.
     * @return true if the JClass was removed, otherwise false.
     */
    public final boolean removeInnerClass(final JClass jClass) {
        if (_innerClasses != null) {
            return _innerClasses.removeElement(jClass);
        }
        return false;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final void print(final JSourceWriter jsw) {
        print(jsw, false);
    }

    /**
     * Prints the source code for this JClass to the given JSourceWriter.
     *
     * @param classOnly If true, the file header, package declaration, and
     *        imports are not printed.
     * @param jsw The JSourceWriter to print to. Must not be null.
     */
    public abstract void print(final JSourceWriter jsw, final boolean classOnly);

    /**
     * Writes to the JSourceWriter the headers for this class file.  Headers
     * include the comment-header, the package declaration, and the imports.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    protected final void printClassHeaders(final JSourceWriter jsw) {
        printHeader(jsw);
        printPackageDeclaration(jsw);

        //-- get imports from inner-classes
        Vector removeImports = null;
        if ((_innerClasses != null) && (_innerClasses.size() > 0)) {
            removeImports = new Vector();
            for (int i = 0; i < _innerClasses.size(); i++) {
                JClass iClass = (JClass) _innerClasses.elementAt(i);
                Enumeration enumeration = iClass.getImports();
                while (enumeration.hasMoreElements()) {
                    String classname = (String) enumeration.nextElement();

                    int paramTypeIndex = classname.indexOf("<Object>");
                    if (paramTypeIndex != -1) {
                        classname = classname.substring(0, paramTypeIndex - 1);
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
                removeImport((String) removeImports.elementAt(i));
            }
        }
    }

    /**
     * Writes to the JSourceWriter the member variables of this class.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    protected final void printMemberVariables(final JSourceWriter jsw) {
        if (_fields.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //--------------------------/");
            jsw.writeln(" //- Class/Member Variables -/");
            jsw.writeln("//--------------------------/");
            jsw.writeln();
        }

        for (int i = 0; i < _fields.size(); i++) {
            JField jField = (JField) _fields.get(i);

            //-- print Java comment
            JDocComment comment = jField.getComment();
            if (comment != null) { comment.print(jsw); }

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
            if (init != null && !jField.isDateTime()) {
                jsw.write(" = ");
                jsw.write(init);
            }

            jsw.writeln(';');
            jsw.writeln();
        }
    }

    /**
     * Writes to the JSourceWriter any static initialization used by this class.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    protected final void printStaticInitializers(final JSourceWriter jsw) {
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
    }

    /**
     * Writes to the JSourceWriter all constructors for this class.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    protected final void printConstructors(final JSourceWriter jsw) {
        if (_constructors.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //----------------/");
            jsw.writeln(" //- Constructors -/");
            jsw.writeln("//----------------/");
            jsw.writeln();
        }

        for (int i = 0; i < _constructors.size(); i++) {
            JConstructor jConstructor = (JConstructor) _constructors.elementAt(i);
            jConstructor.print(jsw);
            jsw.writeln();
        }
    }

    /**
     * Writes to the JSourceWriter all methods belonging to this class.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    protected final void printMethods(final JSourceWriter jsw) {
        if (_methods.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //-----------/");
            jsw.writeln(" //- Methods -/");
            jsw.writeln("//-----------/");
            jsw.writeln();
        }

        for (int i = 0; i < _methods.size(); i++) {
            JMethod jMethod = (JMethod) _methods.elementAt(i);
            jMethod.print(jsw);
            jsw.writeln();
        }
    }

    /**
     * Writes to the JSourceWriter all inner classes belonging to this class.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    protected final void printInnerClasses(final JSourceWriter jsw) {
        if ((_innerClasses != null) && (_innerClasses.size() > 0)) {
            jsw.writeln();
            jsw.writeln("  //-----------------/");
            jsw.writeln(" //- Inner Classes -/");
            jsw.writeln("//-----------------/");
            jsw.writeln();

            for (int i = 0; i < _innerClasses.size(); i++) {
                JClass jClass = (JClass) _innerClasses.elementAt(i);
                jClass.print(jsw, true);
                jsw.writeln();
            }
        }
    }

    //--------------------------------------------------------------------------
}
