/*
 * Copyright 2007 Werner Guttmann
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

import junit.framework.Assert;
import junit.framework.TestCase;
import org.castor.test.entity.*;
import org.castor.xml.InternalContext;
import org.castor.xml.XMLProperties;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.util.ObjectFactory;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import static org.easymock.EasyMock.*;

/**
 * Test case for testing various pieces of functionality of {@link Unmarshaller}.
 */
public class TestUnmarshaller extends TestCase {

    /**
     * Represents the xml used for testing.
     */
    private static final String INPUT_STRING =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<tns:emails xmlns:tns=\"http://castor.org/email\">" +
                    "<tns:email><tns:from>from@castor.org</tns:from><tns:to>to@castor.org</tns:to>" +
                    "</tns:email></tns:emails>";

    /**
     * Represents the xml with additional attribute that is not mapped in Castor config.
     */
    private static final String EXTRA_ATTRIBUTES_STRING =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<tns:emails xmlns:tns=\"http://castor.org/email\">" +
                    "<tns:email status=\"deleted\"><tns:from>from@castor.org</tns:from><tns:to>to@castor.org</tns:to>" +
                    "</tns:email></tns:emails>";

    /**
     * Represents the xml with additional element that is not mapped in Castor config.
     */
    private static final String EXTRA_ELEMENTS_STRING =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<tns:emails xmlns:tns=\"http://castor.org/email\">" +
                    "<tns:email><tns:from>from@castor.org</tns:from><tns:to>to@castor.org</tns:to>" +
                    "<tns:date>2011-06-26</tns:date></tns:email></tns:emails>";

    /**
     * <p>Represents the path to mapping file.</p>
     */
    private static final String EMAIL_MAPPING_FILE = "/org/castor/test/entity/mapping-email.xml";

    /**
     * <p>Represents the path to mapping file.</p>
     */
    private static final String LIBRARY_MAPPING_FILE = "/org/castor/test/entity/mapping-library.xml";

    /**
	 * <p>Represents the xml that contains referencing elements with all referenced ids defined in xml.</p>
	 */
	protected static final String ID_REF_INPUT_STRING =
			"<tns:Library xmlns:tns=\"http://castor.org/library\">" +
					"<tns:Authors><tns:Author id=\"author1\">" +
					"<tns:Name>Carol Dikens</tns:Name></tns:Author></tns:Authors>" +
					"<tns:Books><tns:Book><tns:Title>Oliver Twist</tns:Title>" +
					"<tns:Author idref=\"author1\"/></tns:Book></tns:Books>" +
					"</tns:Library>";

	/**
	 * <p>Represents the xml that contains referencing elements with not all referenced ids defined in xml.</p>
	 */
	protected static final String EXTERNAL_ID_REF_INPUT_STRING =
			"<tns:Library xmlns:tns=\"http://castor.org/library\">" +
					"<tns:Authors><tns:Author id=\"author1\">" +
					"<tns:Name>Carol Dikens</tns:Name></tns:Author></tns:Authors>" +
					"<tns:Books><tns:Book><tns:Title>Oliver Twist</tns:Title>" +
					"<tns:Author idref=\"author2\"/></tns:Book></tns:Books>" +
					"</tns:Library>";

    private static final String testXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><UnmarshalFranz content=\"Bla Bla Bla\" />";
    private Reader _reader;
    private InternalContext _internalContext;

    /**
     * The Unmarshaller tests need an internal context and an input reader.
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        XMLContext xmlContext = new XMLContext();
        _internalContext = xmlContext.getInternalContext();
        _reader = new StringReader(testXML);
    }

    /**
     * Closing the reader.
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        _reader.close();
        _reader = null;
    }

    /**
     * Tests usage of get-/setProperty() methods.
    */
    public void testSetProperty() {

        XMLContext xmlContext = new XMLContext();
        Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
        assertNotNull(unmarshaller);

        String lenientSequenceValidation =
            unmarshaller.getProperty(XMLProperties.LENIENT_SEQUENCE_ORDER);
        assertNotNull(lenientSequenceValidation);
        assertEquals("false", lenientSequenceValidation);

        unmarshaller.setProperty(XMLProperties.LENIENT_SEQUENCE_ORDER, "true");

        lenientSequenceValidation =
            unmarshaller.getProperty(XMLProperties.LENIENT_SEQUENCE_ORDER);
        assertNotNull(lenientSequenceValidation);
        assertEquals("true", lenientSequenceValidation);
    }

