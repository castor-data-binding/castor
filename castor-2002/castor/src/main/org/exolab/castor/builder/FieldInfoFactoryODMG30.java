package org.exolab.castor.builder;


import org.exolab.castor.builder.*;
import org.exolab.castor.builder.types.*;


/**
 * This class is used as a factory to create all the FieldInfo objects used by the 
 * source generator. You may override the FieldInfo classes and this factory for 
 * specific adaptions.
 * @author <a href="mailto:frank.thelen@poet.de">Frank Thelen</a>
 * @version $Revision$ $Date$
**/
public class FieldInfoFactoryODMG30 extends FieldInfoFactory {
    
    /**
     * Creates a new FieldInfoFactoryODMG30
    **/
    public FieldInfoFactoryODMG30 () {
    } //-- FieldInfoFactoryODMG30
    
    public CollectionInfo createCollection (XSType contentType, String name, String elementName) {
        return new CollectionInfoODMG30(contentType,name,elementName);
    }

} //-- FieldInfoFactoryODMG30
