/*
 * Copyright 2007 Edward Kuns
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: XMLDiff.java 0000 2007-01-11 00:00:00Z ekuns $
 */
package org.exolab.castor.tests.framework.xmldiff;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.exolab.castor.tests.framework.xmldiff.xml.XMLFileReader;
import org.exolab.castor.tests.framework.xmldiff.xml.nodes.Attribute;
import org.exolab.castor.tests.framework.xmldiff.xml.nodes.Element;
import org.exolab.castor.tests.framework.xmldiff.xml.nodes.ParentNode;
import org.exolab.castor.tests.framework.xmldiff.xml.nodes.Root;
import org.exolab.castor.tests.framework.xmldiff.xml.nodes.XMLNode;

/**
 * A utility class used to compare two XMLNodes, or XML input files and report
 * the differences between them.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-11 00:00:00 -0600 (Thu, 11 Jan 2007) $
 * @since Castor 1.1
 */
public class XMLDiff {

    /** The namespace of XML Schema. */
    private static final String XMLSCHEMA_INSTANCE = "http://www.w3.org/2001/XMLSchema-instance";

    /** Filename of the 1st XML item being compared. */
    private final String      _file1;
    /** Filename of the 2nd XML item being compared. */
    private final String      _file2;
    /** PrintWriter used for this comparison. */
    private final PrintWriter _pw               = new PrintWriter(System.out, true);

    /**
     * A boolean to indicate whether or not child order should be strictly
     * enforced. By default child order is treated as "all", meaning that as
     * long as a child exists, it doesn't matter which order the children occur.
     */
    private boolean           _strictChildOrder = false;
    /** True if we should print errors to the PrintWriter. */
    private boolean           _print            = true;
    /** True if we have not yet printed the diff header. */
    private boolean           _header           = true;

    /**
     * Constructs an XMLDiff object that is ready to compare the two provided
     * XML files.
     *
     * @param file1 The first XML file for comparison.
     * @param file2 The second XML file for comparison.
     */
    public XMLDiff(final String file1, final String file2) {
        if (file1 == null) {
            String err = "The argument 'file1' may not be null.";
            throw new IllegalArgumentException(err);
        }

        if (file2 == null) {
            String err = "The argument 'file2' may not be null.";
            throw new IllegalArgumentException(err);
        }

        _file1 = file1;
        _file2 = file2;
    }

    /**
     * Compares the two XML documents located at the given URL locations.
     * Returns 0, if no differences are found, otherwise returns a positive
     * number indicating the number of differences.
     * <p>
     * This is the only public method in this class.
     *
     * @param file1 the location of the XML document to compare.
     * @param file2 the location of the XML document to compare against url1.
     * @return 0, if no differences are found, otherwise a positive number
     *         indicating the number of differences.
     * @throws java.io.IOException an this occurs while reading
     */
    public int compare() throws java.io.IOException {
        XMLFileReader reader1 = new XMLFileReader(_file1);
        XMLNode node1 = reader1.read();

        XMLFileReader reader2 = new XMLFileReader(_file2);
        XMLNode node2 = reader2.read();

        return compareNodes(node1, node2);
    }

