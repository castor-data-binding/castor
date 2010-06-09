package org.castor.jpa.scenario.callbacks;

import javax.persistence.PostPersist;

public class BarListener {

    @PostPersist
    public void postPersistBarListener() {
        CallbacksExecutionOrderMemory.addCallbackName("postPersistBarListener");
    }

}
