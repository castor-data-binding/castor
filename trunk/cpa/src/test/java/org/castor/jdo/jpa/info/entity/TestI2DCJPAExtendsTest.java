/*
 * Copyright 2008 Werner Guttmann
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
package org.castor.jdo.jpa.info.entity;

import javax.persistence.Id;

/**
 * Domain class annotated with JPA annotation used for unit testing.
 * @author Peter Schmidt
 */
public class TestI2DCJPAExtendsTest extends TestI2DCJPATest {
    
    /**
     * Show that the primary_key is the ID of this entity.
     */
    @Id
    public String _primaryKey;

}
