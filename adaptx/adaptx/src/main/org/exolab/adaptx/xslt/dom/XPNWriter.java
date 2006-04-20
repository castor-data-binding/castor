/*
 * (C) Copyright Keith Visco 2003  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */


package org.exolab.adaptx.xslt.dom;

import org.exolab.adaptx.xpath.XPathNode;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.Writer;


/**
 * A basic utility class to write an XPathNode, using the
 * org.exolab.adaptx.xslt.dom package as the implementation of 
 * XPathNode. Actually it should work for any XPathNode 
 * implementation, but it's only been tested with the above 
 * listed package.
 *
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class XPNWriter {
    
    private static final String ENTITY_AMP   = "&amp;";
    private static final String ENTITY_GT    = "&gt;";
    private static final String ENTITY_LT    = "&lt;";
    private static final String ENTITY_QUOT  = "&quot;";
    private static final String ENTITY_APOS  = "&apos;";
    
    
    private static final String XML_DECL_BEGIN = "<?xml ";
    private static final String XML_DECL_END   = "?>\n";
    private static final String COMMENT_BEGIN = "<-- ";
    private static final String COMMENT_END   = " -->";
    private static final String PI_BEGIN = "<?";
    private static final String PI_END   = "?>";
    
    
    private static final String VERSION_ATTR = "version";
    
   
    private static final String REUSE_ERR
        = "This XPNWriter was previously used. Please create "+
          "a new one.";
          
    //private URILocation _location = null;
    
    /**
     * A handle to the OutputStream 
     */
    private OutputStream _os = null;
    private Writer _writer = null;
    
    /**
     * A flag indicating to save location information in
     * the XPathNode tree.
    **/
    private boolean _saveLocation = false;
    
    private boolean _usable = true;
    
    /**
     *
     */
     private String _encoding = "UTF-8";
     
    /**
     * A flag which indicates whether or not to indent the text
     */
    private boolean _indent = false;
    
    
    private String _indentStr = "  ";

    /**
     * Creates a new XPNReader for the given URILocation.
     *
     * @param location the URILocation to create this reader for.
     */
    public XPNWriter(OutputStream os) 
        throws java.io.IOException
    {
        super();
        if (os == null) {
            String err = "The argument 'os' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _os = os;
    } //-- XPNWriter
    
    /**
     * Sets the character encoding to use
     *
     * @param encoding the character encoding to use
     */
    public void setEncoding(String encoding) {
        if (encoding == null)
            _encoding = "UTF-8";
        else
            _encoding = encoding;
    } //-- setEncoding
    
    /**
     * Sets a flag which controls writer specific indentation
     *
     * @param indent a flag that when true indicates that the writer
     * should "indent" where possible the start and end tags to
     * make the XML easier to read for human consumption.
     */
    public void setIndentation(boolean indent) {
        _indent = indent;
    } //-- setIndentation

	/**
	 * Writes an XML document representation from the given XPathNode 
	 *
	 * @param node the XPathNode to write
	 */
	public void write(XPathNode node) 
	    throws java.io.IOException
	{
	    if (_writer == null)
	        _writer = new OutputStreamWriter(_os, _encoding);
	        
	    write(node, "");
	} //-- write
    
	/**
	 * Writes an XML document representation from the given XPathNode 
	 *
	 * @param node the XPathNode to write
	 */
	private void write(XPathNode node, String indentStr) 
	    throws java.io.IOException
	{
	    if (node == null) return;
	    
	    switch (node.getNodeType()) {
	        case XPathNode.ROOT: 
	        {
	            _writer.write(XML_DECL_BEGIN);
	            _writer.write(" version=\"1.0\"");
	            _writer.write(XML_DECL_END);
	            XPathNode child = node.getFirstChild();
	            while (child != null) {
	                write(child, "");
	                child = child.getNext();
	            }
	            break;
	        }
	        case XPathNode.ATTRIBUTE:
	        {
	            //-- prefix name if necessary
	            String namespace = node.getNamespaceURI();
	            String prefix = null;
	            if ((namespace != null) && (namespace.length() > 0)) {
	                prefix = node.getNamespacePrefix(namespace);
	            }
	            String qName = node.getLocalName();
	            if ((prefix != null) && (prefix.length() > 0)) {
	                qName = prefix + ':' + qName;
	            }
	            _writer.write(' ');
	            _writer.write(qName);
	            _writer.write("=\"");
	            write(node.getStringValue(), true);
	            _writer.write('\"');
	            break;
	        }
	        case XPathNode.NAMESPACE:
	        {
	            _writer.write(' ');
	            _writer.write("xmlns");
	            String prefix = node.getLocalName();
	            if ((prefix != null) && (prefix.length() > 0)) {
	                _writer.write(':');
	                _writer.write(prefix);
	            }
	            _writer.write("=\"");
	            _writer.write(node.getStringValue());
	            _writer.write('\"');
	            break;
	        }
	        case XPathNode.ELEMENT:
	        {
	            
	            //-- prefix name if necessary
	            String namespace = node.getNamespaceURI();
	            String prefix = null;
	            if ((namespace != null) && (namespace.length() > 0)) {
	                prefix = node.getNamespacePrefix(namespace);
	            }
	            String qName = node.getLocalName();
	            if ((prefix != null) && (prefix.length() > 0)) {
	                qName = prefix + ':' + qName;
	            }
	            //-- handle indentation + start tag 
	            if (_indent)
	                _writer.write(indentStr);
	                
	            _writer.write('<');
	            _writer.write(qName);
	            
	            //-- process namespace nodes
	            XPathNode nsNode = node.getFirstNamespace();
	            while (nsNode != null) {
	                write(nsNode, "");
	                nsNode = nsNode.getNext();
	            }
	            
	            //-- process attributes
	            XPathNode attr = node.getFirstAttribute();
	            while (attr != null) {
	                write(attr, "");
	                attr = attr.getNext();
	            }
	            
	            //-- process children
	            XPathNode child = node.getFirstChild();
	            if (child != null) {
	                _writer.write('>');
	                if (_indent) {
	                    _writer.write('\n');
	                }
	            }
	            else {
	                //-- use empty element shorthand
	                _writer.write("/>");
	                if (_indent)
	                    _writer.write('\n');
	                break;
	            }
	            while (child != null) {
	                write(child, indentStr + _indentStr);
	                child = child.getNext();
	            }
	            
	            //-- write indentation + closing tag
	            if (_indent)
	                _writer.write(indentStr);
	                
	            _writer.write("</");
	            _writer.write(qName);
	            _writer.write('>');
	            
	            if (_indent)
	                _writer.write('\n');
	                
	            break;
	        }
	        case XPathNode.TEXT:
	        {
	            if (_indent)
	                _writer.write(indentStr);
	                
	            write(node.getStringValue(), false);
	            
	            if (_indent)
	                _writer.write('\n');
	                
	            break;
	        }
	        case XPathNode.COMMENT:
	            if (_indent)
	                _writer.write(indentStr);
	            
	            _writer.write(COMMENT_BEGIN);
	            //-- TODO: need to escape text to prevent --> 
	            //-- from inside a comment
	            _writer.write(node.getStringValue());
	            _writer.write(COMMENT_END);
	            if (_indent)
	                _writer.write('\n');
	             break;
	        case XPathNode.PI:
	            if (_indent)
	                _writer.write(indentStr);
	            _writer.write(PI_BEGIN);
	            _writer.write(node.getLocalName());
	            _writer.write(' ');
	            _writer.write(node.getStringValue());
	            _writer.write(PI_END);
	            if (_indent)
	                _writer.write('\n');
	            break;
	        default:
	            break;
	    }
	    _writer.flush();
	    
	} //-- write
	
	/**  
	 * A method that writes the given string to the
	 * underlying write..this method will handle special
	 * XML characters, such as '&'
	 *
	 * @param str the String to write
	 */
	private void write(String str, boolean attribute) 
	    throws IOException
	{
	    write(str.toCharArray(), attribute);
	}
	
	/**  
	 * A method that writes the given string to the
	 * underlying write..this method will handle special
	 * XML characters, such as '&'
	 *
	 * @param chars the char[] to write
	 */
	private void write(char[] chars, boolean attribute) 
	    throws IOException
	{
	    int start = 0;
	    int len   = 0;
	    
	    for (int i = 0; i < chars.length; i++) {
	        
	        switch(chars[i]) {
	            case '&':
	            case '>':
	            case '<':
	            case '"':
	            case '\'':
	                char ch = chars[i];
	                if (len > 0) {
	                    _writer.write(chars, start, len);
	                    start = start+len;
	                    len = 0;
	                }
	                ++start;
	                if (ch == '&')
	                    _writer.write(ENTITY_AMP);
	                else if (ch == '>')
	                    _writer.write(ENTITY_GT);
	                else if (ch == '<') 
	                    _writer.write(ENTITY_LT);
	                else if (ch == '\'') {
	                    if (attribute)
	                        _writer.write(ENTITY_APOS);
	                    else
	                        _writer.write(ch);
	                }
	                else if (ch == '"') {
	                    if (attribute)
	                        _writer.write(ENTITY_QUOT);
	                    else
	                        _writer.write(ch);
	                }
	                break;
	            default:
	                ++len;
	                break;
	        }
	    }
	    if (len > 0) {
	        _writer.write(chars, start, len);
	    }
	} //-- write
	
	
	/**
	 * Test method
	public static void main(String[] args) {
	    
	    Root root = new Root();
	    root.addChild(new ProcessingInstruction("target", "date"));
	    root.addChild(new Comment("Hello, I am a comment"));
	    Element foo = new Element("http://www.test.org/foo","foo");
	    foo.addNamespace(new Namespace("fz", "http://www.test.org/foo"));
	    root.addChild(foo);
	    Element bar = new Element(null, "bar");
	    foo.addChild(bar);
	    foo.addAttribute(new Attribute("name", "foo1"));
	    bar.addChild(new Text("Sample text & <foo>"));
	    bar.addChild(new Comment("Hello, I am a comment also"));
	    bar.addAttribute(new Attribute("http://www.test.org/foo", "name", "bar1"));
	    try {
    	    XPNWriter writer = new XPNWriter(System.out);
    	    writer.setIndentation(true);
	        writer.write(root);
	    }
	    catch(java.io.IOException iox) {
	        iox.printStackTrace();
	    }
	    
	} //-- main
	/* */
	
	
} //-- XPNWriter
