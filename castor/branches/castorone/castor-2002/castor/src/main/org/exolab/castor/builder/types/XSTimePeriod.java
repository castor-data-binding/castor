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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 * $Id$
 */
package org.exolab.castor.builder.types;

import org.exolab.castor.types.TimeDuration;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.javasource.JType;
import org.exolab.javasource.JClass;
import org.exolab.castor.types.OperationNotSupportedException;

import java.text.ParseException;
import java.util.Enumeration;

/**
 * The XML Schema TimePeriod type
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 * @see org.exolab.castor.types.TimePeriod
**/
public class XSTimePeriod extends XSRecurringDuration {

    /**
     * The JType represented by this XSType
     */
    private static final JType JTYPE
        = new JClass("org.exolab.castor.types.TimePeriod");

    public XSTimePeriod() {
       super(XSType.TIME_PERIOD,"","P0Y");
    }

    /**
     * returns a TimePeriod with the duration facet set up
     * @param duration the String value of the duration facet
     * @return a TimePeriod the duration facet set up
     */
    public XSTimePeriod(short type,String duration) {
        super(type,duration,"P0Y");
    }


    public void setPeriod (TimeDuration period) {
        throw new OperationNotSupportedException("in a time period type, the period facet must not be changed");
    }



    /**
     * Reads and sets the facets for this XSTimePeriod
     * @param simpleType the SimpleType containing the facets
     */
    public void setFacets(SimpleType simpleType) {

       //-- copy valid facets
        Enumeration enum = getFacets(simpleType);
        while (enum.hasMoreElements()) {

            Facet facet = (Facet)enum.nextElement();
            String name = facet.getName();

            try {
                //duration
                if (Facet.DURATION.equals(name))
                    setDuration(TimeDuration.parse(facet.getValue()));

            } catch(ParseException e) {
                System.out.println("Error in setting the facets of timePeriod");
                e.printStackTrace();
                return;
            }
        }
    }//setFacets


    public JType getJType() {
       return this.JTYPE;
    }

     /**
	 * Returns the Java code neccessary to create a new instance of the
	 * JType associated with this XSType
	 */
	public String newInstanceCode() {
        String result="new "+getJType().getName()+"(";
       //duration should never be null
        result = result +"\""+ getDuration().toString()+"\"";
        result = result +");";
        return result;
    }
}//XSTimePeriod