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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import harness.TestHarness;
import harness.CastorTestCase;

import java.sql.SQLException;

public class TestTransientAttribute extends CastorTestCase {

    private static final Log _log = LogFactory.getLog (TestTransientAttribute.class);
    
    private JDOCategory    _category;

    private Database       _db;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestTransientAttribute( TestHarness category ) 
    {
        super( category, "tempTC84a", "Transient attribute" );
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp() 
        throws PersistenceException, SQLException
    {
        _db = _category.getDatabase();
    }

    public void tearDown() throws PersistenceException 
    {
        if ( _db.isActive() ) _db.rollback();
        if ( _db.isActive()) _db.close();
    }
    
    public void runTest() throws Exception 
    {
        _db.begin();
        
        TransientMaster entity = (TransientMaster) _db.load(TransientMaster.class, new Integer (1));
        assertNotNull(entity);
        assertEquals (new Integer(1), entity.getId());
        assertEquals ("entity1", entity.getName());
        _log.debug ("loadedEntity.getProperty1() = " + entity.getProperty1());
        assertNull (entity.getProperty1());
        _log.debug ("loadedEntity.getProperty2() = " + entity.getProperty1());
        assertEquals (new Integer (2), entity.getProperty2());
        assertNull (entity.getProperty3());
        assertNull (entity.getEntityTwo());
        assertNull (entity.getEntityThrees());
        
        _db.rollback();
    
        _db.begin();
        
        OQLQuery query = _db.getOQLQuery("SELECT entity FROM jdo.TransientMaster entity WHERE id = $1");
        query.bind(new Integer(1));
        QueryResults results = query.execute();
        
        entity = (TransientMaster) results.next();

        assertNotNull(entity);
        assertEquals (new Integer(1), entity.getId());
        assertEquals ("entity1", entity.getName());
        _log.debug ("loadedEntity.getProperty1() = " + entity.getProperty1());
        assertNull (entity.getProperty1());
        _log.debug ("loadedEntity.getProperty2() = " + entity.getProperty1());
        assertEquals (new Integer (2), entity.getProperty2());
        assertNull (entity.getProperty3());
        assertNull (entity.getEntityTwo());
        assertNull (entity.getEntityThrees());
        
        _db.rollback();

        _db.begin();

        TransientChildOne entityTwo = new TransientChildOne();
        entityTwo.setId(new Integer (200));
        entityTwo.setDescription("entity200");
        
        entity = new TransientMaster();
        entity.setId(new Integer (100));
        entity.setName("entity100");
        entity.setProperty1(new Integer (100));
        entity.setProperty2(new Integer (200));
        entity.setProperty3(new Integer (300));
        entity.setEntityTwo(entityTwo);
        _db.create(entity);
        _db.commit();

        _db.begin();
        TransientMaster loadedEntity = 
            (TransientMaster) _db.load(TransientMaster.class, new Integer (100));
        assertNotNull(loadedEntity);
        assertEquals (new Integer(100), loadedEntity.getId());
        assertEquals ("entity100", loadedEntity.getName());
        
        _log.debug ("loadedEntity.getProperty() = " + loadedEntity.getProperty1());
        
        assertNull (loadedEntity.getProperty1());
        _log.debug ("loadedEntity.getProperty2() = " + loadedEntity.getProperty2());
        assertEquals (new Integer (200), loadedEntity.getProperty2());
        _log.debug ("loadedEntity.getProperty3() = " + loadedEntity.getProperty3());
        assertNull (loadedEntity.getProperty3());
        assertNull (loadedEntity.getEntityTwo());
        assertNull (loadedEntity.getEntityThrees());
        
        _db.commit();

        _db.begin();
        
        entity = (TransientMaster) _db.load(TransientMaster.class, new Integer (1));
        assertNotNull(entity);
        assertEquals (new Integer(1), entity.getId());
        assertEquals ("entity1", entity.getName());
        assertNull (entity.getProperty1());
        assertEquals (new Integer (2), entity.getProperty2());
        assertNull (entity.getEntityTwo());
        assertNull (entity.getEntityThrees());
        
        entity.setProperty2(new Integer (-2));
        
        _db.commit();

        _db.begin();
        
        entity = (TransientMaster) _db.load(TransientMaster.class, new Integer (1));
        assertNotNull(entity);
        assertEquals (new Integer(1), entity.getId());
        assertEquals ("entity1", entity.getName());
        assertNull (entity.getProperty1());
        assertEquals (new Integer (-2), entity.getProperty2());
        assertNull (entity.getProperty3());
        assertNull (entity.getEntityTwo());
        assertNull (entity.getEntityThrees());
        
        entity.setProperty2(new Integer (2));
        
        _db.commit();
    }

}
