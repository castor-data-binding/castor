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

import java.lang.String;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Test class for {@link Parser}. 
 *
 * @author  <a href="nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date$
 */
public class ParseTest implements TokenTypes {

  public static final int NODE_TYPES = 1;
  public static final int NODE_VALUES = 2;

  private static Hashtable tokenTypes = new Hashtable();
  
  static {
    tokenTypes.put(new Integer(END_OF_QUERY), "END_OF_QUERY");
    tokenTypes.put(new Integer(KEYWORD_SELECT), "KEYWORD_SELECT");
    tokenTypes.put(new Integer(IDENTIFIER), "IDENTIFIER");
    tokenTypes.put(new Integer(KEYWORD_AS), "KEYWORD_AS");
    tokenTypes.put(new Integer(COLON), "COLON");
    tokenTypes.put(new Integer(KEYWORD_FROM), "KEYWORD_FROM");
    tokenTypes.put(new Integer(KEYWORD_IN), "KEYWORD_IN");
    tokenTypes.put(new Integer(KEYWORD_WHERE), "KEYWORD_WHERE");
    tokenTypes.put(new Integer(KEYWORD_OR), "KEYWORD_OR");
    tokenTypes.put(new Integer(KEYWORD_AND), "KEYWORD_AND");
    tokenTypes.put(new Integer(EQUAL), "EQUAL");
    tokenTypes.put(new Integer(NOT_EQUAL), "NOT_EQUAL");
    tokenTypes.put(new Integer(KEYWORD_LIKE), "KEYWORD_LIKE");
    tokenTypes.put(new Integer(LT), "LT");
    tokenTypes.put(new Integer(LTE), "LTE");
    tokenTypes.put(new Integer(GT), "GT");
    tokenTypes.put(new Integer(GTE), "GTE");
    tokenTypes.put(new Integer(PLUS), "PLUS");
    tokenTypes.put(new Integer(MINUS), "MINUS");
    tokenTypes.put(new Integer(CONCAT), "CONCAT");
    tokenTypes.put(new Integer(TIMES), "TIMES");
    tokenTypes.put(new Integer(DIVIDE), "DIVIDE");
    tokenTypes.put(new Integer(KEYWORD_MOD), "KEYWORD_MOD");
    tokenTypes.put(new Integer(KEYWORD_ABS), "KEYWORD_ABS");
    tokenTypes.put(new Integer(KEYWORD_NOT), "KEYWORD_NOT");
    tokenTypes.put(new Integer(LPAREN), "LPAREN");
    tokenTypes.put(new Integer(RPAREN), "RPAREN");
    tokenTypes.put(new Integer(DOLLAR), "DOLLAR");
    tokenTypes.put(new Integer(KEYWORD_NIL), "KEYWORD_NIL");
    tokenTypes.put(new Integer(KEYWORD_UNDEFINED), "KEYWORD_UNDEFINED");
    tokenTypes.put(new Integer(BOOLEAN_LITERAL), "BOOLEAN_LITERAL");
    tokenTypes.put(new Integer(LONG_LITERAL), "LONG_LITERAL");
    tokenTypes.put(new Integer(DOUBLE_LITERAL), "DOUBLE_LITERAL");
    tokenTypes.put(new Integer(CHAR_LITERAL), "CHAR_LITERAL");
    tokenTypes.put(new Integer(STRING_LITERAL), "STRING_LITERAL");
    tokenTypes.put(new Integer(DATE_LITERAL), "DATE_LITERAL");
    tokenTypes.put(new Integer(TIME_LITERAL), "TIME_LITERAL");
    tokenTypes.put(new Integer(TIMESTAMP_LITERAL), "TIMESTAMP_LITERAL");
    tokenTypes.put(new Integer(KEYWORD_BETWEEN), "KEYWORD_BETWEEN");
  }

  /**
   * Main function.  Takes OQL query string as command line parameter
   * and prints Parse Tree version of that query to stdout.
   *
   * @param args Pass an OQL query string on the command line.
   */
  public static void main (String args[]) {
    
		try {
      Lexer lexer = new Lexer(args[0]);
      Parser parser = new Parser(lexer);
      ParseTreeNode theTree = parser.getParseTree();
      System.out.println(treeToString(theTree, NODE_TYPES));
      System.out.println(treeToString(theTree, NODE_VALUES));
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
		}
     
  }

  /**
   * Returns a string representation of the tree using lisp tree notation.  
   * (A, B, C, D) means a root a with 
   * children B, C, and D.  (A, (B, C, D), E) means A with a child B who has 
   * children C and D, and another child E (of A).
   *
   * @param theTree the Tree to convert to a string
   * @param printWhat should be one of the static members NODE_TYPES or 
   *    NODE_VALUES to tell the method what to write in the string.
   * @return a string as described above.
   */
  public static String treeToString(ParseTreeNode theTree, int printWhat) {
    String retVal = "";
    
    Token curToken = theTree.getToken();
    
    if ( printWhat == NODE_TYPES )
      retVal = (String)tokenTypes.get(new Integer(curToken.getTokenType()));
    else
      retVal = curToken.getTokenValue();
      
    if ( ! theTree.isLeaf() ) {
      retVal = "( " + retVal;
      for ( Enumeration e = theTree.children(); e.hasMoreElements(); ) {
        retVal = retVal + " , " 
                  + treeToString((ParseTreeNode)e.nextElement(), printWhat);
      }
      retVal = retVal + " )";
    }

    return retVal;
  }

}
