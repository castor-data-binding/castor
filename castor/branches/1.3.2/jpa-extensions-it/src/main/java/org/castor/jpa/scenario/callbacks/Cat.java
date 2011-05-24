package org.castor.jpa.scenario.callbacks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.PersistenceException;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "Callbacks_cat")
public class Cat {

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

    @PrePersist
    protected void validateCreation() throws PersistenceException {
        if (this.name.equals("Waldi")) {
            LOG.debug("Hello from a cat.");
            throw new PersistenceException(String.format(
                    "Cat mustn't be called %s.", this.name));
        }
    }

}
