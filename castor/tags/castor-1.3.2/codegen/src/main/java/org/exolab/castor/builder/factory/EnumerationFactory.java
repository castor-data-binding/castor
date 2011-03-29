/*
 * Copyright 2005 Werner Guttmann
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
package org.exolab.castor.builder.factory;

import java.util.Enumeration;

import org.exolab.castor.builder.AnnotationBuilder;
import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.castor.builder.FactoryState;
import org.exolab.castor.builder.GroupNaming;
import org.exolab.castor.builder.SGTypes;
import org.exolab.castor.builder.SourceGenerator;
import org.exolab.castor.builder.TypeConversion;
import org.exolab.castor.builder.binding.ExtendedBinding;
import org.exolab.castor.builder.binding.XMLBindingComponent;
import org.exolab.castor.builder.binding.xml.EnumBindingType;
import org.exolab.castor.builder.binding.xml.EnumMember;
import org.exolab.castor.builder.types.XSString;
import org.exolab.castor.builder.types.XSType;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JAnnotationType;
import org.exolab.javasource.JArrayType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JConstructor;
import org.exolab.javasource.JDocComment;
import org.exolab.javasource.JEnum;
import org.exolab.javasource.JEnumConstant;
import org.exolab.javasource.JField;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JModifiers;
import org.exolab.javasource.JParameter;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * This class creates the Java sources for XML Schema components that define an
 * enumeration.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner
 *         Guttmann</a>
 * @version $Revision: 6287 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr
 *          2006) $
 */
public final class EnumerationFactory extends BaseFactory {

    /**
     * The TypeConversion instance to use for mapping SimpleTypes into XSTypes.
     */
    private TypeConversion _typeConversion;

    /**
     * A flag indicating that enumerated types should be constructed to perform
     * case insensitive lookups based on the values.
     */
    private boolean _caseInsensitive = false;

    /**
     * Current (hence maximum) suffix for init methods, used to avoid the static
     * initializer limits of a JVM.
     */
    private int _maxSuffix = 0;

    /**
     * Maximum number of enumeration-based constants within a class file.
     */
    private int _maxEnumerationsPerClass;

    /**
     * Creates a new EnumerationFactory for the builder configuration given.
     * 
     * @param config
     *            the current BuilderConfiguration instance.
     * @param groupNaming
     *            The group naming scheme to be used.
     * @param sourceGenerator
     *            the calling source generator.
     */
    public EnumerationFactory(final BuilderConfiguration config,
            final GroupNaming groupNaming, final SourceGenerator sourceGenerator) {
        super(config, null, groupNaming, sourceGenerator);
        _typeConversion = new TypeConversion(getConfig());

        // TODO[WG]: add code to read in max. value from builder property file
        _maxEnumerationsPerClass = config.getMaximumNumberOfConstants();
    } // -- SourceFactory

