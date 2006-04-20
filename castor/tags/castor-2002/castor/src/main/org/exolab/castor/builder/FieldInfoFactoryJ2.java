package org.exolab.castor.builder;


import org.exolab.castor.builder.*;
import org.exolab.castor.builder.types.*;


/**
 * This class is used as a factory to create all the FieldInfo objects used by the 
 * source generator. You may override the FieldInfo classes and this factory for 
 * specific adaptions.
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
**/
public class FieldInfoFactoryJ2 extends FieldInfoFactory {
    
    /**
     * Creates a new FieldInfoFactoryJ2
    **/
    public FieldInfoFactoryJ2 () {
    } //-- FieldInfoFactoryJ2
    
    public CollectionInfo createCollection (XSType contentType, String name, String elementName) {
        return new CollectionInfoJ2(contentType,name,elementName);
    }

} //-- FieldInfoFactoryJ2
