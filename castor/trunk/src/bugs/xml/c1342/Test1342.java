package xml.c1342;

import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.TestCase;

import net.sf.cglib.proxy.Factory;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

public final class Test1342 extends TestCase {
    private static final String MAPPING_FILE = "mapping.xml";
    
    public Test1342() {
        super();
    }

    /**
     * Test unmarshalling of simple bean without proxy.
     * 
     * @throws Exception For any exception thrown.
     */
    public void testUnmarshalSimpleBean() throws Exception {
        Mapping mapping = new Mapping();
        mapping.loadMapping(getClass().getResource(MAPPING_FILE).toExternalForm());
        
        Unmarshaller unmarshaller = new Unmarshaller(Bean.class);
        unmarshaller.setMapping(mapping);
        
        String input
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<bean attribute=\"999\">"
            + "<element>element of 999</element>"
            + "</bean>";
    
        Bean bean = (Bean) unmarshaller.unmarshal(new StringReader(input));
        assertNotNull(bean);
        assertEquals(999, bean.getAttribute().intValue());
        assertEquals("element of 999", bean.getElement());
        assertNull(bean.getReference());
    }

    /**
     * Test unmarshalling of bean refering to another one without proxy.
     * 
     * @throws Exception For any exception thrown.
     */
    public void testUnmarshalReferingBean() throws Exception {
        Mapping mapping = new Mapping();
        mapping.loadMapping(getClass().getResource(MAPPING_FILE).toExternalForm());
        
        Unmarshaller unmarshaller = new Unmarshaller(Bean.class);
        unmarshaller.setMapping(mapping);
        
        String input
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<bean attribute=\"100\">"
            + "<element>element of 100</element>"
            + "<reference attribute=\"999\">"
            + "<element>element of 999</element>"
            + "</reference>"
            + "</bean>";
    
        Bean referer = (Bean) unmarshaller.unmarshal(new StringReader(input));
        assertNotNull(referer);
        assertEquals(100, referer.getAttribute().intValue());
        assertEquals("element of 100", referer.getElement());
        assertNotNull(referer.getReference());

        Bean refered = referer.getReference();
        assertNotNull(refered);
        assertEquals(999, refered.getAttribute().intValue());
        assertEquals("element of 999", refered.getElement());
        assertNull(refered.getReference());
    }
    
    /**
     * Test marshalling of simple bean without proxy.
     * 
     * @throws Exception For any exception thrown.
     */
    public void testMarshalSimpleBean() throws Exception {
        Mapping mapping = new Mapping();
        mapping.loadMapping(getClass().getResource(MAPPING_FILE).toExternalForm());
        
        StringWriter out = new StringWriter();
        Marshaller marshaller = new Marshaller(out);
        marshaller.setMapping(mapping);
        
        Bean bean = new Bean(new Integer(999), "element of 999", null);
        
        marshaller.marshal(bean);
        
        String output = out.toString();
        
        String expected
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<bean attribute=\"999\">"
            + "<element>element of 999</element>"
            + "</bean>";
        
        assertEquals(expected, output);
    }

    /**
     * Test marshalling of bean refering to another one without proxy.
     * 
     * @throws Exception For any exception thrown.
     */
    public void testMarshalReferingBean() throws Exception {
        Mapping mapping = new Mapping();
        mapping.loadMapping(getClass().getResource(MAPPING_FILE).toExternalForm());
        
        StringWriter out = new StringWriter();
        Marshaller marshaller = new Marshaller(out);
        marshaller.setMapping(mapping);
        
        Bean refered = new Bean(new Integer(999), "element of 999", null);
        Bean referer = new Bean(new Integer(100), "element of 100", refered);
        
        marshaller.marshal(referer);
        
        String output = out.toString();
        
        String expected
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<bean attribute=\"100\">"
            + "<element>element of 100</element>"
            + "<reference attribute=\"999\">"
            + "<element>element of 999</element>"
            + "</reference>"
            + "</bean>";
        
        assertEquals(expected, output);
    }

    /**
     * Test marshalling of simple bean with proxy.
     * 
     * @throws Exception For any exception thrown.
     */
    public void testMarshalSimpleBeanProxy() throws Exception {
        Configuration config = LocalConfiguration.getInstance();
        config.getProperties().setProperty(
                Configuration.Property.ProxyInterfaces, "net.sf.cglib.proxy.Factory");
        
        Mapping mapping = new Mapping();
        mapping.loadMapping(getClass().getResource(MAPPING_FILE).toExternalForm());
        
        StringWriter out = new StringWriter();
        Marshaller marshaller = new Marshaller(out);
        marshaller.setMapping(mapping);
        
        Bean bean = new Bean(new Integer(999), "element of 999", null);
        Bean proxy = (Bean) Proxy.newInstance(bean);
        
        assertNotNull(proxy);
        assertEquals(Bean.class, proxy.getClass().getSuperclass());
        assertEquals(1, proxy.getClass().getInterfaces().length);
        assertEquals(Factory.class, proxy.getClass().getInterfaces()[0]);
        
        assertEquals(999, proxy.getAttribute().intValue());
        assertEquals("element of 999", proxy.getElement());
        assertNull(proxy.getReference());
        
        marshaller.marshal(proxy);
        
        String output = out.toString();

        String expected
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<bean attribute=\"999\">"
            + "<element>element of 999</element>"
            + "</bean>";
        
        assertEquals(expected, output);
    }

    /**
     * Test marshalling of bean refering to another one with proxy.
     * 
     * @throws Exception For any exception thrown.
     */
    public void testMarshalReferingBeanProxy() throws Exception {
        Configuration config = LocalConfiguration.getInstance();
        config.getProperties().setProperty(
                Configuration.Property.ProxyInterfaces, "net.sf.cglib.proxy.Factory");
        
        Mapping mapping = new Mapping();
        mapping.loadMapping(getClass().getResource(MAPPING_FILE).toExternalForm());
        
        StringWriter out = new StringWriter();
        Marshaller marshaller = new Marshaller(out);
        marshaller.setMapping(mapping);
        
        Bean refered = new Bean(new Integer(999), "element of 999", null);
        Bean referedProxy = (Bean) Proxy.newInstance(refered);
        
        assertNotNull(referedProxy);
        assertEquals(Bean.class, referedProxy.getClass().getSuperclass());
        assertEquals(1, referedProxy.getClass().getInterfaces().length);
        assertEquals(Factory.class, referedProxy.getClass().getInterfaces()[0]);
        
        assertEquals(999, referedProxy.getAttribute().intValue());
        assertEquals("element of 999", referedProxy.getElement());
        assertNull(referedProxy.getReference());

        Bean referer = new Bean(new Integer(100), "element of 100", referedProxy);
        Bean refererProxy = (Bean) Proxy.newInstance(referer);
        
        assertNotNull(refererProxy);
        assertEquals(Bean.class, refererProxy.getClass().getSuperclass());
        assertEquals(1, refererProxy.getClass().getInterfaces().length);
        assertEquals(Factory.class, refererProxy.getClass().getInterfaces()[0]);
        
        assertEquals(100, refererProxy.getAttribute().intValue());
        assertEquals("element of 100", refererProxy.getElement());
        assertNotNull(refererProxy.getReference());

        marshaller.marshal(refererProxy);
        
        String output = out.toString();
        
        String expected
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<bean attribute=\"100\">"
            + "<element>element of 100</element>"
            + "<reference attribute=\"999\">"
            + "<element>element of 999</element>"
            + "</reference>"
            + "</bean>";
        
        assertEquals(expected, output);
    }
}
