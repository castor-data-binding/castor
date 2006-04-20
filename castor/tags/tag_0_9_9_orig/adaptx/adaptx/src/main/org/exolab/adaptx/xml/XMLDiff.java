/*
 * (C) Copyright Keith Visco 2002  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.clc-marketing.com/xslp/license.txt
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

package org.exolab.adaptx.xml;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xslt.dom.XPNReader;
import org.exolab.adaptx.xslt.dom.Attribute;
import org.exolab.adaptx.xslt.dom.Element;
import org.exolab.adaptx.xslt.dom.Location;
import org.exolab.adaptx.xslt.dom.Root;
import org.exolab.adaptx.xslt.dom.Text;
import org.exolab.adaptx.util.List;

import java.util.StringTokenizer;
import java.io.PrintWriter;

/**
 * A utility class used to compare two XPathNodes, or XML input files 
 * and report the differences between them.
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class XMLDiff {
    
    /**
     * A boolean to indicate whether or not whitespace is
     * important
    **/
    private boolean _ignoreWhitespace = true;
    
    /**
     * A boolean to indicate whether or not the first
     * difference found should terminate the program.
    **/
    private boolean _returnAtFirstDiff = false;    
    
    /**
     * A boolean to indicate whether or not child order
     * should be strictly enforced. By default child order
     * is treated as "all", meaning that as long as a child exists,
     * it doesn't matter which order the children occur.
    **/
    private boolean _strictChildOrder = false;
    
    
    /**
     * Default Constructor
    **/
    public XMLDiff() {
        super();
    } //-- XMLDiff

    
    /**
     * Compares the two XML documents located at the given URL
     * locations. Returns 0, if no differences are found, otherwise 
     * returns a positive number indicating the number of differences.
     *
     * @param url1 the location of the XML document to compare. 
     * @param url2 the location of the XML document to compare against url1.
     * @return 0, if no differences are found, otherwise a positive number 
     * indicating the number of differences.
    **/
    public int compare(String url1, String url2) 
        throws java.io.IOException
    {
        if (url1 == null) {
            String err = "The argument 'url1' may not be null.";
            throw new IllegalArgumentException(err);
        }
        
        if (url2 == null) {
            String err = "The argument 'url2' may not be null.";
            throw new IllegalArgumentException(err);
        }
        
        XPNReader reader = new XPNReader(url1);
        reader.setSaveLocation(true);        
        XPathNode node1 = reader.read();
        
        reader = new XPNReader(url2);
        reader.setSaveLocation(true);
        XPathNode node2 = reader.read();
        
        StateInfo sInfo = new StateInfo();
        sInfo.url1   = url1;
        sInfo.url2   = url2;
        sInfo.header = true;
        sInfo.pw     = new PrintWriter(System.out, true);
        
        return compare(node1, node2, sInfo);
        
    } //-- compare
    
    /**
     * Compares the given XPathNodes. Returns 0, if no differences
     * are found, otherwise returns a positive number indicating the
     * number of differences.
     *
     *
     * @param node1 the XPathNode to compare with. 
     * @param node2 the XPathNode being compared against node1.
     * @return 0, if no differences are found, otherwise a positive number 
     * indicating the number of differences.
    **/
    public int compare(XPathNode node1, XPathNode node2) {
        StateInfo sInfo = new StateInfo();
        sInfo.url1 = "filename unavailable";
        sInfo.url2 = "filename unavailable";
        sInfo.pw = new PrintWriter(System.out, true);
        sInfo.header = true;
        return compare(node1, node2, sInfo);
    } //-- compare

    /**
     * Compares the given XPathNodes. Returns 0, if no differences
     * are found, otherwise returns a positive number indicating the
     * number of differences.
     *
     *
     * @param node1 the XPathNode to compare with. 
     * @param node2 the XPathNode being compared against node1.
     * @return 0, if no differences are found, otherwise a positive number 
     * indicating the number of differences.
    **/
    public int compare
        (XPathNode node1, String filename1, XPathNode node2, String filename2) 
    {
        StateInfo sInfo = new StateInfo();
        sInfo.url1 = filename1;
        sInfo.url2 = filename2;
        sInfo.pw = new PrintWriter(System.out, true);
        sInfo.header = true;
        return compare(node1, node2, sInfo);
    } //-- compare
    
    /**
     * Compares the given XPathNodes. Returns 0, if no differences
     * are found, otherwise returns a positive number indicating the
     * number of differences.
     *
     *
     * @param node1 the XPathNode to compare with. 
     * @param node2 the XPathNode being compared against node1.
     * @return 0, if no differences are found, otherwise a positive number 
     * indicating the number of differences.
    **/
    private int compare(XPathNode node1, XPathNode node2, StateInfo sInfo) {
        
        int diffCount = 0;
        
        boolean print = (sInfo.pw != null);
        
        //-- If Node types are different we simply cannot proceed
        if (node1.getNodeType() != node2.getNodeType()) {
            if (print) {
                sInfo.pw.println("type differ: <" + node1.getLocalName() + ">");
            }
            return 1;
        }
        
        //-- make sure each namespace is correct
        String ns1 = node1.getNamespaceURI();
        String ns2 = node2.getNamespaceURI();
        
        if (ns1 == null) ns1 = "";
        if (ns2 == null) ns2 = "";
        
        if (!ns1.equals(ns2)) {
            
            if (print) {
                String err = "namespaces differ: ('" + ns1 + "' != '" +
                    ns2 + "').";
                sInfo.pw.println(err);
            }
            
            if (_returnAtFirstDiff) 
                return 1;
            else 
                ++diffCount;
        }
        
        //-- compare local names
        String name1 = node1.getLocalName();
        String name2 = node2.getLocalName();
        
        //-- ROOT may or may not have null name
        if (name1 == null) {
            if (name2 != null) {
                if (print) {
                    sInfo.pw.println("names differ: null vs. '" + name2 + '\'');
                }
                ++diffCount;
                return diffCount;
            }
        }
        else if (!name1.equals(name2)) {
            if (print) {
                sInfo.pw.println("name differ: <" + name1 + "(1)> != <" + 
                    name2 + "(2)>");
            }
            ++diffCount;
            return diffCount;
        }
        
        switch (node1.getNodeType()) {
            
            //-- Handle Root node. Normally a Root node only has
            //-- children, but this is really implementation dependant. 
            //-- Sometimes an implementation may handle the document 
            //-- element as the root itself.
            case XPathNode.ROOT:
            {
                XPathNode child1 = node1.getFirstChild();
                XPathNode child2 = node2.getFirstChild();
                    
                while ((child1 != null) && (child2 != null)) {
                        
                    //-- ignore whitespace
                    if (_ignoreWhitespace) {
                        boolean skip = false;
                        if (child1.getNodeType() == XPathNode.TEXT) {
                            if (compareText(child1.getStringValue(), "")) {
                                child1 = child1.getNext();
                                skip = true;
                            }
                        }
                        if (child2.getNodeType() == XPathNode.TEXT) {
                            if (compareText(child2.getStringValue(), "")) {
                                child2 = child2.getNext();
                                skip = true;
                            }
                        }
                        if (skip) continue;
                    }
                        
                    int result = compare(child1, child2, sInfo);
                        
                    if (result != 0) {
                        if (_returnAtFirstDiff) return result;
                        else diffCount += result;
                    }
                    child1 = child1.getNext();
                    child2 = child2.getNext();
                }
                
                while (child1 != null) {
                    //-- ignore whitespace
                    if (_ignoreWhitespace) {
                        if (child1.getNodeType() == XPathNode.TEXT) {
                            if (compareText(child1.getStringValue(), "")) {
                                child1 = child1.getNext();
                                continue;
                            }
                        }
                    }
                    if (print) {
                        //-- print node
                        printLocationInfo(child1, null, sInfo);
                        sInfo.pw.println("- ");
                    }
                    if (_returnAtFirstDiff) return 1;
                    ++diffCount;
                    child1 = child1.getNext();
                }
                while (child2 != null) {
                    //-- ignore whitespace
                    if (_ignoreWhitespace) {
                        if (child2.getNodeType() == XPathNode.TEXT) {
                            if (compareText(child2.getStringValue(), "")) {
                                child2 = child2.getNext();
                                continue;
                            }
                        }
                    }
                    if (print) {
                        //-- print node
                        printLocationInfo(child2, null, sInfo);
                        sInfo.pw.println("+ ");
                    }
                    if (_returnAtFirstDiff) return 1;
                    ++diffCount;
                    child2 = child2.getNext();
                }
                break;
            }
            //-- Handle Element nodes
            case XPathNode.ELEMENT: {
                diffCount += compareElements(node1, node2, sInfo);
                if ((diffCount > 0) && (_returnAtFirstDiff)) {
                    return diffCount;
                }
                break;
            }
            case XPathNode.ATTRIBUTE: {
                if (!compareText(node1.getStringValue(), node2.getStringValue())) 
                {
                    if (_returnAtFirstDiff) return 1;
                    ++diffCount;
                }
                break;
            }
            case XPathNode.TEXT: {
                if (!compareText(node1.getStringValue(), node2.getStringValue())) 
                {
                    
                    if (sInfo.pw != null) {
                        sInfo.pw.println();
                        printLocationInfo(node1, node2, sInfo);
                        printText("- ", node1.getStringValue(), sInfo.pw);
                        sInfo.pw.println();
                        //printStartTag(node2.getParentNode());
                        printText("+ ", node2.getStringValue(), sInfo.pw);
                    }
                    
                    if (_returnAtFirstDiff) return 1;
                    ++diffCount;
                }
                break;
            }
            case XPathNode.NAMESPACE:
                break;
            default:
                break;
        }
        
        return diffCount;
    } //-- compare

    /**
     * Sets the flag for whether or not the order of element children
     * should be ignored. By default this value is <i>true</i>, indicating
     * that element ordering is not important. If order is important,
     * set this value to <i>false</i>.
     *
     * @param ignoreOrder a boolean that when <i>true</i> indicates that
     * child order is not important.
    **/
    public void setIgnoreChildOrder(boolean ignoreOrder) {
        this._strictChildOrder = (!ignoreOrder);
    } //-- setIgnoreChildOrder

    /**
     * Sets the flag for whether or not "ignorable" whitespace should
     * in fact be ignored. By default this value is "true", indicating
     * that all ignorable whitespace is ignored.
     *
     * @param ignoreWhitespace a boolean that when true indicates that
     * all ignorable whitespace should in fact be ignored. If false,
     * then differeces in whitespace will be reported.
    **/
    public void setIgnoreWhitespace(boolean ignoreWhitespace) {
        _ignoreWhitespace = ignoreWhitespace;
    } //-- setIgnoreWhitespace

    /**
     * Sets the flag for whether or not to return immediately
     * upon first difference found. This is useful when using 
     * the diff tool just to determine if two XPathNodes or files 
     * are XML-equivalent without enumerating all the differences.
     *
     * @param returnOnFirst the boolean that when true indicates
     * that the diff should terminate immediate upon first
     * difference. 
     *
    **/
    public void setReturnOnFirstDifference(boolean returnOnFirst) {
        _returnAtFirstDiff = returnOnFirst;
    } //-- setReturnOnFirstDifference
    
    /**
     * Checks the attributes of the given nodes to make sure they are identical 
     * but does not care of the order as per XML 1.0.
     *
    **/
    private boolean compareAttributes
        (XPathNode node1, XPathNode node2, StateInfo sInfo) 
    {
        
        
        XPathNode attr1 = node1.getFirstAttribute();
        XPathNode attr2 = node2.getFirstAttribute();

        boolean print = (sInfo.pw != null);
        
        if (attr1 == null) {
            if (attr2 == null) {
                return true;
            }
            else {
                if (print) {
                    sInfo.pw.print("- ");
                    printStartTag(node1, sInfo.pw);
                    sInfo.pw.print("+ ");
                    printStartTag(node2, sInfo.pw);
                }
                return false;
            }
        }
        else if (attr2 == null) {
            if (print) {
                sInfo.pw.print("- ");
                printStartTag(node1, sInfo.pw);
                sInfo.pw.print("+ ");
                printStartTag(node2, sInfo.pw);
            }
            return false;
        }

        int count1 = 0;
        while (attr1 != null) {
            
            String ns = attr1.getNamespaceURI();
            String attValue2 = node2.getAttribute(ns, attr1.getLocalName());
            
            if (attValue2 == null) {
                if (print) {
                    sInfo.pw.print("- ");
                    printStartTag(node1, sInfo.pw);
                    sInfo.pw.print("+ ");
                    printStartTag(node2, sInfo.pw);
                    sInfo.pw.println("The attribute '" + attr1.getLocalName() +
                        "' does not exist in the second node.");
                }
                return false;
            }
            if (!compareText(attr1.getStringValue(), attValue2)) {
                if (print) {
                    sInfo.pw.print("- ");
                    printStartTag(node1, sInfo.pw);
                    sInfo.pw.print("+ ");
                    printStartTag(node2, sInfo.pw);
                    sInfo.pw.println("The attribute '" + attr1.getLocalName() +
                        "' contains a difference value in the second node.");
                }
                return false;
            }
            
            ++count1;
            
            attr1 = attr1.getNext();
        }
        
        //-- check attr2 to make sure no additional attributes exist
        int count2 = 0;
        while (attr2 != null) {
            ++count2;
            attr2 = attr2.getNext();
        }
        
        if (count1 != count2) {
            if (print) {
                printLocationInfo(node1, node2, sInfo);
                sInfo.pw.print("- ");
                printStartTag(node1, sInfo.pw);
                sInfo.pw.print("+ ");
                printStartTag(node2, sInfo.pw);
            }
            return false;
        }
        
        return true;
    } //-- compareAttributes
    
    /**
     * Compares the two XPathNodes, both of which must be of type
     * XPathNode.ELEMENT, or ROOT.
     *
     * @param node1 the primary XPathNode to comapare against
     * @param node2 the XPathNode to compare against node1
     * @param sInfo the current StateInfo 
     * @return the number of differences
    **/
    private int compareElements
        (XPathNode node1, XPathNode node2, StateInfo sInfo) 
    {
        
        int diffCount = 0;
        
        boolean print = (sInfo.pw != null);
        
        //-- compare attributes
        if (!compareAttributes(node1, node2, sInfo)) {
            if (_returnAtFirstDiff) return 1;
                ++diffCount;
        }

        String name1 = node1.getLocalName();
        String name2 = node2.getLocalName();
        
        if (_strictChildOrder) {
            
            XPathNode child1 = node1.getFirstChild();
            XPathNode child2 = node2.getFirstChild();
                
            while ((child1 != null) && (child2 != null)) {
                    
                    
                //-- ignore whitespace
                if (_ignoreWhitespace) {
                    boolean skip = false;
                    if (child1.getNodeType() == XPathNode.TEXT) {
                        if (compareText(child1.getStringValue(), "")) {
                            child1 = child1.getNext();
                            skip = true;
                        }
                    }
                    if (child2.getNodeType() == XPathNode.TEXT) {
                        if (compareText(child2.getStringValue(), "")) {
                            child2 = child2.getNext();
                            skip = true;
                        }
                    }
                    if (skip) continue;
                }
                    
                int result = compare(child1, child2, sInfo);
                    
                if (result != 0) {
                    if (_returnAtFirstDiff) return result;
                    else diffCount += result;
                }
                child1 = child1.getNext();
                child2 = child2.getNext();
            }
                
            while (child1 != null) {
                if (_ignoreWhitespace) {
                    if (child1.getNodeType() == XPathNode.TEXT) {
                        if (compareText(child1.getStringValue(), "")) {
                            child1 = child1.getNext();
                            continue;
                        }
                    }
                }
                if (print) {
                    //-- print node
                    printLocationInfo(child1, null, sInfo);
                    sInfo.pw.print("- ");
                }
                if (_returnAtFirstDiff) return 1;
                ++diffCount;
                child1 = child1.getNext();
            }
            while (child2 != null) {
                if (_ignoreWhitespace) {
                    if (child2.getNodeType() == XPathNode.TEXT) {
                        if (compareText(child2.getStringValue(), "")) {
                            child2 = child2.getNext();
                            continue;
                        }
                    }
                }
                if (print) {
                    //-- print node
                    printLocationInfo(child2, null, sInfo);
                    sInfo.pw.print("+ ");
                }
                if (_returnAtFirstDiff) return 1;
                ++diffCount;
                child2 = child2.getNext();
            }
        }
        //-- Otherwise element order is not important...this case
        //-- is a bit harder to handle...
        else {
            
            XPathNode child1 = node1.getFirstChild();
            XPathNode child2 = node2.getFirstChild();
                
            int count1 = 0;
            
            // Save PrintWriter
            PrintWriter pw = sInfo.pw;
            sInfo.pw = null;  //-- prevent writing error messages
            
            List used = new List();
            
            while (child1 != null) {
                    
                //-- ignore whitespace
                if (_ignoreWhitespace) {
                    if (child1.getNodeType() == XPathNode.TEXT) {
                        if (compareText(child1.getStringValue(), "")) {
                            child1 = child1.getNext();
                            continue;
                        }
                    }
                }
                ++count1;
                
                child2 = node2.getFirstChild();
                
                int result = 1;
                while ((child2 != null) && (result != 0)) {
                    if (!used.contains(child2)) {
                        result = compare(child1, child2, sInfo);
                        if (result > 0) {
                            child2 = child2.getNext();
                        }
                        else {
                            used.add(child2);
                        }
                    }
                    else {
                        child2 = child2.getNext();
                    }
                }
                    
                //-- if we did not find a match...check
                //-- for best match and use it
                if (result != 0) {
                   
                    if (print) {
                        sInfo.pw = pw;
                        child2 = node2.getFirstChild();
                        while(child2 != null) {
                            if (!used.contains(child2)) {
                                if (hasSameType(child1, child2) &&
                                    hasSameName(child1, child2))
                                {
                                    result = compare(child1, child2, sInfo);
                                    break;
                                }
                            }
                            child2 = child2.getNext();
                        }
                        sInfo.pw = null;
                    }
                    /*
                    if (print) {
                        sInfo.pw = pw;
                        printLocationInfo(child1, null, sInfo);
                        pw.println("- missing child");
                        sInfo.pw = null;
                    }
                    */
                    if (_returnAtFirstDiff) return result;
                    else diffCount += result;
                }
                child1 = child1.getNext();
            }
            //-- replace PrintWriter
            sInfo.pw = pw;
             
            //-- count child2
            int count2 = 0;
            child2 = node2.getFirstChild();
            while (child2 != null) {
                
                //-- ignore whitespace
                if (_ignoreWhitespace) {
                    if (child2.getNodeType() == XPathNode.TEXT) {
                        if (compareText(child2.getStringValue(), "")) {
                            child2 = child2.getNext();
                            continue;
                        }
                    }
                }
                ++count2;
                child2 = child2.getNext();
            }
            
            if (count1 > count2) {
                String err = "additional child elements are contained " +
                    "within element '" + name1 + "' of the first node.";
                        
                if (print)
                    sInfo.pw.println(err);
                    
                if (_returnAtFirstDiff) 
                    return 1;
                    
                diffCount += (count1 - count2);
            }
            else if (count2 > count1) {
                String err = "additional child elements are contained " +
                    "within element '" + name2 + "' of the second node.";
                        
                if (sInfo.pw != null) 
                    sInfo.pw.println(err);

                if (_returnAtFirstDiff) 
                    return 1;
                    
                diffCount += (count2 - count1);
            }
        }
        return diffCount;
    } //-- compareElements

    /**
     * compare text and ignore "ignorableWhitespace"
     * if necessary.
    **/
    private boolean compareText(String s1, String s2) {
        
        if (_ignoreWhitespace) {
            StringTokenizer st1 = new StringTokenizer(s1);
            StringTokenizer st2 = new StringTokenizer(s2);

            while (st1.hasMoreTokens() && st2.hasMoreTokens()) {
                if (!st1.nextToken().equals(st2.nextToken())) {
                    return false;
                }
            }

            if (st1.hasMoreTokens() || st2.hasMoreTokens())
                return false; // one of the string is smaller than the other, we fail...
        }
        else {
            return s1.equals(s2);
        }

        return true;
    } //-- compareText
    
    private static String getNodeLocation(XPathNode node) {
        
        if (node == null) return "??";
        
        int column = -1;
        int line = -1;
        
        String xpath = "XPATH: " + getXPath(node);
        
        if (node instanceof Element) {
            Element elem = (Element)node;
            Location loc = (Location) elem.getProperty(Element.LOCATION_PROPERTY);
            if (loc != null) {
                line = loc.getLineNumber();
                column = loc.getColumnNumber();
            }
        }
        String location = null;
        if (line >= 0) {
            location = "[" + line + ", " + column + "] " + xpath;
        }
        else location = xpath;
        
        return location;
        
    } //-- nodeLocation

    private static String getXPath(XPathNode node) {
        
        String xpath = "";
        
        if (node != null) {
            switch(node.getNodeType()) {
                case XPathNode.ATTRIBUTE:
                    xpath = getXPath(node.getParentNode());
                    xpath += "/@" + node.getLocalName();
                    break;
                case XPathNode.ELEMENT:
                {
                    String name = node.getLocalName();                    
                    xpath = getXPath(node.getParentNode());
                    xpath += "/" + name;
                    //-- calculate position, if necessary
                    int position = 1;
                    XPathNode prev = node.getPrevious();
                    while (prev != null) {
                        if (name.equals(prev.getLocalName()))
                            ++position;
                        prev = prev.getPrevious();
                    }
                    
                    boolean usePosition = (position > 1);
                    if (!usePosition) {
                        //-- check next nodes
                        XPathNode next = node.getNext();
                        while (next != null) {
                            if (name.equals(next.getLocalName())) {
                                usePosition = true;
                                break;
                            }
                            next = next.getNext();
                        }
                    }
                    if (usePosition) {
                        xpath += "[" + position + "]";
                    }
                    break;
                }
                case XPathNode.TEXT:
                    xpath = getXPath(node.getParentNode());
                    xpath += "/text()";
                    break;                
                case XPathNode.ROOT:
                default:
                    break;
            }
        }
        
        return xpath;        
    } //-- getXPath

    private boolean hasSameName(XPathNode node1, XPathNode node2) {
        //-- compare local names
        String name1 = node1.getLocalName();
        String name2 = node2.getLocalName();
        
        //-- ROOT may or may not have null name, so we must check
        //-- for possible null values
        if (name1 == null) {
            return (name2 == null);
        }
        return name1.equals(name2);
    } //-- hasSameName
    
    private boolean hasSameType(XPathNode node1, XPathNode node2) {
        return (node1.getNodeType() == node2.getNodeType());
    } //-- hasSameType
    

    private void printLocationInfo
        (XPathNode node1, XPathNode node2, StateInfo sInfo) 
    {
        if (sInfo.header) {
            sInfo.header = false;
            sInfo.pw.println("--- " + sInfo.url1);
            sInfo.pw.println("+++ " + sInfo.url2);
        }
        sInfo.pw.print("@@ -");
        sInfo.pw.print(getNodeLocation(node1));
        sInfo.pw.print(" +");
        sInfo.pw.print(getNodeLocation(node2));
        sInfo.pw.println(" @@");
    } //-- printLocationInfo
    
    private static void printStartTag(XPathNode node, PrintWriter pw) {
        
        if (node.getNodeType() != XPathNode.ELEMENT) 
            return;
            
        pw.print('<' + node.getLocalName());
        
        XPathNode attr = node.getFirstAttribute();
        while (attr != null) {
            pw.print(' ');
            pw.print(attr.getLocalName());
            pw.print("=\"");
            pw.print(attr.getStringValue());
            pw.print("\"");
            attr = attr.getNext();
        }
        pw.println('>');
    } //-- printStartTag
    
    private static void printText
        (String prefix, String text, PrintWriter pw) 
    {
        if (prefix == null) {
            if (text != null) {
                pw.println(text);
            }
            return;
        }
        if (text == null) {
            pw.println(prefix);
            return;
        }
        int idx = 0;
        while ((idx = text.indexOf('\n')) >= 0) {
            pw.print(prefix);
            pw.println(text.substring(0, idx));
            text = text.substring(idx+1);
        }
        pw.print(prefix);
        pw.println(text);
    } //-- prefix
    
    /**
     * main method, for command line invocation
     * @param args an array of arguments
    **/
    public static void main(String args[]) {
               
        if (args.length < 2) {
            System.out.println();
            System.out.println(" error: missing arguments.");
            System.out.println();
            String className = XMLDiff.class.getName();
            System.out.println(" usage: java " + className + " filename1 filename2");
            return;
        }
        
        XMLDiff diff = new XMLDiff();
        
        try {
            int result = diff.compare(args[0], args[1]);
            String msg = null;
            if (result == 0) {
                msg = "XMLDiff exited normally with no differences found.";
            }
            else {
                msg = "XMLDiff exited normally with " + result + 
                    " difference(s) found.";
            }
            System.out.println();
            System.out.println(msg);
        }
        catch(java.io.IOException iox) {
            iox.printStackTrace();
        }
        
    } //-- main
    
    class StateInfo {
        PrintWriter pw = null;
        String url1    = null;
        String url2    = null;
        boolean header = false;
    }
    
} //-- class: XMLDiff
