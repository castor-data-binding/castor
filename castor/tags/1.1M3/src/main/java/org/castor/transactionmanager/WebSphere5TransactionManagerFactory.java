/*
 * Copyright 2005 Bruce Snyder, Werner Guttmann, Ralf Joachim
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
package org.castor.transactionmanager;

/**
 * An IBM Websphere 5 specific factory for acquiring transactions from 
 * this J2EE container.
 *
 * @author <a href="mailto:ferret DOT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-13 10:49:49 -0600 (Thu, 13 Apr 2006) $
 * @since 1.0
 */
public final class WebSphere5TransactionManagerFactory
extends AbstractTransactionManagerFactory {
    //--------------------------------------------------------------------------

    /** Name of the IBM Websphere specific transaction manager factory class. */
    public static final String FACTORY_CLASS_NAME =
        "com.ibm.ejs.jts.jta.TransactionManagerFactory";
    
    /** Name of the method that is used upon the factory to have a TransactionManager
     *  instance created. */
    public static final String FACTORY_METHOD_NAME = "getTransactionManager";
    
    /** The name of the factory. */
    public static final String NAME = "websphere5";

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * @see org.castor.transactionmanager.AbstractTransactionManagerFactory#getFactoryClassName()
     */
    public String getFactoryClassName() { return FACTORY_CLASS_NAME; }
    
    /**
     * {@inheritDoc}
     * @see org.castor.transactionmanager.AbstractTransactionManagerFactory#getFactoryMethodName()
     */
    public String getFactoryMethodName() { return FACTORY_METHOD_NAME; }
    
    /**
     * {@inheritDoc}
     * @see org.castor.transactionmanager.TransactionManagerFactory#getName()
     */
    public String getName() { return NAME; }

    //--------------------------------------------------------------------------
}
