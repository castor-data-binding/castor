package org.exolab.castor.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Sax2EventFromStaxProducerIntegrationTest extends TestCase {
    private static final String testXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><UnmarshalFranz content=\"Bla Bla Bla\" />";
    private static final String unclosedStartElement = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><UnmarshalFranz content=\"Bla Bla Bla\">";
    private static final String undeclaredPrefix = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><pre:UnmarshalFranz content=\"Bla Bla Bla\" />";
    private static final String differentEndElement = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><UnmarshalFranz content=\"Bla Bla Bla\"></UnmarshalFranz2>";
    private Reader _reader;
    private XMLEventReader _eventReader;

    /**
     * Closing the reader.
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        _reader.close();
        _reader = null;
    }

    public void testUnmarshalling() throws MarshalException,
            ValidationException, XMLStreamException, FactoryConfigurationError {

        _reader = new StringReader(testXML);
        _eventReader = XMLInputFactory.newInstance().createXMLEventReader(
                _reader);

        XMLContext xmlContext = new XMLContext();
        Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
        unmarshaller.setClass(UnmarshalFranz.class);
        Assert.assertNotNull(unmarshaller);

        UnmarshalFranz f = (UnmarshalFranz) unmarshaller.unmarshal(_eventReader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    public void testUnclosedStartElementException() throws XMLStreamException,
            FactoryConfigurationError, ValidationException {

        _reader = new StringReader(unclosedStartElement);

        try {
            _eventReader = XMLInputFactory.newInstance().createXMLEventReader(
                    _reader);
        } catch (Exception e) {
            // if Exception is catched here then it's out of the Unmarshaller
            // scope
            e.printStackTrace();
            return;
        }

        Unmarshaller unmarshaller = new XMLContext().createUnmarshaller();
        unmarshaller.setClass(UnmarshalFranz.class);

        MarshalException fromStax = null, fromSax = null;
        try {
            UnmarshalFranz f1 = (UnmarshalFranz) unmarshaller.unmarshal(_eventReader);
        } catch (MarshalException e) {
            fromStax = e;
        }

        try {
            _reader.reset();
            UnmarshalFranz f2 = (UnmarshalFranz) unmarshaller.unmarshal(_reader);

        } catch (MarshalException e) {
            fromSax = e;
        } catch (ValidationException e) {
        } catch (IOException e) {
            // catch-block for _reader.reset();
            e.printStackTrace();
        }

        Assert.assertNotNull(fromStax);
        Assert.assertNotNull(fromSax);
        Assert.assertEquals(fromStax.getErrorCode(), fromSax.getErrorCode());
    }

//    public void testPrefixUnbound() throws XMLStreamException,
//            FactoryConfigurationError, ValidationException {
//
//        _reader = new StringReader(undeclaredPrefix);
//        try {
//            _eventReader = XMLInputFactory.newInstance().createXMLEventReader(
//                    _reader);
//        } catch (Exception e) {
//            // if Exception is catched here then it's out of the Unmarshaller
//            // scope
//            return;
//        }
//
//        // This shouldn't be reached anymore
//        Unmarshaller unmarshaller = new XMLContext().createUnmarshaller();
//        unmarshaller.setClass(UnmarshalFranz.class);
//
//        MarshalException fromStax = null, fromSax = null;
//        try {
//            UnmarshalFranz f1 = (UnmarshalFranz) unmarshaller.unmarshal(_eventReader);
//        } catch (MarshalException e) {
//            fromStax = e;
//        }
//
//        try {
//            _reader.reset();
//            UnmarshalFranz f2 = (UnmarshalFranz) unmarshaller.unmarshal(_reader);
//
//        } catch (MarshalException e) {
//            fromSax = e;
//        } catch (ValidationException e) {
//        } catch (IOException e) {
//            // catch-block for _reader.reset();
//            e.printStackTrace();
//        }
//
//        Assert.assertNotNull(fromStax);
//        Assert.assertNotNull(fromSax);
//        Assert.assertEquals(fromStax.getErrorCode(), fromSax.getErrorCode());
//    }

    public void testEndTagNameDoesntMatchStartTagName()
            throws XMLStreamException, FactoryConfigurationError,
            ValidationException {

        MarshalException fromStax = null, fromSax = null;

        XMLContext xmlContext = new XMLContext();

        Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
        unmarshaller.setClass(UnmarshalFranz.class);

        _reader = new StringReader(differentEndElement);
        _eventReader = XMLInputFactory.newInstance().createXMLEventReader(
                _reader);

        try {
            UnmarshalFranz f1 = (UnmarshalFranz) unmarshaller.unmarshal(_eventReader);
        } catch (MarshalException e) {
            fromStax = e;
        }

        try {
            _reader.reset();
            UnmarshalFranz f2 = (UnmarshalFranz) unmarshaller.unmarshal(_reader);

        } catch (MarshalException e) {
            fromSax = e;
        } catch (ValidationException e) {
        } catch (IOException e) {
            // catch-block for _reader.reset();
            e.printStackTrace();
        }

        Assert.assertNotNull(fromStax);
        Assert.assertNotNull(fromSax);
        Assert.assertEquals(fromStax.getErrorCode(), fromSax.getErrorCode());
    }
}
