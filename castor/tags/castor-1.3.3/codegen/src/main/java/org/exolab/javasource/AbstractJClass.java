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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.exolab.castor.builder.SourceGenerator;

/**
 * A abstract base class for representations of the Java Source code for a Java Class.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 6668 $ $Date: 2005-05-08 12:32:06 -0600 (Sun, 08 May 2005) $
 * @since 1.1
 */
public abstract class AbstractJClass extends JStructure {

    /** The source code for static initialization. */
    private JSourceCode _staticInitializer;

    /** The list of member variables (fields) of this JClass. */
    private Map<String, JField> _fields = new LinkedHashMap<String, JField>();

    /** The list of member constants of this {@link JClass}. */
    private Map<String, JConstant> _constants = new LinkedHashMap<String, JConstant>();

    /** The list of constructors for this JClass. */
    private Vector<JConstructor> _constructors;

    /** The list of methods of this JClass. */
    private Vector<JMethod> _methods;

    /** A collection of inner classes for this JClass. */
    private Vector<JClass> _innerClasses;

    private Vector<String> _sourceCodeEntries = new Vector<String>();

    /**
     * Returns a collection of (complete) source code fragments.
     * @return A collection of (complete) source code fragments.
     */
    public String[] getSourceCodeEntries() {
        return _sourceCodeEntries.toArray(new String[_sourceCodeEntries.size()]);
    }

    /**
     * Creates a new AbstractJClass with the given name.
     *
     * @param name The name of the AbstractJClass to create.
     */
    protected AbstractJClass(final String name) {
       this(name, false);
    }

