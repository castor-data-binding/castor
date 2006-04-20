package xml;


import java.io.IOException;
import java.io.StringWriter;
import java.io.StringReader;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


public class TypeHandling
    extends CWTestCase
{


    public TypeHandling( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC01", "Test type handling" );
    }


    public void preExecute()
    {
        super.preExecute();
    }


    public void postExecute()
    {
        super.postExecute();
    }


    public boolean run( CWVerboseStream stream )
    {
        boolean result = true;

        try {
            Mapping      mapping;
            Marshaller   marshaller;
            Unmarshaller unmarshaller;
            StringWriter string;
            Types        original;
            Types        copy;

            mapping = new Mapping();
            mapping.loadMapping( getClass().getResource( XMLCategory.MappingFile ) );
            stream.writeVerbose( "Successfully loaded mapping file " + XMLCategory.MappingFile );

            original = new Types( true );
            string = new StringWriter();
            marshaller = new Marshaller( string );
            marshaller.setMapping( mapping );
            marshaller.marshal( original );
            stream.writeVerbose( "Marshalled: " + string.toString() );

            unmarshaller = new Unmarshaller( Types.class );
            unmarshaller.setMapping( mapping );
            copy = (Types) unmarshaller.unmarshal( new StringReader( string.toString() ) );
            stream.writeVerbose( "Unmarshalled: " + string.toString() );

            System.out.println( "Equals " + copy.equals( original ) );

        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }


}
