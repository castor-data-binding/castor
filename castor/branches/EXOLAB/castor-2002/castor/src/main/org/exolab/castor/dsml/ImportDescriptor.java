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


package org.exolab.castor.dsml;


import java.io.Serializable;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;
import org.xml.sax.DocumentHandler;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;
import org.xml.sax.HandlerBase;
import org.xml.sax.helpers.AttributeListImpl;
import org.exolab.castor.util.Messages;


/**
 *
 *
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class ImportDescriptor
    extends HandlerBase
    implements Serializable
{


    public static class Policy
    {
	public static final int DeleteEmpty   = 0x01;
	public static final int ReplaceAttr   = 0x02;
	public static final int RefreshOnly   = 0x04;
	public static final int DefaultPolicy = 0x00;
    }


    static class Names
    {
	static class Element
	{
	    public static final String Policies   = "import-policies";
	    public static final String Policy     = "import-policy";
	}
	static class Attribute
	{
	    public static final String DN            = "dn";
	    public static final String DeleteEmpty   = "delete-empty";
	    public static final String ReplaceAttr   = "replace-attr";
	    public static final String RefreshOnly   = "replace-only";
	}
    }

    
    private Hashtable            _policies = new Hashtable();


    private boolean           _insideRoot;


    public ImportDescriptor()
    {
    }


    public Enumeration listDNs()
    {
	return _policies.keys();
    }


    public void addPolicy( String name, int policy )
    {
	_policies.put( name, new Integer( policy ) );
    }


    public int getDirectPolicy( String name )
    {
	Integer  policy;

	policy = (Integer) _policies.get( name );
	if ( policy != null ) {
	    return policy.intValue();
	} else {
	    return Policy.DefaultPolicy;
	}
    }


    public int getPolicy( String name )
    {
	DN       dn;
	Integer  policy;
	int      i;
	
	policy = (Integer) _policies.get( name );
	if ( policy != null ) {
	    return policy.intValue();
	} else {
	    dn = new DN( name );
	    for ( i = 1 ; i < dn.size() ; ++i ) {
		name = dn.suffix( i );
		policy = (Integer) _policies.get( name );
		if ( policy != null )
		    return policy.intValue();
	    }
	}
	return Policy.DefaultPolicy;
    }


    public void produce( DocumentHandler docHandler )
	throws SAXException
    {
	AttributeListImpl attrList;
	int               policy;
	Enumeration       enum;
	String            name;

	attrList = new AttributeListImpl();
	docHandler.startElement( XML.Namespace.Root, attrList );
	attrList = new AttributeListImpl();
	docHandler.startElement( Names.Element.Policies, attrList );

	enum = listDNs();
	while ( enum.hasMoreElements() ) {
	    name = (String) enum.nextElement();
	    policy = getDirectPolicy( name );
	    attrList = new AttributeListImpl();
	    attrList.addAttribute( Names.Attribute.DN, "ID", name );
	    if ( ( policy & Policy.DeleteEmpty ) != 0 )
		attrList.addAttribute( Names.Attribute.DeleteEmpty, null, "true" );
	    if ( ( policy & Policy.ReplaceAttr ) != 0 )
		attrList.addAttribute( Names.Attribute.ReplaceAttr, null, "true" );
	    if ( ( policy & Policy.RefreshOnly ) != 0 )
		attrList.addAttribute( Names.Attribute.RefreshOnly, null, "true" );
	    docHandler.startElement( Names.Element.Policy, attrList );
	    docHandler.endElement( Names.Element.Policy );
	}

	docHandler.endElement( Names.Element.Policies );
	docHandler.endElement( XML.Namespace.Root );
    }


    public void startElement( String tagName, AttributeList attr )
	throws SAXException
    {
	String  dn;
	int     policy;

	if ( tagName.equals( XML.Namespace.Root ) ) {
	    // Flag when entering (and leaving) the root element.
	    if ( _insideRoot )
		throw new SAXException( Messages.format( "castor.dsml.elementNested",
							 XML.Namespace.Root ) );
	    _insideRoot = true;
	} else {
	    if ( ! _insideRoot )
		throw new SAXException( Messages.format( "castor.dsml.expectingOpeningTag",
							 XML.Namespace.Root, tagName ) );

	    if ( tagName.equals( Names.Element.Policies ) ) {
		// Nothing to do at level of top element
	    } else if ( tagName.equals( Names.Element.Policy ) ) {
		dn = attr.getValue( Names.Attribute.DN );
		if ( dn == null )
		    throw new SAXException( Messages.format( "castor.dsml.missingAttribute",
							     Names.Element.Policy, Names.Attribute.DN ) );
		policy = 0;
		if ( "true".equals( attr.getValue( Names.Attribute.DeleteEmpty ) ) )
		    policy = policy | Policy.DeleteEmpty;
		if ( "true".equals( attr.getValue( Names.Attribute.RefreshOnly ) ) )
		    policy = policy | Policy.RefreshOnly;
		if ( "true".equals( attr.getValue( Names.Attribute.ReplaceAttr ) ) )
		    policy = policy | Policy.ReplaceAttr;
		addPolicy( dn, policy );
	    } else {
		throw new SAXException( Messages.format( "castor.dsml.expectingOpeningTag",
							 Names.Element.Policies, tagName ) );
	    }
	}
    }
	
	
    public void endElement( String tagName )
	throws SAXException
    {
	if ( tagName.equals( XML.Namespace.Root ) ) {
	    if ( _insideRoot )
		_insideRoot = false;
	    else
		throw new SAXException( Messages.format( "castor.dsml.closingOutsideRoot",
							 tagName ) );
	} else {
	    if ( ! _insideRoot )
		throw new SAXException( Messages.format( "castor.dsml.closingOutsideRoot",
							 tagName ) );
	    if ( tagName.equals( Names.Element.Policies ) ) {
		// Nothing to do here
	    } else if (tagName.equals( Names.Element.Policy ) ) {
		// Nothing to do here
	    } else {
		throw new SAXException( Messages.format( "castor.dsml.expectingClosingTag",
							 Names.Element.Policies, tagName ) );
	    }
	}
    }


    static class DN
    {


	private String[] _names;


	DN( String name )
	{
	    StringTokenizer token;
	    int             i;

	    token = new StringTokenizer( name, ", " );
	    _names = new String[ token.countTokens() ];
	    for ( i = 0 ; token.hasMoreTokens() ; ++i ) {
		_names[ i ] = token.nextToken();
	    }
	}


	int size()
	{
	    return _names.length;
	}


	String suffix( int index )
	{
	    StringBuffer name;

	    name = new StringBuffer( _names[ index ] );
	    for ( ++index ; index < _names.length ; ++index ) {
		name.append( ',' ).append( _names[ index ] );
	    }
	    return name.toString();
	}


    }


}

