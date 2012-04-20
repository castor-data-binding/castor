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
package org.castor.cpa.test.test38;

import org.junit.Ignore;

@Ignore
public final class ExtendsEntity extends Entity {
    public static final int DEFAULT_ID = 4;
    public static final String DEFAULT_VALUE_3 = "three";
    public static final String DEFAULT_VALUE_4 = "four";

    private String _value3;
    private String _value4;

    public ExtendsEntity() {
        super();
        setId(DEFAULT_ID);
        _value3 = DEFAULT_VALUE_3;
        _value4 = DEFAULT_VALUE_4;
    }

    public void setValue3(final String value3) { _value3 = value3; }
    public String getValue3() { return _value3; }

    public void setValue4(final String value4) { _value4 = value4; }
    public String getValue4() { return _value4; }

    public String toString() {
        return getId() + " / " + getValue1() + " / " 
                + getValue2() + "/" + getValue3() + "/" + getValue4();
    }
}
