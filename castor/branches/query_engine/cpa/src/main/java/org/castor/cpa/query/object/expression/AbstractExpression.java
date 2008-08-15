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
package org.castor.cpa.query.object.expression;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Function;
import org.castor.cpa.query.Parameter;
import org.castor.cpa.query.TemporalType;
import org.castor.cpa.query.TrimSpecification;
import org.castor.cpa.query.object.AbstractQueryObject;
import org.castor.cpa.query.object.condition.Between;
import org.castor.cpa.query.object.condition.Comparison;
import org.castor.cpa.query.object.condition.Like;
import org.castor.cpa.query.object.function.Abs;
import org.castor.cpa.query.object.function.Length;
import org.castor.cpa.query.object.function.Locate;
import org.castor.cpa.query.object.function.Lower;
import org.castor.cpa.query.object.function.Sqrt;
import org.castor.cpa.query.object.function.Substring;
import org.castor.cpa.query.object.function.Trim;
import org.castor.cpa.query.object.function.Upper;
import org.castor.cpa.query.object.literal.AbstractTemporalLiteral;
import org.castor.cpa.query.object.literal.BigDecimalLiteral;
import org.castor.cpa.query.object.literal.BooleanLiteral;
import org.castor.cpa.query.object.literal.DoubleLiteral;
import org.castor.cpa.query.object.literal.LongLiteral;
import org.castor.cpa.query.object.literal.StringLiteral;

