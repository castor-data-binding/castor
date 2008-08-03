package org.castor.cpa.query.object.expression;

import junit.framework.TestCase;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Field;
import org.castor.cpa.query.Foo;
import org.castor.cpa.query.QueryFactory;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.SelectQuery;
/**
 * Junit Test for testing concat arithmetic expression of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestConcat extends TestCase {
    //--------------------------------------------------------------------------

    private static String _common = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o WHERE ";

    //--------------------------------------------------------------------------

    public static void testAll() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        Field field = schema.field("position");
        Expression exp = new MockExpression();
        Condition condition = field
        .concat("STRING")
        .concat(exp)
        .equal(3);
        select.setWhere(condition);
        select.addSchema(schema);
        String expected = "o.position || 'STRING' || expression = 3";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }

    //--------------------------------------------------------------------------
}
