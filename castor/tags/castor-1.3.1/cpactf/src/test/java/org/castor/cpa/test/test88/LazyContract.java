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

import org.junit.Ignore;

@Ignore
public class LazyContract {
    private int _policyNo;
    private int _contractNo;
    private String _comment;
    private LazyEmployee _employee;
    private Collection<LazyContractCategory> _category;

    public void setPolicyNo(final int policy) { _policyNo = policy; }
    public int getPolicyNo() { return _policyNo; }
    
    public void setContractNo(final int no) { _contractNo = no; }
    public int getContractNo() { return _contractNo; }
    
    public void setComment(final String s) { _comment = s; }
    public String getComment() { return _comment; }
    
    public void setEmployee(final LazyEmployee employee) { _employee = employee; }
    public LazyEmployee getEmployee() { return _employee; }
    
    public void setCategory(final Collection<LazyContractCategory> category) {
        _category = category;
    }
    public Collection<LazyContractCategory> getCategory() { return _category; }
}