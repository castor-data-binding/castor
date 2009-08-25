/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
 */
package org.castor.cpa.test.test20;

import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;

/**
 * Test for SEQUENCE key generator without trigger and not returning.
 */
public final class TestKeyGenSequence extends AbstractTestKeyGenInteger {
    public TestKeyGenSequence(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    // DERBY and MYSQL do not support sequence key generator
    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL);
    }

    public void testKeyGenSequence() throws Exception {
        testOneKeyGen(SequenceObject.class, SequenceExtends.class);
    }
}
