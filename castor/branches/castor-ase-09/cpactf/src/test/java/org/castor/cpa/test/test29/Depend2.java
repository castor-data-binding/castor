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
package org.castor.cpa.test.test29;

import org.junit.Ignore;

/**
 * @table DEPEND2
 * @key-generator MAX
 * @depends jdo.Master
 */
@Ignore
public final class Depend2 {
    /** @sql-name master_oid */
    private DependMaster _master;

    /** @primary-key */
    private int _id;

    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }

    public DependMaster getMaster() { return _master; }
    public void setMaster(final DependMaster master) { _master = master; }
}
