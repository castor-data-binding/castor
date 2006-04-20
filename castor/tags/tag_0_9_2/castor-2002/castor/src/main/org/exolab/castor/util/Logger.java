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


package org.exolab.castor.util;


import java.io.OutputStream;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


/**
 * Simple logging facility. This logger extends <tt>PrintWriter</tt>
 * which is used to trace SQL statements, Castor operations and
 * mapping resolutions.
 * <p>
 * This logger augments <tt>PrintWriter</tt> by adding a prefix to
 * each printed line and optionally a time stamp, enabling easy
 * post-mortem analysis.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class Logger
    extends PrintWriter
{

    
    /**
     * The default prefix to show at the beginning of each line.
     */
    private static final String  Prefix = "Castor";


    /**
     * The default system logger.
     */
    private static PrintWriter  _system = new Logger( System.out ).setPrefix( Prefix );


    /**
     * True if the time should be printed on each line.
     */
    private boolean  _logTime = false;


    /**
     * The prefix to use for this logger, null if lines should not be prefixed.
     */
    private String  _prefix = null;


    /**
     * True if at the beginning of a line.
     */
    private boolean _newLine;


    /**
     * Constructs a new logger to use the specified output stream.
     */
    public Logger( OutputStream output )
    {
        super( output, true );
        _newLine = true;
    }


    /**
     * Constructs a new logger to use the specified writer.
     */
    public Logger( Writer writer )
    {
        super( writer, true );
        _newLine = true;
    }


    /**
     * Returns the default logger. This logger is used to produce
     * system messages.
     */
    public static PrintWriter getSystemLogger()
    {
        return _system;
    }


    /**
     * Sets the default logger. This logger is used to produce
     * system messages.
     */
    public static void setSystemLogger( PrintWriter system )
    {
        if ( system == null )
            throw new NullPointerException( "Argument 'system' is null" );
        _system = system;
    }


    /**
     * Sets the prefix, a short name to print at the beginning
     * of each log line. If a null is passed, no prefix precedes
     * logged lines.
     *
     * @param prefix The prefix to use for each line
     * @return This logger
     */
    public Logger setPrefix( String prefix )
    {
        if ( prefix != null && prefix.length() > 0 )
            _prefix = "[" + prefix + "] ";
        else
            _prefix = "";
        return this;
    }


    /**
     * Determines whether to print the time at the beggining of
     * each log line.
     * 
     * @param logTime True if time should appear at the beggining
     *   of each log line
     * @return This logger
     */
    public Logger setLogTime( boolean logTime )
    {
        _logTime = logTime;
        return this;
    }


    public void println()
    {
        super.println();
        _newLine = true;
    }


    public void println( boolean value )
    {
        prefixLine();
        super.print( value );
        super.println();
        _newLine = true;
    }


    public void print( boolean value )
    {
        prefixLine();
        super.print( value );
    }


    public void println( char value )
    {
        prefixLine();
        super.print( value );
        super.println();
        _newLine = true;
    }


    public void print( char value )
    {
        prefixLine();
        super.print( value );
        _newLine = ( value == '\n' );
    }


    public void println( int value )
    {
        prefixLine();
        super.print( value );
        super.println();
        _newLine = true;
        
    }


    public void print( int value )
    {
        prefixLine();
        super.print( value );
    }


    public void println( long value )
    {
        prefixLine();
        super.print( value );
        super.println();
        _newLine = true;
    }


    public void print( long value )
    {
        prefixLine();
        super.print( value );
    }


    public void println( float value )
    {
        prefixLine();
        super.print( value );
        super.println();
        _newLine = true;
    }


    public void print( float value )
    {
        prefixLine();
        super.print( value );
    }


    public void println( double value )
    {
        prefixLine();
        super.print( value );
        super.println();
        _newLine = true;
    }


    public void print( double value )
    {
        prefixLine();
        super.print( value );
    }


    public void println( char[] value )
    {
        print( value );
        super.println();
        _newLine = true;
    }


    public void print( char[] value )
    {
        int i;
        int offset;
        
        offset = 0;
        for ( i = 0 ; i < value.length ; ++i ) {
            if ( value[ i ] == '\n' ) {
                prefixLine();
                super.write( value, offset, i - offset );
                super.println();
                _newLine = true;
                offset = i + 1;
            }
        }
        if ( i > offset ) {
            prefixLine();
            super.write( value, offset, i - offset );
            _newLine = false;
        }
    }


    public void println( String value )
    {
        print( value );
        super.println();
        _newLine = true;
    }


    public void print( String value )
    {
        int i;
        int offset;
        
        offset = 0;
        for ( i = 0 ; i < value.length() ; ++i ) {
            if ( value.charAt( i ) == '\n' ) {
                prefixLine();
                super.write( value, offset, i - offset );
                super.println();
                _newLine = true;
                offset = i + 1;
            }
        }
        if ( i > offset ) {
            prefixLine();
            super.write( value, offset, i - offset );
            _newLine = false;
        }
    }


    public void println( Object value )
    {
        println( String.valueOf( value ) );
    }
    
    
    public void print( Object value )
    {
        prefixLine();
        println( String.valueOf( value ) );
    }


    /**
     * Called before printing from all of the print methods.
     * If at the beginning of a new line, the data/time and
     * prefix will be printed.
     */
    protected final void prefixLine()
    {
        if ( _newLine ) {
            if ( _logTime ) {
                write( new Date().toString() );
                write( ' ' );
            }
            if ( _prefix != null )
                write( _prefix );
        }
    }


}

