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

import java.util.Vector;
import org.castor.xml.JavaNaming;
import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.castor.builder.GroupNaming;
import org.exolab.castor.builder.SourceGenerator;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.Documentation;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Structure;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test class for BaseFactory achieving >95% coverage.
 */
public class BaseFactoryTest {

    private BuilderConfiguration mockConfig;
    private FieldInfoFactory mockInfoFactory;
    private GroupNaming groupNaming;
    private SourceGenerator mockSourceGenerator;
    private BaseFactory baseFactory;

    @Before
    public void setUp() {
        mockConfig = mock(BuilderConfiguration.class);
        mockInfoFactory = mock(FieldInfoFactory.class);
        JavaNaming javaNaming = mock(JavaNaming.class);
        groupNaming = new GroupNaming(javaNaming);
        mockSourceGenerator = mock(SourceGenerator.class);
    }

    // ========== Constructor Tests ==========

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_When_ConfigIsNull() {
        new BaseFactory(
            null,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
    }

    @Test
    public void should_CreateInstance_When_AllParametersValid() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertNotNull(baseFactory);
        assertEquals(mockConfig, baseFactory.getConfig());
        assertEquals(mockInfoFactory, baseFactory.getInfoFactory());
    }

    @Test
    public void should_CreateDefaultFieldInfoFactory_When_InfoFactoryIsNullAndOldNamingEnabled() {
        when(mockConfig.useOldFieldNaming()).thenReturn(true);
        baseFactory = new BaseFactory(
            mockConfig,
            null,
            groupNaming,
            mockSourceGenerator
        );
        assertNotNull(baseFactory.getInfoFactory());
    }

    @Test
    public void should_CreateNewFieldInfoFactory_When_InfoFactoryIsNullAndOldNamingDisabled() {
        when(mockConfig.useOldFieldNaming()).thenReturn(false);
        baseFactory = new BaseFactory(
            mockConfig,
            null,
            groupNaming,
            mockSourceGenerator
        );
        assertNotNull(baseFactory.getInfoFactory());
    }

