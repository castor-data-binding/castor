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


package org.exolab.castor.persist.sql;


import java.sql.DriverManager;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.Properties;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.conf.*;
import org.exolab.castor.persist.LogInterceptor;
import org.exolab.castor.persist.Key;
import org.exolab.castor.persist.Entity;
import org.exolab.castor.persist.EntityInfo;
import org.exolab.castor.persist.EntityFieldInfo;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.Connector;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.util.Messages;

/**
 * {@link PersistenceFactory} for generic JDBC driver.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class SQLConnector implements Connector {
    private HashMap         _connections;
    private HashMap         _listeners;
    private boolean         _globalTx;
    private Properties      _pros;
    private String          _name;
    private String          _url;
    private LogInterceptor  _log;
    private DataSource      _ds;

    public SQLConnector( String name, String url, Properties pros ) {
        _connections = new HashMap();
        _name  = name;
        _url   = url;
        _pros  = pros;
    }

    public SQLConnector( String name, DataSource ds ) {
        _name  = name;
        _ds    = ds;
    }

    public void start( Key key ) throws PersistenceException {
        try {
            Connection conn;
            if ( _connections.containsKey( key ) )
                return;

            if ( _ds != null )
                conn = _ds.getConnection();
            else
                conn = DriverManager.getConnection( _url, _pros );

            synchronized( _connections ) {
                _connections.put( key, conn );
            }
        } catch (SQLException except) {
            throw new PersistenceException( Messages.format("persist.nested", except), except );
        }
    }

    public void prepare( Key key ) throws PersistenceException {
        ArrayList l;
        synchronized( _listeners ) {
            l = (ArrayList) _listeners.get( key );
        }
        Iterator i = (l != null? l.iterator(): nullItor);
        while ( i.hasNext() ) {
            ConnectorListener cl = (ConnectorListener) i.next();
            cl.connectionPrepare( key );
        }
    }

    public void commit( Key key ) throws PersistenceException {
        try {
            Connection conn = (Connection) _connections.get( key );
            conn.commit();
        } catch ( SQLException e ) {
            throw new PersistenceException( "SQLException during commit", e );
        }
    }

    public void rollback( Key key ) throws PersistenceException {
        try {
            Connection conn = (Connection) _connections.get( key );
            conn.rollback();
        } catch ( SQLException e ) {
            throw new PersistenceException( "SQLException during commit", e );
        }
    }

    public void close( Key key ) throws PersistenceException {
        try {
            Connection conn = (Connection) _connections.get( key );
            conn.close();
        } catch ( SQLException e ) {
            throw new PersistenceException( "SQLException during commit", e );
        }
    }

    public void release( Key key ) throws PersistenceException {
        ArrayList l;
        synchronized( _listeners ) {
            l = (ArrayList) _listeners.remove( key );
        }
        Iterator i = (l != null? l.iterator() : nullItor);
        while ( i.hasNext() ) {
            ConnectorListener cl = (ConnectorListener) i.next();
            cl.connectionRelease( key );
        }
        synchronized( _connections ) {
            _connections.remove( key );
        }
    }
    
    public Connection getConnection( Key key ) {
        synchronized( _connections ) {
            return (Connection) _connections.get( key );
        }
    }

    public void addListener( Key key, ConnectorListener listener ) {
        ArrayList l;
        synchronized( _listeners ) {
            l = (ArrayList) _listeners.get( key );
            if ( l == null ) {
                l = new ArrayList();
                _listeners.put( key, l );
            }
        }
        l.add( key );
    }

    public void removeListener( Key key, ConnectorListener listener ) {
        ArrayList l;
        synchronized( _listeners ) {
            l = (ArrayList) _listeners.get( key );
        }
        if ( l != null )
            l.remove( listener );
    }

    public static interface ConnectorListener {
        public void connectionPrepare( Key key )
            throws PersistenceException;

        public void connectionRelease( Key key )
            throws PersistenceException;
    }


    // ========
    private static Iterator nullItor = new Iterator() {
        public boolean hasNext() {
            return false;
        }
        public Object next() throws NoSuchElementException {
            throw new NoSuchElementException();
        }
        public void remove() throws IllegalStateException {
            throw new IllegalStateException();
        }
    };

}
