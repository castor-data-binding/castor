/*
 * Copyright 2006 Werner Guttmann
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
package org.exolab.castor.builder;

import org.exolab.castor.builder.info.CollectionInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.builder.info.XMLInfo;
import org.exolab.castor.builder.types.XSClass;
import org.exolab.castor.builder.types.XSList;
import org.exolab.castor.builder.types.XSType;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.javasource.JArrayType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JPrimitiveType;
import org.exolab.javasource.JSourceCode;

/**
 * A factory for creating XMLFieldHandler instances as embedded in descriptors classes
 * generated throughout code generation.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6469 $ $Date: 2006-04-13 07:37:49 -0600 (Thu, 13 Apr 2006) $
 */
public class XMLFieldHandlerFactory {

    /**
     * The XML code generator configuration.
     */
    private BuilderConfiguration _config;

    /**
     * Creates an instance of this factory.
     * @param config The XML code generator configuration.
     */
    public XMLFieldHandlerFactory(final BuilderConfiguration config) {
        _config = config;
    }

    /**
     * Creates the XMLFieldHandler for the given FieldInfo.
     *
     * @param member the member for which to create an XMLFieldHandler
     * @param xsType the XSType (XML Schema Type) of this field
     * @param localClassName unqualified (no package) name of this class
     * @param jsc the source code to which we'll add this XMLFieldHandler
     * @param forGeneralizedHandler Whether to generate a generalized field
     *        handler
     */
    public void createXMLFieldHandler(final FieldInfo member,
            final XSType xsType, final String localClassName,
            final JSourceCode jsc, final boolean forGeneralizedHandler) {

        boolean any = false;
        boolean isEnumerated = false;

        // -- a hack, I know, I will change later (kv)
        if (member.getName().equals("_anyObject")) {
            any = true;
        }

        if (xsType.getType() == XSType.CLASS) {
            isEnumerated = ((XSClass) xsType).isEnumerated();
        }

        jsc.add("handler = new org.exolab.castor.xml.XMLFieldHandler() {");
        jsc.indent();

        createGetValueMethod(member, xsType, localClassName, jsc);

        boolean isAttribute = (member.getNodeType() == XMLInfo.ATTRIBUTE_TYPE);
        boolean isContent = (member.getNodeType() == XMLInfo.TEXT_TYPE);

        createSetValueMethod(member, xsType, localClassName, jsc, any, isAttribute, isContent);
        createResetMethod(member, localClassName, jsc);
        createNewInstanceMethod(member, xsType, jsc, forGeneralizedHandler, any, isEnumerated);

        jsc.unindent();
        jsc.add("};");
    } // --end of XMLFieldHandler

    /**
     * Creates the getValue() method of the corresponsing XMLFieldHandler.
     *
     * @param member The member element.
     * @param xsType The XSType instance
     * @param jsc The source code to which to append the 'getValue' method.
     * @param localClassName Name of the object instance as used locally
     */
    private void createGetValueMethod(final FieldInfo member, final XSType xsType,
            final String localClassName, final JSourceCode jsc) {
        // -- getValue(Object) method
        if (_config.useJava50()) {
            jsc.add("@Override");
        }
        jsc.add("public java.lang.Object getValue( java.lang.Object object ) ");
        jsc.indent();
        jsc.add("throws IllegalStateException");
        jsc.unindent();
        jsc.add("{");
        jsc.indent();
        jsc.add(localClassName);
        jsc.append(" target = (");
        jsc.append(localClassName);
        jsc.append(") object;");
        // -- handle primitives
        if ((!xsType.isEnumerated()) && (xsType.getJType().isPrimitive())
                && (!member.isMultivalued())) {
            jsc.add("if(!target." + member.getHasMethodName() + "())");
            jsc.indent();
            jsc.add("return null;");
            jsc.unindent();
        }
        // -- Return field value
        jsc.add("return ");
        String value = "target." + member.getReadMethodName() + "()";
        if (member.isMultivalued()) {
            jsc.append(value); // --Be careful : different for attributes
        } else {
            jsc.append(xsType.createToJavaObjectCode(value));
        }
        jsc.append(";");
        jsc.unindent();
        jsc.add("}");
        // --end of getValue(Object) method
    }

