package org.castor.jpa.scenario.callbacks;

import javax.persistence.PostPersist;

public class FooListener {

    @PostPersist
    public void postPersistFooListener() {
        CallbacksExecutionOrderMemory.addCallbackName("postPersistFooListener");
    }

}
