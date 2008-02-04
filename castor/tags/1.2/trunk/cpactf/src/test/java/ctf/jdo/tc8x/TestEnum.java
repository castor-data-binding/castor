/*
 * Copyright 2005 Ralf Joachim
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
package ctf.jdo.tc8x;

import harness.CastorTestCase;
import harness.TestHarness;
import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class TestEnum extends CastorTestCase  {
    private JDOCategory     _category;

    public TestEnum(final TestHarness category) {
        super(category, "TC85", "TestEnum");
        _category = (JDOCategory) category;
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void runTest() throws Exception {
        testCreateLoadUpdateDelete();
        testQuery();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateLoadUpdateDelete() throws Exception {
        Database database;
        
        // create product
        database = _category.getDatabase();
        database.begin();
        
        Product pc = new Product(1, "LCD", KindEnum.MONITOR);
        database.create(pc);

        database.commit();
        database.close();

        // load created product
        database = _category.getDatabase();
        database.begin();
        
        Product pl1 = new Product(1, "LCD", KindEnum.MONITOR);
        Product pl2 = (Product) database.load(Product.class, new Integer(1));
        assertEquals(pl1, pl2);

        database.commit();
        database.close();

        // update product
        database = _category.getDatabase();
        database.begin();
        
        Product pu = (Product) database.load(Product.class, new Integer(1));
        pu.setName("Laser");
        pu.setKind(KindEnum.PRINTER);

        database.commit();
        database.close();

        // load updated product
        database = _category.getDatabase();
        database.begin();
        
        Product pl3 = new Product(1, "Laser", KindEnum.PRINTER);
        Product pl4 = (Product) database.load(Product.class, new Integer(1));
        assertEquals(pl3, pl4);

        database.commit();
        database.close();

        // delete product
        database = _category.getDatabase();
        database.begin();
        
        Product pd = (Product) database.load(Product.class, new Integer(1));
        database.remove(pd);

        database.commit();
        database.close();
    }

    public void testQuery() throws Exception {
        Database database;
        
        // create some products
        database = _category.getDatabase();
        database.begin();
        
        database.create(new Product(1, "LCD", KindEnum.MONITOR));
        database.create(new Product(2, "Laser", KindEnum.PRINTER));
        database.create(new Product(3, "Desktop", KindEnum.COMPUTER));
        database.create(new Product(4, "Notebook", KindEnum.COMPUTER));

        database.commit();
        database.close();

        // query and delete all product
        database = _category.getDatabase();
        database.begin();
        
        Product pq;
        OQLQuery query = database.getOQLQuery("select p from "
                + ctf.jdo.tc8x.Product.class.getName() + " p order by p.id");
        QueryResults results = query.execute();
        
        pq = (Product) results.next();
        assertEquals(pq, new Product(1, "LCD", KindEnum.MONITOR));
        database.remove(pq);

        pq = (Product) results.next();
        assertEquals(pq, new Product(2, "Laser", KindEnum.PRINTER));
        database.remove(pq);
        
        pq = (Product) results.next();
        assertEquals(pq, new Product(3, "Desktop", KindEnum.COMPUTER));
        database.remove(pq);
        
        pq = (Product) results.next();
        assertEquals(pq, new Product(4, "Notebook", KindEnum.COMPUTER));
        database.remove(pq);
        
        assertFalse(results.hasMore());
        results.close();
        query.close();

        database.commit();
        database.close();
    }
}
