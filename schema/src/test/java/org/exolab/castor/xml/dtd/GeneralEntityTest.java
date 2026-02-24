package org.exolab.castor.xml.dtd;

import junit.framework.TestCase;

/**
 * Comprehensive test coverage for DTD GeneralEntity class
 */
public class GeneralEntityTest extends TestCase {

    private DTDdocument document;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        document = new DTDdocument();
    }

    // Constructor Tests
    public void testDefaultConstructor() {
        GeneralEntity entity = new GeneralEntity();
        assertNotNull(entity);
    }

    public void testConstructorWithNameAndDocument() {
        GeneralEntity entity = new GeneralEntity(document, "myEntity");
        assertNotNull(entity);
        assertEquals("myEntity", entity.getName());
        assertEquals(document, entity.getDocument());
    }

    public void testConstructorWithNullDocument() {
        try {
            new GeneralEntity(null, "entity");
            fail("Should throw IllegalArgumentException for null document");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("document must not be null"));
        }
    }

    public void testConstructorWithNullName() {
        try {
            new GeneralEntity(document, null);
            fail("Should throw IllegalArgumentException for null name");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("name must not be empty"));
        }
    }

    public void testConstructorWithEmptyName() {
        try {
            new GeneralEntity(document, "");
            fail("Should throw IllegalArgumentException for empty name");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("name must not be empty"));
        }
    }

    // Accessor Tests
    public void testGetName() {
        GeneralEntity entity = new GeneralEntity(document, "testEntity");
        assertEquals("testEntity", entity.getName());
    }

    public void testGetDocument() {
        GeneralEntity entity = new GeneralEntity(document, "testEntity");
        assertEquals(document, entity.getDocument());
    }

    // Internal Entity Tests
    public void testSetValue() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        entity.setValue("replacement text");
        assertTrue(entity.isInternal());
        assertEquals("replacement text", entity.getValue());
    }

    public void testSetValueWithNullValue() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        try {
            entity.setValue(null);
            fail("Should throw IllegalArgumentException for null value");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("can not set null value"));
        }
    }

    public void testIsInternalFalse() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        assertFalse(entity.isInternal());
    }

    public void testGetValueNonInternal() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        assertNull(entity.getValue());
    }

    public void testMultipleInternalValues() {
        GeneralEntity entity1 = new GeneralEntity(document, "ent1");
        GeneralEntity entity2 = new GeneralEntity(document, "ent2");

        entity1.setValue("value1");
        entity2.setValue("value2");

        assertEquals("value1", entity1.getValue());
        assertEquals("value2", entity2.getValue());
    }

    // External Public Entity Tests
    public void testSetExternalPublic() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        entity.setExternalPublic(
            "-//W3C//DTD XHTML 1.0",
            "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
        );

        assertTrue(entity.isExternalPublic());
        assertEquals("-//W3C//DTD XHTML 1.0", entity.getPubIdentifier());
        assertEquals(
            "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd",
            entity.getSysIdentifier()
        );
    }

    public void testSetExternalPublicWithNullPubId() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        try {
            entity.setExternalPublic(null, "http://example.com");
            fail("Should throw IllegalArgumentException for null public ID");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("can not set null public ID"));
        }
    }

    public void testSetExternalPublicWithNullSysId() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        try {
            entity.setExternalPublic("-//Example", null);
            fail("Should throw IllegalArgumentException for null system ID");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("can not set null system ID"));
        }
    }

    public void testIsExternalPublicFalse() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        assertFalse(entity.isExternalPublic());
    }

    public void testGetPubIdentifierNonPublic() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        assertNull(entity.getPubIdentifier());
    }

    // External System Entity Tests
    public void testSetExternalSystem() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        entity.setExternalSystem("http://example.com/dtd");

        assertTrue(entity.isExternalSystem());
        assertEquals("http://example.com/dtd", entity.getSysIdentifier());
    }

    public void testSetExternalSystemWithNullSysId() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        try {
            entity.setExternalSystem(null);
            fail("Should throw IllegalArgumentException for null system ID");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("can not set null system ID"));
        }
    }

    public void testIsExternalSystemFalse() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        assertFalse(entity.isExternalSystem());
    }

    public void testGetSysIdentifierForExternalSystem() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        entity.setExternalSystem("file:///dtd/mydtd.dtd");
        assertEquals("file:///dtd/mydtd.dtd", entity.getSysIdentifier());
    }

    public void testGetSysIdentifierNonExternal() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        assertNull(entity.getSysIdentifier());
    }

    // NDATA Tests
    public void testSetNDATAInternal() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        entity.setValue("value");
        entity.setNDATA("notationName");
        // getNotation() only returns value for external entities, not internal
        assertNull(entity.getNotation());
    }

    public void testSetNDATAWithNullNotation() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        try {
            entity.setNDATA(null);
            fail("Should throw IllegalArgumentException for null notation");
        } catch (IllegalArgumentException e) {
            assertTrue(
                e
                    .getMessage()
                    .contains("can not set empty associated notation name")
            );
        }
    }

    public void testSetNDATAWithEmptyNotation() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        try {
            entity.setNDATA("");
            fail("Should throw IllegalArgumentException for empty notation");
        } catch (IllegalArgumentException e) {
            assertTrue(
                e
                    .getMessage()
                    .contains("can not set empty associated notation name")
            );
        }
    }

    public void testGetNotationNonExternal() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        assertNull(entity.getNotation());
    }

    // External Unparsed Entity Tests
    public void testIsExternalUnparsedPublic() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        entity.setExternalPublic("-//Example", "http://example.com");
        entity.setNDATA("notation");

        assertTrue(entity.isExternalUnparsed());
    }

    public void testIsExternalUnparsedSystem() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        entity.setExternalSystem("http://example.com");
        entity.setNDATA("notation");

        assertTrue(entity.isExternalUnparsed());
    }

    public void testIsExternalUnparsedNoNotation() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        entity.setExternalPublic("-//Example", "http://example.com");

        assertFalse(entity.isExternalUnparsed());
    }

    public void testIsExternalUnparsedInternal() {
        GeneralEntity entity = new GeneralEntity(document, "entity");
        entity.setValue("value");
        entity.setNDATA("notation");

        assertFalse(entity.isExternalUnparsed());
    }

    // Complex Scenarios
    public void testInternalEntityWithValue() {
        GeneralEntity entity = new GeneralEntity(document, "copyright");
        entity.setValue("Copyright 2024");

        assertTrue(entity.isInternal());
        assertEquals("Copyright 2024", entity.getValue());
        assertNull(entity.getSysIdentifier());
        assertNull(entity.getPubIdentifier());
    }

    public void testExternalPublicEntity() {
        GeneralEntity entity = new GeneralEntity(document, "xhtml");
        entity.setExternalPublic(
            "-//W3C//DTD XHTML 1.0 Strict//EN",
            "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
        );

        assertTrue(entity.isExternalPublic());
        assertFalse(entity.isExternalSystem());
        assertFalse(entity.isInternal());
        assertEquals(
            "-//W3C//DTD XHTML 1.0 Strict//EN",
            entity.getPubIdentifier()
        );
    }

    public void testExternalSystemEntity() {
        GeneralEntity entity = new GeneralEntity(document, "local");
        entity.setExternalSystem("file:///schemas/local.dtd");

        assertTrue(entity.isExternalSystem());
        assertFalse(entity.isExternalPublic());
        assertEquals("file:///schemas/local.dtd", entity.getSysIdentifier());
    }

    public void testExternalUnparsedEntity() {
        GeneralEntity entity = new GeneralEntity(document, "image");
        entity.setExternalSystem("file:///images/logo.gif");
        entity.setNDATA("gif");

        assertTrue(entity.isExternalUnparsed());
        assertTrue(entity.isExternalSystem());
        assertEquals("gif", entity.getNotation());
    }

    public void testMultipleEntitiesInDocument() {
        GeneralEntity internal = new GeneralEntity(document, "internal");
        GeneralEntity externalPublic = new GeneralEntity(document, "public");
        GeneralEntity externalSystem = new GeneralEntity(document, "system");

        internal.setValue("internal value");
        externalPublic.setExternalPublic("-//Example", "http://example.com");
        externalSystem.setExternalSystem("file:///dtd.xml");

        assertTrue(internal.isInternal());
        assertTrue(externalPublic.isExternalPublic());
        assertTrue(externalSystem.isExternalSystem());
    }

    public void testEntityTypeTransition() {
        GeneralEntity entity = new GeneralEntity(document, "entity");

        entity.setValue("internal");
        assertTrue(entity.isInternal());

        entity.setExternalSystem("http://example.com");
        assertTrue(entity.isExternalSystem());
        assertFalse(entity.isInternal());

        entity.setExternalPublic("-//Example", "http://example.com");
        assertTrue(entity.isExternalPublic());
        assertFalse(entity.isExternalSystem());
    }

    public void testEntityWithSpecialCharacters() {
        GeneralEntity entity = new GeneralEntity(
            document,
            "entity-with_special123"
        );
        entity.setValue("value with <special> & characters");

        assertEquals("entity-with_special123", entity.getName());
        assertEquals("value with <special> & characters", entity.getValue());
    }

    public void testGetNotationForExternalPublic() {
        GeneralEntity entity = new GeneralEntity(document, "video");
        entity.setExternalPublic("-//Video", "http://example.com/video.dtd");
        entity.setNDATA("mpeg");

        assertTrue(entity.isExternalUnparsed());
        assertEquals("mpeg", entity.getNotation());
    }

    public void testGetNotationForExternalSystem() {
        GeneralEntity entity = new GeneralEntity(document, "audio");
        entity.setExternalSystem("http://example.com/audio.dtd");
        entity.setNDATA("wav");

        assertTrue(entity.isExternalUnparsed());
        assertEquals("wav", entity.getNotation());
    }
}