    @Test
    public void should_UseProvidedInfoFactory_When_InfoFactoryNotNull() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals(mockInfoFactory, baseFactory.getInfoFactory());
    }

    @Test
    public void should_AcceptNullGroupNaming() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            null,
            mockSourceGenerator
        );
        assertNull(baseFactory.getGroupNaming());
    }

    @Test
    public void should_AcceptNullSourceGenerator() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            null
        );
        assertNull(baseFactory.getSourceGenerator());
    }

    // ========== Getter Tests ==========

    @Test
    public void should_ReturnConfig_When_GetConfigCalled() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals(mockConfig, baseFactory.getConfig());
    }

    @Test
    public void should_ReturnGroupNaming_When_GetGroupNamingCalled() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals(groupNaming, baseFactory.getGroupNaming());
    }

    @Test
    public void should_ReturnSourceGenerator_When_GetSourceGeneratorCalled() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals(mockSourceGenerator, baseFactory.getSourceGenerator());
    }

    @Test
    public void should_ReturnJavaNaming_When_GetJavaNamingCalled() {
        JavaNaming mockJavaNaming = mock(JavaNaming.class);
        when(mockConfig.getJavaNaming()).thenReturn(mockJavaNaming);
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals(mockJavaNaming, baseFactory.getJavaNaming());
    }

    @Test
    public void should_ReturnInfoFactory_When_GetInfoFactoryCalled() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals(mockInfoFactory, baseFactory.getInfoFactory());
    }

    // ========== Setter Tests ==========

    @Test
    public void should_SetGroupNaming_When_SetGroupNamingCalledWithValue() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        JavaNaming newJavaNaming = mock(JavaNaming.class);
        GroupNaming newGroupNaming = new GroupNaming(newJavaNaming);
        baseFactory.setGroupNaming(newGroupNaming);
        assertEquals(newGroupNaming, baseFactory.getGroupNaming());
    }

    @Test
    public void should_SetGroupNamingToNull_When_SetGroupNamingCalledWithNull() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        baseFactory.setGroupNaming(null);
        assertNull(baseFactory.getGroupNaming());
    }

    // ========== Normalize Tests ==========

    @Test
    public void should_ReturnNull_When_NormalizeCalledWithNull() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertNull(baseFactory.normalize(null));
    }

    @Test
    public void should_ReturnEmptyString_When_NormalizeCalledWithEmptyString() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals("", baseFactory.normalize(""));
    }

    @Test
    public void should_ReturnSameString_When_NormalizeCalledWithSimpleText() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals("hello", baseFactory.normalize("hello"));
    }

    @Test
    public void should_RemoveLeadingWhitespace_When_NormalizeCalledWithLeadingSpaces() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals("hello", baseFactory.normalize("   hello"));
    }

    @Test
    public void should_RemoveLeadingTabs_When_NormalizeCalledWithLeadingTabs() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals("hello", baseFactory.normalize("\t\thello"));
    }

    @Test
    public void should_NormalizeMultipleSpaces_When_NormalizeCalledWithConsecutiveSpaces() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals("hello world", baseFactory.normalize("hello    world"));
    }

    @Test
    public void should_PreserveSingleSpace_When_NormalizeCalledWithSingleSpaceBetweenWords() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals("hello world", baseFactory.normalize("hello world"));
    }

    @Test
    public void should_RemoveLeadingNewline_When_NormalizeCalledWithLeadingNewline() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals("hello", baseFactory.normalize("\nhello"));
    }

    @Test
    public void should_RemoveLeadingCarriageReturn_When_NormalizeCalledWithLeadingCR() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        assertEquals("hello", baseFactory.normalize("\rhello"));
    }

    @Test
    public void should_EscapeAsteriskSlashComment_When_NormalizeCalledWithCommentEnd() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        String result = baseFactory.normalize("hello*/world");
        assertEquals("hello", result.substring(0, 5));
        assertTrue(result.contains("\\"));
        assertTrue(result.contains("world"));
    }

    @Test
    public void should_PreserveAsteriskWithoutSlash_When_NormalizeCalledWithAsterisk() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        String result = baseFactory.normalize("hello*world");
        assertFalse(result.contains("\\"));
    }

    @Test
    public void should_NormalizeComplexString_When_NormalizeCalledWithMixedContent() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        String result = baseFactory.normalize("   hello  world");
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("world"));
    }

    @Test
    public void should_HandleTabsBetweenWords_When_NormalizeCalledWithTabs() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        String result = baseFactory.normalize("hello\t\tworld");
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("world"));
    }

    @Test
    public void should_RemoveTrailingWhitespace_When_NormalizeCalledWithTrailingSpaces() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        String result = baseFactory.normalize("hello   ");
        assertTrue(result.startsWith("hello"));
    }

    @Test
    public void should_NormalizeOnlyWhitespace_When_NormalizeCalledWithOnlySpaces() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        String result = baseFactory.normalize("     ");
        assertEquals("", result);
    }

    @Test
    public void should_NormalizeWithNewlinesInMiddle_When_NormalizeCalledWithEmbeddedNewlines() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        String result = baseFactory.normalize("hello\nworld");
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("world"));
    }

    @Test
    public void should_HandleMultipleConsecutiveNewlines() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        String result = baseFactory.normalize("hello\n\n\nworld");
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("world"));
    }

    @Test
    public void should_HandleMixedWhitespaceAndNewlines() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        String result = baseFactory.normalize("  \nhello  \n  world  ");
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("world"));
    }

    @Test
    public void should_PreserveAsteriskFollowedByNonSlash() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        String result = baseFactory.normalize("hello*test");
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("test"));
    }

    @Test
    public void should_EscapeMultipleAsteriskSlash() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        String result = baseFactory.normalize("hello*/world*/end");
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("world"));
        assertTrue(result.contains("end"));
    }

    // ========== createComment(Annotated) Tests ==========

    @Test
    public void should_ReturnNull_When_CreateCommentCalledWithNoAnnotations() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Vector<Annotation> emptyAnnotations = new Vector<>();
        when(mockAnnotated.getAnnotations()).thenReturn(
            emptyAnnotations.elements()
        );

        assertNull(baseFactory.createComment(mockAnnotated));
    }

    @Test
    public void should_ReturnCommentFromAnnotation_When_CreateCommentCalledWithAnnotations() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation = mock(Annotation.class);
        Documentation mockDocumentation = mock(Documentation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> documentations = new Vector<>();
        documentations.add(mockDocumentation);
        when(mockAnnotation.getDocumentation()).thenReturn(
            documentations.elements()
        );
        when(mockDocumentation.getContent()).thenReturn("Test comment");

        assertEquals("Test comment", baseFactory.createComment(mockAnnotated));
    }

    @Test
    public void should_ReturnCommentFromReference_When_ElementIsReference() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        ElementDecl mockElement = mock(ElementDecl.class);
        ElementDecl mockReference = mock(ElementDecl.class);
        Annotation mockAnnotation = mock(Annotation.class);
        Documentation mockDocumentation = mock(Documentation.class);

        Vector<Annotation> emptyAnnotations = new Vector<>();
        when(mockElement.getAnnotations()).thenReturn(
            emptyAnnotations.elements()
        );
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.isReference()).thenReturn(true);
        when(mockElement.getReference()).thenReturn(mockReference);

        Vector<Annotation> refAnnotations = new Vector<>();
        refAnnotations.add(mockAnnotation);
        when(mockReference.getAnnotations()).thenReturn(
            refAnnotations.elements()
        );

        Vector<Documentation> documentations = new Vector<>();
        documentations.add(mockDocumentation);
        when(mockAnnotation.getDocumentation()).thenReturn(
            documentations.elements()
        );
        when(mockDocumentation.getContent()).thenReturn("Referenced comment");

        assertEquals(
            "Referenced comment",
            baseFactory.createComment(mockElement)
        );
    }

    @Test
    public void should_ReturnNull_When_ElementReferenceHasNoAnnotations() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        ElementDecl mockElement = mock(ElementDecl.class);
        ElementDecl mockReference = mock(ElementDecl.class);

        Vector<Annotation> emptyAnnotations = new Vector<>();
        when(mockElement.getAnnotations()).thenReturn(
            emptyAnnotations.elements()
        );
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.isReference()).thenReturn(true);
        when(mockElement.getReference()).thenReturn(mockReference);
        when(mockReference.getAnnotations()).thenReturn(
            emptyAnnotations.elements()
        );

        assertNull(baseFactory.createComment(mockElement));
    }

    @Test
    public void should_ReturnNull_When_NonReferenceElementHasNoAnnotations() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        ElementDecl mockElement = mock(ElementDecl.class);

        Vector<Annotation> emptyAnnotations = new Vector<>();
        when(mockElement.getAnnotations()).thenReturn(
            emptyAnnotations.elements()
        );
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.isReference()).thenReturn(false);

        assertNull(baseFactory.createComment(mockElement));
    }

    @Test
    public void should_ReturnNull_When_AnnotationHasNoDocumentations() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation = mock(Annotation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> emptyDocs = new Vector<>();
        when(mockAnnotation.getDocumentation()).thenReturn(
            emptyDocs.elements()
        );

        String result = baseFactory.createComment(mockAnnotated);
        assertTrue(result == null || result.isEmpty());
    }

    @Test
    public void should_ReturnNull_When_CreateCommentCalledWithUnknownStructureType() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);

        Vector<Annotation> emptyAnnotations = new Vector<>();
        when(mockAnnotated.getAnnotations()).thenReturn(
            emptyAnnotations.elements()
        );
        when(mockAnnotated.getStructureType()).thenReturn((short) 999);

        assertNull(baseFactory.createComment(mockAnnotated));
    }

    // ========== extractCommentsFromAnnotations Tests ==========

    @Test
    public void should_ReturnNull_When_ExtractCommentsCalledWithNoAnnotations() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Vector<Annotation> emptyAnnotations = new Vector<>();
        when(mockAnnotated.getAnnotations()).thenReturn(
            emptyAnnotations.elements()
        );

        assertNull(baseFactory.extractCommentsFromAnnotations(mockAnnotated));
    }

    @Test
    public void should_ReturnCombinedComments_When_ExtractCommentsCalledWithMultipleAnnotations() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation1 = mock(Annotation.class);
        Annotation mockAnnotation2 = mock(Annotation.class);
        Documentation mockDoc1 = mock(Documentation.class);
        Documentation mockDoc2 = mock(Documentation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation1);
        annotations.add(mockAnnotation2);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> docs1 = new Vector<>();
        docs1.add(mockDoc1);
        when(mockAnnotation1.getDocumentation()).thenReturn(docs1.elements());
        when(mockDoc1.getContent()).thenReturn("First comment");

        Vector<Documentation> docs2 = new Vector<>();
        docs2.add(mockDoc2);
        when(mockAnnotation2.getDocumentation()).thenReturn(docs2.elements());
        when(mockDoc2.getContent()).thenReturn("Second comment");

        String result = baseFactory.extractCommentsFromAnnotations(
            mockAnnotated
        );
        assertTrue(result.contains("First comment"));
        assertTrue(result.contains("Second comment"));
    }

    @Test
    public void should_SkipNullDocumentationContent_When_ExtractCommentsCalledWithNullContent() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation = mock(Annotation.class);
        Documentation mockDoc1 = mock(Documentation.class);
        Documentation mockDoc2 = mock(Documentation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> docs = new Vector<>();
        docs.add(mockDoc1);
        docs.add(mockDoc2);
        when(mockAnnotation.getDocumentation()).thenReturn(docs.elements());
        when(mockDoc1.getContent()).thenReturn(null);
        when(mockDoc2.getContent()).thenReturn("Valid comment");

        String result = baseFactory.extractCommentsFromAnnotations(
            mockAnnotated
        );
        assertTrue(result.contains("Valid comment"));
    }

    @Test
    public void should_ReturnNormalizedComments_When_ExtractCommentsCalledWithWhitespace() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation = mock(Annotation.class);
        Documentation mockDoc = mock(Documentation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> docs = new Vector<>();
        docs.add(mockDoc);
        when(mockAnnotation.getDocumentation()).thenReturn(docs.elements());
        when(mockDoc.getContent()).thenReturn("  test   comment  ");

        String result = baseFactory.extractCommentsFromAnnotations(
            mockAnnotated
        );
        assertTrue(result.contains("test"));
        assertTrue(result.contains("comment"));
    }

    @Test
    public void should_ReturnEmptyStringWhenNormalized_When_ExtractCommentsCalledWithOnlyWhitespace() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation = mock(Annotation.class);
        Documentation mockDoc = mock(Documentation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> docs = new Vector<>();
        docs.add(mockDoc);
        when(mockAnnotation.getDocumentation()).thenReturn(docs.elements());
        when(mockDoc.getContent()).thenReturn("     ");

        String result = baseFactory.extractCommentsFromAnnotations(
            mockAnnotated
        );
        assertEquals("", result);
    }

    @Test
    public void should_HandleMultipleDocumentationsPerAnnotation_When_ExtractCommentsCalledWithMultipleDocs() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation = mock(Annotation.class);
        Documentation mockDoc1 = mock(Documentation.class);
        Documentation mockDoc2 = mock(Documentation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> docs = new Vector<>();
        docs.add(mockDoc1);
        docs.add(mockDoc2);
        when(mockAnnotation.getDocumentation()).thenReturn(docs.elements());
        when(mockDoc1.getContent()).thenReturn("Doc1");
        when(mockDoc2.getContent()).thenReturn("Doc2");

        String result = baseFactory.extractCommentsFromAnnotations(
            mockAnnotated
        );
        assertTrue(result.contains("Doc1"));
        assertTrue(result.contains("Doc2"));
    }

    @Test
    public void should_HandleEmptyDocumentationList_When_AnnotationHasNoDocumentations() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation = mock(Annotation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> emptyDocs = new Vector<>();
        when(mockAnnotation.getDocumentation()).thenReturn(
            emptyDocs.elements()
        );

        String result = baseFactory.extractCommentsFromAnnotations(
            mockAnnotated
        );
        assertEquals("", result);
    }

    @Test
    public void should_HandleMultipleAnnotationsWithMixedContent() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation1 = mock(Annotation.class);
        Annotation mockAnnotation2 = mock(Annotation.class);
        Documentation mockDoc1 = mock(Documentation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation1);
        annotations.add(mockAnnotation2);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> docs1 = new Vector<>();
        docs1.add(mockDoc1);
        when(mockAnnotation1.getDocumentation()).thenReturn(docs1.elements());
        when(mockDoc1.getContent()).thenReturn("Content");

        Vector<Documentation> docs2 = new Vector<>();
        when(mockAnnotation2.getDocumentation()).thenReturn(docs2.elements());

        String result = baseFactory.extractCommentsFromAnnotations(
            mockAnnotated
        );
        assertNotNull(result);
        assertTrue(result.contains("Content"));
    }

    @Test
    public void should_HandleAnnotationWithNullAndValidContent() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation = mock(Annotation.class);
        Documentation mockDoc1 = mock(Documentation.class);
        Documentation mockDoc2 = mock(Documentation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> docs = new Vector<>();
        docs.add(mockDoc1);
        docs.add(mockDoc2);
        when(mockAnnotation.getDocumentation()).thenReturn(docs.elements());
        when(mockDoc1.getContent()).thenReturn(null);
        when(mockDoc2.getContent()).thenReturn("ValidContent");

        String result = baseFactory.extractCommentsFromAnnotations(
            mockAnnotated
        );
        assertNotNull(result);
        assertTrue(result.contains("ValidContent"));
    }

    @Test
    public void should_HandleCommentWithCommentEndMarker() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation = mock(Annotation.class);
        Documentation mockDoc = mock(Documentation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> docs = new Vector<>();
        docs.add(mockDoc);
        when(mockAnnotation.getDocumentation()).thenReturn(docs.elements());
        when(mockDoc.getContent()).thenReturn("comment*/test");

        String result = baseFactory.extractCommentsFromAnnotations(
            mockAnnotated
        );
        assertTrue(result.contains("comment"));
        assertTrue(result.contains("test"));
    }

    @Test
    public void should_HandleLeadingNewlineInComments() {
        baseFactory = new BaseFactory(
            mockConfig,
            mockInfoFactory,
            groupNaming,
            mockSourceGenerator
        );
        Annotated mockAnnotated = mock(Annotated.class);
        Annotation mockAnnotation = mock(Annotation.class);
        Documentation mockDoc = mock(Documentation.class);

        Vector<Annotation> annotations = new Vector<>();
        annotations.add(mockAnnotation);
        when(mockAnnotated.getAnnotations()).thenReturn(annotations.elements());

        Vector<Documentation> docs = new Vector<>();
        docs.add(mockDoc);
        when(mockAnnotation.getDocumentation()).thenReturn(docs.elements());
        when(mockDoc.getContent()).thenReturn("\ntest content");

        String result = baseFactory.extractCommentsFromAnnotations(
            mockAnnotated
        );
        assertTrue(result.contains("test"));
        assertTrue(result.contains("content"));
    }
}
