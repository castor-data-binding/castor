package org.exolab.castor.xml.parsing;

import java.util.Enumeration;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.UnmarshalHandler;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.xml.sax.SAXException;

/**
 * This class is used by the {@link UnmarshalHandler} to handle name spaces. It
 * manages a stack of name spaces, keeps track of when an new name space scope
 * is needed and maps name space URIs to package names.
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 * @since 1.3.2
 */
public class NamespaceHandling {

    /**
     * The name space stack.
     */
    private Namespaces _namespaces = new Namespaces();

    /**
     * A map of name space URIs to package names.
     */
    private HashMap<String, String> _namespaceToPackage = new HashMap<String, String>();

    /**
     * A flag to keep track of when a new name space scope is needed.
     */
    private boolean _createNamespaceScope = true;

    /**
     * The built-in XML prefix used for xml:space, xml:lang and, as the XML 1.0
     * Namespaces document specifies, are reserved for use by XML and XML
     * related specs.
     **/
    private static final String XML_PREFIX = "xml";

    /**
     * Adds a mapping from the given namespace URI to the given package name.
     * 
     * @param nsURI
     *            the namespace URI to map from.
     * @param packageName
     *            the package name to map to.
     */
    public void addNamespaceToPackageMapping(String nsURI, String packageName) {
        _namespaceToPackage.put(StringUtils.defaultString(nsURI), StringUtils
                .defaultString(packageName));

    }

    /**
     * Looks up the package name from the given namespace URI.
     * 
     * @param namespace
     *            the namespace URI to lookup
     * @return the package name or null.
     */
    public String getMappedPackage(final String namespace) {
        return _namespaceToPackage.get(StringUtils.defaultString(namespace));
    }

    /**
     * Saves local namespace declarations to the object model if necessary.
     * 
     * @param classDesc
     *            the current ClassDescriptor.
     * @param object
     *            the Object of the current state
     **/
    public void processNamespaces(XMLClassDescriptor classDesc, Object object) {

        if (classDesc == null) {
            return;
        }

        // -- process namespace nodes
        XMLFieldDescriptor nsDescriptor = classDesc.getFieldDescriptor(null,
                null, NodeType.Namespace);

        if (nsDescriptor != null) {
            FieldHandler handler = nsDescriptor.getHandler();
            if (handler != null) {
                Enumeration<String> enumeration = _namespaces
                        .getLocalNamespacePrefixes();
                while (enumeration.hasMoreElements()) {
                    String nsPrefix = StringUtils.defaultString(enumeration
                            .nextElement());
                    String nsURI = StringUtils.defaultString(_namespaces
                            .getNamespaceURI(nsPrefix));
                    MapItem mapItem = new MapItem(nsPrefix, nsURI);
                    handler.setValue(object, mapItem);
                }
            }
        }
    }

    /**
     * Extracts the prefix and resolves it to it's associated namespace. If the
     * prefix is 'xml', then no resolution will occur, however in all other
     * cases the resolution will change the prefix:value as {NamespaceURI}value
     * 
     * @param value
     *            the QName to resolve.
     * @return
     * @throws SAXException
     *             if the nammespace associated with the prefix is null
     */
    public Object resolveNamespace(Object value) throws SAXException {

        if ((value == null) || !(value instanceof String)) {
            return value;
        }

        String result = (String) value;
        int idx = result.indexOf(':');
        String prefix = null;
        if (idx > 0) {
            prefix = result.substring(0, idx);
            if (XML_PREFIX.equals(prefix)) {
                // -- Do NOT Resolve the 'xml' prefix.
                return value;
            }
            result = result.substring(idx + 1);
        }
        String namespace = getNamespaceURI(prefix);
        if (StringUtils.isNotEmpty(namespace)) {
            result = '{' + namespace + '}' + result;
            return result;
        } else if ((namespace == null) && (prefix != null)) {
            throw new SAXException(
                    "The namespace associated with the prefix: '" + prefix
                            + "' is null.");
        } else {
            return result;
        }

    }

    /**
     * Pops the current namespace instance
     */
    public void removeCurrentNamespaceInstance() {
        _namespaces = _namespaces.getParent();
    }

    /**
     * Binds the namespaceURI to the default namespace.
     * 
     * @param namespaceURI
     *            Namespace URI
     */
    public void addDefaultNamespace(String namespaceURI) {
        _namespaces.addNamespace("", namespaceURI);
    }

    /**
     * Binds the namespaceURI to the prefix
     * 
     * @param prefix
     *            XML name space prefix
     * @param namespaceURI
     *            XML name space URI.
     */
    public void addNamespace(String prefix, String namespaceURI) {
        _namespaces.addNamespace(prefix, namespaceURI);

    }

    /**
     * Gets the prefix that is bound to a namespaceURI.
     * 
     * @param namespaceURI
     *            the namespaceURI to get the prefix from
     * @return prefix
     */
    public String getNamespacePrefix(String namespaceURI) {
        return _namespaces.getNamespacePrefix(namespaceURI);
    }

    /**
     * Gets the namespaceURI that is bound to a prefix.
     * 
     * @param prefix
     *            the prefix to get the namespaceURI from
     * @return namespaceURI The corresponding namespace URI.
     */
    public String getNamespaceURI(String prefix) {
        return _namespaces.getNamespaceURI(prefix);
    }

    /**
     * Gets the namespace URI that is bound to the default name space.
     * 
     * @return namespaceURI The namespace URI bound to the default namespace.
     */
    public String getDefaultNamespaceURI() {
        return _namespaces.getNamespacePrefix("");
    }

    /**
     * Creates a new name space.
     */
    public void createNamespace() {
        _namespaces = _namespaces.createNamespaces();
    }

    /**
     * Returns the current name space context.
     * @return The current name space stack (context).
     */
    public Namespaces getNamespaceContext() {
        return _namespaces;
    }

    /**
     * Indicates whether a new name space scope is needed.
     * 
     * @return true if a new name space scope is necessary.
     */
    public boolean isNewNamespaceScopeNecessary() {
        if (_createNamespaceScope) {
            return true;
        }
        return false;
    }

    /**
     * Starts a new name space scope, and resets the corresponding flag.
     */
    public void startNamespaceScope() {
        createNamespace();
        _createNamespaceScope = true;
    }

    /**
     * Stops a name space scope, and resets the corresponding flag to false.
     */
    public void stopNamespaceScope() {
        createNamespace();
        _createNamespaceScope = false;
    }
}
