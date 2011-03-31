package org.castor.cpa.jpa.info;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public final class JPAVersionManagerTest {
    private JPAVersionManager _manager;
    
    @Before 
    public void setUp() throws Exception {
        _manager = JPAVersionManager.getInstance();
        _manager.reset();
    }
    
    @Test
    public void addedClassIsContained() throws Exception {
        _manager.add(VersionTestClass.class, "version");
        assertTrue(_manager.contains(VersionTestClass.class));
    }
    
    @Test(expected = MultipleVersionFieldDefinitionException.class)
    public void addingVersionFieldForTheSameClassCausesException() throws Exception {
        _manager.add(VersionTestClass.class, "version1");
        _manager.add(VersionTestClass.class, "version2");
    }
    
    @Test
    public void resetClearsManagedGenerators() throws Exception {
        _manager.add(VersionTestClass.class, "version1");
        _manager.reset();
        assertTrue(_manager.isEmpty());
    }
    
    @After
    public void tearDown() throws Exception {
        _manager.reset();
    }
}
