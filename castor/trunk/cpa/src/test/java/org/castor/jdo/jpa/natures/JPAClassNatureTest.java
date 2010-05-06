package org.castor.jdo.jpa.natures;

import static org.junit.Assert.*;

import java.util.Properties;

import org.castor.core.nature.PropertyHolder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class JPAClassNatureTest {

	JPAClassNature nature;
	@Mock
	PropertyHolder holder;

	private static final String canonicalName = JPAClassNature.class
			.getCanonicalName();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void setCachePropertiesSetsRelatedProperty() throws Exception {
		Properties cacheProperties = new Properties();
		cacheProperties.setProperty("type", "none");
		when(holder.hasNature(canonicalName)).thenReturn(true);
		when(holder.getProperty(canonicalName + JPAClassNature.CACHE_PROPERTIES))
				.thenReturn(cacheProperties);
		nature = new JPAClassNature(holder);
		nature.setCacheProperties(cacheProperties);
		assertEquals(cacheProperties, nature.getCacheProperties());
	}

}
