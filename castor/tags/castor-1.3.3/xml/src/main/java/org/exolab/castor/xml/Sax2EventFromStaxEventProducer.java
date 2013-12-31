package org.exolab.castor.xml;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;

public class Sax2EventFromStaxEventProducer extends BaseSax2EventFromStaxProducer {

    /**
     * from here we stream
     */
    private XMLEventReader eventReader;

    /**
     * this represents the current event
     */
    private XMLEvent event;

    public Sax2EventFromStaxEventProducer(XMLEventReader eventReader) {
        this.eventReader = eventReader;
    }

    public void start() throws SAXException {

        int depth = 0;

        try {
            while (eventReader.hasNext()) {
                event = eventReader.nextEvent();
                depth = handleEventType(event.getEventType(), depth);

                if (depth <= 0) {
                    break;
                }
            }

        } catch (XMLStreamException e) {
            // there is no implementation difference between UnmarshallHandler
            // info, warning and error.
            getErrorHandler().warning(
                    new SAXParseException(e.getMessage(), getSAXLocator(e
                            .getLocation()), null));
        }
    }

    @Override
    QName getQName() {
        if (event.isEndElement()) {
            return event.asEndElement().getName();
        } else if (event.isStartElement()) {
            return event.asStartElement().getName();
        }
        return null;
    }

    @Override
    char[] getCharacters() {
        Characters characters = event.asCharacters();
        return characters.getData().toCharArray();
    }

    @Override
    void doStartPrefixMapping() throws SAXException {

        StartElement startElement = event.asStartElement();
        Iterator<Namespace> nsIt = startElement.getNamespaces();

        List<String> prefixList = new LinkedList<String>();

        while (nsIt.hasNext()) {
            Namespace ns = nsIt.next();
            String prefix = getNonEmpty(ns.getPrefix());
            getContentHandler()
                    .startPrefixMapping(prefix, ns.getNamespaceURI());

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

        StartElement startElement = event.asStartElement();
        AttributesImpl atts = new AttributesImpl();
        Iterator<Attribute> it = startElement.getAttributes();
        while (it.hasNext()) {
            Attribute attribute = it.next();

            String uri = attribute.getName().getNamespaceURI();
            String localName = attribute.getName().getLocalPart();
            String prefix = attribute.getName().getPrefix();
            String qName = getQName(prefix, localName);
            String type = attribute.getDTDType();
            String value = attribute.getValue();

            atts.addAttribute(uri, localName, qName, type, value);
        }
        return atts;
    }

    @Override
    Location getLocation() {
        return event.getLocation();
    }

}
