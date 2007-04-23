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

package dtx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.dtx.*;

public class Test {

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(Test.class);
    
    static String DATABASE = "database.xml";
    static String SCHEMA = "product.xsd";
    static int MAX_ID = 10;

    public static void main(String[] argv) {

	Test test = new Test();
	test.run();
    }

    public Test() {
    }

    public void run() {

	try {
	    DTXEngine engine = new DTXEngine();

	    String schemaURL = getClass().getResource(SCHEMA).toString();
	    LOG.info("Schema URL: " + schemaURL);
	    engine.setSchema(schemaURL);
	    String dbURL = getClass().getResource(DATABASE).toString();
        LOG.info("DB URL: " + dbURL);
	    engine.setDatabase(dbURL);

        LOG.info("Setting document handler...");

	    engine.setDocumentHandler(new DemoHandler());

        LOG.info("Preparing query...");

	    DTXQuery qry = engine.prepareQuery("SELECT p FROM myapp.Product p " +
					       "WHERE p.id = $1 ");

        LOG.info("Beginning bind-and-execute loop.");

	    for (int i = 0; i < MAX_ID; i++) {
            LOG.info("Binding id " + i);
            qry.bind(1, i);
            LOG.info("Executing for id " + i);
            qry.execute();
            LOG.info("Done executing " + i);
	    }

	    DTXQuery multiQuery = engine.prepareQuery("SELECT p FROM myapp.Product p " +
					       "WHERE p.id <= $1 AND p.id >= $2 ");

	    multiQuery.bind(1, 6);
	    multiQuery.bind(2, 4);

	    multiQuery.execute();

	} catch (DTXException dtxe) {
	    dtxe.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
    
