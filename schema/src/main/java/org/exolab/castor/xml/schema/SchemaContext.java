package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.Serializer;
import org.xml.sax.Parser;

/**
 * Represents an execution context for schema reading/writing activities.
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision: 7273 $ $Date: 2004-10-05 14:27:10 -0600 (Tue, 05 Oct 2004) $
 *
 * @since 1.2
 */
public interface SchemaContext {

    /**
     * To get the {@link Resolver} to use in Schema*.
     * @return get the {@link Resolver} to use in Schema*
     */
    Resolver getSchemaResolver();

    /**
     * To set the {@link Resolver} for Schema*. 
     * @param schemaResolver the {@link Resolver} for Schema*
     */
    void setSchemaResolver(Resolver resolver);

    /**
     * Return an XML document parser as specified in the configuration file.
     *
     * @return A suitable XML parser
     */
    // TODO REFACTOR with InternalContext
    Parser getParser();

    /**
     * Returns a default serializer for producing an XML document. The caller
     * can specify an alternative output format, may reuse this serializer
     * across several streams, and may serialize both DOM and SAX events. If
     * such control is not required, it is recommended to call one of the other
     * two methods.
     * 
     * @return A suitable serializer
     */
    // TODO REFACTOR with InternalContext
    Serializer getSerializer();

}
