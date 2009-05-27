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
package org.castor.cpa.test.test26;

import java.util.ArrayList;
import java.util.Iterator;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

/**
 * Test object mapping to test_master used to conduct relation tests.
 */
@Ignore
public final class MasterKeyGen implements TimeStampable {
    public static final int DEFAULT_ID = 3;
    public static final String DEFAULT_VALUE = "master";

    private int _id;
    private String _value;
    private Group _group;
    private ArrayList<DetailKeyGen> _details;
    private long _timeStamp;

    public MasterKeyGen() {
        _id = DEFAULT_ID;
        _value = DEFAULT_VALUE;
        _group = null;
        _details = new ArrayList<DetailKeyGen>();
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setValue1(final String value) { _value = value; }
    public String getValue1() { return _value; }

    public Group createGroup() { return new Group(); }
    public void setGroup(final Group group) { _group = group; }
    public Group getGroup() { return _group; }

    public DetailKeyGen createDetail() { return new DetailKeyGen(); }
    public ArrayList<DetailKeyGen> getDetails() { return _details; }
    public void setDetails(final ArrayList<DetailKeyGen> array) { _details = array; }
    public void addDetail(final DetailKeyGen detail) {
        _details.add(detail);
        detail.setMaster(this);
    }
    public DetailKeyGen findDetail(final int id) {
        Iterator<DetailKeyGen> enumeration = _details.iterator();
        while (enumeration.hasNext()) {
            DetailKeyGen detail = enumeration.next();
            if (detail.getId() == id) { return detail; }
        }
        return null;
    }

    public String toString() {
        String details = "";

        for (int i = 0; i < _details.size(); ++i) {
            if (i > 0) { details = details + ", "; }
            details = details + _details.get(i).toString();
        }
        return _id + " / " + _value + " (" + _group + ") { " + details + " }";
    }

    public void jdoSetTimeStamp(final long timeStamp) { _timeStamp = timeStamp; }
    public long jdoGetTimeStamp() { return _timeStamp; }
}
