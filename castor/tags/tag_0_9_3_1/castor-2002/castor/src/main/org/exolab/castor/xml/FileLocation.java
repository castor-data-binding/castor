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

package org.exolab.castor.xml;

/**
 * A simple FileLocation class used for finer grained detail of 
 * exceptions
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class FileLocation implements Location {
    
    
    private final static String NOT_AVAILABLE = "[not available]";
    
    private String _filename = null;
    
    private int _line = -1;
    private int _col  = -1;
    
    /**
     * Creates a new FileLocation
    **/
    public FileLocation() {
        super();
    } //-- FileLocation()
    
    /**
     * Creates a new FileLocation
     * @param filename the name of the file
    **/
    public FileLocation(String filename) {
        super();
        this._filename = filename;
    } //-- FileLocation(String)

    /**
     * Creates a new FileLocation
     * @param line the line number 
     * @param column the column number within the specified line
    **/
    public FileLocation(int line, int column) {
        super();
        this._line = line;
        this._col  = column;
    } //-- FileLocation(int, int)
    
    /**
     * Creates a new FileLocation
     * @param filename the name of the file
     * @param line the line number 
     * @param column the column number within the specified line
    **/
    public FileLocation(String filename, int line, int column) {
        super();
        this._filename = filename;
        this._line = line;
        this._col  = column;
    } //-- FileLocation(String, int, int)
    
    /**
     * Returns the column number for this Location
     * @return the column number for this Location
    **/
    public int getColumnNumber() {
        return _col;
    } //-- getColumnNumber
    
    /**
     * Returns the name of the file to which this FileLocation refers
     * @return the name of the file to which this FileLocation refers
    **/
    public String getFilename() {
        return _filename;
    } //-- getFilename
    
    /**
     * Returns the line number for this location
     * @return the line number for the FileLocation
    **/
    public int getLineNumber() {
        return _line;
    } //-- getLineNumber
    
    /**
     * Sets the column number for this Location
     * @param column the column number for this Location
    **/
    public void setColumnNumber(int column) {
        this._col = column;
    } //-- setColumnNumber
    
    /**
     * Sets the name of the file to which this FileLocation refers
     * @param filename the name of the file to which this FileLocation 
     * refers
    **/
    public void setFilename(String filename) {
        this._filename = filename;
    } //-- setFilename
    
    /**
     * Sets the line number for this Location
     * @param line the line number for this Location
    **/
    public void setLineNumber(int line) {
        this._line = line;
    } //-- setLineNumber
    
    /**
     * Returns the String representation of this Location
     * @return the String representation of this Location
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer("file: ");
        
        if (_filename != null)
            sb.append(_filename);
        else
            sb.append(NOT_AVAILABLE);
            
        //-- line number
        sb.append("; line: ");
        if (_line >= 0) sb.append(_line);
        else sb.append(NOT_AVAILABLE);
        
        //-- column
        sb.append("; column: ");
        if (_col >= 0) sb.append(_col);
        else sb.append(NOT_AVAILABLE);
        
        return sb.toString();
    } //-- toString
    
} //-- Location
