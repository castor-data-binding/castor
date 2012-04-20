/*
 * Copyright 2010 Philipp Erlacher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This tests the methods which SaxEventFromStaxProducer invokes on a ContentHandler.
 * 
 * The methods which SaxEventFromStaxProducer invokes on an ErrorHandler are tested separately.
 * 
 * @author philipperlacher
 * 
 */
public class Sax2EventFromStaxProducerTest extends TestCase {

	/**
	 * all of the readers are global so that they can be closed after testing
	 */
	private XMLEventReader eventReader = null;
	
	private XMLStreamReader streamReader = null;
	
	private Reader reader;
	
	/**
	 * Sets the contentMock to replay mode. Starts the producer.
	 * Verifies the result.
	 * @param contentMock an EasyMock contentHandler object
	 * @param producer a {@link SAX2EventAndErrorProducer}
	 */
	private void compareMethodInvocations(ContentHandler contentMock, SAX2EventAndErrorProducer producer) throws SAXException {
		EasyMock.replay(contentMock);
		producer.start();
		EasyMock.verify(contentMock);
	}
	

	/**
	 * This method uses a XMLReader, sets recorder as ContentHandler and parses testString.
	 *
	 * @param testString
	 * @param contentMock a EasyMock object as contentHandler. It's in the recordMode and records event.
	 * @return attributes every start element contains attribute. Those get returned as a Queue
	 */
	private void parseWithSax(String testString, ContentHandler recorder)
			throws XMLStreamException, FactoryConfigurationError, SAXException,
			IOException {

		InputStream in = new ByteArrayInputStream(testString.getBytes("UTF-8"));
		XMLReader parser = XMLReaderFactory.createXMLReader();
		parser.setContentHandler(recorder);
		parser.parse(new InputSource(in));
	}

	/**
	 * this method instantiate a {@link SAX2EventAndErrorProducer} with a {@link XMLEventReader}
	 * and the given {@link ContentHandler}
	 * 
	 * @param testString this string is going to be parsed by the {@link XMLEventReader}.
	 * @return {@link SAX2EventAndErrorProducer} A usable producer with a {@link ContentHandler} set as content handler
	 * @throws XMLStreamException
	 */
	private SAX2EventAndErrorProducer getSax2EventFromStaxEventProducer(String testString)
			throws XMLStreamException {
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		
		reader = new StringReader(testString);
		eventReader = factory.createXMLEventReader(reader);
		
		return BaseSax2EventFromStaxProducer.createSax2EventFromStax(eventReader);
	}
	
	/**
	 * This method instantiate a {@link SAX2EventAndErrorProducer} with a {@link XMLEventReader}
	 * and the given {@link ContentHandler}
	 * 
	 * @param testString this string is going to be parsed by the {@link XMLEventReader}
	 * @return {@link SAX2EventAndErrorProducer} a usable producer
	 * @throws XMLStreamException
	 */
	private SAX2EventAndErrorProducer getSax2EventFromStaxStreamProducer(String testString)
			throws XMLStreamException {
		
		XMLInputFactory factory = XMLInputFactory.newInstance();

		reader = new StringReader(testString);
		streamReader = factory.createXMLStreamReader(reader);
		
		return BaseSax2EventFromStaxProducer.createSax2EventFromStax(streamReader);
	}