    /**
     * Creates all the necessary enumeration code for a given SimpleType.
     * 
     * @param binding
     *            Extended binding instance
     * @param simpleType
     *            the SimpleType we are processing an enumeration for
     * @param state
     *            our current state
     * @see #processEnumerationAsBaseType
     */
    void processEnumerationAsNewObject(final ExtendedBinding binding,
            final SimpleType simpleType, final FactoryState state) {
        // reset _maxSuffix value to 0
        _maxSuffix = 0;
        boolean generateConstantDefinitions = true;
        int numberOfEnumerationFacets = simpleType
                .getNumberOfFacets(Facet.ENUMERATION);
        if (numberOfEnumerationFacets > _maxEnumerationsPerClass) {
            generateConstantDefinitions = false;
        }

        Enumeration<Facet> enumeration = simpleType
                .getFacets(Facet.ENUMERATION);

        XMLBindingComponent component = new XMLBindingComponent(getConfig(),
                getGroupNaming());
        if (binding != null) {
            component.setBinding(binding);
            component.setView(simpleType);
        }

        // -- select naming for types and instances
        boolean useValuesAsName = true;
        useValuesAsName = selectNamingScheme(component, enumeration,
                useValuesAsName);

        enumeration = simpleType.getFacets(Facet.ENUMERATION);

        JClass jClass = state.getJClass();
        String className = jClass.getLocalName();

        if (component.getJavaClassName() != null) {
            className = component.getJavaClassName();
        }

        // the java 5 way -> create an enum
        if (state.getJClass() instanceof JEnum) {
            createJava5Enum(simpleType, state, component, useValuesAsName,
                    enumeration);
            return;
        }

        JField field = null;
        JField fHash = new JField(SGTypes.createHashtable(getConfig()
                .useJava50()), "_memberTable");
        fHash.setInitString("init()");
        fHash.getModifiers().setStatic(true);

        JSourceCode jsc = null;

        // -- modify constructor
        JConstructor constructor = jClass.getConstructor(0);
        constructor.getModifiers().makePrivate();
        constructor.addParameter(new JParameter(JType.INT, "type"));
        constructor.addParameter(new JParameter(SGTypes.STRING, "value"));
        jsc = constructor.getSourceCode();
        jsc.add("this.type = type;");
        jsc.add("this.stringValue = value;");

        createValueOfMethod(jClass, className);
        createEnumerateMethod(jClass, className);
        createToStringMethod(jClass, className);
        createInitMethod(jClass);
        createReadResolveMethod(jClass);

        // -- Loop through "enumeration" facets
        int count = 0;

        while (enumeration.hasMoreElements()) {
            Facet facet = (Facet) enumeration.nextElement();

            String value = facet.getValue();

            String typeName = null;
            String objName = null;

            if (useValuesAsName) {
                objName = translateEnumValueToIdentifier(component
                        .getEnumBinding(), facet);
            } else {
                objName = "VALUE_" + count;
            }

            // -- create typeName
            // -- Note: this could cause name conflicts
            typeName = objName + "_TYPE";

            // -- Inheritence/Duplicate name cleanup
            boolean addInitializerCode = true;
            if (jClass.getField(objName) != null) {
                // -- either inheritence, duplicate name, or error.
                // -- if inheritence or duplicate name, always take
                // -- the later definition. Do same if error, for now.
                jClass.removeField(objName);
                jClass.removeField(typeName);
                addInitializerCode = false;
            }

            if (generateConstantDefinitions) {
                // -- handle int type
                field = new JField(JType.INT, typeName);
                field.setComment("The " + value + " type");
                JModifiers modifiers = field.getModifiers();
                modifiers.setFinal(true);
                modifiers.setStatic(true);
                modifiers.makePublic();
                field.setInitString(Integer.toString(count));
                jClass.addField(field);

                // -- handle Class type
                field = new JField(jClass, objName);
                field.setComment("The instance of the " + value + " type");

                modifiers = field.getModifiers();
                modifiers.setFinal(true);
                modifiers.setStatic(true);
                modifiers.makePublic();

                StringBuilder init = new StringBuilder(32);
                init.append("new ");
                init.append(className);
                init.append("(");
                init.append(typeName);
                init.append(", \"");
                init.append(escapeValue(value));
                init.append("\")");

                field.setInitString(init.toString());
                jClass.addField(field);

            }
            // -- initializer method

            if (addInitializerCode) {
                jsc = getSourceCodeForInitMethod(jClass);
                jsc.add("members.put(\"");
                jsc.append(escapeValue(value));
                if (_caseInsensitive) {
                    jsc.append("\".toLowerCase(), ");
                } else {
                    jsc.append("\", ");
                }
                if (generateConstantDefinitions) {
                    jsc.append(objName);
                } else {
                    StringBuilder init = new StringBuilder(32);
                    init.append("new ");
                    init.append(className);
                    init.append("(");
                    init.append(Integer.toString(count));
                    init.append(", \"");
                    init.append(escapeValue(value));
                    init.append("\")");
                    jsc.append(init.toString());
                }
                jsc.append(");");
            }

            ++count;
        }

        // -- finish init method
        final JMethod method = jClass.getMethod(this
                .getInitMethodName(_maxSuffix), 0);
        method.getSourceCode().add("return members;");

        // -- add memberTable to the class, we can only add this after all the
        // types,
        // -- or we'll create source code that will generate null pointer
        // exceptions,
        // -- because calling init() will try to add null values to the
        // hashtable.
        jClass.addField(fHash);

        // -- add internal type
        field = new JField(JType.INT, "type");
        field.getModifiers().setFinal(true);
        jClass.addField(field);

        // -- add internal stringValue
        field = new JField(SGTypes.STRING, "stringValue");
        field.setInitString("null");
        jClass.addField(field);

        createGetTypeMethod(jClass, className);
    } // -- processEnumerationAsNewObject

