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


package org.exolab.castor.dsml;


/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public abstract class XML
{


    public static class Namespace
    {

	public static final String URI = "http://www.dsml.org/DSML";
	public static final String Prefix = "dsml";
	public static final String Root = "dsml";

    }


    public static class Schema
    {
	
	public static final String Element = "directory-schema";

	public static class Elements
	{
	    public static final String Name = "name";
	    public static final String Description = "description";
	    public static final String OID = "object-identifier";
	    public static final String Class = "class";
	    public static final String Attribute = "attribute";
	    public static final String AttributeType = "attribute-type";
	    public static final String Syntax = "syntax";
	    public static final String Equality = "equality";
	    public static final String Ordering = "ordering";
	    public static final String Substring = "substring";
	}

	public static class Attributes
	{
	    public static final String Id = "id";
	    public static final String Superior = "superior";
	    public static final String Obsolete = "obsolete";
	    public static final String Type = "type";
	    public static final String SingleValue = "single-value";
	    public static final String UserModification = "user-modification";
	    public static final String Ref = "ref";
	    public static final String Required = "required";

	    public static class Types
	    {
		public static final String Structural = "structural";
		public static final String Abstract = "abstract";
		public static final String Auxiliary = "auxiliary";
	    }
	}

    }


    public static class Entries
    {

	public static final String Element = "directory-entries";
	
	public static class Elements
	{
	    public static final String Entry = "entry";
	    public static final String ObjectClass = "objectclass";
	    public static final String OCValue = "oc-value";
	    public static final String Attribute = "attr";
	    public static final String Value = "value";
	}

	public static class Attributes
	{
	    public static final String DN = "dn";
	    public static final String Name = "name";
	    public static final String Ref = "ref";
	    public static final String Encoding = "encoding";

	    public static class Encodings
	    {
		public static final String Base64 = "base64";
	    }
	}

    }


}
