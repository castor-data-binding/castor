package org.exolab.castor.xml.dtd;

import java.util.Iterator;
import junit.framework.TestCase;

public class AttributeTest extends TestCase {

    private Element element;
    private DTDdocument document;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        document = new DTDdocument();
        element = new Element(document, "testElement");
    }

    public void testAttributeConstructor() {
        Attribute attr = new Attribute(element, "id");
        assertNotNull(attr);
        assertEquals("id", attr.getName());
        assertEquals(element, attr.getElement());
    }

    public void testAttributeConstructorNullElement() {
        try {
            new Attribute(null, "id");
            fail("Should throw IllegalArgumentException for null element");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("element must not be null"));
        }
    }

    public void testAttributeConstructorNullName() {
        try {
            new Attribute(element, null);
            fail("Should throw IllegalArgumentException for null name");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("name must not be empty"));
        }
    }

    public void testAttributeConstructorEmptyName() {
        try {
            new Attribute(element, "");
            fail("Should throw IllegalArgumentException for empty name");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("name must not be empty"));
        }
    }

    public void testGetName() {
        Attribute attr = new Attribute(element, "myattr");
        assertEquals("myattr", attr.getName());
    }

    public void testGetElement() {
        Attribute attr = new Attribute(element, "attr");
        assertEquals(element, attr.getElement());
    }

    public void testSetStringType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setStringType();
        assertTrue(attr.isStringType());
        assertFalse(attr.isIDType());
    }

    public void testSetIDType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setIDType();
        assertTrue(attr.isIDType());
        assertFalse(attr.isStringType());
    }

    public void testSetIDREFType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setIDREFType();
        assertTrue(attr.isIDREFType());
        assertFalse(attr.isIDType());
    }

    public void testSetIDREFSType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setIDREFSType();
        assertTrue(attr.isIDREFSType());
        assertFalse(attr.isIDREFType());
    }

    public void testSetENTITYType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setENTITYType();
        assertTrue(attr.isENTITYType());
        assertFalse(attr.isStringType());
    }

    public void testSetENTITIESType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setENTITIESType();
        assertTrue(attr.isENTITIESType());
        assertFalse(attr.isENTITYType());
    }

    public void testSetNMTOKENType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setNMTOKENType();
        assertTrue(attr.isNMTOKENType());
        assertFalse(attr.isStringType());
    }

    public void testSetNMTOKENSType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setNMTOKENSType();
        assertTrue(attr.isNMTOKENSType());
        assertFalse(attr.isNMTOKENType());
    }

    public void testSetNOTATIONType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setNOTATIONType();
        assertTrue(attr.isNOTATIONType());
        assertFalse(attr.isStringType());
    }

    public void testSetEnumerationType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setEnumerationType();
        assertTrue(attr.isEnumerationType());
        assertFalse(attr.isNOTATIONType());
    }

    public void testSetDEFAULT() {
        Attribute attr = new Attribute(element, "attr");
        attr.setDEFAULT();
        assertTrue(attr.isDEFAULT());
        assertFalse(attr.isREQUIRED());
        assertFalse(attr.isIMPLIED());
        assertFalse(attr.isFIXED());
    }

    public void testSetREQUIRED() {
        Attribute attr = new Attribute(element, "attr");
        attr.setREQUIRED();
        assertTrue(attr.isREQUIRED());
        assertFalse(attr.isDEFAULT());
        assertFalse(attr.isIMPLIED());
        assertFalse(attr.isFIXED());
    }

    public void testSetIMPLIED() {
        Attribute attr = new Attribute(element, "attr");
        attr.setIMPLIED();
        assertTrue(attr.isIMPLIED());
        assertFalse(attr.isDEFAULT());
        assertFalse(attr.isREQUIRED());
        assertFalse(attr.isFIXED());
    }

    public void testSetFIXED() {
        Attribute attr = new Attribute(element, "attr");
        attr.setFIXED();
        assertTrue(attr.isFIXED());
        assertFalse(attr.isDEFAULT());
        assertFalse(attr.isREQUIRED());
        assertFalse(attr.isIMPLIED());
    }

    public void testDefaultValueNull() {
        Attribute attr = new Attribute(element, "attr");
        assertNull(attr.getDefaultValue());
    }

    public void testSetDefaultValue() {
        Attribute attr = new Attribute(element, "attr");
        attr.setDefaultValue("defaultVal");
        assertEquals("defaultVal", attr.getDefaultValue());
    }

    public void testSetDefaultValueMultipleTimes() {
        Attribute attr = new Attribute(element, "attr");
        attr.setDefaultValue("val1");
        assertEquals("val1", attr.getDefaultValue());
        attr.setDefaultValue("val2");
        assertEquals("val2", attr.getDefaultValue());
    }

    public void testAddValue() throws DTDException {
        Attribute attr = new Attribute(element, "attr");
        attr.setEnumerationType();
        attr.addValue("value1");
        Iterator<String> values = attr.getValues();
        assertNotNull(values);
        assertTrue(values.hasNext());
        assertEquals("value1", values.next());
    }

    public void testAddMultipleValues() throws DTDException {
        Attribute attr = new Attribute(element, "attr");
        attr.setEnumerationType();
        attr.addValue("value1");
        attr.addValue("value2");
        attr.addValue("value3");
        Iterator<String> values = attr.getValues();
        assertNotNull(values);
        int count = 0;
        while (values.hasNext()) {
            values.next();
            count++;
        }
        assertEquals(3, count);
    }

    public void testAddDuplicateValue() throws DTDException {
        Attribute attr = new Attribute(element, "attr");
        attr.setEnumerationType();
        attr.addValue("value1");
        try {
            attr.addValue("value1");
            fail("Should throw DTDException for duplicate value");
        } catch (DTDException e) {
            assertTrue(e.getMessage().contains("already contained"));
        }
    }

    public void testGetValuesForNonEnumerationType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setStringType();
        assertNull(attr.getValues());
    }

    public void testGetValuesForStringType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setStringType();
        assertNull(attr.getValues());
    }

    public void testGetValuesForNOTATIONType() throws DTDException {
        Attribute attr = new Attribute(element, "attr");
        attr.setNOTATIONType();
        attr.addValue("notation1");
        Iterator<String> values = attr.getValues();
        assertNotNull(values);
        assertTrue(values.hasNext());
    }

    public void testDefaultOccurranceType() {
        Attribute attr = new Attribute(element, "attr");
        assertTrue(attr.isDEFAULT());
    }

    public void testChangeOccurranceType() {
        Attribute attr = new Attribute(element, "attr");
        attr.setREQUIRED();
        assertTrue(attr.isREQUIRED());
        attr.setIMPLIED();
        assertTrue(attr.isIMPLIED());
        attr.setFIXED();
        assertTrue(attr.isFIXED());
        attr.setDEFAULT();
        assertTrue(attr.isDEFAULT());
    }

    public void testMultipleAttributesSameElement() throws DTDException {
        Attribute attr1 = new Attribute(element, "attr1");
        Attribute attr2 = new Attribute(element, "attr2");
        attr1.setStringType();
        attr2.setIDType();
        assertTrue(attr1.isStringType());
        assertTrue(attr2.isIDType());
    }

    public void testAttributeNameWithSpecialChars() {
        Attribute attr = new Attribute(element, "my-attr_123");
        assertEquals("my-attr_123", attr.getName());
    }

    public void testAttributeTypeTransitions() {
        Attribute attr = new Attribute(element, "attr");
        attr.setStringType();
        assertTrue(attr.isStringType());
        attr.setIDType();
        assertTrue(attr.isIDType());
        assertFalse(attr.isStringType());
        attr.setNMTOKENType();
        assertTrue(attr.isNMTOKENType());
        assertFalse(attr.isIDType());
    }

    public void testGetValuesEmptyEnumeration() {
        Attribute attr = new Attribute(element, "attr");
        attr.setEnumerationType();
        Iterator<String> values = attr.getValues();
        assertNotNull(values);
        assertFalse(values.hasNext());
    }

    public void testAddValueWithSpaces() throws DTDException {
        Attribute attr = new Attribute(element, "attr");
        attr.setEnumerationType();
        attr.addValue("value with spaces");
        Iterator<String> values = attr.getValues();
        assertNotNull(values);
        assertTrue(values.hasNext());
        assertEquals("value with spaces", values.next());
    }

    public void testAttributeWithAllTypes() {
        Attribute[] attrs = new Attribute[10];
        attrs[0] = new Attribute(element, "a1");
        attrs[0].setStringType();
        attrs[1] = new Attribute(element, "a2");
        attrs[1].setIDType();
        attrs[2] = new Attribute(element, "a3");
        attrs[2].setIDREFType();
        attrs[3] = new Attribute(element, "a4");
        attrs[3].setIDREFSType();
        attrs[4] = new Attribute(element, "a5");
        attrs[4].setENTITYType();
        attrs[5] = new Attribute(element, "a6");
        attrs[5].setENTITIESType();
        attrs[6] = new Attribute(element, "a7");
        attrs[6].setNMTOKENType();
        attrs[7] = new Attribute(element, "a8");
        attrs[7].setNMTOKENSType();
        attrs[8] = new Attribute(element, "a9");
        attrs[8].setNOTATIONType();
        attrs[9] = new Attribute(element, "a10");
        attrs[9].setEnumerationType();

        assertTrue(attrs[0].isStringType());
        assertTrue(attrs[1].isIDType());
        assertTrue(attrs[2].isIDREFType());
        assertTrue(attrs[3].isIDREFSType());
        assertTrue(attrs[4].isENTITYType());
        assertTrue(attrs[5].isENTITIESType());
        assertTrue(attrs[6].isNMTOKENType());
        assertTrue(attrs[7].isNMTOKENSType());
        assertTrue(attrs[8].isNOTATIONType());
        assertTrue(attrs[9].isEnumerationType());
    }
}
