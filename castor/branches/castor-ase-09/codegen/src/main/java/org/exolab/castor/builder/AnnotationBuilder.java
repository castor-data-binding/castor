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
package org.exolab.castor.builder;

import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JEnum;
import org.exolab.javasource.JEnumConstant;
import org.exolab.javasource.JField;
import org.exolab.javasource.JMethod;

/**
 * This interface is a hook for (external) tools to add annotations to
 * classes, fields and enums during the XML code generation process.
 * 
 * Custom implementations of {@link AnnotationBuilder} instances can be 
 * added to a code generation execution using 
 * {@link SourceGenerator#addAnnotationBuilder(AnnotationBuilder)} 
 * 
 * @since 1.1.3
 */
public interface AnnotationBuilder {

    /**
     * add annotations to a JClass.
     * @param classInfo the classInfo
     * @param jClass the jClass
     */
    void addClassAnnotations(ClassInfo classInfo, JClass jClass);
    
    /**
     * add annotation to a property definition.
     * @param fieldInfo the fieldInfo
     * @param field the jField
     */
    void addFieldAnnotations(FieldInfo fieldInfo, JField field);
    
    /**
     * add annotations to a getter of a property.
     * @param fieldInfo the fieldInfo 
     * @param method the getter method
     */
    void addFieldGetterAnnotations(FieldInfo fieldInfo, JMethod method);
    
    /**
     * add annotations to a java5 enum.
     * @param simpleType the corresponding simpleType
     * @param jEnums the jEnum
     */
    void addEnumAnnotations(SimpleType simpleType, JEnum jEnums);
    
    /**
     * add annotations to a java5 enum constant.
     * @param facet the corresponding facet
     * @param enumConstant the jEnumConstant
     */
    void addEnumConstantAnnotations(Facet facet, JEnumConstant enumConstant);

}
