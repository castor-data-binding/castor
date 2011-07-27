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
 * Test for generic key generators (MAX and HIGH-LOW).
 */
public class TestKeyGenHighLow extends AbstractTestKeyGenInteger {
    public TestKeyGenHighLow(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.ORACLE);
    }

    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SQL_SERVER);
    }

    public void testKeyGenHighLow() throws Exception {
        testOneKeyGen(HighLowObject.class, HighLowExtends.class);
        testOneKeyGen(HighLowObjectSameConnection.class, HighLowExtendsSameConnection.class);
        testOneKeyGen(HighLowObjectGlobal.class, HighLowExtendsGlobal.class);
    }
}
