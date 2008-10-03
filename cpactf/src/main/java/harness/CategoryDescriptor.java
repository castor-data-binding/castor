/*
 * This class was automatically generated with 
 * <a href="http://castor.org">Castor 0.8 (20000324)</a>,
 * using an XML Schema.
 * $Id
 */

package harness;

import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLFieldHandler;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;

/**
 * 
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 */
public class CategoryDescriptor extends BaseHarnessDescriptor {
    public CategoryDescriptor() {
        _xmlName = "category";
        _nsPrefix = "jtf";
        XMLFieldDescriptorImpl desc = null;
        XMLFieldHandler handler = null;
        //-- initialize attribute descriptors
        
        _attributeDescriptors = new XMLFieldDescriptorImpl[2];
        //-- _name
        desc = new XMLFieldDescriptorImpl(String.class, "_name", "name", NodeType.Attribute);
        desc.setImmutable(true);
        desc.setHandler(new XMLFieldHandler() {
            public Object getValue(final Object object) throws IllegalStateException {
                Category target = (Category) object;
                return target.getName();
            }
            public void setValue(final Object object, final Object value)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Category target = (Category) object;
                    target.setName((String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance(final Object parent) {
                return null;
            }
        });
        _attributeDescriptors[0] = desc;
        
        //-- _name
        desc = new XMLFieldDescriptorImpl(String.class, "_className", "class", NodeType.Attribute);
        desc.setImmutable(true);
        desc.setHandler(new XMLFieldHandler() {
            public Object getValue(final Object object) throws IllegalStateException {
                Category target = (Category) object;
                return target.getClassName();
            }
            public void setValue(final Object object, final Object value)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Category target = (Category) object;
                    target.setClassName((String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance(final Object parent) {
                return null;
            }
        });
        _attributeDescriptors[1] = desc;

        //-- initialize element descriptors
        
        _elementDescriptors = new XMLFieldDescriptorImpl[3];
        //-- _description
        desc = new XMLFieldDescriptorImpl(
                String.class, "_description", "description", NodeType.Element);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public Object getValue(final Object object) throws IllegalStateException {
                Category target = (Category) object;
                return target.getDescription();
            }
            public void setValue(final Object object, final Object value)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Category target = (Category) object;
                    target.setDescription((String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance(final Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setMultivalued(false);
        _elementDescriptors[0] = desc;
                
        //-- _case
        desc = new XMLFieldDescriptorImpl(Case.class, "_case", "case", NodeType.Element);
        desc.setImmutable(false);
        handler = (new XMLFieldHandler() {
            public Object getValue(final Object object) throws IllegalStateException {
                Category target = (Category) object;
                return target.getCase();
            }
            public void setValue(final Object object, final Object value)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Category target = (Category) object;
                    target.addCase((Case) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance(final Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setMultivalued(true);
        _elementDescriptors[1] = desc;

        //-- _object
        desc = (new XMLFieldDescriptorImpl(
                Object.class, "_object", (String) null, NodeType.Element) {
            public boolean matches(final String xmlName) {
                return true;
            }
        });
        desc.setImmutable(false);
        handler = (new XMLFieldHandler() {
            public Object getValue(final Object object) throws IllegalStateException {
                Category target = (Category) object;
                return target.getObject();
            }
            public void setValue(final Object object, final Object value)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Category target = (Category) object;
                    target.setObject(value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance(final Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setMultivalued(false);
        _elementDescriptors[2] = desc;
    }

    public Class < ? > getJavaClass() {
        return Category.class;
    } 
}
