/*
 * Copyright 2008 Filip Hianik, Vanja Culafic
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
package org.exolab.castor.builder.descriptors;

import java.util.List;

import org.castor.core.constants.cpa.JDOConstants;
import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.builder.info.nature.JDOClassInfoNature;
import org.exolab.castor.builder.info.nature.JDOFieldInfoNature;
import org.exolab.castor.builder.info.nature.XMLInfoNature;
import org.exolab.castor.builder.info.nature.relation.JDOOneToManyNature;
import org.exolab.castor.builder.info.nature.relation.JDOOneToOneNature;
import org.exolab.castor.builder.types.XSType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JConstructor;
import org.exolab.javasource.JNaming;
import org.exolab.javasource.JPrimitiveType;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * A class for creating the source code of JDO-specific descriptor classes.
 *
 * @author Filip Hianik
 * @author Vanja Culafic
 * @since 1.2.1
 *
 * @see DescriptorSourceFactory
 * @see JDODescriptorJClass
 *
 */
public final class JDOClassDescriptorFactory {

   /**
    * The BuilderConfiguration instance.
    */
   private final BuilderConfiguration _config;

   /**
    * Contains all fields exclusive identities.
    */
   private String _fields = null;

   /**
    * Contains all identities.
    */
   private String _identities = null;

   /**
    * Creates a new {@link JDOClassDescriptorFactory} with the given
    * configuration.
    *
    * @param config
    *                A {@link BuilderConfiguration} instance
    */
   public JDOClassDescriptorFactory(final BuilderConfiguration config) {
       if (config == null) {
           String err = "The argument 'config' must not be null.";
           throw new IllegalArgumentException(err);
       }
       _config = config;
   }

   /**
    * Creates the Source code of a ClassInfo for a given XML Schema element
    * declaration.
    *
    * @param classInfo
    *                the XML Schema element declaration
    * @return the JClass representing the ClassInfo source code
    */
   public JClass createSource(final ClassInfo classInfo) {
       if (!checkClassInfoNature(classInfo)) {
           return null;
       }

       JClass jClass               = classInfo.getJClass();
       String descriptorClassName  = 
           getQualifiedJDODescriptorClassName(jClass.getName());
       JDODescriptorJClass classDesc = 
           new JDODescriptorJClass(_config, descriptorClassName, jClass);
       JDOClassInfoNature cNature = new JDOClassInfoNature(classInfo);

       //-- get handle to default constructor
       JConstructor ctor   = classDesc.getConstructor(0);
       JSourceCode jsc     = ctor.getSourceCode();

       jsc = createClassInfoPart(classInfo, jsc);

       //=================
       // FieldDescriptors
       //=================

       for (int i = 0; i < classInfo.getElementFields().length; i++) {
           FieldInfo fInfo = classInfo.getElementFields()[i];
           if (checkFieldInfoNatures(fInfo)) {
               
               if (fInfo.hasNature(JDOOneToOneNature.class.getName())) {
                   jsc = createOneToOneFieldInfoPart(fInfo, jsc);
                   
               } else if (fInfo.hasNature(JDOOneToManyNature.class.getName())) {
                   jsc = createOneToManyFieldInfoPart(fInfo, jsc);
               } else {
                   jsc = createEntityFieldInfoPart(fInfo, jsc);
               }
           }
       }


       _fields = setFields(classInfo.getElementFields());
       _identities = setIdentities(cNature.getPrimaryKeys());

       jsc.add("");

       jsc.add("setFields(new FieldDescriptor[] {" + _fields + "});");
       jsc.add("setIdentities(new FieldDescriptor[] {" + _identities + "});");

       return classDesc;
   }

