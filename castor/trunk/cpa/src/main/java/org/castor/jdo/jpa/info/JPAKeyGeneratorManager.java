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
package org.castor.jdo.jpa.info;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JPAKeyGeneratorManager {

    private Map<String, JPAKeyGeneratorDescriptor> generators =
        new ConcurrentHashMap<String, JPAKeyGeneratorDescriptor>();

    public static final String AUTO_GENERATOR_NAME = "AUTO";

    /**
     * Thread-safe singleton implementation based on the initialization on
     * demand holder idiom.
     */
    private static class SingletonHolder {
        private static final JPAKeyGeneratorManager INSTANCE = new JPAKeyGeneratorManager();
    }

    public static JPAKeyGeneratorManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void add(String name, JPAKeyGeneratorDescriptor descriptor)
            throws GeneratorNameAlreadyUsedException {
        if (generators.containsKey(name)) {
            throw new GeneratorNameAlreadyUsedException();
        }
        generators.put(name, descriptor);
    }

    public boolean contains(String name) {
        return generators.containsKey(name);
    }

    public JPAKeyGeneratorDescriptor get(String name) {
        return generators.get(name);
    }

    public void reset() {
        generators.clear();
    }

    public boolean isEmpty() {
        return generators.isEmpty();
    }

    public JPAKeyGeneratorDescriptor getAuto() {
        
        if (!contains(AUTO_GENERATOR_NAME)) {
            synchronized (generators) {
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