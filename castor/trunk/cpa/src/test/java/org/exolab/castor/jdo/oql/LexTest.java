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
 */
package org.exolab.castor.jdo.oql;

import java.util.Hashtable;

import junit.framework.TestCase;

/**
 * Test class for {@link Lexer}. 
 *
 * @author  <a href="nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date: 2006-04-29 05:45:43 -0600 (Sat, 29 Apr 2006) $
 */
public final class LexTest extends TestCase {
    public void testLexer() throws Exception {
        Hashtable tokenTypes = new Hashtable();
        tokenTypes.put(new Integer(TokenType.END_OF_QUERY), "END_OF_QUERY");
        tokenTypes.put(new Integer(TokenType.KEYWORD_SELECT), "KEYWORD_SELECT");
        tokenTypes.put(new Integer(TokenType.IDENTIFIER), "IDENTIFIER");
        tokenTypes.put(new Integer(TokenType.KEYWORD_AS), "KEYWORD_AS");
        tokenTypes.put(new Integer(TokenType.COLON), "COLON");
        tokenTypes.put(new Integer(TokenType.KEYWORD_FROM), "KEYWORD_FROM");
        tokenTypes.put(new Integer(TokenType.KEYWORD_IN), "KEYWORD_IN");
        tokenTypes.put(new Integer(TokenType.KEYWORD_WHERE), "KEYWORD_WHERE");
        tokenTypes.put(new Integer(TokenType.KEYWORD_OR), "KEYWORD_OR");
        tokenTypes.put(new Integer(TokenType.KEYWORD_AND), "KEYWORD_AND");
        tokenTypes.put(new Integer(TokenType.EQUAL), "EQUAL"); // 10
        tokenTypes.put(new Integer(TokenType.NOT_EQUAL), "NOT_EQUAL");
        tokenTypes.put(new Integer(TokenType.KEYWORD_LIKE), "KEYWORD_LIKE");
        tokenTypes.put(new Integer(TokenType.LT), "LT");
        tokenTypes.put(new Integer(TokenType.LTE), "LTE");
        tokenTypes.put(new Integer(TokenType.GT), "GT");
        tokenTypes.put(new Integer(TokenType.GTE), "GTE");
        tokenTypes.put(new Integer(TokenType.PLUS), "PLUS");
        tokenTypes.put(new Integer(TokenType.MINUS), "MINUS");
        tokenTypes.put(new Integer(TokenType.CONCAT), "CONCAT");
        tokenTypes.put(new Integer(TokenType.TIMES), "TIMES"); // 20
        tokenTypes.put(new Integer(TokenType.DIVIDE), "DIVIDE");
        tokenTypes.put(new Integer(TokenType.KEYWORD_MOD), "KEYWORD_MOD");
        tokenTypes.put(new Integer(TokenType.KEYWORD_ABS), "KEYWORD_ABS");
        tokenTypes.put(new Integer(TokenType.KEYWORD_NOT), "KEYWORD_NOT");
        tokenTypes.put(new Integer(TokenType.LPAREN), "LPAREN");
        tokenTypes.put(new Integer(TokenType.RPAREN), "RPAREN");
        tokenTypes.put(new Integer(TokenType.DOLLAR), "DOLLAR");
        tokenTypes.put(new Integer(TokenType.KEYWORD_NIL), "KEYWORD_NIL");
        tokenTypes.put(new Integer(TokenType.KEYWORD_UNDEFINED), "KEYWORD_UNDEFINED");
        tokenTypes.put(new Integer(TokenType.BOOLEAN_LITERAL), "BOOLEAN_LITERAL");
        tokenTypes.put(new Integer(TokenType.LONG_LITERAL), "LONG_LITERAL");
        tokenTypes.put(new Integer(TokenType.DOUBLE_LITERAL), "DOUBLE_LITERAL"); // 30
        tokenTypes.put(new Integer(TokenType.CHAR_LITERAL), "CHAR_LITERAL");
        tokenTypes.put(new Integer(TokenType.STRING_LITERAL), "STRING_LITERAL");
        tokenTypes.put(new Integer(TokenType.DATE_LITERAL), "DATE_LITERAL");
        tokenTypes.put(new Integer(TokenType.TIME_LITERAL), "TIME_LITERAL");
        tokenTypes.put(new Integer(TokenType.TIMESTAMP_LITERAL), "TIMESTAMP_LITERAL");
        tokenTypes.put(new Integer(TokenType.KEYWORD_BETWEEN), "KEYWORD_BETWEEN");

        Lexer lexer = new Lexer("select o from Product o where o.xyz=$1 order by o.id");
        
        while (lexer.hasMoreTokens()) {
            try {
                Token token = lexer.nextToken();
                String type = (String) tokenTypes.get(new Integer(token.getTokenType()));
                System.out.println(type + " : " + token.getTokenValue());
            } catch (Exception ex) {
                fail(ex.getMessage());
            }
        }
    }
}
