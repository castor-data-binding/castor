package org.castor.jpa.scenario.callbacks;

import javax.persistence.PersistenceException;
import javax.persistence.PreRemove;

public class MartianListener {

    @PreRemove
    protected void validatePersistence() {
        throw new PersistenceException("Martians can't be deleted.");
    }

}
