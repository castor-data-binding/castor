/*
 * Copyright 2005 Werner Guttmann
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
package org.castor.cpa.test.test82;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

/**
 * Dependent object for a CTF JUnit test that tests a depend relation.
 */
@Ignore
public final class DependentObjectNoKeyGen implements TimeStampable {
    private int _id;
    private String _descrip;
    private MasterObjectNoKeyGen _master;
    private long _timestamp;

    public int getId() {
        return _id;
    }

    public void setId(final int id) {
        _id = id;
    }

    public String getDescrip() {
        return _descrip;
    }

    public void setDescrip(final String descrip) {
        _descrip = descrip;
    }

    public MasterObjectNoKeyGen getMaster() {
        return _master;
    }

    public void setMaster(final MasterObjectNoKeyGen master) {
        _master = master;
    }

    public long jdoGetTimeStamp() {
        return _timestamp;
    }

    public void jdoSetTimeStamp(final long timestamp) {
        _timestamp = timestamp;
    }
}
