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
package org.castor.cpa.query;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Interface for Select Query of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public interface SelectQuery extends QueryObject {
    
    /**
     * Factory method to create new Schema.
     * 
     * @param schema the schema
     * @param identifier the identifier
     * 
     * @return the schema
     */
    Schema newSchema(String schema, String identifier);
    
    /**
     * Factory method to create new Schema.
     * 
     * @param schema the schema
     * @param identifier the identifier
     * 
     * @return the schema
     */
    Schema newSchema(Class < ? > schema, String identifier);
    
    /**
     * Factory method to create new Parameter.
     * 
     * @param position the <b>int</b>
     * 
     * @return the parameter
     */
    Parameter newParameter(int position);
    
    /**
     * New parameter.
     * 
     * @param name the name
     * 
     * @return the parameter
     */
    Parameter newParameter(String name);
    
    /**
     * New boolean.
     * 
     * @param value the value
     * 
     * @return the literal
     */
    Literal newBoolean(boolean value);
    
    /**
     * New numeric.
     * 
     * @param value the value
     * 
     * @return the literal
     */
    Literal newNumeric(long value);
    
    /**
     * New numeric.
     * 
     * @param value the value
     * 
     * @return the literal
     */
    Literal newNumeric(double value);
    
    /**
     * New numeric.
     * 
     * @param value the value
     * 
     * @return the literal
     */
    Literal newNumeric(BigDecimal value);
    
    /**
     * New string.
     * 
     * @param value the value
     * 
     * @return the literal
     */
    Literal newString(String value);
    
    /**
     * New enum.
     * 
     * @param identifier the identifier
     * 
     * @return the literal
     */
    Literal newEnum(Enum < ? > identifier);
    
    
    /**
     * New order.
     * 
     * @param field the field
     * 
     * @return the order
     */
    Order newOrder(Field field);
    
    /**
     * New order.
     * 
     * @param field the field
     * @param direction the direction
     * 
     * @return the order
     */
    Order newOrder(Field field, OrderDirection direction);
    
    
    /**
     * New temporal.
     * 
     * @param temporalType the temporal type
     * 
     * @return the expression
     */  
    Expression newTemporal(TemporalType temporalType);
    
    /**
     * New temporal.
     * 
     * @param temporalType the temporal type
     * @param value the value
     * 
     * @return the expression
     */
    Expression newTemporal(TemporalType temporalType, Date value);
    
    /**
     * New temporal.
     * 
     * @param temporalType the temporal type
     * @param value the value
     * 
     * @return the expression
     */
    Expression newTemporal(TemporalType temporalType, Calendar value);
    
    /**
     * Sets the distinct.
     * 
     * @param distinct the new distinct
     */
    void setDistinct(boolean distinct);
    
    /**
     * Adds the projection.
     * 
     * @param field the field
     */
    void addProjection(Field field);
    
    /**
     * Adds the schema.
     * 
     * @param schema the schema
     */
    void addSchema(Schema schema);
    
    /**
     * Sets the where.
     * 
     * @param condition the new where
     */
    void setWhere(Condition condition);
    
    /**
     * Sets the order.
     * 
     * @param order the new order
     */
    void setOrder(Order order);
    
    /**
     * Sets the limit.
     * 
     * @param limit the new limit
     */
    void setLimit(int limit);
    
    /**
     * Sets the limit.
     * 
     * @param limit the new limit
     */
    void setLimit(Parameter limit);
    
    /**
     * Sets the limit.
     * 
     * @param limit the limit
     * @param offset the offset
     */
    void setLimit(int limit, int offset);
    
    /**
     * Sets the limit.
     * 
     * @param limit the limit
     * @param offset the offset
     */
    void setLimit(Parameter limit, Parameter offset);
}
