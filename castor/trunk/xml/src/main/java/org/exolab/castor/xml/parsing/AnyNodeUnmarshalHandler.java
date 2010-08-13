package org.exolab.castor.xml.parsing;

import org.exolab.castor.types.AnyNode;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.util.SAX2ANY;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AnyNodeUnmarshalHandler {

	private NamespaceHandling _namespaceHandling;

	/**
	 * A SAX2ANY unmarshaller in case we are dealing with {@literal <any>}.
	 */
	private SAX2ANY _anyUnmarshaller = null;

	/**
	 * The any branch depth.
	 */
	private int _depth = 0;

	/**
	 * Keeps track of the current element information as passed by the parser.
	 */
	private ElementInfo _elemInfo = null;

	public AnyNodeUnmarshalHandler(NamespaceHandling namespaceHandling) {
		_namespaceHandling = namespaceHandling;
	}

	/**
	 * Delegates startElement to SAX2ANY. Sets any branch depth counter to 1.
	 * 
	 * @param name
	 *            Name of the element if we use SAX 2
	 * @param namespace
	 *            Namespace of the element
	 * @param wsPreserve
	 *            preserve whitespaces ?
	 * @return Object anyUnmarshaller get StartingNode
	 * @throws SAXException
	 */
	public Object commonStartElement(String name, String namespace,
			boolean wsPreserve) throws SAXException {
		// 1- creates a new SAX2ANY handler
		_anyUnmarshaller = new SAX2ANY(_namespaceHandling.getNamespaceContext(), wsPreserve);
		// 2- delegates the element handling
		if (_elemInfo._attributeList != null) {
			// -- SAX 1
			startElement(_elemInfo._qName, _elemInfo._attributeList);
		} else {
			// -- SAX 2
			startElement(namespace, name, _elemInfo._qName,
					_elemInfo._attributes);
		}
		// first element so depth can only be one at this point
		_depth = 1;
		return _anyUnmarshaller.getStartingNode();
	}

	/**
	 * delegates ignorableWhitespace call to SAX2ANY.
	 * 
	 * @param ch
	 *            Characters
	 * @param start
	 *            Offset
	 * @param length
	 *            Length
	 * @throws SAXException
	 */
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		_anyUnmarshaller.ignorableWhitespace(ch, start, length);
	}

	/**
	 * Delegates SAX1 startElement to SAX2ANY. Increases any branch depth
	 * counter.
	 * 
	 * @param name Name of the element.
	 * @param attList Attribute list.
	 * @throws SAXException
	 */
	public void startElement(String name, AttributeList attList)
			throws SAXException {
		_depth++;
		_anyUnmarshaller.startElement(name, attList);
		return;
	}

	/**
	 * Delegates SAX2 startElement to SAX2ANY. Increases any branch depth
	 * counter.
	 * 
	 * @param namespaceURI
	 * @param localName
	 * @param qName
	 * @param atts
	 * @throws SAXException
	 */
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		_depth++;
		_anyUnmarshaller.startElement(namespaceURI, localName, qName, atts);
		return;
	}

	/**
	 * Delegates endElement to SAX2ANY.
	 * 
	 * @param name
	 * @throws SAXException
	 */
	public void endElement(String name) throws SAXException {
		_anyUnmarshaller.endElement(name);
		_depth--;
	}

	/**
	 * delegates characters to SAX2ANY.
	 * 
	 * @param ch
	 * @param start
	 * @param length
	 * @throws SAXException
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		_anyUnmarshaller.characters(ch, start, length);
	}

	/**
	 * delegates startPrefixMapping to SAX2ANY.
	 * 
	 * @param prefix
	 * @param uri
	 * @throws SAXException
	 */
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		_anyUnmarshaller.startPrefixMapping(prefix, uri);
	}

	/**
	 * delegates endPrefixMapping to SAX2ANY.
	 * 
	 * @param prefix
	 * @throws SAXException
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		_anyUnmarshaller.endPrefixMapping(prefix);
	}

	/**
	 * Checks if there is a SAX2ANY object
	 * 
	 * @return True if there is a SAX2ANY.
	 */
	public boolean hasAnyUnmarshaller() {
		return _anyUnmarshaller != null;
	}

	/**
	 * Checks if any branch depth counter is zero.
	 * 
	 * @return true if 0
	 */
	public boolean isStartingNode() {
		return _depth == 0;
	}

	/**
	 * Returns SAX2ANY startingNode.
	 * 
	 * @return
	 */
	public AnyNode getStartingNode() {
		AnyNode startingNode = _anyUnmarshaller.getStartingNode();
		_anyUnmarshaller = null;
		return startingNode;
	}

	/**
	 * Preserves passed name and attributes.
	 * 
	 * @param name
	 *            Name
	 * @param atts
	 *            Attributes
	 */
	public void preservePassedArguments(String name, Attributes atts) {
		setElementInfo(_elemInfo, name, atts);
	}

	/**
	 * Preserves passed name and attributes.
	 * 
	 * @param name
	 *            Name
	 * @param attList
	 *            AttributeList
	 */
	public void preservePassedArguments(String name, AttributeList attList) {
		setElementInfo(_elemInfo, name, attList);
	}

	/**
	 * Sets name and attList on element
	 * 
	 * @param element
	 *            ElementInfo, can be null
	 * @param name
	 *            Name
	 * @param attList
	 *            Attributes
	 * @return element on which name and attList are set
	 */
	private ElementInfo setElementInfo(ElementInfo element, String name,
			AttributeList attList) {
		if (element == null) {
			return new ElementInfo(name, attList);
		}
		element.clear();
		element._qName = name;
		element._attributeList = attList;
		return element;
	}

	/**
	 * Sets name and attributes on element
	 * 
	 * @param element
	 *            ElementInfo, can be null
	 * @param name
	 *            Name
	 * @param attributes
	 *            Attributes
	 * @return element on which name and attributes are set
	 */
	private ElementInfo setElementInfo(ElementInfo element, String name,
			Attributes attributes) {
		if (element == null) {
			return new ElementInfo(name, attributes);
		}
		element.clear();
		element._qName = name;
		element._attributes = attributes;
		return element;
	}

	/**
	 * A utility class for keeping track of the qName and how the SAX parser
	 * passed attributes.
	 */
	class ElementInfo {
		private String _qName = null;
		private Attributes _attributes = null;
		private AttributeList _attributeList = null;

		ElementInfo() {
			super();
		}

		ElementInfo(String qName, Attributes atts) {
			super();
			_qName = qName;
			_attributes = atts;
		}

		ElementInfo(String qName, AttributeList atts) {
			super();
			_qName = qName;
			_attributeList = atts;
		}

		void clear() {
			_qName = null;
			_attributes = null;
			_attributeList = null;
		}

	} // -- ElementInfo

}
