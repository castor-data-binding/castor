/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test99;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;

/**
 * Tests that modification to read only objects are not persist in the database.
 */
public final class TestPolymorphismMany2Many extends CPATestCase {
    private static final String DBNAME = "test99";
    private static final String MAPPING = "/org/castor/cpa/test/test99/mapping.xml";

    public TestPolymorphismMany2Many(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL);
    }

    public void testCreateProd() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.setAutoStore(true);
        db.begin();
        
        Product1893 pd1 = new Product1893();
        pd1.setName("Product-1");
        pd1.setDescription("Product-1: <30");
        db.create(pd1);
        
        Product1893 pd2 = new Product1893();
        pd2.setName("Product-2");
        pd2.setDescription("Product-2: <30");
        db.create(pd2);
        
        db.commit();
        db.close();
    }

    public void testCreateCrafts() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.setAutoStore(true);
        db.begin();
        
        Craft crf1 = new Craft();
        crf1.setName("Craft-1");
        crf1.setDescription("Craft-1: <30");
        db.create(crf1);
        
        Craft crf2 = new Craft();
        crf2.setName("Craft-2");
        crf2.setDescription("Craft-2: <30");
        db.create(crf2);
        
        db.commit();
        db.close();
    }

    public void testCreateCulture() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.setAutoStore(true);
        db.begin();
        
        Culture cult1 = new Culture();
        cult1.setName("Culture-1");
        cult1.setDescription("Culture-1:  <30");
        db.create(cult1);
        
        Culture cult2 = new Culture();
        cult2.setName("Culture-2");
        cult2.setDescription("Culture-2: <30");
        db.create(cult2);
        
        db.commit();
        db.close();
    }
    
    public void testCreateAccomodation() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.setAutoStore(true);
        db.begin();
        
        Accommodation acc1 = new Accommodation();
        acc1.setName("Accommodation-1");
        acc1.setDescription("Accommodation-1: <30");
        acc1.setBestSeason("Season-1");
        db.create(acc1);
        
        Accommodation acc2 = new Accommodation();
        acc2.setName("Accommodation-2");
        acc2.setDescription("Accommodation-2: <30");
        acc2.setBestSeason("Season-2");
        db.create(acc2);
        
        db.commit();
        db.close();
    }
    
    public void testCreateComposed() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.setAutoStore(true);
        db.begin();
        
        ComposedProduct compp1 = new ComposedProduct();
        compp1.setName("Composition-1");
        compp1.setExtraName("Xtra-Composition-1");
        compp1.setDescription("Composition-1: <30");
        compp1.setExtraDescription("Xtra-Composition-1: <30");
        db.create(compp1);
        
        ComposedProduct compp2 = new ComposedProduct();
        compp2.setName("Composition-2");
        compp2.setExtraName("Xtra-Composition-2");
        compp2.setDescription("Composition-2: <30");
        compp2.setExtraDescription("Xtra-Composition-2: <30");
        db.create(compp2);
        
        db.commit();
        db.close();
    }
    
    public void testComposeOffer() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.setAutoStore(true);
        db.begin();
        
        Product1893 pd1 = new Product1893();
        pd1.setName("Just product");
        db.create(pd1);
        
        Accommodation  acc1 = new Accommodation();
        acc1.setName("Comp-Accommodation-2");
        acc1.setDescription("Comp-Accommodation-2: <30");
        acc1.setBestSeason("Comp-Season-2");
        db.create(acc1);
        
        ComposedProduct compp1 = new ComposedProduct();
        compp1.setName("Composition-3");
        compp1.setExtraName("Xtra-Composition-3");
        compp1.setDescription("Composition-3: <30");
        compp1.setExtraDescription("Xtra-Composition-3: <30");
        compp1.addSubProduct(pd1);
        compp1.addSubProduct(acc1);
        db.create(compp1);
        
        db.commit();
        db.close();
    }
}
