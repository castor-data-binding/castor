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

import java.io.Reader;
import java.io.StringReader;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.castor.xml.InternalContext;
import org.castor.xml.XMLConfiguration;

/**
 * Test case for testing various pieces of functionality of {@link Unmarshaller}.
 */
public class TestUnmarshaller extends TestCase {
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
            unmarshaller.getProperty(XMLConfiguration.LENIENT_SEQUENCE_ORDER);
        assertNotNull(lenientSequenceValidation);
        assertEquals("false", lenientSequenceValidation);
        
        unmarshaller.setProperty(XMLConfiguration.LENIENT_SEQUENCE_ORDER, "true");
 
        lenientSequenceValidation = 
            unmarshaller.getProperty(XMLConfiguration.LENIENT_SEQUENCE_ORDER);
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
}
