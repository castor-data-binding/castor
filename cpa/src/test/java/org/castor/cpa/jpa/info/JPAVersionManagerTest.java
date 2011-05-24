package org.castor.cpa.jpa.info;

import static org.junit.Assert.assertTrue;

import org.castor.cpa.jpa.info.JPAVersionManager;
import org.castor.cpa.jpa.info.MultipleVersionFieldDefinitionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JPAVersionManagerTest {

    JPAVersionManager manager;
    
    @Before 
    public void setUp() throws Exception {
        manager = JPAVersionManager.getInstance();
        manager.reset();
    }
    
    @Test
    public void addedClassIsContained() throws Exception {
        manager.add(VersionTestClass.class, "version");
        assertTrue(manager.contains(VersionTestClass.class));
    }
    
    @Test(expected=MultipleVersionFieldDefinitionException.class)
    public void addingVersionFieldForTheSameClassCausesException() throws Exception {
        manager.add(VersionTestClass.class, "version1");
        manager.add(VersionTestClass.class, "version2");
    }
    
    @Test
    public void resetClearsManagedGenerators() throws Exception {
        manager.add(VersionTestClass.class, "version1");
        manager.reset();
        assertTrue(manager.isEmpty());
    }
    
    @After
    public void tearDown() throws Exception {
        manager.reset();
    }
}