   /**
    * Returns the fully-qualified class name of the JDODescriptor to create.
    * Given the fully-qualified class name of the class we are creating a
    * JDODescriptor for, return the correct fully-qualified name for the
    * JDODescriptor.
    *
    * @param name
    *                fully-qualified class name of the class we are describing
    * @return the fully-qualified class name of the JDODescriptor to create
    */
   private String getQualifiedJDODescriptorClassName(final String name) {
       String descPackage = JNaming.getPackageFromClassName(name);
       String descClassName = JNaming.getLocalNameFromClassName(name);

       if (descPackage != null && descPackage.length() > 0) {
           descPackage = descPackage + "."
                   + JDOConstants.JDO_DESCRIPTOR_PACKAGE + ".";
       } else {
           descPackage = "";
       }
       // TODO integrate XMLConstants.JDO_DESCRIPTOR_SUFFIX;
       return descPackage + descClassName + "JDODescriptor";
   }

   /**
    * Returns the modified string with the first letter upperCase.
    * @param string String to upper case
    * @return the string with first letter upperCase
    */
   private static String toUpperCaseFirstLetter(final String string) {
       return string.substring(0, 1).toUpperCase() + string.substring(1);
   }

   /**
    * Returns the string which contains names of all FieldDescriptors
    * for Fields that are not identities. Names are separated by comma.
    * @param fInfos Array of FieldInfos
    * @return names of all FieldDescriptors as one String (separated by comma)
    */
   private String setFields(final FieldInfo[] fInfos) {
       String str = "";

       FieldInfo fInfo;
       ClassInfo cInfo;
       for (int i = 0; i < fInfos.length; i++) {
           fInfo = fInfos[i];
           cInfo = fInfo.getDeclaringClassInfo();
           JDOClassInfoNature cNature = new JDOClassInfoNature(cInfo);
           if (cNature.getPrimaryKeys() != null) {
               if (cNature.getPrimaryKeys().contains(new XMLInfoNature(fInfo).getNodeName())) {
                   continue;
               }
           }
           if (str.equals("")) {
               str = str + new XMLInfoNature(fInfo).getNodeName() + "FieldDescr";
           } else {
               str = str + "," +  new XMLInfoNature(fInfo).getNodeName() + "FieldDescr";
           }
       }
       return str;
   }

   /**
    * Returns the string which contains names of all FieldDescriptors
    * for Fields that are identities. Names are separated by comma.
    * @param primaryKeys Array of primary keys.
    * @return names of all FieldDescriptors as one String (separated by comma)
    */
   private String setIdentities(final List primaryKeys) {
       String identities = "";
       Object[] pkArray = null;
       if (primaryKeys != null) {
           pkArray = primaryKeys.toArray();
           for (int i = 0; i < pkArray.length; i++) {
               if (identities.equals("")) {
                   identities = identities + pkArray[i] + "FieldDescr";
               } else {
                   identities = identities + "," + pkArray[i] + "FieldDescr";
               }
           }
       }
       return identities;
   }
   
   /**
    * Checks if ClassInfo has the JDOClassNature and if their required
    * attributes are properly set.
    * @param cInfo the ClassInfo object
    * @return true when ClassInfo has a JDOClassNature with all required
    *              attributes properly set, false otherwise
    */
   private boolean checkClassInfoNature(final ClassInfo cInfo) {
       if (cInfo.hasNature(JDOClassInfoNature.class.getName())) {
           JDOClassInfoNature cNature = new JDOClassInfoNature(cInfo);
           if (cNature.getAccessMode() == null
                   || cNature.getPrimaryKeys() == null
                   || cNature.getPrimaryKeys().isEmpty()
                   || cNature.getTableName() == null
                   || cNature.getTableName().length() == 0) {
               return false;
           }
           return true;
       }
       return false;
   }

