/*
 * Copyright 2007 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.castor.maven.xmlctf;

import java.io.File;
import java.util.Iterator;

import junit.framework.Test;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.castor.xmlctf.TestCaseAggregator;

/**
 * Abstract Maven Mojo that initialises the Junit test cases from xml
 * Subclasses implement the runJUnit method to provide the Runner (eg. text, swing, ..)
 *
 * @since 1.2
 * @requiresProject true
 * @requiresDependencyResolution runtime
 */
public abstract class AbstractTestSuiteMojo extends AbstractMojo {

	/**
	 * Property describing the root directory of the tests to be executed.
	 */
	private static final String TEST_ROOT_PROPERTY = "castor.xmlctf.root";

    /**
     * Target directory used for the testclasses.
     *
     * @parameter expression="./target/xmlctf"
     */
    private  String outputRoot;

    /**
     * The source directory of the tests.
     *
     * @parameter
     */
    private String testRoot;
    
    /**
     * Optional parameter to overwrite the absolute path to the java tools.jar
     * 
     * @parameter
     */
    private String pathToTools;
    
    /**
     * The project whose project files to create.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Starting Castor Mastertestsuite");

		// testRoot checks
		String testRootToUse = System.getProperty(TEST_ROOT_PROPERTY);
		if (testRootToUse ==  null) {
		    testRootToUse = testRoot;          
		}
		
		if (testRootToUse == null) {
 			throw new MojoExecutionException("No testroot found, please specify property -Dcastor.xmlctf.root");
		}

        if (testRootToUse.equals(".") || testRootToUse.equals("..")) {
            //-- convert relative directories "." and ".." to a Canonical path
            File tmp = new File(testRootToUse);
            try {
                testRootToUse = tmp.getCanonicalPath();
            } catch (java.io.IOException iox) {

            }
        } else if (testRootToUse.startsWith("./") || testRootToUse.startsWith(".\\")) {
            //-- Remove leading ./ or .\ -- URLClassLoader can't handle such file URLs
            testRoot = testRoot.substring(2);
        }

        File testRootFile = new File(testRootToUse);
        getLog().info("using testRoot: " + testRootFile.getAbsolutePath());
        
        if (!testRootFile.exists()) {
        	throw new MojoExecutionException("Root not found:" + testRoot);
        }

        // set classpath for testcompiler
        String classpath = "";
        if (pathToTools != null) {
            classpath += pathToTools + System.getProperty("path.separator");
;
        } else {
            classpath += System.getProperty("java.home") + "/lib/tools.jar";
            classpath += System.getProperty("path.separator");
            classpath += System.getProperty("java.home") + "/../lib/tools.jar";            
            classpath += System.getProperty("path.separator");
        }
        
        for (Iterator iter = project.getArtifacts().iterator(); iter.hasNext();) {
            classpath += ((Artifact) iter.next()).getFile().getAbsolutePath();
            classpath += System.getProperty("path.separator");
        }
                  
        // set system proerties f?r 
        System.setProperty("xmlctf.classpath.override",classpath);
        if (getLog().isDebugEnabled()) {
            System.setProperty(TestCaseAggregator.VERBOSE_PROPERTY, "true");
            System.setProperty(TestCaseAggregator.PRINT_STACK_TRACE, "true");
        }
        
		getLog().debug("classpath for sourcegenerator is:" + classpath);

        // run testCase
        runJUnit(new TestCaseAggregator(testRootFile, outputRoot).suite());
	}

	/**
	 * For subclasses to implement to provide a test runner.
	 * @param testSuite The TestSuite to be executed
	 * @throws MojoExecutionException If test execution fails.
	 */
	public abstract void runJUnit(Test testSuite )throws MojoExecutionException;

}
