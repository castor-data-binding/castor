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
 * Copyright 1999-2004 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import org.exolab.castor.net.URIResolver;
import org.exolab.castor.net.util.URIResolverImpl;
import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.AttributeGroupDecl;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Form;
import org.exolab.castor.xml.schema.SchemaContext;
import org.exolab.castor.xml.schema.ModelGroup;
import org.exolab.castor.xml.schema.RedefineSchema;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.ScopableResolver;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.util.AttributeSetImpl;

/**
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
**/
public class SchemaUnmarshaller extends ComponentReader {



    /**
     * W3C XML schema namespace.
     */
    public static final String XSD_NAMESPACE
        = "http://www.w3.org/2001/XMLSchema";


    /**
     * Unsupported namespace definitions, pointing to older
     * XML schema specifications.
     */
    public static final String[] UNSUPPORTED_NAMESPACES = {
        "http://www.w3.org/2000/10/XMLSchema",
        "http://www.w3.org/1999/XMLSchema"
    };

      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * Indicates whether the {@link Schema} processed represents an 
     * included schema.
     */
    private boolean _include = false;
    
    /**
     * The current {@link ComponentReader}.
    **/
    private ComponentReader _unmarshaller;

    /**
     * Flag to indicate that we are inside an annotation element.
    **/
    private int _annotationDepth = 0;

    /**
     * The current branch depth.
    **/
    private int _depth = 0;

    boolean skipAll = false;

    Schema _schema = null;

    private boolean foundSchemaDef = false;


    /**
     * The default namespace URI to be used.
     */
    private String _defaultNS = null;

    /**
     * The {@link SchemaUnmarshaller} state.
     */
    private SchemaUnmarshallerState _state = null;
   
    /**
     * Remapped prefix mappings.
     */
    private RemappedPrefixes _prefixMappings = null;
    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a {@link SchemaUnmarshaller} instance.
     * @param schemaContext A {@link SchemaContext} to be used during schema unmarshalling.
     * @throws XMLException Indicates that the XML schema cannnot be processed
     */
    public SchemaUnmarshaller(final SchemaContext schemaContext)
    throws XMLException {
        this(schemaContext, null, null);
        foundSchemaDef = false;
    } //-- SchemaUnmarshaller

    /**
     * Creates a {@link SchemaUnmarshaller} instance.
     * @param schemaContext A {@link SchemaContext} to be used during schema unmarshalling.
     * @param state A {@link SchemaUnmarshallerState} to be used during unmarshalling.
     * @throws XMLException Indicates that the XML schema cannnot be processed
     */
    public SchemaUnmarshaller(
            final SchemaContext schemaContext,
            final SchemaUnmarshallerState state)
    throws XMLException {
        this(schemaContext, null, null);
        _state = state;
        foundSchemaDef = false;
    } //-- SchemaUnmarshaller

    /**
     * Creates a {@link SchemaUnmarshaller} instance.
     * @param schemaContext A {@link SchemaContext} to be used during schema unmarshalling.
     * @param include Indicates whether the {@link Schema} to be processed ia an included schema.
     * @param state A {@link SchemaUnmarshallerState} to be used during unmarshalling.
     * @param uriResolver {@link URIResolver} to be used during processing.
     * @throws XMLException Signals a problem in processing the XML schema.
     * 
     * Called from IncludeUnmarshaller.
     * 
     * @see {@link IncludeUnmarshaller}
     */
    public SchemaUnmarshaller(
            final SchemaContext schemaContext,
            final boolean include,
            final SchemaUnmarshallerState state,
            final URIResolver uriResolver)
        throws XMLException {

        this(schemaContext, null, uriResolver);
        _state = state;
        _include = include;
        foundSchemaDef = false;
    }

    /**
     * Creates a {@link SchemaUnmarshaller} instance.
     * Exists for backward compatibility 
     * @param schemaContext A {@link SchemaContext} to be used during schema unmarshalling.
     * @param atts Attribute set to be processed.
     * @throws XMLException Signals a problem in processing the XML schema.
     */
    public SchemaUnmarshaller(
            final SchemaContext schemaContext,
            final AttributeSet atts) throws XMLException {
        this(schemaContext, atts, null);
    }

