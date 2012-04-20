package org.exolab.castor.xml;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * This class is used to record the locator values in every SAX event. The
 * recorded locators then get compared to the locator values from the
 * SAXEventFromStaxProducer.
 * 
 * <p>
 * If they are different then there an assertion is going fail
 * </p>
 * 
 * @implements ContentHandler
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 */
public class DocumentLocatorRecorder implements ContentHandler {

	/**
	 * Logger from commons-logging.
	 */
	private static final Log LOG = LogFactory
			.getLog(BaseSax2EventFromStaxProducer.class);

	/**
	 * a fifo to record locations
	 */
	private Queue<LocationElement> locations = null;

	/**
	 * indicatios if we are in recordMode
	 */
	private Boolean recordMode;

	public void setReplayMode() {
		recordMode = false;
	}

	/**
	 * the SAX locator object
	 */
	private Locator locator;

	public DocumentLocatorRecorder() {
		super();
		recordMode = true;
		locations = new LinkedList<LocationElement>();
	}

	/**
	 * calls {@link #handleEvent()}
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		LOG.debug("endDocument");
		handleEvent();
	}

	/**
	 * calls {@link #handleEvent()}
	 */
	public void endDocument() throws SAXException {
		LOG.debug("endDocument");
		handleEvent();
	}

	/**
	 * calls {@link #handleEvent()}
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		LOG.debug("endElement");
		handleEvent();
	}

	/**
	 * calls {@link #handleEvent()}
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		LOG.debug("endPrefixMapping");
		handleEvent();
	}

	/**
	 * calls {@link #handleEvent()}
	 */
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		LOG.debug("ignorableWhitespace");
		handleEvent();
	}

	/**
	 * calls {@link #handleEvent()}
	 */
	public void processingInstruction(String target, String data)
			throws SAXException {
		LOG.debug("processingInstruction");
		handleEvent();
	}

	/**
	 * don't handle this event. Check {@link http
	 * ://jira.codehaus.org/browse/CASTOR-2962} for more information
	 */
	public void setDocumentLocator(Locator locator) {
		LOG.debug("setDocumentLocator");
		this.locator = locator;
	}

	/**
	 * calls {@link #handleEvent()}
	 */
	public void skippedEntity(String name) throws SAXException {
		LOG.debug("skippedEntity");
		handleEvent();
	}

	/**
	 * don't handle this event. Check {@link http
	 * ://jira.codehaus.org/browse/CASTOR-2962} for more information
	 */
	public void startDocument() throws SAXException {
		LOG.debug("startDocument");
	}

	/**
	 * calls {@link #handleEvent()}
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		LOG.debug("startElement");
		handleEvent();
	}

	/**
	 * calls {@link #handleEvent()}
	 */
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		LOG.debug("startPrefixMapping");
		handleEvent();
	}

	/**
	 * if in recordMode then record current location otherwise compare current
	 * location to expected location
	 */
	private void handleEvent() {
		if (inRecordMode())
			recordLocation(getCurrentLocationElement());
		else
			compareWithLocationElement(getCurrentLocationElement());
	}

	/**
	 * are we in record mode ?
	 * 
	 * @return true if we are, false otherwise
	 */
	private boolean inRecordMode() {
		return recordMode;
	}

	/**
	 * record a location
	 * 
	 * @param locationElement
	 *            location to record
	 */
	private void recordLocation(LocationElement locationElement) {
		LOG.debug("record " + locationElement.getColumnNumber() + ":"
				+ locationElement.getLineNumber());
		locations.add(locationElement);

	}

	/**
	 * @return Locator the current location as a new LocationElement
	 */
	private LocationElement getCurrentLocationElement() {
		return new LocationElement(locator);
	}

	/**
	 * compares the expected location with the current location. If they are not
	 * equal then an Assertion fails
	 * 
	 * @param currentLocator
	 *            the current location
	 */
	private void compareWithLocationElement(LocationElement currentLocation) throws AssertionError {
		LOG.debug("comparison: ");
		if(locations.poll().equals(currentLocation))
			return;
		throw new AssertionError("Error !");
	}

	/**
	 * A Wrapper for a locator element.
	 * 
	 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
	 *         Erlacher</a>
	 * 
	 */
	private class LocationElement implements Locator {

		private String systemId;
		private String publicId;
		private int lineNumber;
		private int columnNumber;

		public LocationElement(Locator locator) {
			systemId = locator.getSystemId();
			publicId = locator.getPublicId();
			lineNumber = locator.getLineNumber();
			columnNumber = locator.getColumnNumber();
		}

		public String getSystemId() {
			return systemId;
		}

		public String getPublicId() {
			return publicId;
		}

		public int getLineNumber() {
			return lineNumber;
		}

		public int getColumnNumber() {
			return columnNumber;
		}

		@Override
		public boolean equals(Object that) {
			// check for self-comparison
			if (this == that)
				return true;

			if (!(that instanceof LocationElement))
				return false;

			LocationElement locator = (LocationElement) that;

			// it seems that there is a implementation difference of columnNumber;
			// in the previous version I used +1 wasn't necessary but here it is
			// [PE]
			if (!((columnNumber == locator.getColumnNumber() + 1)
					|| (columnNumber == locator.getColumnNumber())))
				return false;
			if (lineNumber != locator.getLineNumber())
				return false;
			if (!nonEmpty(publicId).equals(nonEmpty(locator.getPublicId())))
				return false;
			if (!nonEmpty(systemId).equals(nonEmpty(locator.getSystemId())))
				return false;

			return true;
		}

		private String nonEmpty(String string) {
			return string == null ? "" : string;
		}
	};

}
