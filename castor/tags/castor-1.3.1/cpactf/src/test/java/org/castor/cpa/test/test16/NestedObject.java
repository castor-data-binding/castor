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
package org.castor.cpa.test.test16;

import org.junit.Ignore;

@Ignore
public final class NestedObject {
    public static final int DEFAULT_ID = 3;
    public static final String DEFAULT_VALUE1 = "one";
    public static final String DEFAULT_VALUE2 = "two";

    private int _id;
    private Nested1 _nested1;
    private Nested2 _nested2;

    public NestedObject() {
        _id = DEFAULT_ID;
        _nested1 = new Nested1(DEFAULT_VALUE1);
        _nested2 = new Nested2(new Nested3(DEFAULT_VALUE2));
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setNested1(final Nested1 nested1) { _nested1 = nested1; }
    public Nested1 getNested1() { return _nested1; }

    public void setNested2(final Nested2 nested2) { _nested2 = nested2; }
    public Nested2 getNested2() { return _nested2; }

    public String toString() {
        return _id + " / " + _nested1 + " / " + _nested2;
    }

    @Ignore
    public static final class Nested1 {
        private String _value1;

        public Nested1() { }
        public Nested1(final String value1) { _value1 = value1; }

        public void setValue1(final String value1) { _value1 = value1; }
        public String getValue1() { return _value1; }

        public String toString() { return _value1; }
    }   
     
    @Ignore
    public static final class Nested2 {
        private Nested3 _nested3;
        
        public Nested2() { }
        public Nested2(final Nested3 nested3) { _nested3  = nested3; }

        public void setNested3(final Nested3 nested3) { _nested3 = nested3; }
        public Nested3 getNested3() { return _nested3; }

        public String toString() { return _nested3.toString(); }
    }    

    @Ignore
    public static final class Nested3 {
        private String _value2;
        
        public Nested3() { }
        public Nested3(final String value2) { _value2 = value2; }

        public void setValue2(final String value2) { _value2 = value2; }
        public String getValue2() { return _value2; }

        public String toString() { return _value2; }
    }    
}
