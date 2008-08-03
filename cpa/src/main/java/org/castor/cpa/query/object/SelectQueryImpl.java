/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.query.object;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Field;
import org.castor.cpa.query.Literal;
import org.castor.cpa.query.Order;
import org.castor.cpa.query.OrderDirection;
import org.castor.cpa.query.Parameter;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.SelectQuery;
import org.castor.cpa.query.TemporalType;
import org.castor.cpa.query.object.literal.BigDecimalLiteral;
import org.castor.cpa.query.object.literal.BooleanLiteral;
import org.castor.cpa.query.object.literal.DoubleLiteral;
import org.castor.cpa.query.object.literal.LongLiteral;
import org.castor.cpa.query.object.literal.StringLiteral;
import org.castor.cpa.query.object.parameter.NamedParameter;
import org.castor.cpa.query.object.parameter.PositionalParameter;

/**
 * Final class that implements SelectQuery.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class SelectQueryImpl extends AbstractQueryObject implements SelectQuery {
    //--------------------------------------------------------------------------

    /** Do the select query deliver distinct results? */
    private boolean _distinct;
    
    /** List of projections of the select query.
     *  First implementation will only support one projection. */
    private final List < Projection > _projections = new ArrayList < Projection > ();
    
    /** List of schemas of the select query.
     *  First implementation will support only one schema without joins. */
    private final List < Schema > _schemas = new ArrayList < Schema > ();
    
    /** Where condition of the select query. */
    private Condition _where;
    
    /** Order clause of the select query. */
    private Order _order;
    
    /** Limit clause of the select query. */
    private Limit _limit;
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Schema newSchema(final String name, final String identifier) {
        return new SchemaImpl(name, identifier);
    }
    
    /**
     * {@inheritDoc}
     */
    public Schema newSchema(final Class < ? > type, final String identifier) {
        return new SchemaImpl(type, identifier);
    }
    
    /**
     * {@inheritDoc}
     */
    public Parameter newParameter(final int position) {
        return new PositionalParameter(position);
    }

    /**
     * {@inheritDoc}
     */
    public Parameter newParameter(final String name) {
        return new NamedParameter(name);
    }
    
    /**
     * {@inheritDoc}
     */
    public Literal newBoolean(final boolean value) {
        return new BooleanLiteral(value);
    }

    /**
     * {@inheritDoc}
     */
    public Literal newNumeric(final long value) {
        return new LongLiteral(value);
    }

    /**
     * {@inheritDoc}
     */
    public Literal newNumeric(final double value) {
        return new DoubleLiteral(value);
    }

    /**
     * {@inheritDoc}
     */
    public Literal newNumeric(final BigDecimal value) {
        return new BigDecimalLiteral(value);
    }

    /**
     * {@inheritDoc}
     */
    public Literal newString(final String value) {
        return new StringLiteral(value);
    }

    /**
     * {@inheritDoc}
     */
    public Literal newEnum(final Enum < ? > identifier) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Order newOrder(final Field field) {     
        return new OrderImpl(field);
    }
    
    /**
     * {@inheritDoc}
     */
    public Order newOrder(final Field field, final OrderDirection direction) {     
        return new OrderImpl(field, direction);
    }  
    
    /**
     * {@inheritDoc}
     */
    public Expression newTemporal(final TemporalType temporalType) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Expression newTemporal(final TemporalType temporalType, final Date value) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Expression newTemporal(final TemporalType temporalType, final Calendar value) {
        // TODO Auto-generated method stub
        return null;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void setDistinct(final boolean distinct) {
       _distinct = distinct; 
    }

    /**
     * {@inheritDoc}
     */
    public void addProjection(final Field field) {
        _projections.add(new Projection(field));
    }

    /**
     * {@inheritDoc}
     */
    public void addProjection(final Field field, final String alias) {
        _projections.add(new Projection(field, alias));
    }

    /**
     * {@inheritDoc}
     */
    public void addSchema(final Schema schema) {
        _schemas.add(schema);
    }

    /**
     * {@inheritDoc}
     */
    public void setWhere(final Condition condition) {
        _where = condition;  
    }
    
    /**
     * {@inheritDoc}
     */
    public void setOrder(final Order order) {
        _order = order;  
    }

    /**
     * {@inheritDoc}
     */
    public void setLimit(final int limit) {
        _limit = new Limit(new LongLiteral(limit));
    }

    /**
     * {@inheritDoc}
     */
    public void setLimit(final Parameter limit) {
        _limit = new Limit(limit);
    }

    /**
     * {@inheritDoc}
     */
    public void setLimit(final int limit, final int offset) {
        _limit = new Limit(new LongLiteral(limit), new LongLiteral(offset));
    }

    /**
     * {@inheritDoc}
     */
    public void setLimit(final Parameter limit, final Parameter offset) {
        _limit = new Limit(limit, offset);
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        sb.append("SELECT ");
        if (_distinct) { sb.append("DISTINCT "); }
        for (Iterator < Projection > iter = _projections.iterator(); iter.hasNext(); ) {
            iter.next().toString(sb);
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(" FROM ");
        for (Iterator < Schema > iter = _schemas.iterator(); iter.hasNext(); ) {
            iter.next().toString(sb);
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        if (_where != null) {
            sb.append(" WHERE ");
            _where.toString(sb);
        }   
        if (_order != null) {
             sb.append(" ORDER BY ");
             _order.toString(sb);
        }
        if (_limit != null) {
            sb.append(" LIMIT ");
            _limit.toString(sb);
        }  
        return sb;
    }

    //--------------------------------------------------------------------------
}