    /**
     * Creates a {@link SchemaUnmarshaller} instance.
     * @param schemaContext A {@link SchemaContext} to be used during schema unmarshalling.
     * @param atts Attribute set to be processed.
     * @param uriResolver {@link URIResolver} to be used during processing.
     * @throws XMLException Signals a problem in processing the XML schema.
     */
    private SchemaUnmarshaller(
            final SchemaContext schemaContext,
            final AttributeSet atts,
            final URIResolver uriResolver)
    throws XMLException {
        super(schemaContext);

        _schema = new Schema();
        //--initialize the schema to ensure that the default namespace
        //--is not set
        _schema.removeNamespace("");
        if (getResolver() == null) {
            setResolver(new ScopableResolver());
        }
        if (uriResolver == null) {
            setURIResolver(new URIResolverImpl());
        } else {
            setURIResolver(uriResolver);
        }
        foundSchemaDef = true;
        _state = new SchemaUnmarshallerState();
        init(atts);
    } //-- SchemaUnmarshaller

    /**
     * Returns the {@link Schema} instance representing the XML schema (file) just 
     * processed.
     * @return the {@link Schema} instance obtained from processing an XML schema file.
     */
    public Schema getSchema() {
        return _schema;
    }

    /**
     * Sets the {@link Schema} instance to be processed.
     * @param schema {@link Schema} instancetp be processed.
     */
    public void setSchema(final Schema schema) {
        _schema = schema;
    }

    /**
     * Returns the Object created by this ComponentReader.
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return getSchema();
    } //-- getObject

    /**
     * Returns the name of the element that this ComponentReader
     * handles.
     * @return the name of the element that this ComponentReader
     * handles
    **/
    public String elementName() {
        return SchemaNames.SCHEMA;
    } //-- elementName


    /**
     * Initializes the Schema object with the given attribute list.
     * @param atts the AttributeList for the schema
     * @throws XMLException Signals a problem in initializing the {@link Schema} 
     * object instance
     */
    private void init(final AttributeSet atts) throws XMLException {
        if (atts == null) {
            return;
        }

        String attValue = null;

        String nsURI = atts.getValue(SchemaNames.TARGET_NS_ATTR);
        if (nsURI != null &&  nsURI.length() == 0) {
            throw new SchemaException("empty string is not a legal namespace.");
        }
        if ((nsURI != null) && (nsURI.length() > 0)) {
            if (!_state.cacheIncludedSchemas) {
                //if we are including a schema we must take care
                //that the namespaces are the same
                if ((_include) && (!_schema.getTargetNamespace().equals(nsURI))) {
                    throw new SchemaException("The target namespace of the included " 
                            + "components must be the same as the target namespace " 
                            + "of the including schema");
                }
            }
               _schema.setTargetNamespace(nsURI);
        }

        _schema.setId(atts.getValue(SchemaNames.ID_ATTR));
        _schema.setVersion(atts.getValue(SchemaNames.VERSION_ATTR));

        //set the default locator of this schema
        if (!_include || _state.cacheIncludedSchemas) {
            _schema.setSchemaLocation(getDocumentLocator().getSystemId());
        }

        //-- attributeFormDefault
        String form = atts.getValue(SchemaNames.ATTR_FORM_DEFAULT_ATTR);
        if (form != null) {
            _schema.setAttributeFormDefault(Form.valueOf(form));
        }

        //-- elementFormDefault
        form = atts.getValue(SchemaNames.ELEM_FORM_DEFAULT_ATTR);
        if (form != null) {
            _schema.setElementFormDefault(Form.valueOf(form));
        }

        //-- @blockDefault
        attValue = atts.getValue(SchemaNames.BLOCK_DEFAULT_ATTR);
        if (attValue != null) {
            _schema.setBlockDefault(attValue);
        }

        //-- @finalDefault
        attValue = atts.getValue(SchemaNames.FINAL_DEFAULT_ATTR);
        if (attValue != null) {
            _schema.setFinalDefault(attValue);
        }

        //--@version
        attValue = atts.getValue(SchemaNames.VERSION_ATTR);
        if (attValue != null) {
            _schema.setVersion(attValue);
        }

    } //-- init

