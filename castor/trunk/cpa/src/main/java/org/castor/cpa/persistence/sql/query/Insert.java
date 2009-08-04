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

import java.text.MessageFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
import org.castor.cpa.persistence.sql.query.expression.Expression;

/**
 * Class to generate SQL Insert query statements. 
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Tue, 28 Jul 2009) $
 */
public final class Insert extends QueryObject {    
    //-----------------------------------------------------------------------------------    

    /** Qualifier of the table to update records of. */
    private final Qualifier _qualifier;
    
    /** Array of Column fields whose values needs to be inserted. */
    private List<Expression> _fields; 
    
    /** Parameter values needs to be inserted. */
    private List<Expression> _values;
    
    /** Sequence expression of the form SEQUENCENAME.nextval. */
    private String _seqExpression;
    
    /** ID of the table. */
    private String _primKeyName;
    
    //-----------------------------------------------------------------------------------    

    /**
     * Construct a SQL insert statement that inserts into the table.
     *  
     * @param name Name of the table in update statement. 
     */
    public Insert(final String name) {        
        _qualifier = new Table(name);
    }    
    
    //-----------------------------------------------------------------------------------    

    /**
     * Appends the provided field to the list of fields. 
     * 
     * @param name Column object representing a column to be inserted.
     * @param value Parameter value to be inserted.
     */
    public void addInsert(final Column name, final Expression value) {
        if (_fields == null) { _fields = new ArrayList<Expression>(); }         
        if (_values == null) { _values = new ArrayList<Expression>(); } 
        
        _fields.add(name);
        _values.add(value);
    }

    /**
     * Appends a field representing a column to be inserted into the table. 
     * 
     * @param name Name of the column to be inserted.
     */
    public void addInsert(final String name) {
        addInsert(new Column(name), new Parameter(name));
    }
    
    /**
     * Appends a field representing a column to be inserted into the table. 
     * 
     * @param qualifier Qualifier to be appended.
     * @param name Name of the column to be inserted.
     */
    public void addInsert(final String qualifier, final String name) {
        addInsert(new Column(new Table(qualifier), name), new Parameter(name));
    }
    
    /**
     * Appends sequence to the insert statement.
     * 
     * @param seqName Name of the sequence.
     * @param primKeyName ID of the Table.
     */
    public void addSequence(final String seqName, final String primKeyName) {
        _primKeyName = primKeyName;
        _seqExpression = MessageFormat.format(seqName, 
                new Object[] {this._qualifier.name(), _primKeyName});
        _seqExpression += ".nextval";    
    }
    
    /**
     * 
     * @return {code}true{code} If sequence has been added to this instance 
     * of insert hierarchy.
     */
    public boolean hasSequence() {
        return !(_seqExpression == null);
    }
    //-----------------------------------------------------------------------------------    
    
    @Override
    public void toString(final QueryContext ctx) {
        ctx.append(QueryConstants.INSERT);
        ctx.append(QueryConstants.SPACE);        
        ctx.append(QueryConstants.INTO);        
        ctx.append(QueryConstants.SPACE);
        
        _qualifier.toString(ctx);
        
        if (hasSequence()) {
            ctx.append(QueryConstants.LPAREN);
            ctx.append(ctx.quoteName(_primKeyName));
            if (_fields != null) {
                ctx.append(QueryConstants.SEPERATOR);
                ctx.append(QueryConstants.SPACE);
            }
        }
        
        if (_fields != null) {
            if (!hasSequence()) {
                ctx.append(QueryConstants.LPAREN);
            }
            
            for (Iterator<Expression> iter = _fields.iterator(); iter.hasNext(); ) {
                iter.next().toString(ctx);
                if (iter.hasNext()) {
                    ctx.append(QueryConstants.SEPERATOR);
                    ctx.append(QueryConstants.SPACE);
                }
            }
        }
        
        if (hasSequence() || _fields != null) {
            ctx.append(QueryConstants.RPAREN); 
        }
        
        ctx.append(QueryConstants.SPACE);
        ctx.append(QueryConstants.VALUES);
        ctx.append(QueryConstants.LPAREN);   
        
        if (hasSequence()) {
            ctx.append(ctx.quoteName(_seqExpression));
            if (_values != null) {
                ctx.append(QueryConstants.SEPERATOR);
                ctx.append(QueryConstants.SPACE);
            }
        }
        
        if (_values != null) {
            for (Iterator<Expression> iter = _values.iterator(); iter.hasNext(); ) {
                iter.next().toString(ctx);
                if (iter.hasNext()) {
                    ctx.append(QueryConstants.SEPERATOR);
                    ctx.append(QueryConstants.SPACE);
                }
            }
        }
        ctx.append(QueryConstants.RPAREN);
    }
    
    //-----------------------------------------------------------------------------------
}
