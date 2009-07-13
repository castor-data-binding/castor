/*
 * Copyright 2005 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.castor.cpa.test.test79;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.QueryResults;

public class TestAutostoreMany extends CPATestCase {

	private Database db;

	private static final String DBNAME = "test79";
	private static final String MAPPING = "/org/castor/cpa/test/test79/mapping.xml";

	public TestAutostoreMany(final String name) {
		super(name);
	}

	public boolean include(final DatabaseEngineType engine) {
		return (engine == DatabaseEngineType.MYSQL)
				|| (engine == DatabaseEngineType.DERBY)
				|| (engine == DatabaseEngineType.POSTGRESQL)
				|| (engine == DatabaseEngineType.HSQL)
				|| (engine == DatabaseEngineType.ORACLE);
	}

	public void setUp() throws Exception {
		db = getJDOManager(DBNAME, MAPPING).getDatabase();
	}

	public void testCreateNoEntityTwo() throws Exception {
		db.begin();
		AutostoreMainMany main = new AutostoreMainMany();
		main.setId(new Integer(100));
		main.setName("main.100");
		db.create(main);
		db.commit();

		db.begin();
		main = (AutostoreMainMany) db.load(AutostoreMainMany.class,
				new Integer(100));
		assertNotNull(main);
		assertEquals(100, main.getId().intValue());
		assertEquals("main.100", main.getName());
		assertNotNull(main.getAssociatedMany());
		assertEquals(0, main.getAssociatedMany().size());
		db.commit();

		db.begin();
		main = (AutostoreMainMany) db.load(AutostoreMainMany.class,
				new Integer(100));
		db.remove(main);
		db.commit();

		db.close();
	}

	public void testCreateWithEntityTwoNoAutoStore() throws Exception {
		db.begin();
		AutostoreAssociatedMany entityTwo = new AutostoreAssociatedMany();
		entityTwo.setId(new Integer(200));
		entityTwo.setName("entity1.200");
		List<AutostoreAssociatedMany> associatedManys = Collections
				.singletonList(entityTwo);
		AutostoreMainMany entityOne = new AutostoreMainMany();
		entityOne.setId(new Integer(200));
		entityOne.setName("entity2.200");
		entityOne.setAssociatedMany(associatedManys);
		db.create(entityOne);
		db.create(entityTwo);
		db.commit();

		db.begin();
		entityOne = (AutostoreMainMany) db.load(AutostoreMainMany.class,
				new Integer(200));
		assertNotNull(entityOne);
		assertEquals(200, entityOne.getId().intValue());
		assertEquals("entity2.200", entityOne.getName());
		assertNotNull(entityOne.getAssociatedMany());
		db.commit();

		db.begin();
		entityOne = (AutostoreMainMany) db.load(AutostoreMainMany.class,
				new Integer(200));
		db.remove(entityOne);
		db.commit();

		db.begin();
		entityTwo = (AutostoreAssociatedMany) db.load(
				AutostoreAssociatedMany.class, new Integer(200));
		db.remove(entityTwo);
		db.commit();

		db.close();
	}

	public void testCreateWithEntityTwoWithAutoStoreDeleteWithoutAutoStore()
			throws Exception {
		db.setAutoStore(true);

		db.begin();
		AutostoreAssociatedMany entityTwo = new AutostoreAssociatedMany();
		entityTwo.setId(new Integer(300));
		entityTwo.setName("entity2.300");
		AutostoreMainMany entityOne = new AutostoreMainMany();
		entityOne.setId(new Integer(300));
		entityOne.setName("entity1.300");
		entityOne.setAssociatedMany(Collections.singletonList(entityTwo));
		db.create(entityOne);
		db.commit();

		db.begin();
		entityOne = (AutostoreMainMany) db.load(AutostoreMainMany.class,
				new Integer(300));
		assertNotNull(entityOne);
		assertEquals(300, entityOne.getId().intValue());
		assertEquals("entity1.300", entityOne.getName());
		assertNotNull(entityOne.getAssociatedMany());

		List<AutostoreAssociatedMany> associatedManys = entityOne
				.getAssociatedMany();
		entityTwo = associatedManys.iterator().next();
		assertEquals(300, entityTwo.getId().intValue());
		assertEquals("entity2.300", entityTwo.getName());
		db.commit();

		db.setAutoStore(false);

		db.begin();
		entityOne = (AutostoreMainMany) db.load(AutostoreMainMany.class,
				new Integer(300));
		db.remove(entityOne);
		db.commit();

		db.begin();
		entityTwo = (AutostoreAssociatedMany) db.load(
				AutostoreAssociatedMany.class, new Integer(300));
		assertNotNull(entityTwo);
		assertEquals(300, entityTwo.getId().intValue());
		assertEquals("entity2.300", entityTwo.getName());

		try {
			entityOne = (AutostoreMainMany) db.load(AutostoreMainMany.class,
					new Integer(300));
			fail("Expected ObjectNotFoundException");
		} catch (ObjectNotFoundException e) {
			//
		}
		db.commit();

		db.begin();
		entityTwo = (AutostoreAssociatedMany) db.load(
				AutostoreAssociatedMany.class, new Integer(300));
		db.remove(entityTwo);
		db.commit();

		db.begin();
		try {
			entityTwo = (AutostoreAssociatedMany) db.load(
					AutostoreAssociatedMany.class, new Integer(300));
			fail("Expected ObjectNotFoundException");
		} catch (ObjectNotFoundException e) {
			//
		}
		db.commit();

		db.close();
	}

	public void testCreateWithEntityTwoWithAutoStoreDeleteWithAutoStore()
			throws Exception {
		db.setAutoStore(true);

		db.begin();
		AutostoreAssociatedMany entityTwo = new AutostoreAssociatedMany();
		entityTwo.setId(new Integer(300));
		entityTwo.setName("entity2.300");
		List<AutostoreAssociatedMany> manys = new ArrayList<AutostoreAssociatedMany>();
		manys.add(entityTwo);
		AutostoreMainMany entityOne = new AutostoreMainMany();
		entityOne.setId(new Integer(300));
		entityOne.setName("entity1.300");
		entityOne.setAssociatedMany(manys);
		db.create(entityOne);
		db.commit();

		db.begin();
		entityOne = (AutostoreMainMany) db.load(AutostoreMainMany.class,
				new Integer(300));
		assertNotNull(entityOne);
		assertEquals(300, entityOne.getId().intValue());
		assertEquals("entity1.300", entityOne.getName());
		assertNotNull(entityOne.getAssociatedMany());
		assertEquals(1, entityOne.getAssociatedMany().size());

		AutostoreAssociatedMany many = entityOne.getAssociatedMany().iterator()
				.next();
		assertEquals(300, many.getId().intValue());
		db.commit();

		db.begin();
		many = (AutostoreAssociatedMany) db.load(AutostoreAssociatedMany.class,
				new Integer(300));
		assertNotNull(many);
		assertEquals(300, many.getId().intValue());
		assertEquals("entity2.300", many.getName());
		db.commit();

		db.begin();
		entityOne = (AutostoreMainMany) db.load(AutostoreMainMany.class,
				new Integer(300));
		db.remove(entityOne);
		db.commit();

		db.begin();
		try {
			entityOne = (AutostoreMainMany) db.load(AutostoreMainMany.class,
					new Integer(300));
			fail("Expected ObjectNotFoundException");
		} catch (ObjectNotFoundException e) {
			//
		}
		try {
			many = (AutostoreAssociatedMany) db.load(
					AutostoreAssociatedMany.class, new Integer(300));
			// TODO remove once support for cascading delete has been added
			// fail("Expected ObjectNotFoundException");
		} catch (ObjectNotFoundException e) {
			//
		}
		db.commit();

		// TODO remove once support for cascading delete has been added
		db.begin();
		many = (AutostoreAssociatedMany) db.load(AutostoreAssociatedMany.class,
				new Integer(300));
		db.remove(many);
		db.commit();

		db.close();
	}

	/**
	 * Test method.
	 * 
	 * @throws Exception
	 *             For any exception thrown.
	 */
	public void testQueryEntityOne() throws Exception {
		db.begin();

		OQLQuery query = db.getOQLQuery("SELECT entity FROM "
				+ AutostoreMainMany.class.getName() + " entity WHERE id = $1");
		query.bind(new Integer(1));
		QueryResults results = query.execute();

		AutostoreMainMany entity = (AutostoreMainMany) results.next();

		assertNotNull(entity);
		assertEquals(new Integer(1), entity.getId());

		List<AutostoreAssociatedMany> associatedManys = entity
				.getAssociatedMany();

		assertNotNull(associatedManys);

		Iterator<AutostoreAssociatedMany> iter = associatedManys.iterator();

		assertTrue(iter.hasNext());
		AutostoreAssociatedMany associatedMany = iter.next();
		assertEquals(new Integer(1), associatedMany.getId());

		assertTrue(iter.hasNext());
		associatedMany = iter.next();
		assertEquals(new Integer(2), associatedMany.getId());

		assertFalse(iter.hasNext());

		db.commit();
		db.close();
	}
}
