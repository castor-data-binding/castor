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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package jdo;


import java.util.Iterator;

/**
 * Test object for different collection types.
 */
public class TestColArray extends TestCol {

    private TestItem[] _item;

    public TestColArray() {
        super();
    }

    public boolean containsItem( TestItem item ) {
        if ( _item == null || _item.length == 0 )
            return false;
        for (int i=0;i<_item.length;i++) {
            if (_item[i].equals(item)) { return true; }
        }

        return false;
    }

    public Iterator itemIterator() {
        if ( _item == null || _item.length == 0 )
            return _emptyItor;

        return new ArrayIterator();
    }

    public void removeItem( TestItem item ) {
        int position = -1;
        for (int i=0;i<_item.length;i++) {
            if (_item[i].equals(item)) { position = i; }
        }
        if (position >=0) {
          TestItem[] aux = new TestItem[_item.length-1];
          for (int i=0;i<_item.length;i++) {
              if (i < position) { aux[i] = _item[i]; }
              if (i > position) { aux[i-1] = _item[i]; }
          }
          _item = aux;
        }        
    }

    public int itemSize() {
        if ( _item == null )
            return 0;

        return _item.length;
    }

    public void setItems( TestItem[] item ) {
        _item = item;
    }

    public TestItem[] getItems() {
        return _item;
    }

    private class ArrayIterator implements Iterator {
        private int position = 0;

        public boolean hasNext() {
            return position<_item.length;
        }
        public Object next() {
            return _item[position++];
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
