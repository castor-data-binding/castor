package org.castor.cpa.persistence.sql.keygen;

import java.sql.Types;
import java.util.Properties;

import org.castor.cpa.persistence.sql.query.PersistenceFactoryMock;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TableKeyGeneratorFactoryTest {

	TableKeyGeneratorFactory factory;
	
	@Before
	public void setUp() throws Exception {
		factory = new TableKeyGeneratorFactory();
	}
	
	@SuppressWarnings("cast")
	@Test
	public void isKeyGeneratorFactory() throws Exception {
		assertTrue(factory instanceof KeyGeneratorFactory); 
	}
	
	@Test
	public void hasNameTable() throws Exception {
		assertEquals(TableKeyGeneratorFactory.NAME, factory.getKeyGeneratorName());
	}
	
	@Test
	public void returnsTableKeyGenerator() throws Exception {
		PersistenceFactoryMock mockFactory = new PersistenceFactoryMock();
		Properties params = new Properties();
		int sqlType = Types.BIGINT;
		assertNotNull(factory.getKeyGenerator(mockFactory, params, sqlType));
	}
}
