package org.exolab.castor.xml.schema.reader;

import org.exolab.castor.xml.schema.Resolver;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.AttributeList;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for SaxUnmarshaller class
 */
public class SaxUnmarshallerTest {

    private ConcreteSaxUnmarshaller unmarshaller;
    private Locator mockLocator;
    private Resolver mockResolver;

    @Before
    public void setUp() {
        unmarshaller = new ConcreteSaxUnmarshaller();
        mockLocator = mock(Locator.class);
        mockResolver = mock(Resolver.class);
    }

    @Test
    public void testConstructor() {
        assertNotNull(unmarshaller);
        assertNull(unmarshaller.getDocumentLocator());
        assertNull(unmarshaller.getResolver());
    }

    @Test
    public void testElementName() {
        assertEquals("testElement", unmarshaller.elementName());
    }

    @Test
    public void testGetObject() {
        Object obj = unmarshaller.getObject();
        assertEquals("testObject", obj);
    }

    @Test
    public void testFinish() throws SAXException {
        unmarshaller.finish();
    }

    @Test
    public void testSetAndGetDocumentLocator() {
        unmarshaller.setDocumentLocator(mockLocator);
        assertEquals(mockLocator, unmarshaller.getDocumentLocator());
    }

    @Test
    public void testSetAndGetResolver() {
        unmarshaller.setResolver(mockResolver);
        assertEquals(mockResolver, unmarshaller.getResolver());
    }

    @Test
    public void testIsWhiteSpace_AllWhitespace() {
        char[] chars = {' ', '\n', '\t', '\r', ' '};
        assertTrue(SaxUnmarshaller.isWhiteSpace(chars, 0, 5));
    }

    @Test
    public void testIsWhiteSpace_WithNonWhitespace() {
        char[] chars = {' ', 'a', '\t'};
        assertFalse(SaxUnmarshaller.isWhiteSpace(chars, 0, 3));
    }

    @Test
    public void testIsWhiteSpace_StartAndLength() {
        char[] chars = {'a', ' ', '\n', '\t', 'b'};
        assertTrue(SaxUnmarshaller.isWhiteSpace(chars, 1, 3));
    }

    @Test
    public void testIsWhiteSpace_EmptyLength() {
        char[] chars = {'a', 'b', 'c'};
        assertTrue(SaxUnmarshaller.isWhiteSpace(chars, 0, 0));
    }

    @Test
    public void testIsWhiteSpace_OnlySpaces() {
        char[] chars = {' ', ' ', ' '};
        assertTrue(SaxUnmarshaller.isWhiteSpace(chars, 0, 3));
    }

    @Test
    public void testIsWhiteSpace_OnlyNewlines() {
        char[] chars = {'\n', '\n'};
        assertTrue(SaxUnmarshaller.isWhiteSpace(chars, 0, 2));
    }

    @Test
    public void testIsWhiteSpace_OnlyTabs() {
        char[] chars = {'\t', '\t', '\t'};
        assertTrue(SaxUnmarshaller.isWhiteSpace(chars, 0, 3));
    }

    @Test
    public void testIsWhiteSpace_OnlyCarriageReturns() {
        char[] chars = {'\r', '\r'};
        assertTrue(SaxUnmarshaller.isWhiteSpace(chars, 0, 2));
    }

    @Test
    public void testError_WithoutLocator() {
        try {
            unmarshaller.error("Test error message");
            fail("Expected SAXException");
        } catch (SAXException e) {
            assertEquals("Test error message", e.getMessage());
        }
    }

