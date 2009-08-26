/*
 * Copyright 2008 Udai Gupta
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
package org.castor.cpa.test.test85;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class TestEnum extends CPATestCase  {
    private static final String DBNAME = "test85";
    private static final String MAPPING = "/org/castor/cpa/test/test85/mapping.xml";
    private Database _db;

    public TestEnum(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void tearDown() throws Exception {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
    }


    public void testCreateLoadUpdateDelete() throws Exception {
        _db.begin(); 
        Product pc = new Product(1, "LCD", KindEnum.MONITOR);
        _db.create(pc);
        _db.commit();

        // load created product
        _db.begin();
        Product pl1 = new Product(1, "LCD", KindEnum.MONITOR);
        Product pl2 = _db.load(Product.class, new Integer(1));
        assertEquals(pl1, pl2);
        _db.commit();

        // update product
        _db.begin();
        Product pu = _db.load(Product.class, new Integer(1));
        pu.setName("Laser");
        pu.setKind(KindEnum.PRINTER);
        _db.commit();

        // load updated product
        _db.begin();
        Product pl3 = new Product(1, "Laser", KindEnum.PRINTER);
        Product pl4 = _db.load(Product.class, new Integer(1));
        assertEquals(pl3, pl4);
        _db.commit();

        // delete product
        _db.begin();
        Product pd = _db.load(Product.class, new Integer(1));
        _db.remove(pd);
        _db.commit();
    }

    public void testQuery() throws Exception {
        // create some products
        _db.begin();
        _db.create(new Product(1, "LCD", KindEnum.MONITOR));
        _db.create(new Product(2, "Laser", KindEnum.PRINTER));
        _db.create(new Product(3, "Desktop", KindEnum.COMPUTER));
        _db.create(new Product(4, "Notebook", KindEnum.COMPUTER));
        _db.commit();

        // query and delete all product
        _db.begin();
        Product pq;
        OQLQuery query = _db.getOQLQuery("select p from "
                + org.castor.cpa.test.test85.Product.class.getName() + " p order by p.id");
        QueryResults results = query.execute();
        pq = (Product) results.next();
        assertEquals(pq, new Product(1, "LCD", KindEnum.MONITOR));
        _db.remove(pq);
        pq = (Product) results.next();
        assertEquals(pq, new Product(2, "Laser", KindEnum.PRINTER));
        _db.remove(pq);
        pq = (Product) results.next();
        assertEquals(pq, new Product(3, "Desktop", KindEnum.COMPUTER));
        _db.remove(pq);
        pq = (Product) results.next();
        assertEquals(pq, new Product(4, "Notebook", KindEnum.COMPUTER));
        _db.remove(pq);  
        assertFalse(results.hasMore());
        results.close();
        query.close();
        _db.commit();

    }
}
