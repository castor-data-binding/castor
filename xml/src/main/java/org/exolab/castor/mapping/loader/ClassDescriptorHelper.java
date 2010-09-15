package org.exolab.castor.mapping.loader;

import java.util.ArrayList;
import java.util.Arrays;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;

public class ClassDescriptorHelper {

    /**
     * Get all the {@link FieldDescriptor}s for non-identity fields, including
     * all the fields in base classes.
     * 
     * @param classDescriptor
     *            ClassMapping instance.
     * @return An array.
     * @throws MappingException
     */
    public static FieldDescriptor[] getFullFields(final ClassDescriptor classDescriptor)
            throws MappingException {
        ClassDescriptor baseClassDescriptor = classDescriptor.getExtends();
        ArrayList<FieldDescriptor> fullFields = new ArrayList<FieldDescriptor>();

        if (baseClassDescriptor != null) {
            ClassDescriptor origin = baseClassDescriptor;
            
            while (origin.getExtends() != null) {
                origin = origin.getExtends();
            }
            
            // recursive call to obtain full fields for base class.
            FieldDescriptor[] fullBaseFieldDescriptors = getFullFields(baseClassDescriptor);
            FieldDescriptor[] currentFields = classDescriptor.getFields();

            // add all base field descriptors
            for (FieldDescriptor baseFieldDescriptor : fullBaseFieldDescriptors) {
                fullFields.add(baseFieldDescriptor);
            }
            // add all fields of the current class
            for (FieldDescriptor currentFieldDescriptor : currentFields) {
                fullFields.add(currentFieldDescriptor);
            }
        } else {
            FieldDescriptor[] fieldDescriptors = ((ClassDescriptorImpl) classDescriptor).getFields();
            fullFields.addAll(Arrays.asList(fieldDescriptors));
        }
        
        return fullFields.toArray(new FieldDescriptor[fullFields.size()]);
    }

    
    /**
     * Get the all the id fields of a class
     * If the class, C, is a dependent class, then
     * the depended class', D, id fields will be
     * appended at the back and returned.
     * If the class is an extended class, the id
     * fields of the extended class will be returned.
     */
    public static FieldDescriptor[] getIdFields(final ClassDescriptor classDescriptor)
            throws MappingException {

        // start with the extended class
        ClassDescriptor base = classDescriptor;
        while (base.getExtends() != null) {
            base = base.getExtends();
        }
        
//        fmDepended = null;

        FieldDescriptor[] identities = ((ClassDescriptorImpl) base).getIdentities();
        
        if (identities == null || identities.length == 0) {
            throw new MappingException("Identity is null!");
        }

//        //INBESTIGATE[WG]: what's the use fo this code
//        fmIds = new FieldMapping[identities.length];
//        fmBase = base.getClassChoice().getFieldMapping();
//        for (int i = 0; i < fmBase.length; i++) {
//            for (int k = 0; k < identities.length; k++) {
//                if (fmBase[i].getName().equals(identities[k])) {
//                    fmIds[k] = fmBase[i];
//                    break;
//                }
//            }
//        }
//        if (fmDepended == null) {
            return identities;
//        }

        //TODO[INVESTIGATE]: look at this dead code
//        // join depend ids and class id
//        fmResult = new FieldMapping[fmDepended.length + identities.length];
//        System.arraycopy(fmIds, 0, fmResult, 0, fmIds.length);
//        System.arraycopy(fmDepended, 0, fmResult, fmIds.length,
//                fmDepended.length);
//        return fmIds;
    }

}


