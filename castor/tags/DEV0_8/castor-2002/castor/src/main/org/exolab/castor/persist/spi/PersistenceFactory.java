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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.persist.spi;


import java.io.PrintWriter;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;


/**
 * Factory for producing new persistence implementations. Used for
 * constructing a persistence service provider (see {@link Persistence})
 * as well as for constructing new query expressions (see {@link
 * QueryExpression}).
 * <p>
 * The factory is specified in the JDO configuration file for the
 * database and is configured through Bean-like accessor methods.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 * @see Persistence
 */
public interface PersistenceFactory
{


    /**
     * Returns the name of this factory. A descriptive name that
     * indicates the type of supported database server or SQL syntax.
     *
     * @return The name of this factory
     */
    public String getFactoryName();


    /**
     * Returns a persistence implementation for the specified object
     * type (given its descriptor) on behalf of the specified cache
     * engine. Return null if no persistence support is available for
     * the specified object type.
     *
     * @param clsDesc The class descriptor
     * @param logInterceptor Interceptor to write log messages to
     *  (may be null)
     * @return A suiteable persistence implementation, or null
     * @throws MappingException Indicates that the object type is not
     *  supported by the persistence engine due to improper mapping
     */
    public Persistence getPersistence( ClassDescriptor clsDesc, LogInterceptor logInterceptor )
        throws MappingException;


    /**
     * Returns a new empty query expression suitable for the underlying
     * SQL engine. The implementation will construct SQL query
     * statements in the preferred syntax.
     *
     * @return New empty query expression
     */
    public QueryExpression getQueryExpression();


}

