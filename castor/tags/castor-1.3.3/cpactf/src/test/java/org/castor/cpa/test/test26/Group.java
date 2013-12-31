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
 * Test object mapping to test_group used to conduct relation the tests.
 */
@Ignore
public final class Group {
    public static final int DEFAULT_ID = 4;
    public static final String DEFAULT_VALUE = "group";

    private int _id;
    private String _value;

    public Group() {
        _id = DEFAULT_ID;
        _value = DEFAULT_VALUE;
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setValue1(final String value) { _value = value; }
    public String getValue1() { return _value; }

    public String toString() {
        return _id + " / " + _value;
    }
}
