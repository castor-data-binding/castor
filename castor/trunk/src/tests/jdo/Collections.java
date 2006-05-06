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


package jdo;


import harness.CastorTestCase;
import harness.TestHarness;

import java.sql.Connection;
import java.sql.SQLException;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;


/**
 * Test for different collection types supported by Castor JDO.
 * This test creates data objects that each has a collection as
 * a field type.
 */
public class Collections extends CastorTestCase {

    private Database       _db;

    private Connection     _conn;

    private JDOCategory    _category;

    public Collections( TestHarness category ) {
        super( category, "TC70", "Collections" );
        _category = (JDOCategory) category;
    }

    public void setUp()
            throws PersistenceException, SQLException {
        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit( false );
    }

    public void runTest() 
            throws PersistenceException, SQLException, Exception {

        runOnce(TestColCollection.class);
        runOnce(TestColArrayList.class);
        runOnce(TestColVector.class);
        runOnce(TestColSet.class);
        runOnce(TestColMap.class);
        runOnce(TestColHashtable.class);
        runSortedSet(TestColSortedSet.class);
        runOnce(TestColIterator.class);
        runOnce(TestColEnumeration.class);

        // TODO[WG]: Causes problems with missing setXXX(ArrayList) method.
        // TODO[WG]: To re-enable, please consult with http://jira.codehaus.org/browse/CASTOR-1147
        // runOnce( TestColAdd.class );

        runArray();
    }

