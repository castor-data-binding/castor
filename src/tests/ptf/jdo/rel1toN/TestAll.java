/*
 * Copyright 2005 Ralf Joachim
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
package ptf.jdo.rel1toN;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run all tests of the ptf.jdo.rel1toN package.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public class TestAll extends TestCase {
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("All ptf.jdo.rel1toN performance tests");

        suite.addTest(TestCreate.suite());
        suite.addTest(TestLoadBiNto1.suite());
        suite.addTest(TestLoadUniNto1.suite());
        suite.addTest(TestLoadBi1toN.suite());
        suite.addTest(TestLoadLazy1toN.suite());
        suite.addTest(TestLoadUni1toN.suite());
        suite.addTest(TestRemove.suite());

        return suite;
    }

    public TestAll(final String name) { super(name); }
}
