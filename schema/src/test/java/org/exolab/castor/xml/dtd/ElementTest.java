package org.exolab.castor.xml.dtd;

import junit.framework.TestCase;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Comprehensive test coverage for DTD Element class
 */
public class ElementTest extends TestCase {

    private DTDdocument document;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        document = new DTDdocument();
    }

    // Constructor Tests
    public void testConstructorWithNameAndDocument() {
        Element elem = new Element(document, "testElement");
        assertNotNull(elem);
        assertEquals("testElement", elem.getName());
        assertSame(document, elem.getDocument());
    }

    public void testConstructorWithDocumentOnly() {
        Element elem = new Element(document);
        assertNotNull(elem);
        assertNull(elem.getName());
        assertSame(document, elem.getDocument());
    }

    public void testConstructorWithNullDocument() {
        try {
            new Element(null, "testElement");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("document must not be null"));
        }
    }

    public void testConstructorWithNullDocumentNoName() {
        try {
            new Element(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("document must not be null"));
        }
    }

    // Accessor Tests
    public void testGetName() {
        Element elem = new Element(document, "myElement");
        assertEquals("myElement", elem.getName());
    }

    public void testGetDocument() {
        Element elem = new Element(document, "myElement");
        assertSame(document, elem.getDocument());
    }

    public void testSetName() {
        Element elem = new Element(document);
        elem.setName("newName");
        assertEquals("newName", elem.getName());
    }

    // Content Type Tests - ANY
    public void testSetAnyContent() {
        Element elem = new Element(document, "elem");
        elem.setAnyContent();
        assertTrue(elem.isAnyContent());
    }

    public void testIsAnyContentFalse() {
        Element elem = new Element(document, "elem");
        assertFalse(elem.isAnyContent());
    }

    // Content Type Tests - EMPTY
    public void testSetEmptyContent() {
        Element elem = new Element(document, "elem");
        elem.setEmptyContent();
        assertTrue(elem.isEmptyContent());
        assertFalse(elem.isAnyContent());
    }

    public void testIsEmptyContentFalse() {
        Element elem = new Element(document, "elem");
        assertFalse(elem.isEmptyContent());
    }

    // Content Type Tests - MIXED
    public void testSetMixedContent() {
        Element elem = new Element(document, "elem");
        elem.setMixedContent();
        assertTrue(elem.isMixedContent());
        assertFalse(elem.isEmptyContent());
    }

    public void testIsMixedContentFalse() {
        Element elem = new Element(document, "elem");
        assertFalse(elem.isMixedContent());
    }

    public void testMixedContentInitializes() {
        Element elem = new Element(document, "elem");
        elem.setMixedContent();
        Iterator<String> children = elem.getMixedContentChildren();
        assertNotNull(children);
        assertFalse(children.hasNext());
    }

    // Content Type Tests - ELEMENTS_ONLY
    public void testSetElemOnlyContent() {
        Element elem = new Element(document, "elem");
        ContentParticle cp = new ContentParticle();
        elem.setElemOnlyContent(cp);
        assertTrue(elem.isElemOnlyContent());
        assertFalse(elem.isMixedContent());
    }

    public void testIsElemOnlyContentFalse() {
        Element elem = new Element(document, "elem");
        assertFalse(elem.isElemOnlyContent());
    }

    public void testGetContentForElemOnly() {
        Element elem = new Element(document, "elem");
        ContentParticle cp = new ContentParticle();
        elem.setElemOnlyContent(cp);
        assertSame(cp, elem.getContent());
    }

    public void testGetContentForNonElemOnly() {
        Element elem = new Element(document, "elem");
        elem.setAnyContent();
        assertNull(elem.getContent());
    }

    // Mixed Content Children Tests
    public void testAddMixedContentChild() throws DTDException {
        Element elem = new Element(document, "elem");
        elem.setMixedContent();
        elem.addMixedContentChild("child1");

        Iterator<String> children = elem.getMixedContentChildren();
        assertNotNull(children);
        assertTrue(children.hasNext());
        assertEquals("child1", children.next());
    }

    public void testAddMultipleMixedContentChildren() throws DTDException {
        Element elem = new Element(document, "elem");
        elem.setMixedContent();
        elem.addMixedContentChild("child1");
        elem.addMixedContentChild("child2");
        elem.addMixedContentChild("child3");

        Iterator<String> children = elem.getMixedContentChildren();
        int count = 0;
        while (children.hasNext()) {
            children.next();
            count++;
        }
        assertEquals(3, count);
    }

    public void testAddDuplicateMixedContentChild() throws DTDException {
        Element elem = new Element(document, "elem");
        elem.setMixedContent();
        elem.addMixedContentChild("child1");

        try {
            elem.addMixedContentChild("child1");
            fail("Should throw DTDException");
        } catch (DTDException e) {
            assertTrue(e.getMessage().contains("already contains"));
        }
    }

    public void testGetMixedContentChildrenNonMixed() {
        Element elem = new Element(document, "elem");
        elem.setAnyContent();
        assertNull(elem.getMixedContentChildren());
    }

    // Attribute Tests
    public void testAddAttribute() {
        Element elem = new Element(document, "elem");
        Attribute attr = new Attribute(elem, "attr1");
        elem.addAttribute(attr);

        Enumeration<Attribute> attrs = elem.getAttributes();
        assertNotNull(attrs);
        assertTrue(attrs.hasMoreElements());
        assertEquals(attr, attrs.nextElement());
    }

    public void testAddMultipleAttributes() {
        Element elem = new Element(document, "elem");
        Attribute attr1 = new Attribute(elem, "attr1");
        Attribute attr2 = new Attribute(elem, "attr2");
        Attribute attr3 = new Attribute(elem, "attr3");

        elem.addAttribute(attr1);
        elem.addAttribute(attr2);
        elem.addAttribute(attr3);

        Enumeration<Attribute> attrs = elem.getAttributes();
        int count = 0;
        while (attrs.hasMoreElements()) {
            attrs.nextElement();
            count++;
        }
        assertEquals(3, count);
    }

    public void testAddDuplicateAttribute() {
        Element elem = new Element(document, "elem");
        Attribute attr1 = new Attribute(elem, "attr1");
        Attribute attr2 = new Attribute(elem, "attr1");

        elem.addAttribute(attr1);
        elem.addAttribute(attr2);

        Enumeration<Attribute> attrs = elem.getAttributes();
        int count = 0;
        while (attrs.hasMoreElements()) {
            attrs.nextElement();
            count++;
        }
        assertEquals(1, count);
    }

    public void testGetAttributesEmpty() {
        Element elem = new Element(document, "elem");
        Enumeration<Attribute> attrs = elem.getAttributes();
        assertNotNull(attrs);
        assertFalse(attrs.hasMoreElements());
    }

    // Content Type Transitions
    public void testContentTypeTransition() {
        Element elem = new Element(document, "elem");
        elem.setAnyContent();
        assertTrue(elem.isAnyContent());

        elem.setEmptyContent();
        assertTrue(elem.isEmptyContent());
        assertFalse(elem.isAnyContent());

        elem.setMixedContent();
        assertTrue(elem.isMixedContent());
        assertFalse(elem.isEmptyContent());
    }

    // Complex Scenarios
    public void testElementWithAnyContentAndAttributes() {
        Element elem = new Element(document, "elem");
        elem.setAnyContent();

        Attribute attr1 = new Attribute(elem, "id");
        Attribute attr2 = new Attribute(elem, "class");

        elem.addAttribute(attr1);
        elem.addAttribute(attr2);

        assertTrue(elem.isAnyContent());

        Enumeration<Attribute> attrs = elem.getAttributes();
        int count = 0;
        while (attrs.hasMoreElements()) {
            attrs.nextElement();
            count++;
        }
        assertEquals(2, count);
    }

    public void testElementWithEmptyContentAndAttributes() {
        Element elem = new Element(document, "elem");
        elem.setEmptyContent();

        Attribute attr = new Attribute(elem, "id");
        elem.addAttribute(attr);

        assertTrue(elem.isEmptyContent());
        assertNull(elem.getContent());

        Enumeration<Attribute> attrs = elem.getAttributes();
        assertTrue(attrs.hasMoreElements());
    }

    public void testElementWithMixedContentAndChildren() throws DTDException {
        Element elem = new Element(document, "elem");
        elem.setMixedContent();

        elem.addMixedContentChild("span");
        elem.addMixedContentChild("b");
        elem.addMixedContentChild("i");

        assertTrue(elem.isMixedContent());

        Iterator<String> children = elem.getMixedContentChildren();
        int count = 0;
        while (children.hasNext()) {
            children.next();
            count++;
        }
        assertEquals(3, count);
    }

    public void testElementWithElemOnlyContent() {
        Element elem = new Element(document, "elem");
        ContentParticle cp = new ContentParticle();
        elem.setElemOnlyContent(cp);

        Attribute attr1 = new Attribute(elem, "id");
        elem.addAttribute(attr1);

        assertTrue(elem.isElemOnlyContent());
        assertNotNull(elem.getContent());

        Enumeration<Attribute> attrs = elem.getAttributes();
        assertTrue(attrs.hasMoreElements());
    }

    public void testMultipleElementsInDocument() throws DTDException {
        Element elem1 = new Element(document, "div");
        Element elem2 = new Element(document, "span");
        Element elem3 = new Element(document, "p");

        elem1.setMixedContent();
        elem2.setAnyContent();
        elem3.setEmptyContent();

        elem1.addMixedContentChild("span");
        elem1.addMixedContentChild("p");

        Attribute attr1 = new Attribute(elem1, "class");
        elem1.addAttribute(attr1);

        assertEquals("div", elem1.getName());
        assertEquals("span", elem2.getName());
        assertEquals("p", elem3.getName());
        assertTrue(elem1.isMixedContent());
        assertTrue(elem2.isAnyContent());
        assertTrue(elem3.isEmptyContent());
    }

    public void testElementSetNameMultipleTimes() {
        Element elem = new Element(document, "oldName");
        assertEquals("oldName", elem.getName());

        elem.setName("newName");
        assertEquals("newName", elem.getName());

        elem.setName("anotherName");
        assertEquals("anotherName", elem.getName());
    }

    public void testGetMixedContentChildrenNull() {
        Element elem = new Element(document, "elem");
        elem.setMixedContent();
        assertNull(elem.getContent());
    }

    public void testGetContentNull() {
        Element elem = new Element(document, "elem");
        elem.setMixedContent();
        assertNull(elem.getContent());
    }

    public void testAttributeAccessAfterMultipleAdds() {
        Element elem = new Element(document, "elem");

        Attribute attr1 = new Attribute(elem, "id");
        Attribute attr2 = new Attribute(elem, "class");
        Attribute attr3 = new Attribute(elem, "style");

        elem.addAttribute(attr1);
        elem.addAttribute(attr2);
        elem.addAttribute(attr3);
        elem.addAttribute(attr1);  // Duplicate

        Enumeration<Attribute> attrs = elem.getAttributes();
        int count = 0;
        while (attrs.hasMoreElements()) {
            attrs.nextElement();
            count++;
        }
        assertEquals(3, count);
    }

    public void testElementWithContentAndChildren() throws DTDException {
        Element elem = new Element(document, "mixed");
        elem.setMixedContent();
        elem.addMixedContentChild("em");
        elem.addMixedContentChild("strong");

        assertTrue(elem.isMixedContent());
        Iterator<String> children = elem.getMixedContentChildren();
        assertNotNull(children);

        int childCount = 0;
        while (children.hasNext()) {
            children.next();
            childCount++;
        }
        assertEquals(2, childCount);
    }

}
