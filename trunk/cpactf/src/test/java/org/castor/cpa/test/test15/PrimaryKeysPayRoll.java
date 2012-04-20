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
package org.castor.cpa.test.test15;

import org.junit.Ignore;

@Ignore
public final class PrimaryKeysPayRoll {
    private int _id;
    private int _holiday;
    private int _rate;
    private PrimaryKeysEmployee _employee;
    
    public PrimaryKeysPayRoll() { }

    public PrimaryKeysPayRoll(final int id, final int holiday, final int rate) {
        _id = id;
        _holiday = holiday;
        _rate = rate;
    }

    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }
    
    public int getHoliday() { return _holiday; }
    public void setHoliday(final int holiday) { _holiday = holiday; }
    
    public int getHourlyRate() { return _rate; }
    public void setHourlyRate(final int rate) { _rate = rate; }
    
    public PrimaryKeysEmployee getEmployee() { return _employee; }
    public void setEmployee(final PrimaryKeysEmployee employee) { _employee = employee; }
}
