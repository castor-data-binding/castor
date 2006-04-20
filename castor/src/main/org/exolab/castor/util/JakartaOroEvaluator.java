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
 * Copyright 2000-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

 package org.exolab.castor.util;

 import org.apache.oro.text.regex.*;
 
 /**
  * An implementation of the RegExpEvaluator that uses the
  * Jakarta ORO Regular Expression library. For more information
  * about the Jakarta ORO library please visit:
  * <a href="http://jakarta.apache.org/oro/">
  * http://jakarta.apache.org/oro/</a>
  *
  * @author <a href="mailto:glenn@voyager.apg.more.net">Glenn Nielsen</a>
  * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
  * @version $Revision$ $Date$
 **/
 public class JakartaOroEvaluator 
    implements RegExpEvaluator
{
    
    private static final String BOL = "^";
    private static final String EOL = "$";
    
    /**
     * The Regular expression
    **/
    private Perl5Pattern _regexp = null;
    
    /**
     * Creates a new JakartaOroEvaluator
    **/
    public JakartaOroEvaluator() {
        super();
    } //-- JakartaOroEvaluator
    
    /**
     * Sets the regular expression to match against during
     * a call to #matches
     *
     * @param rexpr the regular expression
    **/
    public void setExpression(String rexpr) {
        
        if (rexpr != null) {
            try {
                //-- patch and compile expression
                Perl5Compiler compiler = new Perl5Compiler();
                _regexp = (Perl5Pattern) compiler.compile(BOL + rexpr + EOL,
                    Perl5Compiler.SINGLELINE_MASK);
            }
            catch(MalformedPatternException ex) {
                String err = "RegExp Syntax error: ";
                err += ex.getMessage();
                err += " ; error occured with the following "+
                    "regular expression: " + rexpr;
                
                throw new IllegalArgumentException(err);
            }
        }
        else
            _regexp = null;
    } //-- setExpression
    
    /**
     * Returns true if the given String is matched by the 
     * regular expression of this RegExpEvaluator
     *
     * @param value the String to check the production of
     * @return true if the given string matches the regular
     * expression of this RegExpEvaluator
     * @see #setExpression
    **/
    public boolean matches(String value)
    {
        // object to match compiled regular expressions
        Perl5Matcher matcher = new Perl5Matcher();
        // class for accessing results of pattern match
        MatchResult result;
        return matcher.contains(value,_regexp);
    } //-- matches
    
 } //-- JakartaOroEvaluator
 
 

