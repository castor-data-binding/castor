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
package org.castor.cpa.test.test10;

import java.util.Date;

import org.junit.Ignore;

/**
 * Test type handling against test_types.
 */
@Ignore
public final class TypeHandling {
    public static final int DEFAULT_ID = 3;

    private long _id;
    private int _intValue;
    private boolean _intExists = false;
    private boolean _boolIsMethod;
    private Long _longValue;
    private char _charValue;
    private boolean _boolValue;
    private float _floatValue;
    private double _doubleValue;
    private Date _date2;
    private Date _time2;
    private Date _timestamp2;

    public TypeHandling() {
        long time = System.currentTimeMillis();
        // ignore milliseconds. Comment the following line out to see SAP DB bug :-(
        time = time / 1000 * 1000;
        Date date = new Date(time);
        
        _id = DEFAULT_ID;
        _charValue = 'A';
        _date2 = date;
        _time2 = date;
        _timestamp2 = date;
    }

    public void setId(final long id) { _id = id; }
    public long getId() { return _id; }

    public void setDoubleValue(final double doubleValue) { _doubleValue = doubleValue; }
    public double getDoubleValue() { return _doubleValue; }

    public void setFloatValue(final float floatValue) { _floatValue = floatValue; }
    public float getFloatValue() { return _floatValue; }

    public void setIntValue(final int value) {
        _intValue = value;
        _intExists = true;
    }
    public int getIntValue() { return _intValue; }
    public boolean hasIntValue() { return _intExists; }
    public void deleteIntValue() { _intExists = false; }

    public void setLongValue(final Long value) { _longValue = value; }
    public Long getLongValue() { return _longValue; }

    public void setCharValue(final char value) { _charValue = value; }
    public char getCharValue() { return _charValue; }

    public void setBoolValue(final boolean value) { _boolValue = value; }
    public boolean getBoolValue() { return _boolValue; }
    
    public void setBoolIsMethod(final boolean boolIsMethod) {
        _boolIsMethod = boolIsMethod;
    }
    public boolean isBoolIsMethod() { return _boolIsMethod; }

    public void setDate2(final Date date) { _date2 = date; }
    public Date getDate2() { return _date2; }

    public void setTime2(final Date date) { _time2 = date; }
    public Date getTime2() { return _time2; }

    public void setTimestamp2(final Date date) { _timestamp2 = date; }
    public Date getTimestamp2() { return _timestamp2; }

    public String toString() {
        return "" + _id;
    }
}