    /**
     * Compares the given XMLNodes. Returns 0 if no differences are found,
     * otherwise returns a positive number indicating the number of differences.
     *
     * @param node1 the first XMLNode to compare
     * @param node2 the second XMLNode to compare
     * @return 0, if no differences are found, otherwise a positive number
     *         indicating the number of differences.
     */
    private int compareNodes(final XMLNode node1, final XMLNode node2) {
        // Compare node types
        if (!hasSameType(node1, node2)) {
            if (_print) {
                _pw.println("Types differ: <" + node1.getLocalName() + "> and <"
                            + node2.getLocalName() + "> for" + node1.getNodeLocation());
            }
            return 1;
        }

        int diffCount = 0;

        String ns1 = node1.getNamespaceURI();
        String ns2 = node2.getNamespaceURI();
        if (!compareTextNullEqualsEmpty(ns1, ns2)) {
            if (_print) {
                _pw.println("Namespaces differ: ('" + ns1 + "' != '" + ns2 + "') for "
                            + node1.getNodeLocation());
            }

            ++diffCount;
        }

        // Compare local names (if both are null it's OK)
        String name1 = node1.getLocalName();
        String name2 = node2.getLocalName();

        if (name1 == null && name2 != null) {
            if (_print) {
                _pw.println("Names differ: null vs. <" + name2 + "> for "
                            + node1.getNodeLocation());
            }
            ++diffCount;
            return diffCount;
        } else if (name2 == null && name1 != null) {
            if (_print) {
                _pw.println("Names differ: <" + name1 + "> vs null for "
                            + node1.getNodeLocation());
            }
            ++diffCount;
            return diffCount;
        } else if (name1 != null && !name1.equals(name2)) {
            if (_print) {
                _pw.println("Names differ: <" + name1 + "> != <" + name2 + "> for "
                            + node1.getNodeLocation());
            }
            ++diffCount;
            return diffCount;
        }

        // Compare node content
        switch (node1.getNodeType()) {
            case XMLNode.ROOT:
                diffCount += compareElementsStrictOrder((Root)node1, (Root)node2);
                break;

            case XMLNode.ELEMENT:
                diffCount += compareElements((Element)node1, (Element)node2);
                break;

            case XMLNode.ATTRIBUTE:
                diffCount += compareStringValues(node1, node2);
                break;

            case XMLNode.TEXT:
                diffCount += compareStringValues(node1, node2);
                break;

            case XMLNode.PROCESSING_INSTRUCTION:
                // We don't care about comparing processing instructions
                break;

            default:
                System.out.println("Unexpected node type in XMLDiff: " + node1.getNodeType());
                break;
        }

        return diffCount;
    } // -- compare

    /**
     * Compares the String values of the provided XML Nodes.
     * @param node1 The first node to be compared
     * @param node2 The second node to be compared
     * @return 0 if the String values are the same, 1 otherwise
     */
    private int compareStringValues(final XMLNode node1, final XMLNode node2) {
        if (compareText(node1.getStringValue(), node2.getStringValue())) {
            return 0;
        }

        if (_print) {
            _pw.println();
            printLocationInfo(node1, node2);
            printText("- ", node1.getStringValue());
            _pw.println();
            printText("+ ", node2.getStringValue());
        }
        return 1;
    }

    /**
     * Checks the attributes of the given nodes to make sure they are identical
     * but does not care about the attribute order, as per XML 1.0.
     */
    private int compareAttributes(final Element node1, final Element node2) {
        int diffCount = 0;

        for (Iterator i = node1.getAttributeIterator(); i.hasNext(); ) {
            Attribute attr1 = (Attribute) i.next();

            // Does node2 have this attribute at all?
            String attValue2 = node2.getAttribute(attr1.getNamespaceURI(), attr1.getLocalName());
            if (attValue2 == null) {
                // Is this an attribute that is allowed to be missing sometimes?
                if (missingattributeIsIgnorable(attr1)) {
                    continue;
                }

                // If not, complain
                printElementChangeBlock(node1, node2, "Attribute '"
                                        + attr1.getNodeLocation()
                                        + "' does not exist in the second document.");
                diffCount++;
                continue;
            }

            // If it does, does it have the same value?
            String attValue1 = attr1.getStringValue();
            if (!compareTextLikeQName(node1, node2, attValue1, attValue2)) {
                printElementChangeBlock(node1, node2, "Attribute '"
                                        + attr1.getNodeLocation()
                                        + "' values are different.");
                diffCount++;
            }
        }

        // Look for attributes on node 2 that are not on node 1
        for (Iterator i = node2.getAttributeIterator(); i.hasNext(); ) {
            Attribute attr2 = (Attribute) i.next();
            if (node1.getAttribute(attr2.getNamespaceURI(), attr2.getLocalName()) == null) {
                // Is this an attribute that is allowed to be missing sometimes?
                if (missingattributeIsIgnorable(attr2)) {
                    continue;
                }

                // If not, complain
                printElementChangeBlock(node1, node2, "Attribute '"
                                        + attr2.getNodeLocation()
                                        + "' does not exist in the first document.");
                diffCount++;
            }
        }

        return diffCount;
    }

