package jdo.test1214;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IntAndDoubleEntity {
    private static Log LOG = LogFactory.getLog(IntAndDoubleEntity.class);
	
    private int id;
    private Integer property;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getProperty() { return property; }
    public void setProperty(Double property) {
    	LOG.debug ("setProperty(Double)");
        this.property = new Integer (property.intValue());
    }
    public void setProperty(int property) {
    	LOG.debug ("setProperty(int)");
        this.property = new Integer (property);
    }
}
