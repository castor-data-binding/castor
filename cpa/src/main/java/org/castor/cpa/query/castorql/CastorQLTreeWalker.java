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
package org.castor.cpa.query.castorql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Function;
import org.castor.cpa.query.InCondition;
import org.castor.cpa.query.Literal;
import org.castor.cpa.query.Order;
import org.castor.cpa.query.OrderDirection;
import org.castor.cpa.query.Parameter;
import org.castor.cpa.query.QueryFactory;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.SelectQuery;
import org.castor.cpa.query.object.function.CustomFunction;
import org.castor.cpa.query.object.literal.BooleanLiteral;
import org.castor.cpa.query.object.literal.DateLiteral;
import org.castor.cpa.query.object.literal.DoubleLiteral;
import org.castor.cpa.query.object.literal.LongLiteral;
import org.castor.cpa.query.object.literal.StringLiteral;
import org.castor.cpa.query.object.literal.TimeLiteral;
import org.castor.cpa.query.object.literal.TimestampLiteral;
import org.castor.cpa.query.object.parameter.NamedParameter;
import org.castor.cpa.query.object.parameter.PositionalParameter;

/**
 * CastorQL Parser TreeWalker which creates the OQ from a parsed CastorQL
 * string.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr
 *          2006) $
 * @since 1.3
 */
