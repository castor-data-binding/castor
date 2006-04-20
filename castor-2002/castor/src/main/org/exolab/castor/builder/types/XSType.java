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

package org.exolab.castor.builder.types;

import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.javasource.*;

import java.util.Hashtable;
import java.util.Enumeration;


/**
 * The base XML Schema Type class
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public abstract class XSType {


    /**
     * The name of the binary type
    **/
    public static final String BINARY_NAME = "binary";

    /**
     * The name of the boolean type
    **/
    public static final String BOOLEAN_NAME = "boolean";

    /**
     * The name of the CDATA type
     */
     public static final String CDATA_NAME = "CDATA";
    /**
     * The name of the the century type
     */
     public static final String CENTURY_NAME = "century";

    /**
     * The name of the decimal type
    **/
    public static final String DECIMAL_NAME = "decimal";

    /**
     * The name of the date type
     */
    public static final String DATE_NAME = "date";

    /**
     * The name of the double type
    **/
    public static final String DOUBLE_NAME = "double";

    /**
     * The name of the float type
     */
     public static final String FLOAT_NAME  = "float";

    /**
     * The name of the ID type
    **/
    public static final String ID_NAME = "ID";

    /**
     * The name of the IDREF type
    **/
    public static final String IDREF_NAME = "IDREF";

    /**
     * The Name of the IDREFS type
     */
     public static final String IDREFS_NAME = "IDREFS";

    /**
     * The name of the integer type
    **/
    public static final String INTEGER_NAME = "integer";


    /**
     * The name of the long type
    **/
    public static final String LONG_NAME = "long";

    /**
     * The name for the month type
     */
     public static final String MONTH_NAME = "month";

    /**
     * The name of the NCName type
    **/
    public static final String NCNAME_NAME = "NCName";

    /**
     * The name of the non-positive-integer type
     */
     public static final String NON_NEGATIVE_INTEGER_NAME = "nonNegativeInteger";

    /**
     * The name of the non-positive-integer type
     */
     public static final String NON_POSITIVE_INTEGER_NAME = "nonPositiveInteger";

    /**
     * The name of the negative-integer type
    **/
    public static final String NEGATIVE_INTEGER_NAME  = "negativeInteger";


    /**
     * The name of the NMTOKEN type
    **/
    public static final String NMTOKEN_NAME        = "NMTOKEN";

   /**
    * The of the NMTOKENS type
    */
    public static String NMTOKENS_NAME  = "NMTOKENS";

    /**
     * The name of the positive-integer type
    **/
    public static final String POSITIVE_INTEGER_NAME  = "positiveInteger";

    /**
     * the name of the QName type
     */
     public static final String QNAME_NAME = "QName";

    /**
     * the name of the recurring duration type
     */
     public static final String RECURRING_DURATION_NAME ="recurringDuration";

    /**
     * The name of the string type
    **/
    public static final String STRING_NAME  = "string";

    /**
     * The name of the timeInstant type
    **/
    public static final String TIME_INSTANT_NAME = "timeInstant";

    /**
     * the name of the timeDuration type
     */
    public static final String TIME_DURATION_NAME = "timeDuration";

    /**
     * the name of the time type
     */
     public static final String TIME_NAME = "time";
     /**
      * the name of the time period type
      */
     public static final String TIME_PERIOD_NAME    = "timePeriod";
	/**
     * The name of the URIReference type
     */
     public static final String URIRREFERENCE_NAME = "uriReference";

    /**
     * the name of the year type
     */
     public static final String YEAR_NAME = "year";
    /**
	 * The name of the short type
	 */
	public static final String SHORT_NAME = "short";

	/**
	 * The name of the int type
	 */
	public static final String INT_NAME = "int";


    public static final short NULL               = -1;

    //-- this type should change to user-defined or
    //-- something like that
    public static final short CLASS              =  0;

    //--
    public static final short BINARY             =  1;
    public static final short BOOLEAN            =  2;
    public static final short CENTURY            =  3;
    public static final short CDATA              = 30;
    public static final short COLLECTION         =  4;
    public static final short DATE               =  5;
    public static final short DECIMAL            =  6;
    public static final short DOUBLE             =  7;
    public static final short FLOAT              =  8;
    public static final short ID                 =  9;
    public static final short IDREF              = 10;
    public static final short IDREFS             = 11;
    public static final short INT				 = 12;
    public static final short INTEGER            = 13;
    public static final short LONG               = 14;
    public static final short MONTH              = 15;
    public static final short NCNAME             = 16;
    public static final short NEGATIVE_INTEGER   = 17;
    public static final short NMTOKEN            = 18;
    public static final short NMTOKENS           = 33;
    public static final short NON_NEGATIVE_INTEGER = 31;
    public static final short NON_POSITIVE_INTEGER = 32;
    public static final short POSITIVE_INTEGER   = 19;
    public static final short QNAME              = 20;
    public static final short RECURRING_DURATION = 21;
    public static final short SHORT				 = 22;
    public static final short STRING             = 23;
    public static final short TIME               = 24;
    public static final short TIME_DURATION      = 25;
    public static final short TIME_INSTANT       = 26;
    public static final short TIME_PERIOD        = 27;
    public static final short URIREFERENCE       = 28;
	public static final short YEAR               = 29;
    private short   type       = NULL;

    /**
     * Flag signaling an enumerated type
    **/
    private boolean enumerated = false;

    /**
     * Creates a new XSType of the given type
    **/
    protected XSType(short type) {
        this.type = type;
    } //-- XSType

    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public abstract JType getJType();

    /**
     * Reads and sets the facets for XSType
     * @param simpleType the SimpleType containing the facets
     */
     public abstract void setFacets(SimpleType simpleType);

    /**
     * Returns the type of this XSType
     * @return the type of this XSType
    **/
    public short getType() {
        return this.type;
    } //-- getType


	/**
	 * Returns a list of Facets from the simpleType
	 *	(duplicate facets due to extension are filtered out)
     * @param simpletype the Simpletype we want the facets for
     * @return Unique list of facets from the simple type
	 */
	protected static Enumeration getFacets(SimpleType simpleType)
	{
		Hashtable hashTable = new Hashtable();
        Enumeration enum = simpleType.getFacets();
		while (enum.hasMoreElements()) {

            Facet facet = (Facet)enum.nextElement();
            String name = facet.getName();
			hashTable.put(name, facet);
		}
		return hashTable.elements();
	}

    /**
     * Returns the String necessary to convert an instance of this XSType
     * to an Object. This method is really only useful for primitive types
     * @param variableName the name of the instance variable
     * @return the String necessary to convert an instance of this XSType
     * to an Object
    **/
    public String createToJavaObjectCode(String variableName) {
        return variableName;
    } //-- toJavaObject

    /**
     * Returns the Java code neccessary to create a new instance of the
     * JType associated with this XSType
     */
    public String newInstanceCode() {
        return "new "+getJType().getName()+"();";
    } //-- newInstanceCode

    /**
     * Returns the String necessary to convert an Object to
     * an instance of this XSType. This method is really only useful
     * for primitive types
     * @param variableName the name of the Object
     * @return the String necessary to convert an Object to an
     * instance of this XSType
    **/
    public String createFromJavaObjectCode(String variableName) {
        StringBuffer sb = new StringBuffer();

        JType jType = getJType();
        if (jType != null) {
            sb.append('(');
            sb.append(jType.toString());
            sb.append(") ");
        }
        sb.append(variableName);
        return sb.toString();
    } //-- fromJavaObject

    /**
     * Returns true if this XSType represents an enumerated type
     * @return true if this XSType represents an enumerated type
    **/
    public boolean isEnumerated() {
        return enumerated;
    } //-- isEnumerated

    public boolean isPrimitive() {
        switch (type) {
            case BOOLEAN:
            case DOUBLE:
            case FLOAT:
            case INTEGER:
            case LONG:
            case NON_NEGATIVE_INTEGER:
            case NON_POSITIVE_INTEGER:
            case NEGATIVE_INTEGER:
            case POSITIVE_INTEGER:
            case SHORT:
            case INT:
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns the name of this XSType
     * @return the name of this XSType
    **/
    public String getName() {
        switch (type) {
            case BINARY:
                return BINARY_NAME;
            case BOOLEAN:
                return BOOLEAN_NAME;
            case CDATA:
                return CDATA_NAME;
            case CENTURY:
                return CENTURY_NAME;
            case DATE:
                return DATE_NAME;
            case DECIMAL:
                return DECIMAL_NAME;
            case DOUBLE:
                return DOUBLE_NAME;
            case FLOAT:
                return FLOAT_NAME;
            case ID:
                return ID_NAME;
            case IDREF:
                return IDREF_NAME;
            case IDREFS:
                return IDREFS_NAME;
            case INTEGER:
                return INTEGER_NAME;
            case LONG:
                return LONG_NAME;
            case MONTH :
                return MONTH_NAME;
            case NCNAME:
                return NCNAME_NAME;
            case NEGATIVE_INTEGER:
                return NEGATIVE_INTEGER_NAME;
            case NMTOKEN:
                return NMTOKEN_NAME;
            case NMTOKENS:
                return NMTOKENS_NAME;
            case NON_NEGATIVE_INTEGER:
                return NON_NEGATIVE_INTEGER_NAME;
            case NON_POSITIVE_INTEGER:
                return NON_POSITIVE_INTEGER_NAME;
            case POSITIVE_INTEGER:
                return POSITIVE_INTEGER_NAME;
            case QNAME:
                return QNAME_NAME;
            case RECURRING_DURATION:
                return RECURRING_DURATION_NAME;
            case STRING:
                return STRING_NAME;
            case TIME:
                return TIME_NAME;
            case TIME_INSTANT:
                return TIME_INSTANT_NAME;
            case TIME_DURATION:
                return TIME_DURATION_NAME;
            case TIME_PERIOD:
                return TIME_PERIOD_NAME;
            case YEAR :
                return YEAR_NAME;
			case SHORT:
				return SHORT_NAME;
			case INT:
				return INT_NAME;
            default:
                return null;
        }
    } //-- getName

    /**
     * Sets the enumerated flag for this XSClass
     * @param enumerated a boolean indicating whether or not this XSClass
     * represents an enumerated type
    **/
    public void setAsEnumertated(boolean enumerated) {
        this.enumerated = enumerated;
    } //-- setAsEnumerated

} //-- XSType
