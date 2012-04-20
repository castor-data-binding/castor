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
package org.castor.xml.schema.complexType;

import static org.junit.Assert.assertEquals;

import org.castor.xml.schema.ComparisResultExtractor;
import org.castor.xml.schema.ComparisonResult;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.Order;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaNames;
import org.junit.Test;

/**
 * This test covers complex type generation.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 */
public class ComplexTypeTest {

    /**
     * Create simple type
     * 
     * @throws Exception
     */
    @Test
    public void testSingleAttribute() throws Exception {

        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("pre", "my.namespace.org");
        ComplexType cType = schema.createComplexType("myType");
        schema.addComplexType(cType);

        Group group = new Group();
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        // compare
        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("complextype_singleattribute.xsd"));
        assertEquals("single attribute test failed", ComparisonResult.IDENTICAL,
                result);
    }

    /**
     * sequence multiple attributes
     * 
     * @throws Exception
     */
    @Test
    public void testSequenceAttribute() throws Exception {
        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("pre", "my.namespace.org");
        ComplexType cType = schema.createComplexType("myType");
        schema.addComplexType(cType);

        Group group = new Group();
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        ElementDecl e2 = new ElementDecl(schema);
        e2.setName("myAttr2");
        group.addElementDecl(e2);

        ElementDecl e3 = new ElementDecl(schema);
        e3.setName("myAttr3");
        group.addElementDecl(e3);

        // compare
        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("complextype_sequenceattribute.xsd"));
        assertEquals("sequence multiple attributes test failed",
                ComparisonResult.IDENTICAL, result);
    }

    /**
     * un-order attributes
     * 
     * @throws Exception
     */
    @Test
    public void testAllOrderAttribute() throws Exception {
        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // TODO it seems the XMLDiff does not detect order of attributes
        // create targeted schema
        schema.addNamespace("pre", "my.namespace.org");
        ComplexType cType = schema.createComplexType("myType");
        schema.addComplexType(cType);

        Group group = new Group();
        group.setOrder(Order.all);
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        ElementDecl e2 = new ElementDecl(schema);
        e2.setName("myAttr2");
        group.addElementDecl(e2);

        ElementDecl e3 = new ElementDecl(schema);
        e3.setName("myAttr3");
        group.addElementDecl(e3);

        // compare
        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("complextype_allorder.xsd"));
        assertEquals("all order attributes test failed", ComparisonResult.IDENTICAL,
                result);
    }

    /**
     * choice group attributes
     * 
     * @throws Exception
     */
    @Test
    public void testChoiceAttribute() throws Exception {
        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // TODO it seems the XMLDiff does not detect order of attributes
        // create targeted schema
        schema.addNamespace("pre", "my.namespace.org");
        ComplexType cType = schema.createComplexType("myType");
        schema.addComplexType(cType);

        Group group = new Group();
        group.setOrder(Order.choice);
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        ElementDecl e2 = new ElementDecl(schema);
        e2.setName("myAttr2");
        group.addElementDecl(e2);

        ElementDecl e3 = new ElementDecl(schema);
        e3.setName("myAttr3");
        group.addElementDecl(e3);

        // compare
        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("complextype_choiceattribute.xsd"));
        assertEquals("choice group attributes test failed",
                ComparisonResult.IDENTICAL, result);
    }

    /**
     * extension generation test
     * 
     * @throws Exception
     */
    @Test
    public void testExtension() throws Exception {

        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("pre", "my.namespace.org");

        // create base type
        ComplexType cBaseType = schema.createComplexType("baseType");
        schema.addComplexType(cBaseType);

        Group gBase = new Group();
        cBaseType.addGroup(gBase);

        ElementDecl ebase = new ElementDecl(schema);
        ebase.setName("baseAttr");
        gBase.addElementDecl(ebase);

        // create dependency
        ComplexType cType = schema.createComplexType("myType");
        schema.addComplexType(cType);
        cType.setBaseType(cBaseType);
        cType.setDerivationMethod(SchemaNames.EXTENSION);

        Group group = new Group();
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        ElementDecl e2 = new ElementDecl(schema);
        e2.setName("myAttr2");
        group.addElementDecl(e2);

        ElementDecl e3 = new ElementDecl(schema);
        e3.setName("myAttr3");
        group.addElementDecl(e3);

        // compare
        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("complextype_extension.xsd"));
        assertEquals("create extension test failed", ComparisonResult.IDENTICAL,
                result);
    }

    /**
     * extension generation test
     * 
     * @throws Exception
     */
    @Test
    public void testCreateElementForComplexType() throws Exception {

        // create a new XML schema representation.
        Schema schema = new Schema();
        
        // create targeted schema
        schema.addNamespace("pre", "my.namespace.org");

        // create dependency
        ComplexType cType = schema.createComplexType("myType");
        schema.addComplexType(cType);

        Group group = new Group();
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        ElementDecl element = new ElementDecl(schema);
        element.setName("myElement");
        element.setTypeReference("myType");
        schema.addElementDecl(element);

        // compare
        ComparisonResult result = ComparisResultExtractor.doTest(schema, this.getClass().getResource("complextype_elementforcomplextype.xsd"));
        assertEquals("test create element for complexType test failed",
                ComparisonResult.IDENTICAL, result);
    }
}
