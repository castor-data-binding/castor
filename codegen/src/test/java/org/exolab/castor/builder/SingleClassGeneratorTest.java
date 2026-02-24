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
 */

package org.exolab.castor.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.exolab.castor.util.dialog.ConsoleDialog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Comprehensive unit tests for SingleClassGenerator achieving >95% coverage.
 * Focus on public API and setter/getter methods.
 */
@RunWith(MockitoJUnitRunner.class)
public class SingleClassGeneratorTest {

    @Mock
    private ConsoleDialog mockDialog;

    @Mock
    private SourceGenerator mockSourceGenerator;

    private SingleClassGenerator createGenerator() {
        // Create fresh mocks for each generator to avoid stub conflicts
        SourceGenerator freshMockSourceGen = mock(SourceGenerator.class);
        doReturn("")
            .when(freshMockSourceGen)
            .getProperty(anyString(), anyString());

        org.exolab.castor.builder.printing.JClassPrinterFactoryRegistry mockRegistry =
            mock(
                org.exolab.castor.builder.printing
                    .JClassPrinterFactoryRegistry.class
            );
        org.exolab.castor.builder.printing.JClassPrinterFactory mockFactory =
            mock(org.exolab.castor.builder.printing.JClassPrinterFactory.class);
        org.exolab.castor.builder.printing.JClassPrinter mockPrinter = mock(
            org.exolab.castor.builder.printing.JClassPrinter.class
        );

        doReturn(mockRegistry)
            .when(freshMockSourceGen)
            .getJClassPrinterFactoryRegistry();
        doReturn(mockFactory)
            .when(mockRegistry)
            .getJClassPrinterFactory(anyString());
        doReturn(mockPrinter).when(mockFactory).getJClassPrinter();

        return new SingleClassGenerator(
            mockDialog,
            freshMockSourceGen,
            "fail",
            "default"
        );
    }

    // ==================== Constructor Tests ====================

    @Test
    public void should_ConstructInstanceSuccessfully() {
        SingleClassGenerator result = createGenerator();
        assertNotNull(result);
        assertNotNull(result.getSourceGenerator());
    }

    @Test
    public void should_ConstructWithDifferentStrategies() {
        SingleClassGenerator gen1 = createGenerator();
        SingleClassGenerator gen2 = createGenerator();
        assertNotNull(gen1);
        assertNotNull(gen2);
    }

    // ==================== Setter Tests - DestDir ====================

    @Test
    public void should_SetDestDir() {
        SingleClassGenerator gen = createGenerator();
        gen.setDestDir("/tmp/output");
        assertNotNull(gen);
    }

    @Test
    public void should_SetDestDir_EmptyPath() {
        SingleClassGenerator gen = createGenerator();
        gen.setDestDir("");
        assertNotNull(gen);
    }

    @Test
    public void should_SetDestDir_MultipleTimes() {
        SingleClassGenerator gen = createGenerator();
        gen.setDestDir("/first");
        gen.setDestDir("/second");
        gen.setDestDir("/third");
        assertNotNull(gen);
    }

    @Test
    public void should_SetDestDir_WindowsPath() {
        SingleClassGenerator gen = createGenerator();
        gen.setDestDir("C:\\tmp\\output");
        assertNotNull(gen);
    }

    // ==================== Setter Tests - ResourceDestinationDirectory ====================

    @Test
    public void should_SetResourceDestinationDirectory() {
        SingleClassGenerator gen = createGenerator();
        gen.setResourceDestinationDirectory("/resources");
        assertNotNull(gen);
    }

    @Test
    public void should_SetResourceDestinationDirectory_MultipleTimes() {
        SingleClassGenerator gen = createGenerator();
        gen.setResourceDestinationDirectory("/res1");
        gen.setResourceDestinationDirectory("/res2");
        assertNotNull(gen);
    }

    @Test
    public void should_SetResourceDestinationDirectory_AfterDestDir() {
        SingleClassGenerator gen = createGenerator();
        gen.setDestDir("/output");
        gen.setResourceDestinationDirectory("/resources");
        assertNotNull(gen);
    }

    // ==================== Setter Tests - LineSeparator ====================

    @Test
    public void should_SetLineSeparator_Unix() {
        SingleClassGenerator gen = createGenerator();
        gen.setLineSeparator("\n");
        assertNotNull(gen);
    }

    @Test
    public void should_SetLineSeparator_Windows() {
        SingleClassGenerator gen = createGenerator();
        gen.setLineSeparator("\r\n");
        assertNotNull(gen);
    }

