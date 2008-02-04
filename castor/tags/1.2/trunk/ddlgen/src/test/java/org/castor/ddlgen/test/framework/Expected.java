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

import java.net.URL;
import java.util.ArrayList;

import org.castor.ddlgen.DDLGenConfiguration;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

/**
 * This class represent the expected result for various database 
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class Expected {
    //--------------------------------------------------------------------------

    /** Mapping to unmarshal expected result patterns. */
    private static final String MAPPING = "mapping.xml";

    /** Generic database engine. */
    public static final String ENGINE_GENERIC = null;

    /** MySQL database engine. */
    public static final String ENGINE_MYSQL = "mysql";

    /** PostgreSQL database engine. */
    public static final String ENGINE_POSTGRESQL = "postgresql";

    /** HSQL database engine. */
    public static final String ENGINE_HSQL = "hsql";

    /** DB2 database engine. */
    public static final String ENGINE_DB2 = "db2";

    /** Oracle database engine. */
    public static final String ENGINE_ORACLE = "oracle";

    /** SAPDB database engine. */
    public static final String ENGINE_SAPDB = "sapdb";

    /** Derby database engine. */
    public static final String ENGINE_DERBY = "derby";

    /** MSSQL database engine. */
    public static final String ENGINE_MSSQL = "mssql";

    /** PointBase database engine. */
    public static final String ENGINE_POINTBASE = "pointbase";

    /** Sybase database engine. */
    public static final String ENGINE_SYBASE = "sybase";

    //--------------------------------------------------------------------------

    /** Configuration of DDL generator. */
    private DDLGenConfiguration _conf;
    
    /** Expected result patterns to match. */
    private ArrayList _patterns;

    /** Last match pattern used. */
    private transient Match _match;

    //--------------------------------------------------------------------------

    /**
     * Unmarshal expected result patterns from given file with given name.
     * 
     * @param expected Name of the file with the expected result patterns.
     * @return Expected result patterns.
     * @throws Exception If any exception occured during unmarshalling of expected result
     *         patterns.
     */
    public static Expected getExpectedResult(final URL expected) throws Exception {
        // 1. Load the mapping information from the file
        Mapping mapping = new Mapping();
        mapping.loadMapping(Expected.class.getResource(MAPPING));
        
        // 2. Unmarshal the data
        Unmarshaller unmar = new Unmarshaller(mapping);
        InputSource source = new InputSource(expected.toExternalForm());
        return (Expected) unmar.unmarshal(source);
    }

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
        
        for (int i = 0; i < _patterns.size(); i++) {
            Match match = (Match) _patterns.get(i);
            match.setConf(conf);
        }
    }

    /**
     * Get expected result patterns to match.
     * 
     * @return Expected result patterns to match.
     */
    public ArrayList getPatterns() {
        return _patterns;
    }

    /**
     * Set expected result patterns to match.
     * 
     * @param patterns Expected result patterns to match.
     */
    public void setPatterns(final ArrayList patterns) {
        _patterns = patterns;
    }

    //--------------------------------------------------------------------------

    /**
     * Match generated DDL given with expected result pattern of given database engine.
     * 
     * @param engine Database engine the expected result is defined for.
     * @param ddl Generated DDL.
     * @return <code>true</code> if generated DDL matches expected result.
     */
    public boolean match(final String engine, final String ddl) {
        return match(engine, ddl, null);
    }

    /**
     * Match generated DDL given with expected result pattern of given database engine.
     * Replace any parameters of expected result pattern with given parameter values
     * before comparing with generated DDL.
     * 
     * @param engine Database engine the expected result is defined for.
     * @param ddl Generated DDL.
     * @param params Values for parameters in expected result.
     * @return <code>true</code> if generated DDL matches expected result.
     */
    public boolean match(final String engine, final String ddl, final Object[] params) {
        _match = getPatternByEngine(engine);

        try {
            return _match.match(ddl, params);
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Match generated DDL given with expected result pattern of given database engine
     * at given index. Replace any parameters of expected result pattern with given
     * parameter values before comparing with generated DDL.
     * 
     * @param engine Database engine the expected result is defined for.
     * @param index Index of generated DDL and expected result to compare.
     * @param ddl Generated DDL.
     * @param params Values for parameters in expected result.
     * @return <code>true</code> if generated DDL matches expected result.
     */
    public boolean match(final String engine, final int index,
            final String ddl, final Object[] params) {

        _match = getPatternByEngineAndIndex(engine, index);

        try {
            return _match.match(ddl, params);
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Get string describing the last used match pattern.
     * 
     * @return String describing the last used match pattern.
     */
    public String getLastMatchString() {
        return (_match != null) ? _match.toString() : null;
    }

    //--------------------------------------------------------------------------
    
    /**
     * Get expected result pattern of given database engine.
     * 
     * @param engine Database engine the expected result is searched for.
     * @return Expected result pattern. 
     */
    private Match getPatternByEngine(final String engine) {
        for (int i = 0; i < _patterns.size(); i++) {
            Match match = (Match) _patterns.get(i);
            if (match.getEngine() == null) {
                if (engine == null) { return match; }
            } else if (match.getEngine().equals(engine)) {
                return match;
            }
        }
        
        return null;
    }

    /**
     * Get expected result pattern with index of given database engine.
     * 
     * @param engine Database engine the expected result is searched for.
     * @param index Index of the expected result pattern to search for.
     * @return Expected result pattern. 
     */
    private Match getPatternByEngineAndIndex(final String engine, final int index) {
        for (int i = 0; i < _patterns.size(); i++) {
            Match match = (Match) _patterns.get(i);
            if (match.getEngine() == null) {
                if ((engine == null) && (index == match.getIndex())) { return match; }
            } else if (match.getEngine().equals(engine) && (match.getIndex() == index)) {
                return match;
            }
        }
        
        return null;
    }

    //--------------------------------------------------------------------------
}
