/*
 * Copyright 2006 Le Duc Bao, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.ddlgen.test.framework;

import java.text.MessageFormat;

import org.castor.ddlgen.DDLGenConfiguration;

/**
 * Expected result pattern to match.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class Match {
    //--------------------------------------------------------------------------
    
    /** Match generated DDL and expected result exactly. */
    public static final String EXACT = "exact";

    /** Match generated DDL and expected result by ignoring whitespaces. */
    public static final String PLAIN = "plain";

    /** Match generated DDL and expected result with regular expression. */
    public static final String REGEXP = "regexp";

    //--------------------------------------------------------------------------
    
    /** Configuration of DDL generator. */
    private DDLGenConfiguration _conf;
    
    /** Method used to compare generated DDL and expected result. */    
    private String _method;

    /** Index of generated DDL and expected result to compare. */
    private int _index;
    
    /** Database engine the expected result is defined for. */
    private String _engine;

    /** Expected result pattern. */    
    private String _pattern;

    //--------------------------------------------------------------------------

    /**
     * Get configuration of DDL generator.
     * 
     * @return Configuration of DDL generator.
     */
    public DDLGenConfiguration getConf() {
        return _conf;
    }


    /**
     * Set configuration of DDL generator.
     * 
     * @param conf Configuration of DDL generator.
     */
    public void setConf(final DDLGenConfiguration conf) {
        _conf = conf;
    }

    /**
     * Get method used to compare generated DDL and expected result.
     * 
     * @return Method used to compare generated DDL and expected result.
     */
    public String getMethod() {
        return _method;
    }

    /**
     * Set method used to compare generated DDL and expected result.
     * 
     * @param method Method used to compare generated DDL and expected result.
     */
    public void setMethod(final String method) {
        _method = method;
    }        

    /**
     * Get index of generated DDL and expected result to compare.
     * 
     * @return Index of generated DDL and expected result to compare.
     */
    public int getIndex() {
        return _index;
    }

    /**
     * Set index of generated DDL and expected result to compare.
     * 
     * @param index Index of generated DDL and expected result to compare.
     */
    public void setIndex(final int index) {
        _index = index;
    }

    /**
     * Get database engine the expected result is defined for.
     * 
     * @return Database engine the expected result is defined for.
     */
    public String getEngine() {
        return _engine;
    }

    /**
     * Set database engine the expected result is defined for.
     * 
     * @param engine Database engine the expected result is defined for.
     */
    public void setEngine(final String engine) {
        _engine = engine;
    }

    /**
     * Get expected result pattern.
     * 
     * @return Expected result pattern.
     */
    public String getDdl() {
        return _pattern;
    }


    /**
     * Set expected result pattern.
     * 
     * @param pattern Expected result pattern. 
     */
    public void setDdl(final String pattern) {
        _pattern = pattern;
    }

    //--------------------------------------------------------------------------

    /**
     * Match generated DDL given with expected result pattern.
     * 
     * @param ddl Generated DDL.
     * @return <code>true</code> if generated DDL matches expected result.
     */
    public boolean match(final String ddl) {
        return match(ddl, null);
    }

    /**
     * Match generated DDL given with expected result pattern. Replace any parameters of
     * expected result pattern with given parameter values before comparing with
     * generated DDL.
     * 
     * @param ddl Generated DDL.
     * @param params Values for parameters in expected result.
     * @return <code>true</code> if generated DDL matches expected result.
     */
    public boolean match(final String ddl, final Object[] params) {
        if (_method.equals(REGEXP)) {
            return matchRegExp(ddl, params);
        }
        
        if (_method.equals(PLAIN)) {
            return matchPlain(ddl, params);
        }
        
        if (_method.equals(EXACT)) {
            return matchExact(ddl, params);            
        }
        
        throw new IllegalStateException("Unsupport match method: " + _method);
    }

    /**
     * Exactly match generated DDL given with expected result. Replace any parameters of
     * expected result pattern with given parameter values before comparing with
     * generated DDL.
     * 
     * @param ddl Generated DDL.
     * @param params Values for parameters in expected result.
     * @return <code>true</code> if generated DDL matches expected result.
     */
    private boolean matchExact(final String ddl, final Object[] params) {
        String actual = ddl.toLowerCase().trim();
        
        String expected = _pattern;
        if (expected == null) { expected = ""; }

        if ((params != null) && (params.length != 0)) {
            expected = MessageFormat.format(expected, params);
        }
        
        expected = expected.toLowerCase().trim();
        
        return actual.equals(expected);
    }

    /**
     * Match generated DDL given with expected result ignoring whitespace. Replace any
     * parameters of expected result pattern with given parameter values before comparing
     * with generated DDL.
     * 
     * @param ddl Generated DDL.
     * @param params Values for parameters in expected result.
     * @return <code>true</code> if generated DDL matches expected result.
     */
    private boolean matchPlain(final String ddl, final Object[] params) {
        String newline = _conf.getStringValue(
                DDLGenConfiguration.NEWLINE_KEY, DDLGenConfiguration.DEFAULT_NEWLINE);

        String actual = ddl.toLowerCase().trim();
        actual = actual.replaceAll(newline, " ");
        actual = actual.replaceAll("[ \t]+", " ").trim();
        
        String expected = _pattern;
        if (expected == null) { expected = ""; }

        if ((params != null) && (params.length != 0)) {
            expected = MessageFormat.format(expected, params);
        }
        
        expected = expected.toLowerCase().trim();
        
        expected = expected.replaceAll(newline, " ");
        expected = expected.replaceAll("[ \t]+", " ");
        
        return actual.equals(expected);
    }

    /**
     * Match generated DDL given with expected regular expression. Replace any parameters
     * of expected result pattern with given parameter values before comparing with
     * generated DDL.
     * 
     * @param ddl Generated DDL.
     * @param params Values for parameters in expected result.
     * @return <code>true</code> if generated DDL matches expected result.
     */
    private boolean matchRegExp(final String ddl, final Object[] params) {
        String newline = _conf.getStringValue(
                DDLGenConfiguration.NEWLINE_KEY, DDLGenConfiguration.DEFAULT_NEWLINE);
        String indent = _conf.getStringValue(
                DDLGenConfiguration.INDENT_KEY, DDLGenConfiguration.DEFAULT_INDENT);

        String actual = ddl.toLowerCase().trim();

        actual = actual.replaceAll(newline, " ");
        actual = actual.replaceAll(indent, " ");
        actual = actual.replaceAll("[ \t]+", " ");
        
        String expected = _pattern;
        if (expected == null) { expected = ""; }

        if ((params != null) && (params.length != 0)) {
            expected = MessageFormat.format(expected, params);
        }
        
        expected = expected.toLowerCase().trim();
        
        expected = expected.replaceAll(newline, " ");
        expected = expected.replaceAll("[ \t]+", " ");

        return actual.matches(expected);
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "match(method=" + _method + " index=" + _index + " engine=" + _engine
             + "):\n" + _pattern;
    }
    
    //--------------------------------------------------------------------------
}