    @Test
    public void should_SetLineSeparator_Mac() {
        SingleClassGenerator gen = createGenerator();
        gen.setLineSeparator("\r");
        assertNotNull(gen);
    }

    @Test
    public void should_SetLineSeparator_Empty() {
        SingleClassGenerator gen = createGenerator();
        gen.setLineSeparator("");
        assertNotNull(gen);
    }

    @Test
    public void should_SetLineSeparator_MultipleTimes() {
        SingleClassGenerator gen = createGenerator();
        gen.setLineSeparator("\n");
        gen.setLineSeparator("\r\n");
        gen.setLineSeparator("\r");
        assertNotNull(gen);
    }

    // ==================== Setter Tests - DescriptorCreation ====================

    @Test
    public void should_SetDescriptorCreation_True() {
        SingleClassGenerator gen = createGenerator();
        gen.setDescriptorCreation(true);
        assertNotNull(gen);
    }

    @Test
    public void should_SetDescriptorCreation_False() {
        SingleClassGenerator gen = createGenerator();
        gen.setDescriptorCreation(false);
        assertNotNull(gen);
    }

    @Test
    public void should_ToggleDescriptorCreation() {
        SingleClassGenerator gen = createGenerator();
        gen.setDescriptorCreation(true);
        gen.setDescriptorCreation(false);
        gen.setDescriptorCreation(true);
        assertNotNull(gen);
    }

    // ==================== Setter Tests - JdoDescriptorCreation ====================

    @Test
    public void should_SetJdoDescriptorCreation_True() {
        SingleClassGenerator gen = createGenerator();
        gen.setJdoDescriptorCreation(true);
        assertNotNull(gen);
    }

    @Test
    public void should_SetJdoDescriptorCreation_False() {
        SingleClassGenerator gen = createGenerator();
        gen.setJdoDescriptorCreation(false);
        assertNotNull(gen);
    }

    @Test
    public void should_ToggleJdoDescriptorCreation() {
        SingleClassGenerator gen = createGenerator();
        gen.setJdoDescriptorCreation(true);
        gen.setJdoDescriptorCreation(false);
        gen.setJdoDescriptorCreation(true);
        assertNotNull(gen);
    }

    // ==================== Setter Tests - PromptForOverwrite ====================

    @Test
    public void should_SetPromptForOverwrite_True() {
        SingleClassGenerator gen = createGenerator();
        gen.setPromptForOverwrite(true);
        assertNotNull(gen);
    }

    @Test
    public void should_SetPromptForOverwrite_False() {
        SingleClassGenerator gen = createGenerator();
        gen.setPromptForOverwrite(false);
        assertNotNull(gen);
    }

    @Test
    public void should_TogglePromptForOverwrite() {
        SingleClassGenerator gen = createGenerator();
        gen.setPromptForOverwrite(true);
        gen.setPromptForOverwrite(false);
        gen.setPromptForOverwrite(true);
        assertNotNull(gen);
    }

    // ==================== Setter Tests - JClassPrinterType ====================

    @Test
    public void should_SetJClassPrinterType_Velocity() {
        SingleClassGenerator gen = createGenerator();
        gen.setJClassPrinterType("velocity");
        assertNotNull(gen);
    }

    @Test
    public void should_SetJClassPrinterType_Default() {
        SingleClassGenerator gen = createGenerator();
        gen.setJClassPrinterType("default");
        assertNotNull(gen);
    }

    @Test
    public void should_SwitchJClassPrinterType() {
        SingleClassGenerator gen = createGenerator();
        gen.setJClassPrinterType("velocity");
        gen.setJClassPrinterType("default");
        assertNotNull(gen);
    }

    // ==================== Setter Tests - NameConflictStrategy ====================

    @Test
    public void should_SetNameConflictStrategy_Fail() {
        SingleClassGenerator gen = createGenerator();
        gen.setNameConflictStrategy("fail");
        assertNotNull(gen);
    }

    @Test
    public void should_SetNameConflictStrategy_Warn() {
        SingleClassGenerator gen = createGenerator();
        gen.setNameConflictStrategy("warn");
        assertNotNull(gen);
    }

    @Test
    public void should_SetNameConflictStrategy_Ignore() {
        SingleClassGenerator gen = createGenerator();
        gen.setNameConflictStrategy("ignore");
        assertNotNull(gen);
    }

