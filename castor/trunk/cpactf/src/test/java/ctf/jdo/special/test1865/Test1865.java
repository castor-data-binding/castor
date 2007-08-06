/*
 * Copyright 2005 Nick Stuart
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
package ctf.jdo.special.test1865;

import java.util.Properties;

import junit.framework.TestCase;

import org.castor.core.util.Configuration;
import org.castor.cpa.CPAConfiguration;
import org.exolab.castor.jdo.JDOManager;

public final class Test1865 extends TestCase {
    private Object _memInitFlag;
    
    public static void main(final String[] args) throws Exception {
        Test1865 test = new Test1865();
        test.setUp();
        test.testLoad();
        test.tearDown();
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        
        Configuration cfg = CPAConfiguration.getInstance();
        _memInitFlag = cfg.getObject(CPAConfiguration.INITIALIZE_AT_LOAD);
        cfg.put(CPAConfiguration.INITIALIZE_AT_LOAD, Boolean.toString(false));
    }
    
    protected void tearDown() throws Exception {
        Configuration cfg = CPAConfiguration.getInstance();
        if (_memInitFlag != null) {
            cfg.put(CPAConfiguration.INITIALIZE_AT_LOAD, _memInitFlag);
        } else {
            cfg.remove(CPAConfiguration.INITIALIZE_AT_LOAD);
        }

        super.tearDown();
    }
    
    public void testLoad() {
        try {
            String config = getClass().getResource("jdo-conf.xml").toString();
            JDOManager.loadConfiguration(config);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        try {
            JDOManager.createInstance("database");
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
