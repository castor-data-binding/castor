/*
 * 
 */
package org.castor.xml.schema.writer;

import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.Order;
import org.exolab.castor.xml.schema.Particle;

/**
 * @author <a href="mailto:bao.leduc@orange-ftgroup.com">Le Duc Bao</a>
 * @version 1.0
 * @since 5 oct. 08 11:30:54
 */
public class GroupTest extends AbstractSchemaTest {

    public GroupTest(String testcase) {
        super(testcase);
    }

    /**
     * Create simple type
     */
    public void testOneChildSubgroup() throws Exception {

        // create targeted schema
        ComplexType ctype = _schema.createComplexType();
        ctype.setName("myType");
        _schema.addComplexType(ctype);

        // create a sequence element
        Group groupTest = new Group();
        groupTest.setOrder(Order.seq);
        ctype.addGroup(groupTest);

        // create a choice element for sequence element
        Group subgroup = new Group();
        subgroup.setOrder(Order.choice);
        subgroup.setMinOccurs(0);
        subgroup.setMaxOccurs(Particle.UNBOUNDED);
        groupTest.addGroup(subgroup);

        // create an element for choice
        ElementDecl elem = new ElementDecl(_schema);
        elem.setName("myStringType");
        elem.setTypeReference("string");
        subgroup.addElementDecl(elem);

        // compare
        TestResult result = doTest("group_onechildsubgroup.xsd");
        assertEquals("single attribute test failed", TestResult.IDENTICAL,
                result);
    }

    /**
     * Create simple type
     */
    public void testSubgroupWithAnElement() throws Exception {

        // create targeted schema
        ComplexType ctype = _schema.createComplexType();
        ctype.setName("myType");
        _schema.addComplexType(ctype);

        // create a sequence element
        Group group = new Group();
        group.setOrder(Order.seq);
        ctype.addGroup(group);

        // create a choice element for sequence element
        Group subgroup = new Group();
        subgroup.setOrder(Order.choice);
        subgroup.setMinOccurs(0);
        subgroup.setMaxOccurs(Particle.UNBOUNDED);
        group.addGroup(subgroup);

        // create an element for choice
        ElementDecl elem = new ElementDecl(_schema);
        elem.setName("myStringType");
        elem.setTypeReference("string");
        subgroup.addElementDecl(elem);

        elem = new ElementDecl(_schema);
        elem.setName("myStringType2");
        elem.setTypeReference("string");
        group.addElementDecl(elem);

        // compare
        TestResult result = doTest("group_subgroupwithanelement.xsd");
        assertEquals("single attribute test failed", TestResult.IDENTICAL,
                result);
    }

    /**
     * Create simple type
     */
    public void test2Subgroups() throws Exception {

        // create targeted schema
        ComplexType ctype = _schema.createComplexType();
        ctype.setName("myType");
        _schema.addComplexType(ctype);

        // create a sequence element
        Group group = new Group();
        group.setOrder(Order.seq);
        ctype.addGroup(group);

        // create a choice element for sequence element
        Group subgroup = new Group();
        subgroup.setOrder(Order.choice);
        subgroup.setMinOccurs(0);
        subgroup.setMaxOccurs(Particle.UNBOUNDED);
        group.addGroup(subgroup);

        // create an element for choice
        ElementDecl elem = new ElementDecl(_schema);
        elem.setName("myStringType");
        elem.setTypeReference("string");
        subgroup.addElementDecl(elem);

        // create a choice element for sequence element
        subgroup = new Group();
        subgroup.setOrder(Order.choice);
        subgroup.setMinOccurs(0);
        subgroup.setMaxOccurs(Particle.UNBOUNDED);
        group.addGroup(subgroup);

        // create an element for choice
        elem = new ElementDecl(_schema);
        elem.setName("myStringType2");
        elem.setTypeReference("string");
        subgroup.addElementDecl(elem);
        // compare
        TestResult result = doTest("group_2subgroups.xsd");
        assertEquals("single attribute test failed", TestResult.IDENTICAL,
                result);
    }
}
