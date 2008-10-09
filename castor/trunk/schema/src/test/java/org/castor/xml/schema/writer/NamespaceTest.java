/*
 * Copyright 2008 Le Duc Bao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.xml.schema.writer;

/**
 * Namespace tests.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 */
public final class NamespaceTest extends AbstractSchemaTest {
    public NamespaceTest(final String testcase) {
        super(testcase);
    }

    /**
     * Create a schema with single namespace
     */
    public void testSingleNamespace() throws Exception {

        // create targeted schema
        _schema.addNamespace("myprefix", "my.namespace.org");

        TestResult result = doTest("namespace_singlenamespace.xsd");
        assertEquals("single namespace add failed", TestResult.IDENTICAL,
                result);
    }

    /**
     * Test for multiple namespaces
     */
    public void testMultipleNamespace() throws Exception {

        // create targeted schema
        _schema.addNamespace("myprefix", "my.namespace.org");
        _schema.addNamespace("other", "other.namespace.org");

        TestResult result = doTest("namespace_multiplenamespace.xsd");
        assertEquals("multiple namespace add failed", TestResult.IDENTICAL,
                result);
    }
}
