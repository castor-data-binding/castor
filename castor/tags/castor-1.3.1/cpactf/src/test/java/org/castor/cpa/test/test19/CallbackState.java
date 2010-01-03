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
package org.castor.cpa.test.test19;

import org.junit.Ignore;

@Ignore
public final class CallbackState {
    public static final int LOADED      = 0;        
    public static final int STORING     = 1;
    public static final int CREATING    = 2;    
    public static final int CREATED     = 3;   
    public static final int REMOVING    = 4;   
    public static final int REMOVED     = 5;
    public static final int RELEASING   = 6;   
    public static final int USING       = 7;  
    public static final int UPDATED     = 8; 
    public static final int INSTANTIATE = 9;

    private static final short DONT_CARE = 0;       
    private static final short ALLOW     = 1;       
    private static final short DISALLOW  = 2;       

    private short[] _calls = new short[10];

    public CallbackState() {
        init();
    }

    public void init() {
        for (int i = 0; i < _calls.length; i++) {
            _calls[i] = DISALLOW;
        }
    }
    
    public void dontCare(final int i) {
        if ((i < 0) || (i > _calls.length - 1)) { return; }
        _calls[i] = DONT_CARE;
    }
    
    public void allow(final int i) {
        if ((i < 0) || (i > _calls.length - 1)) { return; }
        _calls[i] = ALLOW;
    }

    public void disallow(final int i) {
        if ((i < 0) || (i > _calls.length - 1)) { return; }
        _calls[i] = DISALLOW;
    }

    public boolean equals(final Object object) {
        if (!(object instanceof CallbackState)) { return false; }

        CallbackState other = (CallbackState) object;
        for (int i = 0; i < _calls.length; i++) {
            if ((_calls[i] != DONT_CARE) && (other._calls[i] != DONT_CARE)
                && (_calls[i] != other._calls[i])) { return false; }
        }

        return true;
    }
    
    public int hashCode() {
        return _calls.hashCode();
    }

    public String toString() {
        String str = "[";
        for (int i = 0; i < _calls.length; i++) {
            switch (_calls[i]) {
            case DONT_CARE:
                str += "*";
                break;
            case ALLOW:
                str += "1";
                break;
            case DISALLOW:
                str += "0";
                break;
            default:
                break;
            }
        }
        str += "]";
        return str;
    }
}