    private boolean missingattributeIsIgnorable(Attribute attr) {
        String name = attr.getLocalName();
        String ns = attr.getNamespaceURI();
        if (ns == null) {
            ns = "";
        }

        if (name.equals("noNamespaceSchemaLocation") && ns.equals(XMLSCHEMA_INSTANCE)) {
            return true;
        }
        if (name.equals("schemaLocation") && ns.equals(XMLSCHEMA_INSTANCE)) {
            return true;
        }
        return false;
    }

    /**
     * Compare the provided attribute text as if it were a QName.
     *
     * @param node1 Node containing attribute 1
     * @param node2 Node containing attribute 2
     * @param attValue1 String value of attribute 1
     * @param attValue2 String value of attribute 2
     * @return true if the attributes are equal directly, or equal when compared
     *         as QNames
     */
    private boolean compareTextLikeQName(final XMLNode node1, final XMLNode node2,
                                         final String attValue1, final String attValue2) {
        // If strings are equal, return equal
        if (compareText(attValue1, attValue2)) {
            return true;
        }

        // If neither attribute value has ":" then return false
        final int idx1 = attValue1.indexOf(':');
        final int idx2 = attValue2.indexOf(':');
        if (idx1 < 0 && idx2 < 0) {
            return false;
        }

        final String prefix1;
        final String prefix2;
        final String value1;
        final String value2;

        if (idx1 >= 0) {
            value1 = attValue1.substring(idx1 + 1);
            prefix1 = attValue1.substring(0, idx1);
        } else {
            value1 = attValue1;
            prefix1 = "";
        }

        if (idx2 >= 0) {
            value2 = attValue2.substring(idx2 + 1);
            prefix2 = attValue2.substring(0, idx2);
        } else {
            value2 = attValue2;
            prefix2 = "";
        }

        // Return true if text value is equal and namesspaces are equal
        return compareText(value1, value2)
               && compareTextNullEqualsEmpty(node1.getNamespaceURI(prefix1),
                                             node2.getNamespaceURI(prefix2));
    }

    /**
     * Compares the two XMLNodes, both of which must be of type XMLNode.ELEMENT
     * or XMLNode.ROOT.
     *
     * @param node1 the primary XMLNode to comapare against
     * @param node2 the XMLNode to compare against node1
     * @return the number of differences found in this document tree
     */
    private int compareElements(final Element node1, final Element node2) {
        int diffCount = compareAttributes(node1, node2);

        if (_strictChildOrder) {
            diffCount += compareElementsStrictOrder(node1, node2);
        } else {
            diffCount += compareElementsLooseOrder(node1, node2);
        }
        return diffCount;
    }

    /**
     * Compares the two XMLNodes (not counting attributes) requiring strict
     * child order, both of which must be of type XMLNode.ELEMENT or
     * XMLNode.ROOT.
     *
     * @param node1 the primary XMLNode to comapare against
     * @param node2 the XMLNode to compare against node1
     * @return the number of differences found in this document tree
     */
    private int compareElementsStrictOrder(final ParentNode node1, final ParentNode node2) {
        int diffCount = 0;

        Iterator i1 = node1.getChildIterator();
        Iterator i2 = node2.getChildIterator();

        // Skip all ignorable whitespace and compare with strict order
        if (i1.hasNext() && i2.hasNext()) {
            XMLNode child1 = (XMLNode) i1.next();
            XMLNode child2 = (XMLNode) i2.next();
            while (child1 != null && child2 != null) {
                if (nodeIsIgnorableText(child1)) {
                    if (!i1.hasNext()) {
                        break;
                    }
                    child1 = (XMLNode) i1.next();
                    continue;
                }
                if (nodeIsIgnorableText(child2)) {
                    if (!i2.hasNext()) {
                        break;
                    }
                    child2 = (XMLNode) i2.next();
                    continue;
                }

                diffCount += compareNodes(child1, child2);

                if (!i1.hasNext() || !i2.hasNext()) {
                    break;
                }

                child1 = (XMLNode) i1.next();
                child2 = (XMLNode) i2.next();
            }
        }

        // If we have excess nodes for root1, complain about missing elements
        while (i1.hasNext()) {
            XMLNode child1 = (XMLNode) i1.next();
            if (!nodeIsIgnorableText(child1)) {
                if (_print) {
                    printLocationInfo(child1, null);
                    _pw.println("- ");
                }
                ++diffCount;
            }
        }

        // If we have excess nodes for root2, complain about extra elements
        while (i2.hasNext()) {
            XMLNode child2 = (XMLNode) i2.next();
            if (!nodeIsIgnorableText(child2)) {
                if (_print) {
                    printLocationInfo(child2, null);
                    _pw.println("- ");
                }
                ++diffCount;
            }
        }

        return diffCount;
    }

