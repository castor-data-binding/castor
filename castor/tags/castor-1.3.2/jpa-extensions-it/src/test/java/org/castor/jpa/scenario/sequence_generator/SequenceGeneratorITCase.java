package org.castor.jpa.scenario.sequence_generator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.CacheManager;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class SequenceGeneratorITCase {

	public final Log LOG = LogFactory.getLog(getClass());

	@Autowired
	protected JDOManager jdoManager;

	Database db;
	CacheManager cacheManager;

	@Before
	@Transactional
	public void initDb() throws PersistenceException {
		db = jdoManager.getDatabase();
		assertNotNull(db);
	}

	@After
	@Transactional
	public void cleanDb() throws PersistenceException {
		if (db.isActive()) {
			db.rollback();
		}
		db.close();
	}

	@Test 
	@Transactional
	public void sequenceGeneratorOnIdFieldWillBeUsed() throws Exception {
		createPersistentObject(AnnotatedField.class);
		AnnotatedField loaded = loadPersistentObject(AnnotatedField.class);
		assertTrue(loaded.getId() > 0);
	}
	
	@Test 
	@Transactional
	public void sequenceGeneratorOnClassWillBeUsed() throws Exception {
		createPersistentObject(AnnotatedClass.class);
		AnnotatedClass loaded = loadPersistentObject(AnnotatedClass.class);
		assertTrue(loaded.getId() > 0);
	}
	
	@Test 
	@Transactional
	public void defaultSequenceGeneratorOnIdFieldWillBeUsed() throws Exception {
		createPersistentObject(AnnotatedFieldWithDefaultSequenceName.class);
		AnnotatedFieldWithDefaultSequenceName loaded = loadPersistentObject(AnnotatedFieldWithDefaultSequenceName.class);
		assertTrue(loaded.getId() > 0);
	}
	
	@Test 
	@Transactional
	public void defaultSequenceGeneratorOnClassWillBeUsed() throws Exception {
		createPersistentObject(AnnotatedClassWithDefaultSequenceName.class);
		AnnotatedClassWithDefaultSequenceName loaded = loadPersistentObject(AnnotatedClassWithDefaultSequenceName.class);
		assertTrue(loaded.getId() > 0);
	}

	private <T> void createPersistentObject(Class<T> k) throws Exception {
		T instance = k.newInstance();

		db.begin();
		db.create(instance);
		db.commit();
	}

	@SuppressWarnings("unchecked")
	private <T> T loadPersistentObject(Class<T> k) throws Exception {
		db.begin();
		T loaded = (T) db
				.getOQLQuery(
						"select s from " + k.getCanonicalName() + " s")
				.execute().next();

		db.commit();
		return loaded;
	}
}
