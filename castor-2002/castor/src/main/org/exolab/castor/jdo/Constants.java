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


package org.exolab.castor.jdo;


/**
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class Constants
{


    public static class SQL
    {

	public static final String And   = " AND ";
	public static final String Where = " WHERE ";
	public static final String From  = " FROM ";
	public static final String Order  = " ORDER BY ";
	public static final String Equal = "=";

	/**
	 * Separates between words in an SQL name. An underscore is used
	 * as the most common separator for simple name conversion.
	 */
	public static final char WordSeparator = '_';

	/**
	 * Separates between table name and column name.
	 */
	public static final char TableSeparator = '.';
    }


    public static class DTD
    {

	/**
	 * The public identifier of the DTD.
	 * <pre>
	 * -//EXOLAB/Castor Java/SQL Mapping DTD Version 1.0//EN
	 * </pre>
	 */
	public static final String PublicId = 
	    "-//EXOLAB/Castor Java/SQL Mapping DTD Version 1.0//EN";

	/**
	 * The system identifier of the DTD.
	 * <pre>
	 * http://castor.exolab.org/jdo-mapping.dtd
	 * </pre>
	 */
	public static final String SystemId =
	    "http://castor.exolab.org/jdo-mapping.dtd";

	/**
	 * The resource named of the DTD:
	 * <tt>/org/exolab/castor/jdo/jdo-mapping.dtd</tt>.
	 */
	public static final String Resource =
	    "/org/exolab/castor/jdo/schema/jdo-mapping.dtd";

    }


    public static class Schema
    {

	/**
	 * The public identifier of the XML schema.
	 * <pre>
	 * -//EXOLAB/Castor Java/SQL Mapping Schema Version 1.0//EN
	 * </pre>
	 */
	public static final String PublicId =
	    "-//EXOLAB/Castor Java/SQL Mapping Schema Version 1.0//EN";

	/**
	 * The system identifier of the XML schema.
	 * <pre>
	 * http://castor.exolab.org/jdo-mapping.xsd
	 * </pre>
	 */
	public static final String SystemId =
	    "http://castor.exolab.org/jdo-mapping.xsd";
	
	/**
	 * The namespace prefix: <tt>castor</tt>
	 */
	public static final String prefix =
	    "jdo";

	/**
	 * The namespace URI:
	 * <pre>
	 * http://castor.exolab.org/castor/jdo
	 * </pre>
	 */
	public static final String URI =
	    "http://castor.exolab.org/jdo";
	
	/**
	 * The resource named of the XML schema:
	 * <tt>/org/exolab/castor/jdo/mapping.xsd</tt>.
	 */
	public static final String Resource =
	    "/org/exolab/castor/jdo/schema/jdo-mapping.xsd";
	
    }


}
