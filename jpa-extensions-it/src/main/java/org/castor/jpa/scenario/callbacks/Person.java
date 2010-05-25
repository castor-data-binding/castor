package org.castor.jpa.scenario.callbacks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.*;

@Entity
@Table(name = "Callbacks_person")
public class Person {

    private static final Log LOG = LogFactory.getLog(Person.class);

    private long id;
    private String name;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @PostLoad
    protected void testPostLoadCallbackHooking() {
        LOG.debug(String.format("Hello from `PostLoad`. My name is %s.",
                this.name));
    }

    @PrePersist
    protected void validateCreation() {
        if (this.name.equals("Max Mustermann")) {
            LOG.debug("Hello from a person.");
            throw new PersistenceException(String.format(
                    "Person mustn't be called %s.", this.name));
        }
    }

    @PostPersist
    protected void validatePersistence() {
        if (this.name.equals("Manfred Mustermann")) {
            throw new PersistenceException(String.format(
                    "Person shouldn't be called %s either.", this.name));
        }
    }

    @PreRemove
    protected void validateRemoval() {
        if (this.name.equals("Max Musterfrau")) {
            throw new PersistenceException(this.name + " mustn't be removed.");
        }
    }

    @PostRemove
    protected void validateDeletion() {
        if (this.name.equals("Manfred Musterfrau")) {
            throw new PersistenceException(this.name
                    + " shouldn't be removed either.");
        }
    }

    @PreUpdate
    protected void validateModification() {
        if (this.name.equals("Max Musterfrau")) {
            throw new PersistenceException(String.format(
                    "Person mustn't be renamed to %s.", this.name));
        }
    }

    @PostUpdate
    protected void validateUpdating() {
        if (this.name.equals("Hans Wurst")) {
            throw new PersistenceException(String.format(
                    "Person shouldn't be renamed to %s either.", this.name));
        }
    }

}
