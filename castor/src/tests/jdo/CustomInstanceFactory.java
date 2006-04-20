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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package jdo;


import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.io.Serializable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Externalizable;
import java.io.IOException;
import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.JDO2;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;

import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.InstanceFactory;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;

/**
 * Test for the behaviors the InstanceFactory interface.
 *
 * @author <a href="adc@toolazydogs.com">Alan Cabrera</a>
 * @version $Revision$ $Date$
 */
public class CustomInstanceFactory extends CastorTestCase {

    private JDO                 _jdo;

    private JDOCategory         _category;

    private Database            _db;

    InternalCallbackInterceptor _i;

    public CustomInstanceFactory( TestHarness category ) {
        super( category, "TC48", "InstanceFactory interface tests" );
        _category = (JDOCategory) category;
        _i = new InternalCallbackInterceptor();
    }

    public void setUp() throws PersistenceException {

        _jdo = _category.getJDO();

        _jdo.setCallbackInterceptor(_i);

        _jdo.setInstanceFactory(_i);

        _db  = _category.getDatabase();

        OQLQuery      oql;
        TestObjectTimeStampable object;
        QueryResults  qres;


        stream.println( "Delete everything" );
        _db.begin();
        oql = _db.getOQLQuery( "SELECT p FROM jdo.TestObjectTimeStampable p WHERE id=$1" );
        oql.bind( TestObjectTimeStampable.DefaultId );
        qres = oql.execute();
        while ( qres.hasMore() ) {
            _db.remove( qres.next() );
        }
        oql.close();
        _db.commit();
    }

    public void runTest() throws PersistenceException {

        OQLQuery         oql;
        TestObjectTimeStampable       object;
        QueryResults     qres;
        CallbacksInvoked cbi = new CallbacksInvoked();

        cbi.allow( CallbacksInvoked.CREATING );
        cbi.allow( CallbacksInvoked.USING );
        cbi.allow( CallbacksInvoked.CREATED );
        cbi.allow( CallbacksInvoked.STORING );
        cbi.allow( CallbacksInvoked.RELEASING );

        _i._callbacksInvoked.init();
        _db.begin();

        object = new TestObjectTimeStampable();
        stream.println( "Creating new object: " + object );
        _db.create( object );

        _db.commit();

        if ( !cbi.equals( _i._callbacksInvoked ) ) {
            fail( "Callbacks were not properly invoked: " + cbi + " != " + _i._callbacksInvoked );
        }


        cbi.init();
        cbi.allow( CallbacksInvoked.USING );
        cbi.allow( CallbacksInvoked.LOADED );
        cbi.allow( CallbacksInvoked.STORING );
        cbi.allow( CallbacksInvoked.RELEASING );
        cbi.allow( CallbacksInvoked.INSTANTIATE );

        _i._callbacksInvoked.init();
        _db.begin();

        object = (TestObjectTimeStampable)_db.load( TestObjectTimeStampable.class, new Integer( TestObjectTimeStampable.DefaultId ) );
        object.setValue1("Alan");

        _db.commit();

        if ( !cbi.equals( _i._callbacksInvoked ) ) {
            fail( "Callbacks were not properly invoked: " + cbi + " != " + _i._callbacksInvoked );
        }


        cbi.init();
        cbi.allow( CallbacksInvoked.USING );
        cbi.allow( CallbacksInvoked.UPDATED );
        cbi.allow( CallbacksInvoked.STORING );
        cbi.allow( CallbacksInvoked.RELEASING );

        _i._callbacksInvoked.init();

        object.setValue2( "long transaction new value" );
        _db.begin();
        _db.update( object );
        _db.commit();

        if ( !cbi.equals( _i._callbacksInvoked ) ) {
            fail( "Callbacks were not properly invoked: " + cbi + " != " + _i._callbacksInvoked );
        }


        cbi.init();
        cbi.allow( CallbacksInvoked.USING );
        cbi.allow( CallbacksInvoked.LOADED );
        cbi.allow( CallbacksInvoked.REMOVING );
        cbi.allow( CallbacksInvoked.REMOVED );
        cbi.allow( CallbacksInvoked.RELEASING );
        cbi.allow( CallbacksInvoked.INSTANTIATE );

        _i._callbacksInvoked.init();
        _db.begin();

        object = (TestObjectTimeStampable)_db.load( TestObjectTimeStampable.class, new Integer( TestObjectTimeStampable.DefaultId ) );
        _db.remove( object );

        _db.commit();

        if ( !cbi.equals( _i._callbacksInvoked ) ) {
            fail( "Callbacks were not properly invoked: " + cbi + " != " + _i._callbacksInvoked );
        }

    }

