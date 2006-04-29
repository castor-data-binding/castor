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
 *
 * $Id$
 */


package org.exolab.castor.persist.spi;

/**
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 */
public class Complex {

    private final Object _first;

    private final Object _second;

    private final Object[] _all;

    private final int _size;

    private int _hashCode;

    public Complex( Object o ) {
        _size = 1;
        _first = o;
        _second = null;
        _all = null;
        _hashCode = _first.hashCode();
    }

    public Complex( int length, Object[] o ) {
        _size = length;
        if ( length == 1 ) {
            _first = o[0];
            _second = null;
            _all = null;
            _hashCode = _first==null?0:_first.hashCode();
        } else if ( length == 2 ) {
            _first = o[0];
            _second = o[1];
            _all = null;
            _hashCode = _first==null?0:_first.hashCode();
            _hashCode += _second==null?0:_second.hashCode();
        } else if ( length <= o.length && length > 0 ) {
            _first = null;
            _second = null;
            _all = new Object[_size];
            System.arraycopy( o, 0, _all, 0, _size );
            _hashCode = 0;
            for ( int i=0; i < _size; i++ ) {
                _hashCode += (_all[i]==null?0:_all[i].hashCode());
            }
        } else
            throw new IllegalArgumentException("Invalid Complex object");
    }

    public Complex( Object o1, Object o2 ) {
        _size = 2;
        _first = o1;
        _second = o2;
        _all = null;
        _hashCode = _first==null?0:_first.hashCode();
        _hashCode += _second==null?0:_second.hashCode();
    }

    public Complex( Object[] o ) {
        // try to avoid creation of another object
        if ( o == null || o.length == 0 )
            throw new NullPointerException();

        if ( o.length == 1  ) {
            _size = 1;
            _first = o[0];
            _second = null;
            _all = null;
            _hashCode = _first.hashCode();
        } else if ( o.length == 2 ) {
            _size = 2;
            _first = o[0];
            _second = o[1];
            _all = null;
            _hashCode = _first.hashCode();
            _hashCode += _second==null?0:_second.hashCode();
        } else {
            _size = o.length;
            _first = null;
            _second = null;
            _all = new Object[_size];
            System.arraycopy( o, 0, _all, 0, _size );
            _hashCode = 0;
            for ( int i=0; i < _size; i++ ) {
                _hashCode += (_all[i]==null?0:_all[i].hashCode());
            }
        }
    }

    public Complex( Complex complex ) {
        // try to avoid creation of another object
        if ( complex == null || complex.size() == 0 )
            throw new NullPointerException();

        int dim = complex.size();

        if ( dim == 1  ) {
            _size = 1;
            _first = complex.get(0);
            _second = null;
            _all = null;
            _hashCode = _first.hashCode();
        } else if ( dim == 2 ) {
            _first = complex.get(0);
            _second = complex.get(1);
            _all = null;
            _size = 2;
            _hashCode = _first.hashCode();
            _hashCode += _second==null?0:_second.hashCode();
        } else {
            _size = dim;
            _first = null;
            _second = null;
            _all = new Object[_size];
            System.arraycopy( complex, 0, _all, 0, _size );
            _hashCode = 0;
            for ( int i=0; i < _size; i++ ) {
                _hashCode += (_all[i]==null?0:_all[i].hashCode());
            }
        }
    }

    public boolean equals( Object other ) {
        if ( other == null ) {
            return false;
        }
        if ( !(other instanceof Complex) ) {
            return false;
        }

        Complex comp = (Complex) other;
        if ( comp.size() != _size ) {
            return false;
        }

        for ( int i=0; i < _size; i++ ) {
          if (get(i) == null) {
            if (comp.get(i) != null) { return false; }
          } else if (!get(i).equals(comp.get(i))) {
            return false;
          }
        }
        return true;
    }

    public int size() {
        return _size;
    }

    public boolean hasSomePartsNull() {

        for ( int i=0; i < _size; i++ ) {
            if ( get(i) == null )
                return true;
        }
        return false;
    }
    public int hashCode() {
        return _hashCode;
    }
    public Object get( int i ) {

        if ( i >= _size || i < 0 )
            throw new ArrayIndexOutOfBoundsException();
        if ( _size <= 2 )
          return i == 0  ? _first : _second;

        return _all[i];
    }
    
    /* 
     * Overrides Object.toString()
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        for( int i=0; i < _size; i++ ) {
        	if ( i != 0 ) 
        		sb.append(",");
        	Object obj = get(i);
            sb.append(obj);
            if (obj != null)
            	sb.append("(").append(obj.hashCode()).append(")");
            else
            	sb.append ("(N/A)");
        }
        sb.append(">");
        return sb.toString();
    }

}

