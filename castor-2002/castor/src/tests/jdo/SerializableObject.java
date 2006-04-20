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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package jdo;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Collection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.lang.Math;
import java.util.Vector;
import java.util.Random;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;
import java.util.ArrayList;


/**
 * Test for serializable depedent object
 */
public class SerializableObject extends CWTestCase {

    private Database       _db;


    private Connection     _conn;


    private JDOCategory    _category;


    public SerializableObject( CWTestCategory category )
            throws CWClassConstructorException {

        super( "TC29", "Serializable" );
        _category = (JDOCategory) category;
    }

    public void preExecute() {
        super.preExecute();
    }

    public void postExecute() {
        super.postExecute();
    }


    public boolean run( CWVerboseStream stream ) {
        try {

            _db = _category.getDatabase( stream.verbose() );
            _conn = _category.getJDBCConnection(); 

            stream.writeVerbose( "Running..." );
            stream.writeVerbose( "" );

            // delete everything
            _conn.createStatement().executeUpdate( "DELETE test_serial" );
            _conn.commit();

            // create new object with an serializable dependent object
            _db.begin();
            TestSerial master = new TestSerial();
            master.setId( 1 );
            master.setSerializableObject( new TestSerializableObject() );
            master.getSerializableObject().aCoolString = "Very cool!";
            master.getSerializableObject().ints = new int[] { 1, 3, 5, 7, 9 };
            _db.create( master );
            _db.commit();

            // test if object created properly
            _db.begin();
            TestSerial testSerial = (TestSerial) _db.load( TestSerial.class, new Integer(1) );
            if ( testSerial == null )
                throw new Exception( "Object creation failed!" );
                
            if ( testSerial.getSerializableObject() == null ||
                    !testSerial.getSerializableObject().aCoolString.equals("Very cool!") ||
                    testSerial.getSerializableObject().ints == null ||
                    testSerial.getSerializableObject().ints.length != 5 ||
                    testSerial.getSerializableObject().ints[0] != 1 ||
                    testSerial.getSerializableObject().ints[1] != 3 ||
                    testSerial.getSerializableObject().ints[2] != 5 ||
                    testSerial.getSerializableObject().ints[3] != 7 ||
                    testSerial.getSerializableObject().ints[4] != 9 )
                throw new Exception( "dependent objects creation failed!" + testSerial );

            // modify the object
            testSerial.getSerializableObject().ints[1] = 103;
            testSerial.getSerializableObject().ints[3] = 107;
            testSerial.getSerializableObject().aCoolString = "Very very cool!";
            _db.commit();

            _db.begin();
            testSerial = (TestSerial) _db.load( TestSerial.class, new Integer(1) );
            if ( testSerial == null )
                throw new Exception( "dependent modfiication failed!" + testSerial );
                
            if ( testSerial.getSerializableObject() == null ||
                    !testSerial.getSerializableObject().aCoolString.equals("Very very cool!") ||
                    testSerial.getSerializableObject().ints == null ||
                    testSerial.getSerializableObject().ints.length != 5 ||
                    testSerial.getSerializableObject().ints[0] != 1 ||
                    testSerial.getSerializableObject().ints[1] != 103 ||
                    testSerial.getSerializableObject().ints[2] != 5 ||
                    testSerial.getSerializableObject().ints[3] != 107 ||
                    testSerial.getSerializableObject().ints[4] != 9 )
                throw new Exception( "dependent modification failed!" + testSerial );

            // set the field to null;
            testSerial.getSerializableObject().ints = null;
            testSerial.getSerializableObject().aCoolString = null;
            _db.commit();

            _db.begin();
            testSerial = (TestSerial) _db.load( TestSerial.class, new Integer(1) );
            if ( testSerial == null )
                throw new Exception( "dependent modfiication failed!" + testSerial );
                
            if ( testSerial.getSerializableObject() == null ||
                    testSerial.getSerializableObject().aCoolString != null ||
                    testSerial.getSerializableObject().ints != null )
                throw new Exception( "dependent modification failed!" + testSerial );

            // setSerializableObject( null );
            testSerial.setSerializableObject( null );
            _db.commit();

            _db.begin();
            testSerial = (TestSerial) _db.load( TestSerial.class, new Integer(1) );
            if ( testSerial == null )
                throw new Exception( "dependent modfiication failed!" + testSerial );
                
            if ( testSerial.getSerializableObject() != null )
                throw new Exception( "dependent modification failed!" + testSerial );
            _db.commit();

        } catch ( Exception e ) {
            e.printStackTrace();

            stream.writeVerbose( "Exception: "+ e );
            try {
                if ( _db.isActive() )
                    _db.close();
                return false;
            } catch ( Exception ex ) {
                return false;
            }
        }
        return true;
    }
}


