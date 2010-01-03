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

import org.junit.Ignore;

/**
 * Test object mapping to test_detaul used to conduct relation tests.
 */
@Ignore
public final class DetailKeyGen {
    public static final int DEFAULT_ID = 5;
    public static final String DEFAULT_VALUE = "group";

    private int _id;
    private String _value;
    private MasterKeyGen _master;
    private ArrayList<DetailKeyGen2> _details2;
    private DetailKeyGen3 _detail3;

    public DetailKeyGen(final int id) {
        this();
        _id = id;
    }

    public DetailKeyGen() {
        _value = DEFAULT_VALUE;
        _details2 = new ArrayList<DetailKeyGen2>();
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setValue1(final String value) { _value = value; }
    public String getValue1() { return _value; }

    public void setMaster(final MasterKeyGen master) { _master = master; }
    public MasterKeyGen getMaster() { return _master; }

    public DetailKeyGen2 createDetail2() { return new DetailKeyGen2(); }
    public void setDetails2(final ArrayList<DetailKeyGen2> list) { _details2 = list; }
    public ArrayList<DetailKeyGen2> getDetails2() { return _details2; }
    public void addDetail2(final DetailKeyGen2 detail2) {
        _details2.add(detail2);
        detail2.setDetail(this);
    }
    public DetailKeyGen2 findDetail2(final int id) {
        if (_details2 == null) { return null; }

        Iterator<DetailKeyGen2> enumeration = _details2.iterator();
        while (enumeration.hasNext()) {
            DetailKeyGen2 detail2 = enumeration.next();
            if (detail2.getId() == id) { return detail2; }
        }
        return null;
    }

    public void setDetail3(final DetailKeyGen3 detail3) {
        if (_detail3 != null) { _detail3.setDetail(null); }
        if (detail3 != null) { detail3.setDetail(this); }
        _detail3 = detail3;
    }
    public DetailKeyGen3 getDetail3() { return _detail3; }

    public String toString() {
        String details2 = "";

        if (_details2 != null) {
            for (int i = 0; i < _details2.size(); ++i) {
                if (i > 0) { details2 = details2 + ", "; }
                details2 = details2 + _details2.get(i).toString();
            }
        }
        
        return "<detail: " + _id + " / " + _value + " / "
                + (_master == null ? 0 : _master.getId()) + " / { " + details2
                + " }" + "/" + _detail3 + ">";
    }

    public int hashCode() { return _id; }

    public boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other != null && other instanceof DetailKeyGen) {
            return (((DetailKeyGen) other)._id == _id);
        }
        return false;
    }
}