    public void tearDown() throws PersistenceException {
        if ( _db.isActive() ) _db.rollback();

        OQLQuery      oql;
        TestObjectTimeStampable    object;
        QueryResults  qres;


        stream.println( "Delete everything" );
        _db.begin();
        oql = _db.getOQLQuery( "SELECT p FROM jdo.TestObjectTimeStampable p WHERE id=$1" );
        oql.bind( TestObjectTimeStampable.DefaultId );
        qres = oql.execute();
        while ( qres.hasMore() ) {
            _db.remove( qres.next() );
        }
        oql.close();
        _db.commit();

        _db.close();

        _jdo.setCallbackInterceptor(null);
    }

    class InternalCallbackInterceptor implements CallbackInterceptor, InstanceFactory {

        CallbacksInvoked _callbacksInvoked = new CallbacksInvoked();
        CallbacksInvoked _coverage = new CallbacksInvoked();

        public Class loaded( Object object, short accessMode ) throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.LOADED);
            _coverage.allow(CallbacksInvoked.LOADED);

            return object.getClass();
        }


        public void storing( Object object, boolean modified ) throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.STORING);
            _coverage.allow(CallbacksInvoked.STORING);
        }


        public void creating( Object object, Database db ) throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.CREATING);
            _coverage.allow(CallbacksInvoked.CREATING);
        }


        public void created( Object object ) throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.CREATED);
            _coverage.allow(CallbacksInvoked.CREATED);
        }


        public void removing( Object object ) throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.REMOVING);
            _coverage.allow(CallbacksInvoked.REMOVING);
        }


        public void removed( Object object ) throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.REMOVED);
            _coverage.allow(CallbacksInvoked.REMOVED);
        }


         public void releasing( Object object, boolean committed ) {
            _callbacksInvoked.allow(CallbacksInvoked.RELEASING);
            _coverage.allow(CallbacksInvoked.RELEASING);
        }


        public void using( Object object, Database db ) {
            _callbacksInvoked.allow(CallbacksInvoked.USING);
            _coverage.allow(CallbacksInvoked.USING);
        }


        public void updated( Object object ) throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.UPDATED);
            _coverage.allow(CallbacksInvoked.UPDATED);
        }

        public Object newInstance( String className, ClassLoader loader ) {
            _callbacksInvoked.allow(CallbacksInvoked.INSTANTIATE);
            _coverage.allow(CallbacksInvoked.INSTANTIATE);

            try {
                if (loader != null )
                    return loader.loadClass( className ).newInstance();
                else
                    return Class.forName( className ).newInstance();
            } catch (ClassNotFoundException e) {
            } catch ( IllegalAccessException e ) {
            } catch ( InstantiationException e ) {
            } catch ( ExceptionInInitializerError e ) {
            } catch ( SecurityException e ) {
            }
            return null;
        }
    }

    class CallbacksInvoked implements java.io.Externalizable {

        public static final int LOADED      = 0;        
        public static final int STORING     = 1;
        public static final int CREATING    = 2;    
        public static final int CREATED     = 3;   
        public static final int REMOVING    = 4;   
        public static final int REMOVED     = 5;
        public static final int RELEASING   = 6;   
        public static final int USING       = 7;  
        public static final int UPDATED     = 8; 
        public static final int INSTANTIATE = 9;


        private static final short DONT_CARE = 0;       
        private static final short ALLOW = 1;       
        private static final short DISALLOW = 2;       


        private short[] callsMade = new short[10];

        public CallbacksInvoked() {
            init();
        }

        public CallbacksInvoked(int[] operations) {
            init();

            for (int i=0; i < operations.length; i++) {
                allow( operations[i] );
            }
        }

        public void init() {
            for (int i=0; i < callsMade.length; i++) {
                callsMade[i] = DISALLOW;
            }
        }
        
        public void dontCare(int i) {
            if (i < 0 || i > callsMade.length - 1 ) return;
            callsMade[i] = DONT_CARE;
        }
        
        public void allow(int i) {
            if (i < 0 || i > callsMade.length - 1 ) return;
            callsMade[i] = ALLOW;
        }

        public void disallow(int i) {
            if (i < 0 || i > callsMade.length - 1 ) return;
            callsMade[i] = DISALLOW;
        }

        public boolean equals(Object object) {
            if ( !(object instanceof CallbacksInvoked ) ) return false;

            CallbacksInvoked that = (CallbacksInvoked)object;
            for (int i=0; i < callsMade.length; i++) {
                if (this.callsMade[i] != DONT_CARE
                    && that.callsMade[i] != DONT_CARE
                    && this.callsMade[i] != that.callsMade[i]) return false;
            }

            return true;
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            for (int i=0; i < callsMade.length; i++) {
                out.writeShort( callsMade[i] );
            }
        }
        
        public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {
            for (int i=0; i < callsMade.length; i++) {
                callsMade[i] = in.readShort();
            }
        }

        public String toString() {
            String str = "[";
            for (int i=0; i < callsMade.length; i++) {
                switch (callsMade[i]) {
                case DONT_CARE:
                    str += "*";
                    break;
                case ALLOW:
                    str += "1";
                    break;
                case DISALLOW:
                    str += "0";
                    break;
                }
            }
            str += "]";
            return str;
        }
    }
}
