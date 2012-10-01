package org.exolab.castor.xml;

import java.util.List;
import java.util.Stack;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * This provides shared code for {@link Sax2EventFromStaxEventProducer} and
 * {@link Sax2EventFromStaxStreamProducer}. It consumes StAX events and 
 * produces SAX2 events.
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 */
public abstract class BaseSax2EventFromStaxProducer implements
        SAX2EventAndErrorProducer {

    /**
     * Logger from commons-logging.
     */
    private static final Log LOG = LogFactory
            .getLog(BaseSax2EventFromStaxProducer.class);

    /**
     * A stack to keep track when it's time to invoke endPrefixMapping
     */
    private Stack<List<String>> prefixes = new Stack<List<String>>();


    /**
     * On this interface the SAX methods get invoked
     */
    private ContentHandler contentHandler;

    /**
     * Callback Interface to handle errors
     */
    private ErrorHandler errorHandler;

    public static SAX2EventAndErrorProducer createSax2EventFromStax(
            XMLStreamReader streamReader) {
        return new Sax2EventFromStaxStreamProducer(streamReader);
    }

    public static SAX2EventAndErrorProducer createSax2EventFromStax(
            XMLEventReader eventReader) {
        return new Sax2EventFromStaxEventProducer(eventReader);
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public Stack<List<String>> getPrefixes() {
        return prefixes;
    }

    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * This method takes an eventType and invokes a method to handle that event.
     * 
     * <p>
     * It also takes information about the depth of the read element. Maybe
     * depth changes due to handling that event.
     * </p>
     * 
     * @param eventType
     *            The event type
     * @param depth
     *            The current depth of the element
     * @return depth The updated depth
     * @throws SAXException
     */
    int handleEventType(int eventType, int depth) throws SAXException {
        switch (eventType) {
        case XMLStreamConstants.START_ELEMENT:
            handleStartElement();
            return ++depth;
        case XMLStreamConstants.END_ELEMENT:
            handleEndElement();
            return --depth;
        case XMLStreamConstants.START_DOCUMENT:
            handleStartDocument();
            return ++depth;
        case XMLStreamConstants.END_DOCUMENT:
            handleEndDocument();
            return --depth;
        case XMLStreamConstants.CHARACTERS:
            handleCharacters();
            return depth;
        case XMLStreamConstants.SPACE:
            handleSpace();
            return depth;
        default:
            return depth;
        }
    }

   /**
     * Invoke {@link #handleDocumentLocator()} and {@link
     * getContentHandler().startDocument()};
     * 
     * @throws SAXException
     */
    void handleStartDocument() throws SAXException {
        LOG.info("< handleStartDocument >");

        handleDocumentLocator();

        contentHandler.startDocument();
    }

    /**
     * Handles a end document event.
     * <p>
     * Invoke {@link getContentHandler().endDocument()};
     * </p>
     * 
     * @throws SAXException
     */
    void handleEndDocument() throws SAXException {
        LOG.info("< handleEndDocument >");
        contentHandler.endDocument();

    }

    /**
     * Handles a start element event.
     * <p>
     * Invoke {@link #doStartPrefixMapping()} and {@link
     * getContentHandler().startElement()};
     * </p>
     * 
     * @throws SAXException
     */
    void handleStartElement() throws SAXException {
        LOG.info("< handleStartElement >");

        QName qName = getQName();
        String localName = qName.getLocalPart();
        String uri = qName.getNamespaceURI();
        String prefix = qName.getPrefix();
        String qNameString = getQName(prefix, localName);

        Attributes atts = getAttributes();

        doStartPrefixMapping();

        contentHandler.startElement(uri, localName, qNameString, atts);

    }

    /**
     * Handles an end element event.
     * <p>
     * Invoke {@link getContentHandler().endElement()} and
     * {@link #doEndPrefixMapping()};
     * </p>
     * 
     * @throws SAXException
     */
    void handleEndElement() throws SAXException {
        LOG.info("< handleEndElement >");

        QName qName = getQName();
        String localName = qName.getLocalPart();
        String uri = qName.getNamespaceURI();
        String prefix = qName.getPrefix();
        String qNameString = getQName(prefix, localName);

        contentHandler.endElement(uri, localName, qNameString);

        doEndPrefixMapping();
    }

    /**
     * Handles a space event.
     * 
     * @throws SAXException
     */
    void handleSpace() throws SAXException {

    }

    /**
     * Handles a character event.
     * <p>
     * If chars is ignorable whitespace {@link
     * getContentHandler().ignorableWhitespace will be called. Otherwise {@link
     * getContentHandler().characters()} will be called with characters(char[],
     * 0, length)
     * 
     * </p>
     * 
     * @throws SAXException
     */
    void handleCharacters() throws SAXException {
        LOG.info("< handleCharacters >");
        char[] chars;
        chars = getCharacters();

        if (isIgnorableWhitespace(chars, 0, chars.length))
            contentHandler.ignorableWhitespace(chars, 0, chars.length);
        else
            contentHandler.characters(chars, 0, chars.length);

    }

    /**
     * @param prefix
     * @param localPart
     * @return qName. If prefix length >=1 then it's like prefix:localPart,
     *         otherwise it's just the localPart
     */
    String getQName(String prefix, String localPart) {
        return getNonEmpty(prefix).length() >= 1 ? prefix + ":" + localPart
                : localPart;
    }

    /**
     * If a chars without leading and trailing whitespaces would be empty, this
     * method returns true, otherwise false,
     * 
     * @param chars
     * @param start
     *            the offset
     * @param length
     * @return
     */
    boolean isIgnorableWhitespace(char[] chars, int start, int length) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(chars, start, length);
        String string = buffer.toString();
        if (string.trim().length() == 0)
            return true;
        return false;
    }

    /**
     * If string equals null this returns an empty string, otherwise it returns
     * the string
     * 
     * @param string
     *            the string to check
     * @return a string. If string equals null this returns an empty string,
     *         otherwise it returns the string
     */
    String getNonEmpty(String string) {
        return string == null ? "" : string;
    }

    /**
     * @return a Location
     */
    abstract Location getLocation();

    /**
     * @return characters of the current event.
     */
    abstract char[] getCharacters();

    /**
     * For every declared namespace in the current event {@link
     * getContentHandler().startPrefixMapping()} gets invoked.
     * 
     * @throws SAXException
     */
    abstract void doStartPrefixMapping() throws SAXException;

    /**
     * 
     * @throws SAXException
     */
    abstract void doEndPrefixMapping() throws SAXException;

    /**
     * @return attributes of the current event
     */
    abstract Attributes getAttributes();

    /**
     * @return QName of the current event
     */
    abstract QName getQName();

    /**
     * If {@link #getLocation()} gets a location {@link
     * getContentHandler().setDocumentLocator()} will be called, otherwise not.
     */
    private void handleDocumentLocator() {
        Locator locator = getSAXLocator(getLocation());
        contentHandler.setDocumentLocator(locator);
    }

    /**
     * Gets a {@link org.xml.sax.Locator} to a given {@link Location}.
     * 
     * @param location
     *            A {@link Location}
     * @return A {@link Locator}
     */
    protected Locator getSAXLocator(Location location) {
        return new Locator() {
            public String getSystemId() {
                return getLocation().getSystemId();
            }

            public String getPublicId() {
                return getLocation().getPublicId();
            }

            public int getLineNumber() {
                return getLocation().getLineNumber();
            }

            public int getColumnNumber() {
                return getLocation().getColumnNumber();
            }
        };
    }
}
