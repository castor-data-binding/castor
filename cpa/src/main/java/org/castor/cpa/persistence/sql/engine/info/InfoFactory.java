package org.castor.cpa.persistence.sql.engine.info;

import java.util.HashMap;
import java.util.Map;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;

public final class InfoFactory {
    private Map<ClassDescriptor, TableInfo> _map = new HashMap<ClassDescriptor, TableInfo>();

    public TableInfo createTableInfo(final ClassDescriptor classDescriptor)
    throws MappingException {
        return new TableInfo(classDescriptor, _map);
    }
    
    public void resolveForeignKeys() {
        for (TableInfo tblInf : _map.values()) {
            tblInf.adjustTableLinks();
        }
    }
}
