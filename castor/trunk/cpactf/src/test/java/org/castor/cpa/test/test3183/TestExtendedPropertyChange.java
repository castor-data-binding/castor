/*
 * Copyright 2011 Johannes Venzke, Ralf Joachim
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

package org.castor.cpa.test.test3183;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 * @author <a href="johannes DOT venzke AT revival DOT de">Johannes Venzke</a>
 */
public final class TestExtendedPropertyChange extends CPATestCase {
    private static final String DBNAME = "test3183";
    private static final String MAPPING = "/org/castor/cpa/test/test3183/mapping.xml";
    private Database _db;
    private Integer _orderId;
    
    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.SQL_SERVER);
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
    }
    
    @Test
    public void testPolyOrderPriceChange() throws PersistenceException {
        createPolyOrder();
        changePolyPrice();
        checkPolyPrice();
        _db.getCacheManager().expireCache();
        checkPolyPrice();
    }
    
    private void createPolyOrder() throws PersistenceException {
        _db.begin();
        
        PolyOrderDoc doc = new PolyOrderDoc();
        doc.setName("new order");
        doc.setPrice(0d);
        _db.create(doc);
        
        _orderId = doc.getId();
        
        _db.commit();
    }
    
    private void changePolyPrice() throws PersistenceException {
        _db.begin();
        
        PolyOrderDoc doc = _db.load(PolyOrderDoc.class, _orderId);
        doc.setPrice(10d);
        
        _db.commit();
    }
    
    private void checkPolyPrice() throws PersistenceException {
        _db.begin();
        
        PolyOrderDoc doc = _db.load(PolyOrderDoc.class, _orderId);
        assertEquals(10d, doc.getPrice());
        
        _db.commit();
    }
    
    @Test
    public void testSingleOrderPriceChange() throws PersistenceException {
        createSingleOrder();
        changeSinglePrice();
        checkSinglePrice();
        _db.getCacheManager().expireCache();
        checkSinglePrice();
    }
    
    private void createSingleOrder() throws PersistenceException {
        _db.begin();
        
        SingleOrderDoc doc = new SingleOrderDoc();
        doc.setName("new order");
        doc.setPrice(0d);
        _db.create(doc);
        
        _orderId = doc.getId();
        
        _db.commit();
    }
    
    private void changeSinglePrice() throws PersistenceException {
        _db.begin();
        
        SingleOrderDoc doc = _db.load(SingleOrderDoc.class, _orderId);
        doc.setPrice(10d);
        
        _db.commit();
    }
    
    private void checkSinglePrice() throws PersistenceException {
        _db.begin();
        
        SingleOrderDoc doc = _db.load(SingleOrderDoc.class, _orderId);
        assertEquals(10d, doc.getPrice());
        
        _db.commit();
    }
}
