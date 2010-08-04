package org.exolab.castor.xml;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is used to record the attribute values in every SAX startElement event. The
 * values are then get compared to the attribute values from the
 * SAXEventFromStaxProducer.
 * 
 * <p>
 * If they are different then there an assertion is going fail
 * </p>
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 */
public class AttributeRecorder extends DefaultHandler {

	/**
	 * Logger from commons-logging.
	 */
	private static final Log LOG = LogFactory
			.getLog(BaseSax2EventFromStaxProducer.class);

	/**
	 * a fifo to record attributes
	 */
	private Queue<Set<AttributeElement>> attributes = null;

	/**
	 * indicates if we are in recordMode
	 */
	private Boolean recordMode;

	public AttributeRecorder() {
		super();
		recordMode = true;
		attributes = new LinkedList<Set<AttributeElement>>();
	}

	/**
	 * calls {@link #handleEvent()}
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		handleEvent(atts);
	}

	/**
	 * if in recordMode then record current location otherwise compare current
	 * location to expected location
	 */
	private void handleEvent(Attributes atts) {
		if (inRecordMode())
			recordAttributes(getAttributeSet(atts));
		else
			compareWithAttributes(getAttributeSet(atts));
	}

	public void setReplayMode() {
		recordMode = false;
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
	private void recordAttributes(Set<AttributeElement> attributeSet) {
		attributes.add(attributeSet);
	}

	private Set<AttributeElement> getAttributeSet(Attributes atts) {
		Set<AttributeElement> attSet = new HashSet<AttributeElement>();
		for (int index = 0; index < atts.getLength(); index++) {
			attSet.add(new AttributeElement(atts, index));
		}

		return attSet;
	}

	/**
	 * compares the expected location with the current location. If they are not
	 * equal then an Assertion fails
	 * 
	 * @param currentLocator
	 *            the current location
	 */
	private void compareWithAttributes(Set<AttributeElement> attributeSet) {
		Assert.assertEquals(attributes.poll(), attributeSet);
	}

	/**
	 * A Wrapper for a Attribute element.
	 * 
	 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
	 *         Erlacher</a>
	 * 
	 */
	private class AttributeElement {

		private String localName, uri, type, value;

		public AttributeElement(Attributes att, int index) {
			super();
			localName = att.getLocalName(index);
			uri = att.getURI(index);
			type = att.getType(index);
			value = att.getValue(index);
		}

		public String getLocalName() {
			return localName;
		}

		public String getUri() {
			return uri;
		}

		public String getType() {
			return type;
		}

		public String getValue() {
			return value;
		}

		@Override
		public boolean equals(Object that) {
			// check for self-comparison
			if (this == that)
				return true;

			if (!(that instanceof AttributeElement))
				return false;

			AttributeElement attribute = (AttributeElement) that;

			if (!nonEmpty(localName).equals(nonEmpty(attribute.getLocalName())))
				return false;
			if (!nonEmpty(uri).equals(nonEmpty(attribute.getUri())))
				return false;
			if (!nonEmpty(type).equals(nonEmpty(attribute.getType())))
				return false;
			if (!nonEmpty(value).equals(nonEmpty(attribute.getValue())))
				return false;

			return true;
		}

		private String nonEmpty(String string) {
			return string == null ? "" : string;
		}

	};

}
