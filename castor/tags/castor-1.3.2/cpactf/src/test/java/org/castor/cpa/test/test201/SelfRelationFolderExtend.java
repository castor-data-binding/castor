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
import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public final class SelfRelationFolderExtend
extends SelfRelationFolderParent
implements Serializable, TimeStampable {
    private static final long serialVersionUID = -4455086340402957100L;

    private Collection<SelfRelationFolderExtend> _children =
        new ArrayList<SelfRelationFolderExtend>();
    
    private SelfRelationFolderExtend _parent = null;
  
    public Collection<SelfRelationFolderExtend> getChildren() {
        return _children;
    }
    
    public void setChildren(final Collection<SelfRelationFolderExtend> children) {
        _children = children;
    }
    
    public void addChild(final SelfRelationFolderExtend child) {
        child.setParent(this);
        _children.add(child);
    }
    
    public SelfRelationFolderExtend getParent() { return _parent; }
    public void setParent(final SelfRelationFolderExtend parent) { _parent = parent; }
}
