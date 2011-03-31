package org.castor.cpa.persistence.sql.driver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Types;

import org.junit.Test;

public final class DerbyFactoryTest {
    private DerbyFactory _factory = new DerbyFactory();
    
    @Test
    public void integerSequenceTypeIsSupported() throws Exception {
        assertTrue(_factory.isKeyGeneratorSequenceTypeSupported(Types.INTEGER));
    }
    
    @Test
    public void smallintSequenceTypeIsSupported() throws Exception {
        assertTrue(_factory.isKeyGeneratorSequenceTypeSupported(Types.SMALLINT));
    }
    
    @Test
    public void bigintSequenceTypeIsSupported() throws Exception {
        assertTrue(_factory.isKeyGeneratorSequenceTypeSupported(Types.BIGINT));
    }
    
    @Test
    public void varcharAndOtherSequenceTypesAreNotSupported() throws Exception {
        assertFalse(_factory.isKeyGeneratorSequenceTypeSupported(Types.VARCHAR));
    }
    
    @Test
    public void sequenceValueBeforeSelectIsTheNextValue() throws Exception {
        String actual = _factory.getSequenceBeforeSelectString("seqName", "tableName", 1);
        assertEquals("VALUES (NEXT VALUE FOR seqName)", actual);
    }
    
    @Test
    public void returningSequenceValueWithInsertIsNotSupported() throws Exception {
        assertTrue(_factory.isKeyGeneratorSequenceSupported(false, false));
    }
    
    @Test
    public void usingTriggerForSequenceGenerationIsNotSupported() throws Exception {
        assertTrue(_factory.isKeyGeneratorSequenceSupported(false, false));
    }
}
