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
package org.castor.ddlgen.engine.postgresql;

import org.castor.ddlgen.DDLWriter;
import org.castor.ddlgen.GeneratorException;
import org.castor.ddlgen.keygenerator.IdentityKeyGenerator;
import org.castor.ddlgen.schemaobject.Field;

/**
 * PostgreSQL field.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class PostgresqlField extends Field {
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void toCreateDDL(final DDLWriter writer) throws GeneratorException {
        writer.print(getName());
        writer.print(" ");
        
        if (isIdentity() && (getKeyGenerator() instanceof IdentityKeyGenerator)) {
            if ("integer".equalsIgnoreCase(getType().getSqlType())) {
                writer.print("SERIAL");
            } else {
                writer.print("BIGSERIAL");
            }
        } else {
            writer.print(getType().toDDL(this));
        }

        if (isIdentity() || isRequired()) { writer.print(" NOT NULL"); }
    }

    /**
     * {@inheritDoc}
     */
    public String toCreateDDL() throws GeneratorException {
        StringBuffer sb = new StringBuffer();
        sb.append(getName()).append(" ");

        if (isIdentity() && (getKeyGenerator() instanceof IdentityKeyGenerator)) {
            if ("integer".equalsIgnoreCase(getType().getSqlType())) {
                sb.append("SERIAL");
            } else {
                sb.append("BIGSERIAL");
            }
        } else {
            sb.append(getType().toDDL(this));
        }

        if (isIdentity() || isRequired()) { sb.append(" NOT NULL"); }
        
        return sb.toString();
    }

    //--------------------------------------------------------------------------
}
