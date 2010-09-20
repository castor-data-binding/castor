package org.castor.jpa.scenario.cascading;

import javax.persistence.*;

@Entity
@Table(name = "Cascading_parent")
public class Parent {

    private long id;
    private Child child;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public Child getChild() {
        return child;
    }

    public void setChild(final Child child) {
        this.child = child;
    }

}
