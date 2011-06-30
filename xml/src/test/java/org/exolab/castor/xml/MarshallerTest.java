package org.exolab.castor.xml;

import junit.framework.TestCase;
import org.castor.test.entity.CastorObject;
import org.castor.test.entity.Email;
import org.castor.test.entity.Emails;
import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.easymock.EasyMock.*;

/**
 * @author <a herf="mailto:jmnarloch AT gmail DOT com">Jakub Narloch</a>
 * @version 1.3.3
 * @since 1.3.3
 */
public class MarshallerTest extends TestCase {

    /**
     * Represents the expected result that doesn't contain the xml declaration.
     */
    private static final String EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<tns:emails xmlns:tns=\"http://castor.org/email\">" +
            "<tns:email><tns:from>from@castor.org</tns:from><tns:to>to@castor.org</tns:to>" +
            "</tns:email></tns:emails>";

    /**
     * Represents the expected result that doesn't contain the xml declaration.
     */
    private static final String DOCUMENT_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<tns:emails xmlns:tns=\"http://castor.org/email\">" +
            "<tns:email><tns:from>from@castor.org</tns:from><tns:to>to@castor.org</tns:to>" +
            "</tns:email></tns:emails>";

    /**
     * Represents the expected result that doesn't contain the xml namespaces.
     */
    private static final String SUPPRESSED_NAMESPACE_EXPECTED_STRING =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<emails><email><from>from@castor.org</from><to>to@castor.org</to>" +
                    "</email></emails>";

    /**
     * Represents the expected result with modified root element name.
     */
    private static final String ROOT_ELEMENT_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<tns:mailingList xmlns:tns=\"http://castor.org/email\">" +
            "<tns:email><tns:from>from@castor.org</tns:from><tns:to>to@castor.org</tns:to>" +
            "</tns:email></tns:mailingList>";

    /**
     * Represents the expected result with 'xsi:type' attribute.
     */
    private static final String XSI_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<objects><castor-object xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
            " xsi:type=\"java:org.castor.test.entity.CastorObject\">" +
            "<name>test</name><value>8</value></castor-object></objects>";

    /**
     * Represents the expected result with suppressed 'xsi:type' attribute.
     */
    private static final String SUPPRESSED_XSI_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<objects><castor-object><name>test</name><value>8</value></castor-object></objects>";

    /**
     * Represents the expected result with 'xsi:type' attribute for root element.
     */
    private static final String ROOT_WITH_XSI_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<objects xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
            " xsi:type=\"java:java.util.Arrays$ArrayList\">" +
            "<castor-object xsi:type=\"java:org.castor.test.entity.CastorObject\">" +
            "<name>test</name><value>8</value></castor-object></objects>";

    /**
     * Represents the expected result without 'xsi:type' attribute for root element.
     */
    private static final String ROOT_WITHOUT_XSI_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<objects><castor-object xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
            " xsi:type=\"java:org.castor.test.entity.CastorObject\">" +
            "<name>test</name><value>8</value></castor-object></objects>";

    /**
     * <p>Represents the path to mapping file.</p>
     */
    private static final String EMAIL_MAPPING_FILE = "/org/castor/test/entity/mapping-email.xml";

    /**
     * Represents the instance of emails used for testing.
     */
    private Emails emails;

