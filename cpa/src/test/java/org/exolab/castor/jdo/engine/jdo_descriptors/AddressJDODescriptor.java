/**
 * 
 */
package org.exolab.castor.jdo.engine.jdo_descriptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.engine.SQLTypeInfos;
import org.exolab.castor.jdo.engine.Address;
import org.exolab.castor.jdo.engine.JDOFieldDescriptor;
import org.exolab.castor.jdo.engine.JDOFieldDescriptorImpl;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.xml.ClassChoice;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.MapTo;
import org.exolab.castor.mapping.xml.Sql;
import org.exolab.castor.mapping.xml.types.ClassMappingAccessType;


/**
 * JDO descriptor class for Address type. Describes persistence information for
 * entity Address including Java fields and SQL columns.
 * 
 * @author Tobias Hochwallner, Lukas Lang
 * 
 */
public class AddressJDODescriptor extends ClassDescriptorImpl {

    /**
     * Logger.
     */
    private static final Log LOG = LogFactory
            .getLog(AddressJDODescriptor.class);

    /**
     * Default Constructor. Configures persistence of entity Book.
     */
    public AddressJDODescriptor() {
        super();
        addNature(ClassDescriptorJDONature.class.getName());
        ClassMapping mapping = new ClassMapping();
        ClassChoice choice = new ClassChoice();
        MapTo mapTo = new MapTo();

        LOG.debug("Constructor invoked");

        // Set DB table name
        new ClassDescriptorJDONature(this).setTableName("address");
        // Set corresponding Java class
        setJavaClass(Address.class);
        // Set access mode
        new ClassDescriptorJDONature(this).setAccessMode(AccessMode.Shared);
        // Set cache key
        new ClassDescriptorJDONature(this).addCacheParam("name", "org.castor.cpa.functional.onetoone.Address");

        // Configure class mapping
        mapping.setAccess(ClassMappingAccessType.SHARED);
        mapping.setAutoComplete(true);
        mapping.setName("org.castor.cpa.functional.onetoone.Address");
        // Set class choice
        mapping.setClassChoice(choice);
        // Set table mapping
        mapping.setMapTo(mapTo);
        // Set table
        mapTo.setTable("address");
        // Set mapping
        setMapping(mapping);

        JDOFieldDescriptor idFieldDescr = initId(choice);

        // Set fields
        setFields(new FieldDescriptor[] {});
        // Set identity
        setIdentities((new FieldDescriptor[] { idFieldDescr }));

        LOG.debug("Instantiation finished");
    }

    /**
     * @param choice
     * @return jdo field descriptor for id
     */
    private JDOFieldDescriptor initId(final ClassChoice choice) {
        String idFieldName = "id";
        JDOFieldDescriptorImpl idFieldDescr;
        FieldMapping idFM = new FieldMapping();
        TypeInfo idType = new TypeInfo(java.lang.Integer.class);
        // Set columns required (=not null)
        idType.setRequired(true);
        
        FieldHandler idHandler;
        try {
            idHandler = new FieldHandlerImpl(idFieldName, null, null,
                    Address.class.getMethod("getId", null), Address.class
                            .getMethod("setId", new Class[] { int.class }),
                    idType);
        } catch (SecurityException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        } catch (MappingException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }

        // Instantiate title field descriptor
        idFieldDescr = new JDOFieldDescriptorImpl(idFieldName, idType,
                idHandler, false, new String[] { idFieldName },
                new int[] { SQLTypeInfos
                        .javaType2sqlTypeNum(java.lang.Integer.class) },
                null, new String[] {}, false, false);

        // Set parent class descriptor
        idFieldDescr.setContainingClassDescriptor(this);
        idFieldDescr.setClassDescriptor(this);
        idFieldDescr.setIdentity(true);
        
        idFM.setIdentity(true);
        idFM.setDirect(false);
        idFM.setName("id");
        idFM.setRequired(true);
        idFM.setSetMethod("setId");
        idFM.setGetMethod("getId");
        idFM.setType("integer");

        Sql idSql = new Sql();
        idSql.addName("id");
        idSql.setType("integer");

        idFM.setSql(idSql);

        // Add field mappings
        choice.addFieldMapping(idFM);
        return idFieldDescr;
    }
}
