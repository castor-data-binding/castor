package xml.srcgen.template;

import junit.framework.TestCase;

import org.exolab.castor.builder.SourceGenerator;
import org.xml.sax.InputSource;

public class TestSourceGenerator extends TestCase {

    public void testGeneration() throws Exception {
        SourceGenerator generator = new SourceGenerator();
        String xmlSchema = getClass().getResource("test.xsd").toExternalForm();
        InputSource inputSource = new InputSource(xmlSchema);
        generator.setDestDir("./codegen/src/test/java");
        generator.setSuppressNonFatalWarnings(true);
        
        // uncomment to use Velocity for code generation
//        generator.setJClassPrinterType("velocity");
        
        // uncomment the next line to set a binding file for source generation
//      generator.setBinding(new InputSource(getClass().getResource("binding.xml").toExternalForm()));

        // uncomment the next lines to set custom properties for source generation
//      Properties properties = new Properties();
//      properties.load(getClass().getResource("builder.properties").openStream());
//      generator.setDefaultProperties(properties);

        generator.generateSource(inputSource, getClass().getPackage().getName() + ".generated");
    }
    
}

