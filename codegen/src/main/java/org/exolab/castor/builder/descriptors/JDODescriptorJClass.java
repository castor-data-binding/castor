/*
 * Copyright 2008 Filip Hianik
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

import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JConstructor;
import org.exolab.javasource.JSourceCode;

/**
 * A class which defines the necessary methods for creating the JDO-specific
 * descriptor source files.
 * 
 * @see DescriptorJClass
 * @see DescriptorSourceFactory
 * 
 * @author Filip Hianik
 * @since 1.2.1
 * 
 */
public final class JDODescriptorJClass extends JClass {

    /** 
     * JDODescriptors extend this base class.
     */
    private static final String JDO_CLASS_DESCRIPTOR = 
        "org.exolab.castor.jdo.engine.JDOClassDescriptorImpl";

    /** 
     * The type being described by the Descriptor class we'll generate.
     */
    private final JClass _type;
    
    /** 
     * Source Builder configuration.
     */
    private final BuilderConfiguration _config;

    /**
     * Constructs a JDODescriptorJClass.
     * 
     * @param config
     *                Builder Configuration
     * @param className
     *                name of this descriptor class
     * @param type
     *                the type that is described by this descriptor
     */
    public JDODescriptorJClass(final BuilderConfiguration config,
            final String className, final JClass type) {
        super(className);
        this._config = config;
        this._type = type;
        init();
    }

    /**
     * Initializes this JDODescriptorJClass with the required methods.
     */
    private void init() {
        // Make sure that the Descriptor is extended JDOClassDescriptor even
        // when
        // the user has specified a super class for all the generated classes
        String superClass = null;
        if (_config != null) {
            superClass = _config.getProperty(
                    BuilderConfiguration.Property.SUPER_CLASS, null);
        }

        // boolean extended = false;

        if (_type.getSuperClassQualifiedName() == null
                || _type.getSuperClassQualifiedName().equals(superClass)) {
            setSuperClass(JDO_CLASS_DESCRIPTOR);
        } else {
            if (_type.getSuperClass() == null) {
                setSuperClass(null);
            } else {
                // extended = true;
                // setSuperClass(getSuperClassName());
            }
        }
        superClass = null;

        if (_type.getPackageName() != null
                && _type.getPackageName().length() > 0) {
            addImport(_type.getName());
        }

        // -- add default imports
        addImports();
        // -- add default contructor
        addDefaultConstructor();
    }

    /**
     * Adds our default imports.
     */
    private void addImports() {
        addImport("org.castor.jdo.engine.SQLTypeInfos");
        addImport("org.exolab.castor.jdo.engine.JDOFieldDescriptor");
        addImport("org.exolab.castor.jdo.engine.JDOFieldDescriptorImpl");
        addImport("org.exolab.castor.mapping.AccessMode");
        addImport("org.exolab.castor.mapping.FieldDescriptor");
        addImport("org.exolab.castor.mapping.MappingException");
        addImport("org.exolab.castor.mapping.loader.FieldHandlerImpl");
        addImport("org.exolab.castor.mapping.loader.TypeInfo");
        addImport("org.exolab.castor.mapping.xml.ClassChoice");
        addImport("org.exolab.castor.mapping.xml.ClassMapping");
        addImport("org.exolab.castor.mapping.xml.FieldMapping");
        addImport("org.exolab.castor.mapping.xml.MapTo");
        addImport("org.exolab.castor.mapping.xml.Sql");
        addImport("org.exolab.castor.mapping.xml.types.ClassMappingAccessType");
        addImport("org.castor.core.exception.IllegalClassDescriptorInitialization");
        addImport("java.lang.reflect.Method");
    }

    /**
     * Adds our default constructor.
     */
    private void addDefaultConstructor() {
        addConstructor(createConstructor());
        JConstructor cons = getConstructor(0);
        JSourceCode jsc = cons.getSourceCode();
        jsc.add("super();");
        jsc.add("ClassMapping mapping = new ClassMapping();");
        jsc.add("ClassChoice choice = new ClassChoice();");
        jsc.add("MapTo mapTo = new MapTo();");
    }
}