package org.exolab.castor.xml;

import junit.framework.TestCase;

import org.castor.test.entity.Entity;
import org.exolab.castor.mapping.Mapping;
import org.xml.sax.InputSource;

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

//    public void testGetDescriptor() throws Exception {
//        Mapping mapping = XMLContext.createMapping();
//        mapping.loadMapping(new InputSource(getResource(MAPPING_FILE)));
//        
//        XMLContext context = XMLContext.newInstance();
//        context.addMapping(mapping);
//        assertNotNull (context);
//        
//        XMLClassDescriptor descriptor = context.getDescriptor(Entity.class);
//        assertNotNull(descriptor);
//        assertEquals(Entity.class, descriptor.getJavaClass());
//        
//    }
//
//    public void testGetMissingDescriptor() throws Exception {
//        Mapping mapping = XMLContext.createMapping();
//        mapping.loadMapping(new InputSource(getResource(MAPPING_FILE)));
//        
//        XMLContext context = XMLContext.newInstance();
//        context.addMapping(mapping);
//        assertNotNull (context);
//        
//        XMLClassDescriptor descriptor = context.getDescriptor(EntitySecond.class);
//        assertNull(descriptor);
//    }
//
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
