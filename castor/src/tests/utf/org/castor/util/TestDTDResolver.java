package utf.org.castor.util;

import java.net.URL;

import org.exolab.castor.util.DTDResolver;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

public class TestDTDResolver extends TestCase {

	/**
	 * Castor-specific EntityResolver to test.
	 */
	private DTDResolver entityResolver;

	protected void setUp() throws Exception {
		super.setUp();
		
		entityResolver = new DTDResolver();
	}
	
	private void testSystemId (String systemId) throws Exception {
		InputSource inputSource = entityResolver.resolveEntity(null, systemId);
		assertNotNull(inputSource);
		assertEquals(systemId, inputSource.getSystemId());
	}
	
	public void testMappingDTD() throws Exception {
		testSystemId("http://castor.org/mapping.dtd");
	}
	
	public void testMappingDTDOld() throws Exception {
		testSystemId("http://castor.exolab.org/mapping.dtd");
	}
	
	public void testJDODTD() throws Exception {
		testSystemId("http://castor.org/jdo-conf.dtd");
	}
	
	public void testJdoDTDOld() throws Exception {
		testSystemId("http://castor.exolab.org/jdo-conf.dtd");
	}

	public void testMappingXSD() throws Exception {
		testSystemId("http://castor.org/mapping.xsd");
	}
	
	public void testMappingXSDOld() throws Exception {
		testSystemId("http://castor.exolab.org/mapping.xsd");
	}
	
	public void testJDOXSD() throws Exception {
		testSystemId("http://castor.org/jdo-conf.xsd");
	}
	
	public void testJdoXSDOld() throws Exception {
		testSystemId("http://castor.exolab.org/jdo-conf.xsd");
	}
}
