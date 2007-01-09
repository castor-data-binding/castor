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
package org.castor.ddlgen.engine.hsql;

import java.text.MessageFormat;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.keygenerator.SequenceKeyGenerator;
import org.castor.ddlgen.keygenerator.SequenceKeyGeneratorFactory;
import org.castor.ddlgen.schemaobject.KeyGenerator;

/**
 * Factory class for SEQUENCE key generators for HSQL.
 *
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class HsqlSequenceKeyGeneratorFactory extends SequenceKeyGeneratorFactory {
    /**
     * {@inheritDoc}
     */
    public String toCreateDDL(final KeyGenerator key) {
        SequenceKeyGenerator sequenceKey = (SequenceKeyGenerator) key;
        DDLGenConfiguration conf = sequenceKey.getConfiguration();
        StringBuffer buff = new StringBuffer();
        String tableName = sequenceKey.getTable().getName();
        String pkList = toPrimaryKeyList(key.getTable());
        String sequenceName = MessageFormat.format(sequenceKey.getSequence(),
                new Object[]{tableName, pkList});

        String newline = conf.getStringValue(
                DDLGenConfiguration.NEWLINE_KEY, DDLGenConfiguration.DEFAULT_NEWLINE);
        String indent = conf.getStringValue(
                DDLGenConfiguration.INDENT_KEY, DDLGenConfiguration.DEFAULT_INDENT);

        buff.append(newline).append(newline);
        buff.append("CREATE SEQUENCE ").append(sequenceName).append(" AS INTEGER");
        buff.append(newline).append(indent);
        buff.append("START WITH 1 INCREMENT BY 1");
        buff.append(DDLGenConfiguration.DEFAULT_STATEMENT_DELIMITER);

        if (sequenceKey.isTrigger()) {
            String pkTypeList = toPrimaryKeyTypeList(key.getTable());
            String triggerName = null;
            if (sequenceName.matches(".*SEQ.*")) {
                triggerName = sequenceName.replaceAll("SEQ", "TRG");
            } else {
                triggerName = "TRG" + sequenceName;
            }

            String triggerTemp = conf.getStringValue(
                    DDLGenConfiguration.TRIGGER_TEMPLATE_KEY, "");

            triggerTemp = triggerTemp.replaceAll("<trigger_name>", triggerName);
            triggerTemp = triggerTemp.replaceAll("<sequence_name>", sequenceName);
            triggerTemp = triggerTemp.replaceAll("<table_name>", tableName);
            triggerTemp = triggerTemp.replaceAll("<pk_name>", pkList);
            triggerTemp = triggerTemp.replaceAll("<pk_type>", pkTypeList);
            buff.append(newline);
            buff.append(newline);
            buff.append(triggerTemp);
        }

        return buff.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String toDropDDL(final KeyGenerator key) {
        return "";
    }
}
