/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 */
package ctf.jdo.tc8x;

public class LazyAddress {
    private int _id;
    private String _street;
    private String _city;
    private String _state;
    private String _zip;
    private LazyPerson _person;

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }
    
    public void setStreet(final String street) { _street = street; }
    public String getStreet() { return _street; }
    
    public void setCity(final String city) { _city = city; }
    public String getCity() { return _city; }
    
    public void setState(final String state) { _state = state; }
    public String getState() { return _state; }
    
    public void setZip(final String zip) { _zip = zip; }
    public String getZip() { return _zip; }
    
    public void setPerson(final LazyPerson person) { _person = person; }
    public LazyPerson getPerson() { return _person; }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        sb.append(_id);
        sb.append(": ");
        sb.append(_street);
        sb.append("  ");
        sb.append(_city);
        sb.append("  ");
        sb.append(_state);
        sb.append("  ");
        sb.append(_zip);
        sb.append(" of ");
        if (_person != null) {
            sb.append(_person.getLastName());
            sb.append(", ");
            sb.append(_person.getFirstName());
        } else {
            sb.append("--nobody--");
        }
        sb.append(">");
        return sb.toString();
    }
    
    public int hashCode() {
        return _id;
    }
    
    public boolean equals(final Object o) {
        if (this == o) { return true; }

        if (o instanceof LazyAddress) {
            LazyAddress other = (LazyAddress) o;
            return (other._id == _id);
        }
        return false;
    }
}