    /**
     * Creates an Unmarshaller instance without any argument; sets the
     * root class and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerNoArgs() throws Exception {
        Unmarshaller u = new Unmarshaller();
        u.setClass(UnmarshalFranz.class);
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance without any argument; sets the
     * root object and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerNoArgs2() throws Exception {
        Unmarshaller u = new Unmarshaller();
        u.setObject(new UnmarshalFranz());
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance with root class
     * and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerClassArg() throws Exception {
        Unmarshaller u = new Unmarshaller(UnmarshalFranz.class);
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance with root class
     * and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerClassArgNull() throws Exception {
        Unmarshaller u = new Unmarshaller((Class)null);
        u.setClass(UnmarshalFranz.class);
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance with context only;
     * sets root class and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxArg() throws Exception {
        Unmarshaller u = new Unmarshaller(_internalContext);
        u.setClass(UnmarshalFranz.class);
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance with context only;
     * sets root class and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxArgNull() throws Exception {
        try {
            new Unmarshaller((InternalContext)null);
            Assert.fail("It must not be possible to instantiate Unmarshaller with internalContext == null");
        } catch (IllegalArgumentException e) {
            // expected!
        }
    }

    /**
     * Creates an Unmarshaller instance with context and root class
     * and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxClassArg() throws Exception {
        Unmarshaller u = new Unmarshaller(_internalContext, UnmarshalFranz.class);
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance with context and root class
     * and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxClassArgNullNull() throws Exception {
        try {
            new Unmarshaller((InternalContext)null, (Class)null);
            Assert.fail("It must not be possible to instantiate Unmarshaller with internalContext == null");
        } catch (IllegalArgumentException e) {
            // expected!
        }
    }

    /**
     * Creates an Unmarshaller instance with context and root class
     * and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxClassArgNull() throws Exception {
        Unmarshaller u = new Unmarshaller(_internalContext, (Class)null);
        u.setClass(UnmarshalFranz.class);
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance with context, class and class loader
     * arguments and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxClassClassloaderArg() throws Exception {
        Unmarshaller u = new Unmarshaller(_internalContext, UnmarshalFranz.class, UnmarshalFranz.class.getClassLoader());
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance with context, class and class loader
     * arguments and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxClassClassloaderArgNullNullNull() throws Exception {
        try {
            new Unmarshaller((InternalContext)null, (Class)null, (ClassLoader)null);
            Assert.fail("It must not be possible to instantiate Unmarshaller with internalContext == null");
        } catch (IllegalArgumentException e) {
            // expected!
        }
    }

    /**
     * Creates an Unmarshaller instance with context, class and class loader
     * arguments and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxClassClassloaderArgNullNull() throws Exception {
        Unmarshaller u = new Unmarshaller(_internalContext, (Class)null, (ClassLoader)null);
        u.setClass(UnmarshalFranz.class);
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance with context, class and class loader
     * arguments and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxClassClassloaderArgNull() throws Exception {
        Unmarshaller u = new Unmarshaller(_internalContext, UnmarshalFranz.class, (ClassLoader)null);
        u.setClass(UnmarshalFranz.class);
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance with an root object instance
     * and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerObjectArg() throws Exception {
        Unmarshaller u = new Unmarshaller(new UnmarshalFranz());
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance with an root object instance
     * and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerObjectArgNull() throws Exception {
        Unmarshaller u = new Unmarshaller((Object)null);
        u.setObject(new UnmarshalFranz());
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance withcontext and an object instance argument
     *  and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxObjectArg() throws Exception {
        Unmarshaller u = new Unmarshaller(_internalContext, new UnmarshalFranz());
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Creates an Unmarshaller instance withcontext and an object instance argument
     *  and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxObjectArgNullNull() throws Exception {
        try {
            new Unmarshaller((InternalContext)null, (Object)null);
            Assert.fail("It must not be possible to instantiate Unmarshaller with internalContext == null");
        } catch (IllegalArgumentException e) {
            // expected!
        }
    }

    /**
     * Creates an Unmarshaller instance withcontext and an object instance argument
     *  and calls unmarshal.
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshallerCtxObjectArgNull() throws Exception {
        Unmarshaller u = new Unmarshaller(_internalContext, (Object)null);
        u.setObject(new UnmarshalFranz());
        UnmarshalFranz f = (UnmarshalFranz)u.unmarshal(_reader);
        Assert.assertNotNull(f);
        Assert.assertEquals("Bla Bla Bla", f.getContent());
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setWhitespacePreserve(boolean)} is set to <code>true</code>.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testWhitespacePreserveTrue() throws Exception {

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(EMAIL_MAPPING_FILE);
        unmarshaller.setWhitespacePreserve(true);
        Object result = unmarshalEmails(unmarshaller);
        testEmails(result);
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setWhitespacePreserve(boolean)} is set to
     * <code>false</code>.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testWhitespacePreserveFalse() throws Exception {

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(EMAIL_MAPPING_FILE);
        unmarshaller.setWhitespacePreserve(false);
        Object result = unmarshalEmails(unmarshaller);
        testEmails(result);
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setIgnoreExtraAttributes(boolean)} is set to
     * <code>true</code>.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testIgnoreExtraAttributesTrue() throws Exception {

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(EMAIL_MAPPING_FILE);
        unmarshaller.setIgnoreExtraAttributes(true);
        Object result = unmarshal(unmarshaller, EXTRA_ATTRIBUTES_STRING);
        testEmails(result);
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setIgnoreExtraAttributes(boolean)} is set to
     * <code>false</code>. </p> {@link MarshalException} is expected.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testIgnoreExtraAttributesFalse() throws Exception {

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(EMAIL_MAPPING_FILE);
        unmarshaller.setIgnoreExtraAttributes(false);

        try {
            unmarshal(unmarshaller, EXTRA_ATTRIBUTES_STRING);
            fail("MarshalException was expected.");
        } catch (MarshalException e) {
            // test passed
        }
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setIgnoreExtraElements(boolean)} is set to
     * <code>true</code>.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testIgnoreExtraElementsTrue() throws Exception {

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(EMAIL_MAPPING_FILE);
        unmarshaller.setIgnoreExtraElements(true);

        Object result = unmarshal(unmarshaller, EXTRA_ELEMENTS_STRING);
        testEmails(result);
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setIgnoreExtraElements(boolean)} is set to
     * <code>false</code>. </p> {@link MarshalException} is expected.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testIgnoreExtraElementsFalse() throws Exception {

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(EMAIL_MAPPING_FILE);
        unmarshaller.setIgnoreExtraElements(false);

        try {
            unmarshal(unmarshaller, EXTRA_ELEMENTS_STRING);
            fail("MarshalException was expected.");
        } catch (MarshalException exc) {
            // test passed
        }
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setObject(Object)} is set.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testObject() throws Exception {

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(EMAIL_MAPPING_FILE);

        Emails emails = new Emails();
        unmarshaller.setObject(emails);
        Object result = unmarshalEmails(unmarshaller);

        testEmails(result);
        assertSame("Result Emails is different object.", emails, result);
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setClearCollections(boolean)} is set to <code>true</code>.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testClearCollectionsTrue() throws Exception {

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(EMAIL_MAPPING_FILE);

        Emails emails = new Emails();
        emails.setEmail(new Email[]{new Email()});
        unmarshaller.setObject(emails);
        unmarshaller.setClearCollections(true);
        Object result = unmarshalEmails(unmarshaller);

        assertSame("Result Emails is different object.", emails, result);
        assertEquals("Result Emails has incorrect number of Email.", 1, ((Emails) result).getEmail().length);
        testEmails(result);
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setClearCollections(boolean)} is set to <code>false</code>.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testClearCollectionsFalse() throws Exception {

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(EMAIL_MAPPING_FILE);
        Emails emails = new Emails();
        emails.setEmail(new Email[]{new Email(), null});
        unmarshaller.setObject(emails);
        unmarshaller.setValidation(false);
        unmarshaller.setClearCollections(false);

        Object result = unmarshalEmails(unmarshaller);

        assertSame("Result Emails is different object.", emails, result);
        assertEquals("Result Emails has incorrect number of Email.", 3, ((Emails) result).getEmail().length);
        assertNull("Null Email was expected.", emails.getEmail()[1]);

        assertEquals("Emails has invalid sender.", "from@castor.org", emails.getEmail()[2].getFrom());
        assertEquals("Emails has invalid recipient.", "to@castor.org", emails.getEmail()[2].getTo());
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setUnmarshalListener(org.castor.xml.UnmarshalListener)}
     * is set.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testUnmarshalListener() throws Exception {
        org.castor.xml.UnmarshalListener listener = createMockListener();
        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(EMAIL_MAPPING_FILE);
        unmarshaller.setUnmarshalListener(listener);
        Object result = unmarshalEmails(unmarshaller);
        testEmails(result);
        verify(listener);
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setObjectFactory(org.exolab.castor.util.ObjectFactory)}
     * is set.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testObjectFactory() throws Exception {
        Emails emails = new Emails();

        ObjectFactory objectFactory = createMock(ObjectFactory.class);
        objectFactory.createInstance(Emails.class, null, null);
        expectLastCall().andReturn(emails);

        replay(objectFactory);

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(EMAIL_MAPPING_FILE);
        unmarshaller.setObjectFactory(objectFactory);
        Object result = unmarshalEmails(unmarshaller);
        testEmails(result);

        verify(objectFactory);
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setIDResolver(IDResolver)} is set.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testIDResolver() throws Exception {

        IDResolver idResolver = createMock(IDResolver.class);
        replay(idResolver);

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(LIBRARY_MAPPING_FILE);
        unmarshaller.setValidation(false);
        unmarshaller.setIDResolver(idResolver);
        Object result = unmarshal(unmarshaller, ID_REF_INPUT_STRING);

        testLibrary(result);

        verify(idResolver);
    }

    /**
     * Tests the Unmarshaller when the {@link Unmarshaller#setIDResolver(IDResolver)} is set.
     *
     * @throws Exception in case of unmarshal problems
     */
    public void testIDResolverExternalId() throws Exception {
        Author author = new Author();
        author.setId("author2");
        author.setName("Carol Dikens");

        IDResolver idResolver = createMock(IDResolver.class);
        idResolver.resolve("author2");
        expectLastCall().andReturn(author);
        replay(idResolver);

        Unmarshaller unmarshaller = createUnmarsahllerFromMapping(LIBRARY_MAPPING_FILE);
        unmarshaller.setValidation(false);
        unmarshaller.setIDResolver(idResolver);
        Object result = unmarshal(unmarshaller, EXTERNAL_ID_REF_INPUT_STRING);

        testLibrary(result);

        verify(idResolver);
    }

