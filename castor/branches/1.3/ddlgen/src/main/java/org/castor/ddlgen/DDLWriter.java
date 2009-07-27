/*
 * Copyright 2007 Le Duc Bao, Ralf Joachim
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
package org.castor.ddlgen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.MessageFormat;

/**
 * Replace PrintStream and StringBuffer by a Writer implementation
 * We have various properties to configure output that are in-depended of the schema object:
 * <li/>org.castor.ddlgen.CharFormat=SENSITIVE, UPPER and LOWER
 * <li/>org.castor.ddlgen.Newline=\n
 * <li/>org.castor.ddlgen.Indention=\t
 * 
 * These properties are accessed at various places all around ddlgen at the moment.The idea 
 * is that these properties are set only once at the new Writer and do not need to be 
 * accessed elsewhere. This has the following advantages:
 * <li/>improved performance as the properties don't need to be accessed for every object to output
 * <li/>functionallity to format genertaed ddl is concentrated in one class: the new Writer
 * <li/>all the toDDL(), toDropDDL(), toCreateDDL() methods get much shorter
 * 
 * I thought of the following interface for the new Writer (not complete):
 * <li/>write(String) outputs String as is
 * <li/>writeln(String) calls write(String) followed by newline()
 * <li/>newline() output newline and indention of next line
 * <li/>indent() increases indention
 * <li/>unindent() decreases indention
 * 
 * More write() and writeln() methods for other data types may be added on demand. A further 
 * improvement could be to offer write(String, Object[])  methods that internally use 
 * MessageFormat. This would enable us to use a pattern based approach for DDL generation. 
 * These patterns may sometimes be much easier to read and maintain.
 * 
 * In addition to the introduction of the new Writer it will be required to pass an instance 
 * of the Writer to every method where DDL gets generated. Therefore the parameterless 
 * toCreate() method have to be changed to toCreateDDL(DDLWriter). This also applies to other 
 * such methods.
 * 
 * @author <a href="mailto:leducbao@gmail.com">Le Duc Bao</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1.2
 */
public final class DDLWriter extends Writer {
    //--------------------------------------------------------------------------

    /** Print writer to write all output to. */
    private Writer _writer;
    
    /** Remember errors. */
    private IOException _error = null;

    /** Newline flag is turn on at a new line before any other character gets written. */
    private boolean _isNewline = true;

    /** Current indent level. */
    private int _indentLevel = 0;

    /** String to output to indent a line. */
    private String _indentText = DDLGenConfiguration.DEFAULT_INDENT;

    /** String to output for a new line. */
    private String _newline = DDLGenConfiguration.DEFAULT_NEWLINE;
    
    /** Character format defined by CHAR_FORMAT_KEY in configuration file. */
    private String _chrFormat = DDLGenConfiguration.CHAR_FORMAT_SENSITIVE;
    
    //--------------------------------------------------------------------------

    /**
     * Construct new DDLWriter with given output stream and configuration file.
     * 
     * @param output Output stream to write output characters to.
     * @param conf Configuration.          
     */
    public DDLWriter(final OutputStream output, final Configuration conf) {
        this(new BufferedWriter(new OutputStreamWriter(output)), conf);
    }

    /**
     * Construct new DDLWriter with given writer and configuration file.
     * 
     * @param writer Writer to write output characters to.
     * @param conf Configuration.          
     */
    public DDLWriter(final Writer writer, final Configuration conf) {
        super(writer);

        _writer = writer;

        _newline = conf.getStringValue(
                DDLGenConfiguration.NEWLINE_KEY,
                DDLGenConfiguration.DEFAULT_NEWLINE);
        _indentText = conf.getStringValue(
                DDLGenConfiguration.INDENT_KEY,
                DDLGenConfiguration.DEFAULT_INDENT);        
        _chrFormat = conf.getStringValue(
                DDLGenConfiguration.CHAR_FORMAT_KEY,
                DDLGenConfiguration.CHAR_FORMAT_SENSITIVE);
    }

    /**
     * Flush the writer.
     */
    public void flush() {
        try {
            synchronized (lock) {
                if (_writer == null) { throw new IOException("Writer closed."); }
                _writer.flush();
            }
        } catch (IOException ex) {
            _error = ex;
        }
    }

    /**
     * Close the writer.
     */
    public void close () {
        try {
            synchronized (lock) {
                if (_writer != null) {
                    _writer.close();
                    _writer = null;
                }
            }
        } catch (IOException ex) {
            _error = ex;
        }
    }
    
    /**
     * Check if any error occured at previous operations of the writer. If an IOException was
     * caught at any previous operation of the writer it will be thrown now.
     * 
     * @throws IOException IOException caught at any previous operation of the writer.
     */
    public void checkError() throws IOException {
        if (_error != null) { throw _error; }
    }

    //--------------------------------------------------------------------------

    /**
     * Increase indention by 1.
     */
    public void indent() {
        _indentLevel++;
    }

