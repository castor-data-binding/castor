/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test73;

import java.util.Collection;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public final class ManyPerson implements TimeStampable {

    private int _id;
    private String _value;
    private String _helloworld;
    private String _sthelse;
    private Collection < ManyGroup > _group;
    private long _timeStamp;

    public int getId() {
        return _id;
    }

    public void setId(final int id) {
        _id = id;
    }

    public String getValue1() {
        return _value;
    }

    public void setValue1(final String value) {
        _value = value;
    }

    public String getHelloworld() {
        return _helloworld;
    }

    public void setHelloworld(final String s) {
        _helloworld = s;
    }

    public String getSthelse() {
        return _sthelse;
    }

    public void setSthelse(final String s) {
        _sthelse = s;
    }

    public Collection < ManyGroup > getGroup() {
        return _group;
    }

    public void setGroup(final Collection < ManyGroup > group) {
        _group = group;
    }

    public long jdoGetTimeStamp() {
        return _timeStamp;
    }

    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }

    public String toString() {
        return _id + " / " + _value;
    }
}
