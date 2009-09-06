package org.exolab.castor.xml.schema;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.AbstractProperties;
import org.castor.xml.XMLProperties;
import org.exolab.castor.xml.Serializer;
import org.exolab.castor.xml.util.XMLParserUtils;
import org.xml.sax.Parser;

/**
 * Represents an execution context for schema reading/writing activities.
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision: 7273 $ $Date: 2004-10-05 14:27:10 -0600 (Tue, 05 Oct 2004) $
 *
 * @since 1.2
 */
public class SchemaContextImpl implements SchemaContext {
    
    /**
     * {@link Log} instance used for logging.
     */
    private final Log LOG = LogFactory.getLog(SchemaContextImpl.class);

    private Resolver _schemaResolver;
    
    /**
     * {@link AbstractProperties} instance for retrieving property values related  to XML parsers
     */
    private AbstractProperties _properties;
    
    /**
     * Creates an instance of {@link SchemaContextImpl}.
     */
    public SchemaContextImpl() {
        super();
        _properties = XMLProperties.newInstance();
    }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.xml.schema.SchemaContext#getSchemaResolver()
     */
    public Resolver getSchemaResolver() {
        return _schemaResolver;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.xml.InternalContext#getParser(java.lang.String)
     */
    public Parser getParser(final String features) {
        return XMLParserUtils.getParser(_properties, features);
    }


    /**
     * {@inheritDoc}
     * @see org.exolab.castor.xml.schema.SchemaContext#setSchemaResolver(org.exolab.castor.xml.schema.Resolver)
     */
    public void setSchemaResolver(Resolver resolver) {
        _schemaResolver = resolver;

    }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.xml.schema.SchemaContext#getParser()
     */
    public Parser getParser() {
        return getParser(null);
    }

    /**
     *{@inheritDoc}
     * @see org.castor.xml.InternalContext#getSerializer()
     */
    public Serializer getSerializer() {
        return XMLParserUtils.getSerializer(_properties);
    }

}