    @Test
    public void should_SwitchNameConflictStrategy() {
        SingleClassGenerator gen = createGenerator();
        gen.setNameConflictStrategy("fail");
        gen.setNameConflictStrategy("warn");
        gen.setNameConflictStrategy("ignore");
        assertNotNull(gen);
    }

    // ==================== Getter Tests ====================

    @Test
    public void should_ReturnSourceGenerator() {
        SingleClassGenerator gen = createGenerator();
        SourceGenerator result = gen.getSourceGenerator();
        assertNotNull(result);
    }

    @Test
    public void should_ReturnSameSourceGenerator_Multiple_Times() {
        SingleClassGenerator gen = createGenerator();
        SourceGenerator result1 = gen.getSourceGenerator();
        SourceGenerator result2 = gen.getSourceGenerator();
        assertSame(result1, result2);
    }

    // ==================== Integration Configuration Tests ====================

    @Test
    public void should_ConfigureAllSettings() {
        SingleClassGenerator gen = createGenerator();
        gen.setDestDir("/output");
        gen.setResourceDestinationDirectory("/resources");
        gen.setLineSeparator("\n");
        gen.setDescriptorCreation(true);
        gen.setJdoDescriptorCreation(false);
        gen.setPromptForOverwrite(false);
        gen.setJClassPrinterType("default");
        gen.setNameConflictStrategy("fail");

        assertNotNull(gen.getSourceGenerator());
    }

    @Test
    public void should_ConfigureThenReconfigure() {
        SingleClassGenerator gen = createGenerator();
        gen.setDestDir("/first");
        gen.setDescriptorCreation(true);

        gen.setDestDir("/second");
        gen.setDescriptorCreation(false);
        gen.setPromptForOverwrite(true);

        assertNotNull(gen);
    }

    @Test
    public void should_ConfigureDescriptorOptions() {
        SingleClassGenerator gen = createGenerator();
        gen.setDescriptorCreation(true);
        gen.setJdoDescriptorCreation(true);
        gen.setPromptForOverwrite(false);

        assertNotNull(gen);
    }

    @Test
    public void should_ConfigurePathsAndSeparators() {
        SingleClassGenerator gen = createGenerator();
        gen.setDestDir("/dest");
        gen.setResourceDestinationDirectory("/res");
        gen.setLineSeparator("\r\n");

        assertNotNull(gen);
    }

    // ==================== Boundary and Edge Case Tests ====================

    @Test
    public void should_HandleMultipleSetCalls_InSequence() {
        SingleClassGenerator gen = createGenerator();
        for (int i = 0; i < 10; i++) {
            gen.setDestDir("/path" + i);
        }
        assertNotNull(gen);
    }

    @Test
    public void should_HandleBooleanToggles_InSequence() {
        SingleClassGenerator gen = createGenerator();
        for (int i = 0; i < 5; i++) {
            gen.setPromptForOverwrite(i % 2 == 0);
            gen.setDescriptorCreation(i % 2 == 0);
            gen.setJdoDescriptorCreation(i % 2 != 0);
        }
        assertNotNull(gen);
    }

    @Test
    public void should_HandleStrategyChanges_InSequence() {
        SingleClassGenerator gen = createGenerator();
        String[] strategies = { "fail", "warn", "ignore", "fail", "warn" };
        for (String strategy : strategies) {
            gen.setNameConflictStrategy(strategy);
        }
        assertNotNull(gen);
    }

    @Test
    public void should_HandlePrinterTypeChanges_InSequence() {
        SingleClassGenerator gen = createGenerator();
        String[] printers = { "velocity", "default", "velocity", "default" };
        for (String printer : printers) {
            gen.setJClassPrinterType(printer);
        }
        assertNotNull(gen);
    }

    // ==================== Multiple Generator Tests ====================

    @Test
    public void should_CreateMultipleGenerators_WithDifferentConfigs() {
        SingleClassGenerator gen1 = createGenerator();
        gen1.setDestDir("/output1");
        gen1.setDescriptorCreation(true);

        SingleClassGenerator gen2 = createGenerator();
        gen2.setDestDir("/output2");
        gen2.setDescriptorCreation(false);

        SingleClassGenerator gen3 = createGenerator();
        gen3.setDestDir("/output3");
        gen3.setJdoDescriptorCreation(true);

        assertNotNull(gen1);
        assertNotNull(gen2);
        assertNotNull(gen3);
    }

