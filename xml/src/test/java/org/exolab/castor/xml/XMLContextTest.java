package org.exolab.castor.xml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.castor.test.entity.Entity;
import org.castor.xml.AbstractInternalContext;
import org.castor.xml.InternalContext;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.tools.MappingTool;
import org.exolab.castor.util.ChangeLog2XML;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

public class XMLContextTest {

  private static final String MAPPING_FILE = "org/castor/test/entity/mapping.xml";
  private XMLContext xmlContext;

  @Before
  public void setUp() {
    xmlContext = new XMLContext();
  }

  /**
   * Test default constructor creates valid XMLContext
   */
  @Test
  public void testDefaultConstructor() {
    XMLContext context = new XMLContext();
    assertNotNull(context);
    assertNotNull(context.getInternalContext());
  }

  /**
   * Test {@link AbstractInternalContext} by providing a generated package.
   *
   * @throws Exception
   */
  @Test
  public void testXMLContextByPackage() throws Exception {
    XMLContext context = new XMLContext();
    context.addPackage("org.castor.test.entity");
    assertNotNull(context);

    Unmarshaller unmarshaller = context.createUnmarshaller();
    assertNotNull(unmarshaller);

    unmarshaller.setClass(Entity.class);
    String resource = getResource("org/castor/test/entity/input.xml");
    InputSource source = new InputSource(resource);
    Entity entity = (Entity) unmarshaller.unmarshal(source);
    assertNotNull(entity);
  }

  /**
   * Test XMLContext with a mapping file.
   *
   * @throws Exception
   */
  @Test
  public void testXMLContextByMapping() throws Exception {

    XMLContext xmlContext = new XMLContext();
    Mapping mapping = xmlContext.createMapping();
    mapping.loadMapping(new InputSource(getResource(MAPPING_FILE)));

    xmlContext.addMapping(mapping);

    Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
    assertNotNull(unmarshaller);

    unmarshaller.setClass(Entity.class);
    String resource = getResource("org/castor/test/entity/input.xml");
    InputSource source = new InputSource(resource);
    Entity entity = (Entity) unmarshaller.unmarshal(source);
    assertNotNull(entity);
  }

  /**
   * Test addClass method
   */
  @Test
  public void testAddClass() throws ResolverException {
    XMLContext context = new XMLContext();
    context.addClass(Entity.class);
    assertNotNull(context);
  }

  /**
   * Test addClasses method with array
   */
  @Test
  public void testAddClasses() throws ResolverException {
    XMLContext context = new XMLContext();
    Class<?>[] classes = { Entity.class };
    context.addClasses(classes);
    assertNotNull(context);
  }

  /**
   * Test addClasses with multiple classes
   */
  @Test
  public void testAddMultipleClasses() throws ResolverException {
    XMLContext context = new XMLContext();
    Class<?>[] classes = { Entity.class, String.class };
    context.addClasses(classes);
    assertNotNull(context);
  }

  /**
   * Test addClasses with empty array
   */
  @Test
  public void testAddClassesEmpty() throws ResolverException {
    XMLContext context = new XMLContext();
    Class<?>[] classes = {};
    context.addClasses(classes);
    assertNotNull(context);
  }

  /**
   * Test addPackage method
   */
  @Test
  public void testAddPackage() throws ResolverException {
    XMLContext context = new XMLContext();
    context.addPackage("org.castor.test.entity");
    assertNotNull(context);
  }

  /**
   * Test addPackages method with array
   */
  @Test
  public void testAddPackages() throws ResolverException {
    XMLContext context = new XMLContext();
    String[] packages = { "org.castor.test.entity" };
    context.addPackages(packages);
    assertNotNull(context);
  }

  /**
   * Test addPackages with multiple packages
   */
  @Test
  public void testAddMultiplePackages() throws ResolverException {
    XMLContext context = new XMLContext();
    String[] packages = { "org.castor.test.entity", "java.lang" };
    context.addPackages(packages);
    assertNotNull(context);
  }

  /**
   * Test addPackages with empty array
   */
  @Test
  public void testAddPackagesEmpty() throws ResolverException {
    XMLContext context = new XMLContext();
    String[] packages = {};
    context.addPackages(packages);
    assertNotNull(context);
  }

  /**
   * Test createMapping method
   */
  @Test
  public void testCreateMapping() {
    Mapping mapping = xmlContext.createMapping();
    assertNotNull(mapping);
  }

  /**
   * Test createMarshaller method
   */
  @Test
  public void testCreateMarshaller() {
    Marshaller marshaller = xmlContext.createMarshaller();
    assertNotNull(marshaller);
  }

  /**
   * Test createUnmarshaller method
   */
  @Test
  public void testCreateUnmarshaller() {
    Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
    assertNotNull(unmarshaller);
  }

