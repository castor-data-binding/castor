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

import org.castor.ddlgen.DDLWriter;
import org.castor.ddlgen.schemaobject.KeyGenerator;
import org.exolab.castor.mapping.xml.KeyGeneratorDef;

/**
 * UUID key generator will be handled by Castor so no DDL needs to be created.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class UUIDKeyGenerator extends KeyGenerator {
    //--------------------------------------------------------------------------

    /** Name of key generator algorithm. */
    public static final String ALGORITHM_NAME = "UUID";

    //--------------------------------------------------------------------------

    /**
     * Constructor for default UUID key generator.
     */
    public UUIDKeyGenerator() {
        super(ALGORITHM_NAME, ALGORITHM_NAME);
    }

    /**
     * Constructor for UUID key generator specified by given defintion.
     * 
     * @param definition Key generator definition.
     */
    public UUIDKeyGenerator(final KeyGeneratorDef definition) {
        super(ALGORITHM_NAME, definition.getAlias());
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void toCreateDDL(final DDLWriter writer) { }

    /**
     * {@inheritDoc}
     */
    public void toDropDDL(final DDLWriter writer) { }

    //--------------------------------------------------------------------------
}