    /**
     * Creates new instance of Unmarshaller.
     *
     * @param mapping the path to the mapping file
     *
     * @return configured Unmarshaller instance
     *
     * @throws MappingException if any error occurs when loading mapping file
     */
    private Unmarshaller createUnmarsahllerFromMapping(String mapping) throws MappingException {
        XMLContext xmlContext = createXmlContextFromMapping(mapping);

        Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
        unmarshaller.setValidation(true);
        return unmarshaller;
    }

    /**
     * Creates a XMLContext with loaded mapping file.
     *
     * @param mappingPath the path to the mapping file
     *
     * @return configured XMLContext
     *
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
     * Unmarshals the emails instance.
     *
     * @param unmarshaller the unmarshaller to use
     *
     * @return the unmarshaled object instance
     *
     * @throws Exception if any error exception
     */
    private Object unmarshalEmails(Unmarshaller unmarshaller) throws Exception {
        return unmarshal(unmarshaller, INPUT_STRING);
    }

    /**
     * Unmarshals the emails instance.
     *
     * @param unmarshaller unmarshaller to use
     * @param xml          the xml to unmarshall
     *
     * @return the unmarshaled instance
     *
     * @throws Exception if any error exception
     */
    private Object unmarshal(Unmarshaller unmarshaller, String xml) throws Exception {
        return unmarshaller.unmarshal(new StringReader(xml));
    }

