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
 * @version $Revision$ $Date: 2006-04-27 05:48:18 -0600 (Thu, 27 Apr 2006) $
**/
public class CaseDescriptor extends BaseHarnessDescriptor {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    public CaseDescriptor() {
        _xmlName = "Case";
        _nsPrefix = "jtf";
        XMLFieldDescriptorImpl desc = null;
        //-- initialize attribute descriptors
        
        _attributeDescriptors = new XMLFieldDescriptorImpl[1];

        //-- _class
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_className", "class", NodeType.Attribute);
        desc.setImmutable(true);
        desc.setHandler( new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                Case target = (Case) object;
                return target.getClassName();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Case target = (Case) object;
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
        _attributeDescriptors[0] = desc;

        //-- initialize element descriptors
        
        _elementDescriptors = new XMLFieldDescriptorImpl[0];
                
    }


    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.mapping.ClassDescriptor#getJavaClass()
     */
    public java.lang.Class getJavaClass() {
        return Case.class;
    } //-- java.lang.Class getJavaClass() 
    

}
