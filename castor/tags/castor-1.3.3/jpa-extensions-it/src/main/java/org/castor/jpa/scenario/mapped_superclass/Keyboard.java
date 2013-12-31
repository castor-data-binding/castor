package org.castor.jpa.scenario.mapped_superclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MappedSuperclass_keyboard")
public class Keyboard extends Hardware {

    protected int numberOfKeys;

    @Column
    public int getNumberOfKeys() {
        return numberOfKeys;
    }

    public void setNumberOfKeys(int numberOfKeys) {
        this.numberOfKeys = numberOfKeys;
    }

}