	/**
	 * Tests if {@link SAX2EventAndErrorProducer} invokes the same methods on a {@link ContentHandler} as a
	 * SAX parser would. this tests StaxStream2SaxBridge as well as
	 * StaxEvent2SaxBridge
	 * 
	 * @param sample
	 *            A sample XML
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void executeTest(String sample) throws XMLStreamException,
			FactoryConfigurationError, SAXException, IOException {
		
		
		ContentHandler contentMock = EasyMock.createStrictMock(ContentHandler.class);
		Sax2MethodRecorder recorder = new Sax2MethodRecorder(contentMock);
		
		parseWithSax(sample,recorder);
		SAX2EventAndErrorProducer producer = getSax2EventFromStaxEventProducer(sample);
		
		producer.setContentHandler(contentMock);
		
		compareMethodInvocations(contentMock, producer);
		
		EasyMock.reset(contentMock);	
		recorder = new Sax2MethodRecorder(contentMock);
		
		parseWithSax(sample,recorder);
	
		producer = getSax2EventFromStaxStreamProducer(sample);
		producer.setContentHandler(contentMock);
		
		compareMethodInvocations(contentMock,producer);
	}

	/**
	 * runs executeTest with a sample XML in which there are two attributes
	 * within a single element
	 * 
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testLNameInStartElement() throws XMLStreamException,
			FactoryConfigurationError, SAXException, IOException {

		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<Person first-name=\"Zaphod\" last-name=\"Beeblebrox\" />";

		executeTest(sample);
	}

	/**
	 * runs executeTest with a sample XML in which there is a default namespace
	 * declaration
	 * 
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testNamespaceUriInStartElement() throws SAXException,
			IOException, XMLStreamException, FactoryConfigurationError {

		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<Person xmlns=\"http://early.pearly\" first-name=\"Zaphod\" last-name=\"Beeblebrox\" />";

		executeTest(sample);
	}

	/**
	 * runs executeTest with a sample XML in which there is a namespace
	 * declaration with a prefix
	 * 
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testPrefixInStartElement() throws SAXException, IOException,
			XMLStreamException, FactoryConfigurationError {

		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<p:Person xmlns:p=\"http://early.pearly\" first-name=\"Zaphod\" last-name=\"Beeblebrox\" />";

		executeTest(sample);
	}

	/**
	 * runs executeTest with a sample XML in which there is a namespace
	 * declaration with a prefix and the attributes belong to that namespace
	 * 
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testStartElementPrefixAttributes() throws SAXException,
			IOException, XMLStreamException, FactoryConfigurationError {

		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<p:Person xmlns:p=\"http://early.pearly\" p:first-name=\"Zaphod\" p:last-name=\"Beeblebrox\" />";

		executeTest(sample);
	}

	/**
	 * runs executeTest with a sample XML in which there is a root element with
	 * a namespace declaration and a subelement
	 * 
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testStartElementChildren() throws SAXException, IOException,
			XMLStreamException, FactoryConfigurationError {

		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<p:Person xmlns:p=\"http://early.pearly\">"
				+ "<p:first-name p:last-name=\"Beeblebrox\">Zaphod</p:first-name>"
				+ "</p:Person>";

		executeTest(sample);
	}

	/**
	 * runs executeTest with a sample XML in which there is a root element with
	 * a namespace declaration, and many sub elements
	 * 
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testMoreElements() throws SAXException, IOException,
			XMLStreamException, FactoryConfigurationError {

		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<p:Author xmlns:p=\"http://ursa.minor\">"
				+ "<first-name>Ford</first-name><last-name>Prefect</last-name>"
				+ "<book><title>About Earth</title><description>mostly harmless</description></book>"
				+ "</p:Author>";

		executeTest(sample);
	}

	/**
	 * runs executeTest with a sample XML in which there are namespace
	 * declarations in the root element and namespace declarations in sub
	 * elements, default namespace is also being declared
	 * 
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testStartPrefix() throws SAXException, IOException,
			XMLStreamException, FactoryConfigurationError {

		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<Authors xmlns=\"http://betel.geuse\" xmlns:p=\"http://ursa.minor\" xmlns:pM=\"http://ursa.maior\">"
				+ "<q:Author xmlns:q=\"http://heart.gold\">"
				+ "<first-name>Ford</first-name><last-name>Prefect</last-name>"
				+ "<book><title>About Earth</title><description>mostly harmless</description></book>"
				+ "</q:Author>"
				+ "<pM:Author>"
				+ "<first-name>Ford</first-name><last-name>Prefect</last-name>"
				+ "<book><title>About Earth</title><description>mostly harmless</description></book>"
				+ "</pM:Author>" + "</Authors>";

		executeTest(sample);
	}

	/**
	 * runs executeTest with a sample XML in which there are namespace
	 * declarations in the root element and namespace declarations in sub
	 * elements, default namespace is also being declared
	 * 
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testEndPrefixMapping() throws SAXException, IOException,
			XMLStreamException, FactoryConfigurationError {

		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<Authors xmlns=\"http://betel.geuse\" xmlns:p=\"http://ursa.minor\" xmlns:pM=\"http://ursa.maior\">"
				+ "<q:Author xmlns:q=\"http://heart.gold\"></q:Author>"
				+ "<pM:Author><book></book></pM:Author>" + "</Authors>";

		executeTest(sample);
	}

	/**
	 * runs executeTest with a sample XML in which a DTD is used to define the
	 * structure of the XML and in the XML are ignorableWhitspaces
	 * 
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testIgnorableWhitspace() throws XMLStreamException,
			FactoryConfigurationError, SAXException, IOException {

		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<!DOCTYPE Author [" + "  <!ELEMENT Author (name)>"
				+ "  <!ELEMENT name (#PCDATA)>" + "]>" + "<Author>"
				+ "   <name>" + "      Ford" + "   </name>" + "</Author>";

		executeTest(sample);
	}
	
	
	/**
	 * runs executeTest with a sample XML in which a DTD is used to define the
	 * structure of the XML and in the XML are ignorableWhitspaces
	 * 
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testIgnorableWhitspaceWithoutDTD() throws XMLStreamException,
			FactoryConfigurationError, SAXException, IOException {

		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<!DOCTYPE Author [" + "  <!ELEMENT Author (name)>"
				+ "  <!ELEMENT name (#PCDATA)>" + "]>" + "<Author>"
				+ "   <name>" + "      Ford\n" + "   </name>\n" + "</Author>";

		// executeTest(sample);
	}

	/**
	 * Test if long characters that will send more characters event are recorded
	 * and sent properly
	 * 
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testCharactersBuffer() throws XMLStreamException,
			FactoryConfigurationError, SAXException, IOException {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Person>"
				+ "<first-name> ");
		
		for (int i = 0; i < 1028; i++)
			buffer.append("realyLongName" + i);
		
		buffer.append("</first-name></Person>");

		executeTest(buffer.toString());
	}
	
	/**
	 * tests if locator is produced properly (= is the same than the SAX output)
	 * @throws SAXException
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public void testDocumentLocator() throws SAXException, IOException, XMLStreamException {
		
		String sample = "<?xml version=\"1.0\" ?>"
			+ "<Person xmlns=\"http://early.pearly\" first-name=\"Zaphod\" last-name=\"Beeblebrox\" />";
		
		DocumentLocatorRecorder recorder = new DocumentLocatorRecorder();
		parseWithSax(sample, recorder);
		
		System.out.println();
		
		recorder.setReplayMode();
		
		SAX2EventAndErrorProducer producer = getSax2EventFromStaxStreamProducer(sample);
		producer.setContentHandler(recorder);
		producer.start();
		
	}
	
	/**
	 * tests if locator is produced properly (= the same as it is produced from SAX)
	 * @throws SAXException
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public void testAttributes() throws SAXException, IOException, XMLStreamException {
		
		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<Authors xmlns=\"http://betel.geuse\" xmlns:p=\"http://ursa.minor\" xmlns:pM=\"http://ursa.maior\">"
			+ "<q:Author xmlns:q=\"http://heart.gold\"></q:Author>"
			+ "<pM:Author><book></book></pM:Author>" + "</Authors>";
		
		AttributeRecorder recorder = new AttributeRecorder();
		
		parseWithSax(sample, recorder);
		
		System.out.println();
		
		recorder.setReplayMode();
		
		SAX2EventAndErrorProducer producer = getSax2EventFromStaxStreamProducer(sample);
		producer.setContentHandler(recorder);
		producer.start();
		
	}
	
	/**
	 * Closing the reader.
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	public void tearDown() throws Exception {
		if (reader != null)
			reader.close();
		reader = null;
		if (eventReader != null)
			eventReader.close();
		eventReader = null;
		if (streamReader != null)
			streamReader.close();
		streamReader = null;
	}

}
