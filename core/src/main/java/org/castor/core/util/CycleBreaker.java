/*
 * Copyright 2007 Jim Procter
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
package org.castor.core.util;

import java.util.IdentityHashMap;

/**
 * <p>
 * lightweight mechanism for thread-safe detection of cyclic calls to hashCode or equals in
 * objects created by the XML CodeGenerator.
 * </p>
 * <p>Usage</p>
 * <ol><li>
 * startingToCycle is called on a particular object prior to recursing on it, and recursion
 * should only occur if this call returns false.
 * </li>
 * <li>
 * releaseCycleHandle is called after the recursive call returns in order to release the cycle
 * lock on the object.
 * </li>
 * </ol>
 * <p><strong>Note :</strong> Do not use this cycle breaking mechanism on object comparisons
 * where two instances may share the same reference to some third object, such as a String constant.
 * </p>
 * @author <a href="mailto:jimp@compbio.dundee.ac.uk">Jim Procter</a>
 */
public class CycleBreaker {
    /** Hash of threads and objects that we are keeping track of. */
    private static IdentityHashMap _threadHash = new IdentityHashMap();
    
    /**
     * Test to see if we are about to begin cycling on a method call to beingHashed.
     *  
     * @param beingHashed the object to check for a cycle.
     * @return true if a cycle is about to occur on this non-null object.
     */
    public static boolean startingToCycle(final Object beingHashed) {
        if (beingHashed == null) {
            return false;
        }
        
        Object hthr = _threadHash.get(Thread.currentThread());
        if (hthr == null) {
            _threadHash.put(Thread.currentThread(), hthr = new IdentityHashMap());
            ((IdentityHashMap) hthr).put(beingHashed, beingHashed); 
            return false; // first call. no cycle detected
        }
        Object objhandle = ((IdentityHashMap) hthr).get(beingHashed);
        if (objhandle == null) {
            // this is the default for a hash value currently being computed.
            ((IdentityHashMap) hthr).put(beingHashed, beingHashed);
            return false; // first call. no cycle detected.
        }
        return true;
    }
    
    /**
     * Called to release Cycling lock for this object at the end of a routine
     * where cycles are to be detected.
     * 
     * @param beingHashed the object for which the cycle-lock will be released.
     */
    public static void releaseCycleHandle(final Object beingHashed) {
        if (beingHashed == null) {
            return;
        }
        Object hthr = _threadHash.get(Thread.currentThread());
        if (hthr != null) {
            if (((IdentityHashMap) hthr).containsKey(beingHashed)) {
                ((IdentityHashMap) hthr).remove(beingHashed); 
                // release any references if we have no more CycleHandles
                if (((IdentityHashMap) hthr).size() == 0) {
                    _threadHash.remove(hthr);
                }
            }
        }
    }
}