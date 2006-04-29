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


import java.io.Serializable;
import java.util.Vector;
import java.util.Enumeration;
import org.xml.sax.DocumentHandler;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;
import org.xml.sax.HandlerBase;
import org.xml.sax.helpers.AttributeListImpl;
import org.castor.util.Messages;


/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class SearchDescriptor extends HandlerBase implements Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = -6614367393322175115L;


    public static class Scope
    {

	public static final int OneLevel= 0;
	public static final int Base    = 1;
	public static final int SubTree = 3;

    }


    static class Names
    {
	static class Element
	{
	    public static final String Search      = "search";
	    public static final String ReturnAttr  = "return-attr";
	}

	static class Attribute
	{
	    public static final String AttrName      = "name";
	    public static final String BaseDN        = "base";
	    public static final String Scope         = "scope";
	    public static final String Filter        = "filter";
	    public static final String ScopeOneLevel = "onelevel";
	    public static final String ScopeBase     = "base";
	    public static final String ScopeSubTree  = "subtree";
	}
    }


    private int              _scope = Scope.Base;


    private String           _baseDN;


    private String           _filter;


    private Vector           _returnAttrs;


    private StringBuffer     _attrName;


    private boolean           _insideRoot;


    public SearchDescriptor()
    {
    }


    public int getScope()
    {
	return _scope;
    }


    public void setScope( int scope )
    {
	_scope = scope;
    }


    public String getBaseDN()
    {
	return _baseDN;
    }


    public void setBaseDN( String baseDN )
    {
	_baseDN = baseDN;
    }


    public String getFilter()
    {
	return _filter;
    }


    public void setFilter( String filter )
    {
	_filter = filter;
    }


    public String[] getReturnAttrs()
    {
	if ( _returnAttrs == null )
	    return null;
  String[] array;

  array = new String[ _returnAttrs.size() ];
  _returnAttrs.copyInto( array );
  return array;
    }


    public Enumeration listReturnAttrs()
    {
      if ( _returnAttrs == null ) return new Vector().elements();
      
      return _returnAttrs.elements();
    }
    

    public void addReturnAttr( String attrName )
    {
	if ( _returnAttrs == null )
	    _returnAttrs = new Vector();
	if ( ! _returnAttrs.contains( attrName ) )
	    _returnAttrs.addElement( attrName );
    }


    public void produce( DocumentHandler docHandler )
	throws SAXException
    {
	AttributeListImpl attrList;
	Enumeration       enumeration;

	attrList = new AttributeListImpl();
	docHandler.startElement( XML.Namespace.Root, attrList );

	attrList = new AttributeListImpl();
	if ( _baseDN != null )
	    attrList.addAttribute( Names.Attribute.BaseDN, "CDATA", _baseDN );
	if ( _filter != null )
	    attrList.addAttribute( Names.Attribute.Filter, "CDATA", _filter );
	switch ( _scope ) {
	case Scope.OneLevel:
	    attrList.addAttribute( Names.Attribute.Scope, null,
				   Names.Attribute.ScopeOneLevel );
	    break;
	case Scope.Base:
	    attrList.addAttribute( Names.Attribute.Scope, null,
				   Names.Attribute.ScopeBase );
	    break;
	case Scope.SubTree:
	    attrList.addAttribute( Names.Attribute.Scope, null,
				   Names.Attribute.ScopeSubTree );
	    break;
	}
	docHandler.startElement( Names.Element.Search, attrList );

	if ( _returnAttrs != null ) {
	    enumeration = _returnAttrs.elements();
	    while ( enumeration.hasMoreElements() ) {
		attrList = new AttributeListImpl();
		attrList.addAttribute( Names.Attribute.AttrName, "NMTOKEN",
				       (String) enumeration.nextElement() );
		docHandler.startElement( Names.Element.ReturnAttr, attrList );
		docHandler.endElement( Names.Element.ReturnAttr );
	    }
	}

	docHandler.endElement( Names.Element.Search );
	docHandler.endElement( XML.Namespace.Root );
    }


    public void startElement( String tagName, AttributeList attr )
	throws SAXException
    {
	String value;

	if ( tagName.equals( XML.Namespace.Root ) ) {
	    // Flag when entering (and leaving) the root element.
	    if ( _insideRoot )
		throw new SAXException( Messages.format( "dsml.elementNested",
							 XML.Namespace.Root ) );
	    _insideRoot = true;
	} else {
	    if ( ! _insideRoot )
		throw new SAXException( Messages.format( "dsml.expectingOpeningTag",
							 XML.Namespace.Root, tagName ) );

	    if ( tagName.equals( Names.Element.Search ) ) {
		_baseDN = attr.getValue( Names.Attribute.BaseDN );
		if ( _baseDN == null )
		    throw new SAXException( Messages.format( "dsml.missingAttribute",
							     Names.Element.Search, Names.Attribute.BaseDN ) );
		_filter = attr.getValue( Names.Attribute.Filter );
		value = attr.getValue( Names.Attribute.Scope );
		if ( value != null ) {
		    if ( value.equals( Names.Attribute.ScopeOneLevel ) )
			_scope = Scope.OneLevel;
		    else if ( value.equals( Names.Attribute.ScopeBase ) )
			_scope = Scope.Base;
		    else if ( value.equals( Names.Attribute.ScopeSubTree ) )
			_scope = Scope.SubTree;
		    else
			throw new SAXException( Messages.format( "dsml.invalidValue",
								 Names.Attribute.Scope, value ) );
		}
	    } else if ( tagName.equals( Names.Element.ReturnAttr ) ) {
		if ( _baseDN == null ) {
		    throw new SAXException( Messages.format( "dsml.expectingOpeningTag",
							     Names.Element.Search, tagName ) );
		}
		// Create a string buffer, characters() will fill it up,
		// endElement() will add it to the list.
		_attrName = new StringBuffer();
	    } else {
		throw new SAXException( Messages.format( "dsml.expectingOpeningTag",
							 Names.Element.Search, tagName ) );
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
		throw new SAXException( Messages.format( "dsml.closingOutsideRoot",
							 tagName ) );
	} else {
	    if ( ! _insideRoot )
		throw new SAXException( Messages.format( "dsml.closingOutsideRoot",
							 tagName ) );
	    if ( tagName.equals( Names.Element.Search ) ) {
		// Nothing to do hare
	    } else if (tagName.equals( Names.Element.ReturnAttr ) ) {
		if ( _attrName.length() > 0 ) {
		    addReturnAttr( _attrName.toString() );
		    _attrName = null;
		}
	    } else {
		throw new SAXException( Messages.format( "dsml.expectingClosingTag",
							 Names.Element.Search, tagName ) );
	    }
	}
    }


    public void characters( char[] ch, int offset, int length )
    {
	if ( _attrName != null ) {
	    _attrName.append( ch, offset, length );
	}
    }


}