   /**
    * Checks if FieldInfo has the XMLInfoNature and JDOFieldNature and if 
    * their required attributes are properly set.
    * @param fInfo the FieldInfo object
    * @return true when FieldInfo has a XMLInfoNature and JDOFieldNature 
    *              with all required attributes properly set, false otherwise
    */
   private boolean checkFieldInfoNatures(final FieldInfo fInfo) {
       if (!fInfo.hasNature(XMLInfoNature.class.getName())) {
           return false;
       }
       
       if (fInfo.hasNature(JDOFieldInfoNature.class.getName())) {
           JDOFieldInfoNature fNature = new JDOFieldInfoNature(fInfo);
           if (fNature.getColumnName() == null
                   || fNature.getColumnName().length() == 0
                   || fNature.getColumnType() == null) {
               return false;
           }
           return true;
       }
       
       if (fInfo.hasNature(JDOOneToOneNature.class.getName())) {
           JDOOneToOneNature oneNature = new JDOOneToOneNature(fInfo);
           if (oneNature.getForeignKeys().size() != 1) {
               return false;
           }
           return true;
       }
       
       if (fInfo.hasNature(JDOOneToManyNature.class.getName())) {
           JDOOneToManyNature manyNature = new JDOOneToManyNature(fInfo);
           if (manyNature.getForeignKeys().size() != 1) {
               return false;
           }
           return true;
       }       
       return false;
   }

   /**
    * Creates the ClassInfo part of the JDOClassDescriptor.
    * @param classInfo ClassInfo object
    * @param jsc JSourceCode
    * @return JSourceCode created in this method
    */
   private JSourceCode createClassInfoPart(final ClassInfo classInfo, final JSourceCode jsc) {

       JDOClassInfoNature cNature = new JDOClassInfoNature(classInfo);

       jsc.add("");

       //-- set table name
       String tableName = cNature.getTableName();
       jsc.add("setTableName(\"" + tableName + "\");");

       //-- set corresponding Java class
       // TODO IS THERE A NEED TO CHECK THIS?!
       String className = classInfo.getJClass().getLocalName();
       if ((className != null) && (className.length() > 0)) {
           jsc.add("setJavaClass(");
           jsc.append(className);
           jsc.append(".class);");
       }

       //-- set access mode
       String accessMode = cNature.getAccessMode().getName();
       jsc.add("setAccessMode(AccessMode.valueOf(\"" + accessMode + "\"));");

       //-- set cache key
       // TODO IS THERE A NEED TO CHECK THIS?!
       String fullName = classInfo.getJClass().getName();
       if ((fullName != null) && (fullName.length() > 0)) {
           jsc.add("addCacheParam(\"name\", \"");
           jsc.append(fullName);
           jsc.append("\");");
       }

       jsc.add("");

       //-- Configure class mapping
       jsc.add("mapping.setAccess(ClassMappingAccessType.valueOf(\"");
       jsc.append(accessMode + "\"));");

       jsc.add("mapping.setAutoComplete(true);");

       // TODO IS THERE A NEED TO CHECK THIS?!
       if ((fullName != null) && (fullName.length() > 0)) {
           jsc.add("mapping.setName(\"");
           jsc.append(fullName);
           jsc.append("\");");
       }

       //-- set class choice
       jsc.add("mapping.setClassChoice(choice);");

       //-- set table
       String table = cNature.getTableName();
       jsc.add("mapTo.setTable(\"" + table + "\");");

       //-- set table mapping
       jsc.add("mapping.setMapTo(mapTo);");

       //-- set mapping
       jsc.add("setMapping(mapping);");

       return jsc;
   }

