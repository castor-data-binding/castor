package org.castor.jpa.scenario.callbacks;

import javax.persistence.PostPersist;

public class DogListener {

    @PostPersist
    protected void postPersistDogListener() {
        CallbacksExecutionOrderMemory.addCallbackName("postPersistDogListener");
    }

}
