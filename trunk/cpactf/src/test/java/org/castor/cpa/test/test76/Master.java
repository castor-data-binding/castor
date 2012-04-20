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
package org.castor.cpa.test.test76;

import java.util.ArrayList;

import org.junit.Ignore;

/**
 * @table MASTER
 * @key-generator MAX
 */
@Ignore
public final class Master {
    /** @primary-key */
    private int _id;

    /** @sql-name depend1_oid */
    private Depend1 _depend1;

    /**
     * @field-type jdo.Depend2
     * @many-key master_oid
     */
    private ArrayList<Depend2> _depends2 = new ArrayList<Depend2>();

    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }

    public Depend1 getDepend1() { return _depend1; }
    public void setDepend1(final Depend1 depend1) { _depend1 = depend1; }

    public ArrayList<Depend2> getDepends2() { return _depends2; }
    public void setDepends2(final ArrayList<Depend2> depends2) { _depends2 = depends2; }
    public void addDepend2(final Depend2 depend2) {
        _depends2.add(depend2);
        depend2.setMaster(this);
    }

    public String toString() { return "Master object #" + _id; }
}
