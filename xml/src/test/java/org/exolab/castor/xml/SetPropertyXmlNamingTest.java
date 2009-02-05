/*
 * Copyright 2009 Joachim Grueneis
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

import java.io.StringReader;
import java.io.StringWriter;

import org.xml.sax.InputSource;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author joachim
 *
 */
public class SetPropertyXmlNamingTest extends TestCase {
    
    private static final String RESULT2 =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<foo barBar=\"0\"/>";
    
    private static final String RESULT =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<setPropertyXmlNamingTest$Foo barBar=\"0\"/>";

    public void testSetXmlContextToMixed() throws Exception {
        XMLContext xmlContext = new XMLContext();
        xmlContext.setProperty("org.exolab.castor.xml.naming", "mixed");
        xmlContext.addClass(Foo.class);

        StringWriter writer = new StringWriter();
        Marshaller marshaller = xmlContext.createMarshaller();
        marshaller.setWriter(writer);
        marshaller.marshal(new Foo());

        assertEquals(RESULT, writer.toString());
    }
    
    public void testSetXmlContextToMixedWithUnmarshaller() throws Exception {
        XMLContext xmlContext = new XMLContext();
        xmlContext.setProperty("org.exolab.castor.xml.naming", "mixed");
        xmlContext.addClass(Foo.class);
        Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
        unmarshaller.setClass(Foo.class);

        StringReader sr = new StringReader(RESULT2);
        Foo f = (Foo) unmarshaller.unmarshal(new InputSource(sr));
        Assert.assertNotNull(f);
        Assert.assertEquals(0, f.barBar);
    }

    public void testSetMarshallerToMixed() throws Exception {
        XMLContext xmlContext = new XMLContext();
        xmlContext.addClass(Foo.class);

        StringWriter writer = new StringWriter();
        Marshaller marshaller = xmlContext.createMarshaller();
        marshaller.setWriter(writer);
        marshaller.setProperty("org.exolab.castor.xml.naming", "mixed");
        marshaller.marshal(new Foo());

        assertEquals(RESULT, writer.toString());
    }

    public void testSetNewMarshallerToMixed() throws Exception {
        StringWriter writer = new StringWriter();
        Marshaller marshaller = new Marshaller();
        marshaller.setWriter(writer);
        marshaller.setProperty("org.exolab.castor.xml.naming", "mixed");
        marshaller.marshal(new Foo());

        assertEquals(RESULT, writer.toString());
    }

    public static class Foo {
        public int barBar;
    }

}