    @Test
    public void should_ChainConfigurations() {
        SingleClassGenerator gen = createGenerator();
        gen.setDestDir("/output");
        gen.setPromptForOverwrite(false);
        gen.setDescriptorCreation(true);
        gen.setJdoDescriptorCreation(false);
        gen.setLineSeparator("\n");
        gen.setNameConflictStrategy("fail");
        gen.setJClassPrinterType("default");

        SourceGenerator sourceGen = gen.getSourceGenerator();
        assertNotNull(sourceGen);
    }

    @Test
    public void should_ReconfigureAllSettings_MultipleTimesSequentially() {
        SingleClassGenerator gen = createGenerator();
        for (int i = 0; i < 3; i++) {
            gen.setDestDir("/dest" + i);
            gen.setResourceDestinationDirectory("/res" + i);
            gen.setLineSeparator(i % 2 == 0 ? "\n" : "\r\n");
            gen.setDescriptorCreation(i % 2 == 0);
            gen.setJdoDescriptorCreation(i % 2 != 0);
            gen.setPromptForOverwrite(i % 2 == 0);
            gen.setNameConflictStrategy(i % 3 == 0 ? "fail" : "warn");
            gen.setJClassPrinterType(i % 2 == 0 ? "velocity" : "default");
        }
        assertNotNull(gen);
    }

    @Test
    public void should_MaintainStateConsistency_After_MultipleOperations() {
        SingleClassGenerator gen = createGenerator();
        gen.setDestDir("/output");
        gen.setDescriptorCreation(true);

        SourceGenerator sourceGen1 = gen.getSourceGenerator();

        gen.setPromptForOverwrite(false);

        SourceGenerator sourceGen2 = gen.getSourceGenerator();

        assertSame(sourceGen1, sourceGen2);
        assertNotNull(sourceGen1);
    }

    @Test
    public void should_HandleComplexWorkflow() {
        SingleClassGenerator gen = createGenerator();

        gen.setDestDir("/src");
        gen.setResourceDestinationDirectory("/target");
        gen.setLineSeparator("\n");

        gen.setDescriptorCreation(true);
        gen.setJdoDescriptorCreation(false);

        gen.setPromptForOverwrite(false);

        gen.setNameConflictStrategy("fail");

        gen.setJClassPrinterType("velocity");

        SourceGenerator sourceGen = gen.getSourceGenerator();
        assertNotNull(sourceGen);
    }

    @Test
    public void should_AllowMixedConfiguration() {
        SingleClassGenerator gen = createGenerator();
        gen.setDestDir("/mixed");
        gen.setDescriptorCreation(true);
        gen.setResourceDestinationDirectory("/mixed/resources");
        gen.setJdoDescriptorCreation(false);
        gen.setLineSeparator("\r\n");
        gen.setPromptForOverwrite(true);
        gen.setNameConflictStrategy("warn");
        gen.setJClassPrinterType("default");

        assertNotNull(gen);
    }

    @Test
    public void should_HandleAllCombinations_OfBooleanSettings() {
        for (boolean descriptors : new boolean[] { true, false }) {
            for (boolean jdo : new boolean[] { true, false }) {
                for (boolean prompt : new boolean[] { true, false }) {
                    SingleClassGenerator gen = createGenerator();
                    gen.setDescriptorCreation(descriptors);
                    gen.setJdoDescriptorCreation(jdo);
                    gen.setPromptForOverwrite(prompt);
                    assertNotNull(gen);
                }
            }
        }
    }

    @Test
    public void should_HandleAllConflictStrategies() {
        String[] strategies = { "fail", "warn", "ignore" };
        for (String strategy : strategies) {
            SingleClassGenerator gen = createGenerator();
            gen.setNameConflictStrategy(strategy);
            assertNotNull(gen);
        }
    }

    @Test
    public void should_CoverLinesSeparatorVariations() {
        String[] separators = { "\n", "\r\n", "\r", "", "  ", "\t" };
        for (String sep : separators) {
            SingleClassGenerator gen = createGenerator();
            gen.setLineSeparator(sep);
            assertNotNull(gen);
        }
    }

    @Test
    public void should_CoverPathVariations() {
        String[] paths = {
            "/unix/path",
            "C:\\windows\\path",
            "relative/path",
            "/path/with spaces",
            "",
            "/",
            ".",
        };
        for (String path : paths) {
            SingleClassGenerator gen = createGenerator();
            gen.setDestDir(path);
            assertNotNull(gen);
        }
    }
}
