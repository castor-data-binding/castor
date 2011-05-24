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
package org.castor.cpa.test.test98;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;

@Ignore
class TreadedContainerLoader implements Runnable {

    private static final Log LOG = LogFactory
            .getLog(TreadedContainerLoader.class);
    private static final int REPS = 100;
    
    public void run() {
        int count = 0;
        try {
            for (int i = 0; i < REPS; i++) {
                count = i;
                TestPolymorphismInAThreadedEnv.loadContainer();
            }
            LOG.debug("Second thread successfully loaded " + (count + 1)
                    + " Containers");
        } catch (Exception ex) {
            LOG.error("Exception on second thread loading Container on "
                    + (count + 1) + "th try", ex);
        }
    }
}
