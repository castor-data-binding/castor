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
package org.castor.xml.schema.simpleType;

import static org.junit.Assert.assertEquals;

import org.castor.xml.schema.ComparisResultExtractor;
import org.castor.xml.schema.ComparisonResult;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SimpleType;
import org.junit.Test;

/**
 * This test covers simple type generation.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 */
public class SimpleTypeTest {

    /**
     * very simple type
     * 
     * @throws Exception
     */
    @Test
    public void testSimpleType() throws Exception {

        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("pre", "my.namespace.org");
        SimpleType sType = schema.createSimpleType("myType", "string", "");

        schema.addSimpleType(sType);

        // compare
        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("simpletype_simple.xsd"));
        assertEquals("single attribute test failed", ComparisonResult.IDENTICAL,
                result);
    }

    /**
     * test create attribute, fixed value
     * 
     * @throws Exception
     */
    @Test
    public void testAttributeCreation() throws Exception {

        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("pre", "my.namespace.org");

        AttributeDecl attr = new AttributeDecl(schema);
        attr.setName("myAttr");
        attr.setSimpleTypeReference("string");
        attr.setFixedValue("#hello");
        attr.setUse(AttributeDecl.USE_OPTIONAL);
        schema.addAttribute(attr);

        // compare
        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("simpletype_attributecreation.xsd"));
        assertEquals("testAttributeCreation test failed", ComparisonResult.IDENTICAL,
                result);
    }

    /**
     * test create attribute
     * 
     * @throws Exception
     */
    public void testAttributeCreation2() throws Exception {

        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("pre", "my.namespace.org");

        AttributeDecl attr = new AttributeDecl(schema);
        attr.setName("myAttr");
        attr.setSimpleTypeReference("string");
        attr.setDefaultValue("hello");
        attr.setUse(AttributeDecl.USE_PROHIBITED);

        schema.addAttribute(attr);

        // compare
        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("simpletype_attributecreation2.xsd"));
        assertEquals("testAttributeCreation2 test failed",
                ComparisonResult.IDENTICAL, result);
    }

    /**
     * test create attribute, use required
     * 
     * @throws Exception
     */
    public void testAttributeCreation3() throws Exception {

        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("pre", "my.namespace.org");

        AttributeDecl attr = new AttributeDecl(schema);
        attr.setName("myAttr");
        attr.setSimpleTypeReference("string");
        attr.setDefaultValue("hello");
        attr.setUse(AttributeDecl.USE_REQUIRED);

        schema.addAttribute(attr);

        // compare
        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("simpletype_attributecreation3.xsd"));
        assertEquals("testAttributeCreation3 test failed",
                ComparisonResult.IDENTICAL, result);
    }

    // restriction
    /**
     * test create facet/min-max
     * @throws Exception 
     */
    public void testMinMax() throws Exception {

        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("pre", "my.namespace.org");

        SimpleType sType = schema.createSimpleType("myType", "int", "");

        Facet min = new Facet(Facet.MIN_EXCLUSIVE, "0");
        Facet max = new Facet(Facet.MAX_EXCLUSIVE, "100");
        sType.addFacet(min);
        sType.addFacet(max);

        schema.addSimpleType(sType);

        // compare
        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("simpletype_res_minmax.xsd"));
        assertEquals("testMinMax test failed", ComparisonResult.IDENTICAL, result);
    }
    // min inclusive, max inclusive
    // leng, max length, min length
    // whiteSpace preserve, replace, collapse
    // enumeration
    // union
    // pattern
    // precision, total digits, fraction digits
}
