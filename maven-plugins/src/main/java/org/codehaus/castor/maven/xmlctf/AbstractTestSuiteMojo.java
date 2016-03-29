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

import java.io.File;
import java.util.Iterator;

import junit.framework.Test;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.castor.xmlctf.TestCaseAggregator;

/**
 * Abstract Maven Mojo that initialises the Junit test cases from xml Subclasses implement the
 * runJUnit method to provide the Runner (eg. text, swing, ..)
 * 
 * @since 1.2
 */
public abstract class AbstractTestSuiteMojo extends AbstractMojo {

  /**
   * Property describing the root directory of the tests to be executed.
   */
  private static final String TEST_ROOT_PROPERTY = "castor.xmlctf.root";

  /**
   * Target directory used for the testclasses.
   */
  @Parameter(property = "outputRoot", defaultValue = "./target/xmlctf")
  private String outputRoot;

  /**
   * The source directory of the tests.
   */
  @Parameter(property = "testRoot", defaultValue = "${basedir}/tests/MasterTestSuite/")
  private String testRoot;

  /**
   * Optional parameter to overwrite the absolute path to the java tools.jar
   */
  @Parameter(property = "pathToTools")
  private String pathToTools;

  /**
   * The Maven project whose project files to create.
   */
  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  public void execute() throws MojoExecutionException, MojoFailureException {

    boolean skipMavenTests = Boolean.getBoolean("maven.test.skip");
    skipMavenTests |= Boolean.getBoolean("skipTests");
    skipMavenTests |= Boolean.getBoolean("skipITs");

    if (skipMavenTests) {
      if (getLog().isInfoEnabled()) {
        getLog().info("Skipping XML CTF tests as per configuration.");
      }
      return;
    }

    getLog().info("Starting Castor Mastertestsuite");

    // testRoot checks
    getLog().info("testRoot = " + testRoot);
    String testRootToUse = System.getProperty(TEST_ROOT_PROPERTY);
    if (testRootToUse == null) {
      testRootToUse = testRoot;
    }

    if (testRootToUse == null) {
      throw new MojoExecutionException(
          "No testroot found, please specify property -Dcastor.xmlctf.root");
    }

    if (testRootToUse.equals(".") || testRootToUse.equals("..")) {
      // -- convert relative directories "." and ".." to a Canonical path
      File tmp = new File(testRootToUse);
      try {
        testRootToUse = tmp.getCanonicalPath();
      } catch (java.io.IOException iox) {

      }
    } else if (testRootToUse.startsWith("./") || testRootToUse.startsWith(".\\")) {
      // -- Remove leading ./ or .\ -- URLClassLoader can't handle such
      // file URLs
      testRoot = testRoot.substring(2);
    }

    File testRootFile = new File(testRootToUse);
    getLog().info("using testRoot: " + testRootFile.getAbsolutePath());

    if (!testRootFile.exists()) {
      throw new MojoExecutionException("Root not found:" + testRoot);
    }


    // set classpath for testcompiler
    // TODO
    String dirSeparator = System.getProperty("file.separator");
    StringBuilder classpath = new StringBuilder();
    String pathSeparator = System.getProperty("path.separator");
    for (@SuppressWarnings("unchecked")
    Iterator<Artifact> iter = project.getArtifacts().iterator(); iter.hasNext();) {
      classpath.append(((Artifact) iter.next()).getFile().getAbsolutePath());
      classpath.append(pathSeparator);
    }

    classpath.append(project.getBuild().getTestOutputDirectory());
    classpath.append(pathSeparator);

    if (pathToTools != null) {
      getLog().info("Usage of -DpathToTools !");
      classpath.append(pathToTools + pathSeparator + "tools.jar");
    } else {
      String javaHome = System.getProperty("java.home");
      classpath.append(javaHome);
      classpath.append(dirSeparator);
      classpath.append("lib");
      classpath.append(dirSeparator);
      classpath.append("tools.jar");
      classpath.append(pathSeparator);
      classpath.append(javaHome.substring(0, javaHome.lastIndexOf("/jre")) + dirSeparator + "lib"
          + dirSeparator + "tools.jar");
      classpath.append(pathSeparator);
    }

    // set system proerties for
    System.setProperty("xmlctf.classpath.override", classpath.toString());
    if (getLog().isDebugEnabled()) {
      System.setProperty(TestCaseAggregator.VERBOSE_PROPERTY, "true");
      System.setProperty(TestCaseAggregator.PRINT_STACK_TRACE, "true");
    }

    getLog().info("classpath for sourcegenerator is: " + classpath);
    String[] classpathEntries = StringUtils.split(classpath.toString(), ';');
    for (String classpathEntry : classpathEntries) {
      getLog().info(classpathEntry);
    }

    // run testCase
    TestCaseAggregator aggregator = new TestCaseAggregator(testRootFile, outputRoot);
    // aggregator.
    runJUnit(aggregator.suite());
  }

  /**
   * For subclasses to implement to provide a test runner.
   * 
   * @param testSuite The TestSuite to be executed
   * @throws MojoExecutionException If test execution fails.
   */
  public abstract void runJUnit(Test testSuite) throws MojoExecutionException;

}
