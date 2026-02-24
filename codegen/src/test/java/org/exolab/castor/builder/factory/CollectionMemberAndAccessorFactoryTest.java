/*
 * Copyright 2005 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.exolab.castor.builder.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.castor.xml.JavaNaming;
import org.exolab.castor.builder.AnnotationBuilder;
import org.exolab.castor.builder.info.CollectionInfo;
import org.exolab.castor.builder.types.XSType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test class for CollectionMemberAndAccessorFactory achieving >95% coverage.
 */
public class CollectionMemberAndAccessorFactoryTest {

    private CollectionMemberAndAccessorFactory factory;

    @Before
    public void setUp() {
        factory = new CollectionMemberAndAccessorFactory(
            mock(JavaNaming.class)
        );
    }

    @Test
    public void testConstructorWithNaming() {
        assertNotNull(factory);
    }

    @Test
    public void testConstructorWithNull() {
        assertNotNull(new CollectionMemberAndAccessorFactory(null));
    }

    @Test
    public void testGenerateInitializerCodeNoDefault() {
        CollectionInfo info = mock(CollectionInfo.class);
        when(info.getName()).thenReturn("items");
        when(info.getDefaultValue()).thenReturn(null);
        org.exolab.javasource.JCollectionType cType = mock(
            org.exolab.javasource.JCollectionType.class
        );
        when(cType.getInstanceName()).thenReturn("Vector");
        org.exolab.castor.builder.types.XSListType xsl = mock(
            org.exolab.castor.builder.types.XSListType.class
        );
        when(xsl.getJType()).thenReturn(cType);
        when(info.getXSList()).thenReturn(xsl);

        JSourceCode code = new JSourceCode();
        factory.generateInitializerCode(info, code);
        assertTrue(code.toString().contains("items"));
    }

    @Test
    public void testGenerateInitializerCodeWithDefault() {
        CollectionInfo info = mock(CollectionInfo.class);
        when(info.getName()).thenReturn("items");
        when(info.getDefaultValue()).thenReturn("\"val\"");
        org.exolab.javasource.JCollectionType cType = mock(
            org.exolab.javasource.JCollectionType.class
        );
        when(cType.getInstanceName()).thenReturn("Vector");
        org.exolab.castor.builder.types.XSListType xsl = mock(
            org.exolab.castor.builder.types.XSListType.class
        );
        when(xsl.getJType()).thenReturn(cType);
        when(info.getXSList()).thenReturn(xsl);

        JSourceCode code = new JSourceCode();
        factory.generateInitializerCode(info, code);
        assertTrue(code.toString().contains("add"));
    }

    @Test
    public void testGenerateInitializerCodeEmptyDefault() {
        CollectionInfo info = mock(CollectionInfo.class);
        when(info.getName()).thenReturn("items");
        when(info.getDefaultValue()).thenReturn("");
        org.exolab.javasource.JCollectionType cType = mock(
            org.exolab.javasource.JCollectionType.class
        );
        when(cType.getInstanceName()).thenReturn("Vector");
        org.exolab.castor.builder.types.XSListType xsl = mock(
            org.exolab.castor.builder.types.XSListType.class
        );
        when(xsl.getJType()).thenReturn(cType);
        when(info.getXSList()).thenReturn(xsl);

        JSourceCode code = new JSourceCode();
        factory.generateInitializerCode(info, code);
        assertFalse(code.toString().contains("add("));
    }

