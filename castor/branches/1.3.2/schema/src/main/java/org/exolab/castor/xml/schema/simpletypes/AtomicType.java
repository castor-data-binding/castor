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

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;

/**
 * The base class for built-in atomic Schema types
 * @author <a href="mailto:berry@intalio.com">Arnaud Berry</a>
 * @version $Revision:
**/
public abstract class AtomicType extends SimpleType
{

    /**
     * Gets the pattern facet
     * returns null if there is none
    **/
    String getPattern()
    {
        Facet patternFacet= getFacet(Facet.PATTERN);
        if (patternFacet == null) return null;
        return patternFacet.getValue();
    }

    /**
     *  Tells if the minimum is inclusive or not
     *  Has no meaning if there is no minInclusive/Exclusive facet
    **/
    public boolean isMinInclusive()
    {
        return (getFacet(Facet.MIN_INCLUSIVE) != null);
    }

    /**
     *  Tells if the maximum is inclusive or not
     *  Has no meaning if there is no maxInclusive/Exclusive facet
    **/
    public boolean isMaxInclusive()
    {
        return (getFacet(Facet.MAX_INCLUSIVE) != null);
    }

    /**
     * Gets the MIN_INCLUSIVE facet or the MIN_EXCLUSIVE facet
     * (both are never defined at the same time)
     * returns null if there is no minimum
    **/
    Facet getMin()
    {
        Facet minInclusiveFacet= getFacet(Facet.MIN_INCLUSIVE);
        if (minInclusiveFacet != null) return minInclusiveFacet;

        Facet minExclusiveFacet= getFacet(Facet.MIN_EXCLUSIVE);
        if (minExclusiveFacet != null) return minExclusiveFacet;

        return null;
    }

    /**
     * Gets the MAX_INCLUSIVE facet or the MAX_EXCLUSIVE facet
     * (both are never defined at the same time)
     * returns null if there is no maximum
    **/
    Facet getMax()
    {
        Facet maxInclusiveFacet= getFacet(Facet.MAX_INCLUSIVE);
        if (maxInclusiveFacet != null) return maxInclusiveFacet;

        Facet maxExclusiveFacet= getFacet(Facet.MAX_EXCLUSIVE);
        if (maxExclusiveFacet != null) return maxExclusiveFacet;

        return null;
    }
}




