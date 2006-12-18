/*
 * Copyright 2006 Werner Guttmann, Ralf Joachim
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
package org.exolab.castor.builder.conflictresolution;

import java.util.Enumeration;

import org.exolab.castor.builder.SGStateInfo;
import org.exolab.castor.builder.binding.ExtendedBinding;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.util.ConsoleDialog;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.javasource.JClass;

/**
 * An implementation of {@link ClassNameCRStrategy} that reports
 * any conflict notifications to a console dialog, asking the user whether to
 * stop code generation (as the conflict is not acceptable), or whether to
 * proceed by overwriting an already existing class.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 1.1
 */
public class WarningViaDialogClassNameCRStrategy
        implements ClassNameCRStrategy {

    /**
     * Name of this strategy.
     */
    public static final String NAME = "warnViaConsoleDialog";

    /**
     * The {@link ConsoleDialog} instance to use for output
     */
    private ConsoleDialog _dialog;

    /**
     * Creates an instance of this name conflict resolution strategy, that will
     * use the specified {@link ConsoleDialog} instance to emit warnings to zhe
     * user and ask about an approach to deal with them.
     */
    public WarningViaDialogClassNameCRStrategy() {
    }

    /**
     * @see org.exolab.castor.builder.conflictresolution.ClassNameCRStrategy#dealWithClassNameConflict(org.exolab.castor.builder.SGStateInfo,
     *      org.exolab.castor.builder.info.ClassInfo, org.exolab.javasource.JClass)
     */
    public SGStateInfo dealWithClassNameConflict(SGStateInfo state,
            ClassInfo newClassInfo, JClass conflict) {
        if (!state.getSuppressNonFatalWarnings()) {
            // -- if the ClassInfo are equal, we can just return
            ClassInfo oldClassInfo = state.resolve(conflict);
            if (oldClassInfo == newClassInfo) {
                return state;
            }

            // -- Find the Schema structures that are conflicting
            Annotated a1 = null;
            Annotated a2 = null;

            // Loop until we exhaust the Enumeration or until we have found both
            Enumeration enumeration = state.keys();
            while (enumeration.hasMoreElements() && (a1 == null || a2 == null)) {
                Object key = enumeration.nextElement();
                if (!(key instanceof Annotated)) {
                    continue;
                }

                ClassInfo cInfo = state.resolve(key);
                if (newClassInfo == cInfo) {
                    a1 = (Annotated) key;
                } else if (oldClassInfo == cInfo) {
                    a2 = (Annotated) key;
                }
            }

            StringBuffer error = new StringBuffer();
            error
                    .append("Warning: A class name generation conflict has occured between ");
            if (a1 != null) {
                error.append(SchemaNames.getStructureName(a1));
                error.append(" '");
                error.append(ExtendedBinding.getSchemaLocation(a1));
            } else {
                error.append(newClassInfo.getNodeTypeName());
                error.append(" '");
                error.append(newClassInfo.getNodeName());
            }
            error.append("' and ");
            if (a2 != null) {
                error.append(SchemaNames.getStructureName(a2));
                error.append(" '");
                error.append(ExtendedBinding.getSchemaLocation(a2));
            } else {
                error.append(oldClassInfo.getNodeTypeName());
                error.append(" '");
                error.append(oldClassInfo.getNodeName());
            }
            error.append("'. Please use a Binding file to solve this problem.");
            error.append("Continue anyway [not recommended] ");

            char ch = _dialog
                    .confirm(error.toString(), "yn", "y = yes, n = no");
            if (ch == 'n') {
                state.setStatusCode(SGStateInfo.STOP_STATUS);
            }
        }
        return state;

    }

    /**
     * @see org.exolab.castor.builder.conflictresolution.ClassNameCRStrategy#getName()
     */
    public String getName() {
        return NAME;
    }

    /**
     * @see org.exolab.castor.builder.conflictresolution.ClassNameCRStrategy#setConsoleDialog(org.exolab.castor.builder.util.ConsoleDialog)
     */
    public void setConsoleDialog(ConsoleDialog dialog) {
        this._dialog = dialog;
    }

}
