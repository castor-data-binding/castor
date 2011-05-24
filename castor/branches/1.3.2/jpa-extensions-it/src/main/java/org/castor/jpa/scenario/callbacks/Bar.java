package org.castor.jpa.scenario.callbacks;

import javax.persistence.*;

@Entity
@Table(name = "ExcludeListeners_bar")
@EntityListeners({BarListener.class, BarListener2.class})
public class Bar extends Foo {

    private long id;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @PostPersist
    public void postPersistBar() {
        CallbacksExecutionOrderMemory.addCallbackName("postPersistBar");
    }

}
