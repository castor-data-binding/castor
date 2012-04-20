package xml.schema;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.reader.SchemaReader;
import org.xml.sax.InputSource;

public class TestCastorReader {
     public static void main(String[] args)
     {
         try {
             InputSource source = new InputSource(TestCastorReader.class.getResource("test.xsd").toExternalForm());
             SchemaReader schemaReader = new SchemaReader(source);
             schemaReader.setCacheIncludedSchemas(true);

             Schema schema = schemaReader.read();
             System.out.println("**** Schema is loaded successfully");
             int tnum = schema.getSimpleTypes().size();
             System.out.println("**** Total " + tnum + " simply types in the schema.");

             tnum = schema.getComplexTypes().size();
             System.out.println("**** Total " + tnum + " complex types in the schema.");

         }
         catch (FileNotFoundException e) {
             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
         catch (IOException e) {
             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
     }
 }