    private void createJava5Enum(final SimpleType simpleType,
            final FactoryState state, final XMLBindingComponent component,
            final boolean useValuesAsName, final Enumeration<Facet> enumeration) {

        AnnotationBuilder[] annotationBuilders = state.getSGStateInfo()
                .getSourceGenerator().getAnnotationBuilders();

        JEnum jEnum = (JEnum) state.getJClass();

        jEnum.removeInterface("java.io.Serializable");
        jEnum.removeAnnotation(new JAnnotationType("SuppressWarnings"));

        // add value field
        JField valueField = new JField(new JClass("java.lang.String"), "value");
        JModifiers modifiers = new JModifiers();
        modifiers.setFinal(true);
        modifiers.makePrivate();
        valueField.setModifiers(modifiers);
        jEnum.addField(valueField);

        // add enumConstants field
        JField enumConstantsField = new JField(
                new JClass("java.util.Map<java.lang.String, "
                        + jEnum.getLocalName() + ">"), "enumConstants");
        modifiers = new JModifiers();
        modifiers.setFinal(true);
        modifiers.makePrivate();
        modifiers.setStatic(true);
        enumConstantsField.setModifiers(modifiers);
        enumConstantsField
                .setInitString("new java.util.HashMap<java.lang.String, "
                        + jEnum.getLocalName() + ">()");
        jEnum.addField(enumConstantsField);

        // add static initialization of enumConstants field
        JSourceCode sourceCode = jEnum.getStaticInitializationCode();
        sourceCode.add("for (" + jEnum.getLocalName() + " c: "
                + jEnum.getLocalName() + ".values()) {");
        sourceCode.indent();
        sourceCode.indent();
        sourceCode.add(jEnum.getLocalName() + "."
                + enumConstantsField.getName() + ".put(c."
                + valueField.getName() + ", c);");
        sourceCode.unindent();
        sourceCode.add("}");

        addValueMethod(jEnum);
        addFromValueMethod(jEnum, enumConstantsField);
        addSetValueMethod(jEnum);
        addToStringMethod(jEnum);

        JConstructor constructor = jEnum.createConstructor();
        constructor.addParameter(new JParameter(new JClass("java.lang.String"),
                "value"));
        constructor.setSourceCode("this.value = value;");
        modifiers = new JModifiers();
        modifiers.makePrivate();
        constructor.setModifiers(modifiers);
        jEnum.addConstructor(constructor);

        int enumCount = 0;
        while (enumeration.hasMoreElements()) {
            Facet facet = enumeration.nextElement();
            JEnumConstant enumConstant;
            if (useValuesAsName) {
                enumConstant = new JEnumConstant(
                        translateEnumValueToIdentifier(component
                                .getEnumBinding(), facet), new String[] { "\""
                                + facet.getValue() + "\"" });
            } else {
                enumConstant = new JEnumConstant("VALUE_" + enumCount,
                        new String[] { "\"" + facet.getValue() + "\"" });
            }

            // custom annotations
            for (int i = 0; i < annotationBuilders.length; i++) {
                AnnotationBuilder annotationBuilder = annotationBuilders[i];
                annotationBuilder.addEnumConstantAnnotations(facet,
                        enumConstant);
            }

            jEnum.addEnumConstant(enumConstant);
            enumCount++;
        }

        // custom annotations
        for (int i = 0; i < annotationBuilders.length; i++) {
            AnnotationBuilder annotationBuilder = annotationBuilders[i];
            annotationBuilder.addEnumAnnotations(simpleType, jEnum);
        }
    }

