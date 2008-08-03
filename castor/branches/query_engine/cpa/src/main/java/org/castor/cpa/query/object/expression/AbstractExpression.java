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
import java.util.ArrayList;
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
import org.castor.cpa.query.object.condition.Equal;
import org.castor.cpa.query.object.condition.GreaterEqual;
import org.castor.cpa.query.object.condition.GreaterThan;
import org.castor.cpa.query.object.condition.LessEqual;
import org.castor.cpa.query.object.condition.LessThan;
import org.castor.cpa.query.object.condition.Like;
import org.castor.cpa.query.object.condition.NotEqual;
import org.castor.cpa.query.object.function.Abs;
import org.castor.cpa.query.object.function.Length;
import org.castor.cpa.query.object.function.Locate;
import org.castor.cpa.query.object.function.Lower;
import org.castor.cpa.query.object.function.Sqrt;
import org.castor.cpa.query.object.function.Substring;
import org.castor.cpa.query.object.function.Trim;
import org.castor.cpa.query.object.function.Upper;
import org.castor.cpa.query.object.literal.BigDecimalLiteral;
import org.castor.cpa.query.object.literal.BooleanLiteral;
import org.castor.cpa.query.object.literal.DateLiteral;
import org.castor.cpa.query.object.literal.DoubleLiteral;
import org.castor.cpa.query.object.literal.LongLiteral;
import org.castor.cpa.query.object.literal.StringLiteral;
import org.castor.cpa.query.object.literal.TimeLiteral;
import org.castor.cpa.query.object.literal.TimestampLiteral;

