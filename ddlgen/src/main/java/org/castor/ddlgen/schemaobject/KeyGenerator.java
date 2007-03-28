/*
 * Copyright 2006 Le Duc Bao, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.ddlgen.schemaobject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract base class for all key generators.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public abstract class KeyGenerator extends AbstractSchemaObject {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(KeyGenerator.class);
    
    //--------------------------------------------------------------------------

    /** Alias of the key generator. */
    private String _alias;

    /** Table the key generator creates keys for. */
    private Table _table;
    
    //--------------------------------------------------------------------------
    
    /**
     * Construct key generator with given name and alias.
     * 
     * @param name Name of the key generator algorithm.
     * @param alias Alias of the key generator.
     */
    protected KeyGenerator(final String name, final String alias) {
        super();
        
        setName(name);
        setAlias((alias != null) ? alias : name);
    }

    //--------------------------------------------------------------------------

    /**
     * Set alias of the key generator.
     * 
     * @param alias Alias of the key generator.
     */
    public final void setAlias(final String alias) { _alias = alias; }
    
    /**
     * Get alias of the key generator.
     * 
     * @return Alias of the key generator.
     */
    public final String getAlias() { return _alias; }
    
    /**
     * Set table the key generator creates keys for.
     * 
     * @param table Table the key generator creates keys for.
     */
    public final void setTable(final Table table) {
        _table = table;
    }
    
    /**
     * Get table the key generator creates keys for.
     * 
     * @return Table the key generator creates keys for.
     */
    public final Table getTable() {
        return _table;
    }

    //--------------------------------------------------------------------------

    /**
     * Check wether this key generator is compatible with the given one to allow merge
     * of table definitions.
     * 
     * @param keygen Key generator to merge.
     */
    public final void merge(final KeyGenerator keygen) {
        if (keygen == null) {
            String msg = "Merge table has no key generator";
            LOG.warn(msg);
        } else {
            if ((getAlias() == null) || !getAlias().equalsIgnoreCase(keygen.getAlias())) {
                String msg = "Merge table has different key generator: "
                    + getAlias() + " / " + keygen.getAlias();
                LOG.warn(msg);
            }
        }
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other == null) { return false; }
        if (other.getClass() != this.getClass()) { return false; }
        
        KeyGenerator kg = (KeyGenerator) other;
        return equals(getName(), kg.getName())
            && equals(_alias, kg._alias)
            && equals(_table, kg._table);
    }

    /**
     * {@inheritDoc}
     */
    public final int hashCode() {
        int hashCode = 0;
        if (getName() != null) { hashCode += getName().hashCode(); }
        hashCode *= HASHFACTOR;
        if (_alias != null) { hashCode += _alias.hashCode(); }
        hashCode *= HASHFACTOR;
        if (_table != null) { hashCode += _table.hashCode(); }
        return hashCode;
    }

    //--------------------------------------------------------------------------
}
