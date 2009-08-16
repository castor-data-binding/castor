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
package org.castor.cpa.test.test200;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public final class SelfRelationFolder implements Serializable, TimeStampable {
    private static final long serialVersionUID = -625272206256195346L;

    private Integer _id = null;
    private String _name = null;
    private Collection<SelfRelationFolder> _children = new ArrayList<SelfRelationFolder>();
    private SelfRelationFolder _parent = null;
    private long _timeStamp;

    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }

    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public Collection<SelfRelationFolder> getChildren() {
        return _children;
    }
    
    public void setChildren(final Collection<SelfRelationFolder> children) {
        _children = children;
    }
    
    public void addChild(final SelfRelationFolder child) {
        child.setParent(this);
        _children.add(child);
    }

    public SelfRelationFolder getParent() { return _parent; }
    public void setParent(final SelfRelationFolder parent) { _parent = parent; }

    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) { _timeStamp = timeStamp; }
}
