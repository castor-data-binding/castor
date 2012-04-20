/*
 * Copyright 2011 Jakub Narloch
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

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.easymock.EasyMock.*;

/**
 * Tests the {@link Marshaller} class.
 *
 * @author <a herf="mailto:jmnarloch AT gmail DOT com">Jakub Narloch</a>
 * @version 1.3.3
 * @since 1.3.3
 */
public class MarshallerTest extends BaseMarshallerTest {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String marshal(Marshaller marshaller, Object object) throws Exception {

        StringWriter writer = new StringWriter();
        marshaller.setWriter(writer);
        marshaller.marshal(object);

        return writer.toString();
    }
}
