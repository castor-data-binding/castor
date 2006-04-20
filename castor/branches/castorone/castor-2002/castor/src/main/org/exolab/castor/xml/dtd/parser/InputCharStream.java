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
 * Copyright 2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.dtd.parser;

/**
 * An implementation of interface
 * {@link org.exolab.castor.xml.dtd.parser.CharStream CharStream}.
 * Implements input character stream
 * that maintains line and column number positions of the characters.
 * It also has the capability to backup the stream to some extent.<br>
 * The object of this class is constructed using
 * {@link java.io.Reader java.io.Reader} <tt>reader</tt> and it is left to
 * constructor of the <tt>reader</tt> to set up character encoding correctly.
 * This means that method <u><font color="blue">read</font></u> of
 * the <tt>reader</tt> is used to get next characters, assuming it returns
 * appropriate values. It is recommended to use class
 * {@link java.io.InputStreamReader java.io.InputStreamReader}
 * as a <tt>reader</tt>, which allows to set desired character encoding.
 * This class is an intermediate component between input
 * character reader and the parser.<br>
 * This class is based on the class <b>ASCII_CharStream</b> - implementation
 * of interface
 * {@link org.exolab.castor.xml.dtd.parser.CharStream CharStream},
 * that JavaCC would have generated with
 * the following options set in a JavaCC grammar file: <pre>
 *    JAVA_UNICODE_ESCAPE = false;
 *    UNICODE_INPUT = false;
 *    USER_CHAR_STREAM = false; </pre>
 * Note that this class is not fully JavaCC generated.<br>
 * @author <b>JavaCC</b>, <a href="mailto:totok@intalio.com">Alexander Totok</a>
 * @version $Revision$ $Date$
 */
public final class InputCharStream implements CharStream {

   public static final boolean staticFlag = false;
   int bufsize;
   int available;
   int tokenBegin;
   public int bufpos = -1;
   private int bufline[];
   private int bufcolumn[];

   private int column = 0;
   private int line = 1;

   private boolean prevCharIsCR = false;
   private boolean prevCharIsLF = false;

   private java.io.Reader inputStream;

   private char[] buffer;
   private int maxNextCharInd = 0;
   private int inBuf = 0;

   private final void ExpandBuff(boolean wrapAround) {

      char[] newbuffer = new char[bufsize + 2048];
      int newbufline[] = new int[bufsize + 2048];
      int newbufcolumn[] = new int[bufsize + 2048];

      try {
         if (wrapAround) {
            System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
            System.arraycopy(buffer, 0, newbuffer, bufsize - tokenBegin, bufpos);
            buffer = newbuffer;

            System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
            System.arraycopy(bufline, 0, newbufline, bufsize - tokenBegin, bufpos);
            bufline = newbufline;

            System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
            System.arraycopy(bufcolumn, 0, newbufcolumn, bufsize - tokenBegin, bufpos);
            bufcolumn = newbufcolumn;

            maxNextCharInd = (bufpos += (bufsize - tokenBegin));

         } else {

            System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
            buffer = newbuffer;

            System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
            bufline = newbufline;

            System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
            bufcolumn = newbufcolumn;

            maxNextCharInd = (bufpos -= tokenBegin);
         }
      }
      catch (Throwable t) {
         throw new Error(t.getMessage());
      }

      bufsize += 2048;
      available = bufsize;
      tokenBegin = 0;
   }

   private final void FillBuff() throws java.io.IOException {

      if (maxNextCharInd == available) {
         if (available == bufsize) {
            if (tokenBegin > 2048) {
               bufpos = maxNextCharInd = 0;
               available = tokenBegin;
            } else if (tokenBegin < 0) bufpos = maxNextCharInd = 0;
            else ExpandBuff(false);
         } else if (available > tokenBegin) available = bufsize;
         else if ((tokenBegin - available) < 2048) ExpandBuff(true);
         else available = tokenBegin;
      }

      int i;

      try {
         if ((i = inputStream.read(buffer, maxNextCharInd,
                                   available - maxNextCharInd)) == -1) {
            inputStream.close();
            throw new java.io.IOException();
         } else maxNextCharInd += i;
         return;
      }
      catch(java.io.IOException e) {
         --bufpos;
         backup(0);
         if (tokenBegin == -1) tokenBegin = bufpos;
         throw e;
      }
   }

   public final char BeginToken() throws java.io.IOException {

      tokenBegin = -1;
      char c = readChar();
      tokenBegin = bufpos;
      return c;
   }

   private final void UpdateLineColumn(char c) {

      column++;
      if (prevCharIsLF) {
         prevCharIsLF = false;
         line += (column = 1);
      } else if (prevCharIsCR) {
         prevCharIsCR = false;
         if (c == '\n') prevCharIsLF = true;
         else line += (column = 1);
      }

      switch (c) {
         case '\r' :
            prevCharIsCR = true;
            break;
         case '\n' :
            prevCharIsLF = true;
            break;
         case '\t' :
            column--;
            column += (8 - (column & 07));
            break;
         default :
            break;
      }

      bufline[bufpos] = line;
      bufcolumn[bufpos] = column;
   }

