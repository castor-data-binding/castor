package xml;

import java.net.URL;
import java.io.IOException;
import java.io.StringWriter;
import java.io.StringReader;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import junit.framework.*;

public class TypeHandlingTest
    extends TestCase
{
    
    public TypeHandlingTest(String name)
    {
        super(name);
    }
    
    public void test()
    {
        
        try {
            Mapping      mapping;
            Marshaller   marshaller;
            Unmarshaller unmarshaller;
            StringWriter string;
            Types        original;
            Types        copy;
            
            mapping = new Mapping();
           
             // mapping.loadMapping( getClass().getResource( "mapping.xml" ) ); do not work when the test is run from ant
            
            mapping.loadMapping("src/tests/xml/mapping.xml" );
            
            original = new Types( true );
            string = new StringWriter();
            marshaller = new Marshaller( string );
            marshaller.setMapping( mapping );
            marshaller.marshal( original );
            
            System.out.println("Marshalled: " + string.toString());
            unmarshaller = new Unmarshaller( Types.class );
            unmarshaller.setMapping( mapping );
            copy = (Types) unmarshaller.unmarshal( new StringReader( string.toString() ) );
           
            assert("The original and the copy are not the same",copy.equals( original ));

        } catch ( Exception except ) {
            fail( "Error: " + except );
            
        }
     }
}