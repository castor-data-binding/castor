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
package org.castor.cpa.test.test15;

import org.junit.Ignore;

@Ignore
public class PrimaryKeysOnly {
    private String _firstname;
    private String _lastname;

    public PrimaryKeysOnly() { }

    public final String getFirstName() { return _firstname; }
    public final void setFirstName(final String firstname) { _firstname = firstname; }

    public final String getLastName() { return _lastname; }
    public final void setLastName(final String lastname) { _lastname = lastname; }
}
