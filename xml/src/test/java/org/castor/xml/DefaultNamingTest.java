package org.castor.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { XmlConfigurations.class })
public class DefaultNamingTest {

	@Inject
	private XMLNaming xmlNaming;
	
	@Test
	public void succeedsShortName() {
		String xmlName = xmlNaming.toXMLName("foo");
		assertNotNull(xmlName);
		assertEquals("foo", xmlName);
	}
	
	@Test
	public void succeedsLongerName() {
		String xmlName = xmlNaming.toXMLName("fooBar");
		assertNotNull(xmlName);
		assertEquals("foo-bar", xmlName);
	}

	@Test
	public void succeedsEvenLongerName() {
		String xmlName = xmlNaming.toXMLName("FOOBar");
		assertNotNull(xmlName);
		assertEquals("FOOBar", xmlName);
	}

}
