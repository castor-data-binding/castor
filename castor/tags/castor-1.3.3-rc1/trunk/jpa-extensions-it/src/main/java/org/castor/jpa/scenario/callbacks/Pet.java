package org.castor.jpa.scenario.callbacks;

import javax.persistence.*;

@Entity
@EntityListeners(PetListener.class)
@Table(name = "EntityListeners_pet")
public class Pet extends Animal {

    private long id;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @PostPersist
    protected void postPersistPet() {
        CallbacksExecutionOrderMemory.addCallbackName("postPersistPet");
    }

}
