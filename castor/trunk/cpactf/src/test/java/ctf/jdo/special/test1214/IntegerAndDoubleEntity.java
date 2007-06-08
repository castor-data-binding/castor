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

public class IntegerAndDoubleEntity {
    private static Log LOG = LogFactory.getLog(IntegerAndDoubleEntity.class);
	
    private int id;
    private Integer property;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getProperty() { return property; }
    public void setProperty(Integer property) {
    	LOG.debug ("setProperty(Integer)");
        this.property = property;
    }
    public void setProperty(Double property) {
    	LOG.debug ("setProperty(Double)");
        this.property = new Integer (property.intValue());
    }
}