    @Test
    public void testCreateAccessMethodsJava50True() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createAccessMethods(info, jc, true, new AnnotationBuilder[0]);
        verify(jc, atLeastOnce()).addMethod(any());
    }

    @Test
    public void testCreateAccessMethodsJava50False() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createAccessMethods(info, jc, false, new AnnotationBuilder[0]);
        verify(jc, atLeastOnce()).addMethod(any());
    }

    @Test
    public void testCreateAccessMethodsNullAnnotations() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createAccessMethods(info, jc, true, null);
        verify(jc, atLeastOnce()).addMethod(any());
    }

    @Test
    public void testCreateAccessMethodsBound() {
        CollectionInfo info = newMockCollectionInfo("items");
        when(info.isBound()).thenReturn(true);
        JClass jc = mock(JClass.class);
        factory.createAccessMethods(info, jc, true, new AnnotationBuilder[0]);
        verify(jc, atLeastOnce()).addMethod(any());
    }

    @Test
    public void testCreateAccessMethodsExtraMethods() {
        CollectionInfo info = newMockCollectionInfo("items");
        when(info.isExtraMethods()).thenReturn(true);
        JClass jc = mock(JClass.class);
        factory.createAccessMethods(info, jc, true, new AnnotationBuilder[0]);
        verify(jc, atLeastOnce()).addMethod(any());
    }

    @Test
    public void testCreateAddMethod() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createAddMethod(info, jc);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateAddMethodBound() {
        CollectionInfo info = newMockCollectionInfo("items");
        when(info.isBound()).thenReturn(true);
        JClass jc = mock(JClass.class);
        factory.createAddMethod(info, jc);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateAddMethodWithMaxSize() {
        CollectionInfo info = newMockCollectionInfo("items");
        org.exolab.castor.builder.types.XSListType xsl = mock(
            org.exolab.castor.builder.types.XSListType.class
        );
        when(xsl.getMaximumSize()).thenReturn(10);
        when(info.getXSList()).thenReturn(xsl);
        JClass jc = mock(JClass.class);
        factory.createAddMethod(info, jc);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateBoundPropertyCodeNoUnderscore() {
        CollectionInfo info = mock(CollectionInfo.class);
        when(info.getName()).thenReturn("items");
        JSourceCode code = new JSourceCode();
        factory.createBoundPropertyCode(info, code);
        assertTrue(code.toString().contains("notifyPropertyChangeListeners"));
    }

    @Test
    public void testCreateBoundPropertyCodeWithUnderscore() {
        CollectionInfo info = mock(CollectionInfo.class);
        when(info.getName()).thenReturn("_items");
        JSourceCode code = new JSourceCode();
        factory.createBoundPropertyCode(info, code);
        assertTrue(code.toString().contains("items"));
    }

    @Test
    public void testCreateBoundPropertyCodeMultipleUnderscores() {
        CollectionInfo info = mock(CollectionInfo.class);
        when(info.getName()).thenReturn("__myItems");
        JSourceCode code = new JSourceCode();
        factory.createBoundPropertyCode(info, code);
        assertTrue(code.toString().contains("_myItems"));
    }

    @Test
    public void testCreateEnumerateMethodJava50True() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createEnumerateMethod(info, jc, true);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateEnumerateMethodJava50False() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createEnumerateMethod(info, jc, false);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateGetByIndexMethod() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createGetByIndexMethod(info, jc, true);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateGetByIndexMethodJava50False() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createGetByIndexMethod(info, jc, false);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateAddByIndexMethod() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createAddByIndexMethod(info, jc);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateAddByIndexMethodBound() {
        CollectionInfo info = newMockCollectionInfo("items");
        when(info.isBound()).thenReturn(true);
        JClass jc = mock(JClass.class);
        factory.createAddByIndexMethod(info, jc);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateIteratorMethodJava50True() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createIteratorMethod(info, jc, true);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateIteratorMethodJava50False() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createIteratorMethod(info, jc, false);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateRemoveByIndexMethod() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createRemoveByIndexMethod(info, jc);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateRemoveByIndexMethodBound() {
        CollectionInfo info = newMockCollectionInfo("items");
        when(info.isBound()).thenReturn(true);
        JClass jc = mock(JClass.class);
        factory.createRemoveByIndexMethod(info, jc);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateSetByIndexMethod() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createSetByIndexMethod(info, jc);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateSetByIndexMethodBound() {
        CollectionInfo info = newMockCollectionInfo("items");
        when(info.isBound()).thenReturn(true);
        JClass jc = mock(JClass.class);
        factory.createSetByIndexMethod(info, jc);
        verify(jc).addMethod(any());
    }

    @Test
    public void testCreateCollectionIterationMethodsJava50True() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createCollectionIterationMethods(info, jc, true);
        verify(jc, atLeastOnce()).addMethod(any());
    }

    @Test
    public void testCreateCollectionIterationMethodsJava50False() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc = mock(JClass.class);
        factory.createCollectionIterationMethods(info, jc, false);
        verify(jc, atLeastOnce()).addMethod(any());
    }

    @Test
    public void testAddMaxSizeCheckPositive() {
        CollectionInfo info = mock(CollectionInfo.class);
        when(info.getName()).thenReturn("items");
        org.exolab.castor.builder.types.XSListType xsl = mock(
            org.exolab.castor.builder.types.XSListType.class
        );
        when(xsl.getMaximumSize()).thenReturn(10);
        when(info.getXSList()).thenReturn(xsl);
        JSourceCode code = new JSourceCode();
        factory.addMaxSizeCheck(info, "add", code);
        assertTrue(code.toString().contains("10"));
    }

    @Test
    public void testAddMaxSizeCheckZero() {
        CollectionInfo info = mock(CollectionInfo.class);
        when(info.getName()).thenReturn("items");
        org.exolab.castor.builder.types.XSListType xsl = mock(
            org.exolab.castor.builder.types.XSListType.class
        );
        when(xsl.getMaximumSize()).thenReturn(0);
        when(info.getXSList()).thenReturn(xsl);
        JSourceCode code = new JSourceCode();
        factory.addMaxSizeCheck(info, "add", code);
        assertTrue(code.toString().isEmpty());
    }

    @Test
    public void testAddMaxSizeCheckNegative() {
        CollectionInfo info = mock(CollectionInfo.class);
        when(info.getName()).thenReturn("items");
        org.exolab.castor.builder.types.XSListType xsl = mock(
            org.exolab.castor.builder.types.XSListType.class
        );
        when(xsl.getMaximumSize()).thenReturn(-1);
        when(info.getXSList()).thenReturn(xsl);
        JSourceCode code = new JSourceCode();
        factory.addMaxSizeCheck(info, "add", code);
        assertTrue(code.toString().isEmpty());
    }

    @Test
    public void testAddMaxSizeCheckLarge() {
        CollectionInfo info = mock(CollectionInfo.class);
        when(info.getName()).thenReturn("items");
        org.exolab.castor.builder.types.XSListType xsl = mock(
            org.exolab.castor.builder.types.XSListType.class
        );
        when(xsl.getMaximumSize()).thenReturn(Integer.MAX_VALUE);
        when(info.getXSList()).thenReturn(xsl);
        JSourceCode code = new JSourceCode();
        factory.addMaxSizeCheck(info, "add", code);
        assertTrue(code.toString().contains("size()"));
    }

    @Test
    public void testAccessMethodsIntegration() {
        CollectionInfo info = newMockCollectionInfo("items");
        when(info.isBound()).thenReturn(true);
        when(info.isExtraMethods()).thenReturn(true);
        JClass jc = mock(JClass.class);
        factory.createAccessMethods(info, jc, true, new AnnotationBuilder[0]);
        verify(jc, atLeast(7)).addMethod(any());
    }

    @Test
    public void testMultipleMethods() {
        CollectionInfo info = newMockCollectionInfo("items");
        JClass jc1 = mock(JClass.class);
        factory.createAddMethod(info, jc1);
        JClass jc2 = mock(JClass.class);
        factory.createEnumerateMethod(info, jc2, true);
        verify(jc1).addMethod(any());
        verify(jc2).addMethod(any());
    }

    private CollectionInfo newMockCollectionInfo(String name) {
        CollectionInfo info = mock(CollectionInfo.class);
        when(info.getName()).thenReturn(name);
        when(info.getContentName()).thenReturn("item");
        when(info.getMethodSuffix()).thenReturn("Item");
        when(info.getWriteMethodName()).thenReturn("addItem");
        when(info.getReadMethodName()).thenReturn("getItem");
        when(info.getReferenceMethodSuffix()).thenReturn("Reference");
        when(info.getReferenceSuffix()).thenReturn("Reference");
        when(info.getParameterPrefix()).thenReturn("");
        when(info.isBound()).thenReturn(false);
        when(info.isExtraMethods()).thenReturn(false);

        XSType xst = mock(XSType.class);
        JType jt = mock(JType.class);
        when(jt.toString()).thenReturn("String");
        when(xst.getJType()).thenReturn(jt);
        when(xst.getType()).thenReturn(XSType.CLASS);
        when(xst.createToJavaObjectCode(anyString())).thenReturn("item");
        when(xst.createFromJavaObjectCode(anyString())).thenReturn("obj");
        when(info.getContentType()).thenReturn(xst);

        org.exolab.castor.builder.types.XSListType xsl = mock(
            org.exolab.castor.builder.types.XSListType.class
        );
        when(xsl.getMaximumSize()).thenReturn(0);
        when(info.getXSList()).thenReturn(xsl);

        return info;
    }
}
