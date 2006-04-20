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
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.schema.reader;

import org.exolab.castor.xml.schema.Schema;

import java.util.Hashtable;

/**
 * A class used to save State information for the SchemaUnmarshaller
 * Roughly speaking it keeps track of all the schemas read.
 * This is used to prevent infinite loops when importing or including schema.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */
class SchemaUnmarshallerState {


    private Hashtable _processed = null;

    /**
     * Creates a new SchemaUnmarshallerState
     */
    protected SchemaUnmarshallerState() {
        _processed   = new Hashtable(1);
    } //-- SchemaUnmarshallerState
    /**
     * Marks the given schema as having been processed.
     * @param schemaLocation the key identifying the physical location
     * of the schema to mark.
     * @param schema the Schema to mark as having
     * been processed.
     */
    void markAsProcessed(String schemaLocation, Schema schema) {
        _processed.put(schemaLocation,schema);
    } //-- markAsProcessed

    /**
     * Returns true if the given Schema has been marked as processed
     * @param schema the Schema to check for being marked as processed
     */
    boolean processed(Schema schema) {
        return _processed.contains(schema);
    } //-- processed

    /**
     * Returns true if the given schema location has been marked as processed
     * @param schema location the schema location to check for being marked as processed
     */
    boolean processed(String schemaLocation) {
       return _processed.containsKey(schemaLocation);
    } //-- processed

    /**
     * Returns the schema corresponding to the given schemaLocation
     * @param schemaLocation the schema location of the schema
     */
     Schema getSchema(String schemaLocation) {
         return (Schema)_processed.get(schemaLocation);
     }
}
