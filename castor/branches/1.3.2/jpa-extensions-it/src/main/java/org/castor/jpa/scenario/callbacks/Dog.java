package org.castor.jpa.scenario.callbacks;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@EntityListeners({DogListener.class, DogListener2.class})
@Table(name = "EntityListeners_dog")
public class Dog extends Pet {

    private long id;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

}
