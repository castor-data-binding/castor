/*
 * Copyright 2008 Tobias Hochwallner
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
package org.castor.cpa.functional.onetoone;
import org.exolab.castor.jdo.JDOManager;


/**
 *
 *
 * @author Tobias Hochwallner
 */
public final class OneToOneMixedBATest extends BaseOneToOneTest {

    /**
     * Spring bean name.
     */
    private static final String JDOMANAGER_BEAN = "JDOManagerMixedBA";

    /**
     * Returns the name of the {@link JDOManager} bean.
     * 
     * @return Bean name.
     */
    protected String getJDOManagerBeanName() {
        return JDOMANAGER_BEAN;
    }
}
