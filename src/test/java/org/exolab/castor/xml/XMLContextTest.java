package org.exolab.castor.xml;

import org.castor.test.entity.Entity;
import org.exolab.castor.mapping.Mapping;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

public class XMLContextTest extends TestCase {
    
    private static final String MAPPING_FILE = "org/castor/test/entity/mapping.xml";

    /**
     * Test {@link XMLContext} by providing a generated package.
     * @throws Exception
     */
    public void testXMLContextByPackage() throws Exception {
        XMLContext context = new XMLContext();
        context.addPackage("org.castor.test.entity");
        assertNotNull (context);
        
        Unmarshaller unmarshaller = context.createUnmarshaller();
        assertNotNull(unmarshaller);
        
        unmarshaller.setClass(Entity.class);
        String resource = getResource("org/castor/test/entity/input.xml");
        InputSource source = new InputSource(resource);
        Entity entity = (Entity) unmarshaller.unmarshal(source);
        assertNotNull(entity);
        
    }

    /**
     * Test XMLContext with a mapping file.
     * @throws Exception
     */
    public void testXMLContextByMapping() throws Exception {
        

        Mapping mapping = XMLContext.createMapping();
        mapping.loadMapping(new InputSource(getResource(MAPPING_FILE)));
        
        XMLContext context = new XMLContext();
        context.addMapping(mapping);
        assertNotNull (context);
        
        Unmarshaller unmarshaller = context.createUnmarshaller();
        assertNotNull(unmarshaller);
        
        unmarshaller.setClass(Entity.class);
        String resource = getResource("org/castor/test/entity/input.xml");
        InputSource source = new InputSource(resource);
        Entity entity = (Entity) unmarshaller.unmarshal(source);
        assertNotNull(entity);
        
    }

    /**
     * Returns absolute path for resource.
     * @param resource Relative path to resource
     * 
     * @return Absolute path to resource.
     */
    private String getResource(final String resource) {
        ClassLoader loader = getClass().getClassLoader();
        return loader.getResource(resource).toExternalForm();
    }

}
