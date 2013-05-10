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
package org.castor.cpa.test.test882;

import java.sql.Statement;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for lazy loading of collection supported by Castor. The object in a lazy
 * collection is not loaded from the DBMS until is it requested by the
 * collection.
 */
public final class TestLazyLoadingReadOnly extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestLazyLoadingReadOnly.class);

    private static final String DBNAME = "test882";
    private static final String MAPPING = "/org/castor/cpa/test/test882/mapping.xml";

    private Database _db;

    public TestLazyLoadingReadOnly(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
       return ((engine == DatabaseEngineType.HSQL) 
             || (engine == DatabaseEngineType.HSQL));
    }

//    public boolean include(final DatabaseEngineType engine) {
//        return (engine == DatabaseEngineType.DERBY)
//            || (engine == DatabaseEngineType.HSQL)
//            || (engine == DatabaseEngineType.MYSQL)
//            || (engine == DatabaseEngineType.ORACLE)
//            || (engine == DatabaseEngineType.POSTGRESQL);
//    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        // delete everything directly
        LOG.info("Delete everything");
        Statement stmt = _db.getJdbcConnection().createStatement();
        stmt.executeUpdate("DELETE FROM test882_product");
        stmt.executeUpdate("DELETE FROM test882_productdetail");
        stmt.executeUpdate("DELETE FROM test882_productdetaillazy");
        _db.commit();
        createDataObjects();
    }

    public void tearDown() throws Exception {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
    }

    private void createDataObjects() throws PersistenceException {
       _db.begin();
       Product product = new Product();
       product.setId(1);
       product.setName("product1");
       _db.create(product);
       _db.commit();
       
       _db.begin();
       Product productLoaded = _db.load(Product.class, 1);
       
       ProductDetail detail = new ProductDetail();
       detail.setId(1);
       detail.setName("productdetail1");
       detail.setProduct(productLoaded);
       _db.create(detail);

       detail = new ProductDetail();
       detail.setId(2);
       detail.setName("productdetail2");
       detail.setProduct(productLoaded);
       _db.create(detail);

       detail = new ProductDetail();
       detail.setId(3);
       detail.setName("productdetail3");
       detail.setProduct(productLoaded);
       _db.create(detail);
       _db.commit();

       _db.begin();
       productLoaded = _db.load(Product.class, 1);
       
       ProductDetailLazy detailLazy = new ProductDetailLazy();
       detailLazy.setId(1);
       detailLazy.setName("productdetaillazy1");
       detailLazy.setProduct(productLoaded);
       _db.create(detailLazy);

       detailLazy = new ProductDetailLazy();
       detailLazy.setId(2);
       detailLazy.setName("productdetaillazy2");
       detailLazy.setProduct(productLoaded);
       _db.create(detailLazy);

       detailLazy = new ProductDetailLazy();
       detailLazy.setId(3);
       detailLazy.setName("productdetaillazy3");
       detailLazy.setProduct(productLoaded);
       _db.create(detailLazy);
       _db.commit();

       _db.close();
    }
    
    public void testWriteProductDetails() throws Exception {
       _db.begin();

       OQLQuery query = _db.getOQLQuery("SELECT product FROM " + Product.class.getName() + " product WHERE id = $1");
       query.bind(new Integer(1));
       QueryResults results = query.execute();

       Product product = (Product) results.next();
       Iterator<ProductDetail> iter = product.getDetails().iterator();
       while (iter.hasNext()) {
          ProductDetail detail = iter.next();
          detail.setName("changed!");
       }
       _db.commit();
       _db.close();

       _db.begin();

       query = _db.getOQLQuery("SELECT product FROM " + Product.class.getName() + " product WHERE id = $1");
       query.bind(new Integer(1));
       results = query.execute();

       product = (Product) results.next();
       assertEquals("productdetail2", product.getDetails().toArray(new ProductDetail[0])[1].getName());

       _db.commit();
       _db.close();
    }

    public void testWriteProductDetailsLazy() throws Exception {
       _db.begin();

       OQLQuery query = _db.getOQLQuery("SELECT product FROM " + Product.class.getName() + " product WHERE id = $1");
       query.bind(new Integer(1));
       QueryResults results = query.execute();

       Product product = (Product) results.next();
       Iterator<ProductDetailLazy> iter = product.getDetailslazy().iterator();
       while (iter.hasNext()) {
          ProductDetailLazy detail = iter.next();
          detail.setName("changed!");
       }
       _db.commit();
       _db.close();

       _db.begin();

       query = _db.getOQLQuery("SELECT product FROM " + Product.class.getName() + " product WHERE id = $1");
       query.bind(new Integer(1));
       results = query.execute();

       product = (Product) results.next();
       assertEquals("productdetaillazy2", product.getDetailslazy().toArray(new ProductDetailLazy[0])[1].getName());

       _db.commit();
       _db.close();
    }

}
