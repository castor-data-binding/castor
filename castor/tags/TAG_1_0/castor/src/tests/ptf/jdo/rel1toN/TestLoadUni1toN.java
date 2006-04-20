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
package ptf.jdo.rel1toN;

import java.text.DecimalFormat;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.AccessMode;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class TestLoadUni1toN extends TestCase {
    private static final String JDO_CONF_FILE = "uni-jdo-conf.xml";
    private static final String DATABASE_NAME = "rel1toN_uni";
    
    private static final DecimalFormat DF = new DecimalFormat("#,##0");
    
    private static final Log LOG = LogFactory.getLog(TestLoadUni1toN.class);
    private static boolean _logHeader = false;
    
    private JDOManager      _jdo = null;
    
    private Database        _db = null;
    private OQLQuery        _queryState = null;
    private OQLQuery        _queryStateOID = null;
    private OQLQuery        _queryEquipment = null;
    private OQLQuery        _queryEquipmentOID = null;
    private OQLQuery        _queryService = null;
    private OQLQuery        _queryServiceOID = null;
    
    public static Test suite() throws Exception {
        String config = TestLoadUni1toN.class.getResource(JDO_CONF_FILE).toString();
        JDOManager.loadConfiguration(config, TestLoadUni1toN.class.getClassLoader());
        
        TestSuite suite = new TestSuite("Test load 1:n with unidirectional mapping");

        suite.addTest(new TestLoadUni1toN("testReadWriteEmpty"));
        suite.addTest(new TestLoadUni1toN("testReadWriteCached"));
        suite.addTest(new TestLoadUni1toN("testReadWriteOidEmpty"));
        suite.addTest(new TestLoadUni1toN("testReadWriteOidCached"));

        suite.addTest(new TestLoadUni1toN("testReadOnlyEmpty"));
        suite.addTest(new TestLoadUni1toN("testReadOnlyCached"));
        suite.addTest(new TestLoadUni1toN("testReadOnlyOidEmpty"));
        suite.addTest(new TestLoadUni1toN("testReadOnlyOidCached"));

        suite.addTest(new TestLoadUni1toN("testReadOnlyOidOnly"));

        return suite;
    }

    public TestLoadUni1toN() {
        super();
    }
    
    public TestLoadUni1toN(final String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();

        if (!_logHeader) {
            LOG.info(format("", "begin", "result", "iterate", "commit", "close"));
            _logHeader = true;
        }

        _jdo = JDOManager.createInstance(DATABASE_NAME);
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testReadWriteEmpty() throws Exception {
        long start = System.currentTimeMillis();
        
        _db = _jdo.getDatabase();
        _db.getCacheManager().expireCache();
        _db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = _db.getOQLQuery(
                "SELECT o FROM " + Locked.class.getName() + " o order by o.id");
        QueryResults results = query.execute();
        
        long result = System.currentTimeMillis();
        
        initIterateQueries();

        int count = 0;
        while (results.hasMore()) {
            iterateStates((Locked) results.next(), Database.Shared);

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        _db.commit();
        
        long commit = System.currentTimeMillis();
        
        _db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadWriteEmpty",
                         DF.format(begin - start),
                         DF.format(result - begin),
                         DF.format(iterate - result),
                         DF.format(commit - iterate),
                         DF.format(close - commit)));
    }
    
    public void testReadWriteCached() throws Exception {
        long start = System.currentTimeMillis();
        
        _db = _jdo.getDatabase();
        _db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = _db.getOQLQuery(
                "SELECT o FROM " + Locked.class.getName() + " o order by o.id");
        QueryResults results = query.execute();
        
        long result = System.currentTimeMillis();
        
        initIterateQueries();

        int count = 0;
        while (results.hasMore()) {
            iterateStates((Locked) results.next(), Database.Shared);

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        _db.commit();
        
        long commit = System.currentTimeMillis();
        
        _db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadWriteCached",
                         DF.format(begin - start),
                         DF.format(result - begin),
                         DF.format(iterate - result),
                         DF.format(commit - iterate),
                         DF.format(close - commit)));
    }
    
    public void testReadOnlyEmpty() throws Exception {
        long start = System.currentTimeMillis();
        
        _db = _jdo.getDatabase();
        _db.getCacheManager().expireCache();
        _db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = _db.getOQLQuery(
                "SELECT o FROM " + Locked.class.getName() + " o order by o.id");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        initIterateQueries();

        int count = 0;
        while (results.hasMore()) {
            iterateStates((Locked) results.next(), Database.ReadOnly);

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        _db.commit();
        
        long commit = System.currentTimeMillis();
        
        _db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadOnlyEmpty",
                         DF.format(begin - start),
                         DF.format(result - begin),
                         DF.format(iterate - result),
                         DF.format(commit - iterate),
                         DF.format(close - commit)));
    }
    
    public void testReadOnlyCached() throws Exception {
        long start = System.currentTimeMillis();
        
        _db =  _jdo.getDatabase();
        _db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = _db.getOQLQuery(
                "SELECT o FROM " + Locked.class.getName() + " o order by o.id");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        initIterateQueries();

        int count = 0;
        while (results.hasMore()) {
            iterateStates((Locked) results.next(), Database.ReadOnly);

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        _db.commit();
        
        long commit = System.currentTimeMillis();
        
        _db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadOnlyCached",
                         DF.format(begin - start),
                         DF.format(result - begin),
                         DF.format(iterate - result),
                         DF.format(commit - iterate),
                         DF.format(close - commit)));
    }

    public void testReadWriteOidEmpty() throws Exception {
        long start = System.currentTimeMillis();
        
        _db =  _jdo.getDatabase();
        _db.getCacheManager().expireCache();
        _db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = _db.getOQLQuery(
                "CALL SQL select PTF_LOCKED.ID as ID "
              + "from PTF_LOCKED order by PTF_LOCKED.ID "
              + "AS ptf.jdo.rel1toN.OID");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        initIterateQueriesOID();

        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            iterateStatesOID((Locked) _db.load(Locked.class, oid.getId()),
                             Database.Shared);

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        _db.commit();
        
        long commit = System.currentTimeMillis();
        
        _db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadWriteOidEmpty",
                         DF.format(begin - start),
                         DF.format(result - begin),
                         DF.format(iterate - result),
                         DF.format(commit - iterate),
                         DF.format(close - commit)));
    }
    
    public void testReadWriteOidCached() throws Exception {
        long start = System.currentTimeMillis();
        
        _db =  _jdo.getDatabase();
        _db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = _db.getOQLQuery(
                "CALL SQL select PTF_LOCKED.ID as ID "
              + "from PTF_LOCKED order by PTF_LOCKED.ID "
              + "AS ptf.jdo.rel1toN.OID");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        initIterateQueriesOID();

        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            iterateStatesOID((Locked) _db.load(Locked.class, oid.getId()),
                             Database.Shared);

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        _db.commit();
        
        long commit = System.currentTimeMillis();
        
        _db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadWriteOidCached",
                         DF.format(begin - start),
                         DF.format(result - begin),
                         DF.format(iterate - result),
                         DF.format(commit - iterate),
                         DF.format(close - commit)));
    }
    
    public void testReadOnlyOidEmpty() throws Exception {
        long start = System.currentTimeMillis();
        
        _db =  _jdo.getDatabase();
        _db.getCacheManager().expireCache();
        _db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = _db.getOQLQuery(
                "CALL SQL select PTF_LOCKED.ID as ID "
              + "from PTF_LOCKED order by PTF_LOCKED.ID "
              + "AS ptf.jdo.rel1toN.OID");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        initIterateQueriesOID();

        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            iterateStatesOID((Locked) _db.load(Locked.class, oid.getId(),
                             Database.ReadOnly), Database.ReadOnly);

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        _db.commit();
        
        long commit = System.currentTimeMillis();
        
        _db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadOnlyOidEmpty",
                         DF.format(begin - start),
                         DF.format(result - begin),
                         DF.format(iterate - result),
                         DF.format(commit - iterate),
                         DF.format(close - commit)));
    }
    
    public void testReadOnlyOidCached() throws Exception {
        long start = System.currentTimeMillis();
        
        _db =  _jdo.getDatabase();
        _db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = _db.getOQLQuery(
                "CALL SQL select PTF_LOCKED.ID as ID "
              + "from PTF_LOCKED order by PTF_LOCKED.ID "
              + "AS ptf.jdo.rel1toN.OID");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        initIterateQueriesOID();

        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            iterateStatesOID((Locked) _db.load(Locked.class, oid.getId(),
                             Database.ReadOnly), Database.ReadOnly);

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        _db.commit();
        
        long commit = System.currentTimeMillis();
        
        _db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadOnlyOidCached",
                         DF.format(begin - start),
                         DF.format(result - begin),
                         DF.format(iterate - result),
                         DF.format(commit - iterate),
                         DF.format(close - commit)));
    }
    
    public void testReadOnlyOidOnly() throws Exception {
        long start = System.currentTimeMillis();
        
        _db =  _jdo.getDatabase();
        _db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = _db.getOQLQuery(
                "CALL SQL select PTF_LOCKED.ID as ID "
              + "from PTF_LOCKED order by PTF_LOCKED.ID "
              + "AS ptf.jdo.rel1toN.OID");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            results.next();
            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        _db.commit();
        
        long commit = System.currentTimeMillis();
        
        _db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadOnlyOidOnly",
                         DF.format(begin - start),
                         DF.format(result - begin),
                         DF.format(iterate - result),
                         DF.format(commit - iterate),
                         DF.format(close - commit)));
    }

    private void initIterateQueries() throws Exception {
        _queryState = _db.getOQLQuery(
                "select o from " + State.class.getName() + " o "
              + "where o.locked=$1 order by o.id");
        _queryEquipment = _db.getOQLQuery(
                "select o from " + Equipment.class.getName() + " o "
              + "where o.state=$1 order by o.id");
        _queryService = _db.getOQLQuery(
                "select o from " + Service.class.getName() + " o "
              + "where o.equipment=$1 order by o.id");
    }
    
    private void initIterateQueriesOID() throws Exception {
        _queryStateOID = _db.getOQLQuery(
                "CALL SQL select PTF_STATE.ID as ID from PTF_STATE "
              + "where PTF_STATE.LOCKED_ID=$1 order by PTF_STATE.ID "
              + "AS ptf.jdo.rel1toN.OID");
        _queryEquipmentOID = _db.getOQLQuery(
                "CALL SQL select PTF_EQUIPMENT.ID as ID from PTF_EQUIPMENT "
              + "where PTF_EQUIPMENT.STATE_ID=$1 order by PTF_EQUIPMENT.ID "
              + "AS ptf.jdo.rel1toN.OID");
        _queryServiceOID = _db.getOQLQuery(
                "CALL SQL select PTF_SERVICE.ID as ID from PTF_SERVICE "
              + "where PTF_SERVICE.EQUIPMENT_ID=$1 order by PTF_SERVICE.ID "
              + "AS ptf.jdo.rel1toN.OID");
    }
    
    private void iterateStates(final Locked locked, final AccessMode mode)
    throws Exception {
        _queryState.bind(locked.getId());
        QueryResults results = _queryState.execute(mode);
        
        while (results.hasMore()) {
            iterateEquipments((State) results.next(), mode);
        }
    }
    
    private void iterateStatesOID(final Locked locked, final AccessMode mode)
    throws Exception {
        _queryStateOID.bind(locked.getId());
        QueryResults results = _queryStateOID.execute(mode);
        
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            iterateEquipmentsOID((State) _db.load(State.class, oid.getId(), mode), mode);
        }
    }
    
    private void iterateEquipments(final State state, final AccessMode mode)
    throws Exception {
        _queryEquipment.bind(state.getId());
        QueryResults results = _queryEquipment.execute(mode);
        
        while (results.hasMore()) {
            iterateServices((Equipment) results.next(), mode);
        }
    }
    
    private void iterateEquipmentsOID(final State state, final AccessMode mode)
    throws Exception {
        _queryEquipmentOID.bind(state.getId());
        QueryResults results = _queryEquipmentOID.execute(Database.ReadOnly);
        
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            iterateServicesOID((Equipment) _db.load(Equipment.class, oid.getId(), mode),
                    mode);
        }
    }
    
    private void iterateServices(final Equipment equipment, final AccessMode mode)
    throws Exception {
        _queryService.bind(equipment.getId());
        QueryResults results = _queryService.execute(mode);
        
        while (results.hasMore()) {
            results.next();
        }
    }
    
    private void iterateServicesOID(final Equipment equipment, final AccessMode mode)
    throws Exception {
        _queryServiceOID.bind(equipment.getId());
        QueryResults results = _queryServiceOID.execute(Database.ReadOnly);
        
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            _db.load(Service.class, oid.getId(), mode);
        }
    }
    
    private static String format(final String head, final String begin,
                                 final String result, final String iterate,
                                 final String commit, final String close) {
        
        StringBuffer sb = new StringBuffer();
        sb.append(format(head, 20, true));
        sb.append(format(begin, 10, false));
        sb.append(format(result, 10, false));
        sb.append(format(iterate, 10, false));
        sb.append(format(commit, 10, false));
        sb.append(format(close, 10, false));
        return sb.toString();
    }
    
    private static String format(final String str, final int len, final boolean after) {
        StringBuffer sb = new StringBuffer();
        if (str != null) {
            sb.append(str);
            for (int i = str.length(); i < len; i++) {
                if (after) {
                    sb.append(' ');
                } else {
                    sb.insert(0, ' ');
                }
            }
        } else {
            for (int i = 0; i < len; i++) {
                if (after) {
                    sb.append(' ');
                } else {
                    sb.insert(0, ' ');
                }
            }
        }
        return sb.toString();
    }
}
