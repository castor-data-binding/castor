/*
 * Copyright 2008 Udai Gupta
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
package org.castor.cpa.test.test88;

import java.util.Collection;
import java.util.Date;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public class LazyPerson implements TimeStampable {
    private String _fname;
    private String _lname;
    private Date   _bday;
    private Collection<LazyAddress> _address;
    private long   _timeStamp;

    public void setFirstName(final String fname) { _fname = fname; }
    public String getFirstName() { return _fname; }

    public void setLastName(final String lname) { _lname = lname; }
    public String getLastName() { return _lname; }

    public void setBirthday(final Date date) { _bday = date; }
    public Date getBirthday() { return _bday; }

    public void setAddress(final Collection<LazyAddress> address) { _address = address; }
    public Collection<LazyAddress> getAddress() { return _address; }

    public void jdoSetTimeStamp(final long l) { _timeStamp = l; }
    public long jdoGetTimeStamp() { return _timeStamp; }
}
