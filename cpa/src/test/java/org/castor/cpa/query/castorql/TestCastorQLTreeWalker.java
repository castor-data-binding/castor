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

import java.io.UnsupportedEncodingException;

import org.castor.cpa.query.Parser;
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
    // TODO Add Tests for TIME DATE types
    public final void testOrderBy() {

        try {
            // Mess the Query to see the exceptions
            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " order by o.name, o.id desc";

            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO"
                    + " AS o ORDER BY o.name ASC, o.id DESC";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testLimitOffsetWithInt() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " limit 1 offset 2";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " LIMIT 1 OFFSET 2";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testLimitOffsetWithParameter() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " limit ?1 offset ?2";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " LIMIT ?1 OFFSET ?2";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithBoolean() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted = true";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted = true)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithLong() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted = 67";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted = 67)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithDouble() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted = 67.43";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted = 67.43)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithString() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted = 'Testing'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted = 'Testing')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithDate() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted = DATE '2008-08-04'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted = DATE '2008-08-04')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithTime() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted = Time '03:22:04.9'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted = TIME '03:22:04.900')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareEqualWithTimestamp() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted = TimeStamp '2008-08-05 03:22:04.000'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted = TIMESTAMP '2008-08-05 03:22:04.000')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithBoolean() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted != true";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted != true)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithLong() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted != 67";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted != 67)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithDouble() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted != 67.43";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted != 67.43)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithString() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted != 'Testing'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted != 'Testing')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithDate() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted != DATE '2008-08-04'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted != DATE '2008-08-04')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithTime() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted != Time '03:22:04.000'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted != TIME '03:22:04.000')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareNotEqualWithTimestamp() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted != TimeStamp '2008-08-05 03:22:04.000'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted != TIMESTAMP '2008-08-05 03:22:04.000')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithLong() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted < 67";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted < 67)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithDouble() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted < 67.43";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted < 67.43)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithString() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted < 'Testing'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted < 'Testing')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithDate() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted < DATE '2008-08-04'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted < DATE '2008-08-04')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithTime() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted < Time '03:22:04'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted < TIME '03:22:04.000')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessThanWithTimestamp() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted < TimeStamp '2008-08-05 03:22:04.000'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted < TIMESTAMP '2008-08-05 03:22:04.000')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithLong() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted <= 67";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted <= 67)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithDouble() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted <= 67.43";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted <= 67.43)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithString() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted <= 'Testing'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted <= 'Testing')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithDate() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted <= DATE '2008-08-04'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted <= DATE '2008-08-04')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithTime() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted <= Time '03:22:04'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted <= TIME '03:22:04.000')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareLessEqualWithTimestamp() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted <= TimeStamp '2008-08-05 03:22:04.000'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted <= TIMESTAMP '2008-08-05 03:22:04.000')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithLong() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted >= 67";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted >= 67)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithDouble() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted >= 67.43";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted >= 67.43)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithString() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted >= 'Testing'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted >= 'Testing')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithDate() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted >= DATE '2008-08-04'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted >= DATE '2008-08-04')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithTime() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted >= Time '03:22:04'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted >= TIME '03:22:04.000')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterEqualWithTimestamp() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted >= TimeStamp '2008-08-05 03:22:04.090'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted >= TIMESTAMP '2008-08-05 03:22:04.090')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithLong() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted > 67";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted > 67)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithDouble() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted > 67.43";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted > 67.43)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithString() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted > 'Testing'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted > 'Testing')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithDate() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted > DATE '2008-08-04'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted > DATE '2008-08-04')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithTime() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted > Time '03:22:04'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted > TIME '03:22:04.000')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCompareGreaterThanWithTimestamp() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted > TimeStamp '2008-08-05 03:22:04.009'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o"
                    + " WHERE (o.deleted > TIMESTAMP '2008-08-05 03:22:04.009')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithString() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.$8de_leted LIKE '%s@T#'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.$8de_leted LIKE '%s@T#')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithStringChar() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted LIKE 'sT' ESCAPE 'r'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted LIKE 'sT' ESCAPE 'r')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithStringParameter() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted LIKE 'sT' ESCAPE :N";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted LIKE 'sT' ESCAPE :N)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithParameter() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted LIKE :NamedParameter";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted LIKE :NamedParameter)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithParameterChar() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted LIKE :NamedParameter Escape 'r'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted LIKE :NamedParameter ESCAPE 'r')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testLikeWithParameterParameter() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted LIKE :NamedParameter Escape :N";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted LIKE :NamedParameter ESCAPE :N)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithString() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted NOT LIKE 'sT'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT LIKE 'sT')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithStringChar() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted Not LIKE 'sT' ESCAPE 'r'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT LIKE 'sT' ESCAPE 'r')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithStringParameter() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted nOt LIKE 'sT' ESCAPE :N";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT LIKE 'sT' ESCAPE :N)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithParameter() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted not LIKE :NamedParameter";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT LIKE :NamedParameter)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithParameterChar() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted not LIKE :NamedParameter Escape 'r'";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT LIKE :NamedParameter ESCAPE 'r')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotLikeWithParameterParameter() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted NOT LIKE :NamedParameter Escape :N";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT LIKE :NamedParameter ESCAPE :N)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithLong() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted betweEn 95 and 400 ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted BETWEEN 95 AND 400)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithDouble() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted betweEn 95.43 and 400.95 ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted BETWEEN 95.43 AND 400.95)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithString() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted betweEn 'Low' and 'High' ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted BETWEEN 'Low' AND 'High')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithDate() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted betweEn DATE '2007-08-05' and DATE '2008-08-05' ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted BETWEEN DATE '2007-08-05' AND DATE '2008-08-05')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithTime() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted betweEn TIME '06:52:56' and TIME '08:52:56' ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted BETWEEN TIME '06:52:56.000' AND TIME '08:52:56.000')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testBetweenWithTimestamp() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted betweEn TIMESTAMP '2007-08-05 06:52:56.130' and "
                    + "TIMESTAMP '2008-08-05 08:52:56.130' ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted BETWEEN TIMESTAMP '2007-08-05 06:52:56.130' AND "
                    + "TIMESTAMP '2008-08-05 08:52:56.130')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithLong() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted NOT betweEn 95 and 400 ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT BETWEEN 95 AND 400)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithDouble() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted NOT betweEn 95.43 and 400.95 ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT BETWEEN 95.43 AND 400.95)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithString() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted NOT betweEn 'Low' and 'High' ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT BETWEEN 'Low' AND 'High')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithDate() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted NOT betweEn DATE '2007-08-05' and DATE '2008-08-05' ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT BETWEEN DATE '2007-08-05' AND DATE '2008-08-05')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithTime() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted NOT betweEn TIME '06:52:56' and TIME '08:52:56' ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT BETWEEN TIME '06:52:56.000' AND TIME '08:52:56.000')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNotBetweenWithTimestamp() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted NOT betweEn TIMESTAMP '2007-08-05 06:52:56.013' and "
                    + "TIMESTAMP '2008-08-05 08:52:56.013' ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT BETWEEN TIMESTAMP '2007-08-05 06:52:56.013' AND "
                    + "TIMESTAMP '2008-08-05 08:52:56.013')";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testIn() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted NOT IN (3, 4.0,:asd)";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted NOT  IN (3, 4.0, :asd))";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testNull() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted IS NOT NULL";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted IS NOT NULL)";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testComplex() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted+3*5 = 4 and (o.deleted+3)*5 = 4 "
                    + " or o.deleted LIKE 'jk' and o.deleted IS NULL ";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE ((o.deleted + (3 * 5) = 4) AND ((o.deleted + 3) * 5 = 4))"
                    + " OR ((o.deleted LIKE 'jk') AND (o.deleted IS NULL))";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCustomFunction() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted = CustomFUNCTION (o.deleted)";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted = CustomFUNCTION(o.deleted))";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------

    public final void testCustomFunctionMoreParameters() {

        try {

            String oql = "SelecT DisTinct o.item from de.jsci.pcv.jdo.LieferantJDO as o"
                    + " where o.deleted = CustomFUNCTION (o.deleted,o.edited)";
            Parser parser = new CastorQLParserAdapter();
            QueryObject qo = parser.parse(oql);
            String expected = "SELECT DISTINCT o.item FROM de.jsci.pcv.jdo.LieferantJDO AS o "
                    + "WHERE (o.deleted = CustomFUNCTION(o.deleted, o.edited))";
            String actual = qo.toString();
            // System.out.println(qo.toString());
            assertEquals(expected, actual);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        } catch (TokenManagerError tkme) {
            tkme.printStackTrace(System.out);
        }

    }

    // --------------------------------------------------------------------------
}
