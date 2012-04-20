package org.castor.jpa.scenario.callbacks;

import javax.persistence.PostPersist;

public class DogListener2 {

    @PostPersist
    protected void postPersistDogListener2() {
        CallbacksExecutionOrderMemory.addCallbackName(
                "postPersistDogListener2");
    }

}
