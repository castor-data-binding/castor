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
 * Copyright 1999-2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.simpletypes;

/**
 * Represents "real numbers" (float, double and user types derived from them)
 * Those numbers are of the form m x 2^e
 * The min and max values for
 *     m are accessed with get/setMin/MaxMantissa
 *     e are accessed with get/setMin/MaxExponent
 *
 * m and e are not facets as specified by the xmlschema,
 * they are just a way to regroup real and float under the same class.
 *
 * @author <a href="mailto:berry@intalio.com">Arnaud Berry</a>
 * @version $Revision:
**/
public class RealType extends AtomicType {
    /** SerialVersionUID */
    private static final long serialVersionUID = 3968578511223258942L;
    
    private long _minMantissa;
    private long _maxMantissa;
    private long _minExponent;
    private long _maxExponent;

    public void setMinMantissa(long minMantissa)    { _minMantissa= minMantissa; }
    public void setMaxMantissa(long maxMantissa)    { _maxMantissa= maxMantissa; }
    public void setMinExponent(long minExponent)    { _minExponent= minExponent; }
    public void setMaxExponent(long maxExponent)    { _maxExponent= maxExponent; }

    public long getMinMantissa()                    { return _minMantissa; }
    public long getMaxMantissa()                    { return _maxMantissa; }
    public long getMinExponent()                    { return _minExponent; }
    public long getMaxExponent()                    { return _maxExponent; }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.SimpleType#isNumericType()
     */
    public boolean isNumericType() {
        return true;
    }
    
}




