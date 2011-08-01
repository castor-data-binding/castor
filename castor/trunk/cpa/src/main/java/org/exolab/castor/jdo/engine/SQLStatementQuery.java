/*
 * Copyright 2006 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann, Ralf Joachim
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
 *
 * $Id$
 */
package org.exolab.castor.jdo.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.engine.SQLEngine;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;

public final class SQLStatementQuery {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementQuery.class);

    private final SQLEngine _engine;
    
    private final PersistenceFactory _factory;
    
    private final String _type;

    private final String _mapTo;

    private QueryExpression _queryExpression;

    public SQLStatementQuery(final SQLEngine engine, final PersistenceFactory factory)
    throws MappingException {
        _engine = engine;
        _factory = factory;
        _type = engine.getDescriptor().getJavaClass().getName();
        _mapTo = new ClassDescriptorJDONature(engine.getDescriptor()).getTableName();

        buildStatement();
    }

    private void buildStatement() throws MappingException {
        try {
            QueryExpression expr = _factory.getQueryExpression();
            
            Map<String, Boolean> identitiesUsedForTable = new HashMap<String, Boolean>();
            Vector<String> joinTables = new Vector<String>();

            // join all the extended table
            ClassDescriptor curDesc = _engine.getDescriptor();
            ClassDescriptor baseDesc;
            while (curDesc.getExtends() != null) {
                baseDesc = curDesc.getExtends();
                String[] curDescIdNames = SQLHelper.getIdentitySQLNames(curDesc);
                String[] baseDescIdNames = SQLHelper.getIdentitySQLNames(baseDesc);
                expr.addInnerJoin(
                        new ClassDescriptorJDONature(curDesc).getTableName(), curDescIdNames,
                        new ClassDescriptorJDONature(curDesc).getTableName(), 
                        new ClassDescriptorJDONature(baseDesc).getTableName(), baseDescIdNames,
                        new ClassDescriptorJDONature(baseDesc).getTableName());
                joinTables.add(new ClassDescriptorJDONature(baseDesc).getTableName());
                curDesc = baseDesc;
            }
            
            SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
            SQLFieldInfo[] fields = _engine.getInfo();

            // join all the related/depended table
            String aliasOld = null;
            String alias = null;
            
            for (int i = 0; i < fields.length; i++) {
                SQLFieldInfo field = fields[i];
                
                if (i > 0) { aliasOld = alias; }
                alias = field.getTableName();

                // add id fields for root table if first field points to a separate table
                if ((i == 0) && field.isJoined()) {
                    String[] identities = SQLHelper.getIdentitySQLNames(_engine.getDescriptor());
                    for (int j = 0; j < identities.length; j++) {
                        expr.addColumn(
                                new ClassDescriptorJDONature(curDesc).getTableName(),
                                identities[j]);
                    }
                    identitiesUsedForTable.put(
                            new ClassDescriptorJDONature(curDesc).getTableName(),
                            Boolean.TRUE);
                }
                
                // add id columns to select statement
                if (!alias.equals(aliasOld) && !field.isJoined()) {
                    ClassDescriptor classDescriptor =
                        field.getFieldDescriptor().getContainingClassDescriptor();
                    boolean isTableNameAlreadyAdded = identitiesUsedForTable.containsKey(
                            new ClassDescriptorJDONature(classDescriptor).getTableName());
                    if (!isTableNameAlreadyAdded) {
                        String[] identities = SQLHelper.getIdentitySQLNames(classDescriptor);
                        for (int j = 0; j < identities.length; j++) {
                            expr.addColumn(alias, identities[j]);
                        }
                        identitiesUsedForTable.put(
                                new ClassDescriptorJDONature(classDescriptor).getTableName(),
                                Boolean.TRUE);
                    }
                }

                if (field.isJoined()) {
                    int offset = 0;
                    String[] rightCol = field.getJoinFields();
                    String[] leftCol = new String[ids.length - offset];
                    for (int j = 0; j < leftCol.length; j++) {
                        leftCol[j] = ids[j + offset].getName();
                    }
                    ClassDescriptor clsDescriptor = _engine.getDescriptor();
                    ClassDescriptorJDONature nature = new ClassDescriptorJDONature(clsDescriptor); 
                    if (joinTables.contains(field.getTableName())
                            || nature.getTableName().equals(field.getTableName())) {
                        
                        // should not mix with aliases in ParseTreeWalker
                        alias = alias.replace('.', '_') + "_f" + i;
                        expr.addOuterJoin(_mapTo, leftCol, field.getTableName(), rightCol, alias);
                    } else {
                        expr.addOuterJoin(_mapTo, leftCol,
                        		field.getTableName(), rightCol, field.getTableName());
                        joinTables.add(field.getTableName());
                    }
                }

                for (int j = 0; j < field.getColumnInfo().length; j++) {
                    expr.addColumn(alias, field.getColumnInfo()[j].getName());
                }
                
                expr.addTable(field.getTableName(), alias);
            }

            // 'join' all the extending tables 
            List<ClassDescriptor> classDescriptorsToAdd = new LinkedList<ClassDescriptor>();
            ClassDescriptor classDescriptor = null;
            SQLHelper.addExtendingClassDescriptors(classDescriptorsToAdd,
                    new ClassDescriptorJDONature(_engine.getDescriptor()).getExtended());
            
            if (classDescriptorsToAdd.size() > 0) {
                Iterator<ClassDescriptor> iter = classDescriptorsToAdd.iterator();
                while (iter.hasNext()) {
                    classDescriptor = iter.next();
                    ClassDescriptorJDONature clsDescNature = 
                        new ClassDescriptorJDONature(classDescriptor);
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Adding outer left join for "
                                + classDescriptor.getJavaClass().getName() + " on table "
                                + clsDescNature.getTableName());
                    }
                    
                    String[] engDescIdNames = SQLHelper.getIdentitySQLNames(
                            _engine.getDescriptor());
                    String[] clsDescIdNames = SQLHelper.getIdentitySQLNames(classDescriptor);
                    expr.addOuterJoin(_mapTo, engDescIdNames, 
                            clsDescNature.getTableName(), clsDescIdNames,
                            clsDescNature.getTableName());

                    Persistence persistenceEngine;
                    try {
                        persistenceEngine = _factory.getPersistence(classDescriptor);
                    } catch (MappingException e) {
                        throw new QueryException(
                                "Problem obtaining persistence engine for ClassDescriptor "
                                + classDescriptor.getJavaClass().getName(), e);
                    }

                    SQLEngine engine = (SQLEngine) persistenceEngine;
                    SQLColumnInfo[] idInfos = engine.getColumnInfoForIdentities();
                    for (int i = 0; i < idInfos.length; i++) {
                        expr.addColumn(clsDescNature.getTableName(), idInfos[i].getName());
                    }
                    
                    SQLFieldInfo[] fieldInfos = ((SQLEngine) persistenceEngine).getInfo();
                    for (int i = 0; i < fieldInfos.length; i++) {
                        boolean hasFieldToAdd = false;
                        SQLColumnInfo[] columnInfos = fieldInfos[i].getColumnInfo();
                        if (clsDescNature.getTableName().equals(fieldInfos[i].getTableName())) {
                            for (int j = 0; j < columnInfos.length; j++) {
                                expr.addColumn(clsDescNature.getTableName(),
                                        fieldInfos[i].getColumnInfo()[j].getName());
                            }
                            hasFieldToAdd = true;
                        }
                        
                        if (hasFieldToAdd) {
                            expr.addTable(clsDescNature.getTableName(),
                            		clsDescNature.getTableName());
                        }
                    }
                }
            }
            
            // add table information if the class in question does not have any non-identity fields
            if (fields.length == 0) {
                for (int i = 0; i < ids.length; i++) {
                	expr.addColumn(_mapTo, ids[i].getName());
                }
            }

            _queryExpression = expr;

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.finding", _type, _queryExpression));
            }
        } catch (QueryException ex) {
            LOG.warn("Problem building SQL", ex);
            throw new MappingException(ex);
        }
    }
    
    public QueryExpression getQueryExpression() {
        return (QueryExpression) _queryExpression.clone();
    }
}
