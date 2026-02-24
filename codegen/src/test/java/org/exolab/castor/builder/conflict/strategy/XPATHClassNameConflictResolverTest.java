/*
 * Copyright 2024
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
package org.exolab.castor.builder.conflict.strategy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.castor.xml.JavaNaming;
import org.exolab.castor.builder.SourceGenerator;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.javasource.JClass;
import org.junit.Test;

/**
 * Comprehensive test class for XPATHClassNameConflictResolver.
 * Tests all public methods, edge cases, null handling, and branch conditions.
 * Target: >95% coverage for class, method, line, and branch coverage.
 */
public class XPATHClassNameConflictResolverTest {

    @Test
    public void should_ChangeLocalName_When_PrefixAndLocalNameProvided() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("parent")).thenReturn("Parent");
        when(jClass.getLocalName()).thenReturn("Child");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/parent/child",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("ParentChild");
    }

    @Test
    public void should_ConcatenateMultipleTokens_When_XPathHasMultipleSegments() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("root")).thenReturn("Root");
        when(naming.toJavaClassName("level1")).thenReturn("Level1");
        when(jClass.getLocalName()).thenReturn("Leaf");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/root/level1/leaf",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("RootLevel1Leaf");
    }

    @Test
    public void should_OnlyUseLocalName_When_XPathHasSingleToken() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(jClass.getLocalName()).thenReturn("Element");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/element",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("Element");
    }

    @Test
    public void should_OnlyUseLocalName_When_XPathIsEmpty() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(jClass.getLocalName()).thenReturn("MyClass");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("MyClass");
    }

    @Test
    public void should_HandleTwoTokenXPath() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("parent")).thenReturn("Parent");
        when(jClass.getLocalName()).thenReturn("Child");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/parent/child",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("ParentChild");
    }

    @Test
    public void should_HandleThreeTokenXPath() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("a")).thenReturn("A");
        when(naming.toJavaClassName("b")).thenReturn("B");
        when(jClass.getLocalName()).thenReturn("C");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/a/b/c",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("ABC");
    }

    @Test
    public void should_HandleFourTokenXPath() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("a")).thenReturn("A");
        when(naming.toJavaClassName("b")).thenReturn("B");
        when(naming.toJavaClassName("c")).thenReturn("C");
        when(jClass.getLocalName()).thenReturn("D");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/a/b/c/d",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("ABCD");
    }

    @Test
    public void should_IgnoreFinalTokenWhenCalculatingPrefix() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("first")).thenReturn("First");
        when(naming.toJavaClassName("second")).thenReturn("Second");
        when(jClass.getLocalName()).thenReturn("Third");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/first/second/third",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("FirstSecondThird");
        verify(naming, never()).toJavaClassName("third");
    }

    @Test
    public void should_HandleXPathWithOnlySlashes() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(jClass.getLocalName()).thenReturn("Class");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "///",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("Class");
    }

    @Test
    public void should_NotUseTypedXPath() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("parent")).thenReturn("Parent");
        when(jClass.getLocalName()).thenReturn("Child");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/parent/child",
            "different",
            annotated
        );

        verify(jClass).changeLocalName("ParentChild");
    }

    @Test
    public void should_AcceptNullTypedXPath() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("parent")).thenReturn("Parent");
        when(jClass.getLocalName()).thenReturn("Child");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/parent/child",
            null,
            null
        );

        verify(jClass).changeLocalName("ParentChild");
    }

    @Test
    public void should_CallGetLocalNameOnJClass() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(jClass.getLocalName()).thenReturn("Original");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/parent/original",
            "/typed",
            annotated
        );

        verify(jClass).getLocalName();
    }

    @Test
    public void should_ChangeLocalNameExactlyOnce() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("p")).thenReturn("P");
        when(jClass.getLocalName()).thenReturn("C");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/p/c",
            "/typed",
            annotated
        );

        verify(jClass, times(1)).changeLocalName(anyString());
    }

    @Test(expected = NullPointerException.class)
    public void should_ThrowNullPointerException_When_JClassIsNull() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);

        when(sg.getJavaNaming()).thenReturn(naming);

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            null,
            "/xpath",
            "/typed",
            null
        );
    }

    @Test(expected = NullPointerException.class)
    public void should_ThrowNullPointerException_When_XPathIsNull() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(jClass.getLocalName()).thenReturn("Class");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            null,
            "/typed",
            null
        );
    }

    @Test(expected = NullPointerException.class)
    public void should_ThrowNullPointerException_When_SourceGeneratorNotSet() {
        JClass jClass = mock(JClass.class);
        when(jClass.getLocalName()).thenReturn("Class");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/xpath",
            "/typed",
            null
        );
    }

    @Test
    public void should_InheritFromBaseClassNameConflictResolver() {
        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        assertTrue(resolver instanceof BaseClassNameConflictResolver);
    }

    @Test
    public void should_ImplementClassNameConflictResolver() {
        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        assertTrue(resolver instanceof ClassNameConflictResolver);
    }

    @Test
    public void should_HaveAccessToSourceGenerator() {
        SourceGenerator sg = mock(SourceGenerator.class);

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);

        assertSame(sg, resolver.getSourceGenerator());
    }

    @Test
    public void should_UseCalculateXPathPrefixFromBase() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("a")).thenReturn("A");
        when(naming.toJavaClassName("b")).thenReturn("B");
        when(jClass.getLocalName()).thenReturn("C");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/a/b/c",
            "/typed",
            annotated
        );

        verify(naming).toJavaClassName("a");
        verify(naming).toJavaClassName("b");
    }

    @Test
    public void should_CombineMultiplePrefixes() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("root")).thenReturn("Root");
        when(naming.toJavaClassName("parent")).thenReturn("Parent");
        when(naming.toJavaClassName("sibling")).thenReturn("Sibling");
        when(jClass.getLocalName()).thenReturn("Name");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/root/parent/sibling/name",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("RootParentSiblingName");
    }

    @Test
    public void should_PreserveCaseFromJavaNaming() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("my-element")).thenReturn("MyElement");
        when(jClass.getLocalName()).thenReturn("MyClass");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/my-element/myclass",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("MyElementMyClass");
    }

    @Test
    public void should_HandleSequentialCalls() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("a")).thenReturn("A");
        when(naming.toJavaClassName("b")).thenReturn("B");
        when(jClass.getLocalName()).thenReturn("C");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/a/c",
            "/typed",
            annotated
        );
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/b/c",
            "/typed",
            annotated
        );

        verify(jClass, times(2)).changeLocalName(anyString());
    }

    @Test
    public void should_ProduceConsistentResults() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("parent")).thenReturn("Parent");
        when(jClass.getLocalName()).thenReturn("Child");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/parent/child",
            "/typed",
            annotated
        );
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/parent/child",
            "/typed",
            annotated
        );

        verify(jClass, times(2)).changeLocalName("ParentChild");
    }

    @Test
    public void should_CreateMultipleInstances() {
        XPATHClassNameConflictResolver r1 =
            new XPATHClassNameConflictResolver();
        XPATHClassNameConflictResolver r2 =
            new XPATHClassNameConflictResolver();
        XPATHClassNameConflictResolver r3 =
            new XPATHClassNameConflictResolver();

        assertNotSame(r1, r2);
        assertNotSame(r2, r3);
        assertNotSame(r1, r3);
    }

    @Test
    public void should_ReplaceSourceGenerator() {
        SourceGenerator sg1 = mock(SourceGenerator.class);
        SourceGenerator sg2 = mock(SourceGenerator.class);

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg1);
        resolver.setSourceGenerator(sg2);

        assertSame(sg2, resolver.getSourceGenerator());
    }

    @Test
    public void should_ConcatenateLongPaths() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("level1")).thenReturn("Level1");
        when(naming.toJavaClassName("level2")).thenReturn("Level2");
        when(naming.toJavaClassName("level3")).thenReturn("Level3");
        when(jClass.getLocalName()).thenReturn("Final");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/level1/level2/level3/final",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName("Level1Level2Level3Final");
    }

    @Test
    public void should_VerifyGetSourceGeneratorCalled() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("test")).thenReturn("Test");
        when(jClass.getLocalName()).thenReturn("Name");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/test/name",
            "/typed",
            annotated
        );

        verify(sg, atLeastOnce()).getJavaNaming();
    }

    @Test
    public void should_MaintainStateAfterExecution() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("a")).thenReturn("A");
        when(jClass.getLocalName()).thenReturn("B");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/a/b",
            "/typed",
            annotated
        );

        assertSame(sg, resolver.getSourceGenerator());
    }

    @Test
    public void should_HandleSpecialCharactersInXPath() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        JClass jClass = mock(JClass.class);
        Annotated annotated = mock(Annotated.class);

        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName(anyString())).thenReturn("Token");
        when(jClass.getLocalName()).thenReturn("Class");

        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        resolver.setSourceGenerator(sg);
        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            "/elem-ent:1/class",
            "/typed",
            annotated
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void should_CastToInterface() {
        XPATHClassNameConflictResolver resolver =
            new XPATHClassNameConflictResolver();
        ClassNameConflictResolver interfaceRef = resolver;

        assertNotNull(interfaceRef);
        assertTrue(interfaceRef instanceof ClassNameConflictResolver);
    }
}
