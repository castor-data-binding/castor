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
package org.exolab.castor.builder.info.nature;

import junit.framework.TestCase;

/**
 * Test the implementation of {@link NatureExtendable}. Subclasses MUST override the
 * setUp method and set a {@link NatureExtendable} to _extender. The test case needs a
 * "fresh" {@link NatureExtendable} to guarantee independence for each test.
 * 
 * @author Lukas Lang
 * @since 1.2.1
 */
public abstract class BaseNatureExtendableTest extends TestCase {

    /**
     * The extender to test. Must be set in subclass.
     */
    protected NatureExtendable _extender;

    /**
     * Tests addNature.
     */
    public final void testAddNature() {
        _extender.addNature(JDOClassNature.class.getName());
        assertTrue(_extender.hasNature(JDOClassNature.class.getName()));
    }

    /**
     * Tests a double invoke of addNature.
     */
    public final void testAddNatureTwice() {
        _extender.addNature(JDOClassNature.class.getName());
        _extender.addNature(JDOClassNature.class.getName());
        assertTrue(_extender.hasNature(JDOClassNature.class.getName()));
    }

    /**
     * Tests hasNature without added before.
     */
    public final void testhasNatureNotAddedBefore() {
        assertFalse(_extender.hasNature(JDOClassNature.class.getName()));
    }

}