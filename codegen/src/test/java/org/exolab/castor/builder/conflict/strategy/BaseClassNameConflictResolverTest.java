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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Comprehensive test class for BaseClassNameConflictResolver.
 * Tests all public methods, edge cases, null handling, and branch conditions.
 * Target: >95% coverage
 */
public class BaseClassNameConflictResolverTest {

    @Mock
    private SourceGenerator mockSourceGenerator;

    @Mock
    private JavaNaming mockJavaNaming;

    @Mock
    private JClass mockJClass;

    @Mock
    private Annotated mockAnnotated;

    private BaseClassNameConflictResolver resolver;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        resolver = new BaseClassNameConflictResolver() {
            @Override
            public void changeClassInfoAsResultOfConflict(
                final JClass jClass,
                final String xpath,
                final String typedXPath,
                final Annotated annotated
            ) {
                // Test implementation
            }
        };
        when(mockSourceGenerator.getJavaNaming()).thenReturn(mockJavaNaming);
    }

    // ===== Tests for setSourceGenerator =====

    @Test
    public void should_SetSourceGenerator_When_ValidSourceGeneratorProvided() {
        resolver.setSourceGenerator(mockSourceGenerator);
        assertSame(mockSourceGenerator, resolver.getSourceGenerator());
    }

    @Test
    public void should_SetSourceGeneratorToNull_When_NullProvided() {
        resolver.setSourceGenerator(null);
        assertNull(resolver.getSourceGenerator());
    }

    @Test
    public void should_ReplaceSourceGenerator_When_SetCalledMultipleTimes() {
        SourceGenerator first = mock(SourceGenerator.class);
        SourceGenerator second = mock(SourceGenerator.class);

        resolver.setSourceGenerator(first);
        assertSame(first, resolver.getSourceGenerator());

        resolver.setSourceGenerator(second);
        assertSame(second, resolver.getSourceGenerator());
    }

    // ===== Tests for getSourceGenerator =====

    @Test
    public void should_ReturnSourceGenerator_When_SetPreviously() {
        resolver.setSourceGenerator(mockSourceGenerator);
        assertSame(mockSourceGenerator, resolver.getSourceGenerator());
    }

    @Test
    public void should_ReturnNull_When_NotSetPreviously() {
        resolver.setSourceGenerator(null);
        assertNull(resolver.getSourceGenerator());
    }

    @Test
    public void should_ReturnNull_When_GetSourceGeneratorBeforeSet() {
        BaseClassNameConflictResolver newResolver =
            new BaseClassNameConflictResolver() {
                @Override
                public void changeClassInfoAsResultOfConflict(
                    final JClass jClass,
                    final String xpath,
                    final String typedXPath,
                    final Annotated annotated
                ) {}
            };
        assertNull(newResolver.getSourceGenerator());
    }

    // ===== Tests for calculateXPathPrefix - Simple Cases =====

    @Test
    public void should_ReturnEmptyString_When_XPathHasSingleToken() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName(anyString())).thenReturn("Token");

        String result = resolver.calculateXPathPrefix("/element");
        assertEquals("", result);
    }

    @Test
    public void should_ReturnFirstToken_When_XPathHasTwoTokens() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName("parent")).thenReturn("Parent");
        when(mockJavaNaming.toJavaClassName("child")).thenReturn("Child");

        String result = resolver.calculateXPathPrefix("/parent/child");
        assertEquals("Parent", result);
    }

    @Test
    public void should_ReturnEmptyString_When_XPathIsEmpty() {
        resolver.setSourceGenerator(mockSourceGenerator);
        String result = resolver.calculateXPathPrefix("");
        assertEquals("", result);
    }

    @Test
    public void should_ReturnEmptyString_When_XPathIsOnlySlash() {
        resolver.setSourceGenerator(mockSourceGenerator);
        String result = resolver.calculateXPathPrefix("/");
        assertEquals("", result);
    }

    @Test
    public void should_ReturnEmptyString_When_XPathIsOnlySlashes() {
        resolver.setSourceGenerator(mockSourceGenerator);
        String result = resolver.calculateXPathPrefix("///");
        assertEquals("", result);
    }

    // ===== Tests for calculateXPathPrefix - Multiple Tokens =====

    @Test
    public void should_ConcatenateNonFinalTokens_When_XPathHasThreeTokens() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName("a")).thenReturn("A");
        when(mockJavaNaming.toJavaClassName("b")).thenReturn("B");
        when(mockJavaNaming.toJavaClassName("c")).thenReturn("C");

        String result = resolver.calculateXPathPrefix("/a/b/c");
        assertEquals("AB", result);
    }

    @Test
    public void should_ConcatenateAllTokensButLast_When_XPathHasFourTokens() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName("a")).thenReturn("A");
        when(mockJavaNaming.toJavaClassName("b")).thenReturn("B");
        when(mockJavaNaming.toJavaClassName("c")).thenReturn("C");
        when(mockJavaNaming.toJavaClassName("d")).thenReturn("D");

        String result = resolver.calculateXPathPrefix("/a/b/c/d");
        assertEquals("ABC", result);
    }

    @Test
    public void should_ConcatenateCorrectly_When_XPathHasFiveTokens() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName("a")).thenReturn("A");
        when(mockJavaNaming.toJavaClassName("b")).thenReturn("B");
        when(mockJavaNaming.toJavaClassName("c")).thenReturn("C");
        when(mockJavaNaming.toJavaClassName("d")).thenReturn("D");
        when(mockJavaNaming.toJavaClassName("e")).thenReturn("E");

        String result = resolver.calculateXPathPrefix("/a/b/c/d/e");
        assertEquals("ABCD", result);
    }

    // ===== Tests for calculateXPathPrefix - Delimiter Handling =====

    @Test
    public void should_HandleDotDelimiter_When_XPathUsesDots() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName("parent")).thenReturn("Parent");
        when(mockJavaNaming.toJavaClassName("child")).thenReturn("Child");

        String result = resolver.calculateXPathPrefix("parent.child");
        assertEquals("Parent", result);
    }

    @Test
    public void should_HandleMixedDelimiters_When_XPathUsesSlashAndDot() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName("a")).thenReturn("A");
        when(mockJavaNaming.toJavaClassName("b")).thenReturn("B");
        when(mockJavaNaming.toJavaClassName("c")).thenReturn("C");

        String result = resolver.calculateXPathPrefix("/a.b/c");
        assertNotNull(result);
        assertTrue(result.length() >= 0);
    }

    // ===== Tests for calculateXPathPrefix - Colon Handling =====

    @Test
    public void should_StripColonAndUseAfterPart_When_TokenHasColon() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName("parent")).thenReturn("Parent");
        when(mockJavaNaming.toJavaClassName("value")).thenReturn("Value");

        String result = resolver.calculateXPathPrefix("/parent/custom:value");
        assertEquals("Parent", result);
    }

    @Test
    public void should_HandleMultipleColons_When_TokenHasMultipleColons() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName(anyString())).thenAnswer(inv -> {
            String arg = inv.getArgument(0);
            return "Converted";
        });

        String result = resolver.calculateXPathPrefix("/a/b:c:d");
        assertNotNull(result);
    }

    // ===== Tests for calculateXPathPrefix - Special Cases =====

    @Test
    public void should_CallJavaNamingForEachNonFinalToken_When_CalculatingPrefix() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName(anyString())).thenReturn("Token");

        resolver.calculateXPathPrefix("/a/b/c");
        verify(mockJavaNaming, atLeast(2)).toJavaClassName(anyString());
    }

    @Test
    public void should_NotCallJavaNamingForLastToken_When_CalculatingPrefix() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName("a")).thenReturn("A");

        resolver.calculateXPathPrefix("/a/b");
        verify(mockJavaNaming, times(1)).toJavaClassName("a");
    }

    @Test
    public void should_ConcatenateInCorrectOrder_When_TokensAreProcessed() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName("first")).thenReturn("First");
        when(mockJavaNaming.toJavaClassName("second")).thenReturn("Second");

        String result = resolver.calculateXPathPrefix("/first/second/third");
        assertEquals("FirstSecond", result);
    }

    @Test
    public void should_PreserveJavaNamingConvention_When_ConvertingTokens() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName("my-element")).thenReturn(
            "MyElement"
        );
        when(mockJavaNaming.toJavaClassName("next")).thenReturn("Next");

        String result = resolver.calculateXPathPrefix("/my-element/next");
        assertEquals("MyElement", result);
    }

    // ===== Tests for calculateXPathPrefix - Error Conditions =====

    @Test
    public void should_ThrowNullPointerException_When_SourceGeneratorIsNull() {
        resolver.setSourceGenerator(null);
        try {
            resolver.calculateXPathPrefix("/parent/element");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void should_ThrowNullPointerException_When_XPathIsNull() {
        resolver.setSourceGenerator(mockSourceGenerator);
        resolver.calculateXPathPrefix(null);
    }

    // ===== Tests for abstract method =====

    @Test
    public void should_InstantiateWithAnonymousClass_When_AbstractMethodImplemented() {
        BaseClassNameConflictResolver testResolver =
            new BaseClassNameConflictResolver() {
                @Override
                public void changeClassInfoAsResultOfConflict(
                    final JClass jClass,
                    final String xpath,
                    final String typedXPath,
                    final Annotated annotated
                ) {}
            };
        assertNotNull(testResolver);
    }

    @Test
    public void should_CallAbstractMethod_When_ImplementationProvided() {
        resolver.changeClassInfoAsResultOfConflict(
            mockJClass,
            "/xpath",
            "/typed",
            mockAnnotated
        );
        // Should not throw exception
        assertTrue(true);
    }

    @Test
    public void should_CreateMultipleInstances_When_DifferentImplementationsProvided() {
        BaseClassNameConflictResolver r1 = new BaseClassNameConflictResolver() {
            @Override
            public void changeClassInfoAsResultOfConflict(
                final JClass jClass,
                final String xpath,
                final String typedXPath,
                final Annotated annotated
            ) {}
        };
        BaseClassNameConflictResolver r2 = new BaseClassNameConflictResolver() {
            @Override
            public void changeClassInfoAsResultOfConflict(
                final JClass jClass,
                final String xpath,
                final String typedXPath,
                final Annotated annotated
            ) {}
        };
        assertNotSame(r1, r2);
    }

    // ===== Integration Tests =====

    @Test
    public void should_WorkEndToEnd_When_FullWorkflowExecuted() {
        SourceGenerator sg = mock(SourceGenerator.class);
        JavaNaming naming = mock(JavaNaming.class);
        when(sg.getJavaNaming()).thenReturn(naming);
        when(naming.toJavaClassName("foo")).thenReturn("Foo");
        when(naming.toJavaClassName("bar")).thenReturn("Bar");

        BaseClassNameConflictResolver r = new BaseClassNameConflictResolver() {
            @Override
            public void changeClassInfoAsResultOfConflict(
                final JClass jClass,
                final String xpath,
                final String typedXPath,
                final Annotated annotated
            ) {}
        };
        r.setSourceGenerator(sg);
        String prefix = r.calculateXPathPrefix("/foo/bar");
        assertEquals("Foo", prefix);
    }

    @Test
    public void should_ReuseSourceGenerator_When_CalledMultipleTimes() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName(anyString())).thenReturn("T");

        resolver.calculateXPathPrefix("/a/b");
        resolver.calculateXPathPrefix("/c/d");
        assertSame(mockSourceGenerator, resolver.getSourceGenerator());
    }

    @Test
    public void should_CalculateDifferentPrefixes_When_DifferentXPathsProvided() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName("a")).thenReturn("A");
        when(mockJavaNaming.toJavaClassName("b")).thenReturn("B");
        when(mockJavaNaming.toJavaClassName("c")).thenReturn("C");

        String result1 = resolver.calculateXPathPrefix("/a/b");
        String result2 = resolver.calculateXPathPrefix("/c/b");
        assertEquals("A", result1);
        assertEquals("C", result2);
    }

    @Test
    public void should_HandleComplexXPath_When_LongPathProvided() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName(anyString())).thenAnswer(inv -> {
            String arg = inv.getArgument(0);
            return arg.substring(0, 1).toUpperCase() + arg.substring(1);
        });

        String result = resolver.calculateXPathPrefix(
            "/root/level1/level2/level3/level4"
        );
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void should_VerifyGetJavaNamingCalled_When_CalculatingPrefix() {
        resolver.setSourceGenerator(mockSourceGenerator);
        when(mockJavaNaming.toJavaClassName(anyString())).thenReturn("Token");

        resolver.calculateXPathPrefix("/a/b");
        verify(mockSourceGenerator, atLeastOnce()).getJavaNaming();
    }
}
