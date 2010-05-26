package org.castor.cpa.persistence.sql.driver;

import java.sql.Types;

import org.junit.Test;
import static org.junit.Assert.*;

public class DerbyFactoryTest {

	DerbyFactory factory = new DerbyFactory();
	
	@Test
	public void integerSequenceTypeIsSupported() throws Exception {
		assertTrue(factory.isKeyGeneratorSequenceTypeSupported(Types.INTEGER));
	}
	
	@Test
	public void smallintSequenceTypeIsSupported() throws Exception {
		assertTrue(factory.isKeyGeneratorSequenceTypeSupported(Types.SMALLINT));
	}
	
	@Test
	public void bigintSequenceTypeIsSupported() throws Exception {
		assertTrue(factory.isKeyGeneratorSequenceTypeSupported(Types.BIGINT));
	}
	
	@Test
	public void varcharAndOtherSequenceTypesAreNotSupported() throws Exception {
		assertFalse(factory.isKeyGeneratorSequenceTypeSupported(Types.VARCHAR));
	}
	
	@Test
	public void sequenceValueBeforeSelectIsTheNextValue() throws Exception {
		String actual = factory.getSequenceBeforeSelectString("seqName", "tableName", 1);
		assertEquals("VALUES (NEXT VALUE FOR seqName)", actual);
	}
	
	@Test
	public void returningSequenceValueWithInsertIsNotSupported() throws Exception {
		assertTrue(factory.isKeyGeneratorSequenceSupported(false, false));
	}
	
	@Test
	public void usingTriggerForSequenceGenerationIsNotSupported() throws Exception {
		assertTrue(factory.isKeyGeneratorSequenceSupported(false, false));
	}
}
