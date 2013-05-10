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
package org.castor.ddlgen.engine.mysql;

import org.castor.ddlgen.Configuration;
import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.DDLWriter;
import org.castor.ddlgen.schemaobject.ForeignKey;

/**
 * Foreign key of MySQL database engine.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class MysqlForeignKey extends ForeignKey {
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void toCreateDDL(final DDLWriter writer) {
        Configuration conf = getConfiguration();
        String del = conf.getStringValue(DDLGenConfiguration.FOREIGN_KEY_ON_DELETE_KEY, null);
        String upd = conf.getStringValue(DDLGenConfiguration.FOREIGN_KEY_ON_UPDATE_KEY, null);
        String delimiter = DDLGenConfiguration.DEFAULT_STATEMENT_DELIMITER;
        
        writer.println();
        writer.println();
        writer.println("ALTER TABLE {0}", new Object[] {getTable().getName()});
        writer.println("ADD CONSTRAINT {0}", new Object[] {getName()});
        writer.print("FOREIGN KEY {0} (", new Object[] {getName()});
        fieldNames(writer);
        writer.println(")");
        writer.print("REFERENCES {0} (", new Object[] {getReferenceTable().getName()});
        referencedFieldNames(writer);
        writer.print(")");

        if ((del != null) && !"".equals(del)) {
            writer.println();
            writer.print("ON DELETE ");
            writer.print(del);
        }

        if ((upd != null) && !"".equals(upd)) {
            writer.println();
            writer.print("ON UPDATE ");
            writer.print(upd);
        }
        
        writer.print(delimiter);
    }

    //--------------------------------------------------------------------------
}
