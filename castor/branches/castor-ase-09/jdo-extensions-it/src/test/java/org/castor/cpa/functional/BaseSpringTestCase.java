/*
 * Copyright 2008 Lukas Lang
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
package org.castor.cpa.functional;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Provides a abstract base {@link TestCase} including a Spring
 * {@link ApplicationContext}.
 * 
 * @author Lukas Lang
 * 
 */
public abstract class BaseSpringTestCase extends TestCase {

    /**
     * Spring {@link ApplicationContext}.
     */
    protected ApplicationContext _context;

    /**
     * SetUp initializes Spring {@link ApplicationContext}.
     * 
     * @exception If
     *                SetUp fails.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this._context = getApplicationContext();
    }

    /**
     * Returns a {@link ClassPathXmlApplicationContext}.
     * 
     * @return A {@link ClassPathXmlApplicationContext}.
     */
    protected abstract ApplicationContext getApplicationContext();

}
