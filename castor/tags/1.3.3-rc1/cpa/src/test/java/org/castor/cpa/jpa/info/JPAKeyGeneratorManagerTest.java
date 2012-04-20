package org.castor.cpa.jpa.info;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public final class JPAKeyGeneratorManagerTest {
    private JPAKeyGeneratorManager _manager = JPAKeyGeneratorManager.getInstance();
    private String _name;
    private JPATableGeneratorDescriptor _descriptor = new JPATableGeneratorDescriptor();
    private JPASequenceGeneratorDescriptor _descriptor2 = new JPASequenceGeneratorDescriptor();
    
    @Before 
    public void setUp() throws Exception {
        _name = "name";
    }
    
    @Ignore
    @Test
    public void addedGeneratorIsContained() throws Exception {
        _manager.add(_name, _descriptor);
        assertTrue(_manager.contains(_name));
    }
    
    @Test(expected = GeneratorNameAlreadyUsedException.class)
    public void addingGeneratorWithSameNameCausesException() throws Exception {
        _manager.add(_name, _descriptor);
        _manager.add(_name, _descriptor2);
    }
    
//  @Test
//  public void resetClearsManagedGenerators() throws Exception {
//      manager.add(name, descriptor2);
//      manager.reset();
//      assertTrue(manager.isEmpty());
//  }
    
    @Test
    public void autoGeneratorWillBeCreatedIfDoesntExist() throws Exception {
        _manager.getAuto();
        assertTrue(_manager.contains(JPAKeyGeneratorManager.AUTO_GENERATOR_NAME));
    }
    
    @After
    public void tearDown() throws Exception {
        _manager.reset();
    }
}
