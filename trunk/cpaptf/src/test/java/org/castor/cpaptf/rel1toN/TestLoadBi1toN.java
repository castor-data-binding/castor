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
import java.util.Collection;
import java.util.Iterator;

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
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision:6817 $ $Date: 2006-03-14 06:22:05 -0700 (Tue, 14 Mar 2006) $
 */
public final class TestLoadBi1toN extends TestCase {
    private static final String JDO_CONF_FILE = "bi-jdo-conf.xml";
    private static final String DATABASE_NAME = "rel1toN_bi";
    
    private static final DecimalFormat DF = new DecimalFormat("#,##0");
    
    private static final Log LOG = LogFactory.getLog(TestLoadBi1toN.class);
    private static boolean _logHeader = false;
    
    private JDOManager _jdo = null;
    
    public static Test suite() throws Exception {
        String config = TestLoadBi1toN.class.getResource(JDO_CONF_FILE).toString();
        JDOManager.loadConfiguration(config, TestLoadBi1toN.class.getClassLoader());
        
        TestSuite suite = new TestSuite("Test load 1:n with bidirectional mapping");

        suite.addTest(new TestLoadBi1toN("testReadWriteEmpty"));
        suite.addTest(new TestLoadBi1toN("testReadWriteCached"));
        suite.addTest(new TestLoadBi1toN("testReadWriteOidEmpty"));
        suite.addTest(new TestLoadBi1toN("testReadWriteOidCached"));

        suite.addTest(new TestLoadBi1toN("testReadOnlyEmpty"));
        suite.addTest(new TestLoadBi1toN("testReadOnlyCached"));
        suite.addTest(new TestLoadBi1toN("testReadOnlyOidEmpty"));
        suite.addTest(new TestLoadBi1toN("testReadOnlyOidCached"));

        suite.addTest(new TestLoadBi1toN("testReadOnlyOidOnly"));

        return suite;
    }

    public TestLoadBi1toN() {
        super();
    }
    
    public TestLoadBi1toN(final String name) {
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
        
        Database db = _jdo.getDatabase();
        db.getCacheManager().expireCache();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "SELECT o FROM " + Locked.class.getName() + " o order by o.id");
        QueryResults results = query.execute();
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            iterateStates((Locked) results.next());

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

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
        
        Database db = _jdo.getDatabase();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "SELECT o FROM " + Locked.class.getName() + " o order by o.id");
        QueryResults results = query.execute();
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            iterateStates((Locked) results.next());

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

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
        
        Database db = _jdo.getDatabase();
        db.getCacheManager().expireCache();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "SELECT o FROM " + Locked.class.getName() + " o order by o.id");
        QueryResults results = query.execute(Database.READONLY);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            iterateStates((Locked) results.next());

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

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
        
        Database db = _jdo.getDatabase();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "SELECT o FROM " + Locked.class.getName() + " o order by o.id");
        QueryResults results = query.execute(Database.READONLY);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            iterateStates((Locked) results.next());

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

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
        
        Database db = _jdo.getDatabase();
        db.getCacheManager().expireCache();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "CALL SQL select PTF_LOCKED.ID as ID "
              + "from PTF_LOCKED order by PTF_LOCKED.ID "
              + "AS " + OID.class.getName());
        QueryResults results = query.execute(Database.READONLY);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            iterateStates((Locked) db.load(Locked.class, oid.getId()));

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

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
        
        Database db = _jdo.getDatabase();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "CALL SQL select PTF_LOCKED.ID as ID "
              + "from PTF_LOCKED order by PTF_LOCKED.ID "
              + "AS " + OID.class.getName());
        QueryResults results = query.execute(Database.READONLY);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            iterateStates((Locked) db.load(Locked.class, oid.getId()));

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

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
        
        Database db = _jdo.getDatabase();
        db.getCacheManager().expireCache();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "CALL SQL select PTF_LOCKED.ID as ID "
              + "from PTF_LOCKED order by PTF_LOCKED.ID "
              + "AS " + OID.class.getName());
        QueryResults results = query.execute(Database.READONLY);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            iterateStates((Locked) db.load(Locked.class, oid.getId(), Database.READONLY));

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

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
        
        Database db = _jdo.getDatabase();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "CALL SQL select PTF_LOCKED.ID as ID "
              + "from PTF_LOCKED order by PTF_LOCKED.ID "
              + "AS " + OID.class.getName());
        QueryResults results = query.execute(Database.READONLY);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            iterateStates((Locked) db.load(Locked.class, oid.getId(), Database.READONLY));

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

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
        
        Database db = _jdo.getDatabase();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "CALL SQL select PTF_LOCKED.ID as ID "
              + "from PTF_LOCKED order by PTF_LOCKED.ID "
              + "AS " + OID.class.getName());
        QueryResults results = query.execute(Database.READONLY);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            results.next();
            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadOnlyOidOnly",
                         DF.format(begin - start),
                         DF.format(result - begin),
                         DF.format(iterate - result),
                         DF.format(commit - iterate),
                         DF.format(close - commit)));
    }
    
    private void iterateStates(final Locked locked) {
        Collection states = locked.getStates();
        Iterator iter = states.iterator();
        while (iter.hasNext()) {
            iterateEquipments((State) iter.next());
        }
    }
    
    private void iterateEquipments(final State state) {
        Collection equipments = state.getEquipments();
        Iterator iter = equipments.iterator();
        while (iter.hasNext()) {
            iterateServices((Equipment) iter.next());
        }
    }
    
    private void iterateServices(final Equipment equipment) {
        Collection services = equipment.getServices();
        Iterator iter = services.iterator();
        while (iter.hasNext()) {
            iter.next();
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
