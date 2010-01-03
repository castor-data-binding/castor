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
package org.castor.cpaptf.rel1toN;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision:6817 $ $Date: 2005-06-24 19:41:08 -0600 (Fri, 24 Jun 2005) $
 */
public final class Locked {
    //-------------------------------------------------------------------------
    
    private Integer _id;
    private String _name;
    private boolean _in;
    private boolean _out;
    private Collection < State >  _states = new ArrayList < State > ();

    //-------------------------------------------------------------------------
    
    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }
    
    public boolean getIn() { return _in; }
    public void setIn(final boolean in) { _in = in; }
    
    public boolean getOut() { return _out; }
    public void setOut(final boolean out) { _out = out; }
    
    public Collection < State > getStates() { return _states; }
    public void setStates(final Collection < State > states) {
        _states = states;
    }
    public void addState(final State state) {
        if ((state != null) && (!_states.contains(state))) {
            _states.add(state);
            state.setLocked(this);
        }
    }
    public void removeState(final State state) {
        if ((state != null) && (_states.contains(state))) {
            _states.remove(state);
            state.setLocked(null);
        }
    }
    
    //-------------------------------------------------------------------------
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("<Locked id='"); sb.append(_id);
        sb.append("' name='"); sb.append(_name);
        sb.append("' in='"); sb.append(_in);
        sb.append("' out='"); sb.append(_out);
        sb.append("'/>\n");
        
        return sb.toString();
    }
    
    //-------------------------------------------------------------------------
}
