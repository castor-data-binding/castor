/*
 * Copyright 2005 Nissim Karpenstein, Stein M. Hugubakken
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
package org.exolab.castor.jdo.oql;

/**
 * Used to store Token Type Constants.
 * 
 * @author <a href="nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date$
 */
public final class TokenType {
    public static final int END_OF_QUERY = 0;
    public static final int KEYWORD_SELECT = 1;
    public static final int IDENTIFIER = 2;
    public static final int KEYWORD_AS = 3;
    public static final int COLON = 4;
    public static final int KEYWORD_FROM = 5;
    public static final int KEYWORD_IN = 6;
    public static final int KEYWORD_WHERE = 7;
    public static final int KEYWORD_OR = 8;
    public static final int KEYWORD_AND = 9;
    public static final int EQUAL = 10;
    public static final int NOT_EQUAL = 11;
    public static final int KEYWORD_LIKE = 12;
    public static final int LT = 13;
    public static final int LTE = 14;
    public static final int GT = 15;
    public static final int GTE = 16;
    public static final int PLUS = 17;
    public static final int MINUS = 18;
    public static final int CONCAT = 19; // || string concatenation operator
    public static final int TIMES = 20;
    public static final int DIVIDE = 21;
    public static final int KEYWORD_MOD = 22;
    public static final int KEYWORD_ABS = 23;
    public static final int KEYWORD_NOT = 24;
    public static final int LPAREN = 25;
    public static final int RPAREN = 26;
    public static final int DOLLAR = 27;
    public static final int KEYWORD_NIL = 28;
    public static final int KEYWORD_UNDEFINED = 29;
    public static final int DOT = 30;
    public static final int ARROW = 31; // -> method call operator
    public static final int BOOLEAN_LITERAL = 32;
    public static final int LONG_LITERAL = 33;
    public static final int DOUBLE_LITERAL = 34;
    public static final int CHAR_LITERAL = 35;
    public static final int STRING_LITERAL = 36;
    public static final int DATE_LITERAL = 37;
    public static final int TIME_LITERAL = 38;
    public static final int TIMESTAMP_LITERAL = 39;
    public static final int KEYWORD_BETWEEN = 40;
    public static final int KEYWORD_DISTINCT = 41;
    public static final int KEYWORD_IS_DEFINED = 42;
    public static final int KEYWORD_IS_UNDEFINED = 43;
    public static final int KEYWORD_LIST = 44;
    public static final int COMMA = 45;
    public static final int KEYWORD_ORDER = 46;
    public static final int KEYWORD_BY = 47;
    public static final int KEYWORD_ASC = 48;
    public static final int KEYWORD_DESC = 49;
    public static final int KEYWORD_COUNT = 50;
    public static final int KEYWORD_SUM = 51;
    public static final int KEYWORD_MIN = 52;
    public static final int KEYWORD_MAX = 53;
    public static final int KEYWORD_AVG = 54;
    public static final int KEYWORD_LIMIT = 55;
    public static final int KEYWORD_OFFSET = 56;

    private TokenType() { }
}