   /**
    * Creates the source code of the FieldInfo part appeding it to
    * the source code generated till now.
    * @param fInfo FieldInfo object
    * @param jsc JSourceCode created till now
    * @return JSourceCode created in this method
    */
   private JSourceCode createEntityFieldInfoPart(final FieldInfo fInfo, final JSourceCode jsc) {
       JDOFieldInfoNature fNature = new JDOFieldInfoNature(fInfo);
       JDOClassInfoNature cNature = new JDOClassInfoNature(fInfo.getDeclaringClassInfo());
       XMLInfoNature xmlNature = new XMLInfoNature(fInfo);
       
       //-- set name
       String name = xmlNature.getNodeName();
       jsc.add("");
       jsc.add("//" + name + " field");

       jsc.add("String " + name + "FieldName = \"" + name + "\";");

       //-- initialize objects
       jsc.add("JDOFieldDescriptorImpl " + name + "FieldDescr;");

       jsc.add("FieldMapping " + name + "FM = new FieldMapping();");

       //-- set typeInfo
       String type = null;
       XSType schemaType;
       
       schemaType = xmlNature.getSchemaType();
       JType javaType = schemaType.getJType();
       type = javaType.toString();
       String wrapperType = null;
       if (javaType instanceof JPrimitiveType) {
           wrapperType = ((JPrimitiveType) javaType).getWrapperName();
       } else {
           wrapperType = type;
       }

       // TODO IS THERE A NEED TO CHECK THIS?!
       if ((type != null) && (type.length() > 0)) {
           jsc.add("TypeInfo " + name
                   + "Type = new TypeInfo(" + wrapperType + ".class);");
       }

       jsc.add("// Set columns required (= not null)");
       jsc.add(name + "Type.setRequired("
               + Boolean.toString(xmlNature.isRequired()) + ");");

       jsc.add("");

       jsc.add("FieldHandler " + name + "Handler;");
       jsc.add("try {");

       //-- get/set methods
       jsc.indent();
       // TODO HOW ABOUT GETTING THE NAME FROM NATURE?
       String className = fInfo.getDeclaringClassInfo().getJClass().getLocalName();
       // TODO IS THERE A NEED TO CHECK THIS?!
       if ((className != null) && (className.length() > 0)) {
           jsc.add("Method " + name + "GetMethod = "
                   + className + ".class.getMethod(\"get"
                   + toUpperCaseFirstLetter(name) + "\", null);");
           jsc.add("Method " + name + "SetMethod = "
                   + className + ".class.getMethod(\"set"
                   + toUpperCaseFirstLetter(name) + "\", new Class[]{");
       }

       // TODO IS THERE A NEED TO CHECK THIS?!
       if ((type != null) && (type.length() > 0)) {
           jsc.addIndented(type + ".class});");
       }

       jsc.add("");
       jsc.add(name + "Handler = new FieldHandlerImpl(" + name + "FieldName, ");
       jsc.append("null, null,");
       jsc.addIndented(name + "GetMethod, " + name + "SetMethod, " + name + "Type);");
       jsc.unindent();
       jsc.add("");

       //-- Catch of exceptions
       jsc.add("} catch (SecurityException e1) {");
       jsc.indent();
       jsc.add("throw new RuntimeException(e1.getMessage());");
       jsc.unindent();
       jsc.add("} catch (MappingException e1) {");
       jsc.indent();
       jsc.add("throw new RuntimeException(e1.getMessage());");
       jsc.unindent();
       jsc.add("} catch (NoSuchMethodException e1) {");
       jsc.indent();
       jsc.add("throw new RuntimeException(e1.getMessage());");
       jsc.unindent();
       jsc.add("}");

       //-- JDOFieldDescriptorImpl constructor
       jsc.add("// Instantiate " + name + " field descriptor");
       jsc.add(name + "FieldDescr = new JDOFieldDescriptorImpl(");
       jsc.append(name + "FieldName, " + name + "Type,");
       jsc.indent();
       jsc.add(name + "Handler, ");
       jsc.append(Boolean.toString(fInfo.isTransient()) + ", ");
       String sqlName = new JDOFieldInfoNature(fInfo).getColumnName();
       jsc.append("new String[] { \"" + sqlName + "\" },");
       jsc.add("new int[] {SQLTypeInfos");
       jsc.indent();
       jsc.add(".javaType2sqlTypeNum(");

       // TODO IS THERE NEED TO CHECK THIS?!
       if ((type != null) && (type.length() > 0)) {
           jsc.append(wrapperType + ".class) },");
       }

       jsc.unindent();
       jsc.add("null, new String[] {}, ");
       jsc.append(Boolean.toString(fNature.isDirty()) + ", ");
       jsc.append(Boolean.toString(fNature.isReadOnly()) + ");");
       jsc.unindent();

       jsc.add("");

       //-- parent class descriptor
       jsc.add(name + "FieldDescr.setContainingClassDescriptor(this);");
       
       boolean isPrimaryKey = false;
       if (cNature.getPrimaryKeys() != null) {
           isPrimaryKey = (cNature.getPrimaryKeys().contains(xmlNature.getNodeName()));
       }
       
       jsc.add(name + "FieldDescr.setIdentity(" 
               + Boolean.toString(isPrimaryKey) + ");");

       //-- fieldmapping
       jsc.add(name + "FM.setIdentity(" + Boolean.toString(isPrimaryKey) + ");");
       jsc.add(name + "FM.setDirect(false);");
       jsc.add(name + "FM.setName(\"" + name + "\");");
       jsc.add(name + "FM.setRequired(" + xmlNature.isRequired() + ");");
       jsc.add(name + "FM.setSetMethod(\"set"
               + toUpperCaseFirstLetter(name) + "\");");
       jsc.add(name + "FM.setGetMethod(\"get"
               + toUpperCaseFirstLetter(name) + "\");");

       //-- sql part
       jsc.add("Sql " + name + "Sql = new Sql();");
       jsc.add(name + "Sql.addName(\"" + name + "\");");

       String sqlType = fNature.getColumnType();
       if ((sqlType != null) && (sqlType.length() > 0)) {
           jsc.add(name + "Sql.setType(\"" + sqlType + "\");");
       }

       jsc.add(name + "FM.setSql(" + name + "Sql);");

       if ((type != null) && (type.length() > 0)) {
           jsc.add(name + "FM.setType(\"" + type + "\");");
       }

       jsc.add("choice.addFieldMapping(" + name + "FM);");

       return jsc;
   }

