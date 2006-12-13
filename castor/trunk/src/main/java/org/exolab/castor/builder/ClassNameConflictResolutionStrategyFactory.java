/*
 * Copyright 2006 Werner Guttmann
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
package org.exolab.castor.builder;

import org.exolab.castor.builder.util.ConsoleDialog;

/**
 * Factoyr for creating {@link ClassNameConflictResolutionStrategy} instances for a given type
 */
public class ClassNameConflictResolutionStrategyFactory {
    
    /**
     * Returns a {@link ClassNameConflictResolutionStrategy} instance mathcing the required type.
     * @param nameConflictStrategyType Type of the {@link ClassNameConflictResolutionStrategy}.
     * @param dialog {@link ConsoleDialog} instance for console output, if required
     * @return The adequate {@link ClassNameConflictResolutionStrategy} for the specified type.
     */
    public static ClassNameConflictResolutionStrategy newInstance(final String nameConflictStrategyType, final ConsoleDialog dialog) {
        ClassNameConflictResolutionStrategy strategy = null;
        if (nameConflictStrategyType.equals("warnViaConsole")) {
            strategy = new WarningViaConsoleDialogClassNameConflictResolutionStrategy(dialog);
        }
        return strategy;
    }

}