    /**
     * Compares the two XMLNodes, both of which must be of type XMLNode.ELEMENT
     * or XMLNode.ROOT.
     *
     * @param node1 the primary XMLNode to comapare against
     * @param node2 the XMLNode to compare against node1
     * @return the number of differences found in this document tree
     */
    private int compareElementsLooseOrder(final Element node1, final Element node2) {
        int diffCount = 0;

        final List used = new LinkedList();

        for (Iterator i1 = node1.getChildIterator(); i1.hasNext(); ) {
            XMLNode child1 = (XMLNode) i1.next();
            // Ignore whitespace
            // If we find an exact match, continue with the next node in the list
            if (nodeIsIgnorableText(child1) || foundExactMatch(node2, child1, used)) {
                continue;
            }

            // Check for the best match and use it to count diffs & complain
            if (_print) {
                diffCount += closestMatchDifference(node2, child1, used);
            } else {
                diffCount++;
            }
        }

        // Complain about all children of node2 that are not used and not ignorable whitespace
        for (Iterator i2 = node2.getChildIterator(); i2.hasNext(); ) {
            XMLNode child2 = (XMLNode) i2.next();
            if (!nodeIsIgnorableText(child2) && !used.contains(child2)) {
                if (_print) {
                    _pw.println("Extra child node: " + child2.getNodeLocation());
                }
                ++diffCount;
            }
        }

        return diffCount;
    }

    /**
     * Looks for an exact match for the provided target XMLNode. If found,
     * returns true. Suppresses complaints during search. If an exact match is
     * found, the match is added to the list of "used" items.
     *
     * @param parent The node whose children to search for an exact match
     * @param target The XMLNode we are trying to match
     * @param usedList The list of children of node2 that have already matched
     *        other objects
     * @return true if an exact match is found for the provided node.
     */
    private boolean foundExactMatch(final Element parent, XMLNode target, final List usedList) {
        // Suppress complaints when we are looking for an exact match.
        boolean previousPrint = _print;

        _print = false; // Suppress printing when we are "just looking"
        boolean found = false;
        for (Iterator i2 = parent.getChildIterator(); i2.hasNext(); ) {
            XMLNode child2 = (XMLNode) i2.next();
            if (!usedList.contains(child2) && compareNodes(target, child2) == 0) {
                usedList.add(child2);
                found = true;
                break;
            }
        }

        // Restore printing
        _print = previousPrint;
        return found;
    }

    /**
     * Looks for a close patch to the provided target XMLNode among the children
     * of the provided parent node.  The difference between the closest match
     * and the target is returned. If we cannot even find a close match, then
     * we declare the target missing and return one difference.
     * <p>
     * Note:  This method is only called when printing is enabled.
     *
     * @param parent The node whose children to search for an exact match
     * @param target The XMLNode we are trying to match
     * @param usedList The list of children of node2 that have already matched
     *        other objects
     * @return the difference count
     */
    private int closestMatchDifference(final Element parent, final XMLNode target,
                                       final List usedList) {
        for (Iterator i2 = parent.getChildIterator(); i2.hasNext(); ) {
            XMLNode child2 = (XMLNode) i2.next();
            if (!usedList.contains(child2) && hasSameType(target, child2)
                && hasSameName(target, child2)) {
                usedList.add(child2);
                return compareNodes(target, child2);
            }
        }

        _pw.println("Missing child node: " + target.getNodeLocation() + " for "
                    + target.getNodeLocation());
        return 1;
    }

