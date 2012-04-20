package org.exolab.castor.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * This class gets called by a SAX parser and is used for recording invocations
 * on an EasyMock object.
 * <p>
 * Every method that gets called by the parser and is used in UnmarshalHandler
 * will be recorded. Method invocations that don't get recorded are for example
 * processingInstruction() or skippedEntities().
 * </p>
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 */
public class Sax2MethodRecorder implements ContentHandler {

    /**
     * Logger from commons-logging.
     */
    private static final Log LOG = LogFactory.getLog(Sax2MethodRecorder.class);

    private ContentHandler contentMock;

    /**
     * charactersBuffer is used to save characters events that came in as chunks
     */
    StringBuffer charactersBuffer = null;

    public Sax2MethodRecorder(ContentHandler contentMock) {
        super();
        this.contentMock = contentMock;
    }

    /*
     * from here ContentHandler Methods
     */

    public void startDocument() {
        LOG.debug("< Start Document >");
        try {
            contentMock.startDocument();

        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void endDocument() {
        LOG.debug("< End Document >");
        try {
            contentMock.endDocument();

        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        LOG.debug("< Start Element >");

        contentMock.startElement(EasyMock.eq(uri), EasyMock.eq(localName),
                EasyMock.eq(qName), (Attributes) EasyMock.anyObject());

        return;
    }

    /**
     * receives notification of an endElement. This element will be recorded. If
     * there is a characters event pending, then this will be recorded first.
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        LOG.debug("< End Element >");

        if (isCharBufferSendable())
            sentCharacters();

        contentMock.endElement(EasyMock.eq(uri), EasyMock.eq(localName),
                EasyMock.eq(qName));

        return;
    }

    /**
     * fills a buffer with the characters in ch
     */
    public void characters(char[] ch, int start, int length) {
        LOG.debug("< characters >");

        if (charactersBuffer != null) {
            LOG.debug(" second characters event");
            charactersBuffer.append(ch, start, length);
        } else {
            charactersBuffer = new StringBuffer(length);
            charactersBuffer.append(ch, start, length);
        }
    }

    /**
     * if charactersBuffer is not null it is sendable
     * 
     * @return true if it is sendable, otherwise false
     */
    private boolean isCharBufferSendable() {
        return charactersBuffer != null;
    }

    /**
     * sets charactersBuffer back to null and therefore it's no longer sendable
     */
    private void resetCharBuffer() {
        charactersBuffer = null;
    }

    /**
     * takes charactersBuffer and sends it as a single event to the
     * contentHandler. Afterwards it resets charactersBuffer
     */
    private void sentCharacters() {
        try {
            String string = new String(charactersBuffer);

            char[] chars;
            chars = new char[charactersBuffer.length()];
            chars = string.toCharArray();

            contentMock.characters(EasyMock.aryEq(chars), EasyMock.eq(0),
                    EasyMock.eq(charactersBuffer.length()));

            resetCharBuffer();

        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        LOG.debug("< startPrefixMapping >");

        contentMock.startPrefixMapping(EasyMock.eq(prefix), EasyMock.eq(uri));
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        LOG.debug("< endPrefixMapping >");

        contentMock.endPrefixMapping(EasyMock.eq(prefix));
    }

    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        LOG.debug("< ignorableWhitespace >");

        StringBuffer buffer = new StringBuffer(length);
        buffer.append(ch, start, length);

        String string = new String(buffer);

        char[] chars;
        chars = new char[length];
        chars = string.toCharArray();

        contentMock.ignorableWhitespace(EasyMock.aryEq(chars), EasyMock.eq(0),
                EasyMock.eq(length));
    }

    /**
     * processInstructions won't be recorded unless
     * {@link UnmarshalHandler.processingInstruction} does something
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        return;
    }

    public void setDocumentLocator(Locator locator) {
        LOG.debug("< setDocumentLocator >");

        contentMock.setDocumentLocator((Locator) EasyMock.anyObject());
    }

    /**
     * skippedEntities won't be recorded unless
     * {@link UnmarshalHandler.skippedEntity} does something
     */
    public void skippedEntity(String name) throws SAXException {
        return;
    }

}
