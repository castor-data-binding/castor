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
package org.castor.cpa.test.test88;

import java.util.Collection;
import java.util.Date;

import org.junit.Ignore;

@Ignore
public class LazyEmployee extends LazyPerson {
    private Date _startDate;
    private LazyPayRoll _payroll;
    private LazyContract _contract;
    private Collection<LazyProject> _projects;

    public void setStartDate(final Date startDate) { _startDate = startDate; }
    public Date getStartDate() { return _startDate; }
    
    public void setPayRoll(final LazyPayRoll payroll) { _payroll = payroll; }
    public LazyPayRoll getPayRoll() { return _payroll; }
    
    public void setContract(final LazyContract contract) { _contract = contract; }
    public LazyContract getContract() { return _contract; }
    
    public void setProjects(final Collection<LazyProject> projects) { _projects = projects; }
    public Collection<LazyProject> getProjects() { return _projects; }
}
