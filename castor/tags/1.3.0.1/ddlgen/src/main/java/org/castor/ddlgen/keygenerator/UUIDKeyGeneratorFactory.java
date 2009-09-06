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
package org.castor.ddlgen.keygenerator;

import org.castor.ddlgen.GeneratorException;
import org.castor.ddlgen.KeyGeneratorFactory;
import org.castor.ddlgen.schemaobject.KeyGenerator;
import org.exolab.castor.mapping.xml.KeyGeneratorDef;

/**
 * Factory class for UUID key generators.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class UUIDKeyGeneratorFactory implements KeyGeneratorFactory {
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getAlgorithmName() { return UUIDKeyGenerator.ALGORITHM_NAME; }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasMandatoryParameters() { return false; }

    /**
     * {@inheritDoc}
     */
    public KeyGenerator createKeyGenerator() {
        return new UUIDKeyGenerator();
    }
    
    /**
     * {@inheritDoc}
     */
    public KeyGenerator createKeyGenerator(final KeyGeneratorDef definition)
    throws GeneratorException {
        return new UUIDKeyGenerator(definition);
    }

    //--------------------------------------------------------------------------
}