   /**
    * Creates the source code of the FieldInfo part with
    * one-to-one relation appeding it to
    * the source code generated till now.
    * @param fInfo FieldInfo object
    * @param jsc JSourceCode created till now
    * @return JSourceCode created in this method
    */
   private JSourceCode createOneToOneFieldInfoPart(final FieldInfo fInfo, final JSourceCode jsc) {
//       JDOFieldInfoNature fNature = new JDOFieldInfoNature(fInfo);
       JDOClassInfoNature cNature = new JDOClassInfoNature(fInfo.getDeclaringClassInfo());
       JDOOneToOneNature oneNature = new JDOOneToOneNature(fInfo);
       XMLInfoNature xmlNature = new XMLInfoNature(fInfo);
       
       //-- set name
       String name = xmlNature.getNodeName();
       jsc.add("");
       jsc.add("//" + name + " field");
       jsc.add("String " + name + "FieldName = \"" + name + "\";");
       
       String sqlName = oneNature.getForeignKeys().get(0).toString();
       jsc.add("String " + name + "SqlName = \"" + sqlName + "\";");
       
       //-- initialize objects
       jsc.add("JDOFieldDescriptorImpl " + name + "FieldDescr;");
       jsc.add("FieldMapping " + name + "FM = new FieldMapping();");

       //-- set typeInfo
       String type = null;
       XSType schemaType;
       
       schemaType = xmlNature.getSchemaType();
       JType javaType = schemaType.getJType();
       type = javaType.toString();
       
       String wrapperType = null;
       if (javaType instanceof JPrimitiveType) {
        wrapperType = ((JPrimitiveType) javaType).getWrapperName();
       } else {
           wrapperType = type;
       }


       // TODO IS THERE A NEED TO CHECK THIS?!
       if ((type != null) && (type.length() > 0)) {
           jsc.add("TypeInfo " + name
                   + "Type = new TypeInfo(" + type + ".class);");
       }

       jsc.add("// Set columns required (= not null)");
       jsc.add(name + "Type.setRequired("
               + Boolean.toString(xmlNature.isRequired()) + ");");

       jsc.add("");

       jsc.add("FieldHandler " + name + "Handler;");
       jsc.add("try {");

       //-- get/set methods
       jsc.indent();
       // TODO HOW ABOUT GETTING THE NAME FROM NATURE?
       String className = fInfo.getDeclaringClassInfo().getJClass().getLocalName();
       // TODO IS THERE A NEED TO CHECK THIS?!
       if ((className != null) && (className.length() > 0)) {
           jsc.add("Method " + name + "GetMethod = "
                   + className + ".class.getMethod(\"get"
                   + toUpperCaseFirstLetter(name) + "\", null);");
           jsc.add("Method " + name + "SetMethod = "
                   + className + ".class.getMethod(\"set"
                   + toUpperCaseFirstLetter(name) + "\", new Class[]{");
       }

       // TODO IS THERE A NEED TO CHECK THIS?!
       if ((type != null) && (type.length() > 0)) {
           jsc.addIndented(type + ".class});");
       }

       jsc.add("");
       jsc.add(name + "Handler = new FieldHandlerImpl(" + name + "FieldName, ");
       jsc.append("null, null,");
       jsc.addIndented(name + "GetMethod, " + name + "SetMethod, " + name + "Type);");
       jsc.unindent();
       jsc.add("");

       //-- Catch of exceptions
       jsc.add("} catch (SecurityException e1) {");
       jsc.indent();
       jsc.add("throw new RuntimeException(e1.getMessage());");
       jsc.unindent();
       jsc.add("} catch (MappingException e1) {");
       jsc.indent();
       jsc.add("throw new RuntimeException(e1.getMessage());");
       jsc.unindent();
       jsc.add("} catch (NoSuchMethodException e1) {");
       jsc.indent();
       jsc.add("throw new RuntimeException(e1.getMessage());");
       jsc.unindent();
       jsc.add("}");


       //-- JDOFieldDescriptorImpl constructor
       jsc.add("// Instantiate " + name + " field descriptor");
       jsc.add(name + "FieldDescr = new JDOFieldDescriptorImpl(");
       jsc.append(name + "FieldName, " + name + "Type,");
       jsc.indent();
       jsc.add(name + "Handler, ");
       jsc.append(Boolean.toString(fInfo.isTransient()) + ", ");
       jsc.append("new String[] { " + name + "SqlName },");
       jsc.add("new int[] {SQLTypeInfos");
       jsc.indent();
       jsc.add(".javaType2sqlTypeNum(");

       // TODO IS THERE NEED TO CHECK THIS?!
       if ((type != null) && (type.length() > 0)) {
           jsc.append(wrapperType + ".class) },");
       }

       jsc.unindent();
       jsc.add("null, new String[] { " + name + "SqlName }, ");
       jsc.append(Boolean.toString(oneNature.isDirty()) + ", ");
       jsc.append(Boolean.toString(oneNature.isReadOnly()) + ");");
       jsc.unindent();

       jsc.add("");

       //-- parent class descriptor
       jsc.add(name + "FieldDescr.setContainingClassDescriptor(this);");
       jsc.add(name + "FieldDescr.setClassDescriptor(new " + getLocalName(type) 
               + "JDODescriptor());");
       
       boolean isPrimaryKey = false;
       if (cNature.getPrimaryKeys() != null) {
           isPrimaryKey = (cNature.getPrimaryKeys().contains(xmlNature.getNodeName()));
       }

       //-- fieldmapping
       jsc.add(name + "FM.setIdentity(" + Boolean.toString(isPrimaryKey) + ");");
       jsc.add(name + "FM.setDirect(false);");
       jsc.add(name + "FM.setName(\"" + name + "\");");
       jsc.add(name + "FM.setRequired(" + xmlNature.isRequired() + ");");
       jsc.add(name + "FM.setSetMethod(\"set"
               + toUpperCaseFirstLetter(name) + "\");");
       jsc.add(name + "FM.setGetMethod(\"get"
               + toUpperCaseFirstLetter(name) + "\");");

       //-- sql part
       jsc.add("Sql " + name + "Sql = new Sql();");
       jsc.add(name + "Sql.addName(\"" + sqlName + "\");");

//       String sqlType = fNature.getColumnType();
//       if ((sqlType != null) && (sqlType.length() > 0)) {
//           jsc.add(name + "Sql.setType(\"" + sqlType + "\");");
//       }
       jsc.add(name + "Sql.setType(\"integer\");");
       
       jsc.add(name + "Sql.setManyKey(new String[] {\"" + sqlName + "\"});");
       jsc.add(name + "FM.setSql(" + name + "Sql);");

       if ((type != null) && (type.length() > 0)) {
           jsc.add(name + "FM.setType(\"" + type + "\");");
       }

       jsc.add("choice.addFieldMapping(" + name + "FM);");
       return jsc;
   }
   
