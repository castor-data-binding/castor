/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test201;

import java.io.Serializable;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public class SelfRelationFolderParent implements Serializable, TimeStampable {
    private static final long serialVersionUID = 3809114535230964610L;

    private Integer _id = null;
    private String _name = null;
    private long _timeStamp;
  
    public final Integer getId() { return _id; }
    public final void setId(final Integer id) { _id = id; }
  
    public final String getName() { return _name; }
    public final void setName(final String name) { _name = name; }
    
    public final long jdoGetTimeStamp() { return _timeStamp; }
    public final void jdoSetTimeStamp(final long timeStamp) { _timeStamp = timeStamp; }
}
