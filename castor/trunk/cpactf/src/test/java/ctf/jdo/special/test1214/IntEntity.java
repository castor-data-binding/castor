/*
 * Copyright 2005 Ralf Joachim
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
package jdo.test1214;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IntEntity {
    private static final Log LOG = LogFactory.getLog(IntEntity.class);

    private int id;
    private int property;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProperty() { return property; }
    public void setProperty(int id) {
        LOG.debug ("setProperty(int)");
        this.property = id;
    }
}
