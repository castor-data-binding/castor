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

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Registry for {@link ClassNameConflictResolutionStrategy} implementations obtained from the
 * Castor builder properties file.
 * 
 * @author <a href="mailto:werner DOT guttmann @gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 5951 $ $Date: 2006-04-08 08:58:10 -0600 (Sat, 08 Apr 2006) $
 * @since 1.1
 */
public final class ClassNameConflictResolutionStrategyRegistry {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(
            ClassNameConflictResolutionStrategyRegistry.class);

    /**
     * Association between name of {@link ClassNameConflictResolutionStrategy} 
     * implementation and {@link ClassNameConflictResolutionStrategy} instance. 
     */
    private Map _strategies = new HashMap();

    //--------------------------------------------------------------------------

    /**
     * Construct an instance of {@link ClassNameConflictResolutionStrategyRegistry} 
     * that loads the {@link ClassNameConflictResolutionStrategy} implementations 
     * specified in the given {@link BuilderConfiguration}.
     * 
     * @param  The {@link BuilderConfiguration}.
     */
    public ClassNameConflictResolutionStrategyRegistry(final String enlistedNameConflictStrategies) {
        StringTokenizer tokenizer = new StringTokenizer(enlistedNameConflictStrategies, ", ");
        ClassLoader loader = ClassNameConflictResolutionStrategyRegistry.class.getClassLoader();
        while (tokenizer.hasMoreTokens()) {
            String classname = tokenizer.nextToken();
            try {
                Class cls = loader.loadClass(classname);
                Object obj = cls.newInstance();
                ClassNameConflictResolutionStrategy strategy = (ClassNameConflictResolutionStrategy) obj;
                _strategies.put(strategy.getName(), strategy);
            } catch (Exception except) {
                LOG.error("The ClassNameConflictResolutionStrategy " + classname + " "
                        + "specified in the Castor builder properties file could not "
                        + "be instantiated.");
            }
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the names of all the configured {@link ClassNameConflictResolutionStrategy}
     * implementations. A {@link ClassNameConflictResolutionStrategy} instance can be obtained
     * by the {@link #getClassNameConflictResolutionStrategy} method.
     *
     * @return Names of {@link ClassNameConflictResolutionStrategy} implementations
     */
    public String[] getClassNameConflictResolutionStrategyNames() {
        String[] names = new String[_strategies.size()];
        return (String[]) _strategies.keySet().toArray(names);
    }

    /**
     * Returns a {@link ClassNameConflictResolutionStrategy} with the specified name. Returns
     * null if the named strategy is not supported.
     *
     * @param name The name of the ClassNameConflictResolutionStrategy.
     * @return The {@link TransactionManagerFactory} or null if none exists.
     * @throws IllegalArgumentException If TransactoinManagerFactory
     *         with given name could not be found.
     */
    public ClassNameConflictResolutionStrategy getClassNameConflictResolutionStrategy(final String name)
        throws IllegalArgumentException {
        Object factory = _strategies.get(name);
        if (factory == null) {
            String msg = "The ClassNameConflictResolutionStrategy '" + name + "' "
                       + "does not exist in the Castor builder properties file "
                       + "and is therefore not supported.";
            LOG.error(msg);
            throw new IllegalArgumentException(msg);
        }
        return (ClassNameConflictResolutionStrategy) factory;
    }

}
