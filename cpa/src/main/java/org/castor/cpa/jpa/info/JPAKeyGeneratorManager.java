/*
 * Copyright 2010 Werner Guttmann
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
package org.castor.cpa.jpa.info;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 */
public class JPAKeyGeneratorManager {

    private Map<String, JPAKeyGeneratorDescriptor> _generators =
        new ConcurrentHashMap<String, JPAKeyGeneratorDescriptor>();

    public static final String AUTO_GENERATOR_NAME = "AUTO";

    /**
     * Thread-safe singleton implementation based on the initialization on
     * demand holder idiom.
     */
    private static final class SingletonHolder {
        private static final JPAKeyGeneratorManager INSTANCE = new JPAKeyGeneratorManager();
        
        private SingletonHolder() { }
    }

    public static JPAKeyGeneratorManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void add(final String name, final JPAKeyGeneratorDescriptor descriptor)
            throws GeneratorNameAlreadyUsedException {
        if (_generators.containsKey(name)) {
            throw new GeneratorNameAlreadyUsedException();
        }
        _generators.put(name, descriptor);
    }

    public boolean contains(final String name) {
        return _generators.containsKey(name);
    }

    public JPAKeyGeneratorDescriptor get(final String name) {
        return _generators.get(name);
    }

    public void reset() {
        _generators.clear();
    }

    public boolean isEmpty() {
        return _generators.isEmpty();
    }

    public JPAKeyGeneratorDescriptor getAuto() {
        
        if (!contains(AUTO_GENERATOR_NAME)) {
            synchronized (_generators) {
                JPAKeyGeneratorDescriptor autoDescriptor = new JPATableGeneratorDescriptor();
                try {
                    add(AUTO_GENERATOR_NAME, autoDescriptor);
                } catch (GeneratorNameAlreadyUsedException e) {
                    e.printStackTrace();
                }
            }
        }
        return get(AUTO_GENERATOR_NAME);
    }
}