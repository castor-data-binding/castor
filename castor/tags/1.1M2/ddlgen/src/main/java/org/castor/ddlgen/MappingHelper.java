/*
 * Copyright 2006 Le Duc Bao, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.ddlgen;

import java.util.Enumeration;
import java.util.Vector;

import org.castor.ddlgen.typeinfo.TypeInfo;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;

/** 
 * This class handles all common tasks for manipulating Mapping document.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class MappingHelper {
    //--------------------------------------------------------------------------

    /** Mapping document. */
    private Mapping _mapping;

    /** Type mapper. */
    private TypeMapper _typeMapper;

    //--------------------------------------------------------------------------

    /**
     * Get mapping document.
     * 
     * @return Mapping document.
     */
    public Mapping getMapping() {
        return _mapping;
    }

    /**
     * set mapping document.
     * 
     * @param mapping Mapping document.
     */
    public void setMapping(final Mapping mapping) {
        _mapping = mapping;
    }

    /**
     * Get type mapper.
     * 
     * @return Type mapper.
     */
    public TypeMapper getTypeMapper() {
        return _typeMapper;
    }

    /**
     * Set type mapper.
     * 
     * @param typeMapper Type mapper.
     */
    public void setTypeMapper(final TypeMapper typeMapper) {
        _typeMapper = typeMapper;
    }

    //--------------------------------------------------------------------------

    /**
     * Return the ClassMapping which associated with parameter name.
     * 
     * @param name Name of class to get ClassMapping of.
     * @return ClassMapping of the named class or <code>null</code> if no such
     *         ClassMapping was found.
     */
    public ClassMapping getClassMappingByName(final String name) {
        Enumeration ec = _mapping.getRoot().enumerateClassMapping();
        while (ec.hasMoreElements()) {
            ClassMapping cm = (ClassMapping) ec.nextElement();
            String cmName = cm.getName();
            if ((cmName != null) && cmName.equals(name)) { return cm; }
        }
        return null;
    }

    /**
     * Collect sql type of all identities for class with given name. It also takes care
     * on multiple column identities and extended classes.
     * 
     * <pre>
     * &lt;mapping&gt;
     *   &lt;class name=&quot;myapp.OtherProductGroup&quot; &gt;
     *     &lt;map-to table=&quot;other_prod_group&quot; xml=&quot;group&quot; /&gt;
     *     &lt;field name=&quot;id&quot; type=&quot;integer&quot; identity="true"&gt;
     *       &lt;sql name=&quot;id&quot; type=&quot;integer&quot;/&gt;
     *     &lt;/field&gt;
     *   &lt;/class&gt;
     * 
     *   &lt;class name=&quot;myapp.ProductGroup&quot; identity=&quot;id&quot;&gt;
     *     &lt;map-to table=&quot;prod_group&quot; xml=&quot;group&quot; /&gt;
     *     &lt;field name=&quot;id&quot; type=&quot;myapp.OtherProductGroup&quot; &gt;
     *       &lt;sql name=&quot;prod_id&quot; /&gt;
     *     &lt;/field&gt;
     *   &lt;/class&gt;
     * 
     *   &lt;class name=&quot;myapp.Product&quot; identity=&quot;id&quot;&gt;
     *     &lt;field name=&quot;group&quot; type=&quot;myapp.ProductGroup&quot;&gt;
     *       &lt;sql name=&quot;group_id&quot; /&gt;
     *     &lt;/field&gt;
     *   &lt;/class&gt;
     * &lt;/mapping&gt;     
     * </pre>
     * 
     * @param className Name of class to get type of identities of.
     * @return Array of sql types of all identities.
     * @throws GeneratorException If failed to resolve sql type of identities.
     */
    public synchronized String[] resolveTypeReferenceForIds(final String className)
    throws GeneratorException {
        ClassMapping cm = getClassMappingByName(className);
        if (cm != null) { return resolveTypeReferenceForIds(cm); }
        throw new GeneratorException("can not find class " + className);
    }

    /**
     * Collect sql type of all identities for a ClassMapping. It also takes care on
     * multiple column identities and extended classes.
     * 
     * @param cm ClassMapping to get type of identities of.
     * @return Array of sql types of all identities.
     * @throws GeneratorException If failed to resolve sql type of identities.
     */
    public String[] resolveTypeReferenceForIds(final ClassMapping cm)
    throws GeneratorException {
        boolean isFoundKey = false;
        
        String[] ids = cm.getIdentity();
        Vector types = new Vector();

        Enumeration ef = cm.getClassChoice().enumerateFieldMapping();
        boolean isExistFieldId = isUseFieldIdentity(cm);

        // Go through all fields.
        while (ef.hasMoreElements()) {
            FieldMapping fm = (FieldMapping) ef.nextElement();
            
            // Identity are defined at field.
            if (isExistFieldId && fm.getIdentity()) {
                // Get field type.
                TypeInfo typeinfo = null;
                String sqltype = fm.getSql().getType();

                if (sqltype != null) {
                    typeinfo = _typeMapper.getType(sqltype);
                }

                if (typeinfo == null) {
                    String[] refRefType = resolveTypeReferenceForIds(fm
                            .getType());
                    for (int l = 0; l < refRefType.length; l++) {
                        types.add(refRefType[l]);
                        isFoundKey = true;
                    }
                } else {
                    for (int i = 0; i < fm.getSql().getNameCount(); i++) {
                        types.add(fm.getSql().getType());
                        isFoundKey = true;
                    }
                }
            } else if (!isExistFieldId) {
                // Identities are defined at class tag.
                String fieldName = fm.getName();
                for (int j = 0; j < ids.length; j++) {
                    // If sqlnames[i] equals ids[j] we found a reference type.
                    if (fieldName.equals(ids[j])) {

                        // Check for type if this table is a reference table.
                        TypeInfo typeinfo = null;
                        String sqltype = fm.getSql().getType();

                        // Verify if sqltype exists.
                        if (sqltype != null) {
                            typeinfo = _typeMapper.getType(sqltype);
                        }

                        if (typeinfo == null) {
                            ClassMapping cmRef = getClassMappingByName(fm.getType());
                            // If cmRef is null, the reference class could not be found
                            // so we need to use field type.
                            if (cmRef == null) {                        
                                typeinfo = _typeMapper.getType(fm.getType());
                                
                                if (typeinfo == null) {
                                    throw new TypeNotFoundException(
                                            "Can't resolve type " + fm.getType());
                                }
                                int count = fm.getSql().getNameCount();
                                if (count == 0) { count = fm.getSql().getManyKeyCount(); }
                                
                                for (int l = 0; l < count; l++) {
                                    types.add(fm.getType());
                                    isFoundKey = true;
                                }
                                
                            } else {
                                // Resolve type for reference class.
                                String[] refRefType = resolveTypeReferenceForIds(
                                        fm.getType());
                                for (int l = 0; l < refRefType.length; l++) {
                                    types.add(refRefType[l]);
                                    isFoundKey = true;
                                }
                            }
                        } else {
                            types.add(fm.getSql().getType());
                            isFoundKey = true;
                        }
                    }
                }
            }
        }

        // If there is no identity found, looking in the extend class.
        if (!isFoundKey && (cm.getExtends() != null)) {
           ClassMapping extendClass = (ClassMapping) cm.getExtends(); 
           String[] refRefType = resolveTypeReferenceForIds(extendClass);
           for (int l = 0; l < refRefType.length; l++) {
               types.add(refRefType[l]);
           }               
        }

        return (String[]) types.toArray(new String[types.size()]);
    }

    /**
     * Check if identities of given ClassMapping are defined at its FieldMappings.
     * 
     * @param cm ClassMapping to check for identity definitions at FieldMapping.
     * @return <code>true</code> if identities are defined at fieldMapping.
     */
    public boolean isUseFieldIdentity(final ClassMapping cm) {
        Enumeration ef = cm.getClassChoice().enumerateFieldMapping();

        while (ef.hasMoreElements()) {
            FieldMapping fm = (FieldMapping) ef.nextElement();
            if (fm.getIdentity()) { return true; }
        }
        return false;
    }

    /**
     * Check if given FieldMapping is an identity at given ClassMapping.
     * <pre>
     * &lt;class name=&quot;myapp.ProductGroup&quot; identity=&quot;id&quot;&gt;
     *     &lt;field name=&quot;id&quot; type=&quot;integer&quot; &gt;
     *         &lt;sql name=&quot;id1 id2&quot; type=&quot;integer&quot;/&gt;
     *     &lt;/field&gt;
     * &lt;/class&gt;
     * </pre>
     * 
     * @param cm ClassMapping.
     * @param fm FieldMapping.
     * @return <code>true</code> if FieldMapping is an identity at ClassMapping.
     */
    public boolean isIdentity(final ClassMapping cm, final FieldMapping fm) {
        String[] ids = cm.getIdentity();
        String fieldName = fm.getName();

        for (int j = 0; j < ids.length; j++) {
            if (ids[j].equalsIgnoreCase(fieldName)) { return true; }
        }
        return false;
    }

    /**
     * The identity definitions at class and field are alternative syntax. If
     * both are specified the one at field should take precedence over the class
     * one. In other words if both are specified the one at class will be
     * ignored.
     * 
     * @param cm ClassMapping to get sql names of identities of.
     * @param ext Recursivly search for identities in extended ClassMappings.
     * @return Array of sql names of identities of given ClassMapping.
     */
    public String[] getClassMappingSqlIdentity(final ClassMapping cm, final boolean ext) {
        Vector ids = new Vector();
        
        String[] identities = cm.getIdentity();
        // If child defines identity with same or no type,
        // use name from child and type from parent.
        if (ext && cm.getExtends() != null && identities.length == 0) {
            identities = getClassMappingIdentity((ClassMapping) cm.getExtends());
        }

        Enumeration ef = cm.getClassChoice().enumerateFieldMapping();
        boolean isExistFieldId = isUseFieldIdentity(cm);
        while (ef.hasMoreElements()) {
            FieldMapping fm = (FieldMapping) ef.nextElement();
            // Add all sql columns into identity list.
            if (isExistFieldId && fm.getIdentity()) {
                isExistFieldId = true;
                int ncount = fm.getSql().getNameCount();
                for (int i = 0; i < ncount; i++) {
                    ids.add(fm.getSql().getName(i));
                }
            } else if (!isExistFieldId) {
                // If using class identity, find out all correspondent column names.
                String fieldName = fm.getName();
                for (int j = 0; j < identities.length; j++) {
                    if (fieldName.equals(identities[j])) {
                        // Check for type if this table is a reference table.
                        int ncount = fm.getSql().getNameCount();
                        for (int i = 0; i < ncount; i++) {
                            ids.add(fm.getSql().getName(i));
                        }
                    }
                }               
            }
        }

        // Get identities from parent.
        if (ext && (cm.getExtends() != null) && (ids.size() == 0)) {
            return getClassMappingSqlIdentity((ClassMapping) cm.getExtends(), ext);
        }

        return (String[]) ids.toArray(new String[ids.size()]);
    }

    /**
     * The identity definitions at class and field are alternative syntax. If
     * both are specified the one at field should take precedence over the class
     * one. In other words if both are specified the one at class will be
     * ignored.
     * 
     * @param cm ClassMapping to get identity names of.
     * @return Array of identity names of given ClassMapping.
     */
    public String[] getClassMappingIdentity(final ClassMapping cm) {
        Vector ids = new Vector();
        
        boolean isExistFieldId = false;
        Enumeration ef = cm.getClassChoice().enumerateFieldMapping();
        while (ef.hasMoreElements()) {
            FieldMapping fm = (FieldMapping) ef.nextElement();
            // Add names of all identity columns to list.
            if (isExistFieldId && fm.getIdentity()) {
                isExistFieldId = true;
                ids.add(fm.getName());
            }
        }
        
        if (!isExistFieldId) {
            String[] identities = cm.getIdentity();
            for (int i = 0; i < identities.length; i++) {
                ids.add(identities[i]);
            }
        }

        // If this is a child class, use its parents identities.
        if ((cm.getExtends() != null) && (ids.size() == 0)) {
            return getClassMappingIdentity((ClassMapping) cm.getExtends()); 
        }

        return (String[]) ids.toArray(new String[ids.size()]);
    }

    //--------------------------------------------------------------------------
}
