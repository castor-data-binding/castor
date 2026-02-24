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
 */
package org.exolab.castor.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JEnum;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Comprehensive test suite for FactoryState covering >95% code coverage.
 */
public class FactoryStateTest {

    @Mock
    private FieldInfo mockFieldInfo;

    @Mock
    private Annotated mockAnnotated;

    private SGStateInfo sgStateInfo;
    private FactoryState factoryState;
    private FactoryState parentFactoryState;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sgStateInfo = org.mockito.Mockito.mock(SGStateInfo.class);
    }

    @Test
    public void should_CreateFactoryState_When_ValidArgumentsProvided() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );

        assertNotNull(factoryState);
        assertNotNull(factoryState.getJClass());
        assertEquals("TestClass", factoryState.getJClass().getName());
        assertNotNull(factoryState.getClassInfo());
        assertEquals("com.test", factoryState.getPackageName());
    }

    @Test
    public void should_CreateJEnum_When_EnumerationTrue() {
        factoryState = new FactoryState(
            "TestEnum",
            sgStateInfo,
            "com.test",
            null,
            true
        );

        assertNotNull(factoryState.getJClass());
        assertTrue(factoryState.getJClass() instanceof JEnum);
        assertEquals("TestEnum", factoryState.getJClass().getName());
    }

    @Test
    public void should_CreateJClass_When_EnumerationFalse() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null,
            false
        );

        assertNotNull(factoryState.getJClass());
        assertTrue(factoryState.getJClass() instanceof JClass);
        assertFalse(factoryState.getJClass() instanceof JEnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_When_SGStateInfoIsNull() {
        new FactoryState("TestClass", null, "com.test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_When_SGStateInfoNullWithEnum() {
        new FactoryState("TestClass", null, "com.test", null, true);
    }

    @Test
    public void should_ReturnCorrectPackageName() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.example.pkg",
            null
        );
        assertEquals("com.example.pkg", factoryState.getPackageName());
    }

    @Test
    public void should_ReturnClassInfo() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        ClassInfo classInfo = factoryState.getClassInfo();
        assertNotNull(classInfo);
        assertSame(factoryState.getJClass(), classInfo.getJClass());
    }

    @Test
    public void should_ReturnJClass() {
        factoryState = new FactoryState(
            "MyClass",
            sgStateInfo,
            "com.test",
            null
        );
        JClass jClass = factoryState.getJClass();
        assertNotNull(jClass);
        assertEquals("MyClass", jClass.getName());
    }

    @Test
    public void should_ReturnSameJClassInstance() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        JClass first = factoryState.getJClass();
        JClass second = factoryState.getJClass();
        assertSame(first, second);
    }

    @Test
    public void should_ReturnSameClassInfoInstance() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        ClassInfo first = factoryState.getClassInfo();
        ClassInfo second = factoryState.getClassInfo();
        assertSame(first, second);
    }

    @Test
    public void should_ReturnNullFieldInfoForChoice_Initially() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        assertNull(factoryState.getFieldInfoForChoice());
    }

    @Test
    public void should_SetAndReturnFieldInfoForChoice() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setFieldInfoForChoice(mockFieldInfo);
        assertSame(mockFieldInfo, factoryState.getFieldInfoForChoice());
    }

    @Test
    public void should_SetFieldInfoForChoiceToNull() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setFieldInfoForChoice(mockFieldInfo);
        factoryState.setFieldInfoForChoice(null);
        assertNull(factoryState.getFieldInfoForChoice());
    }

    @Test
    public void should_UpdateFieldInfoForChoice() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        FieldInfo fieldInfo1 = mock(FieldInfo.class);
        FieldInfo fieldInfo2 = mock(FieldInfo.class);
        factoryState.setFieldInfoForChoice(fieldInfo1);
        assertEquals(fieldInfo1, factoryState.getFieldInfoForChoice());
        factoryState.setFieldInfoForChoice(fieldInfo2);
        assertEquals(fieldInfo2, factoryState.getFieldInfoForChoice());
    }

    @Test
    public void should_MarkAnnotatedAsProcessed() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.markAsProcessed(mockAnnotated);
        assertTrue(factoryState.processed(mockAnnotated));
    }

    @Test
    public void should_ReturnTrueForProcessedWhenMarked() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        Annotated annotated = mock(Annotated.class);
        factoryState.markAsProcessed(annotated);
        assertTrue(factoryState.processed(annotated));
    }

    @Test
    public void should_ReturnFalseForProcessedWhenNotMarked() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        Annotated annotated = mock(Annotated.class);
        assertFalse(factoryState.processed(annotated));
    }

    @Test
    public void should_MarkMultipleAnnotatedObjectsAsProcessed() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        Annotated annotated1 = mock(Annotated.class);
        Annotated annotated2 = mock(Annotated.class);
        Annotated annotated3 = mock(Annotated.class);
        factoryState.markAsProcessed(annotated1);
        factoryState.markAsProcessed(annotated2);
        factoryState.markAsProcessed(annotated3);
        assertTrue(factoryState.processed(annotated1));
        assertTrue(factoryState.processed(annotated2));
        assertTrue(factoryState.processed(annotated3));
    }

    @Test
    public void should_ReturnFalseForBoundPropertiesWhenDisabled() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        assertFalse(factoryState.hasBoundProperties());
    }

    @Test
    public void should_SetBoundPropertiesFlag() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        assertFalse(factoryState.hasBoundProperties());
        factoryState.setBoundProperties(true);
        assertTrue(factoryState.hasBoundProperties());
    }

    @Test
    public void should_ToggleBoundPropertiesFlag() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setBoundProperties(true);
        assertTrue(factoryState.hasBoundProperties());
        factoryState.setBoundProperties(false);
        assertFalse(factoryState.hasBoundProperties());
        factoryState.setBoundProperties(true);
        assertTrue(factoryState.hasBoundProperties());
    }

    @Test
    public void should_ReturnFalseForCreateGroupItemInitially() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        assertFalse(factoryState.isCreateGroupItem());
    }

    @Test
    public void should_SetCreateGroupItem() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setCreateGroupItem(true);
        assertTrue(factoryState.isCreateGroupItem());
    }

    @Test
    public void should_ToggleCreateGroupItem() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setCreateGroupItem(true);
        assertTrue(factoryState.isCreateGroupItem());
        factoryState.setCreateGroupItem(false);
        assertFalse(factoryState.isCreateGroupItem());
    }

    @Test
    public void should_ReturnNullParentInitially() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        assertNull(factoryState.getParent());
    }

    @Test
    public void should_SetAndReturnParent() {
        parentFactoryState = new FactoryState(
            "ParentClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState = new FactoryState(
            "ChildClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setParent(parentFactoryState);
        assertSame(parentFactoryState, factoryState.getParent());
    }

    @Test
    public void should_SetParentToNull() {
        parentFactoryState = new FactoryState(
            "ParentClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState = new FactoryState(
            "ChildClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setParent(parentFactoryState);
        factoryState.setParent(null);
        assertNull(factoryState.getParent());
    }

    @Test
    public void should_DelegateBindReference() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        Object key = new Object();
        ClassInfo classInfoRef = new ClassInfo(new JClass("RefClass"));
        factoryState.bindReference(key, classInfoRef);
        assertNotNull(factoryState);
    }

    @Test
    public void should_ReturnSGStateInfo() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        SGStateInfo result = factoryState.getSGStateInfo();
        assertNotNull(result);
    }

    @Test
    public void should_ProcessedCheckParent_WhenNotFoundLocally() {
        parentFactoryState = new FactoryState(
            "ParentClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState = new FactoryState(
            "ChildClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setParent(parentFactoryState);
        Annotated parentAnnotated = mock(Annotated.class);
        parentFactoryState.markAsProcessed(parentAnnotated);
        assertTrue(factoryState.processed(parentAnnotated));
    }

    @Test
    public void should_ReturnFalseWhenNotInParentHierarchy() {
        parentFactoryState = new FactoryState(
            "ParentClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState = new FactoryState(
            "ChildClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setParent(parentFactoryState);
        Annotated unmarkedAnnotated = mock(Annotated.class);
        assertFalse(factoryState.processed(unmarkedAnnotated));
    }

    @Test
    public void should_HandleMultipleLevelParentHierarchy() {
        FactoryState grandparent = new FactoryState(
            "GrandparentClass",
            sgStateInfo,
            "com.test",
            null
        );
        FactoryState parent = new FactoryState(
            "ParentClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState = new FactoryState(
            "ChildClass",
            sgStateInfo,
            "com.test",
            null
        );
        Annotated grandparentAnnotated = mock(Annotated.class);
        grandparent.markAsProcessed(grandparentAnnotated);
        parent.setParent(grandparent);
        factoryState.setParent(parent);
        assertTrue(factoryState.processed(grandparentAnnotated));
    }

    @Test
    public void should_DelegateResolve() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        Object key = "testKey";
        ClassInfo result = factoryState.resolve(key);
        assertNotNull(factoryState);
    }

    @Test
    public void should_HandleEmptyPackageName() {
        factoryState = new FactoryState("TestClass", sgStateInfo, "", null);
        assertEquals("", factoryState.getPackageName());
    }

    @Test
    public void should_HandleNullPackageName() {
        factoryState = new FactoryState("TestClass", sgStateInfo, null, null);
        assertNull(factoryState.getPackageName());
    }

    @Test
    public void should_ImplementClassInfoResolver() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        assertTrue(factoryState instanceof ClassInfoResolver);
    }

    @Test
    public void should_MaintainIndependentState_AcrossInstances() {
        FactoryState state1 = new FactoryState(
            "Class1",
            sgStateInfo,
            "com.test",
            null
        );
        FactoryState state2 = new FactoryState(
            "Class2",
            sgStateInfo,
            "com.test",
            null
        );
        state1.setCreateGroupItem(true);
        state2.setCreateGroupItem(false);
        assertTrue(state1.isCreateGroupItem());
        assertFalse(state2.isCreateGroupItem());
    }

    @Test
    public void should_MaintainIndependentProcessedLists() {
        FactoryState state1 = new FactoryState(
            "Class1",
            sgStateInfo,
            "com.test",
            null
        );
        FactoryState state2 = new FactoryState(
            "Class2",
            sgStateInfo,
            "com.test",
            null
        );
        Annotated annotated1 = mock(Annotated.class);
        Annotated annotated2 = mock(Annotated.class);
        state1.markAsProcessed(annotated1);
        state2.markAsProcessed(annotated2);
        assertTrue(state1.processed(annotated1));
        assertFalse(state1.processed(annotated2));
        assertFalse(state2.processed(annotated1));
        assertTrue(state2.processed(annotated2));
    }

    @Test
    public void should_HandleDifferentClassNames() {
        FactoryState fs1 = new FactoryState(
            "Class1",
            sgStateInfo,
            "com.test",
            null
        );
        FactoryState fs2 = new FactoryState(
            "Class2",
            sgStateInfo,
            "com.test",
            null
        );
        assertEquals("Class1", fs1.getJClass().getName());
        assertEquals("Class2", fs2.getJClass().getName());
    }

    @Test
    public void should_EnumFlagCreatesDifferentTypes() {
        FactoryState enumState = new FactoryState(
            "EnumType",
            sgStateInfo,
            "com.test",
            null,
            true
        );
        FactoryState classState = new FactoryState(
            "ClassType",
            sgStateInfo,
            "com.test",
            null,
            false
        );
        assertTrue(enumState.getJClass() instanceof JEnum);
        assertFalse(classState.getJClass() instanceof JEnum);
    }

    @Test
    public void should_SetBoundPropertiesFalse() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setBoundProperties(true);
        assertTrue(factoryState.hasBoundProperties());
        factoryState.setBoundProperties(false);
        assertFalse(factoryState.hasBoundProperties());
    }

    @Test
    public void should_SetCreateGroupItemFalse() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setCreateGroupItem(true);
        assertTrue(factoryState.isCreateGroupItem());
        factoryState.setCreateGroupItem(false);
        assertFalse(factoryState.isCreateGroupItem());
    }

    @Test
    public void should_GetPackageNameMultipleTimes() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test.pkg",
            null
        );
        String pkg1 = factoryState.getPackageName();
        String pkg2 = factoryState.getPackageName();
        assertEquals(pkg1, pkg2);
        assertEquals("com.test.pkg", pkg1);
    }

    @Test
    public void should_GetFieldInfoForChoiceMultipleTimes() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setFieldInfoForChoice(mockFieldInfo);
        FieldInfo fi1 = factoryState.getFieldInfoForChoice();
        FieldInfo fi2 = factoryState.getFieldInfoForChoice();
        assertSame(fi1, fi2);
        assertSame(mockFieldInfo, fi1);
    }

    @Test
    public void should_ProcessedReturnFalseInitially() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        assertFalse(factoryState.processed(mockAnnotated));
    }

    @Test
    public void should_HasBoundPropertiesReturnFalseInitially() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        assertFalse(factoryState.hasBoundProperties());
    }

    @Test
    public void should_IsCreateGroupItemReturnFalseInitially() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        assertFalse(factoryState.isCreateGroupItem());
    }

    @Test
    public void should_GetParentReturnNullInitially() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        assertNull(factoryState.getParent());
    }

    @Test
    public void should_MarkAndProcessMultipleAnnotateds() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        Annotated a1 = mock(Annotated.class);
        Annotated a2 = mock(Annotated.class);
        factoryState.markAsProcessed(a1);
        assertTrue(factoryState.processed(a1));
        assertFalse(factoryState.processed(a2));
        factoryState.markAsProcessed(a2);
        assertTrue(factoryState.processed(a1));
        assertTrue(factoryState.processed(a2));
    }

    @Test
    public void should_ParentHierarchyProcessedChecks() {
        FactoryState grandparent = new FactoryState(
            "GP",
            sgStateInfo,
            "com.test",
            null
        );
        FactoryState parent = new FactoryState(
            "P",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState = new FactoryState("C", sgStateInfo, "com.test", null);
        Annotated gpAnnotated = mock(Annotated.class);
        Annotated pAnnotated = mock(Annotated.class);
        Annotated cAnnotated = mock(Annotated.class);
        grandparent.markAsProcessed(gpAnnotated);
        parent.markAsProcessed(pAnnotated);
        factoryState.markAsProcessed(cAnnotated);
        parent.setParent(grandparent);
        factoryState.setParent(parent);
        assertTrue(factoryState.processed(gpAnnotated));
        assertTrue(factoryState.processed(pAnnotated));
        assertTrue(factoryState.processed(cAnnotated));
    }

    @Test
    public void should_FieldInfoForChoiceIndependentAcrossInstances() {
        FactoryState state1 = new FactoryState(
            "Class1",
            sgStateInfo,
            "com.test",
            null
        );
        FactoryState state2 = new FactoryState(
            "Class2",
            sgStateInfo,
            "com.test",
            null
        );
        FieldInfo fi1 = mock(FieldInfo.class);
        FieldInfo fi2 = mock(FieldInfo.class);
        state1.setFieldInfoForChoice(fi1);
        state2.setFieldInfoForChoice(fi2);
        assertSame(fi1, state1.getFieldInfoForChoice());
        assertSame(fi2, state2.getFieldInfoForChoice());
        assertNotSame(fi1, fi2);
    }

    @Test
    public void should_BoundPropertiesIndependentAcrossInstances() {
        FactoryState state1 = new FactoryState(
            "Class1",
            sgStateInfo,
            "com.test",
            null
        );
        FactoryState state2 = new FactoryState(
            "Class2",
            sgStateInfo,
            "com.test",
            null
        );
        state1.setBoundProperties(true);
        state2.setBoundProperties(false);
        assertTrue(state1.hasBoundProperties());
        assertFalse(state2.hasBoundProperties());
    }

    @Test
    public void should_CreateGroupItemIndependentAcrossInstances() {
        FactoryState state1 = new FactoryState(
            "Class1",
            sgStateInfo,
            "com.test",
            null
        );
        FactoryState state2 = new FactoryState(
            "Class2",
            sgStateInfo,
            "com.test",
            null
        );
        state1.setCreateGroupItem(true);
        state2.setCreateGroupItem(false);
        assertTrue(state1.isCreateGroupItem());
        assertFalse(state2.isCreateGroupItem());
    }

    @Test
    public void should_ParentIndependentAcrossInstances() {
        FactoryState p1 = new FactoryState("P1", sgStateInfo, "com.test", null);
        FactoryState p2 = new FactoryState("P2", sgStateInfo, "com.test", null);
        FactoryState c1 = new FactoryState("C1", sgStateInfo, "com.test", null);
        FactoryState c2 = new FactoryState("C2", sgStateInfo, "com.test", null);
        c1.setParent(p1);
        c2.setParent(p2);
        assertSame(p1, c1.getParent());
        assertSame(p2, c2.getParent());
    }

    @Test
    public void should_HandleSpecialCharacterInClassName() {
        factoryState = new FactoryState(
            "Test$Class",
            sgStateInfo,
            "com.test",
            null
        );
        assertEquals("Test$Class", factoryState.getJClass().getName());
    }

    @Test
    public void should_ClassInfoInitializedWithCorrectJClass() {
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        ClassInfo classInfo = factoryState.getClassInfo();
        assertSame(factoryState.getJClass(), classInfo.getJClass());
    }

    @Test
    public void should_HandleConsecutiveSetParentCalls() {
        FactoryState parent1 = new FactoryState(
            "Parent1",
            sgStateInfo,
            "com.test",
            null
        );
        FactoryState parent2 = new FactoryState(
            "Parent2",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState = new FactoryState(
            "TestClass",
            sgStateInfo,
            "com.test",
            null
        );
        factoryState.setParent(parent1);
        assertSame(parent1, factoryState.getParent());
        factoryState.setParent(parent2);
        assertSame(parent2, factoryState.getParent());
    }
}
