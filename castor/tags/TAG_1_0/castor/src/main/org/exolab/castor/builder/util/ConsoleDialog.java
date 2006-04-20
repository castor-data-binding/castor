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
 * Copyright 2000-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder.util;

/**
 * A simple utility class to handle command line dialogs
 *
 * @author <a href="mailto:nsgreen@thazar.com">Nathan Green</a>
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
**/
public class ConsoleDialog implements Dialog {
    
    
    /**
     * Creates a new Console Dialog
    **/
    public ConsoleDialog () {
        super();
    } //-- ConsoleDialog
    
    /**
     * Presents a confirmation prompt with the given message.
     *
     * @param message, the confirmation prompt message to display
     * @return true if the user has selected a positive confirmation,
     * otherwise false
    **/
    public boolean confirm(String message) {

        try {
            while (true) {
                System.out.println();
                System.out.print(message);
                System.out.print( "(y|n|?) : ");

                int ch = getChar();

                System.out.println();

                //-- check ch
                switch (ch) {
                    case 'y':
                        return true;
                    case 'n':
                        return false;
                    case '?':
                        System.out.println("y = yes, n = no");
                        break;
                    default:
                        System.out.print("invalid input, expecting ");
                        System.out.println("'y', 'n', or '?'.");
                        break;
                }
            }
        }
        catch (java.io.IOException ix) {
            System.out.println(ix);
        }
        return false;
    } //-- confirm
    
    /**
     * Returns a single char from System.in.
     * @return the character entered, or null if more than one was
     * entered (not including EOLs)
    **/
    private int getChar()
        throws java.io.IOException
    {
        int ch = System.in.read();

        //-- read eoln, or extra characters
        while (System.in.available() > 0) {
            switch (System.in.read()) {
                case '\n':
                case '\r':
                    break;
                default:
                    ch = '\0';
            }
        }
        return ch;
    }
    
    /**
     * Presents a confirmation prompt for values with the
     * given messge.
     *
     * @param message the confirmation prompt to display
     * @param values a list of valid characters to accept
     * @return whatever character the user presses
    **/
    public char confirm(String message, String values)
    {
        return confirm(message, values, "no help available...");
    }
    
    /**
     * Presents a confirmation prompt for values with the
     * given messge
     * @param message the confirmation prompt to display
     * @param values a list of valid characters to accept
     * @param help a help message when the user presses '?'
     * @return whatever character the user presses
    **/
    public char confirm(String message, String values, String help)
    {
        String prompt = makeList(values);

        try {
            while (true) {
                System.out.println();
                System.out.print(message + prompt);

                int ch = getChar();

                System.out.println();

                //-- check ch
                if (values.indexOf(ch) != -1)
                    return (char)ch;
                if (ch == (int)'?')
                    System.out.println(help);
                else {
                    System.out.print("invalid input, expecting ");
                    System.out.println(listInput(values));
                }
            }
        }
        catch (java.io.IOException ix) {
            System.out.println(ix);
        }
        return '\0';
    }
    
    /**
     * Displays the given message to the user. No input is returned from
     * the user.
     *
     * @param message the message to display to the user
     */
    public void notify(String message) {
        System.out.println(message);
    } //-- notify
    
    /**
     * Converts a list of characters into a delimited prompt.  A '?'
     * is automatically put at the end.
     * @param values a list of valid characters to accept
     * @return each character separated by a pipe and in parenthesis
    **/
    private String makeList(String values)
    {
        StringBuffer sb = new StringBuffer(values.length()*2);
        sb.append('(');
        for (int i=0; i<values.length(); i++)
            sb.append(values.charAt(i)).append('|');
        sb.append("?)");
        return sb.toString();
    }
    
    /**
     * Creates a list of valid input options to give a better
     * explanation to the user.
     * @param values a list of valid characters to accept
     * @return each character in single quotes, comma separated
    **/
    private String listInput(String values)
    {
        StringBuffer sb = new StringBuffer(values.length()*4);
        for (int i=0; i<values.length(); i++)
            sb.append('\'')
              .append(values.charAt(i))
              .append("', ");
        sb.append("or '?'");
        return sb.toString();
    }

} //-- Dialog
