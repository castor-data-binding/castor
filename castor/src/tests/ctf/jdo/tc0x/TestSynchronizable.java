/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$ 
 */

package ctf.jdo.tc0x;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.util.LocalConfiguration;

public final class TestSynchronizable extends CastorTestCase {
    /**
     * Property listing all the available {@link TxSynchronizable}
     * implementations (<tt>org.exolab.castor.persit.TxSynchronizable</tt>).
     */
    private static final String TX_SYNCHRONIZABLE_PROPERTY = 
        "org.exolab.castor.persist.TxSynchronizable";

    public static ArrayList     _synchronizables = new ArrayList();

    private JDOCategory         _category;
    private Database            _db;
    private String              _oldProperty;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestSynchronizable(final TestHarness category) {
        super(category, "TC09", "TxSynchronizable interceptor tests");
        _category = ( JDOCategory ) category;
        
        _synchronizables.clear();
    }
    
    /**
     * Get a JDO database
     */
    public void setUp() throws PersistenceException, SQLException {
        LocalConfiguration config = LocalConfiguration.getInstance();
        Properties props = config.getProperties();
        _oldProperty = (String) props.setProperty(
                TX_SYNCHRONIZABLE_PROPERTY, 
                SynchronizableImpl.class.getName());

        _db = _category.getDatabase();
    }

    public void runTest() throws PersistenceException, SQLException {
        _db.begin();
        
        OQLQuery query = _db.getOQLQuery(
                "select o from " + Sample.class.getName() + " o");
        QueryResults result = query.execute();
        while (result.hasMore()) { _db.remove(result.next()); }
        result.close();
        _db.commit();
        
        _synchronizables.clear();
        
        // create a default TestObject
        _db.begin();
        _db.create(new Sample());
        _db.commit();
        
        // update TestObject the first time
        _db.begin();
        Sample st = (Sample) _db.load(Sample.class,
                new Integer(Sample.DEFAULT_ID));
        st.setValue1(Sample.DEFAULT_VALUE_1 + Sample.DEFAULT_VALUE_1);
        st.setValue2(Sample.DEFAULT_VALUE_2 + Sample.DEFAULT_VALUE_2);
        _db.commit();
        
        // update TestObject the second time
        _db.begin();
        Sample lt = (Sample) _db.load(Sample.class,
                new Integer(Sample.DEFAULT_ID));
        lt.setValue1(Sample.DEFAULT_VALUE_1);
        lt.setValue2(Sample.DEFAULT_VALUE_2);
        _db.commit();
        
        // create another default TestObject
        // should fail and rollback
        try {
            _db.begin();
            _db.create(new Sample());
            _db.commit();
        } catch (Exception ex) {
            _db.rollback();
        }

        // remove TestObject
        _db.begin();
        _db.remove(_db.load(Sample.class,
                new Integer(Sample.DEFAULT_ID)));
        _db.commit();
        
        assertTrue("TxSynchronizable should see 5 instead of "
                + _synchronizables.size() + " changes",
                _synchronizables.size() == 5);
        assertTrue("1. change of TxSynchronizable is wrong",
                "created:3 / one / two".equals(_synchronizables.get(0)));
        assertTrue("2. change of TxSynchronizable is wrong",
                "updated:3 / oneone / twotwo".equals(_synchronizables.get(1)));
        assertTrue("3. change of TxSynchronizable is wrong",
                "updated:3 / one / two".equals(_synchronizables.get(2)));
        assertTrue("4. change of TxSynchronizable is wrong",
                "rolledback".equals(_synchronizables.get(3)));
        assertTrue("5. change of TxSynchronizable is wrong",
                "deleted:3 / one / two".equals(_synchronizables.get(4)));
    }

    public void tearDown() throws PersistenceException {
        _synchronizables.clear();
        _db.close();
        
        LocalConfiguration config = LocalConfiguration.getInstance();
        Properties props = config.getProperties();
        if (_oldProperty != null) {
            props.setProperty(TX_SYNCHRONIZABLE_PROPERTY, _oldProperty);
        } else {
            props.remove(TX_SYNCHRONIZABLE_PROPERTY);
        }
    }
}
