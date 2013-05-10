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
import java.util.ArrayList;
import java.util.Collection;
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
             || (engine == DatabaseEngineType.DERBY));
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
       ProductDetail detail1 = new ProductDetail();
       detail1.setId(1);
       detail1.setName("productdetail1");

       ProductDetail detail2 = new ProductDetail();
       detail2.setId(2);
       detail2.setName("productdetail2");

       ProductDetail detail3 = new ProductDetail();
       detail3.setId(3);
       detail3.setName("productdetail3");

       ProductDetailLazy detailLazy = new ProductDetailLazy();
       detailLazy.setId(1);
       detailLazy.setName("productdetaillazy1");

       ProductDetailLazy detailLazy2 = new ProductDetailLazy();
       detailLazy2.setId(2);
       detailLazy2.setName("productdetaillazy2");

       ProductDetailLazy detailLazy3 = new ProductDetailLazy();
       detailLazy3.setId(3);
       detailLazy3.setName("productdetaillazy3");
       
       Product product = new Product();
       product.setId(1);
       product.setName("product1");
       
       Collection<ProductDetail> details = new ArrayList<ProductDetail>();
       detail1.setProduct(product);
       details.add(detail1);
       detail2.setProduct(product);
       details.add(detail2);
       detail3.setProduct(product);
       details.add(detail3);
       product.setDetails(details);

       Collection<ProductDetailLazy> lazyDetails = new ArrayList<ProductDetailLazy>();
       detailLazy.setProduct(product);
       lazyDetails.add(detailLazy);
       detailLazy2.setProduct(product);
       lazyDetails.add(detailLazy2);
       detailLazy3.setProduct(product);
       lazyDetails.add(detailLazy3);
       product.setDetailslazy(lazyDetails);

       _db.create(product);
       _db.commit();
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

       _db.begin();

       query = _db.getOQLQuery("SELECT product FROM " + Product.class.getName() + " product WHERE id = $1");
       query.bind(new Integer(1));
       results = query.execute();

       product = (Product) results.next();
       assertEquals("productdetail2", product.getDetails().toArray(new ProductDetail[0])[1].getName());

       _db.commit();
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

       _db.begin();

       query = _db.getOQLQuery("SELECT product FROM " + Product.class.getName() + " product WHERE id = $1");
       query.bind(new Integer(1));
       results = query.execute();

       product = (Product) results.next();
       assertEquals("productdetaillazy2", product.getDetailslazy().toArray(new ProductDetailLazy[0])[1].getName());

       _db.commit();
    }

}
