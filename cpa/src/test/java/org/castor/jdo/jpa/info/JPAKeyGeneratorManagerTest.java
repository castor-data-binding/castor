package org.castor.jdo.jpa.info;


import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


public class JPAKeyGeneratorManagerTest {

	JPAKeyGeneratorManager manager = JPAKeyGeneratorManager.getInstance();
	String name;
	JPATableGeneratorDescriptor descriptor = new JPATableGeneratorDescriptor();
	JPASequenceGeneratorDescriptor descriptor2 = new JPASequenceGeneratorDescriptor();
	
	@Before 
	public void setUp() throws Exception {
		name = "name";
	}
	
	@Ignore
	@Test
	public void addedGeneratorIsContained() throws Exception {
		manager.add(name, descriptor);
		assertTrue(manager.contains(name));
	}
	
	@Test(expected=GeneratorNameAlreadyUsedException.class)
	public void addingGeneratorWithSameNameCausesException() throws Exception {
		manager.add(name, descriptor);
		manager.add(name, descriptor2);
	}
	
	@Test
	public void resetClearsManagedGenerators() throws Exception {
		manager.add(name, descriptor2);
		manager.reset();
		assertTrue(manager.isEmpty());
	}
	
	@Test
	public void autoGeneratorWillBeCreatedIfDoesntExist() throws Exception {
	    manager.getAuto();
	    assertTrue(manager.contains(JPAKeyGeneratorManager.AUTO_GENERATOR_NAME));
	}
	
	@After
	public void tearDown() throws Exception {
		manager.reset();
	}

}
