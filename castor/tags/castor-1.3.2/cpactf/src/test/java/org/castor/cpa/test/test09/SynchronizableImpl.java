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
package org.castor.cpa.test.test09;

import java.util.Iterator;
import java.util.List;

import org.castor.persist.TransactionContext;
import org.exolab.castor.persist.TxSynchronizable;
import org.junit.Ignore;

@Ignore
public final class SynchronizableImpl implements TxSynchronizable {
    public void committed(final TransactionContext tx) {
        Iterator<?> it = tx.iterateReadWriteObjectsInTransaction();
        if (it.hasNext()) {
            List<String> syncs = TestSynchronizable.getSynchronizableList();
            while (it.hasNext()) {
                Object object = it.next();
                boolean isDeleted = tx.isDeleted(object);
                boolean isCreated = tx.isCreated(object);
                boolean isUpdateCacheNeeded = tx.isUpdateCacheNeeded(object);
                boolean isUpdatePersistNeeded = tx.isUpdatePersistNeeded(object);
                String change = "";
                if (isDeleted) { change = change + "deleted"; }
                if (isCreated) { change = change + "created"; }
                if (isUpdateCacheNeeded || isUpdatePersistNeeded) {
                    change = change + "updated";
                }
                syncs.add(change + ":" + object.toString());
            }
        }
    }
    public void rolledback(final TransactionContext tx) {
        TestSynchronizable.getSynchronizableList().add("rolledback");
    }
}
