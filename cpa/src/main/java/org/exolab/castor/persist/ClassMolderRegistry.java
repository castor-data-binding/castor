/*
 * Copyright 2011 Ralf Joachim, Wensheng Dou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: TableInfo.java 8469 2009-12-28 16:47:54Z rjoachim $
 */
package org.exolab.castor.persist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.castor.cpa.util.JDOClassDescriptorResolver;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.xml.ClassDescriptorResolver;

/**
 * Registry for {@link ClassMolder} implementations obtained from 
 * {@link ClassDescriptorResolver}.
 * 
 * @author <a href="mailto:wsdou55 AT gmail DOT com">Wensheng Dou</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class ClassMolderRegistry {
    //-----------------------------------------------------------------------------------    
    
    /** Association between {@link ClassMolder} name and factory implementation. */
    private final HashMap<String, ClassMolder> _classMolders
        = new HashMap<String, ClassMolder>();

    //-----------------------------------------------------------------------------------    

    /**
     * Construct an instance of ClassMolderRegistry that uses given 
     * {@link ClassDescriptorResolver}.
     * 
     * @param cdResolver {@link ClassDescriptorResolver} instance used for resolving
     *        {@link ClassDescriptor}.
     * @param persistenceFactory Factory for creating persistence engines for each
     *        object described in the map.
     * @param engine the Lockengine
     * @throws MappingException Indicate that one of the mappings is invalid
     */
    public ClassMolderRegistry(final ClassDescriptorResolver cdResolver,
            final PersistenceFactory persistenceFactory, final LockEngine engine)
            throws MappingException {
        try {
            JDOClassDescriptorResolver jdoResolver = (JDOClassDescriptorResolver) cdResolver;
            DatingService ds = new DatingService(jdoResolver.getMappingLoader().getClassLoader());

            Iterator<ClassDescriptor> iter = jdoResolver.descriptorIterator();
            while (iter.hasNext()) {
                ClassDescriptor desc = iter.next();

                if (!(desc.hasNature(ClassDescriptorJDONature.class.getName()))) {
                    throw new IllegalArgumentException(
                            "Class does not have a JDO descriptor");
                }

                if (!new ClassDescriptorJDONature(desc).hasMappedSuperclass()) {
                    Persistence persistence = persistenceFactory.getPersistence(desc);
                    ClassMolder molder = new ClassMolder(ds, jdoResolver,
                            engine, desc, persistence);
                    _classMolders.put(molder.getName(), molder);
                }
            }

            ds.close();

            Iterator<ClassMolder> iterClassMolder = _classMolders.values().iterator();
            while (iterClassMolder.hasNext()) {
                ClassMolder molder = iterClassMolder.next();
                molder.resetResolvers();
                molder.getPriority();
            }
        } catch (ClassNotFoundException e) {
            throw new MappingException("Declared Class not found!");
        }
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Get classMolder which represents the given java data object class.
     * Dependent class will not be returned to avoid persisting 
     * a dependent class without.
     * 
     * @param cls Class instance for which a class molder should be returned. 
     * @return The class molder for the specified class.
     */
    public ClassMolder getClassMolder(final Class<?> cls) {
        return getClassMolder(cls.getName());
    }
    
    /**
     * Get classMolder which represents the given java data object class.
     * Dependent class will not be returned to avoid persisting 
     * a dependent class without.
     * 
     * @param classname the class name
     * @return The class molder for the specified class name.
     */
    public ClassMolder getClassMolder(final String classname) {
        ClassMolder molder = _classMolders.get(classname);
        if (molder != null && !molder.isDependent()) {
            return molder;
        }
        return null;
    }
    
    /**
     * Get classMolder which represents the given java data object class.
     * 
     * @param cls Class instance for which a class molder should be returned. 
     * @return The class molder for the specified class.
     */
    public ClassMolder getClassMolderWithDependent(final Class<?> cls) {
        return getClassMolderWithDependent(cls.getName());
    }
    
    /**
     * Get classMolder which represents the given java data object class.
     * 
     * @param classname the class name
     * @return The class molder for the specified class name.
     */
    public ClassMolder getClassMolderWithDependent(final String classname) {
        return _classMolders.get(classname);
    }
    
    /**
     * Returns the ClassMolder instance that has a named query associated with the name given.
     * 
     * @param name Name of a named query.
     * @return ClassMolder instance associated with the named query.
     */
    public ClassMolder getClassMolderByQuery(final String name) {        
        Iterator<ClassMolder> iterClassMolder = _classMolders.values().iterator();
        while (iterClassMolder.hasNext()) {
            ClassMolder molder = iterClassMolder.next();
            if (molder.getNamedQuery(name) != null) {
                return molder;
            }
        }
        return null;
    }
    
    /**
     * Returns the ClassMolder instance that has a named native query associated with the
     * name given.
     * 
     * @param name Name of a named query.
     * @return ClassMolder instance associated with the named native query.
     */
    public ClassMolder getClassMolderByNativeQuery(final String name) {
        Iterator<ClassMolder> iterClassMolder = _classMolders.values().iterator();
        while (iterClassMolder.hasNext()) {
            ClassMolder molder = iterClassMolder.next();
            if (molder.getNamedNativeQuery(name) != null) {
                return molder;
            }
        }
        return null;
    }
    
    /**
     * @return all the ClassMolders in the ClassMolderRegistry
     */
    HashSet<ClassMolder> getAllClassMolders() {
        HashSet<ClassMolder> molders = new HashSet<ClassMolder>();
        Iterator<ClassMolder> iter = _classMolders.values().iterator();
        while (iter.hasNext()) {
            molders.add(iter.next());
        }
        return molders;
    }

    //-----------------------------------------------------------------------------------    
}
