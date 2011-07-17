/*
 * Copyright 2010 Werner Guttmann
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
package org.castor.cpa.jpa.info;

import javax.persistence.SequenceGenerator;

/**
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 */
public class JPASequenceGeneratorDescriptor extends JPAKeyGeneratorDescriptor {

    private String _sequenceName;

    public String getSequenceName() {
        return _sequenceName;
    }

    public void setSequenceName(final String sequenceName) {
        _sequenceName = sequenceName;
    }

    public static JPASequenceGeneratorDescriptor extract(
            final SequenceGenerator sequenceGenerator) {
        JPASequenceGeneratorDescriptor descriptor = new JPASequenceGeneratorDescriptor();
        descriptor.setName(sequenceGenerator.name());
        descriptor.setAllocationSize(sequenceGenerator.allocationSize());
        descriptor.setInitialValue(sequenceGenerator.initialValue());
        descriptor.setSequenceName(sequenceGenerator.sequenceName());
        
        return descriptor;
    }
}
