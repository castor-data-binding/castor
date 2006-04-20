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


package jdo;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.StringReader;
import java.util.Date;
import java.util.Enumeration;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.engine.ClobImpl;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 */
public class TypeHandling
    extends CWTestCase
{


    private JDOCategory    _category;


    public TypeHandling( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC20", "Type handling" );
        _category = (JDOCategory) category;
    }


    public void preExecute()
    {
        super.preExecute();
    }


    public void postExecute()
    {
        super.postExecute();
    }


    public boolean run( CWVerboseStream stream )
    {
        boolean result = true;
        Database db;
        final byte[] BLOB_VALUE = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        final String CLOB_VALUE = "0123456789";
        int len;
        byte[] bbuf = new byte[BLOB_VALUE.length + 1];
        char[] cbuf = new char[CLOB_VALUE.length() + 1];

        try {
            OQLQuery      oql;
            TestTypes     types;
            Enumeration   enum;
            Date          date;
            Date          time;
            Date          timestamp;
            SimpleDateFormat df;


            // Open transaction in order to perform JDO operations
            db = _category.getDatabase( stream.verbose() );

            // Determine if test object exists, if not create it.
            // If it exists, set the name to some predefined value
            // that this test will later override.
            db.begin();
            oql = db.getOQLQuery( "SELECT types FROM jdo.TestTypes types WHERE id = $(integer)1" );
            // This one tests that bind performs type conversion
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                stream.writeVerbose( "Updating object: " + types );
            } else {
                types = new TestTypes();
                stream.writeVerbose( "Creating new object: " + types );
                db.create( types );
            }
            db.commit();


            stream.writeVerbose( "Testing date/time conversion" );
            db.begin();
            oql = db.getOQLQuery( "SELECT types FROM jdo.TestTypes types WHERE id = $1" );
            // This one tests that bind performs type conversion
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                stream.writeVerbose( "Date type: " + types.getDate().getClass() );
                stream.writeVerbose( "Time type: " + types.getTime().getClass() );
                stream.writeVerbose( "Deleting object: " + types );
                db.remove( types );
            } else {
                stream.writeVerbose( "Error: Could not load types object" );
                result = false;
            }
            db.commit();
            db.begin();
            types = new TestTypes();
            stream.writeVerbose( "Creating new object: " + types );
            db.create( types );
            db.commit();
            stream.writeVerbose( "OK: Handled date/time types" );


            stream.writeVerbose( "Testing null in integer and long fields" );
            db.begin();
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                types.setIntValue( 5 );
                types.deleteIntValue();
                types.setLongValue( null );
            }
            db.commit();
            db.begin();
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                if ( types.getIntValue() != 0 || types.hasIntValue() ) {
                    stream.writeVerbose( "Error: null integer value was not set" );
                    result = false;
                }
                if ( types.getLongValue() != null ) {
                    stream.writeVerbose( "Error: null long value was not set" );
                    result = false;
                }
                types.setIntValue( 5 );
                types.setLongValue( new Long( 5 ) );
            } else {
                stream.writeVerbose( "Error: failed to load object" );
                result = false;
            }
            db.commit();
            db.begin();
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                if ( types.getIntValue() != 5 || ! types.hasIntValue() ) {
                    stream.writeVerbose( "Error: non-null integer value was not set" );
                    result = false;
                }
                if ( ! types.getLongValue().equals( new Long( 5 ) ) ) {
                    stream.writeVerbose( "Error: non-null long value was not set" );
                    result = false;
                }
            } else {
                stream.writeVerbose( "Error: failed to load object" );
                result = false;
            }
            db.commit();
            if ( ! result )
                return false;
            else
                stream.writeVerbose( "OK: null in integer and long field passed" );


            stream.writeVerbose( "Testing value in char field" );
            db.begin();
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                types.setCharValue( 'A' );
            }
            db.commit();
            db.begin();
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                if ( types.getCharValue() != 'A' ) {
                    stream.writeVerbose( "Error: char value was not set" );
                    result = false;
                }
            } else {
                stream.writeVerbose( "Error: failed to load object" );
                result = false;
            }
            db.commit();
            if ( ! result )
                return false;
            else
                stream.writeVerbose( "OK: value in character field passed" );


            stream.writeVerbose( "Testing the boolean->char[01] conversion" );
            db.begin();
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                types.setBoolValue( true );
            }
            db.commit();
            db.begin();
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                if ( types.getBoolValue() != true ) {
                    stream.writeVerbose( "Error: bool value was not set" );
                    result = false;
                }
            } else {
                stream.writeVerbose( "Error: failed to load object" );
                result = false;
            }
            db.commit();
            if ( ! result )
                return false;
            else
                stream.writeVerbose( "OK: The boolean->char[01] conversion passed" );


            stream.writeVerbose( "Testing BLOB and CLOB fields" );
            db.begin();
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                types.setBlob( BLOB_VALUE );
                types.setClob( CLOB_VALUE );
                types.setBlob2( new ByteArrayInputStream( BLOB_VALUE ) );
                types.setClob2( new ClobImpl( new StringReader( CLOB_VALUE ), CLOB_VALUE.length() ) );
            }
            db.commit();
            db.begin();
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();

                if ( types.getBlob() == null || ! (new String(types.getBlob())).equals(new String(BLOB_VALUE)) ) {
                    stream.writeVerbose( "Error: BLOB value was not set" );
                    result = false;
                }
                if ( ! CLOB_VALUE.equals(types.getClob()) ) {
                    stream.writeVerbose( "Error: CLOB value was not set" );
                    result = false;
                }
                if ( types.getBlob2() == null ) {
                    stream.writeVerbose( "Error: InputStream value was not set" );
                    result = false;
                } else {
                    len = types.getBlob2().read(bbuf);
                    if ( ! (new String(bbuf, 0, len)).equals(new String(BLOB_VALUE)) ) {
                        stream.writeVerbose( "Error: InputStream value is wrong" );
                        result = false;
                    }
                }
                if ( types.getClob2() == null ) {
                    stream.writeVerbose( "Error: Clob value was not set" );
                    result = false;
                } else {
                    long clobLen = types.getClob2().length();
                    len = types.getClob2().getCharacterStream().read(cbuf);
                    if ( clobLen != CLOB_VALUE.length() ||
                            ! (new String(cbuf, 0, len)).equals(CLOB_VALUE) ) {
                        stream.writeVerbose( "Error: Clob value is wrong" );
                        result = false;
                    }
                }
            } else {
                stream.writeVerbose( "Error: failed to load object" );
                result = false;
            }
            db.commit();
            if ( ! result )
                return false;
            else
                stream.writeVerbose( "OK: BLOB and CLOB fields passed" );


            stream.writeVerbose( "Testing date->int/numeric/char parameterized conversion" );
            df = new SimpleDateFormat();
            df.applyPattern("yyyy/MM/dd");
            date = df.parse("2000/05/27");
            df.applyPattern("HH:mm:ss.SSS");
            time = df.parse("02:16:01.234");
            df.applyPattern("yyyy/MM/dd HH:mm:ss.SSS");
            timestamp = df.parse("2000/05/27 02:16:01.234");
            db.begin();
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                types.setDate2( date );
                types.setTime2( time );
                types.setTimestamp2( timestamp );
            }
            db.commit();
            db.begin();
            oql.bind( TestTypes.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                types = (TestTypes) enum.nextElement();
                if ( !date.equals( types.getDate2() ) ) {
                    stream.writeVerbose( "Error: date/int value was not set" );
                    result = false;
                }
                if ( !time.equals( types.getTime2() ) ) {
                    stream.writeVerbose( "Error: time/string value was not set" );
                    result = false;
                }
                if ( !timestamp.equals( types.getTimestamp2() ) ) {
                    stream.writeVerbose( "Error: timestamp/numeric value was not set" );
                    result = false;
                }
            } else {
                stream.writeVerbose( "Error: failed to load object" );
                result = false;
            }
            db.commit();
            if ( ! result )
                return false;
            else
                stream.writeVerbose( "OK: date->int/numeric/char conversion passed" );


            db.close();
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }


}

