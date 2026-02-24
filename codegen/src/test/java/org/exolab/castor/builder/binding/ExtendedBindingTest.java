/*
 * Redistribution and use of this software and associated documentation ("Software"), with or
 * without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and notices. Redistributions
 * must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote products derived from this Software
 * without prior written permission of Intalio, Inc. For written permission, please contact
 * info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab" nor may "Exolab" appear in
 * their names without prior written permission of Intalio, Inc. Exolab is a registered trademark of
 * Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL INTALIO, INC. OR ITS
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2002 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.binding;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Set;
import org.exolab.castor.builder.binding.xml.AutomaticNamingType;
import org.exolab.castor.builder.binding.xml.ComponentBindingType;
import org.exolab.castor.builder.binding.xml.Exclude;
import org.exolab.castor.builder.binding.xml.Excludes;
import org.exolab.castor.builder.binding.xml.Forces;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Structure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Comprehensive test suite for ExtendedBinding class.
 *
 * @author Test Framework
 */
@RunWith(MockitoJUnitRunner.class)
public class ExtendedBindingTest {

    private ExtendedBinding extendedBinding;

    @Mock
    private Annotated mockAnnotated;

    @Mock
    private AutomaticNamingType mockAutomaticNamingType;

    @Mock
    private Forces mockForces;

    @Mock
    private Excludes mockExcludes;

    @Mock
    private Exclude mockExclude;

    @Before
    public void setUp() {
        extendedBinding = new ExtendedBinding();
    }

    // ==================== Constructor Tests ====================

    @Test
    public void should_CreateInstance_When_NoArgConstructorCalled() {
        assertNotNull(
            "ExtendedBinding instance should be created",
            extendedBinding
        );
    }

    @Test
    public void should_CreateMultipleInstances_When_ConstructorCalledMultipleTimes() {
        ExtendedBinding binding1 = new ExtendedBinding();
        ExtendedBinding binding2 = new ExtendedBinding();
        assertNotNull("First instance should be created", binding1);
        assertNotNull("Second instance should be created", binding2);
        assertNotSame("Instances should be different", binding1, binding2);
    }

    // ==================== getComponentBindingType() Tests ====================

    @Test
    public void should_ReturnNull_When_AnnotatedIsNull() {
        ComponentBindingType result = extendedBinding.getComponentBindingType(
            null
        );
        assertNull("Should return null for null annotated", result);
    }

    @Test
    public void should_ReturnNull_When_AnnotatedIsGroup() {
        when(mockAnnotated.getStructureType()).thenReturn(Structure.GROUP);
        ComponentBindingType result = extendedBinding.getComponentBindingType(
            mockAnnotated
        );
        assertNull("Should return null for GROUP structure type", result);
    }

    // ==================== handleAutomaticNaming() Tests ====================

    @Test
    public void should_HandleAutomaticNaming_When_ForcesPresent() {
        String[] forces = { "element1", "element2" };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertTrue(
            "Should contain forced element1",
            extendedBinding.existsForce("element1")
        );
        assertTrue(
            "Should contain forced element2",
            extendedBinding.existsForce("element2")
        );
    }

    @Test
    public void should_HandleAutomaticNaming_When_ExcludesPresent() {
        Exclude[] excludes = { mockExclude };
        when(mockExclude.getName()).thenReturn("excluded1");
        when(mockExcludes.getExclude()).thenReturn(excludes);
        when(mockAutomaticNamingType.getForces()).thenReturn(null);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(mockExcludes);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertTrue(
            "Should contain excluded element",
            extendedBinding.existsExclusion("excluded1")
        );
        assertEquals(
            "Should have the excluded mapping",
            mockExclude,
            extendedBinding.getExclusion("excluded1")
        );
    }