/**
 * Abstract base class for Expressions.
 *
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public abstract class AbstractExpression
extends AbstractQueryObject implements Expression {
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
        if (expression instanceof Add) {
            ((Add) expression).addExpression(0, this);
            return expression;
        }
        
        Add add = new Add();
        if (add.getExpressions() == null) {
             add.setExpressions(new ArrayList < Expression > ());
        }
        add.addExpression(this);
        add.addExpression(expression);
        return add;
    }
    
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
        if (expression instanceof Subtract) {
            ((Subtract) expression).addExpression(0, this);
            return expression;
        }
        
        Subtract concat = new Subtract();
        if (concat.getExpressions() == null) {
             concat.setExpressions(new ArrayList < Expression > ());
        }
        concat.addExpression(this);
        concat.addExpression(expression);
        return concat;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Expression concat(final String value) {
        return concat(new StringLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    // TODO implement
    public Expression concat(final Expression expression) {
        if (expression instanceof Concat) {
            ((Concat) expression).addExpression(0, this);
            return expression;
        }
        
        Concat concat = new Concat();
        if (concat.getExpressions() == null) {
             concat.setExpressions(new ArrayList < Expression > ());
        }
        concat.addExpression(this);
        concat.addExpression(expression);
        return concat;
    }

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
    // TODO implement
    public Expression multiply(final Expression expression) {
        if (expression instanceof Multiply) {
            ((Multiply) expression).addExpression(0, this);
            return expression;
        }
        
        Multiply multiply = new Multiply();
        if (multiply.getExpressions() == null) {
             multiply.setExpressions(new ArrayList < Expression > ());
        }
        multiply.addExpression(this);
        multiply.addExpression(expression);
        return multiply;
    }
    
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
    // TODO implement
    public Expression divide(final Expression expression) {
        if (expression instanceof Divide) {
            ((Divide) expression).addExpression(0, this);
            return expression;
        }
        
        Divide divide = new Divide();
        if (divide.getExpressions() == null) {
             divide.setExpressions(new ArrayList < Expression > ());
        }
        divide.addExpression(this);
        divide.addExpression(expression);
        return divide;  
    }     
    
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
        if (expression instanceof Remainder) {
            ((Remainder) expression).addExpression(0, this);
            return expression;
        }
        
        Remainder divide = new Remainder();
        if (divide.getExpressions() == null) {
             divide.setExpressions(new ArrayList < Expression > ());
        }
        divide.addExpression(this);
        divide.addExpression(expression);
        return divide;  
    }
    
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
        if (negate.getExpressions() == null) {
            negate.setExpressions(new ArrayList < Expression > ());
        }
        negate.addExpression(this);
        return negate;  
    }

    /**
     * {@inheritDoc}
     */
    public final Function length() {
        return new Length();
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function abs() {
        return new Abs();
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function sqrt() {
        return new Sqrt();
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function lower() {
        return new Lower();
    }

    /**
     * {@inheritDoc}
     */
    public final Function upper() {
        return new Upper();
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function locate(final String value) {
        Locate loc = new Locate();
        loc.setValue(new StringLiteral(value));
        return loc;
    }

    /**
     * {@inheritDoc}
     */
    public final Function locate(final Expression value) {
        Locate loc = new Locate();
        loc.setValue(value);
        return loc;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function locate(final String value, final int index) {
        Locate loc = new Locate();
        loc.setValue(new StringLiteral(value));
        loc.setIndex(new LongLiteral(index));
        return loc;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function locate(final String value, final Expression index) {
        Locate loc = new Locate();
        loc.setValue(new StringLiteral(value));
        loc.setIndex(index);
        return loc;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function locate(final Expression value, final int index) {
        Locate loc = new Locate();
        loc.setValue(value);
        loc.setIndex(new LongLiteral(index));
        return loc;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function locate(final Expression value, final Expression index) {
        Locate loc = new Locate();
        loc.setValue(value);
        loc.setIndex(index);
        return loc;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function substring(final int index, final int length) {
        Substring sb = new Substring();
        sb.setIndex(new LongLiteral(index));
        sb.setLength(new LongLiteral(length));
        return sb;
    }

    /**
     * {@inheritDoc}
     */
    public final Function substring(final int index, final Expression length) {
        Substring sb = new Substring();
        sb.setIndex(new LongLiteral(index));
        sb.setLength(length);
        return sb;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function substring(final Expression index, final int length) {
        Substring sb = new Substring();
        sb.setIndex(index);
        sb.setLength(new LongLiteral(length));
        return sb;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function substring(final Expression index, final Expression length) {
        Substring sb = new Substring();
        sb.setIndex(index);
        sb.setLength(length);
        return sb;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function trim() {
        Function f = new Trim();
        return f;
    }

    /**
     * {@inheritDoc}
     */
    public final Function trim(final char character) {
        Trim f = new Trim();
        f.setCharacter(new StringLiteral(Character.toString(character)));
        return f;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function trim(final Parameter character) {
        Trim f = new Trim();
        f.setCharacter(character);
        return f;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function trim(final TrimSpecification trimSpecification) {
        Trim f = new Trim();
        f.setSpecification(trimSpecification);
        return f;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function trim(final TrimSpecification trimSpecification, final char character) {
        Trim f = new Trim();
        f.setSpecification(trimSpecification);
        f.setCharacter(new StringLiteral(Character.toString(character)));
        return f;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Function trim(final TrimSpecification trimSpecification,
            final Parameter character) {
        Trim f = new Trim();
        f.setSpecification(trimSpecification);
        f.setCharacter(character);
        return f;        
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition equal(final boolean value) {
        Comparison comp = new Comparison();
        Equal e = new Equal();
        e.setExpression(this);
        comp.setOperator(e);
        comp.setCompare(new BooleanLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition equal(final long value) {
        Comparison comp = new Comparison();
        Equal e = new Equal();
        e.setExpression(this);
        comp.setOperator(e);
        comp.setCompare(new LongLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition equal(final double value) {
        Comparison comp = new Comparison();
        Equal e = new Equal();
        e.setExpression(this);
        comp.setOperator(e);
        comp.setCompare(new DoubleLiteral(value));
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition equal(final BigDecimal value) {
        Comparison comp = new Comparison();
        Equal e = new Equal();
        e.setExpression(this);
        comp.setOperator(e);
        comp.setCompare(new BigDecimalLiteral(value));
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition equal(final String value) {
        Comparison comp = new Comparison();
        Equal e = new Equal();
        e.setExpression(this);
        comp.setOperator(e);
        comp.setCompare(new StringLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition equal(final TemporalType temporalType, final Date value) {
        
        Comparison comp = new Comparison();
        Equal e = new Equal();
        e.setExpression(this);
        comp.setOperator(e);
        Expression d = null;
        if (temporalType == TemporalType.DATE) {
            d = new DateLiteral(value);
        } else if (temporalType == TemporalType.TIME) {
            d = new TimeLiteral(value);
        } else if (temporalType == TemporalType.TIMESTAMP) {
            d = new TimestampLiteral(value);
        }
        comp.setCompare(d);
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition equal(final TemporalType temporalType, final Calendar value) {
        Date date = new Date(value.getTimeInMillis());
        return equal(temporalType, date);
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition equal(final Expression value) {
        Comparison comp = new Comparison();
        Equal e = new Equal();
        e.setExpression(this);
        comp.setOperator(e);
        comp.setCompare(value);
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final boolean value) {
        Comparison comp = new Comparison();
        NotEqual ne = new NotEqual();
        ne.setExpression(this);
        comp.setOperator(ne);
        comp.setCompare(new BooleanLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final long value) {
        Comparison comp = new Comparison();
        NotEqual ne = new NotEqual();
        ne.setExpression(this);
        comp.setOperator(ne);
        comp.setCompare(new LongLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final double value) {
        Comparison comp = new Comparison();
        NotEqual ne = new NotEqual();
        ne.setExpression(this);
        comp.setOperator(ne);
        comp.setCompare(new DoubleLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final BigDecimal value) {
        Comparison comp = new Comparison();
        NotEqual ne = new NotEqual();
        ne.setExpression(this);
        comp.setOperator(ne);
        comp.setCompare(new BigDecimalLiteral(value));
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final String value) {
        Comparison comp = new Comparison();
        NotEqual ne = new NotEqual();
        ne.setExpression(this);
        comp.setOperator(ne);
        comp.setCompare(new StringLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final TemporalType temporalType, final Date value) {
        
        Comparison comp = new Comparison();
        NotEqual e = new NotEqual();
        e.setExpression(this);
        comp.setOperator(e);
        Expression d = null;
        if (temporalType == TemporalType.DATE) {
            d = new DateLiteral(value);
        } else if (temporalType == TemporalType.TIME) {
            d = new TimeLiteral(value);
        } else if (temporalType == TemporalType.TIMESTAMP) {
            d = new TimestampLiteral(value);
        }
        comp.setCompare(d);
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final TemporalType temporalType, final Calendar value) {
        Date date = new Date(value.getTimeInMillis());
        return notEqual(temporalType, date);
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notEqual(final Expression value) {
        Comparison comp = new Comparison();
        NotEqual ne = new NotEqual();
        ne.setExpression(this);
        comp.setOperator(ne);
        comp.setCompare(value);
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final long value) {
        Comparison comp = new Comparison();
        LessThan lt = new LessThan();
        lt.setExpression(this);
        comp.setOperator(lt);
        comp.setCompare(new LongLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final double value) {
        Comparison comp = new Comparison();
        LessThan lt = new LessThan();
        lt.setExpression(this);
        comp.setOperator(lt);
        comp.setCompare(new DoubleLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final BigDecimal value) {
        Comparison comp = new Comparison();
        LessThan lt = new LessThan();
        lt.setExpression(this);
        comp.setOperator(lt);
        comp.setCompare(new BigDecimalLiteral(value));
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final String value) {
        Comparison comp = new Comparison();
        LessThan lt = new LessThan();
        lt.setExpression(this);
        comp.setOperator(lt);
        comp.setCompare(new StringLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final TemporalType temporalType, final Date value) {
        
        Comparison comp = new Comparison();
        LessThan e = new LessThan();
        e.setExpression(this);
        comp.setOperator(e);
        Expression d = null;
        if (temporalType == TemporalType.DATE) {
            d = new DateLiteral(value);
        } else if (temporalType == TemporalType.TIME) {
            d = new TimeLiteral(value);
        } else if (temporalType == TemporalType.TIMESTAMP) {
            d = new TimestampLiteral(value);
        }
        comp.setCompare(d);
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final TemporalType temporalType, final Calendar value) {
        Date date = new Date(value.getTimeInMillis());
        return lessThan(temporalType, date);
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessThan(final Expression value) {
        Comparison comp = new Comparison();
        LessThan lt = new LessThan();
        lt.setExpression(this);
        comp.setOperator(lt);
        comp.setCompare(value);
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final long value) {
        Comparison comp = new Comparison();
        LessEqual le = new LessEqual();
        le.setExpression(this);
        comp.setOperator(le);
        comp.setCompare(new LongLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final double value) {
        Comparison comp = new Comparison();
        LessEqual le = new LessEqual();
        le.setExpression(this);
        comp.setOperator(le);
        comp.setCompare(new DoubleLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final BigDecimal value) {
        Comparison comp = new Comparison();
        LessEqual le = new LessEqual();
        le.setExpression(this);
        comp.setOperator(le);
        comp.setCompare(new BigDecimalLiteral(value));
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final String value) {
        Comparison comp = new Comparison();
        LessEqual le = new LessEqual();
        le.setExpression(this);
        comp.setOperator(le);
        comp.setCompare(new StringLiteral(value));
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final TemporalType temporalType, final Date value) {
        
        Comparison comp = new Comparison();
        LessEqual e = new LessEqual();
        e.setExpression(this);
        comp.setOperator(e);
        Expression d = null;
        if (temporalType == TemporalType.DATE) {
            d = new DateLiteral(value);
        } else if (temporalType == TemporalType.TIME) {
            d = new TimeLiteral(value);
        } else if (temporalType == TemporalType.TIMESTAMP) {
            d = new TimestampLiteral(value);
        }
        comp.setCompare(d);
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final TemporalType temporalType, final Calendar value) {
        Date date = new Date(value.getTimeInMillis());
        return lessEqual(temporalType, date);
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition lessEqual(final Expression value) {
        Comparison comp = new Comparison();
        LessEqual le = new LessEqual();
        le.setExpression(this);
        comp.setOperator(le);
        comp.setCompare(value);
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final long value) {
        Comparison comp = new Comparison();
        GreaterEqual ge = new GreaterEqual();
        ge.setExpression(this);
        comp.setOperator(ge);
        comp.setCompare(new LongLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final double value) {
        Comparison comp = new Comparison();
        GreaterEqual ge = new GreaterEqual();
        ge.setExpression(this);
        comp.setOperator(ge);
        comp.setCompare(new DoubleLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final BigDecimal value) {
        Comparison comp = new Comparison();
        GreaterEqual ge = new GreaterEqual();
        ge.setExpression(this);
        comp.setOperator(ge);
        comp.setCompare(new BigDecimalLiteral(value));
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final String value) {
        Comparison comp = new Comparison();
        GreaterEqual ge = new GreaterEqual();
        ge.setExpression(this);
        comp.setOperator(ge);
        comp.setCompare(new StringLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final TemporalType temporalType, final Date value) {
        
        Comparison comp = new Comparison();
        GreaterEqual e = new GreaterEqual();
        e.setExpression(this);
        comp.setOperator(e);
        Expression d = null;
        if (temporalType == TemporalType.DATE) {
            d = new DateLiteral(value);
        } else if (temporalType == TemporalType.TIME) {
            d = new TimeLiteral(value);
        } else if (temporalType == TemporalType.TIMESTAMP) {
            d = new TimestampLiteral(value);
        }
        comp.setCompare(d);
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final TemporalType temporalType, final Calendar value) {
        Date date = new Date(value.getTimeInMillis());
        return greaterEqual(temporalType, date);
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition greaterEqual(final Expression value) {
        Comparison comp = new Comparison();
        GreaterEqual ge = new GreaterEqual();
        ge.setExpression(this);
        comp.setOperator(ge);
        comp.setCompare(value);
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final long value) {
        Comparison comp = new Comparison();
        GreaterThan gt = new GreaterThan();
        gt.setExpression(this);
        comp.setOperator(gt);
        comp.setCompare(new LongLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final double value) {
        Comparison comp = new Comparison();
        GreaterThan gt = new GreaterThan();
        gt.setExpression(this);
        comp.setOperator(gt);
        comp.setCompare(new DoubleLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final BigDecimal value) {
        Comparison comp = new Comparison();
        GreaterThan gt = new GreaterThan();
        gt.setExpression(this);
        comp.setOperator(gt);
        comp.setCompare(new BigDecimalLiteral(value));
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final String value) {
        Comparison comp = new Comparison();
        GreaterThan gt = new GreaterThan();
        gt.setExpression(this);
        comp.setOperator(gt);
        comp.setCompare(new StringLiteral(value));
        return comp;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final TemporalType temporalType, final Date value) {
        
        Comparison comp = new Comparison();
        GreaterThan e = new GreaterThan();
        e.setExpression(this);
        comp.setOperator(e);
        Expression d = null;
        if (temporalType == TemporalType.DATE) {
            d = new DateLiteral(value);
        } else if (temporalType == TemporalType.TIME) {
            d = new TimeLiteral(value);
        } else if (temporalType == TemporalType.TIMESTAMP) {
            d = new TimestampLiteral(value);
        }
        comp.setCompare(d);
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final TemporalType temporalType, final Calendar value) {
        Date date = new Date(value.getTimeInMillis());
        return greaterThan(temporalType, date);
    }

    /**
     * {@inheritDoc}
     */
    public final Condition greaterThan(final Expression value) {
        Comparison comp = new Comparison();
        GreaterThan gt = new GreaterThan();
        gt.setExpression(this);
        comp.setOperator(gt);
        comp.setCompare(value);
        return comp;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition like(final String pattern) {
        Like l = new Like();
        l.setPattern(new StringLiteral(pattern));
        l.setExpression(this);
        return l;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition like(final String pattern, final char escape) {
        Like l = new Like();
        l.setPattern(new StringLiteral(pattern));
        l.setEscape(new StringLiteral(Character.toString(escape)));
        l.setExpression(this);
        return l;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition like(final String pattern, final Parameter escape) {
        Like l = new Like();
        l.setPattern(new StringLiteral(pattern));
        l.setEscape(escape);
        l.setExpression(this);
        return l;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition like(final Parameter pattern) {
        Like l = new Like();
        l.setPattern(pattern);
        l.setExpression(this);
        return l;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition like(final Parameter pattern, final char escape) {
        Like l = new Like();
        l.setPattern(pattern);
        l.setEscape(new StringLiteral(Character.toString(escape)));
        l.setExpression(this);
        return l;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition like(final Parameter pattern, final Parameter escape) {
        Like l = new Like();
        l.setPattern(pattern);
        l.setEscape(escape);
        l.setExpression(this);
        return l;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final String pattern) {
        Like l = new Like();
        l.setPattern(new StringLiteral(pattern));
        l.setExpression(this);
        l.setNot(true);
        return l;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final String pattern, final char escape) {
        Like l = new Like();
        l.setPattern(new StringLiteral(pattern));
        l.setEscape(new StringLiteral(Character.toString(escape)));
        l.setExpression(this);
        l.setNot(true);
        return l;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final String pattern, final Parameter escape) {
        Like l = new Like();
        l.setPattern(new StringLiteral(pattern));
        l.setEscape(escape);
        l.setExpression(this);
        l.setNot(true);
        return l;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final Parameter pattern) {
        Like l = new Like();
        l.setPattern(pattern);
        l.setExpression(this);
        l.setNot(true);
        return l;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final Parameter pattern, final char escape) {
        Like l = new Like();
        l.setPattern(pattern);
        l.setEscape(new StringLiteral(Character.toString(escape)));
        l.setExpression(this);
        l.setNot(true);
        return l;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notLike(final Parameter pattern, final Parameter escape) {
        Like l = new Like();
        l.setPattern(pattern);
        l.setEscape(escape);
        l.setExpression(this);
        l.setNot(true);
        return l;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition between(final long low, final long high) {
        Between b = new Between();
        b.setLow(new LongLiteral(low));
        b.setHigh(new LongLiteral(high));
        b.setExpression(this);
        return b;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition between(final double low, final double high) {
        Between b = new Between();
        b.setLow(new DoubleLiteral(low));
        b.setHigh(new DoubleLiteral(high));
        b.setExpression(this);
        return b;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition between(final BigDecimal low, final BigDecimal high) {
        Between b = new Between();
        b.setLow(new BigDecimalLiteral(low));
        b.setHigh(new BigDecimalLiteral(high));
        b.setExpression(this);
        return b;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition between(final String low, final String high) {
        Between b = new Between();
        b.setLow(new StringLiteral(low));
        b.setHigh(new StringLiteral(high));
        b.setExpression(this);
        return b;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition between(final TemporalType temporalType,
            final Date low, final Date high) {
        
        Between bw = new Between();
        bw.setExpression(this);
        Expression l = null;
        Expression h = null;
        if (temporalType == TemporalType.DATE) {
            l = new DateLiteral(low);
            h = new DateLiteral(high);
        } else if (temporalType == TemporalType.TIME) {
            l = new TimeLiteral(low);
            h = new TimeLiteral(high);
        } else if (temporalType == TemporalType.TIMESTAMP) {
            l = new TimestampLiteral(low);
            h = new TimestampLiteral(high);
        }
        bw.setLow(l);
        bw.setHigh(h);
        return bw;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition between(final TemporalType temporalType,
            final Calendar low, final Calendar high) {
        Date l = new Date(low.getTimeInMillis());
        Date h = new Date(high.getTimeInMillis());
        return between(temporalType, l, h);
    }

    /**
     * {@inheritDoc}
     */
    public final Condition between(final Expression low, final Expression high) {
        Between b = new Between();
        b.setLow(low);
        b.setHigh(high);
        b.setExpression(this);
        return b;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final long low, final long high) {
        Between b = new Between();
        b.setLow(new LongLiteral(low));
        b.setHigh(new LongLiteral(high));
        b.setExpression(this);
        b.setNot(true);
        return b;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final double low, final double high) {
        Between b = new Between();
        b.setLow(new DoubleLiteral(low));
        b.setHigh(new DoubleLiteral(high));
        b.setExpression(this);
        b.setNot(true);
        return b;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final BigDecimal low, final BigDecimal high) {
        Between b = new Between();
        b.setLow(new BigDecimalLiteral(low));
        b.setHigh(new BigDecimalLiteral(high));
        b.setExpression(this);
        b.setNot(true);
        return b;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final String low, final String high) {
        Between b = new Between();
        b.setLow(new StringLiteral(low));
        b.setHigh(new StringLiteral(high));
        b.setExpression(this);
        b.setNot(true);
        return b;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final TemporalType temporalType,
            final Date low, final Date high) {
        
        Between bw = new Between();
        bw.setExpression(this);
        Expression l = null;
        Expression h = null;
        if (temporalType == TemporalType.DATE) {
            l = new DateLiteral(low);
            h = new DateLiteral(high);
        } else if (temporalType == TemporalType.TIME) {
            l = new TimeLiteral(low);
            h = new TimeLiteral(high);
        } else if (temporalType == TemporalType.TIMESTAMP) {
            l = new TimestampLiteral(low);
            h = new TimestampLiteral(high);
        }
        bw.setLow(l);
        bw.setHigh(h);
        bw.setNot(true);
        return bw;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final TemporalType temporalType,
            final Calendar low, final Calendar high) {
        Date l = new Date(low.getTimeInMillis());
        Date h = new Date(high.getTimeInMillis());
        return notBetween(temporalType, l, h);
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition notBetween(final Expression low, final Expression high) {
        Between b = new Between();
        b.setLow(low);
        b.setHigh(high);
        b.setExpression(this);
        b.setNot(true);
        return b;
    }
}
