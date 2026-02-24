package org.exolab.castor.xml.dtd;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.exolab.castor.xml.schema.SchemaException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class ConverterTest {

  @Test
  public void testNonamespaces()
      throws SchemaException, DTDException, IOException, SAXException, URISyntaxException {

    Converter convertor = new Converter();

    URL in = getClass().getResource("test.dtd");

    File inFile = new File(in.toURI());

    String encoding = "US-ASCII";

    StringWriter writer = new StringWriter();
    convertor.process(new FileReader(inFile), writer, encoding, null,
        new HashMap<String, String>());

    String generatedSchema = writer.toString();
    System.out.println(generatedSchema);

    assertNotNull(generatedSchema);
    assertFalse(generatedSchema.contains("targetNameSpace"));
    assertFalse(generatedSchema.contains("xmlns:test=\"http://test\""));
    assertTrue(generatedSchema.contains("<element ref=\"description\"/>"));
  }

  @Test
  public void testWithTargetnamespace()
      throws SchemaException, DTDException, IOException, SAXException, URISyntaxException {

    Converter convertor = new Converter();

    URL in = getClass().getResource("test.dtd");

    File inFile = new File(in.toURI());

    String encoding = "US-ASCII";

    StringWriter writer = new StringWriter();

    HashMap<String, String> namespaces = new HashMap<String, String>();
    namespaces.put("test", "http://test");

    convertor.process(new FileReader(inFile), writer, encoding, "http://test", namespaces);

    String generatedSchema = writer.toString();

    System.out.println(generatedSchema);

    assertNotNull(generatedSchema);
    assertTrue(generatedSchema.contains("targetNamespace"));
    assertTrue(generatedSchema.contains("xmlns:test=\"http://test\""));
    assertTrue(generatedSchema.contains("<element ref=\"test:description\"/>"));

  }

}
