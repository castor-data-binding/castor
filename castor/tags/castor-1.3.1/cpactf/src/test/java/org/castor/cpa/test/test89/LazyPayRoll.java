/*
 * Copyright 2008 Udai Gupta
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
package org.castor.cpa.test.test89;

import org.junit.Ignore;

@Ignore
public class LazyPayRoll {
    private int _id;
    private int _holiday;
    private int _hourlyRate;
    private LazyEmployee _employee;

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }
    
    public void setHoliday(final int holiday) { _holiday = holiday; }
    public int getHoliday() { return _holiday; }
    
    public void setHourlyRate(final int rate) { _hourlyRate = rate; }
    public int getHourlyRate() { return _hourlyRate; }
    
    public void setEmployee(final LazyEmployee employee) { _employee = employee; }
    public LazyEmployee getEmployee() { return _employee; }
}