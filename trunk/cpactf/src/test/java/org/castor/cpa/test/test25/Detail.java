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
package org.castor.cpa.test.test25;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Ignore;

/**
 * Test object mapping to test_detaul used to conduct relation tests.
 */
@Ignore
public final class Detail {
    public static final String DEFAULT_VALUE = "group";

    private int _id;
    private String _value;
    private Master _master;
    private ArrayList<Detail2> _details2;
    private Detail3 _detail3;

    public Detail(final int id) {
        this();
        _id = id;
    }

    public Detail() {
        _value = DEFAULT_VALUE;
        _details2 = new ArrayList<Detail2>();
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setValue1(final String value) { _value = value; }
    public String getValue1() { return _value; }

    public void setMaster(final Master master) { _master = master; }
    public Master getMaster() { return _master; }

    public Detail2 createDetail2() { return new Detail2(); }
    public void setDetails2(final ArrayList<Detail2> list) { _details2 = list; }
    public ArrayList<Detail2> getDetails2() { return _details2; }
    public void addDetail2(final Detail2 detail2) {
        _details2.add(detail2);
        detail2.setDetail(this);
    }
    public Detail2 findDetail2(final int id) {
        if (_details2 == null) { return null; }

        Iterator<Detail2>enumeration = _details2.iterator();
        while (enumeration.hasNext()) {
            Detail2 detail2 = enumeration.next();
            if (detail2.getId() == id) { return detail2; }
        }
        return null;
    }

    public void setDetail3(final Detail3 detail3) {
        if (_detail3 != null) { _detail3.setDetail(null); }
        if (detail3 != null) { detail3.setDetail(this); }
        _detail3 = detail3;
    }
    public Detail3 getDetail3() { return _detail3; }

    public String toString() {
        String details2 = "";

        if (_details2 != null) {
            for (int i = 0; i < _details2.size(); ++i) {
                if (i > 0) { details2 = details2 + ", "; }
                details2 = details2 + _details2.get(i).toString();
            }
        }
        return "<detail: " + _id + " / " + _value
                + " / " + (_master == null ? 0 : _master.getId())
                + " / { " + details2 + " }>";
    }

    public int hashCode() { return _id; }

    public boolean equals(final Object other) {
        if (other == this) { return true; }
        if ((other != null) && (other instanceof Detail)) {
            return (((Detail) other)._id == _id);
        }
        return false;
    }
}
