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

import org.castor.core.constants.cpa.JDOConstants;
import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.nature.JDOClassInfoNature;
import org.exolab.castor.xml.XMLConstants;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JConstructor;
import org.exolab.javasource.JNaming;
import org.exolab.javasource.JSourceCode;

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
        JClass jClass = classInfo.getJClass();
        String descriptorClassName = getQualifiedJDODescriptorClassName(jClass
                .getName());
        JDODescriptorJClass classDesc = new JDODescriptorJClass(_config,
                descriptorClassName, jClass);
        JDOClassInfoNature cNature = new JDOClassInfoNature(classInfo);

        // -- get handle to default constructor
        JConstructor ctor = classDesc.getConstructor(0);
        JSourceCode jsc = ctor.getSourceCode();

        jsc.add("");

        // -- set table name
        String tableName = cNature.getTableName();
        if ((tableName != null) && (tableName.length() > 0)) {
            jsc.add("setTableName(\"");
            jsc.append(tableName);
            jsc.append("\");");
        }

        // -- set corresponding Java class
        // TODO OR BETTER FROM THE localClassName VARIABLE?
        String className = classInfo.getJClass().getLocalName();
        if ((className != null) && (className.length() > 0)) {
            jsc.add("setJavaClass(");
            jsc.append(className);
            jsc.append(".class);");
        }

        // -- set access mode
        String accessMode = cNature.getAccessMode().getName();
        if ((accessMode != null) && (accessMode.length() > 0)) {
            jsc.add("setAccessMode(AccessMode.valueOf(\"");
            jsc.append(accessMode);
            jsc.append("\"));");
        }

        // -- set cache key
        String fullName = classInfo.getJClass().getName();
        if ((fullName != null) && (fullName.length() > 0)) {
            jsc.add("addCacheParam(\"name\",\"");
            jsc.append(fullName);
            jsc.append("\");");
        }

        jsc.add("");

        // -- Configure class mapping
        String cmat = cNature.getAccessMode().getName();
        if ((cmat != null) && (cmat.length() > 0)) {
            jsc.add("mapping.setAccess(ClassMappingAccessType.valueOf(\"");
            jsc.append(cmat);
            jsc.append("\"));");
        }

        /*
         * String autoComplete = Boolean.toString(classInfo.isAutoComplete());
         * if ((autoComplete != null) && (autoComplete.length() > 0)) {
         * jsc.add("mapping.setAutoComplete("); jsc.append(autoComplete);
         * jsc.append(");"); }
         */
        // TODO Why should that be set to TRUE ? 
        jsc.add("mapping.setAutoComplete(true);");

        // -- set name
        if ((fullName != null) && (fullName.length() > 0)) {
            jsc.add("mapping.setName(\"");
            jsc.append(fullName);
            jsc.append("\");");
        }

        // -- set class choice
        jsc.add("mapping.setClassChoice(choice);");

        // -- set table
        String table = cNature.getTableName();
        if ((table != null) && (table.length() > 0)) {
            jsc.add("mapTo.setTable(\"");
            jsc.append(table);
            jsc.append("\");");
        }

        // -- set table mapping
        jsc.add("mapping.setMapTo(mapTo);");

        // -- set mapping
        jsc.add("setMapping(mapping);");

        // _fields = setFields(classInfo.getElementFields());
        // //_identities = setIdentities(classInfo.getElementFields());
        // _identities = setIdentities(cNature.getPrimaryKeys());
        //        
        // jsc.add("");
        //        
        // jsc.add("setFields(new FieldDescriptor[] {" + _fields + "});");
        // jsc.add("setIdentities(new FieldDescriptor[] {" + _identities +
        // "});");

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

}