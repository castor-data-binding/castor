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
package org.exolab.castor.jdo.engine;

import java.io.Reader;
import java.io.InputStream;
import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * This is an implementation of java.sql.Clob interface that is constructed
 * from java.io.Reader, in needs information about the length of the stream
 * (which is not provided by java.io.Reader interface).
 *
 * It is useful for setting CLOB values in the database.
 * @author <a href="mailto:on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$
 */
public class ClobImpl implements Clob {

    private final Reader _reader;

    private final long _length;

    /**
     * Examples:<br>
     * new ClobImpl(new StringReader(str), str.length())<br>
     * new ClobImpl(new FileReader(file), file.length())
     */
    public ClobImpl(Reader reader, long length) {
        _reader = reader;
        _length = length;
    }

    public InputStream getAsciiStream() {
        return new InputStream() {
            public int read() throws IOException {
                return _reader.read();
            }

            public int available() {
                return (int) Math.min(_length, Integer.MAX_VALUE);
            }

            public void mark(int readlimit) {
                try {
                    _reader.mark(readlimit);
                } catch (IOException ex) {
                    throw new UnsupportedOperationException(ex.toString());
                }
            }

            public boolean markSupported() {
                return _reader.markSupported();
            }

            public void reset() throws IOException {
                _reader.reset();
            }

            public long skip(long n) throws IOException {
                return _reader.skip(n);
            }
            public void close() throws IOException {
                _reader.close();
            }

        };
    }

    public Reader getCharacterStream() {
        return _reader;
    }

    public long length() {
        return _length;
    }

    public String getSubString(long pos, int length) throws SQLException {
        char[] buf = new char[length];

        try {
            _reader.reset();
            _reader.skip(pos);
            _reader.read(buf);
        } catch (IOException ex) {
            throw new SQLException(ex.toString());
        }
        return new String(buf);
    }

    /**
     * Not implemented, I guess it is not needed for writing CLOB
     */
    public long position(Clob searchstr, long start) {
        return 0;
    }

    /**
     * Not implemented, I guess it is not needed for writing CLOB
     */
    public long position(String searchstr, long start) {
        return 0;
    }

    /**
     * Not implemented.   Added to make ClobImpl complient with
     * JDBC 3.0, which is apart of JDK1.4 <p>
     * @author <a href="mailto:adam_e@swbell.net">Adam Esterline</a>
     */
    public OutputStream setAsciiStream(long pos)
                                throws SQLException
    {
        return null;
    }

    /**
     * Not implemented.   Added to make ClobImpl complient with
     * JDBC 3.0, which is apart of JDK1.4 <p>
     * @author <a href="mailto:adam_e@swbell.net">Adam Esterline</a>
     */
    public Writer setCharacterStream(long pos)
                              throws SQLException
    {
        return null;
    }

    /**
     * Not implemented.   Added to make ClobImpl complient with
     * JDBC 3.0, which is apart of JDK1.4 <p>
     * @author <a href="mailto:adam_e@swbell.net">Adam Esterline</a>
     */
    public int setString(long pos, String str) throws SQLException
    {
        return -1;
    }

    /**
     * Not implemented.   Added to make ClobImpl complient with
     * JDBC 3.0, which is apart of JDK1.4 <p>
     * @author <a href="mailto:adam_e@swbell.net">Adam Esterline</a>
     */
    public int setString(long pos, String str, int offset,
             int len) throws SQLException
    {
        return -1;
    }

    /**
     * Not implemented.   Added to make ClobImpl complient with
     * JDBC 3.0, which is apart of JDK1.4 <p>
     * @author <a href="mailto:adam_e@swbell.net">Adam Esterline</a>
     */
    public void truncate(long len) throws SQLException
    {
    }

}
