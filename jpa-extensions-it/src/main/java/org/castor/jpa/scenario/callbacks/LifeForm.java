package org.castor.jpa.scenario.callbacks;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class LifeForm {

    private long id;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

}
