package jdo.test1214;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IntEntity {
    private static final Log LOG = LogFactory.getLog(IntEntity.class);

    private int id;
    private int property;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProperty() { return property; }
    public void setProperty(int id) {
        LOG.debug ("setProperty(int)");
        this.property = id;
    }
}