    /**
     * Decrease indention by 1.
     */
    public void unindent() {
        _indentLevel = (_indentLevel <= 0) ? 0 : _indentLevel - 1;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void write(final char[] buf, final int off, final int len) {
        write(new String(buf, off, len));
    }

    /**
     * {@inheritDoc}
     */
    public void write(final char[] buf) {
        write(new String(buf));
    }
    
    /**
     * {@inheritDoc}
     */
    public void write(final int c) {
        write(new String(new char[] {(char) c}));
    }
    
    /**
     * {@inheritDoc}
     */
    public void write(final String s, final int off, final int len) {
        write(s.substring(off, off + len));
    }
    
    /**
     * {@inheritDoc}
     */
    public void write(final String s) {
        if (s != null) {
            try {
                synchronized (lock) {
                    if (_writer == null) { throw new IOException("Writer closed."); }
                    
                    if (DDLGenConfiguration.CHAR_FORMAT_LOWER.equalsIgnoreCase(_chrFormat)) {
                        _writer.write(s.toLowerCase());
                    } else if (DDLGenConfiguration.CHAR_FORMAT_UPPER.equalsIgnoreCase(_chrFormat)) {
                        _writer.write(s.toUpperCase());
                    } else {
                        _writer.write(s);
                    }
                }
            } catch (InterruptedIOException ex) {
                Thread.currentThread().interrupt();
            } catch (IOException ex) {
                _error = ex;
            }
        }
    }
    
    /**
     * Write indention.
     */
    private void writeIndention() {
        try {
            synchronized (lock) {
                if (_writer == null) { throw new IOException("Writer closed."); }
                
                if (_isNewline) {
                    for (int i = 0; i < _indentLevel; i++) {
                        _writer.write(_indentText);
                    }
                    
                    _isNewline = false;
                }
            }
        } catch (InterruptedIOException ex) {
            Thread.currentThread().interrupt();
        } catch (IOException ex) {
            _error = ex;
        }
    }

    /**
     * Write newline.
     */
    private void writeNewline() {
        try {
            synchronized (lock) {
                if (_writer == null) { throw new IOException("Writer closed."); }
                
                _isNewline = true;
                
                _writer.write(_newline);
            }
        } catch (InterruptedIOException ex) {
            Thread.currentThread().interrupt();
        } catch (IOException ex) {
            _error = ex;
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Print an array of characters.
     * 
     * @param chars Array of chars to be printed.
     */
    public void print(final char[] chars) {
        synchronized (lock) {
            writeIndention();
            write(chars);
        }
    }

    /**
     * Print a double-precision floating-point number.
     * 
     * @param number Double to be printed.
     */
    public void print(final double number) {
        synchronized (lock) {
            writeIndention();
            write(String.valueOf(number));
        }
    }

    /**
     * Print an integer number.
     * 
     * @param number Integer to be printed.
     */
    public void print(final int number) {
        synchronized (lock) {
            writeIndention();
            write(String.valueOf(number));
        }
    }

    /**
     * Print a long number.
     * 
     * @param number Long to be printed.
     */
    public void print(final long number) {
        synchronized (lock) {
            writeIndention();
            write(String.valueOf(number));
        }
    }

    /**
     * Print an object.
     * 
     * @param object Object to be printed.
     */
    public void print(final Object object) {
        synchronized (lock) {
            writeIndention();
            write(String.valueOf(object));
        }
    }

    /**
     * Print a string.
     * 
     * @param string String to be printed.
     */
    public void print(final String string) {
        synchronized (lock) {
            writeIndention();
            write(string);
        }
    }
    
    /**
     * A convenience method to print a formatted string build by filling placeholders of the
     * specified pattern with given arguments.
     * 
     * @param pattern Pattern with placeholders.
     * @param arguments Arguments to replace placeholders in pattern.
     */
    public void print(final String pattern, final Object[] arguments) {
        synchronized (lock) {
            writeIndention();
            write(MessageFormat.format(pattern, arguments));
        }
    }

    //--------------------------------------------------------------------------

    /**
     *  Terminate the current line by writing the line separator string.
     */
    public void println() {
        writeNewline();
    }

    /**
     * Print an array of characters and terminate the line.
     * 
     * @param chars Array of chars to be printed.
     */
    public void println(final char[] chars) {
        synchronized (lock) {
            writeIndention();
            write(chars);
            writeNewline();
        }
    }

    /**
     * Print a double-precision floating-point number and terminate the line.
     * 
     * @param number Double to be printed.
     */
    public void println(final double number) {
        synchronized (lock) {
            writeIndention();
            write(String.valueOf(number));
            writeNewline();
        }
    }

    /**
     * Print an integer number and terminate the line.
     * 
     * @param number Integer to be printed.
     */
    public void println(final int number) {
        synchronized (lock) {
            writeIndention();
            write(String.valueOf(number));
            writeNewline();
        }
    }

    /**
     * Print a long number and terminate the line.
     * 
     * @param number Long to be printed.
     */
    public void println(final long number) {
        synchronized (lock) {
            writeIndention();
            write(String.valueOf(number));
            writeNewline();
        }
    }

    /**
     * Print an object and terminate the line.
     * 
     * @param object Object to be printed.
     */
    public void println(final Object object) {
        synchronized (lock) {
            writeIndention();
            write(String.valueOf(object));
            writeNewline();
        }
    }

    /**
     * Print a string and terminate the line.
     * 
     * @param string String to be printed.
     */
    public void println(final String string) {
        synchronized (lock) {
            writeIndention();
            write(string);
            writeNewline();
        }
    }
    
    /**
     * A convenience method to print a formatted string build by filling placeholders of the
     * specified pattern with given arguments. Line will be terminated after the formatted string.
     * 
     * @param pattern Pattern with placeholders.
     * @param arguments Arguments to replace placeholders in pattern.
     */
    public void println(final String pattern, final Object[] arguments) {
        synchronized (lock) {
            writeIndention();
            write(MessageFormat.format(pattern, arguments));
            writeNewline();
        }
    }

    //--------------------------------------------------------------------------
}
