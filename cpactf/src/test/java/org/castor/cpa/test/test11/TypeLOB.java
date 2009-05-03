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
package org.castor.cpa.test.test11;

import java.sql.Clob;

import org.junit.Ignore;

@Ignore
public final class TypeLOB {
    public static final int DEFAULT_ID = 3;

    private long _id;
    private byte[] _blob;
    private String _clob;
    private Clob _clob2;

    public void setId(final long id) { _id = id; }
    public long getId() { return _id; }

    public void setBlob(final byte[] blob) { _blob = blob; }
    public byte[] getBlob() { return _blob; }

    public void setClob(final String clob) { _clob = clob; }
    public String getClob() { return _clob; }

    public void setClob2(final Clob clob2) { _clob2 = clob2; }
    public Clob getClob2() { return _clob2; }

    public String toString() {
        return "" + _id;
    }
}
