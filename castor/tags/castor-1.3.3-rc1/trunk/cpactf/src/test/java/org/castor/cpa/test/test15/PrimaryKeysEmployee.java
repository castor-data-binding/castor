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

import java.util.Date;

import org.junit.Ignore;

@Ignore
public final class PrimaryKeysEmployee extends PrimaryKeysPerson {
    private Date                _start;
    private PrimaryKeysPayRoll  _payroll;
    private PrimaryKeysContract _contract;
    
    public PrimaryKeysEmployee() { }

    public PrimaryKeysEmployee(final String firstname,
            final String lastname, final Date birth, final Date start) {
        super(firstname, lastname, birth);
        _start = start;
    }

    public Date getStartDate() { return _start; }
    public void setStartDate(final Date start) { _start = start; }
    
    public PrimaryKeysPayRoll getPayRoll() { return _payroll; }
    public void setPayRoll(final PrimaryKeysPayRoll payroll) { _payroll = payroll; }
    
    public PrimaryKeysContract getContract() { return _contract; }
    public void setContract(final PrimaryKeysContract contract) { _contract = contract; }
}