    /**
     * Creates the setValue() method of the corresponsing XMLFieldHandler.
     *
     * @param member The member element.
     * @param xsType The XSType instance
     * @param localClassName Name of the object instance as used locally
     * @param jsc The source code to which to append the 'setValue' method.
     * @param any Whether to create a setValue() method for &lt;xs:any>
     * @param isAttribute Whether to create a setValue() method for an
     *        attribute.
     * @param isContent Whether to create a setValue() method for XML content.
     */
    private void createSetValueMethod(final FieldInfo member,
            final XSType xsType, final String localClassName,
            final JSourceCode jsc, final boolean any, final boolean isAttribute,
            final boolean isContent) {
        if (_config.useJava50()) {
            jsc.add("@Override");
        }
        jsc.add("public void setValue( java.lang.Object object, java.lang.Object value) ");
        jsc.indent();
        jsc.add("throws IllegalStateException, IllegalArgumentException");
        jsc.unindent();
        jsc.add("{");
        jsc.indent();
        jsc.add("try {");
        jsc.indent();
        jsc.add(localClassName);
        jsc.append(" target = (");
        jsc.append(localClassName);
        jsc.append(") object;");
        // -- check for null primitives
        if (xsType.isPrimitive() && !_config.usePrimitiveWrapper()) {
            if ((!member.isRequired()) && (!xsType.isEnumerated())
                    && (!member.isMultivalued())) {
                jsc
                        .add("// if null, use delete method for optional primitives ");
                jsc.add("if (value == null) {");
                jsc.indent();
                jsc.add("target.");
                jsc.append(member.getDeleteMethodName());
                jsc.append("();");
                jsc.add("return;");
                jsc.unindent();
                jsc.add("}");
            } else {
                jsc.add("// ignore null values for non optional primitives");
                jsc.add("if (value == null) return;");
                jsc.add("");
            }
        } // if primitive

        jsc.add("target.");
        jsc.append(member.getWriteMethodName());
        jsc.append("( ");
        if (xsType.isPrimitive() && !_config.usePrimitiveWrapper()) {
            jsc.append(xsType.createFromJavaObjectCode("value"));
        } else if (any) {
            jsc.append(" value ");
        } else {
            jsc.append("(");
            jsc.append(xsType.getJType().toString());
            // special handling for the type package
            // when we are dealing with attributes
            // This is a temporary solution since we need to handle
            // the 'types' in specific handlers in the future
            // i.e add specific FieldHandler in org.exolab.castor.xml.handlers
            // dateTime is not concerned by the following since it is directly
            // handle by DateFieldHandler
            if ((isAttribute | isContent) && xsType.isDateTime()
                    && xsType.getType() != XSType.DATETIME_TYPE) {
                jsc.append(".parse");
                jsc.append(JavaNaming.toJavaClassName(xsType.getName()));
                jsc.append("((java.lang.String) value))");
            } else {
                jsc.append(") value");
            }
        }
        jsc.append(");");

        jsc.unindent();
        jsc.add("}");
        jsc.add("catch (java.lang.Exception ex) {");
        jsc.indent();
        jsc.add("throw new IllegalStateException(ex.toString());");
        jsc.unindent();
        jsc.add("}");
        jsc.unindent();
        jsc.add("}");
        // --end of setValue(Object, Object) method
    }

    /**
     * Creates the resetValue() method of the corresponsing XMLFieldHandler.
     *
     * @param member The member element.
     * @param jsc The source code to which to append the 'resetValue' method.
     * @param localClassName Name of the object instance as used locally
     */
    private void createResetMethod(final FieldInfo member,
            final String localClassName,
            final JSourceCode jsc) {
        // -- reset method (handle collections only)
        if (member.isMultivalued()) {
            CollectionInfo cInfo = (CollectionInfo) member;
            // FieldInfo content = cInfo.getContent();
            jsc.add("public void resetValue(Object object)"
                    + " throws IllegalStateException, IllegalArgumentException {");
            jsc.indent();
            jsc.add("try {");
            jsc.indent();
            jsc.add(localClassName);
            jsc.append(" target = (");
            jsc.append(localClassName);
            jsc.append(") object;");
            String cName = JavaNaming.toJavaClassName(cInfo.getElementName());
            // if (cInfo instanceof CollectionInfoJ2) {
            // jsc.add("target.clear" + cName + "();");
            // } else {
            jsc.add("target.removeAll" + cName + "();");
            // }
            jsc.unindent();
            jsc.add("} catch (java.lang.Exception ex) {");
            jsc.indent();
            jsc.add("throw new IllegalStateException(ex.toString());");
            jsc.unindent();
            jsc.add("}");
            jsc.unindent();
            jsc.add("}");
        }
        // -- end of reset method
    }

    /**
     * Creates the newInstance() method of the corresponsing XMLFieldHandler.
     *
     * @param member The member element.
     * @param xsType The XSType instance
     * @param jsc The source code to which to append the 'newInstance' method.
     * @param forGeneralizedHandler Whether to generate a generalized field
     *        handler
     * @param any Whether to create a newInstance() method for &lt;xs:any>
     * @param isEnumerated Whether to create a newInstance() method for an
     *        enumeration.
     */
    private void createNewInstanceMethod(final FieldInfo member,
            final XSType xsType, final JSourceCode jsc,
            final boolean forGeneralizedHandler, final boolean any,
            final boolean isEnumerated) {
        boolean isAbstract = false;

        // Commented out according to CASTOR-1340
        // if (member.getDeclaringClassInfo() != null) {
        // isAbstract = member.getDeclaringClassInfo().isAbstract();
        // }

        // check whether class of member is declared as abstract
        if (member.getSchemaType() != null
                && member.getSchemaType().getJType() instanceof JClass) {
            JClass jClass = (JClass) member.getSchemaType().getJType();
            isAbstract = jClass.getModifiers().isAbstract();
        }

        if (!isAbstract && xsType.getJType() instanceof JClass) {
            JClass jClass = (JClass) xsType.getJType();
            isAbstract = jClass.getModifiers().isAbstract();
        }

        if (!isAbstract && member.getSchemaType() instanceof XSList) {
            XSList xsList = (XSList) member.getSchemaType();
            if (xsList.getContentType().getJType() instanceof JClass) {
                JClass componentType = (JClass) xsList.getContentType().getJType();
                if (componentType.getModifiers().isAbstract()) {
                    isAbstract = componentType.getModifiers().isAbstract();
                }
            }
        }

        if (_config.useJava50()) {
            jsc.add("@Override");
            jsc.add("@SuppressWarnings(\"unused\")");
        }

        jsc.add("public java.lang.Object newInstance(java.lang.Object parent) {");
        jsc.indent();
        jsc.add("return ");

        if (any || forGeneralizedHandler || isEnumerated
                || xsType.isPrimitive()
                || xsType.getJType().isArray()
                || (xsType.getType() == XSType.STRING_TYPE) || isAbstract) {
            jsc.append("null;");
        } else {
            jsc.append(xsType.newInstanceCode());
        }

        jsc.unindent();
        jsc.add("}");
    }

}