    /**
     * Handles namespace attributes.
     * @param namespaces The name space to handle.
     * @throws XMLException If there's a problem related to namespace handling.
     */
    private void handleNamespaces(final Namespaces namespaces) 
        throws XMLException {

        if (namespaces == null) {
            return;
        }

        Enumeration enumeration = namespaces.getLocalNamespaces();

        while (enumeration.hasMoreElements()) {

            String ns = (String) enumeration.nextElement();
            String[] prefixes = namespaces.getNamespacePrefixes(ns, true);

            if (prefixes.length == 0) {
                //-- this should never happen, but report error just
                //-- in case there is a bug in Namespaces class.
                String error = "unexpected error processing the following "
                    + "namespace: '" + ns + "'; the prefix could not be resolved.";
                throw new XMLException(error);
            }

            boolean hasCollisions = false;
            for (int pIdx = 0; pIdx < prefixes.length; pIdx++) {
                String prefix = prefixes[pIdx];
                
                //-- Since the Schema Object Model does not yet support
                //-- namespace scoping, we need to checking for namespace
                //-- prefix collisions...and remap the prefixes
                String tmpURI = _schema.getNamespace(prefix);
                if ((tmpURI != null) && (foundSchemaDef)) {
                    if (!tmpURI.equals(ns)) {
                        if (!hasCollisions) {
                            hasCollisions = true;
                            if (_prefixMappings == null) {
                                _prefixMappings = new RemappedPrefixes();
                            } else {
                                _prefixMappings = _prefixMappings.newRemappedPrefixes();
                            }
                        }
                        
                        //-- create a new prefix
                        if (prefix.length() == 0) {
                            prefix = "ns";
                        }
                            
                        int count = 1;
                        String newPrefix = prefix + count;
                        tmpURI = _schema.getNamespace(newPrefix);
                        while (tmpURI != null) {
                            if (tmpURI.equals(ns)) {
                                //-- no remapping necessary
                                break;
                            }
                            ++count;
                            newPrefix = prefix + count;
                            tmpURI = _schema.getNamespace(newPrefix);
                        }
                        _prefixMappings.addMapping(prefix, newPrefix);
                        prefix = newPrefix;
                    } else {
                        //-- we may need to "reset" a currently mapped prefix
                        if (_prefixMappings != null) {
                            if (_prefixMappings.isRemappedPrefix(prefix)) {
                                //-- reset mapping in this scope
                                _prefixMappings.addMapping(prefix, prefix);
                            }
                        }
                    }
                }
                //-- end collision handling
                
                if (prefix.length() == 0) {
                    _defaultNS = ns;
                    //register the default namespace with the empty string
                    _schema.addNamespace("", _defaultNS);
                } else {
                    //-- check for old unsupported schema namespaces
                    for (int nsIdx = 0; nsIdx < UNSUPPORTED_NAMESPACES.length; nsIdx++) {
                        if (ns.equals(UNSUPPORTED_NAMESPACES[nsIdx])) {
                            error("The following namespace \"" + ns 
                                    + "\" is no longer supported. Please update to " 
                                    + " the W3C XML Schema Recommendation.");
                        }
                    }
                    _schema.addNamespace(prefix, ns);
                }
            }
        }

    } //-- handleNamespaces

    /** 
     * Remaps any QName attributes for the given element and attributeSet.
     * This method is a work around for the lack of namespace scoping 
     * support in the Schema Object Model
     * 
     * @param name Name of the element handled.
     * @param namespace Namepace of the element processed.
     * @param atts The attributes of the element processed.
     */
    private void handleRemapping(final String name, final String namespace, 
            final AttributeSetImpl atts) {
        
        if (_prefixMappings == null) {
            return;
        }
        
        //-- increase depth for scoping
        _prefixMappings.depth++;
        
        String[] remapAtts = (String[]) RemappedPrefixes.QNAME_TABLE.get(name);
        
        if (remapAtts != null) {
            for (int i = 0; i < remapAtts.length; i++) {
                String value = atts.getValue(remapAtts[i]);
                if (value != null) {
                    value = _prefixMappings.remapQName(value);
                    atts.setAttribute(remapAtts[i], value);
                }
            }
        }
        
    } //-- handleRemapping

