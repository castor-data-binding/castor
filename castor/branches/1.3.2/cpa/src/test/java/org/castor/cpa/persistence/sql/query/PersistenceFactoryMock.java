/*
 * Copyright 2009 Ralf Joachim, Ahmad Hassan
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
package org.castor.cpa.persistence.sql.query;

import org.castor.cpa.persistence.sql.keygen.KeyGenerator;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.QueryExpression;
import org.junit.Ignore;

/**
 * Mock object to test QueryContext.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
@Ignore
public final class PersistenceFactoryMock implements PersistenceFactory {
    public Class<?> adjustSqlType(final Class<?> sqlType) {
        return null;
    }

    public PersistenceQuery getCallQuery(final String call, final Class<?>[] paramTypes,
            final Class<?> javaClass, final String[] fields, final int[] sqlTypes) {
        return null;
    }

    public String getFactoryName() {
        return null;
    }

    public String getIdentitySelectString(final String tableName, final String columnName) {
        return null;
    }

    public KeyGenerator getKeyGenerator(final ClassDescriptor clsDesc) throws MappingException {
        return null;
    }

    public Persistence getPersistence(final ClassDescriptor clsDesc) throws MappingException {
        return null;
    }

    public QueryExpression getQueryExpression() {
        return null;
    }

    public String getSequenceAfterSelectString(final String seqName, final String tableName) {
        return null;
    }

    public String getSequenceBeforeSelectString(final String seqName,
            final String tableName, final int increment) {
        return null;
    }

    public boolean isKeyGeneratorIdentitySupported() {
        return false;
    }

    public boolean isKeyGeneratorIdentityTypeSupported(final int type) {
        return false;
    }

    public boolean isKeyGeneratorSequenceSupported(
            final boolean returning, final boolean trigger) {
        return false;
    }

    public boolean isKeyGeneratorSequenceTypeSupported(final int type) {
        return false;
    }

    public String quoteName(final String name) {
        return "'" + name + "'";
    }

    public String getSequenceNextValString(final String seqName) {
        return "NEXTVAL(" + seqName + ")";
    }
}