    /**
     * Adds a {@link JMethod} instance for "value".
     * @param jEnum The {@link JEnum} instance to add this method to.
     */
    private void addValueMethod(JEnum jEnum) {
        JMethod valueMethod = new JMethod("value", new JClass(
                "java.lang.String"), "the value of this constant");
        valueMethod.setSourceCode("return this.value;");
        jEnum.addMethod(valueMethod, false);
    }

    /**
     * Adds a {@link JMethod} instance for "toString".
     * @param jEnum The {@link JEnum} instance to add this method to.
     */
    private void addToStringMethod(JEnum jEnum) {
        JMethod toStringMethod = new JMethod("toString", new JClass(
                "java.lang.String"), "the value of this constant");
        toStringMethod.setSourceCode("return this.value;");
        jEnum.addMethod(toStringMethod, false);
    }

    /**
     * Adds a {@link JMethod} instance for "setValue(String)".
     * @param jEnum The {@link JEnum} instance to add this method to.
     */
    private void addSetValueMethod(JEnum jEnum) {
        JMethod setValueMethod = new JMethod("setValue");
        setValueMethod.addParameter(new JParameter(new JClass(
                "java.lang.String"), "value"));
        jEnum.addMethod(setValueMethod, false);
    }

    /**
     * Adds a {@link JMethod} instance for "fromValue".
     * @param jEnum The {@link JEnum} instance to add this method to.
     */
    private void addFromValueMethod(JEnum jEnum, JField enumConstantsField) {
        JModifiers modifiers;
        JSourceCode sourceCode;
        JMethod fromValueMethod = new JMethod("fromValue", jEnum,
                "the constant for this value");
        fromValueMethod.addParameter(new JParameter(new JClass(
                "java.lang.String"), "value"));
        sourceCode = new JSourceCode();
        sourceCode.add(jEnum.getLocalName() + " c = " + jEnum.getLocalName()
                + "." + enumConstantsField.getName() + ".get(value);");
        sourceCode.add("if (c != null) {");
        sourceCode.indent();
        sourceCode.add("return c;");
        sourceCode.unindent();
        sourceCode.add("}");
        sourceCode.add("throw new IllegalArgumentException(value);");
        fromValueMethod.setSourceCode(sourceCode);
        modifiers = new JModifiers();
        modifiers.setStatic(true);
        fromValueMethod.setModifiers(modifiers);
        jEnum.addMethod(fromValueMethod, false);
    }

    /**
     * Returns the JSourceCode instance for the current init() method, dealing
     * with static initializer limits of the JVM by creating new init() methods
     * as needed.
     * 
     * @param jClass
     *            The JClass instance for which an init method needs to be added
     * @return the JSourceCode instance for the current init() method
     */
    private JSourceCode getSourceCodeForInitMethod(final JClass jClass) {
        final JMethod currentInitMethod = jClass.getMethod(
                getInitMethodName(_maxSuffix), 0);
        if (currentInitMethod.getSourceCode().size() > _maxEnumerationsPerClass) {
            ++_maxSuffix;
            JMethod mInit = createInitMethod(jClass);
            currentInitMethod.getSourceCode().add(
                    "members.putAll(" + mInit.getName() + "());");
            currentInitMethod.getSourceCode().add("return members;");

            return mInit.getSourceCode();
        }
        return currentInitMethod.getSourceCode();
    }

    /**
     * Returns the method name for an init method.
     * 
     * @param index
     *            index of the init method.
     * @return the method name for an init method.
     */
    private String getInitMethodName(final int index) {
        if (index == 0) {
            return "init";
        }

        return "init" + index;
    }

    private boolean selectNamingScheme(final XMLBindingComponent component,
            final Enumeration<Facet> enumeration, final boolean useValuesAsName) {
        boolean duplicateTranslation = false;
        short numberOfTranslationToSpecialCharacter = 0;

        while (enumeration.hasMoreElements()) {
            Facet facet = enumeration.nextElement();
            String possibleId = translateEnumValueToIdentifier(component
                    .getEnumBinding(), facet);
            if (possibleId.equals("_")) {
                numberOfTranslationToSpecialCharacter++;
                if (numberOfTranslationToSpecialCharacter > 1) {
                    duplicateTranslation = true;
                }
            }

            if (!getJavaNaming().isValidJavaIdentifier(possibleId)) {
                return false;
            }
        }

        if (duplicateTranslation) {
            return false;
        }
        return useValuesAsName;
    }

