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
