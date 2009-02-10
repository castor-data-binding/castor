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

import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;

/**
 * This test covers simple type generation.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 */
public class SimpleTypeTest extends AbstractSchemaTest {

    /**
     * @param Constructor
     */
    public SimpleTypeTest(String testcase) {
        super(testcase);
    }

    /**
     * very simple type
     */
    public void testSimpleType() throws Exception  {

        // create targeted schema
        _schema.addNamespace("pre", "my.namespace.org");
        SimpleType sType = _schema.createSimpleType("myType", "string", "");

        _schema.addSimpleType(sType);

        // compare
        TestResult result = doTest("simpletype_simple.xsd");
        assertEquals("single attribute test failed", TestResult.IDENTICAL,
                result);
    }

    /**
     * test create attribute, fixed value
     */
    public void testAttributeCreation() throws Exception  {

        // create targeted schema
        _schema.addNamespace("pre", "my.namespace.org");

        AttributeDecl attr = new AttributeDecl(_schema);
        attr.setName("myAttr");
        attr.setSimpleTypeReference("string");
        attr.setFixedValue("#hello");
        attr.setUse(AttributeDecl.USE_OPTIONAL);
        _schema.addAttribute(attr);

        // compare
        TestResult result = doTest("simpletype_attributecreation.xsd");
        assertEquals("testAttributeCreation test failed", TestResult.IDENTICAL,
                result);
    }

    /**
     * test create attribute
     */
    public void testAttributeCreation2() throws Exception  {

        // create targeted schema
        _schema.addNamespace("pre", "my.namespace.org");

        AttributeDecl attr = new AttributeDecl(_schema);
        attr.setName("myAttr");
        attr.setSimpleTypeReference("string");
        attr.setDefaultValue("hello");
        attr.setUse(AttributeDecl.USE_PROHIBITED);

        _schema.addAttribute(attr);

        // compare
        TestResult result = doTest("simpletype_attributecreation2.xsd");
        assertEquals("testAttributeCreation2 test failed",
                TestResult.IDENTICAL, result);
    }

    /**
     * test create attribute, use required
     */
    public void testAttributeCreation3() throws Exception  {

        // create targeted schema
        _schema.addNamespace("pre", "my.namespace.org");

        AttributeDecl attr = new AttributeDecl(_schema);
        attr.setName("myAttr");
        attr.setSimpleTypeReference("string");
        attr.setDefaultValue("hello");
        attr.setUse(AttributeDecl.USE_REQUIRED);

        _schema.addAttribute(attr);

        // compare
        TestResult result = doTest("simpletype_attributecreation3.xsd");
        assertEquals("testAttributeCreation3 test failed",
                TestResult.IDENTICAL, result);
    }

    // restriction
    /**
     * test create facet/min-max
     */
    public void testMinMax() throws Exception  {

        // create targeted schema
        _schema.addNamespace("pre", "my.namespace.org");

        SimpleType sType = _schema.createSimpleType("myType", "int", "");

        Facet min = new Facet(Facet.MIN_EXCLUSIVE, "0");
        Facet max = new Facet(Facet.MAX_EXCLUSIVE, "100");
        sType.addFacet(min);
        sType.addFacet(max);

        _schema.addSimpleType(sType);

        // compare
        TestResult result = doTest("simpletype_res_minmax.xsd");
        assertEquals("testMinMax test failed", TestResult.IDENTICAL, result);
    }
    // min inclusive, max inclusive
    // leng, max length, min length
    // whiteSpace preserve, replace, collapse
    // enumeration
    // union
    // pattern
    // precision, total digits, fraction digits
}
