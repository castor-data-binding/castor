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
package org.castor.cpa.test.test81;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

/**
 * Master object for a CTF JUnit test that tests a depend relation. 
 * @author nstuart
 */
@Ignore
public final class MasterObject implements TimeStampable {
    private int _id;
    private String _descrip;
    private DependentObject _depends;
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

    public DependentObject getDepends() {
        return _depends;
    }

    public void setDepends(final DependentObject depends) {
        _depends = depends;
        if (depends != null) {
            depends.setMaster(this);
        }
    }

    public long jdoGetTimeStamp() {
        return _timestamp;
    }
    
    public void jdoSetTimeStamp(final long timestamp) {
        _timestamp = timestamp;
    }
}