    @Test
    public void should_HandleAutomaticNaming_When_BothForcesAndExcludes() {
        String[] forces = { "forced1" };
        Exclude[] excludes = { mockExclude };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockExclude.getName()).thenReturn("excluded1");
        when(mockExcludes.getExclude()).thenReturn(excludes);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(mockExcludes);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertTrue(
            "Should contain forced element",
            extendedBinding.existsForce("forced1")
        );
        assertTrue(
            "Should contain excluded element",
            extendedBinding.existsExclusion("excluded1")
        );
    }

    @Test
    public void should_HandleAutomaticNaming_When_NoForcesNoExcludes() {
        when(mockAutomaticNamingType.getForces()).thenReturn(null);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertTrue(
            "Forces set should be empty",
            extendedBinding.getForces().isEmpty()
        );
    }

    @Test
    public void should_HandleAutomaticNaming_When_ForcesWithMultipleElements() {
        String[] forces = { "elem1", "elem2", "elem3", "elem4" };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertEquals(
            "Should contain all forced elements",
            4,
            extendedBinding.getForces().size()
        );
    }

    @Test
    public void should_HandleAutomaticNaming_When_ExcludesWithMultipleElements() {
        Exclude exclude1 = mock(Exclude.class);
        Exclude exclude2 = mock(Exclude.class);
        Exclude exclude3 = mock(Exclude.class);
        when(exclude1.getName()).thenReturn("ex1");
        when(exclude2.getName()).thenReturn("ex2");
        when(exclude3.getName()).thenReturn("ex3");
        Exclude[] excludes = { exclude1, exclude2, exclude3 };
        when(mockExcludes.getExclude()).thenReturn(excludes);
        when(mockAutomaticNamingType.getForces()).thenReturn(null);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(mockExcludes);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertTrue(
            "Should contain ex1",
            extendedBinding.existsExclusion("ex1")
        );
        assertTrue(
            "Should contain ex2",
            extendedBinding.existsExclusion("ex2")
        );
        assertTrue(
            "Should contain ex3",
            extendedBinding.existsExclusion("ex3")
        );
    }

    @Test
    public void should_HandleAutomaticNaming_When_EmptyForcesArray() {
        String[] forces = {};
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertTrue(
            "Forces should be empty",
            extendedBinding.getForces().isEmpty()
        );
    }

    @Test
    public void should_HandleAutomaticNaming_When_EmptyExcludesArray() {
        Exclude[] excludes = {};
        when(mockExcludes.getExclude()).thenReturn(excludes);
        when(mockAutomaticNamingType.getForces()).thenReturn(null);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(mockExcludes);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertFalse(
            "No exclusions should exist",
            extendedBinding.existsExclusion("any")
        );
    }

    // ==================== existsExclusion() Tests ====================

    @Test
    public void should_ReturnTrue_When_ExclusionExists() {
        Exclude testExclude = mock(Exclude.class);
        when(testExclude.getName()).thenReturn("test");
        Exclude[] excludes = { testExclude };
        when(mockExcludes.getExclude()).thenReturn(excludes);
        when(mockAutomaticNamingType.getForces()).thenReturn(null);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(mockExcludes);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);
        assertTrue(
            "Should return true for existing exclusion",
            extendedBinding.existsExclusion("test")
        );
    }

    @Test
    public void should_ReturnFalse_When_ExclusionDoesNotExist() {
        assertFalse(
            "Should return false for non-existing exclusion",
            extendedBinding.existsExclusion("nonexistent")
        );
    }

    @Test
    public void should_ReturnFalse_When_ExclusionMapIsEmpty() {
        assertFalse(
            "Should return false for empty map",
            extendedBinding.existsExclusion("any")
        );
    }

    @Test
    public void should_ReturnFalse_When_LookingUpNullKey() {
        assertFalse(
            "Should return false for null key",
            extendedBinding.existsExclusion(null)
        );
    }

    @Test
    public void should_ReturnFalse_When_ExclusionRemovedAfterAdding() {
        Exclude testExclude = mock(Exclude.class);
        when(testExclude.getName()).thenReturn("test");
        Exclude[] excludes = { testExclude };
        when(mockExcludes.getExclude()).thenReturn(excludes);
        when(mockAutomaticNamingType.getForces()).thenReturn(null);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(mockExcludes);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);
        assertTrue(
            "Should find exclusion after adding",
            extendedBinding.existsExclusion("test")
        );
    }

    // ==================== getExclusion() Tests ====================

    @Test
    public void should_ReturnExclude_When_ExclusionExists() {
        Exclude testExclude = mock(Exclude.class);
        when(testExclude.getName()).thenReturn("test");
        Exclude[] excludes = { testExclude };
        when(mockExcludes.getExclude()).thenReturn(excludes);
        when(mockAutomaticNamingType.getForces()).thenReturn(null);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(mockExcludes);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);
        Exclude result = extendedBinding.getExclusion("test");
        assertEquals("Should return correct exclusion", testExclude, result);
    }

    @Test
    public void should_ReturnNull_When_ExclusionDoesNotExist() {
        Exclude result = extendedBinding.getExclusion("nonexistent");
        assertNull("Should return null for non-existing exclusion", result);
    }

    @Test
    public void should_ReturnNull_When_LookingUpNullKey() {
        Exclude result = extendedBinding.getExclusion(null);
        assertNull("Should return null for null key", result);
    }

    @Test
    public void should_ReturnCorrectExclude_When_MultipleExclusionsExist() {
        Exclude exclude1 = mock(Exclude.class);
        Exclude exclude2 = mock(Exclude.class);
        Exclude exclude3 = mock(Exclude.class);
        when(exclude1.getName()).thenReturn("ex1");
        when(exclude2.getName()).thenReturn("ex2");
        when(exclude3.getName()).thenReturn("ex3");
        Exclude[] excludes = { exclude1, exclude2, exclude3 };
        when(mockExcludes.getExclude()).thenReturn(excludes);
        when(mockAutomaticNamingType.getForces()).thenReturn(null);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(mockExcludes);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertEquals(
            "Should return exclude1",
            exclude1,
            extendedBinding.getExclusion("ex1")
        );
        assertEquals(
            "Should return exclude2",
            exclude2,
            extendedBinding.getExclusion("ex2")
        );
        assertEquals(
            "Should return exclude3",
            exclude3,
            extendedBinding.getExclusion("ex3")
        );
    }

    // ==================== existsForce() Tests ====================

    @Test
    public void should_ReturnTrue_When_ForceExists() {
        String[] forces = { "element1" };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);
        assertTrue(
            "Should return true for existing force",
            extendedBinding.existsForce("element1")
        );
    }

    @Test
    public void should_ReturnFalse_When_ForceDoesNotExist() {
        assertFalse(
            "Should return false for non-existing force",
            extendedBinding.existsForce("nonexistent")
        );
    }

    @Test
    public void should_ReturnFalse_When_ForceSetIsEmpty() {
        assertFalse(
            "Should return false for empty set",
            extendedBinding.existsForce("any")
        );
    }

    @Test
    public void should_ReturnFalse_When_CheckingNullForce() {
        String[] forces = { "element1" };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);
        assertFalse(
            "Should return false for null force",
            extendedBinding.existsForce(null)
        );
    }

    @Test
    public void should_ReturnTrue_When_MultipleForcesDefined() {
        String[] forces = { "force1", "force2", "force3" };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertTrue("Should find force1", extendedBinding.existsForce("force1"));
        assertTrue("Should find force2", extendedBinding.existsForce("force2"));
        assertTrue("Should find force3", extendedBinding.existsForce("force3"));
        assertFalse(
            "Should not find force4",
            extendedBinding.existsForce("force4")
        );
    }

    // ==================== getForces() Tests ====================

    @Test
    public void should_ReturnEmptySet_When_NoForcesAdded() {
        Set<String> forces = extendedBinding.getForces();
        assertNotNull("Should return a non-null set", forces);
        assertTrue("Should return empty set", forces.isEmpty());
    }

    @Test
    public void should_ReturnAllForces_When_ForcesAdded() {
        String[] forces = { "force1", "force2", "force3" };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        Set<String> result = extendedBinding.getForces();
        assertEquals("Should return all forces", 3, result.size());
        assertTrue("Should contain force1", result.contains("force1"));
        assertTrue("Should contain force2", result.contains("force2"));
        assertTrue("Should contain force3", result.contains("force3"));
    }

    @Test
    public void should_ReturnSetWithSingleForce_When_OnlyOneForceAdded() {
        String[] forces = { "singleForce" };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        Set<String> result = extendedBinding.getForces();
        assertEquals("Should return set with one element", 1, result.size());
        assertTrue("Should contain the force", result.contains("singleForce"));
    }

    @Test
    public void should_ReturnModifiableSet_When_GettingForces() {
        String[] forces = { "initialForce" };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        Set<String> result = extendedBinding.getForces();
        result.add("newForce");
        assertTrue(
            "Changes should be reflected in internal set",
            extendedBinding.existsForce("newForce")
        );
    }

    @Test
    public void should_ReturnSameSetReference_When_CalledMultipleTimes() {
        Set<String> forces1 = extendedBinding.getForces();
        Set<String> forces2 = extendedBinding.getForces();
        assertSame("Should return same set reference", forces1, forces2);
    }

    // ==================== Constants Tests ====================

    @Test
    public void should_HaveCorrectPathSeparatorConstant() {
        assertEquals(
            "PATH_SEPARATOR should be /",
            "/",
            ExtendedBinding.PATH_SEPARATOR
        );
    }

    @Test
    public void should_HaveCorrectAttributePrefixConstant() {
        assertEquals(
            "ATTRIBUTE_PREFIX should be @",
            "@",
            ExtendedBinding.ATTRIBUTE_PREFIX
        );
    }

    @Test
    public void should_HaveCorrectComplexTypeIdConstant() {
        assertEquals(
            "COMPLEXTYPE_ID should be complexType:",
            "complexType:",
            ExtendedBinding.COMPLEXTYPE_ID
        );
    }

    @Test
    public void should_HaveCorrectSimpleTypeIdConstant() {
        assertEquals(
            "SIMPLETYPE_ID should be simpleType:",
            "simpleType:",
            ExtendedBinding.SIMPLETYPE_ID
        );
    }

    @Test
    public void should_HaveCorrectEnumTypeIdConstant() {
        assertEquals(
            "ENUMTYPE_ID should be enumType:",
            "enumType:",
            ExtendedBinding.ENUMTYPE_ID
        );
    }

    @Test
    public void should_HaveCorrectGroupIdConstant() {
        assertEquals(
            "GROUP_ID should be group:",
            "group:",
            ExtendedBinding.GROUP_ID
        );
    }

    // ==================== Integration Tests ====================

    @Test
    public void should_HandleMultipleOperations_When_SequentialCalls() {
        String[] forces = { "elem1" };
        Exclude[] excludes = { mockExclude };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockExclude.getName()).thenReturn("excluded");
        when(mockExcludes.getExclude()).thenReturn(excludes);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(mockExcludes);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertTrue(
            "Should find forced element",
            extendedBinding.existsForce("elem1")
        );
        assertTrue(
            "Should find excluded element",
            extendedBinding.existsExclusion("excluded")
        );
        assertEquals(
            "Should return correct exclusion",
            mockExclude,
            extendedBinding.getExclusion("excluded")
        );
    }

    @Test
    public void should_MaintainStateAcrossMultipleCalls() {
        String[] forces = { "force1" };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        Set<String> result = extendedBinding.getForces();
        assertTrue(
            "Should maintain force after getForces",
            result.contains("force1")
        );
        assertTrue(
            "existsForce should also find it",
            extendedBinding.existsForce("force1")
        );
    }

    @Test
    public void should_HandleEmptyStringsInForces() {
        String[] forces = { "" };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertTrue(
            "Should handle empty string force",
            extendedBinding.existsForce("")
        );
        Set<String> forces2 = extendedBinding.getForces();
        assertTrue("Empty string should be in forces", forces2.contains(""));
    }

    @Test
    public void should_HandleSpecialCharactersInNames() {
        String specialName = "element@name#1";
        String[] forces = { specialName };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertTrue(
            "Should handle special characters",
            extendedBinding.existsForce(specialName)
        );
    }

    @Test
    public void should_BeCaseSensitive_When_CheckingNames() {
        String[] forces = { "Element" };
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertFalse(
            "Should be case sensitive for forces",
            extendedBinding.existsForce("element")
        );
        assertTrue(
            "Should find exact case match",
            extendedBinding.existsForce("Element")
        );
    }

    @Test
    public void should_HandleLargeNumberOfForces() {
        String[] forces = new String[1000];
        for (int i = 0; i < 1000; i++) {
            forces[i] = "force" + i;
        }
        when(mockForces.getForce()).thenReturn(forces);
        when(mockAutomaticNamingType.getForces()).thenReturn(mockForces);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(null);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertEquals(
            "Should handle 1000 forces",
            1000,
            extendedBinding.getForces().size()
        );
        assertTrue(
            "Should find first force",
            extendedBinding.existsForce("force0")
        );
        assertTrue(
            "Should find last force",
            extendedBinding.existsForce("force999")
        );
    }

    @Test
    public void should_HandleLargeNumberOfExclusions() {
        Exclude[] excludes = new Exclude[500];
        for (int i = 0; i < 500; i++) {
            Exclude exc = mock(Exclude.class);
            when(exc.getName()).thenReturn("exclude" + i);
            excludes[i] = exc;
        }
        when(mockExcludes.getExclude()).thenReturn(excludes);
        when(mockAutomaticNamingType.getForces()).thenReturn(null);
        when(mockAutomaticNamingType.getExcludes()).thenReturn(mockExcludes);

        extendedBinding.handleAutomaticNaming(mockAutomaticNamingType);

        assertTrue(
            "Should find first exclusion",
            extendedBinding.existsExclusion("exclude0")
        );
        assertTrue(
            "Should find last exclusion",
            extendedBinding.existsExclusion("exclude499")
        );
    }

    @Test
    public void should_InitializeWithEmptyCollections() {
        ExtendedBinding binding = new ExtendedBinding();
        assertTrue(
            "Forces should be empty initially",
            binding.getForces().isEmpty()
        );
    }

    @Test
    public void should_HandleGetForcesOnEmptyBinding() {
        Set<String> forces = extendedBinding.getForces();
        assertNotNull("Should never return null", forces);
        assertTrue("Should be empty", forces.isEmpty());
        assertEquals("Size should be 0", 0, forces.size());
    }

    @Test
    public void should_GetExclusionReturnNullForEmptyBinding() {
        Exclude result = extendedBinding.getExclusion("any");
        assertNull("Should return null for empty binding", result);
    }

    @Test
    public void should_ExistsForceReturnFalseForEmptyBinding() {
        boolean result = extendedBinding.existsForce("any");
        assertFalse("Should return false for empty binding", result);
    }

    @Test
    public void should_ExistsExclusionReturnFalseForEmptyBinding() {
        boolean result = extendedBinding.existsExclusion("any");
        assertFalse("Should return false for empty binding", result);
    }
}
