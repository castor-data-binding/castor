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

package org.exolab.castor.xml.schema;

/**
 * An XML Schema model group Order
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
**/
public final class Order implements java.io.Serializable {

    public static final short ALL      = 0;
    public static final short SEQUENCE = 1;
    public static final short CHOICE   = 2;

    public static final Order all    = new Order(Order.ALL);
    public static final Order choice = new Order(Order.CHOICE);
    public static final Order seq    = new Order(Order.SEQUENCE);

    private short type = ALL;

    /**
     * Creates a new Order with the given Type;
    **/
    private Order(short type) {
        this.type = type;
    } //-- Order

    public short getType() {
        return this.type;
    } //-- getType

    public String toString() {
        switch(type) {
            case CHOICE:
                return "choice";
            case SEQUENCE:
                return "sequence";
            default:
                return "all";
        }
    }

    public static Order valueOf(String value)
        throws IllegalArgumentException
    {
        if ("all".equals(value)) {
            return Order.all;
        }
        else if ("choice".equals(value)) {
            return Order.choice;
        }
        else if ("sequence".equals(value)) {
            return Order.seq;
        }
        else {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            sb.append(" is not a valid model group order.");
            throw new IllegalArgumentException(sb.toString());
        }
    } //-- valueOf

} //-- Order
