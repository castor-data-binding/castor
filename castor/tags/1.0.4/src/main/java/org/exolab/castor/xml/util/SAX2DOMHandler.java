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
 * Copyright 1999, 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.util;


import java.util.Stack;

import org.xml.sax.*;
import org.w3c.dom.*;

/**
 * A class for converting a SAX events to DOM nodes
 *
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a>
 */
public class SAX2DOMHandler extends HandlerBase {
	private Node _document;
	private Stack _parents = new Stack();
										
	public SAX2DOMHandler(Node node)
	{
		_document = node;
	}
			
	public void startElement(String name, AttributeList attributes)
	{
		Node parent = _parents.size()>0 ? (Node) _parents.peek() : _document;			
		Document document;
		if (parent instanceof Document)
			document = (Document) parent;
		else
			document = parent.getOwnerDocument();
		Element element = document.createElement(name);
		int length = attributes.getLength();
		for(int i=0;i<length;i++)				
			element.setAttribute(attributes.getName(i), attributes.getValue(i));
		parent.appendChild(element);
		_parents.push(element);
	}
			
	public void characters(char[] chars, int offset, int length)
	{
	    String data  = new String(chars, offset, length);
		Node parent = _parents.size()>0 ? (Node) _parents.peek() : _document;
		Node last = parent.getLastChild();
		if ((last != null) && (last.getNodeType() == Node.TEXT_NODE)) {
		    ((Text)last).appendData(data);
		}
		else {
		    Text text = parent.getOwnerDocument().createTextNode(data);
		    parent.appendChild(text);
		}
	    
	}
			
	public void endElement(String name)
	{
		_parents.pop();
	}
}	