    /**
     * Signals the start of an element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element. This may be null.
     * Note: A null namespace is not the same as the default namespace unless
     * the default namespace is also null.
     * @param atts the AttributeSet containing the attributes associated
     * with the element.
     * @param nsDecls the namespace declarations being declared for this
     * element. This may be null.
     * @throws XMLException To indicate a problem in processing the current element.
    **/
    public void startElement(final String name, 
            String namespace, final AttributeSet atts,
            final Namespaces nsDecls) throws XMLException {

        if (skipAll) {
            return;
        }
        
        //-- DEBUG
        //System.out.println("#startElement: " + name + " {" + namespace + "}");
        //-- /DEBUG


        //-- process namespaces...unless we are inside an 
        //-- annotation
        if (_annotationDepth == 0) {
            handleNamespaces(nsDecls);
        }

        //-- backward compatibility, we'll need to
        //-- remove this at some point
        if ((!foundSchemaDef) && (namespace == null)) {
            if (_defaultNS == null) {
                _defaultNS = XSD_NAMESPACE;
                namespace = XSD_NAMESPACE;
                System.out.println("No namespace declaration has been " +
                    "found for " + name);
                System.out.print("   * assuming default namespace of ");
                System.out.println(XSD_NAMESPACE);
            }
        }
        if (namespace == null) {
            namespace = _defaultNS;
            //-- end of backward compatibility
        }

        //-- keep track of annotations
        if (name.equals(SchemaNames.ANNOTATION)) {
            ++_annotationDepth;
        }

        //-- check namespace
        if (!XSD_NAMESPACE.equals(namespace)) {
            if (_annotationDepth == 0) {
                error("'" + name + "' has not been declared in the XML "
                        + "Schema namespace.");
            }
        }

        //-- handle namespace prefix remapping
        if (_annotationDepth == 0) { 
            if (_prefixMappings != null) {
                handleRemapping(name, namespace, (AttributeSetImpl) atts);
            }
        }
        
        //-- Do delagation if necessary
        if (_unmarshaller != null) {
            try {
                _unmarshaller.startElement(name, namespace, atts, nsDecls);
            } catch (RuntimeException rtx) {
                error(rtx);
            }
            ++_depth;
            return;
        }

        if (name.equals(SchemaNames.SCHEMA)) {

            if (foundSchemaDef) {
                illegalElement(name);
            }

            foundSchemaDef = true;
            init(atts);
            return;
        }

        //-- <annotation>
        if (name.equals(SchemaNames.ANNOTATION)) {
            _unmarshaller = new AnnotationUnmarshaller(getSchemaContext(), atts);
        } else if (name.equals(SchemaNames.ATTRIBUTE)) {
            //--<attribute>
            _unmarshaller = new AttributeUnmarshaller(getSchemaContext(), _schema, atts);
        } else if (name.equals(SchemaNames.ATTRIBUTE_GROUP)) {
            //-- <attributeGroup>
            _unmarshaller = new AttributeGroupUnmarshaller(getSchemaContext(), _schema, atts);
        } else if (name.equals(SchemaNames.COMPLEX_TYPE)) {
            //-- <complexType>
            _unmarshaller
                = new ComplexTypeUnmarshaller(getSchemaContext(), _schema, atts);
        } else if (name.equals(SchemaNames.ELEMENT)) {
            //-- <element>
            _unmarshaller
                = new ElementUnmarshaller(getSchemaContext(), _schema, atts);
        } else if (name.equals(SchemaNames.SIMPLE_TYPE)) {
            //-- <simpleType>
            _unmarshaller = new SimpleTypeUnmarshaller(getSchemaContext(), _schema, atts);
        } else if (name.equals(SchemaNames.GROUP)) {
            //-- <group>
             _unmarshaller = new ModelGroupUnmarshaller(getSchemaContext(), _schema, atts);
        } else if (name.equals(SchemaNames.INCLUDE)) {
            //-- <include>
            _unmarshaller
                = new IncludeUnmarshaller(getSchemaContext(), _schema, atts, 
                        getURIResolver(), getDocumentLocator(), _state);
        } else if (name.equals(SchemaNames.IMPORT)) {
            //-- <import>
            _unmarshaller
                = new ImportUnmarshaller(getSchemaContext(), _schema, atts, 
                        getURIResolver(), getDocumentLocator(), _state);
        } else if (name.equals(SchemaNames.REDEFINE)) {
            //-- <redefine>
            _unmarshaller
            = new RedefineUnmarshaller(getSchemaContext(), _schema, atts, 
                    getURIResolver(), getDocumentLocator(), _state);
        } else {
            //-- we should throw a new Exception here
            //-- but since we don't support everything
            //-- yet, simply add an UnknownDef object
            System.out.print('<');
            System.out.print(name);
            System.out.print("> elements are either currently unsupported ");
            System.out.println("or non-valid schema elements.");
            _unmarshaller = new UnknownUnmarshaller(getSchemaContext(), name);
        }

//        unmarshaller.setDocumentLocator(getDocumentLocator());

    } //-- startElement

