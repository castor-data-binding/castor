/*
 * Copyright 2005 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann, Ralf Joachim
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
 *
 * $Id$
 */
package org.exolab.castor.jdo.engine;

import org.exolab.castor.mapping.TypeConvertor;

/**
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:yip AT intalio DOT com">Thomas Yip</a>
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class SQLColumnInfo {
    /** Name of the column. */
    private final String _name;

    /** SQL type of teh coplumn. */
    private final int _sqlType;

    /** TypeConvertor to use when converting to the SQLType of this column. */
    private final TypeConvertor _convertTo;

    /** TypeConvertor to use when converting from the SQLType of this column. */
    private final TypeConvertor _convertFrom;

    /** Type conversion parameters. */
    private final String _convertParam;

    public SQLColumnInfo(final String name, final int type,
                      final TypeConvertor convertTo,
                      final TypeConvertor convertFrom,
                      final String convertParam) {
        
        _name = name;
        _sqlType = type;
        _convertTo = convertTo;
        _convertFrom = convertFrom;
        _convertParam = convertParam;
    }

    public String getName() { return _name; }

    public int getSqlType() { return _sqlType; }

    public TypeConvertor getConvertTo() { return _convertTo; }

    public TypeConvertor getConvertFrom() { return _convertFrom; }

    public String getConvertParam() { return _convertParam; }

    public Object toSQL(final Object object) {
        if ((object == null) || (_convertFrom == null)) {
            return object;
        }
        return _convertFrom.convert(object, _convertParam);
    }

    public Object toJava(final Object object) {
        if ((object == null) || (_convertTo == null)) {
            return object;
        }
        return _convertTo.convert(object, _convertParam);
    }
}
