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
 */
package org.castor.cpa.functional.onetomany;


import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.mapping.MappingException;

/**
 * 
 * @author Lukas Lang
 * 
 */
public class OneToManyWithMappingTest extends BaseOneToManyTest{

    /**
     * Castor Config location.
     */
    public static final String JDO_CONF_FILE = "jdo-conf-mapping.xml";
    /**
     * Database name.
     */
    public String DATABASE_NAME = "OneToManyWithMapping";

    /**
     * SetUp loads JDOManager configuration.
     */
    public void setUpJDO() {
        String config = getClass().getResource(JDO_CONF_FILE).getFile();
        try {
            JDOManager.loadConfiguration(config, getClass().getClassLoader(), null);
            JDOManager.loadConfiguration(config);
            _jdo = JDOManager.createInstance(DATABASE_NAME);
        } catch (MappingException e) {
            e.printStackTrace();
            fail("Instantiation Failed: " + e.getMessage());
        }
    }

}