/**
 * Abstract base class for Expressions.
 *
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public abstract class AbstractExpression extends AbstractQueryObject implements Expression {
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Expression add(final long value) {
        return add(new LongLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Expression add(final double value) {
        return add(new DoubleLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Expression add(final BigDecimal value) {
        return add(new BigDecimalLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public Expression add(final Expression expression) {
        Add add = new Add();
        add.addExpression(this);
        add.addExpression(expression);
        return add;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Expression subtract(final long value) {
        return subtract(new LongLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Expression subtract(final double value) {
        return subtract(new DoubleLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Expression subtract(final BigDecimal value) {
        return subtract(new BigDecimalLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public Expression subtract(final Expression expression) {
        Subtract subtract = new Subtract();
        subtract.addExpression(this);
        subtract.addExpression(expression);
        return subtract;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Expression concat(final String value) {
        return concat(new StringLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public Expression concat(final Expression expression) {
        Concat concat = new Concat();
        concat.addExpression(this);
        concat.addExpression(expression);
        return concat;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Expression multiply(final long value) {
        return multiply(new LongLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Expression multiply(final double value) {
        return multiply(new DoubleLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Expression multiply(final BigDecimal value) {
        return multiply(new BigDecimalLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public Expression multiply(final Expression expression) {
        Multiply multiply = new Multiply();
        multiply.addExpression(this);
        multiply.addExpression(expression);
        return multiply;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Expression divide(final long value) {
        return divide(new LongLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Expression divide(final double value) {
        return divide(new DoubleLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Expression divide(final BigDecimal value) {
        return divide(new BigDecimalLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public Expression divide(final Expression expression) {
        Divide divide = new Divide();
        divide.addExpression(this);
        divide.addExpression(expression);
        return divide;  
    }     
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Expression remainder(final long value) {
        return remainder(new LongLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Expression remainder(final double value) {
        return remainder(new DoubleLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Expression remainder(final BigDecimal value) {
        return remainder(new BigDecimalLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public Expression remainder(final Expression expression) {
        Remainder remainder = new Remainder();
        remainder.addExpression(this);
        remainder.addExpression(expression);
        return remainder;  
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Expression plus() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Expression negate() {
        Negate negate = new Negate();
        negate.setExpression(this);
        return negate;  
    }

    /**
     * {@inheritDoc}
     */
    public final Function length() {
        Length length = new Length();
        length.setString(this);
        return length;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function abs() {
        Abs abs = new Abs();
        abs.setNumber(this);
        return abs;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function sqrt() {
        Sqrt sqrt = new Sqrt();
        sqrt.setNumber(this);
        return sqrt;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function lower() {
        Lower lower = new Lower();
        lower.setString(this);
        return lower;
    }

    /**
     * {@inheritDoc}
     */
    public final Function upper() {
        Upper upper = new Upper();
        upper.setString(this);
        return upper;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Function locate(final String value) {
        return locate(new StringLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Function locate(final Expression value) {
        Locate locate = new Locate();
        locate.setString(this);
        locate.setValue(value);
        return locate;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function locate(final String value, final int index) {
        return locate(new StringLiteral(value), new LongLiteral(index));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function locate(final String value, final Expression index) {
        return locate(new StringLiteral(value), index);
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function locate(final Expression value, final int index) {
        return locate(value, new LongLiteral(index));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function locate(final Expression value, final Expression index) {
        Locate locate = new Locate();
        locate.setString(this);
        locate.setValue(value);
        locate.setIndex(index);
        return locate;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Function substring(final int index, final int length) {
        return substring(new LongLiteral(index), new LongLiteral(length));
    }

    /**
     * {@inheritDoc}
     */
    public final Function substring(final int index, final Expression length) {
        return substring(new LongLiteral(index), length);
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function substring(final Expression index, final int length) {
        return substring(index, new LongLiteral(length));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function substring(final Expression index, final Expression length) {
        Substring substring = new Substring();
        substring.setString(this);
        substring.setIndex(index);
        substring.setLength(length);
        return substring;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Function trim() {
        Trim trim = new Trim();
        trim.setString(this);
        return trim;
    }

    /**
     * {@inheritDoc}
     */
    public final Function trim(final char character) {
        Trim trim = new Trim();
        trim.setString(this);
        trim.setCharacter(new StringLiteral(Character.toString(character)));
        return trim;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function trim(final Parameter character) {
        Trim trim = new Trim();
        trim.setString(this);
        trim.setCharacter(character);
        return trim;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function trim(final TrimSpecification trimSpecification) {
        Trim trim = new Trim();
        trim.setString(this);
        trim.setSpecification(trimSpecification);
        return trim;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function trim(final TrimSpecification trimSpecification, final char character) {
        Trim trim = new Trim();
        trim.setString(this);
        trim.setSpecification(trimSpecification);
        trim.setCharacter(new StringLiteral(Character.toString(character)));
        return trim;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function trim(final TrimSpecification trimSpecification,
            final Parameter character) {
        Trim trim = new Trim();
        trim.setString(this);
        trim.setSpecification(trimSpecification);
        trim.setCharacter(character);
        return trim;        
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Condition equal(final boolean value) {
        return equal(new BooleanLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition equal(final long value) {
        return equal(new LongLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition equal(final double value) {
        return equal(new DoubleLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition equal(final BigDecimal value) {
        return equal(new BigDecimalLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition equal(final String value) {
        return equal(new StringLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition equal(final TemporalType temporalType, final Date value) {
        return equal(AbstractTemporalLiteral.createInstance(temporalType, value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition equal(final TemporalType temporalType, final Calendar value) {
        return equal(AbstractTemporalLiteral.createInstance(temporalType, value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition equal(final Expression value) {
        Comparison comp = new Comparison(Comparison.EQUAL);
        comp.setLeftSide(this);
        comp.setRightSide(value);
        return comp;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final boolean value) {
        return notEqual(new BooleanLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final long value) {
        return notEqual(new LongLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final double value) {
        return notEqual(new DoubleLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final BigDecimal value) {
        return notEqual(new BigDecimalLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final String value) {
        return notEqual(new StringLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final TemporalType temporalType, final Date value) {
        return notEqual(AbstractTemporalLiteral.createInstance(temporalType, value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final TemporalType temporalType, final Calendar value) {
        return notEqual(AbstractTemporalLiteral.createInstance(temporalType, value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final Expression value) {
        Comparison comp = new Comparison(Comparison.NOT_EQUAL);
        comp.setLeftSide(this);
        comp.setRightSide(value);
        return comp;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final long value) {
        return lessThan(new LongLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final double value) {
        return lessThan(new DoubleLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final BigDecimal value) {
        return lessThan(new BigDecimalLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final String value) {
        return lessThan(new StringLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final TemporalType temporalType, final Date value) {
        return lessThan(AbstractTemporalLiteral.createInstance(temporalType, value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final TemporalType temporalType, final Calendar value) {
        return lessThan(AbstractTemporalLiteral.createInstance(temporalType, value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final Expression value) {
        Comparison comp = new Comparison(Comparison.LESS_THAN);
        comp.setLeftSide(this);
        comp.setRightSide(value);
        return comp;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final long value) {
        return lessEqual(new LongLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final double value) {
        return lessEqual(new DoubleLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final BigDecimal value) {
        return lessEqual(new BigDecimalLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final String value) {
        return lessEqual(new StringLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final TemporalType temporalType, final Date value) {
        return lessEqual(AbstractTemporalLiteral.createInstance(temporalType, value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final TemporalType temporalType, final Calendar value) {
        return lessEqual(AbstractTemporalLiteral.createInstance(temporalType, value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final Expression value) {
        Comparison comp = new Comparison(Comparison.LESS_EQUAL);
        comp.setLeftSide(this);
        comp.setRightSide(value);
        return comp;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final long value) {
        return greaterEqual(new LongLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final double value) {
        return greaterEqual(new DoubleLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final BigDecimal value) {
        return greaterEqual(new BigDecimalLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final String value) {
        return greaterEqual(new StringLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final TemporalType temporalType, final Date value) {
        return greaterEqual(AbstractTemporalLiteral.createInstance(temporalType, value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final TemporalType temporalType, final Calendar value) {
        return greaterEqual(AbstractTemporalLiteral.createInstance(temporalType, value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final Expression value) {
        Comparison comp = new Comparison(Comparison.GREATER_EQUAL);
        comp.setLeftSide(this);
        comp.setRightSide(value);
        return comp;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final long value) {
        return greaterThan(new LongLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final double value) {
        return greaterThan(new DoubleLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final BigDecimal value) {
        return greaterThan(new BigDecimalLiteral(value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final String value) {
        return greaterThan(new StringLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final TemporalType temporalType, final Date value) {
        return greaterThan(AbstractTemporalLiteral.createInstance(temporalType, value));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final TemporalType temporalType, final Calendar value) {
        return greaterThan(AbstractTemporalLiteral.createInstance(temporalType, value));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final Expression value) {
        Comparison comp = new Comparison(Comparison.GREATER_THAN);
        comp.setLeftSide(this);
        comp.setRightSide(value);
        return comp;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Condition like(final String pattern) {
        Like like = new Like();
        like.setNot(false);
        like.setExpression(this);
        like.setPattern(new StringLiteral(pattern));
        return like;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition like(final Parameter pattern) {
        Like like = new Like();
        like.setNot(false);
        like.setExpression(this);
        like.setPattern(pattern);
        return like;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition like(final String pattern, final char escape) {
        Like like = new Like();
        like.setNot(false);
        like.setExpression(this);
        like.setPattern(new StringLiteral(pattern));
        like.setEscape(new StringLiteral(Character.toString(escape)));
        return like;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition like(final String pattern, final Parameter escape) {
        Like like = new Like();
        like.setNot(false);
        like.setExpression(this);
        like.setPattern(new StringLiteral(pattern));
        like.setEscape(escape);
        return like;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition like(final Parameter pattern, final char escape) {
        Like like = new Like();
        like.setNot(false);
        like.setExpression(this);
        like.setPattern(pattern);
        like.setEscape(new StringLiteral(Character.toString(escape)));
        return like;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition like(final Parameter pattern, final Parameter escape) {
        Like like = new Like();
        like.setNot(false);
        like.setExpression(this);
        like.setPattern(pattern);
        like.setEscape(escape);
        return like;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final String pattern) {
        Like like = new Like();
        like.setNot(true);
        like.setExpression(this);
        like.setPattern(new StringLiteral(pattern));
        return like;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final String pattern, final char escape) {
        Like like = new Like();
        like.setNot(true);
        like.setExpression(this);
        like.setPattern(new StringLiteral(pattern));
        like.setEscape(new StringLiteral(Character.toString(escape)));
        return like;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final String pattern, final Parameter escape) {
        Like like = new Like();
        like.setNot(true);
        like.setExpression(this);
        like.setPattern(new StringLiteral(pattern));
        like.setEscape(escape);
        return like;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final Parameter pattern) {
        Like like = new Like();
        like.setNot(true);
        like.setExpression(this);
        like.setPattern(pattern);
        return like;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final Parameter pattern, final char escape) {
        Like like = new Like();
        like.setNot(true);
        like.setExpression(this);
        like.setPattern(pattern);
        like.setEscape(new StringLiteral(Character.toString(escape)));
        return like;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final Parameter pattern, final Parameter escape) {
        Like like = new Like();
        like.setNot(true);
        like.setExpression(this);
        like.setPattern(pattern);
        like.setEscape(escape);
        return like;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Condition between(final long low, final long high) {
        return between(new LongLiteral(low), new LongLiteral(high));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition between(final double low, final double high) {
        return between(new DoubleLiteral(low), new DoubleLiteral(high));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition between(final BigDecimal low, final BigDecimal high) {
        return between(new BigDecimalLiteral(low), new BigDecimalLiteral(high));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition between(final String low, final String high) {
        return between(new StringLiteral(low), new StringLiteral(high));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition between(final TemporalType temporalType,
            final Date low, final Date high) {
        return between(AbstractTemporalLiteral.createInstance(temporalType, low),
                AbstractTemporalLiteral.createInstance(temporalType, high));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition between(final TemporalType temporalType,
            final Calendar low, final Calendar high) {
        return between(AbstractTemporalLiteral.createInstance(temporalType, low),
                AbstractTemporalLiteral.createInstance(temporalType, high));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition between(final Expression low, final Expression high) {
        Between between = new Between();
        between.setNot(false);
        between.setExpression(this);
        between.setLow(low);
        between.setHigh(high);
        return between;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final long low, final long high) {
        return notBetween(new LongLiteral(low), new LongLiteral(high));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final double low, final double high) {
        return notBetween(new DoubleLiteral(low), new DoubleLiteral(high));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final BigDecimal low, final BigDecimal high) {
        return notBetween(new BigDecimalLiteral(low), new BigDecimalLiteral(high));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final String low, final String high) {
        return notBetween(new StringLiteral(low), new StringLiteral(high));
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final TemporalType temporalType,
            final Date low, final Date high) {
        return notBetween(AbstractTemporalLiteral.createInstance(temporalType, low),
                AbstractTemporalLiteral.createInstance(temporalType, high));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final TemporalType temporalType,
            final Calendar low, final Calendar high) {
        return notBetween(AbstractTemporalLiteral.createInstance(temporalType, low),
                AbstractTemporalLiteral.createInstance(temporalType, high));
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final Expression low, final Expression high) {
        Between between = new Between();
        between.setNot(true);
        between.setExpression(this);
        between.setLow(low);
        between.setHigh(high);
        return between;
    }

    //--------------------------------------------------------------------------
}
