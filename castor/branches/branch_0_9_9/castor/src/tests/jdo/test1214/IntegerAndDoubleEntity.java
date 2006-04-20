package jdo.test1214;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IntegerAndDoubleEntity {
    private static Log LOG = LogFactory.getLog(IntegerAndDoubleEntity.class);
	
    private int id;
    private Integer property;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getProperty() { return property; }
    public void setProperty(Integer property) {
    	LOG.debug ("setProperty(Integer)");
        this.property = property;
    }
    public void setProperty(Double property) {
    	LOG.debug ("setProperty(Double)");
        this.property = new Integer (property.intValue());
    }
}
