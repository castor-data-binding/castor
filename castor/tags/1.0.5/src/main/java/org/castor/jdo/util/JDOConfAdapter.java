package org.castor.jdo.util;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.conf.Param;
import org.castor.jdo.conf.TransactionDemarcation;
import org.castor.jdo.conf.TransactionManager;
import org.castor.transactionmanager.LocalTransactionManagerFactory;

import org.exolab.castor.mapping.MappingException;

public final class JDOConfAdapter {
    //--------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(JDOConfAdapter.class);

    private JdoConf _jdoConf;
    
    //--------------------------------------------------------------------------
    
    public JDOConfAdapter(final JdoConf jdoConf) {
        _jdoConf = jdoConf;
    }

    //--------------------------------------------------------------------------
    
    public String getName() {
        return _jdoConf.getName();
    }
    
    public String getTransactionManager() throws MappingException {
        TransactionDemarcation demarcation = _jdoConf.getTransactionDemarcation();
        if (LocalTransactionManagerFactory.NAME.equals(demarcation.getMode())) {
            return LocalTransactionManagerFactory.NAME;
        } else if (demarcation.getTransactionManager() != null) {
            return demarcation.getTransactionManager().getName();
        } else {
            String msg = "Missing configuration of TransactionManager.";
            LOG.error(msg);
            throw new MappingException(msg);
        }
    }

    public Properties getTransactionManagerParameters() {
        Properties properties = new Properties();
        TransactionDemarcation demarcation = _jdoConf.getTransactionDemarcation();
        TransactionManager manager = demarcation.getTransactionManager();
        if (manager != null) {
            Enumeration parameters = manager.enumerateParam();
            while (parameters.hasMoreElements()) {
                Param param = (Param) parameters.nextElement();
                properties.put(param.getName(), param.getValue());
            }
        }
        return properties;
    }
    
    //--------------------------------------------------------------------------
}