    /**
     * Creates a new AbstractJClass with the given name.
     *
     * @param name The name of the AbstractJClass to create.
     */
    protected AbstractJClass(final String name, boolean useOldFieldNaming) {
        super(name);
        
        _staticInitializer = new JSourceCode();
        _constructors = new Vector<JConstructor>();
        _methods = new Vector<JMethod>();
        _innerClasses = null;
        
        if (useOldFieldNaming) {
           //-- initialize default Java doc
           getJDocComment().appendComment("Class " + getLocalName() + ".");
        }
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
        return _fields.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public final JConstant getConstant(final String name) {
        return _constants.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public final JField[] getFields() {
        return _fields.values().toArray(new JField[_fields.size()]);
    }

    /**
     * {@inheritDoc}
     */
    public final JConstant[] getConstants() {
        return _constants.values().toArray(new JConstant[_constants.size()]);
    }

    /**
     * Returns the amount of fields.
     * @return The amount of fields.
     */
    public final int getFieldCount() {
        return _fields.size();
    }

    /**
     * Returns the amount of constants.
     * @return The amount of constants.
     */
    public final int getConstantCount() {
        return _constants.size();
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

    public final void addConstant(final JConstant jConstant) {
        if (jConstant == null) {
            throw new IllegalArgumentException("Class constants cannot be null");
        }

        String name = jConstant.getName();

        if (_constants.get(name) != null) {
            String nameToCompare = (name.startsWith("_")) ? name.substring(1) : name;
            nameToCompare = nameToCompare.substring(0, 1).toUpperCase()
                            + nameToCompare.substring(1);
            if (JNaming.isReservedByCastor(nameToCompare)) {
                String warn = "'" + nameToCompare + "' might conflict with a constant name used"
                        + " by Castor.  If you get a complaint\nabout a duplicate name, you will"
                        + " need to use a mapping file or change the name\nof the conflicting"
                        + " schema element.";
                System.out.println(warn);
            }

            String err = "Duplicate name found as a class member: " + name;
            throw new IllegalArgumentException(err);
        }
        _constants.put(name, jConstant);
    }

    /**
     * Removes the field with the given name from this JClass.
     *
     * @param name The name of the field to remove.
     * @return The JField if it was found and removed.
     */
    public final JField removeField(final String name) {
        if (name == null) { return null; }

        JField field = _fields.remove(name);

        //-- clean up imports
        //-- NOT YET IMPLEMENTED
        return field;
    }

    /**
     * Removes the constant with the given name from this {@link JClass}.
     *
     * @param name The name of the constant to remove.
     * @return The JConstant if it was found and removed.
     */
    public final JConstant removeConstant(final String name) {
        if (name == null) { return null; }

        JConstant constant = _constants.remove(name);

        //-- clean up imports
        //-- NOT YET IMPLEMENTED
        return constant;
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
     * Removes the given {@link JConstant} from this {@link JClass}.
     *
     * @param jConstant The {@link JConstant} to remove.
     * @return true if the constant was found and removed.
     */
    public final boolean removeConstant(final JConstant jConstant) {
        if (jConstant == null) { return false; }

        Object constant = _constants.get(jConstant.getName());
        if (constant == jConstant) {
            _constants.remove(jConstant.getName());
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
        return _constructors.elementAt(index);
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
            jcArray[i] = _constructors.elementAt(i);
        }
        return jcArray;
    }
    
    public final int getContructorsCount() {
    	return _constructors.size();
    }
    
    /**
     * Adds the given Constructor to this classes list of constructors. The
     * constructor must have been created with this JClass' createConstructor.
     *
     * @param constructor The constructor to add.
     */
    public void addConstructor(final JConstructor constructor) {
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
            marray[i] = _methods.elementAt(i);
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
            JMethod jMethod = _methods.elementAt(i);
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
        return _methods.elementAt(index);
    }
    
    public final int getMethodCount() {
    	return _methods.size(); 
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
            JMethod tmp = _methods.elementAt(i);
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
            _innerClasses = new Vector<JClass>();
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
    
    public final int getInnerClassCount() {
    	if (_innerClasses != null) {
    		return _innerClasses.size();
    	} 
    	return 0;
    	
    		
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
     * @deprecated Please use the Velocity-template based approach instead.
     * @see SourceGenerator#setJClassPrinterType(String) 
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
     * 
     * @deprecated Please use the Velocity-template based approach instead.
     * @see SourceGenerator#setJClassPrinterType(String) 
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
        Vector<String> removeImports = null;
        if ((_innerClasses != null) && (_innerClasses.size() > 0)) {
            removeImports = new Vector<String>();
            for (int i = 0; i < _innerClasses.size(); i++) {
                JClass iClass = _innerClasses.elementAt(i);
                Enumeration<String> enumeration = iClass.getImports();
                while (enumeration.hasMoreElements()) {
                    String classname = enumeration.nextElement();

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
                removeImport(removeImports.elementAt(i));
            }
        }
    }

    /**
     * Writes to the {@link JSourceWriter} the constant definitions of this class.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    protected final void printConstantDefinitions(final JSourceWriter jsw) {
        for (JConstant constant : _constants.values()) {
            printAbstractJField(jsw, constant);
        }
    }

    /**
     * Prints an {@link AbstractJField} instance to the given {@link JSourceWriter}.
     * 
     * @param jsw The {@link JSourceWriter} to print to.
     * @param field The field to print.
     */
    private void printAbstractJField(final JSourceWriter jsw, final AbstractJField field) {
        //-- print Java comment
        JDocComment comment = field.getComment();
        if (comment != null && comment.getLength() > 0) { 
           comment.print(jsw); 
        }

        //-- print Annotations
        field.printAnnotations(jsw);

        // -- print member
        jsw.write(field.getModifiers().toString());
        jsw.write(' ');

        JType type = field.getType();
        String typeName = type.toString();
        //-- for esthetics use short name in some cases
        if (typeName.equals(toString())) {
            typeName = type.getLocalName();
        }
        jsw.write(typeName);
        jsw.write(' ');
        jsw.write(field.getName());

        String init = field.getInitString();
        if (init != null && !field.isDateTime()) {
            jsw.write(" = ");
            jsw.write(init);
        }

        jsw.writeln(';');
        jsw.writeln();
    }
    
    /**
     * Writes to the JSourceWriter the member variables of this class.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    protected final void printMemberVariables(final JSourceWriter jsw) {
        for (JField field : _fields.values()) {
            printAbstractJField(jsw, field);
        }
    }

    /**
     * Writes to the JSourceWriter any static initialization used by this class.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    protected final void printStaticInitializers(final JSourceWriter jsw) {
        if (!_staticInitializer.isEmpty()) {
            jsw.writeln();
            jsw.writeln("static {");
            jsw.writeln(_staticInitializer.toString());
            jsw.writeln("}");
            jsw.writeln();
        }
    }

    /**
     * Writes to the JSourceWriter all constructors for this class.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    protected final void printConstructors(final JSourceWriter jsw) {
        for (int i = 0; i < _constructors.size(); i++) {
            JConstructor jConstructor = _constructors.elementAt(i);
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
        for (int i = 0; i < _methods.size(); i++) {
            JMethod jMethod = _methods.elementAt(i);
            jMethod.print(jsw);
            jsw.writeln();
        }
    }
    
    protected final void printSourceCodeFragments(final JSourceWriter sourceWriter) {
        for (String sourceCode : _sourceCodeEntries) {
            sourceWriter.writeln(sourceCode);
            sourceWriter.writeln();
        }
        
    }

    /**
     * Writes to the JSourceWriter all inner classes belonging to this class.
     * 
     * @param jsw The JSourceWriter to be used.
     */
    protected final void printInnerClasses(final JSourceWriter jsw) {
        if ((_innerClasses != null) && (_innerClasses.size() > 0)) {
            for (int i = 0; i < _innerClasses.size(); i++) {
                JClass jClass = _innerClasses.elementAt(i);
                jClass.print(jsw, true);
                jsw.writeln();
            }
        }
    }

    /**
     * Adds a complete source code fragment (method) to this class.
     * @param sourceCode The complete source code fragment to be added.
     */
    public void addSourceCode(final String sourceCode) {
        _sourceCodeEntries.add(sourceCode);
    }

    public final int getSourceCodeEntryCount() {
        return _sourceCodeEntries.size(); 
    }
    
}
