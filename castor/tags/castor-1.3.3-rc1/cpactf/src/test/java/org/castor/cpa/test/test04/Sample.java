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
package org.castor.cpa.test.test04;

import org.junit.Ignore;

@Ignore
public final class Sample {
    public static final int       DEFAULT_ID = 3;

    public static final String    DEFAULT_VALUE_1 = "one";

    public static final String    DEFAULT_VALUE_2 = "two";

    private int    _id;

    private String _value1;

    private String _value2;

    public Sample() {
        _id = DEFAULT_ID;
        _value1 = DEFAULT_VALUE_1;
        _value2 = DEFAULT_VALUE_2;
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setValue1(final String value1) { _value1 = value1; }
    public String getValue1() { return _value1; }

    public void setValue2(final String value2) { _value2 = value2; }
    public String getValue2() { return _value2; }

    public String toString() {
        return _id + " / " + _value1 + " / " + _value2;
    }
}