   /**
    * Returns the next character from the input stream. The only method whose
    * implementation is different from its original in the ASCII_CharStream class.
    */
   public final char readChar() throws java.io.IOException {

      if (inBuf > 0) {
         --inBuf;

         //----------------------------------------------------------------------
         // the next line was changed, the original line generated by JavaCC was:
         // return (char)((char)0xff & buffer[(bufpos == bufsize - 1) ? (bufpos = 0) : ++bufpos]);
         //----------------------------------------------------------------------
         return buffer[(bufpos == bufsize - 1) ? (bufpos = 0) : ++bufpos];
      }
      if (++bufpos >= maxNextCharInd) FillBuff();

      //----------------------------------------------------------------------
      // the next line was changed, the original line generated by JavaCC was:
      // char c = (char)((char)0xff & buffer[bufpos]);
      //----------------------------------------------------------------------
      char c = buffer[bufpos];

      UpdateLineColumn(c);
      return (c);
    }

   /**
    * @deprecated
    * @see #getEndColumn
    */
   public final int getColumn() {
      return bufcolumn[bufpos];
   }

   /**
    * @deprecated
    * @see #getEndLine
    */
   public final int getLine() {
      return bufline[bufpos];
   }

   public final int getEndColumn() {
      return bufcolumn[bufpos];
   }

   public final int getEndLine() {
      return bufline[bufpos];
   }

   public final int getBeginColumn() {
      return bufcolumn[tokenBegin];
   }

   public final int getBeginLine() {
      return bufline[tokenBegin];
   }

   public final void backup(int amount) {
      inBuf += amount;
      if ((bufpos -= amount) < 0) bufpos += bufsize;
   }

   /**
    * Constructor, allowing to specify start line and start column of the char
    * stream, and buffer size as well.
    */
   public InputCharStream(java.io.Reader dstream, int startline,
                           int startcolumn, int buffersize) {
      inputStream = dstream;
      line = startline;
      column = startcolumn - 1;
      available = bufsize = buffersize;
      buffer = new char[buffersize];
      bufline = new int[buffersize];
      bufcolumn = new int[buffersize];
   }

   /**
    * Constructor, allowing to specify start line and start column
    * of the char stream.
    */
   public InputCharStream(java.io.Reader dstream, int startline,
                                                   int startcolumn) {
      this(dstream, startline, startcolumn, 4096);
   }

   /**
    * Constructor, instantiating the char stream to begin at 1-st line and
    * 1-st column of <tt>dstream</tt>.
    */
   public InputCharStream(java.io.Reader dstream) {
      this(dstream, 1, 1);
   }

   /**
    * Reinitialization of the char stream, allowing to specify start line
    * and start column of the char stream, and buffer size as well.
    */
   public void ReInit(java.io.Reader dstream, int startline,
                             int startcolumn, int buffersize) {
      inputStream = dstream;
      line = startline;
      column = startcolumn - 1;

      if (buffer == null || buffersize != buffer.length) {
         available = bufsize = buffersize;
         buffer = new char[buffersize];
         bufline = new int[buffersize];
         bufcolumn = new int[buffersize];
      }
      prevCharIsLF = prevCharIsCR = false;
      tokenBegin = inBuf = maxNextCharInd = 0;
      bufpos = -1;
   }

   /**
    * Reinitialization of the char stream, allowing to specify start line
    * and start column of the char stream.
    */
   public void ReInit(java.io.Reader dstream, int startline,
                                              int startcolumn) {
      ReInit(dstream, startline, startcolumn, 4096);
   }

   /**
    * Reinitialization of the char stream, instantiating the char stream
    * to begin at 1-st line and 1-st column of <tt>dstream</tt>.
    */
   public void ReInit(java.io.Reader dstream) {
      ReInit(dstream, 1, 1);
   }

   public final String GetImage() {
      if (bufpos >= tokenBegin)
         return new String(buffer, tokenBegin, bufpos - tokenBegin + 1);
      else
         return new String(buffer, tokenBegin, bufsize - tokenBegin) +
                                   new String(buffer, 0, bufpos + 1);
   }

   public final char[] GetSuffix(int len) {
      char[] ret = new char[len];

      if ((bufpos + 1) >= len)
         System.arraycopy(buffer, bufpos - len + 1, ret, 0, len);
      else {
         System.arraycopy(buffer, bufsize - (len - bufpos - 1), ret, 0,
                                  len - bufpos - 1);
         System.arraycopy(buffer, 0, ret, len - bufpos - 1, bufpos + 1);
      }
      return ret;
   }

   public void Done() {
      buffer = null;
      bufline = null;
      bufcolumn = null;
   }

   /**
    * Method to adjust line and column numbers for the start of a token.
    */
   public void adjustBeginLineColumn(int newLine, int newCol) {
      int start = tokenBegin;
      int len;

      if (bufpos >= tokenBegin) {
         len = bufpos - tokenBegin + inBuf + 1;
      } else {
         len = bufsize - tokenBegin + bufpos + 1 + inBuf;
      }

      int i = 0, j = 0, k = 0;
      int nextColDiff = 0, columnDiff = 0;

      while (i < len &&
             bufline[j = start % bufsize] == bufline[k = ++start % bufsize]) {
         bufline[j] = newLine;
         nextColDiff = columnDiff + bufcolumn[k] - bufcolumn[j];
         bufcolumn[j] = newCol + columnDiff;
         columnDiff = nextColDiff;
         i++;
      }

      if (i < len) {
         bufline[j] = newLine++;
         bufcolumn[j] = newCol + columnDiff;

         while (i++ < len) {
            if (bufline[j = start % bufsize] != bufline[++start % bufsize])
               bufline[j] = newLine++;
            else
               bufline[j] = newLine;
         }
      }

      line = bufline[j];
      column = bufcolumn[j];
   }
}