   /**
    * Creates the source code of the FieldInfo part with
    * one-to-one relation appeding it to
    * the source code generated till now.
    * @param fInfo FieldInfo object
    * @param jsc JSourceCode created till now
    * @return JSourceCode created in this method
    */
   private JSourceCode createOneToManyFieldInfoPart(final FieldInfo fInfo, final JSourceCode jsc) {
       
       //JDOFieldInfoNature fNature = new JDOFieldInfoNature(fInfo);
       JDOClassInfoNature cNature = new JDOClassInfoNature(fInfo.getDeclaringClassInfo());
       JDOOneToManyNature manyNature = new JDOOneToManyNature(fInfo);
       XMLInfoNature xmlNature = new XMLInfoNature(fInfo);
       
       //-- set name
       String name = xmlNature.getNodeName();
       jsc.add("");
       jsc.add("//" + name + " field");
       jsc.add("String " + name + "FieldName = \"" + name + "\";");
       
       String sqlName = manyNature.getForeignKeys().get(0).toString();
       jsc.add("String " + name + "SqlName = \"" + sqlName + "\";");
       
       //-- initialize objects
       jsc.add("JDOFieldDescriptorImpl " + name + "FieldDescr;");
       jsc.add("FieldMapping " + name + "FM = new FieldMapping();");

       //-- set typeInfo
       String type = null;
       XSType schemaType;
       
       schemaType = xmlNature.getSchemaType();
       JType javaType = schemaType.getJType();
       type = javaType.toString();
       
       String wrapperType = null;
       if (javaType instanceof JPrimitiveType) {
        wrapperType = ((JPrimitiveType) javaType).getWrapperName();
       } else {
           wrapperType = type;
       }
       

       // TODO IS THERE A NEED TO CHECK THIS?!
       if ((type != null) && (type.length() > 0)) {
           jsc.add("TypeInfo " + name
                   + "Type = new TypeInfo(" + type + ".class);");
       }

       jsc.add("// Set columns required (= not null)");
       jsc.add(name + "Type.setRequired("
               + Boolean.toString(xmlNature.isRequired()) + ");");

       jsc.add("");

       jsc.add("FieldHandler " + name + "Handler;");
       jsc.add("try {");

       //-- get/set methods
       jsc.indent();
       // TODO HOW ABOUT GETTING THE NAME FROM NATURE?
       String className = fInfo.getDeclaringClassInfo().getJClass().getLocalName();
       // TODO IS THERE A NEED TO CHECK THIS?!
       if ((className != null) && (className.length() > 0)) {
           jsc.add("Method " + name + "GetMethod = "
                   + className + ".class.getMethod(\"get"
                   + toUpperCaseFirstLetter(name) + "\", null);");
           jsc.add("Method " + name + "SetMethod = "
                   + className + ".class.getMethod(\"set"
                   + toUpperCaseFirstLetter(name) + "\", new Class[]{");       
       }
       
       // TODO IS THERE A NEED TO CHECK THIS?!
       if ((type != null) && (type.length() > 0)) {
           jsc.addIndented(type + "[].class});");
       }

       jsc.add("");
       jsc.add(name + "Handler = new FieldHandlerImpl(" + name + "FieldName, ");
       jsc.append("null, null,");
       jsc.addIndented(name + "GetMethod, " + name + "SetMethod, " + name + "Type);");
       jsc.unindent();
       jsc.add("");

       //-- Catch of exceptions
       jsc.add("} catch (SecurityException e1) {");
       jsc.indent();
       jsc.add("throw new RuntimeException(e1.getMessage());");
       jsc.unindent();
       jsc.add("} catch (MappingException e1) {");
       jsc.indent();
       jsc.add("throw new RuntimeException(e1.getMessage());");
       jsc.unindent();
       jsc.add("} catch (NoSuchMethodException e1) {");
       jsc.indent();
       jsc.add("throw new RuntimeException(e1.getMessage());");
       jsc.unindent();
       jsc.add("}");


       //-- JDOFieldDescriptorImpl constructor
       jsc.add("// Instantiate " + name + " field descriptor");
       jsc.add(name + "FieldDescr = new JDOFieldDescriptorImpl(");
       jsc.append(name + "FieldName, " + name + "Type,");
       jsc.indent();
       jsc.add(name + "Handler, ");
       jsc.append(Boolean.toString(fInfo.isTransient()) + ", ");
       jsc.append("new String[] { },");
       jsc.add("new int[] {SQLTypeInfos");
       jsc.indent();
       jsc.add(".javaType2sqlTypeNum(");

       // TODO IS THERE NEED TO CHECK THIS?!
       if ((type != null) && (type.length() > 0)) {
           jsc.append(wrapperType + ".class) },");
       }

       jsc.unindent();
       jsc.add("null, new String[] { " + name + "SqlName }, ");
       jsc.append(Boolean.toString(manyNature.isDirty()) + ", ");
       jsc.append(Boolean.toString(manyNature.isReadOnly()) + ");");
       jsc.unindent();

       jsc.add("");

       //-- parent class descriptor
       jsc.add(name + "FieldDescr.setContainingClassDescriptor(this);");
       jsc.add(name + "FieldDescr.setClassDescriptor(new " + getLocalName(type) 
               + "JDODescriptor());");
       jsc.add(name + "FieldDescr.setMultivalued(true);");
       
       boolean isPrimaryKey = false;
       if (cNature.getPrimaryKeys() != null) {
           isPrimaryKey = (cNature.getPrimaryKeys().contains(xmlNature.getNodeName()));
       }

       //-- fieldmapping
       jsc.add(name + "FM.setIdentity(" + Boolean.toString(isPrimaryKey) + ");");
       jsc.add(name + "FM.setDirect(false);");
       jsc.add(name + "FM.setName(\"" + name + "\");");
       jsc.add(name + "FM.setRequired(" + xmlNature.isRequired() + ");");
       // TODO support of other collection types
       jsc.add(name + "FM.setCollection(FieldMappingCollectionType.ARRAY);");
       
       //-- sql part
       jsc.add("Sql " + name + "Sql = new Sql();");
       jsc.add(name + "Sql.addName(\"" + sqlName + "\");");

//       String sqlType = fNature.getColumnType();
//       if ((sqlType != null) && (sqlType.length() > 0)) {
//           jsc.add(name + "Sql.setType(\"" + sqlType + "\");");
//       }
       jsc.add(name + "Sql.setType(\"integer\");");
       
       jsc.add(name + "Sql.setManyKey(new String[] {\"" + sqlName + "\"});");
       jsc.add(name + "FM.setSql(" + name + "Sql);");

       if ((type != null) && (type.length() > 0)) {
           jsc.add(name + "FM.setType(\"" + type + "\");");
       }

       jsc.add("choice.addFieldMapping(" + name + "FM);");
       
       return jsc;
   }
   
   /**
    * Returns the unqualified Java type name (i.e. without package).
    *
    * @param name the qualified Java type name.
    * @return The unqualified Java type name.
    */
   private String getLocalName(final String name) {
       // -- use getName method in case it's been overloaded
       return JNaming.getLocalNameFromClassName(name);
   }
}