    /**
     * Returns true if the given node is a TEXT node that contains only
     * ignorable whitespace.
     *
     * @return true if the given node is a TEXT node that contains only
     *         ignorable whitespace.
     */
    private boolean nodeIsIgnorableText(final XMLNode child) {
        return (child.getNodeType() == XMLNode.TEXT && compareText(child.getStringValue(), ""));
    }

    /**
     * Returns true if the two Strings are equal, ignoring whitespace
     * differences that are ignorable.
     *
     * @return true if the two Strings are equal, ignoring whitespace
     *         differences that are ignorable.
     */
    private boolean compareText(final String s1, final String s2) {
        if (s1.equals(s2)) {
            return true;
        }

        // Strings are different; compare token by token to ignore whitespace differences
        StringTokenizer st1 = new StringTokenizer(s1);
        StringTokenizer st2 = new StringTokenizer(s2);

        while (st1.hasMoreTokens() && st2.hasMoreTokens()) {
            if (!st1.nextToken().equals(st2.nextToken())) {
                return false;
            }
        }

        // If the Strings have different numbers of tokens, fail
        if (st1.hasMoreTokens() || st2.hasMoreTokens()) {
            return false;
        }

        return true;
    }

    /**
     * Compares two strings. Considers null Strings to be the same as an empty
     * String.
     *
     * @param one The first string to compare.
     * @param two The second string to compare.
     * @return true if the two strings are equals or are both "null or empty"
     */
    private boolean compareTextNullEqualsEmpty(String one, String two) {
        String text1 = (one == null) ? "" : one;
        String text2 = (two == null) ? "" : two;
        return text1.equals(text2);
    }

    private boolean hasSameName(final XMLNode node1, final XMLNode node2) {
        String name1 = node1.getLocalName();
        String name2 = node2.getLocalName();

        // ROOT may or may not have null name, so we must check for possible null values
        if (name1 == null) {
            return (name2 == null);
        }
        return name1.equals(name2);
    }

    private boolean hasSameType(final XMLNode node1, final XMLNode node2) {
        return (node1.getNodeType() == node2.getNodeType());
    }

    private void printLocationInfo(final XMLNode node1, final XMLNode node2) {
        if (_header) {
            _header = false;
            _pw.println("--- " + _file1);
            _pw.println("+++ " + _file2);
        }
        _pw.print("@@ -");
        _pw.print(node1.getNodeLocation());
        _pw.print(" +");
        _pw.print(node2.getNodeLocation());
        _pw.println(" @@");
    }

    private void printElementChangeBlock(final Element node1, final Element node2, final String msg) {
        if (_print) {
            _pw.print("- ");
            printElement(node1);
            _pw.print("+ ");
            printElement(node2);
            if (msg != null) {
                _pw.println(msg);
            }
        }
    }

    private void printElement(final Element node) {
        _pw.print('<' + node.getLocalName());

        for (Iterator i = node.getAttributeIterator(); i.hasNext(); ) {
            Attribute attr = (Attribute) i.next();
            _pw.print(' ');
            _pw.print(attr.getLocalName());
            _pw.print("=\"");
            _pw.print(attr.getStringValue());
            _pw.print("\"");
        }

        _pw.println('>');
    }

    /**
     * Prints the given text. Each line of the text is prefixed with the given
     * prefix. If <code>text</code> has multiple newlines, the prefix will be
     * printed on each line.
     *
     * @param prefix A prefix to display on each line of output
     * @param text The text to display
     */
    private void printText(final String prefix, String text) {
        if (text == null) {
            _pw.println(prefix);
            return;
        }

        int idx = 0;
        while ((idx = text.indexOf('\n')) >= 0) {
            _pw.print(prefix);
            _pw.println(text.substring(0, idx));
            text = text.substring(idx + 1);
        }
        _pw.print(prefix);
        _pw.println(text);
    }

}