    /**
     * Creates 'getType()' method for this enumeration class.
     * 
     * @param jClass
     *            The enumeration class to create this method for.
     * @param className
     *            The name of the class.
     */
    private void createGetTypeMethod(final JClass jClass, final String className) {
        JMethod mGetType = new JMethod("getType", JType.INT,
                "the type of this " + className);
        mGetType.getSourceCode().add("return this.type;");
        JDocComment jdc = mGetType.getJDocComment();
        jdc.appendComment("Returns the type of this " + className);
        jClass.addMethod(mGetType);
    }

    /**
     * Creates 'readResolve(Object)' method for this enumeration class.
     * 
     * @param jClass
     *            The enumeration class to create this method for.
     */
    private void createReadResolveMethod(final JClass jClass) {
        JDocComment jdc;
        JSourceCode jsc;
        JMethod mReadResolve = new JMethod("readResolve", SGTypes.OBJECT,
                "this deserialized object");
        mReadResolve.getModifiers().makePrivate();
        jClass.addMethod(mReadResolve);
        jdc = mReadResolve.getJDocComment();
        jdc.appendComment(" will be called during deserialization to replace ");
        jdc.appendComment("the deserialized object with the correct constant ");
        jdc.appendComment("instance.");
        jsc = mReadResolve.getSourceCode();
        jsc.add("return valueOf(this.stringValue);");
    }

    /**
     * Creates 'init()' method for this enumeration class.
     * 
     * @param jClass
     *            The enumeration class to create this method for.
     * @return an 'init()' method for this enumeration class.
     */
    private JMethod createInitMethod(final JClass jClass) {
        final String initMethodName = getInitMethodName(_maxSuffix);
        JMethod mInit = new JMethod(initMethodName, SGTypes
                .createHashtable(getConfig().useJava50()),
                "the initialized Hashtable for the member table");
        jClass.addMethod(mInit);
        mInit.getModifiers().makePrivate();
        mInit.getModifiers().setStatic(true);
        if (getConfig().useJava50()) {
            mInit.getSourceCode().add(
                    "java.util.Hashtable<Object, Object> members"
                            + " = new java.util.Hashtable<Object, Object>();");
        } else {
            mInit.getSourceCode().add(
                    "java.util.Hashtable members = new java.util.Hashtable();");
        }
        return mInit;
    }

    /**
     * Creates 'toString()' method for this enumeration class.
     * 
     * @param jClass
     *            The enumeration class to create this method for.
     * @param className
     *            The name of the class.
     */
    private void createToStringMethod(final JClass jClass,
            final String className) {
        JMethod mToString = new JMethod("toString", SGTypes.STRING,
                "the String representation of this " + className);
        jClass.addMethod(mToString);
        JDocComment jdc = mToString.getJDocComment();
        jdc.appendComment("Returns the String representation of this ");
        jdc.appendComment(className);
        mToString.getSourceCode().add("return this.stringValue;");
    }

    /**
     * Creates 'enumerate()' method for this enumeration class.
     * 
     * @param jClass
     *            The enumeration class to create this method for.
     * @param className
     *            The name of the class.
     */
    private void createEnumerateMethod(final JClass jClass,
            final String className) {
        // TODO for the time being return Enumeration<Object> for Java 5.0;
        // change
        JMethod mEnumerate = new JMethod("enumerate", SGTypes
                .createEnumeration(SGTypes.OBJECT, getConfig().useJava50(),
                        true), "an Enumeration over all possible instances of "
                + className);
        mEnumerate.getModifiers().setStatic(true);
        jClass.addMethod(mEnumerate);
        JDocComment jdc = mEnumerate.getJDocComment();
        jdc
                .appendComment("Returns an enumeration of all possible instances of ");
        jdc.appendComment(className);
        mEnumerate.getSourceCode().add("return _memberTable.elements();");
    }

