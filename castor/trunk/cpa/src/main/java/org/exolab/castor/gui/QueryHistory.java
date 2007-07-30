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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $$
 */
package org.exolab.castor.gui;

import java.util.Vector;

public class QueryHistory implements java.io.Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = 235997211123063614L;

    private  Vector _querys = new Vector();
    private int _currentPos = 0;
    private  int _maxsize = 20;

    public Vector getQuerys() {
        return _querys;

    }

    public void setQuerys(final Vector querys) {
        _querys = querys;
        _currentPos = _querys.size() - 1;
    }

    public int getMaxHistorySize() {
        return _maxsize;
    }

    public void setMaxHistorySize(final int newSize) {
         _maxsize = newSize;
    }

    public void moveback() {
        if (_currentPos > 0) {
            _currentPos--;
        }
    }

    public void moveforward() {
        if (_currentPos < _querys.size() - 1) {
            _currentPos++;
        }
    }

    public String getCurrentQuery() {
      if (_querys.isEmpty()) {
          return "";
      }
      return (String) _querys.elementAt(_currentPos);
    }

    public void addQuery(final String newQuery) {
        if (_querys.contains(newQuery)) {
            System.out.println("schon drin");
            return;
        }

        _querys.add(newQuery);

        if (_querys.size() > _maxsize) {
            _querys.removeElementAt(0);
        }

        _currentPos = _querys.size() - 1;
    }

    // Get to avoid mashaling
    public String getNextQuery() {
        this.moveforward();
        return this.getCurrentQuery();
    }

    public String getPreviousQuery() {
        this.moveback();
        return this.getCurrentQuery();
    }
}