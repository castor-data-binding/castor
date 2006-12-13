package org.exolab.castor.builder;

import org.exolab.javasource.JClass;

/**
 * Strategy pattern for dealing with class name conflicts
 * during source code generation. The various implementations of this stragtegy
 * will implement individual approaches for dealing with such a naming 
 * collision.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 */
public interface ClassNameConflictResolutionStrategy {

    /**
     * Implements a specific strategy for dealing with class name conflicts 
     * @param state The current source generator state.
     * @param newClassInfo The {@link CLassInfo} for the new class to be generated.
     * @param conflict The {@link JClass} instance representing the potential conflict.
     * @return
     */
    SGStateInfo dealWithClassNameConflict(final SGStateInfo state,
            final ClassInfo newClassInfo, final JClass conflict);

}