    /**
     * Creates 'valueOf(String)' method for this enumeration class.
     * 
     * @param jClass
     *            The enumeration class to create this method for.
     * @param className
     *            The name of the class.
     */
    private void createValueOfMethod(final JClass jClass, final String className) {
        JMethod mValueOf = new JMethod("valueOf", jClass, "the " + className
                + " value of parameter 'string'");
        mValueOf.addParameter(new JParameter(SGTypes.STRING, "string"));
        mValueOf.getModifiers().setStatic(true);
        jClass.addMethod(mValueOf);

        JDocComment jdc = mValueOf.getJDocComment();
        jdc.appendComment("Returns a new " + className);
        jdc.appendComment(" based on the given String value.");

        JSourceCode jsc = mValueOf.getSourceCode();
        jsc.add("java.lang.Object obj = null;\n" + "if (string != null) {\n"
                + " obj = _memberTable.get(string{1});\n" + "}\n"
                + "if (obj == null) {\n"
                + " String err = \"'\" + string + \"' is not a valid {0}\";\n"
                + " throw new IllegalArgumentException(err);\n" + "}\n"
                + "return ({0}) obj;", className,
                (_caseInsensitive ? ".toLowerCase()" : ""));

    }

    /**
     * Creates all the necessary enumeration code from the given SimpleType.
     * Enumerations are handled by creating an Object like the following:
     * 
     * <pre>
     *     public class {name} {
     *         // list of values
     *         {type}[] values = {
     *             ...
     *         };
     * 
     *         // Returns true if the given value is part
     *         // of this enumeration
     *         public boolean contains({type} value);
     * 
     *         // Returns the {type} value whose String value
     *         // is equal to the given String
     *         public {type} valueOf(String strValue);
     *     }
     * </pre>
     * 
     * @param binding
     *            Extended binding instance
     * @param simpleType
     *            the SimpleType we are processing an enumeration for
     * @param state
     *            our current state
     */
    void processEnumerationAsBaseType(final ExtendedBinding binding,
            final SimpleType simpleType, final FactoryState state) {
        SimpleType base = (SimpleType) simpleType.getBaseType();
        XSType baseType = null;

        if (base == null) {
            baseType = new XSString();
        } else {
            baseType = _typeConversion.convertType(base, getConfig()
                    .useJava50());
        }

        Enumeration<Facet> enumeration = simpleType
                .getFacets(Facet.ENUMERATION);

        JClass jClass = state.getJClass();
        String className = jClass.getLocalName();

        JField fValues = null;
        JDocComment jdc = null;
        JSourceCode jsc = null;

        // -- modify constructor
        JConstructor constructor = jClass.getConstructor(0);
        constructor.getModifiers().makePrivate();

        fValues = new JField(new JArrayType(baseType.getJType(), getConfig()
                .useJava50()), "values");

        // -- Loop through "enumeration" facets
        // -- and create the default values for the type.
        int count = 0;

        StringBuilder values = new StringBuilder("{\n");

        while (enumeration.hasMoreElements()) {
            Facet facet = (Facet) enumeration.nextElement();
            String value = facet.getValue();

            // -- Should we make sure the value is valid before proceeding??

            // -- we need to move this code to XSType so that we don't have to
            // do
            // -- special code here for each type

            if (count > 0) {
                values.append(",\n");
            }

            // -- indent for fun
            values.append("    ");

            if (baseType.getType() == XSType.STRING_TYPE) {
                values.append('\"');
                // -- escape value
                values.append(escapeValue(value));
                values.append('\"');
            } else {
                values.append(value);
            }

            ++count;
        }

        values.append("\n}");

        fValues.setInitString(values.toString());
        jClass.addField(fValues);

        // -- #valueOf method
        JMethod method = new JMethod("valueOf", jClass,
                "the String value of the provided " + baseType.getJType());
        method.addParameter(new JParameter(SGTypes.STRING, "string"));
        method.getModifiers().setStatic(true);
        jClass.addMethod(method);
        jdc = method.getJDocComment();
        jdc.appendComment("Returns the " + baseType.getJType());
        jdc.appendComment(" based on the given String value.");
        jsc = method.getSourceCode();

        jsc.add("for (int i = 0; i < values.length; i++) {");
        jsc.add("}");
        jsc.add("throw new IllegalArgumentException(\"");
        jsc.append("Invalid value for ");
        jsc.append(className);
        jsc.append(": \" + string + \".\");");
    } // -- processEnumerationAsBaseType

