package xml;


import java.io.IOException;
import java.io.StringWriter;
import java.io.Reader;
import java.io.InputStreamReader;
//import org.exolab.castor.tools.MappingTool;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 */
public class Mapping
    extends CWTestCase
{


    public Mapping( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC01", "Test mapping loader/generator" );
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
            /*
            org.exolab.castor.mapping.Mapping    mapping;

            mapping = new org.exolab.castor.mapping.Mapping();
            mapping.loadMapping( getClass().getResource( XMLTests.MappingFile ) );
            stream.writeVerbose( "Successfully loaded mapping file " + XMLTests.MappingFile );
            
            MappingTool  tool;
            StringWriter writer;
            String       fileCopy;

            writer = new StringWriter();
            tool = new MappingTool();
            tool.addClass( Root.class );
            tool.write( writer );
            fileCopy = writer.toString();

            Reader  reader;
            String  toolCopy;

            reader = new InputStreamReader( getClass().getResourceAsStream( XMLTests.MappingFile ) );
            writer = new StringWriter();
            while ( reader.ready() )
                writer.write( reader.read() );
            toolCopy = writer.toString();
            */
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }


}

