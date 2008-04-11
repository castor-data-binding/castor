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
package ctf.jdo.tc8x;

import org.exolab.castor.jdo.TimeStampable;

/**
 * Dependent object for test case in the ctf.jdo.tc8x package
 * @author nstuart
 */
public final class DependentObject implements TimeStampable {
    private int _id;
    private String _descrip;
    private MasterObject _master;
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

    public MasterObject getMaster() {
        return _master;
    }

    public void setMaster(final MasterObject master) {
        _master = master;
    }

    public long jdoGetTimeStamp() {
        return _timestamp;
    }
    
    public void jdoSetTimeStamp(final long timestamp) {
        _timestamp = timestamp;
    }
}
