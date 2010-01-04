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

import java.util.ArrayList;

import org.junit.Ignore;

@Ignore
public final class PrimaryKeysContract {
    private int _policy;
    private int _contract;
    private String _comment;
    private PrimaryKeysEmployee _employee;
    private ArrayList<PrimaryKeysCategory> _category;
    
    public PrimaryKeysContract() { }

    public PrimaryKeysContract(final int policy, final int contract, final String comment) {
        _policy = policy;
        _contract = contract;
        _comment = comment;
    }

    public int getPolicyNo() { return _policy; }
    public void setPolicyNo(final int policy) { _policy = policy; }

    public int getContractNo() { return _contract; }
    public void setContractNo(final int contract) { _contract = contract; }

    public String getComment() { return _comment; }
    public void setComment(final String comment) { _comment = comment; }

    public PrimaryKeysEmployee getEmployee() { return _employee; }
    public void setEmployee(final PrimaryKeysEmployee employee) { _employee = employee; }

    public ArrayList<PrimaryKeysCategory> getCategory() { return _category; }
    public void setCategory(final ArrayList<PrimaryKeysCategory> category) {
        _category = category;
    }
}
