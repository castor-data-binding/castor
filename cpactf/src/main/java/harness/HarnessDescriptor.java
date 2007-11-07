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
 * @version $Revision$ $Date: 2005-10-16 07:17:11 -0600 (Sun, 16 Oct 2005) $
**/
public class HarnessDescriptor extends BaseHarnessDescriptor {


      //----------------/
     //- Constructors -/
    //----------------/

    public HarnessDescriptor() {
        _xmlName = "harness";
        _nsPrefix = "jtf";
        XMLFieldDescriptorImpl desc = null;
        XMLFieldHandler handler = null;
        //-- initialize attribute descriptors
        
        _attributeDescriptors = new XMLFieldDescriptorImpl[1];
        //-- _name
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_name", "name", NodeType.Attribute);
        desc.setImmutable(true);
        desc.setHandler( new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                Harness target = (Harness) object;
                return target.getName();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Harness target = (Harness) object;
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
        
        //-- initialize element descriptors
        
        _elementDescriptors = new XMLFieldDescriptorImpl[2];
        //-- _description
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_description", "description", NodeType.Element);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                Harness target = (Harness) object;
                return target.getDescription();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Harness target = (Harness) object;
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
        
        //-- _category
        desc = new XMLFieldDescriptorImpl(Category.class, "_category", "category", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                Harness target = (Harness) object;
                return target.getCategory();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Harness target = (Harness) object;
                    target.setCategory( (Category) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new Category();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(true);
        _elementDescriptors[1] = desc;
        
    }


    /**
    **/
    public java.lang.Class getJavaClass() {
        return Harness.class;
    } //-- java.lang.Class getJavaClass() 

 
}
