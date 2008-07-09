/*
 * Copyright 2008 Lukas Lang
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
 *
 */
package org.castor.core.nature;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;

/**
 * Runs various tests on the {@link BaseNature}. Due to visibility restrictions, an
 * inner class extending the {@link BaseNature} is defined.
 * 
 * @author Lukas Lang
 * @since 1.2.1
 * 
 */
public final class BaseNatureTest extends TestCase {

    /**
     * Internal test Nature.
     * 
     * @author Lukas Lang
     * 
     */
    class BaseNatureWrapper extends BaseNature {

        /**
         * Constructor taking a PropertyHolder
         * 
         * @param holder
         *            to access.
         */
        public BaseNatureWrapper(final PropertyHolder holder) {
            super(holder);
        }

        /**
         * Returns the fully qualified name of the BaseNatureWrapper.
         */
        public String getId() {
            return getClass().getName();
        }
    }

    /**
     * Second internal test Nature.
     * 
     * @author Lukas Lang
     * 
     */
    class SecondBaseNatureWrapper extends BaseNature {

        /**
         * Constructor taking a PropertyHolder
         * 
         * @param holder
         *            to access.
         */
        public SecondBaseNatureWrapper(final PropertyHolder holder) {
            super(holder);
        }

        /**
         * Returns the fully qualified name of the BaseNatureWrapper.
         */
        public String getId() {
            return getClass().getName();
        }
    }

    /**
     * Set properties via two Natures on the same {@link ClassInfo} using the same Key.
     * Values must not overwrite each other.
     */
    public void testUsageOfTwoNaturesUsingSameKey() {
        PropertyHolderTest ci = new PropertyHolderTest();
        ci.addNature(BaseNatureWrapper.class.getName());
        ci.addNature(SecondBaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ci);
        SecondBaseNatureWrapper sbnw = new SecondBaseNatureWrapper(ci);
        bnw.setProperty("key", "a");
        sbnw.setProperty("key", "b");
        assertEquals("a", bnw.getProperty("key"));
        assertEquals("b", sbnw.getProperty("key"));
    }

    /**
     * Error Case. Should throw a NullPointerException if null param in
     * constructor is given.
     */
    public void testNullAsParam() {
        try {
            BaseNatureWrapper bnw = new BaseNatureWrapper(null);
            assertNull(bnw);
            fail("NullPointerException should be thrown!");
        } catch (NullPointerException npe) {
            // do nothing
        }
    }

    /**
     * Tests correct implementation of the getId method.
     */
    public void testId() {
        PropertyHolderTest ci = new PropertyHolderTest();
        ci.addNature(BaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ci);
        assertEquals(
                "org.castor.core.nature.BaseNatureTest$BaseNatureWrapper",
                bnw.getId());
        assertEquals(bnw.getId(), bnw.getClass().getName());
    }

    /**
     * Retrieve a property if it was not set before.
     */
    public void testGetPropertyIfNotSet() {
        PropertyHolderTest ci = new PropertyHolderTest();
        ci.addNature(BaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ci);
        assertNull(bnw.getProperty("keynotset"));
    }

    /**
     * Retrieve a regularly set property.
     */
    public void testGetSetProperty() {
        PropertyHolderTest ci = new PropertyHolderTest();
        ci.addNature(BaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ci);
        bnw.setProperty("testproperty", "testvalue");
        assertEquals("testvalue", bnw.getProperty("testproperty"));
    }

    /**
     * Set a property twice. The last set value is actual.
     */
    public void testSetPropertyTwice() {
        PropertyHolderTest ci = new PropertyHolderTest();
        ci.addNature(BaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ci);
        bnw.setProperty("testproperty", "testvalue");
        bnw.setProperty("testproperty", "testvalueNew");
        assertEquals("testvalueNew", bnw.getProperty("testproperty"));
    }

    /**
     * Set null as value.
     */
    public void testSetNullAsValue() {
        PropertyHolderTest ci = new PropertyHolderTest();
        ci.addNature(BaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ci);
        bnw.setProperty("testproperty", null);
        assertEquals(null, bnw.getProperty("testproperty"));
    }

    /**
     * Null as a key must not be set.
     */
    public void testSetNullAsKey() {
        PropertyHolderTest ci = new PropertyHolderTest();
        ci.addNature(BaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ci);
        bnw.setProperty(null, "testvalue");
        assertEquals(null, bnw.getProperty(null));
    }

    /**
     * Test use of empty String as key. Usage is allowed.
     */
    public void testSetEmptyStringAsKey() {
        PropertyHolderTest ci = new PropertyHolderTest();
        ci.addNature(BaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ci);
        bnw.setProperty("", "testvalue");
        assertEquals("testvalue", bnw.getProperty(""));
    }

    /**
     * Checks if the health check works.
     */
    public void testInstantiationNatureNotSet() {
        PropertyHolderTest ci = new PropertyHolderTest();
        try {
            BaseNatureWrapper bnw = new BaseNatureWrapper(ci);
            assertNull(bnw);
            fail("Health check does not work!");
        } catch (IllegalStateException ise) {
            // do nothing
        }
    }

    /**
     * Checks, if default false strategy for properties not set is well
     * implemented.
     */
    public void testGetBooleanPropertyDefaultFalse() {
        PropertyHolderTest ci = new PropertyHolderTest();
        ci.addNature(BaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ci);
        assertEquals(false, bnw.getBooleanPropertyDefaultFalse("notsetbefore"));
    }

    /**
     * Checks, if default false strategy for properties not set is well
     * implemented. Property was set before.
     */
    public void testGetBooleanPropertyDefaultFalseButSet() {
        PropertyHolderTest ci = new PropertyHolderTest();
        ci.addNature(BaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ci);
        bnw.setProperty("setbefore", new Boolean(true));
        assertEquals(true, bnw.getBooleanPropertyDefaultFalse("setbefore"));
    }
    
    /**
     * Checks, whether a new List is instantiated if property was not set before.
     */
    public void testGetPropertyAsListPropertyNotSet() {
        PropertyHolderTest ph = new PropertyHolderTest();
        ph.addNature(BaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ph);
        List notset = bnw.getPropertyAsList("notsetbefore");
        assertNotNull(notset);
        assertEquals(0, notset.size());
    }
    
    /**
     * Checks, whether a new List is instantiated if property was not set
     * before.
     */
    public void testGetPropertyAsListPropertySet() {
        PropertyHolderTest ph = new PropertyHolderTest();
        ph.addNature(BaseNatureWrapper.class.getName());
        BaseNatureWrapper bnw = new BaseNatureWrapper(ph);
        ArrayList list = new ArrayList();
        list.add("entry");
        bnw.setProperty("setbefore", list);
        List setbefore = bnw.getPropertyAsList("setbefore");
        assertNotNull(setbefore);
        assertEquals(1, setbefore.size());
        assertEquals("entry", setbefore.get(0));
    }    

}
