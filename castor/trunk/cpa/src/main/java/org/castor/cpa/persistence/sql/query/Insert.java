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

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
import org.castor.cpa.persistence.sql.query.expression.Expression;
import org.castor.cpa.persistence.sql.query.expression.NextVal;

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
     * @param name Name of the column to be inserted.
     * @param seqName Name of the sequence.
     */
    public void addInsert(final String name, final String seqName) {
        addInsert(new Column(name), new NextVal(seqName));
    }
    
    //-----------------------------------------------------------------------------------    
    
    @Override
    public void toString(final QueryContext ctx) {
        ctx.append(QueryConstants.INSERT);
        ctx.append(QueryConstants.SPACE);        
        ctx.append(QueryConstants.INTO);        
        ctx.append(QueryConstants.SPACE);
        
        _qualifier.toString(ctx);
        
        if (_fields != null) {
            ctx.append(QueryConstants.LPAREN);
            
            for (Iterator<Expression> iter = _fields.iterator(); iter.hasNext(); ) {
                iter.next().toString(ctx);
                if (iter.hasNext()) {
                    ctx.append(QueryConstants.SEPERATOR);
                    ctx.append(QueryConstants.SPACE);
                }
            }

            ctx.append(QueryConstants.RPAREN); 
        }
        
        ctx.append(QueryConstants.SPACE);
        ctx.append(QueryConstants.VALUES);
        
        if (_values != null) {
            ctx.append(QueryConstants.LPAREN);   

            for (Iterator<Expression> iter = _values.iterator(); iter.hasNext(); ) {
                iter.next().toString(ctx);
                if (iter.hasNext()) {
                    ctx.append(QueryConstants.SEPERATOR);
                    ctx.append(QueryConstants.SPACE);
                }
            }

            ctx.append(QueryConstants.RPAREN);
        }
    }
    
    //-----------------------------------------------------------------------------------
}
