/*
 * Copyright 2007 Ralf Joachim
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
package ctf.jdo.tc20x;

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

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Configuration;
import org.castor.cpa.CPAConfiguration;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

public final class TestTimezone extends CastorTestCase {
    private static final Log LOG = LogFactory.getLog(TestTimezone.class);
    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    private static final String TABLE_NAME = "tc203_timezone_entity";
    
    private JDOCategory _category;

    public TestTimezone(final TestHarness category) {
        super(category, "TC203", "Timezone tests");
        _category = (JDOCategory) category;
    }

    public void runTest() throws Exception {
        testDate();
        testTime();
        testTimestamp();
    }
    
    public void testDate() throws Exception {
        LOG.debug("user.timezone = " + System.getProperty("user.timezone"));
        
        Configuration config = CPAConfiguration.newInstance();
        String testTimezone = config.getString(CPAConfiguration.DEFAULT_TIMEZONE);
        LOG.debug(CPAConfiguration.DEFAULT_TIMEZONE + " = " + testTimezone);
        
        Database database = _category.getDatabase();

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
        database.begin();
        String sqldel = "DELETE FROM " + TABLE_NAME;
        Connection connectiondel = database.getJdbcConnection();
        Statement statementdel = connectiondel.createStatement();
        statementdel.execute(sqldel);
        database.commit();

        /*
         * Insert new entity into the database
         */
        LOG.debug("Insert new entity");
        database.begin();
        TimezoneEntity insertEntity = new TimezoneEntity();
        insertEntity.setId(new Integer(100));
        insertEntity.setName("entity 100");
        insertEntity.setStartDate(date);
        insertEntity.setStartTime(null);
        insertEntity.setStartTimestamp(null);
        database.create(insertEntity);
        database.commit();

        Integer id = insertEntity.getId();
        
        /*
         * Clear the cache to ensure we aren't reading cached data
         */
        LOG.debug("Clearing Castor's cache");
        database.begin();
        database.getCacheManager().expireCache();
        database.commit();

        /*
         * Fetch the object again
         */
        LOG.debug("Fetch entity with id = " + id + " with Castor");
        database.begin();
        Object fetchEntity = database.load(TimezoneEntity.class, id);
        Date castorDate = ((TimezoneEntity) fetchEntity).getStartDate();
        LOG.debug("Castor = " + DF.format(castorDate) + "[" + castorDate.getTime() + "]");
        database.commit();
        
        assertEquals("Castor date differs from original one!", date, castorDate);

        /*
         * Fetch using straight SQL and compare the result with our original date
         */
        LOG.debug("Fetch entity with id = " + id + " with straight SQL");
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        String sql = "SELECT start_date FROM " + TABLE_NAME + " WHERE id = " + id;
        try {
            database.begin();
            Connection connection = database.getJdbcConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            Date localDate = resultSet.getDate(1);
            LOG.debug("SQL without Calendar " + DF.format(localDate)
                    + " [" + localDate.getTime() + "]");
            Date utcDate = resultSet.getDate(1, calendar);
            LOG.debug("SQL with " + testTimezone + " Calendar " + DF.format(utcDate)
                    + " [" + utcDate.getTime() + "]");
            database.commit();
            
            assertEquals("SQL date differs from original one!", date, localDate);
        } catch (PersistenceException e) {
            LOG.error("PersistenceException thrown", e);
            fail();
        } catch (SQLException e) {
            LOG.error("SQLException thrown", e);
            fail();
        }

        database.close();
        
        LOG.debug("user.timezone = " + System.getProperty("user.timezone"));
    }

    public void testTime() throws Exception {
        LOG.debug("user.timezone = " + System.getProperty("user.timezone"));
        
        Configuration config = CPAConfiguration.newInstance();
        String testTimezone = config.getString(CPAConfiguration.DEFAULT_TIMEZONE);
        LOG.debug(CPAConfiguration.DEFAULT_TIMEZONE + " = " + testTimezone);
        
        Database database = _category.getDatabase();

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
        database.begin();
        String sqldel = "DELETE FROM " + TABLE_NAME;
        Connection connectiondel = database.getJdbcConnection();
        Statement statementdel = connectiondel.createStatement();
        statementdel.execute(sqldel);
        database.commit();

        /*
         * Insert new entity into the database
         */
        LOG.debug("Insert new entity");
        database.begin();
        TimezoneEntity insertEntity = new TimezoneEntity();
        insertEntity.setId(new Integer(100));
        insertEntity.setName("entity 100");
        insertEntity.setStartDate(null);
        insertEntity.setStartTime(date);
        insertEntity.setStartTimestamp(null);
        database.create(insertEntity);
        database.commit();

        Integer id = insertEntity.getId();
        
        /*
         * Clear the cache to ensure we aren't reading cached data
         */
        LOG.debug("Clearing Castor's cache");
        database.begin();
        database.getCacheManager().expireCache();
        database.commit();

        /*
         * Fetch the object again
         */
        LOG.debug("Fetch entity with id = " + id + " with Castor");
        database.begin();
        Object fetchEntity = database.load(TimezoneEntity.class, id);
        Date castorDate = ((TimezoneEntity) fetchEntity).getStartTime();
        LOG.debug("Castor = " + DF.format(castorDate) + "[" + castorDate.getTime() + "]");
        database.commit();
        
        assertEquals("Castor date differs from original one!", date, castorDate);

        /*
         * Fetch using straight SQL and compare the result with our original date
         */
        LOG.debug("Fetch entity with id = " + id + " with straight SQL");
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        String sql = "SELECT start_time FROM " + TABLE_NAME + " WHERE id = " + id;
        try {
            database.begin();
            Connection connection = database.getJdbcConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            Date localDate = resultSet.getTime(1);
            LOG.debug("SQL without Calendar " + DF.format(localDate)
                    + " [" + localDate.getTime() + "]");
            Date utcDate = resultSet.getTime(1, calendar);
            LOG.debug("SQL with " + testTimezone + " Calendar " + DF.format(utcDate)
                    + " [" + utcDate.getTime() + "]");
            database.commit();
            
            assertEquals("SQL date differs from original one!", date, utcDate);
        } catch (PersistenceException e) {
            LOG.error("PersistenceException thrown", e);
            fail();
        } catch (SQLException e) {
            LOG.error("SQLException thrown", e);
            fail();
        }

        database.close();
        
        LOG.debug("user.timezone = " + System.getProperty("user.timezone"));
    }

    public void testTimestamp() throws Exception {
        LOG.debug("user.timezone = " + System.getProperty("user.timezone"));
        
        Configuration config = CPAConfiguration.newInstance();
        String testTimezone = config.getString(CPAConfiguration.DEFAULT_TIMEZONE);
        LOG.debug(CPAConfiguration.DEFAULT_TIMEZONE + " = " + testTimezone);
        
        Database database = _category.getDatabase();

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
        database.begin();
        String sqldel = "DELETE FROM " + TABLE_NAME;
        Connection connectiondel = database.getJdbcConnection();
        Statement statementdel = connectiondel.createStatement();
        statementdel.execute(sqldel);
        database.commit();

        /*
         * Insert new entity into the database
         */
        LOG.debug("Insert new entity");
        database.begin();
        TimezoneEntity insertEntity = new TimezoneEntity();
        insertEntity.setId(new Integer(100));
        insertEntity.setName("entity 100");
        insertEntity.setStartDate(null);
        insertEntity.setStartTime(null);
        insertEntity.setStartTimestamp(date);
        database.create(insertEntity);
        database.commit();

        Integer id = insertEntity.getId();
        
        /*
         * Clear the cache to ensure we aren't reading cached data
         */
        LOG.debug("Clearing Castor's cache");
        database.begin();
        database.getCacheManager().expireCache();
        database.commit();

        /*
         * Fetch the object again
         */
        LOG.debug("Fetch entity with id = " + id + " with Castor");
        database.begin();
        Object fetchEntity = database.load(TimezoneEntity.class, id);
        Date castorDate = ((TimezoneEntity) fetchEntity).getStartTimestamp();
        LOG.debug("Castor = " + DF.format(castorDate) + "[" + castorDate.getTime() + "]");
        database.commit();
        
        assertEquals("Castor date differs from original one!", date, castorDate);

        /*
         * Fetch using straight SQL and compare the result with our original date
         */
        LOG.debug("Fetch entity with id = " + id + " with straight SQL");
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        String sql = "SELECT start_stamp FROM " + TABLE_NAME + " WHERE id = " + id;
        try {
            database.begin();
            Connection connection = database.getJdbcConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            Date localDate = resultSet.getTimestamp(1);
            LOG.debug("SQL without Calendar " + DF.format(localDate)
                    + " [" + localDate.getTime() + "]");
            Date utcDate = resultSet.getTimestamp(1, calendar);
            LOG.debug("SQL with " + testTimezone + " Calendar " + DF.format(utcDate)
                    + " [" + utcDate.getTime() + "]");
            database.commit();
            
            assertEquals("SQL date differs from original one!", date, utcDate);
        } catch (PersistenceException e) {
            LOG.error("PersistenceException thrown", e);
            fail();
        } catch (SQLException e) {
            LOG.error("SQLException thrown", e);
            fail();
        }

        database.close();
        
        LOG.debug("user.timezone = " + System.getProperty("user.timezone"));
    }
}
