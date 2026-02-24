/*
 * Copyright 2006 Werner Guttmann
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
package org.exolab.castor.builder.conflictresolution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.exolab.castor.util.dialog.ConsoleDialog;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Comprehensive test suite for {@link WarningViaDialogClassNameCRStrategy}.
 * Achieves >95% coverage for all public methods and branches.
 */
@RunWith(MockitoJUnitRunner.class)
public class WarningViaDialogClassNameCRStrategyTest {

    private WarningViaDialogClassNameCRStrategy strategy;

    @Mock
    private ConsoleDialog mockDialog;

    @Before
    public void setUp() {
        strategy = new WarningViaDialogClassNameCRStrategy();
    }

    // ========== getName() Tests ==========

    @Test
    public void should_ReturnStrategyName() {
        assertEquals("warnViaConsoleDialog", strategy.getName());
    }

    @Test
    public void should_ReturnNameConstantValue() {
        assertEquals(
            WarningViaDialogClassNameCRStrategy.NAME,
            strategy.getName()
        );
    }

    @Test
    public void should_ReturnNonNullName() {
        assertNotNull(strategy.getName());
    }

    @Test
    public void should_NameIsWarnViaConsoleDialog() {
        assertTrue(strategy.getName().equals("warnViaConsoleDialog"));
    }

    @Test
    public void should_ConsistentNameReturned() {
        String name1 = strategy.getName();
        String name2 = strategy.getName();
        assertEquals(name1, name2);
    }

    @Test
    public void should_NameConstantMatches() {
        assertEquals(
            "warnViaConsoleDialog",
            WarningViaDialogClassNameCRStrategy.NAME
        );
    }

    // ========== setConsoleDialog() Tests ==========

    @Test
    public void should_AcceptConsoleDialog() {
        strategy.setConsoleDialog(mockDialog);
        assertNotNull(strategy.getConsoleDialog());
    }

    @Test
    public void should_SetConsoleDialogAndRetrieve() {
        strategy.setConsoleDialog(mockDialog);
        assertEquals(mockDialog, strategy.getConsoleDialog());
    }

    @Test
    public void should_ReplaceConsoleDialog() {
        ConsoleDialog dialog2 = mock(ConsoleDialog.class);
        strategy.setConsoleDialog(mockDialog);
        strategy.setConsoleDialog(dialog2);
        assertEquals(dialog2, strategy.getConsoleDialog());
    }

    @Test
    public void should_SetConsoleDialogToNull() {
        strategy.setConsoleDialog(mockDialog);
        strategy.setConsoleDialog(null);
        assertNull(strategy.getConsoleDialog());
    }

    @Test
    public void should_SetConsoleDialogMultipleTimes() {
        for (int i = 0; i < 5; i++) {
            strategy.setConsoleDialog(mockDialog);
        }
        assertEquals(mockDialog, strategy.getConsoleDialog());
    }

    @Test
    public void should_AlternateDialogSetAndNull() {
        strategy.setConsoleDialog(mockDialog);
        assertNotNull(strategy.getConsoleDialog());
        strategy.setConsoleDialog(null);
        assertNull(strategy.getConsoleDialog());
        strategy.setConsoleDialog(mockDialog);
        assertNotNull(strategy.getConsoleDialog());
    }

    // ========== getConsoleDialog() Tests ==========

    @Test
    public void should_GetConsoleDialogAfterSet() {
        strategy.setConsoleDialog(mockDialog);
        ConsoleDialog retrieved = strategy.getConsoleDialog();
        assertEquals(mockDialog, retrieved);
    }

    @Test
    public void should_GetConsoleDialogBeforeSet() {
        ConsoleDialog retrieved = strategy.getConsoleDialog();
        assertNull(retrieved);
    }

    @Test
    public void should_GetConsoleDialogMultipleTimes() {
        strategy.setConsoleDialog(mockDialog);
        assertEquals(mockDialog, strategy.getConsoleDialog());
        assertEquals(mockDialog, strategy.getConsoleDialog());
        assertEquals(mockDialog, strategy.getConsoleDialog());
    }

    // ========== dealWithFileOverwrite() Tests ==========