    /**
     * Attempts to translate a simpleType enumeration value into a legal java
     * identifier. Translation is through a couple of simple rules:
     * <ul>
     * <li>if the value parses as a non-negative int, the string 'VALUE_' is
     * prepended to it</li>
     * <li>if the value parses as a negative int, the string 'VALUE_NEG_' is
     * prepended to it</li>
     * <li>the value is uppercased</li>
     * <li>the characters <code>[](){}<>'`"</code> are removed</li>
     * <li>the characters <code>|\/?~!@#$%^&*-+=:;.,</code> and any whitespace
     * are replaced with <code>_</code></li>
     * </ul>
     * 
     * @param enumBinding
     *            if not null, a possible custom binding for this enum
     * @param facet
     *            the facet whose enum value is being translated.
     * @return the identifier for the enum value
     * 
     * @author rhett-sutphin@uiowa.edu
     */
    private String translateEnumValueToIdentifier(
            final EnumBindingType enumBinding, final Facet facet) {
        String enumValue = facet.getValue();

        try {
            String enumerationValue = null;
            int intVal = Integer.parseInt(facet.getValue());

            String customMemberName = null;
            if (enumBinding != null) {
                customMemberName = getCustomMemberName(enumBinding, String
                        .valueOf(intVal));
            }

            if (customMemberName != null) {
                enumerationValue = customMemberName;
            } else {
                if (intVal >= 0) {
                    enumerationValue = "VALUE_" + intVal;
                } else {
                    enumerationValue = "VALUE_NEG_" + Math.abs(intVal);
                }
            }

            return enumerationValue;
        } catch (NumberFormatException e) {
            // just keep going
        }

        StringBuilder sb = new StringBuilder(32);
        String customMemberName = null;

        if (enumBinding != null) {
            customMemberName = getCustomMemberName(enumBinding, enumValue);
        }

        if (customMemberName != null) {
            sb.append(customMemberName);
        } else {
            sb.append(enumValue.toUpperCase());
            int i = 0;
            while (i < sb.length()) {
                char c = sb.charAt(i);
                if ("[](){}<>'`\"".indexOf(c) >= 0) {
                    sb.deleteCharAt(i);
                    i--;
                } else if (Character.isWhitespace(c)
                        || "\\/?~!@#$%^&*-+=:;.,".indexOf(c) >= 0) {
                    sb.setCharAt(i, '_');
                }
                i++;
            }
        }
        return sb.toString();
    }

    /**
     * Returns a custom member name (if defined).
     * 
     * @param enumBinding
     *            The {@link EnumBindingType} instance
     * @param enumValue
     *            The enumeration value.
     * @return the custom member name
     */
    private String getCustomMemberName(final EnumBindingType enumBinding,
            final String enumValue) {
        // check whether there's a custom binding for the member name
        String customMemberName = null;
        EnumMember[] enumMembers = enumBinding.getEnumMember();
        for (int i = 0; i < enumMembers.length; i++) {
            if (enumMembers[i].getValue().equals(enumValue)) {
                customMemberName = enumMembers[i].getJavaName();
            }
        }
        return customMemberName;
    }

    /**
     * Set to true if enumerated type lookups should be performed in a case
     * insensitive manner.
     * 
     * @param caseInsensitive
     *            when true
     */
    public void setCaseInsensitive(final boolean caseInsensitive) {
        _caseInsensitive = caseInsensitive;
    }

    /**
     * Escapes special characters in the given String so that it can be printed
     * correctly.
     * 
     * @param str
     *            the String to escape
     * @return the escaped String, or null if the given String was null.
     */
    private static String escapeValue(final String str) {
        if (str == null) {
            return str;
        }

        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            switch (ch) {
            case '\\':
            case '\"':
            case '\'':
                sb.append('\\');
                break;
            default:
                break;
            }
            sb.append(ch);
        }
        return sb.toString();
    } // -- escapeValue

}
