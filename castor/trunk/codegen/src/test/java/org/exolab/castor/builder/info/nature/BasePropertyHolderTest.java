package org.exolab.castor.builder.info.nature;

import junit.framework.TestCase;

/**
 * Tests the set/get capabilities of a PropertyHolder. Subclasses MUST override
 * the setUp method and set a PropertyHolder to _holder. The testcase needs a
 * "fresh" NatureExtendable to guarantee independence for each test.
 * 
 * @author Lukas Lang
 * @since 1.2.1
 * 
 */
public abstract class BasePropertyHolderTest extends TestCase {

    /**
     * Test PropertyHolder to test. Must be set in subclass.
     */
    protected PropertyHolder _holder;

    /**
     * Tests the holder's capability to hold properties.
     */
    public final void testSimpleReadWrite() {
        _holder.setProperty("tablename", "BOOK");
        assertEquals("BOOK", (String) _holder.getProperty("tablename"));
    }

    /**
     * Test verifies the postcondition of setTableName.
     */
    public final void testSetPropertyTwice() {
        _holder.setProperty("tablename", "BOOK");
        _holder.setProperty("tablename", "BOOKD");
        assertEquals("BOOKD", (String) _holder.getProperty("tablename"));
    }

    /**
     * Tests default behavior of properties not set before.
     */
    public final void testGetButNotSetBefore() {
        assertEquals(null, _holder.getProperty("tablename"));
    }
}