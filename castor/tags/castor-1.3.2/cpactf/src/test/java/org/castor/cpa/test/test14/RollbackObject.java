/*
 * Copyright 2009 Ralf Joachim
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
package org.castor.cpa.test.test14;

import org.junit.Ignore;

/**
 * Test object for testing rollback of primitive properties.
 */
@Ignore
public final class RollbackObject {
    public static final long      DEFAULT_ID = 3;
    public static final String    DEFAULT_VALUE1 = "one";
    public static final String    DEFAULT_VALUE2 = "two";
    public static final long      DEFAULT_NUMBER = 5;

    private long   _id;
    private String _value1;
    private String _value2;
    private long   _number;

    public RollbackObject() {
        _id = DEFAULT_ID;
        _value1 = DEFAULT_VALUE1;
        _value2 = DEFAULT_VALUE2;
        _number = DEFAULT_NUMBER;
    }

    public void setId(final long id) { _id = id; }
    public long getId() { return _id; }

    public void setValue1(final String value1) { _value1 = value1; }
    public String getValue1() { return _value1; }

    public void setValue2(final String value2) { _value2 = value2; }
    public String getValue2() { return _value2; }

    public void setNumber(final long number) { _number = number; }
    public long getNumber() { return _number; }

    public String toString() {
        return _id + " / " + _value1 + " / " + _value2 + " / " + _number;
    }
}
