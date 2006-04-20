/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.jdo.oql;

/**
 * Interface used to store Token Type Constants.
 *
 * @author <a href="nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date$
 */
public interface TokenTypes {
  int END_OF_QUERY = 0;
  int KEYWORD_SELECT = 1;
  int IDENTIFIER = 2;
  int KEYWORD_AS = 3;
  int COLON = 4;
  int KEYWORD_FROM = 5;
  int KEYWORD_IN = 6;
  int KEYWORD_WHERE = 7;
  int KEYWORD_OR = 8;
  int KEYWORD_AND = 9;
  int EQUAL = 10;
  int NOT_EQUAL = 11;
  int KEYWORD_LIKE = 12;
  int LT = 13;
  int LTE = 14;
  int GT = 15;
  int GTE = 16;
  int PLUS = 17;
  int MINUS = 18;
  int CONCAT = 19;  // || string concatenation operator
  int TIMES = 20;
  int DIVIDE = 21;
  int KEYWORD_MOD = 22;
  int KEYWORD_ABS = 23;
  int KEYWORD_NOT = 24;
  int LPAREN = 25;
  int RPAREN = 26;
  int DOLLAR = 27;
  int KEYWORD_NIL = 28;
  int KEYWORD_UNDEFINED = 29;
  int DOT = 30;
  int ARROW = 31; // -> method call operator
  int BOOLEAN_LITERAL = 32;
  int LONG_LITERAL = 33;
  int DOUBLE_LITERAL = 34;
  int CHAR_LITERAL = 35;
  int STRING_LITERAL = 36;
  int DATE_LITERAL = 37;
  int TIME_LITERAL = 38;
  int TIMESTAMP_LITERAL = 39;
  int KEYWORD_BETWEEN = 40;
  int KEYWORD_DISTINCT = 41;
  int KEYWORD_IS_DEFINED = 42;
  int KEYWORD_IS_UNDEFINED = 43;
  int KEYWORD_LIST = 44;
  int COMMA = 45;
  int KEYWORD_ORDER = 46;
  int KEYWORD_BY = 47;
  int KEYWORD_ASC = 48;
  int KEYWORD_DESC = 49;
  int KEYWORD_COUNT = 50;
  int KEYWORD_SUM = 51;
  int KEYWORD_MIN = 52;
  int KEYWORD_MAX = 53;
  int KEYWORD_AVG = 54;
  int KEYWORD_LIMIT = 55;//Alex
}