    /**
     * Signals to end of the element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element.
     * @throws XMLException To indicate that the current element cannnot be processed successfully.
    **/
    public void endElement(String name, String namespace)
        throws XMLException {
        if (skipAll) {
            return;
        }

        //-- DEBUG
        //System.out.println("#endElement: " + name + " {" + namespace + "}");
        //-- /DEBUG

        //-- backward compatibility
        if (namespace == null) {
            namespace =  _defaultNS;
        }

        //-- keep track of annotations
        if (name.equals(SchemaNames.ANNOTATION)) {
            --_annotationDepth;
        }
        
        //-- remove namespace remapping, if necessary
        if (_prefixMappings != null) {
            if (_prefixMappings.depth == 0) {
                _prefixMappings = _prefixMappings.getParent();
            } else {
                --_prefixMappings.depth;
            }
        }

        //-- Do delagation if necessary
        if ((_unmarshaller != null) && (_depth > 0)) {
            _unmarshaller.endElement(name, namespace);
            --_depth;
            return;
        }

        //-- use internal JVM String
        name = name.intern();

        if (name == SchemaNames.SCHEMA) {
            return;
        }

        //-- check for name mismatches
        if ((_unmarshaller != null)) {
            if (!name.equals(_unmarshaller.elementName())) {
                String err = "error: missing end element for ";
                err += _unmarshaller.elementName();
                throw new SchemaException(err);
            }
        } else {
            String err = "error: missing start element for " + name;
            throw new SchemaException(err);
        }

        //-- call unmarshaller.finish() to perform any necessary cleanup
        _unmarshaller.finish();

        //-- <annotation>
        if (name.equals(SchemaNames.ANNOTATION)) {
            _schema.addAnnotation((Annotation) _unmarshaller.getObject());
        } else if (name.equals(SchemaNames.ATTRIBUTE)) {
            //-- <attribute>
            _schema.addAttribute((AttributeDecl) _unmarshaller.getObject());
        } else if (name.equals(SchemaNames.ATTRIBUTE_GROUP)) {
            //-- <attributeGroup>
            Object obj = _unmarshaller.getObject();
            try {
                _schema.addAttributeGroup((AttributeGroupDecl) obj);
            } catch (ClassCastException ex) {
                String err = "Top-level AttributeGroups must be defining "
                    + "AttributeGroups and not referring AttributeGroups.";
                error(err);
            }
        } else if (name.equals(SchemaNames.COMPLEX_TYPE)) {
            //-- <complexType>
            ComplexType complexType = null;
            complexType = ((ComplexTypeUnmarshaller) _unmarshaller).getComplexType();
            _schema.addComplexType(complexType);
            if (complexType.getName() != null) {
                getResolver().addResolvable(complexType.getReferenceId(), complexType);
            } else {
                System.out.println("warning: top-level complexType with no name.");
            }
        } else if (name.equals(SchemaNames.SIMPLE_TYPE)) {
            //-- <simpleType>
            SimpleType simpleType = null;
            simpleType = ((SimpleTypeUnmarshaller) _unmarshaller).getSimpleType();
            _schema.addSimpleType(simpleType);
            getResolver().addResolvable(simpleType.getReferenceId(), simpleType);
        } else if (name.equals(SchemaNames.ELEMENT)) {
            //--<element>
            ElementDecl element = null;
            element = ((ElementUnmarshaller) _unmarshaller).getElement();
            _schema.addElementDecl(element);
        } else if (name.equals(SchemaNames.GROUP)) {
            //--<group>
            ModelGroup group = null;
            group = (((ModelGroupUnmarshaller) _unmarshaller).getGroup());
            _schema.addModelGroup(group);
        } else if (name.equals(SchemaNames.REDEFINE)) {
            //--<redefine>
            RedefineSchema redefine = null;
            redefine = (RedefineSchema) (((RedefineUnmarshaller) _unmarshaller).getObject());
            if ((redefine.getSchemaLocation() == null) && (redefine.hasRedefinition())) {
                _schema.removeRedefineSchema(redefine);
                String err = "A <redefine> structure with no 'schemaLocation' " 
                    + "attribute must contain only <annotation> elements";
                error(err);
            }
        }

        _unmarshaller = null;
    } //-- endElement

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.reader.ComponentReader#characters(char[], int, int)
     */
    public void characters(final char[] ch, final int start, final int length)
        throws XMLException {
        //-- Do delagation if necessary
        if (_unmarshaller != null) {
            _unmarshaller.characters(ch, start, length);
        }
    } //-- characters
    
    /**
     * This class handles remapping of namespace prefixes
     * for attributes of type QName. This is needed to 
     * work around a limitation in Castor's Schema Object 
     * Model, which does not support proper namespace
     * scoping yet.
     */
    static class RemappedPrefixes {
        
