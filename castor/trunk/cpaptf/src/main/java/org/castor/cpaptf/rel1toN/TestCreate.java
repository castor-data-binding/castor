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
package org.castor.cpaptf.rel1toN;

import java.text.DecimalFormat;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision:6814 $ $Date: 2005-06-24 19:41:08 -0600 (Fri, 24 Jun 2005) $
 */
public final class TestCreate extends TestCase {
    private static final String JDO_CONF_FILE = "uni-jdo-conf.xml";
    private static final String DATABASE_NAME = "rel1toN_uni";
    
    /** Factor that influences how much test objects are created. Defaults to 1.0 which
     *  means that 10000 service objects are created. Minimum is 0.2 and maximum depends
     *  onthe amount of memory you have available for your virtual machine. */
    private static final double FACTOR = 0.2;
    
    private static final int LOCKED_MAX = 4;
    
    private static final int STATE_MAX = (int) (7 * FACTOR);
    private static final int DEPARTMENT_MAX = (int) (29 * FACTOR);
    private static final int REASON_MAX = (int) (11 * FACTOR);
    private static final int SUPPLIER_MAX = (int) (17 * FACTOR);
    private static final int TYPE_MAX = (int) (333 * FACTOR);
    private static final int EQUIPMENT_MAX = (int) (1967 * FACTOR);
    private static final int SERVICE_MAX = (int) (10000 * FACTOR);
    
    private static final Log LOG = LogFactory.getLog(TestCreate.class);
    
    private JDOManager _jdo = null;

    public static Test suite() throws Exception {
        String config = TestCreate.class.getResource(JDO_CONF_FILE).toString();
        JDOManager.loadConfiguration(config, TestCreate.class.getClassLoader());

        TestSuite suite = new TestSuite("Setup ptf.jdo.rel1toN test objects");

        suite.addTest(new TestCreate("testLoadLocked"));
        
        suite.addTest(new TestCreate("testCreateState"));
        suite.addTest(new TestCreate("testCreateDepartment"));
        suite.addTest(new TestCreate("testCreateReason"));
        suite.addTest(new TestCreate("testCreateSupplier"));
        suite.addTest(new TestCreate("testCreateType"));
        suite.addTest(new TestCreate("testCreateEquipment"));
        suite.addTest(new TestCreate("testCreateService"));

        return suite;
    }

    public TestCreate() {
        super();
    }
    
