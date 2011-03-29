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
package org.castor.xml.schema.namespace;

import static org.junit.Assert.assertEquals;

import org.castor.xml.schema.ComparisResultExtractor;
import org.castor.xml.schema.ComparisonResult;
import org.exolab.castor.xml.schema.Schema;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Namespace tests.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 */
public final class NamespaceTest {

    /**
     * Create a schema with single namespace
     * 
     * @throws Exception
     */
    @Test
    public void testSingleNamespace() throws Exception {

        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("myprefix", "my.namespace.org");

        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("namespace_singlenamespace.xsd"));
        assertEquals("single namespace add failed", ComparisonResult.IDENTICAL,
                result);
    }

    /**
     * Create a schema with single namespace
     * @throws Exception 
     */
    @Ignore
    public void testDiff() throws Exception {

        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("wrong", "my.namespace.org");

        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("namespace_singlenamespace.xsd"));
        assertEquals("test diff", ComparisonResult.DIFFERENCE, result);
    }

    /**
     * Test for multiple namespaces
     * @throws Exception 
     */
    @Test
    public void testMultipleNamespace() throws Exception {

        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("myprefix", "my.namespace.org");
        schema.addNamespace("other", "other.namespace.org");

        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("namespace_multiplenamespace.xsd"));
        assertEquals("multiple namespace add failed", ComparisonResult.IDENTICAL,
                result);
    }
}