    @Test
    public void testError_WithLocator() {
        when(mockLocator.getLineNumber()).thenReturn(42);
        unmarshaller.setDocumentLocator(mockLocator);

        try {
            unmarshaller.error("Test error");
            fail("Expected SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().contains("Test error"));
            assertTrue(e.getMessage().contains("line: 42"));
        }
    }

    @Test
    public void testIllegalAttribute_WithoutLocator() {
        try {
            unmarshaller.illegalAttribute("badAttr");
            fail("Expected SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().contains("Illegal attribute 'badAttr'"));
            assertTrue(e.getMessage().contains("testElement"));
        }
    }

    @Test
    public void testIllegalAttribute_WithLocator() {
        when(mockLocator.getLineNumber()).thenReturn(10);
        unmarshaller.setDocumentLocator(mockLocator);

        try {
            unmarshaller.illegalAttribute("invalidAttr");
            fail("Expected SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().contains("Illegal attribute 'invalidAttr'"));
            assertTrue(e.getMessage().contains("line: 10"));
        }
    }

    @Test
    public void testIllegalElement_WithoutLocator() {
        try {
            unmarshaller.illegalElement("badElement");
            fail("Expected SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().contains("Illegal element 'badElement'"));
            assertTrue(e.getMessage().contains("testElement"));
        }
    }

    @Test
    public void testIllegalElement_WithLocator() {
        when(mockLocator.getLineNumber()).thenReturn(20);
        unmarshaller.setDocumentLocator(mockLocator);

        try {
            unmarshaller.illegalElement("illegalChild");
            fail("Expected SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().contains("Illegal element 'illegalChild'"));
            assertTrue(e.getMessage().contains("line: 20"));
        }
    }

    @Test
    public void testRedefinedElement_SingleParam() {
        try {
            unmarshaller.redefinedElement("duplicate");
            fail("Expected SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().contains("redefintion of element 'duplicate'"));
            assertTrue(e.getMessage().contains("testElement"));
        }
    }

    @Test
    public void testRedefinedElement_WithExtraInfo() {
        try {
            unmarshaller.redefinedElement("duplicate", "Extra information");
            fail("Expected SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().contains("redefintion of element 'duplicate'"));
            assertTrue(e.getMessage().contains("Extra information"));
        }
    }

    @Test
    public void testRedefinedElement_WithLocator() {
        when(mockLocator.getLineNumber()).thenReturn(30);
        unmarshaller.setDocumentLocator(mockLocator);

        try {
            unmarshaller.redefinedElement("dup", "More info");
            fail("Expected SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().contains("redefintion"));
            assertTrue(e.getMessage().contains("line: 30"));
            assertTrue(e.getMessage().contains("More info"));
        }
    }

    @Test
    public void testRedefinedElement_WithNullExtraInfo() {
        try {
            unmarshaller.redefinedElement("element", null);
            fail("Expected SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().contains("redefintion of element 'element'"));
        }
    }

    @Test
    public void testOutOfOrder() {
        try {
            unmarshaller.outOfOrder("wrongOrder");
            fail("Expected SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().contains("out of order element"));
            assertTrue(e.getMessage().contains("wrongOrder"));
            assertTrue(e.getMessage().contains("testElement"));
        }
    }

    @Test
    public void testToInt_ValidInteger() {
        assertEquals(42, SaxUnmarshaller.toInt("42"));
        assertEquals(-100, SaxUnmarshaller.toInt("-100"));
        assertEquals(0, SaxUnmarshaller.toInt("0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToInt_InvalidInteger() {
        SaxUnmarshaller.toInt("notAnInteger");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToInt_EmptyString() {
        SaxUnmarshaller.toInt("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToInt_DecimalNumber() {
        SaxUnmarshaller.toInt("42.5");
    }

    @Test
    public void testCharacters() throws SAXException {
        char[] chars = {'t', 'e', 's', 't'};
        unmarshaller.characters(chars, 0, 4);
    }

    @Test
    public void testEndDocument() throws SAXException {
        unmarshaller.endDocument();
    }

    @Test
    public void testEndElement() throws SAXException {
        unmarshaller.endElement("element");
    }

    @Test
    public void testIgnorableWhitespace() throws SAXException {
        char[] chars = {' ', '\n', '\t'};
        unmarshaller.ignorableWhitespace(chars, 0, 3);
    }

    @Test
    public void testProcessingInstruction() throws SAXException {
        unmarshaller.processingInstruction("target", "data");
    }

    @Test
    public void testStartDocument() throws SAXException {
        unmarshaller.startDocument();
    }

    @Test
    public void testStartElement() throws SAXException {
        AttributeList mockAtts = mock(AttributeList.class);
        unmarshaller.startElement("element", mockAtts);
    }

    @Test(expected = SAXException.class)
    public void testErrorHandler_Error() throws SAXException {
        SAXParseException exception = new SAXParseException("error", mockLocator);
        unmarshaller.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testErrorHandler_FatalError() throws SAXException {
        SAXParseException exception = new SAXParseException("fatal", mockLocator);
        unmarshaller.fatalError(exception);
    }

    @Test(expected = SAXException.class)
    public void testErrorHandler_Warning() throws SAXException {
        SAXParseException exception = new SAXParseException("warning", mockLocator);
        unmarshaller.warning(exception);
    }

    /**
     * Concrete implementation for testing abstract SaxUnmarshaller
     */
    private static class ConcreteSaxUnmarshaller extends SaxUnmarshaller {
        @Override
        public String elementName() {
            return "testElement";
        }

        @Override
        public Object getObject() {
            return "testObject";
        }
    }
}