    public TestCreate(final String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        
        _jdo = JDOManager.createInstance(DATABASE_NAME);
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testLoadLocked() throws Exception {
        Database db = _jdo.getDatabase();
        db.begin();
        
        OQLQuery query = db.getOQLQuery("SELECT o FROM " + Locked.class.getName() + " o");
        QueryResults results = query.execute();
        
        int count = 0;
        while (results.hasMore()) {
            Locked locked = (Locked) results.next();
            assertTrue(new Integer(1).equals(locked.getId())
                    || new Integer(2).equals(locked.getId())
                    || new Integer(3).equals(locked.getId())
                    || new Integer(4).equals(locked.getId()));
            count++;
        }
        assertTrue(4 == count);
        
        db.commit();
        db.close();
    }
    
    public void testCreateState() throws Exception {
        Locked locked;
        int countLocked = 1;
        State state;
        Date date = new Date();

        long time = System.currentTimeMillis();
        int count = 1;
        
        Database db = _jdo.getDatabase();
        db.begin();
        
        while (count <= STATE_MAX) {
            locked = (Locked) db.load(Locked.class, new Integer(countLocked));
            
            state = new State();
            state.setId(new Integer(count));
            state.setName("state " + count);
            state.setLocked(locked);
            state.setInput((count % 2) == 0);
            state.setOutput((count % 2) == 1);
            state.setService((count % 3) == 0);
            state.setChangeFrom((count % 3) == 1);
            state.setChangeTo((count % 3) == 2);
            state.setCreated(date, "user");
            state.setUpdated(date, "user");
            
            db.create(state);

            countLocked = (countLocked < LOCKED_MAX) ? (countLocked + 1) : 1;
            count++;
        }
        
        db.commit();
        db.close();

        LOG.info("Created " + (count - 1) + " state objects in "
               + (System.currentTimeMillis() - time) + "ms.");
    }
    
    public void testCreateDepartment() throws Exception {
        State state;
        int countState = 1;
        Department dept;
        Date date = new Date();

        long time = System.currentTimeMillis();
        int count = 1;

        Database db = _jdo.getDatabase();
        db.begin();
        
        while (count <= DEPARTMENT_MAX) {
            state = (State) db.load(State.class, new Integer(countState));
            
            dept = new Department();
            dept.setId(new Integer(count));
            dept.setName("department " + count);
            dept.setState(state);
            dept.setCreated(date, "user");
            dept.setUpdated(date, "user");
            
            db.create(dept);
            
            countState = (countState < STATE_MAX) ? (countState + 1) : 1;
            count++;
        }
        
        db.commit();
        db.close();

        LOG.info("Created " + (count - 1) + " department objects in "
               + (System.currentTimeMillis() - time) + "ms.");
    }
    
    public void testCreateReason() throws Exception {
        Reason reason;
        Date date = new Date();

        long time = System.currentTimeMillis();
        int count = 1;

        Database db = _jdo.getDatabase();
        db.begin();
        
        while (count <= REASON_MAX) {
            reason = new Reason();
            reason.setId(new Integer(count));
            reason.setName("reason " + count);
            reason.setFailure((count % 2) == 0);
            reason.setCreated(date, "user");
            reason.setUpdated(date, "user");
            
            db.create(reason);
            
            count++;
        }
        
        db.commit();
        db.close();

        LOG.info("Created " + (count - 1) + " reason objects in "
               + (System.currentTimeMillis() - time) + "ms.");
    }
    
    public void testCreateSupplier() throws Exception {
        Supplier supplier;
        Date date = new Date();

        long time = System.currentTimeMillis();
        int count = 1;

        Database db = _jdo.getDatabase();
        db.begin();
        
        while (count <= SUPPLIER_MAX) {
            supplier = new Supplier();
            supplier.setId(new Integer(count));
            supplier.setName("supplier " + count);
            supplier.setCreated(date, "user");
            supplier.setUpdated(date, "user");
            
            db.create(supplier);
            
            count++;
        }
        
        db.commit();
        db.close();

        LOG.info("Created " + (count - 1) + " supplier objects in "
               + (System.currentTimeMillis() - time) + "ms.");
    }
    
    public void testCreateType() throws Exception {
        Type type;
        Date date = new Date();

        long time = System.currentTimeMillis();
        int count = 1;

        Database db = _jdo.getDatabase();
        db.begin();
        
        while (count <= TYPE_MAX) {
            type = new Type();
            type.setId(new Integer(count));
            type.setNumber(new Integer(5000 + count).toString());
            type.setCreated(date, "user");
            type.setUpdated(date, "user");
            
            db.create(type);
            
            count++;
        }
        
        db.commit();
        db.close();

        LOG.info("Created " + (count - 1) + " type objects in "
               + (System.currentTimeMillis() - time) + "ms.");
    }
    
    public void testCreateEquipment() throws Exception {
        DecimalFormat df = new DecimalFormat("000000");
        
        Type type;
        int countType = 1;
        Supplier supplier;
        int countSupplier = 1;
        State state;
        int countState = 1;
        Reason reason;
        int countReason = 1;
        Equipment equip;
        Date date = new Date();

        long time = System.currentTimeMillis();
        int count = 1;

        Database db = _jdo.getDatabase();
        db.begin();
        
        while (count <= EQUIPMENT_MAX) {
            type = (Type) db.load(Type.class, new Integer(countType));
            supplier = (Supplier) db.load(Supplier.class, new Integer(countSupplier));
            state = (State) db.load(State.class, new Integer(countState));
            reason = (Reason) db.load(Reason.class, new Integer(countReason));
            
            equip = new Equipment();
            equip.setId(new Integer(count));
            equip.setType(type);
            equip.setNumber(new Integer(10000 + count).toString());
            equip.setSupplier(supplier);
            equip.setDelivery(new Integer(7 + (count % 13)));
            equip.setCost(new Double(12.3456 * (7 + (count % 13))));
            equip.setSerial("S/N" + df.format(count));
            equip.setState(state);
            equip.setReason(reason);
            equip.setCount(new Integer(count));
            equip.setCreated(date, "user");
            equip.setUpdated(date, "user");
            
            db.create(equip);
            
            countType = (countType < TYPE_MAX) ? (countType + 1) : 1;
            countSupplier = (countSupplier < SUPPLIER_MAX) ? (countSupplier + 1) : 1;
            countState = (countState < STATE_MAX) ? (countState + 1) : 1;
            countReason = (countReason < REASON_MAX) ? (countReason + 1) : 1;
            count++;
        }
        
        db.commit();
        db.close();

        LOG.info("Created " + (count - 1) + " equipment objects in "
               + (System.currentTimeMillis() - time) + "ms.");
    }

    public void testCreateService() throws Exception {
        Equipment equip;
        int countEquip = 1;
        Service service;
        Date date = new Date();

        long time = System.currentTimeMillis();
        int count = 1;

        Database db = _jdo.getDatabase();
        db.begin();
        
        while (count <= SERVICE_MAX) {
            equip = (Equipment) db.load(Equipment.class, new Integer(countEquip));
            
            service = new Service();
            service.setId(new Integer(count));
            service.setEquipment(equip);
            service.setNumber(new Integer(count));
            service.setName("type " + count);
            service.setDate(date);
            service.setFlag1((count % 2) == 0);
            service.setFlag2((count % 2) == 1);
            service.setFlag3((count % 3) == 0);
            service.setFlag4((count % 3) == 1);
            service.setCreated(date, "user");
            service.setUpdated(date, "user");
            
            db.create(service);
            
            countEquip = (countEquip < EQUIPMENT_MAX) ? (countEquip + 1) : 1;
            count++;
        }
        
        db.commit();
        db.close();

        LOG.info("Created " + (count - 1) + " service objects in "
               + (System.currentTimeMillis() - time) + "ms.");
    }
}