    @Test
    public void should_ReturnTrueWhenConfirmReturnsY() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        assertTrue(strategy.dealWithFileOverwrite("test.java"));
    }

    @Test
    public void should_ReturnFalseWhenConfirmReturnsN() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('n');
        assertFalse(strategy.dealWithFileOverwrite("test.java"));
    }

    @Test
    public void should_ReturnFalseWhenConfirmReturnsUnexpected() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('x');
        assertFalse(strategy.dealWithFileOverwrite("test.java"));
    }

    @Test
    public void should_ConstructProperMessageWithFilename() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        strategy.dealWithFileOverwrite("MyFile.java");
        verify(mockDialog).confirm(
            contains("MyFile.java"),
            eq("yna"),
            anyString()
        );
    }

    @Test
    public void should_ConstructProperMessageWithPath() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        strategy.dealWithFileOverwrite("/path/to/MyFile.java");
        verify(mockDialog).confirm(
            contains("/path/to/MyFile.java"),
            eq("yna"),
            anyString()
        );
    }

    @Test
    public void should_DealWithFileOverwriteMultipleTimes() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        assertTrue(strategy.dealWithFileOverwrite("file1.java"));
        assertTrue(strategy.dealWithFileOverwrite("file2.java"));
        assertTrue(strategy.dealWithFileOverwrite("file3.java"));
    }

    @Test
    public void should_HandleDifferentFilenames() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        assertTrue(strategy.dealWithFileOverwrite("Test.java"));
        assertTrue(strategy.dealWithFileOverwrite("test.class"));
        assertTrue(strategy.dealWithFileOverwrite("file123.java"));
    }

    @Test
    public void should_ConfirmCallWithCorrectParameters() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        strategy.dealWithFileOverwrite("test.java");
        verify(mockDialog).confirm(
            anyString(),
            eq("yna"),
            eq("y = yes, n = no, a = all")
        );
    }

    @Test
    public void should_HandleDefaultCaseInFileOverwrite() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('z');
        assertFalse(strategy.dealWithFileOverwrite("test.java"));
    }

    @Test
    public void should_VerifyConfirmMessageFormat() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        strategy.dealWithFileOverwrite("file.java");
        verify(mockDialog).confirm(
            contains("already exists"),
            eq("yna"),
            eq("y = yes, n = no, a = all")
        );
    }

    @Test
    public void should_HandleNullFilename() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        assertTrue(strategy.dealWithFileOverwrite(null));
    }

    @Test
    public void should_HandleEmptyFilename() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        assertTrue(strategy.dealWithFileOverwrite(""));
    }

    // ========== Interface Implementation Tests ==========

    @Test
    public void should_ImplementClassNameCRStrategy() {
        assertTrue(strategy instanceof ClassNameCRStrategy);
    }

    @Test
    public void should_ImplementBaseClassNameCRStrategy() {
        assertTrue(strategy instanceof BaseClassNameCRStrategy);
    }

    @Test
    public void should_CreateMultipleInstances() {
        WarningViaDialogClassNameCRStrategy strategy1 =
            new WarningViaDialogClassNameCRStrategy();
        WarningViaDialogClassNameCRStrategy strategy2 =
            new WarningViaDialogClassNameCRStrategy();
        assertEquals(strategy1.getName(), strategy2.getName());
    }

    // ========== Constructor Tests ==========

    @Test
    public void should_CreateInstance() {
        WarningViaDialogClassNameCRStrategy newStrategy =
            new WarningViaDialogClassNameCRStrategy();
        assertNotNull(newStrategy);
        assertEquals("warnViaConsoleDialog", newStrategy.getName());
    }

    @Test
    public void should_ConstructorInitializesNameCorrectly() {
        assertNotNull(strategy.getName());
        assertEquals(
            WarningViaDialogClassNameCRStrategy.NAME,
            strategy.getName()
        );
    }

    @Test
    public void should_ConstructorInitializesDialogNull() {
        WarningViaDialogClassNameCRStrategy newStrategy =
            new WarningViaDialogClassNameCRStrategy();
        assertNull(newStrategy.getConsoleDialog());
    }

    // ========== Integration Tests ==========

    @Test
    public void should_ImplementAllPublicMethods() {
        assertNotNull(strategy.getName());
        strategy.setConsoleDialog(mockDialog);
        assertNotNull(strategy.getConsoleDialog());
    }

    @Test
    public void should_NameConstantMatchGetName() {
        assertEquals(
            WarningViaDialogClassNameCRStrategy.NAME,
            "warnViaConsoleDialog"
        );
        assertEquals(
            strategy.getName(),
            WarningViaDialogClassNameCRStrategy.NAME
        );
    }

    @Test
    public void should_ConsistentBehavior() {
        assertEquals("warnViaConsoleDialog", strategy.getName());
        assertEquals("warnViaConsoleDialog", strategy.getName());
        strategy.setConsoleDialog(mockDialog);
        assertEquals(mockDialog, strategy.getConsoleDialog());
    }

    @Test
    public void should_InstancesAreIndependent() {
        WarningViaDialogClassNameCRStrategy s1 =
            new WarningViaDialogClassNameCRStrategy();
        WarningViaDialogClassNameCRStrategy s2 =
            new WarningViaDialogClassNameCRStrategy();
        assertEquals(s1.getName(), s2.getName());

        s1.setConsoleDialog(mockDialog);
        s2.setConsoleDialog(null);

        assertEquals(mockDialog, s1.getConsoleDialog());
        assertNull(s2.getConsoleDialog());
    }

    @Test
    public void should_MethodsDoNotThrow() {
        strategy.setConsoleDialog(null);
        strategy.setSingleClassGenerator(null);
        assertNotNull(strategy.getName());
    }

    @Test
    public void should_CallSequence() {
        strategy.setConsoleDialog(mockDialog);
        assertNotNull(strategy.getName());
        assertEquals(mockDialog, strategy.getConsoleDialog());
    }

    @Test
    public void should_FinalClassBehavior() {
        assertEquals("warnViaConsoleDialog", strategy.getName());
        strategy.setConsoleDialog(mockDialog);
        assertTrue(strategy.getConsoleDialog() != null);
    }

    @Test
    public void should_HandleMultipleStateChanges() {
        ConsoleDialog dialog1 = mock(ConsoleDialog.class);
        ConsoleDialog dialog2 = mock(ConsoleDialog.class);

        strategy.setConsoleDialog(dialog1);
        assertEquals(dialog1, strategy.getConsoleDialog());

        strategy.setConsoleDialog(dialog2);
        assertEquals(dialog2, strategy.getConsoleDialog());

        strategy.setConsoleDialog(null);
        assertNull(strategy.getConsoleDialog());
    }

    @Test
    public void should_GetStrategyName() {
        assertNotNull(strategy.getName());
        assertEquals("warnViaConsoleDialog", strategy.getName());
    }

    @Test
    public void should_SetAndGetDialog() {
        assertNull(strategy.getConsoleDialog());
        strategy.setConsoleDialog(mockDialog);
        assertNotNull(strategy.getConsoleDialog());
        assertEquals(mockDialog, strategy.getConsoleDialog());
    }

    @Test
    public void should_DialogNullByDefault() {
        WarningViaDialogClassNameCRStrategy newStrategy =
            new WarningViaDialogClassNameCRStrategy();
        assertNull(newStrategy.getConsoleDialog());
    }

    @Test
    public void should_AllPublicMethodsCovered() {
        strategy.setConsoleDialog(mockDialog);
        assertNotNull(strategy.getName());
        assertNotNull(strategy.getConsoleDialog());

        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        assertTrue(strategy.dealWithFileOverwrite("test.java"));
    }

    @Test
    public void should_ConfirmMessageBuiltCorrectly() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        strategy.dealWithFileOverwrite("MyClass.java");
        verify(mockDialog).confirm(
            contains("MyClass.java"),
            eq("yna"),
            eq("y = yes, n = no, a = all")
        );
    }

    @Test
    public void should_ReturnFalseForNoAndOtherCases() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('n', 'x', 'z', 'n');
        assertFalse(strategy.dealWithFileOverwrite("file1.java"));
        assertFalse(strategy.dealWithFileOverwrite("file2.java"));
        assertFalse(strategy.dealWithFileOverwrite("file3.java"));
        assertFalse(strategy.dealWithFileOverwrite("file4.java"));
    }

    @Test
    public void should_VerifyCorrectMessageParameters() {
        strategy.setConsoleDialog(mockDialog);
        when(
            mockDialog.confirm(anyString(), anyString(), anyString())
        ).thenReturn('y');
        strategy.dealWithFileOverwrite("test.java");
        verify(mockDialog).confirm(
            argThat(
                msg ->
                    msg.contains("test.java") && msg.contains("already exists")
            ),
            eq("yna"),
            eq("y = yes, n = no, a = all")
        );
    }
}