    /**
     * Sets up the test environment.
     *
     * @throws Exception if any error occurs
     */
    protected void setUp() throws Exception {
        super.setUp();

        Email email = new Email();
        email.setFrom("from@castor.org");
        email.setTo("to@castor.org");
        emails = new Emails();
        emails.setEmail(new Email[] {email});
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setSuppressNamespaces(boolean)} is set to <code>true</code>.
     *
     * @throws Exception in case of marshal problems
     */
    public void testSuppressNamespacesTrue() throws Exception {
        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);
        marshaller.setSuppressNamespaces(true);
        String result = marshalEmails(marshaller);
        assertXMLEqual("Marshaller wrote invalid result", SUPPRESSED_NAMESPACE_EXPECTED_STRING, result);
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setSuppressNamespaces(boolean)} is set to <code>false</code>.
     *
     * @throws Exception in case of marshal problems
     */
    public void testSuppressNamespacesFalse() throws Exception {
        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);
        marshaller.setSuppressNamespaces(false);
        String result = marshalEmails(marshaller);
        assertXMLEqual("Marshaller wrote invalid result", EXPECTED_STRING, result);
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setSuppressXSIType(boolean)} is set to <code>true</code>.
     *
     * @throws Exception in case of marshal problems
     */
    public void testSuppressXsiTypeTrue() throws Exception {
        CastorObject castorObject = createCastorObject();

        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);
        marshaller.setSuppressXSIType(true);
        marshaller.setRootElement("objects");
        String result = marshal(marshaller, Arrays.asList(castorObject));
        assertXMLEqual("Marshaller wrote invalid result", SUPPRESSED_XSI_EXPECTED_STRING, result);
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setSuppressXSIType(boolean)} is set to <code>false</code>.
     *
     * @throws Exception in case of marshal problems
     */
    public void testSuppressXsiTypeFalse() throws Exception {
        CastorObject castorObject = createCastorObject();

        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);
        marshaller.setSuppressXSIType(false);
        marshaller.setRootElement("objects");
        String result = marshal(marshaller, Arrays.asList(castorObject));
        assertXMLEqual("Marshaller wrote invalid result", XSI_EXPECTED_STRING, result);
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setMarshalAsDocument(boolean)} is set to <code>true</code>.
     *
     * @throws Exception in case of marshal problems
     */
    public void testMarshalAsDocumentTrue() throws Exception {

        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);
        marshaller.setMarshalAsDocument(true);
        String result = marshalEmails(marshaller);
        assertXMLEqual("Marshaller wrote invalid result", DOCUMENT_EXPECTED_STRING, result);
        Assert.assertTrue("Result doesn't contain xml declaration.",
                result.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setMarshalAsDocument(boolean)} is set to <code>false</code>.
     *
     * @throws Exception in case of marshal problems
     */
    public void testMarshalAsDocumentFalse() throws Exception {

        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);
        marshaller.setMarshalAsDocument(false);
        String result = marshalEmails(marshaller);
        assertXMLEqual("Marshaller wrote invalid result", EXPECTED_STRING, result);
        Assert.assertFalse("Result contains xml declaration.", result.matches("<\\?\\s*xml"));
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setRootElement(String)} is set.
     *
     * @throws Exception in case of marshal problems
     */
    public void testRootElement() throws Exception {

        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);
        marshaller.setRootElement("mailingList");
        String result = marshalEmails(marshaller);
        assertXMLEqual("Marshaller wrote invalid result", ROOT_ELEMENT_EXPECTED_STRING, result);
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setNoNamespaceSchemaLocation(String)} is set.
     *
     * @throws Exception in case of marshal problems
     */
    public void testNoNamespaceSchemaLocation() throws Exception {
        String noNamespaceSchemaLocation = "emails.xsd";

        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);
        marshaller.setNoNamespaceSchemaLocation(noNamespaceSchemaLocation);
        String result = marshalEmails(marshaller);

        assertXpathEvaluatesTo("The xsi:noNamespaceSchemaLocation hasn't been written or has invalid value.",
                noNamespaceSchemaLocation, "/tns:emails/@xsi:noNamespaceSchemaLocation", result);
        assertXMLEqual("Marshaller wrote invalid result", EXPECTED_STRING, result);
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setSchemaLocation(String)} is set.
     *
     * @throws Exception in case of marshal problems
     */
    public void testSchemaLocation() throws Exception {
        String schemaLocation = "emails.xsd";

        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);
        marshaller.setSchemaLocation(schemaLocation);

        String result = marshalEmails(marshaller);

        assertXpathEvaluatesTo("The xsi:noNamespaceSchemaLocation hasn't been written or has invalid value.",
                schemaLocation, "/tns:emails/@xsi:schemaLocation", result);
        assertXMLEqual("Marshaller wrote invalid result", EXPECTED_STRING, result);
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setMarshalAsDocument(boolean)} is set to <code>true</code>.
     *
     * @throws Exception in case of marshal problems
     */
    public void testUseXsiTypeAsRootTrue() throws Exception {
        CastorObject castorObject = createCastorObject();

        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);

        marshaller.setSuppressXSIType(false);
        marshaller.setUseXSITypeAtRoot(true);
        marshaller.setRootElement("objects");
        String result = marshal(marshaller, Arrays.asList(castorObject));
        assertXMLEqual("Marshaller wrote invalid result", ROOT_WITH_XSI_EXPECTED_STRING, result);
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setMarshalAsDocument(boolean)} is set to <code>false</code>.
     *
     * @throws Exception in case of marshal problems
     */
    public void testUseXsiTypeAsRootFalse() throws Exception {
        CastorObject castorObject = createCastorObject();

        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);

        marshaller.setSuppressXSIType(false);
        marshaller.setUseXSITypeAtRoot(false);
        marshaller.setRootElement("objects");
        String result = marshal(marshaller, Arrays.asList(castorObject));
        assertXMLEqual("Marshaller wrote invalid result", ROOT_WITHOUT_XSI_EXPECTED_STRING, result);
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#setMarshalListener(MarshalListener)} is set.
     *
     * @throws Exception in case of marshal problems
     */
    public void testMarshalListener() throws Exception {
        MarshalListener listener = createMockListener();

        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);
        marshaller.setMarshalListener(listener);

        String result = marshalEmails(marshaller);
        assertXMLEqual("Marshaller writes invalid StreamResult", EXPECTED_STRING, result);
        verify(listener);
    }

    /**
     * Tests the Marshaller when the {@link Marshaller#addProcessingInstruction(String, String)} is set.
     *
     * @throws Exception in case of marshal problems
     */
    public void testProcessingInstructions() throws Exception {

        Marshaller marshaller = createMarshallerFromMapping(EMAIL_MAPPING_FILE);
        marshaller.addProcessingInstruction("xml-stylesheet", "href=\"email.xsl\"");

        String result = marshalEmails(marshaller);
        assertXMLEqual("Marshaller writes invalid StreamResult", EXPECTED_STRING, result);
        assertTrue("Marshal result doesn't contain processing instruction.",
                result.contains("<?xml-stylesheet href=\"email.xsl\"?>"));
    }

    /**
     * Asserts the values of xpath expression evaluation is exactly the same as expected value. </p> The xpath may contain
     * the xml namespace prefixes, since namespaces from emails example are being registered.
     *
     * @param msg      the error message that will be used in case of test failure
     * @param expected the expected value
     * @param xpath    the xpath to evaluate
     * @param xmlDoc   the xml to use
     * @throws Exception if any error occurs during xpath evaluation
     */
    private void assertXpathEvaluatesTo(String msg, String expected, String xpath, String xmlDoc) throws Exception {
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("tns", "http://castor.org/email");
        namespaces.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");

        NamespaceContext ctx = new SimpleNamespaceContext(namespaces);
        XpathEngine engine = XMLUnit.newXpathEngine();
        engine.setNamespaceContext(ctx);

        Document doc = XMLUnit.buildControlDocument(xmlDoc);
        NodeList node = engine.getMatchingNodes(xpath, doc);

        assertEquals(msg, expected, node.item(0).getNodeValue());
    }

    /**
     * Creates a instance of {@link CastorObject} for testing.
     *
     * @return a instance of {@link CastorObject}
     */
    private CastorObject createCastorObject() {
        CastorObject castorObject = new CastorObject();
        castorObject.setName("test");
        castorObject.setValue(8);
        return castorObject;
    }

    /**
     * Marshals the given object.
     *
     * @param marshaller the marshaller to use
     * @param object     the object to marshal
     * @return the xml of the marshalled object
     * @throws Exception if any error occurs during marshalling
     */
    private String marshal(Marshaller marshaller, Object object) throws Exception {

        StringWriter writer = new StringWriter();
        marshaller.setWriter(writer);
        marshaller.marshal(object);

        return writer.toString();
    }

    /**
     * Marshals the emails.
     *
     * @param marshaller the marshaller to use
     * @return the xml of the marshalled object
     * @throws Exception if any error occurs during marshalling
     */
    private String marshalEmails(Marshaller marshaller) throws Exception {
        return marshal(marshaller, emails);
    }

    /**
     * Creates new instance of Marshaller.
     *
     * @param mapping the mapping file
     * @return configured Marshaller instance
     * @throws org.exolab.castor.mapping.MappingException
     *          if any error occurs when loading mapping file
     */
    private Marshaller createMarshallerFromMapping(String mapping) throws MappingException {
        XMLContext xmlContext = createXmlContextFromMapping(mapping);

        Marshaller marshaller = xmlContext.createMarshaller();
        marshaller.setValidation(true);
        return marshaller;
    }

    /**
     * Creates a XMLContext with loaded mapping file.
     *
     * @param mappingPath the path to mapping file
     * @return configured XMLContext
     * @throws MappingException if any error occurs while loading mapping file.
     */
    private XMLContext createXmlContextFromMapping(String mappingPath) throws MappingException {
        InputStream mappingFile = getClass().getResourceAsStream(mappingPath);

        XMLContext xmlContext = new XMLContext();
        Mapping mapping = new Mapping();
        mapping.loadMapping(new InputSource(mappingFile));
        xmlContext.addMapping(mapping);
        return xmlContext;
    }

    /**
     * Creates mock {@link MarshalListener} instance.
     *
     * @return newly created mock instance
     */
    private MarshalListener createMockListener() {
        MarshalListener listener = createMock(MarshalListener.class);
        listener.preMarshal(anyObject(Object.class));
        expectLastCall().andReturn(true);
        expectLastCall().times(4);
        listener.postMarshal(anyObject(Object.class));
        expectLastCall().times(4);
        replay(listener);
        return listener;
    }
}
