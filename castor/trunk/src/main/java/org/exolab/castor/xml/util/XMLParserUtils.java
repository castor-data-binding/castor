package org.exolab.castor.xml.util;

import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.Messages;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * A couple of routines to manipulate XMLParser instances. Mostly extracted
 * from 'old' LocalConfiguration class.
 * 
 * @author Joachim Grueneis, jgrueneis_at_gmail_dot_com
 * @version $Id$
 * @since 1.1.3
 */
public class XMLParserUtils {

    /**
     * Logger to be used.
     */
    static final Log LOG = LogFactory.getFactory().getInstance(XMLParserUtils.class);
    
    /** 
     * To set validation feature of XMLReader.
     */
    private static final String VALIDATION = "http://xml.org/sax/features/validation";
    
    /** 
     * To set namespaces feature of XMLReader. 
     */
    private static final String NAMESPACES = "http://xml.org/sax/features/namespaces";

    /**
     * Sets features on XML reader instance.
     * @param properties the Properties to read parser features from
     * @param defaultFeatures any default features to use
     * @param validation Whether to enable validation or not.
     * @param namespaces Whether to enable namespace support for not.
     * @param xmlReader The XMLReader instance to configure.
     */
    public static void setFeaturesOnXmlReader(
            final String parserFeatures,
            final String parserFeaturesToDisable,
            final boolean validation, 
            final boolean namespaces, 
            final XMLReader xmlReader) {
        try {
            xmlReader.setFeature(VALIDATION, validation);
            xmlReader.setFeature(NAMESPACES, namespaces);
            enableFeatures(parserFeatures, xmlReader);
            disableFeatures(parserFeaturesToDisable, xmlReader);
        } catch (SAXException except) {
            LOG.error(Messages.format("conf.configurationError", except));
        }
    }

    /**
     * Enables selected features on the XMLReader instance.
     * @param features Features to enable
     * @param xmlReader XMLReader instance to be configured.
     * @throws SAXNotRecognizedException If the feature is not recognized by the XMLReader.
     * @throws SAXNotSupportedException If the feature is not supported by the XMLReader.
     */
    private static void enableFeatures(final String features, final XMLReader xmlReader) 
        throws SAXNotRecognizedException, SAXNotSupportedException {
        StringTokenizer token;
        if (features != null) {
            token = new StringTokenizer(features, ", ");
            while (token.hasMoreTokens()) {
                xmlReader.setFeature(token.nextToken(), true);
            }
        }
    }

    /**
     * Disables selected features on the XMLReader instance.
     * @param features Features to disable
     * @param xmlReader XMLReader instance to be configured.
     * @throws SAXNotRecognizedException If the feature is not recognized by the XMLReader.
     * @throws SAXNotSupportedException If the feature is not supported by the XMLReader.
     */
    private static void disableFeatures(final String features, final XMLReader xmlReader) 
        throws SAXNotRecognizedException, SAXNotSupportedException {
        StringTokenizer token;
        if (features != null) {
            token = new StringTokenizer(features, ", ");
            while (token.hasMoreTokens()) {
                xmlReader.setFeature(token.nextToken(), false);
            }
        }
    }
    
    /**
     * To get a SAXParser instance which is then used to get either
     * parser or XMLReader.
     * @param validation validation flag to set into parser factory
     * @param namespaces namespace flag to set into parser factory
     * @return the SAXParser for further use
     */
    public static SAXParser getSAXParser(final boolean validation, final boolean namespaces) {
        SAXParser saxParser = null;

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(namespaces);
        factory.setValidating(validation);
        try {
            saxParser = factory.newSAXParser();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully instantiated a JAXP SAXParser instance.");
            }
        } catch (ParserConfigurationException pcx) {
            LOG.error(Messages.format("conf.configurationError", pcx));
        } catch (org.xml.sax.SAXException sx) {
            LOG.error(Messages.format("conf.configurationError", sx));
        }
        return saxParser;
    }
    
    /**
     * Instantiates an {@link XMLReader} instance directly, using {@link Class#forName(String)}
     * to obtain the {@link Class} instance, and uses {@link Class#newInstance()}
     * to create the actual instance.
     * @param className The class name of the {@link XMLReader} instance to be instantiated.
     * @return An {@link XMLReader} instance.
     */
    public static XMLReader instantiateXMLReader(final String className) {
        XMLReader xmlReader;
        try {
            Class cls;
            cls = Class.forName(className);
            xmlReader = (XMLReader) cls.newInstance();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully instantiated " + className);
            }
        } catch (Exception except) {
            throw new RuntimeException(Messages.format(
                    "conf.failedInstantiateParser", className, except));
        }
        return xmlReader;
    }

    /**
     * Instantiates an {@link Parser} instance directly, using {@link Class#forName(String)}
     * to obtain the {@link Class} instance, and uses {@link Class#newInstance()}
     * to create the actual instance.
     * @param className The class name of the {@link Parser} instance to be instantiated.
     * @return An {@link Parser} instance.
     */
    public static Parser instantiateParser (final String className) {
        Parser parser;
        try {
            Class cls;
            cls = Class.forName(className);
            parser = (Parser) cls.newInstance();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully instantiated " + className);
            }
        } catch (Exception except) {
            throw new RuntimeException(Messages.format(
                    "conf.failedInstantiateParser", className, except));
        }
        return parser;
    }
}
