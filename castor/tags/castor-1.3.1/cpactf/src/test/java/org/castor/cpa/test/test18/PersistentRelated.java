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
package org.castor.cpa.test.test18;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public final class PersistentRelated implements TimeStampable, java.io.Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = -6470077375784314255L;

    public static final int       DEFAULT_ID = 5;

    private int              _id;
    private PersistentObject _persistent;
    
    private long             _timeStamp;

    public PersistentRelated() {
        _id = DEFAULT_ID;
    }

    public PersistentRelated(final int id) {
        _id = id;
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setPersistent(final PersistentObject persistent) {
        _persistent = persistent;
    }

    public PersistentObject getPersistent() {
        return _persistent;
    }

    public long jdoGetTimeStamp() {
        return _timeStamp;
    }

    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }

    public String toString() {
        return _id + " / " + (_persistent == null ? 0 : _persistent.getId());
    }
}
