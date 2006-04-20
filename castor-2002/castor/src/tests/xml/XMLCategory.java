package xml;


import org.exolab.jtf.CWTestCategory;
import org.exolab.jtf.CWTestCase;
import org.exolab.exceptions.CWClassConstructorException;



public class XMLCategory
    extends CWTestCategory
{


    public static final String MappingFile = "mapping.xml";


    public XMLCategory( String name, String description, Object obj )
        throws CWClassConstructorException
    {
        super( name, description );
    }


}
