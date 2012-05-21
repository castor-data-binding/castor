package org.castor.xml.schema;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.reader.SchemaReader;
import org.junit.Test;
import org.xml.sax.InputSource;

public class TestCastorReader {
   
   @Test
   public void test() throws IOException {
      InputSource source = new InputSource(this.getClass().getResource("test.xsd").toExternalForm());
      SchemaReader schemaReader = new SchemaReader(source);
      schemaReader.setCacheIncludedSchemas(true);

      Schema schema = schemaReader.read();
      assertNotNull(schema);

      assertTrue(schema.getComplexTypes().size() > 0);
   }
}