        public static final String RESOURCE_NAME 
            = "prefixremap.properties";
        
        public static final String RESOURCE_LOCATION =
            "/org/exolab/castor/xml/schema/reader/";
        
        public static final HashMap QNAME_TABLE = new HashMap();
        private static boolean initialized = false;
        
        static {
            
            synchronized (QNAME_TABLE) {
                
                if (!initialized) {
                    
                    initialized = true;
                    
                    //-- built in mappings
                    
                    //-- attribute                    
                    QNAME_TABLE.put(SchemaNames.ATTRIBUTE, new String [] {
                        SchemaNames.REF_ATTR, SchemaNames.TYPE_ATTR });
                        
                    //-- attributeGroup
                    QNAME_TABLE.put(SchemaNames.ATTRIBUTE_GROUP, new String [] {
                        SchemaNames.REF_ATTR });
                        
                    //-- element
                    QNAME_TABLE.put(SchemaNames.ELEMENT, new String [] {
                        SchemaNames.REF_ATTR, SchemaNames.TYPE_ATTR });
                        
                    //-- extension
                    QNAME_TABLE.put(SchemaNames.EXTENSION, new String [] {
                        SchemaNames.BASE_ATTR });

                    //-- group
                    QNAME_TABLE.put(SchemaNames.GROUP, new String [] {
                        SchemaNames.REF_ATTR });
                        
                    //-- restriction
                    QNAME_TABLE.put(SchemaNames.RESTRICTION, new String [] {
                        SchemaNames.BASE_ATTR });
                        
                    
                    //-- custom mappings
                    String filename = RESOURCE_LOCATION + RESOURCE_NAME;
                    InputStream is = SchemaUnmarshaller.class.getResourceAsStream(filename);
                    Properties props = new Properties();
                    if (is != null) {
                        try {
                            props.load(is);
                        } catch (java.io.IOException iox) {
                            //-- just use built-in mappings
                        }
                    }
                                        
                    Enumeration keys = props.propertyNames();
                    while (keys.hasMoreElements()) {
                        String name =  (String) keys.nextElement();
                        StringTokenizer st = new StringTokenizer(props.getProperty(name), ",");
                        String[] atts = new String[st.countTokens()];
                        int index = 0;
                        while (st.hasMoreTokens()) {
                            atts[index++] = st.nextToken();
                        }
                        QNAME_TABLE.put(name, atts);
                    }
                    
                }
            }
        }
        
        private HashMap _prefixes = null;
        
        private RemappedPrefixes _parent = null;
        
        int depth = 0;
        
        public boolean isRemappedPrefix(String prefix) {
            
            if (prefix == null) prefix = "";
            
            if (_prefixes != null) {
                if (_prefixes.get(prefix) != null) return true;
            }
            
            if (_parent != null) {
                return _parent.isRemappedPrefix(prefix);
            }
            return false;
        }
        
        public RemappedPrefixes getParent() {
            return _parent;
        }
        
        public String getPrefixMapping(final String oldPrefix) {
            
            if (_prefixes != null) {
                String newPrefix = (String)_prefixes.get(oldPrefix);
                if (newPrefix != null)
                    return newPrefix;
            }
            
            if (_parent != null) {
                return _parent.getPrefixMapping(oldPrefix);
            }
            
            return oldPrefix;
        }
        
        public RemappedPrefixes newRemappedPrefixes() {
            RemappedPrefixes rp = new RemappedPrefixes();
            rp._parent = this;
            return rp;
        }
        
        public void addMapping(final String oldPrefix, final String newPrefix) {
            if (_prefixes == null) {
                _prefixes = new HashMap();
            }
            _prefixes.put(oldPrefix, newPrefix);
        }

        public String remapQName(String value) {
            if (value == null) {
                return null;
            }
            
            //-- non-default namespace
            int idx = value.indexOf(':');
            String prefix = "";
            if (idx >= 0) {
                prefix = value.substring(0, idx);
            } else {
                idx = -1;            
            }
            String newPrefix = getPrefixMapping(prefix);
            if (!prefix.equals(newPrefix)) {
                if (newPrefix.length() == 0) {
                    value = value.substring(idx + 1);
                } else {
                    value = newPrefix + ":" + value.substring(idx + 1);
                }
            }
            
            return value;
        } //-- remapValue
        
    }
    
} //-- SchemaUnmarshaller

