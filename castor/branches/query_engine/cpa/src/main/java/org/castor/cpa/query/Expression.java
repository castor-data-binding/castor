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
 * Interface specification for expressions of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public interface Expression extends QueryObject {
    
    /**
     * The method for Add arithmetic expression of query objects.
     * 
     * @param value The <b>long</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression add(long value);
    
    /**
     * The method for Add <b>( + )</b> additive arithmetic expression of query objects.
     * 
     * @param value The <b>double</b> value
     * 
     * @return The <b> Expression</b> of query objects of query objects
     */
    Expression add(double value);
    
    /**
     * The method for Add <b>( + )</b> additive arithmetic expression of query objects.
     * 
     * @param value The <b>BigDecimal</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression add(BigDecimal value);
    
    /**
     * The method for Add <b>( + )</b> additive arithmetic expression of query objects.
     * 
     * @param value The <b>Expression</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression add(Expression value);
    
    /**
     * The method for Subtract <b>( - )</b> additive arithmetic expression of query objects.
     * 
     * @param value The <b>long</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression subtract(long value);
    
    /**
     * The method for Subtract <b>( - )</b> additive arithmetic expression of query objects.
     * 
     * @param value The <b>double</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression subtract(double value);
    
    /**
     * The method for Subtract <b>( - )</b> additive arithmetic expression of query objects.
     * 
     * @param value The <b>BigDecimal</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression subtract(BigDecimal value);
    
    /**
     * The method for Subtract <b>( - )</b> additive arithmetic expression of query objects.
     * 
     * @param value The <b>Expression</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression subtract(Expression value);

    /**
     * The method for Concat <b>( || )</b> additive arithmetic expression of query objects.
     * 
     * @param value The <b>String</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression concat(String value);
    
    /**
     * The method for Concat <b>( || )</b> additive arithmetic expression of query objects.
     * 
     * @param value The <b>Expression</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression concat(Expression value);

    /**
     * The method for Concat <b>( || )</b> additive arithmetic expression of query objects.
     * 
     * @param value The <b>long</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression multiply(long value);
    
    /**
     * The method for Multiply <b>( * )</b> multiplicative arithmetic expression of query objects.
     * 
     * @param value The <b>double</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression multiply(double value);
    
    /**
     * The method for Multiply <b>( * )</b> multiplicative arithmetic expression of query objects.
     * 
     * @param value The <b>BigDecimal</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression multiply(BigDecimal value);
    
    /**
     * The method for Multiply <b>( * )</b> multiplicative arithmetic expression of query objects.
     * 
     * @param value The <b>Expression</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression multiply(Expression value);

    /**
     * The method for Divide <b>( / )</b> multiplicative arithmetic expression of query objects.
     * 
     * @param value The <b>long</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression divide(long value);
    
    /**
     * The method for Divide <b>( / )</b> multiplicative arithmetic expression of query objects.
     * 
     * @param value The <b>double</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression divide(double value);
    
    /**
     * The method for Divide <b>( / )</b> multiplicative arithmetic expression of query objects.
     * 
     * @param value The <b>BigDecimal</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression divide(BigDecimal value);
    
    /**
     * The method for Divide <b>( / )</b> multiplicative arithmetic expression of query objects.
     * 
     * @param value The <b>Expression</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression divide(Expression value);

    /**
     *  The method for Reminder <b>( % )</b> multiplicative arithmetic expression of query objects.
     * 
     * @param value The <b>long</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression remainder(long value);
    
    /**
     * The method for Reminder <b>( % )</b> multiplicative arithmetic expression of query objects.
     * 
     * @param value The <b>double</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression remainder(double value);
    
    /**
     * The method for Reminder <b>( % )</b> multiplicative arithmetic expression of query objects.
     * 
     * @param value The <b>BigDecimal</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression remainder(BigDecimal value);
    
    /**
     * The method for Reminder <b>( % )</b> multiplicative arithmetic expression of query objects.
     * 
     * @param value The <b>Expression</b> value
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression remainder(Expression value);
    
    /**
     * Plus.
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression plus();
    
    /**
     * Negate.
     * 
     * @return The <b> Expression</b> of query objects
     */
    Expression negate();
    
    /**
     * The method for <b>LENGTH</b> function of query objects.
     * 
     * @return The <b>Function</b> of query objects
     */
    Function length();
    
    /**
     * The method for <b>ABS</b> function of query objects.
     * 
     * @return The <b>Function</b> of query objects
     */
    Function abs();
    
    /**
     * The method for <b>SQRT</b> function of query objects.
     * 
     * @return The <b>Function</b> of query objects
     */
    Function sqrt();
    
    /**
     * The method for <b>LOWER</b> function of query objects.
     * 
     * @return The <b>Function</b> of query objects
     */
    Function lower();
    
    /**
     * The method for <b>UPPER</b> function of query objects..
     * 
     * @return The <b>Function</b> of query objects
     */
    Function upper();
    
    /**
     * The method for <b>LOCATE</b> function of query objects.
     * 
     * @param value The <b>String</b> value
     * 
     * @return The <b>Function</b> of query objects
     */
    Function locate(String value);
    
    /**
     * The method for <b>LOCATE</b> function of query objects.
     * 
     * @param value The <b>Expression</b> value
     * 
     * @return The <b>Function</b> of query objects
     */
    Function locate(Expression value);
    
    /**
     *  The method for <b>LOCATE</b> function of query objects.
     * 
     * @param value The <b>String</b> value
     * @param index The index
     * 
     * @return The <b>Function</b> of query objects
     */
    Function locate(String value, int index);
    
    /**
     *  The method for <b>LOCATE</b> function of query objects.
     * 
     * @param value The value
     * @param index The index
     * 
     * @return The <b>Function</b> of query objects
     */
    Function locate(String value, Expression index);
    
    /**
     * Locate.
     * 
     * @param value The value
     * @param index The index
     * 
     * @return The <b>Function</b> of query objects
     */
    Function locate(Expression value, int index);
    
    /**
     * Locate.
     * 
     * @param value The value
     * @param index The index
     * 
     * @return The <b>Function</b> of query objects
     */
    Function locate(Expression value, Expression index);
    
    /**
     * Substring.
     * 
     * @param index The index
     * @param length The length
     * 
     * @return The <b>Function</b> of query objects
     */
    Function substring(int index, int length);
    
    /**
     * Substring.
     * 
     * @param index The index
     * @param length The length
     * 
     * @return The <b>Function</b> of query objects
     */
    Function substring(int index, Expression length);
    
    /**
     * Substring.
     * 
     * @param index The index
     * @param length The length
     * 
     * @return The <b>Function</b> of query objects
     */
    Function substring(Expression index, int length);
    
    /**
     * Substring.
     * 
     * @param index The index
     * @param length The length
     * 
     * @return The <b>Function</b> of query objects
     */
    Function substring(Expression index, Expression length);
    
    /**
     * Trim.
     * 
     * @return The <b>Function</b> of query objects
     */
    Function trim();
    
    /**
     * Trim.
     * 
     * @param character The character
     * 
     * @return The <b>Function</b> of query objects
     */
    Function trim(char character);
    
    /**
     * Trim.
     * 
     * @param character The character
     * 
     * @return The <b>Function</b> of query objects
     */
    Function trim(Parameter character);
    
    /**
     * Trim.
     * 
     * @param trimSpecification The trim specification
     * 
     * @return The <b>Function</b> of query objects
     */
    Function trim(TrimSpecification trimSpecification);
    
    /**
     * Trim.
     * 
     * @param trimSpecification The trim specification
     * @param character The character
     * 
     * @return The <b>Function</b> of query objects
     */
    Function trim(TrimSpecification trimSpecification, char character);
    
    /**
     * Trim.
     * 
     * @param trimSpecification The trim specification
     * @param character The character
     * 
     * @return The <b>Function</b> of query objects
     */
    Function trim(TrimSpecification trimSpecification, Parameter character);
    
    /**
     * Equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition equal(boolean value);
    
    /**
     * Equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition equal(long value);
    
    /**
     * Equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition equal(double value);
    
    /**
     * Equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition equal(BigDecimal value);
    
    /**
     * Equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition equal(String value);
    
    /**
     * Equal.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition equal(TemporalType temporalType, Date value);
    
    /**
     * Equal.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition equal(TemporalType temporalType, Calendar value);
    
    /**
     * Equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition equal(Expression value);
    
    /**
     * Not equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition notEqual(boolean value);
    
    /**
     * Not equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition notEqual(long value);
    
    /**
     * Not equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition notEqual(double value);
    
    /**
     * Not equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition notEqual(BigDecimal value);
    
    /**
     * Not equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition notEqual(String value);
    
    /**
     * Not equal.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition notEqual(TemporalType temporalType, Date value);
    
    /**
     * Not equal.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition notEqual(TemporalType temporalType, Calendar value);
    
    /**
     * Not equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition notEqual(Expression value);
    
    /**
     * Less Then.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessThan(long value);
    
    /**
     * Less Then.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessThan(double value);
    
    /**
     * Less Then.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessThan(BigDecimal value);
    
    /**
     * Less Then.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessThan(String value);
    
    /**
     * Less Then.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessThan(TemporalType temporalType, Date value);
    
    /**
     * Less Then.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessThan(TemporalType temporalType, Calendar value);
    
    /**
     * Less Then.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessThan(Expression value);

    /**
     * Less equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessEqual(long value);
    
    /**
     * Less equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessEqual(double value);
    
    /**
     * Less equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessEqual(BigDecimal value);
    
    /**
     * Less equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessEqual(String value);
    
    /**
     * Less equal.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessEqual(TemporalType temporalType, Date value);
    
    /**
     * Less equal.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessEqual(TemporalType temporalType, Calendar value);
    
    /**
     * Less equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition lessEqual(Expression value);

    /**
     * Greater equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterEqual(long value);
    
    /**
     * Greater equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterEqual(double value);
    
    /**
     * Greater equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterEqual(BigDecimal value);
    
    /**
     * Greater equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterEqual(String value);
    
    /**
     * Greater equal.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterEqual(TemporalType temporalType, Date value);
    
    /**
     * Greater equal.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterEqual(TemporalType temporalType, Calendar value);
    
    /**
     * Greater equal.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterEqual(Expression value);

    /**
     * Greater Then.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterThan(long value);
    
    /**
     * Greater Then.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterThan(double value);
    
    /**
     * Greater Then.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterThan(BigDecimal value);
    
    /**
     * Greater Then.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterThan(String value);
    
    /**
     * Greater Then.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterThan(TemporalType temporalType, Date value);
    
    /**
     * Greater Then.
     * 
     * @param temporalType The temporal type
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterThan(TemporalType temporalType, Calendar value);
    
    /**
     * Greater Then.
     * 
     * @param value The value
     * 
     * @return The condition
     */
    Condition greaterThan(Expression value);
    
    /**
     * Like.
     * 
     * @param pattern The pattern
     * 
     * @return The condition
     */
    Condition like(String pattern);
    
    /**
     * Like.
     * 
     * @param pattern The pattern
     * @param escape The escape
     * 
     * @return The condition
     */
    Condition like(String pattern, char escape);
    
    /**
     * Like.
     * 
     * @param pattern The pattern
     * @param escape The escape
     * 
     * @return The condition
     */
    Condition like(String pattern, Parameter escape);
    
    /**
     * Like.
     * 
     * @param pattern The pattern
     * 
     * @return The condition
     */
    Condition like(Parameter pattern);
    
    /**
     * Like.
     * 
     * @param pattern The pattern
     * @param escape The escape
     * 
     * @return The condition
     */
    Condition like(Parameter pattern, char escape);
    
    /**
     * Like.
     * 
     * @param pattern The pattern
     * @param escape The escape
     * 
     * @return The condition
     */
    Condition like(Parameter pattern, Parameter escape);
    
    /**
     * Not like.
     * 
     * @param pattern The pattern
     * 
     * @return The condition
     */
    Condition notLike(String pattern);
    
    /**
     * Not like.
     * 
     * @param pattern The pattern
     * @param escape The escape
     * 
     * @return The condition
     */
    Condition notLike(String pattern, char escape);
    
    /**
     * Not like.
     * 
     * @param pattern The pattern
     * @param escape The escape
     * 
     * @return The condition
     */
    Condition notLike(String pattern, Parameter escape);
    
    /**
     * Not like.
     * 
     * @param pattern The pattern
     * 
     * @return The condition
     */
    Condition notLike(Parameter pattern);
    
    /**
     * Not like.
     * 
     * @param pattern The pattern
     * @param escape The escape
     * 
     * @return The condition
     */
    Condition notLike(Parameter pattern, char escape);
    
    /**
     * Not like.
     * 
     * @param pattern The pattern
     * @param escape The escape
     * 
     * @return The condition
     */
    Condition notLike(Parameter pattern, Parameter escape);
    
    /**
     * Between.
     * 
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition between(long low, long high);
    
    /**
     * Between.
     * 
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition between(double low, double high);
    
    /**
     * Between.
     * 
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition between(BigDecimal low, BigDecimal high);
    
    /**
     * Between.
     * 
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition between(String low, String high);
    
    /**
     * Between.
     * 
     * @param temporalType The temporal type
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition between(TemporalType temporalType, Date low, Date high);
    
    /**
     * Between.
     * 
     * @param temporalType The temporal type
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition between(TemporalType temporalType, Calendar low, Calendar high);
    
    /**
     * Between.
     * 
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition between(Expression low, Expression high);
    
    /**
     * Not between.
     * 
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition notBetween(long low, long high);
    
    /**
     * Not between.
     * 
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition notBetween(double low, double high);
    
    /**
     * Not between.
     * 
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition notBetween(BigDecimal low, BigDecimal high);
    
    /**
     * Not between.
     * 
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition notBetween(String low, String high);
    
    /**
     * Not between.
     * 
     * @param temporalType The temporal type
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition notBetween(TemporalType temporalType, Date low, Date high);
    
    /**
     * Not between.
     * 
     * @param temporalType The temporal type
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition notBetween(TemporalType temporalType, Calendar low, Calendar high);
    
    /**
     * Not between.
     * 
     * @param low The low
     * @param high The high
     * 
     * @return The condition
     */
    Condition notBetween(Expression low, Expression high);
}
