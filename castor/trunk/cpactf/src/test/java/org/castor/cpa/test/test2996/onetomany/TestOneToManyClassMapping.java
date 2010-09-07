/*
 * Copyright 2008 Lukas Lang, Ralf Joachim
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
package org.castor.cpa.test.test2996.onetomany;

import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.jdo.util.JDOConfFactory;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.mapping.MappingException;

/**
 * Tests one to one relation with class mapping. 
 */
public class TestOneToManyClassMapping extends AbstractTestOneToMany {
    private static final String DBNAME = "test2996-onetomany-class";
    private static final String MAPPING_HOUSE = House.class.getName();
    private static final String MAPPING_FLAT = Flat.class.getName();
    
    public TestOneToManyClassMapping(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL);
    }

    @Override
    protected JDOManager getJDOManager() throws MappingException {
        org.castor.jdo.conf.Database dbConfig = getDbConfig(DBNAME);
        dbConfig.addClassMapping(JDOConfFactory.createClassMapping(MAPPING_HOUSE));
        dbConfig.addClassMapping(JDOConfFactory.createClassMapping(MAPPING_FLAT));
        return getJDOManager(dbConfig);
    }
}
