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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.*;

import org.exolab.javasource.*;

/**
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class IntegerCodeHelper implements CodeHelper {
    
    
    XSInteger xsInteger = null;
    
    /**
     * Creates a new IntegerCodeHelper using the given XSInteger
     * which defines the Integer constraints
    **/
    public IntegerCodeHelper(XSInteger xsInteger) {
        this.xsInteger = xsInteger;
    } //-- IntegerCodeHelper
    
    /**
     * Creates the necessary code to convert an object with the
     * given memberName into a String
     * @param memberName the name of the object to convert to a String
     * @param jsc the JSourceCode to add the source code to
    **/
    public void generateToStringCode(String memberName, JSourceCode jsc) {
        jsc.append("Integer.toString(");
        jsc.append(memberName);
        jsc.append(")");
    }
    
    /**
     * Creates the necessary code to validate an object
     * @param memberName the name of the member that needs validation
     * @param jsc the JSourceCode object to add the source code to
    **/
    public void generateValidationCode(String memberName, JSourceCode jsc) {
        if (xsInteger == null) return;
        
        jsc.add("//-- validation code for integer: ");
        jsc.append(memberName);
        
        if (xsInteger.hasMinimum()) {
            
            jsc.add("if (");
            jsc.append(memberName);
            
            Integer min = xsInteger.getMinExclusive();
            boolean isExclusive = true;
            
            if (min != null) jsc.append(" <= ");
            else {
                min = xsInteger.getMinInclusive();
                jsc.append(" < ");
                isExclusive = false;
            }
            jsc.append(min.toString());
            jsc.append(") {");
            jsc.indent();
            jsc.add("String err = \"");
            jsc.append(memberName.substring(1));
            jsc.append(" cannot be less than ");
            if (isExclusive) jsc.append("or equal to ");
            jsc.append(min.toString());
            jsc.append("\";");
            jsc.add("throw new ValidationException(err);");
            jsc.unindent();
            jsc.add("}");
        }
        
        if (xsInteger.hasMaximum()) {
            jsc.add("if (");
            jsc.append(memberName);
            
            Integer max = xsInteger.getMaxExclusive();
            boolean isExclusive = true;
            if (max != null) jsc.append(" >= ");
            else {
                max = xsInteger.getMaxInclusive();
                jsc.append(" > ");
                isExclusive = false;
            }
            jsc.append(max.toString());
            jsc.append(") {");
            jsc.indent();
            jsc.add("String err = \"");
            jsc.append(memberName);
            jsc.append(" cannot be more than ");
            if (isExclusive) jsc.append("or equal to ");
            jsc.append(max.toString());
            jsc.append("\";");
            jsc.add("throw new ValidationException(err);");
            jsc.unindent();
            jsc.add("}");
        }
        
    }
    
} //-- IntegerCodeHelper
