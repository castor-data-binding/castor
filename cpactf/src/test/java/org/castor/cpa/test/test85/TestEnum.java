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
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
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

    public void testEnumStyleClassNoName() throws Exception {
        ProductEnumStyleClassNoName pc, pl1, pl2, pu, pl3, pl4, pd;

        _db.begin(); 
        pc = new ProductEnumStyleClassNoName(1, "LCD", KindEnumStyleClassNoName.MONITOR);
        _db.create(pc);
        _db.commit();

        // load created product
        _db.begin();
        pl1 = new ProductEnumStyleClassNoName(1, "LCD", KindEnumStyleClassNoName.MONITOR);
        pl2 = _db.load(ProductEnumStyleClassNoName.class, new Integer(1));
        assertEquals(pl1, pl2);
        _db.commit();

        // update product
        _db.begin();
        pu = _db.load(ProductEnumStyleClassNoName.class, new Integer(1));
        pu.setName("Laser");
        pu.setKind(KindEnumStyleClassNoName.PRINTER);
        _db.commit();

        // load updated product
        _db.begin();
        pl3 = new ProductEnumStyleClassNoName(1, "Laser", KindEnumStyleClassNoName.PRINTER);
        pl4 = _db.load(ProductEnumStyleClassNoName.class, new Integer(1));
        assertEquals(pl3, pl4);
        _db.commit();

        // delete product
        _db.begin();
        pd = _db.load(ProductEnumStyleClassNoName.class, new Integer(1));
        _db.remove(pd);
        _db.commit();

        // create some products
        _db.begin();
        _db.create(new ProductEnumStyleClassNoName(1, "LCD",
                KindEnumStyleClassNoName.MONITOR));
        _db.create(new ProductEnumStyleClassNoName(2, "Laser",
                KindEnumStyleClassNoName.PRINTER));
        _db.create(new ProductEnumStyleClassNoName(3, "Desktop",
                KindEnumStyleClassNoName.COMPUTER));
        _db.create(new ProductEnumStyleClassNoName(4, "Notebook",
                KindEnumStyleClassNoName.COMPUTER));
        _db.commit();

        // query and delete all product
        _db.begin();
        ProductEnumStyleClassNoName pq;
        OQLQuery query = _db.getOQLQuery("select p from "
                + org.castor.cpa.test.test85.ProductEnumStyleClassNoName.class.getName()
                + " p order by p.id");
        QueryResults results = query.execute();
        pq = (ProductEnumStyleClassNoName) results.next();
        assertEquals(pq, new ProductEnumStyleClassNoName(1, "LCD",
                KindEnumStyleClassNoName.MONITOR));
        _db.remove(pq);
        pq = (ProductEnumStyleClassNoName) results.next();
        assertEquals(pq, new ProductEnumStyleClassNoName(2, "Laser",
                KindEnumStyleClassNoName.PRINTER));
        _db.remove(pq);
        pq = (ProductEnumStyleClassNoName) results.next();
        assertEquals(pq, new ProductEnumStyleClassNoName(3, "Desktop",
                KindEnumStyleClassNoName.COMPUTER));
        _db.remove(pq);
        pq = (ProductEnumStyleClassNoName) results.next();
        assertEquals(pq, new ProductEnumStyleClassNoName(4, "Notebook",
                KindEnumStyleClassNoName.COMPUTER));
        _db.remove(pq);  
        assertFalse(results.hasMore());
        results.close();
        query.close();
        _db.commit();
    }

    public void testEnumStyleClassSameName() throws Exception {
        ProductEnumStyleClassSameName pc, pl1, pl2, pu, pl3, pl4, pd;

        _db.begin(); 
        pc = new ProductEnumStyleClassSameName(1, "LCD", KindEnumStyleClassSameName.MONITOR);
        _db.create(pc);
        _db.commit();

        // load created product
        _db.begin();
        pl1 = new ProductEnumStyleClassSameName(1, "LCD", KindEnumStyleClassSameName.MONITOR);
        pl2 = _db.load(ProductEnumStyleClassSameName.class, new Integer(1));
        assertEquals(pl1, pl2);
        _db.commit();

        // update product
        _db.begin();
        pu = _db.load(ProductEnumStyleClassSameName.class, new Integer(1));
        pu.setName("Laser");
        pu.setKind(KindEnumStyleClassSameName.PRINTER);
        _db.commit();

        // load updated product
        _db.begin();
        pl3 = new ProductEnumStyleClassSameName(1, "Laser", KindEnumStyleClassSameName.PRINTER);
        pl4 = _db.load(ProductEnumStyleClassSameName.class, new Integer(1));
        assertEquals(pl3, pl4);
        _db.commit();

        // delete product
        _db.begin();
        pd = _db.load(ProductEnumStyleClassSameName.class, new Integer(1));
        _db.remove(pd);
        _db.commit();

        // create some products
        _db.begin();
        _db.create(new ProductEnumStyleClassSameName(1, "LCD",
                KindEnumStyleClassSameName.MONITOR));
        _db.create(new ProductEnumStyleClassSameName(2, "Laser",
                KindEnumStyleClassSameName.PRINTER));
        _db.create(new ProductEnumStyleClassSameName(3, "Desktop",
                KindEnumStyleClassSameName.COMPUTER));
        _db.create(new ProductEnumStyleClassSameName(4, "Notebook",
                KindEnumStyleClassSameName.COMPUTER));
        _db.commit();

        // query and delete all product
        _db.begin();
        ProductEnumStyleClassSameName pq;
        OQLQuery query = _db.getOQLQuery("select p from "
                + org.castor.cpa.test.test85.ProductEnumStyleClassSameName.class.getName()
                + " p order by p.id");
        QueryResults results = query.execute();
        pq = (ProductEnumStyleClassSameName) results.next();
        assertEquals(pq, new ProductEnumStyleClassSameName(1, "LCD",
                KindEnumStyleClassSameName.MONITOR));
        _db.remove(pq);
        pq = (ProductEnumStyleClassSameName) results.next();
        assertEquals(pq, new ProductEnumStyleClassSameName(2, "Laser",
                KindEnumStyleClassSameName.PRINTER));
        _db.remove(pq);
        pq = (ProductEnumStyleClassSameName) results.next();
        assertEquals(pq, new ProductEnumStyleClassSameName(3, "Desktop",
                KindEnumStyleClassSameName.COMPUTER));
        _db.remove(pq);
        pq = (ProductEnumStyleClassSameName) results.next();
        assertEquals(pq, new ProductEnumStyleClassSameName(4, "Notebook",
                KindEnumStyleClassSameName.COMPUTER));
        _db.remove(pq);  
        assertFalse(results.hasMore());
        results.close();
        query.close();
        _db.commit();
    }

    public void testEnumStyleClassDiffName() throws Exception {
        ProductEnumStyleClassDiffName pc, pl1, pl2, pu, pl3, pl4, pd;

        _db.begin(); 
        pc = new ProductEnumStyleClassDiffName(1, "LCD", KindEnumStyleClassDiffName.MONITOR);
        _db.create(pc);
        _db.commit();

        // load created product
        _db.begin();
        pl1 = new ProductEnumStyleClassDiffName(1, "LCD", KindEnumStyleClassDiffName.MONITOR);
        pl2 = _db.load(ProductEnumStyleClassDiffName.class, new Integer(1));
        assertEquals(pl1, pl2);
        _db.commit();

        // update product
        _db.begin();
        pu = _db.load(ProductEnumStyleClassDiffName.class, new Integer(1));
        pu.setName("Laser");
        pu.setKind(KindEnumStyleClassDiffName.PRINTER);
        _db.commit();

        // load updated product
        _db.begin();
        pl3 = new ProductEnumStyleClassDiffName(1, "Laser", KindEnumStyleClassDiffName.PRINTER);
        pl4 = _db.load(ProductEnumStyleClassDiffName.class, new Integer(1));
        assertEquals(pl3, pl4);
        _db.commit();

        // delete product
        _db.begin();
        pd = _db.load(ProductEnumStyleClassDiffName.class, new Integer(1));
        _db.remove(pd);
        _db.commit();

        // create some products
        _db.begin();
        _db.create(new ProductEnumStyleClassDiffName(1, "LCD",
                KindEnumStyleClassDiffName.MONITOR));
        _db.create(new ProductEnumStyleClassDiffName(2, "Laser",
                KindEnumStyleClassDiffName.PRINTER));
        _db.create(new ProductEnumStyleClassDiffName(3, "Desktop",
                KindEnumStyleClassDiffName.COMPUTER));
        _db.create(new ProductEnumStyleClassDiffName(4, "Notebook",
                KindEnumStyleClassDiffName.COMPUTER));
        _db.commit();

        // query and delete all product
        _db.begin();
        ProductEnumStyleClassDiffName pq;
        OQLQuery query = _db.getOQLQuery("select p from "
                + org.castor.cpa.test.test85.ProductEnumStyleClassDiffName.class.getName()
                + " p order by p.id");
        QueryResults results = query.execute();
        pq = (ProductEnumStyleClassDiffName) results.next();
        assertEquals(pq, new ProductEnumStyleClassDiffName(1, "LCD",
                KindEnumStyleClassDiffName.MONITOR));
        _db.remove(pq);
        pq = (ProductEnumStyleClassDiffName) results.next();
        assertEquals(pq, new ProductEnumStyleClassDiffName(2, "Laser",
                KindEnumStyleClassDiffName.PRINTER));
        _db.remove(pq);
        pq = (ProductEnumStyleClassDiffName) results.next();
        assertEquals(pq, new ProductEnumStyleClassDiffName(3, "Desktop",
                KindEnumStyleClassDiffName.COMPUTER));
        _db.remove(pq);
        pq = (ProductEnumStyleClassDiffName) results.next();
        assertEquals(pq, new ProductEnumStyleClassDiffName(4, "Notebook",
                KindEnumStyleClassDiffName.COMPUTER));
        _db.remove(pq);  
        assertFalse(results.hasMore());
        results.close();
        query.close();
        _db.commit();
    }

    public void testJavaEnumSameName() throws Exception {
        ProductJavaEnumSameName pc, pl1, pl2, pu, pl3, pl4, pd;

        _db.begin(); 
        pc = new ProductJavaEnumSameName(1, "LCD", KindJavaEnumSameName.MONITOR);
        _db.create(pc);
        _db.commit();

        // load created product
        _db.begin();
        pl1 = new ProductJavaEnumSameName(1, "LCD", KindJavaEnumSameName.MONITOR);
        pl2 = _db.load(ProductJavaEnumSameName.class, new Integer(1));
        assertEquals(pl1, pl2);
        _db.commit();

        // update product
        _db.begin();
        pu = _db.load(ProductJavaEnumSameName.class, new Integer(1));
        pu.setName("Laser");
        pu.setKind(KindJavaEnumSameName.PRINTER);
        _db.commit();

        // load updated product
        _db.begin();
        pl3 = new ProductJavaEnumSameName(1, "Laser", KindJavaEnumSameName.PRINTER);
        pl4 = _db.load(ProductJavaEnumSameName.class, new Integer(1));
        assertEquals(pl3, pl4);
        _db.commit();

        // delete product
        _db.begin();
        pd = _db.load(ProductJavaEnumSameName.class, new Integer(1));
        _db.remove(pd);
        _db.commit();

        // create some products
        _db.begin();
        _db.create(new ProductJavaEnumSameName(1, "LCD",
                KindJavaEnumSameName.MONITOR));
        _db.create(new ProductJavaEnumSameName(2, "Laser",
                KindJavaEnumSameName.PRINTER));
        _db.create(new ProductJavaEnumSameName(3, "Desktop",
                KindJavaEnumSameName.COMPUTER));
        _db.create(new ProductJavaEnumSameName(4, "Notebook",
                KindJavaEnumSameName.COMPUTER));
        _db.commit();

        // query and delete all product
        _db.begin();
        ProductJavaEnumSameName pq;
        OQLQuery query = _db.getOQLQuery("select p from "
                + org.castor.cpa.test.test85.ProductJavaEnumSameName.class.getName()
                + " p order by p.id");
        QueryResults results = query.execute();
        pq = (ProductJavaEnumSameName) results.next();
        assertEquals(pq, new ProductJavaEnumSameName(1, "LCD",
                KindJavaEnumSameName.MONITOR));
        _db.remove(pq);
        pq = (ProductJavaEnumSameName) results.next();
        assertEquals(pq, new ProductJavaEnumSameName(2, "Laser",
                KindJavaEnumSameName.PRINTER));
        _db.remove(pq);
        pq = (ProductJavaEnumSameName) results.next();
        assertEquals(pq, new ProductJavaEnumSameName(3, "Desktop",
                KindJavaEnumSameName.COMPUTER));
        _db.remove(pq);
        pq = (ProductJavaEnumSameName) results.next();
        assertEquals(pq, new ProductJavaEnumSameName(4, "Notebook",
                KindJavaEnumSameName.COMPUTER));
        _db.remove(pq);  
        assertFalse(results.hasMore());
        results.close();
        query.close();
        _db.commit();
    }

    public void testJavaEnumDiffName() throws Exception {
        ProductJavaEnumDiffName pc, pl1, pl2, pu, pl3, pl4, pd;

        _db.begin(); 
        pc = new ProductJavaEnumDiffName(1, "LCD", KindJavaEnumDiffName.MONITOR);
        _db.create(pc);
        _db.commit();

        // load created product
        _db.begin();
        pl1 = new ProductJavaEnumDiffName(1, "LCD", KindJavaEnumDiffName.MONITOR);
        pl2 = _db.load(ProductJavaEnumDiffName.class, new Integer(1));
        assertEquals(pl1, pl2);
        _db.commit();

        // update product
        _db.begin();
        pu = _db.load(ProductJavaEnumDiffName.class, new Integer(1));
        pu.setName("Laser");
        pu.setKind(KindJavaEnumDiffName.PRINTER);
        _db.commit();

        // load updated product
        _db.begin();
        pl3 = new ProductJavaEnumDiffName(1, "Laser", KindJavaEnumDiffName.PRINTER);
        pl4 = _db.load(ProductJavaEnumDiffName.class, new Integer(1));
        assertEquals(pl3, pl4);
        _db.commit();

        // delete product
        _db.begin();
        pd = _db.load(ProductJavaEnumDiffName.class, new Integer(1));
        _db.remove(pd);
        _db.commit();

        // create some products
        _db.begin();
        _db.create(new ProductJavaEnumDiffName(1, "LCD",
                KindJavaEnumDiffName.MONITOR));
        _db.create(new ProductJavaEnumDiffName(2, "Laser",
                KindJavaEnumDiffName.PRINTER));
        _db.create(new ProductJavaEnumDiffName(3, "Desktop",
                KindJavaEnumDiffName.COMPUTER));
        _db.create(new ProductJavaEnumDiffName(4, "Notebook",
                KindJavaEnumDiffName.COMPUTER));
        _db.commit();

        // query and delete all product
        _db.begin();
        ProductJavaEnumDiffName pq;
        OQLQuery query = _db.getOQLQuery("select p from "
                + org.castor.cpa.test.test85.ProductJavaEnumDiffName.class.getName()
                + " p order by p.id");
        QueryResults results = query.execute();
        pq = (ProductJavaEnumDiffName) results.next();
        assertEquals(pq, new ProductJavaEnumDiffName(1, "LCD",
                KindJavaEnumDiffName.MONITOR));
        _db.remove(pq);
        pq = (ProductJavaEnumDiffName) results.next();
        assertEquals(pq, new ProductJavaEnumDiffName(2, "Laser",
                KindJavaEnumDiffName.PRINTER));
        _db.remove(pq);
        pq = (ProductJavaEnumDiffName) results.next();
        assertEquals(pq, new ProductJavaEnumDiffName(3, "Desktop",
                KindJavaEnumDiffName.COMPUTER));
        _db.remove(pq);
        pq = (ProductJavaEnumDiffName) results.next();
        assertEquals(pq, new ProductJavaEnumDiffName(4, "Notebook",
                KindJavaEnumDiffName.COMPUTER));
        _db.remove(pq);  
        assertFalse(results.hasMore());
        results.close();
        query.close();
        _db.commit();
    }
}
