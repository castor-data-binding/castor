/*
 * Copyright 2008 Joachim Grueneis
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
package org.castor.xml;

/**
 * An interface to allow external "listening" to objects when
 * they are being unmarshalled for various tracking purposes and
 * potential modification. An implementation of
 * this interface may be registered with the Unmarshaller.<br/>
 * This is already a new version of this interface with
 * enhanced callback methods. The orginial implementation still
 * exists but is deprecated {@link org.exolab.castor.xml.UnmarshalListener}.
 * <p/>
 * The UnmarshalListener interface does <em>not</em> report on
 * native data types that are unmarshalled.
 * <p/>
 * The first definition of this interface was by
 * <a href="mailto:paul@priorartisans.com">Paul Christmann</a>,
 * <a href="mailto:kvsico@intalio.com">Keith Visco</a> and
 * <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>.
 * 
 * @author Joachim Grueneis, jgrueneis AT codehaus DOT org
 * @version $Revision$
 * @since 1.2
 */
public interface UnmarshalListener {
    /**
     * This method is called when an object has just been initialized by the
     * Unmarshaller.
     *
     * @param target the Object that was initialized.
     * @param parent the parent of the target that was initialized
     */
    void initialized (final Object target, final Object parent);

    /**
     * This method is called once the attributes have been processed.
     * It indicates that the the fields of the given object corresponding
     * to attributes in the XML document have been set.
     *
     * @param target the Object the object being unmarshalled.
     * @param parent the parent of the target being unmarshalled
     */
    void attributesProcessed(final Object target, final Object parent);

    /**
     * This method is called after a child object has been added during the
     * unmarshalling. This method will be called after {@link #unmarshalled(Object)} has
     * been called for the child.
     *
     * @param fieldName The Name of the field the child is being added to.
     * @param parent The Object being unmarshalled.
     * @param child The Object that was just added.
     */
    void fieldAdded (String fieldName, Object parent, Object child);

    /**
     * This method is called after an object
     * has been completely unmarshalled, including
     * all of its children (if any).
     *
     * @param target the Object that was unmarshalled.
     * @param parent the parent of the target that was unmarshalled
     */
    void unmarshalled (final Object target, final Object parent);
}
