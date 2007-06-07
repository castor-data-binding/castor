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
package org.castor.ddlgen.engine.oracle;

import java.text.MessageFormat;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.DDLWriter;
import org.castor.ddlgen.keygenerator.SequenceKeyGenerator;
import org.castor.ddlgen.keygenerator.SequenceKeyGeneratorFactory;
import org.castor.ddlgen.schemaobject.KeyGenerator;

/**
 * Factory class for SEQUENCE key generators for Oracle.
 *
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class OracleSequenceKeyGeneratorFactory extends SequenceKeyGeneratorFactory {
    /**
     * {@inheritDoc}
     */
    public void toCreateDDL(final KeyGenerator key, final DDLWriter writer) {
        SequenceKeyGenerator sequenceKey = (SequenceKeyGenerator) key;
        String tableName = sequenceKey.getTable().getName();
        String pkList = toPrimaryKeyList(key.getTable());
        String pkTypeList = toPrimaryKeyTypeList(key.getTable());
        String sequenceName = MessageFormat.format(sequenceKey.getSequence(),
                new Object[]{tableName, pkList});

        writer.println();
        writer.println();
        writer.print("CREATE SEQUENCE ");
        writer.println(sequenceName);
        writer.print("MAXVALUE ");
        writer.println(Integer.MAX_VALUE);
        writer.print("INCREMENT BY 1 START WITH 1");
        writer.println(DDLGenConfiguration.DEFAULT_STATEMENT_DELIMITER);

        if (sequenceKey.isTrigger()) {
            String triggerName = null;
            if (sequenceName.matches(".*SEQ.*")) {
                triggerName = sequenceName.replaceAll("SEQ", "TRG");
            } else {
                triggerName = "TRG" + sequenceName;
            }
            DDLGenConfiguration conf = sequenceKey.getConfiguration();
            String triggerTemp = conf.getStringValue(DDLGenConfiguration.TRIGGER_TEMPLATE_KEY, "");

            triggerTemp = triggerTemp.replaceAll("<trigger_name>", triggerName);
            triggerTemp = triggerTemp.replaceAll("<sequence_name>", sequenceName);
            triggerTemp = triggerTemp.replaceAll("<table_name>", tableName);
            triggerTemp = triggerTemp.replaceAll("<pk_name>", pkList);
            triggerTemp = triggerTemp.replaceAll("<pk_type>", pkTypeList);

            writer.println();
            writer.println();
            writer.println(triggerTemp);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void toDropDDL(final KeyGenerator key, final DDLWriter writer) { }
}
