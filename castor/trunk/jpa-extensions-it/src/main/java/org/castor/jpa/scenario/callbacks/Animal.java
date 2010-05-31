package org.castor.jpa.scenario.callbacks;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.Table;

@Entity
@Table(name = "EntityListeners_animal")
public class Animal {

    private long id;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @PostPersist
    protected void postPersistAnimal() {
        CallbacksExecutionOrderMemory.addCallbackName("postPersistAnimal");
    }

    @PostPersist
    protected void postPersistAnimal2() {
        CallbacksExecutionOrderMemory.addCallbackName("postPersistAnimal2");
    }

}
