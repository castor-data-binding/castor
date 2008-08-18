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

import org.castor.cpa.query.ParseException;
import org.castor.cpa.query.TokenManagerError;
import org.castor.cpa.query.QueryObject;

import junit.framework.TestCase;

/**
 * Junit test for testing CastorQL Tree Walker.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestCastorQLTreeWalker extends TestCase {
    // --------------------------------------------------------------------------

    public final void testConstructor() throws ParseException {

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

    public final void testOrderBy() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " order by o.name, o.id desc";

        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO"
                + " AS o ORDER BY o.name ASC, o.id DESC";
        String actual = qo.toString();

        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testLimitOffsetWithInt() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " limit 1 offset 2";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " LIMIT 1 OFFSET 2";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testLimitOffsetWithParameter() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " limit ?1 offset ?2";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " LIMIT ?1 OFFSET ?2";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithBoolean() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted = true";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted = true)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithLong() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted = 67";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted = 67)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithDouble() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted = 67.43";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted = 67.43)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithString() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted = 'Testing'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted = 'Testing')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithDate() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted = DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted = DATE '2008-08-04')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithTime() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted = Time '03:22:04.9'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted = TIME '03:22:04.900')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithTimestamp() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted = TimeStamp '2008-08-05 03:22:04.000'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted = TIMESTAMP '2008-08-05 03:22:04.000')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithBoolean() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted != true";

        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted <> true)";
        String actual = qo.toString();

        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithLong() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted != 67";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted <> 67)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithDouble() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted != 67.43";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted <> 67.43)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithString() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted != 'Testing'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted <> 'Testing')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithDate() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted != DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted <> DATE '2008-08-04')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithTime() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted != Time '03:22:04.000'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted <> TIME '03:22:04.000')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithTimestamp() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted != TimeStamp '2008-08-05 03:22:04.000'";

        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted <> TIMESTAMP '2008-08-05 03:22:04.000')";
        String actual = qo.toString();

        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithLong() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted < 67";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted < 67)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithDouble() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted < 67.43";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted < 67.43)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithString() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted < 'Testing'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted < 'Testing')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithDate() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted < DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted < DATE '2008-08-04')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithTime() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted < Time '03:22:04'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted < TIME '03:22:04.000')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithTimestamp() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted < TimeStamp '2008-08-05 03:22:04.000'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted < TIMESTAMP '2008-08-05 03:22:04.000')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithLong() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted <= 67";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted <= 67)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithDouble() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted <= 67.43";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted <= 67.43)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithString() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted <= 'Testing'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted <= 'Testing')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithDate() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted <= DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted <= DATE '2008-08-04')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithTime() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted <= Time '03:22:04'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted <= TIME '03:22:04.000')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithTimestamp() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted <= TimeStamp '2008-08-05 03:22:04.000'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted <= TIMESTAMP '2008-08-05 03:22:04.000')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithLong() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted >= 67";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted >= 67)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithDouble() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted >= 67.43";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted >= 67.43)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithString() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted >= 'Testing'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted >= 'Testing')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithDate() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted >= DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted >= DATE '2008-08-04')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithTime() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted >= Time '03:22:04'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted >= TIME '03:22:04.000')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithTimestamp() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted >= TimeStamp '2008-08-05 03:22:04.090'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted >= TIMESTAMP '2008-08-05 03:22:04.090')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithLong() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted > 67";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted > 67)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithDouble() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted > 67.43";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted > 67.43)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithString() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted > 'Testing'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted > 'Testing')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithDate() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted > DATE '2008-08-04'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted > DATE '2008-08-04')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithTime() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted > Time '03:22:04'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted > TIME '03:22:04.000')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithTimestamp() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted > TimeStamp '2008-08-05 03:22:04.009'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                + " WHERE (o.deleted > TIMESTAMP '2008-08-05 03:22:04.009')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithString() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.$8de_leted LIKE '%s@T#'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.$8de_leted LIKE '%s@T#')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithStringChar() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted LIKE 'sT' ESCAPE 'r'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted LIKE 'sT' ESCAPE 'r')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithStringParameter() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted LIKE 'sT' ESCAPE :N";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted LIKE 'sT' ESCAPE :N)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithParameter() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted LIKE :NamedParameter";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted LIKE :NamedParameter)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithParameterChar() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted LIKE :NamedParameter Escape 'r'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted LIKE :NamedParameter ESCAPE 'r')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithParameterParameter() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted LIKE :NamedParameter Escape :N";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted LIKE :NamedParameter ESCAPE :N)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithString() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT LIKE 'sT'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT LIKE 'sT')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithStringChar() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted Not LIKE 'sT' ESCAPE 'r'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT LIKE 'sT' ESCAPE 'r')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithStringParameter() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted nOt LIKE 'sT' ESCAPE :N";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT LIKE 'sT' ESCAPE :N)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithParameter() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted not LIKE :NamedParameter";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT LIKE :NamedParameter)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithParameterChar() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted not LIKE :NamedParameter Escape 'r'";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT LIKE :NamedParameter ESCAPE 'r')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithParameterParameter() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT LIKE :NamedParameter Escape :N";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT LIKE :NamedParameter ESCAPE :N)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithLong() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted betweEn 95 and 400 ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted BETWEEN 95 AND 400)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithDouble() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted betweEn 95.43 and 400.95 ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted BETWEEN 95.43 AND 400.95)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithString() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted betweEn 'Low' and 'High' ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted BETWEEN 'Low' AND 'High')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithDate() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted betweEn DATE '2007-08-05' and DATE '2008-08-05' ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted BETWEEN DATE '2007-08-05' AND DATE '2008-08-05')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithTime() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted betweEn TIME '06:52:56' and TIME '08:52:56' ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted BETWEEN TIME '06:52:56.000' AND TIME '08:52:56.000')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithTimestamp() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted betweEn TIMESTAMP '2007-08-05 06:52:56.130' and "
                + "TIMESTAMP '2008-08-05 08:52:56.130' ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted BETWEEN TIMESTAMP '2007-08-05 06:52:56.130' AND "
                + "TIMESTAMP '2008-08-05 08:52:56.130')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithLong() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT betweEn 95 and 400 ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT BETWEEN 95 AND 400)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithDouble() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT betweEn 95.43 and 400.95 ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT BETWEEN 95.43 AND 400.95)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithString() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT betweEn 'Low' and 'High' ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT BETWEEN 'Low' AND 'High')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithDate() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT betweEn DATE '2007-08-05' and DATE '2008-08-05' ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT BETWEEN DATE '2007-08-05' AND DATE '2008-08-05')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithTime() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT betweEn TIME '06:52:56' and TIME '08:52:56' ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT BETWEEN TIME '06:52:56.000' AND TIME '08:52:56.000')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithTimestamp() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT betweEn TIMESTAMP '2007-08-05 06:52:56.013' and "
                + "TIMESTAMP '2008-08-05 08:52:56.013' ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT BETWEEN TIMESTAMP '2007-08-05 06:52:56.013' AND "
                + "TIMESTAMP '2008-08-05 08:52:56.013')";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testInWithPath() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT IN (org.castor.cpa.query.castorql.MockEnum.TEST1" 
                + ", org.castor.cpa.query.castorql.MockEnum.TEST2)";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT  IN (org.castor.cpa.query.castorql.MockEnum.TEST1" 
                + ", org.castor.cpa.query.castorql.MockEnum.TEST2))";
        String actual = qo.toString();
        assertEquals(expected, actual);
    }

    // --------------------------------------------------------------------------

    public final void testInWithPathFail() throws UnsupportedEncodingException {
        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT IN (org.castor.cpa.query.castorql.MockEnum.INVALID)";
        try {
            getQO(oql);
            fail("ParseException expected !!!");
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    // --------------------------------------------------------------------------

    public final void testInParameter() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT IN (:test, ?453,:asd)";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT  IN (:test, ?453, :asd))";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testInLiterals() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted NOT IN (34, 453.34, true, 'String')";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted NOT  IN (34, 453.34, true, 'String'))";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testNull() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted IS NOT NULL";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted IS NOT NULL)";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testComplex() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted+3*5 = 4 and (o.deleted+3)*5 = 4 "
                + " or o.deleted LIKE 'jk' and o.deleted IS NULL ";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE ((o.deleted + (3 * 5) = 4) AND ((o.deleted + 3) * 5 = 4))"
                + " OR ((o.deleted LIKE 'jk') AND (o.deleted IS NULL))";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCustomFunction() throws UnsupportedEncodingException, ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted = CustomFUNCTION (o.deleted)";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted = CustomFUNCTION(o.deleted))";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    // --------------------------------------------------------------------------

    public final void testCustomFunctionMoreParameters() throws UnsupportedEncodingException,
            ParseException {

        String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                + " where o.deleted = CustomFUNCTION (o.deleted,o.edited)";
        QueryObject qo = getQO(oql);
        String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                + "WHERE (o.deleted = CustomFUNCTION(o.deleted, o.edited))";
        String actual = qo.toString();
        assertEquals(expected, actual);

    }

    private QueryObject getQO(final String oql) 
                        throws UnsupportedEncodingException, ParseException {
        CastorQLParser parser = null;
        CastorQLParserTokenManager tkmgr = null;
        try {

            tkmgr = createTkmgr(oql);
            parser = new CastorQLParser(tkmgr);
            SimpleNode root = parser.select_statement();
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

    private SimpleNode getSimpleNode(final String oql) throws UnsupportedEncodingException,
            ParseException {
        CastorQLParser parser = null;
        CastorQLParserTokenManager tkmgr = null;
        try {

            tkmgr = createTkmgr(oql);
            parser = new CastorQLParser(tkmgr);
            return parser.select_statement();
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
