/*
 * This class was automatically generated with 
 * <a href="http://castor.org">Castor 0.8 (20000324)</a>,
 * using an XML Schema.
 * $Id
 */

package harness;


  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLFieldHandler;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;

/**
 * 
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
**/
public class CategoryDescriptor extends BaseHarnessDescriptor {


      //--------------------/
     //- Member Variables -/
    //--------------------/

      //----------------/
     //- Constructors -/
    //----------------/

    public CategoryDescriptor() {
        _xmlName = "category";
        _nsPrefix = "jtf";
        XMLFieldDescriptorImpl desc = null;
        XMLFieldHandler handler = null;
        //-- initialize attribute descriptors
        
        _attributeDescriptors = new XMLFieldDescriptorImpl[2];
        //-- _name
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_name", "name", NodeType.Attribute);
        desc.setImmutable(true);
        desc.setHandler( new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                Category target = (Category) object;
                return target.getName();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Category target = (Category) object;
                    target.setName( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        _attributeDescriptors[0] = desc;
        
        //-- _name
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_className", "class", NodeType.Attribute);
        desc.setImmutable(true);
        desc.setHandler( new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                Category target = (Category) object;
                return target.getClassName();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Category target = (Category) object;
                    target.setClassName( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        _attributeDescriptors[1] = desc;

        //-- initialize element descriptors
        
        _elementDescriptors = new XMLFieldDescriptorImpl[3];
        //-- _description
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_description", "description", NodeType.Element);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                Category target = (Category) object;
                return target.getDescription();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Category target = (Category) object;
                    target.setDescription( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        _elementDescriptors[0] = desc;
                
        //-- _case
        desc = new XMLFieldDescriptorImpl(Case.class, "_case", "case", NodeType.Element);
        desc.setImmutable(false);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                Category target = (Category) object;
                return target.getCase();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Category target = (Category) object;
                    target.addCase( (Case) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(true);
        _elementDescriptors[1] = desc;

        //-- _object
        desc = (new XMLFieldDescriptorImpl(Object.class, "_object", (String) null, NodeType.Element) {
            public boolean matches( String xmlName )
            {
                return true;
            }
        } );
        desc.setImmutable(false);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                Category target = (Category) object;
                return target.getObject();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Category target = (Category) object;
                    target.setObject( value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        _elementDescriptors[2] = desc;

    } //-- org.exolab.castor.mapping.xml.CategoryDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

//    /**
//     * Returns the XML field descriptor matching the given
//     * xml name and nodeType. If NodeType is null, then
//     * either an AttributeDescriptor, or ElementDescriptor
//     * may be returned. Null is returned if no matching
//     * descriptor is available.
//     *
//     * @param name the xml name to match against
//     * @param nodeType, the NodeType to match against, or null if
//     * the node type is not known.
//     * @return the matching descriptor, or null if no matching
//     * descriptor is available.
//     *
//    **/
//    public XMLFieldDescriptor getFieldDescriptor
//        (final String name, final String namespace, final NodeType nodeType) 
//    {
//        
//        
//        boolean wild = (nodeType == null);
//        
//        if (wild || (nodeType == NodeType.Element)) {
//            XMLFieldDescriptor desc = null;
//            for (int i = 0; i < _elementDescriptors.length; i++) {
//                desc = _elementDescriptors[i];
//                if (desc == null) {
//                    continue;
//                }
//                if (desc.matches(name)) {
//                    return desc;
//                }
//            }
//        }
//        
//        if (wild || (nodeType == NodeType.Attribute)) {
//            XMLFieldDescriptor desc = null;
//            for (int i = 0; i < _attributeDescriptors.length; i++) {
//                desc = _attributeDescriptors[i];
//                if (desc == null)
//                    continue;
//                if (desc.matches(name))
//                    return desc;
//            }
//        }
//        
//        return null;
//        
//    } //-- getFieldDescriptor
    
    /**
    **/
    public java.lang.Class getJavaClass() {
        return Category.class;
    } //-- java.lang.Class getJavaClass() 

}
