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
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.javasource.*;
import org.exolab.castor.builder.util.DescriptorJClass;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.castor.builder.types.*;
import org.exolab.castor.types.*;

/**
 * A factory for creating the source code of descriptor classes
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class DescriptorSourceFactory {


    //-- org.exolab.castor.mapping
    private static JClass _ClassDescriptorClass
        = new JClass("org.exolab.castor.mapping.ClassDescriptor");

    private static JClass _FieldDescriptorClass
        = new JClass("org.exolab.castor.mapping.FieldDescriptor");

    //-- org.exolab.castor.xml
    private static JClass fdImplClass
        = new JClass("org.exolab.castor.xml.util.XMLFieldDescriptorImpl");


    private static JClass fdClass
        = new JClass("org.exolab.castor.xml.XMLFieldDescriptor");


    private static JType fdArrayClass = fdClass.createArray();

    private static JClass gvrClass
        = new JClass("org.exolab.castor.xml.GroupValidationRule");

    private static JClass vrClass
        = new JClass("org.exolab.castor.xml.ValidationRule");


    /**
     * Creates the Source code of a MarshalInfo for a given XML Schema
     * element declaration
     * @param element the XML Schema element declaration
     * @return the JClass representing the MarshalInfo source code
    **/
    public static JClass createSource(ClassInfo classInfo) {


        JMethod     method          = null;
        JSourceCode jsc             = null;
        JSourceCode vcode           = null;
        JClass      jClass          = classInfo.getJClass();
        String      className       = jClass.getName();
        String      localClassName  = jClass.getLocalName();


        String variableName = "_"+className;

        DescriptorJClass classDesc
            = new DescriptorJClass(className+"Descriptor", jClass);

        //-- get handle to default constuctor

        JConstructor cons = classDesc.getConstructor(0);
        jsc = cons.getSourceCode();
		jsc.add("super();");

        //-- Set namespace prefix
        String nsPrefix    = classInfo.getNamespacePrefix();
        if ((nsPrefix != null) && (nsPrefix.length() > 0)) {
            jsc.add("nsPrefix = \"");
            jsc.append(nsPrefix);
            jsc.append("\";");
        }

        //-- Set namespace URI
        String nsURI       = classInfo.getNamespaceURI();
        if ((nsURI != null) && (nsURI.length() > 0)) {
            jsc.add("nsURI = \"");
            jsc.append(nsURI);
            jsc.append("\";");
        }

        //-- set XML Name
        String xmlName     = classInfo.getNodeName();
        if (xmlName != null) {
            jsc.add("xmlName = \"");
            jsc.append(xmlName);
            jsc.append("\";");
        }

        jsc.add("XMLFieldDescriptorImpl  desc           = null;");
        jsc.add("XMLFieldHandler         handler        = null;");
        jsc.add("FieldValidator          fieldValidator = null;");


        //-- set grouping compositor
        if (classInfo.isChoice()) {
            jsc.add("");
            jsc.add("//-- set grouping compositor");
            jsc.add("setCompositorAsChoice();");
        }
        else if (classInfo.isSequence()) {
            jsc.add("");
            jsc.add("//-- set grouping compositor");
            jsc.add("setCompositorAsSequence();");
        }


        //-- handle  content
        if (classInfo.allowContent())
           createDescriptor(classInfo.getTextField(), localClassName, null, jsc);


        FieldInfo[] atts = classInfo.getAttributeFields();

        //--------------------------------/
        //- Create attribute descriptors -/
        //--------------------------------/

        jsc.add("//-- initialize attribute descriptors");
        jsc.add("");

        for (int i = 0; i < atts.length; i++) {
            FieldInfo member = atts[i];
            //-- skip transient members
            if (member.isTransient()) continue;
            createDescriptor(member, localClassName, nsURI, jsc);
       }


        //------------------------------/
        //- Create element descriptors -/
        //------------------------------/
       FieldInfo[] elements = classInfo.getElementFields();

        jsc.add("//-- initialize element descriptors");
        jsc.add("");

        for (int i = 0; i < elements.length; i++) {
            FieldInfo member = elements[i];
            //-- skip transient members
            if (member.isTransient()) continue;
            createDescriptor(member, localClassName, nsURI, jsc);
        }

        return classDesc;

    } //-- createSource

    //-------------------/
    //- Private Methods -/
    //-------------------/
    /**
     * Create a specific descriptor for a given member (whether an attribute or
     * an element) represented by a given Class name
     */
    private static void createDescriptor(FieldInfo member, String localClassName,
                                  String nsURI, JSourceCode jsc)
    {

       XSType xsType = member.getSchemaType();
       boolean any = false;
       boolean isEnumerated = false;
       boolean isElement = (member.getNodeType() == FieldInfo.ELEMENT_TYPE);
       boolean isAttribute = (member.getNodeType() == FieldInfo.ATTRIBUTE_TYPE);
       boolean isText = (member.getNodeType() == FieldInfo.TEXT_TYPE);

       jsc.add("//-- ");
       jsc.append(member.getName());


       //-- a hack, I know, I will change later (kv)
       if (member.getName().equals("_anyObject"))
           any = true;


           /*jsc.add("desc = (new XMLFieldDescriptorImpl(");
           jsc.append("Object.class, \"");
           jsc.append(member.getName());
           jsc.append("\", (String)null, NodeType.Element) { ");
           jsc.indent();
           jsc.add("public boolean matches(String xmlName) {");
           jsc.add("    return true;");
           jsc.add("}");
           jsc.unindent();
           jsc.add("});");
        }
        else {*/
        //the previous section should not be used anymore

            if (xsType.getType() == XSType.COLLECTION)
                //Attributes can handle COLLECTION type for NMTOKENS or IDREFS for instance
                xsType = ((CollectionInfo)member).getContent().getSchemaType();

            jsc.add("desc = new XMLFieldDescriptorImpl(");
            jsc.append(classType(xsType.getJType()));
            jsc.append(", \"");
            jsc.append(member.getName());
            jsc.append("\", ");
            String nodeName = member.getNodeName();
            if ( (nodeName != null) && (!isText)) {
                jsc.append("\"");
                jsc.append(member.getNodeName());
                jsc.append("\"");
            } else if (isText) {
                jsc.append("\"PCDATA\"");
            } else {
                   jsc.append("(String)null");
            }

            if (isElement)
               jsc.append(", NodeType.Element);");
            else if (isAttribute)
               jsc.append(", NodeType.Attribute);");
            else if (isText)
               jsc.append(", NodeType.Text);");
            switch (xsType.getType()) {
                case XSType.CLASS:
                   isEnumerated = ((XSClass)xsType).isEnumerated();
                   break;
               case XSType.STRING_TYPE:
                   jsc.add("desc.setImmutable(true);");
                   break;
               //only for attributes
               case XSType.IDREF_TYPE:
                       jsc.add("desc.setReference(true);" );
                       break;
               case XSType.ID_TYPE:
                       jsc.add("this.identity = desc;" );
                       break;
               /***********************/
               default:
                   break;
           } //switch

        //}//else

        //-- handler access methods

       jsc.add("handler = (new XMLFieldHandler() {");
       jsc.indent();

       //-- read method
       jsc.add("public Object getValue( Object object ) ");
       jsc.indent();
       jsc.add("throws IllegalStateException");
       jsc.unindent();
       jsc.add("{");
       jsc.indent();
       jsc.add(localClassName);
       jsc.append(" target = (");
       jsc.append(localClassName);
       jsc.append(") object;");
       //-- handle primitives
	   if ((!xsType.isEnumerated()) && xsType.isPrimitive() && (!member.isMultivalued()))
       {
		   jsc.add("if(!target."+member.getHasMethodName()+"())");
		   jsc.indent();
           jsc.add("return null;");
           jsc.unindent();
        }
        //-- Return field value
        jsc.add("return ");
        String value = "target."+member.getReadMethodName()+"()";
        if (member.isMultivalued()) jsc.append(value);//--Be careful : different for attributes
        else jsc.append(xsType.createToJavaObjectCode(value));
        jsc.append(";");
        jsc.unindent();
        jsc.add("}");
        //--end of read method

        //-- write method
        jsc.add("public void setValue( Object object, Object value) ");
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
        //-- check for null primitives
        if (xsType.isPrimitive()) {
            if ((!member.isRequired()) && (!xsType.isEnumerated()) && (!member.isMultivalued())) {
                 jsc.add("// if null, use delete method for optional primitives ");
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
        }//if primitive

        jsc.add("target.");
        jsc.append(member.getWriteMethodName());
        jsc.append("( ");
        if (xsType.isPrimitive()) {
             jsc.append(xsType.createFromJavaObjectCode("value"));
        } else if (any) {
            jsc.append(" value ");
        } else {
            jsc.append("(");
            jsc.append(xsType.getJType().toString());
            //special handling for the type package
            //when we are dealing with attributes
            //This is a temporary solution since we need to handle
            //the 'types' in specific handlers in the future
            //i.e add specific FieldHandler in org.exolab.castor.xml.handlers
            if (isAttribute && xsType.isDateTime()) {
                jsc.append(".parse");
                jsc.append(JavaNaming.toJavaClassName(xsType.getName()));
                jsc.append("((String) value))");
            }

            else jsc.append(") value");
        }
        jsc.append(");");

        jsc.unindent();
        jsc.add("}");
        jsc.add("catch (Exception ex) {");
        jsc.indent();
        jsc.add("throw new IllegalStateException(ex.toString());");
        jsc.unindent();
        jsc.add("}");
        jsc.unindent();
        jsc.add("}");
        //--end of write method


        //-- newInstance method
        jsc.add("public Object newInstance( Object parent ) {");
        jsc.indent();
        jsc.add("return ");

        if (any || isEnumerated ||
                xsType.isPrimitive() ||
                xsType.getJType().isArray() ||
                (xsType.getType() == XSType.STRING_TYPE))
        {
            jsc.append("null;");
        }
        else {
            jsc.append(xsType.newInstanceCode());
        }
        jsc.unindent();
        jsc.add("}");
        //--end of new Instance method
        jsc.unindent();
        jsc.add("} );");
        //--end of XMLFieldDescriptor

        if (isEnumerated) {
            jsc.add("desc.setHandler( new EnumFieldHandler(");
            jsc.append(classType(xsType.getJType()));
            jsc.append(", handler));");
            jsc.add("desc.setImmutable(true);");
        }
        else if (xsType.getType() == XSType.DATETIME_TYPE) {
            jsc.add("desc.setHandler( new DateFieldHandler(");
            jsc.append("handler));");
            jsc.add("desc.setImmutable(true);");
        }
        else if (xsType.getType() == XSType.DECIMAL_TYPE) {
            jsc.add("desc.setHandler( new DecimalFieldHandler(");
            jsc.append("handler));");
            jsc.add("desc.setImmutable(true);");
        }
        //-- We need to handle NMTOKENS and IDREFS with the
        //-- CollectionFieldHandler
        else if (member.getSchemaType().getType() == XSType.COLLECTION) {

            switch (xsType.getType()) {
                case XSType.NMTOKEN_TYPE:
                case XSType.NMTOKENS_TYPE:
                case XSType.IDREF_TYPE:
                case XSType.IDREFS_TYPE:
                    jsc.add("desc.setHandler( new CollectionFieldHandler(");
                    jsc.append("handler));");
                    break;
                default:
                    jsc.add("desc.setHandler(handler);");
                    break;
            }
        }
        else jsc.add("desc.setHandler(handler);");


        //-- container
        if (member.isContainer()) {
            jsc.add("desc.setContainer(true);");
        }

        //-- namespace
        if (nsURI != null) {
            jsc.add("desc.setNameSpaceURI(\"");
            jsc.append(nsURI);
            jsc.append("\");");
        }

        //-- required
        if (member.isRequired()) {
            jsc.add("desc.setRequired(true);");
        }
        //-- if any it can match all the names
        if (any)
           jsc.add("desc.setMatches(\"*\");");
        //-- mark as multi or single valued for elements
        if (isElement) {
            jsc.add("desc.setMultivalued("+member.isMultivalued());
            jsc.append(");");
        }

        jsc.add("addFieldDescriptor(desc);");
        jsc.add("");

        //-- Add Validation Code
        jsc.add("//-- validation code for: ");
        jsc.append(member.getName());
        jsc.add("fieldValidator = new FieldValidator();");
        validationCode(member, jsc);
        jsc.add("desc.setValidator(fieldValidator);");
        jsc.add("");

    }//--CreateDescriptor

    /**
     * Returns the Class type (as a String) for the given XSType
    **/
    private static String classType(JType jType) {
        if (jType.isPrimitive()) {
            if (jType == JType.Int)
                return "java.lang.Integer.TYPE";
            else if (jType == JType.Double)
                return "java.lang.Double.TYPE";
            else if (jType == JType.Boolean) {
                return "java.lang.Boolean.TYPE";
            }
        }
        return jType.toString() + ".class";
    } //-- classType

    private static void validationCode(FieldInfo member, JSourceCode jsc) {

        //-- a hack, I know, I will change later
        if (member.getName().equals("_anyObject")) return;

        XSType xsType = member.getSchemaType();

        //-- create local copy of field
        //JMember jMember = member.createMember();

        if (xsType.getType() != XSType.COLLECTION) {
            if (member.isRequired()) {
                jsc.add("fieldValidator.setMinOccurs(1);");
            }
        }

        String fixed = member.getFixedValue();
        String pattern = null;
        int fractionDigits = -1;
        int totalDigits = -1;
        //-- create proper validator

        switch (xsType.getType()) {

            case XSType.BYTE_TYPE:
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("ByteValidator bv = new ByteValidator();");
                XSByte xsByte = (XSByte)xsType;
                if (xsByte.hasMinimum()) {
                    Byte min = xsByte.getMinExclusive();
                    if (min != null)
                        jsc.add("bv.setMinExclusive(");
                    else {
                        min = xsByte.getMinInclusive();
                        jsc.add("bv.setMinInclusive(");
                    }
                    jsc.append("(byte)");
                    jsc.append(min.toString());
                    jsc.append(");");
                }
                if (xsByte.hasMaximum()) {
                    Byte max = xsByte.getMaxExclusive();
                    if (max != null)
                        jsc.add("bv.setMaxExclusive(");
                    else {
                        max = xsByte.getMaxInclusive();
                        jsc.add("bv.setMaxInclusive(");
                    }
                    jsc.append("(byte)");
                    jsc.append(max.toString());
                    jsc.append(");");
                }

                //-- fixed values
                if (fixed != null) {
                    //-- make sure we have a valid value...
                    Byte.parseByte(fixed);

                    jsc.add("bv.setFixedValue(");
                    jsc.append("(byte)");
                    jsc.append(fixed);
                    jsc.append(");");
                }
                //-- pattern facet
                pattern = xsByte.getPattern();
                if (pattern != null) {
                    jsc.add("bv.setPattern(\"");
                    jsc.append(escapePattern(pattern));
                    jsc.append("\");");
                }

                jsc.add("fieldValidator.setValidator(bv);");
                jsc.unindent();
                jsc.add("}");
                break;

            case XSType.DECIMAL_TYPE:
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("DecimalValidator dv = new DecimalValidator();");
                XSDecimal xsDecimal = (XSDecimal)xsType;
                if (xsDecimal.hasMinimum()) {
                    java.math.BigDecimal min = xsDecimal.getMinExclusive();
                    if (min != null)
                        jsc.add("dv.setMinExclusive(new java.math.BigDecimal(");
                    else {
                        min = xsDecimal.getMinInclusive();
                        jsc.add("dv.setMinInclusive(new java.math.BigDecimal(");
                    }
                    jsc.append(min.toString()+")");
                    jsc.append(");");
                }
                if (xsDecimal.hasMaximum()) {
                    java.math.BigDecimal max = xsDecimal.getMaxExclusive();
                    if (max != null)
                        jsc.add("dv.setMaxExclusive(new java.math.BigDecimal(");
                    else {
                        max = xsDecimal.getMaxInclusive();
                        jsc.add("dv.setMaxInclusive(new java.math.BigDecimal(");
                    }
                    jsc.append(max.toString()+")");
                    jsc.append(");");
                }

                 //-- totalDigits
                 totalDigits = xsDecimal.getTotalDigits();

                if (totalDigits != -1) {
                   jsc.add("dv.setTotalDigits(");
                   jsc.append(Integer.toString(totalDigits));
                   jsc.append(");");
                }

                 //-- fractionDigits
                fractionDigits = xsDecimal.getFractionDigits();
                if (fractionDigits != -1) {
                   jsc.add("dv.setFractionDigits(");
                   jsc.append(Integer.toString(fractionDigits));
                   jsc.append(");");
                }

                jsc.add("fieldValidator.setValidator(dv);");
                jsc.unindent();
                jsc.add("}");
                break;

            //-- float
            case XSType.FLOAT_TYPE:
            {
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("FloatValidator fv = new FloatValidator();");
                XSFloat xsFloat = (XSFloat)xsType;
                if (xsFloat.hasMinimum()) {
                    Float min = xsFloat.getMinExclusive();
                    if (min != null)
                        jsc.add("fv.setMinExclusive( new Float(");
                    else {
                        min = xsFloat.getMinInclusive();
                        jsc.add("fv.setMinInclusive( new Float(");
                    }
                    if ( (min.equals(new Float(Float.NEGATIVE_INFINITY))) )
                        jsc.append("Float.NEGATIVE_INFINITY");
                    else if ( (min.equals(new Float(Float.POSITIVE_INFINITY))) )
                        jsc.append("Float.POSITIVE_INFINITY");
                    else jsc.append(min.toString());
                    jsc.append("));");
                }
                if (xsFloat.hasMaximum()) {
                    Float max = xsFloat.getMaxExclusive();
                    if (max != null)
                        jsc.add("fv.setMaxExclusive( new Float(");
                    else {
                        max = xsFloat.getMaxInclusive();
                        jsc.add("fv.setMaxInclusive( )");
                    }
                    if ( (max.equals(new Float(Float.NEGATIVE_INFINITY))) )
                        jsc.append("Float.NEGATIVE_INFINITY");
                    else if ( (max.equals(new Float(Float.POSITIVE_INFINITY))) )
                        jsc.append("Float.POSITIVE_INFINITY");
                    else jsc.append(max.toString());
                    jsc.append("));");
                }

                //-- fixed values
                if (fixed != null) {

                   //-- make sure we've got a good value
                   Float test = new Float(fixed);

                    jsc.add("fv.setFixedValue( new Float(");
                    jsc.append(fixed);
                    jsc.append("));");
                }
                //-- pattern facet
                pattern = xsFloat.getPattern();
                if (pattern != null) {
                    jsc.add("fv.setPattern(\"");
                    jsc.append(escapePattern(pattern));
                    jsc.append("\");");
                }

                jsc.add("fieldValidator.setValidator(fv);");
                jsc.unindent();
                jsc.add("}");
                break;
            }

            //-- double
            case XSType.DOUBLE_TYPE:
            {
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("DoubleValidator dv = new DoubleValidator();");
                XSReal xsDouble = (XSReal)xsType;
                if (xsDouble.hasMinimum()) {
                    Double min = xsDouble.getMinExclusive();
                    if (min != null)
                        jsc.add("dv.setMinExclusive(");
                    else {
                        min = xsDouble.getMinInclusive();
                        jsc.add("dv.setMinInclusive(");
                    }
                    jsc.append(min.toString());
                    jsc.append(");");
                }
                if (xsDouble.hasMaximum()) {
                    Double max = xsDouble.getMaxExclusive();
                    if (max != null)
                        jsc.add("dv.setMaxExclusive(");
                    else {
                        max = xsDouble.getMaxInclusive();
                        jsc.add("dv.setMaxInclusive(");
                    }
                    jsc.append(max.toString());
                    jsc.append(");");
                }

                //-- fixed values
                if (fixed != null) {
                    //-- make sure we have a valid value...
                    Double test = new Double(fixed);

                    jsc.add("dv.setFixedValue(");
                    jsc.append(fixed);
                    jsc.append(");");
                }
                //-- pattern facet
                pattern = xsDouble.getPattern();
                if (pattern != null) {
                    jsc.add("dv.setPattern(\"");
                    jsc.append(escapePattern(pattern));
                    jsc.append("\");");
                }

                jsc.add("fieldValidator.setValidator(dv);");
                jsc.unindent();
                jsc.add("}");
                break;
            }

            case XSType.NON_NEGATIVE_INTEGER_TYPE:
            case XSType.NON_POSITIVE_INTEGER_TYPE:
            case XSType.NEGATIVE_INTEGER_TYPE:
            case XSType.POSITIVE_INTEGER_TYPE:
            case XSType.INTEGER_TYPE:
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("IntegerValidator iv = new IntegerValidator();");
                XSInteger xsInteger = (XSInteger)xsType;
                if (xsInteger.hasMinimum()) {
                    Integer min = xsInteger.getMinExclusive();
                    if (min != null)
                        jsc.add("iv.setMinExclusive(");
                    else {
                        min = xsInteger.getMinInclusive();
                        jsc.add("iv.setMinInclusive(");
                    }
                    jsc.append(min.toString());
                    jsc.append(");");
                }
                if (xsInteger.hasMaximum()) {
                    Integer max = xsInteger.getMaxExclusive();
                    if (max != null)
                        jsc.add("iv.setMaxExclusive(");
                    else {
                        max = xsInteger.getMaxInclusive();
                        jsc.add("iv.setMaxInclusive(");
                    }
                    jsc.append(max.toString());
                    jsc.append(");");
                }

                //-- fixed values
                if (fixed != null) {
                    //-- make sure we have a valid value...
                    Integer.parseInt(fixed);

                    jsc.add("iv.setFixedValue(");
                    jsc.append(fixed);
                    jsc.append(");");
                }
                //-- pattern facet
                pattern = xsInteger.getPattern();
                if (pattern != null) {
                    jsc.add("iv.setPattern(\"");
                    jsc.append(escapePattern(pattern));
                    jsc.append("\");");
                }
                //-- totalDigits
                totalDigits = xsInteger.getTotalDigits();

                if (totalDigits != -1) {
                   jsc.add("iv.setTotalDigits(");
                   jsc.append(Integer.toString(totalDigits));
                   jsc.append(");");
                }

                jsc.add("fieldValidator.setValidator(iv);");
                jsc.unindent();
                jsc.add("}");
                break;
			case XSType.INT_TYPE:
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("IntegerValidator iv = new IntegerValidator();");
                XSInt xsInt = (XSInt)xsType;
                if (xsInt.hasMinimum()) {
                    Integer min = xsInt.getMinExclusive();
                    if (min != null)
                        jsc.add("iv.setMinExclusive(");
                    else {
                        min = xsInt.getMinInclusive();
                        jsc.add("iv.setMinInclusive(");
                    }
                    jsc.append(min.toString());
                    jsc.append(");");
                }
                if (xsInt.hasMaximum()) {
                    Integer max = xsInt.getMaxExclusive();
                    if (max != null)
                        jsc.add("iv.setMaxExclusive(");
                    else {
                        max = xsInt.getMaxInclusive();
                        jsc.add("iv.setMaxInclusive(");
                    }
                    jsc.append(max.toString());
                    jsc.append(");");
                }

                //-- fixed values
                if (fixed != null) {
                    //-- make sure we have a valid value...
                    Integer.parseInt(fixed);

                    jsc.add("iv.setFixedValue(");
                    jsc.append(fixed);
                    jsc.append(");");
                }
                //-- pattern facet
                pattern = xsInt.getPattern();
                if (pattern != null) {
                    jsc.add("iv.setPattern(\"");
                    jsc.append(escapePattern(pattern));
                    jsc.append("\");");
                }

                jsc.add("fieldValidator.setValidator(iv);");
                jsc.unindent();
                jsc.add("}");
                break;
            //-- long
            case XSType.LONG_TYPE:
            {
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("LongValidator lv = new LongValidator();");
                XSLong xsLong = (XSLong)xsType;
                if (xsLong.hasMinimum()) {
                    Long min = xsLong.getMinExclusive();
                    if (min != null)
                        jsc.add("lv.setMinExclusive(");
                    else {
                        min = xsLong.getMinInclusive();
                        jsc.add("lv.setMinInclusive(");
                    }
                    jsc.append(min.toString());
                    jsc.append("L);");
                }
                if (xsLong.hasMaximum()) {
                    Long max = xsLong.getMaxExclusive();
                    if (max != null)
                        jsc.add("lv.setMaxExclusive(");
                    else {
                        max = xsLong.getMaxInclusive();
                        jsc.add("lv.setMaxInclusive(");
                    }
                    jsc.append(max.toString());
                    jsc.append("L);");
                }

                //-- fixed values
                if (fixed != null) {
                    //-- make sure we have a valid value...
                    Long.parseLong(fixed);

                    jsc.add("lv.setFixedValue(");
                    jsc.append(fixed);
                    jsc.append(");");
                }
                //-- pattern facet
                pattern = xsLong.getPattern();
                if (pattern != null) {
                    jsc.add("lv.setPattern(\"");
                    jsc.append(escapePattern(pattern));
                    jsc.append("\");");
                }

                jsc.add("fieldValidator.setValidator(lv);");
                jsc.unindent();
                jsc.add("}");
                break;
            }
            case XSType.STRING_TYPE:
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("StringValidator sv = new StringValidator();");
                XSString xsString = (XSString)xsType;
                if ( (xsString.hasMinLength()) && (!xsString.hasLength()) ){
                    jsc.add("sv.setMinLength(");
                    jsc.append(Integer.toString(xsString.getMinLength()));
                    jsc.append(");");
                }
                if ( (xsString.hasMaxLength()) && (!xsString.hasLength()) ) {
                    jsc.add("sv.setMaxLength(");
                    jsc.append(Integer.toString(xsString.getMaxLength()));
                    jsc.append(");");
                }
                if ( xsString.hasLength()) {
                    jsc.add("sv.setLength(");
                    jsc.append(Integer.toString(xsString.getLength()));
                    jsc.append(");");
                }
                if (xsString.hasWhiteSpace()) {
                    jsc.add("sv.setWhiteSpace(\"");
                    jsc.append(xsString.getWhiteSpace());
                    jsc.append("\");");
                }
                //-- fixed values
                if (fixed != null) {
                    jsc.add("sv.setFixedValue(");
                    //fixed should be "the value"
                    jsc.append(fixed);
                    jsc.append(");");
                }
                //-- pattern facet
                pattern = xsString.getPattern();
                if (pattern != null) {
                    jsc.add("sv.setPattern(\"");
                    jsc.append(escapePattern(pattern));
                    jsc.append("\");");
                }

                jsc.add("fieldValidator.setValidator(sv);");
                jsc.unindent();
                jsc.add("}");
                break;

             case XSType.NORMALIZEDSTRING_TYPE:
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("NameValidator nv = new NameValidator(NameValidator.CDATA);");
                XSNormalizedString xsNormalString = (XSNormalizedString)xsType;
                if ( (xsNormalString.hasMinLength()) && (!xsNormalString.hasLength()) ){
                    jsc.add("nv.setMinLength(");
                    jsc.append(Integer.toString(xsNormalString.getMinLength()));
                    jsc.append(");");
                }
                if ( (xsNormalString.hasMaxLength()) && (!xsNormalString.hasLength()) ) {
                    jsc.add("nv.setMaxLength(");
                    jsc.append(Integer.toString(xsNormalString.getMaxLength()));
                    jsc.append(");");
                }
                if ( xsNormalString.hasLength()) {
                    jsc.add("nv.setLength(");
                    jsc.append(Integer.toString(xsNormalString.getLength()));
                    jsc.append(");");
                }
                //-- fixed values
                if (fixed != null) {
                    jsc.add("nv.setFixedValue(\"");
                    jsc.append(fixed);
                    jsc.append("\");");
                }
                //-- pattern facet
                pattern = xsNormalString.getPattern();
                if (pattern != null) {
                    jsc.add("nv.setPattern(\"");
                    jsc.append(escapePattern(pattern));
                    jsc.append("\");");
                }

                jsc.add("fieldValidator.setValidator(nv);");
                jsc.unindent();
                jsc.add("}");
                break;


            case XSType.NCNAME_TYPE:
                jsc.add("fieldValidator.setValidator(new NameValidator());");
                break;
            case XSType.NMTOKEN_TYPE:
                jsc.add("fieldValidator.setValidator(new NameValidator(");
                jsc.append("NameValidator.NMTOKEN));");
                break;
            case XSType.COLLECTION:
                XSList xsList = (XSList)xsType;
                CollectionInfo cInfo = (CollectionInfo)member;
                FieldInfo content = cInfo.getContent();

                jsc.add("fieldValidator.setMinOccurs(");
                jsc.append(Integer.toString(xsList.getMinimumSize()));
                jsc.append(");");
                if (xsList.getMaximumSize() > 0) {
                    jsc.add("fieldValidator.setMaxOccurs(");
                    jsc.append(Integer.toString(xsList.getMaximumSize()));
                    jsc.append(");");
                }
                break;


            case XSType.DATE_TYPE:
            jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("DateTimeValidator dv = new DateTimeValidator();");
                XSDate xsDate = (XSDate)xsType;
                if (xsDate.hasMinimum()) {
                    jsc.add("try {");
                    jsc.indent();
                    Date min = xsDate.getMinExclusive();
                    if (min != null) {
                        jsc.add("org.exolab.castor.types.Date min ="
                                +"org.exolab.castor.types.Date.parseDate("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinExclusive(");
                    } else {
                        min = xsDate.getMinInclusive();
                        jsc.add("org.exolab.castor.types.Date min ="
                                +"org.exolab.castor.types.Date.parseDate("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinInclusive(");
                    }
                    jsc.append("min");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMinimum?

                if (xsDate.hasMaximum()) {
                    jsc.add("try {");
                    jsc.indent();
                    Date max = xsDate.getMaxExclusive();
                    if (max != null) {
                        jsc.add("org.exolab.castor.types.Date max ="
                                +"org.exolab.castor.types.Date.parseDate("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxExclusive(");
                    }
                    else {
                        max = xsDate.getMaxInclusive();
                        jsc.add("org.exolab.castor.types.Date max ="
                                +"org.exolab.castor.types.Date.parseDate("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxInclusive(");
                    }
                    jsc.append("max");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMaximum
                //-- pattern facet

                jsc.add("fieldValidator.setValidator(dv);");
                jsc.unindent();
                jsc.add("}");
                xsDate = null;
                break;
            //-- date
            case XSType.GDAY_TYPE:
            jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("DateTimeValidator dv = new DateTimeValidator();");
                XSGDay xsDay = (XSGDay)xsType;
                if (xsDay.hasMinimum()) {
                    jsc.add("try {");
                    jsc.indent();
                    GDay min = xsDay.getMinExclusive();
                    if (min != null) {
                        jsc.add("org.exolab.castor.types.GDay min ="
                                +"org.exolab.castor.types.GDay.parseGDay("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinExclusive(");
                    } else {
                        min = xsDay.getMinInclusive();
                        jsc.add("org.exolab.castor.types.GDay min ="
                                +"org.exolab.castor.types.GDay.parseGDay("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinInclusive(");
                    }
                    jsc.append("min");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMinimum?

                if (xsDay.hasMaximum()) {
                    jsc.add("try {");
                    jsc.indent();
                    GDay max = xsDay.getMaxExclusive();
                    if (max != null) {
                        jsc.add("org.exolab.castor.types.GDay max ="
                                +"org.exolab.castor.types.GDay.parseGDay("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxExclusive(");
                    }
                    else {
                        max = xsDay.getMaxInclusive();
                        jsc.add("org.exolab.castor.types.GDay max ="
                                +"org.exolab.castor.types.GDay.parseGDay("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxInclusive(");
                    }
                    jsc.append("max");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMaximum
                //-- pattern facet

                jsc.add("fieldValidator.setValidator(dv);");
                jsc.unindent();
                jsc.add("}");
                xsDay = null;
                break;
            //-- gDay
            case XSType.GMONTHDAY_TYPE:
            jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("DateTimeValidator dv = new DateTimeValidator();");
                XSGMonthDay xsMonthDay = (XSGMonthDay)xsType;
                if (xsMonthDay.hasMinimum()) {
                    jsc.add("try {");
                    jsc.indent();
                    GMonthDay min = xsMonthDay.getMinExclusive();
                    if (min != null) {
                        jsc.add("org.exolab.castor.types.GMonthDay min ="
                                +"org.exolab.castor.types.GMonthDay.parseGMonthDay("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinExclusive(");
                    } else {
                        min = xsMonthDay.getMinInclusive();
                        jsc.add("org.exolab.castor.types.GMonthDay min ="
                                +"org.exolab.castor.types.GMonthDay.parseGMonthDay("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinInclusive(");
                    }
                    jsc.append("min");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMinimum?

                if (xsMonthDay.hasMaximum()) {
                    jsc.add("try {");
                    jsc.indent();
                    GMonthDay max = xsMonthDay.getMaxExclusive();
                    if (max != null) {
                        jsc.add("org.exolab.castor.types.GMonthDay max ="
                                +"org.exolab.castor.types.GMonthDay.parseGMonthDay("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxExclusive(");
                    }
                    else {
                        max = xsMonthDay.getMaxInclusive();
                        jsc.add("org.exolab.castor.types.GMonthDay max ="
                                +"org.exolab.castor.types.GMonthDay.parseGMonthDay("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxInclusive(");
                    }
                    jsc.append("max");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMaximum
                //-- pattern facet

                jsc.add("fieldValidator.setValidator(dv);");
                jsc.unindent();
                jsc.add("}");
                xsMonthDay = null;
                break;
            //-- gMonthDay
            case XSType.GMONTH_TYPE:
            jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("DateTimeValidator dv = new DateTimeValidator();");
                XSGMonth xsMonth = (XSGMonth)xsType;
                if (xsMonth.hasMinimum()) {
                    jsc.add("try {");
                    jsc.indent();
                    GMonth min = xsMonth.getMinExclusive();
                    if (min != null) {
                        jsc.add("org.exolab.castor.types.GMonth min ="
                                +"org.exolab.castor.types.GMonth.parseGMonth("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinExclusive(");
                    } else {
                        min = xsMonth.getMinInclusive();
                        jsc.add("org.exolab.castor.types.GMonth min ="
                                +"org.exolab.castor.types.GMonth.parseGMonth("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinInclusive(");
                    }
                    jsc.append("min");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMinimum?

                if (xsMonth.hasMaximum()) {
                    jsc.add("try {");
                    jsc.indent();
                    GMonth max = xsMonth.getMaxExclusive();
                    if (max != null) {
                        jsc.add("org.exolab.castor.types.GMonth max ="
                                +"org.exolab.castor.types.GMonth.parseGMonth("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxExclusive(");
                    }
                    else {
                        max = xsMonth.getMaxInclusive();
                        jsc.add("org.exolab.castor.types.GMonth max ="
                                +"org.exolab.castor.types.GMonth.parseGMonth("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxInclusive(");
                    }
                    jsc.append("max");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMaximum
                //-- pattern facet

                jsc.add("fieldValidator.setValidator(dv);");
                jsc.unindent();
                jsc.add("}");
                break;
            //-- gMonth
            case XSType.GYEARMONTH_TYPE:
            jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("DateTimeValidator dv = new DateTimeValidator();");
                XSGYearMonth xsYearMonth = (XSGYearMonth)xsType;
                if (xsYearMonth.hasMinimum()) {
                    jsc.add("try {");
                    jsc.indent();
                    GYearMonth min = xsYearMonth.getMinExclusive();
                    if (min != null) {
                        jsc.add("org.exolab.castor.types.GYearMonth min ="
                                +"org.exolab.castor.types.GYearMonth.parseGYearMonth("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinExclusive(");
                    } else {
                        min = xsYearMonth.getMinInclusive();
                        jsc.add("org.exolab.castor.types.GYearMonth min ="
                                +"org.exolab.castor.types.GYearMonth.parseGYearMonth("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinInclusive(");
                    }
                    jsc.append("min");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMinimum?

                if (xsYearMonth.hasMaximum()) {
                    jsc.add("try {");
                    jsc.indent();
                    GYearMonth max = xsYearMonth.getMaxExclusive();
                    if (max != null) {
                        jsc.add("org.exolab.castor.types.GYearMonth max ="
                                +"org.exolab.castor.types.GYearMonth.parseGYearMonth("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxExclusive(");
                    }
                    else {
                        max = xsYearMonth.getMaxInclusive();
                        jsc.add("org.exolab.castor.types.GYearMonth max ="
                                +"org.exolab.castor.types.GYearMonth.parseGYearMonth("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxInclusive(");
                    }
                    jsc.append("max");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMaximum
                //-- pattern facet

                jsc.add("fieldValidator.setValidator(dv);");
                jsc.unindent();
                jsc.add("}");
                xsYearMonth =null;
                break;
            //-- gYearMonth
            case XSType.GYEAR_TYPE:
            jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("DateTimeValidator dv = new DateTimeValidator();");
                XSGYear xsYear = (XSGYear)xsType;
                if (xsYear.hasMinimum()) {
                    jsc.add("try {");
                    jsc.indent();
                    GYear min = xsYear.getMinExclusive();
                    if (min != null) {
                        jsc.add("org.exolab.castor.types.GYear min ="
                                +"org.exolab.castor.types.GYear.parseGYear("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinExclusive(");
                    } else {
                        min = xsYear.getMinInclusive();
                        jsc.add("org.exolab.castor.types.GYear min ="
                                +"org.exolab.castor.types.GYear.parseGYear("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinInclusive(");
                    }
                    jsc.append("min");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMinimum?

                if (xsYear.hasMaximum()) {
                    jsc.add("try {");
                    jsc.indent();
                    GYear max = xsYear.getMaxExclusive();
                    if (max != null) {
                        jsc.add("org.exolab.castor.types.GYear max ="
                                +"org.exolab.castor.types.GYear.parseGYear("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxExclusive(");
                    }
                    else {
                        max = xsYear.getMaxInclusive();
                        jsc.add("org.exolab.castor.types.GYear max ="
                                +"org.exolab.castor.types.GYear.parseGYear("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxInclusive(");
                    }
                    jsc.append("max");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMaximum
                //-- pattern facet

                jsc.add("fieldValidator.setValidator(dv);");
                jsc.unindent();
                jsc.add("}");
                xsYear = null;
                break;
            //-- gYear
            case XSType.TIME_TYPE:
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("DateTimeValidator dv = new DateTimeValidator();");
                XSTime xsTime = (XSTime)xsType;
                if (xsTime.hasMinimum()) {
                    jsc.add("try {");
                    jsc.indent();
                    Time min = xsTime.getMinExclusive();
                    if (min != null) {
                        jsc.add("org.exolab.castor.types.Time min ="
                                +"org.exolab.castor.types.Time.parseTime("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinExclusive(");
                    } else {
                        min = xsTime.getMinInclusive();
                        jsc.add("org.exolab.castor.types.Time min ="
                                +"org.exolab.castor.types.Time.parseTime("
                                +"\""+min.toString()+"\");");
                        jsc.add("dv.setMinInclusive(");
                    }
                    jsc.append("min");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMinimum?

                if (xsTime.hasMaximum()) {
                    jsc.add("try {");
                    jsc.indent();
                    Time max = xsTime.getMaxExclusive();
                    if (max != null) {
                        jsc.add("org.exolab.castor.types.Time max ="
                                +"org.exolab.castor.types.Time.parseTime("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxExclusive(");
                    }
                    else {
                        max = xsTime.getMaxInclusive();
                        jsc.add("org.exolab.castor.types.Time max ="
                                +"org.exolab.castor.types.Time.parseTime("
                                +"\""+max.toString()+"\");");
                        jsc.add("dv.setMaxInclusive(");
                    }
                    jsc.append("max");
                    jsc.append(");");
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException e) {");
                    jsc.indent();
                    jsc.add("System.out.println(e);");
                    jsc.add("e.printStackTrace();");
                    jsc.add("return;");
                    jsc.unindent();
                    jsc.add("}");

                }//hasMaximum
                //-- pattern facet

                jsc.add("fieldValidator.setValidator(dv);");
                jsc.unindent();
                jsc.add("}");
                xsTime = null;
                break;
            //-- time

            case XSType.DURATION_TYPE:
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("DurationValidator durationv = new DurationValidator();");
                XSDuration xsDuration = (XSDuration)xsType;
                if (xsDuration.hasMinimum()) {
                    Duration min = xsDuration.getMinExclusive();
                    if (min != null)
                        jsc.add("durationv.setMinExclusive(");
                    else {
                        min = xsDuration.getMinInclusive();
                        jsc.add("durationv.setMinInclusive(");
                    }
                    /* it is better for a good understanding to use
                    the parse method with 'min.toSring()' but in that case
                    we have to deal with the ParseException*/
                    jsc.append("new org.exolab.castor.types.Duration("+min.toLong()+"L)");
                    jsc.append(");");
                }
                if (xsDuration.hasMaximum()) {
                    Duration max = xsDuration.getMaxExclusive();
                    if (max != null)
                        jsc.add("durationv.setMaxExclusive(");
                    else {
                        max = xsDuration.getMaxInclusive();
                        jsc.add("durationv.setMaxInclusive(");
                    }
                    /* it is better for a good understanding to use
                    the parse method with 'min.toSring()' but in that case
                    we have to deal with the ParseException*/
                    jsc.append("new org.exolab.castor.types.Duration("+max.toLong()+"L)");
                    jsc.append(");");
                }

                //-- pattern facet

                jsc.add("fieldValidator.setValidator(durationv);");
                jsc.unindent();
                jsc.add("}");
                break;
            //-- Duration

            default:
                break;
        }
    } //-- validationCode

    /**
     * Escapes special characters in the given String so that it can
     * be printed correctly.
     *
     * @param str the String to escape
     * @return the escaped String, or null if the given String was null.
    **/
    private static String escapePattern(String str) {
        if (str == null) return str;

        //-- make sure we have characters to escape
        if (str.indexOf('\\') < 0) return str;

        StringBuffer sb = new StringBuffer();
        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == '\\') sb.append(ch);
            sb.append(ch);
        }
        return sb.toString();

    } //-- escape

} //-- DescriptorSourceFactory
