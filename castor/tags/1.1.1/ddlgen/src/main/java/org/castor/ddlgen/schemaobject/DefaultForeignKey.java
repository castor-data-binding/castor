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
package org.castor.ddlgen.schemaobject;

import org.castor.ddlgen.DDLGenConfiguration;

/**
 * Default foreign key.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class DefaultForeignKey extends ForeignKey  {
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String toCreateDDL() {
        String newline = getConfiguration().getStringValue(
                DDLGenConfiguration.NEWLINE_KEY, DDLGenConfiguration.DEFAULT_NEWLINE);

        StringBuffer sb = new StringBuffer();
        sb.append(newline).append(newline);
        sb.append("ALTER TABLE ").append(getTable().getName());
        sb.append(newline);
        sb.append("ADD CONSTRAINT ").append(getName());
        sb.append(newline);
        sb.append("FOREIGN KEY (").append(fieldNames()).append(')');
        sb.append(newline);
        sb.append("REFERENCES ").append(getReferenceTable().getName());
        sb.append(" (").append(referencedFieldNames()).append(')');
        sb.append(DDLGenConfiguration.DEFAULT_STATEMENT_DELIMITER);
        return sb.toString();
    }

    //--------------------------------------------------------------------------
}
