package org.castor.jpa.scenario.callbacks;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.PersistenceException;
import javax.persistence.PostPersist;
import javax.persistence.Table;

@Entity
@Table(name = "Callbacks_martian")
@EntityListeners(MartianListener.class)
public class Martian extends LifeForm {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @PostPersist
    protected void validatePersistence() {
        if (this.name.equals("Manfred Mustermann")) {
            throw new PersistenceException(String.format(
                    "Also martians shouldn't be called %s.", this.name));
        }
    }

}
