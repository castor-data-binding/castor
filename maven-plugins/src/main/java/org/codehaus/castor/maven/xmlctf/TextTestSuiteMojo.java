/*
 * Copyright 2005 Werner Guttmann
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

import junit.framework.Test;
import junit.framework.TestResult;
import junit.textui.TestRunner;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Uses a simple junit.ui.TestRunner TextRunner to run the XMLCTF test suite.
 * 
 * @since 1.2
 */
@Mojo(name = "xmlctf-text", requiresDependencyResolution = ResolutionScope.RUNTIME, requiresProject = true)
public class TextTestSuiteMojo extends AbstractTestSuiteMojo {

    public void runJUnit(Test testSuite) throws MojoExecutionException {
        TestResult result = TestRunner.run(testSuite);

        if (result.errorCount() > 0 || result.failureCount() > 0) {
            throw new MojoExecutionException(
                    "Errors or Failures occured testing the MasterTestSuite!");
        }
    }

}
