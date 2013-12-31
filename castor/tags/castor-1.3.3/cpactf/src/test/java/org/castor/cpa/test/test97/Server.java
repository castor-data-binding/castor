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
package org.castor.cpa.test.test97;

import org.junit.Ignore;

@Ignore
public final class Server extends Computer {
    private int _numberOfCPUs;
    private int _supportInYears;

    public int getNumberOfCPUs() { return _numberOfCPUs; }
    public void setNumberOfCPUs(final int numberOfCPUs) {
        _numberOfCPUs = numberOfCPUs;
    }

    public int getSupportInYears() { return _supportInYears; }
    public void setSupportInYears(final int supportInYears) {
        _supportInYears = supportInYears;
    }
}
