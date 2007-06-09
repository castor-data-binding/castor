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

import junit.framework.TestCase;

import org.exolab.castor.jdo.JDOManager;

/**
 *
 * @author nstuart
 */
public final class TestLoadDatabase extends TestCase {
    public static void main(final String[] args) {
        new TestLoadDatabase().testLoad();
    }
    
    /** Creates a new instance of TestLoadDatabase */
    public TestLoadDatabase() { }
    
    public void testLoad() {
        try {
            JDOManager.loadConfiguration(
                    getClass().getResource("jdo-conf.xml").toString());
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
