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
package org.castor.cpa.test.test13;

import org.junit.Ignore;

/**
 * Test object refering to a serializable object.
 */
@Ignore
public final class SerializableReferer {
    private int _id;
    private SerializableObject _tso;

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setSerializableObject(final SerializableObject tso) { _tso = tso; }
    public SerializableObject getSerializableObject() { return _tso; }

    public String toString() {
        return getClass().getName() + ":" + _id + ":" + _tso;
    }
    
    public int hashCode() {
        return (_tso == null) ? _id : _id + _tso.hashCode(); 
    }

    public boolean equals(final Object object) {
        if (object == null) { return false; }
        if (object == this) { return true; }
        if (!(object instanceof SerializableReferer)) { return false; }

        SerializableReferer obj = (SerializableReferer) object;

        return (_id == obj._id)
            && ((_tso == obj._tso) || ((_tso != null) && _tso.equals(obj._tso)));
    }
}
