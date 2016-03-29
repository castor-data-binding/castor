/*
 * Copyright 2007 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.exolab.castor.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Reader;
import java.io.StringReader;

import org.castor.xml.InternalContext;
import org.castor.xml.XMLProperties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for testing various pieces of functionality of {@link Unmarshaller}.
 */
public class TestUnmarshaller {

  private static final String testXML =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?><UnmarshalFranz content=\"Bla Bla Bla\" />";

  private Reader _reader;

  private InternalContext _internalContext;

  /**
   * The Unmarshaller tests need an internal context and an input reader.
   * 
   * @see junit.framework.TestCase#setUp()
   */
  @Before
  public void setUp() {
    XMLContext xmlContext = new XMLContext();
    _internalContext = xmlContext.getInternalContext();
    _reader = new StringReader(testXML);
  }

  /**
   * Closing the reader.
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  @After
  public void tearDown() throws Exception {
    _reader.close();
    _reader = null;
  }

  /**
   * Tests usage of get-/setProperty() methods.
   */
  @Test
  public void testSetProperty() {

    XMLContext xmlContext = new XMLContext();
    Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
    assertNotNull(unmarshaller);

    String lenientSequenceValidation =
        unmarshaller.getProperty(XMLProperties.LENIENT_SEQUENCE_ORDER);
    assertNotNull(lenientSequenceValidation);
    assertEquals("false", lenientSequenceValidation);

    unmarshaller.setProperty(XMLProperties.LENIENT_SEQUENCE_ORDER, "true");

    lenientSequenceValidation = unmarshaller.getProperty(XMLProperties.LENIENT_SEQUENCE_ORDER);
    assertNotNull(lenientSequenceValidation);
    assertEquals("true", lenientSequenceValidation);
  }

  /**
   * Creates an Unmarshaller instance without any argument; sets the root class and calls unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerNoArgs() throws Exception {
    Unmarshaller u = new Unmarshaller();
    u.setClass(UnmarshalFranz.class);
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance without any argument; sets the root object and calls
   * unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerNoArgs2() throws Exception {
    Unmarshaller u = new Unmarshaller();
    u.setObject(new UnmarshalFranz());
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance with root class and calls unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerClassArg() throws Exception {
    Unmarshaller u = new Unmarshaller(UnmarshalFranz.class);
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance with root class and calls unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerClassArgNull() throws Exception {
    Unmarshaller u = new Unmarshaller((Class<?>) null);
    u.setClass(UnmarshalFranz.class);
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance with context only; sets root class and calls unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerCtxArg() throws Exception {
    Unmarshaller u = new Unmarshaller(_internalContext);
    u.setClass(UnmarshalFranz.class);
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance with context only; sets root class and calls unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUnmarshallerCtxArgNull() throws Exception {
    new Unmarshaller((InternalContext) null);
    Assert.fail("It must not be possible to instantiate Unmarshaller with internalContext == null");
  }

  /**
   * Creates an Unmarshaller instance with context and root class and calls unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerCtxClassArg() throws Exception {
    Unmarshaller u = new Unmarshaller(_internalContext, UnmarshalFranz.class);
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance with context and root class and calls unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUnmarshallerCtxClassArgNullNull() throws Exception {
    new Unmarshaller((InternalContext) null, (Class<?>) null);
    Assert.fail("It must not be possible to instantiate Unmarshaller with internalContext == null");
  }

  /**
   * Creates an Unmarshaller instance with context and root class and calls unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerCtxClassArgNull() throws Exception {
    Unmarshaller u = new Unmarshaller(_internalContext, (Class<?>) null);
    u.setClass(UnmarshalFranz.class);
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance with context, class and class loader arguments and calls
   * unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerCtxClassClassloaderArg() throws Exception {
    Unmarshaller u = new Unmarshaller(_internalContext, UnmarshalFranz.class,
        UnmarshalFranz.class.getClassLoader());
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance with context, class and class loader arguments and calls
   * unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUnmarshallerCtxClassClassloaderArgNullNullNull() throws Exception {
    new Unmarshaller((InternalContext) null, (Class<?>) null, (ClassLoader) null);
    Assert.fail("It must not be possible to instantiate Unmarshaller with internalContext == null");
  }

  /**
   * Creates an Unmarshaller instance with context, class and class loader arguments and calls
   * unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerCtxClassClassloaderArgNullNull() throws Exception {
    Unmarshaller u = new Unmarshaller(_internalContext, (Class<?>) null, (ClassLoader) null);
    u.setClass(UnmarshalFranz.class);
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance with context, class and class loader arguments and calls
   * unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerCtxClassClassloaderArgNull() throws Exception {
    Unmarshaller u = new Unmarshaller(_internalContext, UnmarshalFranz.class, (ClassLoader) null);
    u.setClass(UnmarshalFranz.class);
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance with an root object instance and calls unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerObjectArg() throws Exception {
    Unmarshaller u = new Unmarshaller(new UnmarshalFranz());
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance with an root object instance and calls unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerObjectArgNull() throws Exception {
    Unmarshaller u = new Unmarshaller((Object) null);
    u.setObject(new UnmarshalFranz());
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance withcontext and an object instance argument and calls
   * unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerCtxObjectArg() throws Exception {
    Unmarshaller u = new Unmarshaller(_internalContext, new UnmarshalFranz());
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }

  /**
   * Creates an Unmarshaller instance withcontext and an object instance argument and calls
   * unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUnmarshallerCtxObjectArgNullNull() throws Exception {
    new Unmarshaller((InternalContext) null, (Object) null);
    Assert.fail("It must not be possible to instantiate Unmarshaller with internalContext == null");
  }

  /**
   * Creates an Unmarshaller instance withcontext and an object instance argument and calls
   * unmarshal.
   * 
   * @throws Exception in case of unmarshal problems
   */
  @Test
  public void testUnmarshallerCtxObjectArgNull() throws Exception {
    Unmarshaller u = new Unmarshaller(_internalContext, (Object) null);
    u.setObject(new UnmarshalFranz());
    UnmarshalFranz f = (UnmarshalFranz) u.unmarshal(_reader);
    Assert.assertNotNull(f);
    Assert.assertEquals("Bla Bla Bla", f.getContent());
  }
}