    public void runOnce( Class masterClass ) 
            throws PersistenceException, SQLException, Exception {

        stream.println( "Running..." );
        stream.println( "" );

        // delete everything
        _conn.createStatement().executeUpdate( "DELETE FROM test_col" );
        _conn.createStatement().executeUpdate( "DELETE FROM test_item" );
        _conn.createStatement().executeUpdate( "DELETE FROM test_comp_item" );
        _conn.commit();

        // create new TestCol object with elements
        _db.begin();
        TestCol testCol = (TestCol) masterClass.newInstance();
        testCol.setId( 1 );
        _db.create( testCol );
        for ( int i=0; i < 5; i++ ) {
            TestItem newItem = new TestItem( 100+i );
            testCol.addItem( newItem );
            _db.create( newItem );
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = (TestCol) _db.load( masterClass, new Integer(1) );
        if ( testCol == null )
            fail( "Object creation failed!" );
            
        if ( testCol.itemSize() != 5 ||
                !testCol.containsItem( new TestItem( 100 ) ) ||
                !testCol.containsItem( new TestItem( 101 ) ) ||
                !testCol.containsItem( new TestItem( 102 ) ) ||
                !testCol.containsItem( new TestItem( 103 ) ) ||
                !testCol.containsItem( new TestItem( 104 ) ) )
            fail( "Related objects creation failed!" );

        testCol.removeItem( new TestItem( 100 ) );
        testCol.removeItem( new TestItem( 103 ) );
        TestItem newItem = new TestItem( 106 );
        testCol.addItem( newItem );
        _db.create( newItem );
        newItem = new TestItem( 107 );
        testCol.addItem( newItem );
        _db.create( newItem );
        _db.commit();
        
        // test if add and remove work properly.
        _db.begin();
        testCol = (TestCol) _db.load( masterClass, new Integer(1) );
        if ( testCol == null )
            fail( "Object add/remove failed! " + testCol );

        if ( testCol.itemSize() != 5 ||
                !testCol.containsItem( new TestItem( 106 ) ) ||
                !testCol.containsItem( new TestItem( 101 ) ) ||
                !testCol.containsItem( new TestItem( 102 ) ) ||
                !testCol.containsItem( new TestItem( 107 ) ) ||
                !testCol.containsItem( new TestItem( 104 ) ) )
            fail( "Related add/remove failed!" + testCol );

        // test if add and remove rollback properly.
        testCol.removeItem( new TestItem( 102 ) );
        testCol.removeItem( new TestItem( 104 ) );
        newItem = new TestItem( 108 );
        testCol.addItem( newItem );
        newItem = new TestItem( 109 );
        testCol.addItem( newItem );
        _db.create( newItem );
        _db.rollback();

        if ( testCol.itemSize() != 5 ||
                !testCol.containsItem( new TestItem( 106 ) ) ||
                !testCol.containsItem( new TestItem( 101 ) ) ||
                !testCol.containsItem( new TestItem( 102 ) ) ||
                !testCol.containsItem( new TestItem( 107 ) ) ||
                !testCol.containsItem( new TestItem( 104 ) ) )
            fail( "Related add/remove rollback failed!" + testCol );

        // shoud test for update too
    } 

    public void runSortedSet( Class masterClass ) 
    throws PersistenceException, SQLException, Exception {
    	
    	stream.println( "Running..." );
    	stream.println( "" );
    	
//  	delete everything
    	_conn.createStatement().executeUpdate( "DELETE FROM test_col" );
    	_conn.createStatement().executeUpdate( "DELETE FROM test_item" );
    	_conn.createStatement().executeUpdate( "DELETE FROM test_comp_item" );
    	_conn.commit();
    	
//  	create new TestCol object with elements
    	_db.begin();
    	TestColSortedSet testCol = (TestColSortedSet) masterClass.newInstance();
    	testCol.setId( 1 );
    	_db.create( testCol );
    	for ( int i=0; i < 5; i++ ) {
    		TestComparableItem newItem = new TestComparableItem( 100+i );
    		testCol.addItem( newItem );
    		_db.create( newItem );
    	}
    	_db.commit();
    	
//  	test if object created properly
    	_db.begin();
    	testCol = (TestColSortedSet) _db.load( masterClass, new Integer(1) );
    	if ( testCol == null )
    		fail( "Object creation failed!" );
    	
    	int size = testCol.itemSize();
    	if ( size != 5 ||
    			!testCol.containsItem( new TestComparableItem( 100 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 101 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 102 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 103 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 104 ) ) )
    		fail( "Related objects creation failed!" );
    	
    	testCol.removeItem( new TestComparableItem( 100 ) );
    	testCol.removeItem( new TestComparableItem( 103 ) );
    	TestComparableItem newItem = new TestComparableItem( 106 );
    	testCol.addItem( newItem );
    	_db.create( newItem );
    	newItem = new TestComparableItem( 107 );
    	testCol.addItem( newItem );
    	_db.create( newItem );
    	_db.commit();
    	
//  	test if add and remove work properly.
    	_db.begin();
    	testCol = (TestColSortedSet) _db.load( masterClass, new Integer(1) );
    	if ( testCol == null )
    		fail( "Object add/remove failed! " + testCol );
    	
    	if ( testCol.itemSize() != 5 ||
    			!testCol.containsItem( new TestComparableItem( 106 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 101 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 102 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 107 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 104 ) ) )
    		fail( "Related add/remove failed!" + testCol );
    	
//  	test if add and remove rollback properly.
    	testCol.removeItem( new TestComparableItem( 102 ) );
    	testCol.removeItem( new TestComparableItem( 104 ) );
    	newItem = new TestComparableItem( 108 );
    	testCol.addItem( newItem );
    	newItem = new TestComparableItem( 109 );
    	testCol.addItem( newItem );
    	_db.create( newItem );
    	_db.rollback();
    	
    	if ( testCol.itemSize() != 5 ||
    			!testCol.containsItem( new TestComparableItem( 106 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 101 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 102 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 107 ) ) ||
    			!testCol.containsItem( new TestComparableItem( 104 ) ) )
    		fail( "Related add/remove rollback failed!" + testCol );
    	
//  	shoud test for update too
    } 

    /**
     * Special case for array
     */
    public void runArray() 
            throws PersistenceException, SQLException, Exception {

        stream.println( "Running..." );
        stream.println( "" );

        // delete everything
        _conn.createStatement().executeUpdate( "DELETE FROM test_col" );
        _conn.createStatement().executeUpdate( "DELETE FROM test_item" );
        _conn.commit();

        // create new TestCol object with elements
        _db.begin();
        TestColArray testCol = new TestColArray();
        testCol.setId( 1 );
        _db.create( testCol );
        TestItem[] items = new TestItem[5];
        for ( int i=0; i < 5; i++ ) {
            TestItem newItem = new TestItem( 100+i );
            newItem.setTestCol(testCol);
            items[i] = newItem;
            _db.create( newItem );
        }
        testCol.setItems(items);
        _db.commit();
        // test if object created properly
        _db.begin();
        testCol = (TestColArray) _db.load( TestColArray.class, new Integer(1) );
        if ( testCol == null )
            fail( "Object creation failed!" );
            
        if ( testCol.itemSize() != 5 ||
                !testCol.containsItem( new TestItem( 100 ) ) ||
                !testCol.containsItem( new TestItem( 101 ) ) ||
                !testCol.containsItem( new TestItem( 102 ) ) ||
                !testCol.containsItem( new TestItem( 103 ) ) ||
                !testCol.containsItem( new TestItem( 104 ) ) )
            fail( "Related objects creation failed!" );

        testCol.removeItem( new TestItem( 100 ) );
        testCol.removeItem( new TestItem( 103 ) );

        // update array
        TestItem[] oldItems = testCol.getItems();
        TestItem[] newItems = new TestItem[oldItems.length+2];
        System.arraycopy(oldItems, 0, newItems, 0, oldItems.length);

        TestItem newItem = new TestItem( 106 );
        newItem.setTestCol(testCol);
        newItems[newItems.length-2] = newItem;
        _db.create( newItem );

        newItem = new TestItem( 107 );
        newItem.setTestCol(testCol);
        newItems[newItems.length-1] = newItem;
        _db.create( newItem );

        testCol.setItems( newItems );


        _db.commit();
        
        // test if add and remove work properly.
        _db.begin();
        testCol = (TestColArray) _db.load( TestColArray.class, new Integer(1) );
        if ( testCol == null )
            fail( "Object add/remove failed! " + testCol );

        if ( testCol.itemSize() != 5 ||
                !testCol.containsItem( new TestItem( 106 ) ) ||
                !testCol.containsItem( new TestItem( 101 ) ) ||
                !testCol.containsItem( new TestItem( 102 ) ) ||
                !testCol.containsItem( new TestItem( 107 ) ) ||
                !testCol.containsItem( new TestItem( 104 ) ) )
            fail( "Related add/remove failed!" + testCol );

        // test if add and remove rollback properly.
        testCol.removeItem( new TestItem( 102 ) );
        testCol.removeItem( new TestItem( 104 ) );

        // update array
        oldItems = testCol.getItems();
        newItems = new TestItem[oldItems.length+2];
        System.arraycopy(oldItems, 0, newItems, 0, oldItems.length);

        newItem = new TestItem( 108 );
        newItem.setTestCol(testCol);
        newItems[newItems.length-2] = newItem;
        _db.create( newItem );

        newItem = new TestItem( 109 );
        newItem.setTestCol(testCol);
        newItems[newItems.length-1] = newItem;
        _db.create( newItem );

        testCol.setItems( newItems );

        _db.rollback();

        if ( testCol.itemSize() != 5 ||
                !testCol.containsItem( new TestItem( 106 ) ) ||
                !testCol.containsItem( new TestItem( 101 ) ) ||
                !testCol.containsItem( new TestItem( 102 ) ) ||
                !testCol.containsItem( new TestItem( 107 ) ) ||
                !testCol.containsItem( new TestItem( 104 ) ) )
            fail( "Related add/remove rollback failed!" + testCol );
        // shoud test for update too
    } 

    public void tearDown()
            throws PersistenceException, SQLException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
        _conn.close();
    }
}


