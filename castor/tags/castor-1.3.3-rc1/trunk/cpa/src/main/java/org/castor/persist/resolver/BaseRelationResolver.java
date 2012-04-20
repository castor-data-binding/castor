/*
 * Copyright 2010 Werner Guttmann
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
package org.castor.persist.resolver;

import org.castor.persist.TransactionContext;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.FieldMolder;
import org.castor.persist.CascadingType;

/**
 * Abstract base of a {@link org.castor.persist.resolver.ResolverStrategy}
 * implementation for any kind of relation.
 * 
 * @author Michael Schroeder
 * @since 1.3.2
 */
public abstract class BaseRelationResolver implements ResolverStrategy {

    /**
     * Associated {@link ClassMolder}.
     */
    protected ClassMolder _classMolder;

    /**
     * Associated {@link FieldMolder}.
     */
    protected FieldMolder _fieldMolder;

    /**
     * Creates an instance of BasicRelationResolver.
     * 
     * @param classMolder
     *            Associated {@link ClassMolder}
     * @param fieldMolder
     *            Associated {@link FieldMolder}
     */
    public BaseRelationResolver(final ClassMolder classMolder,
            final FieldMolder fieldMolder) {
        _classMolder = classMolder;
        _fieldMolder = fieldMolder;
    }

    /**
     * Indicates whether 'cascading create' mode has been requested.
     * @param tx The active transaction.
     * @return True if 'cascading create' has been requested.
     */
    public boolean isCascadingCreate(final TransactionContext tx) {
        return tx.isAutoStore()
                || _fieldMolder.getCascading().contains(CascadingType.CREATE);
    }


    /**
     * Indicates whether 'cascading delete' mode has been requested.
     * @param tx The active transaction.
     * @return True if 'cascading delete' has been requested.
     */
    // TODO: what's the role of autostore with these ones?
    public boolean isCascadingDelete() {
        return _fieldMolder.getCascading().contains(CascadingType.DELETE);
    }

    /**
     * Indicates whether 'cascading update' mode has been requested.
     * @param tx The active transaction.
     * @return True if 'cascading update' has been requested.
     */
    public boolean isCascadingUpdate(final TransactionContext tx) {
        return tx.isAutoStore()
                || _fieldMolder.getCascading().contains(CascadingType.UPDATE);
    }

}