    /**
     * Asserts the emails instance.
     *
     * @param result the object to assert
     */
    private void testEmails(Object result) {
        Emails emails = (Emails) result;
        assertNotNull("Emails were null.", emails);
        Email email = emails.getEmail()[0];
        assertNotNull("Email was null.", emails);

        assertEquals("Emails has invalid sender.", "from@castor.org", email.getFrom());
        assertEquals("Emails has invalid recipient.", "to@castor.org", email.getTo());
    }

    /**
	 * Creates mock {@link org.castor.xml.UnmarshalListener} instance.
	 *
	 * @return newly created mock instance
	 */
	private org.castor.xml.UnmarshalListener createMockListener() {
		org.castor.xml.UnmarshalListener listener = createMock(org.castor.xml.UnmarshalListener.class);
        listener.initialized(anyObject(Object.class), anyObject(Object.class));
        expectLastCall().times(2);
        listener.fieldAdded(anyObject(String.class), anyObject(Object.class), anyObject(Object.class));
        expectLastCall().times(3);
        listener.attributesProcessed(anyObject(Object.class), anyObject(Object.class));
        expectLastCall().times(2);
        listener.unmarshalled(anyObject(Object.class), anyObject(Object.class));
        expectLastCall().times(4);
        replay(listener);
		return listener;
	}

	/**
	 * Tests the unnarshaled {@link Library} instance.
	 *
	 * @param result the unmarshaled {@link Library} to test
	 */
	private void testLibrary(Object result) {
		Library library = (Library) result;

		assertNotNull("Library is null.", library);
		assertEquals("Library has incorrect number of authors", 1, library.getAuthors().getAuthorCount());
		assertEquals("Library has incorrect number of books", 1, library.getBooks().getBookCount());

		Book book = library.getBooks().getBook(0);
		assertEquals("Book has incorrect title", "Oliver Twist", book.getTitle());
	}
}
