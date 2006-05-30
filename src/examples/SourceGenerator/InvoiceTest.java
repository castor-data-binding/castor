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


import java.io.*;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.util.ClassDescriptorResolverImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//-- import for generated classes
import test.*;
import test.business.*;

import org.exolab.castor.types.Duration;
import org.exolab.castor.types.Date;
import org.exolab.castor.types.Time;

/**
 * Test class for Castor test suite
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
**/
public class InvoiceTest implements PropertyChangeListener {


    public void propertyChange(PropertyChangeEvent event) {
        System.out.println("PropertyChange: " + event.getPropertyName());
    } //-- propertyChange

    public static void main(String[] args) {
	try {


	    System.out.println("Unmarshalling Invoice");

	    MyInvoice invoice = null;
	    invoice = MyInvoice.unmarshal(new FileReader("invoice1.xml"));

	    System.out.println();
	    System.out.println("unmarshalled...performing tests...");
	    System.out.println();

        //-- Display unmarshalled address to the screen
        System.out.println("Invoice");
        System.out.println("-------");
        System.out.println();
        System.out.println("Ship To:");

        ShipTo shipTo = invoice.getShipTo();

        System.out.println("   " + shipTo.getCustomer().getName());

        AddressElement address = shipTo.getCustomer().getAddressElement();

	    System.out.println("   " + address.getStreet1());
	    String street2 = address.getSecondStreet();
	    if (street2 != null)
	        System.out.println("   " + street2);
	    System.out.print("   " + address.getCity());
	    System.out.print(", " + address.getState());
	    System.out.println("  " + address.getZipCode());

        System.out.println("   "+shipTo.getCustomer().getPhone());

	    System.out.println();
        System.out.println("Item:");
        Item[] item = invoice.getItem();
        for (int i=0; i<item.length; i++) {
                String result = item[i].getInStock()? "yes":"no";
                System.out.println("  In Stock: "+result);
                System.out.println("  Category:"+item[i].getCategory());
                System.out.println("  ID:"+item[i].getId());
                System.out.println("  Quantity:"+item[i].getQuantity());
                System.out.println("  Price:"+item[i].getSpecialPrice());
        }
        System.out.println();
	    System.out.println("Shipping Method:");

	    ShippingMethod method = invoice.getShippingMethod();
	    System.out.print("   " + method.getCarrier());
	    System.out.println("  " + method.getOption());
	    System.out.print("   Estimated Time: ");

	    Duration duration = method.getEstimatedDelivery();

	    int years  = duration.getYear();
	    int months = duration.getMonth();
	    int days   = duration.getDay();
	    int hours  = duration.getHour();

	    boolean printComma = false;

	    if (years > 0) {
	        System.out.print(years + " year(s)");
	        printComma = true;
	    }

	    if (months > 0) {
	        if (printComma) System.out.print(", ");
	        System.out.print(months + " month(s)");
	        printComma = true;
	    }

	    if (days > 0) {
	        if (printComma) System.out.print(", ");
	        System.out.print(days + " day(s)");
	        printComma = true;
	    }

	    if (hours > 0) {
	        if (printComma) System.out.print(", ");
	        System.out.print(hours + " hour(s)");
	    }

        System.out.println();

        System.out.println();
	    System.out.println("Shipping Date:");
        ShippingDate date = invoice.getShippingDate();
        Date day = date.getDate();
        Time time = date.getTime();

        System.out.println("   Date :"+day.toString());
        System.out.println("   Time :"+time.toString());
	    System.out.println();

        System.out.println("----End of Invoice----");
        invoice.marshal(new FileWriter("invoice2.xml"));

    } catch (Exception e) {
	    e.printStackTrace();
	}

    }

}