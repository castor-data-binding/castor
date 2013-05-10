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

import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.Order;
import org.exolab.castor.xml.schema.SchemaNames;

/**
 * This test covers complex type generation.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 */
public class ComplexTypeTest extends AbstractSchemaTest {

    /**
     * @param testcase
     */
    public ComplexTypeTest(String testcase) {
        super(testcase);
    }

    /**
     * Create simple type
     * 
     * @throws Exception
     */
    public void testSingleAttribute() throws Exception {
        // create targeted schema
        _schema.addNamespace("pre", "my.namespace.org");
        ComplexType cType = _schema.createComplexType("myType");
        _schema.addComplexType(cType);

        Group group = new Group();
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(_schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        // compare
        TestResult result = doTest("complextype_singleattribute.xsd");
        assertEquals("single attribute test failed", TestResult.IDENTICAL,
                result);
    }

    /**
     * sequence multiple attributes
     * 
     * @throws Exception
     */
    public void testSequenceAttribute() throws Exception {
        // create targeted schema
        _schema.addNamespace("pre", "my.namespace.org");
        ComplexType cType = _schema.createComplexType("myType");
        _schema.addComplexType(cType);

        Group group = new Group();
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(_schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        ElementDecl e2 = new ElementDecl(_schema);
        e2.setName("myAttr2");
        group.addElementDecl(e2);

        ElementDecl e3 = new ElementDecl(_schema);
        e3.setName("myAttr3");
        group.addElementDecl(e3);

        // compare
        TestResult result = doTest("complextype_sequenceattribute.xsd");
        assertEquals("sequence multiple attributes test failed",
                TestResult.IDENTICAL, result);
    }

    /**
     * un-order attributes
     * 
     * @throws Exception
     */
    public void testAllOrderAttribute() throws Exception {

        // TODO it seems the XMLDiff does not detect order of attributes
        // create targeted schema
        _schema.addNamespace("pre", "my.namespace.org");
        ComplexType cType = _schema.createComplexType("myType");
        _schema.addComplexType(cType);

        Group group = new Group();
        group.setOrder(Order.all);
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(_schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        ElementDecl e2 = new ElementDecl(_schema);
        e2.setName("myAttr2");
        group.addElementDecl(e2);

        ElementDecl e3 = new ElementDecl(_schema);
        e3.setName("myAttr3");
        group.addElementDecl(e3);

        // compare
        TestResult result = doTest("complextype_allorder.xsd");
        assertEquals("all order attributes test failed", TestResult.IDENTICAL,
                result);
    }

    /**
     * choice group attributes
     * 
     * @throws Exception
     */
    public void testChoiceAttribute() throws Exception {

        // TODO it seems the XMLDiff does not detect order of attributes
        // create targeted schema
        _schema.addNamespace("pre", "my.namespace.org");
        ComplexType cType = _schema.createComplexType("myType");
        _schema.addComplexType(cType);

        Group group = new Group();
        group.setOrder(Order.choice);
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(_schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        ElementDecl e2 = new ElementDecl(_schema);
        e2.setName("myAttr2");
        group.addElementDecl(e2);

        ElementDecl e3 = new ElementDecl(_schema);
        e3.setName("myAttr3");
        group.addElementDecl(e3);

        // compare
        TestResult result = doTest("complextype_choiceattribute.xsd");
        assertEquals("choice group attributes test failed",
                TestResult.IDENTICAL, result);
    }

    /**
     * extension generation test
     * 
     * @throws Exception
     */
    public void testExtension() throws Exception {
        // create targeted schema
        // create base type
        ComplexType cBaseType = _schema.createComplexType("baseType");
        _schema.addComplexType(cBaseType);

        Group gBase = new Group();
        cBaseType.addGroup(gBase);

        ElementDecl ebase = new ElementDecl(_schema);
        ebase.setName("baseAttr");
        gBase.addElementDecl(ebase);

        // create dependency
        ComplexType cType = _schema.createComplexType("myType");
        _schema.addComplexType(cType);
        cType.setBaseType(cBaseType);
        cType.setDerivationMethod(SchemaNames.EXTENSION);

        Group group = new Group();
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(_schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        ElementDecl e2 = new ElementDecl(_schema);
        e2.setName("myAttr2");
        group.addElementDecl(e2);

        // compare
        TestResult result = doTest("complextype_attributeorder.xsd");
        assertEquals("create extension test failed", TestResult.IDENTICAL,
                result);
    }

    /**
     * extension generation test
     * 
     * @throws Exception
     */
    public void testCreateElementForComplexType() throws Exception {

        // create targeted schema
        _schema.addNamespace("pre", "my.namespace.org");

        // create dependency
        ComplexType cType = _schema.createComplexType("myType");
        _schema.addComplexType(cType);

        Group group = new Group();
        cType.addGroup(group);

        ElementDecl e = new ElementDecl(_schema);
        e.setName("myAttr");
        group.addElementDecl(e);

        ElementDecl element = new ElementDecl(_schema);
        element.setName("myElement");
        element.setTypeReference("myType");
        _schema.addElementDecl(element);

        // compare
        TestResult result = doTest("complextype_elementforcomplextype.xsd");
        assertEquals("test create element for complexType test failed",
                TestResult.IDENTICAL, result);
    }
}
