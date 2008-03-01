/*
 * Copyright 2008 Joachim Grueneis
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
package org.castor.xmlctf.bestpractise;

import java.io.InputStream;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.InputSource;

/**
 * @author Joachim Grueneis, jgrueneis AT codehaus DOT org
 * @version $Id$
 * 
 */
public class XmlContextTest extends TestCase {
    private static final Log LOG = LogFactory.getLog(XmlContextTest.class);

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testWithMapping() {
        try {
            XMLContext context = new XMLContext();
            InputStream strmMapping = this.getClass().getClassLoader().getResourceAsStream("withmapping-mapping.xml");
            Assert.assertNotNull("InputStream of mapping must not be null", strmMapping);
            InputSource srcMapping = new InputSource(strmMapping);
            Assert.assertNotNull("InputSource of mapping must not be null", srcMapping);
            Mapping mapping = new Mapping();
            mapping.loadMapping(srcMapping);
            context.addMapping(mapping);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream("withmapping-input.xml");
            Assert.assertNotNull("InputStream must not be null", stream);
            InputSource is = new InputSource(stream);
            Assert.assertNotNull("Inputstream must not be null", is);
            Object o = unmarshaller.unmarshal(is);
            Assert.assertNotNull("Result of unmarshal must not be null", o);
        } catch (MappingException e) {
            LOG.error("Failed with exception: " + e, e);
            Assert.fail("Failed with exception: " + e);
        } catch (MarshalException e) {
            LOG.error("Failed with exception: " + e, e);
            Assert.fail("Failed with exception: " + e);
        } catch (ValidationException e) {
            LOG.error("Failed with exception: " + e, e);
            Assert.fail("Failed with exception: " + e);
        }
    }

    public void testLoadPackage() {
        try {
            XMLContext context = new XMLContext();
            context.addPackage("org.castor.xmlctf.bestpractise.genpackage");
            // context.getInternalContext().getXMLClassDescriptorResolver().getDescriptorCache();
            Unmarshaller u = context.createUnmarshaller();
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream("genpackage-input.xml");
            Assert.assertNotNull("InputStream must not be null", stream);
            InputSource is = new InputSource(stream);
            Assert.assertNotNull("Inputstream must not be null", is);
            Object o = u.unmarshal(is);
            Assert.assertNotNull("Result of unmarshal must not be null", o);
        } catch (ResolverException e) {
            LOG.error("Failed with exception: " + e, e);
            Assert.fail("Failed with exception: " + e);
        } catch (MarshalException e) {
            LOG.error("Failed with exception: " + e, e);
            Assert.fail("Failed with exception: " + e);
        } catch (ValidationException e) {
            LOG.error("Failed with exception: " + e, e);
            Assert.fail("Failed with exception: " + e);
        }
    }
}
