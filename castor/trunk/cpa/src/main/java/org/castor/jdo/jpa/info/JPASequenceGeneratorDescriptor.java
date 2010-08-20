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
package org.castor.jdo.jpa.info;

import javax.persistence.SequenceGenerator;

public class JPASequenceGeneratorDescriptor extends JPAKeyGeneratorDescriptor {

	private String sequenceName;

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public static JPASequenceGeneratorDescriptor extract(SequenceGenerator sequenceGenerator) {
		JPASequenceGeneratorDescriptor descriptor = new JPASequenceGeneratorDescriptor();
		descriptor.setName(sequenceGenerator.name());
		descriptor.setAllocationSize(sequenceGenerator.allocationSize());
		descriptor.setInitialValue(sequenceGenerator.initialValue());
		descriptor.setSequenceName(sequenceGenerator.sequenceName());
		
		return descriptor;
	}


	
}
