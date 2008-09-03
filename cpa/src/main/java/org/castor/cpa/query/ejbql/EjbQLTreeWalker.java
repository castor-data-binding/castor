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
package org.castor.cpa.query.ejbql;

import java.text.SimpleDateFormat;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.InCondition;
import org.castor.cpa.query.Literal;
import org.castor.cpa.query.Order;
import org.castor.cpa.query.OrderDirection;
import org.castor.cpa.query.Parameter;
import org.castor.cpa.query.ParseException;
import org.castor.cpa.query.QueryFactory;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.SelectQuery;
import org.castor.cpa.query.TrimSpecification;
import org.castor.cpa.query.object.literal.BooleanLiteral;
import org.castor.cpa.query.object.literal.DateLiteral;
import org.castor.cpa.query.object.literal.DoubleLiteral;
import org.castor.cpa.query.object.literal.EnumLiteral;
import org.castor.cpa.query.object.literal.LongLiteral;
import org.castor.cpa.query.object.literal.StringLiteral;
import org.castor.cpa.query.object.literal.TimeLiteral;
import org.castor.cpa.query.object.literal.TimestampLiteral;
import org.castor.cpa.query.object.parameter.NamedParameter;
import org.castor.cpa.query.object.parameter.PositionalParameter;

/**
 * EjbQL Parser TreeWalker which creates the OQ from a parsed EjbQL string.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class EjbQLTreeWalker implements EjbQLParserTreeConstants, EjbQLParserConstants {
    // --------------------------------------------------------------------------

    /** The select. */
    private SelectQuery _select;

    /** The schema. */
    private Schema _schema;

    /** The order. */
    private Order _order;

    // --------------------------------------------------------------------------

    /**
     * Instantiates a new Ejb ql tree walker.
     * 
     * @param node the node
     * @throws ParseException Exception thrown by parser when parsing an invalid query. 
     */
    public EjbQLTreeWalker(final SimpleNode node) throws ParseException {
        if (node != null) {
            if (node.id == JJTEJBQL) {
                selectStatement((SimpleNode) node.jjtGetChild(0));
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new NullPointerException();
        }
    }

    // --------------------------------------------------------------------------

    /**
     * Gets the select.
     * 
     * @return the select
     */
    public SelectQuery getSelect() {
        return _select;
    }

    /**
     * Sets the select.
     * 
     * @param select the new select
     */
    public void setSelect(final SelectQuery select) {
        _select = select;
    }

    /**
     * Select clause.
     * 
     * @param node the node
     * @throws ParseException Exception thrown by parser when parsing an invalid query.  
     */
    private void selectStatement(final SimpleNode node) throws ParseException {
        _select = QueryFactory.newSelectQuery();
        // First the schema need to be setup

        if (((SimpleNode) node.jjtGetChild(1)).id == JJTFROMCLAUSE) {
            fromClause((SimpleNode) node.jjtGetChild(1));
        }
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTSELECTCLAUSE) {
            selectClause((SimpleNode) node.jjtGetChild(0));
        }
        for (int i = 2; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTWHERECLAUSE) {
                whereClause((SimpleNode) node.jjtGetChild(i));
            }
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTORDERBYCLAUSE) {
                orderbyClause((SimpleNode) node.jjtGetChild(i));
            }
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTLIMITCLAUSE) {
                limitClause((SimpleNode) node.jjtGetChild(i));
            }
        }
    }

    /**
     * Select clause.
     * 
     * @param node the node
     */
    private void selectClause(final SimpleNode node) {
        if (node.getKind() == DISTINCT) {
            _select.setDistinct(true);
        }
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTSELECTEXPRESSION) {
            selectExpression((SimpleNode) node.jjtGetChild(0));
        }
    }

    /**
     * Select expression.
     * 
     * @param node the node
     */
    private void selectExpression(final SimpleNode node) {
        for (int i = 0; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTPATH) {
                _select.addProjection(_schema.field(identifier((SimpleNode) node.jjtGetChild(i)
                        .jjtGetChild(1))));

            }
        }
        _select.addSchema(_schema);

    }

    /**
     * From clause.
     * 
     * @param node the node
     */
    private void fromClause(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTFROMDECLARATION) {
            fromDeclaration((SimpleNode) node.jjtGetChild(0));
        }
    }

    /**
     * From declaration.
     * 
     * @param node the node
     */
    private void fromDeclaration(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTPATH
                && (((SimpleNode) node.jjtGetChild(1)).id == JJTIDENTIFIER)) {
            _schema = _select.newSchema(path((SimpleNode) node.jjtGetChild(0)),
                    identifier((SimpleNode) node.jjtGetChild(1)));
        }
    }

    /**
     * Where clause.
     * 
     * @param node the node
     * @throws ParseException Exception thrown by parser when parsing an invalid query. 
     */
    private void whereClause(final SimpleNode node) throws ParseException {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTCONDITIONALEXPRESSION) {
            Condition condition = null;
            condition = conditionalExpression((SimpleNode) node.jjtGetChild(0), condition);
            _select.setWhere(condition);
        }
    }

    /**
     * Orderby clause.
     * 
     * @param node the node
     */
    private void orderbyClause(final SimpleNode node) {
        for (int i = 0; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTORDERBYITEM) {
                orderbyItem((SimpleNode) node.jjtGetChild(i));
            }
        }
    }

    /**
     * Orderby item.
     * 
     * @param node of the SimpleNode of JJTree
     */
    private void orderbyItem(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTPATH) {
            if (_order == null) {
                if (node.getKind() == DESC) {
                    _order = _select.newOrder(_schema.field(identifier((SimpleNode) node
                            .jjtGetChild(0).jjtGetChild(1))), OrderDirection.DESCENDING);
                    _select.setOrder(_order);
                } else {
                    _order = _select.newOrder(_schema.field(identifier((SimpleNode) node
                            .jjtGetChild(0).jjtGetChild(1))));
                    _select.setOrder(_order);
                }
            } else {
                if (node.getKind() == DESC) {
                    _order.add(_schema.field(identifier((SimpleNode) node.jjtGetChild(0)
                            .jjtGetChild(1))), OrderDirection.DESCENDING);
                } else {
                    _order.add(_schema.field(identifier((SimpleNode) node.jjtGetChild(0)
                            .jjtGetChild(1))));
                }
            }
        }
    }

    /**
     * Limit clause.
     * 
     * @param node of the SimpleNode of JJTree
     */
    private void limitClause(final SimpleNode node) {
        if (node.jjtGetNumChildren() == 2) {
            if (((SimpleNode) node.jjtGetChild(0)).id == JJTPARAMETER
                    && ((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER) {
                _select.setLimit(parameter((SimpleNode) node.jjtGetChild(0)),
                        parameter((SimpleNode) node.jjtGetChild(1)));
            } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTINTEGERLITERAL
                    && ((SimpleNode) node.jjtGetChild(1)).id == JJTINTEGERLITERAL) {
                _select.setLimit(integerLiteral((SimpleNode) node.jjtGetChild(0)),
                        integerLiteral((SimpleNode) node.jjtGetChild(1)));
            }
        }
    }

    /**
     * Conditional expression.
     * 
     * @param node of the SimpleNode of JJTree
     * @param condition the condition
     * @return the condition
     * @throws ParseException Exception thrown by parser when parsing an invalid query. 
     */
    private Condition conditionalExpression(final SimpleNode node, final Condition condition)
            throws ParseException {
        Condition c = condition;
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTCONDITIONALTERM) {
            c = conditionalTerm((SimpleNode) node.jjtGetChild(0), c);
        }
        for (int i = 1; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTCONDITIONALTERM) {
                c = c.or(conditionalTerm((SimpleNode) node.jjtGetChild(i), c));
            }
        }
        return c;
    }

    /**
     * Conditional term.
     * 
     * @param node of the SimpleNode of JJTree
     * @param condition the condition
     * @return the condition
     * @throws ParseException Exception thrown by parser when parsing an invalid query. 
     */
    private Condition conditionalTerm(final SimpleNode node, final Condition condition)
            throws ParseException {
        Condition c = condition;
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTCONDITIONALFACTOR) {
            c = conditionalFactor((SimpleNode) node.jjtGetChild(0), c);
        }
        for (int i = 1; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTCONDITIONALFACTOR) {
                c = c.and(conditionalFactor((SimpleNode) node.jjtGetChild(i), c));
            }
        }
        return c;
    }

    /**
     * Conditional factor.
     * 
     * @param node of the SimpleNode of JJTree
     * @param condition the condition
     * @return the condition
     * @throws ParseException Exception thrown by parser when parsing an invalid query. 
     */
    private Condition conditionalFactor(final SimpleNode node, final Condition condition)
            throws ParseException {
        Condition c = condition;
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTCONDITIONALPRIMARY) {
            c = conditionalPrimary((SimpleNode) node.jjtGetChild(0), c);
            if (node.getKind() == NOT) {
                c.not();
            }
        }
        return c;
    }

    /**
     * Conditional primary.
     * 
     * @param node of the SimpleNode of JJTree
     * @param condition the condition
     * @return the condition
     * @throws ParseException Exception thrown by parser when parsing an invalid query. 
     */
    private Condition conditionalPrimary(final SimpleNode node, final Condition condition)
            throws ParseException {
        Condition c = condition;
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTCONDITIONALEXPRESSION) {
            return conditionalExpression((SimpleNode) node.jjtGetChild(0), c);
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTCOMPARISONEXPRESSION) {
            return comparisonExpression((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTBETWEENEXPRESSION) {
            return betweenExpression((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTLIKEEXPRESSION) {
            return likeExpression((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTINEXPRESSION) {
            return inExpression((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTNULLEXPRESSION) {
            return nullExpression((SimpleNode) node.jjtGetChild(0));
        }
        return c;
    }

    /**
     * Comparison expression.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the condition
     */
    private Condition comparisonExpression(final SimpleNode node) {
        Condition condition = null;
        switch (comparisonOperator((SimpleNode) node.jjtGetChild(1))) {
        case EQUAL:
            condition = expression((SimpleNode) node.jjtGetChild(0)).equal(
                    expression((SimpleNode) node.jjtGetChild(2)));
            break;
        case NOTEQUAL:
            condition = expression((SimpleNode) node.jjtGetChild(0)).notEqual(
                    expression((SimpleNode) node.jjtGetChild(2)));
            break;
        case GREATERTHAN:
            condition = expression((SimpleNode) node.jjtGetChild(0)).greaterThan(
                    expression((SimpleNode) node.jjtGetChild(2)));
            break;
        case LESSTHAN:
            condition = expression((SimpleNode) node.jjtGetChild(0)).lessThan(
                    expression((SimpleNode) node.jjtGetChild(2)));
            break;
        case GREATEREQUAL:
            condition = expression((SimpleNode) node.jjtGetChild(0)).greaterEqual(
                    expression((SimpleNode) node.jjtGetChild(2)));
            break;
        case LESSEQUAL:
            condition = expression((SimpleNode) node.jjtGetChild(0)).lessEqual(
                    expression((SimpleNode) node.jjtGetChild(2)));
            break;
        default:
            break;
        }

        return condition;
    }

    /**
     * Comparison operator.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the condition
     */
    private int comparisonOperator(final SimpleNode node) {
        return node.getKind();
    }

    /**
     * Between expression.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the condition
     */
    private Condition betweenExpression(final SimpleNode node) {
        if (node.getKind() == NOT) {
            return expression((SimpleNode) node.jjtGetChild(0)).notBetween(
                    expression((SimpleNode) node.jjtGetChild(1)),
                    expression((SimpleNode) node.jjtGetChild(2)));
        }
        return expression((SimpleNode) node.jjtGetChild(0)).between(
                expression((SimpleNode) node.jjtGetChild(1)),
                expression((SimpleNode) node.jjtGetChild(2)));
    }

    /**
     * Like expression.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the condition
     */
    private Condition likeExpression(final SimpleNode node) {
        Condition condition = null;
        final int three = 3;
        if (node.getKind() == NOT) {
            if (node.jjtGetNumChildren() == three) {
                if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).notLike(
                            parameter((SimpleNode) node.jjtGetChild(1)),
                            parameter((SimpleNode) node.jjtGetChild(2)));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTCHARLITERAL
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTCHARLITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).notLike(
                            stringLiteral((SimpleNode) node.jjtGetChild(1)),
                            stringLiteral((SimpleNode) node.jjtGetChild(2)).charAt(0));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTCHARLITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).notLike(
                            parameter((SimpleNode) node.jjtGetChild(1)),
                            stringLiteral((SimpleNode) node.jjtGetChild(2)).charAt(0));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTCHARLITERAL
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).notLike(
                            stringLiteral((SimpleNode) node.jjtGetChild(1)),
                            parameter((SimpleNode) node.jjtGetChild(2)));
                }
            } else {
                if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).notLike(
                            parameter((SimpleNode) node.jjtGetChild(1)));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTCHARLITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).notLike(
                            stringLiteral((SimpleNode) node.jjtGetChild(1)));
                }
            }
        } else {
            if (node.jjtGetNumChildren() == three) {
                if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).like(
                            parameter((SimpleNode) node.jjtGetChild(1)),
                            parameter((SimpleNode) node.jjtGetChild(2)));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTCHARLITERAL
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTCHARLITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).like(
                            stringLiteral((SimpleNode) node.jjtGetChild(1)),
                            stringLiteral((SimpleNode) node.jjtGetChild(2)).charAt(0));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTCHARLITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).like(
                            parameter((SimpleNode) node.jjtGetChild(1)),
                            stringLiteral((SimpleNode) node.jjtGetChild(2)).charAt(0));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTCHARLITERAL
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).like(
                            stringLiteral((SimpleNode) node.jjtGetChild(1)),
                            parameter((SimpleNode) node.jjtGetChild(2)));
                }
            } else {
                if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).like(
                            parameter((SimpleNode) node.jjtGetChild(1)));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTCHARLITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0)).like(
                            stringLiteral((SimpleNode) node.jjtGetChild(1)));
                }
            }
        }
        return condition;
    }

    /**
     * In expression.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the condition
     * @throws ParseException Exception thrown by parser when parsing an invalid query. 
     */
    private Condition inExpression(final SimpleNode node) throws ParseException {
        InCondition inCond = null;
        if (node.getKind() == NOT) {
            if (((SimpleNode) node.jjtGetChild(0)).id == JJTPATH) {
                inCond = _schema.field(identifier((SimpleNode) node.jjtGetChild(0).jjtGetChild(1)))
                        .notIn();
            }
        } else {
            if (((SimpleNode) node.jjtGetChild(0)).id == JJTPATH) {
                inCond = _schema.field(identifier((SimpleNode) node.jjtGetChild(0).jjtGetChild(1)))
                        .in();
            }
        }
        // **InItem **//
        for (int i = 1; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i).jjtGetChild(0)).id == JJTPARAMETER) {
                inCond.add(parameter((SimpleNode) node.jjtGetChild(i).jjtGetChild(0)));
            } else if (((SimpleNode) node.jjtGetChild(i).jjtGetChild(0)).id == JJTLITERAL) {
                inCond.add(literal((SimpleNode) node.jjtGetChild(i).jjtGetChild(0)));
            } else if (((SimpleNode) node.jjtGetChild(i).jjtGetChild(0)).id == JJTPATH) {
                try {
                    inCond.add(new EnumLiteral(
                            path((SimpleNode) node.jjtGetChild(i).jjtGetChild(0))));
                } catch (IllegalArgumentException ille) {
                    throw new org.castor.cpa.query.ParseException(ille);
                } catch (NullPointerException e) {
                    throw new org.castor.cpa.query.ParseException(e);
                }
            }
        }
        return inCond;
    }

    /**
     * Null expression.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the condition
     */
    private Condition nullExpression(final SimpleNode node) {
        Condition condition = null;
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTPATH) {
            if (node.getKind() == NOT) {
                condition = _schema.field(
                        identifier((SimpleNode) node.jjtGetChild(0).jjtGetChild(1))).isNotNull();
            } else {
                condition = _schema.field(
                        identifier((SimpleNode) node.jjtGetChild(0).jjtGetChild(1))).isNull();
            }
        }
        return condition;
    }

    /**
     * Expression.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the expression
     */

    private Expression expression(final SimpleNode node) {
        if (node.getKind() == PLUS) {
            return term((SimpleNode) node.jjtGetChild(0)).add(
                    term((SimpleNode) node.jjtGetChild(1)));
        } else if (node.getKind() == MINUS) {
            return term((SimpleNode) node.jjtGetChild(0)).subtract(
                    term((SimpleNode) node.jjtGetChild(0)));
        } else if (node.getKind() == CONCATE) {
            return term((SimpleNode) node.jjtGetChild(0)).concat(
                    term((SimpleNode) node.jjtGetChild(0)));
        }
        return term((SimpleNode) node.jjtGetChild(0));
    }

    /**
     * Term.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the expression
     */
    private Expression term(final SimpleNode node) {
        if (node.getKind() == MULTIPLY) {
            return factor((SimpleNode) node.jjtGetChild(0)).multiply(
                    factor((SimpleNode) node.jjtGetChild(1)));
        } else if (node.getKind() == DIVIDE) {
            return factor((SimpleNode) node.jjtGetChild(0)).divide(
                    factor((SimpleNode) node.jjtGetChild(0)));
        } else if (node.getKind() == MOD) {
            return factor((SimpleNode) node.jjtGetChild(0)).remainder(
                    factor((SimpleNode) node.jjtGetChild(0)));
        } else if (node.getKind() == REMAINDER) {
            return factor((SimpleNode) node.jjtGetChild(0)).remainder(
                    factor((SimpleNode) node.jjtGetChild(0)));
        }
        return factor((SimpleNode) node.jjtGetChild(0));
    }

    /**
     * Factor.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the expression
     */
    private Expression factor(final SimpleNode node) {
        if (node.getKind() == PLUS) {
            return primary((SimpleNode) node.jjtGetChild(0)).plus();
        } else if (node.getKind() == MINUS) {
            return primary((SimpleNode) node.jjtGetChild(0)).negate();
        }
        return primary((SimpleNode) node.jjtGetChild(0));
    }

    /**
     * Primary.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the expression
     */
    private Expression primary(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTEXPRESSION) {
            return expression((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTPATH) {
            return _schema.field(identifier((SimpleNode) node.jjtGetChild(0).jjtGetChild(1)));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTPARAMETER) {
            return parameter((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTLITERAL) {
            return literal((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTFUNCTION) {
            return function((SimpleNode) node.jjtGetChild(0));

        }
        return null;
    }

    /**
     * Function.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the function
     */
    private Expression function(final SimpleNode node) {

        switch (node.getKind()) {
        case LENGTH:
            return expression((SimpleNode) node.jjtGetChild(0)).length();
        case LOCATE:
            if (node.jjtGetNumChildren() == 2) {
                return expression((SimpleNode) node.jjtGetChild(0)).locate(
                        expression((SimpleNode) node.jjtGetChild(1)));
            }
            return expression((SimpleNode) node.jjtGetChild(0)).locate(
                    expression((SimpleNode) node.jjtGetChild(1)),
                    expression((SimpleNode) node.jjtGetChild(2)));
        case ABS:
            return expression((SimpleNode) node.jjtGetChild(0)).abs();
        case SQRT:
            return expression((SimpleNode) node.jjtGetChild(0)).sqrt();
        case MOD:
            return expression((SimpleNode) node.jjtGetChild(0)).remainder(
                    expression((SimpleNode) node.jjtGetChild(1)));
        case CONCAT:
            return expression((SimpleNode) node.jjtGetChild(0)).concat(
                    expression((SimpleNode) node.jjtGetChild(1)));
        case SUBSTRING:
            return expression((SimpleNode) node.jjtGetChild(0)).substring(
                    expression((SimpleNode) node.jjtGetChild(1)),
                    expression((SimpleNode) node.jjtGetChild(2)));
        case TRIM:
            final int three = 3;
            if (node.jjtGetNumChildren() == 1) {
                return expression((SimpleNode) node.jjtGetChild(0)).trim();
            } else if (node.jjtGetNumChildren() == 2) {
                if (((SimpleNode) node.jjtGetChild(0)).id == JJTCHARLITERAL) {
                    return expression((SimpleNode) node.jjtGetChild(1)).trim(
                            stringLiteral((SimpleNode) node.jjtGetChild(0)).charAt(0));
                } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTTRIMSPECIFICATION) {
                    return expression((SimpleNode) node.jjtGetChild(1)).trim(
                            trimSpecification((SimpleNode) node.jjtGetChild(0)));
                }
            } else if (node.jjtGetNumChildren() == three) {
                if (((SimpleNode) node.jjtGetChild(0)).id == JJTTRIMSPECIFICATION) {
                    return expression((SimpleNode) node.jjtGetChild(2)).trim(
                            trimSpecification((SimpleNode) node.jjtGetChild(0))
                            , stringLiteral((SimpleNode) node.jjtGetChild(1)).charAt(0));
                }
            }
        case LOWER:
            return expression((SimpleNode) node.jjtGetChild(0)).lower();
        case UPPER:
            return expression((SimpleNode) node.jjtGetChild(0)).upper();
        case CURRENT_DATE:
            break;
        case CURRENT_TIME:
            break;
        case CURRENT_TIMESTAMP:
            break;
        case DATE:
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            DateLiteral date = null;
            try {
                date = new DateLiteral(sdf.parse(dateLiteral((SimpleNode) node.jjtGetChild(0))));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            return date;
        case TIME:
            final int eight = 8;
            final int ten = 10;
            final int eleven = 11;
            String sTime = timeLiteral((SimpleNode) node.jjtGetChild(0));
            sdf = new SimpleDateFormat("HH:mm:ss.SSS");
            TimeLiteral time = null;
            if (sTime.length() == eight) {
                sTime += ".000";
            } else if (sTime.length() == ten) {
                sTime += "00";
            } else if (sTime.length() == eleven) {
                sTime += "0";
            }
            try {
                time = new TimeLiteral(sdf.parse(sTime));
            } catch (java.text.ParseException e) {
                e.printStackTrace(System.out);
            }
            return time;
        case TIMESTAMP:
            String sTimestamp = timestampLiteral((SimpleNode) node.jjtGetChild(0));
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            TimestampLiteral timestamp = null;
            try {
                timestamp = new TimestampLiteral(sdf.parse(sTimestamp));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            return timestamp;
        default:
            break;
        }
        return null;
    }

    /**
     * Trim Speccification.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the literal
     */
    private TrimSpecification trimSpecification(final SimpleNode node) {
        if (node.getKind() == LEADING) {
            return TrimSpecification.LEADING;
        } else if (node.getKind() == BOTH) {
            return TrimSpecification.BOTH;
        } else if (node.getKind() == TRAILING) {
            return TrimSpecification.TRAILING;
        }
        return null;
    }

    /**
     * Literal.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the literal
     */
    private Literal literal(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTBOOLEANLITERAL) {
            return new BooleanLiteral(booleanLiteral((SimpleNode) node.jjtGetChild(0)));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTINTEGERLITERAL) {
            return new LongLiteral(integerLiteral((SimpleNode) node.jjtGetChild(0)));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTFLOATLITERAL) {
            return new DoubleLiteral(floatLiteral((SimpleNode) node.jjtGetChild(0)));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTCHARLITERAL) {
            return new StringLiteral(stringLiteral((SimpleNode) node.jjtGetChild(0)));
        }
        return null;
    }

    /**
     * Parameter.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the parameter
     */
    private Parameter parameter(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTPOSITIONALPARAMETER) {
            return positionalParameter((SimpleNode) node.jjtGetChild(0));
        }
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTNAMEDPARAMETER) {
            return namedParameter((SimpleNode) node.jjtGetChild(0));
        }
        return null;
    }

    /**
     * Positional parameter.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the positional parameter
     */
    private PositionalParameter positionalParameter(final SimpleNode node) {
        return new PositionalParameter(integerLiteral((SimpleNode) node.jjtGetChild(0)));
    }

    /**
     * Named parameter.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the named parameter
     */
    private NamedParameter namedParameter(final SimpleNode node) {
        return new NamedParameter(identifier((SimpleNode) node.jjtGetChild(0)));
    }

    /**
     * Path.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the string
     */
    private String path(final SimpleNode node) {
        String path = "";
        int i = 0;
        for (; node.jjtGetNumChildren() - 1 > i; i++) {
            path += identifier((SimpleNode) node.jjtGetChild(i)) + ".";
        }
        path += identifier((SimpleNode) node.jjtGetChild(i));
        return path;
    }

    /**
     * Identifier.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the string
     */
    private String identifier(final SimpleNode node) {
        return node.getText();
    }

    /**
     * Boolean literal.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the string
     */
    private boolean booleanLiteral(final SimpleNode node) {
        return Boolean.parseBoolean(node.getText());
    }

    /**
     * Integer literal.
     * 
     * @param node
     *            the node
     * @return the int
     */
    private int integerLiteral(final SimpleNode node) {
        return Integer.parseInt(node.getText());
    }

    /**
     * Float literal.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the float
     */
    private double floatLiteral(final SimpleNode node) {
        return Double.parseDouble(node.getText());
    }

    /**
     * Char literal.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the string
     */
    private String stringLiteral(final SimpleNode node) {
        return node.getText().replace("'", "");
    }

    /**
     * Timestamp literal.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the string
     */
    private String timestampLiteral(final SimpleNode node) {
        return node.getText().replace("'", "");
    }

    /**
     * Date literal.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the string
     */
    private String dateLiteral(final SimpleNode node) {
        return node.getText().replace("'", "");
    }

    /**
     * Time literal.
     * 
     * @param node of the SimpleNode of JJTree
     * @return the string
     */
    private String timeLiteral(final SimpleNode node) {
        return node.getText().replace("'", "");
    }
}
