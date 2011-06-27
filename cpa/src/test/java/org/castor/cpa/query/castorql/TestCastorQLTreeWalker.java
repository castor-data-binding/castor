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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.castor.cpa.query.ParseException;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.TokenManagerError;

/**
 * Junit test for testing CastorQL Tree Walker.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestCastorQLTreeWalker extends TestCase {
    // --------------------------------------------------------------------------

    public void testConstructor() {
        try {
            new CastorQLTreeWalker(new SimpleNode(3));
            fail("expected IllegalArgumentException !!!");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            new CastorQLTreeWalker((SimpleNode) null);
            fail("expected NullPointerException !!!");
        } catch (NullPointerException e) {
            assertTrue(true);
        }

        SimpleNode root = null;
        try {
            root = getSimpleNode("SelecT DisTinct o.item from de.jsci as o");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            new CastorQLTreeWalker(root);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception didn't expeccted !!!");
            e.printStackTrace();
        }
    }

    // --------------------------------------------------------------------------
    // minimal select query
    
    public void testSelect() throws Exception {
        String oql = "select a from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r AS o";
        assertEquals(expected, actual);
    }

    // --------------------------------------------------------------------------
    // select with path
    
    public void testSelectPathDot1() throws Exception {
        String oql = "select a.b from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a.b FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectPathDot2() throws Exception {
        String oql = "select a.b.c from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a.b.c FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectPathDot3() throws Exception {
        String oql = "select a.b.c.d from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a.b.c.d FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectPathDart1() throws Exception {
        String oql = "select a->b from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a.b FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectPathDart2() throws Exception {
        String oql = "select a->b->c from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a.b.c FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectPathDart3() throws Exception {
        String oql = "select a->b->c->d from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a.b.c.d FROM r AS o";
        assertEquals(expected, actual);
    }

    // --------------------------------------------------------------------------
    // select with alias
    
    public void testSelectAliasAs1() throws Exception {
        String oql = "select a as x from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a AS x FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectAliasAs2() throws Exception {
        String oql = "select a.b.c.d as x from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a.b.c.d AS x FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectAliasAs3() throws Exception {
        String oql = "select a->b->c->d as x from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a.b.c.d AS x FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectAliasColon1() throws Exception {
        String oql = "select x:a from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a AS x FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectAliasColon2() throws Exception {
        String oql = "select x:a.b.c.d from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a.b.c.d AS x FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectAliasColon3() throws Exception {
        String oql = "select x:a->b->c->d from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a.b.c.d AS x FROM r AS o";
        assertEquals(expected, actual);
    }

    // --------------------------------------------------------------------------
    // select distinct
    
    public void testSelectDistinct1() throws Exception {
        String oql = "select distinct a from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT a FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectDistinct2() throws Exception {
        String oql = "select distinct a.b.c.d as x from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT a.b.c.d AS x FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testSelectDistinct3() throws Exception {
        String oql = "select distinct x:a->b->c->d from r o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT a.b.c.d AS x FROM r AS o";
        assertEquals(expected, actual);
    }

    // --------------------------------------------------------------------------
    // from with path
    
    public void testFromPathDot1() throws Exception {
        String oql = "select a from r.s o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r.s AS o";
        assertEquals(expected, actual);
    }

    public void testFromPathDot2() throws Exception {
        String oql = "select a from r.s.t o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r.s.t AS o";
        assertEquals(expected, actual);
    }

    public void testFromPathDot3() throws Exception {
        String oql = "select a from r.s.t.u o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r.s.t.u AS o";
        assertEquals(expected, actual);
    }

    public void testFromPathDart1() throws Exception {
        String oql = "select a from r->s o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r.s AS o";
        assertEquals(expected, actual);
    }

    public void testFromPathDart2() throws Exception {
        String oql = "select a from r->s->t o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r.s.t AS o";
        assertEquals(expected, actual);
    }

    public void testFromPathDart3() throws Exception {
        String oql = "select a from r->s->t->u o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r.s.t.u AS o";
        assertEquals(expected, actual);
    }

    // --------------------------------------------------------------------------
    // from with as alias
    
    public void testFromAliasAs1() throws Exception {
        String oql = "select a from r as o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testFromAliasAs2() throws Exception {
        String oql = "select a from r.s.t.u as o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r.s.t.u AS o";
        assertEquals(expected, actual);
    }

    public void testFromAliasAs3() throws Exception {
        String oql = "select a from r->s->t->u as o";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r.s.t.u AS o";
        assertEquals(expected, actual);
    }

    // --------------------------------------------------------------------------
    // from with in alias
    
    public void testFromAliasIn1() throws Exception {
        String oql = "select a from o in r";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r AS o";
        assertEquals(expected, actual);
    }

    public void testFromAliasIn2() throws Exception {
        String oql = "select a from o in r.s.t.u";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r.s.t.u AS o";
        assertEquals(expected, actual);
    }

    public void testFromAliasIn3() throws Exception {
        String oql = "select a from o in r->s->t->u";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT a FROM r.s.t.u AS o";
        assertEquals(expected, actual);
    }

    
    
    
    
    
    
    
    
    
    
    // --------------------------------------------------------------------------
    // order
    
    public void testOrderBy() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " order by o.name, o.id desc";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO"
            + " AS o ORDER BY o.name ASC, o.id DESC";
        assertEquals(expected, actual);
    }

    // --------------------------------------------------------------------------
    // limit
    
    public void testLimitOffsetWithInt() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " limit 1 offset 2";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " LIMIT 1 OFFSET 2";
        assertEquals(expected, actual);
    }

    public void testLimitOffsetWithParameter() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " limit ?1 offset ?2";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " LIMIT ?1 OFFSET ?2";
        assertEquals(expected, actual);
    }

    // --------------------------------------------------------------------------
    
    
    
    
    
    
    
    
    
    

    public void testCompareEqualWithBoolean() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted = true";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted = true)";
        assertEquals(expected, actual);
    }

    public void testCompareEqualWithLong() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted = 67";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted = 67)";
        assertEquals(expected, actual);
    }

    public void testCompareEqualWithDouble() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted = 67.43";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted = 67.43)";
        assertEquals(expected, actual);
    }

    public void testCompareEqualWithString() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted = 'Testing'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted = 'Testing')";
        assertEquals(expected, actual);
    }

    public void testCompareEqualWithDate() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted = DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted = DATE '2008-08-04')";
        assertEquals(expected, actual);
    }

    public void testCompareEqualWithTime() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted = Time '03:22:04.9'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted = TIME '03:22:04.900')";
        assertEquals(expected, actual);
    }

    public void testCompareEqualWithTimestamp() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted = TimeStamp '2008-08-05 03:22:04.000'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted = TIMESTAMP '2008-08-05 03:22:04.000')";
        assertEquals(expected, actual);
    }

    public void testCompareNotEqualWithBoolean() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted != true";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted <> true)";
        assertEquals(expected, actual);
    }

    public void testCompareNotEqualWithLong() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted != 67";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted <> 67)";
        assertEquals(expected, actual);
    }

    public void testCompareNotEqualWithDouble() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted != 67.43";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted <> 67.43)";
        assertEquals(expected, actual);
    }

    public void testCompareNotEqualWithString() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted != 'Testing'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted <> 'Testing')";
        assertEquals(expected, actual);
    }

    public void testCompareNotEqualWithDate() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted != DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted <> DATE '2008-08-04')";
        assertEquals(expected, actual);
    }

    public void testCompareNotEqualWithTime() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted != Time '03:22:04.000'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted <> TIME '03:22:04.000')";
        assertEquals(expected, actual);
    }

    public void testCompareNotEqualWithTimestamp() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted != TimeStamp '2008-08-05 03:22:04.000'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted <> TIMESTAMP '2008-08-05 03:22:04.000')";
        assertEquals(expected, actual);
    }

    public void testCompareLessThanWithLong() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted < 67";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted < 67)";
        assertEquals(expected, actual);
    }

    public void testCompareLessThanWithDouble() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted < 67.43";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted < 67.43)";
        assertEquals(expected, actual);
    }

    public void testCompareLessThanWithString() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted < 'Testing'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted < 'Testing')";
        assertEquals(expected, actual);
    }

    public void testCompareLessThanWithDate() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted < DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted < DATE '2008-08-04')";
        assertEquals(expected, actual);
    }

    public void testCompareLessThanWithTime() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted < Time '03:22:04'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted < TIME '03:22:04.000')";
        assertEquals(expected, actual);
    }

    public void testCompareLessThanWithTimestamp() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted < TimeStamp '2008-08-05 03:22:04.000'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted < TIMESTAMP '2008-08-05 03:22:04.000')";
        assertEquals(expected, actual);
    }

    public void testCompareLessEqualWithLong() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted <= 67";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted <= 67)";
        assertEquals(expected, actual);
    }

    public void testCompareLessEqualWithDouble() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted <= 67.43";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted <= 67.43)";
        assertEquals(expected, actual);
    }

    public void testCompareLessEqualWithString() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted <= 'Testing'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted <= 'Testing')";
        assertEquals(expected, actual);
    }

    public void testCompareLessEqualWithDate() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted <= DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted <= DATE '2008-08-04')";
        assertEquals(expected, actual);
    }

    public void testCompareLessEqualWithTime() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted <= Time '03:22:04'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted <= TIME '03:22:04.000')";
        assertEquals(expected, actual);
    }

    public void testCompareLessEqualWithTimestamp() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted <= TimeStamp '2008-08-05 03:22:04.000'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted <= TIMESTAMP '2008-08-05 03:22:04.000')";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterEqualWithLong() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted >= 67";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted >= 67)";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterEqualWithDouble() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted >= 67.43";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted >= 67.43)";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterEqualWithString() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted >= 'Testing'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted >= 'Testing')";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterEqualWithDate() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted >= DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted >= DATE '2008-08-04')";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterEqualWithTime() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted >= Time '03:22:04'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted >= TIME '03:22:04.000')";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterEqualWithTimestamp() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted >= TimeStamp '2008-08-05 03:22:04.090'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted >= TIMESTAMP '2008-08-05 03:22:04.090')";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterThanWithLong() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted > 67";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted > 67)";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterThanWithDouble() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted > 67.43";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted > 67.43)";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterThanWithString() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted > 'Testing'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted > 'Testing')";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterThanWithDate() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted > DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted > DATE '2008-08-04')";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterThanWithTime() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted > Time '03:22:04'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted > TIME '03:22:04.000')";
        assertEquals(expected, actual);
    }

    public void testCompareGreaterThanWithTimestamp() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted > TimeStamp '2008-08-05 03:22:04.009'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
            + " WHERE (o.deleted > TIMESTAMP '2008-08-05 03:22:04.009')";
        assertEquals(expected, actual);
    }

    public void testLikeWithString() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.$8de_leted LIKE '%s@T#'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.$8de_leted LIKE '%s@T#')";
        assertEquals(expected, actual);
    }

    public void testLikeWithStringChar() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted LIKE 'sT' ESCAPE 'r'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted LIKE 'sT' ESCAPE 'r')";
        assertEquals(expected, actual);
    }

    public void testLikeWithStringParameter() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted LIKE 'sT' ESCAPE :N";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted LIKE 'sT' ESCAPE :N)";
        assertEquals(expected, actual);
    }

    public void testLikeWithParameter() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted LIKE :NamedParameter";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted LIKE :NamedParameter)";
        assertEquals(expected, actual);
    }

    public void testLikeWithParameterChar() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted LIKE :NamedParameter Escape 'r'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted LIKE :NamedParameter ESCAPE 'r')";
        assertEquals(expected, actual);
    }

    public void testLikeWithParameterParameter() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted LIKE :NamedParameter Escape :N";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted LIKE :NamedParameter ESCAPE :N)";
        assertEquals(expected, actual);
    }

    public void testNotLikeWithString() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted NOT LIKE 'sT'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT LIKE 'sT')";
        assertEquals(expected, actual);
    }

    public void testNotLikeWithStringChar() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted Not LIKE 'sT' ESCAPE 'r'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT LIKE 'sT' ESCAPE 'r')";
        assertEquals(expected, actual);
    }

    public void testNotLikeWithStringParameter() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted nOt LIKE 'sT' ESCAPE :N";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT LIKE 'sT' ESCAPE :N)";
        assertEquals(expected, actual);
    }

    public void testNotLikeWithParameter() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted not LIKE :NamedParameter";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT LIKE :NamedParameter)";
        assertEquals(expected, actual);
    }

    public void testNotLikeWithParameterChar() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted not LIKE :NamedParameter Escape 'r'";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT LIKE :NamedParameter ESCAPE 'r')";
        assertEquals(expected, actual);
    }

    public void testNotLikeWithParameterParameter() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted NOT LIKE :NamedParameter Escape :N";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT LIKE :NamedParameter ESCAPE :N)";
        assertEquals(expected, actual);
    }

    public void testBetweenWithLong() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted betweEn 95 and 400 ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted BETWEEN 95 AND 400)";
        assertEquals(expected, actual);
    }

    public void testBetweenWithDouble() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted betweEn 95.43 and 400.95 ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted BETWEEN 95.43 AND 400.95)";
        assertEquals(expected, actual);
    }

    public void testBetweenWithString() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted betweEn 'Low' and 'High' ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted BETWEEN 'Low' AND 'High')";
        assertEquals(expected, actual);
    }

    public void testBetweenWithDate() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted betweEn DATE '2007-08-05' and DATE '2008-08-05' ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted BETWEEN DATE '2007-08-05' AND DATE '2008-08-05')";
        assertEquals(expected, actual);
    }

    public void testBetweenWithTime() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted betweEn TIME '06:52:56' and TIME '08:52:56' ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted BETWEEN TIME '06:52:56.000' AND TIME '08:52:56.000')";
        assertEquals(expected, actual);
    }

    public void testBetweenWithTimestamp() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted betweEn TIMESTAMP '2007-08-05 06:52:56.130' and "
            + "TIMESTAMP '2008-08-05 08:52:56.130' ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted BETWEEN TIMESTAMP '2007-08-05 06:52:56.130' AND "
            + "TIMESTAMP '2008-08-05 08:52:56.130')";
        assertEquals(expected, actual);
    }

    public void testNotBetweenWithLong() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted NOT betweEn 95 and 400 ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT BETWEEN 95 AND 400)";
        assertEquals(expected, actual);
    }

    public void testNotBetweenWithDouble() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted NOT betweEn 95.43 and 400.95 ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT BETWEEN 95.43 AND 400.95)";
        assertEquals(expected, actual);
    }

    public void testNotBetweenWithString() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted NOT betweEn 'Low' and 'High' ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT BETWEEN 'Low' AND 'High')";
        assertEquals(expected, actual);
    }

    public void testNotBetweenWithDate() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted NOT betweEn DATE '2007-08-05' and DATE '2008-08-05' ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT BETWEEN DATE '2007-08-05' AND DATE '2008-08-05')";
        assertEquals(expected, actual);
    }

    public void testNotBetweenWithTime() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted NOT betweEn TIME '06:52:56' and TIME '08:52:56' ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT BETWEEN TIME '06:52:56.000' AND TIME '08:52:56.000')";
        assertEquals(expected, actual);
    }

    public void testNotBetweenWithTimestamp() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted NOT betweEn TIMESTAMP '2007-08-05 06:52:56.013' and "
            + "TIMESTAMP '2008-08-05 08:52:56.013' ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT BETWEEN TIMESTAMP '2007-08-05 06:52:56.013' AND "
            + "TIMESTAMP '2008-08-05 08:52:56.013')";
        assertEquals(expected, actual);
    }

    public void testInWithPath() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted NOT IN (o.path1, o.path2, o.path3)";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT  IN ('o.path1', 'o.path2', 'o.path3'))";
        assertEquals(expected, actual);
    }

    public void testInParameter() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted NOT IN (:test, ?453,:asd)";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT  IN (:test, ?453, :asd))";
        assertEquals(expected, actual);
    }

    public void testInLiterals() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted NOT IN (34, 453.34, true, 'String')";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted NOT  IN (34, 453.34, true, 'String'))";
        assertEquals(expected, actual);
    }

    public void testNull() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted IS NOT NULL";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted IS NOT NULL)";
        assertEquals(expected, actual);
    }

    public void testComplex() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted+3*5 = 4 and (o.deleted+3)*5 = 4 "
            + " or o.deleted LIKE 'jk' and o.deleted IS NULL ";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE ((o.deleted + (3 * 5) = 4) AND ((o.deleted + 3) * 5 = 4))"
            + " OR ((o.deleted LIKE 'jk') AND (o.deleted IS NULL))";
        assertEquals(expected, actual);
    }

    public void testCustomFunction() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted = CustomFUNCTION (o.deleted)";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted = CustomFUNCTION(o.deleted))";
        assertEquals(expected, actual);
    }

    public void testCustomFunctionMoreParameters() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where o.deleted = CustomFUNCTION (o.deleted,o.edited)";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.deleted = CustomFUNCTION(o.deleted, o.edited))";
        assertEquals(expected, actual);
    }

    public void testUndefinedFunction() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where IS_UNDEFINED (o.field)";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.field IS NULL)";
        assertEquals(expected, actual);
    }

    public void testDefinedFunction() throws Exception {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
            + " where IS_DEFINED (o.field)";
        QueryObject qo = getQO(oql);
        String actual = qo.toString();
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
            + "WHERE (o.field IS NOT NULL)";
        assertEquals(expected, actual);
    }

    // --------------------------------------------------------------------------

    private QueryObject getQO(final String oql)
    throws UnsupportedEncodingException, ParseException {
        CastorQLParser parser = null;
        CastorQLParserTokenManager tkmgr = null;
        try {
            tkmgr = createTkmgr(oql);
            parser = new CastorQLParser(tkmgr);
            SimpleNode root = parser.castorQL();
            CastorQLTreeWalker tw = new CastorQLTreeWalker(root);
            return tw.getSelect();
        } catch (org.castor.cpa.query.castorql.ParseException e) {
            parser.ReInit(tkmgr);
            throw new ParseException(e);
        } catch (org.castor.cpa.query.castorql.TokenMgrError e) {
            // parser.ReInit(tkmgr);
            throw new TokenManagerError(e);
        }
    }

    private SimpleNode getSimpleNode(final String oql)
    throws UnsupportedEncodingException, ParseException {
        CastorQLParser parser = null;
        CastorQLParserTokenManager tkmgr = null;
        try {
            tkmgr = createTkmgr(oql);
            parser = new CastorQLParser(tkmgr);
            return parser.castorQL();
        } catch (org.castor.cpa.query.castorql.ParseException e) {
            parser.ReInit(tkmgr);
            throw new ParseException(e);
        } catch (org.castor.cpa.query.castorql.TokenMgrError e) {
            // parser.ReInit(tkmgr);
            throw new TokenManagerError(e);
        }
    }

    private CastorQLParserTokenManager createTkmgr(final String oql)
    throws UnsupportedEncodingException {
        StringBuffer stringBuffer = new StringBuffer(oql);
        InputStream bis = new ByteArrayInputStream(stringBuffer.toString().getBytes("UTF-8"));
        InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
        SimpleCharStream jSt = new SimpleCharStream(isr);
        return new CastorQLParserTokenManager(jSt);
    }

    // --------------------------------------------------------------------------
}
