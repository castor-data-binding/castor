package org.castor.jpa.scenario.callbacks;

import javax.persistence.PostPersist;

public class PetListener {

    @PostPersist
    protected void postPersistPetListener() {
        CallbacksExecutionOrderMemory.addCallbackName("postPersistPetListener");
    }

}
