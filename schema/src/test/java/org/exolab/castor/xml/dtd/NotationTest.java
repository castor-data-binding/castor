package org.exolab.castor.xml.dtd;

import junit.framework.TestCase;

/**
 * Comprehensive test coverage for DTD Notation class
 */
public class NotationTest extends TestCase {

    private DTDdocument document;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        document = new DTDdocument();
    }

    // Constructor Tests
    public void testConstructorWithNameAndDocument() {
        Notation notation = new Notation(document, "myNotation");
        assertNotNull(notation);
        assertEquals("myNotation", notation.getName());
        assertEquals(document, notation.getDocument());
    }

    public void testConstructorWithNullDocument() {
        try {
            new Notation(null, "notation");
            fail("Should throw IllegalArgumentException for null document");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("document must not be null"));
        }
    }

    public void testConstructorWithNullName() {
        try {
            new Notation(document, null);
            fail("Should throw IllegalArgumentException for null name");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("name must not be empty"));
        }
    }

    public void testConstructorWithEmptyName() {
        try {
            new Notation(document, "");
            fail("Should throw IllegalArgumentException for empty name");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("name must not be empty"));
        }
    }

    // Accessor Tests
    public void testGetName() {
        Notation notation = new Notation(document, "testNotation");
        assertEquals("testNotation", notation.getName());
    }

    public void testGetDocument() {
        Notation notation = new Notation(document, "notation");
        assertEquals(document, notation.getDocument());
    }

    // PUBLIC Type Tests
    public void testSetPublic() {
        Notation notation = new Notation(document, "notation");
        notation.setPublic("PUBLIC_ID", "SYSTEM_ID");
        assertTrue(notation.isPublic());
        assertEquals("PUBLIC_ID", notation.getPubIdentifier());
        assertEquals("SYSTEM_ID", notation.getSysIdentifier());
    }

    public void testSetPublicWithNullPubId() {
        Notation notation = new Notation(document, "notation");
        try {
            notation.setPublic(null, "http://example.com");
            fail("Should throw IllegalArgumentException for null public ID");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("can not set null public ID"));
        }
    }

    public void testSetPublicWithNullSysId() {
        Notation notation = new Notation(document, "notation");
        try {
            notation.setPublic("PUBLIC_ID", null);
            fail("Should throw IllegalArgumentException for null system ID");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("can not set null system ID"));
        }
    }

    public void testIsPublicFalse() {
        Notation notation = new Notation(document, "notation");
        assertFalse(notation.isPublic());
    }

    public void testGetPubIdentifierDefault() {
        Notation notation = new Notation(document, "notation");
        assertNull(notation.getPubIdentifier());
    }

    // SYSTEM Type Tests
    public void testSetSystem() {
        Notation notation = new Notation(document, "notation");
        notation.setSystem("SYSTEM_ID");
        assertTrue(notation.isSystem());
        assertEquals("SYSTEM_ID", notation.getSysIdentifier());
    }

    public void testSetSystemWithNullSysId() {
        Notation notation = new Notation(document, "notation");
        try {
            notation.setSystem(null);
            fail("Should throw IllegalArgumentException for null system ID");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("can not set null system ID"));
        }
    }

    public void testIsSystemFalse() {
        Notation notation = new Notation(document, "notation");
        assertFalse(notation.isSystem());
    }

    public void testGetSysIdentifierDefault() {
        Notation notation = new Notation(document, "notation");
        assertNull(notation.getSysIdentifier());
    }

    // Identifier Tests
    public void testGetSysIdentifierPublic() {
        Notation notation = new Notation(document, "notation");
        notation.setPublic("-//Example//Notation", "http://example.com");
        assertEquals("http://example.com", notation.getSysIdentifier());
    }

    public void testGetSysIdentifierSystem() {
        Notation notation = new Notation(document, "notation");
        notation.setSystem("file:///notation.txt");
        assertEquals("file:///notation.txt", notation.getSysIdentifier());
    }

    public void testGetPubIdentifierPublic() {
        Notation notation = new Notation(document, "notation");
        notation.setPublic("-//Example//Notation", "http://example.com");
        assertEquals("-//Example//Notation", notation.getPubIdentifier());
    }

    public void testGetPubIdentifierSystem() {
        Notation notation = new Notation(document, "notation");
        notation.setSystem("file:///notation.txt");
        assertNull(notation.getPubIdentifier());
    }

    // Complex Scenarios
    public void testPublicNotation() {
        Notation notation = new Notation(document, "xhtml");
        notation.setPublic(
            "-//W3C//NOTATION XHTML 1.0",
            "http://www.w3.org/TR/xhtml1/"
        );

        assertTrue(notation.isPublic());
        assertEquals("-//W3C//NOTATION XHTML 1.0", notation.getPubIdentifier());
        assertEquals(
            "http://www.w3.org/TR/xhtml1/",
            notation.getSysIdentifier()
        );
    }

    public void testSystemNotation() {
        Notation notation = new Notation(document, "local");
        notation.setSystem("file:///schemas/local.txt");

        assertTrue(notation.isSystem());
        assertEquals("file:///schemas/local.txt", notation.getSysIdentifier());
    }

    public void testMultipleNotationsInDocument() {
        Notation notation1 = new Notation(document, "notation1");
        Notation notation2 = new Notation(document, "notation2");

        notation1.setPublic("-//Example1", "http://example1.com");
        notation2.setSystem("file:///notation2.txt");

        assertTrue(notation1.isPublic());
        assertTrue(notation2.isSystem());
        assertEquals("notation1", notation1.getName());
        assertEquals("notation2", notation2.getName());
    }

    public void testNotationNameVariations() {
        String[] names = {
            "notation",
            "my-notation",
            "my_notation",
            "notation123",
            "NOTATION",
        };

        for (String name : names) {
            Notation notation = new Notation(document, name);
            assertEquals(name, notation.getName());
            assertEquals(document, notation.getDocument());
        }
    }

    public void testNotationTypeTransition() {
        Notation notation = new Notation(document, "notation");

        notation.setPublic("-//Example", "http://example.com");
        assertTrue(notation.isPublic());

        notation.setSystem("file:///notation.txt");
        assertTrue(notation.isSystem());
    }

    public void testPublicNotationWithComplexIdentifiers() {
        Notation notation = new Notation(document, "complex");
        String pubId =
            "-//ISO/IEC 15445:2000//NOTATION Unnamed SGML Document Type//EN";
        String sysId = "http://www.w3.org/TR/1999/REC-html401-19991224/";

        notation.setPublic(pubId, sysId);

        assertTrue(notation.isPublic());
        assertEquals(pubId, notation.getPubIdentifier());
        assertEquals(sysId, notation.getSysIdentifier());
    }

    public void testSystemNotationWithFileURL() {
        Notation notation = new Notation(document, "filenotation");
        String sysId = "file:///C:/schemas/notation.txt";

        notation.setSystem(sysId);

        assertTrue(notation.isSystem());
        assertEquals(sysId, notation.getSysIdentifier());
        assertNull(notation.getPubIdentifier());
    }

    public void testNotationAfterPublicSet() {
        Notation notation = new Notation(document, "test");
        notation.setPublic("PUBLIC", "SYSTEM");

        assertTrue(notation.isPublic());
        assertEquals("PUBLIC", notation.getPubIdentifier());
        assertEquals("SYSTEM", notation.getSysIdentifier());
    }

    public void testNotationAfterSystemSet() {
        Notation notation = new Notation(document, "test");
        notation.setSystem("SYSTEM");

        assertTrue(notation.isSystem());
        assertEquals("SYSTEM", notation.getSysIdentifier());
        assertNull(notation.getPubIdentifier());
    }

    public void testMultipleNotationsWithDifferentTypes() {
        Notation publicNotation = new Notation(document, "public");
        Notation systemNotation = new Notation(document, "system");

        publicNotation.setPublic("-//PUBLIC", "http://example.com");
        systemNotation.setSystem("file:///system.txt");

        assertTrue(publicNotation.isPublic());

        assertTrue(systemNotation.isSystem());

        assertEquals("-//PUBLIC", publicNotation.getPubIdentifier());
        assertEquals("http://example.com", publicNotation.getSysIdentifier());
        assertEquals("file:///system.txt", systemNotation.getSysIdentifier());
    }

    public void testNotationWithSpecialCharactersInName() {
        Notation notation = new Notation(document, "notation-with_special123");
        assertEquals("notation-with_special123", notation.getName());
    }

    public void testNotationWithURLIdentifiers() {
        Notation notation = new Notation(document, "web");
        notation.setPublic("-//Web", "http://example.com/notation");

        assertEquals(
            "http://example.com/notation",
            notation.getSysIdentifier()
        );
        assertEquals("-//Web", notation.getPubIdentifier());
    }

    public void testNotationWithFilePathIdentifiers() {
        Notation notation = new Notation(document, "local");
        notation.setSystem("\\\\server\\schemas\\notation.txt");

        assertEquals(
            "\\\\server\\schemas\\notation.txt",
            notation.getSysIdentifier()
        );
    }

    public void testGetNameAfterCreation() {
        Notation notation = new Notation(document, "finalName");
        assertEquals("finalName", notation.getName());
        assertEquals("finalName", notation.getName());
    }

    public void testGetDocumentAfterCreation() {
        Notation notation = new Notation(document, "test");
        assertSame(document, notation.getDocument());
    }
}
