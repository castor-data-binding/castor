package xml;


import java.io.IOException;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 */
public class Inheritance
    extends CWTestCase
{


    public Inheritance( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC02", "Test inheritence of objects/elements" );
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
            Foo        foo;
            Bar        bar;
            Bar1       bar1;
            Bar2       bar2;

            foo = new Foo();
            bar = new Bar();
            bar.setBaz( "bzzzz" );
            foo.getBar().addElement( bar );
            bar1 = new Bar1();
            bar1.setBaz( "one" );
            bar1.setBaz1( 1 );
            foo.getBar().addElement( bar1 );
            bar2 = new Bar2();
            bar2.setBaz( "two" );
            bar2.setBaz2( true );
            foo.getBar().addElement( bar2 );


            mapping = new Mapping();
            mapping.loadMapping( getClass().getResource( XMLCategory.MappingFile ) );
            stream.writeVerbose( "Successfully loaded mapping file " + XMLCategory.MappingFile );
            
            marshaller = new Marshaller( new OutputStreamWriter( System.out ) );
            marshaller.setMapping( mapping );
            marshaller.marshal( foo );
            marshaller.marshal( bar );
            marshaller.marshal( bar1 );
            marshaller.marshal( bar2 );

            String xml;

            xml = "<foo><bar baz-0=\"bzzzz\"/><bar1 baz-1=\"1\" baz-0=\"one\"/><bar2 baz-2=\"true\" baz-0=\"two\"/></foo>";
            unmarshaller = new Unmarshaller( Foo.class );
            unmarshaller.setMapping( mapping );
            foo = (Foo) unmarshaller.unmarshal( new StringReader( xml ) );
            marshaller.marshal( foo );
            

        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }


}