  /**
   * Test creating multiple marshallers
   */
  @Test
  public void testCreateMultipleMarshallers() {
    Marshaller m1 = xmlContext.createMarshaller();
    Marshaller m2 = xmlContext.createMarshaller();
    assertNotNull(m1);
    assertNotNull(m2);
  }

  /**
   * Test creating multiple unmarshallers
   */
  @Test
  public void testCreateMultipleUnmarshallers() {
    Unmarshaller u1 = xmlContext.createUnmarshaller();
    Unmarshaller u2 = xmlContext.createUnmarshaller();
    assertNotNull(u1);
    assertNotNull(u2);
  }

  /**
   * Test setProperty with String value
   */
  @Test
  public void testSetPropertyString() {
    xmlContext.setProperty("test.property", "value");
    assertNotNull(xmlContext);
  }

  /**
   * Test setProperty with boolean value
   */
  @Test
  public void testSetPropertyBoolean() {
    xmlContext.setProperty("test.boolean", true);
    assertNotNull(xmlContext);
  }

  /**
   * Test setProperty with Object value
   */
  @Test
  public void testSetPropertyObject() {
    Object obj = new Object();
    xmlContext.setProperty("test.object", obj);
    assertNotNull(xmlContext);
  }

  /**
   * Test setProperty with null value
   */
  @Test
  public void testSetPropertyNull() {
    xmlContext.setProperty("test.null", (Object) null);
    assertNotNull(xmlContext);
  }

  /**
   * Test setProperty with false boolean
   */
  @Test
  public void testSetPropertyBooleanFalse() {
    xmlContext.setProperty("test.false", false);
    assertNotNull(xmlContext);
  }

  /**
   * Test getProperty method
   */
  @Test
  public void testGetProperty() {
    xmlContext.setProperty("test.key", "test.value");
    Object value = xmlContext.getProperty("test.key");
    assertEquals("test.value", value);
  }

  /**
   * Test getProperty with non-existent key
   */
  @Test
  public void testGetPropertyNonExistent() {
    Object value = xmlContext.getProperty("non.existent.key");
    assertNull(value);
  }

  /**
   * Test getInternalContext method
   */
  @Test
  public void testGetInternalContext() {
    InternalContext internalContext = xmlContext.getInternalContext();
    assertNotNull(internalContext);
  }

  /**
   * Test setClassLoader method
   */
  @Test
  public void testSetClassLoader() {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    xmlContext.setClassLoader(classLoader);
    assertNotNull(xmlContext);
  }

  /**
   * Test setClassLoader with current thread context loader
   */
  @Test
  public void testSetClassLoaderCurrentThread() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    xmlContext.setClassLoader(classLoader);
    assertNotNull(xmlContext);
  }

  /**
   * Test createMappingTool method
   */
  @Test
  public void testCreateMappingTool() {
    MappingTool mappingTool = xmlContext.createMappingTool();
    assertNotNull(mappingTool);
  }

  /**
   * Test createChangeLog2XML method
   */
  @Test
  public void testCreateChangeLog2XML() {
    ChangeLog2XML changeLog2XML = xmlContext.createChangeLog2XML();
    assertNotNull(changeLog2XML);
  }

  /**
   * Test property setting and retrieval
   */
  @Test
  public void testPropertySetAndGet() {
    String key = "test.property.key";
    String value = "test.property.value";
    xmlContext.setProperty(key, value);
    Object retrieved = xmlContext.getProperty(key);
    assertEquals(value, retrieved);
  }

  /**
   * Test multiple property settings
   */
  @Test
  public void testMultipleProperties() {
    xmlContext.setProperty("prop1", "value1");
    xmlContext.setProperty("prop2", "value2");
    xmlContext.setProperty("prop3", "value3");

    assertEquals("value1", xmlContext.getProperty("prop1"));
    assertEquals("value2", xmlContext.getProperty("prop2"));
    assertEquals("value3", xmlContext.getProperty("prop3"));
  }

  /**
   * Test property overwrite
   */
  @Test
  public void testPropertyOverwrite() {
    xmlContext.setProperty("key", "value1");
    assertEquals("value1", xmlContext.getProperty("key"));
    xmlContext.setProperty("key", "value2");
    assertEquals("value2", xmlContext.getProperty("key"));
  }

  /**
   * Test context state consistency
   */
  @Test
  public void testContextStateConsistency() {
    XMLContext context1 = new XMLContext();
    XMLContext context2 = new XMLContext();
    assertNotNull(context1.getInternalContext());
    assertNotNull(context2.getInternalContext());
  }

  /**
   * Returns absolute path for resource.
   *
   * @param resource Relative path to resource
   *
   * @return Absolute path to resource.
   */
  private String getResource(final String resource) {
    ClassLoader loader = getClass().getClassLoader();
    java.net.URL url = loader.getResource(resource);
    if (url == null) {
      throw new RuntimeException("Resource not found: " + resource);
    }
    return url.toExternalForm();
  }
}
