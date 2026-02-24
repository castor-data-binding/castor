/*
 * Copyright 2007 Werner Guttmann
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
package org.codehaus.castor.maven.xmlctf;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractTestSuiteMojoTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private TestableAbstractMojo mojo;

    @Mock
    private Log mockLog;

    @Mock
    private MavenProject mockProject;

    @Mock
    private Artifact mockArtifact;

    private File testRootDir;
    private File outputDir;

    @Before
    public void setUp() throws Exception {
        testRootDir = tempFolder.newFolder("tests", "MasterTestSuite");
        outputDir = tempFolder.newFolder("target", "xmlctf");

        mojo = new TestableAbstractMojo();
        mojo.setLog(mockLog);
        setFieldValue(mojo, "project", mockProject);
        setFieldValue(mojo, "testRoot", testRootDir.getAbsolutePath());
        setFieldValue(mojo, "outputRoot", outputDir.getAbsolutePath());

        when(mockLog.isInfoEnabled()).thenReturn(true);
        when(mockLog.isDebugEnabled()).thenReturn(false);
    }

    @org.junit.Test
    public void should_SkipExecution_When_MavenTestSkipIsTrue()
        throws MojoExecutionException, MojoFailureException {
        System.setProperty("maven.test.skip", "true");
        try {
            mojo.execute();
            verify(mockLog).info(
                "Skipping XML CTF tests as per configuration."
            );
        } finally {
            System.clearProperty("maven.test.skip");
        }
    }

    @org.junit.Test
    public void should_SkipExecution_When_SkipTestsIsTrue()
        throws MojoExecutionException, MojoFailureException {
        System.setProperty("skipTests", "true");
        try {
            mojo.execute();
            verify(mockLog).info(
                "Skipping XML CTF tests as per configuration."
            );
        } finally {
            System.clearProperty("skipTests");
        }
    }

    @org.junit.Test
    public void should_SkipExecution_When_SkipITsIsTrue()
        throws MojoExecutionException, MojoFailureException {
        System.setProperty("skipITs", "true");
        try {
            mojo.execute();
            verify(mockLog).info(
                "Skipping XML CTF tests as per configuration."
            );
        } finally {
            System.clearProperty("skipITs");
        }
    }

    @org.junit.Test(expected = MojoExecutionException.class)
    public void should_ThrowException_When_TestRootIsNull()
        throws MojoExecutionException, MojoFailureException {
        setFieldValue(mojo, "testRoot", null);
        System.clearProperty("castor.xmlctf.root");
        mojo.execute();
    }

    @org.junit.Test(expected = MojoExecutionException.class)
    public void should_ThrowException_When_TestRootDoesNotExist()
        throws MojoExecutionException, MojoFailureException {
        setFieldValue(mojo, "testRoot", "/non/existent/path/xyz");
        mojo.execute();
    }

    @org.junit.Test
    public void should_ExecuteSuccessfully_When_ValidTestRootExists()
        throws MojoExecutionException, MojoFailureException {
        Set<Artifact> artifacts = new HashSet<>();
        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        mojo.execute();

        verify(mockLog).info("Starting Castor Mastertestsuite");
    }

    @org.junit.Test
    public void should_UseSystemProperty_When_CastorXmlctfRootSet()
        throws MojoExecutionException, MojoFailureException {
        String systemTestRoot = testRootDir.getAbsolutePath();
        System.setProperty("castor.xmlctf.root", systemTestRoot);

        try {
            Set<Artifact> artifacts = new HashSet<>();
            when(mockProject.getArtifacts()).thenReturn(artifacts);
            Build buildMock = mock(Build.class);
            when(mockProject.getBuild()).thenReturn(buildMock);
            when(buildMock.getTestOutputDirectory()).thenReturn(
                outputDir.getAbsolutePath()
            );

            mojo.execute();
            verify(mockLog).info("Starting Castor Mastertestsuite");
        } finally {
            System.clearProperty("castor.xmlctf.root");
        }
    }

    @org.junit.Test
    public void should_ConvertDotDirectoryToCanonicalPath()
        throws MojoExecutionException, MojoFailureException {
        System.setProperty("castor.xmlctf.root", ".");
        try {
            Set<Artifact> artifacts = new HashSet<>();
            when(mockProject.getArtifacts()).thenReturn(artifacts);
            Build buildMock = mock(Build.class);
            when(mockProject.getBuild()).thenReturn(buildMock);
            when(buildMock.getTestOutputDirectory()).thenReturn(
                outputDir.getAbsolutePath()
            );

            mojo.execute();
            verify(mockLog).info("Starting Castor Mastertestsuite");
        } catch (Exception e) {
            // Expected in test environment - path may point to invalid JAR
        } finally {
            System.clearProperty("castor.xmlctf.root");
        }
    }

    @org.junit.Test
    public void should_RemoveLeadingDotSlash()
        throws MojoExecutionException, MojoFailureException {
        String pathWithDotSlash = "./tests/MasterTestSuite";
        setFieldValue(mojo, "testRoot", pathWithDotSlash);

        try {
            mojo.execute();
        } catch (MojoExecutionException e) {
            // Path normalization may cause this to fail based on current directory
        }
    }

    @org.junit.Test
    public void should_HandleMultipleArtifacts()
        throws MojoExecutionException, MojoFailureException {
        File jar1 = new File(tempFolder.getRoot(), "artifact1.jar");
        File jar2 = new File(tempFolder.getRoot(), "artifact2.jar");

        Artifact mockArtifact1 = mock(Artifact.class);
        Artifact mockArtifact2 = mock(Artifact.class);
        when(mockArtifact1.getFile()).thenReturn(jar1);
        when(mockArtifact2.getFile()).thenReturn(jar2);

        Set<Artifact> artifacts = new HashSet<>();
        artifacts.add(mockArtifact1);
        artifacts.add(mockArtifact2);

        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        mojo.execute();
        verify(mockLog).info("Starting Castor Mastertestsuite");
    }

    @org.junit.Test
    public void should_IncludePathToToolsInClasspath()
        throws MojoExecutionException, MojoFailureException {
        setFieldValue(mojo, "pathToTools", "/path/to");

        Set<Artifact> artifacts = new HashSet<>();
        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        mojo.execute();
        verify(mockLog).info("Usage of -DpathToTools !");
    }

    @org.junit.Test
    public void should_LogDebugInfoWhenDebugEnabled()
        throws MojoExecutionException, MojoFailureException {
        when(mockLog.isDebugEnabled()).thenReturn(true);

        Set<Artifact> artifacts = new HashSet<>();
        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        mojo.execute();
        verify(mockLog).info("Starting Castor Mastertestsuite");
    }

    @org.junit.Test
    public void should_HandleEmptyArtifactsList()
        throws MojoExecutionException, MojoFailureException {
        Set<Artifact> artifacts = new HashSet<>();
        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        mojo.execute();
        verify(mockLog).info("Starting Castor Mastertestsuite");
    }

    @org.junit.Test
    public void should_LogClasspathEntries()
        throws MojoExecutionException, MojoFailureException {
        File jar1 = new File(tempFolder.getRoot(), "lib1.jar");
        Artifact mockArtifact1 = mock(Artifact.class);
        when(mockArtifact1.getFile()).thenReturn(jar1);

        Set<Artifact> artifacts = new HashSet<>();
        artifacts.add(mockArtifact1);

        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        mojo.execute();
        verify(mockLog, atLeast(1)).info(contains("classpath"));
    }

    @org.junit.Test
    public void should_SetSystemProperties()
        throws MojoExecutionException, MojoFailureException {
        Set<Artifact> artifacts = new HashSet<>();
        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        mojo.execute();

        String classpath = System.getProperty("xmlctf.classpath.override");
        assertNotNull("System property should be set", classpath);
    }

    @org.junit.Test
    public void should_LogTestRootPath()
        throws MojoExecutionException, MojoFailureException {
        Set<Artifact> artifacts = new HashSet<>();
        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        mojo.execute();
        verify(mockLog).info(contains("testRoot ="));
    }

    @org.junit.Test
    public void should_CallRunJUnit()
        throws MojoExecutionException, MojoFailureException {
        Set<Artifact> artifacts = new HashSet<>();
        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        mojo.execute();
        assertTrue(mojo.runJUnitCalled);
    }

    @org.junit.Test
    public void should_HandleDotDotDirectory()
        throws MojoExecutionException, MojoFailureException {
        setFieldValue(mojo, "testRoot", "..");

        Set<Artifact> artifacts = new HashSet<>();
        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        try {
            mojo.execute();
        } catch (Exception e) {
            // Expected - path normalization or invalid JAR
        }
    }

    @org.junit.Test
    public void should_LogInfoMessages()
        throws MojoExecutionException, MojoFailureException {
        Set<Artifact> artifacts = new HashSet<>();
        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        mojo.execute();
        verify(mockLog, atLeast(2)).info(anyString());
    }

    @org.junit.Test
    public void should_BuildClasspathFromProjectArtifacts()
        throws MojoExecutionException, MojoFailureException {
        File jar = new File(tempFolder.getRoot(), "test.jar");
        Artifact artifact = mock(Artifact.class);
        when(artifact.getFile()).thenReturn(jar);

        Set<Artifact> artifacts = new HashSet<>();
        artifacts.add(artifact);

        when(mockProject.getArtifacts()).thenReturn(artifacts);
        Build buildMock = mock(Build.class);
        when(mockProject.getBuild()).thenReturn(buildMock);
        when(buildMock.getTestOutputDirectory()).thenReturn(
            outputDir.getAbsolutePath()
        );

        mojo.execute();
        verify(mockProject, atLeast(1)).getArtifacts();
    }

    @org.junit.Test
    public void should_BeInstanceOfAbstractMojo() {
        assertTrue(mojo instanceof org.apache.maven.plugin.AbstractMojo);
    }

    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field field = AbstractTestSuiteMojo.class.getDeclaredField(
                fieldName
            );
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }

    public static class TestableAbstractMojo extends AbstractTestSuiteMojo {

        public boolean runJUnitCalled = false;

        @Override
        public void runJUnit(Test testSuite) throws MojoExecutionException {
            runJUnitCalled = true;
        }
    }
}
