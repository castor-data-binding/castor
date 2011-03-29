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

import java.util.ArrayList;
import java.util.Date;

import org.junit.Ignore;

@Ignore
public class PrimaryKeysPerson {
    private String _firstname;
    private String _lastname;
    private Date _birth;
    private ArrayList<PrimaryKeysAddress> _addresses;
    
    public PrimaryKeysPerson() { }

    public PrimaryKeysPerson(final String firstname, final String lastname, final Date birth) {
        _firstname = firstname;
        _lastname = lastname;
        _birth = birth;
    }

    public final String getFirstName() { return _firstname; }
    public final void setFirstName(final String firstname) { _firstname = firstname; }
    
    public final String getLastName() { return _lastname; }
    public final void setLastName(final String lastname) { _lastname = lastname; }
    
    public final Date getBirthday() { return _birth; }
    public final void setBirthday(final Date birth) { _birth = birth; }
    
    public final ArrayList<PrimaryKeysAddress> getAddress() { return _addresses; }
    public final void setAddress(final ArrayList<PrimaryKeysAddress> addresses) {
        _addresses = addresses;
    }
}
