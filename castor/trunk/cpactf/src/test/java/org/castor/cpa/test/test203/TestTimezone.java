/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test203;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.AbstractProperties;
import org.castor.cpa.CPAProperties;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

public final class TestTimezone extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestTimezone.class);
    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    private static final String TABLE_NAME = "test203_timezone_entity";

    private static final String DBNAME = "test203";
    private static final String MAPPING = "/org/castor/cpa/test/test203/mapping.xml";
    
    public TestTimezone(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.
    // Disabled according to problems with timezone handled at CASTOR-2814

    public boolean include(final DatabaseEngineType engine) {
        return false;
//        return (engine == DatabaseEngineType.DERBY)
//            || (engine == DatabaseEngineType.HSQL)
//            || (engine == DatabaseEngineType.MYSQL)
//            || (engine == DatabaseEngineType.POSTGRESQL);
    }
    
    public void testDate() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        LOG.debug("user.timezone = " + System.getProperty("user.timezone"));

        AbstractProperties properties = CPAProperties.newInstance();
        String testTimezone = properties.getString(CPAProperties.DEFAULT_TIMEZONE, "CET");
        LOG.debug(CPAProperties.DEFAULT_TIMEZONE + " = " + testTimezone);

        /*
         * Create a date object
         */
        String dateString = "1968-09-22 00:00:00 " + testTimezone;
        Date date = null;
        try {
            date = DF.parse(dateString);
        } catch (ParseException e) {
            LOG.error("ParseException thrown", e);
            fail("Unable to parse " + dateString);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date = cal.getTime();
        LOG.debug("Original = " + DF.format(date) + "[" + date.getTime() + "]");

        /*
         * Remove old entities from the database
         */
        LOG.debug("Remove old entities");
        db.begin();
        String sqldel = "DELETE FROM " + TABLE_NAME;
        Connection connectiondel = db.getJdbcConnection();
        Statement statementdel = connectiondel.createStatement();
        statementdel.execute(sqldel);
        db.commit();

        /*
         * Insert new entity into the database
         */
        LOG.debug("Insert new entity");
        db.begin();
        TimezoneEntity insertEntity = new TimezoneEntity();
        insertEntity.setId(new Integer(100));
        insertEntity.setName("entity 100");
        insertEntity.setStartDate(date);
        insertEntity.setStartTime(null);
        insertEntity.setStartTimestamp(null);
        db.create(insertEntity);
        db.commit();

        Integer id = insertEntity.getId();

        /*
         * Clear the cache to ensure we aren't reading cached data
         */
        LOG.debug("Clearing Castor's cache");
        db.begin();
        db.getCacheManager().expireCache();
        db.commit();

        /*
         * Fetch the object again
         */
        LOG.debug("Fetch entity with id = " + id + " with Castor");
        db.begin();
        Object fetchEntity = db.load(TimezoneEntity.class, id);
        Date castorDate = ((TimezoneEntity) fetchEntity).getStartDate();
        LOG.debug("Castor = " + DF.format(castorDate) + "[" + castorDate.getTime() + "]");
        db.commit();

        assertEquals("Castor date differs from original one!", date, castorDate);

        /*
         * Fetch using straight SQL and compare the result with our original
         * date
         */
        LOG.debug("Fetch entity with id = " + id + " with straight SQL");
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(testTimezone));
        String sql = "SELECT start_date FROM " + TABLE_NAME + " WHERE id = " + id;
        try {
            db.begin();
            Connection connection = db.getJdbcConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            Date localDate = resultSet.getDate(1);
            LOG.debug("SQL without Calendar " + DF.format(localDate) + " ["
                    + localDate.getTime() + "]");
            Date utcDate = resultSet.getDate(1, calendar);
            LOG.debug("SQL with " + testTimezone + " Calendar "
                    + DF.format(utcDate) + " [" + utcDate.getTime() + "]");
            db.commit();

            assertEquals("SQL date differs from original one!", date, utcDate);
        } catch (PersistenceException e) {
            LOG.error("PersistenceException thrown", e);
            fail();
        } catch (SQLException e) {
            LOG.error("SQLException thrown", e);
            fail();
        }

        db.close();
    }

    public void testTime() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        LOG.debug("user.timezone = " + System.getProperty("user.timezone"));

        AbstractProperties properties = CPAProperties.newInstance();
        String testTimezone = properties.getString(
            CPAProperties.DEFAULT_TIMEZONE, "CET");
        LOG.debug(CPAProperties.DEFAULT_TIMEZONE + " = " + testTimezone);

        /*
         * Create a date object
         */
        String dateString = "1968-09-22 00:00:00 " + testTimezone;
        Date date = null;
        try {
            date = DF.parse(dateString);
        } catch (ParseException e) {
            LOG.error("ParseException thrown", e);
            fail("Unable to parse " + dateString);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(1970, 0, 1);
        date = cal.getTime();
        LOG.debug("Original = " + DF.format(date) + "[" + date.getTime() + "]");

        /*
         * Remove old entities from the database
         */
        LOG.debug("Remove old entities");
        db.begin();
        String sqldel = "DELETE FROM " + TABLE_NAME;
        Connection connectiondel = db.getJdbcConnection();
        Statement statementdel = connectiondel.createStatement();
        statementdel.execute(sqldel);
        db.commit();

        /*
         * Insert new entity into the database
         */
        LOG.debug("Insert new entity");
        db.begin();
        TimezoneEntity insertEntity = new TimezoneEntity();
        insertEntity.setId(new Integer(100));
        insertEntity.setName("entity 100");
        insertEntity.setStartDate(null);
        insertEntity.setStartTime(date);
        insertEntity.setStartTimestamp(null);
        db.create(insertEntity);
        db.commit();

        Integer id = insertEntity.getId();

        /*
         * Clear the cache to ensure we aren't reading cached data
         */
        LOG.debug("Clearing Castor's cache");
        db.begin();
        db.getCacheManager().expireCache();
        db.commit();

        /*
         * Fetch the object again
         */
        LOG.debug("Fetch entity with id = " + id + " with Castor");
        db.begin();
        Object fetchEntity = db.load(TimezoneEntity.class, id);
        Date castorDate = ((TimezoneEntity) fetchEntity).getStartTime();
        LOG.debug("Castor = " + DF.format(castorDate) + "["
                + castorDate.getTime() + "]");
        db.commit();

        assertEquals("Castor date differs from original one!", date, castorDate);

        /*
         * Fetch using straight SQL and compare the result with our original date
         */
        LOG.debug("Fetch entity with id = " + id + " with straight SQL");
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(testTimezone));
        String sql = "SELECT start_time FROM " + TABLE_NAME + " WHERE id = " + id;
        try {
            db.begin();
            Connection connection = db.getJdbcConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            Date localDate = resultSet.getTime(1);
            LOG.debug("SQL without Calendar " + DF.format(localDate) + " ["
                    + localDate.getTime() + "]");
            Date utcDate = resultSet.getTime(1, calendar);
            LOG.debug("SQL with " + testTimezone + " Calendar "
                    + DF.format(utcDate) + " [" + utcDate.getTime() + "]");
            db.commit();

            assertEquals("SQL date differs from original one!", date, utcDate);
        } catch (PersistenceException e) {
            LOG.error("PersistenceException thrown", e);
            fail();
        } catch (SQLException e) {
            LOG.error("SQLException thrown", e);
            fail();
        }

        db.close();
    }

    public void testTimestamp() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        LOG.debug("user.timezone = " + System.getProperty("user.timezone"));

        AbstractProperties properties = CPAProperties.newInstance();
        String testTimezone = properties.getString(
            CPAProperties.DEFAULT_TIMEZONE, "CET");
        LOG.debug(CPAProperties.DEFAULT_TIMEZONE + " = " + testTimezone);

        /*
         * Create a date object
         */
        String dateString = "1968-09-22 00:00:00 " + testTimezone;
        Date date = null;
        try {
            date = DF.parse(dateString);
        } catch (ParseException e) {
            LOG.error("ParseException thrown", e);
            fail("Unable to parse " + dateString);
        }
        LOG.debug("Original = " + DF.format(date) + "[" + date.getTime() + "]");

        /*
         * Remove old entities from the database
         */
        LOG.debug("Remove old entities");
        db.begin();
        String sqldel = "DELETE FROM " + TABLE_NAME;
        Connection connectiondel = db.getJdbcConnection();
        Statement statementdel = connectiondel.createStatement();
        statementdel.execute(sqldel);
        db.commit();

        /*
         * Insert new entity into the database
         */
        LOG.debug("Insert new entity");
        db.begin();
        TimezoneEntity insertEntity = new TimezoneEntity();
        insertEntity.setId(new Integer(100));
        insertEntity.setName("entity 100");
        insertEntity.setStartDate(null);
        insertEntity.setStartTime(null);
        insertEntity.setStartTimestamp(date);
        db.create(insertEntity);
        db.commit();

        Integer id = insertEntity.getId();

        /*
         * Clear the cache to ensure we aren't reading cached data
         */
        LOG.debug("Clearing Castor's cache");
        db.begin();
        db.getCacheManager().expireCache();
        db.commit();

        /*
         * Fetch the object again
         */
        LOG.debug("Fetch entity with id = " + id + " with Castor");
        db.begin();
        Object fetchEntity = db.load(TimezoneEntity.class, id);
        Date castorDate = ((TimezoneEntity) fetchEntity).getStartTimestamp();
        LOG.debug("Castor = " + DF.format(castorDate) + "[" + castorDate.getTime() + "]");
        db.commit();

        assertEquals("Castor date differs from original one!", date, castorDate);

        /*
         * Fetch using straight SQL and compare the result with our original date
         */
        LOG.debug("Fetch entity with id = " + id + " with straight SQL");
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(testTimezone));
        String sql = "SELECT start_stamp FROM " + TABLE_NAME + " WHERE id = " + id;
        try {
            db.begin();
            Connection connection = db.getJdbcConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            Date localDate = resultSet.getTimestamp(1);
            LOG.debug("SQL without Calendar " + DF.format(localDate) + " ["
                    + localDate.getTime() + "]");
            Date utcDate = resultSet.getTimestamp(1, calendar);
            LOG.debug("SQL with " + testTimezone + " Calendar "
                    + DF.format(utcDate) + " [" + utcDate.getTime() + "]");
            db.commit();

            assertEquals("SQL date differs from original one!", date, utcDate);
        } catch (PersistenceException e) {
            LOG.error("PersistenceException thrown", e);
            fail();
        } catch (SQLException e) {
            LOG.error("SQLException thrown", e);
            fail();
        }

        db.close();
    }
}
