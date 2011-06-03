/*
 * Copyright 2008 Tobias Hochwallner, Ralf Joachim
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
package org.castor.cpa.test.test2996.onetoone;

import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.jdo.util.JDOConfFactory;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.mapping.MappingException;

/**
 * Tests one to one relation with mixed XML and class mapping. 
 */
public final class TestOneToOneMixedMapping2 extends AbstractTestOneToOne {
    private static final String DBNAME = "test2996-onetoone-mixed2";
    private static final String MAPPING_ADDRESS = 
        "/org/castor/cpa/test/test2996/onetoone/mapping-address.xml";
    private static final String MAPPING_EMPLOYEE = Employee.class.getName();
    
    public TestOneToOneMixedMapping2(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    @Override
    protected JDOManager getJDOManager() throws MappingException {
        org.castor.jdo.conf.Database dbConfig = getDbConfig(DBNAME);
        dbConfig.addMapping(JDOConfFactory.createXmlMapping(MAPPING_ADDRESS));
        dbConfig.addClassMapping(JDOConfFactory.createClassMapping(MAPPING_EMPLOYEE));
        return getJDOManager(dbConfig);
    }
}
