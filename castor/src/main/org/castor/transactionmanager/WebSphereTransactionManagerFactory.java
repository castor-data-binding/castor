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
 * An IBM Websphere 4 and prior specific factory for acquiring transactions
 * from this particular J2EE container.
 *
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class WebSphereTransactionManagerFactory
extends AbstractTransactionManagerFactory {
    //--------------------------------------------------------------------------

    /** Name of the IBM Websphere specific transaction manager factory class. */
    public static final String FACTORY_CLASS_NAME =
        "com.ibm.ejs.jts.jta.JTSXA";
    
    /** Name of the method that is used upon the factory to have a TransactionManager
     *  instance created. */
    public static final String FACTORY_METHOD_NAME = "getTransactionManager";
    
    /** The name of the factory. */
    public static final String NAME = "websphere";

    //--------------------------------------------------------------------------

    /**
     * @see org.exolab.castor.jdo.transactionmanager.spi.AbstractTransactionManagerFactory
     *      #getFactoryClassName()
     */
    public String getFactoryClassName() { return FACTORY_CLASS_NAME; }
    
    /**
     * @see org.exolab.castor.jdo.transactionmanager.spi.AbstractTransactionManagerFactory
     *      #getFactoryMethodName()
     */
    public String getFactoryMethodName() { return FACTORY_METHOD_NAME; }
    
    /**
     * @see org.exolab.castor.jdo.transactionmanager.TransactionManagerFactory#getName()
     */
    public String getName() { return NAME; }

    //--------------------------------------------------------------------------
}
