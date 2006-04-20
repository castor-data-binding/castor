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
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package ctf.jdo.tc8x;

import harness.TestHarness;
import harness.CastorTestCase;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.persist.spi.Complex;


/**
 * JUnit test to test drive specification of class identity through the use of the
 * identity attribute on the <field> mapping.
 */
public final class TestIdentityPerFieldMapping extends CastorTestCase {
    private JDOCategory _category;

    /**
     * Creates an instance of this CTF test.
     * @param category The test suite of these tests
     */
    public TestIdentityPerFieldMapping(final TestHarness category) {
        super(category, "TC83",
                "Identity definition through identity attribute in field mapping");
        _category = (JDOCategory) category;
    }

    /**
     * @inheritDoc
     * @see junit.framework.TestCase#runTest()
     */
    public void runTest() throws Exception {
        testQueryEntityOne();
        testLoadChild();
        testLoadEntityWithCompoundId();
        testLoadChildWithCompoundId();
    }

    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testQueryEntityOne() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        
        Parent entity = (Parent) db.load(Parent.class, new Integer(1));

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        
        db.commit();
        db.close();
    }
    
    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testLoadChild() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        
        Child child = (Child) db.load(Child.class, new Integer(1));

        assertNotNull(child);
        assertEquals(new Integer(1), child.getId());
        
        db.commit();
        db.close();
    }

    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testLoadEntityWithCompoundId() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        
        ParentWithCompoundId child = (ParentWithCompoundId) 
            db.load(ParentWithCompoundId.class,
                    new Complex(new Integer(1), new Integer(1)));

        assertNotNull(child);
        assertEquals(new Integer(1), child.getId1());
        assertEquals(new Integer(1), child.getId2());
        
        db.commit();
        db.close();
    }

    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testLoadChildWithCompoundId() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        
        ChildWithCompoundId child = (ChildWithCompoundId) 
            db.load(ChildWithCompoundId.class,
                    new Complex(new Integer(1), new Integer(1)));

        assertNotNull(child);
        assertEquals(new Integer(1), child.getId1());
        assertEquals(new Integer(1), child.getId2());
        
        db.commit();
        db.close();
    }

}
