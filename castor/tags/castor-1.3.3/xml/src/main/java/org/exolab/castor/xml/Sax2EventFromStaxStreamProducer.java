package org.exolab.castor.xml;

import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;

public class Sax2EventFromStaxStreamProducer extends BaseSax2EventFromStaxProducer {

	/**
	 * Logger from commons-logging.
	 */
	private static final Log LOG = LogFactory
			.getLog(BaseSax2EventFromStaxProducer.class);
	
	private XMLStreamReader streamReader;
	
	public Sax2EventFromStaxStreamProducer(XMLStreamReader streamReader) {
		this.streamReader = streamReader;
	}

	public void start() throws SAXException {

		int depth = 0;
		
		try {
			do {
				depth = handleEventType(streamReader.getEventType(),depth);
				
				if (depth != 0) {
                    streamReader.next();
                }
				
			} while (depth != 0);

		} catch (XMLStreamException e) {
			// there is no implementation difference between UnmarshallHandler info, warning and error.
			// that's why simple warning is called
			getErrorHandler().warning(new SAXParseException(e.getMessage(), getSAXLocator(e.getLocation()), null));
		}
	}

	@Override
	QName getQName() {
		return streamReader.getName();
	}

	@Override
	char[] getCharacters() {
		return streamReader.getText().toCharArray();
	}

	@Override
	void handleSpace() throws SAXException {
		LOG.info("< handleSpace >");
		String string = streamReader.getText();

		char[] chars;
		chars = new char[string.length()];
		chars = string.toCharArray();
		getContentHandler().ignorableWhitespace(chars, 0, chars.length);
	}

	@Override
	void doStartPrefixMapping() throws SAXException {
		LOG.info("< doStartPrefixMapping >");
		Integer nsCounter = streamReader.getNamespaceCount();

		List<String> prefixList = new LinkedList<String>();

		for (int i = 0; i < nsCounter; i++) {
			String prefix = getNonEmpty(streamReader.getNamespacePrefix(i));
			getContentHandler().startPrefixMapping(prefix,
					streamReader.getNamespaceURI(i));
			prefixList.add(prefix);
		}

		getPrefixes().push(prefixList);
	}

	@Override
	void doEndPrefixMapping() throws SAXException {

		List<String> prefixList = getPrefixes().pop();

		for (String prefix : prefixList) {
			getContentHandler().endPrefixMapping(prefix);
		}
		
	}

	@Override
	Attributes getAttributes() {

		Integer attCounter = streamReader.getAttributeCount();
		AttributesImpl atts = new AttributesImpl();
		for (int i = 0; i < attCounter; i++) {
			QName qName = streamReader.getAttributeName(i);
			String uri = qName.getNamespaceURI();
			String localName = qName.getLocalPart();
			String prefix = qName.getPrefix();
			String qNameString = getQName(prefix, localName);
			String type = streamReader.getAttributeType(i);
			String value = streamReader.getAttributeValue(i);
			atts.addAttribute(uri, localName, qNameString, type, value);
		}

		return atts;
	}

	@Override
	Location getLocation() {
		return streamReader.getLocation();
	}

}
