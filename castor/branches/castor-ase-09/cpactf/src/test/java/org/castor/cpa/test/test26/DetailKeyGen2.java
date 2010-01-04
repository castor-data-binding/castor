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
package org.castor.cpa.test.test26;

import org.junit.Ignore;

/**
 * Test object mapping to test_detail2 used to conduct relation tests.
 */
@Ignore
public final class DetailKeyGen2 {
    public static final int DEFAULT_ID = 5;
    public static final String DEFAULT_VALUE = "value";

    private int _id;
    private String _value;
    private DetailKeyGen _detail;

    public DetailKeyGen2(final int id) {
        _id = id;
        _value = DEFAULT_VALUE;
    }

    public DetailKeyGen2() {
        _value = DEFAULT_VALUE;
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setValue1(final String value) { _value = value; }
    public String getValue1() { return _value; }

    public void setDetail(final DetailKeyGen detail) { _detail = detail; }
    public DetailKeyGen getDetail() { return _detail; }

    public String toString() {
        return _id + " / " + _value + " / "
                + (_detail == null ? 0 : _detail.getId());
    }

    public int hashCode() { return _id; }

    public boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other != null && other instanceof DetailKeyGen2) {
            return (((DetailKeyGen2) other)._id == _id);
        }
        return false;
    }
}
