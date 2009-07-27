/*
 * Copyright 2008 Sebastian Gabmeyer
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
 */
package org.exolab.castor.jdo.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.exolab.castor.jdo.util.jdo_descriptors.ClassToBeResolvedJDODescriptor;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.xml.util.ClassDescriptorResolutionCommand;
import org.exolab.castor.xml.util.ClassLoaderNature;
import org.exolab.castor.xml.util.ClassResolutionByCDR;
import org.exolab.castor.xml.util.PackageBasedCDRResolutionNature;

/**
 * Test case for {@link ClassResolutionByCDR}.
 * 
 * @author Sebastian Gabmeyer
 * @since 1.2.1
 */
public class ClassResolutionByCDRTest extends TestCase {

    private ClassDescriptorResolutionCommand _resolver;

    protected void setUp() throws Exception {
        super.setUp();
        // define a ClassResoltionByCDR resolver
        _resolver = new ClassResolutionByCDR();
        // add a package name to the _resolver telling it where to look for
        // classes to be resolved
        new PackageBasedCDRResolutionNature(_resolver)
                .addPackageName("org.exolab.castor.jdo.util");
        // define the ClassLoader to be used by the _resolver
        ClassLoaderNature clNature = new ClassLoaderNature(_resolver);
        clNature.setClassLoader(getClass().getClassLoader());
    }

    public void testSuccessfulResolve() throws Exception {
        ClassDescriptor classDesc = _resolver.resolve(ClassToBeResolved.class);

        assertEquals(ClassToBeResolvedJDODescriptor.class.getName(), classDesc
                .getClass().getName());
    }

    public void testUnsuccessfulResolve() throws Exception {
        ClassDescriptor classDesc = _resolver
                .resolve(ClassNotToBeResolved.class);

        assertNull(classDesc);
    }

    public void testGetDescriptors() throws Exception {
        int expectedNumOfResolvedClasses = 1;
        List packageNames = new LinkedList();
        packageNames.add("org.exolab.castor.jdo.util");
        Map resolvedClasses = new HashMap();

        for (Iterator it = packageNames.iterator(); it.hasNext();) {
            Map descriptors = ((ClassResolutionByCDR) _resolver)
                    .getDescriptors((String) it.next());
            resolvedClasses.putAll(descriptors);
        }

        assertEquals(resolvedClasses.size(), expectedNumOfResolvedClasses);
    }

    // This was just used for verification, might put this into its own testcase
    // public void testResolutionByFile() throws Exception {
    // // define a ClassResoltionByCDR resolver
    // _resolver = new ClassResolutionByFile();
    // // define the ClassLoader to be used by the _resolver
    // ClassLoaderNature clNature = new ClassLoaderNature(_resolver);
    // clNature.setClassLoader(getClass().getClassLoader());
    //        
    // ClassDescriptor desc = _resolver.resolve(ClassToBeResolved.class);
    // assertEquals(ClassToBeResolvedJDODescriptor.class.getName(),
    // desc.getClass().getName());
    // }
}
