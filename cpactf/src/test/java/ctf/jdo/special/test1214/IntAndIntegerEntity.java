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
package ctf.jdo.special.test1214;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class IntAndIntegerEntity {
    private static final Log LOG = LogFactory.getLog(IntAndIntegerEntity.class);

    private int _id;
    private Integer _property;
    
    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }

    public Integer getProperty() { return _property; }
    public void setProperty(final Integer property) {
        LOG.debug("setProperty(Integer)");
        _property = property;
    }
    public void setProperty(final int property) {
        LOG.debug("setProperty(int)");
        _property = new Integer (property);
    }
}