// @SuppressWarnings("unused")
public final class CastorQLTreeWalker implements CastorQLParserTreeConstants,
        CastorQLParserConstants {

    /** The select. */
    private SelectQuery _select;

    /** The schema. */
    private Schema      _schema;

    /** The order. */
    private Order       _order;

    /** The node. */
    private SimpleNode  _node;

    /**
     * Instantiates a new castor ql tree walker.
     * 
     * @param node
     *                the node
     */
    public CastorQLTreeWalker(final SimpleNode node) {
        _node = node;
        if (node.id == JJTSELECT_STATEMENT) {
            selectStatement(_node);
        } else { // TODO something
        }
    }

    /**
     * Select clause.
     * 
     * @param node
     *                the node
     */
    private void selectStatement(final SimpleNode node) {
        _select = QueryFactory.newSelectQuery();
        // First the schema need to be setup

        if (((SimpleNode) node.jjtGetChild(1)).id == JJTFROM_CLAUSE) {
            fromClause((SimpleNode) node.jjtGetChild(1));
        }
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTSELECT_CLAUSE) {
            selectClause((SimpleNode) node.jjtGetChild(0));
        }
        for (int i = 2; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTWHERE_CLAUSE) {
                whereClause((SimpleNode) node.jjtGetChild(i));
            }
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTORDERBY_CLAUSE) {
                orderbyClause((SimpleNode) node.jjtGetChild(i));
            }
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTLIMIT_CLAUSE) {
                limitClause((SimpleNode) node.jjtGetChild(i));
            }
        }
    }

    /**
     * Select clause.
     * 
     * @param node
     *                the node
     */
    private void selectClause(final SimpleNode node) {
        if (node.getKind() == DISTINCT) {
            _select.setDistinct(true);
        }
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTSELECT_EXPRESSION) {
            selectExpression((SimpleNode) node.jjtGetChild(0));
        }
    }

    /**
     * Select expression.
     * 
     * @param node
     *                the node
     */
    private void selectExpression(final SimpleNode node) {
        for (int i = 0; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTPATH) {
                _select.addProjection(_schema
                        .field(identifier((SimpleNode) node.jjtGetChild(i)
                                .jjtGetChild(1))));

            }
        }
        _select.addSchema(_schema);

    }

    /**
     * From clause.
     * 
     * @param node
     *                the node
     */
    private void fromClause(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTFROM_DECLARATION) {
            fromDeclaration((SimpleNode) node.jjtGetChild(0));
        }
    }

    /**
     * From declaration.
     * 
     * @param node
     *                the node
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
     * @param node
     *                the node
     */
    private void whereClause(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTCONDITIONAL_EXPRESSION) {
            Condition condition = null;
            condition = conditionalExpression((SimpleNode) node.jjtGetChild(0),
                    condition);
            _select.setWhere(condition);
        }
    }

    /**
     * Orderby clause.
     * 
     * @param node
     *                the node
     */
    private void orderbyClause(final SimpleNode node) {
        for (int i = 0; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTORDERBY_ITEM) {
                orderbyItem((SimpleNode) node.jjtGetChild(i));
            }
        }
    }

    /**
     * Orderby item.
     * 
     * @param node
     *                the node
     */
    private void orderbyItem(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTPATH) {
            if (_order == null) {
                if (node.getKind() == DESC) {
                    _order = _select.newOrder(_schema
                            .field(identifier((SimpleNode) node.jjtGetChild(0)
                                    .jjtGetChild(1))),
                            OrderDirection.DESCENDING);
                    _select.setOrder(_order);
                } else {
                    _order = _select.newOrder(_schema
                            .field(identifier((SimpleNode) node.jjtGetChild(0)
                                    .jjtGetChild(1))));
                    _select.setOrder(_order);
                }
            } else {
                if (node.getKind() == DESC) {
                    _order.add(_schema.field(identifier((SimpleNode) node
                            .jjtGetChild(0).jjtGetChild(1))),
                            OrderDirection.DESCENDING);
                } else {
                    _order.add(_schema.field(identifier((SimpleNode) node
                            .jjtGetChild(0).jjtGetChild(1))));
                }
            }
        }
    }

    /**
     * Limit clause.
     * 
     * @param node
     *                the node
     */
    private void limitClause(final SimpleNode node) {
        if (node.jjtGetNumChildren() == 2) {
            if (((SimpleNode) node.jjtGetChild(0)).id == JJTPARAMETER
                    && ((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER) {
                _select.setLimit(parameter((SimpleNode) node.jjtGetChild(0)),
                        parameter((SimpleNode) node.jjtGetChild(1)));
            } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTINTEGER_LITERAL
                    && ((SimpleNode) node.jjtGetChild(1)).id == JJTINTEGER_LITERAL) {
                _select.setLimit(integerLiteral((SimpleNode) node
                        .jjtGetChild(0)), integerLiteral((SimpleNode) node
                        .jjtGetChild(1)));
            }
        }
    }

    /**
     * Conditional expression.
     * 
     * @param node
     *                the node
     * @param condition
     *                the condition
     * @return the condition
     */
    private Condition conditionalExpression(final SimpleNode node,
            Condition condition) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTCONDITIONAL_TERM) {

            condition = conditionalTerm((SimpleNode) node.jjtGetChild(0),
                    condition);

        }
        for (int i = 1; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTCONDITIONAL_TERM) {
                condition = condition.or(conditionalTerm((SimpleNode) node
                        .jjtGetChild(i), condition));
            }
        }
        return condition;
    }

    /**
     * Conditional term.
     * 
     * @param node
     *                the node
     * @param condition
     *                the condition
     * @return the condition
     */
    private Condition conditionalTerm(final SimpleNode node, Condition condition) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTCONDITIONAL_FACTOR) {
            condition = conditionalFactor((SimpleNode) node.jjtGetChild(0),
                    condition);
        }
        for (int i = 1; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTCONDITIONAL_FACTOR) {
                condition = condition.and(conditionalFactor((SimpleNode) node
                        .jjtGetChild(i), condition));
            }
        }
        return condition;
    }

    /**
     * Conditional factor.
     * 
     * @param node
     *                the node
     * @param condition
     *                the condition
     * @return the condition
     */
    private Condition conditionalFactor(final SimpleNode node,
            Condition condition) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTCONDITIONAL_PRIMARY) {
            condition = conditionalPrimary((SimpleNode) node.jjtGetChild(0),
                    condition);
            if (node.getKind() == NOT) {
                condition.not();
            }
        }
        return condition;
    }

    /**
     * Conditional primary.
     * 
     * @param node
     *                the node
     * @param condition
     *                the condition
     * @return the condition
     */
    private Condition conditionalPrimary(final SimpleNode node,
            Condition condition) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTCONDITIONAL_EXPRESSION) {
            return conditionalExpression((SimpleNode) node.jjtGetChild(0),
                    condition);
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTCOMPARISON_EXPRESSION) {
            return comparisonExpression((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTBETWEEN_EXPRESSION) {
            return betweenExpression((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTLIKE_EXPRESSION) {
            return likeExpression((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTIN_EXPRESSION) {
            return inExpression((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTNULL_EXPRESSION) {
            return nullExpression((SimpleNode) node.jjtGetChild(0));
        }
        return null;
    }

    /**
     * Comparison expression.
     * 
     * @param node
     *                the node
     * @return the condition
     */
    private Condition comparisonExpression(final SimpleNode node) {
        Condition condition = null;
        switch (comparisonOperator((SimpleNode) node.jjtGetChild(1))) {
        case EQUAL:
            condition = expression((SimpleNode) node.jjtGetChild(0)).equal(
                    expression((SimpleNode) node.jjtGetChild(2)));
            break;
        case NOTEQUAL1:
            condition = expression((SimpleNode) node.jjtGetChild(0)).notEqual(
                    expression((SimpleNode) node.jjtGetChild(2)));
            break;
        case NOTEQUAL2:
            condition = expression((SimpleNode) node.jjtGetChild(0)).notEqual(
                    expression((SimpleNode) node.jjtGetChild(2)));
            break;    
        case GREATERTHAN:
            condition = expression((SimpleNode) node.jjtGetChild(0))
                    .greaterThan(expression((SimpleNode) node.jjtGetChild(2)));
            break;
        case LESSTHAN:
            condition = expression((SimpleNode) node.jjtGetChild(0)).lessThan(
                    expression((SimpleNode) node.jjtGetChild(2)));
            break;
        case GREATEREQUAL:
            condition = expression((SimpleNode) node.jjtGetChild(0))
                    .greaterEqual(expression((SimpleNode) node.jjtGetChild(2)));
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
     * @param node
     *                the node
     * @return the condition
     */
    private int comparisonOperator(final SimpleNode node) {
        return node.getKind();
    }

    /**
     * Between expression.
     * 
     * @param node
     *                the node
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
     * @param node
     *                the node
     * @return the condition
     */
    private Condition likeExpression(final SimpleNode node) {
        Condition condition = null;
        if (node.getKind() == NOT) {
            if (node.jjtGetNumChildren() == 3) {
                if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .notLike(
                                    parameter((SimpleNode) node.jjtGetChild(1)),
                                    parameter((SimpleNode) node.jjtGetChild(2)));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTSTRING_LITERAL
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTSTRING_LITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .notLike(
                                    stringLiteral((SimpleNode) node
                                            .jjtGetChild(1)),
                                    stringLiteral(
                                            (SimpleNode) node.jjtGetChild(2))
                                            .charAt(0));
                    // FIXME it should not be the alternate for escape_character
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTSTRING_LITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .notLike(
                                    parameter((SimpleNode) node.jjtGetChild(1)),
                                    stringLiteral(
                                            (SimpleNode) node.jjtGetChild(2))
                                            .charAt(0));
                    // FIXME it should not be the alternate for escape_character
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTSTRING_LITERAL
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .notLike(
                                    stringLiteral((SimpleNode) node
                                            .jjtGetChild(1)),
                                    parameter((SimpleNode) node.jjtGetChild(2)));
                }
            } else {
                if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .notLike(
                                    parameter((SimpleNode) node.jjtGetChild(1)));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTSTRING_LITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .notLike(
                                    stringLiteral((SimpleNode) node
                                            .jjtGetChild(1)));
                }
            }
        } else {
            if (node.jjtGetNumChildren() == 3) {
                if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .like(parameter((SimpleNode) node.jjtGetChild(1)),
                                    parameter((SimpleNode) node.jjtGetChild(2)));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTSTRING_LITERAL
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTSTRING_LITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .like(
                                    stringLiteral((SimpleNode) node
                                            .jjtGetChild(1)),
                                    stringLiteral(
                                            (SimpleNode) node.jjtGetChild(2))
                                            .charAt(0));
                    // FIXME it should not be the alternate for escape_character
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTSTRING_LITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .like(
                                    parameter((SimpleNode) node.jjtGetChild(1)),
                                    stringLiteral(
                                            (SimpleNode) node.jjtGetChild(2))
                                            .charAt(0));
                    // FIXME it should not be the alternate for escape_character
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTSTRING_LITERAL
                        && ((SimpleNode) node.jjtGetChild(2)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .like(
                                    stringLiteral((SimpleNode) node
                                            .jjtGetChild(1)),
                                    parameter((SimpleNode) node.jjtGetChild(2)));
                }
            } else {
                if (((SimpleNode) node.jjtGetChild(1)).id == JJTPARAMETER) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .like(parameter((SimpleNode) node.jjtGetChild(1)));
                } else if (((SimpleNode) node.jjtGetChild(1)).id == JJTSTRING_LITERAL) {
                    condition = expression((SimpleNode) node.jjtGetChild(0))
                            .like(
                                    stringLiteral((SimpleNode) node
                                            .jjtGetChild(1)));
                }
            }
        }
        return condition;
    }

    /**
     * In expression.
     * 
     * @param node
     *                the node
     * @return the condition
     */
    private Condition inExpression(final SimpleNode node) {
        InCondition inCond = null;
        if (node.getKind() == NOT) {
            if (((SimpleNode) node.jjtGetChild(0)).id == JJTPATH) {
                inCond = _schema.field(
                        identifier((SimpleNode) node.jjtGetChild(0)
                                .jjtGetChild(1))).notIn();
            }
        } else {
            if (((SimpleNode) node.jjtGetChild(0)).id == JJTPATH) {
                inCond = _schema.field(
                        identifier((SimpleNode) node.jjtGetChild(0)
                                .jjtGetChild(1))).in();
            }
        }
        // **InItem **//
        for (int i = 0; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i).jjtGetChild(0)).id == JJTPARAMETER) {
                inCond.add(parameter((SimpleNode) node.jjtGetChild(i)
                        .jjtGetChild(0)));
            } else if (((SimpleNode) node.jjtGetChild(i).jjtGetChild(0)).id == JJTLITERAL) {
                inCond.add(literal((SimpleNode) node.jjtGetChild(i)
                        .jjtGetChild(0)));
            } // else if (((SimpleNode) node.jjtGetChild(0)).id ==
            // JJTPATH) {
            // inCond.add(_schema.field(
            // identifier((SimpleNode) node.jjtGetChild(0)
            // TODO check .jjtGetChild(1))))
            // }
        }

        return inCond;
    }

    /**
     * Null expression.
     * 
     * @param node
     *                the node
     * @return the condition
     */
    private Condition nullExpression(final SimpleNode node) {
        Condition condition = null;
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTPATH) {
            if (node.getKind() == NOT) {
                condition = _schema.field(
                        identifier((SimpleNode) node.jjtGetChild(0)
                                .jjtGetChild(1))).isNotNull();
            } else {
                condition = _schema.field(
                        identifier((SimpleNode) node.jjtGetChild(0)
                                .jjtGetChild(1))).isNull();
            }
        }
        return condition;
    }

    /**
     * Expression.
     * 
     * @param node
     *                the node
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
     * @param node
     *                the node
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
     * @param node
     *                the node
     * @return the expression
     */
    private Expression factor(final SimpleNode node) {
        if (node.getKind() == PLUS) {
            return primary((SimpleNode) node.jjtGetChild(0)).plus();
        } else if (node.getKind() == MINUS) {
            return primary((SimpleNode) node.jjtGetChild(0)).negate();
        } else if (node.getKind() == ABS) {
            return primary((SimpleNode) node.jjtGetChild(0)).abs();
        }
        return primary((SimpleNode) node.jjtGetChild(0));
    }

    /**
     * Primary.
     * 
     * @param node
     *                the node
     * @return the expression
     */
    private Expression primary(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTEXPRESSION) {
            return expression((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTPATH) {
            return _schema.field(identifier((SimpleNode) node.jjtGetChild(0)
                    .jjtGetChild(1)));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTPARAMETER) {
            return parameter((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTLITERAL) {
            return literal((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTFUNCTION) {
            return function((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTUNDEFINED_FUNCTION) {
            return undefinedFunction((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTDATE_TIME_FUNCTION) {
            return dateTimeFunction((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTDATE_PRIMARY) {
            return datePrimary((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTTIME_PRIMARY) {
            return timePrimary((SimpleNode) node.jjtGetChild(0));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTTIMESTAMP_PRIMARY) {
            return timestampPrimary((SimpleNode) node.jjtGetChild(0));
        }
        return null;
    }

    /**
     * Function.
     * 
     * @param node
     *                the node
     * @return the function
     */
    private Function function(final SimpleNode node) {
        CustomFunction custom = new CustomFunction(identifier((SimpleNode) node
                .jjtGetChild(0)));
        List<Expression> parameters = new ArrayList<Expression>();
        for (int i = 0; node.jjtGetNumChildren() > i; i++) {
            if (((SimpleNode) node.jjtGetChild(i)).id == JJTEXPRESSION) {
                parameters.add(expression((SimpleNode) node.jjtGetChild(i)));
            }
            custom.setParameters(parameters);
        }
        return custom;
    }

    /**
     * Undefined function.
     * 
     * @param node
     *                the node
     * @return the function
     */
    private Function undefinedFunction(final SimpleNode node) {
        // if (node.getKind() == IS_DEFINED) {
        // return (((Field)expression((SimpleNode)
        // node.jjtGetChild(0))).isNotNull();
        // } else {

        // }
        return null;
    }

    /**
     * Datetime function.
     * 
     * @param node
     *                the node
     * @return the function
     */
    private Function dateTimeFunction(final SimpleNode node) {
        // TODO
        return null;
    }

    /**
     * Date primary.
     * 
     * @param node
     *                the node
     * @return the expression
     */
    private Expression datePrimary(final SimpleNode node) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateLiteral date = null;
        try {
            date = new DateLiteral(sdf.parse(dateLiteral((SimpleNode) node
                    .jjtGetChild(0))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Time primary.
     * 
     * @param node
     *                the node
     * @return the expression
     */
    private Expression timePrimary(final SimpleNode node) {
        String sTime = timeLiteral((SimpleNode) node.jjtGetChild(0));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        TimeLiteral time = null;
        if (sTime.length() == 8) {
            sTime += ".000";
        } else if (sTime.length() == 10) {
            sTime += "00";
        } else if (sTime.length() == 11) {
            sTime += "0";
        }

        try {
            time = new TimeLiteral(sdf.parse(sTime));
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        }

        return time;
    }

    /**
     * Timestamp primary.
     * 
     * @param node
     *                the node
     * @return the expression
     */
    private Expression timestampPrimary(final SimpleNode node) {
        String sTimestamp = timestampLiteral((SimpleNode) node.jjtGetChild(0));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        TimestampLiteral timestamp = null;
        try {
            timestamp = new TimestampLiteral(sdf.parse(sTimestamp));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * Literal.
     * 
     * @param node
     *                the node
     * @return the literal
     */
    private Literal literal(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTBOOLEAN_LITERAL) {
            return new BooleanLiteral(booleanLiteral((SimpleNode) node
                    .jjtGetChild(0)));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTINTEGER_LITERAL) {
            return new LongLiteral(integerLiteral((SimpleNode) node
                    .jjtGetChild(0)));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTFLOAT_LITERAL) {
            return new DoubleLiteral(floatLiteral((SimpleNode) node
                    .jjtGetChild(0)));
        } else if (((SimpleNode) node.jjtGetChild(0)).id == JJTSTRING_LITERAL) {
            return new StringLiteral(stringLiteral((SimpleNode) node
                    .jjtGetChild(0)));
        }
        return null;
    }

    /**
     * Parameter.
     * 
     * @param node
     *                the node
     * @return the parameter
     */
    private Parameter parameter(final SimpleNode node) {
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTPOSITIONAL_PARAMETER
                || ((SimpleNode) node.jjtGetChild(0)).id == JJTCASTOR_PARAMETER) {
            return positionalParameter((SimpleNode) node.jjtGetChild(0));
        }
        if (((SimpleNode) node.jjtGetChild(0)).id == JJTNAMED_PARAMETER) {
            return namedParameter((SimpleNode) node.jjtGetChild(0));
        }
        return null;
    }

    /**
     * Positional parameter.
     * 
     * @param node
     *                the node
     * @return the positional parameter
     */
    private PositionalParameter positionalParameter(final SimpleNode node) {
        return new PositionalParameter(integerLiteral((SimpleNode) node
                .jjtGetChild(0)));
    }

    /**
     * Named parameter.
     * 
     * @param node
     *                the node
     * @return the named parameter
     */
    private NamedParameter namedParameter(final SimpleNode node) {
        return new NamedParameter(identifier((SimpleNode) node.jjtGetChild(0)));
    }

    /**
     * Path.
     * 
     * @param node
     *                the node
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
     * @param node
     *                the node
     * @return the string
     */
    private String identifier(final SimpleNode node) {
        return node.getText();
    }

    /**
     * Boolean literal.
     * 
     * @param node
     *                the node
     * @return the string
     */
    private boolean booleanLiteral(final SimpleNode node) {
        return Boolean.parseBoolean(node.getText());
    }

    /**
     * Integer literal.
     * 
     * @param node
     *                the node
     * @return the int
     */
    private int integerLiteral(final SimpleNode node) {
        return Integer.parseInt(node.getText());
    }

    /**
     * Float literal.
     * 
     * @param node
     *                the node
     * @return the float
     */
    private double floatLiteral(final SimpleNode node) {
        return Double.parseDouble(node.getText());
    }

    /**
     * Char literal.
     * 
     * @param node
     *                the node
     * @return the string
     */
    private String charLiteral(final SimpleNode node) {
        return node.getText();
    }

    /**
     * String literal.
     * 
     * @param node
     *                the node
     * @return the string
     */
    private String stringLiteral(final SimpleNode node) {
        return node.getText().replace("'", "");
    }

    /**
     * Timestamp literal.
     * 
     * @param node
     *                the node
     * @return the string
     */
    private String timestampLiteral(final SimpleNode node) {
        return node.getText().replace("'", "");
    }

    /**
     * Date literal.
     * 
     * @param node
     *                the node
     * @return the string
     */
    private String dateLiteral(final SimpleNode node) {
        return node.getText().replace("'", "");
    }

    /**
     * Time literal.
     * 
     * @param node
     *                the node
     * @return the string
     */
    private String timeLiteral(final SimpleNode node) {
        return node.getText().replace("'", "");
    }

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
     * @param select
     *                the new select
     */
    public void setSelect(final SelectQuery select) {
        _select = select;
    }

    /**
     * Gets the schema.
     * 
     * @return the schema
     */
    public Schema getSchema() {
        return _schema;
    }

    /**
     * Sets the schema.
     * 
     * @param schema
     *                the new schema
     */
    public void setSchema(final Schema schema) {
        _schema = schema;
    }

}
