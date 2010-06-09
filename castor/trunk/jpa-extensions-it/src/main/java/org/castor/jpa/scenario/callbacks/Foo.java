package org.castor.jpa.scenario.callbacks;

import javax.persistence.*;

@Entity
@Table(name = "ExcludeListeners_foo")
@EntityListeners(FooListener.class)
@ExcludeDefaultListeners
@ExcludeSuperclassListeners
public class Foo {

    private long id;

    @Id
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @PostPersist
    public void postPersistFoo() {
        CallbacksExecutionOrderMemory.addCallbackName("postPersistFoo");
    }

}
