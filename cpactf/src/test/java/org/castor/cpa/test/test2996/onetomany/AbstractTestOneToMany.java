/*
 * Copyright 2008 Tobias Hochwallner, Ralf Joachim
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
package org.castor.cpa.test.test2996.onetomany;

import java.io.FileInputStream;
import java.sql.Connection;

import org.castor.cpa.test.framework.CPATestCase;
import org.dbunit.DefaultDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.xml.XmlDataSet;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.MappingException;

/**
 * Abstract base class for testing XML and class mapping with classes
 * having a one to many relation. 
 */
public abstract class AbstractTestOneToMany extends CPATestCase {
    private static final String DATA_SET_FILE_SETUP = "dbunit-setup.xml";

    private JDOManager _jdo;

    public AbstractTestOneToMany(final String name) {
        super(name);
    }

    protected abstract JDOManager getJDOManager() throws MappingException;

    /**
     * Creates data objects used by these tests
     */
    public final void setUp() throws Exception {
        // Open transaction in order to perform JDO operations
        _jdo = getJDOManager();

        Connection conn = _jdo.getConnectionFactory().createConnection();
        String filename = getClass().getResource(DATA_SET_FILE_SETUP).getFile();
        IDatabaseTester dbtester = new DefaultDatabaseTester(new DatabaseConnection(conn));
        dbtester.setDataSet(new XmlDataSet(new FileInputStream(filename)));
        dbtester.onSetup();
    }

    /**
     * Tests if loading an flat element from database works.
     */
    public void testLoadFlat() throws Exception {
        Database db = _jdo.getDatabase();

        db.begin();
        Flat flat = db.load(Flat.class, new Long(1));
        assertNotNull(flat);
        assertEquals(1, flat.getId());
        db.commit();

        db.close();
    }

    /**
     * Tests if loading an house element from database including a flat works.
     */
    public void testLoadHouseOneFlat() throws Exception {
        Database db = _jdo.getDatabase();

        db.begin();
        House house = db.load(House.class, new Long(100));
        assertNotNull(house);
        assertEquals(100, house.getId());
        Flat[] flats = house.getFlats();
        assertNotNull(flats);
        assertEquals(1, flats.length);
        Flat flat = house.getFlats()[0];
        assertEquals(1, flat.getId());
        db.commit();

        db.close();
    }

    /**
     * Tests if loading an house element from database including more flats works.
     */
    public void testLoadHouseFlats() throws Exception {
        Database db = _jdo.getDatabase();

        db.begin();
        House house = db.load(House.class, new Long(101));
        assertNotNull(house);
        assertEquals(101, house.getId());
        Flat[] flats = house.getFlats();
        assertNotNull(flats);
        assertEquals(2, flats.length);

        db.commit();

        db.close();
    }

    /**
     * Tests a simple OQL query.
     */
    public void testSelectQuery() throws Exception {
        Database db = _jdo.getDatabase();

        db.begin();
        OQLQuery query = db.getOQLQuery("SELECT h FROM "
                + House.class.getName() + " h WHERE h.flats.id = $1");
        query.bind(2);
        QueryResults result = query.execute();
        House e = (House) result.next();
        assertEquals(101, e.getId());
        assertEquals(2, e.getFlats().length);
        result.close();
        db.commit();

        db.close();
    }
}
