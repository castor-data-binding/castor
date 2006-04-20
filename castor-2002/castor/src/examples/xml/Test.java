package xml;


//import myapp.*;
import java.io.PrintWriter;
import org.xml.sax.InputSource;
import org.exolab.castor.util.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;



public class Test
{


    public static final String ProductFile = "product.xml";
    public static final String Usage = "Usage: test.sh xml [xml|perf]";


    public static void main( String[] args )
    {
	PrintWriter   logger;
	boolean       runPerf = false;
	boolean       runXml = false;
	
	logger = Logger.getSystemLogger();
	if ( args.length < 1 ) {
	    System.out.println( Usage );
	    System.exit( 1 );
	}
	if ( args[ 0 ].equalsIgnoreCase( "perf" ) )
	    runPerf = true;
	else if ( args[ 0 ].equalsIgnoreCase( "xml" ) )
	    runXml = true;
	else {
	    System.out.println( Usage );
	    System.exit( 1 );
	}

	try {
	    if ( runXml ) {
		InputSource  source;
		Unmarshaller umrs;
		Product      product;

		source = new InputSource( Test.class.getResource( ProductFile ).toString() );
		umrs = new Unmarshaller( Product.class );
		umrs.setLogWriter( logger );
		product = (Product) umrs.unmarshal( source );
		Marshaller.marshal( product, new PrintWriter( System.out, true ) );
	    }

	    if ( runPerf ) {
	    }

	} catch ( Exception except ) {
	    logger.println( except );
	    except.printStackTrace( logger );
	}
    }



}


